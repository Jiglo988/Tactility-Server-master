package org.hyperion.rs2;

/**
 * Created by Gilles on 6/02/2016.
 */
public enum LoginResponse {
    NEW_PLAYER(-1),
    EXCHANGES_DATA(0),
    WAIT_AND_TRY_AGAIN(1),
    SUCCESSFUL_LOGIN(2),
    INVALID_CREDENTIALS(3),
    ACCOUNT_DISABLED(4),
    ALREADY_LOGGED_IN(5),
    SERVER_UPDATED(6),
    WORLD_FULL(7),
    UNABLE_TO_CONNECT(8),
    LOGIN_LIMIT_EXCEEDED(9),
    BAD_SESSION_ID(10),
    LOGIN_SERVER_REJECTED(11),
    MEMBERS_ONLY(12),
    COULD_NOT_COMPLETE(13),
    UPDATE_IN_PROGRESS(14),
    LOGIN_ATTEMPTS_EXCEEDED(16),
    AUTHENTICATION_WRONG(22),
    AUTHENTICATION_USED_TWICE(23);

    private final int returnCode;

    LoginResponse(int returnCode) {
        this.returnCode = returnCode;
    }

    public int getReturnCode() {
        return returnCode;
    }
}