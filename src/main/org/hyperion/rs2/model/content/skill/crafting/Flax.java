package org.hyperion.rs2.model.content.skill.crafting;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.ContentEntity;

/**
 * Created by Gilles on 8/09/2015.
 */
public class Flax extends Crafting{

    public static boolean pickFlax(final Player client, int id) {
        if(client.isBusy())
            return false;
        if(client.getRandomEvent().skillAction(2))
            return false;
        client.setBusy(true);
        ContentEntity.startAnimation(client, 2286);
        World.submit(new Task(2000) {
            @Override
            public void execute() {
                if(!client.isBusy()) {
                    this.stop();
                    return;
                }
                ContentEntity.addItem(client, 1779, 1);
                client.setBusy(false);
                this.stop();
            }
        });
        return true;
    }

    public static boolean spinFlax(final Player client, int id) {
        ContentEntity.startAnimation(client, 894);
        client.setBusy(true);
        World.submit(new Task(2000) {
            int amount = ContentEntity.getItemAmount(client, 1779);

            @Override
            public void execute() {
                if(ContentEntity.isItemInBag(client, 1779) && amount > 0 && client.isBusy()) {
                    ContentEntity.startAnimation(client, 894);
                    ContentEntity.deleteItemA(client, 1779, 1);
                    ContentEntity.addItem(client, 1777, 1);
                    ContentEntity.sendMessage(client, "You spin the flax into a bow String.");
                    ContentEntity.addSkillXP(client, 15 * Crafting.EXPMULTIPLIER, Skills.CRAFTING);
                    amount--;
                } else
                    this.stop();
            }

        });
        ContentEntity.addItem(client, 1779, 1);

        return true;
    }
}
