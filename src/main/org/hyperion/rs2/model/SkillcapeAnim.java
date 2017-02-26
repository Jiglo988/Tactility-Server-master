package org.hyperion.rs2.model;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DrHales
 */
public class SkillcapeAnim {

    public static void skillcapeEmote(final Player player) {
        final Item item = player.getEquipment().get(1);
        if (item != null) {
            final Cape cape = Cape.getCapeById(item.getId());
            if (cape != null) {
                if (cape.getSkillId() == -1 || (player.getSkills().getLevel(cape.getSkillId()) >= 99
                        && player.getSkills().getExps()[cape.getSkillId()] >= cape.getExperience())) {
                    cape.action(player);
                } else {
                    player.sendf("You need to be level 99 %s to perform this emote.", cape.equals(Cape.DUNGEONEERING_MASTER) ? "and 200000000 experience" : "");
                }
            } else {
                player.sendMessage("You need to be wearing a skillcape to perform an emote.");
            }
        } else {
            player.sendMessage("You need to be wearing a skillcape to perform an emote.");
        }
    }

    public enum Cape {
        ATTACK(Arrays.asList(9747, 9748, 10639), Skills.ATTACK, 4959, 823),
        DEFENCE(Arrays.asList(9753, 9754, 10641), Skills.DEFENCE, 4961, 824),
        STRENGTH(Arrays.asList(9750, 9751, 10640), Skills.STRENGTH, 4981, 828),
        HITPOINTS(Arrays.asList(9768, 9769, 10647), Skills.HITPOINTS, 4971, 833),
        RANGING(Arrays.asList(9756, 9757, 10642), Skills.RANGED, 4973, 832),
        PRAYER(Arrays.asList(9759, 9760, 10643), Skills.PRAYER, 4979, 829),
        MAGIC(Arrays.asList(9762, 9763, 10644), Skills.MAGIC, 4939, 813),
        COOKING(Arrays.asList(9801, 9802, 10658), Skills.COOKING, 4955, 821),
        WOODCUTTING(Arrays.asList(9807, 9808, 10660), Skills.WOODCUTTING, 4957, 822),
        FLETCHING(Arrays.asList(9783, 9784, 10652), Skills.FLETCHING, 4937, 812),
        FISHING(Arrays.asList(9798, 9799, 10657), Skills.FISHING, 4951, 819),
        FIREMAKING(Arrays.asList(9804, 9805, 10659), Skills.FIREMAKING, 4975, 831),
        CRAFTING(Arrays.asList(9780, 9781, 10651), Skills.CRAFTING, 4949, 818),
        SMITHING(Arrays.asList(9795, 9796, 10656), Skills.SMITHING, 4943, 815),
        MINING(Arrays.asList(9792, 9792, 10655), Skills.MINING, 4941, 814),
        HERBLORE(Arrays.asList(9774, 9775, 10649), Skills.HERBLORE, 4969, 835),
        AGILITY(Arrays.asList(9771, 9772, 10648), Skills.AGILITY, 4977, 830),
        THIEVING(Arrays.asList(9777, 9778, 10650), Skills.THIEVING, 4965, 826),
        SLAYER(Arrays.asList(9786, 9787, 10653), Skills.SLAYER, 4967, 827),
        FARMING(Arrays.asList(9810, 9811, 10661), Skills.FARMING, 4963, 825),
        RUNECRAFTING(Arrays.asList(9765, 9766, 10645), Skills.RUNECRAFTING, 4947, 817),
        CONSTRUCTION(Arrays.asList(9789, 9790, 10654), Skills.CONSTRUCTION, 4953, 820),
        HUNTER(Arrays.asList(9948, 9949, 10646), Skills.HUNTER, 5158, 907),
        SUMMONING(Arrays.asList(12169, 12170, 12524), Skills.SUMMONING, 8525, 1515),
        DUNGEONEERING(Arrays.asList(15706, 18508, 18509), Skills.DUNGEONEERING, -1, -1) {
            @Override
            public void action(final Player player) {
                player.getSkills().stopSkilling();
                player.playAnimation(Animation.create(13190));
                player.playGraphics(Graphic.create(2442));
                player.setCurrentTask(new Task(625L, true, "Dungeoneering Animation Task") {
                    final int value = (int) (Math.random() * (2 + 1));
                    int count;
                    @Override
                    public void execute() {
                        if (count == 1) {
                            player.setPNpc(value == 0 ? 11227 : value == 1 ? 11228 : 11229);
                            player.playAnimation(Animation.create(value == 0 ? 13192 : value == 1 ? 13193 : 13194));
                        } else if (count == 6) {
                            stop();
                        }
                        count++;
                    }
                    @Override
                    public void stop() {
                        player.setPNpc(-1);
                        player.playAnimation(Animation.create(65535));
                        player.playGraphics(Graphic.create(-1));
                        super.stop();
                    }
                });
                TaskManager.submit(player.getCurrentTask());
            }
        },
        DUNGEONEERING_MASTER(Arrays.asList(19709, 19710), Skills.DUNGEONEERING, -1, -1) {
            @Override
            public int getExperience() {
                return 200000000;
            }

            @Override
            public void action(final Player player) {
                player.getSkills().stopSkilling();
                player.playAnimation(Animation.create(13190));
                player.playGraphics(Graphic.create(2442));
                player.setCurrentTask(new Task(625L, true, "Dungeoneering Animation Task") {
                    final int value = (int) (Math.random() * (2 + 1));
                    int count;
                    @Override
                    public void execute() {
                        if (count == 1) {
                            player.setPNpc(value == 0 ? 11227 : value == 1 ? 11228 : 11229);
                            player.playAnimation(Animation.create(value == 0 ? 13192 : value == 1 ? 13193 : 13194));
                        } else if (count == 6) {
                            stop();
                        }
                        count++;
                    }
                    @Override
                    public void stop() {
                        player.setPNpc(-1);
                        player.playAnimation(Animation.create(65535));
                        player.playGraphics(Graphic.create(-1));
                        super.stop();
                    }
                });
                TaskManager.submit(player.getCurrentTask());
            }
        },
        QUEST(Arrays.asList(9813, 10662), -1, 4945, 816);

        private static Map<Integer, Cape> BY_ITEM_ID = new HashMap<>();

        static {
            Arrays.stream(values()).forEach(cape -> cape.getCapes().forEach(integer -> BY_ITEM_ID.put(integer, cape)));
        }

        private final List<Integer> capes;
        private final int skillId, animation, graphics;
        private final int experience = 13034436;

        Cape(List<Integer> capes, int skillId, int animation, int graphics) {
            this.capes = capes;
            this.skillId = skillId;
            this.animation = animation;
            this.graphics = graphics;
        }

        public static Cape getCapeById(final int value) {
            return BY_ITEM_ID.get(value);
        }

        public List<Integer> getCapes() {
            return capes;
        }

        public int getSkillId() {
            return skillId;
        }

        public int getExperience() {
            return experience;
        }

        public void action(final Player player) {
            player.playAnimation(Animation.create(animation));
            player.playGraphics(Graphic.create(graphics));
        }
    }
}
