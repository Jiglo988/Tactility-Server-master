package org.hyperion.sql.impl.log.util;

import org.hyperion.rs2.model.Player;

/**
 * Created by Gilles on 29/02/2016.
 */
public class PlayerDetail {

    private final String name;
    private final int mac;
    private final String ip;

    public PlayerDetail(final String name, final int mac, final String ip) {
        this.name = name;
        this.mac = mac;
        this.ip = ip;
    }

    public String name() {
        return name;
    }

    public int mac() {
        return mac;
    }

    public String ip() {
        return ip;
    }

    public static PlayerDetail detail(final Player player) {
        return new PlayerDetail(player.getName(), player.getUID(), player.getFullIP());
    }
}
