package org.hyperion.rs2.model.content.jge.entry.progress;

import org.hyperion.rs2.model.content.jge.entry.Entry;

/**
 * Created by Administrator on 9/24/2015.
 */
public class Progress {

    public final String playerName;
    public final Entry.Type type;
    public final int unitPrice;
    public final int quantity;

    public final int totalPrice;

    public Progress(final String playerName, final Entry.Type type, final int unitPrice, final int quantity){
        this.playerName = playerName;
        this.type = type;
        this.unitPrice = unitPrice;
        this.quantity = quantity;

        totalPrice = quantity * unitPrice;
    }

    public Progress copy(){
        return new Progress(playerName, type, unitPrice, quantity);
    }

    public String toSaveString(){
        return String.format("%s:%s:%d:%d", playerName, type.name(), unitPrice, quantity);
    }

    public static Progress fromSaveString(final String progress){
        final String[] split = progress.split(":");
        return new Progress(
                split[0],
                Entry.Type.valueOf(split[1]),
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3])
        );
    }

}
