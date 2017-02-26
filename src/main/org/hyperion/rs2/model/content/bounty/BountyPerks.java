package org.hyperion.rs2.model.content.bounty;

import java.util.ArrayList;
import java.util.List;

public final class BountyPerks {

    public static final void main(String[] args) {
    }

    public static enum Perk {
        SPEC_RESTORE(2),
        VENG_REDUCTION(2),
        PRAY_LEECH(2);

        final int maxLevel;

        private Perk(final int maxLevel) {
            this.maxLevel = maxLevel;
        }

        public int getFlag(int level) {
            return 1 << (ordinal() + level * 3);
        }
        
        public String toString() {
        	return super.toString().substring(0, super.toString().indexOf("_"));
        }

    }

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
        for (Perk p : perks)
            addFlag(p.getFlag(hasPerk(p) + 1));
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
        if(oldLevel + 1 > perk.maxLevel)
            return;
        addFlags(perk);
        if(oldLevel > -1)
            removeFlag(perk, oldLevel);
    }

    public int calculateTotalPerks() {
        int totalPerks = 0;
        for(final Perk perk : getPerks()) {
            totalPerks += hasPerk(perk) + 1;
        }
        return totalPerks;
    }
    
    public int calcNextPerkCost() {
    	return (int)(25 + Math.pow(2, calculateTotalPerks()));
    }

    public List<Perk> getPerks() {
        List<Perk> playerperks = new ArrayList<>();
        for (Perk perk : Perk.values()) {
            if (hasPerk(perk) >= 0)
                playerperks.add(perk);
        }
        return playerperks;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder("[");
        for (final Perk perk : getPerks()) {
            builder.append(perk.toString()).append("(");
            for(int i = -1; i < hasPerk(perk); i++) {
                builder.append("I");
            }
            builder.append(")").append(",");
        }
        builder.append("]");
        return builder.toString();
    }
}
