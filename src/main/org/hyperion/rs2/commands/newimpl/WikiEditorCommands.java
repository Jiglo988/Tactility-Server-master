package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Rank;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//</editor-fold>
/**
 * @author DrHales
 * 2/29/2016
 */
public class WikiEditorCommands implements NewCommandExtension {

    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... input) {
            super(key, Rank.WIKI_EDITOR, delay, input);
        }

        public Command(String key, CommandInput... input) {
            super(key, Rank.WIKI_EDITOR, input);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Collections.emptyList();
    }
    //</editor-fold>
}
