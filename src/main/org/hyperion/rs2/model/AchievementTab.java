package org.hyperion.rs2.model;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.joshyachievementsv2.Achievement;
import org.hyperion.rs2.model.joshyachievementsv2.Achievements;
import org.hyperion.rs2.model.joshyachievementsv2.tracker.AchievementProgress;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.packet.ActionsManager;
import org.hyperion.rs2.packet.ButtonAction;
import org.hyperion.util.Time;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Gilles on 29/09/2015.
 */
public class AchievementTab {

    private final Player player;
    private long lastClick = System.currentTimeMillis();
    private final Map<Achievement, Integer> achievementMap = new HashMap<>();

    public AchievementTab(Player player) {
        this.player = player;
        TaskManager.submit(new Task(Time.ONE_SECOND * 2, player) {
            @Override
            protected void execute() {
                constructAchievementTab();
                stop();
            }
        });
    }

    public void constructAchievementTab() {
        int maxId = 0;
        for (Achievement.Difficulty difficulty : Achievement.Difficulty.values()) {
            player.getActionSender().sendString(32011 + maxId, "@or1@" + difficulty.toString());
            player.getActionSender().sendFont(32011 + maxId++, 2);
            List<Achievement> currentAchievements = Achievements.get().stream().filter(achievement -> achievement.difficulty == difficulty).collect(Collectors.toList());
            if (currentAchievements == null)
                return;

            for (Achievement achievement : currentAchievements) {
                achievementMap.put(achievement, 32011 + maxId);
                player.getActionSender().sendString(32011 + maxId, player.getAchievementTracker().progress(achievement).getTabString());
                player.getActionSender().sendTooltip(32011 + maxId++, achievement.title);
            }
            player.getActionSender().sendString(32011 + maxId++, "");
        }
        sendAchievementCompleted();
    }

    public void sendAchievement(Achievement achievement) {
        if (!achievementMap.containsKey(achievement) || player.getAchievementTracker().progress(achievement) == null)
            return;
        player.getActionSender().sendString(achievementMap.get(achievement), player.getAchievementTracker().progress(achievement).getTabString());
        if (player.getAchievementTracker().progress(achievement).finished())
            sendAchievementCompleted();
    }

    private void sendAchievementCompleted() {
        player.getActionSender().sendString("Achievement completed: " + Achievements.get().values().stream().filter(achievement -> player.getAchievementTracker().progress(achievement.id).finished()).distinct().count() + "/" + Achievements.get().size(), 32004);
    }

    public long getLastClick() {
        return lastClick;
    }

    public void setLastClick(long lastClick) {
        this.lastClick = lastClick;
    }

    static {
        ActionsManager.getManager().submit(32002, new ButtonAction() {
            @Override
            public boolean handle(Player player, int id) {
                player.sendMessage("l4unchur13 http://www.arteropk.wikia.com/wiki/Achievements");
                return true;
            }
        });
        Map<Integer, Achievement> achievementMap = new HashMap<>();
        int maxId = 0;
        for (Achievement.Difficulty difficulty : Achievement.Difficulty.values()) {
            maxId++;
            List<Achievement> currentAchievements = Achievements.get().stream().filter(achievement -> achievement.difficulty == difficulty).collect(Collectors.toList());
            if (currentAchievements == null)
                continue;
            for (Achievement achievement : currentAchievements) {
                achievementMap.put(32011 + maxId++, achievement);
            }
            maxId++;
        }
        achievementMap.entrySet().forEach(entry -> ActionsManager.getManager().submit(entry.getKey(), new ButtonAction() {
            @Override
            public boolean handle(Player player, int id) {
                Achievement achievement = entry.getValue();
                if (player.getAchievementTab().getLastClick() + 500 > System.currentTimeMillis()) {
                    player.sendMessage("l4unchur13 http://www.arteropk.wikia.com/wiki/Achievements:ID" + achievement.id);
                } else {
                    player.getAchievementTab().setLastClick(System.currentTimeMillis());
                    final AchievementProgress achievementProgress = player.getAchievementTracker().progress(achievement);
                    player.sendMessage("");
                    player.sendMessage(achievementProgress.info());
                    if (achievementProgress.finished()) {
                        final Timestamp start = achievementProgress.firstStart();
                        final Timestamp finish = achievementProgress.lastFinish();
                        if (start != null && finish != null)
                            player.getActionSender().sendDialogue("@dre@" + achievement.title, ActionSender.DialogueType.MESSAGE, 1,
                                    Animation.FacialAnimation.HAPPY,
                                    "@dre@Started: @bla@" + start,
                                    "@dre@Finished: @bla@" + finish,
                                    "",
                                    "");
                    } else {
                        for (int taskId = 0; taskId < achievement.tasks.size(); taskId++) {
                            achievementProgress.progress(taskId).info(player).forEach(player::sendMessage);
                        }
                    }
                }
                return true;
            }
        }));
    }
}