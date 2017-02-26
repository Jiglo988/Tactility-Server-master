package org.hyperion.rs2.model.joshyachievementsv2.reward.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.joshyachievementsv2.reward.Reward;

public class PointsReward implements Reward{

    public enum Type{
        PK("PK Points"){
            void apply(final Player player, final int amount){
                player.getPoints().setPkPoints(player.getPoints().getPkPoints() + amount);
            }
        },
        VOTE("Vote Points"){
            void apply(final Player player, final int amount){
                player.getPoints().setVotingPoints(player.getPoints().getVotingPoints() + amount);
            }
        },
        DONOR("Donator Points"){
            void apply(final Player player, final int amount){
                player.getPoints().setDonatorPoints(player.getPoints().getDonatorPoints() + amount);
            }
        },
        HONOR("Honor Points"){
            void apply(final Player player, final int amount){
                player.getPoints().setHonorPoints(player.getPoints().getHonorPoints() + amount);
            }
        },
        SLAYER("Slayer Points"){
            void apply(final Player player, final int amount){
                player.getSlayer().setPoints(player.getSlayer().getSlayerPoints() + amount);
            }
        },
        EMBLEM("Emblem Points"){
            void apply(final Player player, final int amount){
                player.getBountyHunter().setEmblemPoints(player.getBountyHunter().getEmblemPoints() + amount);
            }
        },
        BH("Bounty Hunter"){
            void apply(final Player player, final int amount){
                player.getBountyHunter().setKills(player.getBountyHunter().getKills() + amount);
            }
        },
        ELO("Elo"){
            void apply(final Player player, final int amount){
                player.getPoints().setEloRating(player.getPoints().getEloRating() + amount);
            }
        },
        DUNG("Dungeoneering Tokens"){
            void apply(final Player player, final int amount){
                player.getDungeoneering().setTokens(player.getDungeoneering().getTokens() + amount);
            }
        };

        public final String name;

        Type(final String name){
            this.name = name;
        }

        abstract void apply(final Player player, final int amount);
    }

    public final Type type;
    public final int amount;

    public PointsReward(final Type type, final int amount){
        this.type = type;
        this.amount = amount;
    }

    public void reward(final Player player){
        type.apply(player, amount);
        player.sendf("You have been given %,d %s!", amount, type.name);
    }
}
