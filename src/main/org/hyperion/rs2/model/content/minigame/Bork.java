package org.hyperion.rs2.model.content.minigame;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.combat.attack.BorkAndMinions;
import org.hyperion.rs2.model.content.misc.ItemSpawning;
import org.hyperion.rs2.model.content.misc.Percentage;
import org.hyperion.rs2.model.content.misc2.Edgeville;
import org.hyperion.rs2.model.shops.PvMStore;
import org.hyperion.util.Time;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 6/9/15
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bork {

    private static final String KEY = "borkevent";
    private static final String TIME_KEY = "borktime";

    public static final String getTimeKey() {
        return TIME_KEY;
    }

    private static final Position TELEPORT_POSITION = Position.create(3555, 9947, 0);

    public static final Position getTeleportPosition() {
        return TELEPORT_POSITION;
    }

    private static final Position BORK_POSITION = Position.create(3564, 9959, 0);
    private static final Point[] MINION_LOCATIONS = {
            new Point(3551, 9938), new Point(3563, 9941), new Point(3547, 9957)
    };
    private static final long DELAY = Time.TEN_HOURS/2L;

    public static final long getDelay() {
        return DELAY;
    }

    private static final int INTERFACE_ID = 6568;
    private static final int[] CHILD_IDS = {6569, 6570, 6572, 6664};

    static {
    }


    public static final class BorkEvent extends Task {
        private static final double PKP_MULTIPLIER = 5;
        private static final double TOKEN_MULTIPLIER = 1;
        /**
         * This represents the percent which is covered by time, rest is given
         */
        private static final double MOD = 50.0;
        private static final int ORIGINAL_TIME = 500;
        private final java.util.List<NPC> npcs = new ArrayList<>();

        private final Player player;
        private int time;

        public BorkEvent(final Player player) {
            super(1000);
            this.player = player;
            time = ORIGINAL_TIME;
            player.getPermExtraData().put(TIME_KEY, System.currentTimeMillis());
            player.getActionSender().showInterfaceWalkable(INTERFACE_ID);
            player.getExtraData().put(KEY, this);
            player.getActionSender().sendString(CHILD_IDS[0], "Kill BORK");
            //for(; i < CHILD_IDS.length; i++)
                //player.getActionSender().sendString(CHILD_IDS[i], "");
            final int height = player.getIndex() * 4;
            npcs.add(NPCManager.addNPC(BORK_POSITION.transform(0, 0, height),BorkAndMinions.BORK_ID, -1));
            for(int i = 0; i<3; i++)
                npcs.add(NPCManager.addNPC(Position.create(MINION_LOCATIONS[i].x, MINION_LOCATIONS[i].y, height), BorkAndMinions.MINION_ID, -1));


        }

        @Override
        public void execute()  {
            if(time > 0)
                time--;
            updateInterface();
        }

        public void giveReward(boolean kill) {
            int percentIncrease = (int)percentIncrease();
            int tokens = (int)(percentIncrease * TOKEN_MULTIPLIER);
            int pkt = (int)(percentIncrease * PKP_MULTIPLIER/2);
            if(!kill)
            {
                pkt = pkt/3;
                tokens = tokens/3;
                player.sendMessage("You did not manage to defeat bork.");
            }
            player.getBank().add(Item.create(PvMStore.TOKEN, tokens));
            player.getBank().add(Item.create(5020, pkt));
            player.sendf("%d PvM Tokens and %d Pk Tickets have been added to your bank", tokens, pkt);
        }

        public void updateInterface() {
            player.getActionSender().sendString(CHILD_IDS[2], String.format("%.1f %%", percentIncrease()));

        }

        public double percentIncrease() {
            final Percentage percent = new Percentage(time, ORIGINAL_TIME);
            return percent.toDouble(MOD);
        }


        @Override
        public void stop() {
            //stop event
            super.stop();
            //remove all variables
            player.getActionSender().removeAllInterfaces();
            player.getActionSender().showInterfaceWalkable(-1);
            player.getExtraData().remove(KEY);
            //kill all npcs
            for(NPC npc : npcs) {
                if(!npc.isDead()) {
                    npc.serverKilled = true;
                    npc.inflictDamage(new Damage.Hit(npc.health, Damage.HitType.NORMAL_DAMAGE, 0), null);
                }
            }
            //destroy reference to npcs, mem leak
            npcs.clear();



        }

    }

    public static boolean doDeath(final Player player) {
        if(player.getExtraData().get(KEY) == null) {
            return false;
        }
        ((BorkEvent)player.getExtraData().get(KEY)).giveReward(false);
        ((BorkEvent)player.getExtraData().get(KEY)).stop();
        player.setTeleportTarget(Edgeville.POSITION, false);
        return true;
    }

    public static boolean handleBorkDeath(final Player player, final NPC npc) {
        if(player.getExtraData().get(KEY) == null || npc.getDefinition().getId() != BorkAndMinions.BORK_ID) {
            return false;
        }
        ((BorkEvent)player.getExtraData().get(KEY)).giveReward(true);
        ((BorkEvent)player.getExtraData().get(KEY)).stop();
        player.setTeleportTarget(Edgeville.POSITION, false);
        return true;

    }
}
