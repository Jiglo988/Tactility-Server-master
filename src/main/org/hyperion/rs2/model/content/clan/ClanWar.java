package org.hyperion.rs2.model.content.clan;

import org.hyperion.util.Time;

import java.util.stream.Stream;

public class ClanWar {

    private static final long DELAY = Time.ONE_MINUTE * 2;

    private final ClanWarHolder[] clans;

    //when the war begins, walls go down
    private final long start = System.currentTimeMillis() + DELAY;

    public ClanWar(final ClanWarHolder... holders) {
        Stream.of(holders).forEach(h -> h.setWar(this));
        this.clans = holders;
    }

    public boolean started() {
        return start > System.currentTimeMillis();
    }

    public long secondsRemaining() {
        return (start - System.currentTimeMillis())/1000L;
    }



}
