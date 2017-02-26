package org.hyperion.rs2.net.security;

import org.apache.commons.codec.binary.Base64;
import org.hyperion.Server;

import javax.crypto.Cipher;
import java.security.Key;

/**
 * Created by Gilles on 29/10/2015.
 **/

public final class EncryptionStandard {

    public static String encryptPassword(String password) {
        if(password == null)
            return "";
        return encrypt(password.toLowerCase(), Server.getCharFileEncryption().getKey());
    }

    public static String decryptPassword(String password) {
        return decrypt(password, Server.getCharFileEncryption().getKey());
    }

    public static String encryptGoogleKey(String key) {
        return encrypt(key, Server.getCharFileEncryption().getKey());
    }

    public static String decryptGoogleKey(String key) {
        return decrypt(key, Server.getCharFileEncryption().getKey());
    }

    public static String encrypt(String plainText, Key encryptionKey) {
        if(plainText == null)
            return "";
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.encodeBase64String(encryptedBytes);
        } catch(Exception e) {
            e.printStackTrace();
            return plainText;
        }
    }

    private static String decrypt(String encrypted, Key encryptionKey) {
        try {
        Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
        byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(encrypted));

        return new String(plainBytes);
        } catch(Exception e) {
            e.printStackTrace();
            return encrypted;
        }
    }
}
