package org.hyperion.rs2.model;

import org.hyperion.Server;
import org.hyperion.engine.EngineTask;
import org.hyperion.map.pathfinding.Path;
import org.hyperion.map.pathfinding.PathTest;
import org.hyperion.rs2.logging.FileLogging;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.region.RegionManager;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by Gilles on 12/02/2016.
 */
public class PlayerUpdateSequence implements UpdateSequence<Player> {

    public static final int MAX_PACKET_SIZE = 4500;

    /**
     * Used to block the game thread until updating is completed.
     */
    private final Phaser synchronizer;
    /**
     * The thread pool that will update players in parallel.
     */
    private final ExecutorService updateExecutor;

    private static int maxSize = 0;

    /**
     * Create a new {@link PlayerUpdateSequence}.
     *
     * @param synchronizer   used to block the game thread until updating is completed.
     * @param updateExecutor the thread pool that will update players in parallel.
     */
    public PlayerUpdateSequence(Phaser synchronizer, ExecutorService updateExecutor) {
        this.synchronizer = synchronizer;
        this.updateExecutor = updateExecutor;
    }

    @Override
    public void executePreUpdate(Player player) {
        try {
            player.getAutoSaving().process();
            Queue<ChatMessage> messages = player.getChatMessageQueue();
            SummoningMonsters.runEvent(player);
            //TODO REMOVE THIS SHIT WTF
            if (player.cE.summonedNpc != null) {
                player.cE.summonedNpc.ownerId = player.getIndex();
            }
            if (messages.size() > 0) {
                ChatMessage message = player.getChatMessageQueue().poll();
                if (message != null) {
                    player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.CHAT);
                    player.setCurrentChatMessage(message);
                }
            } else {
                player.setCurrentChatMessage(null);
            }
            player.getWalkingQueue().processNextMovement();

            try {
                if (player.isFollowing != null) {
                    int dis = player.getPosition().distance(player.isFollowing.getPosition());
                    if (dis <= 20 && dis > 1) {
                        try {
                            int toX = player.isFollowing.getPosition().getX();
                            int toY = player.isFollowing.getPosition().getY();
                            if (player.isFollowing.getWalkingQueue().getPublicPoint() != null) {
                                toX = player.isFollowing.getWalkingQueue().getPublicPoint().getX();
                                toY = player.isFollowing.getWalkingQueue().getPublicPoint().getY();
                            }
                            int baseX = player.getPosition().getX() - 25;
                            int baseY = player.getPosition().getY() - 25;
                            player.getWalkingQueue().reset();
                            player.getWalkingQueue().setRunningQueue(true);
                            Path p = PathTest.getPath(player.getPosition().getX(), player.getPosition().getY(), toX, toY);
                            if (p != null) {
                                for (int i = 1; i < p.getLength(); i++) {
                                    if ((baseX + p.getX(i)) != toX || (baseY + p.getY(i)) != toY)
                                        player.getWalkingQueue().addStep((baseX + p.getX(i)), (baseY + p.getY(i)));
                                }
                                player.getWalkingQueue().finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!stakeReset(player) && player.cE.getOpponent() != null) {
                    if (!Combat.processCombat(player.cE))
                        Combat.resetAttack(player.cE);
                }

                player.getWalkingQueue().walkingCheck();
            }
        } catch (Exception e) {
            e.printStackTrace();
            World.unregister(player);
        }
    }

    public static boolean stakeReset(final Player player) {
        final Player opp = player.getTrader();
        if (opp != null && !opp.isDead() && !player.isDead() && !opp.getSession().isConnected() && !player.getSession().isConnected() && player.duelAttackable > 0 && opp.duelAttackable > 0) {
            FileLogging.savePlayerLog(opp, "Duel TIE against " + player.getName());
            FileLogging.savePlayerLog(player, " Duel TIE against " + opp.getName());
            Container.transfer(player.getDuel(), player.getInventory());//jet is a smartie
            Container.transfer(opp.getDuel(), opp.getInventory());
            opp.setTeleportTarget(Position.create(3360 + Combat.random(17), 3274 + Combat.random(3), 0), false);
            player.setTeleportTarget(Position.create(3360 + Combat.random(17), 3274 + Combat.random(3), 0), false);
            return true;
        }
        return false;
    }

    @Override
    public void executeUpdate(Player player) {
        EngineTask callable = new EngineTask("Player updating for player " + player.getName(), 2, TimeUnit.SECONDS) {
            @Override
            public Boolean call() {
                try {
                    if (player.isMapRegionChanging()) {
                        player.getActionSender().sendMapRegion();
                    }

                    PacketBuilder updateBlock = new PacketBuilder();
                    PacketBuilder packet = new PacketBuilder(81, Packet.Type.VARIABLE_SHORT);
                    packet.startBitAccess();
                    updateThisPlayerMovement(player, packet);
                    updatePlayer(player, updateBlock, player, false, true);
                    packet.putBits(8, player.getLocalPlayers().size());
                    for (Iterator<Player> it$ = player.getLocalPlayers().iterator(); it$.hasNext(); ) {
                        Player otherPlayer = it$.next();
                        if (World.getPlayers().contains(otherPlayer) && !otherPlayer.isTeleporting() && otherPlayer.getPosition().isWithinDistance(player.getPosition()) && !otherPlayer.isHidden()) {
                            if (updateBlock.size() + packet.size() >= MAX_PACKET_SIZE) {
                                break;
                            }
                            updatePlayerMovement(packet, otherPlayer);

                            if (otherPlayer.getUpdateFlags().isUpdateRequired()) {
                                updatePlayer(player, updateBlock, otherPlayer, false, false);
                            }
                        } else {
                            it$.remove();
                            packet.putBits(1, 1);
                            packet.putBits(2, 3);
                        }
                    }

                    for (Player otherPlayer : RegionManager.getLocalPlayers(player)) {
                        if (player.getLocalPlayers().size() >= 255) {
                            break;
                        }

                        if (otherPlayer == player || player.getLocalPlayers().contains(otherPlayer) || otherPlayer.isHidden()) {
                            continue;
                        }

                        if (updateBlock.size() + packet.size() >= MAX_PACKET_SIZE) {
                            break;
                        }
                        player.getLocalPlayers().add(otherPlayer);
                        addNewPlayer(player, packet, otherPlayer);
                        updatePlayer(player, updateBlock, otherPlayer, true, false);
                    }

                    if (!updateBlock.isEmpty()) {
                        packet.putBits(11, 2047);
                        packet.finishBitAccess();
                        packet.put(updateBlock.toPacket().getPayload());
                    } else {
                        packet.finishBitAccess();
                    }

                    int size = packet.size();
                    if (size > maxSize)
                        maxSize = size;
                    player.write(packet.toPacket());

                    updateBlock = new PacketBuilder();
                    packet = new PacketBuilder(65, Packet.Type.VARIABLE_SHORT);
                    packet.startBitAccess();
                    packet.putBits(8, player.getLocalNPCs().size());
                    for (Iterator<NPC> it$ = player.getLocalNPCs().iterator(); it$.hasNext(); ) {
                        NPC npc = it$.next();
                        if (World.getNpcs().contains(npc) && !npc.isTeleporting() && !npc.isHidden() && npc.getPosition().isWithinDistance(player.getPosition())) {
                            updateNPCMovement(packet, npc);
                            if (npc.getUpdateFlags().isUpdateRequired()) {
                                updateNPC(updateBlock, npc);
                            }
                        } else {
                            it$.remove();
                            packet.putBits(1, 1);
                            packet.putBits(2, 3);
                        }
                    }
                    for (NPC npc : RegionManager.getLocalNpcs(player)) {
                        if (player.getLocalNPCs().size() >= 255) {
                            break;
                        }
                        if (player.getLocalNPCs().contains(npc) || npc.isHidden()) {
                            continue;
                        }
                        player.getLocalNPCs().add(npc);
                        addNewNPC(player, packet, npc);
                        if (npc.getUpdateFlags().isUpdateRequired()) {
                            updateNPC(updateBlock, npc);

                        }
                    }
                    if (!updateBlock.isEmpty()) {
                        packet.putBits(14, 16383);
                        packet.finishBitAccess();
                        packet.put(updateBlock.toPacket().getPayload());
                    } else {
                        packet.finishBitAccess();
                    }
                    player.write(packet.toPacket());
                } catch (Exception e) {
                    e.printStackTrace();
                    World.unregister(player);
                } finally {
                    synchronizer.arriveAndDeregister();
                }
                return true;
            }
        };

        //Here we submit the task
        Future future = updateExecutor.submit(callable);

        try {
            future.get(callable.getTimeout(), callable.getTimeUnit());
        } catch(TimeoutException e) {
            future.cancel(true);
            Server.getLogger().warning("Player update task '" + callable.getTaskName() + "' took too long, cancelled");
            synchronizer.arriveAndDeregister();
        } catch(Exception e) {
            e.printStackTrace();
            synchronizer.arriveAndDeregister();
        }
    }

    @Override
    public void executePostUpdate(Player player) {
        try {
            if (player.getUpdateFlags().get(UpdateFlags.UpdateFlag.HIT_3)) {
                player.getUpdateFlags().reset();
                player.getDamage().setHit1(player.getDamage().getHit3());
                player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.HIT);
            } else
                player.getUpdateFlags().reset();
            player.setTeleporting(false);
            player.setMapRegionChanging(false);
            if (player.cE != null)
                player.cE.isDoingAtk = false;
            player.resetCachedUpdateBlock();
            player.reset();
        } catch (Exception e) {
            e.printStackTrace();
            World.unregister(player);
        }
    }

    /**
     * Adds a new NPC.
     *
     * @param packet The main packet.
     * @param npc    The npc to add.
     */
    private void addNewNPC(Player player, PacketBuilder packet, NPC npc) {
        /*
		 * Write the NPC's index.
		 */
        packet.putBits(14, npc.getIndex());

		/*
		 * Calculate the x and y offsets.
		 */
        int yPos = npc.getPosition().getY() - player.getPosition().getY();
        int xPos = npc.getPosition().getX() - player.getPosition().getX();

		/*
		 * And write them.
		 */
        packet.putBits(5, yPos);
        packet.putBits(5, xPos);

		/*
		 * TODO unsure, but probably discards the client-side walk queue.
		 */
        packet.putBits(1, 0);

		/*
		 * We now write the NPC type id.
		 */
        packet.putBits(14, npc.getDefinition().getId());//should be 12, upgrading to a better client

		/*
		 * And indicate if an update is required.
		 */
        packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);

    }

    /**
     * Update an NPC.
     *
     * @param packet The update block.
     * @param npc    The npc.
     */
    private void updateNPC(PacketBuilder packet, NPC npc) {
		/*
		 * Calculate the mask.
		 */
        int mask = 0;
        final UpdateFlags flags = npc.getUpdateFlags();

        if (flags.get(UpdateFlags.UpdateFlag.ANIMATION)) {
            mask |= 0x10;
        }
        if (flags.get(UpdateFlags.UpdateFlag.HIT_2)) {
            mask |= 0x8;//0x40
        }
        if (flags.get(UpdateFlags.UpdateFlag.GRAPHICS)) {
            mask |= 0x80;
        }
        if (flags.get(UpdateFlags.UpdateFlag.FACE_ENTITY)) {
            mask |= 0x20;
        }
        if (flags.get(UpdateFlags.UpdateFlag.FORCED_CHAT)) {
            mask |= 0x1;
        }
        if (flags.get(UpdateFlags.UpdateFlag.HIT)) {
            mask |= 0x40;//0x8
        }
        if (flags.get(UpdateFlags.UpdateFlag.TRANSFORM)) {
            mask |= 0x2;
        }
        if (flags.get(UpdateFlags.UpdateFlag.FACE_COORDINATE)) {
            mask |= 0x4;
        }

		/*
		 * And write the mask.
		 */
        packet.put((byte) mask);

        if (flags.get(UpdateFlags.UpdateFlag.ANIMATION)) {
            packet.putLEShort(npc.getCurrentAnimation().getId());
            packet.put((byte) npc.getCurrentAnimation().getDelay());
        }
        if (flags.get(UpdateFlags.UpdateFlag.HIT_2)) {
            appendHit2Update(npc, packet);
        }
        if (flags.get(UpdateFlags.UpdateFlag.GRAPHICS)) {
            packet.putShort(npc.getCurrentGraphic().getId());
            packet.putInt(npc.getCurrentGraphic().getDelay());
        }
        if (flags.get(UpdateFlags.UpdateFlag.FACE_ENTITY)) {
            Entity entity = npc.getInteractingEntity();
            packet.putShort(entity == null ? -1 : entity.getClientIndex());
        }
        if (flags.get(UpdateFlags.UpdateFlag.FORCED_CHAT)) {
            //send a string
            packet.putRS2String(npc.forcedMessage);
        }
        if (flags.get(UpdateFlags.UpdateFlag.HIT)) {
            appendHitUpdate(npc, packet);
        }
        if (flags.get(UpdateFlags.UpdateFlag.TRANSFORM)) {
            //packet.putLEShortA();
        }
        if (flags.get(UpdateFlags.UpdateFlag.FACE_COORDINATE)) {
            Position loc = npc.getFaceLocation();
            if (loc == null) {
                packet.putLEShort(0);
                packet.putLEShort(0);
            } else {
                packet.putLEShort(loc.getX() * 2 + 1);
                packet.putLEShort(loc.getY() * 2 + 1);
            }
        }
    }

    /**
     * Update an NPC's movement.
     *
     * @param packet The main packet.
     * @param npc    The npc.
     */
    private void updateNPCMovement(PacketBuilder packet, NPC npc) {
        //NPCFacing.faceBankers(player);
		/*
		 * Check if the NPC is running.
		 */
        if (npc.getSprites().getSecondarySprite() == -1) {
			/*
			 * They are not, so check if they are walking.
			 */
            if (npc.getSprites().getPrimarySprite() == -1) {
				/*
				 * They are not walking, check if the NPC needs an update.
				 */
                if (npc.getUpdateFlags().isUpdateRequired()) {
					/*
					 * Indicate an update is required.
					 */
                    packet.putBits(1, 1);

					/*
					 * Indicate we didn't move.
					 */
                    packet.putBits(2, 0);
                } else {
					/*
					 * Indicate no update or movement is required.
					 */
                    packet.putBits(1, 0);
                }
            } else {
				/*
				 * They are walking, so indicate an update is required.
				 */
                packet.putBits(1, 1);

				/*
				 * Indicate the NPC is walking 1 tile.
				 */
                packet.putBits(2, 1);

				/*
				 * And write the direction.
				 */
                packet.putBits(3, npc.getSprites().getPrimarySprite());

				/*
				 * And write the update flag.
				 */
                packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
            }
        } else {
			/*
			 * They are running, so indicate an update is required.
			 */
            packet.putBits(1, 1);

			/*
			 * Indicate the NPC is running 2 tiles.
			 */
            packet.putBits(2, 2);

			/*
			 * And write the directions.
			 */
            packet.putBits(3, npc.getSprites().getPrimarySprite());
            packet.putBits(3, npc.getSprites().getSecondarySprite());

			/*
			 * And write the update flag.
			 */
            packet.putBits(1, npc.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        }
    }

    /**
     * Adds a new player.
     *
     * @param packet      The packet.
     * @param otherPlayer The player.
     */
    public void addNewPlayer(Player player, PacketBuilder packet, Player otherPlayer) {
		/*
		 * Write the player index.
		 */
        packet.putBits(11, otherPlayer.getIndex());

		/*
		 * Write two flags here: the first indicates an update is required
		 * (this is always true as we add the appearance after adding a player)
		 * and the second to indicate we should discard client-side walk
		 * queues.
		 */
        packet.putBits(1, 1);
        packet.putBits(1, 1);

		/*
		 * Calculate the x and y offsets.
		 */
        int yPos = otherPlayer.getPosition().getY() - player.getPosition().getY();
        int xPos = otherPlayer.getPosition().getX() - player.getPosition().getX();

		/*
		 * Write the x and y offsets.
		 */
        packet.putBits(5, yPos);
        packet.putBits(5, xPos);
    }

    /**
     * Updates a non-this player's movement.
     *
     * @param packet      The packet.
     * @param otherPlayer The player.
     */
    public void updatePlayerMovement(PacketBuilder packet, Player otherPlayer) {
		/*
		 * Check which type of movement took place.
		 */
        if (otherPlayer.getSprites().getPrimarySprite() == -1) {
			/*
			 * If no movement did, check if an update is required.
			 */
            if (otherPlayer.getUpdateFlags().isUpdateRequired()) {
				/*
				 * Signify that an update happened.
				 */
                packet.putBits(1, 1);

				/*
				 * Signify that there was no movement.
				 */
                packet.putBits(2, 0);
            } else {
				/*
				 * Signify that nothing changed.
				 */
                packet.putBits(1, 0);
            }
        } else if (otherPlayer.getSprites().getSecondarySprite() == -1) {
			/*
			 * The player moved but didn't run. Signify that an update is
			 * required.
			 */
            packet.putBits(1, 1);

			/*
			 * Signify we moved one tile.
			 */
            packet.putBits(2, 1);

			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
            packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());

			/*
			 * Write a flag indicating if a block update happened.
			 */
            packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        } else {
			/*
			 * The player ran. Signify that an update happened.
			 */
            packet.putBits(1, 1);

			/*
			 * Signify that we moved two tiles.
			 */
            packet.putBits(2, 2);

			/*
			 * Write the primary sprite (i.e. walk direction).
			 */
            packet.putBits(3, otherPlayer.getSprites().getPrimarySprite());

			/*
			 * Write the secondary sprite (i.e. run direction).
			 */
            packet.putBits(3, otherPlayer.getSprites().getSecondarySprite());

			/*
			 * Write a flag indicating if a block update happened.
			 */
            packet.putBits(1, otherPlayer.getUpdateFlags().isUpdateRequired() ? 1 : 0);
        }
    }

    /**
     * Updates a player.
     *
     * @param packet          The packet.
     * @param otherPlayer     The other player.
     * @param forceAppearance The force appearance flag.
     * @param noChat          Indicates chat should not be relayed to this player.
     */
    public void updatePlayer(Player player, PacketBuilder packet, Player otherPlayer, boolean forceAppearance, boolean noChat) {
		/*
		 * If no update is required and we don't have to force an appearance
		 * update, don't write anything.
		 */
        if (!otherPlayer.getUpdateFlags().isUpdateRequired() && !forceAppearance) {
            return;
        }

		/*
		 * We can used the cached update block!
		 */
        synchronized (otherPlayer) {
            if (otherPlayer.hasCachedUpdateBlock() && otherPlayer != player && !forceAppearance && !noChat) {
                packet.put(otherPlayer.getCachedUpdateBlock().getPayload().flip());
                return;
            }

			/*
			 * We have to construct and cache our own block.
			 */
            PacketBuilder block = new PacketBuilder();

			/*
			 * Calculate the bitmask.
			 */
            int mask = 0;
            final UpdateFlags flags = otherPlayer.getUpdateFlags();

            if (flags.get(UpdateFlags.UpdateFlag.WALK)) {
                mask |= 0x400;
            }
            if (flags.get(UpdateFlags.UpdateFlag.GRAPHICS)) {
                mask |= 0x100;
            }
            if (flags.get(UpdateFlags.UpdateFlag.ANIMATION)) {
                mask |= 0x8;
            }
            if (flags.get(UpdateFlags.UpdateFlag.FORCED_CHAT)) {
                mask |= 0x4;
            }
            if (flags.get(UpdateFlags.UpdateFlag.CHAT) && !noChat) {
                mask |= 0x80;
            }
            if (flags.get(UpdateFlags.UpdateFlag.FACE_ENTITY)) {
                mask |= 0x1;
            }
            if (flags.get(UpdateFlags.UpdateFlag.APPEARANCE) || forceAppearance) {
                mask |= 0x10;
            }
            if (flags.get(UpdateFlags.UpdateFlag.FACE_COORDINATE)) {
                mask |= 0x2;
            }
            if (flags.get(UpdateFlags.UpdateFlag.HIT)) {
                mask |= 0x20;
            }
            if (flags.get(UpdateFlags.UpdateFlag.HIT_2)) {
                mask |= 0x200;
            }

			/*
			 * Check if the bitmask would overflow a byte.
			 */
            if (mask >= 0x100) {
				/*
				 * Write it as a short and indicate we have done so.
				 */
                mask |= 0x40;
                block.put((byte) (mask & 0xFF));
                block.put((byte) (mask >> 8));
            } else {
				/*
				 * Write it as a byte.
				 */
                block.put((byte) (mask));
            }

			/*
			 * Append the appropriate updates.
			 */
            if (flags.get(UpdateFlags.UpdateFlag.WALK)) {
                appendForceMovement(block, otherPlayer);
            }
            if (flags.get(UpdateFlags.UpdateFlag.GRAPHICS)) {
                appendGraphicsUpdate(block, otherPlayer);
            }
            if (flags.get(UpdateFlags.UpdateFlag.ANIMATION)) {
                appendAnimationUpdate(block, otherPlayer);
            }
            if (flags.get(UpdateFlags.UpdateFlag.FORCED_CHAT)) {
                block.putRS2String(otherPlayer.forcedMessage);
            }
            if (flags.get(UpdateFlags.UpdateFlag.CHAT) && !noChat) {
                appendChatUpdate(block, otherPlayer);
            }
            if (flags.get(UpdateFlags.UpdateFlag.FACE_ENTITY)) {
                Entity entity = otherPlayer.getInteractingEntity();
                block.putLEShort(entity == null ? -1 : entity.getClientIndex());
            }
            if (flags.get(UpdateFlags.UpdateFlag.APPEARANCE) || forceAppearance) {
                appendPlayerAppearanceUpdate(block, player, otherPlayer);
            }
            if (flags.get(UpdateFlags.UpdateFlag.FACE_COORDINATE)) {
                Position loc = otherPlayer.getFaceLocation();
                if (loc == null) {
                    block.putLEShortA(0);
                    block.putLEShort(0);
                } else {
                    block.putLEShortA(loc.getX() * 2 + 1);
                    block.putLEShort(loc.getY() * 2 + 1);
                }
            }
            if (flags.get(UpdateFlags.UpdateFlag.HIT)) {
                appendHitUpdate(otherPlayer, block);
            }
            if (flags.get(UpdateFlags.UpdateFlag.HIT_2)) {
                appendHit2Update(otherPlayer, block);
            }

			/*
			 * Convert the block builder to a packet.
			 */
            Packet blockPacket = block.toPacket();

			/*
			 * Now it is over, cache the block if we can.
			 */
            if (otherPlayer != player && !forceAppearance && !noChat) {
                otherPlayer.setCachedUpdateBlock(blockPacket);
            }

			/*
			 * And finally append the block at the end.
			 */
            packet.put(blockPacket.getPayload());
        }
    }

    private static void appendHit2Update(final NPC n, final PacketBuilder updateBlock) {
        updateBlock.putShortA(n.getDamage().getHitDamage2());
        updateBlock.putByteC((byte) n.getDamage().getHitType2());
        updateBlock.put((byte) n.getDamage().getStyleType2());
        updateBlock.putByteA((byte) n.health);
        updateBlock.put((byte) n.maxHealth);

        //System.out.println();
    }

    public static int getCurrentHP(int i, int i1, int i2) {
        double x = (double) i / (double) i1;
        return (int) Math.round(x * i2);
    }

    private static void appendHitUpdate(final NPC n, final PacketBuilder updateBlock) {
        updateBlock.putShortA(n.getDamage().getHitDamage1());
        updateBlock.putByteS((byte) n.getDamage().getHitType1());
        updateBlock.put((byte) n.getDamage().getStyleType1());
        updateBlock.putByteS((byte) getCurrentHP(n.health, n.maxHealth, 100));
        updateBlock.putByteC((byte) 100);
    }

    private static void appendHit2Update(final Player p, final PacketBuilder updateBlock) {
        updateBlock.putShortA((byte) p.getDamage().getHitDamage2());
        updateBlock.putByteS((byte) p.getDamage().getHitType2());
        updateBlock.put((byte) p.getDamage().getStyleType2());
        updateBlock.put((byte) p.getSkills().getLevel(3));
        updateBlock.putByteC(p.getSkills().calculateMaxLifePoints());
    }

    private static void appendHitUpdate(final Player p, final PacketBuilder updateBlock) {
        updateBlock.putShortA(p.getDamage().getHitDamage1());
        updateBlock.putByteA(p.getDamage().getHitType1());
        updateBlock.put((byte) p.getDamage().getStyleType1());
        updateBlock.putByteC(p.getSkills().getLevel(3));
        updateBlock.put((byte) p.getSkills().calculateMaxLifePoints());
    }

    /**
     * Appends an animation update.
     *
     * @param block       The update block.
     * @param otherPlayer The player.
     */
    private void appendAnimationUpdate(PacketBuilder block, Player otherPlayer) {
        block.putLEShort(otherPlayer.getCurrentAnimation().getId());
        block.putByteC(otherPlayer.getCurrentAnimation().getDelay());
    }

    public void appendForceMovement(PacketBuilder block, Player otherPlayer) {
        Position loc = Position.create(otherPlayer.forceWalkX1, otherPlayer.forceWalkY1, 0);
        Position loc2 = Position.create(otherPlayer.forceWalkX2, otherPlayer.forceWalkY2, 0);
        block.putByteS((byte) (loc.getLocalX(otherPlayer.getPosition())));
        block.putByteS((byte) (loc.getLocalY(otherPlayer.getPosition())));
        block.putByteS((byte) (loc2.getLocalX(otherPlayer.getPosition())));
        block.putByteS((byte) (loc2.getLocalY(otherPlayer.getPosition())));
        block.putLEShortA(otherPlayer.forceSpeed1);
        block.putShortA(otherPlayer.forceSpeed2);
        block.putByteS((byte) otherPlayer.forceDirection);
    }

    /**
     * Appends a graphics update.
     *
     * @param block       The update block.
     * @param otherPlayer The player.
     */
    private void appendGraphicsUpdate(PacketBuilder block, Player otherPlayer) {
        block.putLEShort(otherPlayer.getCurrentGraphic().getId());
        block.putInt(otherPlayer.getCurrentGraphic().getDelay());
    }

    /**
     * Appends a chat text update.
     *
     * @param packet      The packet.
     * @param otherPlayer The player.
     */
    private void appendChatUpdate(PacketBuilder packet, Player otherPlayer) {
        ChatMessage cm = otherPlayer.getCurrentChatMessage();
        if (cm == null) {
            return;
        }
        byte[] bytes = cm.getText();

        packet.putLEShort(((cm.getColour() & 0xFF) << 8) | (cm.getEffects() & 0xFF));
        packet.put((byte) Rank.getPrimaryRankIndex(otherPlayer));
        packet.putByteC(bytes.length);
        for (int ptr = bytes.length - 1; ptr >= 0; ptr--) {
            packet.put(bytes[ptr]);
        }
    }

    /**
     * Appends an appearance update.
     *
     * @param packet      The packet.
     * @param otherPlayer The player.
     */
    private void appendPlayerAppearanceUpdate(PacketBuilder packet, Player player, Player otherPlayer) {
        if (otherPlayer.isHidden() && otherPlayer != player)
            return;
        Appearance app = otherPlayer.getAppearance();
        Container eq = otherPlayer.getEquipment();

        PacketBuilder playerProps = new PacketBuilder();
        playerProps.put((byte) app.getGender());
        playerProps.put((byte) otherPlayer.headIconId);
        byte skull = -1;
        if (otherPlayer.isSkulled())
            skull = 0;
        playerProps.put(skull);
        if (!otherPlayer.getNpcState()) {
            for (int i = 0; i < 4; i++) {
                if (eq.isSlotUsed(i)) {
                    playerProps.putShort((short) 0x200 + eq.get(i).getId());
                } else {
                    playerProps.put((byte) 0);
                }
            }
            if (eq.isSlotUsed(Equipment.SLOT_CHEST)) {
                playerProps.putShort((short) 0x200 + eq.get(Equipment.SLOT_CHEST).getId());
            } else {
                playerProps.putShort((short) 0x100 + app.getChest());
            }
            if (eq.isSlotUsed(Equipment.SLOT_SHIELD)) {
                playerProps.putShort((short) 0x200 + eq.get(Equipment.SLOT_SHIELD).getId());
            } else {
                playerProps.put((byte) 0);
            }
            Item chest = eq.get(Equipment.SLOT_CHEST);
            if (chest != null) {
                if (!Equipment.is(Equipment.EquipmentType.PLATEBODY, chest)) {
                    playerProps.putShort((short) 0x100 + app.getArms());
                } else {
                    playerProps.putShort((short) 0x200 + chest.getId());
                }
            } else {
                playerProps.putShort((short) 0x100 + app.getArms());
            }
            if (eq.isSlotUsed(Equipment.SLOT_BOTTOMS)) {
                playerProps.putShort((short) 0x200 + eq.get(Equipment.SLOT_BOTTOMS).getId());
            } else {
                playerProps.putShort((short) 0x100 + app.getLegs());
            }
            Item helm = eq.get(Equipment.SLOT_HELM);
            if (helm != null) {
                if (!Equipment.is(Equipment.EquipmentType.FULL_HELM, helm) && !Equipment.is(Equipment.EquipmentType.FULL_MASK, helm)) {
                    playerProps.putShort((short) 0x100 + app.getHead());
                } else {
                    playerProps.put((byte) 0);
                }
            } else {
                playerProps.putShort((short) 0x100 + app.getHead());
            }

            if (eq.isSlotUsed(Equipment.SLOT_GLOVES)) {
                playerProps.putShort((short) 0x200 + eq.get(Equipment.SLOT_GLOVES).getId());
            } else {
                playerProps.putShort((short) 0x100 + app.getHands());
            }
            if (eq.isSlotUsed(Equipment.SLOT_BOOTS)) {
                playerProps.putShort((short) 0x200 + eq.get(Equipment.SLOT_BOOTS).getId());
            } else {
                playerProps.putShort((short) 0x100 + app.getFeet());
            }
            boolean fullHelm = true;
            if (helm != null) {
                fullHelm = !Equipment.is(Equipment.EquipmentType.FULL_HELM, helm);
            }
            if (app.getGender() != 1 && fullHelm) {
                playerProps.putShort((short) 0x100 + app.getBeard());
            } else {
                playerProps.put((byte) 0);
            }
        } else {
            playerProps.putShort(-1);
            playerProps.putShort(otherPlayer.getNpcId());

        }
        //	System.out.println("END");
        playerProps.put((byte) app.getHairColour()); // hairc
        playerProps.put((byte) app.getTorsoColour()); // torsoc
        playerProps.put((byte) app.getLegColour()); // legc
        playerProps.put((byte) app.getFeetColour()); // feetc
        playerProps.put((byte) app.getSkinColour()); // skinc

        playerProps.putShort((short) app.getStandAnim()); // stand
        playerProps.putShort((short) 0x337); // stand turn
        playerProps.putShort((short) app.getWalkAnim()); // walk
        playerProps.putShort((short) 0x334); // turn 180
        playerProps.putShort((short) 0x335); // turn 90 cw
        playerProps.putShort((short) 0x336); // turn 90 ccw
        playerProps.putShort((short) app.getRunAnim()); // run
        playerProps.putRS2String(otherPlayer.getDisplay());
        playerProps.put((byte) otherPlayer.getSkills().getCombatLevel()); // combat level
        playerProps.putShort(0); // (skill-level instead of combat-level) otherPlayer.getSkills().getTotalLevel()); // total level
        playerProps.putShort(otherPlayer.getKillCount());
        final int id = eq.getItemId(Equipment.SLOT_CAPE);
        //commented out until client changes
        if (eq.isSlotUsed(Equipment.SLOT_CAPE) && (id == 12747 || id == 12744)) {
            playerProps.put((byte) 1);
            if (id == 12747) {
                playerProps.putInt(otherPlayer.compCapePrimaryColor);
                playerProps.putInt(otherPlayer.compCapeSecondaryColor);
            } else {
                playerProps.putInt(otherPlayer.maxCapePrimaryColor);
                playerProps.putInt(otherPlayer.maxCapeSecondaryColor);
            }
        } else {
            playerProps.put((byte) 0);
        }


        playerProps.put((byte) otherPlayer.getGameMode());

        //playerProps.putLong(NameUtils.nameToLong(clanName));

        /*final List<Recolor> recolors = otherPlayer.getRecolorManager().getAll();
        final Iterator<Recolor> itr = recolors.iterator();
        while(itr.hasNext())
            if(!eq.contains(itr.next().getId()))
                itr.remove();
        playerProps.putShort(recolors.size());
        for(final Recolor recolor : recolors)
            playerProps.putRS2String(recolor.toString());


        //System.out.println("player = otherPlayer: " + (player == otherPlayer)); */

        Packet propsPacket = playerProps.toPacket();

        packet.putByteC(propsPacket.getLength());
        packet.put(propsPacket.getPayload());
    }

    /**
     * Updates this player's movement.
     *
     * @param packet The packet.
     */
    private void updateThisPlayerMovement(Player player, PacketBuilder packet) {
		/*
		 * Check if the player is teleporting.
		 */
        if (player.isTeleporting() || player.isMapRegionChanging()) {
			/*
			 * They are, so an update is required.
			 */
            packet.putBits(1, 1);

			/*
			 * This value indicates the player teleported.
			 */
            packet.putBits(2, 3);

			/*
			 * This is the new player height.
			 */
            packet.putBits(2, player.getPosition().getZ());

			/*
			 * This indicates that the client should discard the walking queue.
			 */
            packet.putBits(1, 1);

			/*
			 * This flag indicates if an update block is appended.
			 */
            packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);

			/*
			 * These are the positions.
			 */
            packet.putBits(7, player.getPosition().getLocalY(player.getLastKnownRegion()));
            packet.putBits(7, player.getPosition().getLocalX(player.getLastKnownRegion()));
        } else {
			/*
			 * Otherwise, check if the player moved.
			 */
            if (player.getSprites().getPrimarySprite() == -1) {
				/*
				 * The player didn't move. Check if an update is required.
				 */
                if (player.getUpdateFlags().isUpdateRequired()) {
					/*
					 * Signifies an update is required.
					 */
                    packet.putBits(1, 1);

					/*
					 * But signifies that we didn't move.
					 */
                    packet.putBits(2, 0);
                } else {
					/*
					 * Signifies that nothing changed.
					 */
                    packet.putBits(1, 0);
                }
            } else {
				/*
				 * Check if the player was running.
				 */
                if (player.getSprites().getSecondarySprite() == -1) {
					/*
					 * The player walked, an update is required.
					 */
                    packet.putBits(1, 1);

					/*
					 * This indicates the player only walked.
					 */
                    packet.putBits(2, 1);

					/*
					 * This is the player's walking direction.
					 */
                    packet.putBits(3, player.getSprites().getPrimarySprite());

					/*
					 * This flag indicates an update block is appended.
					 */
                    packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
                } else {
					/*
					 * The player ran, so an update is required.
					 */
                    packet.putBits(1, 1);

					/*
					 * This indicates the player ran.
					 */
                    packet.putBits(2, 2);

					/*
					 * This is the walking direction.
					 */
                    packet.putBits(3, player.getSprites().getPrimarySprite());

					/*
					 * And this is the running direction.
					 */
                    packet.putBits(3, player.getSprites().getSecondarySprite());

					/*
					 * And this flag indicates an update block is appended.
					 */
                    packet.putBits(1, player.getUpdateFlags().isUpdateRequired() ? 1 : 0);
                }
            }
        }
    }
}