package org.hyperion.rs2.model.content.skill.summoning;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.util.TextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Daniel on 5/28/2016.
 */
public enum Pouch {
    SPIRIT_WOLF(-26525, 12047, Charm.GOLD, 7, 2859, 1, 0x1.3333333333333p2, 6829, 6),
    DREADFOWL(-26524, 12043, Charm.GOLD, 8, 2138, 8, 0x1.299999999999ap3, 6824, 4),
    SPIRIT_SPIDER(-26523, 12059, Charm.GOLD, 8, 6291, 8, 0x1.9333333333333p3, 6825, 15),
    THORNY_SNAIL(-26522, 12019, Charm.GOLD, 9, 3363, 13, 0x1.9333333333333p3, 6806, 16),
    GRANITE_CRAB(-26521, 12009, Charm.GOLD, 7, 440, 16, 0x1.599999999999ap4, 6796, 18),
    SPIRIT_MOSQUITO(-26520, 12778, Charm.GOLD, 1, 6319, 17, 0x1.74p5, 7331, 12),
    DESERT_WYRM(-26519, 12049, Charm.GREEN, 45, 1783, 18, 0x1.f333333333333p4, 6831, 19),
    SPIRIT_SCORPION(-26518, 12055, Charm.CRIMSON, 57, 3095, 19, 0x1.4cccccccccccdp6, 6837, 17),
    SPIRIT_TZ_KIH(-26517, 12808, Charm.CRIMSON, 64, 12168, 22, 0x1.8333333333333p6, 7361, 18),
    ALBINO_RAT(-26516, 12067, Charm.BLUE, 75, 2134, 23, 0x1.94ccccccccccdp7, 6847, 22),
    SPIRIT_KALPHITE(-26515, 12063, Charm.BLUE, 51, 3138, 25, 0x1.b8p7, 6994, 22),
    COMPOST_MOUND(-26514, 12091, Charm.GREEN, 47, 6032, 28, 0x1.8e66666666666p5, 6871, 24),
    GIANT_CHINCHOMPA(-26513, 12800, Charm.BLUE, 84, 9976, 29, 0x1.fe66666666666p7, 7353, 31),
    VAMPIRE_BAT(-26512, 12053, Charm.CRIMSON, 81, 3325, 31, 0x1.1p7, 6835, 33),
    HONEY_BADGER(-26511, 12065, Charm.CRIMSON, 84, 12156, 32, 0x1.199999999999ap7, 6845, 25),
    BEAVER(-26510, 12021, Charm.GREEN, 72, 1519, 33, 0x1.ccccccccccccdp5, 6808, 27),
    VOID_RAVAGER(-26509, 12818, Charm.GREEN, 74, 12164, 34, 0x1.dcccccccccccdp5, 7370, 27),
    VOID_SHIFTER(-26508, 12814, Charm.BLUE, 74, 12165, 34, 0x1.dcccccccccccdp5, 7367, 94),
    VOID_SPINNER(-26507, 12780, Charm.BLUE, 74, 12166, 34, 0x1.dcccccccccccdp5, 7333, 27),
    VOID_TORCHER(-26506, 12798, Charm.BLUE, 74, 12167, 34, 0x1.dcccccccccccdp5, 7351, 94),
    BRONZE_MINOTAUR(-26505, 12073, Charm.BLUE, 102, 2349, 36, 0x1.3cccccccccccdp8, 6853, 30),
    BULL_ANT(-26504, 12087, Charm.GOLD, 11, 6010, 40, 0x1.a666666666666p5, 6867, 30),
    MACAW(-26503, 12071, Charm.GREEN, 78, 13572, 41, 0x1.219999999999ap6, 6850, 31),
    EVIL_TURNIP(-26502, 12051, Charm.CRIMSON, 104, 12153, 42, 0x1.719999999999ap7, 6833, 30),
    SPIRIT_COCKATRICE(-26501, 12095, Charm.GREEN, 88, 12109, 43, 0x1.2cccccccccccdp6, 6875, 36),
    SPIRIT_CORAXATRICE(-26500, 12105, Charm.GREEN, 88, 12119, 43, 0x1.2cccccccccccdp6, 6885, 36),
    SPIRIT_GUTHATRICE(-26499, 12097, Charm.GREEN, 88, 12111, 43, 0x1.2cccccccccccdp6, 6877, 36),
    SPIRIT_PENGATRICE(-26498, 12103, Charm.GREEN, 88, 12117, 43, 0x1.2cccccccccccdp6, 6883, 36),
    SPIRIT_SARATRICE(-26497, 12099, Charm.GREEN, 88, 12113, 43, 0x1.2cccccccccccdp6, 6879, 36),
    SPIRIT_VULATRICE(-26496, 12107, Charm.GREEN, 88, 12121, 43, 0x1.2cccccccccccdp6, 6887, 36),
    SPIRIT_ZAMATRICE(-26495, 12101, Charm.GREEN, 88, 12115, 43, 0x1.2cccccccccccdp6, 6881, 36),
    PYRELORD(-26494, 12816, Charm.CRIMSON, 111, 590, 46, 0x1.94ccccccccccdp7, 7377, 37),
    IRON_MINOTAUR(-26493, 12075, Charm.BLUE, 125, 2351, 46, 0x1.94ccccccccccdp8, 6855, 32),
    MAGPIE(-26492, 12041, Charm.GREEN, 88, 1635, 47, 0x1.4cccccccccccdp6, 6824, 34),
    BLOATED_LEECH(-26491, 12061, Charm.CRIMSON, 117, 2132, 49, 0x1.ae66666666666p7, 6843, 34),
    SPIRIT_TERRORBIRD(-26490, 12007, Charm.GOLD, 12, 9978, 52, 0x1.119999999999ap6, 6794, 36),
    ABYSSAL_PARASITE(-26489, 12035, Charm.GREEN, 106, 12161, 54, 0x1.7b33333333333p6, 6818, 30),
    SPIRIT_JELLY(-26488, 12027, Charm.BLUE, 151, 1937, 55, 0x1.e4p8, 6992, 43),
    IBIS(-26487, 12531, Charm.GREEN, 109, 311, 56, 0x1.8b33333333333p6, 6841, 38),
    STEEL_MINOTAUR(-26486, 12077, Charm.BLUE, 141, 2353, 56, 0x1.ecccccccccccdp8, 6841, 46),
    SPIRIT_KYATT(-26485, 12812, Charm.BLUE, 153, 10103, 57, 0x1.f59999999999ap8, 6841, 49),
    SPIRIT_GRAAHK(-26484, 12810, Charm.BLUE, 154, 10099, 57, 0x1.f59999999999ap8, 6841, 49),
    SPIRIT_LARUPIA(-26483, 12784, Charm.BLUE, 155, 10095, 57, 0x1.f59999999999ap8, 6841, 49),
    KARAMTHULHU_OVERLORD(-26482, 12023, Charm.BLUE, 144, 6667, 58, 0x1.fe66666666666p8, 6841, 44),
    SMOKE_DEVIL(-26481, 12085, Charm.CRIMSON, 141, 9736, 61, 0x1.0cp8, 6841, 48),
    ABYSSAL_LURKER(-26480, 12037, Charm.GREEN, 119, 12161, 62, 0x1.b666666666666p6, 6841, 41),
    SPIRIT_COBRA(-26479, 12015, Charm.CRIMSON, 116, 6287, 63, 0x1.14ccccccccccdp8, 6481, 56),
    STRANGER_PLANT(-26478, 12045, Charm.CRIMSON, 128, 8431, 64, 0x1.199999999999ap8, 6841, 49),
    BARKER_TOAD(-26477, 12123, Charm.GOLD, 11, 2150, 66, 0x1.5cp6, 6841, 8),
    MITHRIL_MINOTAUR(-26476, 12079, Charm.BLUE, 152, 2359, 66, 0x1.2266666666666p9, 6841, 55),
    WAR_TORTOISE(-26475, 12031, Charm.GOLD, 1, 7939, 67, 0x1.d4ccccccccccdp5, 6841, 43),
    BUNYIP(-26474, 12029, Charm.GREEN, 110, 383, 68, 0x1.dcccccccccccdp6, 6841, 44),
    FRUIT_BAT(-26473, 12033, Charm.GREEN, 130, 1963, 69, 0x1.e4ccccccccccdp6, 6841, 45),
    RAVENOUS_LOCUST(-26472, 12820, Charm.CRIMSON, 79, 1933, 70, 0x1.08p7, 6841, 24),
    ARCTIC_BEAR(-26471, 12057, Charm.GOLD, 14, 10117, 71, 0x1.74ccccccccccdp6, 6841, 28),
    PHEONIX(-26470, 14623, Charm.CRIMSON, 165, 14616, 72, 0x1.2dp8, 6841, 30),
    OBSIDIAN_GOLEM(-26469, 12792, Charm.BLUE, 195, 12168, 73, 0x1.4133333333333p9, 6841, 55),
    GRANITE_LOBSTER(-26468, 12069, Charm.CRIMSON, 166, 6979, 74, 0x1.459999999999ap8, 6841, 47),
    PRAYING_MANTIS(-26467, 12011, Charm.CRIMSON, 168, 2460, 75, 0x1.499999999999ap8, 6841, 69),
    FORGE_REGENT(-26466, 12782, Charm.GREEN, 141, 10020, 76, 0x1.0cp7, 6841, 45),
    ADAMANT_MINOTAUR(-26465, 12081, Charm.BLUE, 144, 2361, 76, 0x1.4e66666666666p9, 6841, 66),
    TALON_BEAST(-26464, 12794, Charm.CRIMSON, 174, 12162, 77, 0x1.fb9999999999ap9, 6841, 49),
    GIANT_ENT(-26463, 12013, Charm.GREEN, 124, 5933, 78, 0x1.119999999999ap7, 6841, 49),
    FIRE_TITAN(-26462, 12802, Charm.BLUE, 198, 1442, 79, 0x1.5b9999999999ap9, 6841, 62),
    ICE_TITAN(-26461, 12806, Charm.BLUE, 198, 1444, 79, 0x1.5b9999999999ap9, 6841, 64),
    MOSS_TITAN(-26460, 12804, Charm.BLUE, 202, 1440, 79, 0x1.5b9999999999ap9, 6841, 58),
    HYDRA(-26459, 12025, Charm.GREEN, 128, 571, 80, 0x1.199999999999ap7, 6841, 49),
    SPIRIT_DAGGANOTH(-26458, 12017, Charm.CRIMSON, 1, 6155, 83, 0x1.6cccccccccccdp8, 6841, 57),
    LAVA_TITAN(-26457, 12788, Charm.BLUE, 219, 12168, 83, 0x1.6d33333333333p9, 6841, 61),
    SWAMP_TITAN(-26456, 12776, Charm.CRIMSON, 150, 10149, 85, 0x1.759999999999ap8, 6841, 56),
    RUNE_MINOTAUR(-26455, 12083, Charm.BLUE, 1, 2363, 86, 0x1.7a66666666666p9, 6841, 151),
    UNICORN_STALLION(-26454, 12039, Charm.GREEN, 140, 237, 88, 0x1.34ccccccccccdp7, 6841, 54),
    GEYSER_TITAN(-26453, 12786, Charm.BLUE, 222, 1444, 89, 0x1.879999999999ap9, 6841, 69),
    WOLPERTINGER(-26452, 3226, Charm.CRIMSON, 203, 3226, 92, 0x1.94ccccccccccdp8, 6841, 62),
    ABYSSAL_TITAN(-26451, 12796, Charm.GREEN, 113, 12161, 93, 0x1.4666666666666p7, 6841, 32),
    IRON_TITAN(-26450, 12822, Charm.CRIMSON, 198, 1115, 95, 0x1.a19999999999ap8, 6841, 60),
    PACK_YAK(-26449, 12093, Charm.CRIMSON, 211, 10818, 96, 0x1.a666666666666p8, 6841, 58),
    STEEL_TITAN(-26448, 12790, Charm.CRIMSON, 178, 1119, 99, 0x1.b333333333333p8, 6841, 64);

    public static final List<Pouch> VALUES = Arrays.asList(values());
    public static final List<Integer> BUTTONS = VALUES.stream().map(Pouch::getButton).collect(Collectors.toList());
    private static final Map<Integer, Pouch> BY_BUTTON_VALUE = Stream.of(values()).collect(Collectors.toMap(Pouch::getButton, Function.identity())),
            BY_POUCH_VALUE = Stream.of(values()).collect(Collectors.toMap(Pouch::getPouch, Function.identity()));
    private final String NAME;
    private final Charm CHARM;
    private final int BUTTON,
            POUCH,
            INGREDIENT,
            SHARDS,
            LEVEL,
            FAMILIAR,
            DURATION;
    private final double EXPERIENCE;

    Pouch(int button, int pouch, Charm charm, int shards, int ingredient, int level, double experience, int familiar, int duration) {
        this.NAME = TextUtils.titleCase(this.toString().replace("_", " "));
        this.BUTTON = button;
        this.POUCH = pouch;
        this.CHARM = charm;
        this.SHARDS = shards;
        this.INGREDIENT = ingredient;
        this.LEVEL = level;
        this.EXPERIENCE = experience;
        this.FAMILIAR = familiar;
        this.DURATION = duration;
    }

    public static Pouch getByButtonValue(final int value) {
        return BY_BUTTON_VALUE.get(value);
    }

    public static Pouch getByPouchValue(final int value) {
        return BY_POUCH_VALUE.get(value);
    }

    public String getName() {
        return NAME;
    }

    public int getButton() {
        return BUTTON;
    }

    public int getPouch() {
        return POUCH;
    }

    public int getFamiliar() {
        return FAMILIAR;
    }

    public int getDuration() {
        return DURATION;
    }

    private int getCreationAmount(final int a, final int b, final int c, final int d) {
        int amount = a;
        if (b < amount) {
            amount = b;
        }
        if (c < amount) {
            amount = c;
        }
        if (d < amount) {
            amount = d;
        }
        return amount;
    }

    private String missingIngredient(final Player player) {
        return player.getInventory().getCount(18016) < SHARDS
                ? String.format("Spirit Shards @bla@[@red@%d@bla@]@red@", SHARDS) : player.getInventory().getCount(CHARM.getItem()) < 1
                ? String.format("%s Charms", CHARM.toString()) : player.getInventory().getCount(12155) < 1
                ? "Summoning Pouches @bla@[@red@12155@bla@]@red@" : player.getInventory().getCount(INGREDIENT) < 1
                ? String.format("%s @bla@[@red@%d@bla@]@red@", Item.create(INGREDIENT).getDefinition().getName(), INGREDIENT) : "???";
    }

    public boolean hasRequiredLevel(final Player player) {
        return player.getSkills().getLevel(Skills.SUMMONING) >= LEVEL;
    }

    public boolean create(final Player player) {
        if (!hasRequiredLevel(player)) {
            player.sendf("You need a Summoning level of '@red@%d@bla@' to create a '@red@%s@bla@' pouch.", LEVEL, NAME);
            return false;
        }
        int amount = getCreationAmount(player.getInventory().getCount(18016) / SHARDS, player.getInventory().getCount(CHARM.getItem()), player.getInventory().getCount(12155), player.getInventory().getCount(INGREDIENT));
        if (amount < 1) {
            player.sendf("You need more '@red@%s@bla@' in order to create a '@red@%s@bla@' pouch.", missingIngredient(player), NAME);
            return false;
        }
        player.getActionSender().sendSidebarInterface(16, -1);
        player.getActionSender().setViewingSidebar(3);
        player.getInventory().remove(Item.create(18016, SHARDS * amount));
        player.getInventory().remove(Item.create(CHARM.getItem(), amount));
        player.getInventory().remove(Item.create(12155, amount));
        player.getInventory().remove(Item.create(INGREDIENT, amount));
        player.playAnimation(Animation.create(725));
        player.playGraphics(Graphic.create(1207));
        final int LEVEL = player.getSkills().getLevel(Skills.SUMMONING);
        player.getSkills().addExperience(Skills.SUMMONING, (EXPERIENCE * Constants.XPRATE) * amount);
        player.getInventory().add(Item.create(POUCH, amount));
        player.sendf("You infuse '@red@%d@bla@' '@red@%s@bla@' pouch%s.", amount, NAME, amount > 1 ? "es" : "");
        if (player.getSkills().getLevel(Skills.SUMMONING) != LEVEL) {
            player.getSummoningTab().refresh();
        }
        return true;
    }
}
