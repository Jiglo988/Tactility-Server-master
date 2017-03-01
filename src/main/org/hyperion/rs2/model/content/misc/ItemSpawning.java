package org.hyperion.rs2.model.content.misc;

import org.hyperion.Configuration;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.EquipmentReq;
import org.hyperion.rs2.model.content.ClickId;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc2.Dicing;
import org.hyperion.rs2.model.content.misc2.NewGameMode;
import org.hyperion.rs2.model.content.misc2.VotingBox;

public class ItemSpawning {

	/**
	 * Max ID that can be spawned.
	 */
	public static final int MAX_ID = 13000;

	/**
	 * Donator Items.
	 */
	private static final String[] DONATOR_NAMES = {
			"party", "chaotic", "h'ween", "santa", "primal", "3rd", "light", "arcane"
	};

	/**
	 * Items that should be exchanged for Pk points.
	 */
	private static final String[] PKPOINTS_NAMES = {
			"void", "defender", "vesta", "statius", "morrigan", "zuriel", "spirit",
	};

	/**
	 * Items that should not be spawned without explanation.
	 */
	private static final String[] UNSPAWNABLE_NAMES = {
			//guthans
	};

	/**
	 * Items that simply shouldn't be spawned by anone.
	 */
	private static final String[] FORBIDDEN_NAMES = {
			"zanik", "crate", "charm", "more", "null"
	};

	/**
	 * Call this method to spawn an item for the specified player.
	 *
	 * @param player
	 * @param id
	 * @param amount
	 */
	public static void spawnItem(Player player, int id, int amount) {
		if (player.getTrader() != null) {
			return;
		}
        if(!player.getLocation().isSpawningAllowed()) {
			player.sendMessage("You cannot spawn items here.");
			return;
		}
		String message = allowedMessage(id);
		if(message.length() > 0) {
			player.getActionSender().sendMessage(message);
			return;
		}
		//player.getLocation();
		spawnItem(id, amount, player);

	}

    public static boolean buy(final int id, final int amount, final Player player) {
        if(amount > 1000 || amount < 1) {
            player.sendMessage("Invalid amount");
            return false;
        }
        final long full_price = NewGameMode.getUnitPrice(Item.create(id, amount));
        if(full_price < 2 || full_price > Integer.MAX_VALUE) {
            player.getActionSender().sendMessage("This item doesn't have a proper price. If its important please contact an admin!");
            return false;
        }
        int price = (int)full_price;
        if(player.getInventory().getCount(995) >= price) {
            return player.getInventory().remove(Item.create(995, price)) == price;
        } else {
            player.sendf("You need %,d coins to spawn %d of %s", price, amount, ItemDefinition.forId(id).getName());
            return false;
        }
    }
	
	public static void spawnItem(int id, int amount, Player player) {
		if(amount >= player.getInventory().freeSlots() && !(new Item(id).getDefinition().isStackable()))
			amount = player.getInventory().freeSlots();
        if(player.hardMode()) {
            if(!buy(id, amount, player))
                return;
        }
		player.getInventory().add(new Item(id, amount));
	}

	public static boolean copyCheck(final Player player) {
		if (player.duelAttackable > 0)
			return false;
		if (player.getPosition().inPvPArea())
			return false;
		if (player.getPosition().inDuel())
			return false;
		if (player.getPosition().inCorpBeastArea())
			return false;
		if (player.getPosition().inArdyPvPArea())
			return false;
		return player.cE.getOpponent() == null;
	}

	public static boolean copyCheck(Item item, Player player) {
		return ItemSpawning.allowedMessage(item.getId()).length() > 0
				|| !EquipmentReq.canEquipItem(player, item.getId());
	}

	public static boolean canSpawn(int id) {
		return !(allowedMessage(id).length() > 0);
	}

    public static boolean canSpawn(final Player player) {
        return canSpawn(player, true);
    }

    public static boolean canSpawn(final Player player, boolean msg) {
        /*if(LastManStanding.inLMSArea(player.cE.getAbsX(), player.cE.getAbsY())) {
            if(msg)player.getActionSender().sendMessage("You cannot spawn items here.");
            return false;
        }*/
        if(player.getPosition().inPvPArea()) {
            if(msg)player.getActionSender().sendMessage(
                    "You cannot do that in a PvP area.");
            return false;
        } else if(player.duelAttackable > 0) {
            if(msg)player.getActionSender().sendMessage(
                    "You cannot do that in the duel arena.");
            return false;
        }else if(player.getTrader() != null){
            if(msg)player.getActionSender().sendMessage("You cannot do this while trading");
            return false;
        }
//        if(player.getLocation().inSDArea()) {
//            player.sendMessage("You cannot spawn here");
//            return false;
//        }
        if(player.getExtraData().getBoolean("cantteleport"))
            return false;
        if(player.getPosition().inDungeonLobby())
            return false;
        if(ContentManager.handlePacket(ClickType.OBJECT_CLICK1
                , player, ClickId.CAN_TELEPORT))
            return false;
        if((player.cE.getAbsX() >= 2500 && player.cE.getAbsY() >= 4630 &&
                player.cE.getAbsX() <= 2539 && player.cE.getAbsY() <= 4660)) {
            if(msg)player.getActionSender().sendMessage("The corporeal beast stops you from spawning!");
            return false;
        }
        if((player.cE.getAbsX() >= 2256 && player.cE.getAbsY() >= 4680 &&
                player.cE.getAbsX() <= 2287 && player.cE.getAbsY() <= 4711) || player.getPosition().distance(Position.create(3068, 10256, 0)) < 8) {
            if(player.getPosition().getZ() == 0) {
                if(msg)player.sendMessage("It's too hot in here to do that!");
            return false;
            }
        }

        if(Combat.inNonSpawnMulti(player.getPosition().getX(), player.getPosition().getY()))
            return false;
        if(player.getLastAttack().timeSinceLastAttack() < 5000) {
            if(msg)player.getActionSender().sendMessage("Aren't you a little preoccupied to be doing that?");
            return false;
        }
		return !FightPits.inPits(player);
	}
	/**
	 * Checks whether an item can be spawned.
	 *
	 * @param id
	 * @return String with length greater than 0 if item cannot be spawned.
	 */
	public static String allowedMessage(int id) {
		switch(id) {
			case Dicing.DICE_ID:
				return "";
		}
        if(ItemDefinition.forId(id).getName().toLowerCase().replaceAll("_", " ").contains("clue scroll"))
            return "You cannot spawn these!";
		if(id > MAX_ID || id <= 0)
			return "You have specified an id that is out of range.";
		/**
		 * Donator Items. eg D claws
		 */
		switch(id) {
			case 2430:
			case 2431:
			case 2438:
			case 2439:
			case 15332:
            case 17999:
			case 15333:
			case 15334:
			case 15335:
			case 14484:
			case 14485:
			case 13444:
			case 14661:
			case 15441:
			case 15442:
			case 15443:
			case 15444:
            case 6603:
            case 6604:
				return "This item can only be purchased in the donator shop.";
		}
		String itemName = ItemDefinition.forId(id).getName().toLowerCase();
		for(String forbiddenName : DONATOR_NAMES) {
			if(itemName.contains(forbiddenName))
				return "This item can only be purchased in the donator shop.";
		}
		/**
		 * Point Items. eg Fighter Torso
		 */
		switch(id) {
			case 10551:
			case 10548:
			case 6570:
			case 5020:
			case 5021:
			case 5022:
			case 5023:
			case 10566:
			case 10637:
				return "This item can only be purchased in the " + Configuration.getString(Configuration.ConfigurationObject.NAME) + " points shop.";
		}
		for(String forbiddenName : PKPOINTS_NAMES) {
			if(itemName.contains(forbiddenName))
				return "This item can only be purchased in the " + Configuration.getString(Configuration.ConfigurationObject.NAME) + " points shop.";
		}
		/**
		 * Forbidden Items. eg Zaniks Crate
		 */
		switch(id) {
            case 995:
            case 12862:
			case 0:
			case 2412:
			case 2413:
			case 2414:
			case 6199:
			case 14889:
			case 14888:
			case 14887:
			case 14885:
			case 14881:
			case 14880:
			case 14879:
			case 14878:
			case 14876:
			case 11724:
			case 11725:
			case 10025:
			case 10026:
            case 11949:
			case 11726:
			case 11727:
			case 12747:
			case 12744:
            case 12852:
			case 1391:
            case 8195:
            case 5068:
            case 4295:
			case 3243:
			case 11061:
			case 11064:
			case 11238:
			case 11239:
			case 11240:
			case 11241:
			case 11242:
			case 11243:
			case 11244:
			case 11245:
			case 11246:
			case 11247:
			case 11248:
			case 11249:
			case 11250:
			case 11251:
			case 11252:
			case 11253:
			case 11254:
			case 11255:
			case 11256:
			case 11257:
			case 11258:
			case 11260:
			case 11261:
			case 10858:
			case 8851:
			case VotingBox.ID:
				return "This item is too sexy for you to spawn.";
		}
        switch(id) {
            case 19605:
                return "You can only buy this in the emblem pt store";
        }
		for(String forbiddenName : FORBIDDEN_NAMES) {
			if(itemName.contains(forbiddenName))
				return "This item is forbidden and therefore cannot be spawned.";
		}
		for(String forbiddenName : UNSPAWNABLE_NAMES) {
			if(itemName.contains(forbiddenName))
				return "This item is un spawnable.";
		}
		return "";
	}


}
