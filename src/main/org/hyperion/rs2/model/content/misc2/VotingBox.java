package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.io.FileNotFoundException;

public class VotingBox implements ContentTemplate {

    public static final int ID = 3062;

    private static int counter = 0;

    public static void cleanPlayer(Player player) {
        if (player.getInventory() != null)
            for (Item item : player.getInventory().toArray()) {
                checkItem(item);
            }
        if (player.getBank() != null)
            for (Item item : player.getBank().toArray()) {
                checkItem(item);
            }
        if (player.getEquipment() != null)
            for (Item item : player.getEquipment().toArray()) {
                checkItem(item);
            }
        if (player.getBoB() != null)
            for (Item item : player.getBoB().toArray()) {
                checkItem(item);
            }
    }

    private static void checkItem(Item item) {
        if (item == null)
            return;
        /**
         * I think this deletes all the strange boxes upon initialization? That's no good - already unspawnable
         */
        /*if(item.getId() == ID) {
			item.setId(1);
			counter += item.getCount();
			System.out.println("Deleted : " + counter);
		}*/
    }


    @Override
    public boolean clickObject(Player player, int type, int a, int b, int c,
                               int d) {
        if (a == ID && player.getInventory().remove(new Item(ID, 1)) > 0) {
            player.getAchievementTracker().itemOpened(ID);
            player.getPoints().increaseVotingPoints(1);
            player.getPoints().increaseDonatorPoints(1, false);
        }
        return false;
    }

    @Override
    public void init() throws FileNotFoundException {
        // TODO Auto-generated method stub

    }

    @Override
    public int[] getValues(int type) {
        if (type == 1)
            return new int[]{ID};
        return null;
    }

}
