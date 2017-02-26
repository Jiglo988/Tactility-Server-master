package org.hyperion.rs2.model.content.skill.hunter;

import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.impl.NpcDeathTask;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.Constants;
import org.hyperion.rs2.util.EntityList;

import java.util.*;

/**
 * @author Daniel
 *         5/19/2016
 */
public class HunterNPCs {

    private static final int MAXIMUM_NPCS = 250;
    private static final EntityList<NPC> imps = new EntityList<>(MAXIMUM_NPCS);

    public static void startup() {
        for (int array = 0; array < MAXIMUM_NPCS; array++) {
            spawn(Spawn.getRandomLocation());
        }
    }

    public static void spawn(final Spawn spawn) {
        final Impling imp = Impling.getRandomImpling();
        final NPC npc = new NPC(NPCDefinition.forId(imp.imps.get(new Random().nextInt(imp.imps.size()))), -1, Position.create(new Random().nextInt((spawn.maxX - spawn.minX) + 1) + spawn.minX, new Random().nextInt((spawn.maxY - spawn.minY) + 1) + spawn.minY, 0));
        npc.setWalkDistance(npc.getPosition(), 15, 15, 15, 15);
        imps.add(npc);
        NPCManager.addNPC(npc);
    }

    public static boolean remove(final int id, final int x, final int y) {
        for (NPC value : imps) {
            if (value != null && value.getDefinition().getId() == id
                    && value.getPosition().getX() == x
                    && value.getPosition().getY() == y) {
                remove(value);
                return true;
            }
        }
        return false;
    }

    private static void remove(final NPC npc) {
        if (EntityHandler.deregister(npc)) {
            imps.remove(npc);
            npc.destroy();
        }
    }

    private enum Impling {
        BABY(Arrays.asList(1028, 6055), 58),
        YOUNG(Arrays.asList(1029, 6056), 54),
        GOURMET(Arrays.asList(1030, 6057), 50),
        EARTH(Arrays.asList(1031, 6058), 45),
        ESSENCE(Arrays.asList(1032, 6059), 43),
        ELECTRIC(Arrays.asList(1033, 6060), 40),
        NATURE(Arrays.asList(1034, 6061), 38),
        MAGPIE(Arrays.asList(1035, 6062), 32),
        NINJA(Arrays.asList(6053, 6063), 24),
        PIRATE(Arrays.asList(7845, 7846), 18),
        DRAGON(Arrays.asList(6054, 6064), 10),
        KINGLY(Arrays.asList(7903, 7906), 7);

        public static final List<Impling> list = Collections.unmodifiableList(Arrays.asList(values()));
        private final List<Integer> imps;
        private final int chance;

        Impling(List<Integer> imps, int chance) {
            this.imps = imps;
            this.chance = chance;
        }

        public static Impling getRandomImpling() {
            Impling location = null;
            final List<Impling> current = new ArrayList<>(list);
            final List<Impling> temporal = new ArrayList<>();
            while (location == null) {
                final SortedMap<Integer, List<Impling>> map = new TreeMap<>();
                current.stream().forEach(value -> {
                    final int random = (int) (java.lang.Math.random() * ((100 - value.chance) + 1));
                    if (!map.containsKey(random)) {
                        map.put(random, new ArrayList<>());
                    }
                    map.get(random).add(value);
                });
                map.entrySet().stream().sorted((one, key) -> Integer.compare(one.getKey(), key.getKey())).flatMap(integerListEntry -> integerListEntry.getValue().stream()).limit(current.size() / 2).forEach(temporal::add);
                current.clear();
                current.addAll(temporal);
                temporal.clear();
                if (current.size() == 1) {
                    location = current.stream().findFirst().get();
                }
            }
            return location;
        }
    }

    public enum Spawn {
        EDGEVILLE(3068, 3463, 3172, 3502),
        VARROCK(3156, 3328, 3269, 3502),
        FALADOR(2952, 3311, 3065, 3389),
        LUMBRIGE(3174, 3183, 3266, 3299),
        RIMMINGTON(2944, 3221, 3000, 3302),
        DRAYNOR(3100, 3239, 3215, 3344),
        BLACK_KNIGHT_MTN(2962, 3396, 3046, 3519),
        BARBARIAN_VILLAGE(3065, 3388, 3099, 3463),
        PORT_SARIM(3003, 3268, 3069, 3327);

        public static List<Spawn> list = Collections.unmodifiableList(Arrays.asList(values()));
        private final int minX, minY, maxX, maxY;

        Spawn(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public static Spawn getRandomLocation() {
            Spawn location = null;
            final List<Spawn> current = new ArrayList<>(list);
            final List<Spawn> temporal = new ArrayList<>();
            while (location == null) {
                final SortedMap<Integer, List<Spawn>> map = new TreeMap<>();
                current.stream().forEach(value -> {
                    final int random = (int) (java.lang.Math.random() * ((100 - ((value.maxX - value.minX) * (value.maxY - value.minY) / 1000)) + 1));
                    if (!map.containsKey(random)) {
                        map.put(random, new ArrayList<>());
                    }
                    map.get(random).add(value);
                });
                map.entrySet().stream().sorted((one, key) -> Integer.compare(one.getKey(), key.getKey())).flatMap(integerListEntry -> integerListEntry.getValue().stream()).limit(current.size() / 2).forEach(temporal::add);
                current.clear();
                current.addAll(temporal);
                temporal.clear();
                if (current.size() == 1) {
                    location = current.stream().findFirst().get();
                }
            }
            return location;
        }
    }
}
