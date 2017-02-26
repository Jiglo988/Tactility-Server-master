package org.hyperion.rs2.net.security.authenticator;

import org.hyperion.rs2.net.security.EncryptionStandard;

import java.util.List;

import static org.hyperion.rs2.net.security.authenticator.Authentication.AuthenticationResponse.*;

/**
 * Class to control Google Authentication.
 *
 * Created by Gilles on 19/02/2016.
 */
public final class Authentication {

    public enum AuthenticationResponse {
        BACKUP_CODE_USED,
        WRONG_KEY,
        CORRECT_KEY
    }

    private final static GoogleAuthenticator GOOGLE_AUTHENTICATOR = new GoogleAuthenticator();

    public static AuthenticationResponse authenticateKey(String key, List<String> encryptedBackupCodes, int enteredPin) {
        if (GOOGLE_AUTHENTICATOR.authorize(EncryptionStandard.decryptGoogleKey(key), enteredPin))
            return CORRECT_KEY;
        if(encryptedBackupCodes.stream().filter(pin -> pin.equals(EncryptionStandard.encryptGoogleKey(Integer.toString(enteredPin)))).count() > 0)
            return BACKUP_CODE_USED;
        return WRONG_KEY;
    }

    public static GoogleAuthenticatorKey generateCredentials() {
        return GOOGLE_AUTHENTICATOR.createCredentials();
    }

    public static String generateGoogleAuthenticatorQR(String title, String username, GoogleAuthenticatorKey googleAuthenticatorKey) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(title, username, googleAuthenticatorKey);
    }
}
