package org.hyperion.rs2.model;

import org.hyperion.Configuration;
import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.engine.task.impl.WildernessBossTask;
import org.hyperion.rs2.ConnectionHandler;
import org.hyperion.rs2.HostGateway;
import org.hyperion.rs2.LoginResponse;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.container.impl.EquipmentContainerListener;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.model.container.impl.WeaponContainerListener;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.Lock;
import org.hyperion.rs2.model.content.bounty.BountyHunter;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.minigame.Bork;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.minigame.LastManStanding;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.model.itf.impl.PendingRequests;
import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.rs2.model.punishment.Target;
import org.hyperion.rs2.model.punishment.holder.PunishmentHolder;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.sql.impl.log.Log;
import org.hyperion.sql.impl.log.LogManager;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Gilles on 11/02/2016.
 */
public class EntityHandler {

    private static Date LAST_PASS_RESET = createLastPassReset();

    private static Date createLastPassReset() {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse("04-02-2016");
        } catch(ParseException e) {
            return Calendar.getInstance().getTime();
        }
    }

    /**
     * Handler registering of an Entity. Currently only used by Player and NPC
     * @param entity The entity to register
     */
    public static void register(Entity entity) {
        if(entity instanceof Player) {
            register((Player)entity);
        } if(entity instanceof NPC) {
            register((NPC)entity);
        }
    }

    private static void register(Player player) {
        Packet packet = new PacketBuilder().put((byte)LoginResponse.SUCCESSFUL_LOGIN.getReturnCode()).put((byte) Rank.getPrimaryRankIndex(player)).put((byte) 0).toPacket();
        player.getSession().write(packet);
        if(!World.getPlayers().add(player)) {
            player.getSession().close(true);
            return;
        }
        ConnectionHandler.removeIp(player.getShortIP());
        HostGateway.enter(player.getShortIP());
        LogManager.insertLog(Log.ipLog(player));

        System.out.println(String.format("[Registering]: %s @ %s.", player.getSafeDisplayName(), player.getShortIP()));

        /**
         * We send the player his details.
         */
        player.write(new PacketBuilder(249).putByteA(player.isMembers() ? 1 : 0).putLEShortA(player.getIndex()).toPacket());
        player.write(new PacketBuilder(107).toPacket());

        /**
         * Here we actually start loading the player completely
         */
        GameEngine.submitIO(new EngineTask<Boolean>("Fully load player", false) {
            @Override
            public Boolean call() throws Exception {
                return PlayerLoading.loadPlayer(player, PlayerLoading.LoadingType.NON_PRIORITY_ONLY);
            }
        });

        /**
         * A small bit of code to activate the player their active punishments on login.
         */
        final PunishmentHolder holder = PunishmentManager.getInstance().get(player.getName());
        if (holder != null) {
            for (final Punishment p : holder.getPunishments()) {
                p.getCombination().getType().apply(player);
                p.send(player, false);
            }
        } else {
            for (final PunishmentHolder h : PunishmentManager.getInstance().getHolders()) {
                if (player.getName().equalsIgnoreCase(h.getVictimName()))
                    continue;
                h.getPunishments().stream().filter(p -> (p.getCombination().getTarget() == Target.IP && p.getVictimIp().equals(player.getShortIP()))
                        || (p.getCombination().getTarget() == Target.MAC && p.getVictimMac() == player.getUID())
                        || (p.getCombination().getTarget() == Target.SPECIAL && Arrays.equals(p.getVictimSpecialUid(), player.specialUid))).forEach(p -> {
                    p.getCombination().getType().apply(player);
                    p.send(player, false);
                });
            }
        }

        //TODO REMOVE THIS CHECK
        if (LastManStanding.inLMSArea(player.cE.getAbsX(), player.cE.getAbsY())) {
            Magic.teleport(player, Edgeville.POSITION, true);
        }

        /**
         * Now we'll actually log the player in.
         */
        //player.getLogManager().add(LogEntry.login(player));
        player.setActive(true);
        player.isHidden(false);
        player.getPoints().loginCheck();

        player.getActionSender().sendString(38760, player.getSummBar().getAmount() + "");

        if (player.isNew()) {
            player.getInventory().add(Item.create(15707));
            player.sendMessage("@bla@Welcome to @dre@" + Configuration.getString(Configuration.ConfigurationObject.NAME) + ".");
            player.sendMessage("Questions? Visit @whi@::support@bla@ or use the 'Request Help' button.");
            player.sendMessage("Do not forget to @whi@::vote@bla@ and @whi@::donate@bla@ to keep the server alive.");
            player.sendMessage("");
            player.sendImportantMessage("10x decaying PK points boost active for the first 100 minutes!");
        } else {
            if (!player.getInventory().contains(15707) && !player.getBank().contains(15707) && !player.getEquipment().contains(15707))
                player.getInventory().add(Item.create(15707));
            if (player.getTutorialProgress() != 28) {
                if (player.getTutorialProgress() >= 17 && player.getTutorialProgress() <= 20)
                    Magic.teleport(player, Edgeville.POSITION, true, false);
                player.setTutorialProgress(28);
            }
            player.sendMessage("@bla@Welcome back to @dre@" + Configuration.getString(Configuration.ConfigurationObject.NAME) + "@bla@.", "@dre@Current bonus: @bla@2x experience, 1.5x droprates, 2x honor points!");
            /**
             * To remove the bonus;
             * Skills.java#523
             * NpcDeathTask.java#218
             * PlayerPoints.java#245
             */
            if (WildernessBossTask.currentBoss != null) {
                player.sendMessage(WildernessBossTask.currentBoss.getDefinition().getName() + " is somewhere in the wilderness!");
            }
        }

        if (player.getPermExtraData().getLong("passchange") < LAST_PASS_RESET.getTime() && player.getCreatedTime() < LAST_PASS_RESET.getTime()) {
            player.getExtraData().put("cantdoshit", true);
            player.sendMessage("Alert##You MUST change your password!##Please do not use the same password as before!");
            player.setTeleportTarget(Edgeville.POSITION);
            player.getExtraData().put("needpasschange", true);
            InterfaceManager.get(6).show(player);
        }

        if (Rank.getPrimaryRank(player).ordinal() >= Rank.HELPER.ordinal() && !Rank.hasAbility(player, Rank.DEVELOPER))
            World.getPlayers().stream().filter(p -> p != null && !Lock.isEnabled(p, Lock.STAFF_LOGIN) && p != player).forEach(p -> p.sendStaffMessage(Rank.getPrimaryRank(player).toString() + " " + player.getSafeDisplayName() + " has logged in. Feel free to ask him/her for help!"));


        if (Combat.getWildLevel(player.getPosition().getX(), player.getPosition().getY()) > 0) {
            player.getActionSender().sendPlayerOption("Attack", 2, 1);
            player.attackOption = true;
        } else {
            player.getActionSender().sendPlayerOption("null", 2, 1);
        }
        if (!player.getPermExtraData().getBoolean("tradeoption"))
            player.getActionSender().sendPlayerOption("Trade", 4, 0);
        if (!player.getPermExtraData().getBoolean("followoption"))
            player.getActionSender().sendPlayerOption("Follow", 3, 0);
        if (!player.getPermExtraData().getBoolean("profileoption"))
            player.getActionSender().sendPlayerOption("View profile", 6, 0);
        if (player.getPosition().getX() >= 3353
                && player.getPosition().getY() >= 3264
                && player.getPosition().getX() <= 3385
                && player.getPosition().getY() <= 3283) {
            player.getActionSender().sendPlayerOption("Challenge", 5, 0);
            player.duelOption = true;
        } else {
            if (Rank.hasAbility(player, Rank.MODERATOR) && !player.getPosition().inDuel())
                player.getActionSender().sendPlayerOption("Moderate", 5, 0);
            else
                player.getActionSender().sendPlayerOption("null", 5, 0);
        }
        player.getActionSender().sendSidebarInterfaces();
        if (player.getSpellBook().isAncient()) {
            player.getActionSender().sendSidebarInterface(6, 12855);
        } else if (player.getSpellBook().isRegular()) {
            player.getActionSender().sendSidebarInterface(6, 1151);
        } else if (player.getSpellBook().isLunars()) {
            player.getActionSender().sendSidebarInterface(6, 29999);
        }
        if (!player.getPrayers().isDefaultPrayerbook()) {
            player.getActionSender().sendSidebarInterface(5, 22500);
        } else {
            player.getActionSender().sendSidebarInterface(5, 5608);
        }
        player.getWalkingQueue().setRunningToggled(true);
        player.getActionSender().sendMapRegion();
        InterfaceContainerListener interfacecontainerlistener = new InterfaceContainerListener(
                player, 3214);
        player.getInventory().addListener(interfacecontainerlistener);
        player.getSpecBar().sendSpecBar();
        player.getSpecBar().sendSpecAmount();

        player.getActionSender().sendClientConfig(115, 0);// rests bank noting
        InterfaceContainerListener interfacecontainerlistener1 = new InterfaceContainerListener(
                player, 1688);
        player.getEquipment().addListener(interfacecontainerlistener1);
        player.getEquipment().addListener(
                new EquipmentContainerListener(player));
        player.getEquipment().addListener(new WeaponContainerListener(player));
        ActionSender.sendClientConfigs(player);

        player.startUpEvents();
        if (player.fightCavesWave > 0) {
            ContentManager.handlePacket(6, player, 9358, player.fightCavesWave, 1, 1);
        }
        if (player.isNew()) {
            DialogueManager.openDialogue(player, 10000);
        }
        player.getActionSender().sendSkills();
        player.getSpawnTab().createSpawnTab();

        for (int i = 0; i < 7; i++) {
            if (player.getSkills().getLevel(i) >= 119 && i != 3 && i != 5)
                player.getSkills().setLevel(i, 99);
        }

        player.checkCapes();
        /*Checks if a player has (class #) Sacred Clay Items Until smuggles are fixed*/
        player.checkSacredClay();

        if (Rank.isStaffMember(player))
            player.getInterfaceManager().show(PendingRequests.ID);

        player.getGrandExchangeTracker().notifyChanges(false);
        player.getAchievementTracker().load();

        Locations.login(player);

        TaskManager.submit(new Task(Time.FIVE_SECONDS, player) {
            @Override
            protected void execute() {
                player.getActionSender().sendString(1300, "City Teleport");
                player.getActionSender().sendString(1301, "Teleports you to any city.");
                player.getActionSender().sendString(1325, "Training Teleports");
                player.getActionSender().sendString(1326, "Teleports you to training spots.");
                player.getActionSender().sendString(1350, "Minigame Teleport");
                player.getActionSender().sendString(1351, "Teleports you to any minigame.");
                player.getActionSender().sendString(1382, "PK Teleport");
                player.getActionSender().sendString(1383, "Teleports you to the wilderness.");
                player.getActionSender().sendString(1415, "Boss Teleport");
                player.getActionSender().sendString(1416, "Teleports you to dungeons.");

                player.getActionSender().sendString(13037, "City Teleport");
                player.getActionSender().sendString(13038, "Teleports you to any city.");
                player.getActionSender().sendString(13047, "Training Teleports");
                player.getActionSender().sendString(13048, "Teleports you to training spots.");
                player.getActionSender().sendString(13055, "Minigame Teleport");
                player.getActionSender().sendString(13056, "Teleports you to any minigame.");
                player.getActionSender().sendString(13063, "PK Teleport");
                player.getActionSender().sendString(13064, "Teleports you to the wilderness.");
                player.getActionSender().sendString(13071, "Boss Teleport");
                player.getActionSender().sendString(13072, "Teleports you to a dungeon.");

                player.getActionSender().sendString(30067, "City Teleport"); // Needed
                player.getActionSender().sendString(30068, "Teleports you to any city.");

                player.getActionSender().sendString(30109, "Training Teleports"); // Needed
                player.getActionSender().sendString(30110, "Teleports you to training spots.");

                player.getActionSender().sendString(30078, "Minigame Teleport"); // Needed
                player.getActionSender().sendString(30079, "Teleports players to any minigame.");

                player.getActionSender().sendString(30083 + 3, "Boss Teleport"); // Needed
                player.getActionSender().sendString(30083 + 4, "Teleports you to a dungeon.");

                player.getActionSender().sendString(30117, "Player Killing Teleport"); // Needed
                player.getActionSender().sendString(30118, "Teleports you to the wilderness.");
            }
        });
    }

    private static void register(NPC npc) {
        World.getNpcs().add(npc);
    }

    /**
     * Handler deregistering of an Entity. Currently only used by Player and NPC
     * @param entity The entity to deregister
     */
    public static boolean deregister(Entity entity) {
        if (entity instanceof Player) {
            final Player player = entity.cE.getPlayer();
            if (!canDeregister(player) && !player.forcedLogout) {
                TaskManager.submit(new Task(10000L, String.format("%s Logout Task", player.getName())) {
                    int count;
                    @Override
                    public void execute() {
                        if ((count >= 6 && !player.isDead() && player.getSkills().getLevel(Skills.HITPOINTS) >= 1)
                                || canDeregister(player)) {
                            player.forcedLogout = true;
                            stop();
                        }
                        count++;
                    }
                });
                return false;
            }
            return deregister(player);
        }
        TaskManager.cancelTasks(entity);
        return entity instanceof NPC && deregister((NPC) entity);
    }

    private static boolean canDeregister(final Player player) {
        return !((System.currentTimeMillis() - player.cE.lastHit <= 9999)
                || player.getSkills().getLevel(Skills.HITPOINTS) <= 0
                || player.isDead());
    }

    private static boolean deregister(Player player) {
        TaskManager.cancelTasks(player);
        if(!World.getPlayers().remove(player))
            return !World.getPlayers().contains(player);
        System.out.println(String.format("[Deregistering]: %s @ %s.", player.getSafeDisplayName(), player.getShortIP()));
        player.getSkills().stopSkilling();
        Locations.logout(player);
        Combat.logoutReset(player.cE);
        player.getDungeoneering().fireOnLogout(player);
        player.setActive(false);
        LastManStanding.getLastManStanding().leaveGame(player, true);
        Bork.doDeath(player);
        World.resetPlayersNpcs(player);
        World.resetSummoningNpcs(player);
        player.getPermExtraData().put("logintime", player.getPermExtraData().getLong("logintime") + (System.currentTimeMillis() - player.getLogintime()));
        player.getTicketHolder().fireOnLogout();

        try {
            ClanManager.leaveChat(player, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (player.duelAttackable <= 0) {
            Duel.declineTrade(player);
        } else {
            Duel.finishFullyDuel(player);
            player.setPosition(Position.create(3360 + Combat.random(17),
                    3274 + Combat.random(3), 0));
        }
        if (LastManStanding.getLastManStanding().gameStarted && LastManStanding.inLMSArea(player.cE.getAbsX(), player.cE.getAbsY())) {
            LastManStanding.getLastManStanding().leaveGame(player, true);
        }
        Trade.declineTrade(player);
        FightPits.removePlayerFromGame(player, false);
        BountyHunter.fireLogout(player);
        FriendsAssistant.refreshGlobalList(player, true);

        player.getActionQueue().cancelQueuedActions();
        player.getInterfaceState().resetContainers();
        player.isHidden(true);
        HostGateway.exit(player.getShortIP());
        GameEngine.submitIO(new EngineTask("Saving player " + player.getName() + " on logout", false) {
            @Override
            public Boolean call() throws Exception {
                PlayerSaving.setSaving(player);
                World.getLoader().savePlayer(player);
                player.destroy();
                return true;
            }
        });
        return true;
    }

    private static boolean deregister(NPC npc) {
        return World.getNpcs().remove(npc);
    }
}
