package org.hyperion.rs2.model.achievements;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

import java.util.ArrayList;

public class AchievementHandler {

    public static boolean achievementButton(Player player, int buttonId) {
        if(true)
            return false;
        for (int i = 29457; i <= 29462; i++) {
            if(buttonId == i) {
                Difficulty newDifficulty = Difficulty.values()[i - 29457];
                AchievementHandler.openInterface(player, newDifficulty, true);
                player.setViewingDifficulty(newDifficulty);
                return true;
            }
        }
        if(buttonId > 28885 && buttonId < 28950) {
            AchievementData achievementData = getAchievementByDifficulty(player.getViewingDifficulty()).get(buttonId - 28886);
            player.getActionSender().sendString(28883, achievementData.getName() + " Guide");
            loadInformation(player, achievementData);
            if (achievementData.getSteps() > 1)
                player.getActionSender().sendString(28989, "Current Progress: " + player.getAchievementsProgress().get(achievementData) + "/" + achievementData.getSteps());
            return true;
        }
        return false;
    }

    private static ArrayList<AchievementData> getAchievementByDifficulty(Difficulty difficulty) {
        ArrayList<AchievementData> achievements = new ArrayList<>();
        for(AchievementData achievementData : AchievementData.values()) {
            if(achievementData.getDifficulty() == difficulty)
                achievements.add(achievementData);
        }
        return achievements;
    }

    public static void openInterface(Player player, Difficulty difficulty, boolean refresh) {
        if(true)
            return;
        clearInterface(player);
        ArrayList<AchievementData> achievements = getAchievementByDifficulty(difficulty);
        int interfaceId = 28886;
        for(AchievementData achievementData : achievements) {
            player.getActionSender().sendString(interfaceId, getTextColor(player.getAchievementsProgress().get(achievementData), achievementData.getSteps()) + achievementData.getName());
            interfaceId++;
        }
        player.getActionSender().sendString(28881, difficulty.getName() + " Achievement Diary");
        if(!refresh)
            player.getActionSender().showInterface(28880);
    }

    private static void clearInterface(Player player) {
        for(int i = 28989; i < 29014; i++) {
            player.getActionSender().sendString(i, "");
        }
        for(int i = 29402; i < 29409; i++) {
            player.getActionSender().sendString(i, "");
        }
        for(int i = 28886; i <= 28886 + player.getAchievementsProgress().size(); i++) {
            player.getActionSender().sendString(i, "");
        }
        player.getActionSender().sendString(28883, "Achievement Guide");
    }

    private static String getTextColor(int currentStep, int steps) {
        if(currentStep == 0)
            return "@red@";
        else if(currentStep > 0 && currentStep < steps)
            return "@yel@";
        else
            return "@gre@";
    }

    /**
     * Progresses every achievement of this type.
     * @param player Player player
     * @param type Type type of achievement.
     */
    public static void progressAchievement(Player player, String type) {
        if(true)
            return;
        for(int i = 0; i < AchievementData.values().length; i++) {
            AchievementData achievementData = AchievementData.values()[i];
            if(achievementData.getType() == null) {
                if(achievementData.getName().equals(type)) {
                    progress(achievementData, player);
                    return;
                }
            } else if(achievementData.getType().equals(type)) {
                progress(achievementData, player);
            }
        }
    }

    private static void loadInformation(Player player, AchievementData achievementData) {
        for(int i = 28989; i < achievementData.getInstructions().length + 28989; i++) {
            player.getActionSender().sendString(i, achievementData.getInstructions()[i - 28989]);
        }
        for(int i = 29402; i < achievementData.getRewards().length + 29402; i++) {
            player.getActionSender().sendString(i, achievementData.getRewards()[i - 29402]);
        }
    }

    private static void progress(AchievementData achievementData, Player player) {
        if(player.getAchievementsProgress().get(achievementData) == achievementData.getSteps())
            return;
        switch(achievementData.getType()) {
            case "Total":
                player.getAchievementsProgress().put(achievementData, player.getSkills().getTotalLevel() + player.getSkills().getLevel(Skills.CONSTRUCTION));
                break;
            case "Killstreak":
                player.getAchievementsProgress().put(achievementData, player.getKillStreak());
                break;
            default:
                player.getAchievementsProgress().put(achievementData, player.getAchievementsProgress().get(achievementData) + 1);
        }
        if(player.getAchievementsProgress().get(achievementData) >= achievementData.getSteps())
            achievementData.giveReward(player);
    }

}
