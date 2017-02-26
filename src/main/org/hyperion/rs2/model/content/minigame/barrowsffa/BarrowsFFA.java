package org.hyperion.rs2.model.content.minigame.barrowsffa;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.content.specialareas.SpecialArea;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.util.Misc;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BarrowsFFA extends SpecialArea implements ContentTemplate{

    public static BarrowsFFA barrowsFFA;

    private static final int HEIGHT_LEVEL = 1602;
    public static final Position PORTAL_DEFAULT_POSITION = Position.create(3092, 3485, 0); //where the portal will spawn
    private static final Position GAME_DEFAULT_POSITION = Position.create(1867, 4941, HEIGHT_LEVEL); //default location for the game
    private static final Position LOBBY = Position.create(1862, 4939, 2); // default location to enter lobby
    private static final GameObjectDefinition PORTAL_ENTER_OBJECT = GameObjectDefinition.forId(6282); // portal to enter lobby definition

    private static final int EXIT_LOBBY_ID = 8883;
    public static final int DEATH_CHECK_ID = 40050;

    public static final int LOGOUT_CHECK_ID = 40001;

    public static final int DIALOGUE_ID = 4500; // dialogue ids for barrows jank

    private static final int INTERFACE_ID = 21119;

    private static final int[] INTERFACE_CHILD_IDS = new int[] { 21120, 21121, 21122, 21123};

    private final List<Player> lobby = new ArrayList<>(), game = new ArrayList<>();

    private int gameTime, nextGameTime;


    @Override
    public void init() throws FileNotFoundException {
        /*
        barrowsFFA = this;
        World.submit(new Event(1000) {
            @Override
            public void execute() throws IOException {
                process();
            }
        });
        */
    }


    //handles timers, interfaces & shit
    public void process() {

        if(gameTime > 0) {
            gameTime--;
            if(game.size() == 1) {
                endGame();
                return;
            }
            for(Player player : game) {
                sendInterfaceString(player, 0,"Players Left: "+game.size());
                sendInterfaceString(player, 1, "Time left: "+toMinutes(gameTime));
                sendInterfaceString(player, 2, "Kills: "+player.getBarrowsFFA().getKills());
                sendInterfaceString(player, 3, "Lives: "+player.getBarrowsFFA().getLives());
            }

            if(gameTime == 0)
                endGame();
        } else if(--nextGameTime <= 0) {
            if(lobby.size() < 2) {
                lobby.forEach(p -> p.sendMessage("You need at least 4 players to start a game"));
                nextGameTime = 30;
            } else
                startGame();
        }

        for(Player player : lobby) {
            sendInterfaceString(player, 0, "@cya@Players Waiting: @gre@"+lobby.size());
            sendInterfaceString(player, 1, "@cya@Estimated Time Left: @red@" + toMinutes(gameTime + nextGameTime));
            if(player.getBarrowsFFA().getBarrowSet() == null)
                System.out.println("Null set: "+player.getName());
            sendInterfaceString(player, 2, "@cya@Set: @or2@"+player.getBarrowsFFA().getBarrowSet().toString());
            sendInterfaceString(player, 3, "");
        }
    }

    public void startGame() {
        lobby.forEach(game::add);
        lobby.clear();
        for(final Player player : game) {
            enter(player);
            final Object set = player.getBarrowsFFA().getBarrowSet();
            if(set instanceof BarrowSet)
                ((BarrowSet)set).equip(player);

            player.getBarrowsFFA().initialize();
        }

        gameTime = 200 + game.size() * 10;
        nextGameTime = 50;
    }

    public void endGame() {
        //there is a winner
        if(game.size() == 1) {
            final Player winner = game.get(0);
            exit(winner);
            winner.getPoints().increaseMinigamePoints(2);
            gameTime = 0;
        } else {
            for(final Player player : game)
                exit(player);
        }

    }



    @Override
    public boolean canSpawn() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPkArea() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getPkLevel() {
        return 120;
    }

    @Override
    public boolean wildInterface() {
        return false;
    }

    @Override
    public Position getDefaultLocation() {
        return GAME_DEFAULT_POSITION.transform(Misc.random(10), Misc.random(10), 0);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean inArea(Player player) {
        final int x = player.getPosition().getX();
        final int y = player.getPosition().getY();
        final int z = player.getPosition().getZ();
        return inArea(x, y, z) && game.contains(player);  //borders, not implemented
    }

    @Override
    public boolean inArea(int x, int y, int z) {
        return z == HEIGHT_LEVEL && (x < 1905 && y < 4965 && y > 4940 && x > 1855);
    }

    @Override
    public String canEnter(Player player) {
        if(!game.contains(player))
            return "You are not allowed here";
        return "";
    }

    @Override
    public void enter(final Player player) {
        final String enter = canEnter(player);
        if(enter.length() > 2)
            player.sendMessage(enter);
        else Magic.teleport(player, getDefaultLocation(), false);

    }

    @Override
    public void exit(final Player player) {
        if (lobby.contains(player) || (game.contains(player))) {
            if(inArea(player)) {
                player.getEquipment().clear();
                player.getInventory().clear();
            }

            player.setTeleportTarget(PORTAL_DEFAULT_POSITION);
            player.getBarrowsFFA().destroy();

            player.getActionSender().showInterfaceWalkable(-1);
        }
    }


   // @Override  Override commented as it doesn't implement contentTemplate for safety reasons
    public int[] getValues(int type) {
        if(type == ClickType.OBJECT_CLICK1)
           return new int[]{PORTAL_ENTER_OBJECT.getId(), EXIT_LOBBY_ID, DEATH_CHECK_ID, LOGOUT_CHECK_ID};
        if(type == ClickType.DIALOGUE_MANAGER) {
            int[] ret = new int[9];
            for(int i = 0 ; i < ret.length; i++)
                ret[i] = DIALOGUE_ID + i;
            return ret;
        }
        return new int[0];
    }

    //used to enter lobby
    //@Override  Override commented as it doesn't implement contentTemplate for safety reasons
    public boolean objectClickOne(Player player, int id, int x, int y) {


        if(ContentEntity.getTotalAmountOfEquipmentItems(player) != 0 || ContentEntity.getTotalAmountOfItems(player) != 0) {
            player.sendMessage("Please bank all items to join!");
            return false;
        }

        if(id == DEATH_CHECK_ID) {



        }
        if(id == PORTAL_ENTER_OBJECT.getId()) {
            DialogueManager.openDialogue(player, DIALOGUE_ID); // open set selection
        }

        if(id == EXIT_LOBBY_ID) {
            player.face(Position.create(x, y, HEIGHT_LEVEL));
            player.playAnimation(Animation.create(7376));
            World.submit(new Task(600, "barrows ffa lobby") {
                public void execute() {
                    exit(player);
                    this.stop();
                }
            });
        }

        if(id == LOGOUT_CHECK_ID)
            exit(player);

        return false;
    }

    //used to pick your barrows set

    /**
     * Here's the logic, you have 6 barrows sets, but 5 dialogues, so you will have 3 on one interface and a "next" option. Then 3 more and a "back" option.
     */
    //@Override
    public boolean dialogueAction(Player player, int dialogueId) {
        final BarrowSet[] sets = BarrowSet.SETS;
        final int size = (sets.length)/2 + sets.length%2;
        final String[] strings;
        switch(dialogueId) {
            case DIALOGUE_ID:
                strings = new String[size + 1];
                for(int i = 0; i < strings.length; i++)
                    strings[i] = sets[i].toString();
                strings[strings.length - 1] = "Next";
                player.getActionSender().sendDialogue("Select a set", ActionSender.DialogueType.OPTION, - 1, Animation.FacialAnimation.DEFAULT,
                        strings);
                for(int i = 0; i < strings.length; i++)
                    player.getInterfaceState().setNextDialogueId(i, DIALOGUE_ID + i + 1);
                return true;
            case (DIALOGUE_ID + 4):
                strings = new String[size + 1 - sets.length%2];
                for(int i = size; i < sets.length; i++)
                    strings[i - size] = sets[i].toString();
                strings[strings.length - 1] = "Back";
                player.getActionSender().sendDialogue("Select a set", ActionSender.DialogueType.OPTION, - 1, Animation.FacialAnimation.DEFAULT,
                        strings);
                for(int i = 0; i < strings.length; i++)
                    player.getInterfaceState().setNextDialogueId(i, DIALOGUE_ID + 5 + i);
                return true;
            case (DIALOGUE_ID + 8):
                DialogueManager.openDialogue(player, DIALOGUE_ID);
                return true;
            default:


                if(ContentEntity.getTotalAmountOfEquipmentItems(player) != 0 || ContentEntity.getTotalAmountOfItems(player) != 0) {
                    player.sendMessage("Please bank all items to join!");
                    return false;
                }

                final BarrowSet set = BarrowSet.forDialogue(dialogueId);
                player.getBarrowsFFA().setBarrowsSet(set); //to select their barrows set

                lobby.add(player);
                player.setTeleportTarget(LOBBY); // teleport to default lobby location and add them to lobby - after they pick their barrows set
                player.getActionSender().removeChatboxInterface();
                return true;
        }
    }

    public void sendInterfaceString(Player player, int i, String s) {
        player.getActionSender().showInterfaceWalkable(INTERFACE_ID);
        player.getActionSender().sendString(INTERFACE_CHILD_IDS[i], s);

    }

    public static String toMinutes(int i) {
        return String.format("%d:%s%d", i/60, i%60 >= 10 ? "" : "0", i%60);
    }

    public static void spawnObject(final List manager) {
        manager.add(
                new GameObject(PORTAL_ENTER_OBJECT, PORTAL_DEFAULT_POSITION.transform(0, -1, 0), 10, /*rotation*/ 0, false)); //make a portal 1 space away from people will teleport
    }


    public boolean deathCheck(final Player player, final Player killer) {

        System.out.println("Killer: "+killer.getName());
        boolean one = game.contains(killer);
        boolean two = game.contains(player);

        System.out.println(one + " | "+two);
        if(one && two) {

            boolean rampage = killer.getBarrowsFFA().kill(player);
            boolean die = player.getBarrowsFFA().die(killer);

            if(die)
                exit(player);
            if(rampage) {
                game.forEach(p -> p.sendf("%s is on a rampage of %d kills", killer.getName(), killer.getBarrowsFFA().getKillStreak()));
            }
            return true;
        }
        return false;

    }

}
