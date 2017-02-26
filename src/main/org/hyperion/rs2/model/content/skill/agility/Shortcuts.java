package org.hyperion.rs2.model.content.skill.agility;

import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.skill.agility.obstacles.ObstaclePipe;

/**
 * Created by Gilles on 11/09/2015.
 */
public class Shortcuts extends Course {

    public Shortcuts() {
        super(0, 0);
        generateObstacles();
    }

    public void generateObstacles() {
        new ObstaclePipe(9293, 0, 75, Position.create(2886, 9799, 0), Position.create(2892, 9799, 0), 0, this, 0);
        new ObstaclePipe(9293, 0, 75, Position.create(2892, 9799, 0), Position.create(2886, 9799, 0), 0, this, 0);
    }
}
