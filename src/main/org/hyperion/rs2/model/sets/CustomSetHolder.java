package org.hyperion.rs2.model.sets;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/7/14
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomSetHolder {

    public static void main(String args[]) {
        CustomSetHolder holder = new CustomSetHolder(null);

        holder.sets[0] = CustomSet.rand();
        holder.sets[1] = CustomSet.rand();

        System.out.println(holder.toString());

        holder.parse(holder.toString());
        System.out.println(holder.toString());
    }

    private CustomSet[] sets = new CustomSet[3];

    public CustomSet[] getCustomSets() {
        return sets;
    }

    public void setCustomSets(final CustomSet[] value) {
        sets = value;
    }

    private final Player player;

    public CustomSetHolder(final Player player) {
        this.player = player;
    }

    /**
     * tries to assign a custom set slot from the player's current gear
     */

    public boolean save(int slot) {
        final boolean save = player != null && slot < getSlotCount();
        if(save)
            sets[slot] = CustomSet.fromGear(player.getInventory(), player.getEquipment());
        return save;
    }

    /**
     * @return amount of slots for each rank, as array.length (index+1)
     */

    private int getSlotCount() {
        if(Rank.hasAbility(player, Rank.SUPER_DONATOR)) return 3;
        else if(Rank.hasAbility(player, Rank.DONATOR)) return 2;
        else return 1;
    }

    public boolean apply(int slot) {
        if(player == null || sets[slot] == null) {
            return false;
        }
        try {
            return sets[slot].apply(player);
        } catch(final Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for(final CustomSet set : sets)
            if(set != null)
                builder.append(set.toSaveableString());
        return builder.toString();
    }

    public void parse(final String read) {
        try {
           if(read == null || read.length() < 10)
               return;
            final String parts[] = read.split("NEW_SET");
            for(int i = 1; i < parts.length; i++)  {
                try {
                    sets[i-1] = CustomSet.fromString(parts[i]);
                }catch(Exception ex) {
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }



}
