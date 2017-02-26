package org.hyperion.rs2.model.content.skill.dungoneering;

import org.hyperion.Server;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.ShopManager;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.model.content.skill.dungoneering.reward.RingPerks;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.model.itf.impl.DungoneeringParty;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 2/21/15
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DungeoneeringManager implements ContentTemplate {
    public static final int TRADER_ID = 539;
    public static final Position LOBBY = Position.create(2987, 9637, 0);
    private static final int DIALOGUE_ID = 7000;
    public static boolean ENABLED = true;
    private static List<Integer> items;

    public static final List<Integer> parse() {
        final List<ItemDefinition> items = new ArrayList<>();
        for (int i = 0; i < 13_000; i++) {
            if (!ItemSpawning.canSpawn(i))
                continue;
            final ItemDefinition def = ItemDefinition.forId(i);
            if (def == null) continue;
            final String name = def.getName().toLowerCase();
            if (nonviable(def) && !(name.endsWith(" arrow") || (def.getId() < 567 && def.getId() > 553)))
                continue;
            if (def.isNoted())
                continue;
            items.add(def);

        }

        final Iterator<ItemDefinition> defs = items.iterator();
        System.out.println(items.size());
        return items.stream().map(ItemDefinition::getId).collect(Collectors.toList());
    }

    public static boolean cantJoin(final Player player) {
        return (!player.getInventory().contains(15707) && !player.getEquipment().contains(15707))
                || ((Arrays.asList(player.getInventory().toArray()).stream().filter(value -> value != null).anyMatch(value -> value.getDefinition().getId() != 15707))
                || (Arrays.asList(player.getEquipment().toArray())).stream().filter(value -> value != null).anyMatch(value -> value.getDefinition().getId() != 15707))
                || player.cE.summonedNpc != null
                || (player.getBoB() != null && player.getBoB().freeSlots() != player.getBoB().capacity());
    }

    private static final boolean nonviable(final ItemDefinition def) {
        return !full(def.getBonus()) || def.getName().contains("(") || def.getName().contains("/") || def.getName().endsWith("0") || def.getName().endsWith("5") || def.getName().toLowerCase().startsWith("anger");
    }

    private static final boolean full(final int[] bonus) {
        for (int i : bonus)
            if (i > 5)
                return true;
        return false;
    }

    public static List<Integer> getItems() {
        if (items == null)
            items = parse();
        return items;
    }

    public static void setItems(List<Integer> value) {
        items = value;
    }

    public static int randomItem() {
        return getItems().get(Misc.random(getItems().size() - 1));
    }

    public static final void handleDying(final Player player) {
        player.setTeleportTarget(player.getDungeoneering().getCurrentDungeon().getStartRoom().getSpawnLocation(), false);
        player.getDungeoneering().getCurrentDungeon().kill(player);
    }

    @Override
    public int[] getValues(int type) {
        if (type == ClickType.EAT || type == ClickType.ITEM_OPTION7 || type == ClickType.ITEM_OPTOION6) {
            return new int[]{15707};
        } else if (type == ClickType.OBJECT_CLICK1) {
            return new int[]{2477, 2476, 2475, 2804};
        } else if (type == ClickType.DIALOGUE_MANAGER) {
            int[] ret = new int[25];
            for (int i = 0; i < ret.length; i++)
                ret[i] = DIALOGUE_ID + i;
            return ret;
        } else if (type == ClickType.NPC_OPTION1) {
            return new int[]{TRADER_ID, 9711};
        } else if (type == ClickType.NPC_OPTION2) {
            return new int[]{8827, 8824, 9711};
        }
        return new int[0];
    }

    @Override
    public boolean clickObject2(Player player, int type, int npcId, int x, int y, int npcSlot) {
        if (!ENABLED) {
            player.sendMessage("Currently Disabled.");
            return false;
        }
        if (type == ClickType.NPC_OPTION2) {
            if (npcId == 9711) {
                ShopManager.open(player, 81);
                return true;
            }
            try {
                player.debugMessage("Yo3");

                final NPC npc = (NPC) World.getNpcs().get(npcSlot);
                if (npc == null) {
                    player.sendMessage("Null NPC");
                    return false;
                }
                if (npc.isDead())
                    return false;
                final int min_level = npcId == 8824 ? 50 : 80;
                if (player.getSkills().getLevel(Skills.THIEVING) < min_level) {
                    player.sendMessage("You need " + min_level + " thieving level to loot from this npc!");
                    return false;
                }
                player.debugMessage("Yo");


                if (player.getExtraData().getLong("thievingTimer") > System.currentTimeMillis())
                    return false;

                player.getExtraData().put("thievingTimer", System.currentTimeMillis() + 2000L);
                for (int i = 0; i < min_level / 25; i++) {
                    player.getInventory().add(Item.create(DungeoneeringManager.randomItem(), 1));
                }
                player.debugMessage("Yo5");
                player.playAnimation(Animation.create(881));

                npc.serverKilled = true;
                npc.inflictDamage(new Damage.Hit(npc.health, Damage.HitType.NORMAL_DAMAGE, 0), null);

            } catch (Exception e) {
                player.debugMessage("Yo4");

                e.printStackTrace();
            }
        } else if (type == ClickType.ITEM_OPTOION6) {
            if (player.getDungeoneering().inDungeon()) {
                String[] names = player.getDungeoneering().getCurrentDungeon().getPlayers().stream().map(Player::getName).toArray(String[]::new);
                if (names.length > 5) {
                    final String[] old = names.clone();
                    names = new String[5];
                    System.arraycopy(old, 0, names, 0, names.length);
                }
                player.getActionSender().sendDialogue("Teleport", ActionSender.DialogueType.OPTION, 1, Animation.FacialAnimation.DEFAULT, names);
                for (int i = 0; i < names.length; i++) {
                    player.getInterfaceState().setNextDialogueId(i, 7014 + i);
                }
            }
        } else if (type == ClickType.ITEM_OPTION7) {
            player.forceMessage(String.format("I have %,d dungoneering tokens", player.getDungeoneering().getTokens()));
            return true;
        }
        return false;
    }

    @Override
    public boolean itemOptionOne(Player player, int id, int slot, int interfaceId) {
        if (!ENABLED) {
            player.sendMessage("Currently Disabled.");
            return false;
        }
        if (cantJoin(player)) {
            player.sendMessage("You can only bring the ring of kinship - no summoning allowed!");
            return false;
        }
        Magic.teleport(player, LOBBY, false);
        player.SummoningCounter = 0;
        return true;
    }

    @Override
    public boolean npcOptionOne(Player player, int npcId, int npcLocationX, int npcLocationY, int npcSlot) {
        if (!ENABLED) {
            player.sendMessage("Currently Disabled.");
            return false;
        }
        if (npcId == TRADER_ID) {
            ShopManager.open(player, 80);
            return true;
        }

        if (npcId == 9711) {
            DialogueManager.openDialogue(player, 7011);
            return true;
        }

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean objectClickOne(Player player, int id, int x, int y) {
        if (!ENABLED) {
            player.sendMessage("Currently Disabled.");
            return false;
        }
        if (player.getPosition().distance(Position.create(x, y, 0)) > 2)
            return false;
        switch (id) {
            case 2475: {
                if (player.getLocation().equals(Locations.Location.DUNGEONEERING_LOBBY)) {
                    final DungoneeringParty itf = InterfaceManager.<DungoneeringParty>get(DungoneeringParty.ID);
                    itf.hide(player);
                    player.setTeleportTarget(Edgeville.POSITION);
                }
                break;
            }
            case 2804:
                if (Server.isUpdating()) {
                    player.sendMessage("You cannot start a dungeon right now");
                    return true;
                }
                if (player.getDungeoneeringLeader() != null
                        && !player.getDungeoneeringLeader().equals(player)
                        && player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().contains(player)) {
                    player.sendf("You are currently apart of @red@%s@bla@'s team. Leave before assembling a new one.", TextUtils.titleCase(player.getDungeoneeringLeader().getName()));
                    return true;
                }
                final DungoneeringParty itf = InterfaceManager.<DungoneeringParty>get(DungoneeringParty.ID);
                itf.show(player);
                break;
            case 2477:
                final Position loc = player.getDungeoneering().clickPortal();
                if (loc == null) {
                    player.sendMessage("You need to clear the room before progressing");
                    return true;
                }
                Combat.resetAttack(player.getCombat().getOpponent());
                player.setTeleportTarget(loc);
                player.getDungeoneering().getRoom().initialize();
                return true;
            case 2476:
                final Position location = player.getDungeoneering().clickBackPortal();
                if (location == null) {
                    DialogueManager.openDialogue(player, 7002);
                    return true;
                }
                Combat.resetAttack(player.getCombat().getOpponent());
                player.setTeleportTarget(location);
                player.getDungeoneering().getRoom().initialize();
                break;
        }
        return false;
    }

    @Override
    public boolean dialogueAction(Player player, int dialogueId) {
        if (!ENABLED) {
            player.sendMessage("Currently Disabled.");
            return false;
        }
        switch (dialogueId) {
            case 7000:
            case 7001:
                DungoneeringParty.respond(player, dialogueId - 7000);
                break;
            case 7002:
                player.getActionSender().sendDialogue("Leave?", ActionSender.DialogueType.OPTION, 1, Animation.FacialAnimation.DEFAULT,
                        "Yes, I want to leave this dungeon", "No, I don't want to lose my progress");
                final InterfaceState state = player.getInterfaceState();
                state.setNextDialogueId(0, 7003);
                state.setNextDialogueId(1, 7004);
                return true;
            case 7003:
                if (player.getDungeoneering().inDungeon())
                    player.getDungeoneering().getCurrentDungeon().remove(player, false);
                break;
            case 7005:
                player.getDungeoneering().showBindDialogue(player.getActionSender(), player.getInterfaceState());
                return true;
            case 7006:
            case 7007:
            case 7008:
            case 7009:
            case 7010:
                final int slots = 1 + (player.getSkills().getLevel(Skills.DUNGEONEERING) + 1) / 25;
                final int slot = dialogueId - 7006;
                if (slot > slots) {
                    player.sendMessage("This slot is not available for you");
                    break;
                }
                player.getDungeoneering().bind((Item) player.getExtraData().get("binditem"), slot);
                break;
            case 7011:
                player.getActionSender().sendDialogue("Rewards Trader", ActionSender.DialogueType.OPTION, 9711, Animation.FacialAnimation.DEFAULT,
                        "Dungeoneering Guide", "What are perks?", "Upgrade ring perks");
                player.getInterfaceState().setNextDialogueId(0, 7012);
                player.getInterfaceState().setNextDialogueId(1, 7013);
                player.getInterfaceState().setNextDialogueId(2, 7019);

                return true;
            case 7012:
                //player.sendMessage("l4unchur13 http://forums.arteropk.com/index.php/topic/13907-dungeoneering-guide/");
                break;
            case 7013:
                player.getActionSender().sendDialogue("Rewards Trader", ActionSender.DialogueType.NPC, 9711, Animation.FacialAnimation.DEFAULT,
                        "Perks are bonuses that your ring of kinship possesses (when worn)", "They work both inside and outside dungeoneering", "They cost dungeoneering tokens to upgrade");
                player.getInterfaceState().setNextDialogueId(0, -1);

                return true;
            case 7014:
            case 7015:
            case 7016:
            case 7017:
            case 7018:
                try {
                    final Player to = player.getDungeoneering().getCurrentDungeon().getPlayers().get(dialogueId - 7014);
                    player.setTeleportTarget(to.getPosition());
                    player.getDungeoneering().setCurrentRoom(to.getDungeoneering().getRoom());
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case 7019:
                final String[] perk_pricse = Stream.of(RingPerks.Perk.values()).
                        map(perk ->
                                perk.maxLevel != player.getDungeoneering().perks.hasPerk(perk) ?
                                        String.format("%s %d tokens", perk.name(), player.getDungeoneering().perks.calcNextPerkCost(perk.index)) :
                                        String.format("%s MAXED", perk.name())).toArray(String[]::new);
                player.getActionSender().sendDialogue("Rewards Trader", ActionSender.DialogueType.OPTION, 9711, Animation.FacialAnimation.DEFAULT,
                        perk_pricse);
                for (int i = 0; i < 3; i++)
                    player.getInterfaceState().setNextDialogueId(i, 7020 + i);
                return true;
            case 7020:
            case 7021:
            case 7022:
                final RingPerks.Perk perk = RingPerks.Perk.forStyle(dialogueId - 7020);
                if (player.getDungeoneering().perks.hasPerk(perk) == perk.maxLevel) {
                    player.sendMessage("This perk is maxed");
                    break;
                }
                if (player.getDungeoneering().buyPerk(dialogueId - 7020)) {
                    player.sendMessage("You successfully upgrade your perk. Check perk bonuses by right clicking your ring");
                } else {
                    player.sendMessage("You need more tokens to do that!");
                }
                break;
        }

        player.getActionSender().removeChatboxInterface();
        return true;
    }

}
