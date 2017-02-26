package org.hyperion.rs2.model;

import org.hyperion.rs2.util.PasswordEncryption;

public class Password {

    private String encryptedPass;

    private String salt;

    private String realPassword;

    private String tempPassword;

    public String getRealPassword() {
        return realPassword;
    }

    public String getEncryptedPass() {
        return encryptedPass;
    }

    public String getSalt() {
        return salt;
    }

    public void setRealPassword(String password) {
        this.realPassword = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setEncryptedPass(String encrypted) {
        this.encryptedPass = encrypted;
        //System.out.println("Setting encrypted pass: " + encrypted);
    }



    public Password(String password, String salt) {
        setRealPassword(password);
        this.salt = salt;
    }

    public Password(String password) {
        this(password,null);
    }

    public Password() {
        this(null,null);
    }

    public static String encryptPassword(String password, String salt) {
        return PasswordEncryption.sha1(password + salt);
    }


    public String getTempPassword() {
        return tempPassword;
    }


    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

}
