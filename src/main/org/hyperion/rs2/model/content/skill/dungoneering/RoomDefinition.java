package org.hyperion.rs2.model.content.skill.dungoneering;

import org.apache.mina.core.buffer.IoBuffer;
import org.hyperion.Configuration;
import org.hyperion.Server;
import org.hyperion.rs2.model.GameObject;
import org.hyperion.rs2.model.GameObjectDefinition;
import org.hyperion.rs2.model.ObjectManager;
import org.hyperion.rs2.model.Position;
import org.hyperion.util.Misc;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 2/20/15
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class RoomDefinition {

    public static final List<RoomDefinition> ROOM_DEFINITIONS_LIST = new ArrayList<>();
    public static final RoomDefinition START_ROOM;

    static {
        START_ROOM = new RoomDefinition(2908, 9913, 2917, 9912, Arrays.asList(new Point(2910, 9907)));
        ROOM_DEFINITIONS_LIST.remove(START_ROOM);
    }

    public final int x, y;
    public final int x_end, y_end;
    public final List<Point> spawnLocations;

    public RoomDefinition(final int x, final int y, int x_end, int y_end, List<Point> spawnLocations) {
        this.x = x;
        this.y = y;
        this.x_end = x_end;
        this.y_end = y_end;
        this.spawnLocations = spawnLocations;
        ROOM_DEFINITIONS_LIST.add(this);
        ObjectManager.addObject(new GameObject(GameObjectDefinition.forId(2476), Position.create(x, y, 0), 10, 0));
        ObjectManager.addObject(new GameObject(GameObjectDefinition.forId(2477), Position.create(x_end, y_end, 0), 10, 0));
    }

    public final Room getRoom(final Dungeon dungeon, final int loop_around) {
        return new Room(dungeon, this, dungeon.heightLevel * (int)Math.pow(4, loop_around));
    }

    public Point randomLoc() {
        return spawnLocations.get(Misc.random(spawnLocations.size() - 1));
    }

    public String toString() {
        return String.format("LocX: %d LocY: %d EnxX : %d EndY: %d Size: %d", x, y, x_end, y_end, spawnLocations.size());
    }

    public void save(final IoBuffer buffer) {
        buffer.putShort((short) x);
        buffer.putShort((short) y);
        buffer.putShort((short) x_end);
        buffer.putShort((short) y_end);

        buffer.put((byte)spawnLocations.size());

        for(final Point entries : spawnLocations) {
            buffer.putShort((short) entries.x);
            buffer.putShort((short) entries.y);
        }

    }

    public static void load() {
        try {
            File f = new File("./data/roomdef.bin");
            InputStream is = new FileInputStream(f);
            IoBuffer buf = IoBuffer.allocate(1024);
            buf.setAutoExpand(true);
            while(true) {
                byte[] temp = new byte[1024];
                int read = is.read(temp, 0, temp.length);
                if(read == - 1) {
                    break;
                } else {
                    buf.put(temp, 0, read);
                }
            }
            buf.flip();
            int defs = 0;
            while(buf.hasRemaining()) {
                try {
                    int x = buf.getUnsignedShort();
                    int y = buf.getUnsignedShort();
                    int x_end = buf.getUnsignedShort();
                    int y_end = buf.getUnsignedShort();

                    int locs = buf.getUnsigned();

                    final List<Point> points = new ArrayList<>();
                    for(int i = 0; i < locs; i++) {
                        points.add(new Point(buf.getUnsignedShort(), buf.getUnsignedShort()));
                    }

                    new RoomDefinition(x, y, x_end, y_end, points);
                    defs++;
                } catch(Exception ex) {

                }
            }

            if(Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
                Server.getLogger().log(Level.INFO, "Loaded "+defs+" Room Definitions");
        }catch(final Exception ex) {

        }
    }


    public static RoomDefinition rand() {
        return ROOM_DEFINITIONS_LIST.get(Misc.random(ROOM_DEFINITIONS_LIST.size() - 1));
    }

    public static RoomDefinition getStartRoom() {
        return START_ROOM;
    }


}
