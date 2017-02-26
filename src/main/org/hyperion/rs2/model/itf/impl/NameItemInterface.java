package org.hyperion.rs2.model.itf.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 1/30/15
 * Time: 2:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class NameItemInterface extends Interface {

    public NameItemInterface() {
        super(11);
    }


    @Override
    public void handle(Player player, Packet pkt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void send(final Player player, final String string) {
        show(player);
        player.write(createDataBuilder().putRS2String(string).toPacket());
    }
}
