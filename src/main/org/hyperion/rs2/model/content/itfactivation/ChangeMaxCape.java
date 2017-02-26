package org.hyperion.rs2.model.content.itfactivation;

import org.hyperion.engine.task.Task;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.UpdateFlags;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.color.Color;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;

import java.util.stream.Stream;

/**
 * Created by Jet on 1/8/2015.
 */
public class ChangeMaxCape extends Interface implements ContentTemplate{

    private static final int ID = 4;

    public ChangeMaxCape() {
        super(ID);
    }


    @Override
    public void handle(Player player, Packet pkt) {
        final int type = pkt.getByte();
        final int oldColor_1 = player.maxCapePrimaryColor;
        final int oldColor_2 = player.maxCapeSecondaryColor;
        if(type == 0) {
            if(player.getPoints().getPkPoints() >= 10000) {
                player.maxCapePrimaryColor = pkt.getInt();
                player.maxCapeSecondaryColor = pkt.getInt();
                player.sendMessage("You successfully changed your colors");
                player.getPoints().setPkPoints(player.getPoints().getPkPoints() - 10000);
            } else {
                player.sendMessage("You need 10k PK points to change your max cape colors");
            }
        } else {
            player.maxCapePrimaryColor = pkt.getInt();
            player.maxCapeSecondaryColor = pkt.getInt();
            player.cE.lastHit = System.currentTimeMillis();
            player.getLastAttack().updateLastAttacker(player.getLastAttack().getName());
            World.submit(new Task(5000, "max cape color change") {
                @Override
                public void execute() {
                    player.maxCapePrimaryColor = oldColor_1;
                    player.maxCapeSecondaryColor = oldColor_2;
                    player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);
                    //s
                    this.stop();

                }
            });
        }
        player.getUpdateFlags().flag(UpdateFlags.UpdateFlag.APPEARANCE);

    }

    @Override
    public boolean clickObject(Player player, int type, int a, int b, int c, int d) {
        if(System.currentTimeMillis() - player.cE.lastHit < 10000) {
            player.sendMessage("You can't do this right now");
            return false;
        }
        show(player);
        final PacketBuilder builder = createDataBuilder();
        try {
            final int indexOne = Stream.of(Color.values()).filter(x -> x.color == player.maxCapePrimaryColor).findFirst().get().ordinal();
            final int indexTwo = Stream.of(Color.values()).filter(x -> x.color == player.maxCapeSecondaryColor).findFirst().get().ordinal();
            builder.put((byte)indexOne).put((byte)indexTwo);
        }catch(final Exception e) {
            builder.put((byte)0).put((byte)0);
        }
        player.write(builder.toPacket());
        return true;
    }

    @Override
    public int[] getValues(int type) {
        if(type == ClickType.ITEM_OPTION3)
            return new int[]{12744};
        return new int[0];
    }
}
