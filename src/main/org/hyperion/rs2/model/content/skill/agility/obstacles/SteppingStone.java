package org.hyperion.rs2.model.content.skill.agility.obstacles;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.skill.agility.Course;
import org.hyperion.rs2.model.content.skill.agility.Obstacle;
import org.hyperion.util.Misc;

/**
 * Created by Gilles on 12/09/2015.
 */
public class SteppingStone extends Obstacle {
    private Position start,
            end,
            fail;
    private int direction;

    public SteppingStone(int objectId, int skillXp, int levelReq, Position start, Position end, Position fail, int direction, int failRate, Course course, int progress) {
        super(objectId, 769, levelReq, skillXp, failRate, course, progress);
        this.start = start;
        this.end = end;
        this.direction = direction;
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
            player.sendMessage("You carefully start crossing the stepping stones...");
            executeObject(player, "...And safely cross to the other side.", "...But slip and fall in the lava!");
        } else {
            executeObject(player);
        }
        return true;
    }

    @Override
    public void succeed(Player player, int tick, String message) {
        final int a = player.getAppearance().getStandAnim();
        final int b = player.getAppearance().getWalkAnim();
        final int c = player.getAppearance().getRunAnim();
        player.getAppearance().setAnimations(a, animId, animId);
        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);

        World.submit(new Task(600, "stepping stone") {
            int progress = start.distance(end) * 3;
            @Override
            public void execute() {
                if(progress == 0) {
                    player.getSkills().addExperience(Skills.AGILITY, skillXp);
                    reset(player);
                    player.getAppearance().setAnimations(a, b, c);
                    player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
                    if(!message.isEmpty())
                        player.sendMessage(message);
                    course.progressCourse(player, getProgress());
                    stop();
                }
                else if(0 == progress %3) {
                    if(direction == 0)
                        player.getActionSender().forceMovement(player.getPosition().getX(), player.getPosition().getY() + 1);
                    if(direction == 1)
                        player.getActionSender().forceMovement(player.getPosition().getX() + 1, player.getPosition().getY());
                    if(direction == 2)
                        player.getActionSender().forceMovement(player.getPosition().getX(), player.getPosition().getY() - 1);
                    if(direction == 3)
                        player.getActionSender().forceMovement(player.getPosition().getX() - 1, player.getPosition().getY());
                }
                progress--;
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

        World.submit(new Task(600, "stepping stone2") {
            int progress = start.distance(calculateMiddle(start, end)) * 3;

            @Override
            public void execute() {
                if (progress == 0) {
                    if (!message.isEmpty())
                        player.sendMessage(message);
                    player.setTeleportTarget(Position.create(fail.getX(), fail.getY(), fail.getZ()));
                    player.getAgility().appendHit(Misc.random(3) + 3);
                    reset(player);
                    player.getAppearance().setAnimations(a, b, c);
                    player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
                    stop();
                }
                else if (player.getPosition().getX() == calculateMiddle(start, end).getX() && player.getPosition().getY() == calculateMiddle(start, end).getY()) {
                    player.playAnimation(Animation.create(770));
                }
                else if (0 == progress % 3) {
                    if (direction == 0)
                        player.getActionSender().forceMovement(player.getPosition().getX(), player.getPosition().getY() + 1);
                    if (direction == 1)
                        player.getActionSender().forceMovement(player.getPosition().getX() + 1, player.getPosition().getY());
                    if (direction == 2)
                        player.getActionSender().forceMovement(player.getPosition().getX(), player.getPosition().getY() - 1);
                    if (direction == 3)
                        player.getActionSender().forceMovement(player.getPosition().getX() - 1, player.getPosition().getY());
                }
                progress--;
            }
        });
    }

    @Override
    public String toString() {
        return "Stepping stones";
    }
}
