package org.hyperion.rs2.model.content.skill.agility.courses;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.skill.agility.Course;
import org.hyperion.rs2.model.content.skill.agility.obstacles.*;

/**
 * Created by Gilles on 10/09/2015.
 */
public class GnomeStronghold extends Course {
    private final static int EXPMULTIPLIER = (int)(Constants.XPRATE * 1.75) * 9;
    public static Position position = Position.create(2480, 3437, 0);

    Position[] net1Start = {
            Position.create(2476, 3426, 0),
            Position.create(2475, 3426, 0),
            Position.create(2474, 3426, 0),
            Position.create(2473, 3426, 0),
            Position.create(2472, 3426, 0),
            Position.create(2471, 3426, 0)
    };
    Position[] net1End = {
            Position.create(2476, 3424, 1),
            Position.create(2475, 3424, 1),
            Position.create(2474, 3424, 1),
            Position.create(2473, 3424, 1),
            Position.create(2472, 3424, 1),
            Position.create(2471, 3424, 1)
    };
    Position[] net2Start = {
            Position.create(2483, 3425, 0),
            Position.create(2484, 3425, 0),
            Position.create(2485, 3425, 0),
            Position.create(2486, 3425, 0),
            Position.create(2487, 3425, 0),
            Position.create(2488, 3425, 0),
    };
    Position[] net2End = {
            Position.create(2483, 3427, 0),
            Position.create(2484, 3427, 0),
            Position.create(2485, 3427, 0),
            Position.create(2486, 3427, 0),
            Position.create(2487, 3427, 0),
            Position.create(2488, 3427, 0)
    };
    Position[] branch1Start = {
            Position.create(2474, 3422, 1),
            Position.create(2473, 3423, 1),
            Position.create(2472, 3422, 1)
    };
    Position[] branch2Start = {
            Position.create(2486, 3420, 2),
            Position.create(2485, 3419, 2),
            Position.create(2486, 3418, 2)
    };
    Position[] branch3Start = {
            Position.create(2486, 3420, 2),
            Position.create(2486, 3418, 2),
            Position.create(2487, 3421, 2),
            Position.create(2488, 3420, 2),
    };

    public GnomeStronghold() {
        super(60 * EXPMULTIPLIER, 7);
        generateObstacles();
    }

    public void generateObstacles() {
        new LogBalance(2295, 10 * EXPMULTIPLIER, 1, Position.create(2474, 3436, 0), Position.create(2474, 3429, 0), 0, this, 1);
        new ClimbNet(2285, 15 * EXPMULTIPLIER, 1, net1Start, net1End, 0, this, 2);
        new ClimbBranch(2313, 30 * EXPMULTIPLIER, 1, branch1Start, Position.create(2473, 3420, 2), 0, this, 3);
        new RopeBalance(2312, 10 * EXPMULTIPLIER, 1, Position.create(2477, 3420, 2), Position.create(2483, 3420, 2), 0, this, 4);
        new ClimbBranch(2314, 30 * EXPMULTIPLIER, 1, branch2Start, Position.create(2487, 3420, 0), 0, this, 5);
        new ClimbBranch(2315, 30 * EXPMULTIPLIER, 1, branch3Start, Position.create(2487, 3420, 0), 0, this, 5);
        new ClimbNet(2286, 20 * EXPMULTIPLIER, 1, net2Start, net2End, 0, this, 6);
        new ObstaclePipe(154, 10 * EXPMULTIPLIER, 1, Position.create(2484, 3430, 0), Position.create(2484, 3437, 0), 0, this, 7);
        new ObstaclePipe(4058, 10 * EXPMULTIPLIER, 1, Position.create(2487, 3430, 0), Position.create(2487, 3437, 0), 0, this, 7);
    }

    @Override
    public void progressCourse(Player player, int progress) {
        player.getAgility().progressGnomeCourse(progress, this);
    }

    @Override
    public String toString() {
        return "Gnome stronghold agility course";
    }
}

