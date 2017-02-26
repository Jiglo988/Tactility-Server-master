package org.hyperion.rs2.model.itf;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.packet.InterfacePacketHandler;

/**
 * Created by Jet on 1/2/2015.
 */
public abstract class Interface {

    public static final int HIDE = 1 << 1;
    public static final int SHOW = 1 << 2;
    public static final int DRAGGABLE = 1 << 3;
    public static final int UNDRAGGABLE = 1 << 4;
    public static final int CLOSEABLE = 1 << 5;
    public static final int UNCLOSEABLE = 1 << 6;

    private final int id;

    protected Interface(final int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void show(final Player player, final int additionalStates){
        sendState(player, SHOW | additionalStates);
        player.getInterfaceManager().setActive(this, true);
    }

    public void show(final Player player){
        show(player, 0);
    }

    public void hide(final Player player, final int additionalStates){
        sendState(player, HIDE | additionalStates);
        player.getInterfaceManager().setActive(this, false);
    }

    public void hide(final Player player){
        hide(player, 0);
    }

    public void sendState(final Player player, final int states){
        player.write(createStatePacket(states));
    }

    public Packet createStatePacket(final int states){
        return createStateBuilder().put((byte)states).toPacket();
    }

    public void sendResponse(final Player player, final int response){
        player.write(createDataBuilder().put((byte)response).toPacket());
    }

    public PacketBuilder createDataBuilder(){
        return createBuilder(InterfacePacketHandler.DATA_OPCODE, Packet.Type.VARIABLE_SHORT);
    }

    public PacketBuilder createStateBuilder(){
        return createBuilder(InterfacePacketHandler.STATE_OPCODE, Packet.Type.FIXED);
    }

    private PacketBuilder createBuilder(final int opcode, final Packet.Type type){
        return new PacketBuilder(opcode, type).putShort(id);
    }

    public abstract void handle(final Player player, final Packet pkt);

    public static PacketBuilder createStateBuilder(final int id) {
        return createBuilder(InterfacePacketHandler.STATE_OPCODE, Packet.Type.FIXED, id);
    }

    private static PacketBuilder createBuilder(final int opcode, final Packet.Type type, final int id){
        return new PacketBuilder(opcode, type).putShort(id);
    }

    public static Packet createStatePacket(final int states, final int id){
        return createStateBuilder(id).put((byte)states).toPacket();
    }

}
