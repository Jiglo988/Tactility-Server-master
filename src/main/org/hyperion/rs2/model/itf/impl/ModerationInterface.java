package org.hyperion.rs2.model.itf.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 2/10/15
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModerationInterface extends Interface {

    public ModerationInterface() {
        super(12);
    }

    @Override
    public void handle(Player player, Packet pkt) {

    }

    public Packet show(final String name) {
        return createDataBuilder().putRS2String(name).toPacket();
    }
}
