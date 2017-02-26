package org.hyperion.rs2.model.content.authentication;

import org.hyperion.Configuration;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.TaskManager;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.DialogueManager;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.net.security.EncryptionStandard;
import org.hyperion.rs2.net.security.authenticator.Authentication;
import org.hyperion.rs2.net.security.authenticator.GoogleAuthenticatorKey;
import org.hyperion.rs2.saving.PlayerSaving;
import org.hyperion.util.Time;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hyperion.Configuration.ConfigurationObject.NAME;

/**
 * This class will create the player his authentication keys, e-mail the backup codes to him and set everything up to work in the future with it.
 *
 * Created by Gilles on 19/02/2016.
 */
public final class PlayerAuthenticationGenerator {

    private final static Map<String, GoogleAuthenticatorKey> KEY_MAP = new HashMap<>();

    public static void startAuthenticationDialogue(Player player) {
        if(player.getGoogleAuthenticatorKey() == null) {
            player.getActionSender().sendDialogue("Select an option", ActionSender.DialogueType.OPTION, 1, Animation.FacialAnimation.DEFAULT, "Set up authenticator", "More information");
            player.getInterfaceState().setNextDialogueId(0, 650);
            player.getInterfaceState().setNextDialogueId(1, 651);
            return;
        }
        player.getActionSender().sendDialogue("Select an option", ActionSender.DialogueType.OPTION, 1, Animation.FacialAnimation.DEFAULT, "Disable authenticator", "More information");
        player.getInterfaceState().setNextDialogueId(0, 652);
    }

    public static void disableAuthenticator(Player player) {
        player.getActionSender().sendDialogue("Safety master", ActionSender.DialogueType.NPC, 4375, Animation.FacialAnimation.DEFAULT, "To disable your authenticator you simply have to", "give me the code correctly once.");
        player.getInterfaceState().setNextDialogueId(0, 655);
    }

    public static void removeAuthenticator(Player player, String confirmCode) {
        if(!StringUtil.isNumeric(confirmCode)) {
            player.getInterfaceState().setStringListener("authenticator_removal_confirmation", "Key incorrect, try again");
            return;
        }

        if(Authentication.authenticateKey(player.getGoogleAuthenticatorKey(), new ArrayList<>(), Integer.parseInt(confirmCode)) == Authentication.AuthenticationResponse.CORRECT_KEY) {
            player.getActionSender().removeChatboxInterface();
            player.setGoogleAuthenticatorBackup(null);
            player.setGoogleAuthenticatorKey(null);
            player.sendMessage("Authenticator successfully removed!");
            PlayerSaving.save(player);
        } else {
            player.getInterfaceState().setStringListener("authenticator_removal_confirmation", "Key incorrect, try again");
        }
    }

    public static void setupAuthenticator(Player player) {
        GoogleAuthenticatorKey googleAuthenticatorKey = Authentication.generateCredentials();
        player.getActionSender().sendDialogue("Safety master", ActionSender.DialogueType.NPC, 4375, Animation.FacialAnimation.DEFAULT, "A web-page will open, please scan the code with your", "smartphone and then continue.", "", "");
        player.getActionSender().sendWebpage(Authentication.generateGoogleAuthenticatorQR(Configuration.getString(NAME), player.getSafeDisplayName(), googleAuthenticatorKey));
        KEY_MAP.put(player.getSafeDisplayName(), googleAuthenticatorKey);

        TaskManager.submit(new Task(Time.ONE_MINUTE * 3, "Player Authentication removal") {
            String playerName = player.getSafeDisplayName();
            @Override
            protected void execute() {
                KEY_MAP.remove(playerName);
                stop();
            }
        });

        player.getInterfaceState().setNextDialogueId(0, 654);
    }

    public static void confirmAuthenticator(Player player, String confirmCode) {
        if(!KEY_MAP.containsKey(player.getSafeDisplayName())) {
            player.sendMessage("Authentication failed, please try again.");
            setupAuthenticator(player);
            return;
        }

        if(!StringUtil.isNumeric(confirmCode)) {
            player.sendMessage("This authentication code is incorrect, please try again");
            DialogueManager.openDialogue(player, 654);
            return;
        }

        GoogleAuthenticatorKey googleAuthenticatorKey = KEY_MAP.get(player.getSafeDisplayName());

        if(googleAuthenticatorKey == null) {
            player.sendMessage("Authentication failed, please try again.");
            setupAuthenticator(player);
            return;
        }

        if(Authentication.authenticateKey(EncryptionStandard.encryptGoogleKey(googleAuthenticatorKey.getKey()), new ArrayList<>(), Integer.parseInt(confirmCode)) == Authentication.AuthenticationResponse.CORRECT_KEY) {
            createAuthentication(player, googleAuthenticatorKey);
        } else {
            player.getInterfaceState().setStringListener("authenticator_confirmation", "Key incorrect, try again.");
        }
    }

    private static void createAuthentication(Player player, GoogleAuthenticatorKey googleAuthenticatorKey) {
        player.setGoogleAuthenticatorKey(EncryptionStandard.encryptGoogleKey(googleAuthenticatorKey.getKey()));
        player.setGoogleAuthenticatorBackup(googleAuthenticatorKey.getScratchCodes().stream().map(key -> EncryptionStandard.encryptGoogleKey(Integer.toString(key))).collect(Collectors.toList()));
        player.getActionSender().sendDialogue("Safety master", ActionSender.DialogueType.NPC, 4375, Animation.FacialAnimation.DEFAULT, "Your 2-step authentication has now been set up.", "From your next login you will have to enter the code.", "Thank you for keeping your account secure!");
        PlayerSaving.save(player);
    }
}
