package org.hyperion.rs2.model.cluescroll.reward;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.util.ClueScrollUtils;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.util.Misc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ItemReward extends Reward{

    private int id;

    public ItemReward(final int id, final int minAmount, final int maxAmount, final int chance){
        super(Type.ITEM, minAmount, maxAmount, chance);
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setId(final int id){
        this.id = id;
    }

    protected boolean give(final Player player, final int amount, final int index){
        final Item item = Item.create(id, amount);
        player.getActionSender().sendUpdateItem(6963, index, item);
        final BankItem bankItem = new BankItem(0, id, amount);
        if(player.getInventory().hasRoomFor(item)){
            player.getInventory().add(item);
        } else {
            player.getBank().add(bankItem);
            player.sendf("Your reward has been added to your bank.");
        }
        return true;
    }

    protected boolean give(final Player player, final int amount){
        final Item item = Item.create(id, amount);
        final BankItem bankItem = new BankItem(0, id, amount);
        player.sendf("You receive %s '@dre@%s%s@bla@' as a reward.", (amount == 1 ? Misc.aOrAn(item.getDefinition().getName()) : amount), item.getDefinition().getName(), (amount == 1 ? "" : "s"));
        if(player.getInventory().hasRoomFor(item)){
            player.getInventory().add(item);
        } else {
            player.getBank().add(bankItem);
            player.sendf("Your reward has been added to your bank.");
        }
        return true;
    }

    protected void append(final Document doc, final Element root){
        root.appendChild(ClueScrollUtils.createElement(doc, "id", id));
    }

    public String toString(){
        return String.format("%s: %d", super.toString(), id);
    }

    public static ItemReward parse(final Element element){
        final int itemId = ClueScrollUtils.getInteger(element, "id");
        final int minAmount = ClueScrollUtils.getInteger(element, "minAmount");
        final int maxAmount = ClueScrollUtils.getInteger(element, "maxAmount");
        final int chance = ClueScrollUtils.getInteger(element, "chance");
        return new ItemReward(itemId, minAmount, maxAmount, chance);
    }
}
