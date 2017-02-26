package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.util.TextUtils;

/**
 * @author DrHales
 *         5/12/2016
 */
public class PointCommand extends NewCommand {

    private final Type type;

    public PointCommand(String key, Rank rank, Type type) {
        super(key, rank, new CommandInput<>(World::playerIsOnline, "Player", "an Online Player"), new CommandInput<Integer>(integer -> integer > Integer.MIN_VALUE && integer < Integer.MAX_VALUE, "Integer", "Amount of Points"));
        this.type = type;
    }

    @Override
    public boolean execute(final Player player, final String[] input) {
        final Player target = World.getPlayerByName(input[0].trim());
        final int amount = Integer.parseInt(input[1].trim());
        type.increment(target, amount);
        assert target != null;
        player.sendf("%s has been given %,d %s points and now has %,d.", TextUtils.titleCase(target.getName()), amount, TextUtils.titleCase(String.valueOf(type)), type.getPoints(target));
        target.sendf("%s has given you %,d %s points. You now have %,d.", TextUtils.titleCase(player.getName()), amount, TextUtils.titleCase(String.valueOf(type)), type.getPoints(target));
        return true;
    }

    public enum Type {
        DUNGEONEERING {
            @Override
            public void increment(final Player player, final int value) {
                player.getDungeoneering().setTokens(player.getDungeoneering().getTokens() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getDungeoneering().getTokens();
            }
        },
        EMBLEM {
            @Override
            public void increment(final Player player, final int value) {
                player.getBountyHunter().setEmblemPoints(player.getBountyHunter().getEmblemPoints() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getBountyHunter().getEmblemPoints();
            }
        },
        SLAYER {
            @Override
            public void increment(final Player player, final int value) {
                player.getSlayer().setSlayerPoints(player.getSlayer().getSlayerPoints() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getSlayer().getSlayerPoints();
            }
        },
        BOUNTY {
            @Override
            public void increment(final Player player, final int value) {
                player.getBountyHunter().setKills(player.getBountyHunter().getKills() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getBountyHunter().getKills();
            }
        },
        VOTE {
            @Override
            public void increment(final Player player, final int value) {
                player.getPoints().setVotingPoints(player.getPoints().getVotingPoints() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getPoints().getVotingPoints();
            }
        },
        PKING {
            @Override
            public void increment(final Player player, final int value) {
                player.getPoints().setPkPoints(player.getPoints().getPkPoints() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getPoints().getPkPoints();
            }
        },
        ELO {
            @Override
            public void increment(final Player player, final int value) {
                player.getPoints().setEloRating(player.getPoints().getEloRating() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getPoints().getEloRating();
            }
        },
        HONOR {
            @Override
            public void increment(final Player player, final int value) {
                player.getPoints().setHonorPoints(player.getPoints().getHonorPoints() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getPoints().getHonorPoints();
            }
        },
        KILL {
            @Override
            public void increment(final Player player, final int value) {
                player.setKillCount(player.getKillCount() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getKillCount();
            }
        },
        DEATH {
            @Override
            public void increment(final Player player, final int value) {
                player.setDeathCount(player.getDeathCount() + value);
            }

            @Override
            public int getPoints(final Player player) {
                return player.getDeathCount();
            }
        };

        public abstract void increment(Player player, int value);

        public abstract int getPoints(Player player);

    }

}
