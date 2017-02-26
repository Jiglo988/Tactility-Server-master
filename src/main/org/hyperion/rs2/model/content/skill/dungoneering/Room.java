package org.hyperion.rs2.model.content.skill.dungoneering;

import org.hyperion.rs2.model.Damage;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.NPCManager;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.content.skill.FishingV2;
import org.hyperion.util.Misc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 2/20/15
 * Time: 8:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class Room {

    private final List<NPC> npcs = new ArrayList<>();
    public final List<NPC> events = new ArrayList<>();

    private Room child, parent;
    boolean initialized;
    public boolean boss, must_clear;

    public final Dungeon dungeon;
    public final RoomDefinition definition;
    public int heightLevel;

    public Room(final Dungeon dungeon, final RoomDefinition def, final int heightLevel) {
        this.dungeon = dungeon;
        this.definition = def;
        this.heightLevel = heightLevel;
    }

    public boolean cleared() {
        for(final NPC npc : npcs) {
            if(!npc.isDead())
                return false;
        }
        return initialized;
    }

    public Room getChild(){
        return child;
    }

    public void setChild(final Room child) {
                this.child = child;
    }

    public Room getParent() {
        return parent;
    }

    public void setParent(final Room parent) {
        this.parent = parent;
    }

    public void initialize() {
        if(initialized)
            return;
        initialized = true;
        int npcCount = boss ? 1 : ((1 + Misc.random(dungeon.difficulty.spawns + dungeon.teamSize)));
        must_clear = Misc.random(10) < 7;
        for(int i = 0; i < npcCount; i++) {
            final Point loc = definition.randomLoc();
            final NPC npc = NPCManager.addNPC(randomLocation(), boss ? dungeon.difficulty.getBoss() : dungeon.difficulty.getRandomMonster(), -1);
            npc.agressiveDis = 10;
            npcs.add(npc);
        }

        if(Misc.random(7) == 0) {
            switch(Misc.random(2)) {
                default:
                    final NPC npc = NPCManager.addNPC(randomLocation(), FishingV2.FISHING_SPOTS[Misc.random(FishingV2.FISHING_SPOTS.length - 1)], -1);
                    events.add(npc);
                    break;
                case 1:
                    final NPC npc2 = NPCManager.addNPC(randomLocation(), Misc.random(1) == 0 ? 8824 : 8827, -1);
                    events.add(npc2);
                    break;
            }
        }
    }


    public Position getSpawnLocation() {
        return Position.create(definition.x, definition.y, heightLevel);
    }

    public Position getEndLocation() {
        return Position.create(definition.x_end, definition.y_end, heightLevel);
    }

    public Position randomLocation() {
        final Point point = definition.randomLoc();
        return Position.create(point.x, point.y, heightLevel);
    }

    public void destroy() {
        for(NPC npc : npcs) {
            if(!npc.isDead()) {
                npc.serverKilled = true;
                npc.inflictDamage(new Damage.Hit(npc.health, Damage.HitType.NORMAL_DAMAGE, 0), null);
            }
        }
        for(NPC npc : events) {
            if(!npc.isDead()) {
                npc.serverKilled = true;
                npc.inflictDamage(new Damage.Hit(npc.health, Damage.HitType.NORMAL_DAMAGE, 0), null);
            }
        }
        npcs.clear();
        events.clear();
    }

}
