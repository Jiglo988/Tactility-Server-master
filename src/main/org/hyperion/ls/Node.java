package org.hyperion.ls;

import org.apache.mina.core.session.IoSession;
import org.hyperion.util.login.LoginPacket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages a single node (world).
 *
 * @author Graham Edgecombe
 */
public class Node {

	/**
	 * The login server.
	 */
	private LoginServer server;

	/**
	 * The session.
	 */
	private IoSession session;

	/**
	 * The id.
	 */
	private int id;

	/**
	 * A map of players.
	 */
	private Map<String, PlayerData> players = new HashMap<String, PlayerData>();

	/**
	 * Creates a node.
	 *
	 * @param server  The server.
	 * @param session The session.
	 * @param id      The id.
	 */
	public Node(LoginServer server, IoSession session, int id) {
		this.server = server;
		this.session = session;
		this.id = id;
	}

	/**
	 * Registers a new player.
	 *
	 * @param player The player to add.
	 */
	public void register(PlayerData player) {
		players.put(player.getName(), player);
	}

	/**
	 * Removes an old player.
	 *
	 * @param player The player to remove.
	 */
	public void unregister(PlayerData player) {
		players.remove(player.getName());
	}

	/**
	 * Gets a player by their name.
	 *
	 * @param name The player name.
	 * @return The player.
	 */
	public PlayerData getPlayer(String name) {
		return players.get(name);
	}

	/**
	 * Gets the players in this node.
	 *
	 * @return The players in this node.
	 */
	public Collection<PlayerData> getPlayers() {
		return players.values();
	}

	/**
	 * Gets the session.
	 *
	 * @return The session.
	 */
	public IoSession getSession() {
		return session;
	}

	/**
	 * Gets the id.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Handles an incoming packet.
	 *
	 * @param packet The incoming packet.
	 */
	public void handlePacket(LoginPacket packet) {
//		final IoBuffer buf = packet.getPayload();
//		switch(packet.getOpcode()) {
//			case LoginPacket.CHECK_LOGIN: {
//				String name = NameUtils.formatNameForProtocol(IoBufferUtils.getRS2String(buf));
//				String password = IoBufferUtils.getRS2String(buf);
//				LoginDebugger.getDebugger().log("Node check login: " + name + "," + password);
//				LoginResponse res = server.getLoader().checkLogin(new PlayerDetails(null, name, password, 0, "", null, null, "", "Id2"));
//				if(res.getReturnCode() == 2) {
//					PlayerData pd = new PlayerData(name, (int) Rank.getPrimaryRankIndex(res.getPlayerByName()));
//					NodeManager.getNodeManager().register(pd, this);
//				}
//				IoBuffer resp = IoBuffer.allocate(16);
//				resp.setAutoExpand(true);
//				IoBufferUtils.putRS2String(resp, name);
//				resp.put((byte) res.getReturnCode());
//				resp.flip();
//				session.write(new LoginPacket(LoginPacket.CHECK_LOGIN_RESPONSE, resp));
//				break;
//			}
//			case LoginPacket.LOAD: {
//				String name = NameUtils.formatNameForProtocol(IoBufferUtils.getRS2String(buf));
//				Player p = new Player(new PlayerDetails(null, name, "", 0, null, null, "", "Id3"), false);
//				int code = server.getLoader().loadPlayer(p) ? 1 : 0;
//				LoginDebugger.getDebugger().log("7. Loaded Player in Node");
//				IoBuffer resp = IoBuffer.allocate(1024);
//				resp.setAutoExpand(true);
//				IoBufferUtils.putRS2String(resp, name);
//				resp.put((byte) code);
//				if(code == 1) {
//					IoBuffer data = IoBuffer.allocate(16);
//					data.setAutoExpand(true);
//					//p.serialize(data)
//					//PlayerFiles.saveGame(p);
//					data.flip();
//					resp.putShort((short) data.remaining());
//					resp.put(data);
//				}
//				resp.flip();
//				session.write(new LoginPacket(LoginPacket.LOAD_RESPONSE, resp));
//				break;
//			}
//			case LoginPacket.SAVE: {
//				String name = NameUtils.formatNameForProtocol(IoBufferUtils.getRS2String(buf));
//				int dataLength = buf.getUnsignedShort();
//				byte[] data = new byte[dataLength];
//				buf.get(data);
//				IoBuffer dataBuffer = IoBuffer.allocate(dataLength);
//				dataBuffer.put(data);
//				dataBuffer.flip();
//				Player p = new Player(new PlayerDetails(null, name, "", 0, null, null, "", "Id4"), false);
//				//if(PlayerFiles.exists(name))
//				PlayerFiles.saveGame(p);
//				//else
//				//p.deserialize(dataBuffer,false);
//				System.out.println("NODE");
//				int code = server.getLoader().savePlayer(p, "Loginpacket save") ? 1 : 0;
//				IoBuffer resp = IoBuffer.allocate(16);
//				resp.setAutoExpand(true);
//				IoBufferUtils.putRS2String(resp, name);
//				resp.put((byte) code);
//				resp.flip();
//				session.write(new LoginPacket(LoginPacket.SAVE_RESPONSE, resp));
//				break;
//			}
//			case LoginPacket.DISCONNECT: {
//				String name = NameUtils.formatNameForProtocol(IoBufferUtils.getRS2String(buf));
//				PlayerData p = NodeManager.getNodeManager().getPlayerByName(name);
//				if(p != null) {
//					NodeManager.getNodeManager().unregister(p);
//				}
//			}
//			break;
//		}
	}

}
