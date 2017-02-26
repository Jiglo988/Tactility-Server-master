package org.hyperion.rs2.model.content.specialareas;

import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandHandler;
import org.hyperion.rs2.model.OSPK;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.content.minigame.barrowsffa.BarrowsFFA;
import org.hyperion.rs2.model.content.specialareas.impl.HybridZone;
import org.hyperion.rs2.model.content.specialareas.impl.NewGamePK;
import org.hyperion.rs2.model.content.specialareas.impl.PurePk;
import org.hyperion.util.Time;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 11/20/14
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpecialAreaHolder {
    private static final Map<String, SpecialArea> map;

    static {
        map = new HashMap<>();
        map.put("purepk", new PurePk());
        map.put("newgamepk", new NewGamePK());
        map.put("hybrid", new HybridZone());
        map.put("ospk", new OSPK());

        map.entrySet().stream().forEach(area -> {
            NewCommandHandler.submit(
                    new NewCommand(area.getValue().command(area.getKey()).getKey(), Rank.PLAYER, Time.FIFTEEN_SECONDS) {
                        @Override
                        protected boolean execute(Player player, String[] input) {
                            SpecialAreaHolder.get(area.getKey()).ifPresent(area -> area.enter(player));
                            return true;
                        }
                    }
            );
        });
    }

    public static Optional<SpecialArea> get(final String key) {
        return Optional.of(map.get(key));
    }

    public static Set<Map.Entry<String, SpecialArea>> getAll() {
        return map.entrySet();
    }

    public static Collection<SpecialArea> getAreas() {
        return map.values();
    }

    public static void put(final String command, final SpecialArea area, boolean cmd) {
        map.put(command, area);
        if(cmd)
            NewCommandHandler.submit(
                    new NewCommand(area.command(command).getKey()) {
                        @Override
                        protected boolean execute(Player player, String[] input) {
                            return true;
                        }
                    }
            );
    }

}
