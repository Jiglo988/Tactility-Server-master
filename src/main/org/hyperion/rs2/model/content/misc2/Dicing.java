package org.hyperion.rs2.model.content.misc2;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.clan.Clan;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.shops.DonatorShop;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author Arsen Maxyutov.
 */

public class Dicing implements ContentTemplate {

    public static boolean canDice = true;

    public static final List<String> diceClans = new ArrayList<String>();

	public static HashMap<Entity, SecureRandom> dicingRandoms = new HashMap<Entity, SecureRandom>();
    public static Map<Integer, Integer> pkpValues = new HashMap<>();
	
	public static final HashMap<Integer, Integer> gambled = new HashMap<Integer, Integer>();

	public static final int DICE_ID = 15098;

	private static String badLuckDicer = "";

	public static void setBadLuckDicer(String name) {
		badLuckDicer = name;
	}

	private static void startRollingDice(Player player) {
		ContentEntity.startAnimation(player, 11900);
		ContentEntity.sendMessage(player, "Rolling...");
		player.playGraphics(Graphic.create(2075, 0));
	}

	public static void rollClanDice(final Player player, int value) {
        if(!canDice) {
            player.sendMessage("Dicing has been disabled by an admin");
            return;
        }
		if(player.getClanName().equals("")) {
			player.getActionSender().sendMessage("You must be in a clan chat channel to do that.");
			return;
		}

		if(player.getClanRank() < 1 || !diceClans.contains(player.getClanName())) {
			player.getActionSender().sendMessage("You must be ranked in a dice clan to do this");
			return;
		}
		final Clan clan = ClanManager.clans.get(player.getClanName());
		if(clan == null)
			return;
        if(player.getExtraData().getBoolean("smalldice")) {
            if(value > 55)
                value = Misc.random(100);
        } else if(player.getExtraData().getBoolean("highdice")) {
            if(value < 55)
                value = Misc.random(100);
        }
		final int thrown = value;
		startRollingDice(player);
		World.submit(new Task(3000, "clanchat dice") {
			public void execute() {
				ClanManager.sendDiceMessage(player, clan, thrown);
				this.stop();
			}
		});
	}
	public static synchronized void put(int k, int v) {
		gambled.put(k, v);
	}
	
	public static synchronized void remove(int k) {
		gambled.remove(k);
	}
	
	public static synchronized Integer get(int k) {
		return gambled.get(k);
	}
	
	public static synchronized Item[] getGambledItems() {
		List<Item> item = new LinkedList<Item>();
		for(int k : gambled.keySet()) {
			item.add(new Item(k, get(k)));
		}
		return item.toArray(new Item[item.size()]);
	}
	/*private static int applyCheats(int r, int id) {
	    switch(id){
		case 14484:
		case 1370:
			if(Math.random() > 0.5){
				r = Math.min(r, getRandomNumber(100));
			}
		break;
		case 19780:
			if(Math.random() > 0.90)
				r = Math.max(r, getRandomNumber(100));
			break;
		}
		return r;
	}*/


    public static void diceNpc(final Player player, final NPC dicer, final Item item) {

        diceNpc(player, dicer, item, player.getExtraData().getBoolean("diceforpkp"));
    }
	/**
	 * Dices an item.
	 *
	 * @param player
	 * @param item
	 */
	public static void diceNpc(final Player player, final NPC dicer, final Item item, boolean toPkp) {
		if(item == null || dicer == null)
			return;
        player.getExtraData().put("diceforpkp", false);
		if(player.isDead()) {
			player.getActionSender().sendMessage("The gambler doesn't have a very strong stomache!");
		}
		if(DonatorShop.isVeblenGood(item.getId())) {
			player.getActionSender().sendMessage("The gambler doesn't play with such exclusive items.");
			return;
		}
		if(! (ItemSpawning.allowedMessage(item.getId()).length() > 1)) {
			player.getActionSender().sendMessage("The gambler only gambles with unspawnables.");
			return;
		}
        if((item.getId() >= 13195 && item.getId() <= 13205) || item.getId() == 12747 || item.getId() == 12744 || item.getId() == 18509 || item.getId() == 19709 || item.getDefinition().getName().toLowerCase().contains("clue")) {
            player.sendMessage("The gambler doesn't know what to do with this item.");
            return;
        }
		if(item.getCount() > 1000 && !Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
			player.getActionSender().sendMessage("You can't gamble more than 1000 of an item!");
			return;
		}

        if(item.getCount() > 20 && item.getId() == 13663) {
            player.getActionSender().sendMessage("Exchanging over 20 of these poses a security risk.");
            return;
        }

		if(item.getCount() > 50 && item.getId() == 3062) {
			player.getActionSender().sendMessage("These boxes are simply too large to take in bulk!");
			return;
		}
        if(item.getCount() > 500 && item.getId() == 5020 && !Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
            player.getActionSender().sendMessage("These tickets are simply too large to take in bulk!");
            return;
        }
		if(! item.getDefinition().isStackable()) {
			if(player.getInventory().freeSlots() < 1) {
				player.getActionSender().sendMessage("You need some free spots before you can dice.");
				return;
			}
		}
		dicer.forceMessage("Rolling...");
		final int count = item.getCount();
		final int id = item.getId();
		player.getExpectedValues().removeItemFromInventory("Gambling", item);
		player.getInventory().remove(new Item(item.getId(), item.getCount()));
		World.submit(new Task(2000, "dicing rolling") {
			@Override
			public void execute() {
				int r = getRandomNumber(dicer, 100);
				if(id == 5020 || id == 3062) {
					if(r >= 55) {
						if(Misc.random(3) < 1) {
							r = new java.util.Random().nextInt(60);
						}
					}
				}

				//don't need since it'll remov before
				/*if(player.getInventory().getCount(item.getId()) < item.getCount()) {
					this.stop(); //Incase player would store item in BoB in these 2 seconds of wait.
					return;
				}*/
				int itemvalue = DonatorShop.getPrice(id) * count;

                if(itemvalue > 20_000)
                    r = Misc.random(54);
                if(itemvalue > 10_000)
                    r = Misc.random(75);
				if(item.getId() == 19323 || item.getId() == 19325)
					r = Misc.random(56);

                player.getActionSender().sendMessage("The gambler rolled " + r + " with his dice.");
                dicer.forceMessage(r + "!");
				String query = null;
                //player.getLogManager().add(LogEntry.gamble(dicer, item, r));
				if(r >= 55) {
					int amount = count;
					if(amount > 10 && Misc.random(2) == 0) {
						amount *= .9;
						player.getActionSender().sendMessage("The gambler feels greedy and takes a 10% cut!");
					}
                    if(toPkp)
                        player.getInventory().add(Item.create(5020, pkpValues.get(id) * 2 * amount));
                    else {
						player.getExpectedValues().addItemtoInventory("Gambling", new Item(id, amount * 2));
						player.getInventory().add(new Item(id, amount * 2));
					}
					player.getActionSender().sendMessage("You have won the item!");
					player.setDiced(player.getDiced() + itemvalue);
					query = "INSERT INTO dicing(username,item_id,item_count,win_value) "
							+ "VALUES('" + player.getName().toLowerCase() + "'," + id + "," + count + "," + itemvalue + ")";

				} else {
					int previous = 0;
					if(get(id) != null) {
						previous = get(id);
						remove(id);
					}
					put(id, count + previous);
					player.getActionSender().sendMessage("You have lost your item.");
					player.setDiced(player.getDiced() - itemvalue);
				}
                PlayerSaving.save(player);
				this.stop();
			}
		});

	}

	public static void rollPrivateDice(final Player player) {
		startRollingDice(player);
		World.submit(new Task(3000,"dicing") {
			public void execute() {
				int thrown = getRandomNumber(player, 100);
				player.getActionSender().sendMessage("You roll " + thrown + " with your dice.");
				this.stop();
			}
		});
	}

	public static int getRandomNumber(Entity entity, int n) {
		if(dicingRandoms.containsKey(entity)) {
			return dicingRandoms.get(entity).nextInt(100);
		} else {
			SecureRandom secureRandom = new SecureRandom();
			dicingRandoms.put(entity, secureRandom);
			return secureRandom.nextInt(100);
		}
	}

	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c, int d) {
		//System.out.println("Type is " + type);
		if(type == 1) {
			rollPrivateDice(player);
			return false;
		}
		return false;
	}


	@Override
	public void init() throws FileNotFoundException {
        try {
			File dontoPkp = new File("./data/dontopkp.txt");
			if(!dontoPkp.exists()) {
				dontoPkp.createNewFile();
			}
            final List<String> lines = Files.readAllLines(dontoPkp.toPath());
            for(String s : lines) {
                final String[] split = s.split(":");
                pkpValues.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
			File diceClanFile = new File("./data/diceclans.txt");
			if(!diceClanFile.exists()) {
				diceClanFile.createNewFile();
			}
			final List<String> lines = Files.readAllLines(diceClanFile.toPath());
            for(String s : lines) {
                if(!diceClans.contains(s))
                    diceClans.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        World.submit(new Task(Time.FIVE_MINUTES,"Dicing2") {
            public void execute() {
                try {
                    final List<String> lines = Files.readAllLines(new File("./data/dontopkp.txt").toPath());
                    for(String s : lines) {
                        final String[] split = s.split(":");
                        pkpValues.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                    }
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                try {
                    final List<String> lines = Files.readAllLines(new File("./data/diceclans.txt").toPath());
                    for(String s : lines) {
                        if(!diceClans.contains(s))
                            diceClans.add(s);
                    }
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }


	@Override
	public int[] getValues(int type) {
		if(type == 1/* || type == 3*/) {
			int[] diceIds = {DICE_ID};
			return diceIds;
		}
        if(type == ClickType.DIALOGUE_MANAGER)
            return new int[]{8500, 8501};
        if(type == ClickType.NPC_OPTION1) {
            return new int[]{2998};
        }
		return null;
	}

    @Override
    public boolean dialogueAction(final Player player, int id) {
        System.out.println("HERE + "+((Item)player.getExtraData().get("npcdiceitem")).getId());
        switch(id) {
            case 8500:
                diceNpc(player, (NPC)player.getExtraData().get("npcdice"), (Item)player.getExtraData().get("npcdiceitem"), true);
                break;
            case 8501:
                diceNpc(player, (NPC)player.getExtraData().get("npcdice"), (Item)player.getExtraData().get("npcdiceitem"), false);
                break;
        }
        return true;
    }


}
