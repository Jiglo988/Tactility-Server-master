package org.hyperion.rs2.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.hyperion.Server;
import org.hyperion.engine.EngineTask;
import org.hyperion.rs2.LoginResponse;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.PlayerDetails;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.util.IoBufferUtils;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.util.Misc;

import java.security.SecureRandom;

/**
 * Login protocol decoding class.
 *
 * @author Graham Edgecombe
 */
public class RS2LoginDecoder extends CumulativeProtocolDecoder {

    public static final int STATE_OPCODE = 0;
    public static final int STATE_LOGIN = 1;
    public static final int STATE_PRECRYPTED = 2;
    public static final int STATE_CRYPTED = 3;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final byte[] INITIAL_RESPONSE = new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};

    private int state = STATE_OPCODE, size, encryptedSize;
    private long serverKey;

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        switch (state) {
            case STATE_OPCODE:
                if (in.remaining() < 2) {
                    session.close(true);
                    return false;
                }
                if ((in.get() & 0xFF) != 14) {
                    session.close(true);
                    return false;
                }
                state = STATE_LOGIN;
                return true;

            case STATE_LOGIN:
                if (in.remaining() < 1) {
                    state = STATE_OPCODE;
                    session.close(true);
                    return false;
                }
                @SuppressWarnings("unused")
                int nameHash = in.get() & 0xFF;

                serverKey = RANDOM.nextLong();
                session.write(new PacketBuilder().put(INITIAL_RESPONSE).put((byte)0).putLong(serverKey).toPacket());
                state = STATE_PRECRYPTED;
                return true;

            case STATE_PRECRYPTED:
                if (in.remaining() < 2) {
                    state = STATE_OPCODE;
                    session.close(true);
                    return false;
                }

                int loginOpcode = in.get() & 0xFF;
                if (loginOpcode != 16 && loginOpcode != 18) {
                    state = STATE_OPCODE;
                    session.close(true);
                    return false;
                }

                int loginSize = in.get() & 0xFF;
                if (in.remaining() < loginSize) {
                    state = STATE_OPCODE;
                    session.close(true);
                    return false;
                }

                int loginEncryptSize = loginSize - 40;
                if (loginEncryptSize <= 0) {
                    state = STATE_OPCODE;
                    session.close(true);
                    return false;
                }
                state = STATE_CRYPTED;
                size = loginSize;
                encryptedSize = loginEncryptSize;
                return true;
            case STATE_CRYPTED:
                if (in.remaining() < size) {
                    state = STATE_OPCODE;
                    session.close(true);
                    return true;
                }
                String remoteIp = session.getRemoteAddress().toString();

                int returnCode = 0;
                int magicId = in.get() & 0xFF;
                if (magicId != 122) {
                    returnCode = 6;
                }

                @SuppressWarnings("unused")
                int version = in.getShort() & 0xFFFF;

                @SuppressWarnings("unused")
                boolean lowMemoryVersion = false;
                if (in.get() != 5) {
                    returnCode = 6;
                }

                for (int i = 0; i < 9; i++) {
                    in.getInt();
                }

                int reportedSize = in.get() & 0xFF;
                if (reportedSize != --encryptedSize) {
                    state = STATE_OPCODE;
                    session.close(false);
                    in.rewind();
                    return false;
                }

                int blockOpcode = in.get() & 0xFF;
                if (blockOpcode != 10) {
                    returnCode = 6;
                }

                long clientKey = in.getLong();

                long reportedServerKey = in.getLong();
                if (reportedServerKey != serverKey) {
                    returnCode = 6;
                }

                int uid = in.getInt();
                int macId = in.getInt();
                if (macId == 0) {
                    macId = Misc.random(Integer.MAX_VALUE);
                }

                final int[] specialUid = new int[20];
                for (int i = 0; i < specialUid.length; i++)
                    specialUid[i] = in.getInt();

                String name = NameUtils.formatName(IoBufferUtils.getRS2String(in)).trim();
                String pass = IoBufferUtils.getRS2String(in);
                int authenticationCode = in.getInt();
                boolean saveIp = in.getInt() == 1;

                int[] sessionKey = new int[4];
                sessionKey[0] = (int) (clientKey >> 32);
                sessionKey[1] = (int) clientKey;
                sessionKey[2] = (int) (serverKey >> 32);
                sessionKey[3] = (int) serverKey;

                ISAACCipher inCipher = new ISAACCipher(sessionKey);
                for (int i = 0; i < 4; i++) {
                    sessionKey[i] += 50;
                }
                ISAACCipher outCipher = new ISAACCipher(sessionKey);

                session.getFilterChain().remove("protocol");
                session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(RS2CodecFactory.GAME));

                if (returnCode != 0) {
                    session.write(new PacketBuilder().put((byte) returnCode).toPacket()).addListener(future -> {
                        state = STATE_OPCODE;
                        future.getSession().close(false);
                    });
                    return false;
                }
                state = STATE_OPCODE;
                login(new PlayerDetails(session, Misc.formatPlayerName(name), pass, authenticationCode, macId, uid, inCipher, outCipher, remoteIp, specialUid, saveIp));
                return true;
        }
        in.rewind();
        return false;
    }

    public void login(final PlayerDetails playerDetails) {
        Player player = new Player(playerDetails);

        Server.getLoader().getEngine().submitLogin(new EngineTask("Player logging in for " + playerDetails.getName(), false) {
            @Override
            public Boolean call() throws Exception {
                LoginResponse loginResponse = World.getLoader().checkLogin(player, playerDetails);

                if(loginResponse == LoginResponse.NEW_PLAYER) {
                    player.setNew(true);
                    player.setCreatedTime(System.currentTimeMillis());
                    loginResponse = LoginResponse.SUCCESSFUL_LOGIN;
                }

                if(loginResponse != LoginResponse.SUCCESSFUL_LOGIN) {
                    playerDetails.getSession().write(new PacketBuilder().put((byte)loginResponse.getReturnCode()).toPacket()).addListener(future -> future.getSession().close(true));
                    return true;
                }
                player.getSession().setAttribute("player", player);

                if (!World.getLoginQueue().contains(player)) {
                    World.getLoginQueue().add(player);
                }
                return true;
            }

            @Override
            public void stopTask() {
                if (!World.getLoginQueue().contains(player)) {
                    World.getLoginQueue().remove(player);
                }
                if(playerDetails.getSession().isConnected())
                    playerDetails.getSession().write(new PacketBuilder().put((byte)LoginResponse.WAIT_AND_TRY_AGAIN.getReturnCode()).toPacket()).addListener(future -> future.getSession().close(true));
            }
        });
    }
}