package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.DialogueManager;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.Skills;
import org.hyperion.util.Time;

import java.util.Arrays;
import java.util.List;
//</editor-fold>
/**
 * Created by DrHales on 2/29/2016.
 */
public class DonatorCommands implements NewCommandExtension {
    
    public abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.DONATOR, delay, requiredInput);
        }
        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.DONATOR, requiredInput);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new Command("suicide", Time.THIRTY_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (player.getPosition().inFunPk()) {
                            player.cE.hit(player.getSkills().getLevel(Skills.HITPOINTS), player, true, Constants.MELEE);
                            return true;
                        }
                        player.sendMessage("You cannot use the suicide command outside of funpk.");
                        return true;
                    }
                },
                new Command("dp", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        DialogueManager.openDialogue(player, 158);
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}
