package org.hyperion.engine.task.impl;

import org.hyperion.engine.task.Task;
import org.hyperion.map.pathfinding.Path;
import org.hyperion.map.pathfinding.PathTest;
import org.hyperion.rs2.logging.FileLogging;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.Container;

public class PlayerCombatTask extends Task {
	
	public PlayerCombatTask() {
		super(600L);
	}
	
	public synchronized static Player[] cloneEntityList() {
		return World.getPlayers().stream().toArray(Player[]::new);
	}
	
	@Override
	public void execute() {
		//move all combat events to this event, and npcs seperate, doesn't glitch both
        final long startTime = System.currentTimeMillis();
		Player[] players = cloneEntityList();
        //handle all combat first players first
        final int size = players.length;
        final int startPoint = Combat.random(size - 1);
        for(int pid = startPoint; pid < (size + startPoint); pid++) {
            final Player player = players[pid%size];
            try {
                if(player == null || player.cE == null) {
                    System.err.println("ERROR SEVERE: NULL PLAYER");
                    continue;
                }
                //following for players
                if(player.isFollowing != null) {
                    //System.out.println("Following");

                    int dis = player.getPosition().distance(player.isFollowing.getPosition());
                    if(dis <= 20 && dis > 1) {
                        try {
                            int toX = player.isFollowing.getPosition().getX();
                            int toY = player.isFollowing.getPosition().getY();
                            //System.out.println("X : " + startx + " Y : " + starty);
                            if(player.isFollowing.getWalkingQueue().getPublicPoint() != null) {
                                toX = player.isFollowing.getWalkingQueue().getPublicPoint().getX();
                                toY = player.isFollowing.getWalkingQueue().getPublicPoint().getY();
                            }
                            int baseX = player.getPosition().getX() - 25;
                            int baseY = player.getPosition().getY() - 25;
                            player.getWalkingQueue().reset();
                            player.getWalkingQueue().setRunningQueue(true);
                            Path p = PathTest.getPath(player.getPosition().getX(), player.getPosition().getY(), toX, toY);
                            if(p != null) {
                                for(int i = 1; i < p.getLength(); i++) {
                                    if((baseX + p.getX(i)) != toX || (baseY + p.getY(i)) != toY)
                                        player.getWalkingQueue().addStep((baseX + p.getX(i)), (baseY + p.getY(i)));
                                }
                                player.getWalkingQueue().finish();
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(!stakeReset(player) && player.cE.getOpponent() != null) {
                    if(! Combat.processCombat(player.cE))
                        Combat.resetAttack(player.cE);
                }

                player.getWalkingQueue().walkingCheck();
            }
        }
	}
	
	public synchronized static void cleanList() {
		
	}

    public static boolean stakeReset(final Player player) {
        final Player opp = player.getTrader();
        if(opp != null && !opp.isDead() && !player.isDead() && !opp.getSession().isConnected() && !player.getSession().isConnected() && player.duelAttackable > 0 && opp.duelAttackable > 0) {
            FileLogging.savePlayerLog(opp, "Duel TIE against "+player.getName());
            FileLogging.savePlayerLog(player, " Duel TIE against " + opp.getName());
            Container.transfer(player.getDuel(), player.getInventory());//jet is a smartie
            Container.transfer(opp.getDuel(), opp.getInventory());
            opp.setTeleportTarget(Position.create(3360 + Combat.random(17), 3274 + Combat.random(3), 0), false);
            player.setTeleportTarget(Position.create(3360 + Combat.random(17), 3274 + Combat.random(3), 0), false);
            return true;
        }
        return false;
    }

}
