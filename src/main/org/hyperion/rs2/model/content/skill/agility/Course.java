package org.hyperion.rs2.model.content.skill.agility;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.content.ContentManager;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.skill.agility.courses.GnomeStronghold;
import org.hyperion.util.ArrayUtils;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gilles on 10/09/2015.
 */
public class Course implements ContentTemplate {

    private int courseBonusExp,
                maxCourseProgress;

    private static List<Obstacle> obstacles = new ArrayList();
    private static List obstacleId = new ArrayList();
    public Course() {
    }
    public Course(int courseBonusExp, int maxCourseProgress) {
        this.courseBonusExp = courseBonusExp;
        this.maxCourseProgress = maxCourseProgress;
    }

    public int getMaxCourseProgress() {
        return maxCourseProgress;
    }

    public int getCourseBonusExp() {
        return courseBonusExp;
    }

    public void progressCourse(Player player, int progress) {
    }

    @Override
    public int[] getValues(int type) {
        if(type == ContentManager.OBJECT_CLICK1){
            return ArrayUtils.fromList(obstacleId);
        }
        return null;
    }

    @Override
    public boolean clickObject(Player player, int type, int objId, int x, int y, int d) {
        boolean found = false;
        for(int i = 0; i < obstacles.size(); i++) {
            if (objId == obstacles.get(i).objectId) {
                obstacles.get(i).overCome(player);
                found = true;
            }
        }
        return found;
    }

    public static void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
        obstacleId.add(obstacle.objectId);
    }

    @Override
    public String toString() {
        return Misc.ucFirst(this.getClass().getSimpleName().toLowerCase());
    }
}
