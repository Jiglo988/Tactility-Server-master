package org.hyperion.rs2.model.content.skill.agility.obstacles;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.skill.agility.Course;
import org.hyperion.rs2.model.content.skill.agility.Obstacle;
import org.hyperion.util.Misc;

/**
 * Created by Gilles on 11/09/2015.
 */
public class LogBalance extends Obstacle {
    private Position start,
                        end,
                        fail = null;

    public LogBalance(int objectId, int skillXp, int levelReq, Position start, Position end, int failRate, Course course, int progress) {
        super(objectId, 762, levelReq, skillXp, failRate, course, progress);
        this.start = start;
        this.end = end;
    }
    public LogBalance(int objectId, int skillXp, int levelReq, Position start, Position end, Position fail, int failRate, Course course, int progress) {
        super(objectId, 762, levelReq, skillXp, failRate, course, progress);
        this.start = start;
        this.end = end;
        this.fail = fail;
    }

    @Override
    public boolean overCome(Player player) {
        if(player.getPosition().getX() != start.getX() || player.getPosition().getY() != start.getY())
            return false;
        if(!super.overCome(player))
            return false;
        player.getWalkingQueue().setRunningToggled(false);
        if(failRate != 0) {
            player.sendMessage("You walk across the log...");
            executeObject(player, "...And make it to the other side safely.", "...But slip and fall!");
        } else {
            executeObject(player);
        }
        return true;
    }

    @Override
    public void succeed(Player player, int tick, String message) {
        super.succeed(player, start.distance(end) + 1, message);
        player.getActionSender().forceMovement(end.getX(), end.getY(), animId);
        World.submit(new Task((start.distance(end) + 1) * 600) {
            @Override
            public void execute() {
                player.setTeleportTarget(end);
                stop();
            }
        });
    }

    @Override
    public void fail(Player player, int tick, String message) {
        final int a = player.getAppearance().getStandAnim();
        final int b = player.getAppearance().getWalkAnim();
        final int c = player.getAppearance().getRunAnim();
        player.getAppearance().setAnimations(a, animId, animId);
        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);

        World.submit(new Task(600) {
            int progress = start.distance(calculateMiddle(start, end)) + 2;

            @Override
            public void execute() {
                if (progress == 0) {
                    if (!message.isEmpty())
                        player.sendMessage(message);
                    if(fail != null)
                        player.setTeleportTarget(fail);
                    else
                        player.setTeleportTarget(start);
                    player.getAgility().appendHit(Misc.random(3) + 3);
                    reset(player);
                    player.getAppearance().setAnimations(a, b, c);
                    player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
                    stop();
                } else if (player.getPosition().getX() == calculateMiddle(start, end).getX() && player.getPosition().getY() == calculateMiddle(start, end).getY()) {
                    player.playAnimation(Animation.create(770));
                } else if (progress == start.distance(calculateMiddle(start, end)) + 2) {
                    player.getActionSender().forceMovement(calculateMiddle(start, end).getX(), calculateMiddle(start, end).getY());
                }
                progress--;
            }
        });
    }

    @Override
    public String toString() {
        return "Balancing log";
    }
}
