package org.hyperion.rs2.model.content.skill.hunter;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.ArrayUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Daniel
 *         5/19/2016
 */
public class Hunter implements ContentTemplate {

    public static final List<Integer> NPC_IDS = Arrays.asList(1028, 6055, 1029, 6056, 1030, 6057, 1031, 6058, 1032, 6059, 1033, 6060, 1034, 6061, 1035, 6062, 6053, 6063, 7845, 7846, 6054, 6064, 7903, 7906, 5085, 5084, 5083, 5082);

    private void process(final Player player, final int id, final int x, final int y) {
        if ((System.currentTimeMillis() - player.contentTimer) > 1499) {
            final Impling imp = Impling.getImplingForId(id);
            final Net net = player.getEquipment().get(Equipment.SLOT_WEAPON) != null ? Net.getNetById(player.getEquipment().get(Equipment.SLOT_WEAPON).getId()) : null;
            if (imp != null) {
                catchImp(player, imp, net, id, x, y);
            } else {
                final Butterfly butterfly = Butterfly.getButteryflyForId(id);
                if (butterfly != null) {
                    catchButterfly(player, butterfly, net, id, x, y);
                }
            }
        }
    }

    public void loot(final Player player, final int id) {
        final Item item = Item.create(id);
        if (player.getInventory().hasItem(item)) {
            final Jar jar = Jar.getJarForId(id);
            if (jar != null) {
                if (player.getInventory().freeSlots() < 1) {
                    player.sendMessage("You need at least 1 inventory slot to loot jars!");
                    return;
                }
                player.getInventory().remove(item);
                final int reward = jar.getRewards().get(new Random().nextInt(jar.getRewards().size()));
                player.getInventory().add(Item.create(reward, jar.getAmount(reward)));
            }
        }
    }

    private void catchImp(final Player player, final Impling imp, final Net net, final int id, final int x, final int y) {
        player.getSkills().stopSkilling();
        if (player.getSkills().getLevel(Skills.HUNTER) < imp.getLevel()) {
            player.sendf("You need a hunter level of %d to catch this impling.", imp.getLevel());
            return;
        }
        if (net == null && player.getSkills().getLevel(Skills.HUNTER) < (imp.getLevel() + 10)) {
            player.sendMessage("You need a net to catch this imp!");
            return;
        }
        if (player.getInventory().freeSlots() < 1) {
            player.sendMessage("You need some free inventory slots to catch imps!");
            return;
        }
        player.contentTimer = System.currentTimeMillis();
        player.playAnimation(Animation.create(6605));
        player.face(Position.create(x, y, 0));
        player.setCurrentTask(new Task(500L, "Impling Catching Task") {
            @Override
            public void execute() {
                if (HunterNPCs.remove(id, x, y)) {
                    final int count = player.getExtraData().getInt("impscaught") + 1;
                    player.getExtraData().put("impscaught", count);
                    player.getInventory().add(Item.create(imp.getItem()));
                    player.getAchievementTracker().itemSkilled(Skills.HUNTER, imp.getItem(), 1);
                    player.sendMessage("You catch the impling!");
                    player.getSkills().addExperience(Skills.HUNTER, net != null ? net.getBonus(imp.getExperience()) : imp.getExperience());
                    player.sendf("You have now caught @red@%,d@bla@ impling%s.", count, count > 1 ? "s" : "");
                    HunterNPCs.spawn(HunterNPCs.Spawn.getRandomLocation());
                }
                stop();
            }
        });
        TaskManager.submit(player.getCurrentTask());
    }

    private void catchButterfly(final Player player, final Butterfly butterfly, final Net net, final int id, final int x, final int y) {
        player.getSkills().stopSkilling();
        if (player.getSkills().getLevel(Skills.HUNTER) < butterfly.getLevel()) {
            player.sendf("You need a hunter level of %d to catch this butterfly.", butterfly.getLevel());
            return;
        }
        if (net == null) {
            player.sendMessage("You need a net to catch this butterfly!");
            return;
        }
        if (player.getInventory().freeSlots() < 1) {
            player.sendMessage("You need some free inventory slots to catch butterflies!");
            return;
        }
        player.contentTimer = System.currentTimeMillis();
        player.playAnimation(Animation.create(6999));
        player.face(Position.create(x, y, 0));
        player.setCurrentTask(new Task(500L, "Butterfly Catching Task") {
            @Override
            public void execute() {
                if (HunterNPCs.remove(id, x, y)) {
                    player.getInventory().add(Item.create(butterfly.getItem()));
                    player.sendMessage("You catch the butterfly!");
                    player.getSkills().addExperience(Skills.HUNTER, net.getBonus(butterfly.getExperience()));
                    HunterNPCs.spawn(HunterNPCs.Spawn.getRandomLocation());
                }
                stop();
            }
        });
        TaskManager.submit(player.getCurrentTask());
    }

    @Override
    public boolean clickObject(final Player player, final int type, final int id, final int x, final int y, int d) {
        if (type == 10) {
            process(player, id, x, y);
        } else if (type == 17 || type == 22) {
            loot(player, id);
        }
        return false;
    }

    @Override
    public void init() throws FileNotFoundException {
        HunterNPCs.startup();
    }

    @Override
    public int[] getValues(final int value) {
        return value == 10 ? ArrayUtils.fromList(NPC_IDS) : value == 22 ? ArrayUtils.fromList(Jar.IDS) : value == 17 ? ArrayUtils.fromList(Butterfly.IDS) : null;
    }

}
