package org.hyperion.rs2.model.content.ticket;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.misc2.Jail;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.packet.InterfacePacketHandler;
import org.hyperion.rs2.util.TextUtils;

import java.util.Set;
import java.util.TreeSet;

public class TicketManager {

    private final static Set<Ticket> TICKETS = new TreeSet<>((Ticket id_1, Ticket id_2) -> Integer.valueOf(id_1.id).compareTo(id_2.id));

    public static synchronized void add(final Ticket ticket) {
        TICKETS.add(ticket);
        World.getPlayers().stream().filter(player -> player != null && Rank.getPrimaryRank(player).ordinal() >= ticket.min_rank.ordinal()).forEach(player ->
            player.sendMessage("@dre@------------------------------ New ticket submitted ------------------------------",
                    "@dre@Player: @bla@" + TextUtils.ucFirst(ticket.name),
                    "@dre@Title: @bla@" + TextUtils.ucFirst(ticket.title),
                    "@dre@Reason: @bla@" + ticket.request,
                    "@dre@Rank: @bla@" + ticket.min_rank,
                    "@dre@----------------------------------------------------------------------------------"
            ));
        refreshSizeForStaff();
    }

    public static synchronized void remove(final Ticket ticket) {
        TICKETS.remove(ticket);
        refreshSizeForStaff();
    }

    private static int getSizeForPlayer(Player player) {
        return (int)(TICKETS.stream().filter(ticket -> ticket.min_rank.ordinal() <= Rank.getPrimaryRankIndex(player)).count());
    }

    private static void refreshSizeForStaff() {
        World.getPlayers().stream().filter(player -> player != null && Rank.isStaffMember(player)).forEach(player -> {
            final PacketBuilder builder = new PacketBuilder(InterfacePacketHandler.DATA_OPCODE, Packet.Type.VARIABLE_SHORT).putShort(5);
            builder.putTriByte(getSizeForPlayer(player));
            player.write(builder.toPacket());
        });
    }

    public static Ticket forId(final int id) {
        Ticket tick = null;
        for(final Ticket ticket : TICKETS)
            if(ticket.id == id)
                tick = ticket;
        return tick;
    }

    public static void assist(final Player player, final int id) {
        if(!Rank.isStaffMember(player))
            return;
        if(!ItemSpawning.canSpawn(player))
            return;
        Ticket tick = forId(id);
        if(tick != null) {
            final Player p = World.getPlayerByName(tick.name);
            if(p != null) {
                if(!p.getPosition().inDuel() && !p.getPosition().inPvPArea()  && !Jail.inJail(p) && !p.getDungeoneering().inDungeon()) {
                    Magic.teleport(p, player.getPosition(), true);
                    remove(tick);
                }
                        else {
                        player.sendMessage("You can't be assisted while you are in a duel, jail, dung, or wilderness");
                    }

            } else {
                player.sendMessage("Player is offline");
                remove(tick);
            }
        } else {
            player.sendMessage("Ticket doesn't exist");
        }
    }

    public static void display(final Player player) {
        final PacketBuilder builder = new PacketBuilder(InterfacePacketHandler.DATA_OPCODE, Packet.Type.VARIABLE_SHORT).putShort(3);
        builder.putShort((short)getSizeForPlayer(player));
        for(final Ticket ticket : TICKETS) {
            if(Rank.getPrimaryRank(player).ordinal() >= ticket.min_rank.ordinal()) {
                builder.putRS2String(ticket.title);
                builder.putRS2String(ticket.name);
                builder.putRS2String(ticket.request);
                builder.putTriByte(ticket.id);
                builder.put((byte)revert(ticket.min_rank));
            }
        }
        player.write(builder.toPacket());
    }

    public static Rank convert(final int read) {

        switch(read) {
            case 0:
                return Rank.HELPER;
            case 1:
                return Rank.MODERATOR;
            case 2:
                return Rank.ADMINISTRATOR;
        }

        return Rank.ADMINISTRATOR;

    }

    public static int revert(final Rank min_rank) {
        switch(min_rank) {
            case HELPER:
                return 0;
            case MODERATOR:
                return 1;
            case ADMINISTRATOR:
                return 2;
        }
        return 0;
    }

}
