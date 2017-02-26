package org.hyperion.rs2.model;


import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.SummoningData;
import org.hyperion.rs2.model.container.BoB;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.misc.BunyipEvent;
import org.hyperion.rs2.model.content.skill.Summoning;
import org.hyperion.util.Misc;

/**
 * @author Vegas/Arsen/Linus/Jolt/Flux <- Same Person
 */
public class SummoningMonsters {

	// public static ArrayList<NPC> Monsters = new ArrayList<NPC>();
	
	public static final int[] SUMMONING_MONSTERS = {7343, 6823, 6869};
	public static NPCDefinition loadDefinition(int id) {
		int[] bonus = new int[10];
		switch(id) {
		case 7343:
			bonus = new int[10];
			for(int i = 0; i < bonus.length; i++)
				bonus[i] = 320;
			int[] atk = {8183};
			return NPCDefinition.create(7343, 300, 240, bonus, 8184, 8185, atk, 2, "Steel Titan", 0);
		case 6823:
			bonus = new int[10];
			for(int i = 0; i < bonus.length; i++)
				bonus[i] = 150;
			int[] atk1 = {6376};
			return NPCDefinition.create(id, 300, 81, bonus, 6377, 6376, atk1, 2, "Unicorn Stallion", 0);
		case 6869:
			return NPCDefinition.create(id, 250, 139, new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, 
					8305, 8304, new int[]{8303}, 1, "Wolpertinger", 0);
            case 6873:
                return NPCDefinition.create(id, 250, 239, new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
                    0, 0, new int[]{0}, 1, "Pack Yak", 0);
		}
		return null;
	}
	public static void runEvent(Player p) {
		//for (Player p : World.getPlayers()) {
		if(p == null || p.cE.summonedNpc == null) {
			return;
		}

		p.SummoningCounter--;
        refreshSummonTab(p, p.cE.summonedNpc);
	        /*if(p.cE.summonedNpc.cE.getOpponent() == null)
				p.cE.summonedNpc.cE.face(p.getLocation().getX(), p.getLocation().getY());*/
		int distance = Misc.distance(p.getPosition().getX(), p
				.getPosition().getY(), p.cE.summonedNpc.getPosition().getX(),
				p.cE.summonedNpc.getPosition().getY());
		if(distance > 8) {
			Position newlocation = p.getPosition().getCloseLocation();
			p.cE.summonedNpc.setTeleportTarget(newlocation);
			//p.cE.summonedNpc.setLocation(newlocation);
			p.cE.summonedNpc.ownerId = p.getIndex();
			p.cE.summonedNpc.playGraphics(Graphic.create(1315));
			p.cE.summonedNpc.cE.setOpponent(null);
			Combat.follow(p.cE.summonedNpc.cE, p.cE);
			p.cE.summonedNpc.setInteractingEntity(p);

			
		} else if(/*distance > p.cE.summonedNpc.getDefinition().sizeX() && */distance >= 2) {
			//if(p.cE.summonedNpc.cE.getOpponent() == null || p.cE.summonedNpc.cE.getOpponent().getEntity().isDead()) {
				p.cE.summonedNpc.cE.setOpponent(null);
             try {
                 Combat.follow(p.cE.summonedNpc.cE, p.cE);
             } catch(final Exception ex) {
                 World.resetSummoningNpcs(p);
             }
			//}
		}
		if(p.SummoningCounter <= 0) {
			p.getActionSender().sendMessage("Your Summoning monster has died.");
			World.resetSummoningNpcs(p);
		} else if(p.SummoningCounter == 100) {
			p.getActionSender().sendMessage("Your Summoning monster will die in a minute...");
		} else if(p.SummoningCounter == 200) {
			p.getActionSender().sendMessage("Your Summoning monster will die in 2 minutes.");
		}
		//}
	}


	public static void SummonNewNPC(final Player p, int npcID, int itemId) {
		int req = SummoningData.getRequirementForNpcId(npcID);
		if(p.getSkills().getLevel(23) < req) {
			p.getActionSender().sendMessage(
					"You need a Summoning Level of " + req
							+ " to summon this npc.");
			return;
		}
		if(p.duelAttackable > 0) {
			p.getActionSender().sendMessage("You can't do this in the duel arena");
			return;
		}
		if(Summoning.isBoB(npcID))
			BoB.dropBoB(p.getPosition(), p);
		for(int i = 0; i < BOB_NPCS.length; i++) {
			if(BOB_NPCS[i][0] == npcID || BOB_NPCS[i][0] - 1 == npcID)
				p.setBob(BOB_NPCS[i][1]);
		}
		ContentEntity.deleteItemA(p, itemId, 1);
		if(p.cE.summonedNpc != null) {
			World.resetSummoningNpcs(p);
		}
		SummonNewNPC2(p, npcID);
	}

	public static void SummonNewNPC2(final Player p, int npcID) {

		final NPC monster = NPCManager
				.addNPC(p.getPosition().getX(), p.getPosition().getY(),
						p.getPosition().getZ(), npcID, - 1);
		p.SummoningCounter = SummoningData.getTimerById(npcID);
		if(npcID == 6813) {
			World.submit(new BunyipEvent(p));
		}
		monster.ownerId = p.getIndex();
		Combat.follow(monster.getCombat(), p.getCombat());
		monster.summoned = true;
		p.cE.summonedNpc = monster;
		monster.playGraphics(Graphic.create(1315));
        openSummonTab(p, monster);
		World.register(monster);
	}

    public static void renewFamiliar(Player player) {
        if(player.SummoningCounter <= 0)
            return;
        if(player.cE.summonedNpc == null)
            return;
        int npcId = player.cE.summonedNpc.getDefinition().getId();
        if(npcId <= 0)
            return;
        int pouchId = SummoningData.getPouchByNpc(npcId);
        if(player.getInventory().contains(pouchId)) {
            player.getInventory().remove(player.getInventory().getById(pouchId));
            player.SummoningCounter += SummoningData.getTimerById(npcId);
            player.sendMessage("You have renewed your familiar.");
        } else {
            player.sendMessage("You do not have the pouch required to do this.");
        }
    }

    public static void bobToInventory(Player player) {
        if(player.SummoningCounter <= 0)
            return;
        int size = player.getBoB().size();
        if(size > 0) {
            int amountToTransfer = size;
            if(player.getInventory().freeSlots() < size)
                amountToTransfer = player.getInventory().freeSlots();
            for(int index = 0; index < amountToTransfer; index++) {
                if(player.getInventory().size() == player.getInventory().capacity())
                    break;
                Item item = player.getBoB().get(index);
                player.getBoB().remove(item);
                player.getInventory().add(item);
            }
        } else {
            player.sendMessage("Your beast of burden is empty.");
        }
    }

    public static void openSummonTab(Player player, NPC npc) {
        player.getActionSender().sendSidebarInterface(16, 17011);
        player.getActionSender().sendNPCHead(npc.getDefinition().getId(),17027,0);
        refreshSummonTab(player,npc);
    }

    public static void refreshSummonTab(Player player, NPC npc) {
        player.getActionSender().sendString(17017, npc.getDefinition().getName());
        player.getActionSender().sendString(17021, getTimeForTick(player.SummoningCounter));
        player.getActionSender().sendString(17025, player.getSkills().getLevel(Skills.SUMMONING) + "/" + player.getSkills().getLevelForExp(Skills.SUMMONING));
    }

    public static String getTimeForTick(int tick) {
        if(tick <= 0)
            return "0:00";
        long ms = tick * 600;
        int totalMinutes = (int)(ms / 1000);
        int hourLeft = (totalMinutes) / 60;
        int minutesLeft = totalMinutes % 60;
        String hour = (hourLeft > 10) ? ("" + hourLeft) : ("0" + hourLeft);
        String minute = (minutesLeft > 10) ? ("" + minutesLeft) : ("0" + minutesLeft);
        return hour + ":" + minute;
    }

	public static final int[][] BOB_NPCS = {
			//id,space,specific item
			{6807, 3},//Thorny snail
			{6868, 9},//bull ant
			{6795, 12},//spirit terrorbird
			{6816, 18},//war tortoise
			{6874, 30},//pack yack
			{7350, 28},//abyssal titan
			{6822, 18},//abyssal lurker
			{6820, 7},//abyssal parasite
	};


}
