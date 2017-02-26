package org.hyperion.rs2.model.content.skill.cooking;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel
 *         5/21/2016
 */
public class Cooking implements ContentTemplate {

    private final List<Integer> ITEM_IDS = Arrays.asList(317, 321, 2136, 2138, 327, 345, 353, 349, 331, 359, 377, 363, 371, 7944, 383, 395, 389, 15270);
    private final List<Integer> OBJECT_IDS = Arrays.asList(114, 2728, 4172, 8750, 2732, 2728, 2729, 2730, 2731, 2859, 3039);
    private final List<Integer> BUTTON_IDS = Arrays.asList(13720, 13719, 13718, 13717);

    @Override
    public int[] getValues(final int type) {
        return type == 14 ? ArrayUtils.fromList(ITEM_IDS) : type == 0 ? ArrayUtils.fromList(BUTTON_IDS) : null;
    }

    private void cook(final Player player, final int id, final int amount, final int object) {
        player.getSkills().stopSkilling();
        player.getActionSender().removeAllInterfaces();
        final int trimmed = player.getInventory().getCount(id) < amount ? player.getInventory().getCount(id) : amount;
        final Item item = Item.create(id, trimmed);
        if (item == null || !player.getInventory().hasItem(item)) {
            return;
        }
        final Cookable cookable = Cookable.getByItemId(id);
        if (cookable != null) {
            final int animation = object != 2732 ? 896 : 897;
            if (player.getSkills().getLevel(Skills.COOKING) < cookable.getLevel()) {
                player.sendf("You need a cooking level of %d to cook this item.", cookable.getLevel());
                return;
            }
            player.playAnimation(Animation.create(animation));
            player.setCurrentTask(new Task(2500, "Cooking Task") {
                int value = trimmed;

                @Override
                public void execute() {
                    if (player.getRandomEvent().skillAction(2)) {
                        stop();
                        return;
                    }
                    value--;
                    player.getInventory().remove(Item.create(item.getId(), 1));
                    final String name = TextUtils.titleCase(String.valueOf(cookable).replace("_", " "));
                    if (Combat.random(player.getSkills().getLevel(Skills.COOKING) + 3 - cookable.getLevel()) != 1
                            || (player.getSkills().getLevel(Skills.COOKING) > cookable.getSuccess() && cookable.getSuccess() != -1)) {
                        player.getInventory().add(Item.create(cookable.getCooked()));
                        player.getAchievementTracker().itemSkilled(Skills.COOKING, cookable.getCooked(), 1);
                        player.getSkills().addExperience(Skills.COOKING, cookable.getExperience() * (Constants.XPRATE * 5));
                        player.sendf("You succesfully cook some %s.", name);
                    } else {
                        player.getInventory().add(Item.create(cookable.getBurned()));
                        player.getAchievementTracker().itemSkilled(Skills.COOKING, cookable.getBurned(), 1);
                        player.sendf("You accidentally burn the %s.", name);
                    }
                    if (value <= 0 || !player.getInventory().hasItem(Item.create(item.getId()))) {
                        stop();
                        return;
                    }
                    player.playAnimation(Animation.create(animation));
                }

                @Override
                public void stop() {
                    player.playAnimation(Animation.create(65535));
                    super.stop();
                }
            });
            TaskManager.submit(player.getCurrentTask());
        }
    }

    @Override
    public boolean clickObject(final Player player, final int type, final int id, final int slot, final int object, final int a) {
        final String INFORMATION = "Raw Fish ID";
        if (type == 0) {
            if (player.getExtraData().get(INFORMATION) != null) {
                final int item = (Integer) player.getExtraData().get(INFORMATION);
                player.getExtraData().remove(INFORMATION);
                cook(player, item, id == 13720 ? 1 : id == 13719 ? 5 : id == 13718 ? 10 : id == 13717 ? 28 : 0, object);
            }
        } else if (type == 14) {
            final Item item = Item.create(id, player.getInventory().getCount(id));
            if (item == null || !player.getInventory().hasItem(item)) {
                return false;
            }
            if (OBJECT_IDS.stream().anyMatch(value -> value.equals(object))) {
                player.getSkills().stopSkilling();
                player.getInterfaceState().setOpenDialogueId(0);
                player.getInterfaceState().interfaceClosed();
                ContentEntity.removeAllWindows(player);
                player.getExtraData().put(INFORMATION, id);
                player.getActionSender().sendPacket164(1743);
                player.getActionSender().sendInterfaceModel(13716, 250, id);
            }
        }
        return false;
    }
}
