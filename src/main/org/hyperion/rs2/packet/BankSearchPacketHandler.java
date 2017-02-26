package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.TextUtils;

import java.util.Arrays;

/**
 * Created by User on 3/20/2015.
 */
public class BankSearchPacketHandler implements PacketHandler {

    @Override
    public void handle(Player player, Packet packet) {
        long textAsLong = packet.getLong();
        String text = TextUtils.hashToUsername(textAsLong);
        text = text.replaceAll("_", " ").trim().toLowerCase();
        searchFor(player, text);
    }

    public static void searchFor(Player player, String keyword) {
        player.getBankField().setSearching(true);
        player.getBankField().setSearchText(keyword);
        player.getActionSender().sendClientConfig(1000 + player.getBankField().getTabIndex(), 0);
        if (keyword == null)
            return;
        keyword = keyword.trim().toLowerCase();
        Item[] resultContainer = new BankItem[Bank.SIZE];
        int caret = 0;
        if (keyword.length() > 0) {
            for (Item item : player.getBank().toArray())  {
                if (item == null) {
                    continue;
                }
                if (item.getDefinition() == null) {
                    continue;
                }
                if (item.getDefinition().getName() == null) {
                    continue;
                }
                if (item.getDefinition().getName().replaceAll("_", " ").trim().toLowerCase().contains(keyword)) {
                    resultContainer[caret++] = item;
                }
            }
        } else {
            int from = player.getBankField().getOffset(player.getBankField().getTabIndex());
            int to = from + player.getBankField().getTabAmounts()[player.getBankField().getTabIndex()];
            resultContainer = Arrays.copyOf(Arrays.copyOfRange(player.getBank().toArray(), from, to), Bank.SIZE);
        }

        player.getActionSender().sendUpdateItems(Bank.BANK_INVENTORY_INTERFACE, resultContainer);
    }

}
