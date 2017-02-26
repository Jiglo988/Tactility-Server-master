package org.hyperion.rs2.model.content.skill.agility.courses;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.skill.agility.Course;
import org.hyperion.rs2.model.content.skill.agility.obstacles.*;

/**
 * Created by Gilles on 11/09/2015.
 */
public class WildernessAgility extends Course {

    private Position[] rockStart = {
            Position.create(2995, 3937, 0),
            Position.create(2994, 3937, 0),
            Position.create(2993, 3937, 0)
    };
    private Position[] rockEnd = {
            Position.create(2995, 3933, 0),
            Position.create(2994, 3933, 0),
            Position.create(2993, 3933, 0)
    };


    public WildernessAgility() {
    super(35000, 5);
    generateObstacles();
    }

    public void generateObstacles() {
        new ObstaclePipe(2288, 6000, 85, Position.create(3004, 3937, 0), Position.create(3004, 3950, 0), 0, this, 1);
        new RopeSwing(2283, 7000, 85, Position.create(3005, 3953, 0), Position.create(3005, 3958, 0), Position.create(3004, 10354, 0), 0, 30, this, 2);
        new SteppingStone(2311, 7000, 85, Position.create(3002, 3960, 0), Position.create(2996, 3960, 0), Position.create(2999, 3957, 0), 3, 30, this, 3);
        new LogBalance(2297, 7000, 85, Position.create(3002, 3945, 0), Position.create(2994, 3945, 0), Position.create(2998, 10345, 0), 30, this, 4);
        new RockClimbing(2328, 6000, 85, rockStart, rockEnd, 0, this, 5);
    }

    @Override
    public void progressCourse(Player player, int progress) {
        player.getAgility().progressWildernessCourse(progress, this);
    }

    @Override
    public String toString() {
        return "Wilderness agility course";
    }
}
