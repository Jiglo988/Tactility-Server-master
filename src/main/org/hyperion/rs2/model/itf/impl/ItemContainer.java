package org.hyperion.rs2.model.itf.impl;


import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 1/29/15
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemContainer extends Interface {

    public ItemContainer() {
        super(10);
    }

    @Override
    public void handle(Player player, Packet pkt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void sendItems(final Player player,final String name, final int width, final int height, final Item... items) {
        show(player);
        final PacketBuilder builder = createDataBuilder();
        builder.putRS2String(name);
        builder.put((byte)width).put((byte)height);
        builder.put((byte)items.length);
        for(final Item item : items) {
            builder.putShort(item.getId());
            builder.putInt(item.getCount());
        }
        player.write(builder.toPacket());
    }
}
