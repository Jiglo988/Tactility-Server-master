package org.hyperion.rs2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.PlayerDetails;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.authentication.PlayerAuthenticatorVerification;
import org.hyperion.rs2.model.content.authentication.PlayerAuthenticatorVerification.VerifyResponse;
import org.hyperion.rs2.model.punishment.Punishment;
import org.hyperion.rs2.model.punishment.manager.PunishmentManager;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.saving.PlayerLoading;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.util.ObservableCollection;
import org.hyperion.util.Time;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import static org.hyperion.rs2.LoginResponse.*;
import static org.hyperion.rs2.model.content.authentication.PlayerAuthenticatorVerification.VerifyResponse.*;

/**
 * Created by Gilles on 6/02/2016.
 */
public final class GenericWorldLoader implements WorldLoader {

	private final static String ALLOWED_IPS_DIR = "./data/json/allowed_ips.json";
	private final static ObservableCollection<String> ALLOWED_IPS = loadList(ALLOWED_IPS_DIR);
	private final static Map<String, Integer> LOGIN_ATTEMPTS = new HashMap<>();
	private final static Set<String> BLOCKED_PLAYERS = new HashSet<>();

    private final static int MAXIMUM_LOGIN_ATTEMPTS = 5;

	public static ObservableCollection<String> getAllowedIps() {
		return ALLOWED_IPS;
	}

	public static boolean isIpAllowed(String ip) {
		return getAllowedIps().contains(ip);
	}

	static {
		ALLOWED_IPS.addListener(new AbstractNotifyListener() {
			@Override
			public void invalidated(javafx.beans.Observable observable) {
				saveList(ALLOWED_IPS, ALLOWED_IPS_DIR);
			}
		});
	}

	@Override
	public LoginResponse checkLogin(Player player, PlayerDetails playerDetails) {
		if(PlayerSaving.isSaving(player))
			return WAIT_AND_TRY_AGAIN;

		if(LOGIN_ATTEMPTS.get(player.getName()) == null)
			LOGIN_ATTEMPTS.put(player.getName(), 0);

		if(BLOCKED_PLAYERS.contains(player.getName())) {
			return LOGIN_ATTEMPTS_EXCEEDED;
		}

		if(LOGIN_ATTEMPTS.get(player.getName()) >= MAXIMUM_LOGIN_ATTEMPTS) {
			BLOCKED_PLAYERS.add(player.getName());
			World.submit(new Task(Time.ONE_MINUTE, "Login attempt reset for " + player.getName()) {
				String playerName = player.getName();

				@Override
				public void execute() {
					BLOCKED_PLAYERS.remove(playerName);
					LOGIN_ATTEMPTS.remove(playerName);
					stop();
				}
			});
			return LOGIN_ATTEMPTS_EXCEEDED;
		}

		if(World.getPlayers().size() >= Constants.MAX_PLAYERS)
			return WORLD_FULL;

		if(Server.isUpdating())
			return UPDATE_IN_PROGRESS;

		if(playerDetails.getUID() != Configuration.getInt(Configuration.ConfigurationObject.CLIENT_VERSION))
			return SERVER_UPDATED;

		final Punishment punishment = PunishmentManager.getInstance().findBan(playerDetails.getName(), playerDetails.getIpAddress().split(":")[0], playerDetails.getMacAddress(), playerDetails.getSpecialUid());
		if(punishment != null) {
			playerDetails.getSession().write(
					new PacketBuilder()
							.put((byte)ACCOUNT_DISABLED.getReturnCode())
							.putRS2String(punishment.getCombination().getTarget().name())
							.putRS2String(punishment.getIssuerName())
							.putRS2String(punishment.getReason())
							.putRS2String(punishment.getTime().getRemainingTimeStamp())
							.toPacket()).addListener(future -> future.getSession().close(false));
			return ACCOUNT_DISABLED;
		}

		if(!NameUtils.isValidName(player.getName()) || player.getName().startsWith(" ") || player.getName().length() > 12 || player.getName().length() <= 0)
			return INVALID_CREDENTIALS;

		if(World.getPlayerByName(player.getName()) != null)
			return ALREADY_LOGGED_IN;

		/**
		 * If we get this far, we're loading the player his actual details to check.
		 */
		if(!PlayerLoading.loadPlayer(player, PlayerLoading.LoadingType.PRIORITY_ONLY))
			return NEW_PLAYER;

		if(!player.getPassword().equals(playerDetails.getPassword())) {
			LOGIN_ATTEMPTS.put(player.getName(), LOGIN_ATTEMPTS.get(player.getName()) + 1);
			return INVALID_CREDENTIALS;
		}

        if(player.getGoogleAuthenticatorKey() != null && !player.canLoginFreely(player.getShortIP())) {
			VerifyResponse verifyResponse = PlayerAuthenticatorVerification.verifyPlayer(player, playerDetails.getAuthenticationCode());
			if(verifyResponse == PIN_ENTERED_TWICE)
				return AUTHENTICATION_USED_TWICE;
			if(verifyResponse == INCORRECT_PIN) {
				LOGIN_ATTEMPTS.put(player.getName(), LOGIN_ATTEMPTS.get(player.getName()) + 1);
				return AUTHENTICATION_WRONG;
			}
			if(verifyResponse == CORRECT_PIN)
				player.saveIp(player.getShortIP());
		}

		if(Rank.hasAbility(player, Rank.ADMINISTRATOR))
			if(!ALLOWED_IPS.contains(player.getShortIP()) && !ALLOWED_IPS.contains(Integer.toString(player.getLastMac())))
				return INVALID_CREDENTIALS;

		LOGIN_ATTEMPTS.remove(player.getName());
		return SUCCESSFUL_LOGIN;
	}

	@Override
	public boolean savePlayer(Player player) {
		PlayerSaving.save(player);
		return true;
	}

	private static ObservableCollection<String> loadList(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			saveList(fileName);
			return new ObservableCollection<>(new ArrayList<>());
		}

		try (FileReader fileReader = new FileReader(file)) {
			JsonParser parser = new JsonParser();
			JsonArray object = (JsonArray) parser.parse(fileReader);
			return new ObservableCollection<>(new Gson().fromJson(object, new TypeToken<ArrayList<String>>() {}.getType()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ObservableCollection<>(new ArrayList<>());
		}
	}

	private static void saveList(String fileName) {
		saveList(new ObservableCollection<>(new ArrayList<>()), fileName);
	}

	private static void saveList(ObservableCollection<String> list, String fileName) {
		File fileToWrite = new File(fileName);

		if (!fileToWrite.getParentFile().exists()) {
			try {
				if(!fileToWrite.getParentFile().mkdirs())
					return;
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for list file!");
			}
		}

		try (FileWriter writer = new FileWriter(fileToWrite)) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			writer.write(builder.toJson(list, new TypeToken<ObservableCollection<String>>() {}.getType()));
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	static {
	}
}