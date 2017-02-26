package org.hyperion.rs2.model;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.hyperion.Configuration;
import org.hyperion.data.Persistable;
import org.hyperion.engine.task.Task;
import org.hyperion.engine.task.impl.PlayerDeathTask;
import org.hyperion.engine.task.impl.VoteBonusEndTask;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.action.ActionQueue;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.achievements.AchievementData;
import org.hyperion.rs2.model.achievements.Difficulty;
import org.hyperion.rs2.model.cluescroll.ClueScrollManager;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.LastAttacker;
import org.hyperion.rs2.model.combat.npclogs.NPCKillsLogger;
import org.hyperion.rs2.model.combat.pvp.PvPArmourStorage;
import org.hyperion.rs2.model.container.*;
import org.hyperion.rs2.model.container.bank.Bank;
import org.hyperion.rs2.model.container.bank.BankField;
import org.hyperion.rs2.model.container.bank.BankItem;
import org.hyperion.rs2.model.container.duel.Duel;
import org.hyperion.rs2.model.container.impl.TabbedContainer;
import org.hyperion.rs2.model.content.ContentEntity;
import org.hyperion.rs2.model.content.Lock;
import org.hyperion.rs2.model.content.bounty.BountyHunter;
import org.hyperion.rs2.model.content.bounty.BountyPerks;
import org.hyperion.rs2.model.content.clan.Clan;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.content.ge.GrandExchange;
import org.hyperion.rs2.model.content.jge.tracker.JGrandExchangeTracker;
import org.hyperion.rs2.model.content.minigame.DangerousPK.ArmourClass;
import org.hyperion.rs2.model.content.minigame.barrowsffa.BarrowsFFAHolder;
import org.hyperion.rs2.model.content.misc.*;
import org.hyperion.rs2.model.content.misc2.Dicing;
import org.hyperion.rs2.model.content.misc2.RunePouch;
import org.hyperion.rs2.model.content.misc2.SpawnTab;
import org.hyperion.rs2.model.content.misc2.teamboss.TeamBossSession;
import org.hyperion.rs2.model.content.pvptasks.PvPTask;
import org.hyperion.rs2.model.content.skill.Farming;
import org.hyperion.rs2.model.content.skill.Farming.Farm;
import org.hyperion.rs2.model.content.skill.Prayer;
import org.hyperion.rs2.model.content.skill.RandomEvent;
import org.hyperion.rs2.model.content.skill.agility.Agility;
import org.hyperion.rs2.model.content.skill.dungoneering.DungeoneeringHolder;
import org.hyperion.rs2.model.content.skill.slayer.SlayerHolder;
import org.hyperion.rs2.model.content.ticket.TicketHolder;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.model.joshyachievementsv2.tracker.AchievementTracker;
import org.hyperion.rs2.model.recolor.RecolorManager;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.model.sets.CustomSetHolder;
import org.hyperion.rs2.model.shops.LegendaryStore;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.net.ISAACCipher;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.packet.NpcClickHandler;
import org.hyperion.rs2.packet.ObjectClickHandler;
import org.hyperion.rs2.util.AccountLogger;
import org.hyperion.rs2.util.AccountValue;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.rs2.util.TextUtils;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a player-controller character.
 *
 * @author Graham Edgecombe
 */
public class Player extends Entity implements Persistable, Cloneable {

	/**
	 * STRINGS
	 */
	public String lastIp;
	public String display;
	public String bankPin = "";
	public String enterPin = "";
	public String lastSearch = "";
	private String name;
	private String IP;
	private String clanName = "";
	private String[] lastKills = {"", "", "", "", ""};
	private String password;
	private String googleAuthenticatorKey;
	private List<String> googleAuthenticatorBackup;

	/**
	 * INTEGERS
	 */
	private final int uid;
	public int turkeyKills;
	public int EP = 0;
	public int blackMarks = 0;
	public int maxCapePrimaryColor = 0;
	public int maxCapeSecondaryColor = 0;
	public int compCapePrimaryColor;
	public int compCapeSecondaryColor;
	public int tutorialProgress = 0;
	public int duelAttackable = 0;
	public int duelRuleOption = 0;
	public int[] skillRecoverTimer = new int[Skills.SKILL_COUNT];
	public int RFDLevel = 0;
	public int WGLevel = 0;
	public int rangeMiniShots = -1;
	public int rangeMiniScore = 0;
	public int fightCavesWave = 0;
	public int fightCavesKills = 0;
	public int SummoningCounter = 0;
	public int[] checkersRecord;
	public int smithingMenu = -1;
	public int[] delayObjectClick = new int[4];// id,x,y,type
	public int wildernessLevel = -1;
	public int headIconId = -1;
	public int[] specialUid;
	public int[] chatStatus = new int[3];
	public int forceWalkX1;
	public int forceWalkY1;
	public int forceWalkX2;
	public int forceWalkY2;
	public int forceSpeed1;
	public int forceSpeed2;
	public int forceDirection;
	public int slayerTask = 0;
	public int[] pinOrder = new int[10];
	public int skillMenuId = 0;
	public int[] godWarsKillCount = new int[4];
	public int[] itemKeptId = new int[4];
	public int slayerAm = 0;
	public int clueStage = 8;
	public int slayerCooldown = 0;
	public int tutIsland = 10;
	public int tutSubIsland = 0;
	private int pid = -1;
	private int treasureScroll;
	private int gameMode = 0;
	private int diced = 0;
	private int skullTimer = 0;
	private int shopId = -1;
	private int npcId = -1;
	private int fightPitsDamage;
	private int damagedCorp;
	private int pvpTaskAmount;
	private int clanRank = 0;
	private int playerUptime = 0;
	private int killStreak = 0;
	private int bounty = 10;
	private int killCount = 0;
	private int deathCount = 0;
	private int voteStreak = 0;
	private int todayVotes = 0;
	private int lastMac;

	/**
	 * LONG
	 */
	private final long logintime = System.currentTimeMillis();
	public long lastTicketRequest;
	public long foodTimer = System.currentTimeMillis();
	public long comboFoodTimer = System.currentTimeMillis();
	public long specPotionTimer = 0;
	public long chargeTill;
	public long teleBlockTimer = System.currentTimeMillis() - 3600000;
	public long LastTimeLeeched;
	public long lastTimeSoulSplit;
	public long potionTimer = 0;
	public long contentTimer = 0;
	public long lastVeng = 0;
	public long antiFireTimer = 0;
	public long overloadTimer = 0;
	private long previousSessionTime = System.currentTimeMillis();
	private long lastHonorPointsReward = System.currentTimeMillis();
	private long created;
	private long disconnectedTimer = System.currentTimeMillis();
	private long lastVoteStreakIncrease = 0L;
	private long lastEPIncrease = System.currentTimeMillis();
	private long nameLong;
	private long playerRank = 1;
	private long lastDuelUpdate = 0L;
	private long dragonFireSpec = 0L;
	private long lastTeleport = System.currentTimeMillis();
	private List<Long> ignores = new ArrayList<>(100);
	private long locks = 0;
	private long voteBonusEndTime = 0;
	private long lastVoteBonus = 0L;
	/**
	 * DOUBLES
	 */
	public double prayerDrain = 0;
	private double drainRate;

	/**
	 * BYTES
	 */
	public byte currentInterfaceStatus = 0;

	/**
	 * BOOLEANS
	 */
	public boolean cleaned = false;
	public boolean loggedOut = false;
	public boolean forcedLogout = false;
	public boolean showEP = true;
	public boolean active = false;
	public boolean hasBeenInformed;
	public boolean receivedStarter = true;
	public boolean tradeAccept1 = false;
	public boolean tradeAccept2 = false;
	public boolean onConfirmScreen = false;
	public boolean openingTrade = false;
	public boolean duelRule[] = new boolean[24];
	public boolean banEquip[] = new boolean[14];
	public boolean inGame;
	public boolean attackOption = false;
	public boolean duelOption = false;
	public boolean splitPriv = true;
	public boolean specOn = false;
	public boolean isMoving = false;
	public boolean autoRetailate = true;
	public boolean joiningPits = false;
	public boolean isInMuli = false;
	public boolean cannotSwitch = false;
	private boolean newCharacter = false;
	public boolean ignoreOnLogin = false;
	public boolean oldFag = false;
	public boolean inAction;
	public boolean debug;
	public boolean[] invSlot = new boolean[28];
	public boolean[] equipSlot = new boolean[14];
	public boolean isMuted = false;
	public boolean forcedIntoSkilling = false;
	public boolean closeChatInterface = false;
	public boolean vengeance = false;
	public boolean yellMuted = false;
	public boolean resetingPin = false;
	public boolean isOverloaded;
	public boolean superAntiFire = false;
	public boolean openedBoB = false;
	private boolean doublechar = false;
	private boolean needsNamechange = false;
	private boolean isBanking;
	private boolean hasMaxCape = false;
	private boolean hasCompCape = false;
	private boolean canSpawnSet = true;
	private boolean hasTarget = false;
	private boolean members = true;
	private boolean isPlayerBusy = false;
	private boolean isSkilling = false;
	private boolean canWalk = false;
	private boolean npcState = false;
	private boolean isDoingEmote = false;

	/**
	 * OBJECTS
	 */
	private final InterfaceManager interfaceManager = new InterfaceManager(this);
	private final GrandExchange grandExchange = new GrandExchange(this);
	private final BarrowsFFAHolder barrowsFFA = new BarrowsFFAHolder();
	private final TicketHolder ticketHolder = new TicketHolder();
	private final AchievementTracker achievementTracker = new AchievementTracker(this);
	private final RandomEvent randomEvent = new RandomEvent(this);
	private final DungeoneeringHolder dungeoneeringHolder = new DungeoneeringHolder();
	private final ExtraData permExtraData = new ExtraData();
	private final CustomSetHolder customSetHolder = new CustomSetHolder(this);
	private final RecolorManager recolorManager = new RecolorManager(this);
	private final List<TeamBossSession> teamBossSessions = new ArrayList<>();
	private final IoSession session;
	private final ISAACCipher inCipher;
	private final ISAACCipher outCipher;
	private final ActionSender actionSender = new ActionSender(this);
	private final Queue<ChatMessage> chatMessages = new LinkedList<>();
	private final ActionQueue actionQueue = new ActionQueue();
	private final InterfaceState interfaceState = new InterfaceState(this);
	private final Queue<Packet> pendingPackets = new LinkedList<>();
	private final RequestManager requestManager = new RequestManager(this);
	private final BountyHunter bountyHunter = new BountyHunter(this);
	private final BountyPerks bhperks = new BountyPerks();
	private final SlayerHolder slayTask = new SlayerHolder();
	private final Container equipment = new Container(Container.Type.STANDARD, Equipment.SIZE);
	private final Skills skills = new Skills(this);
	private final Container inventory = new Container(Container.Type.STANDARD, Inventory.SIZE);
	private final Container trade = new Container(Container.Type.STANDARD, Trade.SIZE);
	private final Container duel = new Container(Container.Type.STANDARD, Duel.SIZE);
	private final Container runePouch = new Container(Container.Type.ALWAYS_STACK, RunePouch.SIZE);
	private final TabbedContainer bank = new TabbedContainer(Container.Type.ALWAYS_STACK, Bank.SIZE, this);
	private final Settings settings = new Settings();
	public final NPCKillsLogger npckillLogger = new NPCKillsLogger();
	public Player duelWith2 = null;
	public Player onModeration = null;
	public Player challengedBy = null;
	public Player beingFollowed = null;
	public Player isFollowing = null;
	public Player tradeWith2 = null;
	private Player dungeoneeringLeader = null;
	private List<Player> dungeoneeringLobbyTeam = new ArrayList<>();
	public ArmourClass pickedClass = null;
	private Agility agility = new Agility(this);
	private PlayerChecker playerChecker = PlayerChecker.create();
	private HashMap<AchievementData, Integer> achievementsProgress = new HashMap<>();
	private Difficulty viewingDifficulty = Difficulty.VERY_EASY;
	private AccountValue accountValue = new AccountValue(this);
	private AccountLogger logger = new AccountLogger(this);
	private PlayerPoints playerPoints = new PlayerPoints(this);
	private ExpectedValues expectedValues = new ExpectedValues(this);
	private Spam spam = new Spam(this);
	private SpecialBar specbar = new SpecialBar(this);
	private SummoningBar summoningBar = new SummoningBar(this);
	private Yelling yelling = new Yelling();
	private ExtraData extraData = new ExtraData();
	private QuestTab questtab = new QuestTab(this);
	private SummoningTab summoningTab = new SummoningTab(this);
	private SpawnTab spawntab = new SpawnTab(this);
	private AchievementTab achievementtab = new AchievementTab(this);
	private ItemDropping itemDropping = new ItemDropping();
	private TriviaSettings ts = new TriviaSettings(0);
	private Mail mail = new Mail(this);
	private SkillingData sd = new SkillingData();
	private ChatMessage currentChatMessage;
	private AtomicInteger overloadCounter = new AtomicInteger(0);
	private PvPArmourStorage pvpStorage = new PvPArmourStorage();
	private LastAttacker lastAttacker;
	private EquipmentStats bonus = new EquipmentStats();
	private Player tradeWith = null;
	private Prayers prayers = new Prayers(true);
	private Appearance appearance = new Appearance();
	private Container bob;
	private Packet cachedUpdateBlock;
	private SpellBook spellBook = new SpellBook(SpellBook.DEFAULT_SPELLBOOK);
	private FriendList friendList = new FriendList();
	private PvPTask currentPvPTask;
	private Task currentTask;
	private Farm farm = Farming.getFarming().new Farm();
	private AutoSaving autoSaving = new AutoSaving(this);
	private BankField bankField = new BankField(this);
	private Highscores highscores;
	private JGrandExchangeTracker geTracker;
	private Map<String, Long> savedIps = new HashMap<>();

	public Player(PlayerDetails details) {
		this.session = details.getSession();
		this.inCipher = details.getInCipher();
		this.outCipher = details.getOutCipher();
		this.name = details.getName().toLowerCase();
		this.specialUid = details.getSpecialUid();
		this.display = details.getName();
		this.nameLong = NameUtils.nameToLong(this.name);
		this.password = details.getPassword();
		this.uid = details.getMacAddress();
		this.IP = details.getIpAddress();
		this.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		this.setTeleporting(true);
		this.resetPrayers();
		lastAttacker = new LastAttacker(name);
	}

	public Player(int uid) {
		this.inCipher = null;
		this.outCipher = null;
		this.session = null;
		this.uid = uid;
		lastAttacker = new LastAttacker(name);
	}

	public long getLogintime() {
		return logintime;
	}

	public static void resetCorpDamage() {
		for (Player p : World.getPlayers()) {
			if (p == null)
				continue;
			if (p.getCorpDamage() > 0)
				p.setCorpDamage(0);
		}
	}

	private static String getPeopleString() {
		String ppl = " ";
		switch (Misc.random(100)) {
			case 0:
				ppl += "idiots";
				break;
			case 1:
				ppl += "narbs";
				break;
			case 2:
				ppl += "shits";
				break;
			case 3:
				ppl += "enemies";
				break;
			case 4:
				ppl += "noobs";
				break;
			case 5:
				ppl += "chickens";
				break;
			case 6:
				ppl += "fleshbags";
				break;
			default:
				ppl += "people";
				break;
		}
		return ppl;
	}

	public boolean doubleChar() {
		return doublechar;
	}

	public void setDoubleChar(boolean b) {
		System.out.println("Double char case!");
		doublechar = b;
	}

	public boolean needsNameChange() {
		return needsNamechange;
	}

	public void setNeedsNameChange(boolean b) {
		needsNamechange = b;
	}

	@Override
	public Object clone() throws CloneNotSupportedException{
		Player clone = (Player)super.clone();
		return clone;
	}

	/*
	 * Player NPC
	 */

	public final InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public GrandExchange getGrandExchange() {return grandExchange;}

	public BarrowsFFAHolder getBarrowsFFA() { return barrowsFFA; }

	public int getTutorialProgress() {
		return tutorialProgress;
	}

	public void setTutorialProgress(int step) {
		tutorialProgress = step;
	}

	public final TicketHolder getTicketHolder() {
		return ticketHolder;
	}

	public boolean isBanking() {
		return isBanking;
	}

	public void setBanking(boolean status) {
		isBanking = status;
	}

	public NPCKillsLogger getNPCLogs() {
		return npckillLogger;
	}

	public boolean isPidSet(){
		return pid != -1;
	}

	public int getPid(){
		return pid;
	}

	/*
	 * Attributes.
	 */

	public void setPid(final int pid){
		this.pid = pid;
	}

	public boolean hardMode() {
		return gameMode == 1;
	}

	public int getGameMode() {
		return gameMode;
	}

	public void setGameMode(int mode) {
		this.gameMode = mode;
	}

	public void setMaxCape(boolean b) {
		hasMaxCape = b;
	}

	public boolean hasMaxCape() {
		return hasMaxCape;
	}

	public void setCompCape(boolean b) {
		hasCompCape = b;
	}

	public boolean hasCompCape() {
		return hasCompCape;
	}

	public Agility getAgility() {
		return agility;
	}

	public PlayerChecker getChecking() {
		return playerChecker;
	}

	public HashMap<AchievementData, Integer> getAchievementsProgress() {
		return achievementsProgress;
	}

	public AchievementTracker getAchievementTracker(){
		return achievementTracker;
	}

	public RandomEvent getRandomEvent() {
		return randomEvent;
	}

	public Difficulty getViewingDifficulty() {
		return viewingDifficulty;
	}

	public void setViewingDifficulty(Difficulty viewingDifficulty) {
		this.viewingDifficulty = viewingDifficulty;
	}

	public final List<Integer> requiredSkills = Arrays.asList(7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22/*, 23*/, 24);

	public boolean checkMaxCapeRequirment() {
		return requiredSkills.stream().allMatch(value -> getSkills().getLevels()[value] >= 99 && getPoints().getEloPeak() >= 1900);
	}

	public boolean checkCompCapeReq() {
		if (!checkMaxCapeRequirment()) {
			return false;
		}
		for (int array : requiredSkills) {
			if (array != 25) {
				if (getSkills().getExps()[array] < 200000000) {
					return hasCompCape;
				}
			}
		}
		if (getPoints().getEloPeak() < 2200) {
			return hasCompCape;
		}
		return true;
	}

	public void checkCapes() {
		checkContainers(18509, skills.getRealLevels()[Skills.DUNGEONEERING]== 99, "Dungeoneering cape");
		checkContainers(19709, skills.getExperience(Skills.DUNGEONEERING) == Skills.MAXIMUM_EXP, "Dungeoneering master cape");
		checkContainers(12744, checkMaxCapeRequirment(), "Max cape");
		//checkContainers(12747, checkCompCapeReq(), "Completionist cape");
	}

	public void checkSacredClay() {
		Item.SACRED_CLAY.forEach(value -> checkContainers(value, false, String.format("Sacred Clay (Class %d)", value)));
	}

	private void checkContainers(final int id, final boolean add, String name) {
		final Container[] containers = new Container[]{bank, equipment, inventory};
		boolean contains = false;
		for(final Container container : containers) {
			if(container.contains(id)) {
				contains = true;
				if(!add) {
					container.remove(Item.create(id, container.getCount(id)));
				}
			}
		}

		if(!contains && add) {
			for (Player p : World.getPlayers()) {
				p.sendLootMessage("Achievement", getSafeDisplayName() + " has just achieved " + name + "!");
			}
			bank.add(new BankItem(0, id, 1));
		}
	}

	/**
	 * Gets the KDR value rounded to 3 decimals.
	 *
	 * @return
	 */
	public double getKDR() {
		double kdr = getKillCount();
		if(getDeathCount() != 0) {
			kdr = (double) getKillCount() / (double) getDeathCount();
		}
		kdr = Misc.round(kdr, 3);
		return kdr;
	}

	public long getLastHonorPointsReward() {
		return lastHonorPointsReward;
	}

	public void setLastHonorPointsReward(long time) {
		lastHonorPointsReward = time;
	}

	public long getPreviousSessionTime() {
		return previousSessionTime;
	}

	public void setPreviousSessionTime(long time) {
		previousSessionTime = time;
	}

	public DungeoneeringHolder getDungeoneering() {
		return dungeoneeringHolder;
	}

	public CustomSetHolder getCustomSetHolder() {
		return customSetHolder;
	}

	public TriviaSettings getTrivia() {
		return ts;
	}

	public RecolorManager getRecolorManager(){
		return recolorManager;
	}

	public SummoningBar getSummBar() {
		return summoningBar;
	}

	public Mail getMail() {
		return mail;
	}

	public LastAttacker getLastAttack() {
		return lastAttacker;
	}

	public SkillingData getSkillingData() {
		return sd;
	}

	public ItemDropping getDropping() {
		return itemDropping;
	}

	public QuestTab getQuestTab() {
		return questtab;
	}

	public SummoningTab getSummoningTab() { return summoningTab; }

	public SpawnTab getSpawnTab() {
		return spawntab;
	}

	public AchievementTab getAchievementTab() {
		return achievementtab;
	}

	//public News getNews() {
	//return news;
	//}

	public ExtraData getExtraData() {
		return extraData;
	}

	public ExtraData getPermExtraData() {
		return permExtraData;
	}

	public Yelling getYelling() {
		return yelling;
	}

	public SpecialBar getSpecBar() {
		return specbar;
	}

	public Spam getSpam() {
		return spam;
	}

	public PlayerPoints getPoints() {
		return playerPoints;
	}

	public ExpectedValues getExpectedValues() {
		return expectedValues;
	}

	public AccountLogger getLogging() {
		return logger;
	}

	public AccountValue getAccountValue() {
		return accountValue;
	}

	public void setCanSpawnSet(boolean b) {
		this.canSpawnSet = b;
	}

	public boolean canSpawnSet() {
		return canSpawnSet;
	}

	public void init() {


		try {
			File f = new File("./data/charfarm/"+getName()+".bin");
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
			Farming.deserialize(buf, this);
		}catch(final Exception ex) {

		}


	}

	public void serialize() {
		try {
			OutputStream os = new FileOutputStream("data/charfarm/"+this.getName()+".bin");
			IoBuffer buf = IoBuffer.allocate(1024);
			buf.setAutoExpand(true);
			Farming.serialize(buf,this);
			buf.flip();
			byte[] data = new byte[buf.limit()];
			buf.get(data);
			os.write(data);
			os.flush();
			os.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasTarget() {
		return hasTarget;
	}

	public void setHasTarget(boolean b) {
		hasTarget = b;
	}

	public boolean isServerOwner() {
		return getName().equalsIgnoreCase(Configuration.getString(Configuration.ConfigurationObject.OWNER));
	}

	public long getCreatedTime() {
		return created;
	}

	public void setCreatedTime(long created) {
		this.created = created;
	}

	/**
	 * Gets the carried risk value of the player, the value of
	 * the Inventory + Equipment which is expressed in Pk Points
	 * where 1 Donator Point = 1000 Pk Points.
	 *
	 * @return
	 */
	public int getRisk() {
		int totalvalue = 0;
		List<Item> list = new ArrayList<Item>(Equipment.SIZE + Inventory.SIZE);
		for(Item item : getInventory().toArray()) {
			list.add(item);
		}
		for(Item item : getEquipment().toArray()) {
			list.add(item);
		}
		for(Item item : list) {
			if(item == null)
				continue;
			if(ItemSpawning.allowedMessage(item.getId()).length() > 0) {
				int value = ShopManager.getPoints(63, item.getId());
				if(value != 50000 && value != 0)
					totalvalue += value * 1000 * item.getCount();
				else {
					value = ShopManager.getPoints(75, item.getId());
					if(value != 50000 && value != 0) {
						totalvalue += value * 1000 * item.getCount();
					} else {
						value = ShopManager.getPoints(71, item.getId());
						if(value != 50000 && value != 0) {
							totalvalue += value * item.getCount();
						}
					}
				}

			}
		}
		//List<Item> keepItems = DeathDrops.itemsKeptOnDeath(this);
		/*for(Item item: keepItems) {
			if(item == null)
				continue;
			if(ItemSpawning.allowedMessage(item.getId()).length() > 0) {
				int value = ShopManager.getPoints(63, item.getId());
				if(value != 50000 && value != 0)
					totalvalue -= value*1000*item.getCount();
				else {
					value = ShopManager.getPoints(75, item.getId());
					if(value != 50000 && value != 0) {
						totalvalue -= value*1000*item.getCount();
					} else {
						value = ShopManager.getPoints(71, item.getId());
						if(value != 50000 && value != 0) {
							totalvalue -= value * item.getCount();
						}
					}
				}
			}
		}*/
		return totalvalue;
	}

	public boolean isDisconnected() {
		return System.currentTimeMillis() - disconnectedTimer > 15000;
	}

	public void updateDisconnectedTimer() {
		disconnectedTimer = System.currentTimeMillis();
	}

	/**
	 * Used to see the duration of the player's session.
	 *
	 * @returns for how long the player was online in milliseconds.
	 */
	public long getCurrentSessionTime() {
		return System.currentTimeMillis() - logintime;
	}

	/**
	 * Sets the player's password.
	 *
	 */


	public final List<TeamBossSession> getTeamSessions() {
		return teamBossSessions;
	}

	public int getDiced() {
		return diced;
	}

	public void setDiced(int diced) {
		this.diced = diced;
	}

	public boolean isSkulled() {
		return skullTimer > 0;
	}

	public void setSkulled(boolean skulled) {
		if(! isSkulled() && skulled) {
			Prayer.setHeadIcon(this);
		}
		if(skulled) {
			skullTimer = 1200;
		} else {
			Prayer.setHeadIcon(this);
			skullTimer = 0;
		}
	}

	public void decreaseSkullTimer() {
		if(skullTimer > 0) {
			skullTimer--;
			if(skullTimer == 0) {
				setSkulled(false);
			}
		}

	}

	public int getSkullTimer() {
		return skullTimer;
	}

	public void setSkullTimer(int timer) {
		skullTimer = timer;
	}

	public long getLastVoteStreakIncrease() {
		return lastVoteStreakIncrease;
	}

	public void setLastVoteStreakIncrease(long time) {
		lastVoteStreakIncrease = time;
	}

	public long getLastEPIncrease() {
		return lastEPIncrease;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void increaseEP() {
		if (getLocation().equals(Locations.Location.FUN_PK_AREA)) {
			return;
		}
		if(EP == 100 || getPosition().getZ() != 0)
			return;
		int addEP = Misc.random(15) + 15;
		if(EP + addEP > 100)
			EP = 100;
		else
			EP += addEP;
		sendPkMessage("Your earn potention has increased by " + addEP +"!");
		if(wildernessLevel > 0)
			getActionSender().sendWildLevel(wildernessLevel);
		else
			getActionSender().sendPvPLevel(false);
		lastEPIncrease = System.currentTimeMillis();
	}

	public void removeEP() {
		EP = (int) (EP * Math.random() / 2);
		if(wildernessLevel > 0)
			getActionSender().sendWildLevel(wildernessLevel);
		else
			getActionSender().sendPvPLevel(false);
	}

	public void resetOverloadCounter() {
		overloadCounter.set(0);
	}

	public AtomicInteger getOverloadCounter() {
		return overloadCounter;
	}

	public PvPArmourStorage getPvPStorage() {
		return pvpStorage;
	}

	public BountyHunter getBountyHunter() {
		return bountyHunter;
	}

	public BountyPerks getBHPerks() {
		return bhperks;
	}

	public void refreshDuelTimer() {
		lastDuelUpdate = System.currentTimeMillis();
	}

	public boolean hasDuelTimer() {
		return System.currentTimeMillis() - lastDuelUpdate < 5000;
	}

	public void resetDFS() {
		dragonFireSpec = System.currentTimeMillis();
	}

	public boolean canDFS() {
		return (System.currentTimeMillis() - dragonFireSpec) > 160000; //160 secs, 2:30
	}

	public long lastTickReq() {
		return lastTicketRequest;
	}

	public final SlayerHolder getSlayer() {
		return slayTask;
	}

	public void refreshTickReq() {
		this.lastTicketRequest = System.currentTimeMillis();
	}

	/**
	 * Getters/Setters for person ready to be moderated
	 */
	public Player getModeration() {
		return onModeration;
	}

	public void setModeration(Player p) {
		this.onModeration = p;
	}

	public boolean isBusy() {
		return isPlayerBusy;
	}

	public void setBusy(boolean b) {
		isPlayerBusy = b;
	}

	public boolean isSkilling() {
		return isSkilling;
	}

	public void setSkilling(boolean b) {
		isSkilling = b;
	}

	public boolean canWalk() {
		return canWalk;
	}

	public void setCanWalk(boolean b) {
		canWalk = b;
	}

	public boolean handleClickNow() {
		if(delayObjectClick[3] > 0) {
			int i = 0;
			while(delayObjectClick[3] >= 4) {
				delayObjectClick[3] -= 4;
				i++;
			}
			if(i == 0) {// objects
				ObjectClickHandler.clickObject(this, delayObjectClick[0], delayObjectClick[1], delayObjectClick[2], delayObjectClick[3]);
			} else if(i == 1) {// npcs
				NpcClickHandler.handle(this, delayObjectClick[3], delayObjectClick[0]);
			}
			delayObjectClick[3] = - 1;
			return true;
		}
		return false;
	}

	public void setPNpc(int id) {
		this.npcId = id;
		this.npcState = id > -1;
		getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	public boolean getNpcState() {
		return npcState;
	}

	public int getNpcId() {
		return npcId;
	}

	public Prayers getPrayers() {
		return prayers;
	}

	public double getDrainRate() {
		return drainRate;
	}

	public double setDrainRate(double i) {
		return drainRate = i;
	}

	public void resetDrainRate() {
		drainRate = 0;
	}

	public void resetPrayers() {
		getPrayers().clear();
		resetDrainRate();
		/*
		 * for(int a = 83; a < 108; a++) getActionSender().sendClientConfig(a,
		 * 0);
		 */
		/*
		 * for(int a = 601; a < 609; a++) getActionSender().sendClientConfig(a,
		 * 0);
		 */
		Prayer.resetInterface(this);
		Prayer.setHeadIcon(this);
		// reset headicon;
		// clears arraylist
	}

	public Container getRunePouch() {
		return runePouch;
	}

	public String getFullIP() {
		return IP;
	}

	public String getShortIP() {
		return TextUtils.shortIp(IP);
	}

	public void setIP(String IP) {
		this.IP = IP;
	}

	public boolean isNew() {
		return newCharacter;
	}

	public void setNew(boolean isNew) {
		newCharacter = isNew;
	}

	public boolean isNewlyCreated() {
		return getTotalOnlineTime() < Time.FIVE_MINUTES * 3;
	}
	// friends, 2 off

	public long getTotalOnlineTime() {
		return getPermExtraData().getLong("logintime") + (System.currentTimeMillis() - logintime);
	}

	/**
	 * Gets the request manager.
	 *
	 * @return The request manager.
	 */
	public RequestManager getRequestManager() {
		return requestManager;
	}

	/**
	 * Gets the player's name expressed as a long.
	 *
	 * @return The player's name expressed as a long.
	 */
	public long getNameAsLong() {
		return nameLong;
	}

	/**
	 * Gets the player's settings.
	 *
	 * @return The player's settings.
	 */
	public Settings getSettings() {
		return settings;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int a) {
		shopId = a;
	}

	public Player getTrader() {
		return tradeWith;
	}

	public Player getDungeoneeringLeader() {
		return dungeoneeringLeader;
	}

	public void setDungeoneeringLeader(final Player value) {
		this.dungeoneeringLeader = value;
	}

	public List<Player> getDungeoneeringLobbyTeam() {
		return dungeoneeringLobbyTeam;
	}

	public void setTradeWith(Player p) {
		tradeWith = p;
	}

	/**
	 * Writes a packet to the <code>IoSession</code>. If the player is not yet
	 * active, the packets are queued.
	 *
	 * @param packet The packet.
	 */
	public void write(Packet packet) {
		synchronized(this) {
			if(! active) {
				pendingPackets.add(packet);
			} else {
				for(Packet pendingPacket : pendingPackets) {
					session.write(pendingPacket);
				}
				pendingPackets.clear();
				getExtraData().put("packetsWrite", getExtraData().getInt("packetsWrite")+1);
				session.write(packet);
			}
		}
	}

	public int getPendingPacketsCount(){
		return pendingPackets.size();
	}

	/**
	 * Gets the player's bank.
	 *
	 * @return The player's bank.
	 */
	public Container getBank() {
		return bank;
	}

	/**
	 * Gets the player's BoB.
	 *
	 * @return The player's BoB.
	 */
	public Container getBoB() {
		return bob;
	}

	public void setBob(int size) {
		bob = new Container(Container.Type.STANDARD, size);
	}

	/**
	 * Gets the interface state.
	 *
	 * @return The interface state.
	 */
	public InterfaceState getInterfaceState() {
		return interfaceState;
	}

	/**
	 * Checks if there is a cached update block for this cycle.
	 *
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean hasCachedUpdateBlock() {
		return cachedUpdateBlock != null;
	}

	/**
	 * Gets the cached update block.
	 *
	 * @return The cached update block.
	 */
	public Packet getCachedUpdateBlock() {
		return cachedUpdateBlock;
	}

	/**
	 * Sets the cached update block for this cycle.
	 *
	 * @param cachedUpdateBlock The cached update block.
	 */
	public void setCachedUpdateBlock(Packet cachedUpdateBlock) {
		this.cachedUpdateBlock = cachedUpdateBlock;
	}

	/**
	 * Resets the cached update block.
	 */
	public void resetCachedUpdateBlock() {
		cachedUpdateBlock = null;
	}

	/**
	 * Gets the current chat message.
	 *
	 * @return The current chat message.
	 */
	public ChatMessage getCurrentChatMessage() {
		return currentChatMessage;
	}

	/**
	 * Sets the current chat message.
	 *
	 * @param currentChatMessage The current chat message to set.
	 */
	public void setCurrentChatMessage(ChatMessage currentChatMessage) {
		this.currentChatMessage = currentChatMessage;
	}

	/**
	 * Gets the queue of pending chat messages.
	 *
	 * @return The queue of pending chat messages.
	 */
	public Queue<ChatMessage> getChatMessageQueue() {
		return chatMessages;
	}

	/**
	 * Gets the player's appearance.
	 *
	 * @return The player's appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Gets the player's equipment.
	 *
	 * @return The player's equipment.
	 */
	public Container getEquipment() {
		return equipment;
	}

	/**
	 * Gets the player's skills.
	 *
	 * @return The player's skills.
	 */
	public Skills getSkills() {
		return skills;
	}

	/**
	 * Gets the action sender.
	 *
	 * @return The action sender.
	 */
	public ActionSender getActionSender() {
		return actionSender;
	}

	/**
	 * Gets the incoming ISAAC cipher.
	 *
	 * @return The incoming ISAAC cipher.
	 */
	public ISAACCipher getInCipher() {
		return inCipher;
	}

	/**
	 * Gets the outgoing ISAAC cipher.
	 *
	 * @return The outgoing ISAAC cipher.
	 */
	public ISAACCipher getOutCipher() {
		return outCipher;
	}

	/**
	 * Gets the player's name.
	 *
	 * @return The player's name.
	 */
	public String getName() {
		return name;
	}

	public void setName(String playerName) {
		if(NameUtils.isValidName(playerName)) {
			name = playerName;
		} else {
			for(int i = 0; i < 100; i++) {
				System.out.println("Trying to set name: " + playerName);
			}
		}
	}

	public String getSafeDisplayName(){
		return getDisplay() != null && !getDisplay().isEmpty() ? TextUtils.titleCase(getDisplay()) : getName();
	}

	public String getDisplay() {
		return display;
	}

	/**
	 * Gets the player's password.
	 *
	 * @return The player's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the player's UID.
	 *
	 * @return The player's UID.
	 */
	public int getUID() {
		return uid;
	}

	/**
	 * Gets the <code>IoSession</code>.
	 *
	 * @return The player's <code>IoSession</code>.
	 */
	public IoSession getSession() {
		return session;
	}

	public long getPlayerRank() {
		return playerRank;
	}

	public void setPlayerRank(long playerRank) {
		this.playerRank = playerRank;
		getQuestTab().updateComponent(QuestTab.QuestTabComponent.RANK);
	}

	/**
	 * Checks if this player has a member's account.
	 *
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isMembers() {
		return members;
	}

	/**
	 * Sets the members flag.
	 *
	 * @param members The members flag.
	 */
	public void setMembers(boolean members) {
		this.members = members;
	}

	@Override
	public String toString() {
		return Player.class.getName() + " [name=" + name + " playerRank=" + playerRank
				+ " members=" + members + " index=" + this.getIndex() + "]";
	}

	/**
	 * Gets the active flag.
	 *
	 * @return The active flag.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active flag.
	 *
	 * @param active The active flag.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the action queue.
	 *
	 * @return The action queue.
	 */
	public ActionQueue getActionQueue() {
		return actionQueue;
	}

	/**
	 * Gets the inventory.
	 *
	 * @return The inventory
	 */
	public Container getInventory() {
		return inventory;
	}

	public Container getTrade() {
		return trade;
	}

	public Container getDuel() {
		return duel;
	}

	public EquipmentStats getBonus() {
		return bonus;
	}

	/**
	 * Updates the players' options when in a PvP area.
	 */
	public void updatePlayerAttackOptions(boolean enable) {
		if(enable) {
			actionSender.sendInteractionOption("Attack", 1, true);
			// actionSender.sendOverlay(381);
		} else {
			if(Rank.hasAbility(getPlayerRank(), Rank.ADMINISTRATOR))
				actionSender.sendInteractionOption("Moderate", 1, false);
		}
	}

	/**
	 * Manages updateflags and HP modification when a hit occurs.
	 *
	 * @param source The Entity dealing the blow.
	 */
	public void inflictDamage(Hit inc, Entity source) {
		if (isDead()) {
			return;
		}
		if(inc.getDamage() < 0)
			return;
		if(! getUpdateFlags().get(UpdateFlag.HIT)) {
			getDamage().setHit1(inc);
			getUpdateFlags().flag(UpdateFlag.HIT);
		} else {
			if(! getUpdateFlags().get(UpdateFlag.HIT_2)) {
				getDamage().setHit2(inc);
				getUpdateFlags().flag(UpdateFlag.HIT_2);
			} else {
				getDamage().setHit3(inc);
				getUpdateFlags().flag(UpdateFlag.HIT_3);
			}

		}
		skills.detractLevel(Skills.HITPOINTS, inc.getDamage());
		if(skills.getLevel(Skills.HITPOINTS) <= 0) {
			if(!this.isDead()) {
				World.submit(new PlayerDeathTask(this));
				if (prayers.isEnabled(21)) {
					Prayer.retribution(this);
				} else if (prayers.isEnabled(47)) {
					Prayer.wrath(this);
				}
			}
		}
	}

	public void debugMessage(String s) {
		if(debug)
			this.getActionSender().sendMessage(s);
	}

	public void heal(int hp) {
		heal(hp, 3);
	}

	public void heal(int hp, int skill) {
		int cHp = skills.getLevel(skill);
		if(skill == 3) {
			if((cHp + hp) > skills.calculateMaxLifePoints())
				skills.setLevel(3, skills.calculateMaxLifePoints());
			else
				skills.setLevel(skill, (cHp + hp));
		} else if((cHp + hp) > skills.getLevelForExp(skill))
			skills.setLevel(skill, skills.getLevelForExp(skill));
		else
			skills.setLevel(skill, (cHp + hp));
		if(skills.getLevel(3) <= 0) {
			World.submit(new PlayerDeathTask(this));
			this.setDead(true);
		}
	}

	public void heal(int hp, boolean brew) {
		int cHp = skills.getLevel(3);
		int j = 3;
		int brewBonus = (int)(skills.calculateMaxLifePoints() * .15);
		if(j == 3) {
			if((cHp + hp) > skills.calculateMaxLifePoints() + brewBonus)
				skills.setLevel(3, skills.calculateMaxLifePoints() + brewBonus);
			else
				skills.setLevel(3, (cHp + hp));
		}
		if(skills.getLevel(3) <= 0) {
			World.submit(new PlayerDeathTask(this));
			this.setDead(true);
		}
	}

	public void inflictDamage(Hit inc) {
		this.inflictDamage(inc, null);
	}

	public int getInflictDamage(int damg, Entity source, boolean poison, int style) {
		HitType hitType = HitType.NORMAL_DAMAGE;
		boolean npc = source instanceof NPC;
		if(npc) {
			NPC n = (NPC)source;
			if(n.getDefinition().getId() == 50 || ( n.getDefinition().getId() == 8133 && (style == Constants.MAGE || style == Constants.RANGE)))
				npc = false;
		}
		//getActionSender().sendMessage("Generated damg: " + damg + ", npc: " + npc + ", style = " + style);
		int trueStyle = style;
		if(trueStyle >= 5)
			trueStyle -= 5;
		if(source != null) {
			if (trueStyle == Constants.MELEE) {
				if (getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_MELEE)) {
					damg *= 0.6;
					if (npc)
						damg = 0;
				} else if (getPrayers().isEnabled(Prayers.CURSE_DEFLECT_MELEE)) {
					damg *= 0.6;
					if (npc)
						damg = 0;
					if (Misc.random(3) == 1) {
						this.playAnimation(Animation.create(12573));
						this.playGraphics(Graphic.create(2230));
						source.cE.hit(damg / 10, this, false, Constants.DEFLECT);
					}
				}
			} else if (trueStyle == Constants.MAGE) {
				if (getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_MAGE)) {
					damg *= 0.6;
					if (npc)
						damg = 0;
				} else if (getPrayers().isEnabled(Prayers.CURSE_DEFLECT_MAGIC)) {
					damg *= 0.6;
					if (npc)
						damg = 0;
					if (Misc.random(3) == 1) {
						this.playAnimation(Animation.create(12573));
						this.playGraphics(Graphic.create(2228));
						source.cE.hit(damg / 10, this, false, Constants.DEFLECT);
					}
				}
			} else if (trueStyle == Constants.RANGE) {
				if (getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_RANGE)) {
					damg *= 0.6;
					if (npc)
						damg = 0;
				} else if (getPrayers().isEnabled(Prayers.CURSE_DEFLECT_RANGED)) {
					damg *= 0.6;
					if (npc)
						damg = 0;
					if (Misc.random(3) == 1) {
						this.playAnimation(Animation.create(12573));
						this.playGraphics(Graphic.create(2229));
						source.cE.hit(damg / 10, this, false, Constants.DEFLECT);
					}
				}
			}
		}

		/** Ring of life */
		if (Combat.ringOfLifeEqupped(this) && !Combat.usingPhoenixNecklace(this)) {
			if(getLocation().canTeleport(this)) {
				final int newhp = getSkills().getLevel(3) - damg;
				if (newhp < Math.floor(getSkills().calculateMaxLifePoints() * .13) && newhp > 0) {
					getEquipment().set(Equipment.SLOT_RING, null);
					getWalkingQueue().reset();
					ContentEntity.playerGfx(this, 1684);
					ContentEntity.startAnimation(this, 9603);
					extraData.put("combatimmunity", System.currentTimeMillis() + 4000L);
					World.submit(new Task(200, "combatimmunity") {
						int loop = 0;

						public void execute() {
							if (loop == 5) {
								setTeleportTarget(Position.create(3225, 3218, 0));
								sendMessage("Your ring of life saves you, but is destroyed in the process.");
								this.stop();
							}
							loop++;
						}
					});
					return 0;
				}
			}
		}

		/** The phoenix necklace effect. */
		if (Combat.usingPhoenixNecklace(this)) {
			int newhp = getSkills().getLevel(3) - damg;
			if (newhp < Math.floor(getSkills().calculateMaxLifePoints() / 3.5) && newhp > 0) {
				getEquipment().set(Equipment.SLOT_AMULET, null);
				heal(damg);
				ContentEntity.playerGfx(this, 436);
				extraData.put("combatimmunity", System.currentTimeMillis() + 300L);
				sendMessage("Your phoenix necklace heals you, but is destroyed in the process.");
				return 0;
			}
		}

		return damg;
	}

	public int inflictDamage(int damg, Entity source, boolean poison, int style) {
		getInterfaceState().resetInterfaces();
		HitType hitType = HitType.NORMAL_DAMAGE;
		boolean npc = source instanceof NPC;
		getInterfaceState().resetInterfaces();
		/*if(npc) {
			NPC n = (NPC)source;
			if(n.getDefinition().getId() == 8133 && (style == Constants.MAGE || style == Constants.RANGE))
				npc = false;
		}
		//getActionSender().sendMessage("Generated damg: " + damg + ", npc: " + npc + ", style = " + style);
		int trueStyle = style;
		if(trueStyle >= 5)
			trueStyle -= 5;
        if(source != null) {
            if (trueStyle == Constants.MELEE) {
                if (getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_MELEE)) {
                    damg *= 0.6;
                    if (npc)
                        damg = 0;
                } else if (getPrayers().isEnabled(Prayers.CURSE_DEFLECT_MELEE)) {
                    damg *= 0.6;
                    if (npc)
                        damg = 0;
                    if (Misc.random(3) == 1) {
                        this.playAnimation(Animation.create(12573));
                        this.playGraphics(Graphic.create(2230));
                        source.cE.hit(damg / 10, this, false, Constants.DEFLECT);
                    }
                }
            } else if (trueStyle == Constants.MAGE) {
                if (getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_MAGE)) {
                    damg *= 0.6;
                    if (npc)
                        damg = 0;
                } else if (getPrayers().isEnabled(Prayers.CURSE_DEFLECT_MAGIC)) {
                    damg *= 0.6;
                    if (npc)
                        damg = 0;
                    if (Misc.random(3) == 1) {
                        this.playAnimation(Animation.create(12573));
                        this.playGraphics(Graphic.create(2228));
                        source.cE.hit(damg / 10, this, false, Constants.DEFLECT);
                    }
                }
            } else if (trueStyle == Constants.RANGE) {
                if (getPrayers().isEnabled(Prayers.PRAYER_PROTECT_FROM_RANGE)) {
                    damg *= 0.6;
                    if (npc)
                        damg = 0;
                } else if (getPrayers().isEnabled(Prayers.CURSE_DEFLECT_RANGED)) {
                    damg *= 0.6;
                    if (npc)
                        damg = 0;
                    if (Misc.random(3) == 1) {
                        this.playAnimation(Animation.create(12573));
                        this.playGraphics(Graphic.create(2229));
                        source.cE.hit(damg / 10, this, false, Constants.DEFLECT);
                    }
                }
            }
        }
	*/
		//If hitting more than hitpoints
		if(source instanceof Player && LegendaryStore.ThirdAgeSet.setFor(style).has(((Player) source).getEquipment())) {
			damg *= 1.15;

		}
		if(damg > skills.getLevel(Skills.HITPOINTS)) {
			damg = skills.getLevel(Skills.HITPOINTS);
		}
		// if(Rank.hasAbility(this, Rank.ADMINISTRATOR)) {
		if(extraData.getLong("combatimmunity") > System.currentTimeMillis())
			damg = 0;
		// }

		if(damg < 0)
			damg = 0;
		if(poison)
			hitType = HitType.POISON_DAMAGE;
		else if(damg <= 0)
			hitType = HitType.NO_DAMAGE;
		if(source instanceof Player) {
			try {
				if(duelAttackable > 0 && World.getPlayers().get(source.getIndex()).isDead())
					return 0;
			}catch(Exception e) {

			}
		}
		Hit hit = new Hit(damg, hitType, style);
		inflictDamage(hit, source);
		Prayer.redemption(this);
		return damg;
	}

	@Override
	public void deserialize(IoBuffer buf, boolean accCheckerMode) {// load
		System.out.println("Calling deserializable");
	}

	public void emoteTabPlay(Animation anim) {
		if(isDoingEmote) {
			getActionSender().sendMessage("You are already doing an emote.");
			return;
		} else {
			playAnimation(anim);
			isDoingEmote = true;
			inAction = true;
			World.submit(new Task(1000, "emote") {
				@Override
				public void execute() {
					isDoingEmote = false;
					inAction = false;
					this.stop();
				}

			});
			ClueScrollManager.trigger(this, anim.getId());
		}
	}

	public void startUpEvents() {
		// getActionSender().sendString(29161,
		// "Players Online: "+World.getPlayers().size());
		// getActionSender().sendString(29162, Server.getCurrentSessionTime());
		FriendsAssistant.initialize(this);
		for(int i = 0; i < 5; i++) {
			getInterfaceState().setNextDialogueId(i, - 1);
		}
	}

	@Override
	public void serialize(IoBuffer buf) {// save method
		System.out.println("Calling serialize");
	}

	@Override
	public void addToRegion(Region region) {
		region.addPlayer(this);
	}

	@Override
	public void removeFromRegion(Region region) {
		region.removePlayer(this);
	}

	@Override
	public int getClientIndex() {
		return this.getIndex() + 32768;
	}

	@Override
	public void inflictDamage(int damage, HitType type) {
		// TODO Auto-generated method stub

	}

	public int getEnergy() {
		return 100;
	}

	public void increasePitsDamage(int fightPitsDamage) {
		this.fightPitsDamage += fightPitsDamage;
	}

	public int getPitsDamage() {
		return fightPitsDamage;
	}

	public void setPitsDamage(int fightPitsDamage) {
		this.fightPitsDamage = fightPitsDamage;
	}

	public void increaseCorpDamage(int i) {
		damagedCorp += i;
	}

	public int getCorpDamage() {
		return damagedCorp;
	}

	public void setCorpDamage(int i) {
		damagedCorp = i;
	}

	public SpellBook getSpellBook() {
		return spellBook;
	}

	public FriendList getFriends() {
		return friendList;
	}

	public void updateTeleportTimer() {
		lastTeleport = System.currentTimeMillis();
	}

	public long getTimeSinceLastTeleport() {
		return System.currentTimeMillis() - lastTeleport;
	}

	public boolean isMagicTeleporting() {
		return System.currentTimeMillis() - lastTeleport <= 5000;
	}

	public void setTeleBlock(long l) {
		// TODO Auto-generated method stub
		teleBlockTimer = l;
	}

	public Task getCurrentTask() { return currentTask; }

	public void setCurrentTask(final Task value) {
		if (currentTask != null) {
			currentTask.stop();
		}
		this.currentTask = value; }

	public PvPTask getPvPTask() {
		return currentPvPTask;
	}

	public void setPvPTask(PvPTask task) {
		currentPvPTask = task;
	}

	public int getPvPTaskAmount() {
		return pvpTaskAmount;
	}

	public void setPvPTaskAmount(int am) {
		pvpTaskAmount = am;
	}

	public void decrementPvPTask(int delta) {
		pvpTaskAmount = pvpTaskAmount - delta;
	}

	public int pvpTaskToInteger() {
		return PvPTask.toInteger(currentPvPTask);
	}

	public boolean isTeleBlocked() {
		return System.currentTimeMillis() <= teleBlockTimer;
	}

	public int getClanRank() {
		return clanRank;
	}

	public void setClanRank(int r) {
		clanRank = r;
	}

	public boolean isClanMainOwner() {
		if(clanName == null || clanName.isEmpty())
			return false;
		Clan clan = ClanManager.clans.get(clanName);
		return clan != null && clan.getOwner().equalsIgnoreCase(getName());
	}

	public String getPlayersNameInClan() {
		//System.out.println("Clanranker is " + clanRank);
		if(isClanMainOwner())
			return "[Owner] " + getDisplay();
		return getClanRankName() + getDisplay();
	}

	public String getClanRankName() {
		String rank = "";
		switch(clanRank) {
			case 0:
				return "";
			case 1:
				rank = "Recruit";
				if(Dicing.diceClans.contains(clanName)) rank = "100K max";
				break;
			case 2:
				rank = "Corporal";
				if(Dicing.diceClans.contains(clanName)) rank = "500K max";
				break;
			case 3:
				rank = "Sergeant";
				if(Dicing.diceClans.contains(clanName)) rank = "Unlimited";
				break;
			case 4:
				rank = "Lieutenant";
				break;
			case 5:
				rank = "Owner";
				break;
			case 6:
				rank = "Mod";
				break;
			case 7:
				rank = "Admin";
				break;
		}
		return "[" + rank + "] ";
	}

	public String getClanName() {
		return clanName;
	}

	public void setClanName(String clanName) {
		this.clanName = clanName;
	}

	public void resetClanName() {
		this.clanName = "";
	}

	public AutoSaving getAutoSaving() {
		return autoSaving;
	}

	public int getPlayerUptime() {
		return playerUptime;
	}

	public int getBounty() {
		return bounty;
	}

	public void resetBounty() {
		bounty = 10;
	}

	public void resetKillStreak() {
		killStreak = 0;
	}

	public int getKillStreak() {
		return killStreak;
	}

	public void setKillStreak(int killStreak) {
		this.killStreak = killStreak;
	}

	public void increaseKillStreak() {
		killStreak++;
		getAchievementTracker().onKillstreak(killStreak);
		actionSender.sendString(36505, "Killstreak: @red@" + killStreak);
		bounty = (int)(4 * Math.pow(killStreak, 1.87));
		if(bounty > 40_000)
			bounty = 40_000;
		if(bounty < 10)
			bounty = 10;
		switch(killStreak) {
			case 5:
				World.getPlayers().stream().filter(p -> p != null).forEach(p -> p.sendPkMessage(getSafeDisplayName() + " is on a "
						+ killStreak + " killstreak!"));
				break;
			case 7:
				World.getPlayers().stream().filter(p -> p != null).forEach(p -> p.sendPkMessage(getSafeDisplayName()
						+ " has begun a rampage with a killstreak of " + killStreak));
				break;
			case 9:
				World.getPlayers().stream().filter(p -> p != null).forEach(p -> p.sendPkMessage(getSafeDisplayName()
						+ " is on a massacre with " + killStreak + " kills!"));
				break;
		}
		if(killStreak >= 10) {
			if(Math.random() > 0.5) {
				World.getPlayers().stream().filter(p -> p != null).forEach(p -> {
					p.sendPkMessage(getSafeDisplayName() + " now has "
							+ killStreak + " kills in a row! Kill him and gain "
							+ bounty + " Pk points!");
				});
			} else {
				String ppl = getPeopleString();
				World.getPlayers().stream().filter(p -> p != null).forEach(p -> {
					p.sendPkMessage(getSafeDisplayName() + " has killed "
							+ killStreak + ppl + " in a row! Kill him and gain "
							+ bounty + " Pk points!");
				});
			}
		}
	}

	public void addLastKill(String name) {
		lastKills[0] = lastKills[1];
		lastKills[1] = lastKills[2];
		lastKills[2] = lastKills[3];
		lastKills[3] = lastKills[4];
		lastKills[4] = name;
	}

	public boolean killedRecently(String name) {
		for(String s : lastKills) {
			if(s.equals(name))
				return true;
		}
		return false;
	}

	public void increaseKillCount() {
		killCount++;
	}

	public ActionSender sendHeadedMessage(final String color, final String header, final Object... message) {
		Arrays.asList(message).forEach(value -> actionSender.sendMessage(String.format("%s%s%s", (color != null ? color : ""), (header != null ? String.format("%s ", header) : ""), value.toString())));
		return getActionSender();
	}

	public ActionSender sendMessage(Object... message) {
		return sendHeadedMessage(null, null, message);
	}

	public ActionSender sendPkMessage(Object... message) {
		if(!Lock.isEnabled(this, Lock.PK_MESSAGES))
			return sendHeadedMessage("@dbl@", "[TactilityPk]", message);
		return getActionSender();
	}

	public ActionSender sendLootMessage(String tag, Object... message) {
		if(!Lock.isEnabled(this, Lock.LOOT_MESSAGES))
			return sendHeadedMessage("@gre@", "[" + tag + "]", message);
		return getActionSender();
	}

	public ActionSender sendServerMessage(Object... message) {
		return sendHeadedMessage("@whi@", "[Server]", message);
	}

	public ActionSender sendStaffMessage(Object... message) {
		return sendHeadedMessage("@blu@", "[Staff]", message);
	}

	public ActionSender sendClanMessage(Object... message) {
		return sendHeadedMessage("@dre@", null, message);
	}

	public ActionSender sendImportantMessage(Object... message) {
		return sendHeadedMessage("@dre@", "[Important]", message);
	}

	public ActionSender sendf(String message, Object... args) {
		try {
			actionSender.sendMessage(String.format(message, args));
		}catch(Exception e) {
			//fail in formatter, ignore
		}
		return getActionSender();
	}

	public void increaseDeathCount() {
		deathCount++;
	}

	public BankField getBankField() {
		return bankField;
	}

	public int getKillCount() {
		return killCount;
	}

	public void setKillCount(int kc) {
		killCount = kc;
		questtab.updateComponent(QuestTab.QuestTabComponent.KILLS);
		questtab.updateComponent(QuestTab.QuestTabComponent.KILL_DEATH);
	}

	public int getDeathCount() {
		return deathCount;
	}

	public void setDeathCount(int dc) {
		deathCount = dc;
		questtab.updateComponent(QuestTab.QuestTabComponent.DEATHS);
		questtab.updateComponent(QuestTab.QuestTabComponent.KILL_DEATH);
	}

	public boolean isExtremedBoosted() {
		return false;
	}

	public boolean isOverloaded() {
		return isOverloaded;
	}

	public void setOverloaded(boolean b) {
		isOverloaded = b;
	}

	public void resetDeathItemsVariables() {
		for(int i = 0; i < invSlot.length; i++) {
			invSlot[i] = false;
			if(i <= 13)
				equipSlot[i] = false;
			if(i <= 3)
				itemKeptId[i] = 0;
		}

	}

	public Farm getFarm() {
		// TODO Auto-generated method stub
		return farm;
	}

	public void removeAsTax(int amount) {
		if(ContentEntity.getItemAmount(this, 995) >= amount) {
			ContentEntity.deleteItemA(this, 995, amount);
		} else {
			for(int i = 0; i < getBank().size(); i++) {
				Item item = getBank().get(i);
				if(item != null && item.getId() == 995) {
					if (item.getCount() <= amount)
						getBank().remove(item);
					else
						getBank().remove(new BankItem(0, 995, amount));
				}
			}
		}
	}

	/**
	 * Returns the PlayerRights
	 */
	public String getQuestTabRank() {
		return Rank.getPrimaryRank(getPlayerRank()).toString();
	}

	/**
	 * Gets the highscores, initializes them if needed.
	 *
	 * @return
	 */
	public Highscores getHighscores() {
		if(highscores == null)
			highscores = new Highscores(this);
		return highscores;
	}

	@SuppressWarnings("deprecation")
	public void addCharge(int seconds) {
		if (chargeTill < System.currentTimeMillis())
			chargeTill = System.currentTimeMillis();
		Date date = new Date(chargeTill);
		date.setSeconds((date.getSeconds() + seconds));
		chargeTill = date.getTime();
	}

	@Override
	public boolean equals(Object other) {
		if(other == null)
			return false;
		if(! (other instanceof Player))
			return false;
		if(other == this)
			return true;
		return getName().equalsIgnoreCase(((Player) other).getName());

	}

	/** Does the player have a active charge?*/
	public boolean hasCharge() {
		return chargeTill > System.currentTimeMillis();
	}

	public int getTurkeyKills() {
		return turkeyKills;
	}

	public void setTurkeyKills(int turkeyKills) {
		this.turkeyKills = turkeyKills;
	}

	public boolean hasFinishedTG() {
		return turkeyKills >= 50;
	}

	public int setTreasureScroll(int treasureScroll) {
		return this.treasureScroll = treasureScroll;
	}

	public int getTreasureScroll() {
		return treasureScroll;
	}

	public JGrandExchangeTracker getGrandExchangeTracker(){
		if(geTracker == null)
			geTracker = new JGrandExchangeTracker(this);
		return geTracker;
	}

	public int getStartValue() {
		return startValue;
	}

	public void setStartValue(int value) {
		this.startValue = value;
	}

	private int startValue = -1;

	public String verificationCode = "";
	public boolean verificationCodeEntered = true;
	public int verificationCodeAttemptsLeft = 3;

	public List<Long> getIgnores() {
		return ignores;
	}

	public void setIgnores(final List<Long> VALUE) {
		ignores = VALUE;
	}

	public int getVoteStreak() {
		return voteStreak;
	}

	public void setVoteStreak(int voteStreak) {
		this.voteStreak = voteStreak;
	}

	public int getTodayVotes() {
		return todayVotes;
	}

	public void setTodayVotes(int todayVotes) {
		this.todayVotes = todayVotes;
	}

	public long getLocks() {
		return locks;
	}

	public void setLocks(long locks) {
		this.locks = locks;
	}

	public long getVoteBonusEndTime() {
		return voteBonusEndTime;
	}

	public void setVoteBonusEndTime(long bonusEnd, boolean applyEvent) {
		if(applyEvent) {
			World.submit(new VoteBonusEndTask(this, bonusEnd));
		}
		this.voteBonusEndTime = (applyEvent ? System.currentTimeMillis() : 0) + bonusEnd;
	}

	public long getLastVoteBonus() {
		return lastVoteBonus;
	}

	public void setLastVoteBonus(long lastVoteBonus) {
		this.lastVoteBonus = lastVoteBonus;
	}

	public String getGoogleAuthenticatorKey() {
		return googleAuthenticatorKey;
	}

	public void setGoogleAuthenticatorKey(String googleAuthenticatorKey) {
		this.googleAuthenticatorKey = googleAuthenticatorKey;
	}

	public List<String> getGoogleAuthenticatorBackup() {
		return googleAuthenticatorBackup;
	}

	public void setGoogleAuthenticatorBackup(List<String> googleAuthenticatorBackup) {
		this.googleAuthenticatorBackup = googleAuthenticatorBackup;
	}

	public int getLastMac() {
		return lastMac;
	}

	public void setLastMac(int lastMac) {
		this.lastMac = lastMac;
	}

	public Map<String, Long> getSavedIps() {
		return savedIps;
	}

	public void setSavedIps(Map<String, Long> savedIps) {
		this.savedIps = savedIps;
	}

	public void saveIp(String ipAddress) {
		if(savedIps.containsKey(ipAddress))
			return;
		sendImportantMessage("The IP " + ipAddress + " has been saved for 7 days!");
		savedIps.put(ipAddress, System.currentTimeMillis() + Time.ONE_DAY * 7);
	}

	public boolean canLoginFreely(String ipAddress) {
		if(!savedIps.containsKey(ipAddress))
			return false;
		if(savedIps.get(ipAddress) >= System.currentTimeMillis())
			return true;
		savedIps.remove(ipAddress);
		return false;
	}
}