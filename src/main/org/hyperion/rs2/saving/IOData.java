package org.hyperion.rs2.saving;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.EloRating;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.pvptasks.PvPTask;
import org.hyperion.rs2.model.content.skill.slayer.SlayerTask;
import org.hyperion.rs2.model.possiblehacks.DataType;
import org.hyperion.rs2.model.possiblehacks.PossibleHacksHolder;
import org.hyperion.rs2.model.sets.CustomSet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Gilles on 4/02/2016.
 */
public enum IOData {
    USERNAME {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getName());
        }
    },
    PASSWORD {
        @Override
        protected boolean priorityLoading() {
            return true;
        }

        @Override
        public boolean shouldSave(Player player) {
            return player.getPassword() != null;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPassword());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setPassword(element.getAsString());
        }
    },
    RANK {
        @Override
        protected boolean priorityLoading() {
            return true;
        }

        @Override
        public boolean shouldSave(Player player) {
            return player.getPlayerRank() != 1;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPlayerRank());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setPlayerRank(element.getAsLong());
        }
    },
    LOCKS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getLocks() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getLocks());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setLocks(element.getAsLong());
        }
    },
    GAMEMODE {
        @Override
        public boolean shouldSave(Player player) {
            return player.getGameMode() != 1;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getGameMode());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setGameMode(element.getAsInt());
        }
    },
    SAVED_IPS {
        @Override
        protected boolean priorityLoading() {
            return true;
        }
        @Override
        public boolean shouldSave(Player player) {
            return !player.getSavedIps().isEmpty();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getSavedIps(), new TypeToken<Map<String, Long>>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setSavedIps(builder.fromJson(element, new TypeToken<Map<String, Long>>(){}.getType()));
        }
    },
    LOCATION {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getPosition(), new TypeToken<Position>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setPosition(builder.fromJson(element, new TypeToken<Position>(){}.getType()));
        }
    },
    VERIFY_CODE {
        @Override
        public boolean shouldSave(Player player) {
            return player.verificationCode != null && !player.verificationCode.equals("");
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.verificationCode);
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.verificationCode = element.getAsString();
            player.verificationCodeEntered = !player.verificationCode.isEmpty();
        }
    },
    BANK_PIN {
        @Override
        public boolean shouldSave(Player player) {
            return player.bankPin != null && !player.bankPin.equals("") && !player.bankPin.equals("null");
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.bankPin);
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.bankPin = element.getAsString();
        }
    },
    PID {
        @Override
        public boolean shouldSave(Player player) {
            return player.isPidSet();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPid());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setPid(element.getAsInt());
        }
    },
    GOOGLE_AUTHENTICATOR_KEY {
        @Override
        protected boolean priorityLoading() {
            return true;
        }

        @Override
        public boolean shouldSave(Player player) {
            return player.getGoogleAuthenticatorKey() != null;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getGoogleAuthenticatorKey());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setGoogleAuthenticatorKey(element.getAsString());
        }
    },
    GOOGLE_AUTHENTICATOR_BACKUP_CODES {
        @Override
        protected boolean priorityLoading() {
            return true;
        }

        @Override
        public boolean shouldSave(Player player) {
            return player.getGoogleAuthenticatorBackup() != null;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getGoogleAuthenticatorBackup(), new TypeToken<List<String>>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setGoogleAuthenticatorBackup(builder.fromJson(element, new TypeToken<List<String>>(){}.getType()));
        }
    },
    LAST_IP {
        @Override
        protected boolean priorityLoading() {
            return true;
        }

        @Override
        public boolean shouldSave(Player player) {
            return player.getFullIP() != null;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getFullIP());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.lastIp = element.getAsString().replace("/", "");
            if (!player.getFullIP().replace("/", "").split(":")[0].equals(player.lastIp.split(":")[0])) {
                PossibleHacksHolder.getInstance().add(player, player.getFullIP(), DataType.PROTOCOL);
            }
        }
    },
    LAST_MAC {
        @Override
        protected boolean priorityLoading() {
            return true;
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setLastMac(element.getAsInt());
            if (player.getUID() != player.getLastMac()) {
                PossibleHacksHolder.getInstance().add(player, String.valueOf(player.getUID()), DataType.ADDRESS);
            }
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getUID());
        }
    },
    ACCOUNT_VALUE {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getAccountValue().getTotalValueWithoutPointsAndGE());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setStartValue(element.getAsInt());
        }
    },
    DICED_VALUE {
        @Override
        public boolean shouldSave(Player player) {
            return player.getDiced() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getDiced());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setDiced(element.getAsInt());
        }
    },
    PREVIOUS_LOGIN {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(System.currentTimeMillis());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setPreviousSessionTime(element.getAsLong());
        }
    },
    FIRST_LOGIN {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getCreatedTime());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setCreatedTime(element.getAsLong());
        }
    },
    LAST_HONOR {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getLastHonorPointsReward());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setLastHonorPointsReward(element.getAsLong());
        }
    },
    LAST_VOTE_STREAK_INCREASE {
        @Override
        public boolean shouldSave(Player player) {
            return player.getLastVoteStreakIncrease() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getLastVoteStreakIncrease());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setLastVoteStreakIncrease(element.getAsLong());
        }
    },

    VOTING_STREAK {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getVoteStreak());
        }

        @Override
        public boolean shouldSave(Player player) {
            return player.getVoteStreak() != 0;
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setVoteStreak(element.getAsInt());
        }
    },
    TODAY_VOTES {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getTodayVotes());
        }

        @Override
        public boolean shouldSave(Player player) {
            return player.getTodayVotes() != 0;
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setTodayVotes(element.getAsInt());
        }
    },
    LAST_VOTE_BONUS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getLastVoteBonus() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getLastVoteBonus());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setLastVoteBonus(element.getAsLong());
        }
    },
    VOTE_BONUS_END {
        @Override
        public boolean shouldSave(Player player) {
            return player.getVoteBonusEndTime() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getVoteBonusEndTime() - System.currentTimeMillis());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setVoteBonusEndTime(element.getAsLong(), true);
        }
    },
    ELO {
        @Override
        public boolean shouldSave(Player player) {
            return player.getPoints().getEloRating() != EloRating.DEFAULT_ELO_START_RATING;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPoints().getEloRating());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPoints().setEloRating(element.getAsInt());
        }
    },
    ELO_PEAK {
        @Override
        public boolean shouldSave(Player player) {
            return player.getPoints().getEloPeak() != EloRating.DEFAULT_ELO_START_RATING;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPoints().getEloPeak());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPoints().setEloPeak(element.getAsInt());
        }
    },
    SPECIAL_ATTACK {
        @Override
        public boolean shouldSave(Player player) {
            return player.getSpecBar().getAmount() != SpecialBar.FULL;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getSpecBar().getAmount());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getSpecBar().setAmount(element.getAsInt());
        }
    },
    ATTACK_TYPE {
        @Override
        public boolean shouldSave(Player player) {
            return player.cE.getAtkType() != 2;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.cE.getAtkType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.cE.setAtkType(element.getAsInt());
        }
    },
    MAGIC_SPELLBOOK {
        @Override
        public boolean shouldSave(Player player) {
            return player.getSpellBook().toInteger() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getSpellBook().toInteger());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getSpellBook().changeSpellBook(element.getAsInt());
        }
    },
    DEFAULT_ALTAR {
        @Override
        public boolean shouldSave(Player player) {
            return !player.getPrayers().isDefaultPrayerbook();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPrayers().isDefaultPrayerbook());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPrayers().setPrayerbook(element.getAsBoolean());
        }
    },
    CLAN_NAME {
        @Override
        public boolean shouldSave(Player player) {
            return !player.getClanName().equals("");
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getClanName());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            String clanName = element.getAsString();
            if(clanName == null || clanName.length() <= 0)
                return;
            ClanManager.joinClanChat(player, clanName, true);
        }
    },
    YELL_TAG {
        @Override
        public boolean shouldSave(Player player) {
            return !player.getYelling().getTag().equals("");
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getYelling().getTag());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getYelling().setYellTitle(element.getAsString());
        }
    },
    DONATOR_POINTS_BOUGHT {
        @Override
        public boolean shouldSave(Player player) {
            return player.getPoints().getDonatorPointsBought() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPoints().getDonatorPointsBought());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPoints().setDonatorsBought(element.getAsInt());
            if(element.getAsInt() >= 2000)
                Rank.addAbility(player, Rank.DONATOR);
            if(element.getAsInt() >= 10000)
                Rank.addAbility(player, Rank.SUPER_DONATOR);
        }
    },
    DONATOR_POINTS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getPoints().getDonatorPoints() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPoints().getDonatorPoints());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPoints().setDonatorPoints(element.getAsInt());
        }
    },
    PK_POINTS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getPoints().getPkPoints() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPoints().getPkPoints());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPoints().setPkPoints(element.getAsInt());
        }
    },
    VOTING_POINTS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getPoints().getVotingPoints() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPoints().getVotingPoints());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPoints().setVotingPoints(element.getAsInt());
        }
    },
    EMBLEM_POINTS {
        @Override
        public boolean shouldSave(Player player) { return player.getBountyHunter().getEmblemPoints() != 0; }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getBountyHunter().getEmblemPoints());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getBountyHunter().setEmblemPoints(element.getAsInt());
        }
    },
    HONOR_POINTS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getPoints().getHonorPoints() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPoints().getHonorPoints());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPoints().setHonorPoints(element.getAsInt());
        }
    },
    SLAYER_POINTS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getSlayer() != null && player.getSlayer().getSlayerPoints() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getSlayer().getSlayerPoints());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getSlayer().setPoints(element.getAsInt());
        }
    },
    SLAYER_TASK_STREAK {
        @Override
        public boolean shouldSave(Player player) {
            return player.getSlayer() != null && player.getSlayer().getTotalTasks() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getSlayer().getTotalTasks());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getSlayer().setTotalTasks(element.getAsInt());
        }
    },
    SKULL_TIMER {
        @Override
        public boolean shouldSave(Player player) {
            return player.getSkullTimer() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getSkullTimer());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setSkullTimer(element.getAsInt());
        }
    },
    EARN_POTENTIAL {
        @Override
        public boolean shouldSave(Player player) {
            return player.EP != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.EP);
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.EP = element.getAsInt();
        }
    },
    GODWARS_KILL_COUNT {
        @Override
        public boolean shouldSave(Player player) {
            return Arrays.stream(player.godWarsKillCount).average().getAsDouble() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.godWarsKillCount, new TypeToken<Integer[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.godWarsKillCount = builder.fromJson(element.getAsJsonArray(), int[].class);
        }
    },
    SLAYER_TASK {
        @Override
        public boolean shouldSave(Player player) {
            return player.getSlayer() != null && player.getSlayer().getTask() != null && player.getSlayer().getTaskAmount() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            JsonObject object = new JsonObject();
            object.addProperty("taskId", player.getSlayer().getTask().name());
            object.addProperty("taskAmount", player.getSlayer().getTaskAmount());
            return object;
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            JsonObject object = element.getAsJsonObject();
            if(object.has("taskId"))
                player.getSlayer().setTask(SlayerTask.valueOf(object.get("taskId").getAsString()));
            if(object.has("taskAmount"))
                player.getSlayer().setTaskAmount(object.get("taskAmount").getAsInt());
        }
    },
    KILL_STREAK {
        @Override
        public boolean shouldSave(Player player) {
            return player.getKillStreak() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getKillStreak());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setKillStreak(element.getAsInt());
        }
    },
    KILL_COUNT {
        @Override
        public boolean shouldSave(Player player) {
            return player.getKillCount() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getKillCount());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setKillCount(element.getAsInt());
        }
    },
    DEATH_COUNT {
        @Override
        public boolean shouldSave(Player player) {
            return player.getDeathCount() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getDeathCount());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setDeathCount(element.getAsInt());
        }
    },
    TAB_AMOUNT {
        @Override
        public boolean shouldSave(Player player) {
            return player.getBankField().getTabAmount() > 2;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getBankField().getTabAmount());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getBankField().setTabAmount(element.getAsInt());
        }
    },
    CLEANED {
        @Override
        public boolean shouldSave(Player player) {
            return player.cleaned;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.cleaned);
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.cleaned = element.getAsBoolean();
        }
    },
    FIGHT_CAVE_WAVE {
        @Override
        public boolean shouldSave(Player player) {
            return player.fightCavesWave != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.fightCavesWave);
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.fightCavesWave = element.getAsInt();
        }
    },
    MAX_CAPE {
        @Override
        public boolean shouldSave(Player player) {
            return player.hasMaxCape();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.hasMaxCape());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setMaxCape(element.getAsBoolean());
        }
    },
    COMP_CAPE {
        @Override
        public boolean shouldSave(Player player) {
            return player.hasCompCape();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.hasCompCape());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setCompCape(element.getAsBoolean());
        }
    },
    PVP_TASK {
        @Override
        public boolean shouldSave(Player player) {
            return PvPTask.toInteger(player.getPvPTask()) != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(PvPTask.toInteger(player.getPvPTask()));
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setPvPTask(PvPTask.toTask(element.getAsInt()));
        }
    },
    PVP_TASK_AMOUNT {
        @Override
        public boolean shouldSave(Player player) {
            return player.getPvPTaskAmount() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPvPTaskAmount());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.setPvPTaskAmount(element.getAsInt());
        }
    },
    E_MAIL {
        @Override
        public boolean shouldSave(Player player) {
            return !player.getMail().toString().equals("");
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getMail().toString());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getMail().setMail(element.getAsString());
        }
    },
    PVP_ARMOUR {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPvPStorage().toString());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) {
            player.getPvPStorage().editFromString(element.getAsString());
        }
    },
    BH_KILLS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getBountyHunter().getKills() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getBountyHunter().getKills());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getBountyHunter().setKills(element.getAsInt());
        }
    },
    BH_PERKS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getBHPerks().perkLevel() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getBHPerks().perkLevel());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getBHPerks().setPerk(element.getAsInt());
        }
    },
    NPC_KILLS {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getNPCLogs().toString());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getNPCLogs().edit(element.getAsString());
        }
    },
    BONUS_EXPERIENCE {
        @Override
        public boolean shouldSave(Player player) {
            return player.getSkills().getBonusXP().isPresent();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getSkills().getBonusXP().get().toString());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getSkills().setBonusXP(Skills.CurrentBonusXP.load(element.getAsString()));
        }
    },
    DUNGEONEERING {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getDungeoneering().save());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getDungeoneering().load(element.getAsString());
        }
    },
    RUNE_POUCH {
        @Override
        public boolean shouldSave(Player player) {
            return player.getRunePouch().capacity() != player.getRunePouch().freeSlots();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getRunePouch().getItems(), new TypeToken<Item[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getRunePouch().setItems(builder.fromJson(element, new TypeToken<Item[]>(){}.getType()));
        }
    },
    TUTORIAL_PROGRESS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getTutorialProgress() != 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getTutorialProgress());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setTutorialProgress(element.getAsInt());
        }
    },
    MAX_CAPE_COLOURS {
        @Override
        public boolean shouldSave(Player player) {
            return player.maxCapePrimaryColor > 0 || player.maxCapeSecondaryColor > 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            JsonObject object = new JsonObject();
            object.addProperty("primary-color", player.maxCapePrimaryColor);
            object.addProperty("secondary-color", player.maxCapeSecondaryColor);
            return object;
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            JsonObject object = element.getAsJsonObject();
            if(object.has("primary-color"))
                player.maxCapePrimaryColor = object.get("primary-color").getAsInt();
            if(object.has("secondary-color"))
                player.maxCapeSecondaryColor = object.get("secondary-color").getAsInt();
        }
    },
    COMP_CAPE_COLOURS {
        @Override
        public boolean shouldSave(Player player) {
            return player.compCapePrimaryColor > 0 || player.compCapeSecondaryColor > 0;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            JsonObject object = new JsonObject();
            object.addProperty("primary-color", player.compCapePrimaryColor);
            object.addProperty("secondary-color", player.compCapeSecondaryColor);
            return object;
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            JsonObject object = element.getAsJsonObject();
            if(object.has("primary-color"))
                player.compCapePrimaryColor = object.get("primary-color").getAsInt();
            if(object.has("secondary-color"))
                player.compCapeSecondaryColor = object.get("secondary-color").getAsInt();
        }
    },
    PERMANENT_EXTRA_DATA {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return new JsonPrimitive(player.getPermExtraData().getSaveableString());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getPermExtraData().parse(element.getAsString());
        }
    },
    APPEARANCE {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getAppearance().getLook(), new TypeToken<Integer[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getAppearance().setLook(builder.fromJson(element.getAsJsonArray(), int[].class));
        }
    },
    LEVELS {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getSkills().getLevels(), new TypeToken<Integer[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getSkills().setLevels(builder.fromJson(element.getAsJsonArray(), int[].class));
        }
    },
    EXPERIENCE {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getSkills().getExps(), new TypeToken<int[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getSkills().setExps(builder.fromJson(element.getAsJsonArray(), int[].class));
        }
    },
    INVENTORY {
        @Override
        public boolean shouldSave(Player player) {
            return player.getInventory().freeSlots() != player.getInventory().capacity();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getInventory().getItems(), new TypeToken<Item[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getInventory().setItems(builder.fromJson(element, new TypeToken<Item[]>(){}.getType()));
        }
    },
    EQUIPMENT {
        @Override
        public boolean shouldSave(Player player) {
            return player.getEquipment().freeSlots() != player.getEquipment().capacity();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getEquipment().getItems(), new TypeToken<Item[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getEquipment().setItems(builder.fromJson(element, new TypeToken<Item[]>(){}.getType()));
        }
    },
    SAVE_SETS {
        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getCustomSetHolder().getCustomSets(), new TypeToken<CustomSet[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getCustomSetHolder().setCustomSets(builder.fromJson(element.getAsJsonArray(), CustomSet[].class));
        }
    },
    BANK {
        @Override
        public boolean shouldSave(Player player) {
            return player.getBank().freeSlots() != player.getBank().capacity();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(Arrays.stream(player.getBank().getItems()).filter(item -> item != null).toArray(), new TypeToken<BankItem[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getBank().setFiringEvents(false);
            Arrays.stream((BankItem[])builder.fromJson(element, new TypeToken<BankItem[]>(){}.getType())).filter(bankItem -> bankItem != null).forEach(bankItem -> player.getBank().add(new BankItem(bankItem.getTabIndex(), bankItem.getId(), bankItem.getCount())));
            player.getBank().setFiringEvents(true);
        }
    },
    FRIENDS {
        @Override
        public boolean shouldSave(Player player) {
            return player.getFriends() != null;
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getFriends().toArray(), new TypeToken<Long[]>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.getFriends().setFriends(builder.fromJson(element.getAsJsonArray(), long[].class));
        }
    },
    IGNORES {
        @Override
        public boolean shouldSave(Player player) {
            return !player.getIgnores().isEmpty();
        }

        @Override
        public JsonElement saveValue(Player player, Gson builder) {
            return builder.toJsonTree(player.getIgnores(), new TypeToken<List<Long>>(){}.getType());
        }

        @Override
        public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {
            player.setIgnores(builder.fromJson(element, new TypeToken<List<Long>>(){}.getType()));
        }
    };

    public final static IOData[] VALUES = values();
    private final static String CHAR_FILE_PATH = "./data/characters";

    private final static Map<String, IOData> priorityLoading = Stream.of(VALUES).filter(IOData::priorityLoading).collect(Collectors.toMap(IOData::toString, Function.identity()));
    private final static Map<String, IOData> nonPriorityLoading = Stream.of(VALUES).filter(ioData -> !ioData.priorityLoading()).collect(Collectors.toMap(IOData::toString, Function.identity()));

    public static Map<String, IOData> getPriorityLoading() {
        return priorityLoading;
    }

    public static Map<String, IOData> getNonPriorityLoading() {
        return nonPriorityLoading;
    }

    public static String getCharFilePath() {
        return CHAR_FILE_PATH;
    }

    public boolean shouldSave(Player player) {
        return true;
    }
    public abstract JsonElement saveValue(Player player, Gson builder);
    protected boolean priorityLoading() {
        return false;
    }
    public void loadValue(Player player, JsonElement element, Gson builder) throws Exception {}

    @Override
    public String toString() {
        return name().toLowerCase().replaceAll("_", "-");
    }
}
