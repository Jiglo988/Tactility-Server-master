package org.hyperion.rs2.model.content.skill.agility.obstacles;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.skill.agility.Course;
import org.hyperion.rs2.model.content.skill.agility.Obstacle;

/**
 * Created by Gilles on 12/09/2015.
 */
public class RockClimbing extends Obstacle {
    private Position[] start,
            end;

    public RockClimbing(int objectId, int skillXp, int levelReq, Position[] start, Position[] end, int failRate, Course course, int progress) {
        super(objectId, 844, levelReq, skillXp, failRate, course, progress);
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean overCome(Player player) {
        if(!super.overCome(player))
            return false;
        player.getWalkingQueue().setRunningToggled(false);
        for(int i = 0; i < start.length; i++) {
            if(player.getPosition().getX() == start[i].getX() && player.getPosition().getY() == start[i].getY()) {
                executeObject(player);
            }
        }
        return true;

    }

    @Override
    public void succeed(Player player, int tick, String message) {
        int j = 0;
        for(int i = 0; i < start.length; i++) {
            if(start[i].getX() == player.getPosition().getX() && start[i].getY() == player.getPosition().getY())
                j = i;
        }
        player.getActionSender().forceMovement(end[j].getX(), end[j].getY(), animId);
        super.succeed(player, start[j].distance(end[j]) + 1, message);
    }

    @Override
    public void fail(Player player, int tick, String message) {
        int j = 0;
        for(int i = 0; i < start.length; i++) {
            if(start[i].getX() == player.getPosition().getX() && start[i].getY() == player.getPosition().getY())
                j = i;
        }
        super.fail(player, start[j].distance(end[j]) + 1, message);
    }

    @Override
    public String toString() {
        return "Rocks";
    }
}
