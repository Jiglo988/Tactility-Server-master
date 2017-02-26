package org.hyperion.rs2.model;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.content.skill.summoning.Pouch;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.packet.ActionsManager;
import org.hyperion.rs2.packet.ButtonAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 6/20/2016.
 */
public class SummoningTab {

    static {
        Pouch.BUTTONS.forEach(value -> ActionsManager.getManager().submit(value, new ButtonAction() {
            @Override
            public boolean handle(Player player, int id) {
                final Pouch pouch = Pouch.getByButtonValue(id);
                return pouch != null && pouch.create(player);
            }
        }));
    }

    private final Player PLAYER;

    private final ActionSender SENDER;

    private final List<Pouch> UNLOCKED = new ArrayList<>();

    public SummoningTab(Player player) {
        this.PLAYER = player;
        this.SENDER = PLAYER.getActionSender();
        TaskManager.submit(new Task(2000L, PLAYER) {
            @Override
            protected void execute() {
                stop();
                Pouch.VALUES.forEach(value -> send(value.getButton(), "", ""));
                refresh();
            }
        });
    }

    public void refresh() {
        Pouch.VALUES.stream().filter(pouch -> pouch.hasRequiredLevel(PLAYER) && !UNLOCKED.contains(pouch)).forEach(this::add);
    }

    private void add(final Pouch POUCH) {
        if (!UNLOCKED.contains(POUCH)) {
            UNLOCKED.add(POUCH);
            SENDER.sendFont(POUCH.getButton(), 2);
            send(POUCH.getButton(), String.format("@gre@%s", POUCH.getName()), String.format("Create '@red@%s@whi@' Pouch", POUCH.getName()));
        }
    }

    private void send(final int ID, final String A, final String B) {
        SENDER.sendString(ID, A);
        SENDER.sendTooltip(ID, B);
        SENDER.sendScrollbarLength(39010, (UNLOCKED.size() + 2) * 14);
    }

}
