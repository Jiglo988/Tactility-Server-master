package org.hyperion.rs2.model.shops;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.content.minigame.FightPits;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.misc2.MysteryBox;
import org.hyperion.rs2.net.ActionSender;

import java.util.HashMap;

/**
 * @author Arsen Maxyutov.
 */
public class DonatorShop extends Shop {

	public static final double RESELL_RATE = 0.7;

	public static final int[][] VEBLEN_GOODS = {
			{13672, 20000},
			{14479, 20000},
			{13117, 25000}, //put
			{14494, 20000},
	};

	private static HashMap<Integer, Object> donatorItems = new HashMap<Integer, Object>();

	private static boolean first = true;

	public static boolean isVeblenGood(int itemId) {
		for(int[] veblen_good : VEBLEN_GOODS) {
			if(veblen_good[0] == itemId)
				return true;
		}
		return false;
	}

	public void addStaticItem(Item item) {
		super.addStaticItem(item);
		donatorItems.put(item.getDefinition().getNormalId(), new Object());
		//System.out.println("Setting donator : " + item.getDefinition().getNormalId());
	}

	public DonatorShop(int id, String name, Container container) {
		super(id, name, container, false);
	}

	@Override
	public void sellToShop(Player player, Item item) {
		if(player.needsNameChange() || player.doubleChar()) {
			return;
		}
		if(item.getDefinition().getName().contains("partyhat")){
			player.sendf("You cannot sell back rare items to the shop any longer!");
			return;
		}
		if(isVeblenGood(item.getId()) && player.isServerOwner()) {
			getContainer().add(item);
			player.getExpectedValues().sellToStore(item);
			player.getActionSender().sendUpdateItems(3823,
					player.getInventory().toArray());
			updatePlayers();
			for(Player p : World.getPlayers()) {
				if (p != null) {
					p.sendServerMessage("Exclusive items have been added to the donator shop");
					p.sendServerMessage("Only " + item.getCount() + " of these items will ever be sold.");
				}
			}
			return;
		}
        if(item.getId() == LEGENDARY_TICKET || item.getId() == 6603) {
            player.sendMessage("You cannot sell this item back to the store");
            return;
        }
		int price = getPrice(item.getId());
		if(price <= 5) {
			player.getActionSender().sendMessage("This item cannot be sold.");
			return;
		}
		int payment = item.getCount() * price;
		payment *= RESELL_RATE;
		player.getExpectedValues().sellToStore(item);
		player.getInventory().remove(item);
		getContainer().add(item);
		if(payment > 0) {
			player.getPoints().increaseDonatorPoints(payment, false);
		}
		player.getActionSender().sendUpdateItems(3823,
				player.getInventory().toArray());
		updatePlayers();
	}

	@Override
	public void buyFromShop(Player player, Item item) {
		if(player.needsNameChange() || player.doubleChar()) {
			return;
		}
		long lastbuy = player.getExtraData().getLong("lastbuy");
		if(System.currentTimeMillis() - lastbuy < 1000)
			return;
		player.getExtraData().put("lastbuy", System.currentTimeMillis());
		int price = item.getCount() * getPrice(item.getId());
		if(price <= 0 && player.getInventory().freeSlots() != 0) {
			ActionSender.yellModMessage("@dre@" + player.getSafeDisplayName() + " found a unbuyable item in the donator store.");
			return;
		}
		if(player.getPoints().getDonatorPoints() >= price) {
			player.getPoints().setDonatorPoints(player.getPoints().getDonatorPoints() - price);
			this.getContainer().remove(item);
			player.getExpectedValues().buyFromStore(item);
			player.getInventory().add(item);
			player.getActionSender().sendUpdateItems(3823, player.getInventory().toArray());
			updatePlayers();
			if(isVeblenGood(item.getId())) {
				if(first) {
					first = false;
					for(Player p : World.getPlayers()) {
						if (p != null) {
							p.sendServerMessage(player.getSafeDisplayName() + " was the first one to buy an exclusive item today!");
						}
					}
				} else {
					for(Player p : World.getPlayers()) {
						if (p != null) {
							p.sendServerMessage(player.getSafeDisplayName() + " has just bought an exclusive item!");
						}
					}
				}
			}
		} else {
			player.getActionSender().sendMessage(
					"You don't have enough donator points to buy this item.");
		}
	}

	@Override
	public void valueBuyItem(Player player, Item item) {
		int price = getPrice(item.getId());
		if(price <= 0) {
			ActionSender.yellModMessage("@dre@" + player.getSafeDisplayName() + " found a unbuyable item in the donator store.");
			return;
		}
		String message = "The shop will sell a '@dre@"
				+ item.getDefinition().getProperName() + "@bla@' for " + price + " donator points.";
		if(price == 1) {
			message = message.replace("points", "point");
		}

		player.getActionSender().sendMessage(message);
        if(item.getId() == LEGENDARY_TICKET || item.getId() == 6603)
            player.sendImportantMessage("You cannot sell this item back to the shop");
	}

	@Override
	public void valueSellItem(Player player, Item item) {
		int price = getPrice(item.getId());
        if(item.getId() == LEGENDARY_TICKET) {
            player.sendImportantMessage("You cannot sell this back to the store");
            return;
        }
		if(price <= 5) {
			player.getActionSender().sendMessage("This item cannot be sold.");
			return;
		}
		price *= RESELL_RATE;
		String message = "The shop will buy a '@dre@"
				+ item.getDefinition().getProperName() + "@bla@' for " + price + " donator points.";
		if(price == 1) {
			message = message.replace("points", "point");
		}
		player.getActionSender().sendMessage(message);
	}

	@Override
	public void process() {
		for(Item item : getStaticItems()) {
			if(item == null)
				continue;
			if(getContainer().contains(item.getId())) {
				Item shopItem = getContainer().getById(item.getId());
				int delta = item.getCount() - shopItem.getCount();
				if(delta > 0) {
					int addCount = Math.max(delta / 10, 1);
					getContainer().add(new Item(item.getId(), addCount));
				} else if(delta < 0) {
					getContainer().remove(new Item(item.getId()));
				}
			} else {
				getContainer().add(new Item(item.getId()));
			}
		}
		updatePlayers();
	}
	
	public static int getValue(int itemId) {
		switch(itemId) {
		case 11694:	//Ags
		return 1399;

		case 16691:  //novite full helm
		case 17239:  //novite platebody
		case 16669:  //novite platelegs
		case 16909:  //primal 2h sword
		case 16425:  //primal maul
		case 16403:  //primal longsword
		case 15773:  //primal battleaxe
		case 15349: //ardougne cloak 3
        case 19605:
        case 17646:
			return 5000;

		case 15347: //ardougne cloak 2
			return 3500;

            case 18739:
            case 18740:
                return 2000;
            case 17039:
                return 5000;


		case 17341:  //novite kiteshield
		case 16339:  //novite boots
		case 15753:  //novite battleaxe
		case 16889:  //novite 2h sword
		case 16405:  //novite maul
		case 16383:  //novite longsword
			return 3000;

		//case 19713:  //torva helm (disabled)
		case 19714:
		case 19715:
			//case 19716:  //pernix mask (disabled)
		case 19717:
		case 19718:
			//case 19719:  //virtus mask (disabled)
		case 19720:
		case 19721:
		case 16955:  //primal rapier
		case 15345: //ardougne cloak 1
		case 18361:  //eagle eye kiteshield
			return 2500;

		case 16711:
		case 17259:
		case 16689:
		case 17361:
		case 16359:
			return 1999;

		case 14484:  //claws
            return 1499;
		case 16935:  //novite rapier
		case 18363:  //farseer kiteshield
			return 1199;
			
		}
		return 0;
	}
	
	/**
	 * For other sources
	 *
	 * @param itemId
	 * @return
	 */
	public static int getPrice(int itemId) {
        try {
            final ItemDefinition def = ItemDefinition.forId(itemId);
            if(def.isNoted())
		        itemId = def.getNormalId();
        }catch (Exception e) {

        }
		for(int[] veblen_good : VEBLEN_GOODS) {
			if(veblen_good[0] == itemId) {
				return veblen_good[1];
			}
		}
		
		if(ItemSpawning.canSpawn(itemId)) {
			return 0;
		}
		
		if(FightPits.rewardItems.contains(itemId))
			return 1000;
		switch(itemId) {

            case 16401:
                return 5_000;
            case 16953:
                return 4_000;
            case 17135:
                return 3_000;
            case 16687:
                return 2_000;
            case 17257:
                return 3_000;
            case 16709:
                return 1_000;
            case 17359:
                return 1_500;
            case 16357:
                return 2_000;

            case 19325:
                return 2000;
            case 19323:
                return 2500;

			case 17999:
				return 999;

            case 18739:
            case 18740:
                return 2000;
            case 17039:
                return 5000;
			case 16691:  //novite full helm
			case 17239:  //novite platebody
			case 16669:  //novite platelegs
			case 16909:  //primal 2h sword
			case 16425:  //primal maul
			case 16403:  //primal longsword
			case 15773:  //primal battleaxe
			case 15349: //ardougne cloak 3
            case 19605:
            case 17646:
				return 5000;
            case 13663:
                return 1000;

			case 15347: //ardougne cloak 2
				return 3500;

            case 17640:
                return 1000;


			case 17341:  //novite kiteshield
			case 16339:  //novite boots
			case 15753:  //novite battleaxe
			case 16889:  //novite 2h sword
			case 16405:  //novite maul
			case 16383:  //novite longsword
				return 3000;

			//case 19713:  //torva helm (disabled)
			case 19714:
			case 19715:
				//case 19716:  //pernix mask (disabled)
			case 19717:
			case 19718:
				//case 19719:  //virtus mask (disabled)
			case 19720:
			case 19721:
			case 16955:  //primal rapier
			case 15345: //ardougne cloak 1
			case 18361:  //eagle eye kiteshield
				return 2500;

			case 16711:
			case 17259:
			case 16689:
			case 17361:
			case 16359:
				return 1999;

			case 14484:  //claws
                return 1499;
			case 16935:  //novite rapier
			case 18363:  //farseer kiteshield
				return 1199;

			//case 18351:
			//case 18349:
			//case 18353:
			case 18355:
			case 18359:
			case 18357:
				return 899;
				//all chaotics are giving ppl shit tons of "don points"

			case 11794:
				return 1399;
            //phats
			case 1042:
				return 55000; //899
			case 1038:
                return 50000; //899 all phats
			case 1040:
                return 40000;
			case 1044:
                return 44000;
			case 1046:
                return 52500;
			case 1048:
				return 47500;

			case 15042:
				return 1199;

			case 13740:
				return 1999;
				//return 699;
				//divine old - 699
			case 13742:
				return 1499;
				//return 199;
				// ely old - 199
			case 15060:
			case 13744:
				return 399;
				//return 199;
				
			case 13738:
				return 699;
				//return 199;
				//arcane & spec old - 199

			case 13352:
			case 13353:
			case 13354:
			case 13355:
			case 13356:
			case 15241:
			case 19143:
			case 19146:
			case 19149:
			case 1037:
			case 10887:
			case 3140:
			case 15006:
			case 15020:
				return 399;
            //santa - 399 dp
            case 1050:
                return 30000;
            //hweens 399dp
            case 1057:
                return 25000;
            case 1055:
                return 20000;
            case 1053:
                return 17000;

			case 1419:
			case 10696:
			case 11698:
			case 11700:
			case 11696:
			case 19613:
			case 19615:
			case 19617:
			case 18333:
			case 18335:
			case 13736:
				return 299;

			case 11718:
			case 11720:
			//case 11722:
			//case 11724:
			//case 11726:
			case 19459:
			case 19461:
			case 19463:
			case 19465:
				return 199;

			case 11730:
			case 15061:
			case 15062:
			case 15063:
			case 15064:
			case 15065:
			case 15066:
			case 15067:
			case 15068:
				return 150;

			case 10330:
			case 10332:
			case 10334:
			case 10336:
			case 10338:
			case 10340:
			case 10342:
			case 10344:
			case 10346:
			case 10348:
			case 10350:
			case 10352:
			case 13734:
				return 200;

			case 6889:
			case 6914:
			case 10547:
			case 10548:
			case 10549:
			case 10550:
			case 1149:
			case 4087:
			case 4585:
			case MysteryBox.ID:
				return 99;

			case 11728:
			case 15441:
			case 15442:
			case 15443:
			case 15444:
				return 99;

			case 4710:
			case 4718:
			case 4726:
			case 4734:
			case 4747:
			case 4755:
			case 4708:
			case 4712:
			case 4714:
			case 4716:
			case 4720:
			case 4722:
			case 4724:
			case 4728:
			case 4730:
			case 4732:
			case 4736:
			case 4738:
			case 4745:
			case 4749:
			case 4751:
			case 4753:
			case 4757:
			case 4759:
				return 79;

			case 8839:
			case 8840:
				return 75;

			case 6916:
			case 6918:
			case 6920:
			case 6922:
			case 6924:
			case 10551:
				return 50;

			case 7462:
				return 40;

			case 6570:
				return 35;

			case 11663:
			case 11664:
			case 11665:
			case 8842:
				return 30;

			case 7806:
			case 7807:
			case 8849:
			case 8850:
				return 24;

			case 7808:
			case 10499:
			case 8848:
				return 20;

			case 4508:
			case 7809:
				return 18;

			case 8847:
			case 6585:
				return 15;

			case 4509:
				return 14;

			case 4510:
			case 8846:
				return 10;

			case 4511:
			case 4566:
				return 8;

			case 4512:
				return 6;

			case 2439:
			case 8845:
				return 5;
				
			case 2430:    //spec restore potion
				return 3;

			case 13883: //morrigans throwing axe
			case 13879: // morrigans javelin
				return 2;

			case 19152: //saradomin arrow
			case 19157: //guthix arrow
			case 19162: //zamorak arrow
				return 2;

			case 15015: //lunar ring (i)
				return 1;

            case 6603:
                return 0;
		}
		return 0;

	}
}
