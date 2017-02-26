package org.hyperion.rs2.model.content.bounty;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.util.Time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gilles on 14/09/2015.
 */
public class BountyHunterLogout extends Task {

    public BountyHunterLogout() {
        super(Time.ONE_MINUTE * 20);
    }

    private static final Object LOCK = new Object();

    private static final int LOGOUT_LIMIT = 2;

    private static Map<String, Integer> logoutsInWild = new HashMap<>();
    private static List<Player> blocked = new ArrayList();

    public static void addLogout(Player player) {
        synchronized (LOCK) {
            logoutsInWild.put(player.getName(), logoutsInWild.getOrDefault(player.getName(), 0) + 1);
        }
    }

    public static boolean isBlocked(Player player) {
        synchronized (LOCK) {

             if(player == null)
                return false;
            return blocked.contains(player);
        }
    }

    public static void playerLogout(Player player) {
        synchronized (LOCK) {
            addLogout(player);
            if(logoutsInWild.getOrDefault(player.getName(), 0) >= LOGOUT_LIMIT) {
                if(!blocked.contains(player))
                    blocked.add(player);
            }
        }
    }

    public void execute() {
        synchronized (LOCK) {
            logoutsInWild.clear();
            blocked.clear();
        }
    }
}
