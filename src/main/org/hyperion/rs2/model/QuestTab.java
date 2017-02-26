package org.hyperion.rs2.model;

import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.combat.EloRating;
import org.hyperion.rs2.model.content.Events;
import org.hyperion.rs2.model.content.Lock;
import org.hyperion.rs2.model.content.bounty.BountyPerks;
import org.hyperion.rs2.packet.ActionsManager;
import org.hyperion.rs2.packet.ButtonAction;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Gilles on 16/02/2016.
 */
public class QuestTab {

    public enum QuestTabComponent implements ButtonAction {
        EMPTY_LINE {
            @Override
            public String toString() {
                return "";
            }
        },
        SERVER_INFORMATION {
            @Override
            public QuestTabComponent[] containsComponents() {
                return new QuestTabComponent[]{PLAYERS_ONLINE, STAFF_ONLINE, BONUS_SKILL, UPTIME, EVENT};
            }
        },
        PLAYERS_ONLINE {
            @Override
            public String updateValue(Player player) {
                return Integer.toString((int)(World.getPlayers().size() * Configuration.getDouble(Configuration.ConfigurationObject.PLAYER_MULTIPLIER)));
            }

            @Override
            public boolean handle(Player player, int id) {
                final List<Player> list = World.getPlayers().stream().filter(other -> !other.isHidden()).collect(Collectors.toList());
                player.sendf("There is currently '%,d' player%s playing TactilityPk.", list.size(), list.size() != 1 ? "s" : "");
                return true;
            }
        },
        STAFF_ONLINE {
            @Override
            public String updateValue(Player player) {
                return Integer.toString(StaffManager.getOnlineStaff().size());
            }

            @Override
            public boolean handle(Player player, int id) {
                List<Player> onlineStaff = StaffManager.getOnlineStaff();
                player.getActionSender().sendMessage("Staff online: @dre@" + onlineStaff.size());
                for (Player staffMember : onlineStaff) {
                    final Rank rank = Rank.getPrimaryRank(staffMember);
                    player.getActionSender().sendMessage(String.format(
                            "[%s%s@bla@] - %s%s",
                            rank.getYellColor(), staffMember.display == null || staffMember.display.isEmpty() ? staffMember.getName() : staffMember.display,
                            rank.getYellColor(), rank));
                }
                return true;
            }
        },
        BONUS_SKILL {
            @Override
            public String updateValue(Player player) {
                return Misc.getSkillName(Skills.BONUS_SKILL);
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Show future bonus skills";
            }

            @Override
            public boolean handle(Player player, int id) {
                Calendar c = Calendar.getInstance();
                player.sendMessage("@dre@The next bonus skills will be; ");
                int dayOfYear = (c.get(Calendar.DAY_OF_YEAR) + 4);
                for (int i = 1; i <= 3; i++) {
                    int bonusSkill = ((dayOfYear + i) % (Skills.SKILL_COUNT - 8)) + 7;
                    if (bonusSkill == 21) {
                        player.sendMessage("@dre@" + i + ". @bla@Random");
                    } else {
                        player.sendMessage("@dre@" + i + ". @bla@" + Misc.getSkillName(bonusSkill));
                    }
                }
                return true;
            }
        },
        UPTIME {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.ADMINISTRATOR);
            }

            @Override
            public String updateValue(Player player) {
                return Server.getUptime().toString();
            }
        },
        EVENT {
            @Override
            public boolean hasComponent(Player player) {
                return !Events.eventName.equals("");
            }

            @Override
            public String updateValue(Player player) {
                return Events.eventName;
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Teleport to event";
            }

            @Override
            public boolean handle(Player player, int id) {
                if (Objects.equals(Events.eventName, ""))
                    return false;
                Events.joinEvent(player);
                return true;
            }
        },
        PK_INFORMATION {
            @Override
            public QuestTabComponent[] containsComponents() {
                return new QuestTabComponent[]{KILLS, DEATHS, KILL_DEATH, PVP_RATING, ITEMS_KEPT_ON_DEATH};
            }
        },
        KILLS {
            @Override
            public boolean hasComponent(Player player) {
                return player.getKillCount() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getKillCount());
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                player.forceMessage("I have " + (player.getKillCount() == 0 ? "no" : player.getKillCount()) + " " + (player.getKillCount() == 1 ? "kill" : "kills") + " so far.");
                return true;
            }
        },
        DEATHS {
            @Override
            public boolean hasComponent(Player player) {
                return player.getDeathCount() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getDeathCount());
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                player.forceMessage("I have " + (player.getDeathCount() == 0 ? "no" : player.getDeathCount()) + " " + (player.getDeathCount() == 1 ? "death" : "deaths") + " so far.");
                return true;
            }
        },
        KILL_DEATH {
            @Override
            public boolean hasComponent(Player player) {
                return player.getKDR() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Double.toString(player.getKDR());
            }

            @Override
            public String toString() {
                return "Kill/Death";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                player.forceMessage(String.format("My kill/deathratio is %.2f.", player.getKDR()));
                return true;
            }
        },
        PVP_RATING {
            @Override
            public boolean hasComponent(Player player) {
                return player.getPoints().getEloRating() != EloRating.DEFAULT_ELO_START_RATING;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getPoints().getEloRating());
            }

            @Override
            public String toString() {
                return "PvP rating";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                player.forceMessage("My PvP rating is " + player.getPoints().getEloRating() + ". " + (player.getPoints().getEloRating() == player.getPoints().getEloPeak() ? "This is also my best PvP rating ever." : "My best PvP rating ever was " + player.getPoints().getEloPeak() + "."));
                return true;
            }
        },
        ITEMS_KEPT_ON_DEATH {
            @Override
            public String getMouseOverText(Player player) {
                return "Open " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                player.getActionSender().openItemsKeptOnDeathInterface(player);
                return true;
            }
        },
        INGAME_POINTS {
            @Override
            public QuestTabComponent[] containsComponents() {
                return new QuestTabComponent[]{PK_POINTS, VOTING_POINTS, DONATOR_POINTS, DONATOR_POINTS_BOUGHT, HONOR_POINTS};
            }
        },
        PK_POINTS {
            @Override
            public boolean hasComponent(Player player) {
                return player.getPoints().getPkPoints() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getPoints().getPkPoints());
            }

            @Override
            public String toString() {
                return Configuration.getString(Configuration.ConfigurationObject.NAME) + " points";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                player.forceMessage("I have " + (player.getPoints().getPkPoints() == 0 ? "no" : player.getPoints().getPkPoints()) + " " + (player.getPoints().getPkPoints() == 1 ? "TactilityPk point" : "TactilityPk points") + ".");
                return true;
            }
        },
        VOTING_POINTS {
            @Override
            public boolean hasComponent(Player player) {
                return player.getPoints().getVotingPoints() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getPoints().getVotingPoints());
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                DialogueManager.openDialogue(player, 540);
                return true;
            }
        },
        DONATOR_POINTS {
            @Override
            public boolean hasComponent(Player player) {
                return player.getPoints().getDonatorPoints() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getPoints().getDonatorPoints());
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                player.forceMessage(player.getPoints().getDonatorPointsBought() == 0 ? "I have never bought any donator points." : "I bought " + player.getPoints().getDonatorPointsBought() + " donator points for $" + player.getPoints().getDonatorPointsBought() / 100 + " and " + (player.getPoints().getDonatorPointsBought() == player.getPoints().getDonatorPoints() ? "still have them all." : "still have " + player.getPoints().getDonatorPoints() + " of them left."));
                return true;
            }
        },
        DONATOR_POINTS_BOUGHT {
            @Override
            public boolean hasComponent(Player player) {
                return player.getPoints().getDonatorPointsBought() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getPoints().getDonatorPointsBought());
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                player.forceMessage(player.getPoints().getDonatorPointsBought() == 0 ? "I have never bought any donator points." : "I bought " + player.getPoints().getDonatorPointsBought() + " donator points for $" + player.getPoints().getDonatorPointsBought() / 100 + " and " + (player.getPoints().getDonatorPointsBought() == player.getPoints().getDonatorPoints() ? "still have them all." : "still have " + player.getPoints().getDonatorPoints() + " of them left."));
                return true;
            }
        },
        HONOR_POINTS {
            @Override
            public boolean hasComponent(Player player) {
                return player.getPoints().getHonorPoints() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getPoints().getHonorPoints());
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                DialogueManager.openDialogue(player, 530);
                return true;
            }
        },
        BOUNTY_HUNTER {
            @Override
            public QuestTabComponent[] containsComponents() {
                return new QuestTabComponent[]{BOUNTY_HUNTER_POINTS, BOUNTY_HUNTER_STATUS, BOUNTY_HUNTER_PERKS};
            }
        },
        BOUNTY_HUNTER_POINTS {
            @Override
            public boolean hasComponent(Player player) {
                return player.getBountyHunter().getKills() != 0;
            }

            @Override
            public String updateValue(Player player) {
                return Integer.toString(player.getBountyHunter().getKills());
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Yell " + super.toString();
            }

            @Override
            public String toString() {
                return "Bounty H. points";
            }

            @Override
            public boolean handle(Player player, int id) {
                player.forceMessage("I have " + (player.getBountyHunter().getKills() == 0 ? "no" : player.getBountyHunter().getKills()) + " " + (player.getBountyHunter().getKills() == 1 ? "BH point" : "BH points") + ".");
                return true;
            }
        },
        BOUNTY_HUNTER_STATUS {
            @Override
            public String updateValue(Player player) {
                return Lock.isEnabled(player, Lock.BOUNTY_HUNTER) ? "Disabled" : "Enabled";
            }

            @Override
            public String toString() {
                return "Bounty H. status";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Toggle " + super.toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                Lock.switchLock(player, Lock.BOUNTY_HUNTER);
                player.getQuestTab().updateComponent(this);
                return true;
            }
        },
        BOUNTY_HUNTER_PERKS {
            @Override
            public String toString() {
                return "Click to see the BH perks";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "See " + super.toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                int perk1 = 0;
                int perk2 = 0;
                int perk3 = 0;
                for (int i = -1; i < player.getBHPerks().hasPerk(BountyPerks.Perk.SPEC_RESTORE); i++) {
                    perk1++;
                }
                for (int i = -1; i < player.getBHPerks().hasPerk(BountyPerks.Perk.VENG_REDUCTION); i++) {
                    perk2++;
                }
                for (int i = -1; i < player.getBHPerks().hasPerk(BountyPerks.Perk.PRAY_LEECH); i++) {
                    perk3++;
                }
                player.sendMessage("@dre@" + (perk1 == 0 ? "" : "(Level " + perk1 + ") ") + "Special perk:@bla@ Increase special attack after a kill.");
                player.sendMessage("@dre@" + (perk2 == 0 ? "" : "(Level " + perk2 + ") ") + "Veng reduction:@bla@ Reduce cooldown on vengeance.");
                player.sendMessage("@dre@" + (perk3 == 0 ? "" : "(Level " + perk3 + ") ") + "Prayer leech:@bla@ Leech opponent's prayer on hit.");
                return true;
            }
        },
        LOCKS {
            @Override
            public QuestTabComponent[] containsComponents() {
                return new QuestTabComponent[]{YELL_LOCK, YELL_TITLES_LOCK, TRIVIA, STAFF_LOGIN_MESSAGES, PK_MESSAGES, LOOT_MESSAGES, EXPERIENCE_LOCK};
            }
        },
        YELL_LOCK {
            @Override
            public String updateValue(Player player) {
                return !Lock.isEnabled(player, Lock.YELL) ? "Disabled" : "Enabled";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Toggle " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                Lock.switchLock(player, Lock.YELL);
                player.getQuestTab().updateComponent(this);
                return true;
            }
        },
        YELL_TITLES_LOCK {
            @Override
            public String updateValue(Player player) {
                return !Lock.isEnabled(player, Lock.YELL_TITLES) ? "Disabled" : "Enabled";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Toggle " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                Lock.switchLock(player, Lock.YELL_TITLES);
                player.getQuestTab().updateComponent(this);
                return true;
            }
        },
        TRIVIA {
            @Override
            public String updateValue(Player player) {
                return Lock.isEnabled(player, Lock.TRIVIA) ? "Enabled" : "Disabled";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Toggle " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                Lock.switchLock(player, Lock.TRIVIA);
                player.getQuestTab().updateComponent(this);
                return true;
            }
        },
        STAFF_LOGIN_MESSAGES {
            @Override
            public String updateValue(Player player) {
                return Lock.isEnabled(player, Lock.STAFF_LOGIN) ? "Disabled" : "Enabled";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Toggle " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                Lock.switchLock(player, Lock.STAFF_LOGIN);
                player.getQuestTab().updateComponent(this);
                return true;
            }
        },
        PK_MESSAGES {
            @Override
            public String updateValue(Player player) {
                return Lock.isEnabled(player, Lock.PK_MESSAGES) ? "Disabled" : "Enabled";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Toggle " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                Lock.switchLock(player, Lock.PK_MESSAGES);
                player.getQuestTab().updateComponent(this);
                return true;
            }
        },
        LOOT_MESSAGES {
            @Override
            public String updateValue(Player player) {
                return Lock.isEnabled(player, Lock.LOOT_MESSAGES) ? "Disabled" : "Enabled";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Toggle " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                Lock.switchLock(player, Lock.LOOT_MESSAGES);
                player.getQuestTab().updateComponent(this);
                return true;
            }
        },
        EXPERIENCE_LOCK {
            @Override
            public String updateValue(Player player) {
                return !Lock.isEnabled(player, Lock.EXPERIENCE_LOCK) ? "Disabled" : "Enabled";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Toggle " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                Lock.switchLock(player, Lock.EXPERIENCE_LOCK);
                player.getQuestTab().updateComponent(this);
                return true;
            }
        },
        RANK {
            @Override
            public QuestTabComponent[] containsComponents() {
                return new QuestTabComponent[]{PLAYER, HERO, LEGEND, VETERAN, DONATOR, SUPER_DONATOR, WIKI_EDITOR, EVENT_MANAGER, HELPER, FORUM_MODERATOR, MODERATOR, GLOBAL_MODERATOR, COMMUNITY_MANAGER, HEAD_MODERATOR, ADMINISTRATOR, DEVELOPER, OWNER};
            }

            @Override
            public boolean hasComponent(Player player) {
                return player.getPlayerRank() != 1;
            }
        },
        PLAYER {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.PLAYER);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.PLAYER ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        HERO {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.HERO);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.HERO ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        LEGEND {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.LEGEND);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.LEGEND ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        VETERAN {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.VETERAN);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.VETERAN ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        DONATOR {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.DONATOR);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.DONATOR ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        SUPER_DONATOR {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.SUPER_DONATOR);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.SUPER_DONATOR ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        WIKI_EDITOR {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.WIKI_EDITOR);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.WIKI_EDITOR ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        EVENT_MANAGER {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.EVENT_MANAGER);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.EVENT_MANAGER ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        HELPER {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.HELPER);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.HELPER ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        FORUM_MODERATOR {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.FORUM_MODERATOR);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.FORUM_MODERATOR ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        MODERATOR {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.MODERATOR);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.MODERATOR ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        GLOBAL_MODERATOR {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.GLOBAL_MODERATOR);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.GLOBAL_MODERATOR ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        COMMUNITY_MANAGER {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.COMMUNITY_MANAGER);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.COMMUNITY_MANAGER ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        HEAD_MODERATOR {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.HEAD_MODERATOR);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.HEAD_MODERATOR ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        ADMINISTRATOR {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.ADMINISTRATOR);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.ADMINISTRATOR ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        DEVELOPER {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.DEVELOPER);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.DEVELOPER ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        },
        OWNER {
            @Override
            public boolean hasComponent(Player player) {
                return Rank.hasAbility(player, Rank.OWNER);
            }

            @Override
            public String getColorCode(Player player) {
                return Rank.getPrimaryRank(player) == Rank.OWNER ? "gre" : "or1";
            }

            @Override
            public String getMouseOverText(Player player) {
                return "Change rank to " + toString();
            }

            @Override
            public boolean handle(Player player, int id) {
                if(!Rank.hasAbility(player, Rank.valueOf(name())))
                    return false;
                player.setPlayerRank(Rank.setPrimaryRank(player, Rank.valueOf(name())));
                return true;
            }
        };

        private final static QuestTabComponent[] VALUES = values();
        public final static List<QuestTabComponent> TITLES = Stream.of(VALUES).filter(QuestTabComponent::isTitle).collect(Collectors.toList());
        public String updateValue(Player player) {
            return null;
        }

        public boolean hasComponent(Player player) {
            if(isTitle()) {
                for (QuestTabComponent questTabComponent : containsComponents()) {
                    if (questTabComponent.hasComponent(player))
                        return true;
                }
            } else {
                return true;
            }
            return false;
        }

        public String getMouseOverText(Player player) {
            return "";
        }

        public String getColorCode(Player player) {
            return "or1";
        }

        public QuestTabComponent[] containsComponents() {
            return null;
        }

        public final boolean isTitle() {
            return containsComponents() != null;
        }

        public void addObserver() {}

        @Override
        public String toString() {
            return Misc.ucFirst(name().replaceAll("_", " "));
        }
    }

    private final Map<QuestTabComponent, Integer> byComponent = new HashMap<>();
    private final Map<Integer, QuestTabComponent> byInteger = new HashMap<>();
    private final Player player;
    private boolean constructed = false;

    public QuestTab(Player player) {
        this.player = player;
        TaskManager.submit(new Task(Time.ONE_SECOND * 2, player) {
            @Override
            protected void execute() {
                constructQuestTab();
                stop();
            }
        });
    }

    private void constructQuestTab() {
        for(int i = 0; i < QuestTabComponent.values().length + QuestTabComponent.TITLES.size() + 3; i++) {
            player.getActionSender().sendString(33010 + i, "");
        }
        constructed = true;
        byComponent.clear();
        byInteger.clear();
        int currentIndex = 33010;
        for (QuestTabComponent title : QuestTabComponent.TITLES) {
            if(!title.hasComponent(player))
                continue;
            byComponent.put(QuestTabComponent.EMPTY_LINE, currentIndex);
            byInteger.put(currentIndex++, QuestTabComponent.EMPTY_LINE);
            updateComponent(QuestTabComponent.EMPTY_LINE, true);
            byComponent.put(title, currentIndex);
            byInteger.put(currentIndex++, title);
            updateComponent(title, true);
            for (QuestTabComponent questTabComponent : title.containsComponents()) {
                if(!questTabComponent.hasComponent(player))
                    continue;
                byComponent.put(questTabComponent, currentIndex);
                byInteger.put(currentIndex++, questTabComponent);
                updateComponent(questTabComponent, true);
                questTabComponent.addObserver();
            }
        }
        player.getActionSender().sendScrollbarLength(33010, (byComponent.size() + QuestTabComponent.TITLES.size()) * 14);
    }

    public void updateComponent(QuestTabComponent questTabComponent) {
        updateComponent(questTabComponent, false);
    }

    public void updateComponent(QuestTabComponent questTabComponent, boolean construction) {
        if(!constructed)
            return;
        if (!questTabComponent.hasComponent(player)) {
            if(byComponent.containsKey(questTabComponent))
                constructQuestTab();
            return;
        }

        if(!byComponent.containsKey(questTabComponent)) {
            constructQuestTab();
            return;
        }

        if(questTabComponent.isTitle() && !construction) {
            Arrays.stream(questTabComponent.containsComponents()).forEach(component -> player.getQuestTab().updateComponent(component));
            return;
        }

        int stringId = byComponent.get(questTabComponent);

        //This will make it big or small
        if(construction) {
            if (questTabComponent.isTitle())
                player.getActionSender().sendFont(stringId, 2);
            else
                player.getActionSender().sendFont(stringId, 0);
        }

        player.getActionSender().sendString(stringId, (questTabComponent.isTitle() ? "@yel@" : "@" + questTabComponent.getColorCode(player) + "@  ") + questTabComponent.toString() + (questTabComponent.updateValue(player) != null ? ": @gre@" + questTabComponent.updateValue(player) : ""));
        player.getActionSender().sendTooltip(stringId, questTabComponent.getMouseOverText(player));
    }

    public boolean handleButton(Player player, int buttonId) {
        if(byInteger == null)
            return false;

        buttonId = (32532 + buttonId) + 33010 - QuestTabComponent.TITLES.size();

        if (!byInteger.containsKey(buttonId))
            return false;
        QuestTabComponent questTabComponent = byInteger.get(buttonId);
        return questTabComponent != null && questTabComponent.handle(player, buttonId);
    }

    static {
        for(int i = 0; i < QuestTabComponent.values().length + (QuestTabComponent.TITLES.size() * 2); i++) {
            ActionsManager.getManager().submit(-32532 + i, new ButtonAction() {
                @Override
                public boolean handle(Player player, int id) {
                    return player.getQuestTab().handleButton(player, id);
                }
            });
        }
    }
}
