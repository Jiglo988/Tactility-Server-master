package org.hyperion.rs2.model.content;

public final class ClickType {
	/**
	 * mapId - packet
	 * 0 - actionButtons
	 * 1 - bury, eat item
	 * 2 - item option 1
	 * 3 - item option 2
	 * 4 - item option 3
	 * 5 - item option 4
	 * 6 - object 1
	 * 7 - object 2
	 * 8 - object 3
	 * 9 - npc attack
	 * 10 - npc 1
	 * 11 - npc 2
	 * 12 - npc 3
	 * 13 - item on item
	 * 14 - item on object
	 * 15 - item on npc
	 * 16 - npc died
	 * 17 - item option 6
	 * 18 - magic on item
	 * 19 - item on object (2) - for faming
	 * 20 - dialogue manager
	 * 21 - item option 5
	 * 22 - item option 7
	 */
	
	public static final int ACTION_BUTTON = 0, EAT = 1, ITEM_OPTION1 = 2, ITEM_OPTION2 = 3,
			ITEM_OPTION3 = 4, ITEM_OPTION4 = 5, OBJECT_CLICK1 = 6, OBJECT_CLICK2 = 7, OBJECT_CLICK3 = 8,
			NPC_ATTACK = 9, NPC_OPTION1 = 10, NPC_OPTION2 = 11, NPC_OPTION3 = 12, ITEM_ON_ITEM = 13,
			ITEM_ON_OBJECT = 14, ITEM_ON_NPC = 15, NPC_DEATH = 16, ITEM_OPTOION6 = 17,
			MAGIC_ON_ITEM = 18, ITEM_ON_OBJECT2 = 19, DIALOGUE_MANAGER = 20, ITEM_OPTION5 = 21,
			ITEM_OPTION7 = 22;
}
