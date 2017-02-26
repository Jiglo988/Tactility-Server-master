package org.hyperion.rs2.model.content;

import org.hyperion.rs2.model.Player;

/**
 * Created by Gilles on 16/02/2016.
 */
public enum Lock {
    YELL,
    YELL_TITLES,
    STAFF_LOGIN,
    EXPERIENCE_LOCK,
    INFORMATION_MESSAGES,
    DOUBLE_EXPERIENCE,
    INCREASED_DROP_RATE,
    REDUCED_YELL_DELAY,
    BOUNTY_HUNTER,
    TRIVIA,
    PK_MESSAGES,
    LOOT_MESSAGES;

    public long getBitMask() {
        return 1L << ordinal() + 1;
    }

    public static boolean isEnabled(Player player, Lock lock) {
        return (player.getLocks() & lock.getBitMask()) != 0;
    }

    public static void switchLock(Player player, Lock lock) {
        if(isEnabled(player, lock)) {
            player.setLocks(player.getLocks() & ~lock.getBitMask());
        } else {
            player.setLocks(player.getLocks() | lock.getBitMask());
        }
    }

    public static boolean toggleOn(Player player, Lock lock) {
        if(isEnabled(player, lock))
            return false;
        switchLock(player, lock);
        return true;
    }

    public static boolean toggleOff(Player player, Lock lock) {
        if(!isEnabled(player, lock))
            return false;
        switchLock(player, lock);
        return true;
    }
}
