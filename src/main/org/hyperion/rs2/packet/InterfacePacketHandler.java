package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.net.Packet;

/**
 * Created by Jet on 1/2/2015.
 */
public class InterfacePacketHandler implements PacketHandler{

    public static final int STATE_OPCODE = 140;
    public static final int DATA_OPCODE = 141;

    public void handle(final Player player, final Packet pkt){
        switch(pkt.getOpcode()){
            case STATE_OPCODE:
                handleState(player, pkt);
                break;
            case DATA_OPCODE:
                handleData(player, pkt);
                break;
        }
    }

    private void handleState(final Player player, final Packet pkt){
        //empty for now
    }

    private void handleData(final Player player, final Packet pkt){
        final short id = pkt.getShort();
        final Interface itf = InterfaceManager.get(id);
        if(itf == null)
            return;
        itf.handle(player, pkt);
    }
}
