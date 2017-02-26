package org.hyperion.rs2.model.content.skill;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.net.ActionSender;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Firemaking implements ContentTemplate {

    private static final int EXPMULTIPLIER = 5 * Constants.XPRATE;
    private Map<Position, Fire> fires = new HashMap<>();
    private Map<Integer, Log> logs = new HashMap<>();

    public void lightLogs(final Player player, final int logId) {
        if (player.isBusy()) {
            return;
        }
        if (player.getRandomEvent().skillAction(8)) {
            return;
        }
        if (!ContentEntity.isItemInBag(player, 590)) {
            ContentEntity.sendMessage(player, "You need a tinderbox to light a fire.");
            return;
        }
        if (fires.get(player.getPosition()) != null)
            return;
        final Log log = logs.get(logId);
        if (log == null)
            return;
        if (ContentEntity.getLevelForXP(player, 11) < log.level) {
            ContentEntity.sendMessage(player, "You need " + log.level + " firemaking to light this log.");
            return;
        }
        if (!player.getLocation().isFiremakingAllowed()) {
            player.sendMessage("You cannot light a fire in this area.");
            return;
        }
        ContentEntity.deleteItem(player, logId);
        int timerLower = ContentEntity.getLevelForXP(player, 11) * 100;
        ContentEntity.startAnimation(player, 733);
        int timer = log.timer + ContentEntity.random(log.timer) - timerLower;
        if (timer < 2500)
            timer = 2500;
        player.setBusy(true);
        World.submit(new org.hyperion.engine.task.Task(timer) {
            @Override
            public void execute() {
                if (!player.isBusy()) {
                    this.stop();
                    return;
                }
                ContentEntity.startAnimation(player, -1);
                player.getAchievementTracker().itemSkilled(Skills.FIREMAKING, logId, 1);
                fires.put(player.getPosition(), new Fire(player.getPosition(), ContentEntity.random((log.timer))));
                int obj = 2732;
                if (logId == 1513) {
                    int r = ContentEntity.random(2);
                    if (r == 0)
                        obj = 11404;
                    else if (r == 2)
                        obj = 11405;
                    else if (r == 1)
                        obj = 11406;
                }
                ContentEntity.addSkillXP(player, log.xp * EXPMULTIPLIER, 11);
                createObject(player.getPosition(), obj);
                ContentEntity.sendMessage(player, "You light a fire.");
                try {
                    player.vacateSquare();
                } catch (Exception e) {
                    ActionSender.yellModMessage("Error with firemaking, player: " + player.getName());
                    this.stop();
                    return;
                }
                if (!player.getPosition().inDuel())
                    player.setBusy(false);
                this.stop();
            }
        });
    }

    private void process() {
        ArrayList<Fire> outFires = new ArrayList<>();
        for (Map.Entry<Position, Fire> entry : fires.entrySet()) {
            Fire f = entry.getValue();
            if (f.timer == 0) {
                removeObject(f.position);
                sendAshes(f.position);
                outFires.add(f);
            } else
                f.timer--;
        }
        outFires.stream().forEach(value -> fires.remove(value.position));
        outFires.clear();
    }

    private void createObject(Position loc, int fire) {
        World.getPlayers().stream().forEach(player -> player.getActionSender().sendCreateObject(fire, 10, 0, loc));
    }

    private void removeObject(Position loc) {
        World.getPlayers().stream().filter(player -> player != null).forEach(player -> player.getActionSender().sendDestroyObject(10, 0, loc));
    }

    private void sendAshes(Position loc) {
        GlobalItem globalItem = new GlobalItem(null, loc, new Item(592, 1));
        GlobalItemManager.addToItems(globalItem);
        GlobalItemManager.createItem(globalItem);
        globalItem.itemHidden = false;
    }

    @Override
    public void init() throws FileNotFoundException {
        logs.put(1511, new Log(1511, 40, 1, 30));
        logs.put(1521, new Log(1521, 60, 15, 45));
        logs.put(1519, new Log(1519, 90, 30, 70));
        logs.put(1517, new Log(1517, 135, 45, 100));
        logs.put(1515, new Log(1515, 202, 60, 150));
        logs.put(1513, new Log(1513, 303, 70, 200));
        World.submit(new Task(1000, "firemaking") {
            @Override
            public void execute() {
                process();
            }
        });
    }

    @Override
    public int[] getValues(int type) {
        return type == 13 ? new int[]{590, 1511, 1521, 1519, 1517, 1515, 1513} : null;
    }

    @Override
    public boolean clickObject(final Player player, final int type, final int item1, final int slot1, final int item2, final int slot2) {
        if (type == 13 && (item1 == 590 || item2 == 590)) {
            lightLogs(player, item1 == 590 ? item2 : item1);
        }
        return false;
    }

    public static class Log {
        public int logId;
        public int timer;
        public int level;
        public int xp;

        public Log(int logId, int xp, int level, int timer) {
            this.logId = logId;
            this.timer = timer;
            this.level = level;
            this.xp = xp;
        }
    }

    public static class Fire {
        public Position position;
        public int timer;

        public Fire(Position loc, int time) {
            position = loc;
            timer = time;
        }
    }

}
