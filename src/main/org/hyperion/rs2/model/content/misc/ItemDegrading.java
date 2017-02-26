package org.hyperion.rs2.model.content.misc;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.util.Misc;

public class ItemDegrading {

	public static void check(Player player) {
		if(Misc.random(2000) == 0) {
			ItemDegrading.checkChaoticDegrades(player);
		}
	}

	private static void checkPvpDegrades(Player p) {
		Item weapon = p.getEquipment().get(Equipment.SLOT_WEAPON);
		if(weapon != null) {
			switch(weapon.getId()) {
				case 13899:
				case 13905:
				case 13902:
				case 13867:
					p.getEquipment().set(Equipment.SLOT_WEAPON, null);
					p.getActionSender().sendMessage("Your " + weapon.getDefinition().getProperName() + " has degraded..");
					ContentEntity.playerGfx(p, 1301);
					break;
			}
		}
		Item body = p.getEquipment().get(Equipment.SLOT_CHEST);
		if(body != null) {
			switch(body.getId()) {
				case 13887:
				case 13884:
				case 13870:
				case 13858:
					p.getEquipment().set(Equipment.SLOT_CHEST, null);
					ContentEntity.playerGfx(p, 1301);
					p.getActionSender().sendMessage("Your " + body.getDefinition().getProperName() + " has degraded..");
					break;
			}
		}
		Item legs = p.getEquipment().get(Equipment.SLOT_BOTTOMS);
		if(legs != null) {
			switch(legs.getId()) {
				case 13893:
				case 13890:
				case 13873:
				case 13861:
					p.getEquipment().set(Equipment.SLOT_BOTTOMS, null);
					ContentEntity.playerGfx(p, 1301);
					p.getActionSender().sendMessage("Your " + legs.getDefinition().getProperName() + " has degraded..");
					break;
			}
		}
		Item helm = p.getEquipment().get(Equipment.SLOT_HELM);
		if(helm != null) {
			switch(helm.getId()) {
				case 13896:
				case 13876:
				case 13864:
					p.getEquipment().set(Equipment.SLOT_HELM, null);
					ContentEntity.playerGfx(p, 1301);
					p.getActionSender().sendMessage("Your " + helm.getDefinition().getProperName() + " has degraded..");
					break;
			}
		}

	}

	private static void checkChaoticDegrades(Player p) {
		Item weapon = p.getEquipment().get(Equipment.SLOT_WEAPON);
		if(weapon == null)
			return;
		switch(weapon.getId()) {
			case 18349:
				p.getEquipment().set(Equipment.SLOT_WEAPON, new Item(18350, 1));
				ContentEntity.playerGfx(p, 1301);
				p.getActionSender().sendMessage("Your " + weapon.getDefinition().getProperName() + " has broken..");
				break;
			case 18351:
				p.getEquipment().set(Equipment.SLOT_WEAPON, new Item(18352, 1));
				ContentEntity.playerGfx(p, 1301);
				p.getActionSender().sendMessage("Your " + weapon.getDefinition().getProperName() + " has broken..");
				break;
			case 18353:
				p.getEquipment().set(Equipment.SLOT_WEAPON, new Item(18354, 1));
				ContentEntity.playerGfx(p, 1301);
				p.getActionSender().sendMessage("Your " + weapon.getDefinition().getProperName() + " has broken..");
				break;
			case 18355:
				p.getEquipment().set(Equipment.SLOT_WEAPON, new Item(18356, 1));
				ContentEntity.playerGfx(p, 1301);
				p.getActionSender().sendMessage("Your " + weapon.getDefinition().getProperName() + " has broken..");
				break;
			case 18357:
				p.getEquipment().set(Equipment.SLOT_WEAPON, new Item(18358, 1));
				ContentEntity.playerGfx(p, 1301);
				p.getActionSender().sendMessage("Your " + weapon.getDefinition().getProperName() + " has broken..");
				break;
		}
		if(p.getEquipment().get(Equipment.SLOT_SHIELD) != null && p.getEquipment().get(Equipment.SLOT_SHIELD).getId() == 18359) {
			p.getActionSender().sendMessage("Your " + p.getEquipment().get(Equipment.SLOT_SHIELD).getDefinition().getProperName() + " has broken..");
			p.getEquipment().set(Equipment.SLOT_SHIELD, new Item(18360, 1));
			ContentEntity.playerGfx(p, 1301);
		}
	}
}
