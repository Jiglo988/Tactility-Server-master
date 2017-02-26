package org.hyperion.rs2.model.content.minigame;
// Yay

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.combat.attack.Barrows;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;

public class Barrows3 implements ContentTemplate {

    private static final int DIGGING_EMOTE = 0x33f;

    public static final String KILLSCOUNT_KEY = "barrowskillcount";

    public static final String BROTHERS_KILLED_KEY = "brotherskilled";

    public static final String BROTHER_TARGET = "targetbarrows";

    public static final int[] REWARDS = {
        /*ahrim*/4708, 4710, 4712, 4714,
        /*dharok*/4716, 4718, 4720, 4722,
		/*verac*/4724, 4726, 4728, 4730,
		/*guthan*/4732, 4734, 4736, 4738,
		/*karil*/4745, 4747, 4749, 4751,
		/*torag*/4753, 4755, 4757, 4759,};

    public static final int[] BONUS_REWARDS = {
            4740, 4694, 4695, 4696, 4697, 4698, 4699,};

    public static int boolArrayToInt(boolean[] array) {
        int total = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                total += Math.pow(2, i);
            }
        }
        return total;
    }

    public static boolean[] intToBoolArray(int integer, int size) {
        boolean[] array = new boolean[size];
        for (int i = size - 1; i >= 0; i--) {
            int idxvalue = (int) Math.pow(2, i);
            if (integer >= idxvalue) {
                array[i] = true;
                integer -= idxvalue;
            }
        }
        return array;
    }

    public void dig(final Player player) {
        if (ContentEntity.isInArea(player, 3550, 3269, 3580, 3305)) {
            ContentEntity.startAnimation(player, DIGGING_EMOTE);
            World.submit(new Task(1000, "dig") {
                @Override
                public void execute() {
                    ContentEntity.startAnimation(player, -1);
                    boolean entered = false;
                    if (ContentEntity.isInArea(player, 3561, 3285, 3567, 3290)) {//ahrim
                        ContentEntity.teleport(player, 3557, 9703, 3);
                        entered = true;
                    }
                    if (ContentEntity.isInArea(player, 3553, 3294, 3559, 3299)) {//verac
                        ContentEntity.teleport(player, 3556, 9718, 3);
                        entered = true;
                    }
                    if (ContentEntity.isInArea(player, 3575, 3280, 3579, 3284)) {//guthan
                        ContentEntity.teleport(player, 3568, 9683, 3);
                        entered = true;
                    }
                    if (ContentEntity.isInArea(player, 3562, 3273, 3567, 3277)) {//karil
                        ContentEntity.teleport(player, 3546, 9684, 3);
                        entered = true;
                    }
                    if (ContentEntity.isInArea(player, 3572, 3294, 3577, 3300)) {//dharok
                        ContentEntity.teleport(player, 3578, 9706, 3);
                        entered = true;
                    }
                    if (ContentEntity.isInArea(player, 3551, 3280, 3556, 3285)) {//torag
                        ContentEntity.teleport(player, 3534, 9704, 3);
                        entered = true;
                    }
                    if (entered) {
                        ContentEntity.sendString(player, "Killcount: " + player.getExtraData().getInt(KILLSCOUNT_KEY), 4536);
                        ContentEntity.showInterfaceWalkable(player, 4535);
                        if (player.getExtraData().getInt(BROTHER_TARGET) == ExtraData.DEFAULT_INT_VALUE)
                            player.getExtraData().put(BROTHER_TARGET, Barrows.AHRIM + Misc.random(5));
                    }
                    this.stop();
                }

            });
        }

    }

    private static boolean inLocation(final Player player, int id) {
        return (id == 6771 || id == 6703
                ? player.getLocation().equals(Locations.Location.VERACS_BARROWS) : id == 6772 || id == 6706
                ? player.getLocation().equals(Locations.Location.GUTHANS_BARROWS) : id == 6773 || id == 6704
                ? player.getLocation().equals(Locations.Location.TORAGS_BARROWS) : id == 6821 || id == 6702
                ? player.getLocation().equals(Locations.Location.AHRIMS_BARROWS) : id == 6822 || id == 6705
                ? player.getLocation().equals(Locations.Location.KHARILS_BARROWS) : id == 6823 || id == 6707
                ? player.getLocation().equals(Locations.Location.DHAROKS_BARROWS) : false);
    }

    public static int npcForCoffin(int id) {
        return (id == 6771
                ? Barrows.VERAC : id == 6772
                ? Barrows.GUTHAN : id == 6773
                ? Barrows.TORAG : id == 6821
                ? Barrows.AHRIM : id == 6822
                ? Barrows.KARIL : id == 6823
                ? Barrows.DHAROK : -1);
    }

    public void openChest(final Player player) {
        if (player.getExtraData().getInt(KILLSCOUNT_KEY) >= 3) {
            int rewardscount = player.getExtraData().getInt(KILLSCOUNT_KEY);
            for (int i = 0; i < rewardscount; i++) {
                Item barrowsReward = new Item(REWARDS[Misc.random(REWARDS.length - 1)]);
                if (player.getInventory().freeSlots() > 0) {
                    player.getInventory().add(barrowsReward);
                } else {
                    player.getActionSender().sendMessage("You don't have enough room in your inventory!");
                    break;
                }
            }
            if (player.getInventory().freeSlots() > 0) {
                Item bonusReward = new Item(BONUS_REWARDS[Misc.random(BONUS_REWARDS.length - 1)]);
                player.getInventory().add(bonusReward);
            }
            player.getAchievementTracker().barrowsTrip();
            ContentEntity.sendMessage(player, "Congratulations on completing the barrows minigame.");
            player.getExtraData().remove(BROTHERS_KILLED_KEY);
            player.getExtraData().remove(KILLSCOUNT_KEY);
            player.getExtraData().remove(BROTHER_TARGET);
            World.submit(new Task(2000, "barrows teleport") {
                @Override
                public void execute() {
                    Magic.teleport(player, Edgeville.POSITION, true);
                    player.getActionSender().showInterfaceWalkable(-1);
                    this.stop();
                }
            });

        } else {
            ContentEntity.sendMessage(player, "You have not got enough kills to open the chest.");
        }
    }

    static {
    }

    public void clickStairs(final Player player, final int id) {
        if (!inLocation(player, id)) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        World.submit(new Task(600, "barrows entering") {
            @Override
            public void execute() {
                switch (id) {
                    case 6705: //karil
                        ContentEntity.teleport(player, 3565 + Misc.random(1), 3275 + Misc.random(1), 0);
                        break;
                    case 6706: //guthan
                        ContentEntity.teleport(player, 3576 + Misc.random(1), 3282 + Misc.random(1), 0);
                        break;
                    case 6707: //dharoks
                        ContentEntity.teleport(player, 3574 + Misc.random(1), 3298 + Misc.random(1), 0);
                        break;
                    case 6702: //ahrim
                        ContentEntity.teleport(player, 3564 + Misc.random(1), 3288 + Misc.random(1), 0);
                        break;
                    case 6704: //torag
                        ContentEntity.teleport(player, 3552 + Misc.random(1), 3282 + Misc.random(1), 0);
                        break;
                    case 6703: //verac
                        ContentEntity.teleport(player, 3556 + Misc.random(1), 3297 + Misc.random(1), 0);
                        break;
                }
                player.setHasTarget(false);
                this.stop();
            }
        });
    }

    public void killNpc(Player client, int id) {
        int killsInt = client.getExtraData().getInt(BROTHERS_KILLED_KEY);
        boolean[] killedbrothers = intToBoolArray(killsInt, 6);
        killedbrothers[id - Barrows.AHRIM] = true;
        killsInt = boolArrayToInt(killedbrothers);
        client.getExtraData().put(BROTHERS_KILLED_KEY, killsInt);
        client.getExtraData().put(KILLSCOUNT_KEY, client.getExtraData().getInt(KILLSCOUNT_KEY) + 1);
        ContentEntity.sendString(client, "Killcount: " + client.getExtraData().getInt(KILLSCOUNT_KEY), 4536);
        client.setHasTarget(false);
    }

    public static int newItemId(int item) {
        if (item <= 4856 || item >= 4994)
            return item;
        switch (item) {
            case 4856://ahrim
                return 4708;
            case 4862:
                return 4710;
            case 4868:
                return 4712;
            case 4874:
                return 4714;

            case 4880://dharok
                return 4716;
            case 4886:
                return 4718;
            case 4892:
                return 4720;
            case 4898:
                return 4722;

            case 4904://gutahn
                return 4724;
            case 4910:
                return 4726;
            case 4916:
                return 4728;
            case 4922:
                return 4730;

            case 4952://torag
                return 4745;
            case 4958:
                return 4747;
            case 4964:
                return 4749;
            case 4970:
                return 4751;

            case 4976://verac
                return 4753;
            case 4982:
                return 4755;
            case 4988:
                return 4757;
            case 4994:
                return 4759;
        }
        return item;
    }

    public static void confirmCoffinTeleport(final Player player) {
        World.submit(new Task(100, "Barrows spawning") {
            @Override
            public void execute() {
                ContentEntity.teleport(player, 3551, 9692, 0);
                NPC n = NPCManager.addNPC(3553, 9694, 0, player.getExtraData().getInt(BROTHER_TARGET), -1);
                n.forceMessage("You dare disturb my slumber!!");
                n.agressiveDis = 7;
                n.ownerId = player.getIndex();
                this.stop();
            }
        });
    }

    public static boolean clickCoffin(final Player client, int oId, int oX, int oY) {
        if (!inLocation(client, oId)) {
            return false;
        }
        final int brotherId = npcForCoffin(oId);
        if (client.getExtraData().getInt(BROTHER_TARGET) == brotherId) { //if target brother
            DialogueManager.openDialogue(client, 65);
        } else { //if not target brother
            boolean[] brothersKilled = intToBoolArray(client.getExtraData().getInt(BROTHERS_KILLED_KEY), 6);
            if (brothersKilled[brotherId - Barrows.AHRIM]) {
                client.getActionSender().sendMessage("It appears to be empty.");
                return true;
            }
            if (!client.hasTarget()) {
                boolean found = false;
                for (int i = 1; i <= World.npcs.size(); i++) {
                    if (World.npcs.get(i) != null) {
                        NPC npc = (NPC) World.npcs.get(i);
                        if (npc.ownerId == client.getIndex() && client.cE.summonedNpc != npc) {
                            npc.forceMessage("I'm not done with you " + client.getSafeDisplayName() + "!");
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    NPC n = NPCManager.addNPC(client.getPosition().getX(), client.getPosition().getY(), client.getPosition().getZ(), npcForCoffin(oId), -1);
                    n.forceMessage("You dare disturb my slumber!");
                    n.agressiveDis = 7;
                    n.ownerId = client.getIndex();
                    World.register(n);
                    client.setHasTarget(true);
                }
            }
        }
        return true;
    }

    @Override
    public void init() throws FileNotFoundException {
    }

    @Override
    public int[] getValues(int type) {
        if (type == 1) {
            int[] j = {952};
            return j;
        }
        if (type == 6) {
            int[] j = {6771, 6772, 6773, 6821, 6822, 6823,/*stairs*/6707, 6706, 6705, 6702, 6704, 6703,/*chest*/10284};
            return j;
        }
        if (type == 16) {
            int[] j = {2025, 2026, 2027, 2028, 2029, 2030,};
            return j;
        }
        return null;
    }

    @Override
    public boolean clickObject(final Player client, final int type, final int oId, final int oX, final int oY, final int a) {
        if (type == 1) {
            dig(client);
        } else if (type == 6) {
            if (oId == 10284)
                openChest(client);
            else if (oId >= 6702 && oId <= 6707)
                clickStairs(client, oId);
            switch (oId) {
                case 6771:
                case 6772:
                case 6773:
                case 6821:
                case 6822:
                case 6823:
                    clickCoffin(client, oId, oX, oY);
                    break;
            }

        } else if (type == 16) {
            killNpc(client, oId);
        }
        return false;
    }
}