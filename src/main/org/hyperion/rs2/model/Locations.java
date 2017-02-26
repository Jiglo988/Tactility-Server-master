package org.hyperion.rs2.model;

import org.hyperion.engine.task.impl.OverloadStatsTask;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.content.minigame.Bork;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.model.content.misc2.Jail;
import org.hyperion.rs2.model.content.skill.dungoneering.DungeoneeringManager;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.model.itf.impl.DungoneeringParty;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;

import java.util.Arrays;

/**
 * Created by Gilles on 3/03/2016.
 */
public class Locations {

    /**
     * Gets called on login to do the required actions, and assign a player his original location.
     *
     * @param player The player
     */
    public static void login(Player player) {
        player.setLocation(Location.getLocation(player));
        player.getLocation().login(player);
        player.getLocation().enterArea(player);
    }

    /**
     * Gets called on logout to clean up after a player, to make sure he is not in instanced
     * places for example.
     *
     * @param player The player
     */
    public static void logout(Player player) {
        player.getLocation().logout(player);
    }

    /**
     * To add a new location;
     * Take the BOTTOM LEFT and the TOP RIGHT corner
     * <p>
     * ADD Coordinates bottom left corner
     * ADD Coordinates top right corner
     * <p>
     * THIS CAN INCLUDE MORE SQUARES THAN JUST ONE. SIMPLY ADD ANOTHER ONE
     * AFTER THE FIRST 2 NUMBERS. SQUARES CAN OVERLAP
     * AND IT WILL TAKE THE FIRST INITIALIZED AS PRIORITY.
     */
    public enum Location {
        DUNGEONEERING_PVM(new int[]{3136, 3327}, new int[]{5442, 5567}, true, false, true, true, false, false, Rank.PLAYER) {
            @Override
            public boolean canTeleport(Player player) {
                player.sendMessage("You cannot teleport while in a Dungeon.");
                return false;
            }

            @Override
            public boolean onDeath(Player player) {
                player.setTeleportTarget(player.getDungeoneering().getCurrentDungeon().getStartRoom().getSpawnLocation(), false);
                player.getDungeoneering().getCurrentDungeon().kill(player);
                return true;
            }

            @Override
            public void enter(Player player) {
                if (!player.getDungeoneering().inDungeon()) {
                    player.setTeleportTarget(Edgeville.POSITION);
                }
            }

            @Override
            public void leave(Player player) {
                if (player.getDungeoneering().inDungeon() && (!player.getLocation().equals(DUNGEONEERING_LOBBY)
                        && !player.getLocation().equals(DUNGEONEERING_START))) {
                    player.getDungeoneering().getCurrentDungeon().remove(player, false);
                    //player.setTeleportTarget(Position.create(2908, 9913, player.getDungeoneering().getCurrentDungeon().getStartRoom().heightLevel), false);
                    //player.getDungeoneering().setCurrentRoom(player.getDungeoneering().getCurrentDungeon().getStartRoom());
                }
            }
        },
        DUNGEONEERING_START(new int[]{2884, 2943}, new int[]{9881, 9919}, false, false, true, false, false, false, Rank.PLAYER) {
            @Override
            public boolean canTeleport(Player player) {
                player.sendMessage("You cannot teleport while in a Dungeon.");
                return false;
            }

            @Override
            public boolean onDeath(Player player) {
                player.setTeleportTarget(player.getDungeoneering().getCurrentDungeon().getStartRoom().getSpawnLocation(), false);
                player.getDungeoneering().getCurrentDungeon().kill(player);
                return true;
            }

            @Override
            public void enter(Player player) {
                if (!player.getDungeoneering().inDungeon()) {
                    player.setTeleportTarget(Edgeville.POSITION);
                }
            }

            @Override
            public void leave(Player player) {
                if (player.getDungeoneering().inDungeon() && (!player.getLocation().equals(DUNGEONEERING_LOBBY)
                        && !player.getLocation().equals(DUNGEONEERING_PVM))) {
                    player.getDungeoneering().getCurrentDungeon().remove(player, false);
                    //player.setTeleportTarget(Position.create(2908, 9913, player.getDungeoneering().getCurrentDungeon().getStartRoom().heightLevel), false);
                    //player.getDungeoneering().setCurrentRoom(player.getDungeoneering().getCurrentDungeon().getStartRoom());
                }
            }
        },
        DUNGEONEERING_LOBBY(new int[]{2981, 2993}, new int[]{9629, 9644}, new int[]{0}, false, false, true, false, false, false, Rank.PLAYER) {
            @Override
            public boolean canTeleport(Player player) {
                player.sendMessage("You cannot teleport out of the Lobby.");
                return false;
            }

            @Override
            public void enter(Player player) {
                if ((!DungeoneeringManager.ENABLED) || (!player.getInventory().contains(15707)
                        && !player.getEquipment().contains(15707))
                        || ((Arrays.asList(player.getInventory().toArray()).stream().filter(value -> value != null).anyMatch(value -> value.getDefinition().getId() != 15707))
                        || (Arrays.asList(player.getEquipment().toArray())).stream().filter(value -> value != null).anyMatch(value -> value.getDefinition().getId() != 15707))) {
                    player.setTeleportTarget(Edgeville.POSITION);
                }
            }

            @Override
            public void leave(Player player) {
                if (!player.getLocation().equals(Location.DUNGEONEERING_START)) {
                    DungoneeringParty.removeFromLobbyParty(player);
                }
            }
        },
        DUNGEONEERING_LOBBY_BORDER(new int[]{2975, 2997}, new int[]{9625, 9648}, false, false, false, false, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                DungoneeringParty.removeFromLobbyParty(player);
                player.setTeleportTarget(Edgeville.POSITION);
            }
        },
        BORK(new int[]{3490, 3585}, new int[]{9915, 9970}, true, true, false, false, false, false) {
            @Override
            public boolean onDeath(Player player) {
                return Bork.doDeath(player);
            }
        },
        FUN_PK_AREA(new int[]{2586, 2602, 2603, 2606, 2581, 2585}, new int[]{3151, 3172, 3151, 3172, 3151, 3172}, true, true, true, false, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.attackOption = true;
                    if (player.getNpcState()) {
                        player.setPNpc(-1);
                    }
                }
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                return true;
            }

            @Override
            public void leave(Player player) {
                player.getActionSender().sendPlayerOption("null", 2, 1);
                player.attackOption = false;
            }

            @Override
            public boolean onDeath(Player player) {
                player.setTeleportTarget(Position.create(3096, 3471, 0), false);
                return true;
            }
        },
        AVATAR_OF_DESTRUCTION_AREA(new int[]{2641, 2668}, new int[]{9617, 9664}, new int[]{0}, true, true, false, false, false, false, Rank.PLAYER),
        CORPOREAL_BEAST_AREA(new int[]{2499, 2539}, new int[]{4630, 4663}, new int[]{0}, true, true, true, false, false, false, Rank.PLAYER),
        EDGEVILLE_BANK_BANKER_AREA(new int[]{3095, 3098}, new int[]{3488, 3493}, false, false, false, false, false, false, Rank.OWNER),
        EDGEVILLE_BANK_AREA(new int[]{3091, 3094, 3095, 3098, 3090, 3090}, new int[]{3488, 3499, 3494, 3499, 3493, 3497}, false, false, false, false, true, true, Rank.PLAYER),
        EDGEVILLE_AREA(new int[]{3067, 3101}, new int[]{3462, 3519}, new int[]{0}, false, true, true, false, true, true, Rank.PLAYER),
        AFK_AREA(new int[]{2138, 2164}, new int[]{5091, 5106}, false, true, true, true, true, true, Rank.PLAYER),
        DONATOR_PLACE_AREA(new int[]{2344, 2389}, new int[]{4938, 4987}, false, true, true, false, true, true, Rank.DONATOR),
        SUPER_DONATOR_AREA(new int[]{2028, 2045}, new int[]{4517, 4541}, false, true, true, false, true, true, Rank.SUPER_DONATOR),
        SUPER_DONATOR_PVM_AREA(new int[]{3464, 3511}, new int[]{9478, 9524}, false, true, true, false, true, true, Rank.SUPER_DONATOR),
        GRAARDOR_ROOM(new int[]{2864, 2876, 2869, 2871}, new int[]{5351, 5369, 5370, 5372}, true, true, false, false, false, false, Rank.PLAYER),
        KREE_ARRA_ROOM(new int[]{2824, 2842, 2821, 2823}, new int[]{5296, 5308, 5301, 5303}, true, true, false, false, false, false, Rank.PLAYER),
        TSUTSAROTH_ROOM(new int[]{2918, 2936, 2937, 2940}, new int[]{5318, 5331, 5322, 5326}, true, false, false, false, false, false, Rank.PLAYER),
        ZILYANA_ROOM(new int[]{2889, 2907, 2885, 2888}, new int[]{5258, 5276, 5267, 5270}, true, true, false, false, false, false, Rank.PLAYER),
        KBD_AREA(new int[]{2256, 2287}, new int[]{4680, 4711}, new int[]{0}, true, true, true, false, false, false, Rank.PLAYER),
        OSPK_BANK_AREA(new int[]{2256, 2261}, new int[]{4680, 4711}, new int[]{600}, false, false, false, false, true, true, Rank.PLAYER),
        OSPK_AREA(new int[]{2262, 2287}, new int[]{4680, 4711}, new int[]{600}, false, false, true, false, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.setCanSpawnSet(false);
                    player.attackOption = true;
                    if (player.getNpcState()) {
                        player.setPNpc(-1);
                    }
                    if (player.isOverloaded())
                        OverloadStatsTask.OverloadFactory.applyBoosts(player);
                }
            }

            @Override
            public void leave(Player player) {
                player.setCanSpawnSet(true);
                player.cE.getDamageDealt().clear();
                player.getActionSender().sendPlayerOption("null", 2, 1);
                player.attackOption = false;
                player.getActionSender().sendWildLevel(getWildernessLevel(player));
                player.wildernessLevel = getWildernessLevel(player);
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                return validWildernessDifference(player, target);
            }

            @Override
            public void process(Player player) {
                processWilderness(player);
            }
        },
        PURE_PK_BANK_AREA(new int[]{2256, 2261}, new int[]{4680, 4711}, new int[]{444}, false, false, false, false, true, true, Rank.PLAYER),
        PURE_PK_AREA(new int[]{2262, 2287}, new int[]{4680, 4711}, new int[]{444}, false, false, true, false, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.setCanSpawnSet(false);
                    player.attackOption = true;
                    if (player.getNpcState()) {
                        player.setPNpc(-1);
                    }
                    if (player.isOverloaded())
                        OverloadStatsTask.OverloadFactory.applyBoosts(player);
                }
            }

            @Override
            public void leave(Player player) {
                player.setCanSpawnSet(true);
                player.cE.getDamageDealt().clear();
                player.getActionSender().sendPlayerOption("null", 2, 1);
                player.attackOption = false;
                player.getActionSender().sendWildLevel(getWildernessLevel(player));
                player.wildernessLevel = getWildernessLevel(player);
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                return validWildernessDifference(player, target);
            }

            @Override
            public void process(Player player) {
                processWilderness(player);
            }
        },
        WILDERNESS_MULTI(new int[]{3004, 3063, 3134, 3325, 3196, 3325, 3149, 3325, 3149, 3215, 3215, 3400, 3014, 3215, 2989, 3008, 2992, 3006}, new int[]{3601, 3716, 3523, 3648, 3646, 3781, 3781, 3845, 3845, 3903, 3845, 4000, 3856, 3903, 3914, 3930, 10340, 10364}, true, true, true, true, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.setCanSpawnSet(false);
                    player.attackOption = true;
                    if (player.getNpcState()) {
                        player.setPNpc(-1);
                    }
                    if (player.isOverloaded())
                        OverloadStatsTask.OverloadFactory.applyBoosts(player);
                }
            }

            @Override
            public void leave(Player player) {
                player.setCanSpawnSet(true);
                player.cE.getDamageDealt().clear();
                player.getActionSender().sendPlayerOption("null", 2, 1);
                player.attackOption = false;
                player.getActionSender().sendWildLevel(getWildernessLevel(player));
                player.wildernessLevel = getWildernessLevel(player);
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                return validWildernessDifference(player, target);
            }

            @Override
            public boolean canTeleport(Player player) {
                return canWildernessTeleport(player);
            }

            @Override
            public void process(Player player) {
                processWilderness(player);
            }
        },
        KBD_WILDERNESS_ENTRANCE(new int[]{3063, 3070}, new int[]{10253, 10261}, new int[]{0}, false, true, true, false, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.setCanSpawnSet(false);
                    player.attackOption = true;
                    if (player.getNpcState()) {
                        player.setPNpc(-1);
                    }
                    if (player.isOverloaded())
                        OverloadStatsTask.OverloadFactory.applyBoosts(player);
                }
            }

            @Override
            public void leave(Player player) {
                player.setCanSpawnSet(true);
                player.cE.getDamageDealt().clear();
                player.getActionSender().sendPlayerOption("null", 2, 1);
                player.attackOption = false;
                player.getActionSender().sendWildLevel(getWildernessLevel(player));
                player.wildernessLevel = getWildernessLevel(player);
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                return validWildernessDifference(player, target);
            }

            @Override
            public boolean canTeleport(Player player) {
                return canWildernessTeleport(player);
            }

            @Override
            public void process(Player player) {
                processWilderness(player);
            }
        },
        WILDERNESS_DUNGEON(new int[]{3010, 3058}, new int[]{10306, 10349}, false, true, true, false, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.setCanSpawnSet(false);
                    player.attackOption = true;
                    if (player.getNpcState()) {
                        player.setPNpc(-1);
                    }
                    if (player.isOverloaded())
                        OverloadStatsTask.OverloadFactory.applyBoosts(player);
                }
            }

            @Override
            public void leave(Player player) {
                player.setCanSpawnSet(true);
                player.cE.getDamageDealt().clear();
                player.getActionSender().sendPlayerOption("null", 2, 1);
                player.attackOption = false;
                player.getActionSender().sendWildLevel(getWildernessLevel(player));
                player.wildernessLevel = getWildernessLevel(player);
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                return validWildernessDifference(player, target);
            }

            @Override
            public boolean canTeleport(Player player) {
                return canWildernessTeleport(player);
            }

            @Override
            public void process(Player player) {
                processWilderness(player);
            }
        },
        _13S_AREA(new int[]{2971, 2982}, new int[]{3606, 3615}, new int[]{24}, false, true, false, false, true, true, Rank.PLAYER),
        WILDERNESS(new int[]{2941, 3392, 2986, 3012, 3653, 3706, 3650, 3653}, new int[]{3520, 3968, 10338, 10366, 3441, 3538, 3457, 3472}, false, true, true, true, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.setCanSpawnSet(false);
                    player.attackOption = true;
                    if (player.getNpcState()) {
                        player.setPNpc(-1);
                    }
                    if (player.isOverloaded())
                        OverloadStatsTask.OverloadFactory.applyBoosts(player);
                }
            }

            @Override
            public void leave(Player player) {
                player.setCanSpawnSet(true);
                player.cE.getDamageDealt().clear();
                player.getActionSender().sendPlayerOption("null", 2, 1);
                player.attackOption = false;
                player.getActionSender().sendWildLevel(getWildernessLevel(player));
                player.wildernessLevel = getWildernessLevel(player);
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                return validWildernessDifference(player, target);
            }

            @Override
            public boolean canTeleport(Player player) {
                return canWildernessTeleport(player);
            }

            @Override
            public void process(Player player) {
                processWilderness(player);
            }
        },
        FIGHT_CAVES(new int[]{2360, 2445}, new int[]{5045, 5125}, true, true, false, false, false, false, Rank.PLAYER) {
            @Override
            public boolean onDeath(Player player) {
                player.getActionSender().sendMessage("Too bad, you didn't complete fight caves!");
                player.setTeleportTarget(Position.create(2439, 5171, 0), false);
                return true;
            }

            @Override
            public void leave(Player player) {
                player.fightCavesWave = 0;
                player.getActionSender().showInterfaceWalkable(-1);
            }

            @Override
            public boolean canTeleport(Player player) {
                player.sendMessage("You cannot teleport from the Fight Caves.");
                return false;
            }
        },
        FIGHT_PITS(new int[]{2370, 2425}, new int[]{5133, 5167}, true, true, true, false, false, false, Rank.PLAYER) {
            @Override
            public boolean onDeath(Player player) {
                return FightPits.pitsDeath(player);
            }

            @Override
            public void enter(Player player) {
                FightPits.fightPitsCheck(player);
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.attackOption = true;
                }
            }

            @Override
            public boolean canTeleport(Player player) {
                player.sendMessage("You cannot teleport from the Fight Pits.");
                return false;
            }

            @Override
            public void leave(Player player) {
                FightPits.fightPitsCheck(player);
                if (player.attackOption) {
                    player.getActionSender().sendPlayerOption("null", 2, 1);
                    player.attackOption = false;
                }
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                return FightPits.inGame(player) && !FightPits.isSameTeam(player, target);
            }
        },
        FIGHT_PITS_WAIT_ROOM(new int[]{2393, 2404}, new int[]{5168, 5176}, false, false, false, false, false, false, Rank.PLAYER) {

        },
        DUEL_ARENA_BANK(new int[]{3380, 3384}, new int[]{3267, 3271}, false, false, false, false, true, true, Rank.PLAYER),
        DUEL_ARENA(new int[]{3332, 3358, 3333, 3357, 3334, 3356, 3335, 3355, 3336, 3354, 3337, 3353, 3338, 3352, 3339, 3351, 3363, 3389, 3364, 3388, 3365, 3387, 3366, 3386, 3367, 3385, 3368, 3384, 3369, 3383, 3370, 3382, 3332, 3358, 3333, 3357, 3334, 3356, 3335, 3355, 3336, 3354, 3337, 3353, 3338, 3352, 3339, 3351, 3363, 3389, 3364, 3388, 3365, 3387, 3366, 3386, 3367, 3385, 3368, 3384, 3369, 3383, 3370, 3382, 3332, 3358, 3333, 3357, 3334, 3356, 3335, 3355, 3336, 3354, 3337, 3353, 3338, 3352, 3339, 3351, 3363, 3389, 3364, 3388, 3365, 3387, 3366, 3386, 3367, 3385, 3368, 3384, 3369, 3383, 3370, 3382}, new int[]{3250, 3252, 3249, 3253, 3247, 3255, 3246, 3256, 3246, 3256, 3245, 3257, 3244, 3258, 3244, 3258, 3250, 3252, 3249, 3253, 3247, 3255, 3246, 3256, 3246, 3256, 3245, 3257, 3245, 3257, 3244, 3258, 3231, 3233, 3230, 3234, 3228, 3236, 3227, 3237, 3227, 3237, 3226, 3238, 3226, 3238, 3225, 3239, 3231, 3233, 3230, 3234, 3228, 3236, 3227, 3237, 3227, 3237, 3226, 3238, 3226, 3238, 3225, 3239, 3212, 3214, 3211, 3215, 3209, 3217, 3208, 3218, 3208, 3218, 3207, 3219, 3207, 3219, 3206, 3220, 3212, 3214, 3211, 3215, 3209, 3217, 3208, 3218, 3208, 3218, 3207, 3219, 3207, 3219, 3206, 3220}, false, false, false, false, false, false, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (player.duelAttackable <= 0) {
                    player.setTeleportTarget(Position.create(3360 + Combat.random(17), 3274 + Combat.random(3), 0), false);
                    return;
                }
                if (!player.attackOption) {
                    player.getActionSender().sendPlayerOption("Attack", 2, 0);
                    player.attackOption = true;
                }
            }

            @Override
            public void leave(Player player) {
                if (player.attackOption) {
                    player.getActionSender().sendPlayerOption("null", 2, 0);
                    player.attackOption = false;
                }
            }

            @Override
            public boolean canTeleport(Player player) {
                player.sendMessage("You cannot teleport while being in a duel!");
                return false;
            }

            @Override
            public boolean onDeath(Player player) {
                if (player.duelAttackable > 0) {
                    Duel.finishFullyDuel(player);
                    return true;
                }
                return false;
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                if (player.duelAttackable > 0
                        && player.duelAttackable == target.getIndex()) {
                    return true;
                }
                player.sendMessage("This is not your opponent!");
                return false;
            }
        },
        DUEL_ARENA_LOBBY(new int[]{3355, 3379, 3374, 3379, 3327, 3392}, new int[]{3267, 3279, 3280, 3286, 3203, 3266}, new int[]{0}, false, false, false, false, false, true, Rank.PLAYER) {
            @Override
            public void enter(Player player) {
                if (!player.duelOption) {
                    player.getActionSender().sendPlayerOption("Challenge", 5, 0);
                    if (player.getNpcState()) {
                        player.setPNpc(-1);
                    }
                    player.duelOption = true;
                }
            }

            @Override
            public void leave(Player player) {
                if (player.duelOption) {
                    if ((Rank.hasAbility(player, Rank.MODERATOR)))
                        player.getActionSender().sendPlayerOption("Moderate", 5, 0);
                    else
                        player.getActionSender().sendPlayerOption("null", 5, 0);
                    player.duelOption = false;
                }
            }
        },
        GUTHANS_BARROWS(new int[]{3565, 3574}, new int[]{9683, 9691}, new int[]{3}, false, false, false, false, false, false, Rank.PLAYER),
        KHARILS_BARROWS(new int[]{3546, 3556}, new int[]{9679, 9687}, new int[]{3}, false, false, false, false, false, false, Rank.PLAYER),
        DHAROKS_BARROWS(new int[]{3569, 3578}, new int[]{9703, 9709}, new int[]{3}, false, false, false, false, false, false, Rank.PLAYER),
        TORAGS_BARROWS(new int[]{3534, 3544}, new int[]{9700, 9707}, new int[]{3}, false, false, false, false, false, false, Rank.PLAYER),
        VERACS_BARROWS(new int[]{3550, 3559}, new int[]{9711, 9718}, new int[]{3}, false, false, false, false, false, false, Rank.PLAYER),
        AHRIMS_BARROWS(new int[]{3551, 3560}, new int[]{9695, 9703}, new int[]{3}, false, false, false, false, false, false, Rank.PLAYER),
        BARROWS(new int[]{3520, 3598, 3543, 3584, 3543, 3560}, new int[]{9653, 9750, 3265, 3314, 9685, 9702}, false, false, false, false, false, false, Rank.PLAYER),
        JAIL(new int[]{2089, 2105, 2087, 2090, 2087, 2090, 2104, 2108, 2096, 2099, 2086, 2088}, new int[]{4421, 4436, 4436, 4439, 4419, 4422, 4419, 4422, 4420, 4420, 4428, 4429}, false, false, false, false, true, true, Rank.PLAYER) {
            @Override
            public boolean canTeleport(Player player) {
                if (!Rank.hasAbility(player, Rank.HELPER)) {
                    player.sendMessage("You cannot teleport out of jail.");
                    return false;
                }
                return true;
            }
        },
        JAIL_FULL_AREA(new int[]{2065, 2111}, new int[]{4416, 4455}, false, false, false, false, true, true, Rank.HELPER) {
            @Override
            public void enter(Player player) {
                if (!Rank.hasAbility(player, Rank.DEVELOPER)){
                    player.setTeleportTarget(Jail.POSITION);
                }
            }
        },
        DEFAULT(null, null, false, true, true, true, true, true);

        /**
         * The corners, from low to high
         */
        private final int[] x, y, z;
        /**
         * Is the area multi or not
         */
        private final boolean multi;
        /**
         * Can they use summoning or not
         */
        private final boolean summonAllowed;
        /**
         * Can they follow players or not
         */
        private final boolean followingAllowed;
        /**
         * Can they firemake here or not
         */
        private final boolean firemakingAllowed;
        /**
         * Can they use the bank command here or not
         */
        private final boolean bankingAllowed;
        /**
         * Can they use the spawn command here or not
         */
        private final boolean spawningAllowed;
        /**
         * Is there a required rank to be in this area?
         */
        private final Rank minimumRank;

        Location(int[] x, int[] y) {
            this(x, y, new int[]{}, false, false, false, false, false, false, Rank.PLAYER);
        }

        Location(int[] x, int[] y, boolean multi, boolean summonAllowed, boolean followingAllowed, boolean firemakingAllowed, boolean bankingAllowed, boolean spawningAllowed) {
            this(x, y, new int[]{}, multi, summonAllowed, followingAllowed, firemakingAllowed, bankingAllowed, spawningAllowed, Rank.PLAYER);
        }

        Location(int[] x, int[] y, boolean multi, boolean summonAllowed, boolean followingAllowed, boolean firemakingAllowed, boolean bankingAllowed, boolean spawningAllowed, Rank minimumRank) {
            this(x, y, new int[]{}, multi, summonAllowed, followingAllowed, firemakingAllowed, bankingAllowed, spawningAllowed, minimumRank);
        }

        Location(int[] x, int[] y, int[] z, boolean multi, boolean summonAllowed, boolean followingAllowed, boolean firemakingAllowed, boolean bankingAllowed, boolean spawningAllowed, Rank minimumRank) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.multi = multi;
            this.summonAllowed = summonAllowed;
            this.followingAllowed = followingAllowed;
            this.firemakingAllowed = firemakingAllowed;
            this.bankingAllowed = bankingAllowed;
            this.spawningAllowed = spawningAllowed;
            this.minimumRank = minimumRank;
        }

        public final int[] getX() {
            return x;
        }

        public final int[] getY() {
            return y;
        }

        public final int[] getZ() {
            return z;
        }

        public final boolean isMulti() {
            return multi;
        }

        public final boolean isSummonAllowed() {
            return summonAllowed;
        }

        public final boolean isFollowingAllowed() {
            return followingAllowed;
        }

        public final boolean isFiremakingAllowed() {
            return firemakingAllowed;
        }

        public final boolean isBankingAllowed() {
            return bankingAllowed;
        }

        public final boolean isSpawningAllowed() {
            return spawningAllowed;
        }

        public final Rank getMinimumRank() {
            return minimumRank;
        }

        /**
         * This determines what happens on login.
         * This can be left empty, and only needs to be overwritten if it does something
         *
         * @param player The player
         */
        public void login(Player player) {
        }

        /**
         * This is the default call when a player enters an area. This code
         * checks if they have the rank, and then pass the player on
         * to the area-specific code.
         *
         * @param player The player
         */
        public final void enterArea(Player player) {
            if (!Rank.hasAbility(player, getMinimumRank())) {
                player.setTeleportTarget(Edgeville.POSITION);
                return;
            }
            //This is a temp block of code for TESTING
            if (Rank.hasAbility(player, Rank.DEVELOPER)) {
                player.sendf("[@dre@Location@bla@]:@dre@%s", TextUtils.titleCase(name().replace("_", " ")));
            }
            enter(player);
        }

        /**
         * This determines what happens when the player enters an area.
         * This can be left empty, and only needs to be overwritten if it does something
         *
         * @param player The player
         */
        public void enter(Player player) {
        }

        /**
         * This determines what happens when the player leaves an area.
         * This can be left empty, and only needs to be overwritten if it does something
         *
         * @param player The player
         */
        public void leave(Player player) {
        }

        /**
         * This determines what happens when the player logs out in an area.
         * This can be left empty, and only needs to be overwritten if it does something
         *
         * @param player The player
         */
        public void logout(Player player) {
        }

        /**
         * Gets called every time the player moves. This can be used to for
         * example show an interface.
         *
         * @param player The player
         */
        public void process(Player player) {
        }

        /**
         * Tells whether or not the player can teleport.
         * This can be given checks and other values.
         * This must also provide feedback messages by itself.
         *
         * @param player The player to check
         * @return True when able to teleport, false when not.
         */
        public boolean canTeleport(Player player) {
            return true;
        }

        /**
         * Gets called when the player dies. This can be overwritten to make the
         * player do special actions.
         *
         * @param player The player
         * @return {@link true} when it can ignore the other death events. This will
         * cancel out item loss, and all other normal death sequencing, including teleporting away.
         * {@link false} when it still has to do the other death event.
         */
        public boolean onDeath(Player player) {
            return false;
        }

        /**
         * Gets called when an npc is killed in this area.
         *
         * @param killer The player
         * @param npc    The npc that got killed
         * @return {@link true} when it can stop executing the other npc death code. Usefull for example in bork.
         */
        public boolean handleKilledNPC(Player killer, NPC npc) {
            return false;
        }

        /**
         * @param player The player attacking.
         * @param target The player being attacked.
         * @return Whether the player can attack another player.
         */
        public boolean canAttack(Player player, Player target) {
            return false;
        }

        /**
         * STATIC
         */

        //TODO MAKE THIS MORE EFFICIENT
        public static boolean inLocation(Entity entity, Location location) {
            return location != Location.DEFAULT ? inLocation(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ(), location) : getLocation(entity) == Location.DEFAULT;
        }

        public static Location getLocation(Entity entity) {
            for (Location location : Location.values()) {
                if (location != Location.DEFAULT)
                    if (inLocation(entity, location))
                        return location;
            }
            return Location.DEFAULT;
        }

        public static boolean inLocation(int absX, int absY, int absZ, Location location) {
            if (location.getZ().length != 0) {
                boolean height = false;
                for (int array : location.getZ())
                    if (absZ == array) {
                        height = true;
                        break;
                    }
                if (!height)
                    return false;
            }
            int checks = location.getX().length - 1;
            for (int i = 0; i <= checks; i += 2) {
                if (absX >= location.getX()[i] && absX <= location.getX()[i + 1]) {
                    if (absY >= location.getY()[i] && absY <= location.getY()[i + 1]) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static void process(Entity entity) {
        final Location current = Location.getLocation(entity);
        if (entity.getLocation().equals(current)) {
            if (entity instanceof Player) {
                entity.getLocation().process((Player) entity);
            }
        } else {
            final Location previous = entity.getLocation();
            entity.setLocation(current);
            if (entity instanceof Player) {
                final Player player = (Player) entity;
                player.getActionSender().sendMultiZone(current.isMulti() ? 1 : 0);
                previous.leave(player);
                entity.getLocation().enterArea(player);
            }
        }
    }

    private static int getWildernessLevel(final Player player) {
        final int x = player.getPosition().getX();
        final int y = player.getPosition().getY();
        return (y >= 10340 && y <= 10364 && x <= 3008 && x >= 2992) ? ((y - 10340) / 8) + 3
                : (y >= 3520 && y <= 3967 && x <= 3392 && x >= 2942) ? (((y - 3520) / 8) + 3)
                : (y <= 10349 && x >= 3010 && x <= 3058 && y >= 10306) ? 57
                : (x >= 3064 && x <= 3070 && y >= 10252 && y <= 10260) ? 53
                : (player.getLocation().equals(Location.OSPK_AREA) || player.getLocation().equals(Location.PURE_PK_AREA)) ? 12
                : -1;
    }

    private static boolean validWildernessDifference(final Player player, final Player target) {
        final int combat = player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel() < 0
                ? target.getSkills().getCombatLevel() - player.getSkills().getCombatLevel()
                : player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel();
        final int difference = Math.min(player.wildernessLevel, target.wildernessLevel);
        if (combat <= difference && difference > 0) {
            return true;
        }
        player.sendMessage(difference <= 0 ? "Your opponent is not in the wilderness." : "You need to go deeper into the wilderness to attack this player.");
        return false;
    }

    private static boolean canWildernessTeleport(final Player player) {
        if (player.wildernessLevel > 20 && !Rank.hasAbility(player, Rank.DEVELOPER)) {
            player.sendMessage("You cannot teleport above level 20 wilderness.");
            return false;
        } else if (player.cE.getOpponent() != null && player.wildernessLevel > 0) {
            player.removeEP();
            player.sendMessage("@blu@You have lost EP because you have teleported during combat.");
        }
        return true;
    }

    private static void processWilderness(final Player player) {
        final int level = getWildernessLevel(player);
        if (player.wildernessLevel != level) {
            player.wildernessLevel = level;
            if (level != -1) {
                player.getActionSender().sendWildLevel(player.wildernessLevel);
            }
        }
    }

}
