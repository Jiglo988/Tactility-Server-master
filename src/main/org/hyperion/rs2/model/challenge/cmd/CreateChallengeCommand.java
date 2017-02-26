package org.hyperion.rs2.model.challenge.cmd;

import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.challenge.Challenge;
import org.hyperion.rs2.model.challenge.ChallengeManager;
import org.hyperion.rs2.model.content.misc.ItemSpawning;

public class CreateChallengeCommand extends Command{

    public CreateChallengeCommand(){
        super("createchallenge", Rank.COMMUNITY_MANAGER);
    }

    public boolean execute(final Player player, final String input){
        final String line = filterInput(input).trim();
        final int i = line.indexOf(',');
        if(i == -1){
            player.sendf("Incorrect syntax: ::createchallenge length,id (amount");
            return false;
        }
        int length;
        try{
            length = Integer.parseInt(line.substring(0, i).trim());
            if(length < 15 || length > 70)
                throw new Exception();
        }catch(Exception ex){
            player.sendf("Enter a valid length (15-70)");
            return false;
        }
        final String[] itemParts = line.substring(i+1).trim().split(" +");
        int id;
        int amount = 1;
        try{
            id = Integer.parseInt(itemParts[0].trim());
            if(itemParts.length == 2)
                amount = Integer.parseInt(itemParts[1].trim());
        }catch(Exception ex){
            player.sendf("Error parsing prize");
            return false;
        }
        if(ItemSpawning.canSpawn(id)){
            player.sendf("You cannot do this with spawnables");
            return false;
        }
        final Item item = player.getInventory().getById(id);
        if(item == null){
            player.sendf("No item in inventory with id: %d", id);
            return false;
        }
        if(item.getDefinition().isStackable()){
            if(amount > item.getCount())
                player.sendf("Lowered amount from %,d to %,d", amount, amount = item.getCount());
        }else{
            int maxCount = 0;
            for(final Item it : player.getInventory().toArray())
                if(it != null && it.getId() == id)
                    maxCount++;
            if(amount > maxCount)
                player.sendf("Lowered amount from %,d to %,d", amount, amount = item.getCount());
        }
        player.getInventory().remove(new Item(id, amount));
        final Challenge challenge = Challenge.create(player, length, id, amount);
        ChallengeManager.add(challenge);
        for(final Player p : World.getPlayers())
            if(p != null)
                challenge.send(p, true);
        return true;
    }

}
