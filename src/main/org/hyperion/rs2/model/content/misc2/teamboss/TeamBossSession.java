package org.hyperion.rs2.model.content.misc2.teamboss;

import org.hyperion.engine.task.impl.NpcDeathTask;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.model.content.specialareas.SpecialArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/19/14
 * Time: 1:07 PM
 *
 * To change this template use File | Settings | File Templates.
 */
public abstract class TeamBossSession {

    private static List<TeamBossSession> sessions = new ArrayList<>();

    protected final List<NPC> npcs;
    protected final List<Player> players;
    protected final NPCDrop[] rewards;
    protected final int x;
    protected final int y;

    public TeamBossSession(final int x, final int y, final NPCDrop[] rewards, final NPC[] npcs, final Player... players) {
        this.npcs = Arrays.asList(npcs);
        this.players = Arrays.asList(players);
        this.rewards = rewards;
        this.x = x;
        this.y = y;
        sessions.add(this);
    }

    public final void handleNpcDeath(final NPC npc) {
        if(npcs.remove(npc))
            if(npcs.size() == 0)
                handleReward();
    }

    public final void handlePlayerDeath(final Player player) {
        if(players.remove(player))  {
            if(players.isEmpty())
                destroySession();
            player.getTeamSessions().remove(this);
        }
    }

    public final void destroySession() {
        players.forEach(p -> {
            sendHome(p);
            players.remove(p);
            p.getTeamSessions().remove(TeamBossSession.this);
        });
        npcs.stream().map(NpcDeathTask::new).forEach(World::submit);
        sessions.remove(this);
    }

    public final void startSession() {
        final int pid = players.get(0).getIndex();
        for(final Player player : players)
            Magic.teleport(player, getStartLocation(pid), false);
    }

    public final void check() {
        for(final Iterator<Player> it = players.iterator(); it.hasNext();) {
            final Player p = it.next();
            if(p.isDead() || !getArea().inArea(p))
                it.remove();
        }
    }

    public abstract void handleReward();
    public abstract SpecialArea getArea();

    public void sendHome(final Player player) {
        player.setTeleportTarget(Edgeville.POSITION);
    }

    public Position getStartLocation(final int index) {
        return Position.create(x, y, index * 4);
    }

}
