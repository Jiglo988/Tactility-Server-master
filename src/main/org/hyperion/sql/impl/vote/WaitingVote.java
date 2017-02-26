package org.hyperion.sql.impl.vote;

import java.sql.Date;

public class WaitingVote {

    private final int index;
    private final String fakePlayerName;
    private final String playerName;
    private final boolean runelocus;
    private final boolean topg;
    private final boolean rspslist;
    private final boolean runelocusProcessed;
    private final boolean topgProcessed;
    private final boolean rspslistProcessed;
    private final boolean processed;
    private final Date date;

    public WaitingVote(final int index, final String fakePlayerName, final String playerName, final boolean runelocus, final boolean topg, final boolean rspslist, final boolean runelocusProcessed, final boolean topgProcessed, final boolean rspslistProcessed, final boolean processed, final Date date) {
        this.index = index;
        this.fakePlayerName = fakePlayerName;
        this.playerName = playerName;
        this.runelocus = runelocus;
        this.topg = topg;
        this.rspslist = rspslist;
        this.runelocusProcessed = runelocusProcessed;
        this.topgProcessed = topgProcessed;
        this.rspslistProcessed = rspslistProcessed;
        this.processed = processed;
        this.date = date;
    }

    public int index() {
        return index;
    }

    public String fakePlayerName() {
        return fakePlayerName;
    }

    public String playerName() {
        return playerName;
    }

    public boolean runelocus() {
        return runelocus;
    }

    public boolean runelocusProcessed() {
        return runelocusProcessed;
    }

    public boolean topg() {
        return topg;
    }

    public boolean topgProcessed() {
        return topgProcessed;
    }

    public boolean rspslist() {
        return rspslist;
    }

    public boolean rspslistProcessed() {
        return rspslistProcessed;
    }

    public boolean processed() {
        return processed;
    }

    public Date date() {
        return date;
    }
}
