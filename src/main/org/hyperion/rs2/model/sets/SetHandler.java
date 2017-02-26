package org.hyperion.rs2.model.sets;

import org.hyperion.rs2.model.DialogueManager;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.misc.ItemSpawning;

public class SetHandler {
	public static final boolean handleSet(Player player, int id) {
		try {

            if(player.hardMode())
                throw new CantSpawnSetException() {
                    @Override public String getMessage() {
                        return "You cannot spawn a set in this game mode";
                    }
                };

			if(!ItemSpawning.canSpawn(player))
				throw new CantSpawnSetException();
			switch(id) {
			case 31421:
				if(! player.canSpawnSet())
					throw new CantSpawnSetException();
				if(ContentEntity.getTotalAmountOfEquipmentItems(player) == 0) {
					if(player.getSkills().getLevels()[Skills.DEFENCE] >= 70 && player.getSkills().getLevels()[Skills.ATTACK] >= 70) {
						SetUtility.getInstantSet(player,
								SetData.getMeleeSet()
						);
						SetUtility.addSetOfItems(player,
								SetData.getMeleeItems());
						return true;
					} else {
						player.getActionSender().sendMessage("You need 70 attack and defense to spawn this!");
					}
				} else {
					throw new ItemsEquippedException();				
				}
				break;
			case 31422:
				if(! player.canSpawnSet())
					throw new CantSpawnSetException();
				if(ContentEntity.getTotalAmountOfEquipmentItems(player) == 0) {
					if(player.getSkills().getLevels()[Skills.DEFENCE] >= 70 && player.getSkills().getLevels()[Skills.RANGED] >= 70) {
						SetUtility.getInstantSet(player,
								SetData.getRangeSet()
						);
						SetUtility.addSetOfItems(player,
								SetData.getRangeItems());
						return true;
					} else {
						player.getActionSender().sendMessage("You need 70 attack and ranged to spawn this!");
					}
				} else {
					throw new ItemsEquippedException();				
				}
				break;
			case 31423:
				if(! player.canSpawnSet())
					throw new CantSpawnSetException();

				if(ContentEntity.getTotalAmountOfEquipmentItems(player) == 0) {
					if(player.getSkills().getLevels()[Skills.DEFENCE] >= 70 && player.getSkills().getLevels()[Skills.MAGIC] >= 70) {
						SetUtility.getInstantSet(player, SetData.getHybridSet());
						SetUtility.addSetOfItems(player, SetData.getHybridItems());
						return true;
					} else {
						player.getActionSender().sendMessage("You need at least 70 magic, attack, and defence to spawn this");
					}
				} else {
					throw new ItemsEquippedException();				
				}
				break;
			case 31424:
				if(!player.canSpawnSet())
					throw new CantSpawnSetException();
				if(ContentEntity.getTotalAmountOfEquipmentItems(player) == 0) {
					DialogueManager.openDialogue(player, 155);
					return true;
				} else {
					throw new ItemsEquippedException();				
				}
			case 31425:
				if(!player.canSpawnSet())
					throw new CantSpawnSetException();
				if(ContentEntity.getTotalAmountOfEquipmentItems(player) == 0) {
					DialogueManager.openDialogue(player, 161);
					return true;
				} else {
					throw new ItemsEquippedException();
				}
			case 31426:
				if(! player.canSpawnSet())
					throw new CantSpawnSetException();

				if(ContentEntity.getTotalAmountOfEquipmentItems(player) == 0) {
					if(player.getSkills().getLevels()[Skills.DEFENCE] >= 55 && player.getSkills().getLevels()[Skills.MAGIC] >= 70) {
						SetUtility.getInstantSet(player, SetData.getWelfSet());
						SetUtility.addSetOfItems(player, SetData.getWelfItems());
						return true;
					} else {
						player.getActionSender().sendMessage("You need at least 55 defence 70 magic & attack to spawn this");
					}
				} else {
					throw new ItemsEquippedException();				
				}
				break;
			}
		} catch(CantSpawnSetException|ItemsEquippedException e) {
			player.sendMessage(e.getMessage());
			return false;
		}
		return false;
	}
}
