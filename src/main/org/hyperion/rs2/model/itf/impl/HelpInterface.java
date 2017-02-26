package org.hyperion.rs2.model.itf.impl;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.content.ticket.TicketManager;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;

/**
 * Created by Jet on 1/10/2015.
 */
public class HelpInterface extends Interface {

    private static final int ID = 3;

    public HelpInterface() {
        super(ID);
    }

    public void handle(final Player player, final Packet pkt) {

        final int id = pkt.getInt();
        final int state = pkt.getByte();

        System.out.println("ID is: "+id);
        if(state == 0)
            TicketManager.assist(player, id);
        else
            TicketManager.remove(TicketManager.forId(id));

    }

    static {
    }


}
