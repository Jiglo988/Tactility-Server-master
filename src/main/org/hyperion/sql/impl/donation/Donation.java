package org.hyperion.sql.impl.donation;

import java.sql.Timestamp;

public class Donation {

    public static final int DP_PER_DOLLAR = 100;

    public static final int DP_FOR_DONATOR = 20 * DP_PER_DOLLAR;
    public static final int DP_FOR_SUPER_DONATOR = 100 * DP_PER_DOLLAR;

    private final String tokenId;
    private final String name;
    private final double amount;
    private final boolean finished;
    private final int row;
    private final Timestamp time;
    private final boolean passed;
    private final boolean recklesspk;
    private final int index;
    private final String method;
    private final boolean chargeBacked;
    private final String mail;

    public Donation(final String tokenId, final String name, final double amount, final boolean finished, final int row, final Timestamp time, final boolean passed, final boolean recklesspk, final int index, final String method, final boolean chargeBacked, final String mail) {
        this.tokenId = tokenId;
        this.name = name;
        this.amount = amount;
        this.finished = finished;
        this.row = row;
        this.time = time;
        this.passed = passed;
        this.recklesspk = recklesspk;
        this.index = index;
        this.method = method;
        this.chargeBacked = chargeBacked;
        this.mail = mail;
    }

    public String tokenId() {
        return tokenId;
    }

    public String name() {
        return name;
    }

    public double amount() {
        return amount;
    }

    public int dollars() {
        return (int) amount;
    }

    public int points() {
        return dollars() * DP_PER_DOLLAR;
    }

    public boolean finished() {
        return finished;
    }

    public int row() {
        return row;
    }

    public Timestamp time() {
        return time;
    }

    public boolean passed() {
        return passed;
    }

    public boolean recklesspk() {
        return recklesspk;
    }

    public int index() {
        return index;
    }

    public String method() {
        return method;
    }

    public boolean chargeBacked() {
        return chargeBacked;
    }

    public String mail() {
        return mail;
    }
}
