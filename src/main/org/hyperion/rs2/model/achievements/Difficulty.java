package org.hyperion.rs2.model.achievements;

public enum Difficulty {

    VERY_EASY(4, "Very Easy"), EASY(2, "Easy"), MEDIUM(5, "Medium"), HARD(3, "Hard"), VERY_HARD(4, "Very Hard"), LEGENDARY(2, "Legendary");

    private int numberOfAchievements;
    private String name;

    Difficulty(int numberOfAchievements, String name) {
        this.numberOfAchievements = numberOfAchievements;
        this.name = name;
    }

    public int getNumberOfAchievements() {
        return numberOfAchievements;
    }

    public String getName() { return name; }

}
