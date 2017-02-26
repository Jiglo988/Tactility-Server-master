package org.hyperion.rs2.commands.newimpl;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Rank;

import java.util.Collections;
import java.util.List;
/**
 * @author DrHales
 * 2/29/2016
 */
public class HeroCommands implements NewCommandExtension {

    public abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... requiredInput) {
            super(key, Rank.HERO, delay, requiredInput);
        }

        public Command(String key, CommandInput... requiredInput) {
            super(key, Rank.HERO, requiredInput);
        }
    }

    @Override
    public List<NewCommand> init() {
        return Collections.emptyList();
    }
}
