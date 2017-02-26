package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.DialogueManager;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.shops.DonatorShop;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.util.Misc;

import java.io.*;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/28/14
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewGameMode implements ContentTemplate {


    private static final String HARD_GAME_GUIDE = "http://forums.tactilitypk.com/topic/10309-hard-game-mode-guide/";
    private static final String NORMAL_GAME_GUIDE = "http://forums.tactilitypk.com/forum/62-golden-guides/";

    /**
     * Bank starter
     */
    private static final int[][] MAIN_STARTER = new int[][] { //{id, amt},
            {562, 10000},
            {563, 10000}, {561, 10000}, {554, 10000}, {557, 10000}, {555, 10000}, {560, 10000},
            {9075, 10000}, {565, 10000}, {391, 10000}, {10828, 100}, {11283, 100}, {11732, 100},
            {7462, 100}, {2414, 100}, {10499, 100}, {6731, 100}, {6733, 100}, {6735, 100},
            {6737, 100}, {4151, 100}, {4587, 100}, {1305, 100}, {1434, 100}, {1215, 100},
            {4153, 100}, {11716, 100}, {6585, 100}, {1725, 100}, {1712, 100}, {4716, 100},
            {4753, 100}, {4745, 100}, {4724, 100}, {4708, 100}, {4732, 100}, {6128, 100},
            {9185, 100}, {2581, 100}, {4675, 100}, {4720, 100}, {4757, 100}, {4749, 100}, {4728, 100},
            {4712, 100}, {4736, 100}, {6129, 100}, {9245, 10000}, {2503, 100}, {6889, 100},  {4722, 100},
            {4759, 100}, {4751, 100}, {4730, 100}, {4714, 100}, {4738, 100}, {6130, 100}, {11235, 100},
            {2497, 100}, {6914, 100}, {4718, 100}, {4755, 100}, {4747, 100}, {4726, 100}, {4710, 100},
            {4734, 100}, {4131, 100}, {11212, 10000}, {2577, 100}, {6920, 100}, {2436, 500}, {2440, 500}, {2442, 500}, {2434, 500},
            {2444, 500}, {3040, 500}, {2446, 500}, {6685, 500},
            {3024, 500}, {15332, 5}, {145, 500}, {157, 500},
            {163, 500}, {139, 500}, {169, 500}, {3042, 500},
            {175, 500}, {6687, 500}, {3026, 500}, {15333, 5}, {147, 500},
            {159, 500}, {165, 500}, {141, 500}, {171, 500}, {3044, 500},
            {177, 500}, {6689, 500}, {3028, 500}, {15334, 5}, {149, 500},
            {161, 500}, {167, 500}, {143, 500}, {173, 500},
            {3046, 500}, {179, 500}, {6691, 500}, {3030, 500}, {15335, 5},
    };

    private static final int IRON_STARTER[][] = {
            {15332, 2},{6685, 100}, {3024, 75}, {2436, 50}, {2440, 50}, {2444, 50}, {3040, 50}, //pots
            {4716, 3}, {4718, 3}, {4720, 3}, {4722, 3}, {6524, 3},//dharoks
            {7462, 1}, {1725, 10}, {11732, 2}, {4151, 2}, {5698, 5}, {2550, 5} //other necessities
            , {391, 500}, {557, 10000}, {9075, 4000}, {560, 2000} //food
    };

    public static final double SELL_REDUCTION = 0.8;


    private static final Map<Integer, Integer> prices;

    public static final Map<Integer, Integer> getPrices() {
        return prices;
    }

    static {
        prices = new TreeMap<Integer, Integer>();
        try(final BufferedReader reader = new BufferedReader(new FileReader(new File("./data/prices.txt")))) {
            for(String s = ""; (s = reader.readLine()) != null;) {
                try {
                    final String[] parts = s.split(" ");
                    prices.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                } catch(final Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch(IOException e) {

        }
    }

    public static long getUnitPrice(final int id) {
        return getUnitPrice(id, 1);
    }

    public static long getUnitPrice(final int id, int amount)  {
        int donorprice = DonatorShop.getPrice(id);
        if(donorprice > 100)
            return BigInteger.valueOf(donorprice).multiply(BigInteger.valueOf(amount)).multiply(BigInteger.valueOf(20_000)).longValueExact();
        return BigInteger.valueOf(prices.getOrDefault(id, 0)).multiply(BigInteger.valueOf(amount)).longValueExact();
    }

    public static long getSetPrice(final Item... items) {
        return Stream.of(items).mapToLong(NewGameMode::getUnitPrice).sum();
    }

    public static long getUnitPrice(final Item item) {
        return item == null ? 0 : getUnitPrice(item.getId(), item.getCount());
    }


    @Override
    public void init() throws FileNotFoundException {

    }

    @Override
    public int[] getValues(int type) {
        if(type == ClickType.DIALOGUE_MANAGER)
            return new int[]{10000, 10001, 10002, 10003, 10004};
        return new int[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    private final static String[] answers = {
            "@blu@Begin tutorial:@bla@ You will receive a '@dre@Fire Cape@bla@' upon completion.",
            "@blu@Skip tutorial:@bla@ You will not receive the tutorial reward."
    };

    @Override
    public boolean dialogueAction(Player player, int dialogueId) {
        switch(dialogueId) {
            case 10000:
                int random = Misc.random(1);
                String answer1 = answers[random];
                String answer2 = answers[random == 1 ? 0 : 1];
                player.getActionSender().sendDialogue("Select an option", ActionSender.DialogueType.OPTION, 1, Animation.FacialAnimation.DEFAULT, answer1, answer2);
                player.getInterfaceState().setNextDialogueId(random, 10002);
                player.getInterfaceState().setNextDialogueId(random == 1 ? 0 : 1, 10003);
                return true;
            case 10001:
                ClanManager.joinClanChat(player, "help", false);
                for(int i = 0; i < MAIN_STARTER.length; i++) {
                    player.getExpectedValues().addItemtoInventory("Starter", Item.create(MAIN_STARTER[i][0], MAIN_STARTER[i][1]));
                    try {
                        if( i >= MAIN_STARTER.length - 40) {
                            player.getBank().add(new BankItem(1, MAIN_STARTER[i][0], MAIN_STARTER[i][1]));
                        } else {
                            player.getBank().add(new BankItem(0, MAIN_STARTER[i][0], MAIN_STARTER[i][1]));
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                player.sendImportantMessage("A starter has been added to your bank!");
                player.sendMessage("l4unchur13 " + NORMAL_GAME_GUIDE);
                player.getActionSender().removeChatboxInterface();
                player.setTutorialProgress(28);
                for(int i = 0; i <= 6; i++) {
                    player.getSkills().setLevel(i, 99);
                    player.getSkills().setExperience(i, Math.max(13100000, player.getSkills().getExperience(i)));
                }
                return true;
            case 10002:
                DialogueManager.openDialogue(player, 2100);
                return true;
            case 10003:
                Magic.teleport(player, Edgeville.POSITION, true);
                player.getActionSender().sendDialogue("Select an option", ActionSender.DialogueType.OPTION,1, Animation.FacialAnimation.DEFAULT,
                        "Normal mode (recommended)", "Hard mode");
                player.getInterfaceState().setNextDialogueId(0, 10001);
                player.getInterfaceState().setNextDialogueId(1, 10004);
                return true;
            case 10004:
                ClanManager.joinClanChat(player, "help", false);
                player.getInventory().add(Item.create(995, 15_000_000));
                for(int i = 0; i < IRON_STARTER.length; i++) {
                    player.getExpectedValues().addItemtoInventory("Starter", Item.create(IRON_STARTER[i][0], IRON_STARTER[i][1]));
                    try {
                        player.getBank().add(new BankItem(0, IRON_STARTER[i][0], IRON_STARTER[i][1]));
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                player.setGameMode(1);
                player.sendMessage("l4unchur13 " + HARD_GAME_GUIDE);
                player.sendImportantMessage("Welcome to Hard Mode", "Check your bank for starter items");
                player.getActionSender().removeChatboxInterface();
                player.setTutorialProgress(28);
                return true;
        }
        return false;
    }

    static {
    }
}
