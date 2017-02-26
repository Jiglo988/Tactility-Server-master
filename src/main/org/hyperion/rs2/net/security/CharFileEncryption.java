package org.hyperion.rs2.net.security;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;

/**
 * Created by Gilles on 11/11/2015.
 */
public final class CharFileEncryption implements Serializable {

    private final Key key;
    static final long serialVersionUID = 1561486465;

    public CharFileEncryption(String password) {
        this(convertPassword(password));
    }

    public CharFileEncryption(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public static Key convertPassword(String password) {
        return new SecretKeySpec(password.getBytes(), "AES");
    }
}