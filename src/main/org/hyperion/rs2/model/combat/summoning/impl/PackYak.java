package org.hyperion.rs2.model.combat.summoning.impl;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.summoning.AbstractSummoningSpecial;
import org.hyperion.rs2.model.container.bank.BankItem;

public final class PackYak extends AbstractSummoningSpecial {	
	private final int usedWith, slot;
	
	public PackYak(int usedWith, int slot) {
		this.usedWith = usedWith;
		this.slot = slot;
	}
	@Override
	public int requiredSpecial() {
		return 50;
	}

	@Override
	public boolean requiresOpponent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getScrollId() { return 12435; }

	@Override
	public boolean checkRequirements(Player p) {
		return true;
	}

	@Override
	public void execute(Player player) {
        Item item = player.getInventory().get(slot);
        BankItem bankItem = new BankItem(0, item.getId(), item.getCount());
        if(player.getInventory().remove(item) == item.getCount()) {
            player.getBank().add(bankItem);
        }
        player.playAnimation(Animation.create(7660));
        player.playGraphics(Graphic.create(1300));
	}

	@Override
	public void executeOpponent(Entity p) throws NullPointerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeFamiliar(NPC n) {
		//npc.getCombat().doAnim();
		//npc.playGraphics(Graphic.create());
	}

}
