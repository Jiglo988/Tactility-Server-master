package org.hyperion.rs2.model.itf.impl;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.ticket.TicketManager;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;

/**
 * Created by Jet on 1/10/2015.
 */
public class AskForHelp extends Interface {

    private static final int ID = 2;

    public AskForHelp() {
        super(ID);
    }

    public void handle(final Player player, final Packet pkt) {

        final String title = pkt.getRS2String();
        final String text = pkt.getRS2String();
        final Rank rank = TicketManager.convert(pkt.getByte());


        if(player.getTicketHolder().canMakeTicket()) {
            player.getTicketHolder().create(player.getName(), title, text, rank);
            player.sendMessage("You successfully submit your ticket");
        } else
            player.sendMessage("You can only create a ticket once every 60 seconds");

    }

    static {
    }
}
