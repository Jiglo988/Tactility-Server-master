package org.hyperion.rs2.model.joshyachievementsv2.tracker;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.joshyachievementsv2.Achievement;
import org.hyperion.rs2.model.joshyachievementsv2.Achievements;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class AchievementProgress{

    public final int achievementId;
    private final Map<Integer, AchievementTaskProgress> progress;

    public AchievementProgress(final int achievementId){
        this.achievementId = achievementId;

        progress = new TreeMap<>();

        achievement().tasks.stream()
                .forEach(t -> add(new AchievementTaskProgress(achievementId, t.id)));
    }

    public Timestamp firstStart(){
        return streamAvailableProgress()
                .min(Comparator.comparing(atp -> atp.startDate))
                .map(atp -> atp.startDate)
                .orElse(null);
    }

    public Timestamp lastFinish(){
        return streamAvailableProgress()
                .max(Comparator.comparing(atp -> atp.finishDate))
                .map(atp -> atp.finishDate)
                .orElse(null);
    }

    public Achievement achievement(){
        return Achievements.get().get(achievementId);
    }

    public void add(final AchievementTaskProgress atp){
        progress.put(atp.taskId, atp);
    }

    public AchievementTaskProgress progress(final int taskId){
        return progress.get(taskId);
    }

    public Stream<AchievementTaskProgress> streamAvailableProgress(){
        return progress.values().stream()
                .filter(AchievementTaskProgress::started);
    }

    public int progress(){
        return progress.values().stream()
                .mapToInt(atp -> atp.progress)
                .sum();
    }

    public boolean finished(){
        return tasksFinished();
    }

    public boolean tasksFinished(){
        return progress.values().stream()
                .allMatch(AchievementTaskProgress::finished);
    }

    public String progressColor(){
        if(finished())
            return "@gre@";
        else if(progress.values().stream().anyMatch(atp -> atp.started() || atp.progress > 0))
            return "@or1@";
        else
            return "@red@";
    }

    public String getTabString(){
        String color = "@red@";
        if(progress.values().stream().anyMatch(atp -> atp.started() || atp.progress > 0))
            color = "@or1@";
        if(finished())
            color = "@gre@";
        return color + getShortTitle();
    }

    public String getShortTitle(){
        return (achievement().title.length() <= 26 ? achievement().title : achievement().title.substring(0, 25).trim() + "...");
    }

    public void sendProgressHeader(final Player player){
        player.sendf("@dre@Achievement progress: %s %1.2%", getShortTitle(), progress());
    }

    public void sendAllTaskProgressHeaders(final Player player){
        progress.values()
                .forEach(p -> p.sendProgress(player, false));
    }

    public double progressPercent(){
        return progress() * 100d / achievement().tasks.threshold;
    }

    public String info(){
        final double percent = progressPercent();
        return String.format("@dre@Achievement %d | %s | %s%%", achievement().id + 1, achievement().shortTitle, percent);
    }
}
