package org.hyperion.rs2.model.joshyachievementsv2.tracker;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.joshyachievementsv2.Achievement;
import org.hyperion.rs2.model.joshyachievementsv2.Achievements;
import org.hyperion.rs2.model.joshyachievementsv2.task.Task;
import org.hyperion.rs2.model.joshyachievementsv2.task.impl.*;
import org.hyperion.sql.DbHub;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AchievementTracker {

    private static boolean active = false;

    private final Player player;
    private final Map<Integer, AchievementProgress> progress;

    public boolean errorLoading;

    public AchievementTracker(final Player player) {
        this.player = player;

        progress = new TreeMap<>();
    }

    public void load(){
        if(!active)
            return;
        if(!DbHub.getPlayerDb().isInitialized()) {
            active = false;
            return;
        }
        final List<AchievementTaskProgress> taskProgress = DbHub.getPlayerDb().getAchievements().loadTaskProgress(player);
        if (taskProgress == null) {
            errorLoading = true;
            return;
        }
        taskProgress.forEach(atp -> progress(atp.achievementId).add(atp));
    }

    public void sendInfo(final Achievement a) {
        final AchievementProgress p = progress(a.id);
        p.sendProgressHeader(player);
        a.instructions.instruct(player);
        p.sendAllTaskProgressHeaders(player);
    }

    public void sendInfo(final int achievementId) {
        final Achievement achievement = Achievements.get().get(achievementId);
        if (achievement != null)
            sendInfo(achievement);
    }

    public void add(final AchievementProgress ap) {
        progress.put(ap.achievementId, ap);
    }

    public Stream<AchievementTaskProgress> streamAvailableTaskProgress() {
        return progress.values().stream()
                .flatMap(AchievementProgress::streamAvailableProgress);
    }

    public AchievementProgress progress(final int achievementId) {
        if(!progress.containsKey(achievementId))
            progress.put(achievementId, new AchievementProgress(achievementId));
        return progress.get(achievementId);
    }

    private AchievementProgress putAndGetProgress(final int achievementId) {
        final AchievementProgress p = new AchievementProgress(achievementId);
        add(p);
        return p;
    }

    public AchievementProgress progress(final Achievement achievement) {
        return progress(achievement.id);
    }

    public AchievementTaskProgress taskProgress(final int achievementId, final int taskId) {
        return progress(achievementId).progress(taskId);
    }

    public AchievementTaskProgress taskProgress(final Task task) {
        return taskProgress(task.achievementId, task.id);
    }

    private boolean canDoTask(final Task task) {
        if(taskProgress(task).finished()){
            player.debugMessage("canDoTask#task progress is finished: " + task.desc);
            return false;
        }
        if(task.hasPreTask() && !taskProgress(task.preTask()).finished()){
            player.debugMessage("canDoTask#preTask is not finished: " + task.desc);
            return false;
        }
        return true;
    }

    private Optional<Task> findAvailableTask(final Class<? extends Task> clazz, final Predicate<Task> pred, final int progress) {
        return Achievements.get().streamTasks(clazz)
                .filter(t -> {
                    if(!pred.test(t))
                        return false;
                    if(!canDoTask(t)){
                        player.debugMessage("Can't do task: " + t.desc);
                        return false;
                    }
                    if(!t.canProgress(taskProgress(t).progress, progress)){
                        player.debugMessage("Can't progress: " + t.desc);
                        return false;
                    }
                    if(!t.constraints.constrained(player)){
                        player.debugMessage("Not constrained: " + t.desc);
                        return false;
                    }
                    return true;
                })
                .min(Comparator.comparingInt(t -> t.threshold));
        /*return Achievements.get().streamTasks(clazz)
                .filter(pred.and(this::canDoTask)
                        .and(t -> t.canProgress(taskProgress(t).progress, progress))
                        .and(t -> t.constraints.constrained(player)))
                .min(Comparator.comparingInt(t -> t.threshold));*/
    }

    private Optional<Task> findAvailableTask(final Task.Filter filter, final int progress) {
        return findAvailableTask(filter.clazz, filter, progress);
    }

    private void progress(final Task.Filter filter, final int progress) {
        if (!active || errorLoading)
            return;
        findAvailableTask(filter, progress)
                .ifPresent(t -> progress(t, progress));
    }

    private void progress(final Task task, final int progress) {
        if(!DbHub.getPlayerDb().isInitialized())
            return;
        final AchievementProgress ap = progress(task.achievementId);
        final AchievementTaskProgress atp = ap.progress(task.id);
        if (ap.finished() || atp.finished())
            return; //this shouldnt happen but just to be safe
        final boolean shouldInsert = !atp.started();
        if (!atp.started())
            atp.startNow();
        final int oldProgress = atp.progress;
        if(task instanceof KillstreakTask)
            atp.progress = progress;
        else
            atp.progress(progress);
        if(!(shouldInsert ? DbHub.getPlayerDb().getAchievements().insertTaskProgress(player, atp) : DbHub.getPlayerDb().getAchievements().updateTaskProgress(player, atp))){
            if(shouldInsert)
                atp.startDate = null;
            atp.progress = oldProgress;
            return;
        }
        ap.sendProgressHeader(player);
        atp.sendProgress(player, true);
        if (atp.taskFinished()) {
            atp.finishNow();
            if(!DbHub.getPlayerDb().getAchievements().updateTaskProgress(player, atp)){
                atp.finishDate = null;
                return;
            }
            player.sendLootMessage("Achievement", String.format("Task '%s' complete! Congratulations!", task.desc));
            if (ap.tasksFinished()) {
                player.sendLootMessage("Achievement", String.format("%s complete! Congratulations!", ap.achievement().title));
                ap.achievement().rewards.reward(player);
            }
        }
        player.getAchievementTab().sendAchievement(ap.achievement());
    }

    public void barrowsTrip() {
        progress(BarrowsTripTask.filter(), 1);
    } //call these methods where they should be called: player.getAchievementTracker().XXXXXXXXXX()

    public void bountyHunterKill() {
        progress(BountyHunterKillTask.filter(), 1);
    }

    private void fightPits(final FightPitsTask.Result result) {
        progress(FightPitsTask.filter(result), 1);
    }

    public void fightPitsWin() {
        fightPits(FightPitsTask.Result.WIN);
    }

    public void fightPitsLose() {
        fightPits(FightPitsTask.Result.LOSE);
    }

    public void itemOpened(final int itemId, final int quantity) {
        progress(ItemOpenTask.filter(itemId), quantity);
    }

    public void itemOpened(final int itemId) {
        itemOpened(itemId, 1);
    }

    public void bountyKill(final int bounty) {
        progress(KillForBountyTask.filter(), bounty);
    }

    public void onKillstreak(final int killstreak) {
        progress(KillstreakTask.filter(), killstreak);
    }

    public void npcKill(final int npcId) {
        progress(NpcKillTask.filter(npcId), 1);
    }

    public void playerKill(){
        progress(PlayerKillTask.filter(), 1);
    }

    private void pickupItem(final PickupItemTask.From from, final int itemId, final int quantity) {
        progress(PickupItemTask.filter(from, itemId), quantity);
    }

    public void pickupItemFromNpc(final int itemId, final int quantity) {
        pickupItem(PickupItemTask.From.NPC, itemId, quantity);
    }

    public void pickupItemFromPlayer(final int itemId, final int quantity) {
        pickupItem(PickupItemTask.From.PLAYER, itemId, quantity);
    }

    public void bountyPlaced(final int bounty) {
        progress(PlaceBountyTask.filter(), bounty);
    }

    public void itemSkilled(final int skill, final int itemId, final int quantity) {
        progress(SkillItemTask.filter(skill, itemId), quantity);
    }

    public void voted(final int times) {
        progress(VoteTask.filter(), times);
    }

    public void voted() {
        voted(1);
    }

    public void slayerTaskCompleted(final int npcId) {
        progress(SlayerTask.filter(npcId), 1);
    }

    public void dungFloorCompleted(final DungeoneeringFloorsTask.Difficulty difficulty, final DungeoneeringFloorsTask.Size size) {
        progress(DungeoneeringFloorsTask.filter(difficulty, size), 1);
    }

    public static void active(final boolean active){
        AchievementTracker.active = active;
    }

    public static boolean active(){
        return active;
    }
}
