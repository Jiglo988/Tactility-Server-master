
package org.hyperion.rs2.model.punishment.cmd;

import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.punishment.*;
import org.hyperion.rs2.model.punishment.holder.PunishmentHolder;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;
import org.hyperion.rs2.saving.IOData;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.util.TextUtils;

import java.util.concurrent.TimeUnit;

public class PunishCommand extends NewCommand {
    
    private final Target target;
    private final Type type;
    private final Combination combination;

    public PunishCommand(String key, Rank rank, Target target, Type type) {
        super(key, rank, new CommandInput<>(PlayerLoading::playerExists, "Player", "An Existing Player"), new CommandInput<String>(string -> string != null && Integer.parseInt(string.split(" ")[0].trim()) > 0 && Unit.getUnit(string.split(" ")[1]) != null, "String", "Duration & Unit"), new CommandInput<String>(string -> string != null, "String", "Punishment Reason"));
        this.target = target;
        this.type = type;
        this.combination = Combination.of(target, type);
    }

    @Override
    public boolean execute(final Player player, final String[] input) {
        final String victim = input[0].trim();
        final Player other = World.getPlayerByName(victim);
        final Unit unit = Unit.getUnit(input[1].split(" ")[1].trim());
        assert unit != null;
        final long duration = unit.getDuration(Integer.parseInt(input[1].split(" ")[0].trim()));
        final String reason = input[2].trim();
        if (other != null && Rank.isStaffMember(other) && !Rank.hasAbility(player, Rank.DEVELOPER)) {
            player.sendMessage("You cannot punish other staff members.");
            return true;
        }
        player.sendMessage("Loading Target information; Please wait...");
        GameEngine.submitIO(new EngineTask<Boolean>("Punishment Command", 8, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                String protocol = other != null ? other.getShortIP() : PlayerLoading.getProperty(victim, IOData.LAST_IP).get().getAsString();
                if (protocol.contains("=")) {
                    protocol = protocol.substring(protocol.indexOf('/') + 1, protocol.indexOf(':'));
                }
                final int mac = other != null ? other.getUID() : PlayerLoading.getProperty(victim, IOData.LAST_MAC).get().getAsInt();
                final int[] uid = other != null ? other.specialUid : new int[20];
                final Time time = Time.create(duration, unit.getUnit());
                final PunishmentHolder holder = PunishmentManager.getInstance().get(victim);
                final Punishment old = holder != null ? holder.get(combination) : null;
                final String ip = protocol;
                TaskManager.submit(new Task(200L, "Finishing Punishment") {
                    @Override
                    public void execute() {
                        stop();
                        if (holder != null && old != null) {
                            old.setIssuer(player);
                            old.getTime().setStartTime(System.currentTimeMillis());
                            old.getTime().set(time);
                            old.setReason(reason);
                            if (other != null) {
                                old.send(other, true);
                            }
                            old.send(player, true);
                            old.update();
                        } else {
                            final Punishment punishment = Punishment.create(player, victim, ip, mac, uid, combination, time, reason);
                            if (other != null) {
                                punishment.send(other, true);
                            }
                            punishment.send(player, true);
                            punishment.apply();
                            PunishmentManager.getInstance().add(punishment);
                            punishment.insert();
                        }
                        player.sendf("[Punished]:%s,%s,%s,%s,%s", TextUtils.titleCase(victim), capitalize(unit), reason, capitalize(target), capitalize(type));
                    }
                });
                return true;
            }

            @Override
            public void stopTask() {
                player.sendf("Task Timed out punished player %s. Please try again later...", TextUtils.titleCase(victim));
            }
        });
        return true;
    }

    private String capitalize(final Object value) {
        return Character.toString(String.valueOf(value).charAt(0)).toUpperCase() + String.valueOf(value).substring(1).toLowerCase();
    }

    private enum Unit {
        SECOND(TimeUnit.SECONDS) {
            @Override
            public int getDuration(int value) {
                return value;
            }
        },
        MINUTE(TimeUnit.MINUTES) {
            @Override
            public int getDuration(int value) {
                return value;
            }
        },
        HOUR(TimeUnit.HOURS) {
            @Override
            public int getDuration(int value) {
                return value;
            }
        },
        DAY(TimeUnit.DAYS) {
            @Override
            public int getDuration(int value) {
                return value;
            }
        },
        WEEK(TimeUnit.DAYS) {
            @Override
            public int getDuration(int value) {
                return value * 7;
            }
        },
        MONTH(TimeUnit.DAYS) {
            @Override
            public int getDuration(int value) {
                return value * 30;
            }
        },
        YEAR(TimeUnit.DAYS) {
            @Override
            public int getDuration(int value) {
                return value * 365;
            }
        },
        DECADE(TimeUnit.DAYS) {
            @Override
            public int getDuration(int value) {
                return value * 3652;
            }
        },
        CENTURY(TimeUnit.DAYS) {
            @Override
            public int getDuration(int value) {
                return value * 36524;
            }
        };

        private final TimeUnit unit;

        Unit(TimeUnit unit) {
            this.unit = unit;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public abstract int getDuration(int value);

        public static Unit getUnit(final String value) {
            return value.toLowerCase().contains(String.valueOf(SECOND).toLowerCase())
                    ? SECOND : value.toLowerCase().contains(String.valueOf(MINUTE).toLowerCase())
                    ? MINUTE : value.toLowerCase().contains(String.valueOf(HOUR).toLowerCase())
                    ? HOUR : value.toLowerCase().contains(String.valueOf(DAY).toLowerCase())
                    ? DAY : value.toLowerCase().contains(String.valueOf(WEEK).toLowerCase())
                    ? WEEK : value.toLowerCase().contains(String.valueOf(MONTH).toLowerCase())
                    ? MONTH : value.toLowerCase().contains(String.valueOf(YEAR).toLowerCase())
                    ? YEAR : value.toLowerCase().contains(String.valueOf(DECADE).toLowerCase())
                    ? DECADE : value.toLowerCase().contains(String.valueOf(CENTURY).toLowerCase())
                    ? CENTURY : null;
        }
    }
}