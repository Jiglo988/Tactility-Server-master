package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.net.Packet;

public class DropItemPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		int itemId = packet.getShortA();
	    /*junk?*/
		int x = packet.get();
		int y = packet.get();
		int itemSlot = packet.getShortA();
		if(itemId < 0 || itemId > ItemDefinition.MAX_ID || itemSlot < 0 || itemSlot > 27)
			return;
		if((player.isDead() || System.currentTimeMillis() - player.cE.lastHit < 10000) && !ItemSpawning.canSpawn(itemId)) {
			player.getActionSender().sendMessage("You can't drop items, while in combat.");
			return;
		}
		if(player.getSkills().getLevel(3) <= 1) {
			player.sendMessage("You cannot drop items while you have low health.");
			return;
		}
		Item toRemove = player.getInventory().get(itemSlot);
		if(toRemove == null)
			return;
        if(itemId == 15707) {
            player.sendMessage("Perks: ");
            player.sendMessage(player.getDungeoneering().perks.boosts());
            return;
        }
		if(itemId == 12747 || itemId == 12744 || itemId == 18509 || itemId == 19709) {
			player.getActionSender().sendMessage("You cannot drop this item.");
			return;
		}
		if (!player.getDropping().canDrop(toRemove.getId())) {
			player.getActionSender().sendMessage("Please confirm you want to drop this item by dropping it again.");
			return;
		}
		player.getExpectedValues().dropItem(toRemove);
		if(Rank.hasAbility(player, Rank.MODERATOR))
			GlobalItemManager.dropItem(player, itemId, itemSlot);
		else {
			player.getSkills().stopSkilling();
			player.getInventory().remove(toRemove);
		    player.getActionSender().sendMessage("Your item disappears.");
        }
		player.getDropping().reset();


	}


}

