package org.hyperion.rs2.model.joshyachievementsv2.constraint.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.joshyachievementsv2.constraint.Constraint;

public class PrayerBookConstraint implements Constraint{

    public enum PrayerBook{

        DEFAULT("Normal"){
            boolean using(final Player player){
                return player.getPrayers().isDefaultPrayerbook();
            }
        },
        CURSES("Curses"){
            boolean using(final Player player){
                return !player.getPrayers().isDefaultPrayerbook();
            }
        };

        private final String name;

        PrayerBook(final String name){
            this.name = name;
        }

        abstract boolean using(final Player player);
    }

    public final PrayerBook book;

    private final String desc;

    public PrayerBookConstraint(final PrayerBook book){
        this.book = book;

        desc = String.format("Using the %s prayer book", book.name);
    }

    public boolean constrained(final Player player){
        return book.using(player);
    }

    public String desc(){
        return desc;
    }
}
