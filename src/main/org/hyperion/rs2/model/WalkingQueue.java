package org.hyperion.rs2.model;

import org.hyperion.engine.task.impl.PlayerDeathTask;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.minigame.GodWars;
import org.hyperion.rs2.util.DirectionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * <p>
 * A <code>WalkingQueue</code> stores steps the client needs to walk and allows
 * this queue of steps to be modified.
 * </p>
 * <p/>
 * <p>
 * The class will also process these steps when {@link #processNextMovement()}
 * is called. This should be called once per server cycle.
 * </p>
 *
 * @author Graham Edgecombe
 */
// TODO implement 'travelback' algorithm so you are unable to noclip while map
// TODO region is loading?
public class WalkingQueue {


    public static BufferedWriter ATTACK_DEBUG;

    static {
        try {
            ATTACK_DEBUG = new BufferedWriter(new FileWriter(new File("./data/debug.txt")));
        } catch (Exception e) {

        }
    }

    public static long lastRecord = 0L;

    /**
     * Represents a single point in the queue.
     *
     * @author Graham Edgecombe
     */
    public static class Point {

        /**
         * The x-coordinate.
         */
        private final int x;

        /**
         * The y-coordinate.
         */
        private final int y;

        /**
         * The direction to walk to this point.
         */
        private final int dir;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        /**
         * Creates a point.
         *
         * @param x   X coord.
         * @param y   Y coord.
         * @param dir Direction to walk to this point.
         */
        public Point(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

    }

    private int lastCombatLevel = -1;

    /**
     * The maximum size of the queue. If there are more points than this size,
     * they are discarded.
     */
    public static final int MAXIMUM_SIZE = 50;

    /**
     * The entity.
     */
    private Entity entity;

    /**
     * The queue of waypoints.
     */
    private Point[] walkingQueue = new Point[50];

    private int currentStep = 0;
    private int currentSize = 0;

    /**
     * Run toggle (button in client).
     */
    private boolean runToggled = false;

    /**
     * Run for this queue (CTRL-CLICK) toggle.
     */
    private boolean runQueue = false;

    /**
     * Creates the <code>WalkingQueue</code> for the specified
     * <code>Entity</code>.
     *
     * @param entity The entity whose walking queue this is.
     */
    public WalkingQueue(Entity entity) {
        this.entity = entity;
    }

    /**
     * Sets the run toggled flag.
     *
     * @param runToggled The run toggled flag.
     */
    public void setRunningToggled(boolean runToggled) {
        this.runToggled = runToggled;
    }

    /**
     * Sets the run queue flag.
     *
     * @param runQueue The run queue flag.
     */
    public void setRunningQueue(boolean runQueue) {
        this.runQueue = runQueue;
    }

    /**
     * Gets the run toggled flag.
     *
     * @return The run toggled flag.
     */
    public boolean isRunningToggled() {
        return runToggled;
    }

    /**
     * Gets the running queue flag.
     *
     * @return The running queue flag.
     */
    public boolean isRunningQueue() {
        return runQueue;
    }

    /**
     * Checks if any running flag is set.
     *
     * @return <code>true</code. if so, <code>false</code> if not.
     */
    public boolean isRunning() {
        return runToggled || runQueue;
    }

    /**
     * Resets the walking queue so it contains no more steps.
     */
    public void reset() {
        /*
         * if(entity instanceof Player &&
		 * entity.cE.getPlayerByName().getName().contains("lux"))
		 * System.out.println("Resetting queue");
		 */
        runQueue = false;
        currentStep = 0;
        currentSize = 0;
        for (int i = 0; i < walkingQueue.length; i++) {
            walkingQueue[i] = null;
        }
    }

    /**
     * Checks if the queue is empty.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Removes the first waypoint which is only used for calculating directions.
     * This means walking begins at the correct time.
     */
    public void finish() {
        currentStep = 0;
    }

    public Point getPublicPoint() {
        if (currentSize > 0 && currentStep + 2 < currentSize && isRunning()) {
            return walkingQueue[currentStep + 2];
        }
        if (currentSize > 0 && currentStep + 1 < currentSize) {
            return walkingQueue[currentStep + 1];
        }
        if (currentSize > 0 && currentStep < currentSize) {
            return walkingQueue[currentStep];
        } else {
            return null;
        }
    }

    public void addStep(int x, int y) {
        //Make npcs not walk too far away from their spawn location.
        if (walkingQueue == null)
            return;
        if (entity instanceof NPC) {
            Position target = Position.create(x, y, 0);
            if (target.distance(((NPC) entity).getSpawnPosition()) > 15)
                return;
        }
        int lastX = entity.getPosition().getX();
        int lastY = entity.getPosition().getY();
        if (currentStep > 0) {
            lastX = walkingQueue[currentStep - 1].getX();
            lastY = walkingQueue[currentStep - 1].getY();
        }

        int diffX = x - lastX;
        int diffY = y - lastY;

        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int i = 0; i < max; i++) {

            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }

            addStepInternal(x - diffX, y - diffY);
        }
    }

    private void addStepInternal(int x, int y) {
        if (currentStep >= MAXIMUM_SIZE - 1 || currentSize >= MAXIMUM_SIZE - 1) {
            return;
        }

        int lastX = entity.getPosition().getX();
        int lastY = entity.getPosition().getY();
        if (currentStep > 0) {
            lastX = walkingQueue[currentStep - 1].getX();
            lastY = walkingQueue[currentStep - 1].getY();
        }

        int diffX = x - lastX;
        int diffY = y - lastY;

        int dir = DirectionUtils.direction(diffX, diffY);
        if (dir > -1) {
            walkingQueue[currentStep++] = new Point(x, y, dir);
            currentSize++;
        }
    }

    public boolean walkingCheck() {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Locations.process(entity);

            if (entity.cE.isFrozen() || entity.isDead()) {
                reset();
            }

            /*if (player.getSkills().getLevel(Skills.HITPOINTS) == 0 && !player.isDead()) {
                if (player.duelAttackable <= 0) {
                    World.submit(new PlayerDeathTask(player));
                }
                return false;
            }*/
        }
        return true;
    }

    private boolean inArdyPvp = true;

    /**
     * Processes the next player's movement.
     */
    public void processNextMovement() {
		/*
		 * Store the teleporting flag.
		 */
        boolean teleporting = entity.hasTeleportTarget();

		/*
		 * The points which we are walking to.
		 */
        Point walkPoint = null, runPoint = null;

		/*
		 * Checks if the player is teleporting i.e. not walking.
		 */
        if (teleporting) {
			/*
			 * Reset the walking queue as it will no longer apply after the
			 * teleport.
			 */
            reset();

			/*
			 * Set the 'teleporting' flag which indicates the player is
			 * teleporting.
			 */
            entity.setTeleporting(true);

			/*
			 * Sets the player's new location to be their target.
			 */
            entity.setPosition(entity.getTeleportTarget());

            // turn on the godwars interface
            if (entity instanceof Player) {
                GodWars.godWars.checkGodWarsInterface((Player) entity);
            }

			/*
			 * Resets the teleport target.
			 */
            entity.resetTeleportTarget();
        } else {
			/*
			 * If the player isn't teleporting, they are walking (or standing
			 * still). We get the next direction of movement here.
			 */
            walkPoint = getNextPoint();

			/*
			 * Technically we should check for running here.
			 */
            if (runToggled || runQueue) {
                runPoint = getNextPoint();
            }

			/*
			 * Now set the sprites.
			 */
            int walkDir = walkPoint == null ? -1 : walkPoint.dir;
            int runDir = runPoint == null ? -1 : runPoint.dir;
            if (entity instanceof Player) {
                Player player = (Player) entity;
                if (walkDir == -1 && runDir == -1) {
                    player.isMoving = false;
                    if (player.handleClickNow()) {

                    }
                } else {
                    player.isMoving = true;
                }
            }
            entity.getSprites().setSprites(walkDir, runDir);
        }

		/*
		 * Check for a map region change, and if the map region has changed, set
		 * the appropriate flag so the new map region packet is sent.
		 */
        int diffX = entity.getPosition().getX()
                - entity.getLastKnownRegion().getRegionX() * 8;
        int diffY = entity.getPosition().getY()
                - entity.getLastKnownRegion().getRegionY() * 8;
        boolean changed = false;
        if (diffX < 16) {
            changed = true;
        } else if (diffX >= 88) {
            changed = true;
        }
        if (diffY < 16) {
            changed = true;
        } else if (diffY >= 88) {
            changed = true;
        }

        if (changed) {
			/*
			 * Set the map region changing flag so the new map region packet is
			 * sent upon the next update.
			 */
            entity.setMapRegionChanging(true);
        }

    }

    /**
     * Gets the next point of movement.
     *
     * @return The next point.
     */
    private Point getNextPoint() {
		/*
		 * Take the next point from the queue.
		 */
        Point p = null;
        if (currentStep < currentSize)
            p = walkingQueue[currentStep++];

		/*
		 * Checks if there are no more points.
		 */
        if (p == null || p.dir == -1) {
			/*
			 * Return <code>null</code> indicating no movement happened.
			 */
            return null;
        } else {
			/*
			 * Set the player's new location.
			 */
            int diffX = Constants.DIRECTION_DELTA_X[p.dir];
            int diffY = Constants.DIRECTION_DELTA_Y[p.dir];
            entity.setPosition(entity.getPosition().transform(diffX, diffY, 0));
			/*
			 * And return the direction.
			 */
            return p;
        }
    }

    public void destroy() {
        entity = null;
    }

    protected static boolean hasSnowball(Player player) {
        if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
            if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 11951) {
                return true;
            }
        }
        return false;
    }

}
