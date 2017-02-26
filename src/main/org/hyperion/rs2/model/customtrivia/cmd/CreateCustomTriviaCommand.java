package org.hyperion.rs2.model.customtrivia.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.customtrivia.CustomTrivia;
import org.hyperion.rs2.model.customtrivia.CustomTriviaManager;

public class CreateCustomTriviaCommand extends Command{

    public CreateCustomTriviaCommand(){
        super("createtrivia", Rank.COMMUNITY_MANAGER);
    }

    public boolean execute(final Player player, final String input) throws Exception{
        final String line = filterInput(input).trim();
        final String[] parts = line.split(",");
        if(parts.length != 4 && parts.length != 3){
            player.sendf("Syntax: ::createtrivia question,answer,item_id,item_quantity");
            return false;
        }
        final String question = parts[0].trim();
        if(question.isEmpty()){
            player.sendf("Enter a valid trivia question");
            return false;
        }
        final String answer = parts[1].trim();
        if(answer.isEmpty()){
            player.sendf("Enter a valid trivia answer");
            return false;
        }
        final String itemIdStr = parts[2].trim();
        if(!itemIdStr.matches("\\d{1,5}")){
            player.sendf("Enter a valid item id");
            return false;
        }
        final int itemId = Integer.parseInt(itemIdStr);
        int quantity = 1;
        if(parts.length == 4){
            final String itemQuantityStr = parts[3].trim();
            if(!itemQuantityStr.matches("\\d{1,8}")){
                player.sendf("Enter a valid item quantity");
                return false;
            }
            quantity = Integer.parseInt(itemQuantityStr);
        }
        if(!player.getInventory().contains(itemId)){
            player.sendf("Your inventory does not contain that item id");
            return false;
        }
        final int realQuantity = player.getInventory().getCount(itemId);
        if(quantity > realQuantity){
            player.sendf("Quantity lowered from %,d to %,d", quantity, realQuantity);
            quantity = realQuantity;
        }
        final Item prize = new Item(itemId, quantity);
        player.getInventory().remove(prize);
        final CustomTrivia trivia = new CustomTrivia(player, question, answer, prize);
        CustomTriviaManager.addNew(trivia);
        return true;
    }
}
