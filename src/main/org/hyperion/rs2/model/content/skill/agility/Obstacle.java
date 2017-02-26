package org.hyperion.rs2.model.content.skill.agility;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.util.Misc;

/**
 * Created by Gilles on 10/09/2015.
 */
public class Obstacle {

    protected int   objectId,
                    animId,
                    skillXp,
                    levelReq,
                    failRate,
                    progress;
    protected Course course;

    public int getObjectId() {
        return objectId;
    }

    public int getAnimId() {
        return animId;
    }

    public int getSkillXp() {
        return skillXp;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public int getFailRate() {
        return failRate;
    }

    public int getProgress() {
        return progress;
    }

    public Course getCourse() {
        return course;
    }

    public Obstacle(int objectId, int animId, int levelReq, int skillXp, int failRate, Course course, int progress) {
        if(failRate > 100 || failRate < 0) {
            this.failRate = 0;
            return;
        } else {
            this.failRate = failRate;
        }
        if(levelReq > 99 || levelReq < 0) {
            this.levelReq = 0;
        } else {
            this.levelReq = levelReq;
        }
        this.objectId = objectId;
        this.animId = animId;
        this.skillXp = skillXp;
        this.course = course;
        this.progress = progress;
        Course.addObstacle(this);
    }

    public boolean overCome(Player player) {
        if(player == null) {
            return false;
        }
        if(player.isBusy()) {
            return false;
        }
        if(player.getSkills().getLevel(Skills.AGILITY) < levelReq) {
            player.sendMessage("You need an agility level of " + levelReq + " to use this " + (course.getClass() == Shortcuts.class ? "shortcut" : this.toString().toLowerCase()) + ".");
            return false;
        }
        return !player.getRandomEvent().skillAction();
    }

    public void executeObject(Player player) {
        executeObject(player, "", "");
    }

    public void executeObject(Player player, int failRate) {
        executeObject(player, "", "");
    }

    public void executeObject(Player player, String succeedMessage, String failMessage) {
        player.setBusy(true);
        player.getAgility().setBusy(true);
        if(failRate != 0) {
            failRate -= ((player.getSkills().getLevel(Skills.AGILITY) - levelReq) / 2) * 10;
            if(Misc.random(1000) <= failRate) {
                fail(player, 0, failMessage);
                return;
            }
        }
        succeed(player, 0, succeedMessage);
    }

    public void succeed(Player player, int tick, String message) {
        World.submit(new Task(tick * 600) {
            @Override
            public void execute() {
                player.getSkills().addExperience(Skills.AGILITY, skillXp);
                reset(player);
                if(!message.isEmpty())
                    player.sendMessage(message);
                player.setBusy(false);
                player.getAgility().setBusy(false);
                course.progressCourse(player, progress);
                this.stop();
            }
        });
    }

    public void fail(Player player, int tick, String message) {
        World.submit(new Task(tick * 600) {
            @Override
            public void execute() {
                reset(player);
                if(!message.isEmpty())
                    player.sendMessage(message);
                this.stop();
            }
        });
    }

    public static void reset(Player player) {
        player.setBusy(false);
        player.getAgility().setBusy(false);
        player.getWalkingQueue().setRunningToggled(true);
        player.getAppearance().setWalkAnim(0x337); //default walk animation
        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
    }

    public static Position calculateMiddle(Position start, Position end) {
        int middleX = start.getX();
        int middleY = start.getY();

        if(start.getX() != end.getX()) {
            if(start.getX() > end.getX())
                middleX = start.getX() - start.distance(end)/2;
            else
                middleX = start.getX() + start.distance(end)/2;
        }
        if(start.getY() != end.getY()) {
            if(start.getY() > end.getY())
                middleY = start.getY() - start.distance(end)/2;
            else
                middleY = start.getY() + start.distance(end)/2;
        }
        return Position.create(middleX, middleY, start.getZ());
    }

    @Override
    public String toString() {
        return Misc.ucFirst(this.getClass().getSimpleName().toLowerCase());
    }



}
