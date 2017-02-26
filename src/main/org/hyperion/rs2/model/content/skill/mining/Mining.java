package org.hyperion.rs2.model.content.skill.mining;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.ArrayUtils;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel
 *         5/20/2016
 */
public class Mining implements ContentTemplate {

    private final List<Integer> OBJECTS = Arrays.asList(450, 2491, 2108, 2109, 2094, 2095, 14902, 2090, 2091, 14906, 2092, 2093, 14913, 2100, 2101, 14902, 2096, 2097, 14850, 2098, 2099, 2102, 2103, 14853, 2104, 2105, 14862, 14859, 14860, 1755, 2112, 2113);

    @Override
    public int[] getValues(int value) {
        return (value == 6 || value == 7) ? ArrayUtils.fromList(OBJECTS) : null;
    }

    @Override
    public boolean clickObject(final Player player, final int type, final int id, final int x, final int y, int d) {
        return handleMiningObjects(player, id) || (type == 6 && mine(player, id, x, y)) || (type == 7 && prospect(player, id, x, y));
    }

    private boolean mine(final Player player, final int id, final int x, final int y) {
        player.getSkills().stopSkilling();
        if (id == 450) {
            player.sendMessage("This rock contains no ore.");
            return false;
        }
        final Rock rock = Rock.getRockById(id);
        if (rock != null) {
            final Pickaxe pickaxe = Pickaxe.getPickaxe(player);
            if (pickaxe == null) {
                player.sendMessage("You do not have a pickaxe you can use.");
                return false;
            }
            if (player.getSkills().getLevel(Skills.MINING) < rock.getLevel()) {
                player.sendf("You need a mining level of %d to mine this rock.", rock.getLevel());
                return false;
            }
            if (player.getInventory().freeSlots() < 1) {
                player.sendMessage("There is not enough space in your inventory.");
                return false;
            }
            player.cE.face(x, y);
            player.playAnimation(Animation.create(pickaxe.getAnimation()));
            final int cycles = getCycles(player, pickaxe, rock) < 1 ? 1 : getCycles(player, pickaxe, rock);
            player.setCurrentTask(new Task(600L, "Mining Rocks Task") {
                int cycle = 0;

                @Override
                public void execute() {
                    if (player.getInventory().freeSlots() < 1) {
                        player.sendMessage("You do not have any free inventory space left.");
                        stop();
                        return;
                    }
                    if (cycle < cycles) {
                        cycle++;
                        player.playAnimation(Animation.create(pickaxe.getAnimation()));
                    } else if (cycle == cycles) {
                        player.getInventory().add(Item.create(rock.getItem()));
                        player.getAchievementTracker().itemSkilled(Skills.MINING, rock.getItem(), 1);
                        player.getSkills().addExperience(Skills.MINING, rock.getExperience() * (Constants.XPRATE * 6));
                        player.sendMessage("You get some ore.");
                        cycle = 0;
                        if (rock.getRespawn() > 0) {
                            final GameObject expired = new GameObject(GameObjectDefinition.forId(450), Position.create(x, y, player.getPosition().getZ()), 10, 0);
                            ObjectManager.addObject(expired);
                            TaskManager.submit(new Task(rock.getRespawn(), String.format("%s Ore Respawn Task", TextUtils.titleCase(String.valueOf(rock)))) {
                                @Override
                                public void execute() {
                                    ObjectManager.replace(expired, new GameObject(GameObjectDefinition.forId(id), Position.create(x, y, player.getPosition().getZ()), 10, 0));
                                    stop();
                                }
                            });
                            stop();
                        } else {
                            stop();
                        }
                    }
                }

                @Override
                public void stop() {
                    player.playAnimation(Animation.create(65535));
                    super.stop();
                }
            });
            TaskManager.submit(player.getCurrentTask());
            return true;
        }
        return false;
    }

    private int getCycles(final Player player, final Pickaxe pickaxe, final Rock rock) {
        return Misc.inclusiveRandom((int) (rock.getTicks() - (player.getSkills().getLevel(Skills.MINING) * 0.01) + pickaxe.getSpeed()), rock.getTicks());
    }

    private boolean prospect(final Player player, final int id, final int x, final int y) {
        player.getSkills().stopSkilling();
        if (id == 450) {
            player.sendMessage("This rock contains no ore.");
            return false;
        }
        final Rock rock = Rock.getRockById(id);
        if (rock != null) {
            player.cE.face(x, y);
            player.sendMessage("You examine the rock for ores...");
            TaskManager.submit(new Task(1000L, "Mining Prospect Task") {
                @Override
                public void execute() {
                    player.sendf("This rock contains %s ore.", TextUtils.titleCase(String.valueOf(rock).replace("_", " ")));
                    player.playAnimation(Animation.create(65535));
                    stop();
                }
            });
            return true;
        }
        return false;
    }

    private boolean handleMiningObjects(final Player player, final int value) {
        if (value == 1755) {
            player.setTeleportTarget(Position.create(player.getPosition().getX(), player.getPosition().getY() - 6400, 0));
            return true;
        } else if (value == 2112 || value == 2113) {
            if (player.getSkills().getLevel(Skills.MINING) < 60) {
                player.sendMessage("You need 60 mining to enter the Mining Guild.");
                return false;
            }
            return true;
        }
        return false;
    }

}
