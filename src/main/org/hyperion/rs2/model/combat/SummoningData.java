package org.hyperion.rs2.model.combat;

import org.hyperion.rs2.model.combat.summoning.SummoningSpecial;
import org.hyperion.util.Time;

import java.util.HashMap;
import java.util.Map;


public class SummoningData {


	public static final int ATTACK = 0;
	public static final int MAGIC = 6;
	public static final int DEFENCE = 1;
	public static final int MELEE = 3;
	public static final int RANGE = 4;
	public static final int STRENGTH = 2;
	public static final int NOTHING = - 1;


	/* Npc id, Pouch id, Name, Level required, Time living */

	/**
	 * {@link SummoningSpecial}
	 */
	public enum SummonType {
		SPIRIT_WOLF(6829, 12047, 1, 600, ATTACK),
		DREADFOWL(6825, 12043, 4, 400, MAGIC),//dreadfowl
		SPIRITSPIDER(6841, 12059, 10, 1500, MELEE),//Spirit spider
		THORNYSNAIL(6806, 12019, 13, 1600, RANGE),//Thorny snail
		GRANITECRAB(6796, 12009, 16, 1800, DEFENCE),//Granite crab
		SPIRIT_MOSQUITO(7331, 12778, 17, 1200, ATTACK),//spirit mosquito
		DESERT_WYRM(9470, 12049, 18, 1900, STRENGTH),//Desert wyrm
		SPIRIT_SCORPION(6838, 12055, 19, 1700, MELEE),//Spirit scorpion
		SPIRITTZKIH(7361, 12808, 22, 1800, MAGIC),//Spirit tz-kih
		ALBINORAT(6847, 12067, 23, 2200, ATTACK),//Albino rat
		COMPOSTMOUND(6871, 12091, 28, 2400, STRENGTH),//compost mound
		GIANTCHINCHOMPA(7353, 12800, 29, 3100, RANGE),//Giant chinchompa
		VAMPIREBAT(9474, 12053, 31, 3300, MELEE),//Vampire bat
		HONEYBADGER(6845, 12065, 32, 2500, STRENGTH),//honey badger
		BEAVER(6808, 12021, 33, 2700, NOTHING),//beaver
		VOIDRANGER(9476, 12818, 34, 2700, STRENGTH),//void ravager
		VOIDSPINNER(7334, 12780, 34, 2700, DEFENCE),//void spinner
		VOIDSHIFTER(7367, 12814, 34, 9400, ATTACK),//void shifter
		VOICTORCHER(7352, 12798, 34, 9400, MAGIC),//void torcher
		BRONZEMINO(6853, 12073, 36, 3000, DEFENCE),//bronze minotaur
		BULANT(6867, 12087, 40, 3000, MELEE),//bul ant
		MACAW(6851, 12071, 41, 3100, NOTHING),//macaw
		EVILTURNIP(6834, 12051, 42, 3000, RANGE),//evil turnip
		SPIRIT_COCKA(6875, 12095, 43, 3600, MAGIC),//spirit cockatrice
		SPIRIT_SARAT(6879, 12099, 43, 3600, MAGIC),//spirit saratrice
		SPIRIT_GUATRICE(6877, 12097, 43, 3600, MAGIC),//spirit gutatrice
		SPIRIT_ZAMA(6881, 12101, 43, 3600, MAGIC),//spirit zamatrice
		SPIRIT_PENGA(6883, 12103, 43, 3600, MAGIC),//spirit pengatrice
		SPIRIT_CORXA(6885, 12105, 43, 3600, MAGIC),//spirit coraxatrice
		SPIRIT_VULA(6887, 12107, 43, 3600, MAGIC),//spirit vulatrice
		PYRELORD(7377, 12816, 46, 3200, STRENGTH),//Pyrelord
		IRONMINO(6855, 12075, 46, 3700, DEFENCE),//iron minotaur
		MAGPIE(6824, 12041, 47, 3400, NOTHING),//magpie
		BLOATEDLEECH(6843, 12061, 49, 3400, ATTACK),//bloated leech
		SPIRITTERRORBIRD(6794, 12007, 52, 3600, MELEE),//spirit terrorbird
		ABYSSALPARA(6819, 12035, 54, 3000, MAGIC),//abyssal parasite
		SPIRITJELLY(6992, 12027, 55, 4300, STRENGTH),//spirit jelly
		STEELMINOT(6857, 12077, 56, 3800, DEFENCE),//steel minotaur
		IBIS(6991, 12531, 56, 4600, NOTHING),//ibis
		GRAAHK(7363, 12810, 57, 4900, STRENGTH),//spirit graahk
		KYAY(7365, 12812, 57, 4900, ATTACK),//Spirit kyat
		LARUPIA(7337, 12784, 57, 4900, MELEE),//spirit larupia
		KARMTHLU(6809, 12023, 58, 4400, RANGE),//karmthulu overlord
		SMOKEDEVIL(6866, 12085, 61, 4800, MAGIC),//smoke devil
		LURKER(6821, 12037, 62, 4100, MELEE),//abyssal lurker
		COBRA(6802, 12015, 63, 5600, ATTACK),//spirit cobra
		PLANT(6827, 12045, 64, 4900, MELEE),//stranger plant
		MITHMINO(6859, 12079, 66, 5500, DEFENCE),//mithril minotaur
		BARKERTOAD(6889, 12123, 66, 800, STRENGTH),//barker toad
		WARTORTISE(6815, 12031, 67, 4300, DEFENCE),//war tortoise
		BUNYIP(6813, 12029, 68, 4400, ATTACK),//bunyip
		FRUITBAT(6817, 12033, 69, 2400, NOTHING),//fruit bat
		LOCUSTS(7373, 12820, 70, 2800, ATTACK),//ravenous locust
		ARTICBEAR(6839, 12057, 71, 3000, MELEE),//arctic bear
		PHOENIX(8538, 14623, 72, 5500, MAGIC),//phoenix
		GOLEMOBSD(9485, 12792, 73, 4700, STRENGTH),//obsidian golem
		GRANITELOBY(6850, 12069, 74, 6900, DEFENCE),//granite lobster
		PRAYINGMANTIS(6798, 12011, 75, 6900, ATTACK),//praying mantis
		ADDYMINO(6861, 12081, 76, 6600, DEFENCE),//adamant minotaur
		FORGEREGENT(7335, 12782, 76, 4500, RANGE),//forge regent
		TALONBEAST(7347, 12794, 77, 4900, STRENGTH),//talon beast
		GIANTENT(6800, 12013, 78, 4900, MELEE),//giant ent
		FIRETITAN(7355, 12802, 79, 6200, MAGIC),//fire titan
		ICETITAN(7357, 12804, 79, 6400, ATTACK),//ice titan
		MOSSTITAN(7359, 12806, 79, 5800, STRENGTH),//moss titan
		HYDRA(6811, 12025, 80, 4900, RANGE),//hydra
		DAGANNOTH(6804, 12017, 83, 5700, MELEE),//spirit dagannoth
		LAVATITAN(7341, 12788, 83, 6100, STRENGTH),//lava titan
		SWAMPTITAN(7329, 12776, 85, 5600, ATTACK),//swamp titan
		RUNEMINO(6863, 12083, 86, 15100, DEFENCE),//rune minotaur
		UNICORNSTALLION(6823, 12039, 88, 5400, MELEE),//unicorn stallion
		GEYSERTITAN(7339, 12786, 89, 6900, RANGE),//geyser titan
		WOLPERTINGER(6869, 12089, 92, 6200, MAGIC),//wolpertinger
		ABBYTITAN(7349, 12796, 93, 3200, ATTACK),//abyssal titan
		IRONTITAN(7375, 12822, 95, 6000, DEFENCE),//iron titan
		PACKYAK(6873, 12093, 96, 5800, STRENGTH),//pack yak
		STEELTITAN(7343, 12790, 99, 6400, RANGE),//Steel titan
        REV_KNIGHT(6692, 17989, 99, (int)Time.TEN_MINUTES, MAGIC),
        REV_BEAST(6691, 17988, 90,(int) Time.TEN_MINUTES, MAGIC),
        REV_ORK(6690, 17987, 80, (int)Time.TEN_MINUTES, MAGIC),
        REV_DEMON(6689, 17986, 75, (int)Time.TEN_MINUTES, MAGIC),
        REV_HELLHOUND(6688, 17985, 70, (int)Time.TEN_MINUTES, MAGIC)
        ; //rev knight

		/* Npc id, Pouch id, Name, Level required, Time living */
		SummonType(int npcId, int pouchId, int level, int timeLiving, int skillId) {
			this.npcId = npcId;
			this.pouchId = pouchId;
			this.level = level;
			this.timeLiving = timeLiving;
			this.skillId = skillId;
		}

		public int npcId, pouchId, level, timeLiving, skillId;

	}

	;

	private static Map<Integer, SummonType> summonsNpcId = new HashMap<Integer, SummonType>();
	private static Map<Integer, SummonType> summonsPouchId = new HashMap<Integer, SummonType>();

	static {
		for(SummonType summonType : SummonType.values()) {
			summonsNpcId.put(summonType.npcId, summonType);
			summonsPouchId.put(summonType.pouchId, summonType);
		}
	}

	public static int getTimerById(int npcId) {
		if(summonsNpcId.get(npcId) == null)
			return 0;
		return summonsNpcId.get(npcId).timeLiving;
	}

    public static int getPouchByNpc(int npcId) {
        if(summonsNpcId.get(npcId) == null)
            return 0;
        return summonsNpcId.get(npcId).pouchId;
    }

	public static int getNpcbyPouchId(int pouchId) {
		if(summonsPouchId.get(pouchId) == null)
			return 0;
		return summonsPouchId.get(pouchId).npcId;//+1 is u can attack in wild
	}

	public static int getRequirementForNpcId(int npcId) {
		if(summonsNpcId.get(npcId) == null)
			return 0;
		return summonsNpcId.get(npcId).level;
	}


}