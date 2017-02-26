package org.hyperion.rs2.model.content.transport.tabs;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel
 */
public enum Tab {
    VARROCK(8007, 3216, 3424, 0),
    LUMBRIDGE(8008, 3221, 3217, 0),
    FALADOR(8009, 2964, 3370, 0),
    CAMELOT(8010, 2756, 3479, 0),
    ARDOUGNE(8011, 2661, 3306, 0),
    WATCHTOWER(8012, 2549, 3113, 0),
    BOUNTY(18806) {
        @Override
        public void process(final Player player) {
            final Player target = player.getBountyHunter().getTarget();
            if (target != null) {
                if (target.getPosition().inPvPArea()) {
                    final int x = target.getPosition().getX();
                    final int y = target.getPosition().getY();
                    final int wild = Combat.getWildLevel(x, y);
                    final boolean multi = Combat.isInMulti(target.cE);
                    if (wild <= 20 && !multi) {
                        teleport(player, getId(), x, y, target.getPosition().getZ());
                    } else {
                        DialogueManager.openDialogue(player, 171);
                    }
                }
            }
        }
    };

    private static final Map<Integer, Tab> BY_INTEGER_VALUE = Stream.of(values()).collect(Collectors.toMap(Tab::getId, Function.identity()));
    private final int id, x, y, z;

    Tab(int id, int x, int y, int z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Tab(int id) { this(id, -1, -1, -1); }

    public static Tab getByIntegerValue(final int value) { return BY_INTEGER_VALUE.get(value); }

    public static void teleport(final NPC npc, final int x, final int y, final int z) {
        npc.playAnimation(Animation.create(4069, 0));
        npc.playGraphics(Graphic.create(678, 0));
        Combat.resetAttack(npc.cE);
        World.submit(new Task(1200L, "Tab Task 1") {
            @Override
            public void execute() {
                npc.playAnimation(Animation.create(4071));
                stop();

            }
        });
        World.submit(new Task(2400L, "Tab Task 2") {
            @Override
            public void execute() {
                npc.setTeleportTarget(Position.create(x + Combat.random(1), y + Combat.random(1), z));
                npc.playAnimation(Animation.create(-1));
                npc.setTeleporting(false);
                stop();
            }
        });
    }

    public static void teleport(final Player player, final int id, final int x, final int y, final int z) {
        player.getSkills().stopSkilling();
        final Item item = Item.create(id, 1);
        if (item == null || !player.getInventory().hasItem(item)) {
            return;
        }
        if (player.isTeleBlocked()) {
            player.sendMessage("You are currently teleblocked.");
            return;
        }
        if (player.isDead() || player.getTimeSinceLastTeleport() < 4600 || !player.getLocation().canTeleport(player)) {
            return;
        }
        player.getInventory().remove(item);
        player.inAction = false;
        if ((player.getPosition().getX() >= 2814 && player.getPosition().getX() <= 2942 && player.getPosition().getY() >= 5250 && player.getPosition().getY() <= 5373)
                && (x < 2814 || x > 2942 || y < 5250 || y > 5373)) {
            player.getActionSender().showInterfaceWalkable(-1);
        }
        player.playAnimation(Animation.create(4069, 0));
        player.playGraphics(Graphic.create(678, 0));
        Combat.resetAttack(player.cE);
        player.updateTeleportTimer();
        World.submit(new Task(1200L, "Tab Task 1") {
            @Override
            public void execute() {
                player.playAnimation(Animation.create(4071));
                stop();

            }
        });
        World.submit(new Task(2400L, "Tab Task 2") {
            @Override
            public void execute() {
                player.setTeleportTarget(Position.create(x + Combat.random(1), y + Combat.random(1), z));
                player.playAnimation(Animation.create(-1));
                stop();
            }
        });
    }

    public int getId() { return id; }

    public void process(Player player) { teleport(player, id, x, y, z); }
}
