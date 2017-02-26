package org.hyperion.rs2.model.content.skill.dungoneering.reward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 3/1/15
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class RingPerks {

    private int perks = 0;

    public void setPerk(final int perks) {
        this.perks = perks;
    }

    public int perkLevel() {
        return perks;
    }

    public int hasPerk(final Perk perk) {
        for (int level = perk.maxLevel; level >= 0; level--) {
            int flag = perk.getFlag(level);
            if ((flag & perks) == flag)
                return level;
        }
        return -1;
    }

    public boolean hasPerk(final int flag) {
        return (perks & flag) == flag;
    }

    public void addFlags(final Perk... perks) {
        Arrays.asList(perks).forEach(value -> addFlag(value.getFlag(hasPerk(value) + 1)));
    }

    private void addFlag(final int perk) {
        perks |= perk;
    }

    public boolean removeFlag(final Perk perk, final int level) {
        if (hasPerk(perk.getFlag(level))) {
            perks &= ~perk.getFlag(level);
            return true;
        }
        return false;
    }

    public void upgradePerk(Perk perk) {
        final int oldLevel = hasPerk(perk);
        if (oldLevel + 1 > perk.maxLevel)
            return;
        addFlags(perk);
        if (oldLevel > -1)
            removeFlag(perk, oldLevel);
    }

    public int calcNextPerkCost(int style) {
        return (int) (35_000 * Math.pow(1.5, hasPerk(Perk.forStyle(style)) + 1));
    }

    public double bonus(final int style, boolean accuracy) {
        double base = 1.0;
        Perk perk = Perk.forStyle(style);
        final int level = hasPerk(perk) + 1;
        base += accuracy ? perk.getAccuracyPercent(level) : perk.getBonusPercent(level);
        return base;
    }

    public double boost(final int style, final boolean accuracy, final double original) {
        return (int) (original * bonus(style, accuracy));
    }

    public List<Perk> getPerks() {
        final List<Perk> list = new ArrayList<>();
        Arrays.asList(Perk.values()).stream().filter(value -> hasPerk(value) >= 0).forEach(list::add);
        return list;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final Perk perk : Perk.values()) {
            final int perkLevel = hasPerk(perk) + 1;
            builder.append(perk.name()).append(" - ").append("Hit Boost: ").
                    append(String.format("%.1f%%", perk.getBonusPercent(perkLevel) * 100D)).append("Accuracy Boost: ").
                    append(String.format("%.1f%%", perk.getAccuracyPercent(perkLevel) * 100D)).append("_B_");
        }
        return builder.toString();
    }

    public String[] boosts() {
        return toString().split("_B_");
    }

    public enum Perk {
        MELEE(0, 3) {
            @Override
            double getBonusPercent(final int level) {
                return (((double) level * 1.2) / 100D);
            }

            @Override
            double getAccuracyPercent(final int level) {
                return ((level) * 2 / 100D);
            }
        },
        RANGE(1, 3) {
            @Override
            double getBonusPercent(final int level) {
                return ((level * 1.5) / 100D);
            }

            @Override
            double getAccuracyPercent(final int level) {
                return ((level) * 2 / 100D);
            }

        },
        MAGIC(2, 3) {
            @Override
            double getBonusPercent(final int level) {
                return (((double) level * 1.5) / 100D);
            }

            @Override
            double getAccuracyPercent(final int level) {
                return ((level) * 2 / 100D);
            }
        };

        public final int index, maxLevel;

        Perk(final int index, final int maxLevel) {
            this.index = index;
            this.maxLevel = maxLevel;
        }

        public static final Perk forStyle(int style) {
            if (style >= 5)
                style -= 5;
            for (final Perk perk : values()) {
                if (perk.index == style)
                    return perk;
            }
            return null;
        }

        double getBonusPercent(final int level) {
            throw new AbstractMethodError();
        }

        double getAccuracyPercent(final int level) {
            throw new AbstractMethodError();
        }

        public int getFlag(int level) {
            return 1 << (ordinal() + level * 3);
        }

        public String toString() {
            return super.toString().substring(0, super.toString().indexOf("_"));
        }

    }
}
