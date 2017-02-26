package org.hyperion.engine.task.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.hyperion.engine.EngineTask;
import org.hyperion.engine.GameEngine;
import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.security.EncryptionStandard;
import org.hyperion.rs2.saving.IOData;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.util.TextUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author DrHales
 *         5/18/2016
 */
public class CheckInformationCommand extends NewCommand {

    public CheckInformationCommand() {
        super("checkinfo", Rank.DEVELOPER, new CommandInput<>(PlayerLoading::playerExists, "Player", "An Existing Player"));
    }

    @Override
    public boolean execute(final Player player, final String[] input) {
        final String value = input[0].trim();
        final Player target = World.getPlayerByName(value);
        final List<String> list = new ArrayList<>();
        GameEngine.submitIO(new EngineTask<Boolean>("Check Player Information Task", 4, TimeUnit.SECONDS) {
            @Override
            public Boolean call() throws Exception {
                Optional<JsonElement> element;
                element = PlayerLoading.getProperty(value, IOData.RANK);
                if (element.isPresent()) {
                    if (!Rank.hasAbility(player.getPlayerRank(), Rank.getPrimaryRank(element.get().getAsLong()))) {
                        player.sendf("You cannot check %s's information.", TextUtils.titleCase(value));
                        return true;
                    }
                }
                list.add("@dre@-----Information-----");
                element = getElement(value, IOData.PASSWORD);
                list.add(String.format("[Password]:%s", EncryptionStandard.decryptPassword(target != null ? target.getPassword() : element != null ? element.get().getAsString() : "No Data")));
                element = getElement(value, IOData.E_MAIL);
                list.add(String.format("[E-Mail]:%s", target != null ? target.getMail() : element != null ? element.get().getAsString() : "No E-Mail"));
                element = getElement(value, IOData.BANK_PIN);
                list.add(String.format("[Bank Pin]:%s", (target != null ? target.bankPin : element != null ? element.get().getAsString() : "No Bank Pin")));
                element = getElement(value, IOData.VERIFY_CODE);
                list.add(String.format("[Verification]:%s", (target != null ? target.verificationCode : element != null ? element.get().getAsString() : "No Verification")));
                element = getElement(value, IOData.RANK);
                list.add(String.format("[Rank]:%s", String.valueOf(target != null ? Rank.getPrimaryRank(target.getPlayerRank()) : element != null ? Rank.getPrimaryRank(element.get().getAsInt()) : "No Data")));
                element = getElement(value, IOData.LAST_IP);
                list.add(String.format("[Protocol]:%s", (target != null ? target.lastIp : element != null ? element.get().getAsString() : "No Data")));
                element = getElement(value, IOData.LAST_MAC);
                list.add(String.format("[Address]:%s", String.valueOf(target != null ? target.getLastMac() : element != null ? element.get().getAsInt() : "No Data")));
                element = getElement(value, IOData.FIRST_LOGIN);
                list.add(String.format("[Creation Date]:%s", String.valueOf(target != null ? new Date(target.getCreatedTime()) : element != null ? new Date(element.get().getAsLong()) : "No Data")));
                element = getElement(value, IOData.PREVIOUS_LOGIN);
                list.add(String.format("[Last Login]:%s", String.valueOf(target != null ? new Date(target.getPreviousSessionTime()) : element != null ? new Date(element.get().getAsLong()) : "No Data")));
                element = getElement(value, IOData.TODAY_VOTES);
                list.add(String.format("[Votes Today]:%s", String.valueOf(target != null ? target.getTodayVotes() : element != null ? element.get().getAsInt() : "No Votes Today")));
                list.add("@dre@-----Points----------");
                element = getElement(value, IOData.ACCOUNT_VALUE);
                list.add(String.format("[Account Value]:%s", String.valueOf(target != null ? target.getAccountValue().getTotalValue() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.PK_POINTS);
                list.add(String.format("[PK Points]:%s", String.valueOf(target != null ? target.getPoints().getPkPoints() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.DICED_VALUE);
                list.add(String.format("[Diced]:%s", String.valueOf(target != null ? target.getDiced() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.ELO);
                list.add(String.format("[ELO]:%s", String.valueOf(target != null ? target.getPoints().getEloRating() : element != null ? element.get().getAsInt() : "1200")));
                element = getElement(value, IOData.ELO_PEAK);
                list.add(String.format("[ELO Peak]:%s", String.valueOf(target != null ? target.getPoints().getEloPeak() : element != null ? element.get().getAsInt() : "1200")));
                element = getElement(value, IOData.DONATOR_POINTS);
                list.add(String.format("[Donator Points]:%s", String.valueOf(target != null ? target.getPoints().getDonatorPoints() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.DONATOR_POINTS_BOUGHT);
                list.add(String.format("[Points Bought]:%s", String.valueOf(target != null ? target.getPoints().getDonatorPointsBought() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.VOTING_POINTS);
                list.add(String.format("[Vote Points]:%s", String.valueOf(target != null ? target.getPoints().getVotingPoints() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.EMBLEM_POINTS);
                list.add(String.format("[Emblem Points]:%s", String.valueOf(target != null ? target.getBountyHunter().getEmblemPoints() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.HONOR_POINTS);
                list.add(String.format("[Honor Points]:%s", String.valueOf(target != null ? target.getPoints().getHonorPoints() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.SLAYER_POINTS);
                list.add(String.format("[Slayer Points]:%s", String.valueOf(target != null ? target.getSlayer().getSlayerPoints() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.KILL_COUNT);
                list.add(String.format("[Kill Count]:%s", String.valueOf(target != null ? target.getKillCount() : element != null ? element.get().getAsInt() : "0")));
                element = getElement(value, IOData.DEATH_COUNT);
                list.add(String.format("[Death Count]:%s", String.valueOf(target != null ? target.getDeathCount() : element != null ? element.get().getAsInt() : "0")));
                return true;
            }

            @Override
            public void stopTask() {
                player.sendMessage("Request timed out... Please try again at a later point.");
            }
        });
        if (list.isEmpty()) {
            player.sendf("Something went wrong loading %s's Player Data.", TextUtils.titleCase(value));
            return true;
        }
        player.getActionSender().displayInformation(value, list);
        return true;
    }

    private Optional<JsonElement> getElement(final String value, final IOData data) {
        return PlayerLoading.getProperty(value, data).isPresent() ? PlayerLoading.getProperty(value, data) : null;
    }

}
