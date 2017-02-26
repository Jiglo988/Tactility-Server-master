package org.hyperion.sql.impl.log.type;

import org.hyperion.rs2.model.Item;
import org.hyperion.sql.impl.log.Log;
import org.hyperion.sql.impl.log.util.PlayerDetail;

/**
 * Created by Gilles on 29/02/2016.
 */
public class TradeLog extends Log {
    private final Item[] playerItems, otherPlayerItems;
    private final PlayerDetail player, otherPlayer;

    public TradeLog(PlayerDetail player, Item[] playerItems, PlayerDetail otherPlayer, Item[] otherPlayerItems) {
        super(now() , LogType.TRADE);
        this.player = player;
        this.playerItems = playerItems;
        this.otherPlayer = otherPlayer;
        this.otherPlayerItems = otherPlayerItems;
    }

    public Item[] getPlayerItems() {
        return playerItems;
    }

    public Item[] getOtherPlayerItems() {
        return otherPlayerItems;
    }

    public PlayerDetail getPlayer() {
        return player;
    }

    public PlayerDetail getOtherPlayer() {
        return otherPlayer;
    }
}
