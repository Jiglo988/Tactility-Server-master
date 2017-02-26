package org.hyperion.rs2.model.container;

import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.misc2.MysteryBox;
import org.hyperion.rs2.model.shops.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Shopping utility class.
 *
 * @author Arsen Maxyutov.
 */
public class ShopManager {

    public static final int SIZE = 40;

    public static final int PLAYER_INVENTORY_INTERFACE = 3823;

    public static final int SHOP_INVENTORY_INTERFACE = 3900;

    private static List<Shop> shops = new ArrayList<>();

    public static void open(final Player player, final int value) {
        if (player.getSkills().getLevel(Skills.HITPOINTS) <= 1) {
            player.sendMessage("You cannot open shops when you have low health.");
            return;
        }
        player.getExtraData().put("geshop", 0);
        player.getActionSender().sendInterfaceInventory(3824, 3822);
        player.getActionSender().sendUpdateItems(3823, player.getInventory().toArray());
        Shop shop = Shop.forId(value);
        player.getActionSender().sendUpdateItems(3900, shop.getContainer().toArray());
        player.getActionSender().sendString(3901, value == 78 ? String.format("Emblem Points: @gre@%,d", player.getBountyHunter().getEmblemPoints()) : (value == 63 || value == 64) ? String.format("Donator points: @gre@%,d", player.getPoints().getDonatorPoints()) : value == 75 ? String.format("Voting points: @gre@%,d", player.getPoints().getVotingPoints()) : value == 71 ? String.format("TactilityPk points: @gre@%,d", player.getPoints().getPkPoints()) : value == 76 ? String.format("Honor points: @gre@%,d", player.getPoints().getHonorPoints()) : value == 81 ? String.format("Dungeoneering tokens: @gre@%,d", player.getDungeoneering().getTokens()) : shop.getName());
        player.setShopId(value);
    }

    public static void sellItem(final Player player, final int item, int slot, int amount) {
        if (player.getShopId() < 0 || item == Shop.COINS_ID)
            return;
        if (!ItemsTradeable.isTradeable2(item, player.getGameMode())) {
            player.getActionSender().sendMessage("You cannot sell this item in any shop.");
            return;
        }
        int value = player.getInventory().getCount(item);
        if (value == 0)
            return;
        if (amount > value)
            amount = value;
        Shop shop = Shop.forId(player.getShopId());
        if (shop.isGeneral() || shop.isStatic(item) || player.isServerOwner() && DonatorShop.isVeblenGood(item))
            shop.sellToShop(player, new Item(item, amount));
        else
            player.getActionSender().sendMessage("You can't sell this item to this shop.");
    }

    public static void valueSellItem(Player player, int itemId) {
        if (player.getShopId() < 0 || itemId == Shop.COINS_ID)
            return;
        Item item = player.getInventory().getById(itemId);
        if (item == null)
            return;
        Shop shop = Shop.forId(player.getShopId());
        if (shop.isGeneral() || shop.isStatic(itemId))
            shop.valueSellItem(player, new Item(itemId));
        else
            player.getActionSender().sendMessage("You can't sell this item to this shop.");
    }

    public static void valueBuyItem(Player player, int itemId) {
        if (player.getShopId() < 0 || itemId == 995)
            return;
        Shop shop = Shop.forId(player.getShopId());
        Item item = shop.getContainer().getById(itemId);
        if (item == null)
            return;
        shop.valueBuyItem(player, new Item(itemId));
    }

    public static void buyItem(Player player, int itemId, int slot, int amount) {
        if (player.getShopId() <= -1)
            return;
        if (amount > player.getInventory().freeSlots() && !ItemDefinition.forId(itemId).isStackable())
            amount = player.getInventory().freeSlots();
        Shop shop = Shop.forId(player.getShopId());
        Item shopItem = shop.getContainer().getById(itemId);
        if (shopItem == null)
            return;
        if (amount > shopItem.getCount()) {
            amount = shopItem.getCount();
        }
        shop.buyFromShop(player, new Item(itemId, amount));
    }

    /*TODO: 1 simple boolean | only gets called when checking risk; shouldn't be that big of a deal.*/
    public static int getPoints(int shopId, int itemId) {
        switch(shopId) {
            case 75:
            case 76:
                switch(itemId) {
                    case 17237:
                    case 17017:
                    case 16755:
                    case 18747:
                    case 16865:
                        return 20;
                    case 16931:
                    case 17171:
                        return 10;
                    case 19780:
                        return 50;
                    case 15220: // Imbued rings
                    case 15020:
                    case 15019:
                    case 15018:
                    case 19747:
                    case 13101:
                        return 20;
                    case 15600:
                    case 15606:
                    case 15612:
                    case 15618:
                    case 15602:
                    case 15608:
                    case 15614:
                    case 15620:
                    case 15604:
                    case 15610:
                    case 15616:
                    case 15622:
                    case 15021:
                    case 15022:
                    case 15023:
                    case 15024:
                    case 15025:
                    case 15026:
                    case 15027:
                    case 15028:
                    case 15029:
                    case 15030:
                    case 15031:
                    case 15032:
                    case 15033:
                    case 15034:
                    case 15035:
                    case 15036:
                    case 15037:
                    case 15038:
                    case 15039:
                    case 15040:
                    case 15041:
                    case 15042:
                    case 15043:

                    case 15044:
                        return 5;
                    case 14876:
                    case 2890:
                        return 3;
                }
                break;
            case 71:
                if(itemId >= 8845 && itemId <= 8850) {
                    return (itemId - 8844) * 100;
                }
                switch(itemId) {
                    case 15272:
                        return 1;
                    case 6570:
                    case 8842:
                        return 500;
                    case 8839:
                    case 8840:
                    case 10547:
                    case 10548:
                    case 10549:
                    case 10550:
                        return 750;
                    case 11663:
                    case 11664:
                    case 11665:
                    case 10551:
                        return 1000;
                    case 18333:
                    case 18335:
                        return 1500;
                    case 15243:
                        return 1;
                    case 13902:
                    case 13899:
                        return 5000;
                    case 13887:
                    case 13893:
                    case 13884:
                    case 13890:
                    case 13896:
                        return 2500;
                }
            case 62:// shop id 62
                switch(itemId) {
                    case 4151:
                        return 5000;
                    case 15441:
                    case 15442:
                    case 15443:
                    case 15444:
                        return 8000;
                    case 15600:
                    case 15606:
                    case 15612:
                    case 15618:
                        return 5000;
                    case 15602:
                    case 15608:
                    case 15614:
                    case 15620:
                        return 3000;
                    case 15604:
                    case 15610:
                    case 15616:
                    case 15622:
                        return 4000;

                    case 15021:
                    case 15022:
                    case 15023:
                    case 15024:
                    case 15025:
                    case 15026:
                    case 15027:
                    case 15028:
                    case 15029:
                    case 15030:
                    case 15031:
                    case 15032:
                    case 15033:
                    case 15034:
                    case 15035:
                    case 15036:
                    case 15037:
                    case 15038:
                    case 15039:
                    case 15040:
                    case 15041:
                    case 15042:
                    case 15043:
                    case 15044:
                        return 2500;
                }
                break;
            case 63:
            case 64:
            case 65:
                switch(itemId) {
                    case 16909:
                        return 4000;
                    case 19713:
                    case 19714:
                    case 19715:
                    case 19716:
                    case 19717:
                    case 19718:
                    case 19719:
                    case 19720:
                    case 19721:
                        return 1999;
                    case 16711:
                    case 17259:
                    case 16689:
                    case 17361:
                    case 16359:
                    case 16955:
                        return 1499;
                    case 14484:
                        return 1199;
                    case 18351:
                    case 18349:
                    case 18353:
                    case 18355:
                    case 18359:
                    case 18357:
                        return 899;
                    case 11794:
                        return 999;
                    case 15486:
                    case 1038:
                    case 1040:
                    case 1042:
                    case 1044:
                    case 1046:
                    case 1048:

                        return 899;

                    case 15042:
                        return 1199;
                    case 13740:
                        return 599;
                    case 15060:
                    case 13738:
                    case 13742:
                    case 13744:
                        return 199;
                    case 13352:
                    case 13353:
                    case 13354:
                    case 13355:
                    case 13356:
                    case 15241:
                    case 19143:
                    case 19146:
                    case 19149:
                    case 1050:
                    case 1053:
                    case 1055:
                    case 1057:
                    case 1037:
                    case 10887:
                    case 3140:
                    case 15006:
                    case 15020:
                        return 399;
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
                    case 11722:
                    case 11724:
                    case 11726:
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
                        return 100;
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
                    case 2431:
                    case 2430:
                    case 15332:
                    case 15015:
                        return 1;
                    case 13883:
                    case 13879:
                        return 1;
                    case 19152:
                    case 19157:
                    case 19162:
                        return 1;

                    default:
                        return 50000;

                }
        }
        return Integer.MAX_VALUE;
    }

    public static Shop forId(int id) {
        return shops.get(id);
    }

    public static void reloadShops() {
        shops = new ArrayList<>();
        init();
    }

    public static void init() {
        final long initial = System.currentTimeMillis();
        try (BufferedReader reader = new BufferedReader(new FileReader("./data/newshops.cfg"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] split = line.split(",");
                final int id = Integer.parseInt(split[0]);
                final String name = split[1];
                final String type = split[2];
                final Container container = new Container(Container.Type.ALWAYS_STACK, SIZE);
                final Shop shop = type.contains("donator")
                        ? new DonatorShop(id, name, container) : type.contains("vote")
                        ? new VoteShop(id, name, container) : type.contains("pkshop")
                        ? new PkShop(id, name, container) : type.contains("rlhonorshop")
                        ? new RecklessHonorShop(id, name, container) : type.contains("devhonorshop")
                        ? new DeviousHonorShop(id, name, container) : type.contains("general")
                        ? new CurrencyShop(id, name, container, Shop.COINS_ID, true) : type.contains("specialist")
                        ? new CurrencyShop(id, name, container, Shop.COINS_ID, false) : type.contains("tzhaar")
                        ? new CurrencyShop(id, name, container, 6529, false) : type.contains("slayer")
                        ? new SlayerShop(id, name, container) : type.contains("emblem")
                        ? new EmblemShop(id, name, container) : type.contains("pvm")
                        ? new PvMStore(id, name, container) : type.contains("legendary")
                        ? new LegendaryStore(id, name, container) : type.contains("dungeon")
                        ? new DungeoneeringStore(id, name, container) : null;
                if (shop != null) {
                    for (int array = 3; array < split.length; array++) {
                        final String part = split[array].trim();
                        if (part.length() == 0) {
                            break;
                        }
                        final String[] sub = part.split("-");
                        Item item = new Item(Integer.parseInt(sub[0]), Integer.parseInt(sub[1]));
                        container.add(item);
                        shop.addStaticItem(item);
                    }
                    shops.add(shop);
                }
            }
            reader.close();
        } catch (IOException ex) {
            Server.getLogger().log(Level.WARNING, "Error Parsing Shops.", ex);
        }
        Server.getLogger().info(String.format("Loaded %,d Shops in %,dms.", shops.size(), System.currentTimeMillis() - initial));
        TaskManager.submit(new Task(10000L, "Shops Refreshing Task") {
            @Override
            public void execute() {
                shops.stream().filter(shop -> shop != null).forEach(Shop::process);
            }
        });
    }

}
