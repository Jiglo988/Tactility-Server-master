package org.hyperion.sql.impl.log.type;

import org.hyperion.sql.impl.log.Log;

import java.sql.Timestamp;

/**
 * Created by Gilles on 1/03/2016.
 */
public class IPLog extends Log {

    private final String playerName;
    private final String ip;

    public IPLog(String playerName, String ip) {
        super(now(), LogType.IP);
        this.playerName = playerName;
        this.ip = ip;
    }
    public IPLog(Timestamp timestamp, String playerName, String ip) {
        super(timestamp, LogType.IP);
        this.playerName = playerName;
        this.ip = ip;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getIp() {
        return ip;
    }
}
