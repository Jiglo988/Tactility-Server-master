package org.hyperion.rs2.model.itf.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.ticket.TicketManager;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;

/**
 * Created by Jet on 1/10/2015.
 */
public class PendingRequests extends Interface {

    public static final int ID = 5;

    public PendingRequests() {
        super(ID);
    }

    public void handle(final Player player, final Packet pkt) {
        TicketManager.display(player);
        player.write(Interface.createStatePacket(SHOW, 3));
    }

}
