package org.hyperion.rs2.model.content.bounty;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.model.content.Lock;
import org.hyperion.rs2.model.content.minigame.LastManStanding;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BountyHunter {


    public static final int BASE_POINTS = 5;
    private static final int DP_SPLIT = 850;

    private enum Emblem {
        TIER_1(1),
        TIER_2(2),
        TIER_3(4),
        TIER_4(8),
        TIER_5(15),
        TIER_6(25),
        TIER_7(35),
        TIER_8(50),
        TIER_9(70),
        TIER_10(100);


        private static final Map<Integer, Emblem> EMBLEM_MAP = Stream.of(values()).collect(Collectors.toMap(e -> e.id, Function.<Emblem>identity()));

        private static final int BASE_ID = 13195;

        private final int reward;
        private final int id;
        Emblem(final int multiplier) {
            this.reward = multiplier * BASE_POINTS;
            this.id = ordinal() + BASE_ID;
        }

        private Emblem upgrade() {
            return ordinal() == values().length - 1 ? this : values()[ordinal() + 1];
        }

        public static Emblem getBest(final Container inventory) {
            for(int i = values().length - 1; i >= 0; i--) {
                if(inventory.contains(values()[i].id))
                    return values()[i];
            }
            return null;
        }

        public static Emblem forId(int id) {
            return EMBLEM_MAP.get(id);
        }

        private static List<Item> getEmblems(final Container inventory) {
            return Stream.of(inventory.toArray()).filter(Objects::nonNull).filter(item -> forId(item.getId()) != null).collect(Collectors.toList());
        }

        private static int getTotalVal(final Item[] items) {
            return Stream.of(items).filter(Objects::nonNull).filter(item -> forId(item.getId()) != null).mapToInt(item -> forId(item.getId()).reward).sum();
        }
    }

    private int bhPoints = 0;
    private int emblemPoints = 0;
	private final Player player;
	private Player target;
    private Player prevTarget;
    private boolean enabled = true;

    public Player getPrevTarget() {
        return prevTarget;
    }

    public void setPrevTarget(Player prevTarget) {
        this.prevTarget = prevTarget;
    }

    public BountyHunter(Player player) {
		this.player = player;
	}
	
	public void findTarget() {
		for(final Player p : World.getPlayers()) {
			if(p.isHidden() || !applicable(p) || this.player.equals(p) || !levelCheck(p) || !wealthCheck(p) || !wildLevelCheck(p) || p.equals(prevTarget)) continue;
			    assignTarget(p);
			break;
		}
	}

    public void sendBHTarget() {
        player.getActionSender().sendString("@or1@Target: @gre@" + (player.getBountyHunter().getTarget() != null ? player.getBountyHunter().getTarget().getSafeDisplayName() : "None"), 36502);
    }

    public void clearTarget() {
        if(player.getBountyHunter().getTarget() == null)
            return;
        Player oldTarget = target;
        setPrevTarget(player.getBountyHunter().getTarget());
        setTarget(null);
        player.getActionSender().removeArrow();
        sendBHTarget();
        oldTarget.getBountyHunter().setPrevTarget(oldTarget.getBountyHunter().getTarget());
        oldTarget.getBountyHunter().setTarget(null);
        oldTarget.getActionSender().removeArrow();
        oldTarget.getBountyHunter().sendBHTarget();
}
	
	public void assignTarget(Player p) {
        if(target == p)
            return;
		this.target = p;
        sendBHTarget();
        player.getActionSender().createArrow(target);
        p.getBountyHunter().assignTarget(player);

	}
	
	public boolean levelCheck(Player p) {
		return Math.abs(p.getCombat().getCombat() - player.getCombat().getCombat()) < 12 && player.getPosition().getZ() == p.getPosition().getZ() && player.getUID() != p.getUID();
	}
	
	public boolean wildLevelCheck(final Player opp) {
		final int oppLevel = Combat.getWildLevel(opp.cE.getAbsX(), opp.cE.getAbsY(), opp.cE.getAbsZ());
		final int playerLevel = Combat.getWildLevel(player.cE.getAbsX(), player.cE.getAbsY(), player.cE.getAbsZ());
		return (oppLevel < 10 && playerLevel < 10) || (oppLevel >= 10 && playerLevel >= 10);
	}
	
	private boolean wealthCheck(final Player opp) {
		final int accValue = player.getAccountValue().getTotalValue();
		final int oppAccValue = opp.getAccountValue().getTotalValue();
		return (oppAccValue < DP_SPLIT && accValue < DP_SPLIT) || (accValue >= DP_SPLIT && oppAccValue >= DP_SPLIT);
	}

    public static boolean applicable(Player player) {
        if(player == null)
            return false;
        return player.getBountyHunter().target == null && applicable2(player);
    }

    public static boolean applicable2(Player player) {
        if(player == null)
            return false;
        return player.getPosition().inPvPArea() && !player.getPosition().inFunPk() && !LastManStanding.inLMSArea(player.cE.getAbsX(), player.cE.getAbsY()) && !Lock.isEnabled(player, Lock.BOUNTY_HUNTER) && !BountyHunterLogout.isBlocked(player);
    }
	
	public static void fireLogout(final Player player) {
		final Player targ = player.getBountyHunter().getTarget();
		if(targ != null) {
            BountyHunterLogout.playerLogout(player);
            targ.getBountyHunter().sendBHTarget();
            targ.getBountyHunter().setPrevTarget(player);
			targ.getBountyHunter().setTarget(null);
			targ.getActionSender().removeArrow();
		}
	}
	
	public void handleBHKill(final Player opp) {
		if(!opp.equals(target)) return;
        if(opp.getSkills().getCombatLevel() < 80 || player.getSkills().getCombatLevel() < 80)
            return;
		player.sendPkMessage("You now have " + incrementAndGet() + " BH points!");
		handleBHDrops(opp);
        player.getAchievementTracker().bountyHunterKill();
		for(Player p : new Player[]{player, opp}) {
			p.getBountyHunter().target = null;
			p.getActionSender().createArrow(10, -1);
		}
        final List<Item> emblems = Emblem.getEmblems(opp.getInventory());
        for(final Item item : emblems) {
            player.getBank().add(new BankItem(0, item.getId(), opp.getInventory().remove(item)));
            player.sendMessage("A " + Emblem.forId(item.getId()).toString() + " emblem has been added to your bank.");
        }
	}
	
	public void handleBHDrops(final Player opp) {
        GlobalItem gI = new GlobalItem(player, opp.getPosition().getX(),
                opp.getPosition().getY(), opp.getPosition().getZ(),
                Item.create(Emblem.BASE_ID, 1));
        GlobalItemManager.newDropItem(player, gI);
        upgradeEmblem();
	}

    private void upgradeEmblem() {
        final Container inventory = player.getInventory();
        final Emblem best = Emblem.getBest(inventory);
        if(best != null) {
            final int slot = inventory.getSlotById(best.id);
            inventory.remove(slot, Item.create(best.id));
            inventory.add(Item.create(best.upgrade().id), slot);
        }
    }

    public int emblemExchangePrice() {
        return Emblem.getTotalVal(player.getInventory().toArray());
    }

    public void exchangeEmblems() {
        final List<Item> toSubtract = Emblem.getEmblems(player.getInventory());
        final int toAdd = Emblem.getTotalVal(toSubtract.toArray(new Item[toSubtract.size()]));
        for(Item item : toSubtract) {
            player.getInventory().remove(item);
        }
        emblemPoints += toAdd;
    }

    public boolean switchEnabled() {
        return enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKills() {
        return bhPoints;
    }

    public int incrementAndGet() {
        return ++bhPoints;
    }

    public void setKills(final int kills) {
        this.bhPoints = kills;
        player.getQuestTab().updateComponent(QuestTab.QuestTabComponent.BOUNTY_HUNTER_POINTS);
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public int getEmblemPoints() {
        return emblemPoints;
    }

    public void setEmblemPoints(final int points) {
        emblemPoints = points;
    }

}
