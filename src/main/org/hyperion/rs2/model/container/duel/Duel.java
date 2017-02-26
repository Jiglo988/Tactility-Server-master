package org.hyperion.rs2.model.container.duel;

import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.impl.OverloadStatsTask;
import org.hyperion.rs2.logging.FileLogging;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.achievements.AchievementHandler;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatEntity;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.duel.DuelRule.DuelRules;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.rs2.util.NameUtils;

// Referenced classes of package org.hyperion.rs2.model.container:
//            Container

public class Duel {

    public static final int SIZE = 28;
    public static final int PLAYER_INVENTORY_INTERFACE = 3322;
    public static final int DUEL_INVENTORY_INTERFACE = 6669;
    public static final int DUEL_INVENTORY_INTERFACE2 = 6670;
    public static String rulesOption[] = new String[9];
    public static final int DUEL_RULE_ID[] = {
            1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096,
            8192, 16384, 32768, 0x10000, 0x20000, 0x40000, 0x80000, 0x200000, 0x800000, 0x1000000,
            0x4000000, 0x8000000
    };

    public Duel() {
    }

    public static void open(Player player, Player opponent) {
        if (player.getSkills().getLevel(3) <= 1) {
            player.sendMessage("You cannot duel players when you have low health.");
            return;
        }
        if (player.duelAttackable > 0)
            return;
        if (!player.getPosition().isWithinDistance(opponent.getPosition(), 3)) {
            player.getActionSender().sendMessage("You are too far away to open a duel.");
            return;
        }
        if (Server.isUpdating()) {
            player.getActionSender().sendMessage("You can't duel during an update.");
            return;
        }
        /*if(player.getUID() == opponent.getUID()){
            player.sendf("You cannot duel yourself!");
            return;
        }*/
        player.setBusy(true);
        opponent.setBusy(true);
        player.currentInterfaceStatus = 2;
        opponent.currentInterfaceStatus = 2;
        player.duelRuleOption = 0;
        opponent.duelRuleOption = 0;
        for (int i = 0; i < 22; i++) {
            player.duelRule[i] = false;
            opponent.duelRule[i] = false;
            if (i < 14) {
                player.banEquip[i] = false;
                opponent.banEquip[i] = false;
            }
        }

        for (int j = 0; j < 11; j++) {
            player.getActionSender().sendUpdateItem(13824, j, opponent.getEquipment().get(j));
            opponent.getActionSender().sendUpdateItem(13824, j, player.getEquipment().get(j));
        }

        player.getActionSender().sendClientConfig(286, player.duelRuleOption);
        opponent.getActionSender().sendClientConfig(286, opponent.duelRuleOption);
        player.tradeWith2 = null;
        opponent.tradeWith2 = null;
        player.tradeAccept1 = false;
        player.tradeAccept2 = false;
        player.onConfirmScreen = false;
        opponent.onConfirmScreen = false;
        opponent.tradeAccept1 = false;
        opponent.tradeAccept2 = false;
        player.getActionSender().sendString(669, "No Switching");
        opponent.getActionSender().sendString(669, "No Switching");
        player.cannotSwitch = false;
        opponent.cannotSwitch = false;
        player.getActionSender().sendInterfaceInventory(6575, 3321);
        opponent.getActionSender().sendInterfaceInventory(6575, 3321);
        player.getActionSender().sendUpdateItems(3322, player.getInventory().toArray());
        player.getActionSender().sendUpdateItems(6669, player.getDuel().toArray());
        player.getActionSender().sendUpdateItems(6670, opponent.getDuel().toArray());
        opponent.getActionSender().sendUpdateItems(3322, player.getInventory().toArray());
        opponent.getActionSender().sendUpdateItems(6669, player.getDuel().toArray());
        opponent.getActionSender().sendUpdateItems(6670, opponent.getDuel().toArray());
        player.getInterfaceState().addListener(player.getDuel(), new InterfaceContainerListener(player, 6669));
        player.getInterfaceState().addListener(player.getDuel(), new InterfaceContainerListener(opponent, 6670));
        player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, 3322));
        opponent.getInterfaceState().addListener(opponent.getDuel(), new InterfaceContainerListener(opponent, 6669));
        opponent.getInterfaceState().addListener(opponent.getDuel(), new InterfaceContainerListener(player, 6670));
        opponent.getInterfaceState().addListener(opponent.getInventory(), new InterfaceContainerListener(opponent, 3322));
        player.getActionSender().sendString(6684, "Are you sure you want to make this duel?");
        opponent.getActionSender().sendString(6684, "Are you sure you want to make this duel?");
        opponent.getActionSender().sendString(6671, "Dueling with: " + player.getSafeDisplayName() + "           Opponent's Equipment");
        player.getActionSender().sendString(6671, "Dueling with: " + opponent.getSafeDisplayName() + "           Opponent's Equipment");
        player.setTradeWith(opponent);
        opponent.setTradeWith(player);
    }

    public static void withdraw(Player player, int slot, int id, int amount) {
        if (player.getTrader() == null)
            return;
        if (player.tradeAccept1 && player.getTrader().tradeAccept1)
            return;
        Item item = player.getDuel().get(slot);
        if (item == null) {
            return; // invalid packet, or client out of sync
        }
        if (item.getId() != id) {
            return; // invalid packet, or client out of sync
        }
        player.openingTrade = false;
        player.getTrader().openingTrade = false;
        int transferAmount = item.getCount();
        if (transferAmount >= amount) {
            transferAmount = amount;
        } else if (transferAmount == 0) {
            return; // invalid packet, or client out of sync
        }
        int newId = item.getId(); // TODO deal with withdraw as notes!
        ItemDefinition def = ItemDefinition.forId(newId);
        if (def.isStackable()) {
            if (player.getInventory().freeSlots() <= 0 && player.getInventory().getById(newId) == null) {
                player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
            }
        } else {
            int free = player.getInventory().freeSlots();
            if (transferAmount > free) {
                player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
                transferAmount = free;
            }
        }
        // now add it to inv
        if (player.getInventory().add(new Item(newId, transferAmount))) {
            // all items in the bank are stacked, makes it very easy!
            int newAmount = item.getCount() - transferAmount;
            if (newAmount <= 0) {
                player.getDuel().set(slot, null);
            } else {
                player.getDuel().set(slot, new Item(item.getId(), newAmount));
            }
            player.getTrader().getActionSender().sendUpdateItems(3416, player.getDuel().toArray());
            player.tradeAccept1 = false;
            player.tradeAccept2 = false;
            player.getTrader().tradeAccept1 = false;
            player.getTrader().tradeAccept2 = false;
            //World.getAbuseHandler().cacheMessage(player,player.getName()+": removed: "+newId+":"+transferAmount+" from trade.");
            player.getTrader().getActionSender().sendString(3431, "Are you sure you want to make this trade?");
            player.getActionSender().sendString(3431, "Are you sure you want to make this trade?");
        } else {
            player.getActionSender().sendMessage("You don't have enough inventory space to withdraw that many."); // this is the real message
        }
    }


    /**
     * Deposits an item.
     *
     * @param player The player.
     * @param slot   The slot in the player's inventory.
     * @param id     The item id.
     * @param amount The amount of the item to deposit.
     */
    public static void deposit(Player player, int slot, int id, int amount) {
        /*
        if(player.getExtraData().getBoolean("cantdoshit")) {
            player.sendMessage("Please PM a moderator as your account is locked for its own safety!");
            return;
        }
        */
        if (player.tradeAccept1 && player.getTrader() != null && player.getTrader().tradeAccept1)
            return;
        if (player.getTrader() == null || player.getGameMode() != player.getTrader().getGameMode()) {
            player.sendMessage("You cannot stake when you are in separate game modes");
            return;
        }

        if ((player.getGameMode() == 1) && (player.getUID() == player.getTrader().getUID() || player.getShortIP().equalsIgnoreCase(player.getTrader().getShortIP()))) {
            player.sendMessage("You cannot stake with this person");
            return;
        }

        if (player.getTrader() != null && player.isNewlyCreated() && player.hardMode() || player.getTrader().isNewlyCreated() && player.getTrader().hardMode()) {
            player.sendMessage("You or your partner is too new to stake");
            return;
        }
        if (!ItemsTradeable.isTradeable2(id, player.getGameMode())) {
            player.getActionSender().sendMessage("You cannot stake this item.");
            return;
        }
        boolean inventoryFiringEvents = player.getInventory().isFiringEvents();
        player.getInventory().setFiringEvents(false);
        try {
            Item item = player.getInventory().get(slot);
            if (item == null) {
                return; // invalid packet, or client out of sync
            }
            if (item.getId() != id) {
                return; // invalid packet, or client out of sync
            }
            player.openingTrade = false;
            player.getTrader().openingTrade = false;
            int transferAmount = item.getCount();
            if (!item.getDefinition().isStackable())
                transferAmount = player.getInventory().getCount(id);
            if (transferAmount >= amount) {
                transferAmount = amount;
            } else if (transferAmount == 0) {
                return; // invalid packet, or client out of sync
            }
            boolean noted = item.getDefinition().isNoted();
            if (item.getDefinition().isStackable() || noted) {
                int bankedId = item.getId();
                if (player.getDuel().freeSlots() < 1 && player.getDuel().getById(bankedId) == null) {
                    player.getActionSender().sendMessage("You don't have enough space."); // this is the real message
                }
                // we only need to remove from one stack
                int newInventoryAmount = item.getCount() - transferAmount;
                Item newItem;
                if (newInventoryAmount <= 0) {
                    newItem = null;
                } else {
                    newItem = new Item(item.getId(), newInventoryAmount);
                }
                if (!player.getDuel().add(new Item(bankedId, transferAmount))) {
                    player.getActionSender().sendMessage("You don't have enough space."); // this is the real message
                } else {
                    player.getInventory().set(slot, newItem);
                    player.getInventory().fireItemsChanged();
                    player.getDuel().fireItemsChanged();
                }
            } else {
                if (player.getDuel().freeSlots() < transferAmount) {
                    player.getActionSender().sendMessage("You don't have enough space."); // this is the real message
                }
                if (!player.getDuel().add(new Item(item.getId(), transferAmount))) {
                    player.getActionSender().sendMessage("You don't have enough space."); // this is the real message
                } else {
                    // we need to remove multiple items
                    for (int i = 0; i < transferAmount; i++) {
	                    /* if(i == 0) {
							player.getInventory().set(slot, null);
						} else { */
                        player.getInventory().set(player.getInventory().getSlotById(item.getId()), null);
                        // }
                    }
                    player.getInventory().fireItemsChanged();
                }
            }
        } finally {
            //World.getAbuseHandler().cacheMessage(player,player.getName()+": added: "+id+":"+amount+" to trade.");
            player.getInventory().setFiringEvents(inventoryFiringEvents);
            if (player.getTrader() == null || player.getDuel() == null) {
                System.out.println("MARTIN YOU SHOULD FIX THIS LUL");
                return;
            }
            player.getTrader().getActionSender().sendUpdateItems(3416, player.getDuel().toArray());
            player.tradeAccept1 = false;
            player.tradeAccept2 = false;
            player.getTrader().tradeAccept1 = false;
            player.getTrader().tradeAccept2 = false;
            player.getTrader().getActionSender().sendString(6684, "Are you sure you want to make this trade?");
            player.getActionSender().sendString(6684, "Are you sure you want to make this trade?");
        }
    }

    public static String listConfirmScreen(Item[] items) {
        String sendTrade = "Absolutely nothing!";
        String sendAmount = "";
        int count = 0;
        for (Item item : items) {
            if (item == null)
                continue;
            if (item.getId() > 0) {
                if ((item.getCount() >= 1000) && (item.getCount() < 1000000)) {
                    sendAmount = "@cya@" + (item.getCount() / 1000) + "K @whi@("
                            + NameUtils.formatInt(item.getCount()) + ")";
                } else if (item.getCount() >= 1000000) {
                    sendAmount = "@gre@" + (item.getCount() / 1000000)
                            + " million @whi@(" + NameUtils.formatInt(item.getCount())
                            + ")";
                } else {
                    sendAmount = "" + NameUtils.formatInt(item.getCount());
                }
                if (count == 0) {
                    sendTrade = "";
                    count = 2;
                }
                if (count == 1) {
                    sendTrade = sendTrade + "\\n" + item.getDefinition().getName();
                } else if (count == 2) {
                    sendTrade = sendTrade + " " + item.getDefinition().getName();
                    count = 0;
                }
                if (item.getDefinition().isStackable()) {
                    sendTrade = sendTrade + " x " + sendAmount;
                }
                sendTrade = sendTrade + "     ";
                count++;
            }
        }
        return sendTrade;
    }

    public static void confirmScreen(Player player) {
        if (player.duelAttackable > 0)
            return;
        player.getActionSender().sendString(8250, "Hitpoints will be restored.");
        player.getActionSender().sendString(8238, "Boosted stats will be restored.");
        player.getActionSender().sendString(8240, "");
        player.getActionSender().sendString(8241, "");
        String as[] = {
                "Players cannot forfeit!", "Players cannot move.", "Players cannot use range.", "Players cannot use melee.", "Players cannot use magic.", "Players cannot drink pots.", "Players cannot eat food.", "Players cannot use prayer."
        };
        int i = 8242;
        for (int clear = 0; clear <= 10; clear++) {
            player.getActionSender().sendString(i + clear, "");
            player.getTrader().getActionSender().sendString(i + clear, "");
        }
        for (int j = 0; j <= 10; j++) {
            if (player.duelRule[j]) {
                player.getActionSender().sendString(i, DuelRule.forId(j).getMessage());
                player.getTrader().getActionSender().sendString(i, DuelRule.forId(j).getMessage());
                i++;
            }
        }

        for (int k = 1; k < 5; ) {
            player.getActionSender().sendString(i, "");
            player.getTrader().getActionSender().sendString(i, "");
            k++;
            i++;
        }

        player.getTrader().getActionSender().sendString(8250, "Hitpoints will be restored.");
        player.getTrader().getActionSender().sendString(8238, "Boosted stats will be restored.");
        player.getTrader().getActionSender().sendString(8240, "");
        player.getTrader().getActionSender().sendString(8241, "");
        player.getActionSender().sendString(6571, "Are you sure you want to accept this duel?");
        player.getTrader().getActionSender().sendString(6571, "Are you sure you want to accept this duel?");
        String s = listConfirmScreen(player
                .getDuel().toArray());
        String s1 = listConfirmScreen(player.getTrader()
                .getDuel().toArray());
        player.getActionSender().removeAllInterfaces();
        player.getTrader().getActionSender().removeAllInterfaces();
        player.getActionSender().sendString(6516, s);
        player.getTrader().getActionSender().sendString(6516, s1);
        player.getActionSender().sendString(6517, s1);
        player.getTrader().getActionSender().sendString(6517, s);
        player.getActionSender().sendInterfaceInventory(6412, 197);
        player.getActionSender().sendUpdateItems(3214,
                player.getInventory().toArray());
        player.getTrader().getActionSender().sendInterfaceInventory(6412, 197);
        player.getTrader().getActionSender().sendUpdateItems(3214,
                player.getTrader().
                        getInventory().toArray());
        player.onConfirmScreen = true;
        player.getTrader().onConfirmScreen = true;
    }

    public static int getItemsRemovedCount(Player player) {
        int i = 0;
        for (int j = 0; j < 14; j++) {
            if (player.banEquip[j] && player.getEquipment().get(j) != null) {
                i++;
            }
        }

        return i;
    }

    public static void finishTrade(Player player) {
        if (player.getTrader() == null)
            return;
        if (Server.isUpdating()) {
            player.getActionSender().sendMessage("You can't duel during an update.");
            return;
        }
        if (player.getInventory().freeSlots() < player.getDuel().size() + player.getTrader().getDuel().size() + getItemsRemovedCount(player)) {
            player.getActionSender().sendMessage("You don't have enough space to for this duel.");
            player.getTrader().getActionSender().sendMessage("The other player doesn't have enough space for this duel.");
            return;
        }
        if (player.getTrader().getInventory().freeSlots() < player.getDuel().size() + player.getTrader().getDuel().size() + getItemsRemovedCount(player.getTrader())) {
            player.getTrader().getActionSender().sendMessage("You don't have enough space to for this duel.");
            player.getActionSender().sendMessage("The other player doesn't have enough space for this duel.");
            return;
        }
        if (player.getEquipment().getItemId(Equipment.SLOT_WEAPON) != player.getTrader().getEquipment().getItemId(Equipment.SLOT_WEAPON) && player.duelRule[DuelRules.SWITCH.ordinal()]) {
            player.sendMessage("You cannot accept a duel with a different weapon than your opponent");
            return;
        }
        if (player.tradeAccept1 && player.getTrader().tradeAccept1 && !player.tradeAccept2 && !player.getTrader().tradeAccept2) {
            confirmScreen(player);
        }
        if (!player.tradeAccept1 || !player.getTrader().tradeAccept1 || !player.tradeAccept2 || !player.getTrader().tradeAccept2) {
            char c = '\u19AB';
            if (!player.onConfirmScreen && !player.getTrader().onConfirmScreen) {
                c = '\u1A1C';
            }
            if ((player.tradeAccept1 && !player.getTrader().tradeAccept1) || (player.tradeAccept2 && !player.getTrader().tradeAccept2)) {
                player.getActionSender().sendString(c, "Waiting on the other player.");
                player.getTrader().getActionSender().sendString(c, "Other player has accepted.");
            } else if ((!player.tradeAccept1 && player.getTrader().tradeAccept1) || (!player.tradeAccept2 && player.getTrader().tradeAccept2)) {
                player.getTrader().getActionSender().sendString(c, "Waiting on the other player.");
                player.getActionSender().sendString(c, "Other player has accepted.");
            }
            return;
        } else {
            player.getActionSender().removeAllInterfaces();
            player.getTrader().getActionSender().removeAllInterfaces();
            startDueling(player);
            return;
        }
    }

    public static void declineTrade(Player player) {
        player.onConfirmScreen = false;
        //player.cannotSwitch = false;
        //player.debugMessage("declined trade");
        //System.out.println("decline: "+player.getName());
        if (player.getTrader() != null && player.getTrader().getTrader() != null && player.getTrader().getTrader().equals(player)) {
            //World.getAbuseHandler().cacheMessage(player, (new StringBuilder()).append(player.getName()).append(": declined a trade with: ").append(player.getTrader().getName()).toString());
            if (player.duelAttackable == 0) {
                player.getTrader().duelAttackable = 0;
                player.getTrader().duelWith2 = null;
                Container.transfer(player.getTrader().getDuel(), player.getTrader().getInventory());
                player.getTrader().setTradeWith(null);
                player.getTrader().setBusy(false);
            }
            player.getTrader().getActionSender().removeAllInterfaces();
            player.getActionSender().removeAllInterfaces();
        }
        if (player.duelAttackable == 0) {
            player.duelAttackable = 0;
            player.setTradeWith(null);
            player.duelWith2 = null;
            player.getInventory();
            Container.transfer(player.getDuel(), player.getInventory());
            player.setBusy(false);
        }
    }

    public static void healup(final Player player) {
        for (int i = 0; i < Skills.SKILL_NAME.length; i++)
            player.getSkills().setLevel(i, player.getSkills().getLevelForExp(i));
        player.getSpecBar().setAmount(SpecialBar.FULL);
        player.specOn = false;
        player.getCombat().morrigansLeft = 0;
        player.getActionSender().resetFollow();
        player.getSpecBar().sendSpecAmount();
        player.getSpecBar().sendSpecBar();
    }

    public static void startDueling(final Player player) {
        if (!player.getPosition().isWithinDistance(player.getTrader().getPosition(), 10))
            return;
        if (!player.onConfirmScreen)
            return;
        if (player.duelAttackable > 0)
            return;
        player.setOverloaded(false);
        player.getTrader().setOverloaded(false);
        player.getExtraData().remove(OverloadStatsTask.KEY);
        player.getTrader().getExtraData().remove(OverloadStatsTask.KEY);
        player.overloadTimer = 0;
        player.getTrader().overloadTimer = 0;
        for (int i = 0; i < 6; i++) {
            player.getSkills().normalizeLevel(i);
            player.getTrader().getSkills().normalizeLevel(i);
        }

        player.resetPrayers();
        player.getTrader().resetPrayers();
        player.duelAttackable = 3001;
        player.getTrader().duelAttackable = 3001;
        healup(player);
        healup(player.getTrader());
        player.SummoningCounter = 0;
        player.getTrader().SummoningCounter = 0;
        player.getActionSender().sendPlayerOption("null", 4, 0);
        player.getTrader().getActionSender().sendPlayerOption("null", 4, 0);
        player.onConfirmScreen = false;
        player.vengeance = false;
        player.getTrader().vengeance = false;
        teleportToArena(player);
        player.getWalkingQueue().finish();
        player.getWalkingQueue().reset();
        removeBanEquip(player);
        removeBanEquip(player.getTrader());
        World.submit(new Task(1000) {

            int timer = 3;

            @Override
            public void execute() {
                player.forceMessage((new StringBuilder()).append("").append(timer).toString());
                if (player.getTrade() == null) {
                    this.stop();
                    return;
                }
                if (player.getTrader() == null) {
                    this.stop();
                    return;
                }
                player.getTrader().forceMessage((new StringBuilder()).append("").append(timer).toString());
                player.getWalkingQueue().finish();
                player.getWalkingQueue().reset();
                player.getTrader().getWalkingQueue().finish();
                player.getTrader().getWalkingQueue().reset();
                if (timer == 0) {
                    player.forceMessage("FIGHT!");
                    player.getTrader().forceMessage("FIGHT!");
                    player.duelAttackable = player.getTrader().getIndex();
                    player.getTrader().duelAttackable = player.getIndex();
                    stop();
                }
                timer--;
            }
        });
    }

    public static void removeBanEquip(Player player) {
        boolean removed = true;
        for (int i = 0; i < 14; i++) {
            if (player.banEquip[i] && player.getEquipment().get(i) != null) {
                if (!Container.transfer(player.getEquipment(), player.getInventory(), i, player.getEquipment().get(i).getId()))
                    player.getEquipment().set(i, null);
            }
        }
    }

    public static void finishDuel(Player player, Player opponent, boolean won) {
        player.setDead(false);
        player.cE.setPoisoned(false);
        player.cannotSwitch = false;
        player.getCombat().morrigansLeft = 0;
        if (won) {
            player.getActionSender().sendUpdateItems(6822, player.getDuel().toArray());
            player.getActionSender().sendString(6839, (new StringBuilder()).append("").append(player.getSkills().getCombatLevel()).toString());
            player.getActionSender().sendString(6840, (new StringBuilder()).append("").append(player.getName()).toString());

            player.getInventory();
            player.getExpectedValues().stake(opponent.getDuel().getItems(), true);
            opponent.getExpectedValues().stake(opponent.getDuel().getItems(), false);
            Container.transfer(player.getDuel(), player.getInventory());//jet is a dumbass

            player.getInventory();
            Container.transfer(opponent.getDuel(), player.getInventory());
            AchievementHandler.progressAchievement(player, "Duel");
        }
        healup(player);
        player.setTeleportTarget(Position.create(3360 + Combat.random(17), 3274 + Combat.random(3), 0), false);
        Combat.resetAttack(player.cE);
        player.playAnimation(Animation.create(-1));
        player.getActionSender().sendMessage("You have " + (won ? "won" : "lost") + " the duel.");
        player.getActionSender().sendPlayerOption("Trade", 4, 0);
        FileLogging.savePlayerLog(opponent, "Duel " + (won ? "Won" : "Lost") + " against " + player.getName());
        player.tradeAccept2 = false;
        player.duelAttackable = 0;
        PlayerSaving.save(player);

        if (won)
            player.getActionSender().showInterface(6733);
        else
            declineTrade(player);
    }

    public static void finishFullyDuel(final Player player) {
        Player target = player.getTrader();
        if (target != null) {
            finishDuel(target, player, true);
            finishDuel(player, target, false);
        } else {
            player.getDuel().clear();
        }
    }

    public static void selectRule(Player player, int i, boolean flag, int j) {
        if (player == null || player.getTrader() == null)
            return;
        if (player.duelAttackable > 0 || player.onConfirmScreen)
            return;
        if (!player.equals(player.getTrader().getTrader())) {
            player.getActionSender().sendMessage("Anti-bug has stopped you!");
            return;
        }
        final DuelRules rule = DuelRule.forId(i);
        if (flag) {
            selectRule(player.getTrader(), i, false, j);
        }
        player.debugMessage("I was: " + i);
        if (i == 9) {
            player.cannotSwitch = !player.cannotSwitch;
            player.debugMessage("you now cannotswitch: " + player.cannotSwitch);
        }
        if (i >= 11) {
            player.banEquip[j] = !player.duelRule[i];
        }
        if (!player.duelRule[i]) {
            player.duelRule[i] = true;
            if (rule != null)
                player.duelRuleOption |= DuelRule.forId(i).getFlag();
            else player.duelRuleOption |= DUEL_RULE_ID[i];
        } else {
            player.duelRule[i] = false;
            if (rule != null)
                player.duelRuleOption &= ~DuelRule.forId(i).getFlag();
            else player.duelRuleOption &= ~DUEL_RULE_ID[i];
        }
        player.getActionSender().sendClientConfig(286, player.duelRuleOption);
        player.tradeAccept1 = false;
        player.tradeAccept2 = false;
        player.getActionSender().sendString(6684, "Are you sure you want to accept this duel?");
    }

    public static void teleportToArena(Player player) {
        int x1 = 3336 + Combat.random(12);
        int y1 = 3247 + Combat.random(6);
        int x2 = 3336 + Combat.random(12);
        int y2 = 3247 + Combat.random(6);
        if (player.duelRule[1]) {
            x2 = x1 - 1;
            y2 = y1;
        }
        if (player.duelRule[8]) {
            y1 -= 19;
            y2 -= 19;
        }
        player.setTeleportTarget(Position.create(x1, y1, 0), false);
        player.getTrader().setTeleportTarget(Position.create(x2, y2, 0), false);
    }

}
