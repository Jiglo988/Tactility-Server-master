package org.hyperion.rs2.model.achievements;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;

public enum AchievementData {

    KILL_5("Kill 5 Players","Kill", Difficulty.VERY_EASY, 5, 250, 0,
            new String[] {"", "", "You must defeat 5 players in the wilderness", "in order to complete this achievement."}, new String[] {"", "", "250 pkp"}),
    FUN_PK("Teleport to FunPk", Difficulty.VERY_EASY, 1, 100, 0,
            new String[] {"", "", "Teleport to the Fun Pk zone and try out", "some safe pvp. This zone is great for 1v1s", "and fighting friends!"}, new String[] {"", "", "100 pkp"}),
    NEW_PRAYERS("New Prayers", Difficulty.VERY_EASY, 1, 100, 0,
            new String[] {"", "", "In Edgeville, change your prayer book", "so you have curses. This will give you an", "edge while you pk!"}, new String[] {"", "", "100 pkp"}),
    TOTAL_800("Total Level 800", "Total", Difficulty.VERY_EASY, 800, 200, 0,
            new String[] {"", "", "Achieve a total level of at least 800 to", "complete this achievement"}, new String[] {"", "", "200 pkp"}),
    KILL_25("Kill 25 Players", "Kill", Difficulty.EASY, 25, 500, 0,
            new String[] {"", "", "You must defeat 25 players in the wilderness", "in order to complete this achievement."}, new String[] {"", "", "500 pkp"}),
    DUNGEON_5("5 Dungeon Runs", "Dungeon", Difficulty.EASY, 5, 400, 0,
            new String[] {"", "", "You must complete a total of 5", "complete dungeoneering dungeons in order to.", "complete this achievement!"}, new String[] {"", "", "400 pkp"}),
    KILL_50("Kill 50 Players", "Kill", Difficulty.MEDIUM, 50, 1000, 0,
            new String[] {"", "", "You must defeat 50 players in the wilderness", "in order to complete this achievement."}, new String[] {"", "100x Rocktail", "1000 pkp"}, new Item[] {new Item(15272, 100)}),
    STREAK_6("Streak of 6", "Killstreak", Difficulty.MEDIUM, 6, 500, 50,
            new String[] {"", "", "You must kill 6 players in the wilderness", "without dying once. If you die before you", "reach 6 kills, your killstreak will reset!"}, new String[] {"", "50 donator points", "500 pkp"}),
    DUEL_20("Win 20 Duels", "Duel", Difficulty.MEDIUM, 20, 800, 0,
            new String[] {"", "", "You must win 20 duels", "in order to complete this achievement."}, new String[] {"", "", "800 pkp"}),
    DUNGEON_15("15 Dungeon Runs", "Dungeon", Difficulty.MEDIUM, 15, 700, 0,
            new String[] {"", "", "You must complete a total of 15", "complete dungeoneering dungeons in order to.", "complete this achievement!"}, new String[] {"", "", "700 pkp"}),
    TOTAL_1100("Total Level 1100", "Total", Difficulty.MEDIUM, 1100, 600, 50,
            new String[] {"", "", "Achieve a total level of at least 1100 to", "complete this achievement"}, new String[] {"", "50 donator points", "600 pkp"}),
    KILL_100("Kill 100 Players", "Kill", Difficulty.HARD, 100, 1750, 100,
            new String[] {"", "", "You must defeat 100 players in the wilderness", "in order to complete this achievement."}, new String[] {"", "100 donator points", "1750 pkp"}),
    DUEL_50("Win 50 Duels", "Duel", Difficulty.HARD, 50, 1000, 0,
            new String[] {"", "", "You must win 50 duels", "in order to complete this achievement."}, new String[] {"", "", "1000 pkp"}),
    TOTAL_1500("Total Level 1500", "Total", Difficulty.HARD, 1500, 1000, 50,
            new String[] {"", "", "Achieve a total level of at least 1500 to", "complete this achievement"}, new String[] {"", "50 donator points", "1000 pkp"}),
    KILL_250("Kill 250 Players", "Kill", Difficulty.VERY_HARD, 250, 2500, 150,
            new String[] {"", "", "You must defeat 250 players in the wilderness", "in order to complete this achievement."}, new String[] {"", "150 donator points", "2500 pkp"}),
    STREAK_10("Streak of 10", "Killstreak", Difficulty.VERY_HARD, 10, 1000, 50,
            new String[] {"", "", "You must kill 10 players in the wilderness", "without dying once. If you die before you", "reach 6 kills, your killstreak will reset!"}, new String[] {"", "50 donator points", "1000 pkp", "20 Overload Potions"}, new Item[] {new Item(15332, 20)}),
    DUNGEON_50("50 Dungeon Runs", "Dungeon", Difficulty.VERY_HARD, 50, 1200, 0,
            new String[] {"", "", "You must complete a total of 50", "complete dungeoneering dungeons in order to.", "complete this achievement!"}, new String[] {"", "", "1200 pkp"}),
    TOTAL_1800("Total Level 1800", "Total", Difficulty.VERY_HARD, 1800, 1250, 50,
            new String[] {"", "", "Achieve a total level of at least 1800 to", "complete this achievement"}, new String[] {"", "50 donator points", "1250 pkp"}),
    KILL_500("Kill 500 Players", "Kill", Difficulty.LEGENDARY, 500, 2000, 250,
            new String[] {"", "", "You must defeat 500 players in the wilderness", "in order to complete this achievement."}, new String[] {"", "200 donator points", "2000 pkp", "1x TokHaar-Kal"}, new Item[] {new Item(19111, 1)}),
    DUNGEON_200("200 Dungeon Runs", "Dungeon", Difficulty.LEGENDARY, 200, 3000, 100,
            new String[] {"", "", "You must complete a total of 200", "complete dungeoneering dungeons in order to.", "complete this achievement!"}, new String[] {"", "100 donator points", "3000 pkp"});

    private int steps, pkp, dp;
    private String name, type;
    private String[] instructions, rewards;
    private Difficulty difficulty;
    private Item[] items;

    AchievementData(String name, String type, Difficulty difficulty, int steps, int pkp, int dp, String[] instructions, String[] rewards, Item[] items) {
        this.name = name;
        this.type = type;
        this.difficulty = difficulty;
        this.steps = steps;
        this.pkp = pkp;
        this.dp = dp;
        this.instructions = instructions;
        this.rewards = rewards;
        this.items = items;
    }

    AchievementData(String name, String type, Difficulty difficulty, int steps, int pkp, int dp, String[] instructions, String[] rewards) {
        this.name = name;
        this.type = type;
        this.difficulty = difficulty;
        this.steps = steps;
        this.pkp = pkp;
        this.dp = dp;
        this.instructions = instructions;
        this.rewards = rewards;
        items = null;
    }

    AchievementData(String name, Difficulty difficulty, int steps, int pkp, int dp, String[] instructions, String[] rewards) {
        this.name = name;
        this.difficulty = difficulty;
        this.steps = steps;
        this.pkp = pkp;
        this.dp = dp;
        this.instructions = instructions;
        this.rewards = rewards;
        items = null;
        type = null;
    }

    public int getSteps() {
        return steps;
    }

    public int getPkp() {
        return pkp;
    }

    public int getDp() {
        return dp;
    }

    public String getName() { return name; }

    public String getType() {
        return type;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public String[] getRewards() {
        return rewards;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Item[] getItems() {
        return items;
    }

    public void giveReward(Player player) {
        player.getPoints().increasePkPoints(pkp);
        player.getActionSender().sendMessage("You have completed the achievement @blu@" + name);
        String message = "You have been rewarded " + pkp + " pkp";
        if(dp > 0) {
            player.getPoints().increaseDonatorPoints(dp);
            message += " and " + dp + " donator points!";
        }
        if(items != null) {
            for(Item item : items) {
                player.getBank().add(item);
            }
            player.getActionSender().sendMessage("Your rewards were added to your bank!");
        }
        player.getActionSender().sendMessage(message);
    }

}