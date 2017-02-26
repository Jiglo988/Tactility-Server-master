package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;

import java.util.*;

/**
 * Created by DrHales on 3/4/2016.
 */
public class ViewPacketActivityCommand extends NewCommand {

    public ViewPacketActivityCommand() {
        super("viewpacketactivity", Rank.ADMINISTRATOR, new CommandInput<Integer>(integer -> integer > 0, "Integer", "Packet ID"));
    }

    public boolean execute(final Player player, final String[] input) {
        final List<Player> players = new ArrayList<>(World.getPlayers());
        int size = Math.min(players.size(), Integer.parseInt(input[0].trim()));
        Arrays.asList(Style.values()).stream().forEach(style -> style.execute(player, players, size));
        return true;
    }

    public enum Style implements Comparator<Player> {

        PACKET_COUNT("Packet Count", "packetCount"),
        PACKETS_READ("Packets Read", "packetsRead"),
        PACKETS_WRITE("Packets Wrote", "packetsWrite");

        private final String name;
        private final String key;

        Style(final String name, final String key) {
            this.name = name;
            this.key = key;
        }

        public int compare(final Player player, final Player other) {
            return other.getExtraData().getInt(key) - player.getExtraData().getInt(key);
        }

        public void execute(final Player player, final List<Player> players, final int size) {
            long total = 0;
            for (final Player other : players)
                total += other.getExtraData().getInt(key);
            player.sendf("@blu@%s Activity @bla@- @red@%,d @blu@Total", name, total);
            Collections.sort(players, this);
            for (int i = size - 1; i >= 0; i--) {
                final Player p = players.get(i);
                player.sendf("@blu@%s@bla@: @red@%,d", p.getName(), p.getExtraData().get(key));
            }
        }


    }

}
