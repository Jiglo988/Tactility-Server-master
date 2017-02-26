package org.hyperion.rs2.commands.newimpl;
//<editor-fold defaultstate="collapsed" desc="Imports">
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.NewCommandExtension;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.SpellBook;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.content.skill.Prayer;
import org.hyperion.util.Time;

import java.util.Arrays;
import java.util.List;
//</editor-fold>
/**
 * Created by DrHales on 2/29/2016.
 */
public class SuperDonatorCommands implements NewCommandExtension {

    private abstract class Command extends NewCommand {
        public Command(String key, long delay, CommandInput... input) {
            super(key, Rank.SUPER_DONATOR, delay, input);
        }

        public Command(String key, CommandInput... input) {
            super(key, Rank.SUPER_DONATOR, input);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Commands List">
    @Override
    public List<NewCommand> init() {
        return Arrays.asList(
                new Command("openge", Time.FIVE_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        player.getGrandExchangeTracker().openInterface();
                        return true;
                    }
                },
                new Command("bank", Time.FIVE_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        Bank.open(player, false);
                        return true;
                    }
                },
                new Command("switchprayers", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!player.getLocation().isSpawningAllowed()) {
                            player.sendMessage("You cannot do this here.");
                            return true;
                        }
                        Prayer.changeCurses(player);
                        return true;
                    }
                },
                new Command("lunars", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!player.getLocation().isSpawningAllowed()) {
                            player.sendMessage("You cannot do this here.");
                            return true;
                        }
                        player.getSpellBook().changeSpellBook(SpellBook.LUNAR_SPELLBOOK);
                        player.getActionSender().sendSidebarInterface(6, 29999);
                        return true;
                    }
                },
                new Command("ancients", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!player.getLocation().isSpawningAllowed()) {
                            player.sendMessage("You cannot do this here.");
                            return true;
                        }
                        player.getSpellBook().changeSpellBook(SpellBook.ANCIENT_SPELLBOOK);
                        player.getActionSender().sendSidebarInterface(6, 12855);
                        return true;
                    }
                },
                new Command("moderns", Time.FIFTEEN_SECONDS) {
                    @Override
                    protected boolean execute(Player player, String[] input) {
                        if (!player.getLocation().isSpawningAllowed()) {
                            player.sendMessage("You cannot do this here.");
                            return true;
                        }
                        player.getSpellBook().changeSpellBook(SpellBook.REGULAR_SPELLBOOK);
                        player.getActionSender().sendSidebarInterface(6, 1151);
                        return true;
                    }
                }
        );
    }
    //</editor-fold>
}
