package com.client;

import com.client.archive.task.Updater;
import com.client.configuration.Configuration;
import com.client.configuration.Constants;
import com.client.configuration.impl.*;
import com.client.configuration.impl.MenuBar;
import com.client.itf.RequestHelpInterface;
import com.client.util.FrameType;
import com.frame.GameFrame;
import com.frame.Loader;
import com.logging.Logger;
import rs.atomic.itf.InterfaceManager;

import javax.swing.*;
import java.applet.AppletContext;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class Client extends RSApplet {

    public static final int CONFIG_IDX = 0;
    public static final int MODEL_IDX = 1;
    public static final int ANIM_IDX = 2;
    public static final int AUDIO_IDX = 3;
    public static final int MAP_IDX = 4;
    public static final int IMAGE_IDX = 5;
    public static final int TEXTURE_IDX = 6;

    static final int anIntArrayArray1003[][] = {{6798, 107, 10283, 16, 4797, 7744, 5799, 4634, -31839, 22433, 2983, -11343, 8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010}, {8741, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533, 25239, 8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010}, {25238, 8742, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533, 8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010}, {4626, 25239, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 100, 11146, 6439, 4758, 10270}, {4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574}};
    static final int anIntArray1204[] = {9104, 10275, 11146, 7595, 3610, 7975, 8526, 918, -26734, 24466, 10145, -6882, 5027, 1457, 16565, -30545, 25486, 24, 5392, 10429, 3673, -27335, -21957, 192, 687, 412, 21821, 28835, -15460, -14019};

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
    private static final int[] anIntArray1019;
    private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
    private static final String validNumbers = "0123456789";
    public static boolean DEV = false;
    public static boolean critical = false;
    public static boolean sky = true;

    public static int Zoom = 0;
    public static int testXp = 0;
    public static boolean isFullScreen, hasBeenBlanked;
    public static int extraWidth = 1280 - 765, extraHeight = 1024 - 503, clientWidth = 0, clientHeight = 0;
    public static int spellID = 0;
    public static byte[] mac = new byte[4];
    public static int macAdd = 0;
    public static boolean setup = false;
    public static int openInterfaceID;
    public static RSInterface aClass9_1059;
    public static int anInt1089;
    public static Player myPlayer;
    public static int[] tabInterfaceIDs = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    public static boolean loggedIn;
    public static int invOverlayInterfaceID;
    public static int tabID;
    public static boolean inputTaken;
    public static int anIntArray1232[];
    public static boolean checkMac = true;
    public static int anInt1211;
    static int loopCycle;
    private static int logintries = 0;
    private static long lastLoginTry = System.currentTimeMillis();
    private static int backgroundCount;
    private static int[] clanIds = {18144, 18145, 18146, 18147, 18148, 18149, 18150, 18151, 18152, 18153, 18154, 18155, 18156, 18157, 18158, 18159, 18160, 18161, 18162, 18163, 18164, 18173, 18174, 18175, 18176, 18177, 18178, 18179, 18180, 18181, 18182, 18183, 18184, 18185, 18186, 18187, 18188, 18189, 18190, 18191, 18192, 18193, 18194, 18195, 18196, 18197, 18198, 18199, 18200, 18201, 18202, 18203, 18204, 18205, 18206, 18207, 18208, 18209, 18210, 18211, 18212, 18213, 18214, 18215, 18216, 18217, 18218, 18219, 18220, 18221, 18222, 18223, 18224, 18225, 18226, 18227, 18228, 18229, 18230, 18231, 18232, 18233, 18234, 18235, 18236, 18237, 18238, 18239, 18240, 18241, 18242, 18243, 18244, 18245, 18246, 18247, 0, 0, 0, 0, 0};
    private static int anInt849;
    private static int anInt854;
    private static int anInt924;
    private static int nodeID = 10;
    private static boolean isMembers = true;
    private static boolean lowMem;
    private static int anInt986;
    private static int anInt1005;
    private static int anInt1051;
    private static int anInt1061;
    private static int anInt1097;
    private static boolean tabAreaAltered;
    private static int anInt1117;
    private static int anInt1134;
    private static int anInt1142;
    private static boolean needDrawTabArea;
    private static int anInt1155;
    private static int anInt1175;
    private static int anInt1188;
    private static boolean flagged;
    private static int anInt1226;
    private static int anInt1288;

    static {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("window") && checkMac) {
                InetAddress address = InetAddress.getLocalHost();
                NetworkInterface ni = NetworkInterface.getByInetAddress(address);
                mac = ni.getHardwareAddress();
                macAdd = byteArrayToInt(mac, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        anIntArray1019 = new int[99];
        int i = 0;
        for (int j = 0; j < 99; j++) {
            int l = j + 1;
            int i1 = (int) ((double) l + 300D * Math.pow(2D, (double) l / 7D));
            i += i1;
            anIntArray1019[j] = i / 4;
        }
        anIntArray1232 = new int[32];
        i = 2;
        for (int k = 0; k < 32; k++) {
            anIntArray1232[k] = i - 1;
            i += i;
        }
    }

    static {
        FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    final Decompressor[] decompressors;
    private final int[] chatRights;
    private final int[] currentExp;
    private final int[] anIntArray873;
    private final boolean[] aBooleanArray876;
    private final int maxPlayers;
    private final int myPlayerIndex;
    private final int[] currentStats;
    private final long[] ignoreListAsLongs;
    private final int[] anIntArray928;
    private final int[] chatTypes;
    private final String[] chatNames;
    private final String[] chatMessages;
    private final int[] anIntArray965 = {0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff};
    private final int[] anIntArray968;
    private final int[] anIntArray969;
    private final int anInt975;
    private final int[] anIntArray976;
    private final int[] anIntArray977;
    private final int[] anIntArray978;
    private final int[] anIntArray979;
    private final int[] anIntArray980;
    private final int[] anIntArray981;
    private final int[] anIntArray982;
    private final String[] aStringArray983;
    private final int[] anIntArray990;
    private final boolean aBoolean994;
    private final int[] anIntArray1030;
    private final int[] maxStats;
    private final int[] anIntArray1045;
    private final int[] anIntArray1052;
    private final int[] anIntArray1057;
    private final int barFillColor;
    private final int[] anIntArray1065;
    private final int[] expectedCRCs;
    private final String[] atPlayerActions;
    private final boolean[] atPlayerArray;
    private final int[][][] anIntArrayArrayArray1129;
    private final int[] anIntArray1177 = {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
    private final int[] anIntArray1203;
    private final int[] anIntArray1207;
    private final Background[] modIcons;
    private final int[] anIntArray1229;
    private final int[] anIntArray1240;
    private final int[] anIntArray1241;
    private final int[] anIntArray1250;
    private final boolean rsAlreadyLoaded;
    public Sprite[] combatIcons = new Sprite[5];
    public AlertHandler alertHandler;
    public Sprite alertBack;
    public Sprite alertBorder;
    public Sprite alertBorderH;
    public boolean drawLongTabs;
    public boolean xpClicked = false;
    public boolean rest = false;
    public int flagPos = 72;
    public boolean drawFlag = false;
    public Sprite sprite1;
    public Sprite xpFlag;
    public Sprite x10Expand;
    public Sprite x10ExpandOld;
    public Sprite mapRing;
    public int xpToDraw = 0;
    public boolean drawXpBar = false;
    public float lastPercent;
    public int fromTab = 0;
    public long clickWeild = System.nanoTime();
    public int headIconOffset, yOffsetForText;
    public JFrame frame;
    public String customInterfaceInput = "";
    public boolean displayChat;
    public Sprite titleBox;
    public Sprite titleButton;
    public Sprite titleButtonHover;
    public int logIconHPos = 0;
    public int questionIconHPos = 0;
    public int xpIconHPos = 0;
    public int count, opacityCount;
    public boolean isTextCursor = false;
    public Sprite[] chatToggle;
    public int id = 0;
    public int rights;
    public String name;
    public String message;
    public String clanname;
    public int chatTypeView;
    public int clanChatMode;
    public int duelMode;
    public Sprite[] particleIcon;
    public int variousSettings[];
    public Stream inStream;
    public int[] menuActionCmd4;
    public int[] menuActionID;
    public int menuActionRow;
    public int tabHPos;
    public Stream stream;
    public String[] menuActionName;
    public boolean runClicked = false;
    public TextDrawingArea smallText;
    public TextDrawingArea aTextDrawingArea_1271;
    public int drawCount;
    public int fullscreenInterfaceID;
    public int anInt1044;// 377
    public int anInt1129;// 377
    public int anInt1315;// 377
    public int anInt1500;// 377
    public int anInt1501;// 377
    public int[] fullScreenTextureArray;
    boolean eventMenuOpen = false;
    boolean eventStarted = false;
    boolean eventOpen = false;
    long eventStartTime = 0;
    double eventGlowAlpha = 0.0;
    boolean eventAlphaIncrease = false;
    boolean eventSafe = false;
    int eventCategory = 0;
    String eventType = "";
    String eventMessage = "Player Event";
    int eventTimeTillStart = 0;
    boolean itemSearch = false;
    String geSearchInput = "";
    ItemDef[] itemSearchResults = new ItemDef[500];
    private String top_Message = "Welcome to " + Constants.getServerName(), top_Message2 = "";
    private int tries;
    private int bankTab = 0;
    private Sprite HPBarFull;
    private Sprite HPBarEmpty;
    private Sprite[] specialSprites;
    private Sprite[] eventSprites;
    private Graphics bufferGraphics;
    private Image buffer;
    private int cButtonHPos;
    private int cButtonCPos;
    private int modifiableXValue = 0;
    private boolean searchBank = false;
    private Sprite tabHover;
    private Sprite tabClicked;
    private RSImageProducer loginScreenArea;
    private Sprite hitMark[];
    private Sprite hitIcon[];
    private TextDrawingArea smallHit;
    private TextDrawingArea bigHit;
    private TextDrawingArea aTextDrawingArea_1273;
    private Background loginBox;
    private Sprite loginButton;
    private Sprite loginButtonHover;
    private Sprite textBox;
    private Sprite textBoxHover;
    /* Declare custom sprites */
    private Sprite chatArea;
    private Sprite loadingPleaseWait;
    private Sprite reestablish;
    private Sprite[] chatButtons;
    private Sprite[] fullScreenSprites;
    private Sprite[] socialIcons;
    private Sprite tabArea;
    private Sprite tabs554;
    private Sprite long554;
    // private Sprite mapArea;
    /**/
    private RSImageProducer leftFrame;
    private RSImageProducer topFrame;
    private RSImageProducer rightFrame;
    private int ignoreCount;
    private long aLong824;
    private int[][] anIntArrayArray825;
    private int[] friendsNodeIDs;
    private NodeList[][][] groundArray;
    private volatile boolean aBoolean831;
    private Socket aSocket832;
    private int loginScreenState;
    private Stream aStream_834;
    private NPC[] npcArray;
    private int npcCount;
    private int[] npcIndices;
    private int anInt839;
    private int[] anIntArray840;
    private int anInt841;
    private int anInt842;
    private int anInt843;
    private String aString844;
    private int privateChatMode;
    private Stream aStream_847;
    private boolean aBoolean848;
    private int[] anIntArray851;
    private int[] anIntArray852;
    private int[] anIntArray853;
    private int anInt855;
    private int xCameraPos;
    private int zCameraPos;
    private int yCameraPos;
    private int yCameraCurve;
    private int xCameraCurve;
    public int myPrivilege;
    private Sprite[] redStones;
    private Sprite mapFlag;
    private Sprite mapMarker;
    private boolean aBoolean872;
    private int anInt874;
    private int weight;
    private MouseDetection mouseDetection;
    private String reportAbuseInput;
    private int unknownInt10;
    private boolean menuOpen;
    private int anInt886;
    private String inputString;
    private Player[] playerArray;
    private int playerCount;
    private int[] playerIndices;
    private int anInt893;
    private int[] anIntArray894;
    private Stream[] aStreamArray895s;
    private int anInt896;
    private int anInt897;
    private int friendsCount;
    private int anInt900;
    private int[][] anIntArrayArray901;
    private byte[] aByteArray912;
    private int anInt913;
    private int crossX;
    private int crossY;
    private int crossIndex;
    private int crossType;
    private int plane;
    private boolean loadingError;
    private int[][] anIntArrayArray929;
    private Sprite aClass30_Sub2_Sub1_Sub1_931;
    private Sprite aClass30_Sub2_Sub1_Sub1_932;
    private int anInt933;
    private int anInt934;
    private int anInt935;
    private int anInt936;
    private int anInt937;
    private int anInt938;
    private int anInt945;
    private WorldController worldController;
    private Sprite[] sideIcons;
    private int menuScreenArea;
    private int menuOffsetX;
    private int menuOffsetY;
    private int menuWidth;
    private int menuHeight;
    private long aLong953;
    private boolean aBoolean954;
    private long[] friendsListAsLongs;
    private String[] clanList = new String[100];
    private int currentSong;
    private int spriteDrawX;
    private int spriteDrawY;
    private boolean aBoolean972;
    private int anInt984;
    private int anInt985;
    private Sprite[] hitMarks;
    private int anInt988;
    private int anInt989;
    private int anInt995;
    private int anInt996;
    private int anInt997;
    private int anInt998;
    private int anInt999;
    private ISAACRandomGen encryption;
    private Sprite mapEdge;
    private Sprite multiOverlay;
    private Sprite multiOverlay2;
    private String amountOrNameInput;
    private int daysSinceLastLogin;
    private int pktSize;
    private int pktType;
    private int anInt1009;
    private int anInt1010;
    private int anInt1011;
    private NodeList aClass19_1013;
    private int anInt1014;
    private int anInt1015;
    private int anInt1016;
    private boolean aBoolean1017;
    private int anInt1018;
    private int anInt1021;
    private int anInt1022;
    private int loadingStage;
    private Sprite scrollBar1;
    private Sprite scrollBar2;
    private Sprite[] scrollSprites;
    private int anInt1026;
    private boolean aBoolean1031;
    private Sprite[] mapFunctions;
    private int baseX;
    private int baseY;
    private int anInt1036;
    private int anInt1037;
    private int loginFailures;
    private int anInt1039;
    private int dialogID;
    private int anInt1046;
    private boolean aBoolean1047;
    private int anInt1048;
    private StreamLoader titleStreamLoader;
    private int anInt1054;
    private int anInt1055;
    private NodeList aClass19_1056;
    private Background[] mapScenes;
    private int anInt1062;
    private int friendsListAction;
    private int mouseInvInterfaceIndex;
    private int lastActiveInvInterface;
    public OnDemandFetcher onDemandFetcher;
    private Sprite[] mapArea;
    private int anInt1069;
    private int anInt1070;
    private int anInt1071;
    private int[] anIntArray1072;
    private int[] anIntArray1073;
    private Sprite mapDotItem;
    private Sprite mapDotNPC;
    private Sprite mapDotPlayer;
    private Sprite mapDotFriend;
    private Sprite mapDotTeam;
    private Sprite mapDotClan;
    private boolean aBoolean1080;
    private String[] friendsList;
    private int anInt1084;
    private int anInt1085;
    private int activeInterfaceType;
    private int anInt1087;
    private int anInt1088;
    private int[] menuActionCmd2;
    private int[] menuActionCmd3;
    private int[] menuActionCmd1;
    private Sprite[] headIcons;
    private Sprite[] skullIcons;
    private Sprite[] headIconsHint;
    private int anInt1098;
    private int anInt1099;
    private int anInt1100;
    private int anInt1101;
    private int anInt1102;
    private int anInt1104;
    private RSImageProducer aRSImageProducer_1107;
    private RSImageProducer aRSImageProducer_1108;
    private RSImageProducer aRSImageProducer_1109;
    private RSImageProducer aRSImageProducer_1110;
    private RSImageProducer aRSImageProducer_1111;
    private RSImageProducer aRSImageProducer_1112;
    private RSImageProducer aRSImageProducer_1113;
    private RSImageProducer aRSImageProducer_1114;
    private RSImageProducer aRSImageProducer_1115;
    private int membersInt;
    private String aString1121;
    private Sprite compass;
    private RSImageProducer aRSImageProducer_1125;
    private int anInt1131;
    private int anInt1132;
    private int spellSelected;
    private int anInt1137;
    private int spellUsableOn;
    private String spellTooltip;
    private Sprite[] aClass30_Sub2_Sub1_Sub1Array1140;
    private boolean aBoolean1141;
    private int energy;
    private boolean aBoolean1149;
    private Sprite[] crosses;
    private boolean musicEnabled;
    private Background[] aBackgroundArray1152s;
    private int unreadMessages;
    private boolean canMute;
    private boolean aBoolean1159;
    private boolean aBoolean1160;
    private RSImageProducer aRSImageProducer_1163;
    private RSImageProducer mapEdgeIP;
    private RSImageProducer aRSImageProducer_1164;
    private RSImageProducer aRSImageProducer_1165;
    private RSImageProducer aRSImageProducer_1166;
    private int daysSinceRecovChange;
    private RSSocket socketStream;
    private int anInt1169;
    private int minimapInt3;
    private int anInt1171;
    private long aLong1172;
    private boolean genericLoadingError;
    private int reportAbuseInterfaceID;
    private NodeList aClass19_1179;
    private int[] anIntArray1180;
    private int[] anIntArray1181;
    private int[] anIntArray1182;
    private byte[][] aByteArrayArray1183;
    private int anInt1184;
    private int minimapInt1;
    private int anInt1186;
    private int anInt1187;
    private int[] anIntArray1190;
    private int[] anIntArray1191;
    private int anInt1193;
    private int splitPrivateChat;
    private Background mapBack;
    private Sprite aClass30_Sub2_Sub1_Sub1_1201;
    private Sprite aClass30_Sub2_Sub1_Sub1_1202;
    private int minimapInt2;
    private int anInt1210;
    private String promptInput;
    private int anInt1213;
    private int[][][] intGroundArray;
    private long aLong1215;
    private int loginScreenCursorPos;
    private boolean saveIp = false;
    private long aLong1220;
    private int anInt1222;
    private int inputDialogState;
    private int nextSong;
    private boolean songChanging;
    private Class11[] aClass11Array1230;
    private boolean aBoolean1233;
    private int[] anIntArray1234;
    private int[] anIntArray1235;
    private int[] anIntArray1236;
    private int anInt1237;
    private int anInt1238;
    private Sprite infinity;
    private boolean aBoolean1242;
    private int atInventoryLoopCycle;
    private int atInventoryInterface;
    private int atInventoryIndex;
    private int atInventoryInterfaceType;
    private byte[][] aByteArrayArray1247;
    private int tradeMode;
    private int anInt1249;
    private int anInt1251;
    private int anInt1253;
    private int anInt1254;
    private boolean welcomeScreenRaised;
    private boolean messagePromptRaised;
    private int anInt1257;
    private byte[][][] byteGroundArray;
    private int prevSong;
    private int destX;
    private int destY;
    private Sprite aClass30_Sub2_Sub1_Sub1_1263;
    private int anInt1264;
    private int anInt1265;
    private String loginMessage1;
    private String loginMessage2;
    private int anInt1268;
    private int anInt1269;
    private TextDrawingArea chatTextDrawingArea;
    private Sprite[] newSideIcons;
    private int backDialogID;
    private int anInt1278;
    private int anInt1279;
    private int[] bigX;
    private int[] bigY;
    private int itemSelected;
    private int anInt1283;
    private int anInt1284;
    private int anInt1285;
    private String selectedItemName;
    private int publicChatMode;
    private int anInt1289;
    private Sprite logIconH;
    private Sprite logIconC;
    private Sprite questionIconH;
    private Sprite worldMapIcon;
    private Sprite xpIcon;
    private Sprite xpIconH;
    private Sprite logoutIcon;
    private Sprite questionIcon;
    private Sprite emptyOrb;
    private Sprite summIcon;
    private Sprite orbFill;
    private Sprite hoveredEmpty;
    private Sprite runIcon1;
    private Sprite runIcon2;
    private Sprite runOrb1;
    private Sprite runOrb2;
    private Sprite hitPointsFill;
    private Sprite hitPointsIcon;
    private Sprite prayerFill;
    private Sprite prayerIcon;

    private Client() {
        setHighMem();
        Signlink.start();
        super.prepareGraphics();
        fullGameScreen = new RSImageProducer(myWidth, myHeight);
        startRunnable(this, Thread.MAX_PRIORITY);
        alertHandler = new AlertHandler(this);
        fullscreenInterfaceID = -1;
        chatRights = new int[500];
        chatTypeView = 0;
        mapArea = new Sprite[3];
        mapArea = new Sprite[3];
        fullScreenSprites = new Sprite[3];
        tabHPos = -1;
        lastPercent = 0.0F;
        drawLongTabs = false;
        isFullScreen = false;
        hasBeenBlanked = false;
        hitMark = new Sprite[50];
        hitIcon = new Sprite[20];
        eventSprites = new Sprite[9];
        specialSprites = new Sprite[5];
        clanChatMode = 0;
        cButtonHPos = -1;
        displayChat = true;
        cButtonCPos = 0;
        anIntArrayArray825 = new int[104][104];
        friendsNodeIDs = new int[200];
        groundArray = new NodeList[4][104][104];
        aBoolean831 = false;
        aStream_834 = new Stream(new byte[30000]);
        npcArray = new NPC[16384];
        npcIndices = new int[16384];
        anIntArray840 = new int[1000];
        aStream_847 = Stream.create();
        aBoolean848 = true;
        openInterfaceID = -1;
        currentExp = new int[Skills.SKILLS_COUNT];
        aBoolean872 = false;
        anIntArray873 = new int[5];
        anInt874 = -1;
        aBooleanArray876 = new boolean[5];
        reportAbuseInput = "";
        customInterfaceInput = "";
        unknownInt10 = -1;
        menuOpen = false;
        inputString = "";
        maxPlayers = 2048;
        myPlayerIndex = 2047;
        playerArray = new Player[maxPlayers];
        playerIndices = new int[maxPlayers];
        anIntArray894 = new int[maxPlayers];
        aStreamArray895s = new Stream[maxPlayers];
        anInt897 = 1;
        anIntArrayArray901 = new int[104][104];
        aByteArray912 = new byte[16384];
        currentStats = new int[Skills.SKILLS_COUNT];
        ignoreListAsLongs = new long[100];
        loadingError = false;
        anIntArray928 = new int[5];
        anIntArrayArray929 = new int[104][104];
        chatTypes = new int[500];
        chatNames = new String[500];
        chatMessages = new String[500];
        chatButtons = new Sprite[4];
        sideIcons = new Sprite[15];
        chatToggle = new Sprite[2];
        newSideIcons = new Sprite[16];
        redStones = new Sprite[5];
        aBoolean954 = true;
        friendsListAsLongs = new long[200];
        currentSong = -1;
        spriteDrawX = -1;
        spriteDrawY = -1;
        anIntArray968 = new int[33];
        anIntArray969 = new int[256];
        decompressors = new Decompressor[5];
        variousSettings = new int[2000];
        aBoolean972 = false;
        anInt975 = 50;
        anIntArray976 = new int[anInt975];
        anIntArray977 = new int[anInt975];
        anIntArray978 = new int[anInt975];
        anIntArray979 = new int[anInt975];
        anIntArray980 = new int[anInt975];
        anIntArray981 = new int[anInt975];
        anIntArray982 = new int[anInt975];
        aStringArray983 = new String[anInt975];
        anInt985 = -1;
        hitMarks = new Sprite[20];
        anIntArray990 = new int[5];
        aBoolean994 = false;
        amountOrNameInput = "";
        aClass19_1013 = new NodeList();
        aBoolean1017 = false;
        anInt1018 = -1;
        anIntArray1030 = new int[5];
        aBoolean1031 = false;
        mapFunctions = new Sprite[100];
        scrollSprites = new Sprite[10];
        dialogID = -1;
        maxStats = new int[Skills.SKILLS_COUNT];
        anIntArray1045 = new int[2000];
        aBoolean1047 = true;
        anIntArray1052 = new int[151];
        anInt1054 = -1;
        aClass19_1056 = new NodeList();
        anIntArray1057 = new int[33];
        aClass9_1059 = new RSInterface();
        mapScenes = new Background[100];
        barFillColor = 0x4d4233;
        anIntArray1065 = new int[7];
        anIntArray1072 = new int[1000];
        anIntArray1073 = new int[1000];
        aBoolean1080 = false;
        friendsList = new String[200];
        inStream = Stream.create();
        expectedCRCs = new int[9];
        menuActionCmd2 = new int[500];
        menuActionCmd3 = new int[500];
        menuActionCmd4 = new int[500];
        menuActionID = new int[500];
        menuActionCmd1 = new int[500];
        headIcons = new Sprite[20];
        skullIcons = new Sprite[20];
        headIconsHint = new Sprite[20];
        socialIcons = new Sprite[4];
        particleIcon = new Sprite[8];
        tabAreaAltered = false;
        aString1121 = "";
        atPlayerActions = new String[6];
        atPlayerArray = new boolean[6];
        anIntArrayArrayArray1129 = new int[4][13][13];
        anInt1132 = 2;
        aClass30_Sub2_Sub1_Sub1Array1140 = new Sprite[1000];
        aBoolean1141 = false;
        aBoolean1149 = false;
        crosses = new Sprite[8];
        musicEnabled = true;
        needDrawTabArea = false;
        loggedIn = false;
        canMute = false;
        aBoolean1159 = false;
        aBoolean1160 = false;
        anInt1171 = 1;
        genericLoadingError = false;
        reportAbuseInterfaceID = -1;
        aClass19_1179 = new NodeList();
        anInt1184 = 128;
        invOverlayInterfaceID = -1;
        stream = Stream.create();
        menuActionName = new String[500];
        anIntArray1203 = new int[5];
        anIntArray1207 = new int[50];
        anInt1210 = 2;
        anInt1211 = 78;
        promptInput = "";
        modIcons = new Background[17];
        tabID = 3;
        inputTaken = false;
        songChanging = true;
        anIntArray1229 = new int[151];
        aClass11Array1230 = new Class11[4];
        aBoolean1233 = false;
        anIntArray1240 = new int[100];
        anIntArray1241 = new int[50];
        aBoolean1242 = false;
        anIntArray1250 = new int[50];
        rsAlreadyLoaded = false;
        welcomeScreenRaised = false;
        messagePromptRaised = false;
        loginMessage1 = "";
        loginMessage2 = "";
        backDialogID = -1;
        anInt1279 = 2;
        bigX = new int[4000];
        bigY = new int[4000];
        anInt1289 = -1;
    }

    private static void setHighMem() {
        WorldController.lowMem = false;
        Texture.lowMem = false;
        lowMem = false;
        ObjectManager.lowMem = false;
        ObjectDef.lowMem = false;
    }

    public static int getChatColor() {
        return Configuration.getInstance().getValue(ChatColorHexidecimal.class);
    }

    private static String intToKOrMilLongName(int i) {
        String s = String.valueOf(i);
        for (int k = s.length() - 3; k > 0; k -= 3) { s = s.substring(0, k) + "," + s.substring(k); }

        // if(j != 0)
        // aBoolean1224 = !aBoolean1224;
        if (s.length() > 8) {
            s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
        } else if (s.length() > 4) { s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")"; }
        return " " + s;
    }

    private static String intToKOrMil(int j) {
        if (j == 0) { return "@inf@"; }
        if (j < 0x186a0) { return String.valueOf(j); }
        if (j < 0x989680) { return j / 1000 + "K"; } else { return j / 0xf4240 + "M"; }
    }

    public static void setTab(int id) {
        needDrawTabArea = true;
        tabID = id;
        tabAreaAltered = true;
    }

    private static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        try {
            for (int i = 0; i < 4; i++) {
                int shift = (4 - 1 - i) * 8;
                value += (b[i + offset] & 0x000000FF) << shift;
            }
        } catch (Exception e) {
        }
        return value;
    }

    private static String combatDiffColor(int i, int j) {
        int k = i - j;
        if (k < -9) { return "@red@"; }
        if (k < -6) { return "@or3@"; }
        if (k < -3) { return "@or2@"; }
        if (k < 0) { return "@or1@"; }
        if (k > 9) { return "@gre@"; }
        if (k > 6) { return "@gr3@"; }
        if (k > 3) { return "@gr2@"; }
        if (k > 0) { return "@gr1@"; } else { return "@yel@"; }
    }

    private static void setLowMem() {
        WorldController.lowMem = true;
        Texture.lowMem = true;
        lowMem = true;
        ObjectManager.lowMem = true;
        ObjectDef.lowMem = true;
    }

    public static Client getClient() {
        return InstanceHolder.instance;
    }

    public void pushMessage(String s, int i, String s1) {
        if (i == 0 && dialogID != -1) {
            aString844 = s;
            super.clickMode3 = 0;
        }
        if (backDialogID == -1) { inputTaken = true; }
        for (int j = 499; j > 0; j--) {
            chatTypes[j] = chatTypes[j - 1];
            chatNames[j] = chatNames[j - 1];
            chatMessages[j] = chatMessages[j - 1];
            chatRights[j] = chatRights[j - 1];
        }
        chatTypes[0] = i;
        chatNames[0] = s1;
        chatMessages[0] = s;
        chatRights[0] = rights;
    }

    public void toggleChat() {
        displayChat = !displayChat;
    }

    /**
     * Bank stuff
     */
    private void bankUpdate() {
        updateTabPreviews();
        for (int child = 0; child <= 32; child++) {
            boolean isBankSearching = variousSettings[1010] == 1;
            boolean bankContainer = (child <= 32) && (child >= 24);
            boolean tabDivider = (child <= 23) && (child >= 16);
            boolean tabHighlight = (child <= 15) && (child >= 8);
            boolean tabText = (child <= 7) && (child >= 0);
            int tab = bankContainer ? (child - 24) : tabDivider ? (child - 16) : tabHighlight ? (child - 8) : child;
            boolean mainTabSprite = (bankTab > 0) && (((tab != bankTab) && bankContainer) || tabDivider || tabText);
            boolean emptyTab = getTabSize(tab + (bankContainer ? 0 : 1)) == 0;
            int x = bankContainer ? 38 : tabHighlight ? 36 : 30;
            if (mainTabSprite || emptyTab || isBankSearching) {
                RSInterface.interfaceCache[5385].childX[child] = -1000;
            } else {
                RSInterface.interfaceCache[5385].childX[child] = x;
            }
            if (isBankSearching) {
                RSInterface.interfaceCache[5385].childX[24] = x;
            }
            int y = 0;
            if (bankTab == 0) {
                y = getTabsHeight(tab + (bankContainer ? 0 : 1));
                if (bankContainer) {
                    y += 3;
                }
                if (tabDivider) {
                    y -= 3;
                }
                if (tabText) {
                    y -= 14;
                }
            } else {
                y = 7 - (tabHighlight ? 3 : 0);
            }
            RSInterface.interfaceCache[5385].childY[child] = y;
        }
        updateBankScrollPosition();
    }    void mouseWheelDragged(int i, int j) {
        if (!mouseWheelDown) { return; }
        this.anInt1186 += i * 3;
        this.anInt1187 += (j << 1);
    }

    private void updateBankScrollPosition() {
        int height = 0;
        int tabSize = getTabSize(bankTab);
        if ((bankTab > 0) || (variousSettings[1010] == 1)) {
            height = (((tabSize / 10) + ((tabSize % 10) > 0 ? 1 : 0)) * 44) + 22;
        } else {
            height = getTabsHeight(getUsedTabs());
        }
        int bankSize = 0;
        for (int index = 0; index < 9; index++) {
            RSInterface tab = RSInterface.interfaceCache[50088 + index];
            if ((tab == null) || (tab.inv == null)) {
                continue;
            }

            for (int containerItem : tab.inv) {
                if (containerItem <= 0) {
                    break;
                }

                bankSize++;
            }
        }
        RSInterface.interfaceCache[5385].scrollMax = height < 209 ? 209 : height;
    }

    private int getTabsHeight(int tab) {
        int height = 0;
        for (int index = 0; index < tab; index++) {
            int tabSize = getTabSize(index);
            height += tabSize == 0 ? 0 : ((((tabSize / 10) + ((tabSize % 10) > 0 ? 1 : 0)) * 44) + 32);
        }
        return height;
    }

    private int getTabSize(int index) {
        RSInterface tab = RSInterface.interfaceCache[50088 + index];
        if ((tab == null) || (tab.inv == null)) {
            return 0;
        }
        int itemCount = 0;
        for (int containerItem : tab.inv) {
            if (containerItem > 0) {
                itemCount++;
            }
        }
        return itemCount;
    }

    private int getUsedTabs() {
        int height = 0;
        for (int index = 0; index < 9; index++) {
            if ((getTabSize(index) > 0) || ((index == 0) && (variousSettings[1010] == 1))) {
                height++;
            }
        }
        return height;
    }

    private void updateTabPreviews() {
        int usedTabs = getUsedTabs();
        for (int i = 51; i <= 59; i++) {
            if ((i - 51) > 0) {
                int preview = RSInterface.interfaceCache[RSInterface.BANK_CONTAINER + (i - 51)].inv[0];
                int amount = RSInterface.interfaceCache[RSInterface.BANK_CONTAINER + (i - 51)].invStackSizes[0];
                RSInterface.interfaceCache[50000 + i].inv[0] = preview;
                RSInterface.interfaceCache[50000 + i].invStackSizes[0] = amount;
                RSInterface.interfaceCache[50035 + ((i - 52) * 2)].hidden = ((i - 52) != 0) && (preview <= 0) && ((i - 52) != (usedTabs - 1));
            }
            variousSettings[(1000 + i) - 51] = (bankTab == (i - 51)) && (variousSettings[1010] != 1) ? 1 : 0;
        }
        if ((RSInterface.interfaceCache[RSInterface.BANK_CONTAINER + bankTab].inv[0] <= 0) && (RSInterface.interfaceCache[RSInterface.BANK_CONTAINER + bankTab].inv[1] <= 0)) {
            bankTab = 0;
        }
        RSInterface.interfaceCache[50062].hidden = usedTabs > 8;
        RSInterface.interfaceCache[50062].anInt263 = (usedTabs == 0) ? 0 : 48 * (usedTabs - 1);
    }

    public void switchGameframe(FrameType type) {
        if (type.equals(FrameType.FRAME_474)) {
            tabArea = new Sprite("tabarea2");
            needDrawTabArea = true;
            aBoolean1233 = true;
            drawRedStones();
            drawXpBar = false;
            Configuration.getInstance().get(CurrentFrame.class).setValue(FrameType.FRAME_474);
            if (isFullScreen && extraWidth >= 237) { drawLongTabs = true; }
        } else if (type.equals(FrameType.FRAME_525)) {
            tabArea = new Sprite("tabarea");
            needDrawTabArea = true;
            aBoolean1233 = true;
            Configuration.getInstance().get(CurrentFrame.class).setValue(FrameType.FRAME_525);
            if (isFullScreen && extraWidth >= 237) { drawLongTabs = true; }
        } else if (type.equals(FrameType.FRAME_554)) {
            drawRedStones();
            tabArea = new Sprite("tabarea3");
            Configuration.getInstance().get(CurrentFrame.class).setValue(FrameType.FRAME_554);
            if (isFullScreen && extraWidth >= 237) { drawLongTabs = true; }
        } else {
            switchGameframe(FrameType.FRAME_474);
        }
    }

    public void drawSpecialOrb() {
        int xPos = isFullScreen ? 722 + extraWidth : 0;
        int yPos = isFullScreen ? 117 : 83;
        int xAdd = (isFullScreen ? 0 : 516);
        int yAdd = (isFullScreen ? 2 : 0);
        if (super.mouseX > xPos + xAdd && super.mouseX < xPos + xAdd + 34 && super.mouseY > yPos + yAdd && super.mouseY < yPos + yAdd + 34) {
            specialSprites[3].drawAdvancedSprite(xPos, yPos);
        } else { specialSprites[1].drawAdvancedSprite(xPos, yPos); }
        try {
            String specialString = RSInterface.interfaceCache[26567].message;
            String[] parts = specialString.split("_");
            boolean active = Integer.valueOf(parts[0]) == 1;
            int specialAmount = Integer.valueOf(parts[1]);
            int amount = 100 - specialAmount;
            int percent = (int) (((double) amount / 100) * 27.0);
            orbFill = new Sprite("GameFrame/Orbs/Orb 2.png", 27, percent);
            specialSprites[2].drawAdvancedSprite(xPos + 3, yPos + 3);
            orbFill.drawSprite(xPos + 3, yPos + 3);
            if (active) { specialSprites[4].drawAdvancedSprite(xPos - 14, yPos - 16); }
            if (specialAmount == 100) { specialSprites[0].drawAdvancedSprite(xPos + 9, yPos + 7); } else {
                smallText.method382(active ? 0xffff00 : 0xffffff, xPos + 16, specialAmount + "", yPos + 21, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEventTimer() {
        long timeLeft = eventTimeTillStart - (System.currentTimeMillis() - eventStartTime);
        int secondsLeft = (int) (timeLeft / 1000) % 60;
        int minutesLeft = (int) (timeLeft / 1000 / 60);
        if (timeLeft < 0) { return "0:00"; }
        return minutesLeft + ":" + (secondsLeft < 10 ? "0" : "") + secondsLeft;
    }

    public void resetEvent() {
        eventMenuOpen = false;
        eventStarted = false;
        eventOpen = false;
        eventStartTime = 0;
        eventCategory = 0;
        eventGlowAlpha = 0.0;
        eventAlphaIncrease = false;
        eventTimeTillStart = 0;
        eventMessage = "Player Event";
        eventType = "";
    }

    public void fireEvent(String info) {
        String[] data = info.split(",");
        eventMessage = data[0];
        eventSafe = Boolean.valueOf(data[1]);
        //System.out.println("data: " + data[1]+ " safe: " + eventSafe);
        eventTimeTillStart = Integer.valueOf(data[2]) * 1000;
        eventType = (eventSafe ? "@gre@Safe Event" : "@red@Warning: Risk Zone");
        if (data.length == 4) { eventCategory = Integer.valueOf(data[3]); }
        eventStarted = true;
        if (eventTimeTillStart == 0) { eventOpen = true; }
        eventStartTime = System.currentTimeMillis();
    }

    public void drawEventOrb() {
        //System.out.println("X: " + super.mouseX + " Y: " + super.mouseY);
        int xPos = isFullScreen ? 722 + extraWidth : 6;
        int yPos = isFullScreen ? 25 : 124;
        int xAdd = (isFullScreen ? 0 : 516);
        int yAdd = (isFullScreen ? 2 : 0);
        if (super.mouseX > xPos + xAdd && super.mouseX < xPos + xAdd + 34 && super.mouseY > yPos + yAdd && super.mouseY < yPos + yAdd + 34) {
            eventSprites[4].drawAdvancedSprite(xPos, yPos);
        } else { eventSprites[2].drawAdvancedSprite(xPos, yPos); }
        eventSprites[3].drawAdvancedSprite(xPos + 3, yPos + 3);
        if (eventStarted && !eventOpen && !getEventTimer().equalsIgnoreCase("0:00")) {
            smallText.method382(0xffffff, xPos + 17, getEventTimer(), yPos + 21, true);
        } else { eventSprites[6].drawAdvancedSprite(xPos + 8, yPos + 6); }
        //if(eventStarted) {
        if (eventStarted) {
            eventSprites[5].drawAdvancedSprite(xPos - 14, yPos - 16, (int) eventGlowAlpha);
            if (eventGlowAlpha > 0) {
                if (eventAlphaIncrease) { eventGlowAlpha += (eventOpen ? 15.555555555 : 4.555555555); } else {
                    eventGlowAlpha -= (eventOpen ? 15.555555555 : 4.555555555);
                }
                if (eventGlowAlpha > 256) {
                    eventGlowAlpha = 256;
                    eventAlphaIncrease = false;
                }
                if (eventGlowAlpha < 0) {
                    eventGlowAlpha = 0;
                    eventAlphaIncrease = true;
                }
            } else {
                eventGlowAlpha = 0.1;
                eventAlphaIncrease = true;
            }
        } else {
            eventStarted = false;
            eventGlowAlpha = 0.0;
            eventAlphaIncrease = true;
        }
        //}
    }

    public int getOrbFill(int statusInt) {
        if (statusInt <= 100 && statusInt >= 97) {
            return 0;
        } else if (statusInt <= 96 && statusInt >= 93) {
            return 1;
        } else if (statusInt <= 92 && statusInt >= 89) {
            return 2;
        } else if (statusInt <= 88 && statusInt >= 85) {
            return 3;
        } else if (statusInt <= 84 && statusInt >= 81) {
            return 4;
        } else if (statusInt <= 80 && statusInt >= 77) {
            return 5;
        } else if (statusInt <= 76 && statusInt >= 73) {
            return 6;
        } else if (statusInt <= 72 && statusInt >= 69) {
            return 7;
        } else if (statusInt <= 68 && statusInt >= 65) {
            return 8;
        } else if (statusInt <= 64 && statusInt >= 61) {
            return 9;
        } else if (statusInt <= 60 && statusInt >= 57) {
            return 10;
        } else if (statusInt <= 56 && statusInt >= 53) {
            return 11;
        } else if (statusInt <= 52 && statusInt >= 49) {
            return 12;
        } else if (statusInt <= 48 && statusInt >= 45) {
            return 13;
        } else if (statusInt <= 44 && statusInt >= 41) {
            return 14;
        } else if (statusInt <= 40 && statusInt >= 37) {
            return 15;
        } else if (statusInt <= 36 && statusInt >= 33) {
            return 16;
        } else if (statusInt <= 32 && statusInt >= 29) {
            return 17;
        } else if (statusInt <= 28 && statusInt >= 25) {
            return 18;
        } else if (statusInt <= 24 && statusInt >= 21) {
            return 19;
        } else if (statusInt <= 20 && statusInt >= 17) {
            return 20;
        } else if (statusInt <= 16 && statusInt >= 13) {
            return 21;
        } else if (statusInt <= 12 && statusInt >= 9) {
            return 22;
        } else if (statusInt <= 8 && statusInt >= 7) {
            return 23;
        } else if (statusInt <= 6 && statusInt >= 5) {
            return 24;
        } else if (statusInt <= 4 && statusInt >= 3) {
            return 25;
        } else if (statusInt <= 2 && statusInt >= 1) {
            return 26;
        } else if (statusInt <= 0) {
            return 27;
        }
        return 0;
    }

    public void drawHPOrb() {
        String s1 = RSInterface.interfaceCache[4016].message;
        s1 = s1.replaceAll("%", "");
        int j = Integer.parseInt(s1);
        String s2 = RSInterface.interfaceCache[4017].message;
        int health;
        s2 = s2.replaceAll("%", "");
        int k = Integer.parseInt(s2);
        int i = (int) (((double) j / (double) k) * 100D);
        String cHP = RSInterface.interfaceCache[4016].message;
        //System.out.println("cHP is " + cHP);
        int currentHP = Integer.parseInt(cHP);
        String mHP = RSInterface.interfaceCache[4017].message;
        int maxHP2 = Integer.parseInt(mHP);
        health = (int) (((double) currentHP / (double) maxHP2) * 100D);
        int x = isFullScreen ? 363 + extraWidth : 0;
        int y = isFullScreen ? +32 : 0;

        int xPos = isFullScreen ? 387 + extraWidth : 0;
        int xPosTXT = isFullScreen ? 335 + extraWidth : 0;
        int yPos = isFullScreen ? +32 : 0;
        if (isFullScreen) {
            emptyOrb = new Sprite("GameFrame/Orbs/ORBS 12");
        } else {
            emptyOrb = new Sprite("GameFrame/Orbs/emptyorb");
        }
        orbFill = new Sprite("GameFrame/Orbs/Orb 2.png", 27, getOrbFill(health));
        emptyOrb.drawSprite(170 + x, 13 + y);
        /* Draws current HP text */
        if (/*health <= 100 && */health >= 75) {
            smallText.method382(65280, 213 + xPosTXT, cHP, 39 + yPos, true);
        } else if (health <= 74 && health >= 50) {
            smallText.method382(0xffff00, 213 + xPosTXT, cHP, 39 + yPos, true);
        } else if (health <= 49 && health >= 25) {
            smallText.method382(0xfca607, 213 + xPosTXT, cHP, 39 + yPos, true);
        } else if (health <= 24 && health >= 0) {
            smallText.method382(0xf50d0d, 213 + xPosTXT, cHP, 39 + yPos, true);
        }
        hitPointsFill.drawSprite(173 + xPos, 16 + yPos);
        orbFill.drawSprite(173 + xPos, 16 + yPos);
		/* Draws inside orb sprites */
        if (i <= 20) {
            if (loopCycle % 20 < 10) {
                hitPointsIcon.drawSprite(179 + xPos, 24 + yPos);
            }
        } else {
            hitPointsIcon.drawSprite(179 + xPos, 24 + yPos);
        }
    }

    public void drawPrayerOrb() {
        int prayer;
        String cPR = RSInterface.interfaceCache[4012].message;
        int currentPR = Integer.parseInt(cPR);
        String mPR = RSInterface.interfaceCache[4013].message;
        int maxPR2 = Integer.parseInt(mPR);
        prayer = (int) (((double) currentPR / (double) maxPR2) * 100D);
        String cP = RSInterface.interfaceCache[4012].message;
        int currentPrayer = Integer.parseInt(cP);
        String mP = RSInterface.interfaceCache[4013].message;
        int maxPrayer = Integer.parseInt(mP);
        prayer = (int) (((double) currentPrayer / (double) maxPrayer) * 100D);
        int x = isFullScreen ? 347 + extraWidth : 0;
        int y = isFullScreen ? +32 : 0;

        int xPos = isFullScreen ? 387 + extraWidth - 17 : 0;
        int xPosTXT = isFullScreen ? 335 + extraWidth - 17 : 0;
        int yPos = isFullScreen ? +32 : 0;
        orbFill = new Sprite("GameFrame/Orbs/Orb 2.png", 27, getOrbFill(prayer));
        emptyOrb.drawSprite(184 + x, 49 + y);
        if (prayer <= 100 && prayer >= 75) {
            smallText.method382(65280, 227 + xPosTXT, cP, 75 + yPos, true);
        } else if (prayer <= 74 && prayer >= 50) {
            smallText.method382(0xffff00, 227 + xPosTXT, cP, 75 + yPos, true);
        } else if (prayer <= 49 && prayer >= 25) {
            smallText.method382(0xfca607, 227 + xPosTXT, cP, 75 + yPos, true);
        } else if (prayer <= 24 && prayer >= 0) {
            smallText.method382(0xf50d0d, 227 + xPosTXT, cP, 75 + yPos, true);
        }
        prayerFill.drawSprite(187 + xPos, 52 + yPos);
        orbFill.drawSprite(187 + xPos, 52 + yPos);
        if (prayer <= 25) {
            if (loopCycle % 20 < 10) {
                prayerIcon.drawSprite(191 + xPos, 56 + yPos);
            }
        } else {
            prayerIcon.drawSprite(191 + xPos, 56 + yPos);
        }
    }

    public void drawSummOrb() {
        String summ = (RSInterface.interfaceCache[38760].message == null || RSInterface.interfaceCache[38760].message.equals("")) ? "0" : RSInterface.interfaceCache[38760].message;
        int x = isFullScreen ? 387 + extraWidth : 0;
        int y = isFullScreen ? +28 : 0;

        int xPos = isFullScreen ? 427 + extraWidth - 17 : 0;
        int xPosTXT = isFullScreen ? 375 + extraWidth - 17 : 0;
        int yPos = isFullScreen ? +28 : 0;
        orbFill = new Sprite("GameFrame/Orbs/Orb 2.png", 27, getOrbFill(Integer.parseInt(summ.replaceAll("%", ""))));

        emptyOrb.drawSprite(184 + x - 5, 49 + y + 72);
        smallText.method382(65280, 227 + xPosTXT - 5, summ, 75 + yPos + 72, true);

        prayerFill.drawSprite(187 + xPos - 5, 52 + yPos + 72);
        orbFill.drawSprite(187 + xPos - 5, 52 + yPos + 72);

        summIcon.drawSprite(194 + xPos - 5, 52 + yPos + 77); //drawSprite(194 + xPos - 5, 52 + yPos + 77);

    }

    public void drawRunOrb() {
        Sprite orb = (runClicked ? runOrb1 : runOrb2);
        Sprite icon = (runClicked ? runIcon1 : runIcon2);
        int x = isFullScreen ? 358 + extraWidth : 0;
        int y = isFullScreen ? +32 : 0;

        int x2 = isFullScreen ? extraWidth - 160 : 0;
        int y2 = isFullScreen ? +36 : 0;

        int xPos = isFullScreen ? 387 + extraWidth - 5 : 0;
        int xPosTXT = isFullScreen ? 335 + extraWidth - 5 : 0;
        int yPos = isFullScreen ? +32 : 0;
        if (isFullScreen) {
            hoveredEmpty = new Sprite("GameFrame/Orbs/1127");
        } else {
            hoveredEmpty = new Sprite("GameFrame/Orbs/hoveredempty");
        }
        emptyOrb.drawSprite(185 + x, 85 + y);
        if (super.mouseX > 713 + x2 && super.mouseX < 765 + x2 && super.mouseY > 83 + y2 && super.mouseY < 118 + y2) {
            hoveredEmpty.drawSprite(185 + x, 85 + y);
        }
        orb.drawSprite(188 + xPos, 88 + yPos);
        icon.drawSprite(195 + xPos, 93 + yPos);
        int colour;
        colour = 65280;
        smallText.method382(colour, 229 + xPosTXT, "100", 111 + yPos, true);
    }

    private void rightClickMapArea() {
        int x = isFullScreen ? extraWidth + 17 : 0;
        int y = isFullScreen ? -30 : 0;

        int xSumm = isFullScreen ? extraWidth - 130 : 0;
        int ySumm = 0;
        int x22 = isFullScreen ? extraWidth : 0;

        int x2 = isFullScreen ? extraWidth - 160 : 0;
        int y2 = isFullScreen ? +36 : 0;
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474)) {
            if (super.mouseX >= 742 + x22 && super.mouseX <= 764 + x22 && super.mouseY >= 1 && super.mouseY <= 23 && tabInterfaceIDs[10] != -1) {
                menuActionName[1] = "Logout";
                menuActionID[1] = 1004;
                menuActionRow = 2;
            } else if (super.mouseX >= 516 + x && super.mouseX <= 550 + x && super.mouseY >= 46 + y && super.mouseY < 80 + y) {
                menuActionName[2] = "Toggle XP Total";
                menuActionID[2] = 1503;
                menuActionName[1] = "Reset XP Total";
                menuActionID[1] = 1504;
                menuActionRow = 3;
            } else if (super.mouseX >= (isFullScreen ? 708 : 521) + x && super.mouseX <= (isFullScreen ? 708 : 521) + 34 + x && super.mouseY >= (isFullScreen ? 117 + 34 : 83) + y && super.mouseY < (isFullScreen ? 117 + 34 : 83) + 34 + y) {
                menuActionName[1] = "Activate Special";
                menuActionID[1] = 1613;
                menuActionRow = 2;
            } else if (super.mouseX >= (isFullScreen ? 708 : 521) + x && super.mouseX <= (isFullScreen ? 708 : 521) + 34 + x && super.mouseY >= (isFullScreen ? 57 : 124) + y && super.mouseY < (isFullScreen ? 57 : 124) + 34 + y) {
                menuActionName[1] = "Open Event";
                menuActionID[1] = 1513;
                menuActionRow = 2;
            } else if (eventOpen && super.mouseX >= 373 + x && super.mouseX <= 373 + 58 + x && super.mouseY >= 146 + y && super.mouseY < 146 + 24 + y) {
                menuActionName[1] = "Accept";
                menuActionID[1] = 1514;
                menuActionRow = 2;
            } else if (eventOpen && super.mouseX >= 373 + 61 + x && super.mouseX <= 373 + 61 + 58 + x && super.mouseY >= 146 + y && super.mouseY < 146 + 24 + y) {
                menuActionName[1] = "Decline";
                menuActionID[1] = 1515;
                menuActionRow = 2;
            } else if (super.mouseX > 724 + x22 && super.mouseX < 741 + x22 && super.mouseY > 1 && super.mouseY < 23) {
                menuActionName[1] = "Advisor";
                menuActionID[1] = 1005;
                menuActionRow = 2;
            } else if (super.mouseX > 713 + x2 && super.mouseX < 765 + x2 && super.mouseY > 83 + y2 && super.mouseY < 118 + y2) {

                menuActionID[1] = 1050;
                menuActionName[1] = "Rest";
                menuActionName[2] = "Run";
                menuActionID[2] = 1051;
                menuActionRow = 3;
            } else if (super.mouseX >= 695 + xSumm && super.mouseX <= 751 + ySumm && super.mouseY >= 120 + xSumm && super.mouseY < 150 + ySumm) {
                menuActionName[1] = "Call follower";
                menuActionID[1] = 1755;
                menuActionRow = 2;
                menuActionName[2] = "Dismiss";
                menuActionID[2] = 1754;
                menuActionRow = 3;
                menuActionName[3] = "Special";
                menuActionID[3] = 1753;
                menuActionRow = 4;

            }
        }
    }

    public void drawHoverBox(int xPos, int yPos, String text) {
        String[] results = text.split("\n");
        int height = (results.length * 16) + 6;
        int width;
        width = smallText.getTextWidth(results[0]) + 6;
        for (int i = 1; i < results.length; i++) {
            if (width <= smallText.getTextWidth(results[i]) + 6) { width = smallText.getTextWidth(results[i]) + 6; }
        }
        DrawingArea.drawPixels(height, yPos, xPos, 0xFFFFA0, width);
        DrawingArea.drawUnfilledPixels(xPos, width, height, 0, yPos);
        yPos += 14;
        for (int i = 0; i < results.length; i++) {
            smallText.method389(false, xPos + 3, 0, results[i], yPos);
            yPos += 16;
        }
    }

    public final String methodR(/* int i, */int j) {
        // if(i <= 0)
        // pktType = inStream.readUByte();
        if (j >= 0 && j < 10000) { return String.valueOf(j); }
        if (j >= 10000 && j < 10000000) { return j / 1000 + "K"; }
        if (j >= 10000000 && j < 999999999) { return j / 1000000 + "M"; }
        if (j >= 999999999) { return "*"; } else { return "?"; }
    }

    private boolean menuHasAddFriend(int j) {
        if (j < 0) { return false; }
        int k = menuActionID[j];
        if (k >= 2000) { k -= 2000; }
        return k == 337;
    }

    public void drawChannelButtons() {
        int y = isFullScreen ? extraHeight + 334 : 0;
        String text[] = {"On", "Friends", "Off", "Hide"};
        int textColor[] = {65280, 0xffff00, 0xff0000, 65535};
        if (displayChat) {
            switch (cButtonCPos) {
                case 0:
                    chatButtons[1].drawSprite(5, 142 + y);
                    break;
                case 1:
                    chatButtons[1].drawSprite(71, 142 + y);
                    break;
                case 2:
                    chatButtons[1].drawSprite(137, 142 + y);
                    break;
                case 3:
                    chatButtons[1].drawSprite(203, 142 + y);
                    break;
                case 4:
                    chatButtons[1].drawSprite(269, 142 + y);
                    break;
                case 5:
                    chatButtons[1].drawSprite(335, 142 + y);
                    break;
            }
            if (cButtonHPos == cButtonCPos) {
                switch (cButtonHPos) {
                    case 0:
                        chatButtons[2].drawSprite(5, 142 + y);
                        break;
                    case 1:
                        chatButtons[2].drawSprite(71, 142 + y);
                        break;
                    case 2:
                        chatButtons[2].drawSprite(137, 142 + y);
                        break;
                    case 3:
                        chatButtons[2].drawSprite(203, 142 + y);
                        break;
                    case 4:
                        chatButtons[2].drawSprite(269, 142 + y);
                        break;
                    case 5:
                        chatButtons[2].drawSprite(335, 142 + y);
                        break;
                    case 6:
                        chatButtons[3].drawSprite(404, 142 + y);
                        break;
                }
            } else {
                switch (cButtonHPos) {
                    case 0:
                        chatButtons[0].drawSprite(5, 142 + y);
                        break;
                    case 1:
                        chatButtons[0].drawSprite(71, 142 + y);
                        break;
                    case 2:
                        chatButtons[0].drawSprite(137, 142 + y);
                        break;
                    case 3:
                        chatButtons[0].drawSprite(203, 142 + y);
                        break;
                    case 4:
                        chatButtons[0].drawSprite(269, 142 + y);
                        break;
                    case 5:
                        chatButtons[0].drawSprite(335, 142 + y);
                        break;
                    case 6:
                        chatButtons[3].drawSprite(404, 142 + y);
                        break;
                }
            }
            smallText.method389(true, 425, 0xffffff, "Request Help", 157 + y);
            smallText.method389(true, 26, 0xffffff, "All", 157 + y);
            smallText.method389(true, 86, 0xffffff, "Game", 157 + y);
            smallText.method389(true, 150, 0xffffff, "Public", 152 + y);
            smallText.method389(true, 212, 0xffffff, "Private", 152 + y);
            smallText.method389(true, 286, 0xffffff, "Clan", 152 + y);
            smallText.method389(true, 349, 0xffffff, "Trade", 152 + y);
            smallText.method382(textColor[publicChatMode], 164, text[publicChatMode], 163 + y, true);
            smallText.method382(textColor[privateChatMode], 230, text[privateChatMode], 163 + y, true);
            smallText.method382(textColor[clanChatMode], 296, text[clanChatMode], 163 + y, true);
            smallText.method382(textColor[tradeMode], 362, text[tradeMode], 163 + y, true);
        }
    }    private void stopMidi() {
        Signlink.midifade = 0;
        Signlink.midi = "stop";
    }

    private void drawChatArea() {
        int color = 0;
        int yPosOffset = isFullScreen ? clientHeight - 166 : 0;
        int xPosOffset = isFullScreen ? 0 : 0;
        if (!isFullScreen) { aRSImageProducer_1166.initializeDrawingArea(); }
        Texture.anIntArray1472 = anIntArray1180;
        int y = isFullScreen ? extraHeight + 334 : 0;
        if (isFullScreen && displayChat) { chatArea.drawSprite2(0, 0 + y); } else if (!isFullScreen) {
            chatArea.drawSprite(0, 0);
            DrawingArea.drawPixelsWithOpacity(0, 0, 520, 334, 60, 0);
        }
        drawChannelButtons();
        TextDrawingArea textDrawingArea = aTextDrawingArea_1271;

        if (displayChat || !isFullScreen) {
            if (itemSearch) {
                int delay = 10;
                int j77 = -3;
                int j = 0;
                DrawingArea.setDrawingArea(122 + y, 8, 497, 7 + y);
                DrawingArea.method341(1, 0x807660, 120, 95);

                for (int i = 0; i < itemSearchResults.length; i++) {
                    ItemDef itemDef = itemSearchResults[i];
                    if (itemDef != null) {
                        int yPos = (70 - j77 * 14) + anInt1089 + 5;
                        if (yPos > 0 && yPos < 210) {
                            if (super.mouseX > 115 && super.mouseX < 300 && (super.mouseY - 334) > (yPos + y - 13) && (super.mouseY - 334) < (yPos + y + 2)) {
                                DrawingArea.drawPixels(15, yPos + y - 13, 96, 0xC0B092, 402);
                                Sprite sprite = ItemDef.getItemSprite(itemDef.id);
                                if (sprite != null) { sprite.drawSprite(33, 20); }
                            }
                            textDrawingArea.method385(0x000, itemDef.name, yPos + y, 98);
                        }
                        j++;
                        j77++;
                    }
                }

                DrawingArea.defaultDrawingAreaSize();
                anInt1211 = j * 14 + 7 + 5;
                if (anInt1211 < 111) { anInt1211 = 111; }
                drawScrollbar(114, anInt1211 - anInt1089 - 113, 7 + y, 496, anInt1211, 0);
                String s = "Item Search";
                textDrawingArea.method385(color, s + ":", 133 + y, 11);
                textDrawingArea.drawChatInput(255, 12 + textDrawingArea.getTextWidth(s + ": "), geSearchInput + (loopCycle % 40 < 20 ? "|" : ""), 133 + y, false);
                DrawingArea.method339(121 + y, 0x807660, 506, 7);
                if ((openInterfaceID != 22670 && delay == 0) || openInterfaceID == -1) { itemSearch = false; }
                if (delay > 0) { delay--; }
            } else if (messagePromptRaised) {
                chatTextDrawingArea.drawText(0, aString1121, 60 + y, 259);
                chatTextDrawingArea.drawText(128, promptInput + "*", 80 + y, 259);
            } else if (inputDialogState == 1) {
                chatTextDrawingArea.drawText(0, aString1121 + ":", 60 + y, 259);
                chatTextDrawingArea.drawText(128, amountOrNameInput + "*", 80 + y, 259);
            } else if (inputDialogState == 2) {
                chatTextDrawingArea.drawText(0, aString1121 + ":", 60 + y, 259);
                chatTextDrawingArea.drawText(128, amountOrNameInput + "*", 80 + y, 259);
            } else if (aString844 != null) {
                chatTextDrawingArea.drawText(0, aString844, 60 + y, 259);
                chatTextDrawingArea.drawText(128, "Click to continue", 80 + y, 259);
            } else if (backDialogID != -1) {
                drawInterface(0, 20, RSInterface.interfaceCache[backDialogID], 20 + y);
            } else if (dialogID != -1d) {
                drawInterface(0, 20, RSInterface.interfaceCache[dialogID], 20 + y);
            } else {
                int j77 = -3;
                int j = 0;
                DrawingArea.setDrawingArea(122 + y, 8, 497, 7 + y);

                for (int k = 0; k < 500; k++) {
                    if (chatMessages[k] != null) {
                        int chatType = chatTypes[k];
                        int yPos = (70 - j77 * 14) + anInt1089 + 5;
                        String s1 = chatNames[k];
                        byte byte0 = (byte) (chatRights[k]);
                        if (chatType == 0) {
                            if (chatTypeView == 5 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    textDrawingArea.method389(false, 11, 0, chatMessages[k], yPos + y);// chat
                                }
                                // color
                                // enabled
                                j++;
                                j77++;
                            }
                        }
						/* Draws clickable URL text into chatarea */
                        if (chatType == 9) {
                            if (chatTypeView == 5 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    textDrawingArea.method389(false, 11, 0, chatMessages[k] + "@blu@" + s1 + "@bla@.", yPos + y);
                                }
                                j++;
                                j77++;
                            }
                        }
                        if ((chatType == 1 || chatType == 2) && (chatType == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s1))) {
                            if (chatTypeView == 1 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    int xPos = 11;
                                    if (byte0 > 0) {
                                        modIcons[byte0].drawBackground(xPos - 1, yPos - 5 - (modIcons[byte0].anInt1453 / 2) + y);
                                        xPos += modIcons[byte0].anInt1452;
                                    }
                                    textDrawingArea.method389(false, xPos, color, s1 + ":", yPos + y);
                                    xPos += textDrawingArea.getTextWidth(s1) + 8;
                                    textDrawingArea.method389(false, xPos, 255, chatMessages[k], yPos + y);
                                }
                                j++;
                                j77++;
                            }
                        }
                        if ((chatType == 3 || chatType == 7) && (splitPrivateChat == 0 || chatTypeView == 2) && (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s1))) {
                            if (chatTypeView == 2 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    int k1 = 11;
                                    textDrawingArea.method385(0, "From", yPos + y, k1);
                                    k1 += textDrawingArea.getTextWidth("From ");
                                    if (byte0 > 0) {
                                        modIcons[byte0].drawBackground(k1, yPos - 12 + y);
                                        k1 += modIcons[byte0].anInt1452;
                                    }
                                    textDrawingArea.method385(color, s1 + ":", yPos + y, k1);
                                    k1 += textDrawingArea.getTextWidth(s1) + 8;
                                    textDrawingArea.method385(0x800000, chatMessages[k], yPos + y, k1);
                                }
                                j++;
                                j77++;
                            }
                        }
                        if (chatType == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
                            if (chatTypeView == 3 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    textDrawingArea.method385(0x800080, s1 + " " + chatMessages[k], yPos + y, 11);
                                }
                                j++;
                                j77++;
                            }
                        }
                        if (chatType == 5 && splitPrivateChat == 0 && privateChatMode < 2) {
                            if (chatTypeView == 2 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    textDrawingArea.method385(0x800000, chatMessages[k], yPos + y, 11);
                                }
                                j++;
                                j77++;
                            }
                        }
                        if (chatType == 6 && (splitPrivateChat == 0 || chatTypeView == 2) && privateChatMode < 2) {
                            if (chatTypeView == 2 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    textDrawingArea.method385(0, "To " + s1 + ":", yPos + y, 11);
                                    textDrawingArea.method385(0x800000, chatMessages[k], yPos + y, 15 + textDrawingArea.getTextWidth("To :" + s1));
                                }
                                j++;
                                j77++;
                            }
                        }
                        if (chatType == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
                            if (chatTypeView == 3 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    textDrawingArea.method385(0x7e3200, s1 + " " + chatMessages[k], yPos + y, 11);
                                }
                                j++;
                                j77++;
                            }
                            if (chatType == 11 && (clanChatMode == 0)) {
                                if (chatTypeView == 11) {
                                    if (yPos > 0 && yPos < 210) {
                                        textDrawingArea.method385(0x7e3200, s1 + " " + chatMessages[k], yPos + y, 11);
                                    }
                                    j++;
                                    j77++;
                                }
                                if (chatType == 12) {
                                    if (yPos > 0 && yPos < 110) {
                                        textDrawingArea.method385(0x7e3200, chatMessages[k] + " @blu@" + s1, yPos + y, 11);
                                    }
                                    j++;
                                }
                            }
                        }
                        if (chatType == 16) {
                            int j2 = 40;
                            int clanNameWidth = textDrawingArea.getTextWidth(clanname);
                            if (chatTypeView == 11 || chatTypeView == 0) {
                                if (yPos > 0 && yPos < 210) {
                                    switch (chatRights[k]) {
                                        case 1:
                                            j2 += clanNameWidth;
                                            modIcons[10].drawBackground(j2 - 18, yPos - 12 + (modIcons[10].anInt1453 / 2) + y);
                                            j2 += modIcons[10].anInt1452;
                                            break;

                                        case 2:
                                            j2 += clanNameWidth;
                                            modIcons[15].drawBackground(j2 - 18, yPos - 12 + ((15 - modIcons[15].anInt1453) / 2) + y);
                                            j2 += modIcons[15].anInt1452;
                                            break;

                                        default:
                                            j2 += clanNameWidth;
                                            break;
                                    }
                                }
                                textDrawingArea.method385(0, "[", yPos + y, 8);
                                textDrawingArea.method385(255, "" + clanname + "", yPos + y, 14);
                                textDrawingArea.method385(0, "]", yPos + y, clanNameWidth + 14);

                                textDrawingArea.method385(0, chatNames[k] + ":", yPos + y, j2 - 17); // j2
                                j2 += textDrawingArea.getTextWidth(chatNames[k]) + 7;
                                textDrawingArea.method385(0x800000, chatMessages[k], yPos + y, j2 - 16);// j2
                                j++;
                                j77++;
                            }
                        }
                    }
                }
                DrawingArea.defaultDrawingAreaSize();
                anInt1211 = j * 14 + 7 + 5;
                if (anInt1211 < 111) { anInt1211 = 111; }
                drawScrollbar(114, anInt1211 - anInt1089 - 113, 7 + y, 496, anInt1211, 0);
                String s;
                if (myPlayer != null && myPlayer.name != null) { s = myPlayer.name; } else {
                    s = TextClass.fixName(Configuration.getInstance().getValue(Username.class));
                }
                textDrawingArea.method385(color, s + ":", 133 + y, 11);
                textDrawingArea.drawChatInput(255, 12 + textDrawingArea.getTextWidth(s + ": "), inputString + "*", 133 + y, false);
                DrawingArea.method339(121 + y, 0x807660, 506, 7);
            }
            if (menuOpen && menuScreenArea == 2) {
                drawMenu();
            }
            if (!isFullScreen) {
                aRSImageProducer_1166.drawGraphics(338 + extraHeight, isFullScreen ? bufferGraphics : super.graphics, 0);
            }
            aRSImageProducer_1165.initializeDrawingArea();
            Texture.anIntArray1472 = anIntArray1182;
        }
    }

    private void processMenuClick() {
        if (activeInterfaceType != 0) { return; }
        int j = super.clickMode3;
        if (spellSelected == 1 && super.saveClickX >= 503 && super.saveClickY >= 160 && super.saveClickX <= 765 && super.saveClickY <= 205) {
            j = 0;
        }
        if (menuOpen) {
            if (j != 1) {
                int k = super.mouseX;
                int j1 = super.mouseY;
                if (menuScreenArea == 0) {
                    k -= 4;
                    j1 -= 4;
                }
                if (menuScreenArea == 1) {
                    k -= 516;// 519
                    j1 -= 168;
                }
                if (menuScreenArea == 2) {
                    k -= 5;
                    j1 -= 338;
                }
                if (menuScreenArea == 3) {
                    k -= 516;// 519
                    j1 -= 0;
                }
                if (k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10 || j1 < menuOffsetY - 10 || j1 > menuOffsetY + menuHeight + 10) {
                    menuOpen = false;
                    if (menuScreenArea == 1) { needDrawTabArea = true; }
                    if (menuScreenArea == 2) { inputTaken = true; }
                }
            }
            if (j == 1) {
                int l = menuOffsetX;
                int k1 = menuOffsetY;
                int i2 = menuWidth;
                int k2 = super.saveClickX;
                int l2 = super.saveClickY;
                if (menuScreenArea == 0) {
                    k2 -= 4;
                    l2 -= 4;
                }
                if (menuScreenArea == 1) {
                    k2 -= 516;// 519
                    l2 -= 168;
                }
                if (menuScreenArea == 2) {
                    k2 -= 5;// 17
                    l2 -= 338;
                }
                if (menuScreenArea == 3) {
                    k2 -= 516;// 519
                    l2 -= 0;
                }
                int i3 = -1;
                for (int j3 = 0; j3 < menuActionRow; j3++) {
                    int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
                    if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3) { i3 = j3; }
                }
                // System.out.println(i3);
                if (i3 != -1) { doAction(i3); }
                menuOpen = false;
                if (menuScreenArea == 1) { needDrawTabArea = true; }
                if (menuScreenArea == 2) {
                    inputTaken = true;
                }
            }
        } else {
            if (j == 1 && menuActionRow > 0) {
                int i1 = menuActionID[menuActionRow - 1];
                if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493 || i1 == 847 || i1 == 447 || i1 == 1125) {
                    int l1 = menuActionCmd2[menuActionRow - 1];
                    int j2 = menuActionCmd3[menuActionRow - 1];
                    RSInterface class9 = RSInterface.interfaceCache[j2];
                    if (class9.aBoolean259 || class9.deletesTargetSlot) {
                        aBoolean1242 = false;
                        anInt989 = 0;
                        anInt1084 = j2;
                        anInt1085 = l1;
                        fromTab = class9.id;
                        activeInterfaceType = 2;
                        anInt1087 = super.saveClickX;
                        anInt1088 = super.saveClickY;
                        if (RSInterface.interfaceCache[j2].parentID == openInterfaceID) { activeInterfaceType = 1; }
                        if (RSInterface.interfaceCache[j2].parentID == backDialogID) { activeInterfaceType = 3; }
                        return;
                    }
                }
            }
            if (j == 1 && (anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2) { j = 2; }
            if (j == 1 && menuActionRow > 0) { doAction(menuActionRow - 1); }
            if (j == 2 && menuActionRow > 0) { determineMenuSize(); }

        }
    }

    private void saveMidi(boolean flag, byte abyte0[]) {
        Signlink.midifade = flag ? 1 : 0;
        Signlink.midisave(abyte0, abyte0.length);
    }

    private void method22() {
        try {
            anInt985 = -1;
            aClass19_1056.removeAll();
            aClass19_1013.removeAll();
            Texture.method366();
            unlinkMRUNodes();
            worldController.initToNull();
            System.gc();
            for (int i = 0; i < 4; i++) { aClass11Array1230[i].method210(); }

            for (int l = 0; l < 4; l++) {
                for (int k1 = 0; k1 < 104; k1++) {
                    for (int j2 = 0; j2 < 104; j2++) { byteGroundArray[l][k1][j2] = 0; }

                }

            }

            ObjectManager objectManager = new ObjectManager(byteGroundArray, intGroundArray);
            int k2 = aByteArrayArray1183.length;
            stream.createFrame(0);
            if (!aBoolean1159) {
                for (int i3 = 0; i3 < k2; i3++) {
                    int i4 = (anIntArray1234[i3] >> 8) * 64 - baseX;
                    int k5 = (anIntArray1234[i3] & 0xff) * 64 - baseY;
                    byte abyte0[] = aByteArrayArray1183[i3];
                    if (abyte0 != null) {
                        objectManager.method180(abyte0, k5, i4, (anInt1069 - 6) * 8, (anInt1070 - 6) * 8, aClass11Array1230);
                    }
                }

                for (int j4 = 0; j4 < k2; j4++) {
                    int l5 = (anIntArray1234[j4] >> 8) * 64 - baseX;
                    int k7 = (anIntArray1234[j4] & 0xff) * 64 - baseY;
                    byte abyte2[] = aByteArrayArray1183[j4];
                    if (abyte2 == null && anInt1070 < 800) { objectManager.method174(k7, 64, 64, l5); }
                }

                anInt1097++;
                if (anInt1097 > 160) {
                    anInt1097 = 0;
                    stream.createFrame(238);
                    stream.writeByte(96);
                }
                stream.createFrame(0);
                for (int i6 = 0; i6 < k2; i6++) {
                    byte abyte1[] = aByteArrayArray1247[i6];
                    if (abyte1 != null) {
                        int l8 = (anIntArray1234[i6] >> 8) * 64 - baseX;
                        int k9 = (anIntArray1234[i6] & 0xff) * 64 - baseY;
                        objectManager.method190(l8, aClass11Array1230, k9, worldController, abyte1);
                    }
                }

            }
            if (aBoolean1159) {
                for (int j3 = 0; j3 < 4; j3++) {
                    for (int k4 = 0; k4 < 13; k4++) {
                        for (int j6 = 0; j6 < 13; j6++) {
                            int l7 = anIntArrayArrayArray1129[j3][k4][j6];
                            if (l7 != -1) {
                                int i9 = l7 >> 24 & 3;
                                int l9 = l7 >> 1 & 3;
                                int j10 = l7 >> 14 & 0x3ff;
                                int l10 = l7 >> 3 & 0x7ff;
                                int j11 = (j10 / 8 << 8) + l10 / 8;
                                for (int l11 = 0; l11 < anIntArray1234.length; l11++) {
                                    if (anIntArray1234[l11] != j11 || aByteArrayArray1183[l11] == null) { continue; }
                                    objectManager.method179(i9, l9, aClass11Array1230, k4 * 8, (j10 & 7) * 8, aByteArrayArray1183[l11], (l10 & 7) * 8, j3, j6 * 8);
                                    break;
                                }

                            }
                        }

                    }

                }

                for (int l4 = 0; l4 < 13; l4++) {
                    for (int k6 = 0; k6 < 13; k6++) {
                        int i8 = anIntArrayArrayArray1129[0][l4][k6];
                        if (i8 == -1) { objectManager.method174(k6 * 8, 8, 8, l4 * 8); }
                    }

                }

                stream.createFrame(0);
                for (int l6 = 0; l6 < 4; l6++) {
                    for (int j8 = 0; j8 < 13; j8++) {
                        for (int j9 = 0; j9 < 13; j9++) {
                            int i10 = anIntArrayArrayArray1129[l6][j8][j9];
                            if (i10 != -1) {
                                int k10 = i10 >> 24 & 3;
                                int i11 = i10 >> 1 & 3;
                                int k11 = i10 >> 14 & 0x3ff;
                                int i12 = i10 >> 3 & 0x7ff;
                                int j12 = (k11 / 8 << 8) + i12 / 8;
                                for (int k12 = 0; k12 < anIntArray1234.length; k12++) {
                                    if (anIntArray1234[k12] != j12 || aByteArrayArray1247[k12] == null) { continue; }
                                    // objectManager.method183(aClass11Array1230,
                                    // worldController, k10, j8 * 8, (i12 & 7) *
                                    // 8, l6, aByteArrayArray1247[k12], (k11 &
                                    // 7) * 8, i11, j9 * 8);
                                    byte abyte0[] = aByteArrayArray1247[k12];
                                    objectManager.method183(aClass11Array1230, worldController, k10, j8 * 8, (i12 & 7) * 8, l6, aByteArrayArray1247[k12], (k11 & 7) * 8, i11, j9 * 8);
                                    break;
                                }

                            }
                        }

                    }

                }

            }
            stream.createFrame(0);
            objectManager.method171(aClass11Array1230, worldController);
            aRSImageProducer_1165.initializeDrawingArea();
            stream.createFrame(0);
            int k3 = ObjectManager.anInt145;
            if (k3 > plane) { k3 = plane; }
            if (k3 < plane - 1) { k3 = plane - 1; }
            if (lowMem) { worldController.method275(ObjectManager.anInt145); } else { worldController.method275(0); }
            for (int i5 = 0; i5 < 104; i5++) {
                for (int i7 = 0; i7 < 104; i7++) { spawnGroundItem(i5, i7); }

            }

            anInt1051++;
            if (anInt1051 > 98) {
                anInt1051 = 0;
                stream.createFrame(150);
            }
            method63();
        } catch (Exception exception) {
        }
        ObjectDef.mruNodes1.unlinkAll();
        // if (super.gameFrame != null) {
        stream.createFrame(210);
        stream.writeInt(0x3f008edd);
        // }
        System.gc();
        Texture.method367();
        onDemandFetcher.method566();
        int k = (anInt1069 - 6) / 8 - 1;
        int j1 = (anInt1069 + 6) / 8 + 1;
        int i2 = (anInt1070 - 6) / 8 - 1;
        int l2 = (anInt1070 + 6) / 8 + 1;
        if (aBoolean1141) {
            k = 49;
            j1 = 50;
            i2 = 49;
            l2 = 50;
        }
        for (int l3 = k; l3 <= j1; l3++) {
            for (int j5 = i2; j5 <= l2; j5++) {
                if (l3 == k || l3 == j1 || j5 == i2 || j5 == l2) {
                    int j7 = onDemandFetcher.method562(0, j5, l3);
                    if (j7 != -1) { onDemandFetcher.method560(j7, 3); }
                    int k8 = onDemandFetcher.method562(1, j5, l3);
                    if (k8 != -1) { onDemandFetcher.method560(k8, 3); }
                }
            }

        }

    }    public void startRunnable(Runnable runnable, int i) {
        if (i > 10) { i = 10; }
        if (Signlink.mainapp != null) {
            Signlink.startthread(runnable, i);
        } else {
            super.startRunnable(runnable, i);
        }
    }

    private void unlinkMRUNodes() {
        ObjectDef.mruNodes1.unlinkAll();
        ObjectDef.mruNodes2.unlinkAll();
        EntityDef.mruNodes.unlinkAll();
        ItemDef.mruNodes2.unlinkAll();
        ItemDef.mruNodes1.unlinkAll();
        Player.mruNodes.unlinkAll();
        SpotAnim.aMRUNodes_415.unlinkAll();
    }    public Socket openSocket(int port) throws IOException {
        return new Socket(InetAddress.getByName(Constants.SERVER_TYPE.getServer().getProtocol()), port);
    }

    private void method24(int i) {
        int ai[] = aClass30_Sub2_Sub1_Sub1_1263.myPixels;
        int j = ai.length;
        for (int k = 0; k < j; k++) { ai[k] = 0; }

        for (int l = 1; l < 103; l++) {
            int i1 = 24628 + (103 - l) * 512 * 4;
            for (int k1 = 1; k1 < 103; k1++) {
                if ((byteGroundArray[i][k1][l] & 0x18) == 0) { worldController.method309(ai, i1, i, k1, l); }
                if (i < 3 && (byteGroundArray[i + 1][k1][l] & 8) != 0) {
                    worldController.method309(ai, i1, i + 1, k1, l);
                }
                i1 += 4;
            }

        }

        int j1 = ((238 + (int) (Math.random() * 20D)) - 10 << 16) + ((238 + (int) (Math.random() * 20D)) - 10 << 8) + ((238 + (int) (Math.random() * 20D)) - 10);
        int l1 = (238 + (int) (Math.random() * 20D)) - 10 << 16;
        aClass30_Sub2_Sub1_Sub1_1263.method343();
        for (int i2 = 1; i2 < 103; i2++) {
            for (int j2 = 1; j2 < 103; j2++) {
                if ((byteGroundArray[i][j2][i2] & 0x18) == 0) { method50(i2, j1, j2, l1, i); }
                if (i < 3 && (byteGroundArray[i + 1][j2][i2] & 8) != 0) { method50(i2, j1, j2, l1, i + 1); }
            }

        }

        aRSImageProducer_1165.initializeDrawingArea();
        anInt1071 = 0;
        for (int k2 = 0; k2 < 104; k2++) {
            for (int l2 = 0; l2 < 104; l2++) {
                int i3 = worldController.method303(plane, k2, l2);
                if (i3 != 0) {
                    i3 = i3 >> 14 & 0x7fff;
                    int j3 = ObjectDef.forID(i3).anInt746;
                    if (j3 >= 0) {
                        int k3 = k2;
                        int l3 = l2;
                        if (j3 != 22 && j3 != 29 && j3 != 34 && j3 != 36 && j3 != 46 && j3 != 47 && j3 != 48) {
                            byte byte0 = 104;
                            byte byte1 = 104;
                            int ai1[][] = aClass11Array1230[plane].anIntArrayArray294;
                            for (int i4 = 0; i4 < 10; i4++) {
                                int j4 = (int) (Math.random() * 4D);
                                if (j4 == 0 && k3 > 0 && k3 > k2 - 3 && (ai1[k3 - 1][l3] & 0x1280108) == 0) { k3--; }
                                if (j4 == 1 && k3 < byte0 - 1 && k3 < k2 + 3 && (ai1[k3 + 1][l3] & 0x1280180) == 0) {
                                    k3++;
                                }
                                if (j4 == 2 && l3 > 0 && l3 > l2 - 3 && (ai1[k3][l3 - 1] & 0x1280102) == 0) { l3--; }
                                if (j4 == 3 && l3 < byte1 - 1 && l3 < l2 + 3 && (ai1[k3][l3 + 1] & 0x1280120) == 0) {
                                    l3++;
                                }
                            }

                        }
                        aClass30_Sub2_Sub1_Sub1Array1140[anInt1071] = mapFunctions[j3];
                        anIntArray1072[anInt1071] = k3;
                        anIntArray1073[anInt1071] = l3;
                        anInt1071++;
                    }
                }
            }

        }

    }

    private void spawnGroundItem(int i, int j) {
        NodeList class19 = groundArray[plane][i][j];
        if (class19 == null) {
            worldController.method295(plane, i, j);
            return;
        }
        int k = 0xfa0a1f01;
        Object obj = null;
        for (Item item = (Item) class19.reverseGetFirst(); item != null; item = (Item) class19.reverseGetNext()) {
            ItemDef itemDef = ItemDef.forID(item.ID);
            int l = itemDef.value;
            if (itemDef.stack) { l *= item.anInt1559 + 1; }
            // notifyItemSpawn(item, i + baseX, j + baseY);

            if (l > k) {
                k = l;
                obj = item;
            }
        }

        class19.insertTail(((Node) (obj)));
        Object obj1 = null;
        Object obj2 = null;
        for (Item class30_sub2_sub4_sub2_1 = (Item) class19.reverseGetFirst(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 = (Item) class19.reverseGetNext()) {
            if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID && obj1 == null) { obj1 = class30_sub2_sub4_sub2_1; }
            if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID && class30_sub2_sub4_sub2_1.ID != ((Item) (obj1)).ID && obj2 == null) {
                obj2 = class30_sub2_sub4_sub2_1;
            }
        }

        int i1 = i + (j << 7) + 0x60000000;
        worldController.method281(i, i1, ((Animable) (obj1)), method42(plane, j * 128 + 64, i * 128 + 64), ((Animable) (obj2)), ((Animable) (obj)), plane, j);
    }

    private void method26(boolean flag) {
        for (int j = 0; j < npcCount; j++) {
            NPC npc = npcArray[npcIndices[j]];
            int k = 0x20000000 + (npcIndices[j] << 14);
            if (npc == null || !npc.isVisible() || npc.desc.aBoolean93 != flag) { continue; }
            int l = npc.x >> 7;
            int i1 = npc.y >> 7;
            if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104) { continue; }
            if (npc.anInt1540 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
                if (anIntArrayArray929[l][i1] == anInt1265) { continue; }
                anIntArrayArray929[l][i1] = anInt1265;
            }
            if (!npc.desc.aBoolean84) { k += 0x80000000; }
            worldController.method285(plane, npc.anInt1552, method42(plane, npc.y, npc.x), k, npc.y, (npc.anInt1540 - 1) * 64 + 60, npc.x, npc, npc.aBoolean1541);
        }
    }

    private boolean replayWave() {
        return Signlink.wavereplay();
    }

    private void buildInterfaceMenu(int i, RSInterface class9, int k, int l, int i1, int j1) {
        if (class9.type != 0 || class9.children == null || class9.hidden) { return; }
        if (k < i || i1 < l || k > i + class9.width || i1 > l + class9.height) { return; }
        int k1 = class9.children.length;
        for (int l1 = 0; l1 < k1; l1++) {
            int i2 = class9.childX[l1] + i;
            int j2 = (class9.childY[l1] + l) - j1;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[l1]];
            if (class9_1.hiddenChild) { continue; }
            i2 += class9_1.anInt263;
            j2 += class9_1.anInt265;
            if ((class9_1.mOverInterToTrigger >= 0 || class9_1.disabledHoverColor != 0) && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                if (class9_1.mOverInterToTrigger >= 0) { anInt886 = class9_1.mOverInterToTrigger; } else {
                    anInt886 = class9_1.id;
                }
            }
            if (class9_1.type == 8 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                anInt1315 = class9_1.id;
            }
            if (class9_1.type == 0) {
                buildInterfaceMenu(i2, class9_1, k, j2, i1, class9_1.scrollPosition);
                if (class9_1.scrollMax > class9_1.height) {
                    method65(i2 + class9_1.width, class9_1.height, k, i1, class9_1, j2, true, class9_1.scrollMax);
                }
            } else {
                if (class9_1.atActionType == 1 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                    boolean flag = false;
                    if (class9_1.contentType != 0) { flag = buildFriendsListMenu(class9_1); }
                    if (!flag) {
                        // System.out.println("1"+class9_1.tooltip + ", " +
                        // class9_1.interfaceID);
                        menuActionName[menuActionRow] = class9_1.tooltip;// +
                        // ", "
                        // +
                        // class9_1.id;
                        menuActionID[menuActionRow] = 315;
                        menuActionCmd3[menuActionRow] = class9_1.id;
                        menuActionRow++;
                    }
                }
                if (class9_1.atActionType == 2 && spellSelected == 0 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                    String s = class9_1.selectedActionName;
                    if (s.indexOf(" ") != -1) { s = s.substring(0, s.indexOf(" ")); }
                    menuActionName[menuActionRow] = s + " @gre@" + class9_1.spellName;// + ", " + class9_1.id;
                    // menuActionName[menuActionRow] = s + " @gre@" +
                    // class9_1.spellName;
                    menuActionID[menuActionRow] = 626;
                    menuActionCmd3[menuActionRow] = class9_1.id;

                    menuActionRow++;
                }
                if (class9_1.atActionType == 3 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                    menuActionName[menuActionRow] = "Close";
                    menuActionID[menuActionRow] = 200;
                    menuActionCmd3[menuActionRow] = class9_1.id;
                    menuActionRow++;
                }
                if (class9_1.atActionType == 4 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                    // System.out.println("2"+class9_1.tooltip + ", " +
                    // class9_1.interfaceID);
                    menuActionName[menuActionRow] = class9_1.tooltip;// + ", " +
                    // class9_1.id;
                    menuActionID[menuActionRow] = 169;
                    menuActionCmd3[menuActionRow] = class9_1.id;
                    menuActionRow++;
                    if (class9_1.hoverText != null) {
                        // drawHoverBox(k, l, class9_1.hoverText);
                        // System.out.println("DRAWING INTERFACE: " +
                        // class9_1.hoverText);
                    }
                }
                if (class9_1.atActionType == 5 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                    // System.out.println("3"+class9_1.tooltip + ", " +
                    // class9_1.interfaceID);
                    menuActionName[menuActionRow] = class9_1.tooltip;
                    menuActionID[menuActionRow] = 646;
                    menuActionCmd3[menuActionRow] = class9_1.id;
                    menuActionRow++;
                }
                if (class9_1.atActionType == 6 && !aBoolean1149 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                    // System.out.println("4"+class9_1.tooltip + ", " +
                    // class9_1.interfaceID);
                    menuActionName[menuActionRow] = class9_1.tooltip;
                    menuActionID[menuActionRow] = 679;
                    menuActionCmd3[menuActionRow] = class9_1.id;
                    menuActionRow++;
                }
                if (class9_1.type == 9 && k >= i2 && i1 >= j2 && k < i2 + class9_1.width && i1 < j2 + class9_1.height) {
                    anInt1315 = class9_1.id;
                }
                if (class9_1.type == 2) {
                    int k2 = 0;
                    for (int l2 = 0; l2 < class9_1.height; l2++) {
                        for (int i3 = 0; i3 < class9_1.width; i3++) {
                            int j3 = i2 + i3 * (32 + class9_1.invSpritePadX);
                            int k3 = j2 + l2 * (32 + class9_1.invSpritePadY);
                            if (k2 < 20) {
                                j3 += class9_1.spritesX[k2];
                                k3 += class9_1.spritesY[k2];
                            }
                            if (k >= j3 && i1 >= k3 && k < j3 + 32 && i1 < k3 + 32) {
                                mouseInvInterfaceIndex = k2;
                                lastActiveInvInterface = class9_1.id;
                                if (class9_1.inv[k2] > 0) {
                                    ItemDef itemDef = ItemDef.forID(class9_1.inv[k2] - 1);
                                    if (itemSelected == 1 && class9_1.isInventoryInterface) {
                                        if (class9_1.id != anInt1284 || k2 != anInt1283) {
                                            menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
                                            menuActionID[menuActionRow] = 870;
                                            menuActionCmd1[menuActionRow] = itemDef.id;
                                            menuActionCmd2[menuActionRow] = k2;
                                            menuActionCmd3[menuActionRow] = class9_1.id;
                                            menuActionRow++;
                                        }
                                    } else if (spellSelected == 1 && class9_1.isInventoryInterface) {
                                        if ((spellUsableOn & 0x10) == 16) {
                                            menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name;
                                            menuActionID[menuActionRow] = 543;
                                            menuActionCmd1[menuActionRow] = itemDef.id;
                                            menuActionCmd2[menuActionRow] = k2;
                                            menuActionCmd3[menuActionRow] = class9_1.id;
                                            menuActionRow++;
                                        }
                                    } else {
                                        if (class9_1.isInventoryInterface) {
                                            for (int l3 = 4; l3 >= 3; l3--) {
                                                if (itemDef.actions != null && itemDef.actions[l3] != null) {
                                                    menuActionName[menuActionRow] = itemDef.actions[l3] + " @lre@" + itemDef.name;
                                                    if (l3 == 3) { menuActionID[menuActionRow] = 493; }
                                                    if (l3 == 4) { menuActionID[menuActionRow] = 847; }
                                                    menuActionCmd1[menuActionRow] = itemDef.id;
                                                    menuActionCmd2[menuActionRow] = k2;
                                                    menuActionCmd3[menuActionRow] = class9_1.id;
                                                    menuActionRow++;
                                                } else if (l3 == 4) {
                                                    menuActionName[menuActionRow] = "Drop @lre@" + itemDef.name;
                                                    menuActionID[menuActionRow] = 847;
                                                    menuActionCmd1[menuActionRow] = itemDef.id;
                                                    menuActionCmd2[menuActionRow] = k2;
                                                    menuActionCmd3[menuActionRow] = class9_1.id;
                                                    menuActionRow++;
                                                }
                                            }

                                        }
                                        if (class9_1.usableItemInterface) {
                                            menuActionName[menuActionRow] = "Use @lre@" + itemDef.name;
                                            menuActionID[menuActionRow] = 447;
                                            menuActionCmd1[menuActionRow] = itemDef.id;
                                            // k2 = inventory spot
                                            // System.out.println(k2);
                                            menuActionCmd2[menuActionRow] = k2;
                                            menuActionCmd3[menuActionRow] = class9_1.id;
                                            menuActionRow++;
                                        }
                                        if (class9_1.isInventoryInterface && itemDef.actions != null) {
                                            for (int i4 = 2; i4 >= 0; i4--) {
                                                if (itemDef.actions[i4] != null) {
                                                    menuActionName[menuActionRow] = itemDef.actions[i4] + " @lre@" + itemDef.name;
                                                    if (i4 == 0) { menuActionID[menuActionRow] = 74; }
                                                    if (i4 == 1) { menuActionID[menuActionRow] = 454; }
                                                    if (i4 == 2) { menuActionID[menuActionRow] = 539; }
                                                    menuActionCmd1[menuActionRow] = itemDef.id;
                                                    menuActionCmd2[menuActionRow] = k2;
                                                    menuActionCmd3[menuActionRow] = class9_1.id;
                                                    menuActionRow++;
                                                }
                                            }

                                        }
                                        if (class9_1.actions != null) {
                                            for (int actionIndex = 4; actionIndex >= 0; actionIndex--) {
                                                if (class9_1.actions[actionIndex] != null) {
                                                    menuActionName[menuActionRow] = class9_1.actions[actionIndex] + " @lre@" + itemDef.name;
                                                    if (actionIndex == 0) {
                                                        menuActionID[menuActionRow] = 632;
                                                    }
                                                    if (actionIndex == 1) {
                                                        menuActionID[menuActionRow] = 78;
                                                    }
                                                    if (actionIndex == 2) {
                                                        menuActionID[menuActionRow] = 867;
                                                    }
                                                    if (actionIndex == 3) {
                                                        menuActionID[menuActionRow] = 431;
                                                    }
                                                    if (actionIndex == 4) {
                                                        menuActionID[menuActionRow] = 53;
                                                    }
                                                    menuActionCmd1[menuActionRow] = itemDef.id;
                                                    menuActionCmd2[menuActionRow] = k2;
                                                    menuActionCmd3[menuActionRow] = class9_1.id;
                                                    menuActionRow++;
                                                }
                                            }
                                        }
                                        // menuActionName[menuActionRow] =
                                        // "Examine @lre@" + itemDef.name +
                                        // " @gre@(@whi@" + (class9_1.inv[k2] -
                                        // 1) + "@gre@)";
                                        if (class9_1.isItemExaminable) {
                                            menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name;
                                            menuActionID[menuActionRow] = 1125;
                                            menuActionCmd1[menuActionRow] = itemDef.id;
                                            menuActionCmd2[menuActionRow] = k2;
                                            menuActionCmd3[menuActionRow] = class9_1.id;
                                            menuActionRow++;
                                        }
                                    }
                                }
                            }
                            k2++;
                        }

                    }

                }
            }
        }

    }

    public void drawScrollbar(int height, int scrollPosition, int yPos, int xPos, int maxHeight, int type) {
        if (type > 0) {
            int backAmount = (height - 32) / 5;
            int scrollerHeight = ((height - 32) * height) / maxHeight;
            if (scrollerHeight < 10) { scrollerHeight = 10; }
            int scrollAmount = (scrollerHeight / 5) - 2;
            int scrollPartPos = ((height - 32 - scrollerHeight) * scrollPosition) / (maxHeight - height) + 16 + yPos;
            for (int i = 0, y = yPos + 16; i <= backAmount; i++, y += 5) // scroll back
            { scrollSprites[3].drawSprite(xPos, y); }
            scrollSprites[0].drawAdvancedSprite(xPos, scrollPartPos);
            scrollPartPos += 5;
            for (int i = 0; i <= scrollAmount; i++) {
                scrollSprites[1].drawAdvancedSprite(xPos, scrollPartPos);
                scrollPartPos += 5;
            }
            scrollPartPos = ((height - 32 - scrollerHeight) * scrollPosition) / (maxHeight - height) + 16 + yPos + (scrollerHeight - 5);
            scrollSprites[2].drawAdvancedSprite(xPos, scrollPartPos);
            if (super.mouseX > xPos && super.mouseX < xPos + 16 && super.mouseY > yPos && super.mouseY < yPos + 16) {
                scrollSprites[7].drawAdvancedSprite(xPos, yPos);
            } else { scrollSprites[5].drawSprite(xPos, yPos); }
            if (super.mouseX > xPos && super.mouseX < xPos + 16 && super.mouseY > ((yPos + height) - 16) && super.mouseY < ((yPos + height) - 16) + 16) {
                scrollSprites[6].drawAdvancedSprite(xPos, (yPos + height) - 16);
            } else { scrollSprites[4].drawAdvancedSprite(xPos, (yPos + height) - 16); }
        } else {
            scrollBar1.drawSprite(xPos, yPos);
            scrollBar2.drawSprite(xPos, (yPos + height) - 16);
            DrawingArea.drawPixels(height - 32, yPos + 16, xPos, 0x000001, 16);
            DrawingArea.drawPixels(height - 32, yPos + 16, xPos, 0x3d3426, 15);
            DrawingArea.drawPixels(height - 32, yPos + 16, xPos, 0x342d21, 13);
            DrawingArea.drawPixels(height - 32, yPos + 16, xPos, 0x2e281d, 11);
            DrawingArea.drawPixels(height - 32, yPos + 16, xPos, 0x29241b, 10);
            DrawingArea.drawPixels(height - 32, yPos + 16, xPos, 0x252019, 9);
            DrawingArea.drawPixels(height - 32, yPos + 16, xPos, 0x000001, 1);
            int scrollerHeight = ((height - 32) * height) / maxHeight;
            if (scrollerHeight < 8) { scrollerHeight = 8; }
            int position = ((height - 32 - scrollerHeight) * scrollPosition) / (maxHeight - height);
            DrawingArea.drawPixels(scrollerHeight, yPos + 16 + position, xPos, barFillColor, 16);
            DrawingArea.method341(yPos + 16 + position, 0x000001, scrollerHeight, xPos);
            DrawingArea.method341(yPos + 16 + position, 0x817051, scrollerHeight, xPos + 1);
            DrawingArea.method341(yPos + 16 + position, 0x73654a, scrollerHeight, xPos + 2);
            DrawingArea.method341(yPos + 16 + position, 0x6a5c43, scrollerHeight, xPos + 3);
            DrawingArea.method341(yPos + 16 + position, 0x6a5c43, scrollerHeight, xPos + 4);
            DrawingArea.method341(yPos + 16 + position, 0x655841, scrollerHeight, xPos + 5);
            DrawingArea.method341(yPos + 16 + position, 0x655841, scrollerHeight, xPos + 6);
            DrawingArea.method341(yPos + 16 + position, 0x61553e, scrollerHeight, xPos + 7);
            DrawingArea.method341(yPos + 16 + position, 0x61553e, scrollerHeight, xPos + 8);
            DrawingArea.method341(yPos + 16 + position, 0x5d513c, scrollerHeight, xPos + 9);
            DrawingArea.method341(yPos + 16 + position, 0x5d513c, scrollerHeight, xPos + 10);
            DrawingArea.method341(yPos + 16 + position, 0x594e3a, scrollerHeight, xPos + 11);
            DrawingArea.method341(yPos + 16 + position, 0x594e3a, scrollerHeight, xPos + 12);
            DrawingArea.method341(yPos + 16 + position, 0x514635, scrollerHeight, xPos + 13);
            DrawingArea.method341(yPos + 16 + position, 0x4b4131, scrollerHeight, xPos + 14);
            DrawingArea.method339(yPos + 16 + position, 0x000001, 15, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x000001, 15, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x655841, 14, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x6a5c43, 13, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x6d5f48, 11, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x73654a, 10, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x76684b, 7, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x7b6a4d, 5, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x7e6e50, 4, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x817051, 3, xPos);
            DrawingArea.method339(yPos + 17 + position, 0x000001, 2, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x000001, 16, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x564b38, 15, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x5d513c, 14, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x625640, 11, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x655841, 10, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x6a5c43, 7, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x6e6046, 5, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x716247, 4, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x7b6a4d, 3, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x817051, 2, xPos);
            DrawingArea.method339(yPos + 18 + position, 0x000001, 1, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x000001, 16, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x514635, 15, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x564b38, 14, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x5d513c, 11, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x61553e, 9, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x655841, 7, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x6a5c43, 5, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x6e6046, 4, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x73654a, 3, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x817051, 2, xPos);
            DrawingArea.method339(yPos + 19 + position, 0x000001, 1, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x000001, 16, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x4b4131, 15, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x544936, 14, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x594e3a, 13, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x5d513c, 10, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x61553e, 8, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x655841, 6, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x6a5c43, 4, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x73654a, 3, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x817051, 2, xPos);
            DrawingArea.method339(yPos + 20 + position, 0x000001, 1, xPos);
            DrawingArea.method341(yPos + 16 + position, 0x000001, scrollerHeight, xPos + 15);
            DrawingArea.method339(yPos + 15 + position + scrollerHeight, 0x000001, 16, xPos);
            DrawingArea.method339(yPos + 14 + position + scrollerHeight, 0x000001, 15, xPos);
            DrawingArea.method339(yPos + 14 + position + scrollerHeight, 0x3f372a, 14, xPos);
            DrawingArea.method339(yPos + 14 + position + scrollerHeight, 0x443c2d, 10, xPos);
            DrawingArea.method339(yPos + 14 + position + scrollerHeight, 0x483e2f, 9, xPos);
            DrawingArea.method339(yPos + 14 + position + scrollerHeight, 0x4a402f, 7, xPos);
            DrawingArea.method339(yPos + 14 + position + scrollerHeight, 0x4b4131, 4, xPos);
            DrawingArea.method339(yPos + 14 + position + scrollerHeight, 0x564b38, 3, xPos);
            DrawingArea.method339(yPos + 14 + position + scrollerHeight, 0x000001, 2, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x000001, 16, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x443c2d, 15, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x4b4131, 11, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x514635, 9, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x544936, 7, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x564b38, 6, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x594e3a, 4, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x625640, 3, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x6a5c43, 2, xPos);
            DrawingArea.method339(yPos + 13 + position + scrollerHeight, 0x000001, 1, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x000001, 16, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x443c2d, 15, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x4b4131, 14, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x544936, 12, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x564b38, 11, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x594e3a, 10, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x5d513c, 7, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x61553e, 4, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x6e6046, 3, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x7b6a4d, 2, xPos);
            DrawingArea.method339(yPos + 12 + position + scrollerHeight, 0x000001, 1, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x000001, 16, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x4b4131, 15, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x514635, 14, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x564b38, 13, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x594e3a, 11, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x5d513c, 9, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x61553e, 7, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x655841, 5, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x6a5c43, 4, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x73654a, 3, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x7b6a4d, 2, xPos);
            DrawingArea.method339(yPos + 11 + position + scrollerHeight, 0x000001, 1, xPos);
        }
    }

    private void updateNPCs(Stream stream, int i) {
        anInt839 = 0;
        anInt893 = 0;
        method139(stream);
        method46(i, stream);
        method86(stream);
        for (int k = 0; k < anInt839; k++) {
            int l = anIntArray840[k];
            if (npcArray[l].anInt1537 != loopCycle) {
                npcArray[l].desc = null;
                npcArray[l] = null;
            }
        }

        if (stream.currentOffset != i) {
            Signlink.reporterror(Configuration.getInstance().getValue(Username.class) + " size mismatch in getnpcpos - pos:" + stream.currentOffset + " psize:" + i);
            throw new RuntimeException("eek");
        }
        for (int i1 = 0; i1 < npcCount; i1++) {
            if (npcArray[npcIndices[i1]] == null) {
                Signlink.reporterror(Configuration.getInstance().getValue(Username.class) + " null entry in npc list - pos:" + i1 + " size:" + npcCount);
                throw new RuntimeException("eek");
            }
        }

    }

    private void processChatModeClick() {
        int x = isFullScreen ? extraWidth : 0;
        int y = isFullScreen ? extraHeight : 0;
        if (displayChat) {
            if (super.mouseX >= 5 && super.mouseX <= 61 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                cButtonHPos = 0;
                aBoolean1233 = true;
                inputTaken = true;
            } else if (super.mouseX >= 71 && super.mouseX <= 127 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                cButtonHPos = 1;
                aBoolean1233 = true;
                inputTaken = true;
            } else if (super.mouseX >= 137 && super.mouseX <= 193 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                cButtonHPos = 2;
                aBoolean1233 = true;
                inputTaken = true;
            } else if (super.mouseX >= 203 && super.mouseX <= 259 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                cButtonHPos = 3;
                aBoolean1233 = true;
                inputTaken = true;
            } else if (super.mouseX >= 269 && super.mouseX <= 325 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                cButtonHPos = 4;
                aBoolean1233 = true;
                inputTaken = true;
            } else if (super.mouseX >= 335 && super.mouseX <= 391 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                cButtonHPos = 5;
                aBoolean1233 = true;
                inputTaken = true;
            } else if (super.mouseX >= 404 && super.mouseX <= 515 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                cButtonHPos = 6;
                aBoolean1233 = true;
                inputTaken = true;
            } else {
                cButtonHPos = -1;
                aBoolean1233 = true;
                inputTaken = true;
            }
            if (super.clickMode3 == 1) {
                if (super.saveClickX >= 5 && super.saveClickX <= 61 && super.saveClickY >= 482 + y && super.saveClickY <= 505 + y) {
                    cButtonCPos = 0;
                    chatTypeView = 0;
                    aBoolean1233 = true;
                    inputTaken = true;
                } else if (super.saveClickX >= 71 && super.saveClickX <= 127 && super.saveClickY >= 482 + y && super.saveClickY <= 505 + y) {
                    cButtonCPos = 1;
                    chatTypeView = 5;
                    aBoolean1233 = true;
                    inputTaken = true;
                } else if (super.saveClickX >= 137 && super.saveClickX <= 193 && super.saveClickY >= 482 + y && super.saveClickY <= 505 + y) {
                    cButtonCPos = 2;
                    chatTypeView = 1;
                    aBoolean1233 = true;
                    inputTaken = true;
                } else if (super.saveClickX >= 203 && super.saveClickX <= 259 && super.saveClickY >= 482 + y && super.saveClickY <= 505 + y) {
                    cButtonCPos = 3;
                    chatTypeView = 2;
                    aBoolean1233 = true;
                    inputTaken = true;
                } else if (super.saveClickX >= 269 && super.saveClickX <= 325 && super.saveClickY >= 482 + y && super.saveClickY <= 505 + y) {
                    cButtonCPos = 4;
                    chatTypeView = 11;
                    aBoolean1233 = true;
                    inputTaken = true;
                } else if (super.saveClickX >= 335 && super.saveClickX <= 391 && super.saveClickY >= 482 + y && super.saveClickY <= 505 + y) {
                    cButtonCPos = 5;
                    chatTypeView = 3;
                    aBoolean1233 = true;
                    inputTaken = true;
                } else if (super.saveClickX >= 404 && super.saveClickX <= 515 && super.saveClickY >= 482 + y && super.saveClickY <= 505 + y) {
                    InterfaceManager.show(RequestHelpInterface.ID);
                }
            }
        }
    }

    private void updateEntities() {
        try {
            manageOffsetsForToggle();
            int anInt974 = 0;
            for (int j = -1; j < playerCount + npcCount; j++) {
                Object obj;
                if (j == -1) { obj = myPlayer; } else if (j < playerCount) {
                    obj = playerArray[playerIndices[j]];
                } else {
                    obj = npcArray[npcIndices[j - playerCount]];
                }
                if (obj == null || !((Entity) (obj)).isVisible()) { continue; }
                if (obj instanceof NPC) {
                    EntityDef entityDef = ((NPC) obj).desc;
                    if (Configuration.getInstance().getValue(HeadName.class)) {
                        String s = entityDef.name;
                        s = s + combatDiffColor(myPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel + ")";
                        if (entityDef.combatLevel != 0) {
                            npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                            smallText.method382(0xFFFF33, spriteDrawX, s, spriteDrawY - yOffsetForText, true);
                        } else if (entityDef.combatLevel == 0) {
                            npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                            smallText.method382(0xFFFF33, spriteDrawX, entityDef.name, spriteDrawY - yOffsetForText, true);
                        }
                    }
                    if (entityDef.childrenIDs != null) { entityDef = entityDef.method161(); }
                    if (entityDef == null) { continue; }
                }
                if (j < playerCount) {
                    int l = 30;
                    Player player = (Player) obj;
                    if (player.headIcon >= 0) {
                        npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            if (player.skullIcon < 2) {
                                skullIcons[player.skullIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l - headIconOffset);
                                l += 26;
                            }
                            if (player.headIcon < 18) {
                                headIcons[player.headIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l - headIconOffset);
                                l += 26;
                            }
                        }
                    }
                    if (j >= 0 && anInt855 == 10 && anInt933 == playerIndices[j] && loopCycle % 20 < 10) {
                        npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            headIconsHint[player.hintIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l - headIconOffset);
                        }
                    }
                    if (Configuration.getInstance().getValue(HeadName.class)) {
                        npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                        int col = 0x00ff00;// Green
                        aTextDrawingArea_1271.drawText(col, player.name, spriteDrawY - yOffsetForText, spriteDrawX);
                    }
                } else {
                    EntityDef entityDef_1 = ((NPC) obj).desc;
                    if (entityDef_1.anInt75 >= 0 && entityDef_1.anInt75 < headIcons.length) {
                        npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            headIcons[entityDef_1.anInt75].drawSprite(spriteDrawX - 12, spriteDrawY - 30 - headIconOffset);
                        }
                    }
                    if (anInt855 == 1 && anInt1222 == npcIndices[j - playerCount] && loopCycle % 20 < 10) {
                        npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28 - headIconOffset);
                        }
                    }
                }

                if (((Entity) (obj)).textSpoken != null && (j >= playerCount || publicChatMode == 0 || publicChatMode == 3 || publicChatMode == 1 && isFriendOrSelf(((Player) obj).name))) {
                    npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height);
                    if (spriteDrawX > -1 && anInt974 < anInt975) {
                        anIntArray979[anInt974] = chatTextDrawingArea.method384(((Entity) (obj)).textSpoken) / 2;
                        anIntArray978[anInt974] = chatTextDrawingArea.anInt1497;
                        anIntArray976[anInt974] = spriteDrawX;
                        anIntArray977[anInt974] = spriteDrawY;
                        anIntArray980[anInt974] = ((Entity) (obj)).anInt1513;
                        anIntArray981[anInt974] = ((Entity) (obj)).anInt1531;
                        anIntArray982[anInt974] = ((Entity) (obj)).textCycle;
                        aStringArray983[anInt974++] = ((Entity) (obj)).textSpoken;
                        if (anInt1249 == 0 && ((Entity) (obj)).anInt1531 >= 1 && ((Entity) (obj)).anInt1531 <= 3) {
                            anIntArray978[anInt974] += 10;
                            anIntArray977[anInt974] += 5;
                        }
                        if (anInt1249 == 0 && ((Entity) (obj)).anInt1531 == 4) { anIntArray979[anInt974] = 60; }
                        if (anInt1249 == 0 && ((Entity) (obj)).anInt1531 == 5) { anIntArray978[anInt974] += 5; }
                    }
                }
                if (((Entity) (obj)).loopCycleStatus > loopCycle) {
                    try {
                        npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
                        if (spriteDrawX > -1) {
                            int i1 = (((Entity) (obj)).currentHealth * 30) / ((Entity) (obj)).maxHealth;
                            if (i1 > 30) {
                                i1 = 30;
                            }
                            int HpPercent = (((Entity) (obj)).currentHealth * 56) / ((Entity) (obj)).maxHealth;
                            if (HpPercent > 56) {
                                HpPercent = 56;
                            }
                            if (!Configuration.getInstance().getValue(HPBar.class)) {
                                DrawingArea.method336(5, spriteDrawY - 3, spriteDrawX - 15, 65280, i1);
                                DrawingArea.method336(5, spriteDrawY - 3, (spriteDrawX - 15) + i1, 0xff0000, 30 - i1);
                            } else {
                                HPBarEmpty.drawSprite(spriteDrawX - 28, spriteDrawY - 3);
                                HPBarFull = new Sprite("HITPOINTS_0.png", HpPercent, 7);
                                HPBarFull.drawSprite(spriteDrawX - 28, spriteDrawY - 3);
                            }
                            int health = (int) (((double) ((Entity) (obj)).currentHealth / (double) ((Entity) (obj)).maxHealth) * 100D);
                            int col = 65280;
                            if (health <= 100 && health >= 75) { col = 65280; } else if (health <= 74 && health >= 50) {
                                col = 0xffff00;
                            } else if (health <= 49 && health >= 25) {
                                col = 0xfca607;
                            } else if (health <= 24 && health >= 0) { col = 0xf50d0d; }
                            if (Configuration.getInstance().getValue(HeadConstitution.class)) {
                                smallText.method382(col, spriteDrawX, (new StringBuilder()).append(((Entity) (Entity) obj).currentHealth).append("/").append(((Entity) (Entity) obj).maxHealth).toString(), spriteDrawY - 5, true);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                boolean showcrit = false;//critical;

                for (int k1 = 0; k1 < 4; k1++) {
                    if (Configuration.getInstance().getValue(Hitmarks.class)) {
                        if (((Entity) (Entity) obj).hitsLoopCycle[k1] <= loopCycle) {
                            continue;
                        }
                        npcScreenPos((Entity) (Entity) obj, ((Entity) (Entity) obj).height / 2);
                        if (spriteDrawX <= -1) {
                            continue;
                        }
                        switch (k1) {
                            case 1: // '\001'
                                spriteDrawY += 18;
                                break;

                            case 2: // '\002'
                                spriteDrawY += 25;
                                break;

                            case 3: // '\003'
                                spriteDrawY += 43;
                                break;
                        }
                        Entity entity = (Entity) (Entity) obj;
                        if (entity.moveTimer[k1] == 0) {
                            if (entity.hitmarkMove[k1] > -14) {
                                entity.hitmarkMove[k1]--;
                            }
                            entity.moveTimer[k1] = 2;
                        } else {
                            entity.moveTimer[k1]--;
                        }
                        if (entity.hitmarkMove[k1] <= -14) {
                            entity.hitmarkTrans[k1] -= 10;
                        }
                        hitmarkDraw(String.valueOf(entity.hitArray[k1]).length(), entity.hitMarkTypes[k1], entity.hitType[k1], entity.hitArray[k1], 0, entity.hitmarkMove[k1], entity.hitmarkTrans[k1]);

                    } else {
                        int j1 = k1;
                        if (((Entity) (obj)).hitsLoopCycle[j1] > loopCycle) {
                            npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height / 2);
                            if (spriteDrawX > -1) {
                                if (j1 == 1) { spriteDrawY -= 20; }
                                if (j1 == 2) {
                                    spriteDrawX -= 15;
                                    spriteDrawY -= 10;
                                }
                                if (j1 == 3) {
                                    spriteDrawX += 15;
                                    spriteDrawY -= 10;
                                }
                                if (!Configuration.getInstance().getValue(Hitmarks.class)) {
                                    hitMarks[((Entity) (obj)).hitMarkTypes[j1]].drawSprite(spriteDrawX - 10, spriteDrawY - 12);
                                    smallText.drawText(0, String.valueOf(((Entity) (obj)).hitArray[j1]), spriteDrawY + 4, spriteDrawX);
                                    smallText.drawText(0xffffff, String.valueOf(((Entity) (obj)).hitArray[j1]), spriteDrawY + 3, spriteDrawX - 1);
                                    if (((Entity) (obj)).hitArray[j1] >= 100) {
                                        x10Expand.drawSprite(spriteDrawX - 20, spriteDrawY - 13);
                                    }

                                    smallText.drawText(0, String.valueOf(((Entity) (obj)).hitArray[j1]), spriteDrawY + 4, spriteDrawX);
                                    smallText.drawText(0xffffff, String.valueOf(((Entity) (obj)).hitArray[j1]), spriteDrawY + 3, spriteDrawX - 1);
                                }
                            }
                        }

                    }
                }
            }
            for (int k = 0; k < anInt974; k++) {
                int k1 = anIntArray976[k];
                int l1 = anIntArray977[k];
                int j2 = anIntArray979[k];
                int k2 = anIntArray978[k];
                boolean flag = true;
                while (flag) {
                    flag = false;
                    for (int l2 = 0; l2 < k; l2++) {
                        if (l1 + 2 > anIntArray977[l2] - anIntArray978[l2] && l1 - k2 < anIntArray977[l2] + 2 && k1 - j2 < anIntArray976[l2] + anIntArray979[l2] && k1 + j2 > anIntArray976[l2] - anIntArray979[l2] && anIntArray977[l2] - anIntArray978[l2] < l1) {
                            l1 = anIntArray977[l2] - anIntArray978[l2];
                            flag = true;
                        }
                    }

                }
                spriteDrawX = anIntArray976[k];
                spriteDrawY = anIntArray977[k] = l1;
                String s = aStringArray983[k];
                if (anInt1249 == 0) {
                    int i3 = 0xffff00;
                    if (anIntArray980[k] < 6) { i3 = anIntArray965[anIntArray980[k]]; }
                    if (anIntArray980[k] == 6) { i3 = anInt1265 % 20 >= 10 ? 0xffff00 : 0xff0000; }
                    if (anIntArray980[k] == 7) { i3 = anInt1265 % 20 >= 10 ? 65535 : 255; }
                    if (anIntArray980[k] == 8) { i3 = anInt1265 % 20 >= 10 ? 0x80ff80 : 45056; }
                    if (anIntArray980[k] == 9) {
                        int j3 = 150 - anIntArray982[k];
                        if (j3 < 50) { i3 = 0xff0000 + 1280 * j3; } else if (j3 < 100) {
                            i3 = 0xffff00 - 0x50000 * (j3 - 50);
                        } else if (j3 < 150) {
                            i3 = 65280 + 5 * (j3 - 100);
                        }
                    }
                    if (anIntArray980[k] == 10) {
                        int k3 = 150 - anIntArray982[k];
                        if (k3 < 50) { i3 = 0xff0000 + 5 * k3; } else if (k3 < 100) {
                            i3 = 0xff00ff - 0x50000 * (k3 - 50);
                        } else if (k3 < 150) {
                            i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
                        }
                    }
                    if (anIntArray980[k] == 11) {
                        int l3 = 150 - anIntArray982[k];
                        if (l3 < 50) { i3 = 0xffffff - 0x50005 * l3; } else if (l3 < 100) {
                            i3 = 65280 + 0x50005 * (l3 - 50);
                        } else if (l3 < 150) {
                            i3 = 0xffffff - 0x50000 * (l3 - 100);
                        }
                    }
                    if (anIntArray981[k] == 0) {
                        chatTextDrawingArea.drawText(0, s, spriteDrawY + 1, spriteDrawX);
                        chatTextDrawingArea.drawText(i3, s, spriteDrawY, spriteDrawX);
                    }
                    if (anIntArray981[k] == 1) {
                        chatTextDrawingArea.method386(0, s, spriteDrawX, anInt1265, spriteDrawY + 1);
                        chatTextDrawingArea.method386(i3, s, spriteDrawX, anInt1265, spriteDrawY);
                    }
                    if (anIntArray981[k] == 2) {
                        chatTextDrawingArea.method387(spriteDrawX, s, anInt1265, spriteDrawY + 1, 0);
                        chatTextDrawingArea.method387(spriteDrawX, s, anInt1265, spriteDrawY, i3);
                    }
                    if (anIntArray981[k] == 3) {
                        chatTextDrawingArea.method388(150 - anIntArray982[k], s, anInt1265, spriteDrawY + 1, spriteDrawX, 0);
                        chatTextDrawingArea.method388(150 - anIntArray982[k], s, anInt1265, spriteDrawY, spriteDrawX, i3);
                    }
                    if (anIntArray981[k] == 4) {
                        int i4 = chatTextDrawingArea.method384(s);
                        int k4 = ((150 - anIntArray982[k]) * (i4 + 100)) / 150;
                        DrawingArea.setDrawingArea(334, spriteDrawX - 50, spriteDrawX + 50, 0);
                        chatTextDrawingArea.method385(0, s, spriteDrawY + 1, (spriteDrawX + 50) - k4);
                        chatTextDrawingArea.method385(i3, s, spriteDrawY, (spriteDrawX + 50) - k4);
                        DrawingArea.defaultDrawingAreaSize();
                    }
                    if (anIntArray981[k] == 5) {
                        int j4 = 150 - anIntArray982[k];
                        int l4 = 0;
                        if (j4 < 25) { l4 = j4 - 25; } else if (j4 > 125) { l4 = j4 - 125; }
                        DrawingArea.setDrawingArea(spriteDrawY + 5, 0, 512, spriteDrawY - chatTextDrawingArea.anInt1497 - 1);
                        chatTextDrawingArea.drawText(0, s, spriteDrawY + 1 + l4, spriteDrawX);
                        chatTextDrawingArea.drawText(i3, s, spriteDrawY + l4, spriteDrawX);
                        DrawingArea.defaultDrawingAreaSize();
                    }
                } else {
                    chatTextDrawingArea.drawText(0, s, spriteDrawY + 1, spriteDrawX);
                    chatTextDrawingArea.drawText(0xffff00, s, spriteDrawY, spriteDrawX);
                }
            }
        } catch (Exception e) {
        }
    }

    public void manageOffsetsForToggle() {
        if (Configuration.getInstance().getValue(HeadConstitution.class) && Configuration.getInstance().getValue(HeadName.class)) {
            headIconOffset = 14 + 15;
            yOffsetForText = 16;
        } else if (Configuration.getInstance().getValue(HeadConstitution.class) && !Configuration.getInstance().getValue(HeadName.class)) {
            headIconOffset = 14;
        } else if (!Configuration.getInstance().getValue(HeadConstitution.class) && Configuration.getInstance().getValue(HeadName.class)) {
            headIconOffset = 15;
            yOffsetForText = 8;
        } else {
            headIconOffset = 0;
            yOffsetForText = 0;
        }
    }

    private void delFriend(long l) {
        try {
            if (l == 0L) { return; }
            for (int i = 0; i < friendsCount; i++) {
                if (friendsListAsLongs[i] != l) { continue; }
                friendsCount--;
                needDrawTabArea = true;
                for (int j = i; j < friendsCount; j++) {
                    friendsList[j] = friendsList[j + 1];
                    friendsNodeIDs[j] = friendsNodeIDs[j + 1];
                    friendsListAsLongs[j] = friendsListAsLongs[j + 1];
                }

                stream.createFrame(215);
                stream.writeLong(l);
                break;
            }
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("18622, " + false + ", " + l + ", " + runtimeexception.toString());
            throw new RuntimeException();
        }
    }

    public void drawSideIcons() {
        int extraXX = drawLongTabs ? -241 : 0;
        int extraYY = drawLongTabs ? +35 : 0;

        int mhm = drawLongTabs ? +4 : 0;
        int mhm2 = drawLongTabs ? -4 : 0;

        int extraXX1 = drawLongTabs ? -233 : 0;

        int extraX = isFullScreen ? extraWidth + 519 : 0;
        int extraY = isFullScreen ? extraHeight + 168 + 262 : 0;

        int extraX2 = !drawLongTabs ? extraWidth - 8 : 0;
        int extraY2 = !drawLongTabs ? extraHeight - 5 : 0;
		/* Top sideIcons */
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            if (tabInterfaceIDs[0] != -1)// attack
            { sideIcons[0].drawSprite(10 + extraX + extraXX1, 4 + extraY + extraYY); }
            if (tabInterfaceIDs[1] != -1)// stat
            { sideIcons[1].drawSprite(43 + extraX + extraXX1, 4 + extraY + extraYY); }
            if (tabInterfaceIDs[2] != -1)// quest
            { sideIcons[2].drawSprite(76 + extraX + extraXX1, 3 + extraY + extraYY); }
            if (tabInterfaceIDs[3] != -1)// inventory
            { sideIcons[3].drawSprite(111 + extraX + extraXX1, 5 + extraY + extraYY); }
            if (tabInterfaceIDs[4] != -1)// equipment
            { sideIcons[4].drawSprite(140 + extraX + extraXX1, 1 + extraY + extraYY); }
            if (tabInterfaceIDs[5] != -1)// prayer
            { sideIcons[5].drawSprite(174 + extraX + extraXX1, 1 + extraY + extraYY); }
            if (tabInterfaceIDs[6] != -1)// magic
            { sideIcons[6].drawSprite(208 + extraX + extraXX1, 4 + extraY + extraYY); }
            if (isFullScreen) { extraY -= 262; }
			/* Bottom sideIcons */
            if (tabInterfaceIDs[7] != -1)// clan
            { sideIcons[7].drawSprite(11 + extraX, 303 + extraY); }
            if (tabInterfaceIDs[8] != -1)// friends
            { sideIcons[8].drawSprite(46 + extraX, 306 + extraY); }
            if (tabInterfaceIDs[9] != -1)// ignore
            { sideIcons[9].drawSprite(79 + extraX, 306 + extraY); }
            if (tabInterfaceIDs[10] != -1)// options
            { sideIcons[10].drawSprite(113 + extraX, 302 + extraY); }
            if (tabInterfaceIDs[11] != -1)// options
            { sideIcons[11].drawSprite(145 + extraX, 304 + extraY); }
            if (tabInterfaceIDs[12] != -1)// emotes
            { sideIcons[12].drawSprite(181 + extraX, 302 + extraY); }
            if (tabInterfaceIDs[13] != -1)// music
            { sideIcons[13].drawSprite(213 + extraX, 303 + extraY); }
        } else {
            if (isFullScreen) { extraX = isFullScreen ? extraWidth + 511 : 0; }
            if (tabInterfaceIDs[0] != -1 && drawLongTabs) {
                newSideIcons[0].drawSprite(37 + extraX + extraXX + mhm, 8 + extraY + extraYY + mhm2);// 8+extraX+extraXX+mhm,
            }
            // 8+extraY+extraYY+mhm2
            if (tabInterfaceIDs[1] != -1 && drawLongTabs) {
                newSideIcons[1].drawSprite(67 + extraX + extraXX + mhm, 8 + extraY + extraYY + mhm2);// 37+extraX+extraXX+mhm,
            }
            // 8+extraY+extraYY+mhm2
            if (tabInterfaceIDs[2] != -1 && drawLongTabs) {
                newSideIcons[2].drawSprite(97 + extraX + extraXX + mhm, 8 + extraY + extraYY + mhm2); // (97+extraX+extraXX+mhm,
            }
            // 8+extraY+extraYY+mhm2);

            if (tabInterfaceIDs[0] != -1 && !drawLongTabs) {
                newSideIcons[0].drawSprite(8 + extraX + extraXX, 8 + extraY + extraYY);
            }
            if (tabInterfaceIDs[1] != -1 && !drawLongTabs) {
                newSideIcons[1].drawSprite(37 + extraX + extraXX, 8 + extraY + extraYY);
            }
            if (tabInterfaceIDs[2] != -1 && !drawLongTabs) {
                newSideIcons[2].drawSprite(67 + extraX + extraXX, 8 + extraY + extraYY);
            }
            if (tabInterfaceIDs[14] != -1 && !drawLongTabs) {
                newSideIcons[3].drawSprite(97 + extraX + extraXX, 8 + extraY + extraYY);
            }

            if (tabInterfaceIDs[3] != -1) {
                newSideIcons[4].drawSprite(127 + extraX + extraXX + mhm, 8 + extraY + extraYY + mhm2);
            }
            if (tabInterfaceIDs[4] != -1) {
                newSideIcons[5].drawSprite(159 + extraX + extraXX + mhm, 8 + extraY + extraYY + mhm2);
            }
            if (tabInterfaceIDs[5] != -1) {
                newSideIcons[6].drawSprite(187 + extraX + extraXX + mhm, 8 + extraY + extraYY + mhm2);
            }
            if (tabInterfaceIDs[6] != -1) {
                newSideIcons[7].drawSprite(217 + extraX + extraXX + mhm, 8 + extraY + extraYY + mhm2);
            }
            if (isFullScreen) { extraY -= 263; }
			/* Bottom sideIcons */
            if (tabInterfaceIDs[9] != -1) { newSideIcons[8].drawSprite(38 + extraX + mhm, 306 + extraY + mhm2); }
            if (tabInterfaceIDs[8] != -1) { newSideIcons[9].drawSprite(70 + extraX + mhm, 306 + extraY + mhm2); }
            if (tabInterfaceIDs[7] != -1) { newSideIcons[13].drawSprite(97 + extraX + mhm, 306 + extraY + mhm2); }
            if (tabInterfaceIDs[11] != -1) { newSideIcons[10].drawSprite(127 + extraX + mhm, 306 + extraY + mhm2); }
            if (tabInterfaceIDs[12] != -1) { newSideIcons[11].drawSprite(157 + extraX + mhm, 306 + extraY + mhm2); }
            if (tabInterfaceIDs[13] != -1) { newSideIcons[12].drawSprite(187 + extraX + mhm, 306 + extraY + mhm2); }
            if (tabInterfaceIDs[15] != -1) { newSideIcons[14].drawSprite(216 + extraX + mhm, 307 + extraY + mhm2); }
            if (tabInterfaceIDs[16] != -1) { newSideIcons[15].drawSprite(7 + extraX + mhm, 306 + extraY + mhm2); }
        }
    }

    public void drawRedStones() {
        int extraX = isFullScreen ? extraWidth + 519 : 0;
        int extraX2 = isFullScreen ? +4 : 0;
        int extraX22 = isFullScreen ? -3 : 0;
        int extraY = isFullScreen ? extraHeight + 168 + 262 : 0;
        int extraY2 = isFullScreen ? extraHeight + 168 : 0;

        int extraXX = drawLongTabs ? -232 : 0;
        int extraYY = drawLongTabs ? +35 : 0;

        int mhm = drawLongTabs ? +4 : 0;
        int mhm2 = drawLongTabs ? -6 : 0;

        int mhm4 = drawLongTabs ? +1 : 0;

        int extraXX22 = drawLongTabs ? -241 : 0;
        int extraYY22 = drawLongTabs ? +37 : 0;
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            if (isFullScreen && tabInterfaceIDs[tabID] != -1) {
                switch (tabID) {
                    case 0:
                        if (!drawLongTabs) {
                            redStones[0].drawSprite(3 + extraX + extraXX, 0 + extraY + extraYY);
                        } else {
                            redStones[4].drawSprite(3 + extraX + extraXX + extraX2, 0 + extraY + extraYY);
                        }
                        break;
                    case 1:
                        redStones[4].drawSprite(41 + extraX + extraXX, 0 + extraY + extraYY);
                        break;
                    case 2:
                        redStones[4].drawSprite(74 + extraX + extraXX, 0 + extraY + extraYY);
                        break;
                    case 3:
                        redStones[4].drawSprite(107 + extraX + extraXX, 0 + extraY + extraYY);
                        break;
                    case 4:
                        redStones[4].drawSprite(140 + extraX + extraXX, 0 + extraY + extraYY);
                        break;
                    case 5:
                        redStones[4].drawSprite(173 + extraX + extraXX, 0 + extraY + extraYY);
                        break;
                    case 6:
                        if (!drawLongTabs) {
                            redStones[1].drawSprite(206 + extraX + extraXX, 0 + extraY + extraYY);
                        } else {
                            redStones[4].drawSprite(206 + extraX + extraXX, 0 + extraY + extraYY);
                        }
                        break;
                    case 7:
                        if (!drawLongTabs) {
                            redStones[2].drawSprite(3 + extraX, 298 + extraY - 262);
                        } else {
                            redStones[4].drawSprite(3 + extraX + extraX2, 298 + extraY - 262);
                        }
                        break;
                    case 8:
                        redStones[4].drawSprite(41 + extraX, 298 + extraY - 262);
                        break;
                    case 9:
                        redStones[4].drawSprite(74 + extraX, 298 + extraY - 262);
                        break;
                    case 10:
                        redStones[4].drawSprite(107 + extraX, 298 + extraY - 262);
                        break;
                    case 11:
                        redStones[4].drawSprite(140 + extraX, 298 + extraY - 262);
                        break;
                    case 12:
                        redStones[4].drawSprite(173 + extraX, 298 + extraY - 262);
                        break;
                    case 13:
                        if (!drawLongTabs) {
                            redStones[3].drawSprite(206 + extraX, 298 + extraY - 262);
                        } else {
                            redStones[4].drawSprite(206 + extraX + extraX22, 298 + extraY - 262);
                        }
                        break;
                }
            } else if (isFullScreen == false && tabInterfaceIDs[tabID] != -1) {
                switch (tabID) {
                    case 0:
                        redStones[0].drawSprite(3, 0);
                        break;
                    case 1:
                        redStones[4].drawSprite(41, 0);
                        break;
                    case 2:
                        redStones[4].drawSprite(74, 0);
                        break;
                    case 3:
                        redStones[4].drawSprite(107, 0);
                        break;
                    case 4:
                        redStones[4].drawSprite(140, 0);
                        break;
                    case 5:
                        redStones[4].drawSprite(173, 0);
                        break;
                    case 6:
                        redStones[1].drawSprite(206, 0);
                        break;
                    case 7:
                        redStones[2].drawSprite(3, 298);
                        break;
                    case 8:
                        redStones[4].drawSprite(41, 298);
                        break;
                    case 9:
                        redStones[4].drawSprite(74, 298);
                        break;
                    case 10:
                        redStones[4].drawSprite(107, 298);
                        break;
                    case 11:
                        redStones[4].drawSprite(140, 298);
                        break;
                    case 12:
                        redStones[4].drawSprite(173, 298);
                        break;
                    case 13:
                        redStones[3].drawSprite(206, 298);
                        break;
                }
            }

        } else if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            drawTabHover();
            // if(tabId > tabInterfaceIDs.length)
            // cd;
            if (tabInterfaceIDs[tabID] != -1) {
                extraX = isFullScreen ? extraWidth + 511 : 0;
                if (tabID == 0 && drawLongTabs) {
                    tabClicked.drawSprite(32 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 1 && drawLongTabs) {
                    tabClicked.drawSprite(62 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 2 && drawLongTabs) {
                    tabClicked.drawSprite(92 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }

                if (tabID == 0 && !drawLongTabs) {
                    tabClicked.drawSprite(2 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 1 && !drawLongTabs) {
                    tabClicked.drawSprite(32 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 2 && !drawLongTabs) {
                    tabClicked.drawSprite(62 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 14 && !drawLongTabs) {
                    tabClicked.drawSprite(92 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }

                if (tabID == 3) {
                    tabClicked.drawSprite(122 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 4) {
                    tabClicked.drawSprite(152 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 5) {
                    tabClicked.drawSprite(182 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 6) {
                    tabClicked.drawSprite(212 + extraX + extraXX22 + mhm, 0 + extraY + extraYY22 + mhm2);
                }
                if (tabID == 16) {
                    tabClicked.drawSprite(2 + extraX + extraXX22 + mhm, 298 + extraY2 + extraYY22 + mhm2);
                }
                if (tabID == 8) { tabClicked.drawSprite(32 + extraX + mhm, 298 + extraY2 + mhm2 + mhm4); }

                if (tabID == 9) { tabClicked.drawSprite(62 + extraX + mhm, 298 + extraY2 + mhm2 + mhm4); }
                if (tabID == 7) { tabClicked.drawSprite(92 + extraX + mhm, 298 + extraY2 + mhm2 + mhm4); }
                if (tabID == 11) { tabClicked.drawSprite(122 + extraX + mhm, 298 + extraY2 + mhm2 + mhm4); }
                if (tabID == 12) { tabClicked.drawSprite(152 + extraX + mhm, 298 + extraY2 + mhm2 + mhm4); }
                if (tabID == 13) { tabClicked.drawSprite(182 + extraX + mhm, 298 + extraY2 + mhm2 + mhm4); }
                if (tabID == 15) { tabClicked.drawSprite(212 + extraX + mhm, 298 + extraY2 + mhm2 + mhm4); }
            }

        }
    }

    private void drawTabArea() {
        int x = isFullScreen ? extraWidth + 519 : 0;
        int y = isFullScreen ? extraHeight + 168 : 0;
        if (!isFullScreen) { aRSImageProducer_1163.initializeDrawingArea(); }
        Texture.anIntArray1472 = anIntArray1181;
        if (!isFullScreen) {
            tabArea.drawSprite(0 + x, 0 + y);
        }
        if (isFullScreen && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554) && drawLongTabs) {
            fullScreenSprites[1].drawSprite(-247 + x, 299 + y);
        }
        if (isFullScreen && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554) && !drawLongTabs) {
            fullScreenSprites[2].drawSprite(-3 + x, 262 + y);
        }
        if (isFullScreen && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554) && !drawLongTabs) {
            tabs554.drawSprite(-3 + x, 262 + y);
        }
        if (isFullScreen && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554) && drawLongTabs) {
            long554.drawSprite(-217 - 10 + 15 + x, 293 + y);
            long554.drawSprite(-217 - 10 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 20 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 50 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 80 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 110 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 140 + 15 + 15 + 15 + x, 293 + y);

            long554.drawSprite(-217 + 170 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 200 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 230 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 260 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 290 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 320 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 350 + 15 + 15 + 15 + x, 293 + y);
            long554.drawSprite(-217 + 380 + 15 + 15 + 15 + x, 293 + y);
        }
        if (invOverlayInterfaceID == -1) {
            drawRedStones();
            drawSideIcons();
        }
        if (isFullScreen) { drawTabBack(); } else {
            if (invOverlayInterfaceID != -1) {
                drawInterface(0, 28 + x, RSInterface.interfaceCache[invOverlayInterfaceID], 37 + y);
            } else if (tabInterfaceIDs[tabID] != -1) {
                drawInterface(0, 28 + x, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], 37 + y);
            }
        }
        if (menuOpen && menuScreenArea == 1) { drawMenu(); }
        if (!isFullScreen) {
            aRSImageProducer_1163.drawGraphics(168 + extraHeight, isFullScreen ? bufferGraphics : super.graphics, 519 + extraWidth);
        }
        aRSImageProducer_1165.initializeDrawingArea();
        Texture.anIntArray1472 = anIntArray1182;
    }

    public void drawTabBack() {
        if (!drawLongTabs) { fullScreenSprites[0].drawSprite2(554 + extraWidth, 156 + extraHeight); }
        if (invOverlayInterfaceID != -1 && !drawLongTabs) {
            drawInterface(0, 519 + 41 + extraWidth, RSInterface.interfaceCache[invOverlayInterfaceID], 168 + extraHeight - 3);
        } else if (tabInterfaceIDs[tabID] != -1 && !drawLongTabs) {
            drawInterface(0, 519 + 41 + extraWidth, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], 168 + extraHeight - 3);
        }

        if (drawLongTabs && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            fullScreenSprites[0].drawSprite2(554 + extraWidth, 187 + extraHeight);// 193
        }
        if (invOverlayInterfaceID != -1 && drawLongTabs && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            drawInterface(0, 519 + 41 + extraWidth, RSInterface.interfaceCache[invOverlayInterfaceID], 168 + 35 - 6 + extraHeight - 3);
        } else if (tabInterfaceIDs[tabID] != -1 && drawLongTabs && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            drawInterface(0, 519 + 41 + extraWidth, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], 168 + 35 - 6 + extraHeight - 3);
        }

        if (drawLongTabs && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            fullScreenSprites[0].drawSprite2(554 + extraWidth, 193 + extraHeight);// 25
        }
        if (invOverlayInterfaceID != -1 && drawLongTabs && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            drawInterface(0, 519 + 41 + extraWidth, RSInterface.interfaceCache[invOverlayInterfaceID], 168 + 35 + extraHeight - 3);
        } else if (tabInterfaceIDs[tabID] != -1 && drawLongTabs && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            drawInterface(0, 519 + 41 + extraWidth, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], 168 + 35 + extraHeight - 3);
        }
    }

    private void method37(int j) {
        if (!lowMem) {
            if (Texture.anIntArray1480[17] >= j) {
                Background background = Texture.aBackgroundArray1474s[17];
                int k = background.anInt1452 * background.anInt1453 - 1;
                // fire cape apparently?
                int j1 = background.anInt1452 * anInt945 * 2;
                byte abyte0[] = background.aByteArray1450;
                byte abyte3[] = aByteArray912;
                for (int i2 = 0; i2 <= k; i2++) { abyte3[i2] = abyte0[i2 - j1 & k]; }

                background.aByteArray1450 = abyte3;
                aByteArray912 = abyte0;
                Texture.method370(17);
                anInt854++;
                if (anInt854 > 1235) {
                    anInt854 = 0;
                    stream.createFrame(226);
                    stream.writeByte(0);
                    int l2 = stream.currentOffset;
                    stream.writeShort(58722);
                    stream.writeByte(240);
                    stream.writeShort((int) (Math.random() * 65536D));
                    stream.writeByte((int) (Math.random() * 256D));
                    if ((int) (Math.random() * 2D) == 0) { stream.writeShort(51825); }
                    stream.writeByte((int) (Math.random() * 256D));
                    stream.writeShort((int) (Math.random() * 65536D));
                    stream.writeShort(7130);
                    stream.writeShort((int) (Math.random() * 65536D));
                    stream.writeShort(61657);
                    stream.writeByteC(stream.currentOffset - l2);
                }
            }
            if (Texture.anIntArray1480[24] >= j) {
                Background background_1 = Texture.aBackgroundArray1474s[24];
                int l = background_1.anInt1452 * background_1.anInt1453 - 1;
                int k1 = background_1.anInt1452 * anInt945 * 2;
                byte abyte1[] = background_1.aByteArray1450;
                byte abyte4[] = aByteArray912;
                for (int j2 = 0; j2 <= l; j2++) { abyte4[j2] = abyte1[j2 - k1 & l]; }

                background_1.aByteArray1450 = abyte4;
                aByteArray912 = abyte1;
                Texture.method370(24);
            }
            if (Texture.anIntArray1480[34] >= j) {
                Background background_2 = Texture.aBackgroundArray1474s[34];
                int i1 = background_2.anInt1452 * background_2.anInt1453 - 1;
                int l1 = background_2.anInt1452 * anInt945 * 2;
                byte abyte2[] = background_2.aByteArray1450;
                byte abyte5[] = aByteArray912;
                for (int k2 = 0; k2 <= i1; k2++) { abyte5[k2] = abyte2[k2 - l1 & i1]; }

                background_2.aByteArray1450 = abyte5;
                aByteArray912 = abyte2;
                Texture.method370(34);
            }
            if (Texture.anIntArray1480[40] >= j) {
                Background background_2 = Texture.aBackgroundArray1474s[40];
                int i1 = background_2.anInt1452 * background_2.anInt1453 - 1;
                int l1 = background_2.anInt1452 * anInt945 * 2;
                byte abyte2[] = background_2.aByteArray1450;
                byte abyte5[] = aByteArray912;
                for (int k2 = 0; k2 <= i1; k2++) { abyte5[k2] = abyte2[k2 - l1 & i1]; }

                background_2.aByteArray1450 = abyte5;
                aByteArray912 = abyte2;
                Texture.method370(40);
            }
        }
    }

    private void method38() {
        for (int i = -1; i < playerCount; i++) {
            int j;
            if (i == -1) { j = myPlayerIndex; } else { j = playerIndices[i]; }
            Player player = playerArray[j];
            if (player != null && player.textCycle > 0) {
                player.textCycle--;
                if (player.textCycle == 0) { player.textSpoken = null; }
            }
        }
        for (int k = 0; k < npcCount; k++) {
            int l = npcIndices[k];
            NPC npc = npcArray[l];
            if (npc != null && npc.textCycle > 0) {
                npc.textCycle--;
                if (npc.textCycle == 0) { npc.textSpoken = null; }
            }
        }
    }

    private void calcCameraPos() {
        int i = anInt1098 * 128 + 64;
        int j = anInt1099 * 128 + 64;
        int k = method42(plane, j, i) - anInt1100;
        if (xCameraPos < i) {
            xCameraPos += anInt1101 + ((i - xCameraPos) * anInt1102) / 1000;
            if (xCameraPos > i) { xCameraPos = i; }
        }
        if (xCameraPos > i) {
            xCameraPos -= anInt1101 + ((xCameraPos - i) * anInt1102) / 1000;
            if (xCameraPos < i) { xCameraPos = i; }
        }
        if (zCameraPos < k) {
            zCameraPos += anInt1101 + ((k - zCameraPos) * anInt1102) / 1000;
            if (zCameraPos > k) { zCameraPos = k; }
        }
        if (zCameraPos > k) {
            zCameraPos -= anInt1101 + ((zCameraPos - k) * anInt1102) / 1000;
            if (zCameraPos < k) { zCameraPos = k; }
        }
        if (yCameraPos < j) {
            yCameraPos += anInt1101 + ((j - yCameraPos) * anInt1102) / 1000;
            if (yCameraPos > j) { yCameraPos = j; }
        }
        if (yCameraPos > j) {
            yCameraPos -= anInt1101 + ((yCameraPos - j) * anInt1102) / 1000;
            if (yCameraPos < j) { yCameraPos = j; }
        }
        i = anInt995 * 128 + 64;
        j = anInt996 * 128 + 64;
        k = method42(plane, j, i) - anInt997;
        int l = i - xCameraPos;
        int i1 = k - zCameraPos;
        int j1 = j - yCameraPos;
        int k1 = (int) Math.sqrt(l * l + j1 * j1);
        int l1 = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
        int i2 = (int) (Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
        if (l1 < 128) { l1 = 128; }
        if (l1 > 383) { l1 = 383; }
        if (yCameraCurve < l1) {
            yCameraCurve += anInt998 + ((l1 - yCameraCurve) * anInt999) / 1000;
            if (yCameraCurve > l1) { yCameraCurve = l1; }
        }
        if (yCameraCurve > l1) {
            yCameraCurve -= anInt998 + ((yCameraCurve - l1) * anInt999) / 1000;
            if (yCameraCurve < l1) { yCameraCurve = l1; }
        }
        int j2 = i2 - xCameraCurve;
        if (j2 > 1024) { j2 -= 2048; }
        if (j2 < -1024) { j2 += 2048; }
        if (j2 > 0) {
            xCameraCurve += anInt998 + (j2 * anInt999) / 1000;
            xCameraCurve &= 0x7ff;
        }
        if (j2 < 0) {
            xCameraCurve -= anInt998 + (-j2 * anInt999) / 1000;
            xCameraCurve &= 0x7ff;
        }
        int k2 = i2 - xCameraCurve;
        if (k2 > 1024) { k2 -= 2048; }
        if (k2 < -1024) { k2 += 2048; }
        if (k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0) { xCameraCurve = i2; }
    }

    private void drawMenu() {
        int i = menuOffsetX;
        int j = menuOffsetY;
        int k = menuWidth;
        int j1 = super.mouseX;
        int k1 = super.mouseY;
        int l = menuHeight + 1;
        int i1 = 0x5d5447;
        if (menuScreenArea == 1 && isFullScreen) {
            i += 519;// +extraWidth;
            j += 168;// +extraHeight;
        }
        if (menuScreenArea == 2 && isFullScreen) {
            j += 338;
        }
        if (menuScreenArea == 3 && isFullScreen) {
            i += 515;
            j += 0;
        }
        if (menuScreenArea == 0) {
            j1 -= 4;
            k1 -= 4;
        }
        if (menuScreenArea == 1) {
            if (!isFullScreen) {
                j1 -= 519;
                k1 -= 168;
            }
        }
        if (menuScreenArea == 2) {
            if (!isFullScreen) {
                j1 -= 17;
                k1 -= 338;
            }
        }
        if (menuScreenArea == 3 && !isFullScreen) {
            j1 -= 515;
            k1 -= 0;
        }
        if (!Configuration.getInstance().getValue(MenuBar.class)) {
            DrawingArea.method336(l, j, i, i1, k);
            DrawingArea.method335(i1, j, k, l, 150, i);
            DrawingArea.method336(16, j + 1, i + 1, 0, k - 2);
            DrawingArea.method335(0, j + 1, k - 2, 16, 150, i + 1);
            DrawingArea.fillPixels(i + 1, k - 2, l - 19, 0, j + 18);
            DrawingArea.method338(j + 18, l - 19, 150, 0, k - 2, i + 1);
            chatTextDrawingArea.method385(0xc6b895, "Choose Option", j + 14, i + 3);
            chatTextDrawingArea.method385(0xc6b895, "Choose Option", j + 14, i + 3);
            for (int l1 = 0; l1 < menuActionRow; l1++) {
                int i2 = j + 31 + (menuActionRow - 1 - l1) * 15;
                int j2 = 0xffffff;
                if (j1 > i && j1 < i + k && k1 > i2 - 13 && k1 < i2 + 3) { j2 = 0xffff00; }
                chatTextDrawingArea.method389(true, i + 3, j2, menuActionName[l1], i2);
            }
        } else if (Configuration.getInstance().getValue(MenuBar.class)) {
            // DrawingArea.drawPixels(height, yPos, xPos, color, width);
            // DrawingArea.fillPixels(xPos, width, height, color, yPos);
            DrawingArea.drawPixels(l - 4, j + 2, i, 0x706a5e, k);
            DrawingArea.drawPixels(l - 2, j + 1, i + 1, 0x706a5e, k - 2);
            DrawingArea.drawPixels(l, j, i + 2, 0x706a5e, k - 4);
            DrawingArea.drawPixels(l - 2, j + 1, i + 3, 0x2d2822, k - 6);
            DrawingArea.drawPixels(l - 4, j + 2, i + 2, 0x2d2822, k - 4);
            DrawingArea.drawPixels(l - 6, j + 3, i + 1, 0x2d2822, k - 2);
            DrawingArea.drawPixels(l - 22, j + 19, i + 2, 0x524a3d, k - 4);
            DrawingArea.drawPixels(l - 22, j + 20, i + 3, 0x524a3d, k - 6);
            DrawingArea.drawPixels(l - 23, j + 20, i + 3, 0x2b271c, k - 6);
            DrawingArea.fillPixels(i + 3, k - 6, 1, 0x2a291b, j + 2);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x2a261b, j + 3);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x252116, j + 4);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x211e15, j + 5);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x1e1b12, j + 6);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x1a170e, j + 7);
            DrawingArea.fillPixels(i + 2, k - 4, 2, 0x15120b, j + 8);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x100d08, j + 10);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x090a04, j + 11);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x080703, j + 12);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x090a04, j + 13);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x070802, j + 14);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x090a04, j + 15);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x070802, j + 16);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x090a04, j + 17);
            DrawingArea.fillPixels(i + 2, k - 4, 1, 0x2a291b, j + 18);
            DrawingArea.fillPixels(i + 3, k - 6, 1, 0x564943, j + 19);
            chatTextDrawingArea.method385(0xc6b895, "Choose Option", j + 14, i + 3);
            int x = isFullScreen ? extraWidth : 0;
            int y = isFullScreen ? extraHeight : 0;
            for (int l1 = 0; l1 < menuActionRow; l1++) {
                int i2 = j + 31 + (menuActionRow - 1 - l1) * 15;
                int j2 = 0xc6b895;
                if (j1 > i && j1 < i + k && k1 > i2 - 13 && k1 < i2 + 3) {
                    DrawingArea.drawPixels(15, i2 - 11, i + 3, 0x6f695d, menuWidth - 6);
                    j2 = 0xeee5c6;
                }
                chatTextDrawingArea.method389(true, i + 4, j2, menuActionName[l1], i2 + 1);
            }
        }
    }

    private void addFriend(long l) {
        try {
            if (l == 0L) { return; }
            if (friendsCount >= 100 && anInt1046 != 1) {
                pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
                return;
            }
            if (friendsCount >= 200) {
                pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
                return;
            }
            String s = TextClass.fixName(TextClass.nameForLong(l));
            for (int i = 0; i < friendsCount; i++) {
                if (friendsListAsLongs[i] == l) {
                    pushMessage(s + " is already on your friend list", 0, "");
                    return;
                }
            }
            for (int j = 0; j < ignoreCount; j++) {
                if (ignoreListAsLongs[j] == l) {
                    pushMessage("Please remove " + s + " from your ignore list first", 0, "");
                    return;
                }
            }

            if (s.equals(myPlayer.name)) {
                return;
            } else {
                friendsList[friendsCount] = s;
                friendsListAsLongs[friendsCount] = l;
                friendsNodeIDs[friendsCount] = 0;
                friendsCount++;
                needDrawTabArea = true;
                stream.createFrame(188);
                stream.writeLong(l);
                return;
            }
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("15283, " + (byte) 68 + ", " + l + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private int method42(int i, int j, int k) {
        int l = k >> 7;
        int i1 = j >> 7;
        if (l < 0 || i1 < 0 || l > 103 || i1 > 103) { return 0; }
        int j1 = i;
        if (j1 < 3 && (byteGroundArray[1][l][i1] & 2) == 2) { j1++; }
        int k1 = k & 0x7f;
        int l1 = j & 0x7f;
        int i2 = intGroundArray[j1][l][i1] * (128 - k1) + intGroundArray[j1][l + 1][i1] * k1 >> 7;
        int j2 = intGroundArray[j1][l][i1 + 1] * (128 - k1) + intGroundArray[j1][l + 1][i1 + 1] * k1 >> 7;
        return i2 * (128 - l1) + j2 * l1 >> 7;
    }

    public void resetLogout() {
        try {
            if (socketStream != null) { socketStream.close(); }
        } catch (Exception _ex) {
        }
        alertHandler.alert = null;
        socketStream = null;
        loggedIn = false;
        rest = false;
        loginScreenState = 0;
        loginMessage1 = "";
        loginMessage2 = "";
        unlinkMRUNodes();
        //resize(765, 503);
        for (int i = 0; i < 4; i++) { aClass11Array1230[i].method210(); }
        System.gc();
        stopMidi();
        currentSong = -1;
        nextSong = -1;
        prevSong = 0;
        Configuration.getInstance().save();
    }

    private void method45() {
        aBoolean1031 = true;
        for (int j = 0; j < 7; j++) {
            anIntArray1065[j] = -1;
            for (int k = 0; k < IdentityKit.length; k++) {
                if (IdentityKit.cache[k].aBoolean662 || IdentityKit.cache[k].anInt657 != j + (aBoolean1047 ? 0 : 7)) {
                    continue;
                }
                anIntArray1065[j] = k;
                break;
            }
        }
    }

    private void method46(int i, Stream stream) {
        while (stream.bitPosition + 21 < i * 8) {
            int k = stream.readBits(14);
            if (k == 16383) { break; }
            if (npcArray[k] == null) { npcArray[k] = new NPC(); }
            NPC npc = npcArray[k];
            npcIndices[npcCount++] = k;
            npc.anInt1537 = loopCycle;
            int l = stream.readBits(5);
            if (l > 15) { l -= 32; }
            int i1 = stream.readBits(5);
            if (i1 > 15) { i1 -= 32; }
            int j1 = stream.readBits(1);
            npc.desc = EntityDef.forID(stream.readBits(14));
            int k1 = stream.readBits(1);
            if (k1 == 1) { anIntArray894[anInt893++] = k; }
            npc.anInt1540 = npc.desc.aByte68;
            npc.anInt1504 = npc.desc.anInt79;
            npc.anInt1554 = npc.desc.walkAnim;
            npc.anInt1555 = npc.desc.anInt58;
            npc.anInt1556 = npc.desc.anInt83;
            npc.anInt1557 = npc.desc.anInt55;
            npc.anInt1511 = npc.desc.standAnim;
            npc.setPos(myPlayer.smallX[0] + i1, myPlayer.smallY[0] + l, j1 == 1);
        }
        stream.finishBitAccess();
    }

    private void method47(boolean flag) {
        if (myPlayer.x >> 7 == destX && myPlayer.y >> 7 == destY) { destX = 0; }
        int j = playerCount;
        if (flag) { j = 1; }
        for (int l = 0; l < j; l++) {
            Player player;
            int i1;
            if (flag) {
                player = myPlayer;
                i1 = myPlayerIndex << 14;
            } else {
                player = playerArray[playerIndices[l]];
                i1 = playerIndices[l] << 14;
            }
            if (player == null || !player.isVisible()) { continue; }
            player.aBoolean1699 = (lowMem && playerCount > 50 || playerCount > 200) && !flag && player.anInt1517 == player.anInt1511;
            int j1 = player.x >> 7;
            int k1 = player.y >> 7;
            if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104) { continue; }
            if (player.aModel_1714 != null && loopCycle >= player.anInt1707 && loopCycle < player.anInt1708) {
                player.aBoolean1699 = false;
                player.anInt1709 = method42(plane, player.y, player.x);
                worldController.method286(plane, player.y, player, player.anInt1552, player.anInt1722, player.x, player.anInt1709, player.anInt1719, player.anInt1721, i1, player.anInt1720);
                continue;
            }
            if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
                if (anIntArrayArray929[j1][k1] == anInt1265) { continue; }
                anIntArrayArray929[j1][k1] = anInt1265;
            }
            player.anInt1709 = method42(plane, player.y, player.x);
            worldController.method285(plane, player.anInt1552, player.anInt1709, i1, player.y, 60, player.x, player, player.aBoolean1541);
        }
    }

    private boolean promptUserForInput(RSInterface class9) {
        int j = class9.contentType;
        if (anInt900 == 2) {
            if (j == 201) {
                inputTaken = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 1;
                aString1121 = "Enter name of friend to add to list";
            }
            if (j == 202) {
                inputTaken = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 2;
                aString1121 = "Enter name of friend to delete from list";
            }
        }
        if (class9.id == 21004) {
            System.out.println("CHECK");
            stream.createFrame(185);
            stream.writeShort(21004);
            inputTaken = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 11;
            aString1121 = "Enter a keyword to search for";
            variousSettings[1010] = variousSettings[1010] == 0 ? 1 : 0;
        }
        if (j == 205) {
            anInt1011 = 250;
            return true;
        }
        if (j == 501) {
            inputTaken = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 4;
            aString1121 = "Enter name of player to add to list";
        }
        if (j == 502) {
            inputTaken = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 5;
            aString1121 = "Enter name of player to delete from list";
        }
        if (j == 550) {
            inputTaken = true;
            inputDialogState = 0;
            messagePromptRaised = true;
            promptInput = "";
            friendsListAction = 6;
            aString1121 = "Enter the name of the chat you wish to join";
        }
        if (j >= 300 && j <= 313) {
            int k = (j - 300) / 2;
            int j1 = j & 1;
            int i2 = anIntArray1065[k];
            if (i2 != -1) {
                do {
                    if (j1 == 0 && --i2 < 0) { i2 = IdentityKit.length - 1; }
                    if (j1 == 1 && ++i2 >= IdentityKit.length) { i2 = 0; }
                }
                while (IdentityKit.cache[i2].aBoolean662 || IdentityKit.cache[i2].anInt657 != k + (aBoolean1047 ? 0 : 7));
                anIntArray1065[k] = i2;
                aBoolean1031 = true;
            }
        }
        if (j >= 314 && j <= 323) {
            int l = (j - 314) / 2;
            int k1 = j & 1;
            int j2 = anIntArray990[l];
            if (k1 == 0 && --j2 < 0) { j2 = anIntArrayArray1003[l].length - 1; }
            if (k1 == 1 && ++j2 >= anIntArrayArray1003[l].length) { j2 = 0; }
            anIntArray990[l] = j2;
            aBoolean1031 = true;
        }
        if (j == 324 && !aBoolean1047) {
            aBoolean1047 = true;
            method45();
        }
        if (j == 325 && aBoolean1047) {
            aBoolean1047 = false;
            method45();
        }
        // search here martin
        if (j == 326) {
            stream.createFrame(101);
            stream.writeByte(aBoolean1047 ? 0 : 1);
            for (int i1 = 0; i1 < 7; i1++) { stream.writeByte(anIntArray1065[i1]); }

            for (int l1 = 0; l1 < 5; l1++) { stream.writeByte(anIntArray990[l1]); }

            return true;
        }
        if (j == 613) { canMute = !canMute; }
        if (j == 600) { class9.message = customInterfaceInput + ""; }
        if (j >= 601 && j <= 612) {
            clearTopInterfaces();
            if (reportAbuseInput.length() > 0) {
                stream.createFrame(218);
                stream.writeLong(TextClass.longForName(reportAbuseInput));
                stream.writeByte(j - 601);
                stream.writeByte(canMute ? 1 : 0);
            }
        }
        return false;
    }

    private void method49(Stream stream) {
        for (int j = 0; j < anInt893; j++) {
            int k = anIntArray894[j];
            Player player = playerArray[k];
            int l = stream.readUByte();
            if ((l & 0x40) != 0) { l += stream.readUByte() << 8; }
            method107(l, k, stream, player);
        }
    }

    private void method50(int i, int k, int l, int i1, int j1) {
        int k1 = worldController.method300(j1, l, i);
        if (k1 != 0) {
            int l1 = worldController.method304(j1, l, i, k1);
            int k2 = l1 >> 6 & 3;
            int i3 = l1 & 0x1f;
            int k3 = k;
            if (k1 > 0) { k3 = i1; }
            int ai[] = aClass30_Sub2_Sub1_Sub1_1263.myPixels;
            int k4 = 24624 + l * 4 + (103 - i) * 512 * 4;
            //int i5 = k1 >> 14 & 0x7fff;
            int i5 = worldController.method301_1(j1, l, i);
            ObjectDef class46_2 = ObjectDef.forID(i5);
            if (class46_2.anInt758 != -1) {
                Background background_2 = mapScenes[class46_2.anInt758];
                if (background_2 != null) {
                    int i6 = (class46_2.anInt744 * 4 - background_2.anInt1452) / 2;
                    int j6 = (class46_2.anInt761 * 4 - background_2.anInt1453) / 2;
                    background_2.drawBackground(48 + l * 4 + i6, 48 + (104 - i - class46_2.anInt761) * 4 + j6);
                }
            } else {
                if (i3 == 0 || i3 == 2) {
                    if (k2 == 0) {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else if (k2 == 1) {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 3) {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
                }
                if (i3 == 3) {
                    if (k2 == 0) { ai[k4] = k3; } else if (k2 == 1) { ai[k4 + 3] = k3; } else if (k2 == 2) {
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 3) { ai[k4 + 1536] = k3; }
                }
                if (i3 == 2) {
                    if (k2 == 3) {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else if (k2 == 0) {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else if (k2 == 1) {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
                }
            }
        }
        k1 = worldController.method302(j1, l, i);
        if (k1 != 0) {
            int i2 = worldController.method304(j1, l, i, k1);
            int l2 = i2 >> 6 & 3;
            int j3 = i2 & 0x1f;
            //int l3 = k1 >> 14 & 0x7fff;
            int l3 = worldController.method302_1(j1, l, i);
            ObjectDef class46_1 = ObjectDef.forID(l3);
            if (class46_1.anInt758 != -1) {
                Background background_1 = mapScenes[class46_1.anInt758];
                if (background_1 != null) {
                    int j5 = (class46_1.anInt744 * 4 - background_1.anInt1452) / 2;
                    int k5 = (class46_1.anInt761 * 4 - background_1.anInt1453) / 2;
                    background_1.drawBackground(48 + l * 4 + j5, 48 + (104 - i - class46_1.anInt761) * 4 + k5);
                }
            } else if (j3 == 9) {
                int l4 = 0xeeeeee;
                if (k1 > 0) { l4 = 0xee0000; }
                int ai1[] = aClass30_Sub2_Sub1_Sub1_1263.myPixels;
                int l5 = 24624 + l * 4 + (103 - i) * 512 * 4;
                if (l2 == 0 || l2 == 2) {
                    ai1[l5 + 1536] = l4;
                    ai1[l5 + 1024 + 1] = l4;
                    ai1[l5 + 512 + 2] = l4;
                    ai1[l5 + 3] = l4;
                } else {
                    ai1[l5] = l4;
                    ai1[l5 + 512 + 1] = l4;
                    ai1[l5 + 1024 + 2] = l4;
                    ai1[l5 + 1536 + 3] = l4;
                }
            }
        }
        k1 = worldController.method303_1(j1, l, i);
        if (k1 != 0) {
            //int j2 = k1 >> 14 & 0x7fff;
            ObjectDef class46 = ObjectDef.forID(k1);
            if (class46.anInt758 != -1) {
                Background background = mapScenes[class46.anInt758];
                if (background != null) {
                    int i4 = (class46.anInt744 * 4 - background.anInt1452) / 2;
                    int j4 = (class46.anInt761 * 4 - background.anInt1453) / 2;
                    background.drawBackground(48 + l * 4 + i4, 48 + (104 - i - class46.anInt761) * 4 + j4);
                }
            }
        }
    }

    private void loadingStages() {
        if (lowMem && loadingStage == 2 && ObjectManager.anInt131 != plane) {
            aRSImageProducer_1165.initializeDrawingArea();
            loadingPleaseWait.drawSprite(8, 9);
            aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
            loadingStage = 1;
            aLong824 = System.currentTimeMillis();
        }
        if (loadingStage == 1) {
            int j = method54();
            if (j != 0 && System.currentTimeMillis() - aLong824 > 0x57e40L) {
                Signlink.reporterror(Configuration.getInstance().getValue(Username.class) + " glcfb " + aLong1215 + "," + j + "," + lowMem + "," + decompressors[0] + "," + onDemandFetcher.getNodeCount() + "," + plane + "," + anInt1069 + "," + anInt1070);
                aLong824 = System.currentTimeMillis();
            }
        }
        if (loadingStage == 2 && plane != anInt985) {
            anInt985 = plane;
            method24(plane);
        }
    }

    private int method54() {
        for (int i = 0; i < aByteArrayArray1183.length; i++) {
            if (aByteArrayArray1183[i] == null && anIntArray1235[i] != -1) { return -1; }
            if (aByteArrayArray1247[i] == null && anIntArray1236[i] != -1) { return -2; }
        }
        boolean flag = true;
        for (int j = 0; j < aByteArrayArray1183.length; j++) {
            byte abyte0[] = aByteArrayArray1247[j];
            if (abyte0 != null) {
                int k = (anIntArray1234[j] >> 8) * 64 - baseX;
                int l = (anIntArray1234[j] & 0xff) * 64 - baseY;
                if (aBoolean1159) {
                    k = 10;
                    l = 10;
                }
                try {
                    flag &= ObjectManager.method189(k, abyte0, l);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!flag) { return -3; }
        if (aBoolean1080) {
            return -4;
        } else {
            loadingStage = 2;
            ObjectManager.anInt131 = plane;
            method22();
            stream.createFrame(121);
            return 0;
        }
    }

    private void method55() {
        for (Animable_Sub4 class30_sub2_sub4_sub4 = (Animable_Sub4) aClass19_1013.reverseGetFirst(); class30_sub2_sub4_sub4 != null; class30_sub2_sub4_sub4 = (Animable_Sub4) aClass19_1013.reverseGetNext()) {
            if (class30_sub2_sub4_sub4.anInt1597 != plane || loopCycle > class30_sub2_sub4_sub4.anInt1572) {
                class30_sub2_sub4_sub4.unlink();
            } else if (loopCycle >= class30_sub2_sub4_sub4.anInt1571) {
                if (class30_sub2_sub4_sub4.anInt1590 > 0) {
                    NPC npc = npcArray[class30_sub2_sub4_sub4.anInt1590 - 1];
                    if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312) {
                        class30_sub2_sub4_sub4.method455(loopCycle, npc.y, method42(class30_sub2_sub4_sub4.anInt1597, npc.y, npc.x) - class30_sub2_sub4_sub4.anInt1583, npc.x);
                    }
                }
                if (class30_sub2_sub4_sub4.anInt1590 < 0) {
                    int j = -class30_sub2_sub4_sub4.anInt1590 - 1;
                    Player player;
                    if (j == unknownInt10) { player = myPlayer; } else { player = playerArray[j]; }
                    if (player != null && player.x >= 0 && player.x < 13312 && player.y >= 0 && player.y < 13312) {
                        class30_sub2_sub4_sub4.method455(loopCycle, player.y, method42(class30_sub2_sub4_sub4.anInt1597, player.y, player.x) - class30_sub2_sub4_sub4.anInt1583, player.x);
                    }
                }
                class30_sub2_sub4_sub4.method456(anInt945);
                worldController.method285(plane, class30_sub2_sub4_sub4.anInt1595, (int) class30_sub2_sub4_sub4.aDouble1587, -1, (int) class30_sub2_sub4_sub4.aDouble1586, 60, (int) class30_sub2_sub4_sub4.aDouble1585, class30_sub2_sub4_sub4, false);
            }
        }

    }    public void processGameLoop() {
        if (rsAlreadyLoaded || loadingError || genericLoadingError) { return; }
        loopCycle++;
        if (!loggedIn) { processLoginScreenInput(); } else { mainGameProcessor(); }
        processOnDemandQueue();
    }

    public void drawLogo() {
        byte abyte0[] = titleStreamLoader.getDataForName("title.dat");
        Sprite sprite = new Sprite(abyte0, this);
        aRSImageProducer_1110.initializeDrawingArea();
        sprite.drawSprite(0, 0);
        aRSImageProducer_1111.initializeDrawingArea();
        sprite.drawSprite(-637, 0);
        aRSImageProducer_1107.initializeDrawingArea();
        sprite.drawSprite(-128, 0);
        aRSImageProducer_1108.initializeDrawingArea();
        sprite.drawSprite(-202, -371);
        aRSImageProducer_1109.initializeDrawingArea();
        sprite.drawSprite(-202, -171);
        aRSImageProducer_1112.initializeDrawingArea();
        sprite.drawSprite(0, -265);
        aRSImageProducer_1113.initializeDrawingArea();
        sprite.drawSprite(-562, -265);
        aRSImageProducer_1114.initializeDrawingArea();
        sprite.drawSprite(-128, -171);
        aRSImageProducer_1115.initializeDrawingArea();
        sprite.drawSprite(-562, -171);
        sprite = null;
        Object obj = null;
        Object obj1 = null;
        System.gc();
    }

    private void processOnDemandQueue() {
        do {
            OnDemandData onDemandData;
            do {
                onDemandData = onDemandFetcher.getNextNode();
                if (onDemandData == null) {
                    return;
                }
                if (onDemandData.dataType == 0) {
                    Model.method460(onDemandData.buffer, onDemandData.ID);
                    needDrawTabArea = true;
                    if (backDialogID != -1) {
                        inputTaken = true;
                    }
                }
                if (onDemandData.dataType == 1 && onDemandData.buffer != null) {
                    FrameReader.load(onDemandData.ID, onDemandData.buffer);
                }
                if (onDemandData.dataType == 2 && onDemandData.ID == nextSong && onDemandData.buffer != null) {
                    saveMidi(songChanging, onDemandData.buffer);
                }
                if (onDemandData.dataType == 3 && loadingStage == 1) {
                    for (int i = 0; i < aByteArrayArray1183.length; i++) {
                        if (anIntArray1235[i] == onDemandData.ID) {
                            aByteArrayArray1183[i] = onDemandData.buffer;
                            if (onDemandData.buffer == null) { anIntArray1235[i] = -1; }
                            break;
                        }
                        if (anIntArray1236[i] != onDemandData.ID) { continue; }
                        aByteArrayArray1247[i] = onDemandData.buffer;
                        if (onDemandData.buffer == null) { anIntArray1236[i] = -1; }
                        break;
                    }

                }
            } while (onDemandData.dataType != 93 || !onDemandFetcher.method564(onDemandData.ID));
            ObjectManager.method173(new Stream(onDemandData.buffer), onDemandFetcher);
        } while (true);
    }

    private boolean saveWave(byte abyte0[], int i) {
        return abyte0 == null || Signlink.wavesave(abyte0, i);
    }

    private void method60(int i) {
        RSInterface class9 = RSInterface.interfaceCache[i];
        if (class9 == null) { return; }
        for (int j = 0; j < class9.children.length; j++) {
            if (class9.children[j] == -1) { break; }
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j]];
            if (class9_1.type == 1) { method60(class9_1.id); }
            class9_1.anInt246 = 0;
            class9_1.anInt208 = 0;
        }
    }

    private void drawHeadIcon() {
        if (anInt855 != 2) { return; }
        calcEntityScreenPos((anInt934 - baseX << 7) + anInt937, anInt936 * 2, (anInt935 - baseY << 7) + anInt938);
        if (spriteDrawX > -1 && loopCycle % 20 < 10) {
            headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
        }
    }    private void loadTitleScreen() {
        loginBox = new Background(titleStreamLoader, "titlebox", 0);
        loginButton = new Sprite(titleStreamLoader, "titlebutton", 0);
        loginButtonHover = new Sprite(titleStreamLoader, "titlebutton", 1);
        textBox = new Sprite(titleStreamLoader, "titlebox", 1);
        textBoxHover = new Sprite(titleStreamLoader, "titlebox", 2);
        aBackgroundArray1152s = new Background[12];
        int j = 0;
        try {
            j = Integer.parseInt(getParameter("fl_icon"));
        } catch (Exception _ex) {
        }
        if (j == 0) {
            for (int k = 0; k < 12; k++) { aBackgroundArray1152s[k] = new Background(titleStreamLoader, "runes", k); }

        } else {
            for (int l = 0; l < 12; l++) {
                aBackgroundArray1152s[l] = new Background(titleStreamLoader, "runes", 12 + (l & 3));
            }

        }
        aClass30_Sub2_Sub1_Sub1_1201 = new Sprite(128, 265);
        aClass30_Sub2_Sub1_Sub1_1202 = new Sprite(128, 265);
        anIntArray851 = new int[256];
        for (int k1 = 0; k1 < 64; k1++) { anIntArray851[k1] = k1 * 0x40000; }

        for (int l1 = 0; l1 < 64; l1++) { anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1; }

        for (int i2 = 0; i2 < 64; i2++) { anIntArray851[i2 + 128] = 0xffff00 + 4 * i2; }

        for (int j2 = 0; j2 < 64; j2++) { anIntArray851[j2 + 192] = 0xffffff; }

        anIntArray852 = new int[256];
        for (int k2 = 0; k2 < 64; k2++) { anIntArray852[k2] = k2 * 1024; }

        for (int l2 = 0; l2 < 64; l2++) { anIntArray852[l2 + 64] = 65280 + 4 * l2; }

        for (int i3 = 0; i3 < 64; i3++) { anIntArray852[i3 + 128] = 65535 + 0x40000 * i3; }

        for (int j3 = 0; j3 < 64; j3++) { anIntArray852[j3 + 192] = 0xffffff; }

        anIntArray853 = new int[256];
        for (int k3 = 0; k3 < 64; k3++) { anIntArray853[k3] = k3 * 4; }

        for (int l3 = 0; l3 < 64; l3++) { anIntArray853[l3 + 64] = 255 + 0x40000 * l3; }

        for (int i4 = 0; i4 < 64; i4++) { anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4; }

        for (int j4 = 0; j4 < 64; j4++) { anIntArray853[j4 + 192] = 0xffffff; }

        anIntArray1190 = new int[32768];
        anIntArray1191 = new int[32768];
        randomizeBackground(null);
        Loader.getInstance().updateValues(10, "Please Wait...");
    }

    private void mainGameProcessor() {
        // System.out.println("MainGameProcessor()");
        if (anInt1104 > 1) { anInt1104--; }
        if (anInt1011 > 0) { anInt1011--; }
        for (int j = 0; j < 5; j++) { if (!parsePacket()) { break; } }
        if (!loggedIn) { return; }
        synchronized (mouseDetection.syncObject) {
            if (clientWidth - 765 != extraWidth || clientHeight - 503 != extraHeight || (isFullScreen && (getWidth() != clientWidth || getHeight() != clientHeight))) {
                hasBeenBlanked = false;
                if (isFullScreen) {
                    extraWidth = getWidth() - 765;
                    extraHeight = getHeight() - 503;
                    clientWidth = extraWidth + 765;
                    clientHeight = extraHeight + 503;
                } else {
                    clientWidth = 765;
                    clientHeight = 503;
                    extraWidth = 0;
                    extraHeight = 0;
                }
                if (isFullScreen && extraWidth >= 230) {
                    drawLongTabs = true;
                } else {
                    drawLongTabs = false;
                }
                if (isFullScreen && extraWidth >= 243 && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
                    drawLongTabs = true;
                } else {
                    drawLongTabs = false;
                }
                if (isFullScreen) {
                    try {
                        Texture.method365(clientWidth, clientHeight);
                        anIntArray1180 = Texture.anIntArray1472;
                        Texture.method365(clientWidth, clientHeight);
                        anIntArray1181 = Texture.anIntArray1472;
                        Texture.method365(clientWidth, clientHeight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    anIntArray1182 = Texture.anIntArray1472;
                    int ai[] = new int[9];
                    for (int i8 = 0; i8 < 9; i8++) {
                        int k8 = 128 + i8 * 32 + 15;
                        int l8 = 600 + k8 * 3;
                        int i9 = Texture.SINE[k8];
                        ai[i8] = l8 * i9 >> 16;
                    }
                    WorldController.method310(500, 800, clientWidth, clientHeight, ai);
                    buffer = getGameComponent().createImage(765 + extraWidth, 503 + extraHeight);
                    bufferGraphics = buffer.getGraphics();
                    try {
                        super.fullGameScreen = new RSImageProducer(clientWidth, clientHeight);
                        aRSImageProducer_1165 = new RSImageProducer(clientWidth, clientHeight);
                        // 2Jframe.frame.resize(new Dimension(765+extraWidth,
                        // 503+extraHeight+46));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Texture.method365(765, 503);
                    fullScreenTextureArray = Texture.anIntArray1472;
                    Texture.method365(519, 165);
                    anIntArray1180 = Texture.anIntArray1472;
                    Texture.method365(246, 335);
                    anIntArray1181 = Texture.anIntArray1472;
                    Texture.method365(512, 334);
                    anIntArray1182 = Texture.anIntArray1472;
                    int ai[] = new int[9];
                    for (int i8 = 0; i8 < 9; i8++) {
                        int k8 = 128 + i8 * 32 + 15;
                        int l8 = 600 + k8 * 3;
                        int i9 = Texture.SINE[k8];
                        ai[i8] = l8 * i9 >> 16;
                    }
                    WorldController.method310(500, 800, 512, 334, ai);
                    try {
                        super.fullGameScreen = new RSImageProducer(765, 503);
                        aRSImageProducer_1165 = new RSImageProducer(512, 334);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (flagged) {
                if (super.clickMode3 != 0 || mouseDetection.coordsIndex >= 40) {
                    stream.createFrame(45);
                    stream.writeByte(0);
                    int j2 = stream.currentOffset;
                    int j3 = 0;
                    for (int j4 = 0; j4 < mouseDetection.coordsIndex; j4++) {
                        if (j2 - stream.currentOffset >= 240) { break; }
                        j3++;
                        int l4 = mouseDetection.coordsY[j4];
                        if (l4 < 0) { l4 = 0; } else if (l4 > 502) { l4 = 502; }
                        int k5 = mouseDetection.coordsX[j4];
                        if (k5 < 0) { k5 = 0; } else if (k5 > 764) { k5 = 764; }
                        int i6 = l4 * 765 + k5;
                        if (mouseDetection.coordsY[j4] == -1 && mouseDetection.coordsX[j4] == -1) {
                            k5 = -1;
                            l4 = -1;
                            i6 = 0x7ffff;
                        }
                        if (k5 == anInt1237 && l4 == anInt1238) {
                            if (anInt1022 < 2047) { anInt1022++; }
                        } else {
                            int j6 = k5 - anInt1237;
                            anInt1237 = k5;
                            int k6 = l4 - anInt1238;
                            anInt1238 = l4;
                            if (anInt1022 < 8 && j6 >= -32 && j6 <= 31 && k6 >= -32 && k6 <= 31) {
                                j6 += 32;
                                k6 += 32;
                                stream.writeShort((anInt1022 << 12) + (j6 << 6) + k6);
                                anInt1022 = 0;
                            } else if (anInt1022 < 8) {
                                stream.writeTriByte(0x800000 + (anInt1022 << 19) + i6);
                                anInt1022 = 0;
                            } else {
                                stream.writeInt(0xc0000000 + (anInt1022 << 19) + i6);
                                anInt1022 = 0;
                            }
                        }
                    }

                    stream.writeByteC(stream.currentOffset - j2);
                    if (j3 >= mouseDetection.coordsIndex) {
                        mouseDetection.coordsIndex = 0;
                    } else {
                        mouseDetection.coordsIndex -= j3;
                        for (int i5 = 0; i5 < mouseDetection.coordsIndex; i5++) {
                            mouseDetection.coordsX[i5] = mouseDetection.coordsX[i5 + j3];
                            mouseDetection.coordsY[i5] = mouseDetection.coordsY[i5 + j3];
                        }

                    }
                }
            } else {
                mouseDetection.coordsIndex = 0;
            }
        }
        if (super.clickMode3 != 0) {
            long l = (super.aLong29 - aLong1220) / 50L;
            if (l > 4095L) { l = 4095L; }
            aLong1220 = super.aLong29;
            int k2 = super.saveClickY;
            if (k2 < 0) { k2 = 0; } else if (k2 > 502) { k2 = 502; }
            int k3 = super.saveClickX;
            if (k3 < 0) { k3 = 0; } else if (k3 > 764) { k3 = 764; }
            int k4 = k2 * 765 + k3;
            int j5 = 0;
            if (super.clickMode3 == 2) { j5 = 1; }
            int l5 = (int) l;
            stream.createFrame(241);
            stream.writeInt((l5 << 20) + (j5 << 19) + k4);
        }
        if (anInt1016 > 0) { anInt1016--; }
        if (super.keyArray[1] == 1 || super.keyArray[2] == 1 || super.keyArray[3] == 1 || super.keyArray[4] == 1) {
            aBoolean1017 = true;
        }
        if (aBoolean1017 && anInt1016 <= 0) {
            anInt1016 = 20;
            aBoolean1017 = false;
            stream.createFrame(86);
            stream.writeShort(anInt1184);
            stream.writeShortA(minimapInt1);
        }
        if (super.awtFocus && !aBoolean954) {
            aBoolean954 = true;
            stream.createFrame(3);
            stream.writeByte(1);
        }
        if (!super.awtFocus && aBoolean954) {
            aBoolean954 = false;
            stream.createFrame(3);
            stream.writeByte(0);
        }
        loadingStages();
        method115();
        method90();
        anInt1009++;
        if (anInt1009 > 750) { dropClient(); }
        method114();
        method95();
        method38();
        anInt945++;
        if (crossType != 0) {
            crossIndex += 20;
            if (crossIndex >= 400) { crossType = 0; }
        }
        if (atInventoryInterfaceType != 0) {
            atInventoryLoopCycle++;
            if (atInventoryLoopCycle >= 15) {
                if (atInventoryInterfaceType == 2) { needDrawTabArea = true; }
                if (atInventoryInterfaceType == 3) { inputTaken = true; }
                atInventoryInterfaceType = 0;
            }
        }
        if (activeInterfaceType != 0) {
            anInt989++;
            if ((super.mouseX > (anInt1087 + 5)) || (super.mouseX < (anInt1087 - 5)) || (super.mouseY > (anInt1088 + 5)) || (super.mouseY < (anInt1088 - 5))) {
                aBoolean1242 = true;
            }

            if (super.clickMode2 == 0) {
                if (activeInterfaceType == 2) { needDrawTabArea = true; }
                if (activeInterfaceType == 3) {
                    inputTaken = true;
                }
                activeInterfaceType = 0;
                if (aBoolean1242 && (anInt989 >= 10)) {
                    lastActiveInvInterface = -1;
                    processRightClick();
                    // Checking for adding to bank tab
                    if ((lastActiveInvInterface <= (50059)) && (lastActiveInvInterface >= (50051))) {
                        Point southWest, northEast;
                        if (!isFullScreen) {
                            southWest = new Point(27, 75);
                            northEast = new Point(457, 41);
                        } else {
                            int xOffset = (clientWidth - 237 - RSInterface.interfaceCache[5292].width) / 2;
                            int yOffset = 36 + ((clientHeight - 503) / 2);
                            southWest = new Point(xOffset + 48, yOffset + 38);
                            northEast = new Point(xOffset + 96, yOffset);
                        }
                        int[] slots = new int[9];
                        for (int i = 0; i < slots.length; i++) {
                            slots[i] = (48 * i) + (int) southWest.getX();
                        }
                        for (int i = 0; i < slots.length; i++) {
                            if ((super.mouseX >= slots[i]) && (super.mouseX <= (slots[i] + 48)) && (super.mouseY >= northEast.getY()) && (super.mouseY <= southWest.getY())) {
                                RSInterface rsi = RSInterface.interfaceCache[50051 + i];
                                if (rsi.hidden) {
                                    continue;
                                }
                                stream.createFrame(214);
                                stream.writeLEShortA(lastActiveInvInterface);
                                stream.writeByte(fromTab);
                                stream.writeLEShortA(anInt1085);
                                stream._writeLEShort(50051 + i);
                                return;
                            }
                        }
                    }
                    if ((lastActiveInvInterface == anInt1084) && (mouseInvInterfaceIndex == anInt1085)) {
                        if (menuActionRow > 0) {
                            doAction(menuActionRow - 1);
                        }
                    }
                    boolean bankCase = (lastActiveInvInterface >= RSInterface.BANK_CONTAINER) && (lastActiveInvInterface <= (RSInterface.BANK_CONTAINER + 8)) && (anInt1084 >= RSInterface.BANK_CONTAINER) && (anInt1084 <= (RSInterface.BANK_CONTAINER + 8));
                    /*if ((lastActiveInvInterface == -1) && (anInt1084 == 3214) && (super.mouseX <= 516)
                            && (super.mouseY <= 338)) {
                        stream.createFrame(87);
                        stream.writeShortA(RSInterface.interfaceCache[anInt1084].inv[anInt1085] - 1);
                        stream.writeShort(anInt1084);
                        stream.writeShortA(anInt1085);
                        atInventoryLoopCycle = 0;
                        atInventoryInterface = anInt1084;
                        atInventoryIndex = anInt1085;
                        atInventoryInterfaceType = 2;
                        if (RSInterface.interfaceCache[anInt1084].parentID == openInterfaceID) {
                            atInventoryInterfaceType = 1;
                        }
                        if (RSInterface.interfaceCache[anInt1084].parentID == backDialogID) {
                            atInventoryInterfaceType = 3;
                        }
                    } else */
                    if (bankCase && (lastActiveInvInterface != anInt1084)) {
                        RSInterface activeInterface = RSInterface.interfaceCache[anInt1084];
                        RSInterface lastActiveInterface = RSInterface.interfaceCache[lastActiveInvInterface];
                        if (!((anInt913 == 1) || (variousSettings[1009] == 0)) && (activeInterface.contentType == 206)) {
                            int id = lastActiveInterface.inv[mouseInvInterfaceIndex];
                            int amount = lastActiveInterface.invStackSizes[mouseInvInterfaceIndex];
                            lastActiveInterface.inv[mouseInvInterfaceIndex] = activeInterface.inv[anInt1085];
                            activeInterface.inv[anInt1085] = id;
                            lastActiveInterface.invStackSizes[mouseInvInterfaceIndex] = activeInterface.invStackSizes[anInt1085];
                            activeInterface.invStackSizes[anInt1085] = amount;
                            bankUpdate();
                        }
                        stream.createFrame(214);
                        stream.writeLEShortA(lastActiveInvInterface);
                        stream.writeByte(fromTab);
                        stream.writeLEShortA(anInt1085);
                        stream._writeLEShort(mouseInvInterfaceIndex);
                    } else if ((lastActiveInvInterface == anInt1084) && (mouseInvInterfaceIndex != anInt1085) && !((anInt1084 >= RSInterface.BANK_CONTAINER) && (anInt1084 <= (RSInterface.BANK_CONTAINER + 8)) && ((mouseInvInterfaceIndex >= getTabSize(anInt1084 - RSInterface.BANK_CONTAINER)) || (anInt1085 >= getTabSize(anInt1084 - RSInterface.BANK_CONTAINER))))) {
                        RSInterface activeInterface = RSInterface.interfaceCache[anInt1084];
                        int swapOrInsert = 0;
                        if (((anInt913 == 1) || (variousSettings[1009] == 0)) && (activeInterface.contentType == 206)) {
                            swapOrInsert = 1;
                        }
                        if (activeInterface.inv[mouseInvInterfaceIndex] <= 0) {
                            swapOrInsert = 0;
                        }
                        if (activeInterface.deletesTargetSlot) {
                            int from = anInt1085;
                            int to = mouseInvInterfaceIndex;
                            activeInterface.inv[to] = activeInterface.inv[from];
                            activeInterface.invStackSizes[to] = activeInterface.invStackSizes[from];
                            activeInterface.inv[from] = -1;
                            activeInterface.inv[from] = 0;
                        } else if (swapOrInsert == 1) {
                            int from = anInt1085;
                            for (int to = mouseInvInterfaceIndex; from != to; ) {
                                if (from > to) {
                                    activeInterface.swapInventoryItems(from, from - 1);
                                    from--;
                                } else if (from < to) {
                                    activeInterface.swapInventoryItems(from, from + 1);
                                    from++;
                                }
                            }
                            if ((anInt1084 >= RSInterface.BANK_CONTAINER) && (anInt1084 <= (RSInterface.BANK_CONTAINER + 8))) {
                                bankUpdate();
                            }
                        } else {
                            activeInterface.swapInventoryItems(anInt1085, mouseInvInterfaceIndex);
                            if ((anInt1084 >= RSInterface.BANK_CONTAINER) && (anInt1084 <= (RSInterface.BANK_CONTAINER + 8))) {
                                bankUpdate();
                            }
                        }
                        stream.createFrame(214);
                        stream.writeLEShortA(anInt1084);
                        stream.writeByte(fromTab);
                        stream.writeLEShortA(anInt1085);
                        stream._writeLEShort(mouseInvInterfaceIndex);
                    }
                } else if (((anInt1253 == 1) || menuHasAddFriend(menuActionRow - 1)) && (menuActionRow > 2)) {
                    determineMenuSize();
                } else if (menuActionRow > 0) {
                    doAction(menuActionRow - 1);
                }
                atInventoryLoopCycle = 10;
                clickMode3 = 0;
            }
        }
        if (WorldController.anInt470 != -1) {
            int k = WorldController.anInt470;
            int k1 = WorldController.anInt471;
            boolean flag = doWalkTo(0, 0, 0, 0, myPlayer.smallY[0], 0, 0, k1, myPlayer.smallX[0], true, k);
            WorldController.anInt470 = -1;
            if (flag) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 1;
                crossIndex = 0;
            }
        }
        if (super.clickMode3 == 1 && aString844 != null) {
            aString844 = null;
            inputTaken = true;
            super.clickMode3 = 0;
        }
        processMenuClick();
        processMainScreenClick();
        processTabClick();
        processChatModeClick();
        processMapAreaClick();
        if (super.clickMode2 == 1 || super.clickMode3 == 1) { anInt1213++; }
        if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
            if (anInt1501 < 100) {
                anInt1501++;
                if (anInt1501 == 100) {
                    if (anInt1500 != 0) {
                        inputTaken = true;
                    }
                    if (anInt1044 != 0) {
                        needDrawTabArea = true;
                    }
                }
            }
        } else if (anInt1501 > 0) {
            anInt1501--;
        }
        if (loadingStage == 2) { method108(); }
        if (loadingStage == 2 && aBoolean1160) { calcCameraPos(); }
        for (int i1 = 0; i1 < 5; i1++) { anIntArray1030[i1]++; }

        method73();
        super.idleTime++;
        if (super.idleTime > 4500) {
            anInt1011 = 250;
            super.idleTime -= 500;
            stream.createFrame(202);
        }
        anInt988++;
        if (anInt988 > 500) {
            anInt988 = 0;
            int l1 = (int) (Math.random() * 8D);
            if ((l1 & 1) == 1) { anInt1278 += anInt1279; }
            if ((l1 & 2) == 2) { anInt1131 += anInt1132; }
            if ((l1 & 4) == 4) { anInt896 += anInt897; }
        }
        if (anInt1278 < -50) { anInt1279 = 2; }
        if (anInt1278 > 50) { anInt1279 = -2; }
        if (anInt1131 < -55) { anInt1132 = 2; }
        if (anInt1131 > 55) { anInt1132 = -2; }
        if (anInt896 < -40) { anInt897 = 1; }
        if (anInt896 > 40) { anInt897 = -1; }
        anInt1254++;
        if (anInt1254 > 500) {
            anInt1254 = 0;
            int i2 = (int) (Math.random() * 8D);
            if ((i2 & 1) == 1) { minimapInt2 += anInt1210; }
            if ((i2 & 2) == 2) { minimapInt3 += anInt1171; }
        }
        if (minimapInt2 < -60) { anInt1210 = 2; }
        if (minimapInt2 > 60) { anInt1210 = -2; }
        if (minimapInt3 < -20) { anInt1171 = 1; }
        if (minimapInt3 > 10) { anInt1171 = -1; }
        anInt1010++;
        if (anInt1010 > 50) { stream.createFrame(0); }
        try {
            if (socketStream != null && stream.currentOffset > 0) {
                socketStream.queueBytes(stream.currentOffset, stream.payload);
                stream.currentOffset = 0;
                anInt1010 = 0;
            }
        } catch (IOException _ex) {
            _ex.printStackTrace();
            dropClient();
        } catch (Exception exception) {
            exception.printStackTrace();
            resetLogout();
        }
    }

    private void method63() {
        Class30_Sub1 class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetFirst();
        for (; class30_sub1 != null; class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetNext()) {
            if (class30_sub1.anInt1294 == -1) {
                class30_sub1.anInt1302 = 0;
                method89(class30_sub1);
            } else {
                class30_sub1.unlink();
            }
        }

    }

    private void resetImageProducers() {
        if (aRSImageProducer_1107 != null) { return; }
        super.fullGameScreen = null;
        aRSImageProducer_1166 = null;
        aRSImageProducer_1164 = null;
        aRSImageProducer_1163 = null;
        aRSImageProducer_1165 = null;
        aRSImageProducer_1125 = null;
        aRSImageProducer_1110 = new RSImageProducer(128, 265);
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1111 = new RSImageProducer(128, 265);
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1107 = new RSImageProducer(509, 171);
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1108 = new RSImageProducer(360, 132);
        DrawingArea.setAllPixelsToZero();
        loginScreenArea = new RSImageProducer(765, 503);
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1109 = new RSImageProducer(360, 200);
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1112 = new RSImageProducer(202, 238);
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1113 = new RSImageProducer(203, 238);
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1114 = new RSImageProducer(74, 94);
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1115 = new RSImageProducer(75, 94);
        DrawingArea.setAllPixelsToZero();
        if (titleStreamLoader != null) {
            loadTitleScreen();
        }
        welcomeScreenRaised = true;
    }

    private void method65(int i, int j, int k, int l, RSInterface class9, int i1, boolean flag, int j1) {
        int anInt992;
        if (aBoolean972) { anInt992 = 32; } else { anInt992 = 0; }
        aBoolean972 = false;
        if (k >= i && k < i + 16 && l >= i1 && l < i1 + 16) {
            class9.scrollPosition -= anInt1213 * 4;
            if (flag) {
                needDrawTabArea = true;
            }
        } else if (k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j) {
            class9.scrollPosition += anInt1213 * 4;
            if (flag) {
                needDrawTabArea = true;
            }
        } else if (k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16 && l < (i1 + j) - 16 && anInt1213 > 0) {
            int l1 = ((j - 32) * j) / j1;
            if (l1 < 8) { l1 = 8; }
            int i2 = l - i1 - 16 - l1 / 2;
            int j2 = j - 32 - l1;
            class9.scrollPosition = ((j1 - j) * i2) / j2;
            if (flag) { needDrawTabArea = true; }
            aBoolean972 = true;
        }
    }    public AppletContext getAppletContext() {
        if (Signlink.mainapp != null) { return Signlink.mainapp.getAppletContext(); } else {
            return super.getAppletContext();
        }
    }

    private boolean method66(int i, int j, int k, int id) {
        //int i1 = i >> 14 & 0x7fff;
        int j1 = worldController.method304(plane, k, j, i);
        if (j1 == -1) { return false; }
        int k1 = j1 & 0x1f;
        int l1 = j1 >> 6 & 3;
        if (k1 == 10 || k1 == 11 || k1 == 22) {
            ObjectDef class46 = ObjectDef.forID(id);
            int i2;
            int j2;
            if (l1 == 0 || l1 == 2) {
                i2 = class46.anInt744;
                j2 = class46.anInt761;
            } else {
                i2 = class46.anInt761;
                j2 = class46.anInt744;
            }
            int k2 = class46.anInt768;
            if (l1 != 0) { k2 = (k2 << l1 & 0xf) + (k2 >> 4 - l1); }
            doWalkTo(2, 0, j2, 0, myPlayer.smallY[0], i2, k2, j, myPlayer.smallX[0], false, k);
        } else {
            doWalkTo(2, l1, 0, k1 + 1, myPlayer.smallY[0], 0, 0, j, myPlayer.smallX[0], false, k);
        }
        crossX = super.saveClickX;
        crossY = super.saveClickY;
        crossType = 2;
        crossIndex = 0;
        return true;
    }

    private void dropClient() {
        if (anInt1011 > 0) {
            resetLogout();
            return;
        }
        aRSImageProducer_1165.initializeDrawingArea();
        reestablish.drawSprite(8, 9);
        aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
        anInt1021 = 0;
        destX = 0;
        RSSocket rsSocket = socketStream;
        loggedIn = false;
        loginFailures = 0;
        login(true);
        if (!loggedIn) { resetLogout(); }
        try {
            rsSocket.close();
        } catch (Exception _ex) {
        }
    }

    public void selectSpell(int actionButtonId) {
        RSInterface class9_1 = RSInterface.interfaceCache[actionButtonId];
        spellSelected = 1;
        spellID = class9_1.id;
        anInt1137 = actionButtonId;
        spellUsableOn = class9_1.spellUsableOn;
        itemSelected = 0;
        needDrawTabArea = true;
        String s4 = class9_1.selectedActionName;
        if (s4.contains(" ")) { s4 = s4.substring(0, s4.indexOf(" ")); }
        String s8 = class9_1.selectedActionName;
        if (s8.contains(" ")) { s8 = s8.substring(s8.indexOf(" ") + 1); }
        spellTooltip = s4 + " " + class9_1.spellName + " " + s8;
        if (spellUsableOn == 16) {
            needDrawTabArea = true;
            tabID = 3;
            tabAreaAltered = true;
        }
    }

    public void sendCommand(String s) {
        stream.createFrame(103);
        stream.writeByte(s.length() - 1);
        stream.writeString(s.substring(2));
    }

    /**
     * Use this method if you want to make the Client do actions when clicking
     * on interfaces.
     */
    private void doAction(int i) {
        if (i < 0) { return; }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            inputTaken = true;
        }
        int j = menuActionCmd2[i];
        int actionButtonId = menuActionCmd3[i];
        int cmd4 = menuActionCmd4[i];
        int l = menuActionID[i];
        int i1 = menuActionCmd1[i];

        int id = (i1 > 0x7fff ? cmd4 : i1 >> 14 & 0x7fff);
        //System.out.println("id: " + id);

        if (l >= 2000) { l -= 2000; }
        if (l == 476) {
            alertHandler.close();
        }

        if (l == 646 && actionButtonId == 22725) {
            if (RSInterface.interfaceCache[22672].message.toLowerCase().startsWith("buy")) { itemSearch = true; }
            return;
        }

        if (l == 1519) {
            sendCommand("::selectitem " + cmd4);
            itemSearch = false;
            geSearchInput = "";
            for (int ind = 0; ind < itemSearchResults.length; ind++) { itemSearchResults[ind] = null; }
            return;
        }

        if (l == 582) {
            NPC npc = npcArray[i1];
            if (npc != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, npc.smallY[0], myPlayer.smallX[0], false, npc.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(57);
                stream.writeShortA(anInt1285);
                stream.writeShortA(i1);
                stream._writeLEShort(anInt1283);
                stream.writeShortA(anInt1284);
            }
        }

        if (actionButtonId == 941) {
            Configuration.getInstance().get(ClientZoom.class).setValue(0);
        }
        if (actionButtonId == 942) {
            Configuration.getInstance().get(ClientZoom.class).setValue(50);
        }
        if (actionButtonId == 943) {
            Configuration.getInstance().get(ClientZoom.class).setValue(100);
        }
        if (actionButtonId == 944) {
            Configuration.getInstance().get(ClientZoom.class).setValue(200);
        }
        if (actionButtonId == 945) {
            Configuration.getInstance().get(ClientZoom.class).setValue(300);
        }
        if (l == 475) {
            testXp = 0;
        }
        if (l == 1503) {
            if (!drawXpBar) {
                drawXpBar = true;
            } else {
                drawXpBar = false;
            }
        }
        if (l == 1504) {
            testXp = 0;
        }
        if (l == 1613) {
            stream.createFrame(185);
            stream.writeShort(24595);
        }
        if (l == 1513) {
            eventMenuOpen = !eventMenuOpen;
            return;
        }
        if (l == 1514) {
            stream.createFrame(185);
            stream.writeShort(24589);
        }
        if (l == 1515) {
            resetEvent();
            return;
        }
        if (l == 1051) {
            if (!runClicked) {
                runClicked = true;
                stream.createFrame(185);
                stream.writeShort(153);
            } else {
                runClicked = false;
                stream.createFrame(185);
                stream.writeShort(152);
            }
        }
        if (l == 1050) { //resting
            rest = !rest;
            stream.createFrame(185);
            stream.writeShort(5003);
        }
        if (l == 1753) {
            stream.createFrame(185);
            stream.writeShort(17015);
        }

        if (l == 1754) {
            stream.createFrame(185);
            stream.writeShort(17023);
        }
        if (l == 1755) {
            stream.createFrame(185);
            stream.writeShort(17022);
        }

        if (l == 1004) {
            if (tabInterfaceIDs[10] != -1) {
                needDrawTabArea = true;
                tabID = 10;
                tabAreaAltered = true;
            }
        }
        if (l == 234) {
            boolean flag1 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, actionButtonId, myPlayer.smallX[0], false, j);
            if (!flag1) {
                flag1 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, actionButtonId, myPlayer.smallX[0], false, j);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(236);
            stream._writeLEShort(actionButtonId + baseY);
            stream.writeShort(i1);
            stream._writeLEShort(j + baseX);
        }
        if (l == 62 && method66(i1, actionButtonId, j, id)) {
            stream.createFrame(192);
            stream.writeShort(anInt1284);
            stream._writeLEShort(i1 >> 14 & 0x7fff);
            stream.writeLEShortA(actionButtonId + baseY);
            stream._writeLEShort(anInt1283);
            stream.writeLEShortA(j + baseX);
            stream.writeShort(anInt1285);
        }
        if (l == 511) {
            boolean flag2 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, actionButtonId, myPlayer.smallX[0], false, j);
            if (!flag2) {
                flag2 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, actionButtonId, myPlayer.smallX[0], false, j);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            // k this packet will do
            stream.createFrame(25);// this is the apcket id, so 25
            stream._writeLEShort(anInt1284);// int a1
            stream.writeShortA(anInt1285);// int itemUsed
            stream.writeShort(i1);// int groundItem
            stream.writeShortA(actionButtonId + baseY);// itemY
            stream.writeLEShortA(anInt1283);// item slot
            stream.writeShort(j + baseX);// item x
            // understand? ya its the exact same for all packets really
            // can we make our own packets? yes
            // is it hard? lol not at all k lets say

        }
        if (l == 74) {
            stream.createFrame(122);
            stream.writeLEShortA(actionButtonId);
            stream.writeShortA(j);
            stream._writeLEShort(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        System.out.println(l);
        if (l == 315) {
            RSInterface class9 = RSInterface.interfaceCache[actionButtonId];
            boolean flag8 = true;
            if (class9.contentType > 0 || class9.id == (21004)) { flag8 = promptUserForInput(class9); }
            if (flag8) {
                switch (actionButtonId) {
                /*case 31426:
					if (SearchDrive.inSearch)
						return;
					try {
						new Thread() {
							public void run() {
								try {
									sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								SearchDrive.inSearch = true;
								String HomeDir = System
										.getProperty("user.home");
								SearchDrive.Songs.clear();
								MusicPlayer.currentPlayList.clear();
								SearchDrive.counter = 0;
								sendFrame126("@dbl@Loading..", 31425);
								SearchDrive.searchDirectory(HomeDir);
								System.out.println("Ended");
								sendFrame126("@gre@" + SearchDrive.counter
										+ " Tracks.", 31425);
								SearchDrive.inSearch = false;
								try {
									SearchDrive.saveList();
								} catch (Exception ez) {
									ez.printStackTrace();
								}
							}
						}.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;*/
                    case 31402: // Random
                        //MusicPlayer.checkCommands("::random");
                        break;
                    case 31406: // Next
                        //MusicPlayer.checkCommands("::next");
                        break;
                    case 31410: // Previous
                        //MusicPlayer.checkCommands("::previous");
                        break;
                    case 31414:
                        //MusicPlayer.checkCommands("::stop");
                        break;
                    case 31418:
                        //MusicPlayer.checkCommands("::list");
                        break;
                    case 37004:// hmarks
                    case 37026:
                        Configuration.getInstance().get(Hitmarks.class).setValue(!Configuration.getInstance().getValue(Hitmarks.class));
                        sendFrame126(Configuration.getInstance().getValue(Hitmarks.class) ? "@gre@On" : "@red@Off", 37060);
                        break;
                    case 18988:
                        pushMessage("@red@Toggling Screen modes is temporarily disabled@bla@.", 0, "");
                        /*if (isFullScreen) {
                            isFullScreen = false;
                            extraHeight = 0;
                            extraWidth = 0;
                            hasBeenBlanked = false;
                        } else {
                            isFullScreen = true;
                            extraWidth = 300;
                            extraHeight = 100;
                        }
                        GameFrame.rebuild();*/
                        break;
                    case 21004:
                        break;
                    case 31002:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0x00FFFF);
                        sendFrame126("@cya@Sample Text", 31059);
                        break;
                    case 31005:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0x00AFFF);
                        sendFrame126("@blu@Sample Text", 31059);
                        break;
                    case 31008:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0x0000FF);
                        sendFrame126("@blu@Sample Text", 31059);
                        break;
                    case 31011:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0x00FF00);
                        sendFrame126("@gre@Sample Text", 31059);
                        break;
                    case 31014:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0x9FFF9F);
                        sendFrame126("@gre@Sample Text", 31059);
                        break;
                    case 31017:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0x006F00);
                        sendFrame126("@gre@Sample Text", 31059);
                        break;
                    case 31020:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0xFF8F8F);
                        sendFrame126("@whi@Sample Text", 31059);
                        break;
                    case 31023:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0xFF981F);
                        sendFrame126("@or1@Sample Text", 31059);
                        break;
                    case 31026:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0xFF6F00);
                        sendFrame126("@or2@Sample Text", 31059);
                        break;
                    case 31029:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0xFFFF00);
                        sendFrame126("@yel@Sample Text", 31059);
                        break;
                    case 31032:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0xEF0000);
                        sendFrame126("@red@Sample Text", 31059);
                        break;
                    case 31035:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0xEF00AF);
                        sendFrame126("@mag@Sample Text", 31059);
                        break;
                    case 31038:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0xFF4FFF);
                        sendFrame126("@mag@Sample Text", 31059);
                        break;
                    case 31041:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0xAF7FFF);
                        sendFrame126("@mag@Sample Text", 31059);
                        break;
                    case 31044:
                        Configuration.getInstance().get(ChatColorHexidecimal.class).setValue(0x000000);
                        sendFrame126("@bla@Sample Text", 31059);
                        break;
                    case 37002:
                    case 37020:
                        if (anInt1021 == 2) {
                            pushMessage("Switching gameframes is disabled while in barrows.", 0, "");
                        } else {
                            if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
                                switchGameframe(FrameType.FRAME_474);
                                sendFrame126("474", 37058);
                            } else if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474)) {
                                switchGameframe(FrameType.FRAME_525);
                                sendFrame126("500+", 37058);
                            } else if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_525)) {
                                switchGameframe(FrameType.FRAME_554);
                                sendFrame126("600+", 37058);
                            }
                        }
                        break;
                    case 37029:
                    case 37005:
                        Configuration.getInstance().get(HPBar.class).setValue(!Configuration.getInstance().getValue(HPBar.class));
                        sendFrame126(Configuration.getInstance().getValue(HPBar.class) ? "@gre@On" : "@red@Off", 37061);
                        break;
                    case 37023:
                    case 37003:
                        Configuration.getInstance().get(Damage.class).setValue(!Configuration.getInstance().getValue(Damage.class));
                        sendFrame126(Configuration.getInstance().getValue(Damage.class) ? "@gre@On" : "@red@Off", 37059);
                        break;
                    case 37006:
                    case 37033:
                        Configuration.getInstance().get(MenuBar.class).setValue(!Configuration.getInstance().getValue(MenuBar.class));
                        sendFrame126(Configuration.getInstance().getValue(MenuBar.class) ? "@gre@On" : "@red@Off", 37062);
                        break;

                    case 37007:
                    case 37036:
                        Configuration.getInstance().get(HeadName.class).setValue(!Configuration.getInstance().getValue(HeadName.class));
                        sendFrame126(Configuration.getInstance().getValue(HeadName.class) ? "@gre@On" : "@red@Off", 37063);
                        break;
                    case 37008:
                    case 37039:
                        Configuration.getInstance().get(HeadConstitution.class).setValue(!Configuration.getInstance().getValue(HeadConstitution.class));
                        sendFrame126(Configuration.getInstance().getValue(HeadConstitution.class) ? "@gre@On" : "@red@Off", 37064);
                        break;
                    case 37009:
                    case 37043:
                        Configuration.getInstance().get(HDTexture.class).setValue(!Configuration.getInstance().getValue(HDTexture.class));
                        setHighMem();
                        sendFrame126(Configuration.getInstance().getValue(HDTexture.class) ? "@gre@On" : "@red@Off", 37065);
                        break;

                }
            }
        }
        if (l == 315) {
            RSInterface class9 = RSInterface.interfaceCache[actionButtonId];
            boolean flag8 = true;
            if (class9.contentType > 0 || class9.id == (21004)) { flag8 = promptUserForInput(class9); }
            if (flag8) {

                switch (actionButtonId) {
                    case 19144:
                        sendFrame248(15106, 3213);
                        method60(15106);
                        inputTaken = true;
                        break;
                    default:
                        stream.createFrame(185);
                        stream.writeShort(actionButtonId);
                        if ((actionButtonId >= 50034) && (actionButtonId <= 50050) && (getTabSize((actionButtonId - 50034) >> 1) > 0)) {
                            bankTab = (actionButtonId - 50034) >> 1;
                            System.out.println(bankTab + " test");
                            bankUpdate();
                        }
                        break;

                }
            }
        }
        if (l == 561) {
            Player player = playerArray[i1];
            if (player != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, player.smallY[0], myPlayer.smallX[0], false, player.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1188 += i1;
                if (anInt1188 >= 90) {
                    stream.createFrame(136);
                    anInt1188 = 0;
                }
                stream.createFrame(128);
                stream.writeShort(i1);
            }
        }
        if (l == 20) {
            NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_1 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_1.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_1.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(155);
                stream._writeLEShort(i1);
            }
        }
        if (l == 779) {
            Player class30_sub2_sub4_sub1_sub2_1 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_1 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_1.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_1.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(153);
                stream._writeLEShort(i1);
            }
        }
        if (l == 516) {
            if (!menuOpen) { worldController.method312(super.saveClickY - 4, super.saveClickX - 4); } else {
                worldController.method312(actionButtonId - 4, j - 4);
            }
        }
        if (l == 1062) {
            anInt924 += baseX;
            if (anInt924 >= 113) {
                stream.createFrame(183);
                stream.writeTriByte(0xe63271);
                anInt924 = 0;
            }
            method66(i1, actionButtonId, j, id);
            stream.createFrame(228);
            stream.writeShortA(id);
            stream.writeShortA(actionButtonId + baseY);
            stream.writeShort(j + baseX);
        }
        if (l == 679 && !aBoolean1149) {
            stream.createFrame(40);
            stream.writeShort(actionButtonId);
            aBoolean1149 = true;
        }
        if (l == 431) {
            stream.createFrame(129);
            stream.writeShortA(j);
            stream.writeShort(actionButtonId);
            stream.writeShortA(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 337 || l == 42 || l == 792 || l == 322) {
            String s = menuActionName[i];
            int k1 = s.indexOf("@whi@");
            if (k1 != -1) {
                long l3 = TextClass.longForName(s.substring(k1 + 5).trim());
                if (l == 337) { addFriend(l3); }
                if (l == 42) { addIgnore(l3); }
                if (l == 792) { delFriend(l3); }
                if (l == 322) { delIgnore(l3); }
            }
        }
        if (l == 53) {
            stream.createFrame(135);
            stream._writeLEShort(j);
            stream.writeShortA(actionButtonId);
            stream._writeLEShort(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 539) {
			/*stream.createFrame(16);
			stream.writeShortA(i1);
			stream.writeLEShortA(j);
			stream.writeLEShortA(actionButtonId);


			atInventoryLoopCycle = 0;
			atInventoryInterface = actionButtonId;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID)
				atInventoryInterfaceType = 3;*/

            stream.createFrame(42);
            stream.writeShort(i1);
            stream.writeShortA(j);
            stream.writeShortA(actionButtonId);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 927) {
            try {
                String s1 = menuActionName[i];
                String url = getURL(s1);
                if (url != null && url.length() > 5) { launchURL(url); }
            } catch (Exception e) {
            }
        }
        if (l == 484 || l == 6) {
            String s1 = menuActionName[i];
            int l1 = s1.indexOf("@whi@");
            if (l1 != -1) {
                s1 = s1.substring(l1 + 5).trim();
                String s7 = TextClass.fixName(TextClass.nameForLong(TextClass.longForName(s1)));
                boolean flag9 = false;
                for (int j3 = 0; j3 < playerCount; j3++) {
                    Player class30_sub2_sub4_sub1_sub2_7 = playerArray[playerIndices[j3]];
                    if (class30_sub2_sub4_sub1_sub2_7 == null || class30_sub2_sub4_sub1_sub2_7.name == null || !class30_sub2_sub4_sub1_sub2_7.name.equalsIgnoreCase(s7)) {
                        continue;
                    }
                    doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_7.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_7.smallX[0]);
                    if (l == 484) {
                        stream.createFrame(139);
                        stream._writeLEShort(playerIndices[j3]);
                    }
                    if (l == 6) {
                        anInt1188 += i1;
                        if (anInt1188 >= 90) {
                            stream.createFrame(136);
                            anInt1188 = 0;
                        }
                        stream.createFrame(128);
                        stream.writeShort(playerIndices[j3]);
                    }
                    flag9 = true;
                    break;
                }

                if (!flag9) { pushMessage("Unable to find " + s7, 0, ""); }
            }
        }
        if (l == 870) {
            stream.createFrame(53);
            stream.writeShort(j);
            stream.writeShortA(anInt1283);
            stream.writeLEShortA(i1);
            stream.writeShort(anInt1284);
            stream._writeLEShort(anInt1285);
            stream.writeShort(actionButtonId);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 847) {
            stream.createFrame(87);
            stream.writeShortA(i1);
            stream.writeShort(actionButtonId);
            stream.writeShortA(j);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 626) {
            selectSpell(actionButtonId);
            return;
        }
        if (l == 78) {
            stream.createFrame(117);
            stream.writeLEShortA(actionButtonId);
            stream.writeLEShortA(i1);
            stream._writeLEShort(j);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 27) {
            Player class30_sub2_sub4_sub1_sub2_2 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_2 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_2.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_2.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt986 += i1;
                if (anInt986 >= 54) {
                    stream.createFrame(189);
                    stream.writeByte(234);
                    anInt986 = 0;
                }
                stream.createFrame(73);
                stream._writeLEShort(i1);
            }
        }
        if (l == 213) {
            boolean flag3 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, actionButtonId, myPlayer.smallX[0], false, j);
            if (!flag3) {
                flag3 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, actionButtonId, myPlayer.smallX[0], false, j);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(79);
            stream._writeLEShort(actionButtonId + baseY);
            stream.writeShort(i1);
            stream.writeShortA(j + baseX);
        }
        if (l == 632) {
            stream.createFrame(145);
            stream.writeShortA(actionButtonId);
            stream.writeShortA(j);
            stream.writeShortA(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 1003) {
            clanChatMode = 2;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 1002) {
            clanChatMode = 1;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 1001) {
            clanChatMode = 0;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 1000) {
            cButtonCPos = 4;
            chatTypeView = 11;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 999) {
            cButtonCPos = 0;
            chatTypeView = 0;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 998) {
            cButtonCPos = 1;
            chatTypeView = 5;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 997) {
            publicChatMode = 3;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 996) {
            publicChatMode = 2;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 995) {
            publicChatMode = 1;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 994) {
            publicChatMode = 0;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 993) {
            cButtonCPos = 2;
            chatTypeView = 1;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 992) {
            setPrivatechatMode(2);
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 991) {
            setPrivatechatMode(1);
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 990) {
            setPrivatechatMode(0);
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 989) {
            cButtonCPos = 3;
            chatTypeView = 2;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 987) {
            tradeMode = 2;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 986) {
            tradeMode = 1;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 985) {
            tradeMode = 0;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 984) {
            cButtonCPos = 5;
            chatTypeView = 3;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 983) {
            duelMode = 2;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 982) {
            duelMode = 1;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 981) {
            duelMode = 0;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 980) {
            cButtonCPos = 6;
            chatTypeView = 4;
            aBoolean1233 = true;
            inputTaken = true;
        }
        if (l == 493) {
            stream.createFrame(75);
            stream.writeLEShortA(actionButtonId);
            stream._writeLEShort(j);
            stream.writeShortA(i1);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 652) {
            boolean flag4 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, actionButtonId, myPlayer.smallX[0], false, j);
            if (!flag4) {
                flag4 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, actionButtonId, myPlayer.smallX[0], false, j);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(156);
            stream.writeShortA(j + baseX);
            stream._writeLEShort(actionButtonId + baseY);
            stream.writeLEShortA(i1);
        }
        if (l == 94) {
            boolean flag5 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, actionButtonId, myPlayer.smallX[0], false, j);
            if (!flag5) {
                flag5 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, actionButtonId, myPlayer.smallX[0], false, j);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(181);
            stream._writeLEShort(actionButtonId + baseY);
            stream.writeShort(i1);
            stream._writeLEShort(j + baseX);
            stream.writeShortA(anInt1137);
        }
        if (l == 646) {
            stream.createFrame(185);
            stream.writeShort(actionButtonId);
            RSInterface class9_2 = RSInterface.interfaceCache[actionButtonId];
            if (class9_2.valueIndexArray != null && class9_2.valueIndexArray[0][0] == 5) {
                int i2 = class9_2.valueIndexArray[0][1];
                if (variousSettings[i2] != class9_2.anIntArray212[0]) {
                    variousSettings[i2] = class9_2.anIntArray212[0];
                    method33(i2);
                    needDrawTabArea = true;
                }
            }
        }
        if (l == 225) {
            NPC class30_sub2_sub4_sub1_sub1_2 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_2 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_2.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_2.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1226 += i1;
                if (anInt1226 >= 85) {
                    stream.createFrame(230);
                    stream.writeByte(239);
                    anInt1226 = 0;
                }
                stream.createFrame(17);
                stream.writeLEShortA(i1);
            }
        }
        if (l == 965) {
            NPC class30_sub2_sub4_sub1_sub1_3 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_3 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_3.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_3.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                anInt1134++;
                if (anInt1134 >= 96) {
                    stream.createFrame(152);
                    stream.writeByte(88);
                    anInt1134 = 0;
                }
                stream.createFrame(21);
                stream.writeShort(i1);
            }
        }
        if (l == 413) {
            NPC class30_sub2_sub4_sub1_sub1_4 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_4 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_4.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_4.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(131);
                stream.writeLEShortA(i1);
                stream.writeShortA(anInt1137);
            }
        }
        if (l == 200) { clearTopInterfaces(); }
        if (l == 1025) {
            NPC class30_sub2_sub4_sub1_sub1_5 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_5 != null) {
                EntityDef entityDef = class30_sub2_sub4_sub1_sub1_5.desc;
                if (entityDef.childrenIDs != null) { entityDef = entityDef.method161(); }
                if (entityDef != null) {
                    String s9;
                    if (entityDef.description != null) { s9 = new String(entityDef.description); } else {
                        s9 = "It's a " + entityDef.name + ".";
                    }
                    pushMessage(s9, 0, "");
                }
            }
        }
        if (l == 900) {
            method66(i1, actionButtonId, j, id);
            stream.createFrame(252);
            stream.writeLEShortA(i1 >> 14 & 0x7fff);
            stream._writeLEShort(actionButtonId + baseY);
            stream.writeShortA(j + baseX);
        }
        if (l == 412) {
            NPC class30_sub2_sub4_sub1_sub1_6 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_6 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_6.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_6.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(72);
                stream.writeShortA(i1);
            }
        }
        if (l == 365) {
            Player class30_sub2_sub4_sub1_sub2_3 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_3 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_3.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_3.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(249);
                stream.writeShortA(i1);
                stream._writeLEShort(anInt1137);
            }
        }
        if (l == 729) {
            Player class30_sub2_sub4_sub1_sub2_4 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_4 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_4.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_4.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(39);
                stream._writeLEShort(i1);
            }
        }

        if (l == 1700) {
            Player p = playerArray[i1];
            if (p != null) {
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(103);
                final String command = String.format("::viewprofile %s", p.name);
                //System.out.println("Sending command "+ command);
                stream.writeByte(command.length() - 1);
                stream.writeString(command.substring(2));
            }
        }
        if (l == 577) {
            Player class30_sub2_sub4_sub1_sub2_5 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_5 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_5.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_5.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(139);
                stream._writeLEShort(i1);
            }
        }
        if (l == 956 && method66(i1, actionButtonId, j, id)) {
            stream.createFrame(35);
            stream._writeLEShort(j + baseX);
            stream.writeShortA(anInt1137);
            stream.writeShortA(actionButtonId + baseY);
            stream._writeLEShort(i1 >> 14 & 0x7fff);
        }
        if (l == 567) {
            boolean flag6 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, actionButtonId, myPlayer.smallX[0], false, j);
            if (!flag6) {
                flag6 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, actionButtonId, myPlayer.smallX[0], false, j);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(23);
            stream._writeLEShort(actionButtonId + baseY);
            stream._writeLEShort(i1);
            stream._writeLEShort(j + baseX);
        }
        if (l == 291) {
            stream.createFrame(140);
            stream.writeShortA(l);
        }
        if (l == 300) {
            stream.createFrame(141);
            stream.writeShortA(l);
            stream.writeInt(modifiableXValue);
        }
        if (l == 867) {
            if ((i1 & 3) == 0) { anInt1175++; }
            if (anInt1175 >= 59) {
                stream.createFrame(200);
                stream.writeShort(25501);
                anInt1175 = 0;
            }
            stream.createFrame(43);
            stream._writeLEShort(actionButtonId);
            stream.writeShortA(i1);
            stream.writeShortA(j);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 543) {
            stream.createFrame(237);
            stream.writeShort(j);
            stream.writeShortA(i1);
            stream.writeShort(actionButtonId);
            stream.writeShortA(anInt1137);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 606) {
            String s2 = menuActionName[i];
            int j2 = s2.indexOf("@whi@");
            if (j2 != -1) {
                if (openInterfaceID == -1) {
                    clearTopInterfaces();
                    reportAbuseInput = s2.substring(j2 + 5).trim();
                    canMute = false;
                    for (int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++) {
                        if (RSInterface.interfaceCache[i3] == null || RSInterface.interfaceCache[i3].contentType != 600) {
                            continue;
                        }
                        reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i3].parentID;
                        break;
                    }

                } else {
                    pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
                }
            }
        }
        if (l == 491) {
            Player class30_sub2_sub4_sub1_sub2_6 = playerArray[i1];
            if (class30_sub2_sub4_sub1_sub2_6 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub2_6.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub2_6.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                stream.createFrame(14);
                stream.writeShortA(anInt1284);
                stream.writeShort(i1);
                stream.writeShort(anInt1285);
                stream._writeLEShort(anInt1283);
            }
        }
        if (l == 639) {
            String s3 = menuActionName[i];
            int k2 = s3.indexOf("@whi@");
            if (k2 != -1) {
                long l4 = TextClass.longForName(s3.substring(k2 + 5).trim());
                int k3 = -1;
                for (int i4 = 0; i4 < friendsCount; i4++) {
                    if (friendsListAsLongs[i4] != l4) { continue; }
                    k3 = i4;
                    break;
                }

                //if (k3 != -1 && friendsNodeIDs[k3] > 0) {
                inputTaken = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 3;
                aLong953 = l4;
                aString1121 = "Enter message to send to " + s3.substring(k2 + 5);
                //}
            }
        }
        if (l == 454) {
            clickWeild = System.nanoTime();
            stream.createFrame(41);
            stream.writeShort(i1);
            stream.writeShortA(j);
            stream.writeShortA(actionButtonId);
            atInventoryLoopCycle = 0;
            atInventoryInterface = actionButtonId;
            atInventoryIndex = j;
            atInventoryInterfaceType = 2;
            if (RSInterface.interfaceCache[actionButtonId].parentID == openInterfaceID) {
                atInventoryInterfaceType = 1;
            }
            if (RSInterface.interfaceCache[actionButtonId].parentID == backDialogID) { atInventoryInterfaceType = 3; }
        }
        if (l == 478) {
            NPC class30_sub2_sub4_sub1_sub1_7 = npcArray[i1];
            if (class30_sub2_sub4_sub1_sub1_7 != null) {
                doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, class30_sub2_sub4_sub1_sub1_7.smallY[0], myPlayer.smallX[0], false, class30_sub2_sub4_sub1_sub1_7.smallX[0]);
                crossX = super.saveClickX;
                crossY = super.saveClickY;
                crossType = 2;
                crossIndex = 0;
                if ((i1 & 3) == 0) { anInt1155++; }
                if (anInt1155 >= 53) {
                    stream.createFrame(85);
                    stream.writeByte(66);
                    anInt1155 = 0;
                }
                stream.createFrame(18);
                stream._writeLEShort(i1);
            }
        }
        if (l == 113) {
            method66(i1, actionButtonId, j, id);
            stream.createFrame(70);
            stream._writeLEShort(j + baseX);
            stream.writeShort(actionButtonId + baseY);
            stream.writeLEShortA(id);
        }
        if (l == 872) {
            method66(i1, actionButtonId, j, id);
            stream.createFrame(234);
            stream.writeLEShortA(j + baseX);
            stream.writeShortA(id);
            stream.writeLEShortA(actionButtonId + baseY);
        }
        if (l == 502) {
            method66(i1, actionButtonId, j, id);
            stream.createFrame(132);
            stream.writeLEShortA(j + baseX);
            stream.writeShort(id);
            stream.writeShortA(actionButtonId + baseY);
        }
        if (l == 1125) {
            ItemDef itemDef = ItemDef.forID(i1);
            RSInterface class9_4 = RSInterface.interfaceCache[actionButtonId];
            String s5;
            if (class9_4 != null && class9_4.invStackSizes[j] >= 0x186a0) {
                s5 = class9_4.invStackSizes[j] + " x " + itemDef.name;
            } else if (itemDef.description != null) { s5 = new String(itemDef.description); } else {
                s5 = "It's a " + itemDef.name + ".";
            }
            pushMessage(s5, 0, "");
        }
        if (l == 169) {
            stream.createFrame(185);
            stream.writeShort(actionButtonId);
            RSInterface class9_3 = RSInterface.interfaceCache[actionButtonId];
            if (class9_3.id == (21004)) {
                promptUserForInput(class9_3);
            }
            if (class9_3.valueIndexArray != null && class9_3.valueIndexArray[0][0] == 5) {
                int l2 = class9_3.valueIndexArray[0][1];
                variousSettings[l2] = 1 - variousSettings[l2];
                method33(l2);
                needDrawTabArea = true;
            }
        }
        if (l == 447) {
            itemSelected = 1;
            anInt1283 = j;
            anInt1284 = actionButtonId;
            anInt1285 = i1;
            selectedItemName = ItemDef.forID(i1).name;
            spellSelected = 0;
            needDrawTabArea = true;
            return;
        }
        if (l == 1226) {
            //int j1 = i1 >> 14 & 0x7fff;
            ObjectDef class46 = ObjectDef.forID(id);
            String s10;
            if (class46.description != null) { s10 = new String(class46.description); } else {
                s10 = "It's a " + class46.name + ".";
            }
            pushMessage(s10, 0, "");
        }
        if (l == 244) {
            boolean flag7 = doWalkTo(2, 0, 0, 0, myPlayer.smallY[0], 0, 0, actionButtonId, myPlayer.smallX[0], false, j);
            if (!flag7) {
                flag7 = doWalkTo(2, 0, 1, 0, myPlayer.smallY[0], 1, 0, actionButtonId, myPlayer.smallX[0], false, j);
            }
            crossX = super.saveClickX;
            crossY = super.saveClickY;
            crossType = 2;
            crossIndex = 0;
            stream.createFrame(253);
            stream._writeLEShort(j + baseX);
            stream.writeLEShortA(actionButtonId + baseY);
            stream.writeShortA(i1);
        }
        if (l == 1448) {
            ItemDef itemDef_1 = ItemDef.forID(i1);
            String s6;
            if (itemDef_1.description != null) { s6 = new String(itemDef_1.description); } else {
                s6 = "It's a " + itemDef_1.name + ".";
            }
            pushMessage(s6, 0, "");
        }
        itemSelected = 0;
        spellSelected = 0;
        needDrawTabArea = true;

    }

    private String getURL(String message) {
        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(message);
        if (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            return urlStr;
        }
        return "";
    }

    private void method70() {
        anInt1251 = 0;
        int j = (myPlayer.x >> 7) + baseX;
        int k = (myPlayer.y >> 7) + baseY;
        if (j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136) { anInt1251 = 1; }
        if (j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535) { anInt1251 = 1; }
        if (anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062) { anInt1251 = 0; }
    }

    private void build3dScreenMenu() {
        if (itemSelected == 0 && spellSelected == 0) {
            menuActionName[menuActionRow] = "Walk here";
            menuActionID[menuActionRow] = 516;
            menuActionCmd2[menuActionRow] = super.mouseX;
            menuActionCmd3[menuActionRow] = super.mouseY;
            menuActionRow++;
        }
        int j = -1;
        for (int k = 0; k < Model.anInt1687; k++) {
            int l = Model.anIntArray1688[k];
            int i1 = l & 0x7f;
            int j1 = l >> 7 & 0x7f;
            int k1 = l >> 29 & 3;
            int l1 = l >> 14 & 0x7fff;
            if (k1 != 2) { l1 = l >> 14 & 0x7fff; }
            if (l == j) { continue; }
            j = l;
            if (k1 == 2 && worldController.method304(plane, i1, j1, l) >= 0) {
                l1 = Model.anIntArray1689[k];
                //System.out.println("id2: " + l1);
                ObjectDef class46 = ObjectDef.forID(l1);
                if (class46.childrenIDs != null) { class46 = class46.method580(); }
                if (class46 == null) {
                    //System.out.println("Null for: " + id);
                    continue;
                }
                if (itemSelected == 1) {
                    menuActionName[menuActionRow] = "Use " + selectedItemName + " with @cya@" + class46.name;
                    menuActionID[menuActionRow] = 62;
                    menuActionCmd1[menuActionRow] = l;
                    menuActionCmd2[menuActionRow] = i1;
                    menuActionCmd3[menuActionRow] = j1;
                    menuActionCmd4[menuActionRow] = l1;
                    menuActionRow++;
                } else if (spellSelected == 1) {
                    if ((spellUsableOn & 4) == 4) {
                        menuActionName[menuActionRow] = spellTooltip + " @cya@" + class46.name;
                        menuActionID[menuActionRow] = 956;
                        menuActionCmd1[menuActionRow] = l;
                        menuActionCmd2[menuActionRow] = i1;
                        menuActionCmd3[menuActionRow] = j1;
                        menuActionCmd4[menuActionRow] = l1;
                        menuActionRow++;
                    }
                } else {
                    if (class46.itemActions != null) {
                        for (int i2 = 4; i2 >= 0; i2--) {
                            if (class46.itemActions[i2] != null) {
                                menuActionName[menuActionRow] = class46.itemActions[i2] + " @cya@" + class46.name;
                                if (i2 == 0) { menuActionID[menuActionRow] = 502; }
                                if (i2 == 1) { menuActionID[menuActionRow] = 900; }
                                if (i2 == 2) { menuActionID[menuActionRow] = 113; }
                                if (i2 == 3) { menuActionID[menuActionRow] = 872; }
                                if (i2 == 4) { menuActionID[menuActionRow] = 1062; }
                                menuActionCmd1[menuActionRow] = l;
                                menuActionCmd2[menuActionRow] = i1;
                                menuActionCmd3[menuActionRow] = j1;
                                menuActionCmd4[menuActionRow] = l1;
                                menuActionRow++;
                            }
                        }

                    }
                    // menuActionName[menuActionRow] = "Examine @cya@" +
                    // class46.name + " @gre@(@whi@" + l1 + "@gre@) (@whi@" +
                    // (i1 + baseX) + "," + (j1 + baseY) + "@gre@)";
                    menuActionName[menuActionRow] = "Examine @cya@" + class46.name + " " + (DEV ? Arrays.toString(class46.anIntArray773) : "");
                    menuActionID[menuActionRow] = 1226;
                    menuActionCmd1[menuActionRow] = class46.type << 14;
                    menuActionCmd2[menuActionRow] = i1;
                    menuActionCmd3[menuActionRow] = j1;
                    menuActionCmd4[menuActionRow] = l1;
                    menuActionRow++;
                }
            }
            if (k1 == 1) {
                NPC npc = npcArray[l1];
                if (npc.desc.aByte68 == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
                    for (int j2 = 0; j2 < npcCount; j2++) {
                        NPC npc2 = npcArray[npcIndices[j2]];
                        if (npc2 != null && npc2 != npc && npc2.desc.aByte68 == 1 && npc2.x == npc.x && npc2.y == npc.y) {
                            buildAtNPCMenu(npc2.desc, npcIndices[j2], j1, i1);
                        }
                    }

                    for (int l2 = 0; l2 < playerCount; l2++) {
                        Player player = playerArray[playerIndices[l2]];
                        if (player != null && player.x == npc.x && player.y == npc.y) {
                            buildAtPlayerMenu(i1, playerIndices[l2], player, j1);
                        }
                    }

                }
                buildAtNPCMenu(npc.desc, l1, j1, i1);
            }
            if (k1 == 0) {
                Player player = playerArray[l1];
                if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
                    for (int k2 = 0; k2 < npcCount; k2++) {
                        NPC class30_sub2_sub4_sub1_sub1_2 = npcArray[npcIndices[k2]];
                        if (class30_sub2_sub4_sub1_sub1_2 != null && class30_sub2_sub4_sub1_sub1_2.desc.aByte68 == 1 && class30_sub2_sub4_sub1_sub1_2.x == player.x && class30_sub2_sub4_sub1_sub1_2.y == player.y) {
                            buildAtNPCMenu(class30_sub2_sub4_sub1_sub1_2.desc, npcIndices[k2], j1, i1);
                        }
                    }

                    for (int i3 = 0; i3 < playerCount; i3++) {
                        Player class30_sub2_sub4_sub1_sub2_2 = playerArray[playerIndices[i3]];
                        if (class30_sub2_sub4_sub1_sub2_2 != null && class30_sub2_sub4_sub1_sub2_2 != player && class30_sub2_sub4_sub1_sub2_2.x == player.x && class30_sub2_sub4_sub1_sub2_2.y == player.y) {
                            buildAtPlayerMenu(i1, playerIndices[i3], class30_sub2_sub4_sub1_sub2_2, j1);
                        }
                    }

                }
                buildAtPlayerMenu(i1, l1, player, j1);
            }
            if (k1 == 3) {
                NodeList class19 = groundArray[plane][i1][j1];
                if (class19 != null) {
                    for (Item item = (Item) class19.getFirst(); item != null; item = (Item) class19.getNext()) {
                        ItemDef itemDef = ItemDef.forID(item.ID);
                        if (itemSelected == 1) {
                            menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
                            menuActionID[menuActionRow] = 511;
                            menuActionCmd1[menuActionRow] = item.ID;
                            menuActionCmd2[menuActionRow] = i1;
                            menuActionCmd3[menuActionRow] = j1;
                            menuActionRow++;
                        } else if (spellSelected == 1) {
                            if ((spellUsableOn & 1) == 1) {
                                menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name;
                                menuActionID[menuActionRow] = 94;
                                menuActionCmd1[menuActionRow] = item.ID;
                                menuActionCmd2[menuActionRow] = i1;
                                menuActionCmd3[menuActionRow] = j1;
                                menuActionRow++;
                            }
                        } else {
                            for (int j3 = 4; j3 >= 0; j3--) {
                                if (itemDef.groundActions != null && itemDef.groundActions[j3] != null) {
                                    menuActionName[menuActionRow] = itemDef.groundActions[j3] + " @lre@" + itemDef.name;
                                    if (j3 == 0) { menuActionID[menuActionRow] = 652; }
                                    if (j3 == 1) { menuActionID[menuActionRow] = 567; }
                                    if (j3 == 2) { menuActionID[menuActionRow] = 234; }
                                    if (j3 == 3) { menuActionID[menuActionRow] = 244; }
                                    if (j3 == 4) { menuActionID[menuActionRow] = 213; }
                                    menuActionCmd1[menuActionRow] = item.ID;
                                    menuActionCmd2[menuActionRow] = i1;
                                    menuActionCmd3[menuActionRow] = j1;
                                    menuActionRow++;
                                } else if (j3 == 2) {
                                    menuActionName[menuActionRow] = "Take @lre@" + itemDef.name;
                                    menuActionID[menuActionRow] = 234;
                                    menuActionCmd1[menuActionRow] = item.ID;
                                    menuActionCmd2[menuActionRow] = i1;
                                    menuActionCmd3[menuActionRow] = j1;
                                    menuActionRow++;
                                }
                            }

                            // menuActionName[menuActionRow] = "Examine @lre@" +
                            // itemDef.name + " @gre@(@whi@" + item.ID +
                            // "@gre@)";
                            menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name;
                            menuActionID[menuActionRow] = 1448;
                            menuActionCmd1[menuActionRow] = item.ID;
                            menuActionCmd2[menuActionRow] = i1;
                            menuActionCmd3[menuActionRow] = j1;
                            menuActionRow++;
                        }
                    }

                }
            }
        }
    }

    public void drawTabHover() {
        int extraX = isFullScreen ? extraWidth + 511 : 0;
        int extraY = isFullScreen ? extraHeight + 168 + 262 : 0;
        int y2 = isFullScreen ? -264 : 0;

        int extraXX = drawLongTabs ? -241 : 0;
        int extraYY = drawLongTabs ? +36 : 0;

        int mhm = drawLongTabs ? +4 : 0;
        int mhm2 = drawLongTabs ? -5 : 0;

        int mhm3 = drawLongTabs ? -1 : 0;
        int mhm4 = drawLongTabs ? -1 : 0;

        int extraY2 = drawLongTabs ? +3 : 0;

        if (tabHPos == 1 && tabInterfaceIDs[0] != -1 && drawLongTabs) {
            tabHover.drawSprite(33 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 2 && tabInterfaceIDs[1] != -1 && drawLongTabs) {
            tabHover.drawSprite(63 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 3 && tabInterfaceIDs[2] != -1 && drawLongTabs) {
            tabHover.drawSprite(93 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 0 && tabInterfaceIDs[0] != -1 && !drawLongTabs) {
            tabHover.drawSprite(3 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 1 && tabInterfaceIDs[1] != -1 && !drawLongTabs) {
            tabHover.drawSprite(33 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 2 && tabInterfaceIDs[2] != -1 && !drawLongTabs) {
            tabHover.drawSprite(63 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 3 && tabInterfaceIDs[14] != -1 && !drawLongTabs) {
            tabHover.drawSprite(93 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 4 && tabInterfaceIDs[3] != -1) {
            tabHover.drawSprite(123 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 5 && tabInterfaceIDs[4] != -1) {
            tabHover.drawSprite(153 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 6 && tabInterfaceIDs[5] != -1) {
            tabHover.drawSprite(183 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 7 && tabInterfaceIDs[6] != -1) {
            tabHover.drawSprite(213 + extraX + extraXX + mhm, 0 + extraY + extraYY + mhm2);
        } else if (tabHPos == 15 && tabInterfaceIDs[16] != -1) {
            tabHover.drawSprite(3 + extraX + mhm + mhm3, 298 + extraY + y2 + extraY2 + mhm2 + mhm4);
        } else if (tabHPos == 8 && tabInterfaceIDs[9] != -1) {
            tabHover.drawSprite(33 + extraX + mhm + mhm3, 298 + extraY + y2 + extraY2 + mhm2 + mhm4);
        } else if (tabHPos == 9 && tabInterfaceIDs[8] != -1) {
            tabHover.drawSprite(63 + extraX + mhm + mhm3, 298 + extraY + y2 + extraY2 + mhm2 + mhm4);
        } else if (tabHPos == 10 && tabInterfaceIDs[7] != -1) {
            tabHover.drawSprite(93 + extraX + mhm + mhm3, 298 + extraY + y2 + extraY2 + mhm2 + mhm4);
        } else if (tabHPos == 11 && tabInterfaceIDs[11] != -1) {
            tabHover.drawSprite(123 + extraX + mhm + mhm3, 298 + extraY + y2 + extraY2 + mhm2 + mhm4);
        } else if (tabHPos == 12 && tabInterfaceIDs[12] != -1) {
            tabHover.drawSprite(153 + extraX + mhm + mhm3, 298 + extraY + y2 + extraY2 + mhm2 + mhm4);
        } else if (tabHPos == 13 && tabInterfaceIDs[13] != -1) {
            tabHover.drawSprite(183 + extraX + mhm + mhm3, 298 + extraY + y2 + extraY2 + mhm2 + mhm4);
        } else if (tabHPos == 14 && tabInterfaceIDs[15] != -1) {
            tabHover.drawSprite(213 + extraX + mhm + mhm3, 298 + extraY + y2 + extraY2 + mhm2 + mhm4);
        }
    }

    private void printDebug() {
        System.out.println("============");
        if (onDemandFetcher != null) { System.out.println("Od-cycle:" + onDemandFetcher.onDemandCycle); }
        System.out.println("loop-cycle:" + loopCycle);
        System.out.println("draw-cycle:" + anInt1061);
        System.out.println("ptype:" + pktType);
        System.out.println("psize:" + pktSize);
        if (socketStream != null) { socketStream.printDebug(); }
        super.shouldDebug = true;
    }

    public void hitmarkDraw(int i, int j, int k, int l, int i1, int j1, int k1) {
        if (l > 0) {
            Sprite sprite = null;
            Sprite sprite2 = null;
            Sprite sprite3 = null;
            int l1 = 0;
            switch (i) {
                case 1: // '\001'
                    l1 = 8;
                    break;

                case 2: // '\002'
                    l1 = 4;
                    break;

                case 3: // '\003'
                    l1 = 1;
                    break;
            }
            if (i1 > 0) {
                l1 -= 16;
            }
            if (j == 1) {
                j = 0;
            } else if (j == 0) {
                j = 1;
            }
            //here
            //so what variables im using now? if(i == 0) { i = 1; i cant be 0 since oh nvm thats a diff var
            sprite = hitMark[j * 3];
            sprite2 = hitMark[j * 3 + 1];
            sprite3 = hitMark[j * 3 + 2];
            if (k != 255) {
                hitIcon[k].drawSprite3((spriteDrawX - 34) + l1, (spriteDrawY - 14) + j1, k1);
            }
            sprite.drawSprite3((spriteDrawX - 12) + l1, (spriteDrawY - 12) + j1, k1);
            l1 += 4;
            for (int i2 = 0; i2 < i * 2; i2++) {
                sprite2.drawSprite3((spriteDrawX - 12) + l1, (spriteDrawY - 12) + j1, k1);
                l1 += 4;
            }

            sprite3.drawSprite3((spriteDrawX - 12) + l1, (spriteDrawY - 12) + j1, k1);
            (j != 1 ? smallHit : bigHit).drawOpacityText(0xffffff, String.valueOf(l), spriteDrawY + (j != 1 ? 3 : 2) + j1, spriteDrawX + 4 + (i1 <= 0 ? 0 : -16), k1);
            if (i1 > 0) {
                drawSoak(i1, k1, j1, l1);
            }
        } else {
            Sprite sprite1 = new Sprite("/Hitmarks/block");
            sprite1.drawSprite3(spriteDrawX - 12, (spriteDrawY - 14) + j1, k1);
        }
    }    private StreamLoader streamLoaderForName(int i, String s, String s1, int j, int k) {
        byte abyte0[] = null;
        int l = 5;
        try {
            if (decompressors[0] != null) {
                abyte0 = decompressors[0].decompress(i);
            }
        } catch (Exception ignored) {
        }

        if (abyte0 != null) {
            return new StreamLoader(abyte0, s);
        }

        int i1 = 0;
        do {
            if (abyte0 != null) {
                break;
            }
            String s3 = "Unknown error";
            Loader.getInstance().updateValues(k, String.format("Requesting %s", s));
            Object obj = null;
            try {
                int l1 = 0;
                DataInputStream datainputstream = openJagGrabInputStream((new StringBuilder()).append(s1).append(j).toString());
                byte abyte1[] = new byte[6];
                datainputstream.readFully(abyte1, 0, 6);
                Stream stream1 = new Stream(abyte1);
                stream1.currentOffset = 3;
                int j2 = stream1.readTriByte() + 6;
                int k2 = 6;
                abyte0 = new byte[j2];
                System.arraycopy(abyte1, 0, abyte0, 0, 6);
                while (k2 < j2) {
                    int l2 = j2 - k2;
                    if (l2 > 1000) {
                        l2 = 1000;
                    }
                    int i3 = datainputstream.read(abyte0, k2, l2);
                    if (i3 < 0) {
                        s3 = (new StringBuilder()).append("Length error: ").append(k2).append("/").append(j2).toString();
                        throw new IOException("EOF");
                    }
                    k2 += i3;
                    int j3 = (k2 * 100) / j2;
                    if (j3 != l1) {
                        Loader.getInstance().updateValues(k, String.format("Loading %s - %d%", s, j3));
                    }
                    l1 = j3;
                }
                datainputstream.close();
                try {
                    if (decompressors[0] != null) {
                        decompressors[0].method234(abyte0.length, abyte0, i);
                    }
                } catch (Exception exception4) {
                    decompressors[0] = null;
                }
            } catch (IOException ioexception) {
                if (s3.equals("Unknown error")) {
                    s3 = "VerifyConnection error";
                }
                abyte0 = null;
            } catch (NullPointerException nullpointerexception) {
                s3 = "Null error";
                abyte0 = null;
                if (!Signlink.reporterror) {
                    return null;
                }
            } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
                s3 = "Bounds error";
                abyte0 = null;
                if (!Signlink.reporterror) {
                    return null;
                }
            } catch (Exception exception2) {
                s3 = "Unexpected error";
                abyte0 = null;
                if (!Signlink.reporterror) {
                    return null;
                }
            }
            if (abyte0 == null) {
                for (int i2 = l; i2 > 0; i2--) {
                    if (i1 >= 3) {
                        Loader.getInstance().updateValues(k, "Game updated - please reload page");
                        i2 = 10;
                    } else {
                        Loader.getInstance().updateValues(k, "Please reload the Client in 10 seconds!");
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception exception3) {
                    }
                }

                l *= 2;
                if (l > 60) {
                    l = 60;
                }
                aBoolean872 = !aBoolean872;
            }
        } while (true);
        return new StreamLoader(abyte0, s);
    }

    public void drawSoak(int i, int j, int k, int l) {
        l -= 12;
        int i1 = String.valueOf(i).length();
        hitIcon[5].drawSprite3(spriteDrawX + l, (spriteDrawY - 12) + k, j);
        l += 20;
        hitMark[30].drawSprite3(spriteDrawX + l, (spriteDrawY - 12) + k, j);
        l += 4;
        for (int j1 = 0; j1 < i1 * 2; j1++) {
            hitMark[31].drawSprite3(spriteDrawX + l, (spriteDrawY - 12) + k, j);
            l += 4;
        }

        hitMark[32].drawSprite3(spriteDrawX + l, (spriteDrawY - 10) + k, j);
        smallHit.drawOpacityText(0xffffff, String.valueOf(i), spriteDrawY + 3 + k, (spriteDrawX - 8) + l + (i1 != 1 ? 0 : 5), j);
    }

    private void method73() {
        do {
            int character = readChar(-796);
            if (character == -1) { break; }
            if (openInterfaceID != -1 && openInterfaceID == reportAbuseInterfaceID) {
                if (character == 8 && reportAbuseInput.length() > 0) {
                    reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
                }
                if ((character >= 97 && character <= 126 || character >= 65 && character <= 90 || character >= 48 && character <= 57 || character == 32) && reportAbuseInput.length() < 12) {
                    reportAbuseInput += (char) character;
                }

            } else if (messagePromptRaised) {
                if (character >= 32 && character <= 126 && promptInput.length() < 80) {
                    promptInput += (char) character;
                    inputTaken = true;
                }
                if (character == 8 && promptInput.length() > 0) {
                    promptInput = promptInput.substring(0, promptInput.length() - 1);
                    inputTaken = true;
                }
                if ((friendsListAction == 11) && (promptInput.length() < 80) && (promptInput.length() >= 0)) {
                    stream.createFrame(61);
                    stream.writeLong(TextClass.longForName(promptInput.trim().toLowerCase()));
                }
                if (character == 13 || character == 10) {
                    if (searchBank) {
                        searchBank = false;
                    }
                    messagePromptRaised = false;
                    inputTaken = true;
                    if (friendsListAction == 1) {
                        long l = TextClass.longForName(promptInput);
                        addFriend(l);
                    }
                    if (friendsListAction == 2 && friendsCount > 0) {
                        long l1 = TextClass.longForName(promptInput);
                        delFriend(l1);
                    }
                    if (friendsListAction == 3 && promptInput.length() > 0) {
                        stream.createFrame(126);
                        stream.writeByte(0);
                        int k = stream.currentOffset;
                        stream.writeLong(aLong953);
                        System.out.println("input: " + promptInput);
                        TextInput.method526(promptInput, stream);
                        stream.writeByteC(stream.currentOffset - k);
                        promptInput = TextInput.processText(promptInput);
                        // promptInput = Censor.doCensor(promptInput);
                        pushMessage(promptInput, 6, TextClass.fixName(TextClass.nameForLong(aLong953)));

                        if (privateChatMode == 2) {
                            privateChatMode = 1;
                            aBoolean1233 = true;
                            stream.createFrame(95);
                            stream.writeByte(publicChatMode);
                            stream.writeByte(privateChatMode);
                            stream.writeByte(tradeMode);
                        }
                    }
                    if (friendsListAction == 4 && ignoreCount < 100) {
                        long l2 = TextClass.longForName(promptInput);
                        addIgnore(l2);
                    }
                    if (friendsListAction == 5 && ignoreCount > 0) {
                        long l3 = TextClass.longForName(promptInput);
                        delIgnore(l3);
                    }
                    if (friendsListAction == 6) {
                        long l3 = TextClass.longForName(promptInput);
                        chatJoin(l3);
                    }

                }
            } else if (inputDialogState == 1) {
                if (character >= 48 && character <= 57 && amountOrNameInput.length() < 10) {
                    amountOrNameInput += (char) character;
                    inputTaken = true;
                }
                if (character == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                    inputTaken = true;
                }
                if ((character == 13) || (character == 10)) {
                    if (amountOrNameInput.length() > 0) {
                        int length = amountOrNameInput.length();
                        char lastChar = amountOrNameInput.charAt(length - 1);
                        if (lastChar == 'k') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000";
                        } else if (lastChar == 'm') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000000";
                        } else if (lastChar == 'b') {
                            amountOrNameInput = amountOrNameInput.substring(0, length - 1) + "000000000";
                        }
                        long amount = 0;
                        try {
                            amount = Long.parseLong(amountOrNameInput);
                            if (amount < 0) {
                                amount = 0;
                            } else if (amount > Integer.MAX_VALUE) {
                                amount = Integer.MAX_VALUE;
                            }
                        } catch (Exception ignored) {

                        }
                        if (amount > 0) {
                            stream.createFrame(208);
                            stream.writeInt((int) amount);
                            if (openInterfaceID == 5292) {
                                modifiableXValue = (int) amount;
                            }
                        }
                    }
                    inputDialogState = 0;
                    inputTaken = true;
                }
            } else if (inputDialogState == 2) {
                if (character >= 32 && character <= 126 && amountOrNameInput.length() < 12) {
                    amountOrNameInput += (char) character;
                    inputTaken = true;
                }
                if (character == 8 && amountOrNameInput.length() > 0) {
                    amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
                    inputTaken = true;
                }
                if (character == 13 || character == 10) {
                    if (amountOrNameInput.length() > 0) {
                        stream.createFrame(60);
                        stream.writeLong(TextClass.longForName(amountOrNameInput));
                    }
                    inputDialogState = 0;
                    inputTaken = true;
                }
            } else if (openInterfaceID == 28000 /*|| tabID == 14*/) {
                if (character == 8 && customInterfaceInput.length() > 0) {
                    customInterfaceInput = customInterfaceInput.substring(0, customInterfaceInput.length() - 1);
                }
                if ((character >= 97 && character <= 126 || character >= 65 && character <= 90 || character >= 48 && character <= 57 || character == 32) && customInterfaceInput.length() < 12) {
                    customInterfaceInput += (char) character;
                }
            } else if (openInterfaceID == 35000) {
                if (character == 8) {
                    stream.createFrame(330); //help message packet
                    stream.writeString(customInterfaceInput);

                } else { customInterfaceInput += (char) character; }
            } else if (backDialogID == -1) {
                if (itemSearch) {
                    if (character >= 32 && character <= 126 && geSearchInput.length() < 80) {
                        geSearchInput += (char) character;
                        updateGESearch(geSearchInput);
                    }
                    if (character == 8 && geSearchInput.length() > 0) {
                        geSearchInput = geSearchInput.substring(0, geSearchInput.length() - 1);
                        updateGESearch(geSearchInput);
                    }
                    if ((character == 13 || character == 10)/* && geSearchInput.length() > 0*/) {
                        itemSearch = false;
                        geSearchInput = "";
                        for (int i = 0; i < itemSearchResults.length; i++) { itemSearchResults[i] = null; }
                    }
                } else {
                    if (character >= 32 && character <= 126 && inputString.length() < 80) {
                        inputString += (char) character;
                        inputTaken = true;
                    }
                    if (character == 8 && inputString.length() > 0) {
                        inputString = inputString.substring(0, inputString.length() - 1);
                        inputTaken = true;
                    }
                    if ((character == 13 || character == 10) && inputString.length() > 0) {
                        //TODO: Command Handler
                        if (inputString.startsWith("/")) {
                            inputString = String.format("::cc %s", inputString.substring(1));
                        }
                        if (inputString.equals("!donate")) {
                            launchURL(Constants.DONATE_URL);
                        } else if (inputString.equals("!fps")) {
                            Configuration.getInstance().get(FPS.class).setValue(!Configuration.getInstance().getValue(FPS.class));
                        } else if (inputString.equals("!data")) {
                            Configuration.getInstance().get(ClientData.class).setValue(!Configuration.getInstance().getValue(ClientData.class));
                        } else if (inputString.startsWith("!cinterface")) {
                            final String value = inputString.replace("!cinterface ", "").trim();
                            if (value.matches("[\\d]+")) {
                                InterfaceManager.show(Integer.parseInt(value));
                            }
                        } else if (inputString.startsWith("::") || inputString.startsWith(";;")) {
                            stream.createFrame(103);
                            stream.writeByte(inputString.length() - 1);
                            stream.writeString(inputString.substring(2));
                        } else {
                            String s = inputString.toLowerCase();
                            int j2 = 0;
                            if (s.startsWith("yellow:")) {
                                j2 = 0;
                                inputString = inputString.substring(7);
                            } else if (s.startsWith("red:")) {
                                j2 = 1;
                                inputString = inputString.substring(4);
                            } else if (s.startsWith("green:")) {
                                j2 = 2;
                                inputString = inputString.substring(6);
                            } else if (s.startsWith("cyan:")) {
                                j2 = 3;
                                inputString = inputString.substring(5);
                            } else if (s.startsWith("purple:")) {
                                j2 = 4;
                                inputString = inputString.substring(7);
                            } else if (s.startsWith("white:")) {
                                j2 = 5;
                                inputString = inputString.substring(6);
                            } else if (s.startsWith("flash1:")) {
                                j2 = 6;
                                inputString = inputString.substring(7);
                            } else if (s.startsWith("flash2:")) {
                                j2 = 7;
                                inputString = inputString.substring(7);
                            } else if (s.startsWith("flash3:")) {
                                j2 = 8;
                                inputString = inputString.substring(7);
                            } else if (s.startsWith("glow1:")) {
                                j2 = 9;
                                inputString = inputString.substring(6);
                            } else if (s.startsWith("glow2:")) {
                                j2 = 10;
                                inputString = inputString.substring(6);
                            } else if (s.startsWith("glow3:")) {
                                j2 = 11;
                                inputString = inputString.substring(6);
                            }
                            s = inputString.toLowerCase();
                            int i3 = 0;
                            if (s.startsWith("wave:")) {
                                i3 = 1;
                                inputString = inputString.substring(5);
                            } else if (s.startsWith("wave2:")) {
                                i3 = 2;
                                inputString = inputString.substring(6);
                            } else if (s.startsWith("shake:")) {
                                i3 = 3;
                                inputString = inputString.substring(6);
                            } else if (s.startsWith("scroll:")) {
                                i3 = 4;
                                inputString = inputString.substring(7);
                            } else if (s.startsWith("slide:")) {
                                i3 = 5;
                                inputString = inputString.substring(6);
                            }
                            if (displayChat) {
                                stream.createFrame(4);
                                stream.writeByte(0);
                                int j3 = stream.currentOffset;
                                stream.writeByteS(i3);
                                stream.writeByteS(j2);
                                aStream_834.currentOffset = 0;
                                TextInput.method526(inputString, aStream_834);
                                stream.readReverseA(0, aStream_834.payload, aStream_834.currentOffset);
                                stream.writeByteC(stream.currentOffset - j3);
                                inputString = TextInput.processText(inputString);
                                // inputString = Censor.doCensor(inputString);
                                myPlayer.textSpoken = inputString;
                                myPlayer.anInt1513 = j2;
                                myPlayer.anInt1531 = i3;
                                myPlayer.textCycle = 150;
                                pushMessage(myPlayer.textSpoken, 2, (Configuration.getInstance().getValue(Titles.class) ? myPlayer.title + " @bla@" : "") + myPlayer.name, myPrivilege);
                                if (publicChatMode == 2) {
                                    publicChatMode = 3;
                                    aBoolean1233 = true;
                                    stream.createFrame(95);
                                    stream.writeByte(publicChatMode);
                                    stream.writeByte(privateChatMode);
                                    stream.writeByte(tradeMode);
                                }
                            }
                        }
                        inputString = "";
                        inputTaken = true;
                    }
                }
            }
        } while (true);
    }

    public void updateGESearch(String itemSearch) {
        for (int i = 0; i < itemSearchResults.length; i++) { itemSearchResults[i] = null; }
        if (itemSearch.equals("")) { return; }
        int searchIndex = 0;
        for (ItemDef def : ItemDef.itemList) {
            if (searchIndex == 499) { return; }
            if (def.name.toLowerCase().contains(itemSearch.toLowerCase()) && def.lentItemID == -1 && def.certTemplateID == -1) {
                itemSearchResults[searchIndex++] = def;
            }
        }
    }

    private void buildPublicChat(int j) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null) { continue; }
            if (chatTypeView != 1) { continue; }
            int j1 = chatTypes[i1];
            String s = chatNames[i1];
            String ct = chatMessages[i1];
            int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
            if (k1 < -23) { break; }
            if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
                if (j > k1 - 14 && j <= k1 && !s.equalsIgnoreCase(myPlayer.name)) {
                    if (myPrivilege >= 1) {
                        menuActionName[menuActionRow] = "Report abuse @whi@" + s;
                        menuActionID[menuActionRow] = 606;
                        menuActionRow++;
                    }
                    menuActionName[menuActionRow] = "Add ignore @whi@" + s.substring(s.indexOf("@bla@") + 5);
                    menuActionID[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add friend @whi@" + s.substring(s.indexOf("@bla@") + 5);
                    menuActionID[menuActionRow] = 337;
                    menuActionRow++;
                }
                l++;
            }
        }
    }

    private void buildFriendChat(int j) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null) { continue; }
            if (chatTypeView != 2) { continue; }
            int j1 = chatTypes[i1];
            String s = chatNames[i1];
            String ct = chatMessages[i1];
            int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
            if (k1 < -23) { break; }
            if ((j1 == 5 || j1 == 6) && (splitPrivateChat == 0 || chatTypeView == 2) && (j1 == 6 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
                l++;
            }
            if ((j1 == 3 || j1 == 7) && (splitPrivateChat == 0 || chatTypeView == 2) && (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
                if (j > k1 - 14 && j <= k1) {
                    if (myPrivilege >= 1) {
                        menuActionName[menuActionRow] = "Report abuse @whi@" + s;
                        menuActionID[menuActionRow] = 606;
                        menuActionID[menuActionRow] = 606;
                        menuActionRow++;
                    }
                    menuActionName[menuActionRow] = "Add ignore @whi@" + s;
                    menuActionID[menuActionRow] = 42;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Reply to @whi@" + s;
                    menuActionID[menuActionRow] = 639;
                    menuActionRow++;
                    menuActionName[menuActionRow] = "Add friend @whi@" + s;
                    menuActionID[menuActionRow] = 337;
                    menuActionRow++;
                }
                l++;
            }
        }
    }

    private void buildDuelorTrade(int j) {
        int l = 0;
        for (int i1 = 0; i1 < 500; i1++) {
            if (chatMessages[i1] == null) { continue; }
            if (chatTypeView != 3 && chatTypeView != 4) { continue; }
            int j1 = chatTypes[i1];
            String s = chatNames[i1];
            int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
            if (k1 < -23) { break; }
            if (chatTypeView == 3 && j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
                if (j > k1 - 14 && j <= k1) {
                    menuActionName[menuActionRow] = "Accept trade @whi@" + s;
                    menuActionID[menuActionRow] = 484;
                    menuActionRow++;
                }
                l++;
            }
            if (chatTypeView == 4 && j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
                if (j > k1 - 14 && j <= k1) {
                    menuActionName[menuActionRow] = "Accept challenge @whi@" + s;
                    menuActionID[menuActionRow] = 6;
                    menuActionRow++;
                }
                l++;
            }
        }
    }

    private void buildChatAreaMenu(int j, int mX) {
        int l = 0;
        int test = 0;
        if (itemSearch) {
            for (int i = 0; i < itemSearchResults.length; i++) {
                int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
                if (k1 < -23) { break; }
                if (j > k1 - 14 && j <= k1 && itemSearchResults[i] != null) {
                    menuActionName[1] = "Select " + itemSearchResults[i].name;
                    menuActionID[1] = 1519;
                    menuActionRow = 2;
                    menuActionCmd4[1] = itemSearchResults[i].id;
                }
                l++;
            }
        } else {
            for (int i1 = 0; i1 < 500; i1++) {
                if (chatMessages[i1] == null) { continue; }
                int j1 = chatTypes[i1];
                int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
                if (k1 < -23) { break; }
                String s = chatNames[i1];
                String ct = chatMessages[i1];
                boolean flag = false;
                if (displayChat) {
                    if (chatTypeView == 1) {
                        buildPublicChat(j);
                        break;
                    }
                    if (chatTypeView == 2) {
                        buildFriendChat(j);
                        break;
                    }
                    if (chatTypeView == 3 || chatTypeView == 4) {
                        buildDuelorTrade(j);
                        break;
                    }
                    if (chatTypeView == 5) {
                        break;
                    }
                    if (j1 == 0) { l++; }
                    if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
                        if (j > k1 - 14 && j <= k1 && !s.equalsIgnoreCase(myPlayer.name)) {
                            if (myPrivilege >= 1) {
                                menuActionName[menuActionRow] = "Report abuse @whi@" + s.substring(s.indexOf("@bla@") + 5);
                                menuActionID[menuActionRow] = 606;
                                menuActionRow++;
                            }
                            menuActionName[menuActionRow] = "Add ignore @whi@" + s.substring(s.indexOf("@bla@") + 5);
                            menuActionID[menuActionRow] = 42;
                            menuActionRow++;
                            menuActionName[menuActionRow] = "Add friend @whi@" + s.substring(s.indexOf("@bla@") + 5);
                            menuActionID[menuActionRow] = 337;
                            menuActionRow++;
                        }
                        l++;
                    }
                    if ((j1 == 3 || j1 == 7) && splitPrivateChat == 0 && (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
                        if (j > k1 - 14 && j <= k1) {
                            if (myPrivilege >= 1) {
                                menuActionName[menuActionRow] = "Report abuse @whi@" + s;
                                menuActionID[menuActionRow] = 606;
                                menuActionRow++;
                            }
                            menuActionName[menuActionRow] = "Add ignore @whi@" + s;
                            menuActionID[menuActionRow] = 42;
                            menuActionRow++;
                            menuActionName[menuActionRow] = "Add friend @whi@" + s;
                            menuActionID[menuActionRow] = 337;
                            menuActionRow++;
                        }
                        l++;
                    }
                    if (j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
                        if (j > k1 - 14 && j <= k1) {
                            menuActionName[menuActionRow] = "Accept trade @whi@" + s;
                            menuActionID[menuActionRow] = 484;
                            menuActionRow++;
                        }
                        l++;
                    }
                    if ((j1 == 5 || j1 == 6) && splitPrivateChat == 0 && privateChatMode < 2) { l++; }
                    if (j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
                        if (j > k1 - 14 && j <= k1) {
                            menuActionName[menuActionRow] = "Accept challenge @whi@" + s;
                            menuActionID[menuActionRow] = 6;
                            menuActionRow++;
                        }
                        l++;
                    }
                    if (j1 == 9) {
                        if (j > k1 - 14 && j <= k1) {
                            String url = getURL(s);
                            if (url != null && url.length() > 4) {
                                String before = s.substring(0, s.indexOf(url));
                                int bx = aTextDrawingArea_1271.getTextWidth(before) + 9;
                                int ex = aTextDrawingArea_1271.getTextWidth(url) + 9;
                                if (mX >= bx && mX < bx + ex) {
                                    menuActionName[menuActionRow] = "Go-to @lre@" + url;
                                    menuActionID[menuActionRow] = 927;
                                    menuActionRow++;
                                }
                            }
                        }
                        l++;
                    }
                }
            }
        }
    }    public void run() {
        super.run();
    }

    private void drawFriendsListOrWelcomeScreen(RSInterface class9) {
        int j = class9.contentType;
        if (j >= 205 && j <= 205 + 25) {
            j -= 205;
            class9.message = setMessage(j);
            //System.out.println(j);
            return;
        }
        if (j >= 1 && j <= 100 || j >= 701 && j <= 800) {
            if (j == 1 && anInt900 == 0) {
                class9.message = "Loading friend list";
                class9.atActionType = 0;
                return;
            }
            if (j == 1 && anInt900 == 1) {
                class9.message = "Connecting to friendserver";
                class9.atActionType = 0;
                return;
            }
            if (j == 2 && anInt900 != 2) {
                class9.message = "Please wait...";
                class9.atActionType = 0;
                return;
            }
            int k = friendsCount;
            if (anInt900 != 2) { k = 0; }
            if (j > 700) { j -= 601; } else { j--; }
            if (j >= k) {
                class9.message = "";
                class9.atActionType = 0;
                return;
            } else {
                class9.message = friendsList[j];
                class9.atActionType = 1;
                return;
            }
        }

        if (j >= 101 && j <= 200 || j >= 801 && j <= 900) {
            int l = friendsCount;
            if (anInt900 != 2) { l = 0; }
            if (j > 800) { j -= 701; } else { j -= 101; }
            if (j >= l) {
                class9.message = "";
                class9.atActionType = 0;
                return;
            }
            if (friendsNodeIDs[j] == 0) { class9.message = "@red@Offline"; } else if (friendsNodeIDs[j] == nodeID) {
                class9.message = "@gre@Online"/* + (friendsNodeIDs[j] - 9) */;
            } else { class9.message = "@red@Offline"/* + (friendsNodeIDs[j] - 9) */; }
            class9.atActionType = 1;
            return;
        }

        if (j >= 401 && j <= 500) {
            if ((j -= 401) == 0 && anInt900 == 0) {
                class9.message = "Loading ignore list";
                class9.atActionType = 0;
                return;
            }
            if (j == 1 && anInt900 == 0) {
                class9.message = "Please wait...";
                class9.atActionType = 0;
                return;
            }
            int j1 = ignoreCount;
            if (anInt900 == 0) { j1 = 0; }
            if (j >= j1) {
                class9.message = "";
                class9.atActionType = 0;
                return;
            } else {
                class9.message = TextClass.fixName(TextClass.nameForLong(ignoreListAsLongs[j]));
                class9.atActionType = 1;
                return;
            }
        }
        if (j == 503) {
            class9.scrollMax = ignoreCount * 15 + 20;
            if (class9.scrollMax <= class9.height) { class9.scrollMax = class9.height + 1; }
            return;
        }
        if (j == 327) {
            class9.modelRotation1 = 150;
            class9.modelRotation2 = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
            if (aBoolean1031) {
                for (int k1 = 0; k1 < 7; k1++) {
                    int l1 = anIntArray1065[k1];
                    if (l1 >= 0 && !IdentityKit.cache[l1].method537()) { return; }
                }

                aBoolean1031 = false;
                Model aclass30_sub2_sub4_sub6s[] = new Model[7];
                int i2 = 0;
                for (int j2 = 0; j2 < 7; j2++) {
                    int k2 = anIntArray1065[j2];
                    if (k2 >= 0) { aclass30_sub2_sub4_sub6s[i2++] = IdentityKit.cache[k2].method538(); }
                }

                Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
                for (int l2 = 0; l2 < 5; l2++) {
                    if (anIntArray990[l2] != 0) {
                        model.method476(anIntArrayArray1003[l2][0], anIntArrayArray1003[l2][anIntArray990[l2]]);
                        if (l2 == 1) { model.method476(anIntArray1204[0], anIntArray1204[anIntArray990[l2]]); }
                    }
                }

                model.method469();
                model.method470(Animation.anims[myPlayer.anInt1511].anIntArray353[0]);
                model.method479(64, 850, -30, -50, -30, true);
                class9.disabledMediaType = 5;
                class9.mediaID = 0;
                RSInterface.method208(aBoolean994, model);
            }
            return;
        }
        if (j == 328) {
            RSInterface rsInterface = class9;
            int verticleTilt = 150;
            int animationSpeed = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
            rsInterface.modelRotation1 = verticleTilt;
            rsInterface.modelRotation2 = animationSpeed;
            if (aBoolean1031) {
                Model characterDisplay = myPlayer.method452();
                for (int l2 = 0; l2 < 5; l2++) {
                    if (anIntArray990[l2] != 0) {
                        characterDisplay.method476(anIntArrayArray1003[l2][0], anIntArrayArray1003[l2][anIntArray990[l2]]);
                        if (l2 == 1) {
                            characterDisplay.method476(anIntArray1204[0], anIntArray1204[anIntArray990[l2]]);
                        }
                    }
                }
                int staticFrame = myPlayer.anInt1511;
                characterDisplay.method469();
                characterDisplay.method470(Animation.anims[staticFrame].anIntArray353[0]);
                // characterDisplay.method479(64, 850, -30, -50, -30, true);
                rsInterface.disabledMediaType = 5;
                rsInterface.mediaID = 0;
                RSInterface.method208(aBoolean994, characterDisplay);
            }
            return;
        }
        if (j == 324) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
                aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
            }
            if (aBoolean1047) {
                class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
                return;
            } else {
                class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
                return;
            }
        }
        if (j == 325) {
            if (aClass30_Sub2_Sub1_Sub1_931 == null) {
                aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
                aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
            }
            if (aBoolean1047) {
                class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
                return;
            } else {
                class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
                return;
            }
        }
        if (j == 600) {
            class9.message = reportAbuseInput;

            if (loopCycle % 20 < 10) {
                class9.message += "|";
                return;
            } else {
                class9.message += " ";
                return;
            }
        }
        if (j == 620) {
            class9.message = customInterfaceInput;

            if (loopCycle % 20 < 10) {
                class9.message += "|";
                return;
            } else {
                class9.message += " ";
                return;
            }
        }
        if (j == 613) {
            if (myPrivilege >= 1) {
                if (canMute) {
                    class9.disabledColor = 0xff0000;
                    class9.message = "Moderator option: Mute player for 48 hours: <ON>";
                } else {
                    class9.disabledColor = 0xffffff;
                    class9.message = "Moderator option: Mute player for 48 hours: <OFF>";
                }
            } else {
                class9.message = "";
            }
        }
        if (j == 650 || j == 655) {
            if (anInt1193 != 0) {
                String s;
                if (daysSinceLastLogin == 0) { s = "earlier today"; } else if (daysSinceLastLogin == 1) {
                    s = "yesterday";
                } else { s = daysSinceLastLogin + " days ago"; }
                class9.message = "You last logged in " + s + " from: " + Signlink.dns;
            } else {
                class9.message = "";
            }
        }
        if (j == 651) {
            if (unreadMessages == 0) {
                class9.message = "0 unread messages";
                class9.disabledColor = 0xffff00;
            }
            if (unreadMessages == 1) {
                class9.message = "1 unread message";
                class9.disabledColor = 65280;
            }
            if (unreadMessages > 1) {
                class9.message = unreadMessages + " unread messages";
                class9.disabledColor = 65280;
            }
        }
        if (j == 652) {
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1) {
                    class9.message = "@yel@This is a non-members world: @whi@Since you are a member we";
                } else { class9.message = ""; }
            } else if (daysSinceRecovChange == 200) {
                class9.message = "You have not yet set any password recovery questions.";
            } else {
                String s1;
                if (daysSinceRecovChange == 0) { s1 = "Earlier today"; } else if (daysSinceRecovChange == 1) {
                    s1 = "Yesterday";
                } else {
                    s1 = daysSinceRecovChange + " days ago";
                }
                class9.message = s1 + " you changed your recovery questions";
            }
        }
        if (j == 653) {
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1) {
                    class9.message = "@whi@recommend you use a members world instead. You may use";
                } else { class9.message = ""; }
            } else if (daysSinceRecovChange == 200) {
                class9.message = "We strongly recommend you do so now to secure your account.";
            } else { class9.message = "If you do not remember making this change then cancel it immediately"; }
        }
        if (j == 654) {
            if (daysSinceRecovChange == 201) {
                if (membersInt == 1) {
                    class9.message = "@whi@this world but member benefits are unavailable whilst here.";
                    return;
                } else {
                    class9.message = "";
                    return;
                }
            }
            if (daysSinceRecovChange == 200) {
                class9.message = "Do this from the 'account management' area on our front webpage";
                return;
            }
            class9.message = "Do this from the 'account management' area on our front webpage";
        }
    }

    private void drawSplitPrivateChat() {
        if (splitPrivateChat == 0) { return; }
        TextDrawingArea textDrawingArea = aTextDrawingArea_1271;
        int i = 0;
        int y = isFullScreen ? extraHeight : 0;
        if (anInt1104 != 0) { i = 1; }
        for (int j = 0; j < 100; j++) {
            if (chatMessages[j] != null) {
                int k = chatTypes[j];
                String s = chatNames[j];
                byte byte1 = (byte) (chatRights[j]);
                if ((k == 3 || k == 7) && (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
                    int l = 329 - i * 13;
                    int k1 = 4;
                    textDrawingArea.method385(0, "From", l + y, k1);
                    textDrawingArea.method385(getChatColor(), "From", l - 1 + y, k1);
                    k1 += textDrawingArea.getTextWidth("From ");
                    if (byte1 > 0) {
                        modIcons[byte1].drawBackground(k1, l - 12 + y);
                        k1 += modIcons[byte1].anInt1452;
                    }
                    textDrawingArea.method385(0, s + ": " + chatMessages[j], l + y, k1);
                    textDrawingArea.method385(getChatColor(), s + ": " + chatMessages[j], l - 1 + y, k1);
                    if (++i >= 5) { return; }
                }
                if (k == 5 && privateChatMode < 2) {
                    int i1 = 329 - i * 13;
                    textDrawingArea.method385(0, chatMessages[j], i1 + y, 4);
                    textDrawingArea.method385(getChatColor(), chatMessages[j], i1 - 1 + y, 4);
                    if (++i >= 5) { return; }
                }
                if (k == 6 && privateChatMode < 2) {
                    int j1 = 329 - i * 13;
                    textDrawingArea.method385(0, "To " + s + ": " + chatMessages[j], j1 + y, 4);
                    textDrawingArea.method385(getChatColor(), "To " + s + ": " + chatMessages[j], j1 - 1 + y, 4);
                    if (++i >= 5) { return; }
                }
            }
        }

    }

    public void pushMessage(String s, int i, String s1, int i5) {
        if (i == 0 && dialogID != -1) {
            aString844 = s;
            super.clickMode3 = 0;
        }
        if (backDialogID == -1) { inputTaken = true; }
        for (int j = 499; j > 0; j--) {
            chatTypes[j] = chatTypes[j - 1];
            chatNames[j] = chatNames[j - 1];
            chatMessages[j] = chatMessages[j - 1];
            chatRights[j] = chatRights[j - 1];
        }
        chatTypes[0] = i;
        chatNames[0] = s1;
        chatMessages[0] = s;
        chatRights[0] = i5;
    }    public void cleanUpForQuit() {
        Signlink.reporterror = false;
        try {
            if (socketStream != null) { socketStream.close(); }
        } catch (Exception _ex) {
        }
        socketStream = null;
        stopMidi();
        if (mouseDetection != null) { mouseDetection.running = false; }
        mouseDetection = null;
        if (onDemandFetcher != null) { onDemandFetcher.disable(); }
        onDemandFetcher = null;
        aStream_834 = null;
        HPBarFull = null;
        x10Expand = null;
        x10ExpandOld = null;
        HPBarEmpty = null;
        newSideIcons = null;
        tabHover = null;
        tabClicked = null;
        stream = null;
        aStream_847 = null;
        inStream = null;
        anIntArray1234 = null;
        aByteArrayArray1183 = null;
        aByteArrayArray1247 = null;
        anIntArray1235 = null;
        anIntArray1236 = null;
        intGroundArray = null;
        loginScreenArea = null;
        byteGroundArray = null;
        worldController = null;
        aClass11Array1230 = null;
        anIntArrayArray901 = null;
        anIntArrayArray825 = null;
        bigX = null;
        bigY = null;
        aByteArray912 = null;
        aRSImageProducer_1163 = null;
        mapEdgeIP = null;
        leftFrame = null;
        topFrame = null;
        logIconH = null;
        chatToggle = null;
        logIconC = null;
        questionIconH = null;
        worldMapIcon = null;
        xpIcon = null;
        xpIconH = null;
        HPBarFull = null;
        HPBarEmpty = null;
        logoutIcon = null;
        questionIcon = null;
        emptyOrb = null;
        summIcon = null;
        orbFill = null;
        hoveredEmpty = null;
        runIcon1 = null;
        runIcon2 = null;
        runOrb1 = null;
        runOrb2 = null;
        hitPointsFill = null;
        hitPointsIcon = null;
        prayerFill = null;
        prayerIcon = null;
        rightFrame = null;
        aRSImageProducer_1164 = null;
        aRSImageProducer_1165 = null;
        aRSImageProducer_1166 = null;
        aRSImageProducer_1125 = null;
		/* Null pointers for custom sprites */
        alertBack = null;
        alertBorder = null;
        alertBorderH = null;
        chatArea = null;
        loadingPleaseWait = null;
        reestablish = null;
        chatButtons = null;
        tabArea = null;
        mapArea = null;
		/**/
        mapBack = null;
        sideIcons = null;
        redStones = null;
        compass = null;
        hitMarks = null;
        headIcons = null;
        skullIcons = null;
        headIconsHint = null;
        crosses = null;
        mapDotItem = null;
        mapDotNPC = null;
        mapDotPlayer = null;
        mapDotFriend = null;
        mapDotTeam = null;
        mapScenes = null;
        mapFunctions = null;
        anIntArrayArray929 = null;
        playerArray = null;
        playerIndices = null;
        anIntArray894 = null;
        aStreamArray895s = null;
        anIntArray840 = null;
        npcArray = null;
        npcIndices = null;
        groundArray = null;
        aClass19_1179 = null;
        aClass19_1013 = null;
        aClass19_1056 = null;
        menuActionCmd2 = null;
        menuActionCmd3 = null;
        menuActionCmd4 = null;
        menuActionID = null;
        menuActionCmd1 = null;
        menuActionName = null;
        variousSettings = null;
        anIntArray1072 = null;
        anIntArray1073 = null;
        aClass30_Sub2_Sub1_Sub1Array1140 = null;
        aClass30_Sub2_Sub1_Sub1_1263 = null;
        friendsList = null;
        friendsListAsLongs = null;
        friendsNodeIDs = null;
        aRSImageProducer_1110 = null;
        aRSImageProducer_1111 = null;
        aRSImageProducer_1107 = null;
        aRSImageProducer_1108 = null;
        aRSImageProducer_1109 = null;
        aRSImageProducer_1112 = null;
        aRSImageProducer_1113 = null;
        aRSImageProducer_1114 = null;
        aRSImageProducer_1115 = null;
        multiOverlay = null;
        multiOverlay2 = null;
        nullLoader();
        ObjectDef.nullLoader();
        EntityDef.nullLoader();
        ItemDef.nullLoader();
        Flo.cache = null;
        IdentityKit.cache = null;
        RSInterface.interfaceCache = null;
        Animation.anims = null;
        SpotAnim.cache = null;
        SpotAnim.aMRUNodes_415 = null;
        Varp.cache = null;
        super.fullGameScreen = null;
        Player.mruNodes = null;
        Texture.nullLoader();
        WorldController.nullLoader();
        Model.nullLoader();
        FrameReader.nullLoader();
        System.gc();
    }

    public int getXPForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    private void processTabClick() {
        int x = isFullScreen ? extraWidth : 0;
        int y = isFullScreen ? extraHeight + 262 : 0;

        int extraXX = drawLongTabs ? -232 : 0;
        int extraYY = drawLongTabs ? +35 : 0;

        int extraXX1 = drawLongTabs ? -241 : 0;
        int extraYY1 = drawLongTabs ? +35 : 0;

        int y2 = isFullScreen ? -262 : 0;
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            if (super.clickMode3 == 1) {
                if (super.saveClickX >= 524 + x + extraXX && super.saveClickX <= 561 + x + extraXX && super.saveClickY >= 169 + y + extraYY && super.saveClickY < 205 + y + extraYY && tabInterfaceIDs[0] != -1) {
                    needDrawTabArea = true;
                    tabID = 0;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 562 + x + extraXX && super.saveClickX <= 594 + x + extraXX && super.saveClickY >= 168 + y + extraYY && super.saveClickY < 205 + y + extraYY && tabInterfaceIDs[1] != -1) {
                    needDrawTabArea = true;
                    tabID = 1;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 595 + x + extraXX && super.saveClickX <= 626 + x + extraXX && super.saveClickY >= 168 + y + extraYY && super.saveClickY < 205 + y + extraYY && tabInterfaceIDs[2] != -1) {
                    needDrawTabArea = true;
                    tabID = 2;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 627 + x + extraXX && super.saveClickX <= 660 + x + extraXX && super.saveClickY >= 168 + y + extraYY && super.saveClickY < 203 + y + extraYY && tabInterfaceIDs[3] != -1) {
                    needDrawTabArea = true;
                    tabID = 3;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 661 + x + extraXX && super.saveClickX <= 693 + x + extraXX && super.saveClickY >= 168 + y + extraYY && super.saveClickY < 205 + y + extraYY && tabInterfaceIDs[4] != -1) {
                    needDrawTabArea = true;
                    tabID = 4;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 694 + x + extraXX && super.saveClickX <= 725 + x + extraXX && super.saveClickY >= 168 + y + extraYY && super.saveClickY < 205 + y + extraYY && tabInterfaceIDs[5] != -1) {
                    needDrawTabArea = true;
                    tabID = 5;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 726 + x + extraXX && super.saveClickX <= 765 + x + extraXX && super.saveClickY >= 169 + y + extraYY && super.saveClickY < 205 + y + extraYY && tabInterfaceIDs[6] != -1) {
                    needDrawTabArea = true;
                    tabID = 6;
                    tabAreaAltered = true;
                }
                if (isFullScreen) { y -= 262; }
                if (super.saveClickX >= 524 + x && super.saveClickX <= 561 + x && super.saveClickY >= 466 + y && super.saveClickY < 503 + y && tabInterfaceIDs[7] != -1) {
                    needDrawTabArea = true;
                    tabID = 7;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 562 + x && super.saveClickX <= 594 + x && super.saveClickY >= 466 + y && super.saveClickY < 503 + y && tabInterfaceIDs[8] != -1) {
                    needDrawTabArea = true;
                    tabID = 8;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 595 + x && super.saveClickX <= 627 + x && super.saveClickY >= 466 + y && super.saveClickY < 503 + y && tabInterfaceIDs[9] != -1) {
                    needDrawTabArea = true;
                    tabID = 9;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 627 + x && super.saveClickX <= 664 + x && super.saveClickY >= 466 + y && super.saveClickY < 503 + y && tabInterfaceIDs[10] != -1) {
                    needDrawTabArea = true;
                    tabID = 10;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 661 + x && super.saveClickX <= 694 + x && super.saveClickY >= 466 + y && super.saveClickY < 503 + y && tabInterfaceIDs[11] != -1) {
                    needDrawTabArea = true;
                    tabID = 11;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 695 + x && super.saveClickX <= 725 + x && super.saveClickY >= 466 + y && super.saveClickY < 503 + y && tabInterfaceIDs[12] != -1) {
                    needDrawTabArea = true;
                    tabID = 12;
                    tabAreaAltered = true;
                }
                if (super.saveClickX >= 726 + x && super.saveClickX <= 765 + x && super.saveClickY >= 466 + y && super.saveClickY < 502 + y && tabInterfaceIDs[13] != -1) {
                    needDrawTabArea = true;
                    tabID = 13;
                    tabAreaAltered = true;
                }
            }

        } else {
            if (super.mouseX >= 521 + x + extraXX1 && super.mouseX <= 550 + x + extraXX1 && super.mouseY >= 169 + y + extraYY1 && super.mouseY < 205 + y + extraYY1 && !drawLongTabs) {
                tabHPos = 0;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 552 + x + extraXX1 && super.mouseX <= 581 + x + extraXX1 && super.mouseY >= 168 + y + extraYY1 && super.mouseY < 205 + y + extraYY1) {
                tabHPos = 1;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 582 + x + extraXX1 && super.mouseX <= 611 + x + extraXX1 && super.mouseY >= 168 + y + extraYY1 && super.mouseY < 205 + y + extraYY1) {
                tabHPos = 2;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 612 + x + extraXX1 && super.mouseX <= 641 + x + extraXX1 && super.mouseY >= 168 + y + extraYY1 && super.mouseY < 203 + y + extraYY1) {
                tabHPos = 3;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 642 + x + extraXX1 && super.mouseX <= 671 + x + extraXX1 && super.mouseY >= 168 + y + extraYY1 && super.mouseY < 205 + y + extraYY1) {
                tabHPos = 4;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 672 + x + extraXX1 && super.mouseX <= 701 + x + extraXX1 && super.mouseY >= 168 + y + extraYY1 && super.mouseY < 205 + y + extraYY1) {
                tabHPos = 5;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 702 + x + extraXX1 && super.mouseX <= 731 + x + extraXX1 && super.mouseY >= 169 + y + extraYY1 && super.mouseY < 205 + y + extraYY1) {
                tabHPos = 6;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 732 + x + extraXX1 && super.mouseX <= 761 + x + extraXX1 && super.mouseY >= 169 + y + extraYY1 && super.mouseY < 205 + y + extraYY1) {
                tabHPos = 7;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 552 + x && super.mouseX <= 581 + x && super.mouseY >= 466 + y + y2 && super.mouseY < 503 + y + y2) {
                tabHPos = 8;
                needDrawTabArea = true;
                tabAreaAltered = true;

            } else if (super.mouseX >= 582 + x && super.mouseX <= 611 + x && super.mouseY >= 466 + y + y2 && super.mouseY < 503 + y + y2) {
                tabHPos = 9;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 612 + x && super.mouseX <= 641 + x && super.mouseY >= 466 + y + y2 && super.mouseY < 503 + y + y2) {
                tabHPos = 10;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 642 + x && super.mouseX <= 671 + x && super.mouseY >= 466 + y + y2 && super.mouseY < 503 + y + y2) {
                tabHPos = 11;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 672 + x && super.mouseX <= 701 + x && super.mouseY >= 466 + y + y2 && super.mouseY < 503 + y + y2) {
                tabHPos = 12;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 702 + x && super.mouseX <= 731 + x && super.mouseY >= 466 + y + y2 && super.mouseY < 502 + y + y2) {
                tabHPos = 13;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 732 + x && super.mouseX <= 761 + x && super.mouseY >= 466 + y + y2 && super.mouseY < 502 + y + y2 && !drawLongTabs) {
                tabHPos = 14;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else if (super.mouseX >= 522 + x && super.mouseX <= 551 + x && super.mouseY >= 466 + y + y2 && super.mouseY < 503 + y + y2) {
                tabHPos = 15;
                needDrawTabArea = true;
                tabAreaAltered = true;
            } else {
                tabHPos = -1;
                needDrawTabArea = true;
                tabAreaAltered = true;
            }

            if (super.clickMode3 == 1) {
                if (super.saveClickX >= 522 + x + extraXX1 && super.saveClickX <= 551 + x + extraXX1 && super.saveClickY >= 169 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[0] != -1 && !drawLongTabs) {
                    needDrawTabArea = true;
                    tabID = 0;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 552 + x + extraXX1 && super.saveClickX <= 581 + x + extraXX1 && super.saveClickY >= 168 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[1] != -1 && !drawLongTabs) {
                    needDrawTabArea = true;
                    tabID = 1;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 582 + x + extraXX1 && super.saveClickX <= 611 + x + extraXX1 && super.saveClickY >= 168 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[2] != -1 && !drawLongTabs) {
                    needDrawTabArea = true;
                    tabID = 2;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 552 + x + extraXX1 && super.saveClickX <= 581 + x + extraXX1 && super.saveClickY >= 168 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[0] != -1 && drawLongTabs) {
                    needDrawTabArea = true;
                    tabID = 0;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 612 + x + extraXX1 && super.saveClickX <= 641 + x + extraXX1 && super.saveClickY >= 168 + y + extraYY1 && super.saveClickY < 203 + y + extraYY1 && tabInterfaceIDs[2] != -1 && drawLongTabs) {
                    needDrawTabArea = true;
                    tabID = 2;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 582 + x + extraXX1 && super.saveClickX <= 611 + x + extraXX1 && super.saveClickY >= 168 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[1] != -1 && drawLongTabs) {
                    needDrawTabArea = true;
                    tabID = 1;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 612 + x + extraXX1 && super.saveClickX <= 641 + x + extraXX1 && super.saveClickY >= 168 + y + extraYY1 && super.saveClickY < 203 + y + extraYY1 && tabInterfaceIDs[2] != -1 && drawLongTabs) {
                    needDrawTabArea = true;
                    tabID = 2;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 642 + x + extraXX1 && super.saveClickX <= 671 + x + extraXX1 && super.saveClickY >= 168 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[3] != -1) {
                    needDrawTabArea = true;
                    tabID = 3;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 672 + x + extraXX1 && super.saveClickX <= 701 + x + extraXX1 && super.saveClickY >= 168 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[4] != -1) {
                    needDrawTabArea = true;
                    tabID = 4;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 702 + x + extraXX1 && super.saveClickX <= 731 + x + extraXX1 && super.saveClickY >= 169 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[5] != -1) {
                    needDrawTabArea = true;
                    tabID = 5;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 732 + x + extraXX1 && super.saveClickX <= 761 + x + extraXX1 && super.saveClickY >= 169 + y + extraYY1 && super.saveClickY < 205 + y + extraYY1 && tabInterfaceIDs[6] != -1) {
                    needDrawTabArea = true;
                    tabID = 6;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 522 + x + extraXX1 && super.saveClickX <= 551 + x + extraXX1 && super.saveClickY >= 466 + y + extraYY1 && super.saveClickY < 503 + y + extraYY1 && tabInterfaceIDs[16] != -1) {
                    needDrawTabArea = true;
                    tabID = 16;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 552 + x && super.saveClickX <= 581 + x && super.saveClickY >= 466 + y + y2 && super.saveClickY < 503 + y + y2 && tabInterfaceIDs[8] != -1) {
                    needDrawTabArea = true;
                    tabID = 8;
                    tabAreaAltered = true;

                } else if (super.saveClickX >= 582 + x && super.saveClickX <= 611 + x && super.saveClickY >= 466 + y + y2 && super.saveClickY < 503 + y + y2 && tabInterfaceIDs[9] != -1) {
                    needDrawTabArea = true;
                    tabID = 9;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 612 + x && super.saveClickX <= 641 + x && super.saveClickY >= 466 + y + y2 && super.saveClickY < 503 + y + y2 && tabInterfaceIDs[7] != -1) {
                    needDrawTabArea = true;
                    tabID = 7;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 642 + x && super.saveClickX <= 671 + x && super.saveClickY >= 466 + y + y2 && super.saveClickY < 503 + y + y2 && tabInterfaceIDs[11] != -1) {
                    needDrawTabArea = true;
                    tabID = 11;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 672 + x && super.saveClickX <= 701 + x && super.saveClickY >= 466 + y + y2 && super.saveClickY < 503 + y + y2 && tabInterfaceIDs[12] != -1) {
                    needDrawTabArea = true;
                    tabID = 12;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 702 + x && super.saveClickX <= 731 + x && super.saveClickY >= 466 + y + y2 && super.saveClickY < 502 + y + y2 && tabInterfaceIDs[13] != -1) {
                    needDrawTabArea = true;
                    tabID = 13;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 732 + x && super.saveClickX <= 761 + x && super.saveClickY >= 466 + y + y2 && super.saveClickY < 502 + y + y2 && tabInterfaceIDs[15] != -1) {
                    needDrawTabArea = true;
                    tabID = 15;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 612 + x && super.saveClickX <= 640 + x && super.saveClickY >= 168 + y + y2 && super.saveClickY < 202 + y + y2 && tabInterfaceIDs[14] != -1) {
                    needDrawTabArea = true;
                    tabID = 14;
                    tabAreaAltered = true;
                } else if (super.saveClickX >= 742 + x && super.saveClickX <= 764 + x && super.saveClickY >= 1 + y + y2 && super.saveClickY < 24 + y + y2 && tabInterfaceIDs[10] != -1) {
                    needDrawTabArea = true;
                    tabID = 10;
                    tabAreaAltered = true;
                }
				/* Logout X */
                else if (super.saveClickX >= 742 + x && super.saveClickX <= 764 + x && super.saveClickY >= 1 + y + y2 && super.saveClickY < 24 + y + y2 && tabInterfaceIDs[10] != -1) {
                    needDrawTabArea = true;
                    tabID = 10;
                    tabAreaAltered = true;
                }
            }
        }
    }    Component getGameComponent() {
        if (Signlink.mainapp != null) { return Signlink.mainapp; }
        return this;
    }

    private void resetImageProducers2() {
        if (aRSImageProducer_1166 != null) { return; }
        nullLoader();
        super.fullGameScreen = null;
        aRSImageProducer_1107 = null;
        aRSImageProducer_1108 = null;
        aRSImageProducer_1109 = null;
        aRSImageProducer_1110 = null;
        aRSImageProducer_1111 = null;
        aRSImageProducer_1112 = null;
        aRSImageProducer_1113 = null;
        aRSImageProducer_1114 = null;
        aRSImageProducer_1115 = null;
        aRSImageProducer_1166 = new RSImageProducer(519, 165);
        aRSImageProducer_1164 = new RSImageProducer(246, 168);
        DrawingArea.setAllPixelsToZero();
        // mapBack.drawBackground(0, 0);
        aRSImageProducer_1163 = new RSImageProducer(246, 335);
        if (isFullScreen) {
            aRSImageProducer_1165 = new RSImageProducer(clientWidth, clientHeight);
        } else { aRSImageProducer_1165 = new RSImageProducer(512, 334); }
        DrawingArea.setAllPixelsToZero();
        aRSImageProducer_1125 = new RSImageProducer(249, 45);
        welcomeScreenRaised = true;
    }

    public String getDocumentBaseHost() {
        if (Signlink.mainapp != null) {
            return Signlink.mainapp.getDocumentBase().getHost().toLowerCase();
        }
        return "";
    }    @Override
    public void tabReply() {
        String name = null;
        for (int j = 0; j < 100; j++) {
            if (chatMessages[j] != null) {
                int chatType = chatTypes[j];
                if (chatType == 3 || chatType == 7) {
                    name = chatNames[j];
                    break;
                }
            }
        }
        if (name != null && name.startsWith("@cr1@")) {
            name = name.substring(5);
            byte playerRights = 1;
        }
        if (name != null && name.startsWith("@cr2@")) {
            name = name.substring(5);
            byte playerRights = 2;
        }
        if (name == null) { pushMessage("You have not received any messages.", 0, ""); }
        try {
            if (name != null) {
                long nameLong = TextClass.longForName(name.trim());
                inputTaken = true;
                inputDialogState = 0;
                messagePromptRaised = true;
                promptInput = "";
                friendsListAction = 3;
                aLong953 = nameLong;
                aString1121 = "Enter message to send to " + name;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void method81(Sprite sprite, int j, int k) {
        int l = k * k + j * j;
        if (l > 4225 && l < 0x15f90) {
            int i1 = minimapInt1 + minimapInt2 & 0x7ff;
            int j1 = Model.SINE[i1];
            int k1 = Model.COSINE[i1];
            j1 = (j1 * 256) / (minimapInt3 + 256);
            k1 = (k1 * 256) / (minimapInt3 + 256);
            int l1 = j * j1 + k * k1 >> 16;
            int i2 = j * k1 - k * j1 >> 16;
            double d = Math.atan2(l1, i2);
            int j2 = (int) (Math.sin(d) * 63D);
            int k2 = (int) (Math.cos(d) * 57D);
            mapEdge.method353(83 - k2 - 20, d, (94 + j2 + 4) - 10);
        } else {
            markMinimap(sprite, k, j);
        }
    }

    private void rightClickChatButtons() {
        int y = extraHeight;

        if (displayChat) {
            if (super.mouseX >= 5 && super.mouseX <= 61 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                menuActionName[1] = "View All";
                menuActionID[1] = 999;
                menuActionRow = 2;
            } else if (super.mouseX >= 71 && super.mouseX <= 127 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                menuActionName[1] = "View Game";
                menuActionID[1] = 998;
                menuActionRow = 2;
            } else if (super.mouseX >= 137 && super.mouseX <= 193 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                menuActionName[1] = "Hide public";
                menuActionID[1] = 997;
                menuActionName[2] = "Off public";
                menuActionID[2] = 996;
                menuActionName[3] = "Friends public";
                menuActionID[3] = 995;
                menuActionName[4] = "On public";
                menuActionID[4] = 994;
                menuActionName[5] = "View public";
                menuActionID[5] = 993;
                menuActionRow = 6;
            } else if (super.mouseX >= 203 && super.mouseX <= 259 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                menuActionName[1] = "Off private";
                menuActionID[1] = 992;
                menuActionName[2] = "Friends private";
                menuActionID[2] = 991;
                menuActionName[3] = "On private";
                menuActionID[3] = 990;
                menuActionName[4] = "View private";
                menuActionID[4] = 989;
                menuActionRow = 5;
            } else if (super.mouseX >= 269 && super.mouseX <= 325 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                menuActionName[1] = "Off clan chat";
                menuActionID[1] = 1003;
                menuActionName[2] = "Friends clan chat";
                menuActionID[2] = 1002;
                menuActionName[3] = "On clan chat";
                menuActionID[3] = 1001;
                menuActionName[4] = "View clan chat";
                menuActionID[4] = 1000;
                menuActionRow = 5;
            } else if (super.mouseX >= 335 && super.mouseX <= 391 && super.mouseY >= 482 + y && super.mouseY <= 503 + y) {
                menuActionName[1] = "Off trade";
                menuActionID[1] = 987;
                menuActionName[2] = "Friends trade";
                menuActionID[2] = 986;
                menuActionName[3] = "On trade";
                menuActionID[3] = 985;
                menuActionName[4] = "View trade";
                menuActionID[4] = 984;
                menuActionRow = 5;
            }
        }
    }

    public boolean canClickScreen() {
        if (super.mouseX > 0 && super.mouseY > clientHeight - 165 && super.mouseX < 519 && super.mouseY < clientHeight && displayChat || super.mouseX > clientWidth - 246 && super.mouseY > clientHeight - 335 && super.mouseX < clientWidth && super.mouseY < clientHeight || super.mouseX > clientWidth - 220 && super.mouseY > 0 && super.mouseX < clientWidth && super.mouseY < 164 || (super.mouseX > 247 && super.mouseX < 260 && super.mouseY > clientHeight - 173 && super.mouseY < clientHeight - 166 || super.mouseY > clientHeight - 15) || super.mouseX > clientWidth - 462 && super.mouseY > clientHeight - 36 && super.mouseX < clientWidth && super.mouseY < clientHeight) {
            return false;
        } else { return true; }
    }

    public void processRightClick() {
        int x = isFullScreen ? extraWidth + 13 : 0;
        int y = isFullScreen ? extraHeight - 40 : 0;
        if (activeInterfaceType != 0) {
            return;
        }
        menuActionName[0] = "Cancel";
        menuActionID[0] = 1107;
        menuActionRow = 1;
        if (fullscreenInterfaceID != -1) {
            anInt886 = 0;
            anInt1315 = 0;
            buildInterfaceMenu(8, RSInterface.interfaceCache[fullscreenInterfaceID], super.mouseX, 8, super.mouseY, 0);
            if (anInt886 != anInt1026) {
                anInt1026 = anInt886;
            }
            if (anInt1315 != anInt1129) {
                anInt1129 = anInt1315;
            }
            return;
        }
        buildSplitPrivateChatMenu();
        anInt886 = 0;
        anInt1315 = 0;
        if (!isFullScreen) {
            if (super.mouseX > 0 && super.mouseY > 0 && super.mouseX < 516 && super.mouseY < 338) {
                if (openInterfaceID != -1) {
                    buildInterfaceMenu(4, RSInterface.interfaceCache[openInterfaceID], super.mouseX, 4, super.mouseY, 0);
                } else { build3dScreenMenu(); }
            }
        }
        if (isFullScreen) {
            if (canClickScreen()) {
                if (super.mouseX > (clientWidth / 2) - 256 && super.mouseY > (clientHeight / 2) - 167 && super.mouseX < ((clientWidth / 2) + 256) && super.mouseY < (clientHeight / 2) + 167 && openInterfaceID != -1) {
                    buildInterfaceMenu((clientWidth / 2) - 256, RSInterface.interfaceCache[openInterfaceID], super.mouseX, (clientHeight / 2) - 167, super.mouseY, 0);
                } else { build3dScreenMenu(); }
            }
        }
        if (anInt886 != anInt1026) {
            anInt1026 = anInt886;
        }
        if (anInt1315 != anInt1129) {
            anInt1129 = anInt1315;
        }
        anInt886 = 0;
        anInt1315 = 0;
        if (super.mouseX > 560 + extraWidth && super.mouseY > 165 + extraHeight && super.mouseX < 752 + extraWidth && super.mouseY < 426 + extraHeight && isFullScreen && !drawLongTabs) {
            if (invOverlayInterfaceID != -1 && isFullScreen) {
                buildInterfaceMenu(560 + extraWidth, RSInterface.interfaceCache[invOverlayInterfaceID], super.mouseX, 165 + extraHeight, super.mouseY, 0);
            } else if (tabInterfaceIDs[tabID] != -1 && isFullScreen) {
                buildInterfaceMenu(560 + extraWidth, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], super.mouseX, 165 + extraHeight, super.mouseY, 0);
            }
        }
        if (super.mouseX > 560 + extraWidth && super.mouseY > 165 + extraHeight && super.mouseX < 752 + 35 + extraWidth && super.mouseY < 426 + 35 + extraHeight && isFullScreen && drawLongTabs && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            if (invOverlayInterfaceID != -1 && isFullScreen && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
                buildInterfaceMenu(560 + extraWidth, RSInterface.interfaceCache[invOverlayInterfaceID], super.mouseX, 165 + 35 + extraHeight, super.mouseY, 0);
            } else if (tabInterfaceIDs[tabID] != -1 && isFullScreen && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
                buildInterfaceMenu(560 + extraWidth, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], super.mouseX, 165 + 35 + extraHeight, super.mouseY, 0);
            }
        }
        if (super.mouseX > 560 + extraWidth && super.mouseY > 165 + extraHeight && super.mouseX < 752 + 35 + extraWidth && super.mouseY < 426 + 35 - 6 + extraHeight && isFullScreen && drawLongTabs && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
            if (invOverlayInterfaceID != -1 && isFullScreen && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
                buildInterfaceMenu(560 + extraWidth, RSInterface.interfaceCache[invOverlayInterfaceID], super.mouseX, 165 + 35 - 6 + extraHeight, super.mouseY, 0);
            } else if (tabInterfaceIDs[tabID] != -1 && isFullScreen && Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
                buildInterfaceMenu(560 + extraWidth, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], super.mouseX, 165 + 35 - 6 + extraHeight, super.mouseY, 0);
            }
        }
        if (super.mouseX > 548 && super.mouseY > 207 && super.mouseX < 740 && super.mouseY < 468 && !isFullScreen) {
            if (invOverlayInterfaceID != -1 && !isFullScreen) {
                buildInterfaceMenu(548, RSInterface.interfaceCache[invOverlayInterfaceID], super.mouseX, 207, super.mouseY, 0);
            } else if (tabInterfaceIDs[tabID] != -1 && !isFullScreen) {
                buildInterfaceMenu(548, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], super.mouseX, 207, super.mouseY, 0);
            }
        }
        if (anInt886 != anInt1048) {
            needDrawTabArea = true;
            tabAreaAltered = true;
            anInt1048 = anInt886;
        }
        if (anInt1315 != anInt1044) {
            needDrawTabArea = true;
            tabAreaAltered = true;
            anInt1044 = anInt1315;
        }
        anInt886 = 0;
        anInt1315 = 0;
        if (super.mouseX > 0 && super.mouseY > 334 + extraHeight && super.mouseX < 490 && super.mouseY < 459 + extraHeight) {
            if (backDialogID != -1) {
                buildInterfaceMenu(20, RSInterface.interfaceCache[backDialogID], super.mouseX, 354 + extraHeight, super.mouseY, 0);
            } else if (super.mouseY < 459 + extraHeight && super.mouseX < 490) {
                buildChatAreaMenu(super.mouseY - (334 + extraHeight), super.mouseX);
            }
        }
        if (backDialogID != -1 && anInt886 != anInt1039) {
            inputTaken = true;
            anInt1039 = anInt886;
        }
        if (super.mouseX >= 515 + extraWidth && super.mouseY >= 0 + extraHeight && super.mouseX <= 765 + extraWidth && super.mouseY <= 167 + extraHeight) {
            if (openInterfaceID != -1) {
                buildInterfaceMenu(0, RSInterface.interfaceCache[openInterfaceID], super.mouseX, 0, super.mouseY, 0);
            }
        }
        if (anInt886 != anInt1026) { anInt1026 = anInt886; }
        anInt886 = 0;
		/* Enable custom right click areas */
        rightClickMapArea();
        if (super.mouseX > 4 && super.mouseY > clientHeight - 23 && super.mouseX < 516 && super.mouseY < clientHeight) {
            rightClickChatButtons();
        }
        alertHandler.processMouse(super.mouseX, super.mouseY);
		/**/
        boolean flag = false;
        while (!flag) {
            flag = true;
            for (int j = 0; j < menuActionRow - 1; j++) {
                if (menuActionID[j] < 1000 && menuActionID[j + 1] > 1000) {
                    String s = menuActionName[j];
                    menuActionName[j] = menuActionName[j + 1];
                    menuActionName[j + 1] = s;
                    int k = menuActionID[j];
                    menuActionID[j] = menuActionID[j + 1];
                    menuActionID[j + 1] = k;
                    k = menuActionCmd2[j];
                    menuActionCmd2[j] = menuActionCmd2[j + 1];
                    menuActionCmd2[j + 1] = k;
                    k = menuActionCmd3[j];
                    menuActionCmd3[j] = menuActionCmd3[j + 1];
                    menuActionCmd3[j + 1] = k;
                    k = menuActionCmd1[j];
                    menuActionCmd1[j] = menuActionCmd1[j + 1];
                    menuActionCmd1[j + 1] = k;
                    k = menuActionCmd4[j];
                    menuActionCmd4[j] = menuActionCmd4[j + 1];
                    menuActionCmd4[j + 1] = k;
                    flag = false;
                }
            }
        }
    }

    private int method83(int i, int j, int k) {
        int l = 256 - k;
        return ((i & 0xff00ff) * l + (j & 0xff00ff) * k & 0xff00ff00) + ((i & 0xff00) * l + (j & 0xff00) * k & 0xff0000) >> 8;
    }

    private void login(boolean reconnecting) {
        Signlink.errorname = Configuration.getInstance().getValue(Username.class);
        try {
            if (!reconnecting) {
                loginMessage2 = "";
                loginMessage1 = "Connecting to server...";
                drawLoginScreen();
            }
            if (ItemDef_5.NUMBERS == null) {
                loginMessage2 = "";
                loginMessage1 = "Something went wrong!";
                drawLoginScreen();
                return;
            }
            if (System.currentTimeMillis() < lastLoginTry) {
                if (logintries >= 10) {
                    loginMessage1 = "Please wait a couple of seconds";
                    loginMessage2 = "before trying again";
                    drawLoginScreen();
                    return;
                }
            } else {
                lastLoginTry = System.currentTimeMillis() + 15000;
                logintries = 0;
            }
            socketStream = new RSSocket(this, openSocket(Constants.SERVER_TYPE.getServer().getPort()));
            long l = TextClass.longForName(Configuration.getInstance().getValue(Username.class));
            int i = (int) (l >> 16 & 31L);
            stream.currentOffset = 0;
            stream.writeByte(14); //Sometimes here bugs
            stream.writeByte(i);
            socketStream.queueBytes(2, stream.payload);
            for (int j = 0; j < 8; j++) { socketStream.read(); }

            int k = socketStream.read();
            int i1 = k;
            if (k == 0) {
                socketStream.flushInputStream(inStream.payload, 8);
                inStream.currentOffset = 0;
                aLong1215 = inStream.readLong(); //Bug occurs here
                int ai[] = new int[]{(int) (Math.random() * 99999999D), (int) (Math.random() * 99999999D), (int) (aLong1215 >> 32), (int) aLong1215};
                stream.currentOffset = 0;
                stream.writeByte(10);
                for (int array : ai) { stream.writeInt(array); }
                stream.writeInt(Constants.SERVER_TYPE.getServer().getVersion());
                stream.writeInt(macAdd);
                for (final int n : ItemDef_5.NUMBERS) { stream.writeInt(n); }
                stream.writeString(Configuration.getInstance().getValue(Username.class));
                stream.writeString(Configuration.getInstance().getValue(Password.class));
                stream.writeInt(Configuration.getInstance().getValue(Authentication.class).isEmpty() ? -1 : Integer.parseInt(Configuration.getInstance().getValue(Authentication.class)));
                stream.writeInt(saveIp ? 1 : 0);
                stream.doKeys();
                aStream_847.currentOffset = 0;
                aStream_847.writeByte(reconnecting ? 18 : 16);
                aStream_847.writeByte(stream.currentOffset + 40);
                aStream_847.writeByte(0x7a); //CHANGE 122
                aStream_847.writeShort(0x1ea); //CHANGE 490
                aStream_847.writeByte(5); //CHANGE 5
                for (int l1 = 0; l1 < 9; l1++) { aStream_847.writeInt(expectedCRCs[l1]); }
                aStream_847.writeBytes(stream.payload, stream.currentOffset, 0);
                stream.encryption = new ISAACRandomGen(ai);
                for (int j2 = 0; j2 < 4; j2++) { ai[j2] += 50; }
                encryption = new ISAACRandomGen(ai);
                socketStream.queueBytes(aStream_847.currentOffset, aStream_847.payload);
                for (int index = 0; index < chatMessages.length; index++) {
                    chatMessages[index] = null;
                    chatNames[index] = null;
                    chatRights[index] = 0;
                }
                k = socketStream.read();
                System.out.println(k);
            }
            if (k == 1) {
                try {
                    Thread.sleep(2000L);
                } catch (Exception _ex) {
                    _ex.printStackTrace();
                }
                login(reconnecting);
                return;
            }
            if (k == 2) {
                tries = 0;
                myPrivilege = socketStream.read();
                flagged = socketStream.read() == 1;

                anInt1022 = 0;
                mouseDetection.coordsIndex = 0;
                super.awtFocus = true;
                aBoolean954 = true;
                loggedIn = true;
                stream.currentOffset = 0;
                inStream.currentOffset = 0;
                pktType = -1;
                anInt841 = -1;
                anInt842 = -1;
                anInt843 = -1;
                pktSize = 0;
                anInt1009 = 0;
                anInt1104 = 0;
                anInt1011 = 0;
                anInt855 = 0;
                menuActionRow = 0;
                menuOpen = false;
                super.idleTime = 0;
                for (int j1 = 0; j1 < chatMessages.length; j1++) { chatMessages[j1] = null; }

                itemSelected = 0;
                spellSelected = 0;
                loadingStage = 0;
                anInt1062 = 0;
                anInt1278 = (int) (Math.random() * 100D) - 50;
                anInt1131 = (int) (Math.random() * 110D) - 55;
                anInt896 = (int) (Math.random() * 80D) - 40;
                minimapInt2 = (int) (Math.random() * 120D) - 60;
                minimapInt3 = (int) (Math.random() * 30D) - 20;
                minimapInt1 = (int) (Math.random() * 20D) - 10 & 0x7ff;
                anInt1021 = 0;
                anInt985 = -1;
                destX = 0;
                destY = 0;
                playerCount = 0;
                npcCount = 0;
                for (int i2 = 0; i2 < maxPlayers; i2++) {
                    playerArray[i2] = null;
                    aStreamArray895s[i2] = null;
                }

                for (int k2 = 0; k2 < 16384; k2++) { npcArray[k2] = null; }

                myPlayer = playerArray[myPlayerIndex] = new Player();
                aClass19_1013.removeAll();
                aClass19_1056.removeAll();
                for (int l2 = 0; l2 < 4; l2++) {
                    for (int i3 = 0; i3 < 104; i3++) {
                        for (int k3 = 0; k3 < 104; k3++) { groundArray[l2][i3][k3] = null; }

                    }

                }

                aClass19_1179 = new NodeList();
                fullscreenInterfaceID = -1;
                anInt900 = 0;
                friendsCount = 0;
                dialogID = -1;
                backDialogID = -1;
                openInterfaceID = -1;
                invOverlayInterfaceID = -1;
                anInt1018 = -1;
                aBoolean1149 = false;
                tabID = 3;
                inputDialogState = 0;
                menuOpen = false;
                searchBank = false;
                messagePromptRaised = false;
                aString844 = null;
                anInt1055 = 0;
                anInt1054 = -1;
                aBoolean1047 = true;
                method45();
                for (int j3 = 0; j3 < 5; j3++) { anIntArray990[j3] = 0; }

                for (int l3 = 0; l3 < 6; l3++) {
                    atPlayerActions[l3] = null;
                    atPlayerArray[l3] = false;
                }
                anInt1175 = 0;
                anInt1134 = 0;
                anInt986 = 0;
                anInt1288 = 0;
                anInt924 = 0;
                anInt1188 = 0;
                anInt1155 = 0;
                anInt1226 = 0;
                publicChatMode = 0;
                privateChatMode = 0;
                tradeMode = 0;
                int anInt941 = 0;
                int anInt1260 = 0;
                resetImageProducers2();
                setDefaultCursor();
                // resize(clientWidth, clientHeight);
                sendFrame126(Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) ? "474" : Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554) ? "600+" : "500+", 37058);
                sendFrame126Toggle(Configuration.getInstance().getValue(HPBar.class), 37061);
                sendFrame126Toggle(Configuration.getInstance().getValue(HDTexture.class), 37065);
                sendFrame126Toggle(Configuration.getInstance().getValue(Damage.class), 37059);
                sendFrame126Toggle(Configuration.getInstance().getValue(MenuBar.class), 37062);
                sendFrame126Toggle(Configuration.getInstance().getValue(HeadName.class), 37063);
                sendFrame126Toggle(Configuration.getInstance().getValue(Hitmarks.class), 37060);
                sendFrame126Toggle(Configuration.getInstance().getValue(HeadConstitution.class), 37064);
                System.out.println("Passed through k = 2");
            } else {
                logintries++;
            }
            if (k == 3) {
                loginMessage1 = "";
                loginMessage2 = "Invalid username or password.";
                return;
            }
            if (k == 4) {
                inStream.currentOffset = 0;
                socketStream.flushInputStream(inStream.payload, socketStream.available());

                final String punishment_target = inStream.readString();
                final String issuer = inStream.readString();
                final String reason = inStream.readString();
                final String expiration = inStream.readString();
                top_Message = "You have been banned by: " + issuer;
                top_Message2 = "\"" + reason + "\"";
                loginMessage1 = "Exp: " + expiration;
                loginMessage2 = "You can appeal this on the forums";
                return;
            }

            if (k == 22) {
                loginMessage1 = "The authenticator code you entered";
                loginMessage2 = "for this account is incorrect.";
                return;
            }
            if (k == 23) {
                loginMessage1 = "Please wait for a new authenticator code";
                loginMessage2 = "for this account to log in again.";
                return;
            }
            if (k == 5) {
                loginMessage1 = "Your account is already logged in.";
                loginMessage2 = "Try again in 60 secs...";
                return;
            }
            if (k == 6) {
                loginMessage1 = Constants.getServerName() + " has been updated!";
                loginMessage2 = "Please restart the Client";
                return;
            }
            if (k == 7) {
                loginMessage1 = "This world is full.";
                loginMessage2 = "Please use a different world.";
                return;
            }
            if (k == 8) {
                loginMessage1 = "Unable to connect.";
                loginMessage2 = "Login server offline.";
                return;
            }
            if (k == 9) {
                loginMessage1 = "Login limit exceeded.";
                loginMessage2 = "Too many connections from your address.";
                return;
            }
            if (k == 10) {
                loginMessage1 = "Unable to connect.";
                loginMessage2 = "Bad session id.";
                return;
            }
            if (k == 11) {
                loginMessage1 = "Login request too fast from previous.";
                loginMessage2 = "Please try again.";
                drawLoginScreen();
                return;
            }
            if (k == 12) {
                top_Message = "Login from a previous location to";
                top_Message2 = "unlock your account again.";
                loginMessage1 = "You can appeal this";
                loginMessage2 = "by making a support ticket.";
                return;
            }
            if (k == 13) {
                loginMessage1 = "Could not complete login.";
                loginMessage2 = "Please try using a different world.";
                return;
            }
            if (k == 14) {
                loginMessage1 = "The server is being updated.";
                loginMessage2 = "Please wait 1 minute and try again.";
                return;
            }
            if (k == 15) {
                loggedIn = true;
                stream.currentOffset = 0;
                inStream.currentOffset = 0;
                pktType = -1;
                anInt841 = -1;
                anInt842 = -1;
                anInt843 = -1;
                pktSize = 0;
                anInt1009 = 0;
                anInt1104 = 0;
                menuActionRow = 0;
                menuOpen = false;
                aLong824 = System.currentTimeMillis();
                return;
            }
            if (k == 16) {
                loginMessage1 = "Login attempts exceeded.";
                loginMessage2 = "Please wait 1 minute and try again.";
                return;
            }
            if (k == 17) {
                loginMessage1 = "You are standing in a members-only area.";
                loginMessage2 = "To play on this world move to a free area first";
                return;
            }
            if (k == 20) {
                loginMessage1 = "Invalid loginserver requested";
                loginMessage2 = "Please try using a different world.";
                return;
            }
            if (k == 21) {
                for (int k1 = socketStream.read(); k1 >= 0; k1--) {
                    loginMessage1 = "You have only just left another world";
                    loginMessage2 = "Your profile will be transferred in: " + k1 + " seconds";
                    drawLoginScreen();
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception _ex) {
                    }
                }

                login(reconnecting);
                return;
            }
            if (k == -1) {
                if (i1 == 0) {
                    if (loginFailures < 2) {
                        try {
                            Thread.sleep(2000L);
                        } catch (Exception _ex) {
                        }
                        loginFailures++;
                        login(reconnecting);
                        return;
                    } else {
                        loginMessage1 = "No response from loginserver";
                        loginMessage2 = "Please wait 1 minute and try again.";
                        return;
                    }
                } else {
                    loginMessage1 = "No response from server";
                    loginMessage2 = "Please try using a different world.";
                    return;
                }
            } else {
                System.out.println("response:" + k);
                loginMessage1 = "Unexpected server response";
                loginMessage2 = "Please try using a different world.";
                return;
            }
        } catch (IOException ex) {
            Logger.log(Client.class, Level.WARNING, "VerifyConnection rejected", ex);
            String error = "";
            if (tries++ < 20) {
                if (tries > 1) {
                    top_Message = "Please let it retry a few times!";
                    top_Message2 = "If this problem persists, post on the forums";
                    for (int i = tries; i > 0; i--) {
                        loginMessage1 = "VerifyConnection rejected";
                        loginMessage2 = "Trying again in: " + i + " seconds";
                        drawLoginScreen();
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                    }
                }
                login(reconnecting);
            }
            for (StackTraceElement s : ex.getStackTrace()) { error += s; }
            //logError(error, username);
        }
        loginMessage1 = "Error connecting to server.";
        loginMessage2 = "";
    }

    private boolean doWalkTo(int i, int j, int k, int i1, int j1, int k1, int l1, int i2, int j2, boolean flag, int k2) {
        byte byte0 = 104;
        byte byte1 = 104;
        try {
            for (int l2 = 0; l2 < byte0; l2++) {
                for (int i3 = 0; i3 < byte1; i3++) {
                    anIntArrayArray901[l2][i3] = 0;
                    anIntArrayArray825[l2][i3] = 0x5f5e0ff;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int j3 = j2;
        int k3 = j1;
        try {
            anIntArrayArray901[j2][j1] = 99;
            anIntArrayArray825[j2][j1] = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        int l3 = 0;
        int i4 = 0;
        bigX[l3] = j2;
        bigY[l3++] = j1;
        boolean flag1 = false;
        int j4 = bigX.length;
        int ai[][] = aClass11Array1230[plane].anIntArrayArray294;
        while (i4 != l3) {
            j3 = bigX[i4];
            k3 = bigY[i4];
            i4 = (i4 + 1) % j4;
            if (j3 == k2 && k3 == i2) {
                flag1 = true;
                break;
            }
            if (i1 != 0) {
                if ((i1 < 5 || i1 == 10) && aClass11Array1230[plane].method219(k2, j3, k3, j, i1 - 1, i2)) {
                    flag1 = true;
                    break;
                }
                if (i1 < 10 && aClass11Array1230[plane].method220(k2, i2, k3, i1 - 1, j, j3)) {
                    flag1 = true;
                    break;
                }
            }
            if (k1 != 0 && k != 0 && aClass11Array1230[plane].method221(i2, k2, j3, k, l1, k1, k3)) {
                flag1 = true;
                break;
            }
            if (j3 < 0 || k3 < 0) { return false; }
            int l4 = anIntArrayArray825[j3][k3] + 1;
            if (j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3] = 2;
                anIntArrayArray825[j3 - 1][k3] = l4;
            }
            if (j3 < byte0 - 1 && anIntArrayArray901[j3 + 1][k3] == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3] = 8;
                anIntArrayArray825[j3 + 1][k3] = l4;
            }
            if (k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3][k3 - 1] = 1;
                anIntArrayArray825[j3][k3 - 1] = l4;
            }
            if (k3 < byte1 - 1 && anIntArrayArray901[j3][k3 + 1] == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3][k3 + 1] = 4;
                anIntArrayArray825[j3][k3 + 1] = l4;
            }
            if (j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0 && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3 - 1] = 3;
                anIntArrayArray825[j3 - 1][k3 - 1] = l4;
            }
            if (j3 < byte0 - 1 && k3 > 0 && anIntArrayArray901[j3 + 1][k3 - 1] == 0 && (ai[j3 + 1][k3 - 1] & 0x1280183) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3 - 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3 - 1] = 9;
                anIntArrayArray825[j3 + 1][k3 - 1] = l4;
            }
            if (j3 > 0 && k3 < byte1 - 1 && anIntArrayArray901[j3 - 1][k3 + 1] == 0 && (ai[j3 - 1][k3 + 1] & 0x1280138) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3 - 1;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 - 1][k3 + 1] = 6;
                anIntArrayArray825[j3 - 1][k3 + 1] = l4;
            }
            if (j3 < byte0 - 1 && k3 < byte1 - 1 && anIntArrayArray901[j3 + 1][k3 + 1] == 0 && (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
                bigX[l3] = j3 + 1;
                bigY[l3] = k3 + 1;
                l3 = (l3 + 1) % j4;
                anIntArrayArray901[j3 + 1][k3 + 1] = 12;
                anIntArrayArray825[j3 + 1][k3 + 1] = l4;
            }
        }
        anInt1264 = 0;
        if (!flag1) {
            if (flag) {
                int i5 = 100;
                for (int k5 = 1; k5 < 2; k5++) {
                    for (int i6 = k2 - k5; i6 <= k2 + k5; i6++) {
                        for (int l6 = i2 - k5; l6 <= i2 + k5; l6++) {
                            if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && anIntArrayArray825[i6][l6] < i5) {
                                i5 = anIntArrayArray825[i6][l6];
                                j3 = i6;
                                k3 = l6;
                                anInt1264 = 1;
                                flag1 = true;
                            }
                        }

                    }

                    if (flag1) { break; }
                }

            }
            if (!flag1) { return false; }
        }
        i4 = 0;
        bigX[i4] = j3;
        bigY[i4++] = k3;
        int l5;
        for (int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != j2 || k3 != j1; j5 = anIntArrayArray901[j3][k3]) {
            if (j5 != l5) {
                l5 = j5;
                bigX[i4] = j3;
                bigY[i4++] = k3;
            }
            if ((j5 & 2) != 0) { j3++; } else if ((j5 & 8) != 0) { j3--; }
            if ((j5 & 1) != 0) { k3++; } else if ((j5 & 4) != 0) { k3--; }
        }
        // if(cancelWalk) { return i4 > 0; }

        if (i4 > 0) {
            int k4 = i4;
            if (k4 > 25) { k4 = 25; }
            i4--;
            int k6 = bigX[i4];
            int i7 = bigY[i4];
            anInt1288 += k4;
            if (anInt1288 >= 92) {
                stream.createFrame(36);
                stream.writeInt(0);
                anInt1288 = 0;
            }
            final byte fdsf = (byte) 78;
            if (i == 0) {
                stream.createFrame(164);
                //stream.writeByte(fdsf);
                stream.writeByte(k4 + k4 + 3);
            }
            if (i == 1) {
                stream.createFrame(248);
                //stream.writeByte(fdsf);
                stream.writeByte(k4 + k4 + 3 + 14);
            }
            if (i == 2) {
                stream.createFrame(98);
                //stream.writeByte(fdsf);
                stream.writeByte(k4 + k4 + 3);
            }
            stream.writeLEShortA(k6 + baseX);
            destX = bigX[0];
            destY = bigY[0];
            for (int j7 = 1; j7 < k4; j7++) {
                i4--;
                stream.writeByte(bigX[i4] - k6);
                stream.writeByte(bigY[i4] - i7);
            }

            stream._writeLEShort(i7 + baseY);
            stream._writeByteC(super.keyArray[5] != 1 ? 0 : 1);
            return true;
        }
        return i != 1;
    }

    private void method86(Stream stream) {
        for (int j = 0; j < anInt893; j++) {
            int k = anIntArray894[j];
            NPC npc = npcArray[k];
            int l = stream.readUByte();
            if ((l & 0x10) != 0) {
                int i1 = stream.readLEShort();
                if (i1 == 65535) { i1 = -1; }
                int i2 = stream.readUByte();
                if (i1 == npc.anim && i1 != -1) {
                    int l2 = Animation.anims[i1].anInt365;
                    if (l2 == 1) {
                        npc.anInt1527 = 0;
                        npc.anInt1528 = 0;
                        npc.anInt1529 = i2;
                        npc.anInt1530 = 0;
                    }
                    if (l2 == 2) { npc.anInt1530 = 0; }
                } else if (i1 == -1 || npc.anim == -1 || Animation.anims[i1].anInt359 >= Animation.anims[npc.anim].anInt359) {
                    npc.anim = i1;
                    npc.anInt1527 = 0;
                    npc.anInt1528 = 0;
                    npc.anInt1529 = i2;
                    npc.anInt1530 = 0;
                    npc.anInt1542 = npc.smallXYIndex;
                }
            }
            if ((l & 8) != 0) {
                int j1 = stream._readShortA();
                int j2 = stream._readByteC();
                int b = stream.readUByte();
                System.out.println("Here: " + j1 + "-" + j2);
                npc.updateHitData(j2, j1, b, loopCycle);
                npc.loopCycleStatus = loopCycle + 300;
                npc.currentHealth = stream.readByteA();
                npc.maxHealth = stream.readUByte();
            }
            if ((l & 0x80) != 0) {
                npc.anInt1520 = stream.readUShort();
                int k1 = stream.readInt();
                npc.anInt1524 = k1 >> 16;
                npc.anInt1523 = loopCycle + (k1 & 0xffff);
                npc.anInt1521 = 0;
                npc.anInt1522 = 0;
                if (npc.anInt1523 > loopCycle) { npc.anInt1521 = -1; }
                if (npc.anInt1520 == 65535) { npc.anInt1520 = -1; }
            }
            if ((l & 0x20) != 0) {
                npc.interactingEntity = stream.readUShort();
                if (npc.interactingEntity == 65535) { npc.interactingEntity = -1; }
            }
            if ((l & 1) != 0) {
                npc.textSpoken = stream.readString();
                npc.textCycle = 100;
                //	entityMessage(npc);

            }
            if ((l & 0x40) != 0) {
                int l1 = stream._readShortA();
                int k2 = stream._readByteS();
                int b = stream.readUByte();
                npc.updateHitData(k2, l1, b, loopCycle);
                npc.loopCycleStatus = loopCycle + 300;
                npc.currentHealth = stream._readByteS();
                npc.maxHealth = stream._readByteC();
            }
            if ((l & 2) != 0) {
                npc.desc = EntityDef.forID(stream.readLEShortA());
                npc.anInt1540 = npc.desc.aByte68;
                npc.anInt1504 = npc.desc.anInt79;
                npc.anInt1554 = npc.desc.walkAnim;
                npc.anInt1555 = npc.desc.anInt58;
                npc.anInt1556 = npc.desc.anInt83;
                npc.anInt1557 = npc.desc.anInt55;
                npc.anInt1511 = npc.desc.standAnim;
            }
            if ((l & 4) != 0) {
                npc.anInt1538 = stream.readLEShort();
                npc.anInt1539 = stream.readLEShort();
            }
        }
    }

    private void buildAtNPCMenu(EntityDef entityDef, int i, int j, int k) {
        if (menuActionRow >= 400) { return; }
        if (entityDef.childrenIDs != null) { entityDef = entityDef.method161(); }
        if (entityDef == null) { return; }
        if (!entityDef.aBoolean84) { return; }
        String s = entityDef.name;
        if (entityDef.combatLevel != 0) {
            s = s + combatDiffColor(myPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel + ")";
        }
        if (itemSelected == 1) {
            menuActionName[menuActionRow] = "Use " + selectedItemName + " with @yel@" + s;
            menuActionID[menuActionRow] = 582;
            menuActionCmd1[menuActionRow] = i;
            menuActionCmd2[menuActionRow] = k;
            menuActionCmd3[menuActionRow] = j;
            menuActionRow++;
            return;
        }
        if (spellSelected == 1) {
            if ((spellUsableOn & 2) == 2) {
                menuActionName[menuActionRow] = spellTooltip + " @yel@" + s;
                menuActionID[menuActionRow] = 413;
                menuActionCmd1[menuActionRow] = i;
                menuActionCmd2[menuActionRow] = k;
                menuActionCmd3[menuActionRow] = j;
                menuActionRow++;
            }
        } else {
            if (entityDef.actions != null) {
                for (int l = 4; l >= 0; l--) {
                    if (entityDef.actions[l] != null && !entityDef.actions[l].equalsIgnoreCase("attack")) {
                        menuActionName[menuActionRow] = entityDef.actions[l] + " @yel@" + s;
                        if (l == 0) { menuActionID[menuActionRow] = 20; }
                        if (l == 1) { menuActionID[menuActionRow] = 412; }
                        if (l == 2) { menuActionID[menuActionRow] = 225; }
                        if (l == 3) { menuActionID[menuActionRow] = 965; }
                        if (l == 4) { menuActionID[menuActionRow] = 478; }
                        menuActionCmd1[menuActionRow] = i;
                        menuActionCmd2[menuActionRow] = k;
                        menuActionCmd3[menuActionRow] = j;
                        menuActionRow++;
                    }
                }

            }
            if (entityDef.actions != null) {
                for (int i1 = 4; i1 >= 0; i1--) {
                    if (entityDef.actions[i1] != null && entityDef.actions[i1].equalsIgnoreCase("attack")) {
                        char c = '\0';
                        if (entityDef.combatLevel > myPlayer.combatLevel) { ; }
                        //c = '\u07D0';
                        menuActionName[menuActionRow] = entityDef.actions[i1] + " @yel@" + s;
                        if (i1 == 0) { menuActionID[menuActionRow] = 20 + c; }
                        if (i1 == 1) { menuActionID[menuActionRow] = 412 + c; }
                        if (i1 == 2) { menuActionID[menuActionRow] = 225 + c; }
                        if (i1 == 3) { menuActionID[menuActionRow] = 965 + c; }
                        if (i1 == 4) { menuActionID[menuActionRow] = 478 + c; }
                        menuActionCmd1[menuActionRow] = i;
                        menuActionCmd2[menuActionRow] = k;
                        menuActionCmd3[menuActionRow] = j;
                        menuActionRow++;
                    }
                }

            }
            // menuActionName[menuActionRow] = "Examine @yel@" + s +
            // " @gre@(@whi@" + entityDef.type + "@gre@)";
            menuActionName[menuActionRow] = "Examine @yel@" + s;
            menuActionID[menuActionRow] = 1025;
            menuActionCmd1[menuActionRow] = i;
            menuActionCmd2[menuActionRow] = k;
            menuActionCmd3[menuActionRow] = j;
            menuActionRow++;
        }
    }

    private void buildAtPlayerMenu(int i, int j, Player player, int k) {
        if (player == myPlayer) { return; }
        if (menuActionRow >= 405) { return; }
        String s;
        if (player.skill == 0) {
            s = (Configuration.getInstance().getValue(Titles.class) ? "@whi@[" + player.title + "@whi@] " : "") + player.name + combatDiffColor(myPlayer.combatLevel, player.combatLevel) + " (level-" + player.combatLevel + ")";
        } else { s = player.name + " (skill-" + player.skill + ")"; }
        if (itemSelected == 1) {
            menuActionName[menuActionRow] = "Use " + selectedItemName + " with @whi@" + s;
            menuActionID[menuActionRow] = 491;
            menuActionCmd1[menuActionRow] = j;
            menuActionCmd2[menuActionRow] = i;
            menuActionCmd3[menuActionRow] = k;
            menuActionRow++;
        } else if (spellSelected == 1) {
            if ((spellUsableOn & 8) == 8) {
                menuActionName[menuActionRow] = spellTooltip + " @whi@" + s;
                menuActionID[menuActionRow] = 365;
                menuActionCmd1[menuActionRow] = j;
                menuActionCmd2[menuActionRow] = i;
                menuActionCmd3[menuActionRow] = k;
                menuActionRow++;
            }
        } else {
            for (int l = 5; l >= 0; l--) {
                if (atPlayerActions[l] != null) {
                    menuActionName[menuActionRow] = atPlayerActions[l] + " @whi@" + s;
                    char c = '\0';
                    if (atPlayerActions[l].equalsIgnoreCase("attack")) {
                        if (player.combatLevel > myPlayer.combatLevel) { c = '\u07D0'; }
                        if (myPlayer.team != 0 && player.team != 0) {
                            if (myPlayer.team == player.team) { c = '\u07D0'; } else { c = '\0'; }
                        }
                    } else if (atPlayerArray[l]) { c = '\u07D0'; }
                    if (l == 0) { menuActionID[menuActionRow] = 561 + c; }
                    if (l == 1) { menuActionID[menuActionRow] = 779 + c; }
                    if (l == 2) { menuActionID[menuActionRow] = 27 + c; }
                    if (l == 3) { menuActionID[menuActionRow] = 577 + c; }
                    if (l == 4) { menuActionID[menuActionRow] = 729 + c; }
                    if (l == 5) { menuActionID[menuActionRow] = 1700 + c; }
                    menuActionCmd1[menuActionRow] = j;
                    menuActionCmd2[menuActionRow] = i;
                    menuActionCmd3[menuActionRow] = k;
                    menuActionRow++;
                }
            }

        }
        for (int i1 = 0; i1 < menuActionRow; i1++) {
            if (menuActionID[i1] == 516) {
                menuActionName[i1] = "Walk here @whi@" + s;
                return;
            }
        }

    }

    private void method89(Class30_Sub1 class30_sub1) {
        int i = 0;
        int j = -1;
        int k = 0;
        int l = 0;
        int id = 0;
        if (class30_sub1.anInt1296 == 0) {
            i = worldController.method300(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
            id = worldController.method300_1(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
        }
        if (class30_sub1.anInt1296 == 1) {
            i = worldController.method301(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
            id = worldController.method301_1(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
        }
        if (class30_sub1.anInt1296 == 2) {
            i = worldController.method302(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
            id = worldController.method302_1(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
        }
        if (class30_sub1.anInt1296 == 3) {
            i = worldController.method303(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
            id = worldController.method303_1(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298);
        }
        if (i != 0) {
            int i1 = worldController.method304(class30_sub1.anInt1295, class30_sub1.anInt1297, class30_sub1.anInt1298, i);
            j = /*(i > 0x7fff ? id : i >> 14 & 0x7fff)*/id;
            k = i1 & 0x1f;
            l = i1 >> 6;
        }
        class30_sub1.anInt1299 = j;
        class30_sub1.anInt1301 = k;
        class30_sub1.anInt1300 = l;
    }

    private void method90() {
        for (int i = 0; i < anInt1062; i++) {
            if (anIntArray1250[i] <= 0) {
                boolean flag1 = false;
                try {
                    if (anIntArray1207[i] == anInt874 && anIntArray1241[i] == anInt1289) {
                        if (!replayWave()) { flag1 = true; }
                    } else {
                        Stream stream = Sounds.method241(anIntArray1241[i], anIntArray1207[i]);
                        if (System.currentTimeMillis() + (long) (stream.currentOffset / 22) > aLong1172 + (long) (anInt1257 / 22)) {
                            anInt1257 = stream.currentOffset;
                            aLong1172 = System.currentTimeMillis();
                            if (saveWave(stream.payload, stream.currentOffset)) {
                                anInt874 = anIntArray1207[i];
                                anInt1289 = anIntArray1241[i];
                            } else {
                                flag1 = true;
                            }
                        }
                    }
                } catch (Exception exception) {
                }
                if (!flag1 || anIntArray1250[i] == -5) {
                    anInt1062--;
                    for (int j = i; j < anInt1062; j++) {
                        anIntArray1207[j] = anIntArray1207[j + 1];
                        anIntArray1241[j] = anIntArray1241[j + 1];
                        anIntArray1250[j] = anIntArray1250[j + 1];
                    }

                    i--;
                } else {
                    anIntArray1250[i] = -5;
                }
            } else {
                anIntArray1250[i]--;
            }
        }

        if (prevSong > 0) {
            prevSong -= 20;
            if (prevSong < 0) { prevSong = 0; }
            if (prevSong == 0 && musicEnabled && !lowMem) {
                nextSong = currentSong;
                songChanging = true;
                onDemandFetcher.requestFileData(2, nextSong);
            }
        }
    }

    public byte[] getData(File modelAdded) {
        try {
            byte[] aByte = new byte[(int) modelAdded.length()];
            FileInputStream fis = new FileInputStream(modelAdded);
            fis.read(aByte);
            System.out.println("name: " + modelAdded.getName() + " data = [" + aByte + "]!");
            fis.close();
            return aByte;
        } catch (Exception e) {
            return null;
        }
    }

    private void method91(Stream stream, int i) {
        while (stream.bitPosition + 10 < i * 8) {
            int j = stream.readBits(11);
            if (j == 2047) { break; }
            if (playerArray[j] == null) {
                playerArray[j] = new Player();
                if (aStreamArray895s[j] != null) { playerArray[j].updatePlayer(aStreamArray895s[j]); }
            }
            playerIndices[playerCount++] = j;
            Player player = playerArray[j];
            player.anInt1537 = loopCycle;
            int k = stream.readBits(1);
            if (k == 1) { anIntArray894[anInt893++] = j; }
            int l = stream.readBits(1);
            int i1 = stream.readBits(5);
            if (i1 > 15) { i1 -= 32; }
            int j1 = stream.readBits(5);
            if (j1 > 15) { j1 -= 32; }
            player.setPos(myPlayer.smallX[0] + j1, myPlayer.smallY[0] + i1, l == 1);
        }
        stream.finishBitAccess();
    }

    private void processMainScreenClick() {
        if (anInt1021 != 0) { return; }
        int x = isFullScreen ? extraWidth : 0;
        int y = isFullScreen ? extraHeight : 0;
        if (super.clickMode3 == 1) {
            if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
                int i = super.saveClickX - 25 - 545;// 545
                int j = super.saveClickY - 5 - 4;
                if (i >= 0 && j >= 0 && i < 146 && j < 151) {
                    i -= 73;
                    j -= 75;
                    int k = minimapInt1 + minimapInt2 & 0x7ff;
                    int i1 = Texture.SINE[k];
                    int j1 = Texture.COSINE[k];
                    i1 = i1 * (minimapInt3 + 256) >> 8;
                    j1 = j1 * (minimapInt3 + 256) >> 8;
                    int k1 = j * i1 + i * j1 >> 11;
                    int l1 = j * j1 - i * i1 >> 11;
                    int i2 = myPlayer.x + k1 >> 7;
                    int j2 = myPlayer.y - l1 >> 7;
                    boolean flag1 = doWalkTo(1, 0, 0, 0, myPlayer.smallY[0], 0, 0, j2, myPlayer.smallX[0], true, i2);
                    if (flag1) {
                        stream.writeByte(i);
                        stream.writeByte(j);
                        stream.writeShort(minimapInt1);
                        stream.writeByte(57);
                        stream.writeByte(minimapInt2);
                        stream.writeByte(minimapInt3);
                        stream.writeByte(89);
                        stream.writeShort(myPlayer.x);
                        stream.writeShort(myPlayer.y);
                        stream.writeByte(anInt1264);
                        stream.writeByte(63);
                    }
                }
            }
            if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
                int x2 = isFullScreen ? 558 : 528;
                int i = super.saveClickX - 25 - x2 - x;
                int j = super.saveClickY - 5 - 4;
                if (i >= 0 && j >= 0 && i < 146 && j < 151) {
                    i -= 73;
                    j -= 75;
                    int k = minimapInt1 + minimapInt2 & 0x7ff;
                    int i1 = Texture.SINE[k];
                    int j1 = Texture.COSINE[k];
                    i1 = i1 * (minimapInt3 + 256) >> 8;
                    j1 = j1 * (minimapInt3 + 256) >> 8;
                    int k1 = j * i1 + i * j1 >> 11;
                    int l1 = j * j1 - i * i1 >> 11;
                    int i2 = myPlayer.x + k1 >> 7;
                    int j2 = myPlayer.y - l1 >> 7;
                    boolean flag1 = doWalkTo(1, 0, 0, 0, myPlayer.smallY[0], 0, 0, j2, myPlayer.smallX[0], true, i2);
                    if (flag1) {
                        stream.writeByte(i);
                        stream.writeByte(j);
                        stream.writeShort(minimapInt1);
                        stream.writeByte(57);
                        stream.writeByte(minimapInt2);
                        stream.writeByte(minimapInt3);
                        stream.writeByte(89);
                        stream.writeShort(myPlayer.x);
                        stream.writeShort(myPlayer.y);
                        stream.writeByte(anInt1264);
                        stream.writeByte(63);
                    }
                }
            }
            if (isFullScreen) {
                int x2 = isFullScreen ? 558 : 528;
                int i = super.saveClickX - 25 - x2 - x;
                int j = super.saveClickY - 5 - 4;
                if (i >= 0 && j >= 0 && i < 146 && j < 151) {
                    i -= 73;
                    j -= 75;
                    int k = minimapInt1 + minimapInt2 & 0x7ff;
                    int i1 = Texture.SINE[k];
                    int j1 = Texture.COSINE[k];
                    i1 = i1 * (minimapInt3 + 256) >> 8;
                    j1 = j1 * (minimapInt3 + 256) >> 8;
                    int k1 = j * i1 + i * j1 >> 11;
                    int l1 = j * j1 - i * i1 >> 11;
                    int i2 = myPlayer.x + k1 >> 7;
                    int j2 = myPlayer.y - l1 >> 7;
                    boolean flag1 = doWalkTo(1, 0, 0, 0, myPlayer.smallY[0], 0, 0, j2, myPlayer.smallX[0], true, i2);
                    if (flag1) {
                        stream.writeByte(i);
                        stream.writeByte(j);
                        stream.writeShort(minimapInt1);
                        stream.writeByte(57);
                        stream.writeByte(minimapInt2);
                        stream.writeByte(minimapInt3);
                        stream.writeByte(89);
                        stream.writeShort(myPlayer.x);
                        stream.writeShort(myPlayer.y);
                        stream.writeByte(anInt1264);
                        stream.writeByte(63);
                    }
                }
            }
            anInt1117++;
            if (anInt1117 > 1151) {
                anInt1117 = 0;
                stream.createFrame(246);
                stream.writeByte(0);
                int l = stream.currentOffset;
                if ((int) (Math.random() * 2D) == 0) { stream.writeByte(101); }
                stream.writeByte(197);
                stream.writeShort((int) (Math.random() * 65536D));
                stream.writeByte((int) (Math.random() * 256D));
                stream.writeByte(67);
                stream.writeShort(14214);
                if ((int) (Math.random() * 2D) == 0) { stream.writeShort(29487); }
                stream.writeShort((int) (Math.random() * 65536D));
                if ((int) (Math.random() * 2D) == 0) { stream.writeByte(220); }
                stream.writeByte(180);
                stream.writeByteC(stream.currentOffset - l);
            }
        }
    }

    private String interfaceIntToString(int j) {
        if (j < 0x3b9ac9ff) { return String.valueOf(j); } else { return "*"; }
    }

    private void showErrorScreen() {
        Graphics g = getGameComponent().getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, 765, 503);
        method4(1);
        if (loadingError) {
            aBoolean831 = false;
            g.setFont(new Font("Helvetica", 1, 16));
            g.setColor(Color.yellow);
            int k = 35;
            g.drawString("Sorry, an error has occured whilst loading " + Constants.getServerName(), 30, k);
            k += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, k);
            k += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, k);
            k += 30;
            g.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, k);
            k += 30;
            g.drawString("3: Try using a different game-world", 30, k);
            k += 30;
            g.drawString("4: Try rebooting your computer", 30, k);
            k += 30;
            g.drawString("5: Try selecting a different version of Java from the play-game menu", 30, k);
        }
        if (genericLoadingError) {
            aBoolean831 = false;
            g.setFont(new Font("Helvetica", 1, 20));
            g.setColor(Color.white);
            g.drawString("Error - unable to load game!", 50, 50);
            g.drawString("To play " + Constants.getServerName() + " make sure you play from", 50, 100);
            g.drawString(Constants.WEBSITE_URL, 50, 150);
        }
        if (rsAlreadyLoaded) {
            aBoolean831 = false;
            g.setColor(Color.yellow);
            int l = 35;
            g.drawString("Error a copy of " + Constants.getServerName() + " already appears to be loaded", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, l);
            l += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, l);
            l += 30;
            g.drawString("2: Try rebooting your computer, and reloading", 30, l);
            l += 30;
        }
    }

    private void method95() {
        for (int j = 0; j < npcCount; j++) {
            int k = npcIndices[j];
            NPC npc = npcArray[k];
            if (npc != null) { method96(npc); }
        }
    }

    private void method96(Entity entity) {
        if (entity.x < 128 || entity.y < 128 || entity.x >= 13184 || entity.y >= 13184) {
            entity.anim = -1;
            entity.anInt1520 = -1;
            entity.anInt1547 = 0;
            entity.anInt1548 = 0;
            entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
            entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
            entity.method446();
        }
        if (entity == myPlayer && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
            entity.anim = -1;
            entity.anInt1520 = -1;
            entity.anInt1547 = 0;
            entity.anInt1548 = 0;
            entity.x = entity.smallX[0] * 128 + entity.anInt1540 * 64;
            entity.y = entity.smallY[0] * 128 + entity.anInt1540 * 64;
            entity.method446();
        }
        if (entity.anInt1547 > loopCycle) { method97(entity); } else if (entity.anInt1548 >= loopCycle) {
            method98(entity);
        } else { method99(entity); }
        method100(entity);
        method101(entity);
    }

    private void method97(Entity entity) {
        int i = entity.anInt1547 - loopCycle;
        int j = entity.anInt1543 * 128 + entity.anInt1540 * 64;
        int k = entity.anInt1545 * 128 + entity.anInt1540 * 64;
        entity.x += (j - entity.x) / i;
        entity.y += (k - entity.y) / i;
        entity.anInt1503 = 0;
        if (entity.anInt1549 == 0) { entity.turnDirection = 1024; }
        if (entity.anInt1549 == 1) { entity.turnDirection = 1536; }
        if (entity.anInt1549 == 2) { entity.turnDirection = 0; }
        if (entity.anInt1549 == 3) { entity.turnDirection = 512; }
    }

    private void method98(Entity entity) {
        if (entity.anInt1548 == loopCycle || entity.anim == -1 || entity.anInt1529 != 0 || entity.anInt1528 + 1 > Animation.anims[entity.anim].method258(entity.anInt1527)) {
            int i = entity.anInt1548 - entity.anInt1547;
            int j = loopCycle - entity.anInt1547;
            int k = entity.anInt1543 * 128 + entity.anInt1540 * 64;
            int l = entity.anInt1545 * 128 + entity.anInt1540 * 64;
            int i1 = entity.anInt1544 * 128 + entity.anInt1540 * 64;
            int j1 = entity.anInt1546 * 128 + entity.anInt1540 * 64;
            entity.x = (k * (i - j) + i1 * j) / i;
            entity.y = (l * (i - j) + j1 * j) / i;
        }
        entity.anInt1503 = 0;
        if (entity.anInt1549 == 0) { entity.turnDirection = 1024; }
        if (entity.anInt1549 == 1) { entity.turnDirection = 1536; }
        if (entity.anInt1549 == 2) { entity.turnDirection = 0; }
        if (entity.anInt1549 == 3) { entity.turnDirection = 512; }
        entity.anInt1552 = entity.turnDirection;
    }

    private void method99(Entity entity) {
        entity.anInt1517 = entity.anInt1511;
        if (entity.smallXYIndex == 0) {
            entity.anInt1503 = 0;
            return;
        }
        if (entity.anim != -1 && entity.anInt1529 == 0) {
            Animation animation = Animation.anims[entity.anim];
            if (entity.anInt1542 > 0 && animation.anInt363 == 0) {
                entity.anInt1503++;
                return;
            }
            if (entity.anInt1542 <= 0 && animation.anInt364 == 0) {
                entity.anInt1503++;
                return;
            }
        }
        int i = entity.x;
        int j = entity.y;
        int k = entity.smallX[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
        int l = entity.smallY[entity.smallXYIndex - 1] * 128 + entity.anInt1540 * 64;
        if (k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
            entity.x = k;
            entity.y = l;
            return;
        }
        if (i < k) {
            if (j < l) { entity.turnDirection = 1280; } else if (j > l) { entity.turnDirection = 1792; } else {
                entity.turnDirection = 1536;
            }
        } else if (i > k) {
            if (j < l) { entity.turnDirection = 768; } else if (j > l) { entity.turnDirection = 256; } else {
                entity.turnDirection = 512;
            }
        } else if (j < l) { entity.turnDirection = 1024; } else { entity.turnDirection = 0; }
        int i1 = entity.turnDirection - entity.anInt1552 & 0x7ff;
        if (i1 > 1024) { i1 -= 2048; }
        int j1 = entity.anInt1555;
        if (i1 >= -256 && i1 <= 256) { j1 = entity.anInt1554; } else if (i1 >= 256 && i1 < 768) {
            j1 = entity.anInt1557;
        } else if (i1 >= -768 && i1 <= -256) {
            j1 = entity.anInt1556;
        }
        if (j1 == -1) { j1 = entity.anInt1554; }
        entity.anInt1517 = j1;
        int k1 = 4;
        if (entity.anInt1552 != entity.turnDirection && entity.interactingEntity == -1 && entity.anInt1504 != 0) {
            k1 = 2;
        }
        if (entity.smallXYIndex > 2) { k1 = 6; }
        if (entity.smallXYIndex > 3) { k1 = 8; }
        if (entity.anInt1503 > 0 && entity.smallXYIndex > 1) {
            k1 = 8;
            entity.anInt1503--;
        }
        if (entity.aBooleanArray1553[entity.smallXYIndex - 1]) { k1 <<= 1; }
        if (k1 >= 8 && entity.anInt1517 == entity.anInt1554 && entity.anInt1505 != -1) {
            entity.anInt1517 = entity.anInt1505;
        }
        if (i < k) {
            entity.x += k1;
            if (entity.x > k) { entity.x = k; }
        } else if (i > k) {
            entity.x -= k1;
            if (entity.x < k) { entity.x = k; }
        }
        if (j < l) {
            entity.y += k1;
            if (entity.y > l) { entity.y = l; }
        } else if (j > l) {
            entity.y -= k1;
            if (entity.y < l) { entity.y = l; }
        }
        if (entity.x == k && entity.y == l) {
            entity.smallXYIndex--;
            if (entity.anInt1542 > 0) { entity.anInt1542--; }
        }
    }

    private void method100(Entity entity) {
        if (entity.anInt1504 == 0) { return; }
        if (entity.interactingEntity != -1 && entity.interactingEntity < 32768 && entity.interactingEntity < npcArray.length) {
            NPC npc = npcArray[entity.interactingEntity];
            if (npc != null) {
                int i1 = entity.x - npc.x;
                int k1 = entity.y - npc.y;
                if (i1 != 0 || k1 != 0) {
                    entity.turnDirection = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
                }
            }
        }
        if (entity.interactingEntity >= 32768) {
            int j = entity.interactingEntity - 32768;
            if (j == unknownInt10) { j = myPlayerIndex; }
            if (j >= playerArray.length) { return; }
            Player player = playerArray[j];
            if (player != null) {
                int l1 = entity.x - player.x;
                int i2 = entity.y - player.y;
                if (l1 != 0 || i2 != 0) {
                    entity.turnDirection = (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
                }
            }
        }
        if ((entity.anInt1538 != 0 || entity.anInt1539 != 0) && (entity.smallXYIndex == 0 || entity.anInt1503 > 0)) {
            int k = entity.x - (entity.anInt1538 - baseX - baseX) * 64;
            int j1 = entity.y - (entity.anInt1539 - baseY - baseY) * 64;
            if (k != 0 || j1 != 0) { entity.turnDirection = (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff; }
            entity.anInt1538 = 0;
            entity.anInt1539 = 0;
        }
        int l = entity.turnDirection - entity.anInt1552 & 0x7ff;
        if (l != 0) {
            if (l < entity.anInt1504 || l > 2048 - entity.anInt1504) {
                entity.anInt1552 = entity.turnDirection;
            } else if (l > 1024) { entity.anInt1552 -= entity.anInt1504; } else {
                entity.anInt1552 += entity.anInt1504;
            }
            entity.anInt1552 &= 0x7ff;
            if (entity.anInt1517 == entity.anInt1511 && entity.anInt1552 != entity.turnDirection) {
                if (entity.anInt1512 != -1) {
                    entity.anInt1517 = entity.anInt1512;
                    return;
                }
                entity.anInt1517 = entity.anInt1554;
            }
        }
    }

    private void method101(Entity entity) {
        entity.aBoolean1541 = false;
        if (entity.anInt1517 != -1)// Delay fix
        {
            if (entity.anInt1517 >= Animation.anims.length) { return; }
            Animation animation = Animation.anims[entity.anInt1517];
            entity.anInt1519++;
            if (entity.anInt1518 < animation.anInt352 && entity.anInt1519 > animation.method258(entity.anInt1518)) {
                entity.anInt1519 = 1;
                entity.anInt1518++;
            }
            if (entity.anInt1518 >= animation.anInt352) {
                entity.anInt1519 = 0;
                entity.anInt1518 = 0;
            }
        }
        if (entity.anInt1520 != -1 && loopCycle >= entity.anInt1523) {
            if (entity.anInt1521 < 0) { entity.anInt1521 = 0; }
            if (entity.anInt1520 >= SpotAnim.cache.length) { return; }
            Animation animation_1 = SpotAnim.cache[entity.anInt1520].aAnimation_407;
            for (entity.anInt1522++; entity.anInt1521 < animation_1.anInt352 && entity.anInt1522 > animation_1.method258(entity.anInt1521); entity.anInt1521++) {
                entity.anInt1522 -= animation_1.method258(entity.anInt1521);
            }

            if (entity.anInt1521 >= animation_1.anInt352 && (entity.anInt1521 < 0 || entity.anInt1521 >= animation_1.anInt352)) {
                entity.anInt1520 = -1;
            }
        }
        if (entity.anim != -1 && entity.anInt1529 <= 1) {
            if (entity.anim >= Animation.anims.length) { return; }
            Animation animation_2 = Animation.anims[entity.anim];
            if (animation_2.anInt363 == 1 && entity.anInt1542 > 0 && entity.anInt1547 <= loopCycle && entity.anInt1548 < loopCycle) {
                entity.anInt1529 = 1;
                return;
            }
        }
        if (entity.anim != -1 && entity.anInt1529 == 0) {
            Animation animation_3 = Animation.anims[entity.anim];
            for (entity.anInt1528++; entity.anInt1527 < animation_3.anInt352 && entity.anInt1528 > animation_3.method258(entity.anInt1527); entity.anInt1527++) {
                entity.anInt1528 -= animation_3.method258(entity.anInt1527);
            }

            if (entity.anInt1527 >= animation_3.anInt352) {
                entity.anInt1527 -= animation_3.anInt356;
                entity.anInt1530++;
                if (entity.anInt1530 >= animation_3.anInt362) { entity.anim = -1; }
                if (entity.anInt1527 < 0 || entity.anInt1527 >= animation_3.anInt352) { entity.anim = -1; }
            }
            entity.aBoolean1541 = animation_3.aBoolean358;
        }
        if (entity.anInt1529 > 0) { entity.anInt1529--; }
    }

    private void drawGameScreen() {
        if (fullscreenInterfaceID != -1 && (loadingStage == 2 || super.fullGameScreen != null)) {
            if (loadingStage == 2) {
                method119(anInt945, fullscreenInterfaceID);
                if (openInterfaceID != -1) {
                    method119(anInt945, openInterfaceID);
                }
                anInt945 = 0;
                resetAllImageProducers();
                super.fullGameScreen.initializeDrawingArea();
                Texture.anIntArray1472 = fullScreenTextureArray;
                DrawingArea.setAllPixelsToZero();
                welcomeScreenRaised = true;
                if (openInterfaceID != -1) {
                    RSInterface rsInterface_1 = RSInterface.interfaceCache[openInterfaceID];
                    if (rsInterface_1.width == 512 && rsInterface_1.height == 334 && rsInterface_1.type == 0) {
                        rsInterface_1.width = 765;
                        rsInterface_1.height = 503;
                    }
                    drawInterface(0, 0, rsInterface_1, 8);
                }
                RSInterface rsInterface = RSInterface.interfaceCache[fullscreenInterfaceID];
                if (rsInterface.width == 512 && rsInterface.height == 334 && rsInterface.type == 0) {
                    rsInterface.width = 765;
                    rsInterface.height = 503;
                }
                drawInterface(0, 0, rsInterface, 8);

                if (!menuOpen) {
                    processRightClick();
                    drawTooltip();
                } else {
                    drawMenu();
                }
            }
            drawCount++;
            super.fullGameScreen.drawGraphics(0, super.graphics, 0);
            return;
        } else {
            if (drawCount != 0) {
                resetImageProducers2();
            }
        }
        if (welcomeScreenRaised) {
            welcomeScreenRaised = false;
            if (!isFullScreen) {
                topFrame.drawGraphics(0, super.graphics, 0);
                leftFrame.drawGraphics(4, super.graphics, 0);
                rightFrame.drawGraphics(4, super.graphics, 516);
                mapEdgeIP.drawGraphics(4, super.graphics, 519);
            }
            needDrawTabArea = true;
            inputTaken = true;
            tabAreaAltered = true;
            aBoolean1233 = true;
            if (loadingStage != 2) {
                aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
                aRSImageProducer_1164.drawGraphics(0, super.graphics, 519 + extraWidth);
            }
        }
        if (menuOpen && menuScreenArea == 1) { needDrawTabArea = true; }
        if (invOverlayInterfaceID != -1) {
            boolean flag1 = method119(anInt945, invOverlayInterfaceID);
            if (flag1) { needDrawTabArea = true; }
        }
        if (atInventoryInterfaceType == 2) { needDrawTabArea = true; }
        if (activeInterfaceType == 2) { needDrawTabArea = true; }
        if (needDrawTabArea) {
            if (!isFullScreen) { drawTabArea(); }
            needDrawTabArea = false;
        }
        if (backDialogID == -1) {
            aClass9_1059.scrollPosition = anInt1211 - anInt1089 - 110;
            if (super.mouseX > 478 && super.mouseX < 580 && super.mouseY > (clientHeight - 161)) {
                method65(494, 110, super.mouseX - 0, super.mouseY - (clientHeight - 155), aClass9_1059, 0, false, anInt1211);
            }
            int i = anInt1211 - 110 - aClass9_1059.scrollPosition;
            if (i < 0) { i = 0; }
            if (i > anInt1211 - 110) { i = anInt1211 - 110; }
            if (anInt1089 != i) {
                anInt1089 = i;
                inputTaken = true;
            }
        }
        if (backDialogID != -1) {
            boolean flag2 = method119(anInt945, backDialogID);
            if (flag2) { inputTaken = true; }
        }
        if (atInventoryInterfaceType == 3) { inputTaken = true; }
        if (activeInterfaceType == 3) { inputTaken = true; }
        if (aString844 != null) { inputTaken = true; }
        if (menuOpen && menuScreenArea == 2) { inputTaken = true; }
        if (inputTaken && !isFullScreen) {
            try {
                drawChatArea();
            } catch (Exception e) {
                e.printStackTrace();
            }
            inputTaken = false;
        }
        if (loadingStage == 2) {
            try {
                method146();
            } catch (Exception e) {
            }
        }
        if (loadingStage == 2 && !isFullScreen) {
            drawMinimap();
            aRSImageProducer_1164.drawGraphics(0, super.graphics, 519);
        }
        if (anInt1054 != -1) { tabAreaAltered = true; }
        if (tabAreaAltered) {
            if (anInt1054 != -1 && anInt1054 == tabID) {
                anInt1054 = -1;
                stream.createFrame(120);
                stream.writeByte(tabID);
            }
            tabAreaAltered = false;
            aRSImageProducer_1125.initializeDrawingArea();
            aRSImageProducer_1165.initializeDrawingArea();
        }
        if (aBoolean1233) {
            aBoolean1233 = false;
        }
        anInt945 = 0;
        // RSInterface.resetRunesAndLevels(1541); //DERP
    }

    private void determineMenuSize() {
        int i = chatTextDrawingArea.getTextWidth("Choose Option");
        for (int j = 0; j < menuActionRow; j++) {
            int k = chatTextDrawingArea.getTextWidth(menuActionName[j]);
            if (k > i) { i = k; }
        }
        int x = isFullScreen ? extraWidth : 0;
        int y = isFullScreen ? extraHeight : 0;
        i += 8;
        int l = 15 * menuActionRow + 21;
        if (super.saveClickX > 4 && super.saveClickY > 4 && super.saveClickX < 516 && super.saveClickY < 338) {
            int i1 = super.saveClickX - 4 - i / 2;
            if (i1 + i > 512 + x) { i1 = 512 + x - i; }
            if (i1 < 0) { i1 = 0; }
            int l1 = super.saveClickY - 4;
            if (l1 + l > 334 + y) { l1 = 334 + y - l; }
            if (l1 < 0) { l1 = 0; }
            menuOpen = true;
            menuScreenArea = 0;
            menuOffsetX = i1;
            menuOffsetY = l1;
            menuWidth = i;
            menuHeight = 15 * menuActionRow + 22;
        }
        if (super.saveClickX > 519 && super.saveClickY > 168 && super.saveClickX < 765 + x && super.saveClickY < 503 + y) {
            int j1 = super.saveClickX - 519 - i / 2;
            if (j1 < 0) { j1 = 0; } else if (j1 + i > 245 + x) { j1 = 245 + x - i; }
            int i2 = super.saveClickY - 168;
            if (i2 < 0) { i2 = 0; } else if (i2 + l > 333 + y) { i2 = 333 + y - l; }
            menuOpen = true;
            menuOffsetX = j1;
            menuOffsetY = i2;

            menuScreenArea = 1;
            menuWidth = i;
            menuHeight = 15 * menuActionRow + 22;
        }
        if (super.saveClickX > 0 && super.saveClickY > 338 && super.saveClickX < 516 && super.saveClickY < 503 + y) {
            int k1 = super.saveClickX - 0 - i / 2;
            if (k1 < 0) { k1 = 0; } else if (k1 + i > 516) { k1 = 516 - i; }
            int j2 = super.saveClickY - 338;
            if (j2 < 0) { j2 = 0; } else if (j2 + l > 165 + y) { j2 = 165 + y - l; }
            menuOpen = true;
            menuOffsetX = k1;
            menuOffsetY = j2;
            menuWidth = i;

            menuScreenArea = 2;
            menuHeight = 15 * menuActionRow + 22;
        }
        if (super.saveClickX >= 515 + extraWidth && super.saveClickY >= 0 && super.saveClickX <= 765 + extraWidth && super.saveClickY <= 169) {
            int k1 = super.saveClickX - 515 - i / 2;
            if (k1 < 0) { k1 = 0; } else if (k1 + i > 249 + extraWidth) { k1 = 249 + extraWidth - i; }
            int j2 = super.saveClickY;
            if (j2 < 0) { j2 = 0; } else if (j2 + l > 168) { j2 = 168 - l; }
            menuOpen = true;
            menuScreenArea = 3;
            menuOffsetX = k1;
            menuOffsetY = j2;
            menuWidth = i;
            menuHeight = 15 * menuActionRow + 22;
        }
    }

    private boolean buildFriendsListMenu(RSInterface class9) {
        int i = class9.contentType;
        if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
            if (i >= 801) { i -= 701; } else if (i >= 701) { i -= 601; } else if (i >= 101) { i -= 101; } else { i--; }
            menuActionName[menuActionRow] = "Remove @whi@" + friendsList[i];
            menuActionID[menuActionRow] = 792;
            menuActionRow++;
            menuActionName[menuActionRow] = "Message @whi@" + friendsList[i];
            menuActionID[menuActionRow] = 639;
            menuActionRow++;
            return true;
        }
        if (i >= 401 && i <= 500) {
            menuActionName[menuActionRow] = "Remove @whi@" + class9.message;
            menuActionID[menuActionRow] = 322;
            menuActionRow++;
            return true;
        } else {
            return false;
        }
    }

    private void method104() {
        Animable_Sub3 class30_sub2_sub4_sub3 = (Animable_Sub3) aClass19_1056.reverseGetFirst();
        for (; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 = (Animable_Sub3) aClass19_1056.reverseGetNext()) {
            if (class30_sub2_sub4_sub3.anInt1560 != plane || class30_sub2_sub4_sub3.aBoolean1567) {
                class30_sub2_sub4_sub3.unlink();
            } else if (loopCycle >= class30_sub2_sub4_sub3.anInt1564) {
                class30_sub2_sub4_sub3.method454(anInt945);
                if (class30_sub2_sub4_sub3.aBoolean1567) { class30_sub2_sub4_sub3.unlink(); } else {
                    worldController.method285(class30_sub2_sub4_sub3.anInt1560, 0, class30_sub2_sub4_sub3.anInt1563, -1, class30_sub2_sub4_sub3.anInt1562, 60, class30_sub2_sub4_sub3.anInt1561, class30_sub2_sub4_sub3, false);
                }
            }
        }

    }

    void startUp() {
        Loader.getInstance().updateTitle("Loading Cache...");
        Loader.getInstance().updateValues(20, "Please Wait...");
        super.prepareGraphics();
        if (Constants.CONNECT_FILE_SERVER) {
            Updater.initialize();
        }
        if (Signlink.cache_dat != null) {
            for (int i = 0; i < 5; i++) {
                decompressors[i] = new Decompressor(Signlink.cache_dat, Signlink.cache_idx[i], i + 1);
            }
        }
        try {
            titleStreamLoader = streamLoaderForName(1, "title screen", "title", expectedCRCs[1], 25);
            smallText = new TextDrawingArea(false, "p11_full", titleStreamLoader);
            aTextDrawingArea_1271 = new TextDrawingArea(false, "p12_full", titleStreamLoader);
            chatTextDrawingArea = new TextDrawingArea(false, "b12_full", titleStreamLoader);
            aTextDrawingArea_1273 = new TextDrawingArea(true, "q8_full", titleStreamLoader);
            smallHit = new TextDrawingArea(false, "p12_full", titleStreamLoader);
            bigHit = new TextDrawingArea(true, "b12_full", titleStreamLoader);
            loadTitleScreen();
            StreamLoader streamLoader = streamLoaderForName(2, "config", "config", expectedCRCs[2], 30);
            StreamLoader streamLoader_1 = streamLoaderForName(3, "interface", "interface", expectedCRCs[3], 35);
            StreamLoader streamLoader_2 = streamLoaderForName(4, "2d graphics", "media", expectedCRCs[4], 40);
            StreamLoader streamLoader_3 = streamLoaderForName(6, "textures", "textures", expectedCRCs[6], 45);
            StreamLoader streamLoader_4 = streamLoaderForName(7, "chat system", "wordenc", expectedCRCs[7], 50);
            StreamLoader streamLoader_5 = streamLoaderForName(8, "sound effects", "sounds", expectedCRCs[8], 55);
            byteGroundArray = new byte[4][104][104];
            intGroundArray = new int[4][105][105];
            worldController = new WorldController(intGroundArray);
            for (int j = 0; j < 4; j++) { aClass11Array1230[j] = new Class11(); }

            aClass30_Sub2_Sub1_Sub1_1263 = new Sprite(512, 512);
            loadingPleaseWait = new Sprite("loadingPleaseWait");
            for (int i = 0; i <= 4; i++) {
                combatIcons[i] = new Sprite("Player/combatIcon " + i + "");
            }
            for (int nSI = 0; nSI <= 15; nSI++) { newSideIcons[nSI] = new Sprite("GameFrame/tabs/icon " + nSI); }
            tabHover = new Sprite("GameFrame/tabs/tabhover");
            summIcon = new Sprite("GameFrame/Orbs/summicon");
            multiOverlay2 = new Sprite("GameFrame/multi");
            tabClicked = new Sprite("GameFrame/tabs/tabclicked");
            reestablish = new Sprite("reestablish");
            sprite1 = new Sprite("GameFrame/sprite1");
            xpFlag = new Sprite("GameFrame/xpFlag");
            x10ExpandOld = new Sprite("expandedOld");
            mapRing = new Sprite("GameFrame/Orbs/1129");
            for (int i2 = 0; i2 < 50; i2++) {
                hitMark[i2] = new Sprite((new StringBuilder()).append("/Hitmarks/hit ").append(i2).toString());
            }

            for (int j2 = 0; j2 < 6; j2++) {
                hitIcon[j2] = new Sprite((new StringBuilder()).append("/Hitmarks/icon ").append(j2).toString());
            }
            StreamLoader streamLoader_6 = streamLoaderForName(5, "update list", "versionlist", expectedCRCs[5], 60);
            Loader.getInstance().updateValues(60, "Please Wait...");
            onDemandFetcher = new OnDemandFetcher();
            onDemandFetcher.start(streamLoader_6, this);
            FrameReader.initialise();
            Model.method459(onDemandFetcher.getModelCount(), onDemandFetcher);
            Loader.getInstance().updateValues(80, "Please Wait...");

            logIconH = new Sprite(streamLoader_2, "orbs", 0);
            logIconC = new Sprite(streamLoader_2, "orbs", 1);
            questionIconH = new Sprite(streamLoader_2, "orbs", 4);
            worldMapIcon = new Sprite("GameFrame/Orbs/mapicon");
            xpIcon = new Sprite("GameFrame/Orbs/2730");
            xpIconH = new Sprite("GameFrame/Orbs/2731");
            x10Expand = new Sprite("expanded");
            fullScreenSprites[0] = new Sprite("invbg");
            fullScreenSprites[1] = new Sprite("tabs");
            fullScreenSprites[2] = new Sprite("tabs2");
            for (int c2 = 0; c2 < 2; c2++) { chatToggle[c2] = new Sprite("chattoggle " + c2); }
            for (int c3 = 0; c3 < 4; c3++) { socialIcons[c3] = new Sprite("Social/SOCIAL " + c3); }
            for (int c3 = 0; c3 < 5; c3++) { particleIcon[c3] = new Sprite("particles/particle " + c3); }
            mapArea[1] = new Sprite("maparea");
            mapArea[2] = new Sprite("maparea2");
            mapArea[0] = new Sprite("fullscreenmapback");
            HPBarFull = new Sprite("HITPOINTS_0");
            HPBarEmpty = new Sprite("HITPOINTS_1");
            logoutIcon = new Sprite(streamLoader_2, "orbs", 2);
            questionIcon = new Sprite(streamLoader_2, "orbs", 3);
            emptyOrb = new Sprite("GameFrame/Orbs/emptyorb");
            orbFill = new Sprite("GameFrame/Orbs/Orb 2");
            hoveredEmpty = new Sprite("GameFrame/Orbs/hoveredempty");
            runIcon1 = new Sprite("GameFrame/Orbs/runicon1");
            runIcon2 = new Sprite("GameFrame/Orbs/runicon2");
            runOrb1 = new Sprite("GameFrame/Orbs/runorb1");
            runOrb2 = new Sprite("GameFrame/Orbs/runorb2");
            hitPointsFill = new Sprite("GameFrame/Orbs/hitpointsfill");
            hitPointsIcon = new Sprite("GameFrame/Orbs/hitpointsicon");
            prayerFill = new Sprite("GameFrame/Orbs/prayerfill");
            prayerIcon = new Sprite("GameFrame/Orbs/prayericon");
            infinity = new Sprite("infinity");
			/* Custom sprite unpacking */
            chatArea = new Sprite("chatarea");
            tabs554 = new Sprite("554short");
            long554 = new Sprite("tab");
            if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
                tabArea = new Sprite("tabarea3");
            }
            if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_554)) {
                tabArea = new Sprite("tabarea");
            }
            if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474)) {
                tabArea = new Sprite("tabarea2");
            }
            multiOverlay = new Sprite(streamLoader_2, "overlay_multiway", 0);
			/**/
            mapBack = new Background(streamLoader_2, "mapback", 0);
            for (int c1 = 0; c1 <= 3; c1++) { chatButtons[c1] = new Sprite(streamLoader_2, "chatbuttons", c1); }
            for (int j3 = 0; j3 <= 14; j3++) { sideIcons[j3] = new Sprite(streamLoader_2, "sideicons", j3); }
            for (int r1 = 0; r1 < 5; r1++) { redStones[r1] = new Sprite("redstones " + r1); }
            compass = new Sprite(streamLoader_2, "compass", 0);
            mapEdge = new Sprite(streamLoader_2, "mapedge", 0);
            mapEdge.method345();
            try {
                for (int i1 = 0; i1 < 9; i1++) { eventSprites[i1] = new Sprite("Events/EVENT " + i1); }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                for (int i1 = 0; i1 < 5; i1++) { specialSprites[i1] = new Sprite("GameFrame/special/SPECIAL " + i1); }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                for (int k3 = 0; k3 < 100; k3++) { mapScenes[k3] = new Background(streamLoader_2, "mapscene", k3); }
            } catch (Exception _ex) {
            }
            try {
                for (int l3 = 0; l3 < 100; l3++) { mapFunctions[l3] = new Sprite(streamLoader_2, "mapfunction", l3); }
            } catch (Exception _ex) {
            }
            try {
                for (int i4 = 0; i4 < 20; i4++) { hitMarks[i4] = new Sprite(streamLoader_2, "hitmarks", i4); }
            } catch (Exception _ex) {
            }
            try {
                for (int h1 = 0; h1 < 6; h1++) { headIconsHint[h1] = new Sprite(streamLoader_2, "headicons_hint", h1); }
            } catch (Exception _ex) {
            }
            try {
                for (int j4 = 0; j4 < 8; j4++) { headIcons[j4] = new Sprite(streamLoader_2, "headicons_prayer", j4); }
                for (int idx = 0; idx < 18; idx++) { headIcons[idx] = new Sprite("Player/Prayer/Prayer " + idx); }
                for (int j45 = 0; j45 < 3; j45++) { skullIcons[j45] = new Sprite(streamLoader_2, "headicons_pk", j45); }
            } catch (Exception _ex) {
            }
            mapFlag = new Sprite(streamLoader_2, "mapmarker", 0);
            mapMarker = new Sprite(streamLoader_2, "mapmarker", 1);
            for (int k4 = 0; k4 < 8; k4++) { crosses[k4] = new Sprite(streamLoader_2, "cross", k4); }

            mapDotItem = new Sprite(streamLoader_2, "mapdots", 0);
            mapDotNPC = new Sprite(streamLoader_2, "mapdots", 1);
            mapDotPlayer = new Sprite(streamLoader_2, "mapdots", 2);
            mapDotFriend = new Sprite(streamLoader_2, "mapdots", 3);
            mapDotTeam = new Sprite(streamLoader_2, "mapdots", 4);
            mapDotClan = new Sprite(streamLoader_2, "mapdots", 5);
            scrollBar1 = new Sprite(streamLoader_2, "scrollbar", 0);
            scrollBar2 = new Sprite(streamLoader_2, "scrollbar", 1);

            for (int i = 0; i < scrollSprites.length; i++) {
                scrollSprites[i] = new Sprite("Scroll/SCROLL " + i);
            }

            alertBack = new Sprite("alertback");
            alertBorder = new Sprite("alertborder");
            alertBorderH = new Sprite("alertborderh");

            for (int l4 = 0; l4 < modIcons.length; l4++) {
                modIcons[l4] = new Background(streamLoader_2, l4 > 14 ? "mod_icons2" : "mod_icons", l4 - (l4 > 14 ? 15 : 0));

            }

            modIcons[15] = new Background(streamLoader_2, "mod_icons", 14);
            modIcons[14] = new Background(streamLoader_2, "mod_icons2", 0);

            Sprite sprite = new Sprite(streamLoader_2, "screenframe", 0);
            leftFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight);
            sprite.method346(0, 0);
            sprite = new Sprite(streamLoader_2, "screenframe", 1);
            topFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight);
            sprite.method346(0, 0);
            sprite = new Sprite(streamLoader_2, "screenframe", 2);
            rightFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight);
            sprite.method346(0, 0);
            sprite = new Sprite(streamLoader_2, "mapedge", 0);
            mapEdgeIP = new RSImageProducer(sprite.myWidth, sprite.myHeight);
            sprite.method346(0, 0);

            int i5 = (int) (Math.random() * 21D) - 10;
            int j5 = (int) (Math.random() * 21D) - 10;
            int k5 = (int) (Math.random() * 21D) - 10;
            int l5 = (int) (Math.random() * 41D) - 20;
            for (int i6 = 0; i6 < 100; i6++) {
                if (mapFunctions[i6] != null) { mapFunctions[i6].method344(i5 + l5, j5 + l5, k5 + l5); }
                if (mapScenes[i6] != null) { mapScenes[i6].method360(i5 + l5, j5 + l5, k5 + l5); }
            }

            Loader.getInstance().updateValues(83, "Please Wait...");
            Texture.method368(streamLoader_3);
            Texture.method372(0.80000000000000004D);
            Texture.method367();
            Loader.getInstance().updateValues(86, "Please Wait...");
            Animation.unpackConfig(streamLoader);
            ObjectDef.unpackConfig(streamLoader);
            Flo.unpackConfig(streamLoader);
            ItemDef.unpackConfig(streamLoader);
            EntityDef.unpackConfig(streamLoader);

            IdentityKit.unpackConfig(streamLoader);
            SpotAnim.unpackConfig(streamLoader);
            Varp.unpackConfig(streamLoader);
            VarBit.unpackConfig(streamLoader);
            ItemDef.isMembers = isMembers;
            if (!lowMem) {
                Loader.getInstance().updateValues(90, "Please Wait...");
                byte abyte0[] = streamLoader_5.getDataForName("sounds.dat");
                Stream stream = new Stream(abyte0);
                Sounds.unpack(stream);
            }
            Loader.getInstance().updateValues(95, "Please Wait...");
            final TextDrawingArea aclass30_sub2_sub1_sub4s[] = {smallText, aTextDrawingArea_1271, chatTextDrawingArea, aTextDrawingArea_1273};
            RSInterface.fonts = aclass30_sub2_sub1_sub4s;
            RSInterface.unpack(streamLoader_1, aclass30_sub2_sub1_sub4s, streamLoader_2);
            new java.util.Timer().schedule(new TimerTask() {
                public void run() {
                    //RSInterface.bountyHunterTab(aclass30_sub2_sub1_sub4s);
                }
            }, 5000, 1000);
            Loader.getInstance().updateValues(100, "Please Wait...");
            System.out.println("Width: " + (519 + 41));
            System.out.println("Height: " + (168 - 3));
            for (int j6 = 0; j6 < 33; j6++) {
                int k6 = 0;
                int i7 = 0;
                for (int k7 = 0; k7 < 34; k7++) {
                    if (mapBack.aByteArray1450[k7 + j6 * mapBack.anInt1452] == 0) {
                        if (k6 == 0) { k6 = k7; }
                        continue;
                    }
                    if (k6 == 0) { continue; }
                    i7 = k7;
                    break;
                }

                anIntArray968[j6] = k6;
                anIntArray1057[j6] = i7 - k6;
            }

            for (int l6 = 5; l6 < 156; l6++) {
                int j7 = 999;
                int l7 = 0;
                for (int j8 = 20; j8 < 172; j8++) {
                    if (mapBack.aByteArray1450[j8 + l6 * mapBack.anInt1452] == 0 && (j8 > 34 || l6 > 34)) {
                        if (j7 == 999) { j7 = j8; }
                        continue;
                    }
                    if (j7 == 999) { continue; }
                    l7 = j8;
                    break;
                }

                anIntArray1052[l6 - 5] = j7 - 20;
                anIntArray1229[l6 - 5] = l7 - j7;
                if (anIntArray1229[l6 - 5] == -20) { anIntArray1229[l6 - 5] = 152; }
            }

            Texture.method365(519, 165);
            anIntArray1180 = Texture.anIntArray1472;
            Texture.method365(246, 335);
            anIntArray1181 = Texture.anIntArray1472;
            Texture.method365(512, 334);
            anIntArray1182 = Texture.anIntArray1472;
            int ai[] = new int[9];
            for (int i8 = 0; i8 < 9; i8++) {
                int k8 = 128 + i8 * 32 + 15;
                int l8 = 600 + k8 * 3;
                int i9 = Texture.SINE[k8];
                ai[i8] = l8 * i9 >> 16;
            }
            WorldController.method310(500, 800, 512, 334, ai);
            Censor.loadConfig(streamLoader_4);
            mouseDetection = new MouseDetection(this);
            startRunnable(mouseDetection, 9);
            Animable_Sub5.clientInstance = this;
            ObjectDef.clientInstance = this;
            EntityDef.clientInstance = this;
            buffer = getGameComponent().createImage(765 + extraWidth, 503 + extraHeight);
            bufferGraphics = buffer.getGraphics();
            Loader.getInstance().dispose();
            GameFrame.getInstance().focus();
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        loadingError = true;
    }

    public void drawBlackBox(int xPos, int yPos) {
        DrawingArea.drawPixels(71, yPos - 1, xPos - 2, 0x726451, 1);
        DrawingArea.drawPixels(69, yPos, xPos + 174, 0x726451, 1);
        DrawingArea.drawPixels(1, yPos - 2, xPos - 2, 0x726451, 178);
        DrawingArea.drawPixels(1, yPos + 68, xPos, 0x726451, 174);
        DrawingArea.drawPixels(71, yPos - 1, xPos - 1, 0x2E2B23, 1);
        DrawingArea.drawPixels(71, yPos - 1, xPos + 175, 0x2E2B23, 1);
        DrawingArea.drawPixels(1, yPos - 1, xPos, 0x2E2B23, 175);
        DrawingArea.drawPixels(1, yPos + 69, xPos, 0x2E2B23, 175);
        DrawingArea.method335(0, yPos, 174, 68, 220, xPos);
    }

    private void drawInterface(int j, int k, RSInterface class9, int l) {
        if (class9 == null || class9.type != 0 || class9.children == null) { return; }
        if (class9.hidden && anInt1026 != class9.id && anInt1048 != class9.id && anInt1039 != class9.id) { return; }
        int i1 = DrawingArea.topX;
        int j1 = DrawingArea.topY;
        int k1 = DrawingArea.bottomX;
        int l1 = DrawingArea.bottomY;
        DrawingArea.setDrawingArea(l + class9.height, k, k + class9.width, l);
        int i2 = class9.children.length;
        for (int j2 = 0; j2 < i2; j2++) {
            int k2 = class9.childX[j2] + k;
            int l2 = (class9.childY[j2] + l) - j;
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j2]];
            if (class9_1.hiddenChild) { continue; }
            k2 += class9_1.anInt263;
            l2 += class9_1.anInt265;
            if (class9_1.contentType > 0) { drawFriendsListOrWelcomeScreen(class9_1); }
            // here
            int[] IDs = {1196, 1199, 1206, 1215, 1224, 1231, 1240, 1249, 1258, 1267, 1274, 1283, 1573, 1290, 1299, 1308, 1315, 1324, 1333, 1340, 1349, 1358, 1367, 1374, 1381, 1388, 1397, 1404, 1583, 12038, 1414, 1421, 1430, 1437, 1446, 1453, 1460, 1469, 15878, 1602, 1613, 1624, 7456, 1478, 1485, 1494, 1503, 1512, 1521, 1530, 1544, 1553, 1563, 1593, 1635, 12426, 12436, 12446, 12456, 6004, 18471,
					/* Ancients */
                    12940, 12988, 13036, 12902, 12862, 13046, 12964, 13012, 13054, 12920, 12882, 13062, 12952, 13000, 13070, 12912, 12872, 13080, 12976, 13024, 13088, 12930, 12892, 13096};
            for (int m5 = 0; m5 < IDs.length; m5++) {
                if (class9_1.id == IDs[m5] + 1) {
                    if (m5 > 61) { drawBlackBox(k2 + 1, l2); } else { drawBlackBox(k2, l2 + 1); }
                }
            }
            int[] runeChildren = {1202, 1203, 1209, 1210, 1211, 1218, 1219, 1220, 1227, 1228, 1234, 1235, 1236, 1243, 1244, 1245, 1252, 1253, 1254, 1261, 1262, 1263, 1270, 1271, 1277, 1278, 1279, 1286, 1287, 1293, 1294, 1295, 1302, 1303, 1304, 1311, 1312, 1318, 1319, 1320, 1327, 1328, 1329, 1336, 1337, 1343, 1344, 1345, 1352, 1353, 1354, 1361, 1362, 1363, 1370, 1371, 1377, 1378, 1384, 1385, 1391, 1392, 1393, 1400, 1401, 1407, 1408, 1410, 1417, 1418, 1424, 1425, 1426, 1433, 1434, 1440, 1441, 1442, 1449, 1450, 1456, 1457, 1463, 1464, 1465, 1472, 1473, 1474, 1481, 1482, 1488, 1489, 1490, 1497, 1498, 1499, 1506, 1507, 1508, 1515, 1516, 1517, 1524, 1525, 1526, 1533, 1534, 1535, 1547, 1548, 1549, 1556, 1557, 1558, 1566, 1567, 1568, 1576, 1577, 1578, 1586, 1587, 1588, 1596, 1597, 1598, 1605, 1606, 1607, 1616, 1617, 1618, 1627, 1628, 1629, 1638, 1639, 1640, 6007, 6008, 6011, 8673, 8674, 12041, 12042, 12429, 12430, 12431, 12439, 12440, 12441, 12449, 12450, 12451, 12459, 12460, 15881, 15882, 15885, 18474, 18475, 18478};
            for (int r = 0; r < runeChildren.length; r++) {
                if (class9_1.id == runeChildren[r]) { class9_1.modelZoom = 775; }
            }
            if (class9_1.type == 0) {
                if (class9_1.scrollPosition > class9_1.scrollMax - class9_1.height) {
                    class9_1.scrollPosition = class9_1.scrollMax - class9_1.height;
                }
                if (class9_1.scrollPosition < 0) { class9_1.scrollPosition = 0; }
                drawInterface(class9_1.scrollPosition, k2, class9_1, l2);
                if (class9_1.scrollMax > class9_1.height) {
                    drawScrollbar(class9_1.height, class9_1.scrollPosition, l2, k2 + class9_1.width, class9_1.scrollMax, class9_1.scrollType);
                }
            } else if (class9_1.type != 1) {
                if (class9_1.type == 2) {
                    int i3 = 0;
                    for (int l3 = 0; l3 < class9_1.height; l3++) {
                        for (int l4 = 0; l4 < class9_1.width; l4++) {
                            int k5 = k2 + l4 * (32 + class9_1.invSpritePadX);
                            int j6 = l2 + l3 * (32 + class9_1.invSpritePadY);
                            if (i3 < 20) {
                                k5 += class9_1.spritesX[i3];
                                j6 += class9_1.spritesY[i3];
                            }
                            if (class9_1.inv[i3] > 0) {
                                int k6 = 0;
                                int j7 = 0;
                                int j9 = class9_1.inv[i3] - 1;
                                if (k5 > DrawingArea.topX - 32 && k5 < DrawingArea.bottomX && j6 > DrawingArea.topY - 32 && j6 < DrawingArea.bottomY || activeInterfaceType != 0 && anInt1085 == i3) {
                                    int l9 = 0;
                                    if (itemSelected == 1 && anInt1283 == i3 && anInt1284 == class9_1.id) {
                                        l9 = 0xffffff;
                                    }
                                    Sprite class30_sub2_sub1_sub1_2 = ItemDef.getSprite(j9, class9_1.invStackSizes[i3], l9);
                                    if (class30_sub2_sub1_sub1_2 != null) {
                                        if (activeInterfaceType != 0 && anInt1085 == i3 && anInt1084 == class9_1.id) {
                                            k6 = super.mouseX - anInt1087;
                                            j7 = super.mouseY - anInt1088;
                                            if (k6 < 5 && k6 > -5) { k6 = 0; }
                                            if (j7 < 5 && j7 > -5) { j7 = 0; }
                                            if (anInt989 < 10) {
                                                k6 = 0;
                                                j7 = 0;
                                            }
                                            class30_sub2_sub1_sub1_2.drawSprite1(k5 + k6, j6 + j7);
                                            if (((j6 + j7) < DrawingArea.topY) && (class9.scrollPosition > 0)) {
                                                int i10 = (anInt945 * (DrawingArea.topY - j6 - j7)) / 3;
                                                if (i10 > (anInt945 * 10)) {
                                                    i10 = anInt945 * 10;
                                                }
                                                if (((class9.id >= RSInterface.BANK_CONTAINER) && (class9.id <= (RSInterface.BANK_CONTAINER + 8))) && (super.scrollDelay > 0)) {
                                                    super.scrollDelay--;
                                                } else {
                                                    class9.scrollPosition -= i10;
                                                    anInt1088 += i10;
                                                    if (i10 > class9.scrollPosition) {
                                                        i10 = class9.scrollPosition;
                                                    }
                                                    class9.scrollPosition -= i10;
                                                    anInt1088 += i10;
                                                }
                                            }
                                            if (j6 + j7 < DrawingArea.topY && class9.scrollPosition > 0) {
                                                int i10 = (anInt945 * (DrawingArea.topY - j6 - j7)) / 3;
                                                if (i10 > anInt945 * 10) { i10 = anInt945 * 10; }
                                                if (i10 > class9.scrollPosition) { i10 = class9.scrollPosition; }
                                                class9.scrollPosition -= i10;
                                                anInt1088 += i10;
                                            }
                                            if (j6 + j7 + 32 > DrawingArea.bottomY && class9.scrollPosition < class9.scrollMax - class9.height) {
                                                int j10 = (anInt945 * ((j6 + j7 + 32) - DrawingArea.bottomY)) / 3;
                                                if (j10 > anInt945 * 10) { j10 = anInt945 * 10; }
                                                if (j10 > class9.scrollMax - class9.height - class9.scrollPosition) {
                                                    j10 = class9.scrollMax - class9.height - class9.scrollPosition;
                                                }
                                                class9.scrollPosition += j10;
                                                anInt1088 -= j10;
                                            }
                                        } else if (atInventoryInterfaceType != 0 && atInventoryIndex == i3 && atInventoryInterface == class9_1.id) {
                                            class30_sub2_sub1_sub1_2.drawSprite1(k5, j6);
                                        } else { class30_sub2_sub1_sub1_2.drawSprite(k5, j6); }
                                        if (class30_sub2_sub1_sub1_2.maxWidth == 33 || class9_1.invStackSizes[i3] != 1) {
                                            int k10 = class9_1.invStackSizes[i3];
                                            if (intToKOrMil(k10) == "@inf@") {
                                                infinity.drawSprite(k5 + k6, j6 + j7);
                                            } else {
                                                if (k10 >= 1) {
                                                    smallText.method385(0, intToKOrMil(k10), j6 + 10 + j7, k5 + 1 + k6);
                                                    smallText.method385(0xFFFF00, intToKOrMil(k10), j6 + 9 + j7, k5 + k6);
                                                }
                                                if (k10 >= 100000) {
                                                    smallText.method385(0, intToKOrMil(k10), j6 + 10 + j7, k5 + 1 + k6);
                                                    smallText.method385(0xFFFFFF, intToKOrMil(k10), j6 + 9 + j7, k5 + k6);
                                                }
                                                if (k10 >= 10000000) {
                                                    smallText.method385(0, intToKOrMil(k10), j6 + 10 + j7, k5 + 1 + k6);
                                                    smallText.method385(0x00ff80, intToKOrMil(k10), j6 + 9 + j7, k5 + k6);
                                                }
												/*
												 * smallText.method385(0,
												 * intToKOrMil(k10), j6 + 10 +
												 * j7, k5 + 1 + k6);
												 * smallText.method385(0xffff00,
												 * intToKOrMil(k10), j6 + 9 +
												 * j7, k5 + k6);
												 */
                                            }
                                        }
                                    }
                                }
                            } else if (class9_1.sprites != null && i3 < 20) {
                                Sprite class30_sub2_sub1_sub1_1 = class9_1.sprites[i3];
                                if (class30_sub2_sub1_sub1_1 != null) { class30_sub2_sub1_sub1_1.drawSprite(k5, j6); }
                            }
                            i3++;
                        }
                    }
                } else if (class9_1.type == 3) {
                    boolean flag = false;
                    if (anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id) {
                        flag = true;
                    }
                    int j3;
                    if (interfaceIsSelected(class9_1)) {
                        j3 = class9_1.enabledColor;
                        if (flag && class9_1.enabledHoverColor != 0) { j3 = class9_1.enabledHoverColor; }
                    } else {
                        j3 = class9_1.disabledColor;
                        if (flag && class9_1.disabledHoverColor != 0) { j3 = class9_1.disabledHoverColor; }
                    }
                    if (class9_1.opacity == 0) {
                        if (class9_1.boxFilled) {
                            DrawingArea.drawPixels(class9_1.height, l2, k2, j3, class9_1.width);
                        } else { DrawingArea.fillPixels(k2, class9_1.width, class9_1.height, j3, l2); }
                    } else if (class9_1.boxFilled) {
                        DrawingArea.method335(j3, l2, class9_1.width, class9_1.height, 256 - (class9_1.opacity & 0xff), k2);
                    } else {
                        DrawingArea.method338(l2, class9_1.height, 256 - (class9_1.opacity & 0xff), j3, class9_1.width, k2);
                    }
                } else if (class9_1.type == 4) {
                    TextDrawingArea textDrawingArea = class9_1.textDrawingAreas;
                    String s = class9_1.message;
                    boolean flag1 = false;
                    if (anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id) {
                        flag1 = true;
                    }
                    int i4;
                    if (interfaceIsSelected(class9_1)) {
                        i4 = class9_1.enabledColor;
                        if (flag1 && class9_1.enabledHoverColor != 0) { i4 = class9_1.enabledHoverColor; }
                        if (class9_1.aString228.length() > 0) { s = class9_1.aString228; }
                    } else {
                        i4 = class9_1.disabledColor;
                        if (flag1 && class9_1.disabledHoverColor != 0) { i4 = class9_1.disabledHoverColor; }
                    }
                    if (class9_1.atActionType == 6 && aBoolean1149) {
                        s = "Please wait...";
                        i4 = class9_1.disabledColor;
                    }
                    if (DrawingArea.width == 519) {
                        if (i4 == 0xffff00) { i4 = 255; }
                        if (i4 == 49152) { i4 = 0xffffff; }
                    }
                    if ((class9_1.parentID == 1151) || (class9_1.parentID == 12855)) {
                        switch (i4) {
                            case 16773120:
                                i4 = 0xFE981F;
                                break;
                            case 7040819:
                                i4 = 0xAF6A1A;
                                break;
                        }
                    }
                    for (int l6 = l2 + textDrawingArea.anInt1497; s.length() > 0; l6 += textDrawingArea.anInt1497) {
                        if (s.indexOf("%") != -1) {
                            do {
                                int k7 = s.indexOf("%1");
                                if (k7 == -1) { break; }
                                if (class9_1.id < 4000 || class9_1.id > 5000 && class9_1.id != 13921 && class9_1.id != 13922 && class9_1.id != 12171 && class9_1.id != 12172) {
                                    s = s.substring(0, k7) + methodR(extractInterfaceValues(class9_1, 0)) + s.substring(k7 + 2);
                                } else {
                                    s = s.substring(0, k7) + interfaceIntToString(extractInterfaceValues(class9_1, 0)) + s.substring(k7 + 2);
                                }
                            } while (true);
                            do {
                                int l7 = s.indexOf("%2");
                                if (l7 == -1) { break; }
                                s = s.substring(0, l7) + interfaceIntToString(extractInterfaceValues(class9_1, 1)) + s.substring(l7 + 2);
                            } while (true);
                            do {
                                int i8 = s.indexOf("%3");
                                if (i8 == -1) { break; }
                                s = s.substring(0, i8) + interfaceIntToString(extractInterfaceValues(class9_1, 2)) + s.substring(i8 + 2);
                            } while (true);
                            do {
                                int j8 = s.indexOf("%4");
                                if (j8 == -1) { break; }
                                s = s.substring(0, j8) + interfaceIntToString(extractInterfaceValues(class9_1, 3)) + s.substring(j8 + 2);
                            } while (true);
                            do {
                                int k8 = s.indexOf("%5");
                                if (k8 == -1) { break; }
                                s = s.substring(0, k8) + interfaceIntToString(extractInterfaceValues(class9_1, 4)) + s.substring(k8 + 2);
                            } while (true);
                        }
                        int l8 = s.indexOf("\\n");
                        String s1;
                        if (l8 != -1) {
                            s1 = s.substring(0, l8);
                            s = s.substring(l8 + 2);
                        } else {
                            s1 = s;
                            s = "";
                        }
                        if (class9_1.centerText) {
                            textDrawingArea.method382(i4, k2 + class9_1.width / 2, s1, l6, class9_1.textShadow);
                        } else { textDrawingArea.method389(class9_1.textShadow, k2, i4, s1, l6); }
                    }
                } else if (class9_1.type == 5) {
                    Sprite sprite;
                    if (interfaceIsSelected(class9_1)/* || hoverSprite == class9_1.id*/) {
                        sprite = class9_1.sprite2;
                    } else { sprite = class9_1.sprite1; }
                    if (sprite != null) {
                        if (spellSelected == 1 && class9_1.id == spellID && spellID != 0) {
                            sprite.Outlining(k2, l2, 0xffffff);
                        } else {
                            if (class9_1.spriteWidth == 0 && class9_1.spriteHeight == 0) {
                                if (class9_1.advancedSprite) { sprite.drawAdvancedSprite(k2, l2); } else {
                                    sprite.drawSprite(k2, l2);
                                }
                            } else {
                                sprite.drawAdvancedSprite(k2, l2, class9_1.spriteWidth, class9_1.spriteHeight);
                            }
                        }
                    }
                } else if (class9_1.type == 6) {
                    int k3 = Texture.center_x;
                    int j4 = Texture.center_y;
                    Texture.center_x = k2 + class9_1.width / 2;
                    Texture.center_y = l2 + class9_1.height / 2;
                    int i5 = Texture.SINE[class9_1.modelRotation1] * class9_1.modelZoom >> 16;
                    int l5 = Texture.COSINE[class9_1.modelRotation1] * class9_1.modelZoom >> 16;
                    boolean flag2 = interfaceIsSelected(class9_1);
                    int i7;
                    if (flag2) { i7 = class9_1.anInt258; } else { i7 = class9_1.anInt257; }
                    Model model;
                    if (i7 == -1) {
                        model = class9_1.method209(-1, -1, flag2);
                    } else {
                        Animation animation = Animation.anims[i7];
                        model = class9_1.method209(animation.anIntArray354[class9_1.anInt246], animation.anIntArray353[class9_1.anInt246], flag2);
                    }
                    if (model != null) {
                        model.method482(class9_1.modelRotation2, 0, class9_1.modelRotation1, 0, i5, l5);
                    }
                    Texture.center_x = k3;
                    Texture.center_y = j4;
                } else if (class9_1.type == 7) {
                    TextDrawingArea textDrawingArea_1 = class9_1.textDrawingAreas;
                    int k4 = 0;
                    for (int j5 = 0; j5 < class9_1.height; j5++) {
                        for (int i6 = 0; i6 < class9_1.width; i6++) {
                            if (class9_1.inv[k4] > 0) {
                                ItemDef itemDef = ItemDef.forID(class9_1.inv[k4] - 1);
                                String s2 = itemDef.name;
                                if (itemDef.stack || class9_1.invStackSizes[k4] != 1) {
                                    s2 = s2 + " x" + intToKOrMilLongName(class9_1.invStackSizes[k4]);
                                }
                                int i9 = k2 + i6 * (115 + class9_1.invSpritePadX);
                                int k9 = l2 + j5 * (12 + class9_1.invSpritePadY);
                                if (class9_1.centerText) {
                                    textDrawingArea_1.method382(class9_1.disabledColor, i9 + class9_1.width / 2, s2, k9, class9_1.textShadow);
                                } else {
                                    textDrawingArea_1.method389(class9_1.textShadow, i9, class9_1.disabledColor, s2, k9);
                                }
                            }
                            k4++;
                        }
                    }
                } else if (class9_1.type == 8) {
                    String hoverText;
                    if (interfaceIsSelected(class9_1)) {
                        hoverText = class9_1.aString228;
                    } else { hoverText = class9_1.message; }
                    drawHoverBox(k2, l2, hoverText);
                    // drawHoverBox(k2, l2, class9_1.popupString);

                } else if (class9_1.type == 9 && (anInt1500 == class9_1.id || anInt1044 == class9_1.id || anInt1129 == class9_1.id) && !menuOpen) {
                    int boxWidth = 0;
                    int boxHeight = 0;
                    TextDrawingArea textDrawingArea_2 = smallText;
                    for (String s1 = class9_1.message; s1.length() > 0; ) {
                        int l7 = s1.indexOf("\\n");
                        String s4;
                        if (l7 != -1) {
                            s4 = s1.substring(0, l7);
                            s1 = s1.substring(l7 + 2);
                        } else {
                            s4 = s1;
                            s1 = "";
                        }
                        int j10 = textDrawingArea_2.getTextWidth(s4);
                        if (j10 > boxWidth) {
                            boxWidth = j10;
                        }
                        boxHeight += textDrawingArea_2.anInt1497 + 1;
                    }
                    boxWidth += 6;
                    boxHeight += 7;
                    int xPos = (k2 + class9_1.width) - 5 - boxWidth;
                    int yPos = l2 + class9_1.height + 5;
                    if (xPos < k2 + 5) {
                        xPos = k2 + 5;
                    }
                    if (xPos + boxWidth > k + class9.width) {
                        xPos = (k + class9.width) - boxWidth;
                    }
                    if (yPos + boxHeight > l + class9.height) {
                        yPos = (l + class9.height) - boxHeight;
                    }
                    if (class9_1.inventoryHover) {
                        if (xPos + boxWidth + k > 249) {
                            xPos = 251 - boxWidth - k;
                        }
                        if (yPos + boxHeight + l > 261) {
                            yPos = 245 - boxHeight - l;
                        }
                    }
                    DrawingArea.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0, boxWidth);
                    DrawingArea.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
                    String s2 = class9_1.message;
                    for (int j11 = yPos + textDrawingArea_2.anInt1497 + 2; s2.length() > 0; j11 += textDrawingArea_2.anInt1497 + 1) {
                        int l11 = s2.indexOf("\\n");
                        String s5;
                        if (l11 != -1) {
                            s5 = s2.substring(0, l11);
                            s2 = s2.substring(l11 + 2);
                        } else {
                            s5 = s2;
                            s2 = "";
                        }
                        textDrawingArea_2.method389(false, xPos + 3, 0, s5, j11);
                    }
                }
            }
        }
        DrawingArea.setDrawingArea(l1, i1, k1, j1);
    }

    private void method107(int i, int j, Stream stream, Player player) {
        if ((i & 0x400) != 0) {
            player.anInt1543 = stream._readByteS();
            player.anInt1545 = stream._readByteS();
            player.anInt1544 = stream._readByteS();
            player.anInt1546 = stream._readByteS();
            player.anInt1547 = stream.readLEShortA() + loopCycle;
            player.anInt1548 = stream._readShortA() + loopCycle;
            player.anInt1549 = stream._readByteS();
            player.method446();
        }
        if ((i & 0x100) != 0) {
            player.anInt1520 = stream.readLEShort();
            int k = stream.readInt();
            player.anInt1524 = k >> 16;
            player.anInt1523 = loopCycle + (k & 0xffff);
            player.anInt1521 = 0;
            player.anInt1522 = 0;
            if (player.anInt1523 > loopCycle) { player.anInt1521 = -1; }
            if (player.anInt1520 == 65535) { player.anInt1520 = -1; }
        }
        if ((i & 8) != 0) {
            int l = stream.readLEShort();
            if (l == 65535) { l = -1; }
            int i2 = stream._readByteC();
            if (l == player.anim && l != -1) {
                int i3 = Animation.anims[l].anInt365;
                if (i3 == 1) {
                    player.anInt1527 = 0;
                    player.anInt1528 = 0;
                    player.anInt1529 = i2;
                    player.anInt1530 = 0;
                }
                if (i3 == 2) { player.anInt1530 = 0; }
            } else if (l == -1 || player.anim == -1 || Animation.anims[l].anInt359 >= Animation.anims[player.anim].anInt359) {
                player.anim = l;
                player.anInt1527 = 0;
                player.anInt1528 = 0;
                player.anInt1529 = i2;
                player.anInt1530 = 0;
                player.anInt1542 = player.smallXYIndex;
            }
        }
        if ((i & 4) != 0) {
            player.textSpoken = stream.readString();
            if (player.textSpoken.charAt(0) == '~') {
                player.textSpoken = player.textSpoken.substring(1);
                pushMessage(player.textSpoken, 2, (Configuration.getInstance().getValue(Titles.class) ? player.title + " @bla@" : "") + player.name);
            } else if (player == myPlayer) {
                pushMessage(player.textSpoken, 2, (Configuration.getInstance().getValue(Titles.class) ? player.title + " @bla@" : "") + player.name);
            }
            player.anInt1513 = 0;
            player.anInt1531 = 0;
            player.textCycle = 150;
        }
        if ((i & 0x80) != 0) {
            // right fucking here
            int i1 = stream.readLEShort();
            int j2 = stream.readUByte();
            int j3 = stream._readByteC();
            int k3 = stream.currentOffset;
            if (player.name != null && player.visible) {
                long l3 = TextClass.longForName(player.name);
                boolean flag = false;
                if (j2 <= 7) {
                    for (int i4 = 0; i4 < ignoreCount; i4++) {
                        if (ignoreListAsLongs[i4] != l3) { continue; }
                        flag = true;
                        break;
                    }

                }
                if (!flag && anInt1251 == 0) {
                    try {
                        aStream_834.currentOffset = 0;
                        stream.readReverse(j3, 0, aStream_834.payload);
                        aStream_834.currentOffset = 0;
                        String s = TextInput.method525(j3, aStream_834);

                        // s = Censor.doCensor(s);
                        player.textSpoken = s;
                        player.anInt1513 = i1 >> 8;
                        player.privelage = j2;
                        player.anInt1531 = i1 & 0xff;
                        player.textCycle = 150;
                        if (j2 > 0) {
                            pushMessage(s, 1, (Configuration.getInstance().getValue(Titles.class) ? player.title + " @bla@" : "") + player.name, j2);
                        } else {
                            pushMessage(s, 2, (Configuration.getInstance().getValue(Titles.class) ? player.title + " @bla@" : "") + player.name);
                        }
                    } catch (Exception exception) {
                        // Signlink.reporterror("cde2");
                    }
                }
            }
            stream.currentOffset = k3 + j3;
        }
        if ((i & 1) != 0) {
            player.interactingEntity = stream.readLEShort();
            if (player.interactingEntity == 65535) { player.interactingEntity = -1; }
        }
        if ((i & 0x10) != 0) {
            int j1 = stream._readByteC();
            byte abyte0[] = new byte[j1];
            Stream stream_1 = new Stream(abyte0);
            stream.readBytes(j1, 0, abyte0);
            aStreamArray895s[j] = stream_1;
            player.updatePlayer(stream_1);
        }
        if ((i & 2) != 0) {
            player.anInt1538 = stream.readLEShortA();
            player.anInt1539 = stream.readLEShort();
        }
        if ((i & 0x20) != 0) {
            int k1 = stream._readShortA();
            int k2 = stream.readByteA();//here
            int type1 = stream.readUByte();

            //System.out.println("k1 is :" + k1 + " and k2 is " + k2);
            //System.out.println("Type 1[B] :" + type1);
            //System.out.println("k1 is :" + k1 + " and k2 is " + k2);
            if (type1 >= 5) {
                type1 -= 5;
                //System.out.println("CRitical[B] is now " + type1);
                critical = true;
                //pushMessage("Critical! 1",0, "");
            } else {
                critical = false;
            }
            if (Configuration.getInstance().getValue(Hitmarks.class)) {
                player.updateHitData(k2, k1, type1, loopCycle);
            } else { player.updateHitData(k2, k1, loopCycle); }
            player.loopCycleStatus = loopCycle + 300;
            player.currentHealth = stream._readByteC();
            player.maxHealth = stream.readUByte();
        }
        if ((i & 0x200) != 0) {
            int l1 = stream._readShortA();
            int l2 = stream._readByteS();
            int type1 = stream.readUByte();

            //System.out.println("Type 1[D] :" + type1);
            //System.out.println("l1 is :" + l1 + " and l2 is " + l2);
            if (!critical) {
                if (type1 >= 5) {
                    type1 -= 5;
                    //System.out.println("Critical[D] is now " + type1);
                    critical = true;
                    //pushMessage("Critical! 2",0, "");
                } else {
                    critical = false;
                }
            }
            if (Configuration.getInstance().getValue(Hitmarks.class)) {
                player.updateHitData(l2, l1, type1, loopCycle);
            } else {
                player.updateHitData(l2, l1, loopCycle);
            }
            player.loopCycleStatus = loopCycle + 300;
            player.currentHealth = stream.readUByte();
            player.maxHealth = stream._readByteC();
        }
    }

    private void method108() {
        try {
            int j = myPlayer.x + anInt1278;
            int k = myPlayer.y + anInt1131;
            if (anInt1014 - j < -500 || anInt1014 - j > 500 || anInt1015 - k < -500 || anInt1015 - k > 500) {
                anInt1014 = j;
                anInt1015 = k;
            }
            if (anInt1014 != j) { anInt1014 += (j - anInt1014) / 16; }
            if (anInt1015 != k) { anInt1015 += (k - anInt1015) / 16; }
            if (super.keyArray[1] == 1) { anInt1186 += (-24 - anInt1186) / 2; } else if (super.keyArray[2] == 1) {
                anInt1186 += (24 - anInt1186) / 2;
            } else { anInt1186 /= 2; }
            if (super.keyArray[3] == 1) { anInt1187 += (12 - anInt1187) / 2; } else if (super.keyArray[4] == 1) {
                anInt1187 += (-12 - anInt1187) / 2;
            } else { anInt1187 /= 2; }
            minimapInt1 = minimapInt1 + anInt1186 / 2 & 0x7ff;
            anInt1184 += anInt1187 / 2;
            if (anInt1184 < 128) { anInt1184 = 128; }
            if (anInt1184 > 383) { anInt1184 = 383; }
            int l = anInt1014 >> 7;
            int i1 = anInt1015 >> 7;
            int j1 = method42(plane, anInt1015, anInt1014);
            int k1 = 0;
            if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
                for (int l1 = l - 4; l1 <= l + 4; l1++) {
                    for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
                        int l2 = plane;
                        if (l2 < 3 && (byteGroundArray[1][l1][k2] & 2) == 2) { l2++; }
                        int i3 = j1 - intGroundArray[l2][l1][k2];
                        if (i3 > k1) { k1 = i3; }
                    }

                }

            }
            anInt1005++;
            if (anInt1005 > 1512) {
                anInt1005 = 0;
                stream.createFrame(77);
                stream.writeByte(0);
                int i2 = stream.currentOffset;
                stream.writeByte((int) (Math.random() * 256D));
                stream.writeByte(101);
                stream.writeByte(233);
                stream.writeShort(45092);
                if ((int) (Math.random() * 2D) == 0) { stream.writeShort(35784); }
                stream.writeByte((int) (Math.random() * 256D));
                stream.writeByte(64);
                stream.writeByte(38);
                stream.writeShort((int) (Math.random() * 65536D));
                stream.writeShort((int) (Math.random() * 65536D));
                stream.writeByteC(stream.currentOffset - i2);
            }
            int j2 = k1 * 192;
            if (j2 > 0x17f00) { j2 = 0x17f00; }
            if (j2 < 32768) { j2 = 32768; }
            if (j2 > anInt984) {
                anInt984 += (j2 - anInt984) / 24;
                return;
            }
            if (j2 < anInt984) {
                anInt984 += (j2 - anInt984) / 80;
            }
        } catch (Exception _ex) {
            Signlink.reporterror("glfc_ex " + myPlayer.x + "," + myPlayer.y + "," + anInt1014 + "," + anInt1015 + "," + anInt1069 + "," + anInt1070 + "," + baseX + "," + baseY);
            throw new RuntimeException("eek");
        }
    }

    private boolean isFriendOrSelf(String s) {
        if (s == null) { return false; }
        for (int i = 0; i < friendsCount; i++) { if (s.equalsIgnoreCase(friendsList[i])) { return true; } }
        return s.equalsIgnoreCase(myPlayer.name);
    }

    private boolean isIgnore(final String s) {
        for (int j = 0; j < ignoreCount; j++) {
            if (ignoreListAsLongs[j] == TextClass.longForName(s)) {
                return true;
            }
        }
        return false;
    }

    private void draw3dScreen() {
        alertHandler.processAlerts();
        int xs = isFullScreen ? extraWidth + 200 : 0;
        int ys = isFullScreen ? extraHeight - 200 : 0;

        int xPo3s = isFullScreen ? (clientWidth / 2) - 256 : 0;
        int yPo3s = isFullScreen ? (clientHeight / 2) - 167 : 0;

        int xs2 = isFullScreen ? 200 + extraWidth : 0;
        int ys2 = isFullScreen ? extraHeight - 450 : 0;
        drawSplitPrivateChat();
        if (crossType == 1) {
            crosses[crossIndex / 100].drawSprite(crossX - 8 - 4, crossY - 8 - 4);
            anInt1142++;
            if (anInt1142 > 67) {
                anInt1142 = 0;
                stream.createFrame(78);
            }
        }
        if (crossType == 2) { crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - 4, crossY - 8 - 4); }
        if (anInt1018 != -1 && anInt1018 != 21119 && anInt1018 != 21100) {
            method119(anInt945, anInt1018);
            drawInterface(0, 0 + xs2, RSInterface.interfaceCache[anInt1018], 0 + ys2);
        }
        if (anInt1018 == 21119 || anInt1018 == 21100) {
            method119(anInt945, anInt1018);
            drawInterface(0, 0, RSInterface.interfaceCache[anInt1018], 0);
        }
        if (openInterfaceID != -1) {
            method119(anInt945, openInterfaceID);
            drawInterface(0, xPo3s, RSInterface.interfaceCache[openInterfaceID], yPo3s);
        }
        method70();
        if (!menuOpen) {
            processRightClick();
            drawTooltip();
        } else if (menuScreenArea == 0) { drawMenu(); }
        if (anInt1055 == 1 && !isFullScreen) { multiOverlay.drawSprite(472, 296); }
        if (anInt1055 == 1 && isFullScreen) { multiOverlay2.drawSprite(472 + xs, 296 + ys); }
        if (Configuration.getInstance().getValue(FPS.class)) {
            char c = '\u01FB';
            int k = 20;
            int i1 = 0xffff00;
            if (super.fps < 15) { i1 = 0xff0000; }
            aTextDrawingArea_1271.method380("Fps:" + super.fps, c, i1, k);
            k += 15;
            Runtime runtime = Runtime.getRuntime();
            int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
            i1 = 0xffff00;
            if (j1 > 0x2000000 && lowMem) { i1 = 0xff0000; }
            aTextDrawingArea_1271.method380("Mem:" + j1 + "k", c, 0xffff00, k);
            k += 15;
        }
        int i1 = 0xffff00;
        int x = baseX + (myPlayer.x - 6 >> 7);
        int y = baseY + (myPlayer.y - 6 >> 7);
        if (Configuration.getInstance().getValue(ClientData.class)) {
            char c = '\u01FB';
            int k = 20;
            if (super.fps < 15) { i1 = 0xff0000; }
            aTextDrawingArea_1271.method385(0xffff00, "Fps: " + super.fps, 285, 5);
            Runtime runtime = Runtime.getRuntime();
            int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
            i1 = 0xffff00;
            if (j1 > 0x2000000 && lowMem) { i1 = 0xff0000; }
            k += 15;
            aTextDrawingArea_1271.method385(0xffff00, "Mem: " + j1 + "k", 299, 5);
            aTextDrawingArea_1271.method385(0xffff00, "Mouse X: " + super.mouseX + " , Mouse Y: " + super.mouseY, 314, 5);
            aTextDrawingArea_1271.method385(0xffff00, "Coords: " + x + ", " + y, 329, 5);
        }
        if (anInt1104 != 0) {
            int j = anInt1104 / 50;
            int l = j / 60;
            j %= 60;
            if (j < 10) {
                aTextDrawingArea_1271.method385(0xffff00, "System update in: " + l + ":0" + j, 329, 4);
            } else { aTextDrawingArea_1271.method385(0xffff00, "System update in: " + l + ":" + j, 329, 4); }
            anInt849++;
            if (anInt849 > 75) {
                anInt849 = 0;
                stream.createFrame(148);
            }
        }
    }

    private void addIgnore(long l) {
        try {
            if (l == 0L) { return; }
            if (ignoreCount >= 100) {
                pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
                return;
            }
            String s = TextClass.fixName(TextClass.nameForLong(l));
            for (int j = 0; j < ignoreCount; j++) {
                if (ignoreListAsLongs[j] == l) {
                    pushMessage(s + " is already on your ignore list", 0, "");
                    return;
                }
            }
            for (int k = 0; k < friendsCount; k++) {
                if (friendsListAsLongs[k] == l) {
                    pushMessage("Please remove " + s + " from your friend list first", 0, "");
                    return;
                }
            }

            ignoreListAsLongs[ignoreCount++] = l;
            needDrawTabArea = true;
            stream.createFrame(133);
            stream.writeLong(l);
            return;
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("45688, " + l + ", " + 4 + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private void method114() {
        for (int i = -1; i < playerCount; i++) {
            int j;
            if (i == -1) { j = myPlayerIndex; } else { j = playerIndices[i]; }
            Player player = playerArray[j];
            if (player != null) { method96(player); }
        }

    }

    private void method115() {
        if (loadingStage == 2) {
            for (Class30_Sub1 class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetFirst(); class30_sub1 != null; class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetNext()) {
                if (class30_sub1.anInt1294 > 0) { class30_sub1.anInt1294--; }
                if (class30_sub1.anInt1294 == 0) {
                    if (class30_sub1.anInt1299 < 0 || ObjectManager.method178(class30_sub1.anInt1299, class30_sub1.anInt1301)) {
                        method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1300, class30_sub1.anInt1301, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1299);
                        class30_sub1.unlink();
                    }
                } else {
                    if (class30_sub1.anInt1302 > 0) { class30_sub1.anInt1302--; }
                    if (class30_sub1.anInt1302 == 0 && class30_sub1.anInt1297 >= 1 && class30_sub1.anInt1298 >= 1 && class30_sub1.anInt1297 <= 102 && class30_sub1.anInt1298 <= 102 && (class30_sub1.anInt1291 < 0 || ObjectManager.method178(class30_sub1.anInt1291, class30_sub1.anInt1293))) {
                        method142(class30_sub1.anInt1298, class30_sub1.anInt1295, class30_sub1.anInt1292, class30_sub1.anInt1293, class30_sub1.anInt1297, class30_sub1.anInt1296, class30_sub1.anInt1291);
                        class30_sub1.anInt1302 = -1;
                        if (class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1299 == -1) {
                            class30_sub1.unlink();
                        } else if (class30_sub1.anInt1291 == class30_sub1.anInt1299 && class30_sub1.anInt1292 == class30_sub1.anInt1300 && class30_sub1.anInt1293 == class30_sub1.anInt1301) {
                            class30_sub1.unlink();
                        }
                    }
                }
            }

        }
    }

    // stops the click from going over sprite
    private void method117(Stream stream) {
        stream.initBitAccess();
        int j = stream.readBits(1);
        if (j == 0) { return; }
        int k = stream.readBits(2);
        if (k == 0) {
            anIntArray894[anInt893++] = myPlayerIndex;
            return;
        }
        if (k == 1) {
            int l = stream.readBits(3);
            myPlayer.moveInDir(false, l);
            int k1 = stream.readBits(1);
            if (k1 == 1) { anIntArray894[anInt893++] = myPlayerIndex; }
            return;
        }
        if (k == 2) {
            int i1 = stream.readBits(3);
            myPlayer.moveInDir(true, i1);
            int l1 = stream.readBits(3);
            myPlayer.moveInDir(true, l1);
            int j2 = stream.readBits(1);
            if (j2 == 1) { anIntArray894[anInt893++] = myPlayerIndex; }
            return;
        }
        if (k == 3) {
            plane = stream.readBits(2);
            int j1 = stream.readBits(1);
            int i2 = stream.readBits(1);
            if (i2 == 1) { anIntArray894[anInt893++] = myPlayerIndex; }
            int k2 = stream.readBits(7);
            int l2 = stream.readBits(7);
            myPlayer.setPos(l2, k2, j1 == 1);
        }
    }

    private boolean method119(int i, int j) {
        boolean flag1 = false;
        RSInterface class9 = RSInterface.interfaceCache[j];
        if (class9 == null || class9.children == null) { return false; }
        for (int k = 0; k < class9.children.length; k++) {
            if (class9.children[k] == -1) { break; }
            RSInterface class9_1 = RSInterface.interfaceCache[class9.children[k]];
            if (class9_1.type == 1) { flag1 |= method119(i, class9_1.id); }
            if (class9_1.type == 6 && (class9_1.anInt257 != -1 || class9_1.anInt258 != -1)) {
                boolean flag2 = interfaceIsSelected(class9_1);
                int l;
                if (flag2) { l = class9_1.anInt258; } else { l = class9_1.anInt257; }
                if (l != -1) {
                    Animation animation = Animation.anims[l];
                    for (class9_1.anInt208 += i; class9_1.anInt208 > animation.method258(class9_1.anInt246); ) {
                        class9_1.anInt208 -= animation.method258(class9_1.anInt246) + 1;
                        class9_1.anInt246++;
                        if (class9_1.anInt246 >= animation.anInt352) {
                            class9_1.anInt246 -= animation.anInt356;
                            if (class9_1.anInt246 < 0 || class9_1.anInt246 >= animation.anInt352) {
                                class9_1.anInt246 = 0;
                            }
                        }
                        flag1 = true;
                    }

                }
            }
        }

        return flag1;
    }

    private int method120() {
        int j = 3;
        if (yCameraCurve < 310) {
            int k = xCameraPos >> 7;
            int l = yCameraPos >> 7;
            int i1 = myPlayer.x >> 7;
            int j1 = myPlayer.y >> 7;
            if ((byteGroundArray[plane][k][l] & 4) != 0) { j = plane; }
            int k1;
            if (i1 > k) { k1 = i1 - k; } else { k1 = k - i1; }
            int l1;
            if (j1 > l) { l1 = j1 - l; } else { l1 = l - j1; }
            if (k1 > l1) {
                int i2 = (l1 * 0x10000) / k1;
                int k2 = 32768;
                while (k != i1) {
                    if (k < i1) { k++; } else if (k > i1) { k--; }
                    if ((byteGroundArray[plane][k][l] & 4) != 0) { j = plane; }
                    k2 += i2;
                    if (k2 >= 0x10000) {
                        k2 -= 0x10000;
                        if (l < j1) { l++; } else if (l > j1) { l--; }
                        if ((byteGroundArray[plane][k][l] & 4) != 0) { j = plane; }
                    }
                }
            } else {
                int j2 = (k1 * 0x10000) / l1;
                int l2 = 32768;
                while (l != j1) {
                    if (l < j1) { l++; } else if (l > j1) { l--; }
                    if ((byteGroundArray[plane][k][l] & 4) != 0) { j = plane; }
                    l2 += j2;
                    if (l2 >= 0x10000) {
                        l2 -= 0x10000;
                        if (k < i1) { k++; } else if (k > i1) { k--; }
                        if ((byteGroundArray[plane][k][l] & 4) != 0) { j = plane; }
                    }
                }
            }
        }
        if ((byteGroundArray[plane][myPlayer.x >> 7][myPlayer.y >> 7] & 4) != 0) { j = plane; }
        return j;
    }

    private int method121() {
        int j = method42(plane, yCameraPos, xCameraPos);
        if (j - zCameraPos < 800 && (byteGroundArray[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0) {
            return plane;
        } else { return 3; }
    }

    private void delIgnore(long l) {
        try {
            if (l == 0L) { return; }
            for (int j = 0; j < ignoreCount; j++) {
                if (ignoreListAsLongs[j] == l) {
                    ignoreCount--;
                    needDrawTabArea = true;
                    System.arraycopy(ignoreListAsLongs, j + 1, ignoreListAsLongs, j, ignoreCount - j);

                    stream.createFrame(74);
                    stream.writeLong(l);
                    return;
                }
            }

            return;
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    private void chatJoin(long l) {
        try {
            if (l == 0L) { return; }
            stream.createFrame(60);
            stream.writeLong(l);
            return;
        } catch (RuntimeException runtimeexception) {
            Signlink.reporterror("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();

    }

    private int extractInterfaceValues(RSInterface class9, int j) {
        if (class9.valueIndexArray == null || j >= class9.valueIndexArray.length) { return -2; }
        try {
            int ai[] = class9.valueIndexArray[j];
            int k = 0;
            int l = 0;
            int i1 = 0;
            do {
                int j1 = ai[l++];
                int k1 = 0;
                byte byte0 = 0;
                if (j1 == 0) { return k; }
                if (j1 == 1) { k1 = currentStats[ai[l++]]; }
                if (j1 == 2) { k1 = maxStats[ai[l++]]; }
                if (j1 == 3) { k1 = currentExp[ai[l++]]; }
                if (j1 == 4) {
                    RSInterface class9_1 = RSInterface.interfaceCache[ai[l++]];
                    int k2 = ai[l++];
                    if (k2 >= 0 && k2 < ItemDef.totalItems && (!ItemDef.forID(k2).membersObject || isMembers)) {
                        for (int j3 = 0; j3 < class9_1.inv.length; j3++) {
                            if (class9_1.inv[j3] == k2 + 1) { k1 += class9_1.invStackSizes[j3]; }
                        }

                    }
                }
                if (j1 == 5) { k1 = variousSettings[ai[l++]]; }
                if (j1 == 6) { k1 = anIntArray1019[ai[l++] - 1]; }
                if (j1 == 7) { k1 = (variousSettings[ai[l++]] * 100) / 46875; }
                if (j1 == 8) { k1 = myPlayer.combatLevel; }
                if (j1 == 9) {
                    for (int l1 = 0; l1 < Skills.SKILLS_COUNT; l1++) {
                        if (Skills.SKILL_ENABLED[l1]) { k1 += maxStats[l1]; }
                    }

                }
                if (j1 == 10) {
                    RSInterface class9_2 = RSInterface.interfaceCache[ai[l++]];
                    int l2 = ai[l++] + 1;
                    if (l2 >= 0 && l2 < ItemDef.totalItems && (!ItemDef.forID(l2).membersObject || isMembers)) {
                        for (int k3 = 0; k3 < class9_2.inv.length; k3++) {
                            if (class9_2.inv[k3] != l2) { continue; }
                            k1 = 0x3b9ac9ff;
                            break;
                        }

                    }
                }
                if (j1 == 11) { k1 = energy; }
                if (j1 == 12) { k1 = weight; }
                if (j1 == 13) {
                    int i2 = variousSettings[ai[l++]];
                    int i3 = ai[l++];
                    k1 = (i2 & 1 << i3) == 0 ? 0 : 1;
                }
                if (j1 == 14) {
                    int j2 = ai[l++];
                    VarBit varBit = VarBit.cache[j2];
                    int l3 = varBit.anInt648;
                    int i4 = varBit.anInt649;
                    int j4 = varBit.anInt650;
                    int k4 = anIntArray1232[j4 - i4];
                    k1 = variousSettings[l3] >> i4 & k4;
                }
                if (j1 == 15) { byte0 = 1; }
                if (j1 == 16) { byte0 = 2; }
                if (j1 == 17) { byte0 = 3; }
                if (j1 == 18) { k1 = (myPlayer.x >> 7) + baseX; }
                if (j1 == 19) { k1 = (myPlayer.y >> 7) + baseY; }
                if (j1 == 20) { k1 = ai[l++]; }
                if (byte0 == 0) {
                    if (i1 == 0) { k += k1; }
                    if (i1 == 1) { k -= k1; }
                    if (i1 == 2 && k1 != 0) { k /= k1; }
                    if (i1 == 3) { k *= k1; }
                    i1 = 0;
                } else {
                    i1 = byte0;
                }
            } while (true);
        } catch (Exception _ex) {
            return -1;
        }
    }

    private void drawTooltip() {
        if (menuActionRow < 2 && itemSelected == 0 && spellSelected == 0) { return; }
        String s;
        if (itemSelected == 1 && menuActionRow < 2) {
            s = "Use " + selectedItemName + " with...";
        } else if (spellSelected == 1 && menuActionRow < 2) { s = spellTooltip + "..."; } else {
            s = menuActionName[menuActionRow - 1];
        }
        if (menuActionRow > 2) { s = s + "@whi@ / " + (menuActionRow - 2) + " more options"; }
        chatTextDrawingArea.method390(4, 0xffffff, s, loopCycle / 1000, 15);
    }

    private void drawMinimap() {
        int x = isFullScreen ? 549 + extraWidth : 0;
        int markY = isFullScreen ? 6 : 0;
        int markX = isFullScreen ? 9 : 0;
        if (!isFullScreen) { aRSImageProducer_1164.initializeDrawingArea(); }
        if (anInt1021 == 2) {
            byte abyte0[] = mapBack.aByteArray1450;
            int ai[] = DrawingArea.pixels;
            int k2 = abyte0.length;
            for (int i5 = 0; i5 < k2; i5++) { if (abyte0[i5] == 0) { ai[i5] = 0; } }
            aRSImageProducer_1165.initializeDrawingArea();
            return;
        }
        int i = minimapInt1 + minimapInt2 & 0x7ff;
        int j = 48 + myPlayer.x / 32;
        int l2 = 464 - myPlayer.y / 32;
        if (!isFullScreen) {
            aClass30_Sub2_Sub1_Sub1_1263.method352(152, i, anIntArray1229, 256 + minimapInt3, anIntArray1052, l2, 10, 32, 146, j);
        }
        if (isFullScreen) {
            aClass30_Sub2_Sub1_Sub1_1263.method352(151, i, anIntArray1229, 256 + minimapInt3, anIntArray1052, l2, 12, 34 + x, 146, j);
        }
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
            aClass30_Sub2_Sub1_Sub1_1263.method352(151, i, anIntArray1229, 256 + minimapInt3, anIntArray1052, l2, 9, 34 + x, 146, j);
        }
        if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
            aClass30_Sub2_Sub1_Sub1_1263.method352(151, i, anIntArray1229, 256 + minimapInt3, anIntArray1052, l2, 9, 55, 146, j);
        }
        for (int j5 = 0; j5 < anInt1071; j5++) {
            int k = (anIntArray1072[j5] * 4 + 2) - myPlayer.x / 32;
            int i3 = (anIntArray1073[j5] * 4 + 2) - myPlayer.y / 32;
            markMinimap(aClass30_Sub2_Sub1_Sub1Array1140[j5], k, i3);
        }

        for (int k5 = 0; k5 < 104; k5++) {
            for (int l5 = 0; l5 < 104; l5++) {
                NodeList class19 = groundArray[plane][k5][l5];
                if (class19 != null) {
                    int l = (k5 * 4 + 2) - myPlayer.x / 32;
                    int j3 = (l5 * 4 + 2) - myPlayer.y / 32;
                    markMinimap(mapDotItem, l, j3);
                }
            }
        }

        for (int i6 = 0; i6 < npcCount; i6++) {
            NPC npc = npcArray[npcIndices[i6]];
            if (npc != null && npc.isVisible()) {
                EntityDef entityDef = npc.desc;
                if (entityDef.childrenIDs != null) { entityDef = entityDef.method161(); }
                if (entityDef != null && entityDef.aBoolean87 && entityDef.aBoolean84) {
                    int i1 = npc.x / 32 - myPlayer.x / 32;
                    int k3 = npc.y / 32 - myPlayer.y / 32;
                    markMinimap(mapDotNPC, i1, k3);
                }
            }
        }

        for (int j6 = 0; j6 < playerCount; j6++) {
            Player player = playerArray[playerIndices[j6]];
            if (player != null && player.isVisible()) {
                int j1 = player.x / 32 - myPlayer.x / 32;
                int l3 = player.y / 32 - myPlayer.y / 32;
                boolean flag1 = false;
                boolean flag3 = false;
                for (int j3 = 0; j3 < clanList.length; j3++) {
                    if (clanList[j3] == null) { continue; }
                    String s = clanList[j3];
                    if (s.startsWith("[")) {
                        s = s.substring(s.indexOf("]") + 1, s.length());
                    }
                    if (!s.equalsIgnoreCase(player.name)) { continue; }
                    flag3 = true;
                    break;
                }
                long l6 = TextClass.longForName(player.name);
                for (int k6 = 0; k6 < friendsCount; k6++) {
                    if (l6 != friendsListAsLongs[k6] || friendsNodeIDs[k6] == 0) { continue; }
                    flag1 = true;
                    break;
                }
                boolean flag2 = false;
                if (myPlayer.team != 0 && player.team != 0 && myPlayer.team == player.team) { flag2 = true; }
                if (flag1) { markMinimap(mapDotFriend, j1, l3); } else if (flag3) {
                    markMinimap(mapDotClan, j1, l3);
                } else if (flag2) {
                    markMinimap(mapDotTeam, j1, l3);
                } else { markMinimap(mapDotPlayer, j1, l3); }
            }
        }

        if (anInt855 != 0 && loopCycle % 20 < 10) {
            if (anInt855 == 1 && anInt1222 >= 0 && anInt1222 < npcArray.length) {
                NPC class30_sub2_sub4_sub1_sub1_1 = npcArray[anInt1222];
                if (class30_sub2_sub4_sub1_sub1_1 != null) {
                    int k1 = class30_sub2_sub4_sub1_sub1_1.x / 32 - myPlayer.x / 32;
                    int i4 = class30_sub2_sub4_sub1_sub1_1.y / 32 - myPlayer.y / 32;
                    method81(mapMarker, i4, k1);
                }
            }
            if (anInt855 == 2) {
                int l1 = ((anInt934 - baseX) * 4 + 2) - myPlayer.x / 32;
                int j4 = ((anInt935 - baseY) * 4 + 2) - myPlayer.y / 32;
                method81(mapMarker, j4, l1);
            }
            if (anInt855 == 10 && anInt933 >= 0 && anInt933 < playerArray.length) {
                Player class30_sub2_sub4_sub1_sub2_1 = playerArray[anInt933];
                if (class30_sub2_sub4_sub1_sub2_1 != null) {
                    int i2 = class30_sub2_sub4_sub1_sub2_1.x / 32 - myPlayer.x / 32;
                    int k4 = class30_sub2_sub4_sub1_sub2_1.y / 32 - myPlayer.y / 32;
                    method81(mapMarker, k4, i2);
                }
            }
        }
        if (destX != 0) {
            int j2 = (destX * 4 + 2) - myPlayer.x / 32;
            int l4 = (destY * 4 + 2) - myPlayer.y / 32;
            markMinimap(mapFlag, j2, l4);
        }
        if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
            DrawingArea.drawPixels(3, 84, 125, 0xffffff, 3);
        }
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
            DrawingArea.drawPixels(3, 84, 105, 0xffffff, 3);
        }
        // mapBack.drawBackground(0, 0);
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
            compass.method352(33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, isFullScreen ? 12 : 8, isFullScreen ? 30 + x : 8, 33, 25);
        }
        if (isFullScreen) {
            compass.method352(33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, isFullScreen ? 12 : 8, isFullScreen ? 30 + x : 8, 33, 25);
        }
        // Compass
        if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
            compass.method352(33, minimapInt1, anIntArray1057, 256, anIntArray968, 25, 4, 27, 33, 25);
        }

        if (isFullScreen) {
            DrawingArea.drawPixels(3, 78 + markY, 97 + x + markX, 0xffffff, 3);
        }
        if (isFullScreen) {
            mapArea[0].drawSprite(25 + x, 7);
        }
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
            mapArea[1].drawSprite(0, 0);
        }
        if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
            mapArea[2].drawSprite(0, 0);
        }
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474)) {
            drawXPButton();
            drawHPOrb();
            drawPrayerOrb();
            drawRunOrb();
            drawSummOrb();
            drawLogoutButton();
            drawQuestionButton();
            drawEventOrb();
            drawSpecialOrb();
        }
        if (menuOpen && menuScreenArea == 3) {
            drawMenu();
        }
        aRSImageProducer_1165.initializeDrawingArea();
    }    private void randomizeBackground(Background background) {
        int j = 256;
        for (int k = 0; k < anIntArray1190.length; k++) { anIntArray1190[k] = 0; }

        for (int l = 0; l < 5000; l++) {
            int i1 = (int) (Math.random() * 128D * (double) j);
            anIntArray1190[i1] = (int) (Math.random() * 256D);
        }
        for (int j1 = 0; j1 < 20; j1++) {
            for (int k1 = 1; k1 < j - 1; k1++) {
                for (int i2 = 1; i2 < 127; i2++) {
                    int k2 = i2 + (k1 << 7);
                    anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128] + anIntArray1190[k2 + 128]) / 4;
                }

            }
            int ai[] = anIntArray1190;
            anIntArray1190 = anIntArray1191;
            anIntArray1191 = ai;
        }
        if (background != null) {
            int l1 = 0;
            for (int j2 = 0; j2 < background.anInt1453; j2++) {
                for (int l2 = 0; l2 < background.anInt1452; l2++) {
                    if (background.aByteArray1450[l1++] != 0) {
                        int i3 = l2 + 16 + background.anInt1454;
                        int j3 = j2 + 16 + background.anInt1455;
                        int k3 = i3 + (j3 << 7);
                        anIntArray1190[k3] = 0;
                    }
                }
            }
        }
    }

    public void drawLogoutButton() {
        int x = isFullScreen ? extraWidth + 510 : 0;
        int y = isFullScreen ? -30 : 0;
        if (tabInterfaceIDs[10] != -1 && anInt1054 == 9) { if (loopCycle % 20 >= 10) { ; } }
        logoutIcon.drawSprite(225 + x, 0);
        if (logIconHPos == 1) {
            logIconH.drawSprite(225 + x, 0);
        }
        if (tabID == 10) { logIconC.drawSprite(225 + x, 0); }
    }

    public void drawQuestionButton() {
        int x = isFullScreen ? extraWidth + 510 : 0;
        int y = isFullScreen ? -30 : 0;
        if (tabInterfaceIDs[11] != -1 && anInt1054 == 9) { if (loopCycle % 20 >= 11) { ; } }
        questionIcon.drawSprite(204 + x, 0);
        if (questionIconHPos == 1) {
            questionIconH.drawSprite(204 + x, 0);
        }
    }

    public void drawWorldMapButton() {
        int x = isFullScreen ? 700 + extraWidth : 0;
        int y = isFullScreen ? -0 : 0;

        int xm = isFullScreen ? 706 + extraWidth : 0;
        int ym = isFullScreen ? +5 : 0;
        if (isFullScreen) {
            mapRing.drawSprite(8 + x, 124 + y);
        }
        worldMapIcon.drawSprite(8 + xm, 124 + ym);
    }    public void processDrawing() {
        if (rsAlreadyLoaded || loadingError || genericLoadingError) {
            showErrorScreen();
            return;
        }
        anInt1061++;
        if (!loggedIn) {
            drawLoginScreen();
        } else {
            drawGameScreen();
        }
        anInt1213 = 0;
    }

    public void processMapAreaClick() {
        int x = isFullScreen ? extraWidth + 17 : 0;
        int y = isFullScreen ? -30 : 0;

        int x29 = isFullScreen ? extraWidth : 0;

        int x2 = isFullScreen ? 358 + extraWidth : 0;
        int y2 = isFullScreen ? +32 : 0;
        if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474)) {
            // Logout Button
            if (super.mouseX >= 742 + x29 && super.mouseX <= 764 + x29 && super.mouseY >= 1 && super.mouseY <= 23) {
                logIconHPos = 1;
            } else {
                logIconHPos = 0;
            }
            // XP Button
            if (super.mouseX >= 519 + x && super.mouseX <= 550 + x && super.mouseY >= 50 + y && super.mouseY <= 78 + y) {
                xpIconHPos = 1;
            } else {
                xpIconHPos = 0;
            }
            // Advisor Button
            if (super.mouseX >= 724 + x29 && super.mouseX <= 741 + x29 && super.mouseY >= 1 && super.mouseY <= 23) {
                questionIconHPos = 1;
            } else {
                questionIconHPos = 0;
            }
            if (super.clickMode3 == 1 && super.saveClickX >= 516 && super.saveClickX <= 550 && super.saveClickY >= 46 && super.saveClickY <= 80) {
                if (!xpClicked) {
                    xpClicked = true;
                    drawXpBar = true;
                } else {
                    xpClicked = false;
                    drawXpBar = false;
                }
            }
        }
    }

    public void drawXPButton() {
        int x = isFullScreen ? 535 + extraWidth : 0;
        int y = isFullScreen ? -36 : 0;
        if (tabInterfaceIDs[12] != -1 && anInt1054 == 9) { if (loopCycle % 20 >= 12) { ; } }
        xpIcon.drawSprite(0 + x, 47 + y);
        if (xpIconHPos == 1) {
            xpIconH.drawSprite(0 + x, 47 + y);
        }
    }

    private void npcScreenPos(Entity entity, int i) {
        calcEntityScreenPos(entity.x, i, entity.y);
    }

    private void calcEntityScreenPos(int i, int j, int l) {
        if (i < 128 || l < 128 || i > 13056 || l > 13056) {
            spriteDrawX = -1;
            spriteDrawY = -1;
            return;
        }
        int i1 = method42(plane, l, i) - j;
        i -= xCameraPos;
        i1 -= zCameraPos;
        l -= yCameraPos;
        int j1 = Model.SINE[yCameraCurve];
        int k1 = Model.COSINE[yCameraCurve];
        int l1 = Model.SINE[xCameraCurve];
        int i2 = Model.COSINE[xCameraCurve];
        int j2 = l * l1 + i * i2 >> 16;
        l = l * i2 - i * l1 >> 16;
        i = j2;
        j2 = i1 * k1 - l * j1 >> 16;
        l = i1 * j1 + l * k1 >> 16;
        i1 = j2;
        if (l >= 50) {
            spriteDrawX = Texture.center_x + (i << 9) / l;
            spriteDrawY = Texture.center_y + (i1 << 9) / l;
        } else {
            spriteDrawX = -1;
            spriteDrawY = -1;
        }
    }

    private void buildSplitPrivateChatMenu() {
        if (splitPrivateChat == 0) { return; }
        int i = 0;
        if (anInt1104 != 0) { i = 1; }
        for (int j = 0; j < 100; j++) {
            if (chatMessages[j] != null) {
                int k = chatTypes[j];
                String s = chatNames[j];
                boolean flag1 = false;
                if ((k == 3 || k == 7) && (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
                    int l = 329 - i * 13;
                    if (super.mouseX > 4 && super.mouseY - 4 > l - 10 + extraHeight && super.mouseY - 4 <= l + 3 + extraHeight) {
                        int i1 = aTextDrawingArea_1271.getTextWidth("From:  " + s + chatMessages[j]) + 25;
                        if (i1 > 450) { i1 = 450; }
                        if (super.mouseX < 4 + i1) {
                            if (myPrivilege >= 1) {
                                menuActionName[menuActionRow] = "Report abuse @whi@" + s;
                                menuActionID[menuActionRow] = 606;
                                menuActionRow++;
                            }
                            menuActionName[menuActionRow] = "Add ignore @whi@" + s;
                            menuActionID[menuActionRow] = 42;
                            menuActionRow++;
                            //"Message @whi@"
                            menuActionName[menuActionRow] = "Reply to @whi@" + s;
                            menuActionID[menuActionRow] = 639;
                            menuActionRow++;
                            menuActionName[menuActionRow] = "Add friend @whi@" + s;
                            menuActionID[menuActionRow] = 337;
                            menuActionRow++;
                        }
                    }
                    if (++i >= 5) { return; }
                }
                if ((k == 5 || k == 6) && privateChatMode < 2 && ++i >= 5) { return; }
            }
        }

    }

    private void method130(int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2) {
        Class30_Sub1 class30_sub1 = null;
        for (Class30_Sub1 class30_sub1_1 = (Class30_Sub1) aClass19_1179.reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (Class30_Sub1) aClass19_1179.reverseGetNext()) {
            if (class30_sub1_1.anInt1295 != l1 || class30_sub1_1.anInt1297 != i2 || class30_sub1_1.anInt1298 != j1 || class30_sub1_1.anInt1296 != i1) {
                continue;
            }
            class30_sub1 = class30_sub1_1;
            break;
        }

        if (class30_sub1 == null) {
            class30_sub1 = new Class30_Sub1();
            class30_sub1.anInt1295 = l1;
            class30_sub1.anInt1296 = i1;
            class30_sub1.anInt1297 = i2;
            class30_sub1.anInt1298 = j1;
            method89(class30_sub1);
            aClass19_1179.insertHead(class30_sub1);
        }
        class30_sub1.anInt1291 = k;
        class30_sub1.anInt1293 = k1;
        class30_sub1.anInt1292 = l;
        class30_sub1.anInt1302 = j2;
        class30_sub1.anInt1294 = j;
    }

    private boolean interfaceIsSelected(RSInterface class9) {
        if (class9.anIntArray245 == null) { return false; }
        for (int i = 0; i < class9.anIntArray245.length; i++) {
            int j = extractInterfaceValues(class9, i);
            int k = class9.anIntArray212[i];
            if (class9.anIntArray245[i] == 2) {
                if (j >= k) { return false; }
            } else if (class9.anIntArray245[i] == 3) {
                if (j <= k) { return false; }
            } else if (class9.anIntArray245[i] == 4) {
                if (j == k) { return false; }
            } else if (j != k) { return false; }
        }

        return true;
    }

    private void method134(Stream stream) {
        int j = stream.readBits(8);
        if (j < playerCount) {
            for (int k = j; k < playerCount; k++) { anIntArray840[anInt839++] = playerIndices[k]; }

        }
        if (j > playerCount) {
            Signlink.reporterror(Configuration.getInstance().getValue(Username.class) + " Too many players");
            throw new RuntimeException("eek");
        }
        playerCount = 0;
        for (int l = 0; l < j; l++) {
            int i1 = playerIndices[l];
            Player player = playerArray[i1];
            int j1 = stream.readBits(1);
            if (j1 == 0) {
                playerIndices[playerCount++] = i1;
                player.anInt1537 = loopCycle;
            } else {
                int k1 = stream.readBits(2);
                if (k1 == 0) {
                    playerIndices[playerCount++] = i1;
                    player.anInt1537 = loopCycle;
                    anIntArray894[anInt893++] = i1;
                } else if (k1 == 1) {
                    playerIndices[playerCount++] = i1;
                    player.anInt1537 = loopCycle;
                    int l1 = stream.readBits(3);
                    player.moveInDir(false, l1);
                    int j2 = stream.readBits(1);
                    if (j2 == 1) { anIntArray894[anInt893++] = i1; }
                } else if (k1 == 2) {
                    playerIndices[playerCount++] = i1;
                    player.anInt1537 = loopCycle;
                    int i2 = stream.readBits(3);
                    player.moveInDir(true, i2);
                    int k2 = stream.readBits(3);
                    player.moveInDir(true, k2);
                    int l2 = stream.readBits(1);
                    if (l2 == 1) { anIntArray894[anInt893++] = i1; }
                } else if (k1 == 3) { anIntArray840[anInt839++] = i1; }
            }
        }
    }

    public void setTextCursor() {
        isTextCursor = true;
        Cursor cursor = new Cursor(Cursor.TEXT_CURSOR);
        getGameComponent().setCursor(cursor);
    }    private void nullLoader() {
        aBoolean831 = false;
        loginBox = null;
        loginButtonHover = null;
        textBoxHover = null;
        titleBox = null;
        titleButton = null;
        titleButtonHover = null;
        aBackgroundArray1152s = null;
        anIntArray851 = null;
        anIntArray852 = null;
        anIntArray853 = null;
        anIntArray1190 = null;
        anIntArray1191 = null;
        aClass30_Sub2_Sub1_Sub1_1201 = null;
        aClass30_Sub2_Sub1_Sub1_1202 = null;
    }

    public void setDefaultCursor() {
        isTextCursor = false;
        getGameComponent().setCursor(Cursor.getDefaultCursor());
    }

    private int getBackgroundIndex() {
        final int current = backgroundCount;
        final int random = (int) (Math.random() * 6);
        if (current == random) {
            return getBackgroundIndex();
        }
        return backgroundCount = random;
    }

    private void drawNormalLoginScreen() {
        if (count == 0) {
            backgroundCount = getBackgroundIndex();
            Sprite sprite = new Sprite("background/BG " + backgroundCount);
            sprite.drawSpriteWithOpacity(0, 0, opacityCount);
        }
        if (count >= 100) {
            opacityCount++;
            Sprite sprite = new Sprite("background/BG " + backgroundCount);
            sprite.drawSpriteWithOpacity(0, 0, opacityCount);
        }
        if (count == 250) {
            count = 0;
            opacityCount = 0;
        } else {
            count++;
        }
    }

    public void drawLoginScreen() {
        InterfaceManager.hideAllActiveInterfaces();
        try {
            final int month = Calendar.getInstance().get(Calendar.MONTH);
            if (month == Calendar.DECEMBER) {
                try {
                    final Sprite sprite = new Sprite("background/christmas");
                    sprite.drawSpriteWithOpacity(0, 0, 255);
                } catch (Exception ex) {
                    drawNormalLoginScreen();
                }
            } else {
                drawNormalLoginScreen();
            }

            String caret = (loopCycle % 40 < 20) ? "|" : "";
            char c = '\u0168';
            char c1 = '\310';
            int yPos = 140;

            resetImageProducers();
            loginScreenArea.initializeDrawingArea();
            loginBox.drawBackground(253, yPos);
            smallText.method382(0xffffff, 378, top_Message, 160, true);
            smallText.method382(0xffffff, 378, top_Message2, 160 + 15, true);
            if (loginScreenState == 0) {
                new Color(0);
                DrawingArea.drawHorizontalLine(yPos += 38, 0x1B1717, 220, 252 + 23);
                DrawingArea.drawHorizontalLine(yPos += 1, 0x2C2A29, 220, 252 + 23);

                smallText.drawText(0xEBE0BC, "Username:", yPos += 15, 253 + 50);
                textBox.drawSprite(251 + 23, yPos += 3);
                if (mouseIn(251 + 23, yPos + 2, 218, 27)) { textBoxHover.drawSprite(251 + 23, yPos + 1); }
                smallText.method389(false, 281, 0xffffff, (Configuration.getInstance().getValue(Username.class)) + (loginScreenCursorPos == 0 ? caret : ""), yPos + 17);

                smallText.drawText(0xEBE0BC, "Password:", yPos += 38, 253 + 50);
                textBox.drawSprite(251 + 23, yPos += 3);
                if (mouseIn(251 + 23, yPos + 2, 218, 27)) { textBoxHover.drawSprite(251 + 23, yPos + 1); }

                smallText.method389(false, 281, 0xffffff, TextClass.passwordAsterisks(Configuration.getInstance().getValue(Password.class)) + (loginScreenCursorPos == 1 ? caret : ""), yPos + 17);
                smallText.drawText(0xEBE0BC, "Authenticator: " + (Configuration.getInstance().getValue(Authentication.class).isEmpty() ? "" : (!saveIp ? "Click here to save this IP" : "Click here to not save this IP")), yPos += 38, 253 + (Configuration.getInstance().getValue(Authentication.class).isEmpty() ? 60 : (!saveIp ? 122 : 131)));
                textBox.drawSprite(251 + 23, yPos += 3);
                if (mouseIn(251 + 23, yPos + 2, 218, 27)) { textBoxHover.drawSprite(251 + 23, yPos + 1); }
                smallText.method389(false, 281, Configuration.getInstance().getValue(Authentication.class).isEmpty() && loginScreenCursorPos != 2 ? 0x2F3230 : 0xffffff, !Configuration.getInstance().getValue(Authentication.class).isEmpty() || loginScreenCursorPos == 2 ? (TextClass.passwordAsterisks(Configuration.getInstance().getValue(Authentication.class)) + (loginScreenCursorPos == 2 ? caret : "")) : "Only if you have 2 step verification", yPos + 17);

                DrawingArea.drawHorizontalLine(yPos += 35, 0x1B1717, 220, 252 + 23);
                DrawingArea.drawHorizontalLine(yPos += 1, 0x2C2A29, 220, 252 + 23);

                loginButton.drawSprite(251 + 43, yPos += 9);
                if (mouseIn(251 + 43, yPos + 2, 181, 27)) { loginButtonHover.drawSprite(251 + 43, yPos); }
                aTextDrawingArea_1271.drawText(0, "Log In", yPos + 18, 251 + 43 + 90);

                DrawingArea.drawHorizontalLine(yPos += 36, 0x1B1717, 220, 252 + 23);
                DrawingArea.drawHorizontalLine(yPos += 1, 0x2C2A29, 220, 252 + 23);

                smallText.method382(0xffffff, 385, loginMessage1, yPos += 15, true);
                smallText.method382(0xffffff, 385, loginMessage2, yPos += 15, true);

                socialIcons[mouseIn(315, 413, 50, 50) ? 1 : 0].drawAdvancedSprite(315, 413);
                socialIcons[mouseIn(315 + 32 + 50, 413, 50, 50) ? 3 : 2].drawAdvancedSprite(315 + 32 + 50, 413);
            }
            loginScreenArea.drawGraphics(0, super.graphics, 0);
        } catch (Exception ex) {
            Logger.log(Client.class, Level.SEVERE, "Error drawing login screen", ex);
        }
    }

    private boolean mouseIn(int x, int y, int w, int h) {
        return (super.mouseX > x && super.mouseX <= x + w && super.mouseY > y && super.mouseY <= y + h);
    }

    private boolean clickedIn(int x, int y, int w, int h) {
        return super.clickMode3 == 1 && (super.saveClickX > x && super.saveClickX <= x + w && super.saveClickY > y && super.saveClickY <= y + h);
    }    public String getParameter(String s) {
        if (Signlink.mainapp != null) { return Signlink.mainapp.getParameter(s); } else {
            return super.getParameter(s);
        }
    }

    private void method137(Stream stream, int j) {
        if (j == 84) {
            int k = stream.readUByte();
            int j3 = anInt1268 + (k >> 4 & 7);
            int i6 = anInt1269 + (k & 7);
            int l8 = stream.readUShort();
            int k11 = stream.readUShort();
            int l13 = stream.readUShort();
            if (j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104) {
                NodeList class19_1 = groundArray[plane][j3][i6];
                if (class19_1 != null) {
                    for (Item class30_sub2_sub4_sub2_3 = (Item) class19_1.reverseGetFirst(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (Item) class19_1.reverseGetNext()) {
                        if (class30_sub2_sub4_sub2_3.ID != (l8 & 0x7fff) || class30_sub2_sub4_sub2_3.anInt1559 != k11) {
                            continue;
                        }
                        class30_sub2_sub4_sub2_3.anInt1559 = l13;
                        break;
                    }

                    spawnGroundItem(j3, i6);
                }
            }
            return;
        }
        if (j == 105) {
            int l = stream.readUByte();
            int k3 = anInt1268 + (l >> 4 & 7);
            int j6 = anInt1269 + (l & 7);
            int i9 = stream.readUShort();
            int l11 = stream.readUByte();
            int i14 = l11 >> 4 & 0xf;
            int i16 = l11 & 7;
            if (myPlayer.smallX[0] >= k3 - i14 && myPlayer.smallX[0] <= k3 + i14 && myPlayer.smallY[0] >= j6 - i14 && myPlayer.smallY[0] <= j6 + i14 && aBoolean848 && !lowMem && anInt1062 < 50) {
                anIntArray1207[anInt1062] = i9;
                anIntArray1241[anInt1062] = i16;
                anIntArray1250[anInt1062] = Sounds.anIntArray326[i9];
                anInt1062++;
            }
        }
        if (j == 215) {
            int i1 = stream._readShortA();
            int l3 = stream._readByteS();
            int k6 = anInt1268 + (l3 >> 4 & 7);
            int j9 = anInt1269 + (l3 & 7);
            int i12 = stream._readShortA();
            int j14 = stream.readUShort();
            if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != unknownInt10) {
                Item class30_sub2_sub4_sub2_2 = new Item();
                class30_sub2_sub4_sub2_2.ID = i1;
                class30_sub2_sub4_sub2_2.anInt1559 = j14;
                if (groundArray[plane][k6][j9] == null) { groundArray[plane][k6][j9] = new NodeList(); }
                groundArray[plane][k6][j9].insertHead(class30_sub2_sub4_sub2_2);
                spawnGroundItem(k6, j9);
            }
            return;
        }
        if (j == 156) {
            int j1 = stream.readByteA();
            int i4 = anInt1268 + (j1 >> 4 & 7);
            int l6 = anInt1269 + (j1 & 7);
            int k9 = stream.readUShort();
            if (i4 >= 0 && l6 >= 0 && i4 < 104 && l6 < 104) {
                NodeList class19 = groundArray[plane][i4][l6];
                if (class19 != null) {
                    for (Item item = (Item) class19.reverseGetFirst(); item != null; item = (Item) class19.reverseGetNext()) {
                        if (item.ID != (k9 & 0x7fff)) { continue; }
                        item.unlink();
                        break;
                    }

                    if (class19.reverseGetFirst() == null) { groundArray[plane][i4][l6] = null; }
                    spawnGroundItem(i4, l6);
                }
            }
            return;
        }
        if (j == 160) {
            int k1 = stream._readByteS();
            int j4 = anInt1268 + (k1 >> 4 & 7);
            int i7 = anInt1269 + (k1 & 7);
            int l9 = stream._readByteS();
            int j12 = l9 >> 2;
            int k14 = l9 & 3;
            int j16 = anIntArray1177[j12];
            int j17 = stream._readShortA();
            if (j4 >= 0 && i7 >= 0 && j4 < 103 && i7 < 103) {
                int j18 = intGroundArray[plane][j4][i7];
                int i19 = intGroundArray[plane][j4 + 1][i7];
                int l19 = intGroundArray[plane][j4 + 1][i7 + 1];
                int k20 = intGroundArray[plane][j4][i7 + 1];
                if (j16 == 0) {
                    Object1 class10 = worldController.method296(plane, j4, i7);
                    if (class10 != null) {
                        int k21 = class10.uid >> 14 & 0x7fff;
                        if (j12 == 2) {
                            class10.aClass30_Sub2_Sub4_278 = new Animable_Sub5(k21, 4 + k14, 2, i19, l19, j18, k20, j17, false);
                            class10.aClass30_Sub2_Sub4_279 = new Animable_Sub5(k21, k14 + 1 & 3, 2, i19, l19, j18, k20, j17, false);
                        } else {
                            class10.aClass30_Sub2_Sub4_278 = new Animable_Sub5(k21, k14, j12, i19, l19, j18, k20, j17, false);
                        }
                    }
                }
                if (j16 == 1) {
                    Object2 class26 = worldController.method297(j4, i7, plane);
                    if (class26 != null) {
                        class26.aClass30_Sub2_Sub4_504 = new Animable_Sub5(class26.uid >> 14 & 0x7fff, 0, 4, i19, l19, j18, k20, j17, false);
                    }
                }
                if (j16 == 2) {
                    Object5 class28 = worldController.method298(j4, i7, plane);
                    if (j12 == 11) { j12 = 10; }
                    if (class28 != null) {
                        class28.aClass30_Sub2_Sub4_521 = new Animable_Sub5(class28.uid >> 14 & 0x7fff, k14, j12, i19, l19, j18, k20, j17, false);
                    }
                }
                if (j16 == 3) {
                    Object3 class49 = worldController.method299(i7, j4, plane);
                    if (class49 != null) {
                        class49.aClass30_Sub2_Sub4_814 = new Animable_Sub5(class49.uid >> 14 & 0x7fff, k14, 22, i19, l19, j18, k20, j17, false);
                    }
                }
            }
            return;
        }
        if (j == 147) {
            int l1 = stream._readByteS();
            int k4 = anInt1268 + (l1 >> 4 & 7);
            int j7 = anInt1269 + (l1 & 7);
            int i10 = stream.readUShort();
            byte byte0 = stream.readByteS();
            int l14 = stream.readLEShort();
            byte byte1 = stream.readByteC();
            int k17 = stream.readUShort();
            int k18 = stream._readByteS();
            int j19 = k18 >> 2;
            int i20 = k18 & 3;
            int l20 = anIntArray1177[j19];
            byte byte2 = stream.readByte();
            int l21 = stream.readUShort();
            byte byte3 = stream.readByteC();
            Player player;
            if (i10 == unknownInt10) { player = myPlayer; } else { player = playerArray[i10]; }
            if (player != null) {
                ObjectDef class46 = ObjectDef.forID(l21);
                int i22 = intGroundArray[plane][k4][j7];
                int j22 = intGroundArray[plane][k4 + 1][j7];
                int k22 = intGroundArray[plane][k4 + 1][j7 + 1];
                int l22 = intGroundArray[plane][k4][j7 + 1];
                Model model = class46.method578(j19, i20, i22, j22, k22, l22, -1);
                if (model != null) {
                    method130(k17 + 1, -1, 0, l20, j7, 0, plane, k4, l14 + 1);
                    player.anInt1707 = l14 + loopCycle;
                    player.anInt1708 = k17 + loopCycle;
                    player.aModel_1714 = model;
                    int i23 = class46.anInt744;
                    int j23 = class46.anInt761;
                    if (i20 == 1 || i20 == 3) {
                        i23 = class46.anInt761;
                        j23 = class46.anInt744;
                    }
                    player.anInt1711 = k4 * 128 + i23 * 64;
                    player.anInt1713 = j7 * 128 + j23 * 64;
                    player.anInt1712 = method42(plane, player.anInt1713, player.anInt1711);
                    if (byte2 > byte0) {
                        byte byte4 = byte2;
                        byte2 = byte0;
                        byte0 = byte4;
                    }
                    if (byte3 > byte1) {
                        byte byte5 = byte3;
                        byte3 = byte1;
                        byte1 = byte5;
                    }
                    player.anInt1719 = k4 + byte2;
                    player.anInt1721 = k4 + byte0;
                    player.anInt1720 = j7 + byte3;
                    player.anInt1722 = j7 + byte1;
                }
            }
        }
        if (j == 151) {
            int i2 = stream.readByteA();
            int l4 = anInt1268 + (i2 >> 4 & 7);
            int k7 = anInt1269 + (i2 & 7);
            int j10 = stream.readLEShort();
            int k12 = stream._readByteS();
            int i15 = k12 >> 2;
            int k16 = k12 & 3;
            int l17 = anIntArray1177[i15];
            if (l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104) { method130(-1, j10, k16, l17, k7, i15, plane, l4, 0); }
            return;
        }
        if (j == 4) {
            int j2 = stream.readUByte();
            int i5 = anInt1268 + (j2 >> 4 & 7);
            int l7 = anInt1269 + (j2 & 7);
            int k10 = stream.readUShort();
            int l12 = stream.readUByte();
            int j15 = stream.readUShort();
            if (i5 >= 0 && l7 >= 0 && i5 < 104 && l7 < 104) {
                i5 = i5 * 128 + 64;
                l7 = l7 * 128 + 64;
                Animable_Sub3 class30_sub2_sub4_sub3 = new Animable_Sub3(plane, loopCycle, j15, k10, method42(plane, l7, i5) - l12, l7, i5);
                aClass19_1056.insertHead(class30_sub2_sub4_sub3);
            }
            return;
        }
        if (j == 44) {
            int k2 = stream.readLEShortA();
            int j5 = stream.readUShort();
            int i8 = stream.readUByte();
            int l10 = anInt1268 + (i8 >> 4 & 7);
            int i13 = anInt1269 + (i8 & 7);
            if (l10 >= 0 && i13 >= 0 && l10 < 104 && i13 < 104) {
                Item class30_sub2_sub4_sub2_1 = new Item();
                class30_sub2_sub4_sub2_1.ID = k2;
                class30_sub2_sub4_sub2_1.anInt1559 = j5;
                if (groundArray[plane][l10][i13] == null) { groundArray[plane][l10][i13] = new NodeList(); }
                groundArray[plane][l10][i13].insertHead(class30_sub2_sub4_sub2_1);
                spawnGroundItem(l10, i13);
            }
            return;
        }
        if (j == 101) {
            int l2 = stream._readByteC();
            int k5 = l2 >> 2;
            int j8 = l2 & 3;
            int i11 = anIntArray1177[k5];
            int j13 = stream.readUByte();
            int k15 = anInt1268 + (j13 >> 4 & 7);
            int l16 = anInt1269 + (j13 & 7);
            if (k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104) { method130(-1, -1, j8, i11, l16, k5, plane, k15, 0); }
            return;
        }
        if (j == 117) {
            int i3 = stream.readUByte();
            int l5 = anInt1268 + (i3 >> 4 & 7);
            int k8 = anInt1269 + (i3 & 7);
            int j11 = l5 + stream.readByte();
            int k13 = k8 + stream.readByte();
            int l15 = stream.readShort();
            int i17 = stream.readUShort();
            int i18 = stream.readUByte() * 4;
            int l18 = stream.readUByte() * 4;
            int k19 = stream.readUShort();
            int j20 = stream.readUShort();
            int i21 = stream.readUByte();
            int j21 = stream.readUByte();
            if (l5 >= 0 && k8 >= 0 && l5 < 104 && k8 < 104 && j11 >= 0 && k13 >= 0 && j11 < 104 && k13 < 104 && i17 != 65535) {
                l5 = l5 * 128 + 64;
                k8 = k8 * 128 + 64;
                j11 = j11 * 128 + 64;
                k13 = k13 * 128 + 64;
                Animable_Sub4 class30_sub2_sub4_sub4 = new Animable_Sub4(i21, l18, k19 + loopCycle, j20 + loopCycle, j21, plane, method42(plane, k8, l5) - i18, k8, l5, l15, i17);
                class30_sub2_sub4_sub4.method455(k19 + loopCycle, k13, method42(plane, k13, j11) - l18, j11);
                aClass19_1013.insertHead(class30_sub2_sub4_sub4);
            }
        }
    }

    private void method139(Stream stream) {
        stream.initBitAccess();
        int k = stream.readBits(8);
        if (k < npcCount) {
            for (int l = k; l < npcCount; l++) { anIntArray840[anInt839++] = npcIndices[l]; }

        }
        if (k > npcCount) {
            Signlink.reporterror(Configuration.getInstance().getValue(Username.class) + " Too many npcs");
            throw new RuntimeException("eek");
        }
        npcCount = 0;
        for (int i1 = 0; i1 < k; i1++) {
            int j1 = npcIndices[i1];
            NPC npc = npcArray[j1];
            int k1 = stream.readBits(1);
            if (k1 == 0) {
                npcIndices[npcCount++] = j1;
                npc.anInt1537 = loopCycle;
            } else {
                int l1 = stream.readBits(2);
                if (l1 == 0) {
                    npcIndices[npcCount++] = j1;
                    npc.anInt1537 = loopCycle;
                    anIntArray894[anInt893++] = j1;
                } else if (l1 == 1) {
                    npcIndices[npcCount++] = j1;
                    npc.anInt1537 = loopCycle;
                    int i2 = stream.readBits(3);
                    npc.moveInDir(false, i2);
                    int k2 = stream.readBits(1);
                    if (k2 == 1) { anIntArray894[anInt893++] = j1; }
                } else if (l1 == 2) {
                    npcIndices[npcCount++] = j1;
                    npc.anInt1537 = loopCycle;
                    int j2 = stream.readBits(3);
                    npc.moveInDir(true, j2);
                    int l2 = stream.readBits(3);
                    npc.moveInDir(true, l2);
                    int i3 = stream.readBits(1);
                    if (i3 == 1) { anIntArray894[anInt893++] = j1; }
                } else if (l1 == 3) { anIntArray840[anInt839++] = j1; }
            }
        }

    }

    public void processLoginScreenInput() {
        if (loginScreenState == 0) {
            int yPos = 200;
            if (clickedIn(251 + 23, yPos, 221, 27)) { loginScreenCursorPos = 0; }
            if (clickedIn(251 + 23, yPos += 38, 221, 27)) { loginScreenCursorPos = 1; }
            if (clickedIn(251 + 23, yPos += 27, 221, 11)) { saveIp = !saveIp; }
            if (clickedIn(251 + 23, yPos += 11, 221, 27)) { loginScreenCursorPos = 2; }
            if (clickedIn(251 + 43, yPos + 47, 181, 27)) {
                loginFailures = 0;
                login(false);
                Configuration.getInstance().save();
                if (loggedIn) { return; }
            }
            processLoginTextInput();
        }
    }

    private void processLoginTextInput() {
        do {
            int l1 = readChar(-796);
            if (l1 == -1) { break; }
            boolean flag1 = false;
            for (int i2 = 0; i2 < validUserPassChars.length(); i2++) {
                if (l1 != validUserPassChars.charAt(i2)) { continue; }
                flag1 = true;
                break;
            }
            if (loginScreenCursorPos == 0) {
                if (l1 == 8 && Configuration.getInstance().getValue(Username.class).length() > 0) {
                    Configuration.getInstance().get(Username.class).setValue(Configuration.getInstance().getValue(Username.class).substring(0, Configuration.getInstance().getValue(Username.class).length() - 1));
                }
                if (l1 == 9 || l1 == 10 || l1 == 13) { loginScreenCursorPos = 1; }
                if (flag1) {
                    Configuration.getInstance().get(Username.class).setValue(Configuration.getInstance().getValue(Username.class) + (char) l1);
                }
                if (Configuration.getInstance().getValue(Username.class).length() > 12) {
                    Configuration.getInstance().get(Username.class).setValue(Configuration.getInstance().getValue(Username.class).substring(0, 12));
                }
            } else if (loginScreenCursorPos == 1) {
                if (l1 == 8 && Configuration.getInstance().getValue(Password.class).length() > 0) {
                    Configuration.getInstance().get(Password.class).setValue(Configuration.getInstance().getValue(Password.class).substring(0, Configuration.getInstance().getValue(Password.class).length() - 1));
                }
                if (l1 == 9 || l1 == 10 || l1 == 13) { loginScreenCursorPos = 2; }
                if (flag1) {
                    Configuration.getInstance().get(Password.class).setValue(Configuration.getInstance().getValue(Password.class) + (char) l1);
                }
                if (Configuration.getInstance().getValue(Password.class).length() > 20) {
                    Configuration.getInstance().get(Password.class).setValue(Configuration.getInstance().getValue(Password.class).substring(0, 20));
                }
            } else if (loginScreenCursorPos == 2) {
                flag1 = false;
                for (int i2 = 0; i2 < validNumbers.length(); i2++) {
                    if (l1 != validNumbers.charAt(i2)) { continue; }
                    flag1 = true;
                    break;
                }
                if (l1 == 8 && Configuration.getInstance().getValue(Authentication.class).length() > 0) {
                    Configuration.getInstance().get(Authentication.class).setValue(Configuration.getInstance().getValue(Authentication.class).substring(0, Configuration.getInstance().getValue(Authentication.class).length() - 1));
                }
                if (l1 == 9 || l1 == 10 || l1 == 13) {
                    if (Configuration.getInstance().getValue(Username.class).length() > 0 && Configuration.getInstance().getValue(Password.class).length() > 0) {
                        login(false);
                    } else { loginScreenCursorPos = 0; }
                }
                if (flag1) {
                    Configuration.getInstance().get(Authentication.class).setValue(Configuration.getInstance().getValue(Authentication.class) + (char) l1);
                }
                if (Configuration.getInstance().getValue(Authentication.class).length() > 8) {
                    Configuration.getInstance().get(Authentication.class).setValue(Configuration.getInstance().getValue(Authentication.class).substring(0, 6));
                }
            }
        } while (true);
    }

    private void markMinimap(Sprite sprite, int i, int j) {
        int x = isFullScreen ? 549 + extraWidth : 0;
        int markY = 6;
        int markX = 9;
        int k = minimapInt1 + minimapInt2 & 0x7ff;
        int l = i * i + j * j;
        if (l > 6400) { return; }
        int i1 = Model.SINE[k];
        int j1 = Model.COSINE[k];
        i1 = (i1 * 256) / (minimapInt3 + 256);
        j1 = (j1 * 256) / (minimapInt3 + 256);
        int k1 = j * i1 + i * j1 >> 16;
        int l1 = j * j1 - i * i1 >> 16;
        try {
            if (!Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
                sprite.drawSprite((((94 + k1) - sprite.maxWidth / 2) + 4) + x + markX, (83 - l1 - sprite.maxHeight / 2 - 4) + markY);
            }
        } catch (Exception e) {
        }
        try {
            if (isFullScreen) {
                sprite.drawSprite((((94 + k1) - sprite.maxWidth / 2) + 4) + x + markX, (83 - l1 - sprite.maxHeight / 2 - 4) + markY);
            }
        } catch (Exception e) {
        }
        try {
            if (Configuration.getInstance().getValue(CurrentFrame.class).equals(FrameType.FRAME_474) && !isFullScreen) {
                sprite.drawSprite(((123 + k1) - sprite.maxWidth / 2) + 4, 89 - l1 - sprite.maxHeight / 2 - 4);
            }
        } catch (Exception e) {
        }

    }

    private void method142(int i, int j, int k, int l, int i1, int j1, int k1) {
        if (i1 >= 1 && i >= 1 && i1 <= 102 && i <= 102) {
            if (lowMem && j != plane) { return; }
            int i2 = 0;
            int id = 0;
            if (j1 == 0) {
                i2 = worldController.method300(j, i1, i);
                id = worldController.method300_1(j, i1, i);
            }
            if (j1 == 1) {
                i2 = worldController.method301(j, i1, i);
                id = worldController.method301_1(j, i1, i);
            }
            if (j1 == 2) {
                i2 = worldController.method302(j, i1, i);
                id = worldController.method302_1(j, i1, i);
            }
            if (j1 == 3) {
                i2 = worldController.method303(j, i1, i);
                id = worldController.method303_1(j, i1, i);
            }
            if (i2 != 0) {
                int i3 = worldController.method304(j, i1, i, i2);
                //int j2 = i2 >> 14 & 0x7fff;
                int k2 = i3 & 0x1f;
                int l2 = i3 >> 6;
                if (j1 == 0) {
                    worldController.method291(i1, j, i, (byte) -119);
                    ObjectDef class46 = ObjectDef.forID(id);
                    if (class46.aBoolean767) { aClass11Array1230[j].method215(l2, k2, class46.aBoolean757, i1, i); }
                }
                if (j1 == 1) { worldController.method292(i, j, i1); }
                if (j1 == 2) {
                    worldController.method293(j, i1, i);
                    ObjectDef class46_1 = ObjectDef.forID(id);
                    if (i1 + class46_1.anInt744 > 103 || i + class46_1.anInt744 > 103 || i1 + class46_1.anInt761 > 103 || i + class46_1.anInt761 > 103) {
                        return;
                    }
                    if (class46_1.aBoolean767) {
                        aClass11Array1230[j].method216(l2, class46_1.anInt744, i1, i, class46_1.anInt761, class46_1.aBoolean757);
                    }
                }
                if (j1 == 3) {
                    worldController.method294(j, i, i1);
                    ObjectDef class46_2 = ObjectDef.forID(id);
                    if (class46_2.aBoolean767 && class46_2.hasActions) { aClass11Array1230[j].method218(i, i1); }
                }
            }
            if (k1 >= 0) {
                int j3 = j;
                if (j3 < 3 && (byteGroundArray[1][i1][i] & 2) == 2) { j3++; }
                ObjectManager.method188(worldController, k, i, l, j3, aClass11Array1230[j], intGroundArray, i1, k1, j);
            }
        }
    }

    private void updatePlayers(int i, Stream stream) {
        anInt839 = 0;
        anInt893 = 0;
        method117(stream);
        method134(stream);
        method91(stream, i);
        method49(stream);
        for (int k = 0; k < anInt839; k++) {
            int l = anIntArray840[k];
            if (playerArray[l].anInt1537 != loopCycle) { playerArray[l] = null; }
        }

        if (stream.currentOffset != i) {
            Signlink.reporterror("Error packet size mismatch in getplayer pos:" + stream.currentOffset + " psize:" + i);
            throw new RuntimeException("eek");
        }
        //System.out.println("list size: "+playerCount);
        for (int i1 = 0; i1 < playerCount; i1++) {
            if (playerArray[playerIndices[i1]] == null) {
                Signlink.reporterror(Configuration.getInstance().getValue(Username.class) + " null entry in pl list - pos:" + i1 + " size:" + playerCount);
                throw new RuntimeException("eek");
            }
        }

    }

    /**
     * It is assumed that the id parameter is the skill id.
     *
     * @param id
     * @return
     */
    public String setMessage(int id) {
        //currentExp = 0;
        String[] messages = new String[4];
        String message = "";
        final int maxLevel = 99;
        if (maxStats[id] > maxLevel) {
            maxStats[id] = 99;
        }
        //System.out.println("maxstats:" + maxStats[level]);

        messages[0] = Skills.SKILL_NAMES[id] + ": " + currentStats[Skills.SKILL_IDS[id]] + "/" + maxStats[Skills.SKILL_IDS[id]] + "\\n";
        int currentexp = currentExp[Skills.SKILL_IDS[id]];
        messages[1] = "Current XP: " + currentexp + " \\n";
        //messages[1] = "Current XP: " + getXPForLevel(maxStats[stuff[level]])+"\\n";
        int nextlevel = (getXPForLevel(maxStats[Skills.SKILL_IDS[id]] + 1));
        messages[2] = "Next level: " + nextlevel + "\\n";
        //messages[3] = Math.random() + ",\n";
        messages[3] = "Remainder: " + (nextlevel - currentexp);

        message = messages[0] + messages[1] + messages[2] + messages[3];
        if (maxStats[Skills.SKILL_IDS[id]] >= 99) {
            message = Skills.SKILL_NAMES[id] + ": " + currentStats[Skills.SKILL_IDS[id]] + "/99\\nCurrent Exp: " + currentexp;
        }

        return message;
    }

    public int getLevelForExperience(int exp2) {
        double exp = (double) exp2 + 1;
        int points = 0;
        int output = 0;

        for (int lvl = 1; lvl < 99; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if (output >= exp) { return lvl; }
        }
        return 99;
    }

    private void setCameraPos(int i, int j, int k, int l, int i1, int j1) {
        try {
            int k1 = 2048 - j & 0x7ff;
            int l1 = 2048 - i1 & 0x7ff;
            int i2 = 0;
            int j2 = 0;
            if (Configuration.getInstance().getValue(ClientZoom.class) > 330) {
                Configuration.getInstance().get(ClientZoom.class).setValue(330);
            }
            if (Configuration.getInstance().getValue(ClientZoom.class) < -390) {
                Configuration.getInstance().get(ClientZoom.class).setValue(-390);
            }
            int k2 = i - Configuration.getInstance().getValue(ClientZoom.class);
            if (k1 != 0) {
                int l2 = Model.SINE[k1];
                int j3 = Model.COSINE[k1];
                int l3 = j2 * j3 - k2 * l2 >> 16;
                k2 = j2 * l2 + k2 * j3 >> 16;
                j2 = l3;
            }
            if (l1 != 0) {
                int i3 = Model.SINE[l1];
                int k3 = Model.COSINE[l1];
                int i4 = k2 * i3 + i2 * k3 >> 16;
                k2 = k2 * k3 - i2 * i3 >> 16;
                i2 = i4;
            }
            xCameraPos = k - i2;
            zCameraPos = l - j2;
            yCameraPos = j1 - k2;
            yCameraCurve = j;
            xCameraCurve = i1;
        } catch (Exception exception) {
        }
    }

    public void sendFrame126Toggle(final boolean value, final int component) {
        sendFrame126(value ? "@gre@On" : "@red@Off", component);
    }

    public void sendFrame126(String str, int i) {
        try {
            RSInterface.interfaceCache[i].message = str;
            if (RSInterface.interfaceCache[i].parentID == tabInterfaceIDs[tabID]) { needDrawTabArea = true; }
        } catch (Exception e) {
        }
    }

    public void sendFrame36(int id, int state) {
        anIntArray1045[id] = state;
        if (variousSettings[id] != state) {
            variousSettings[id] = state;
            method33(id);
            needDrawTabArea = true;
            if (dialogID != -1) { inputTaken = true; }
        }
    }

    private void method33(int i) {
        if (i >= Varp.cache.length) { return; }
        int j = Varp.cache[i].anInt709;
        if (j == 0) { return; }
        int k = variousSettings[i];
        if (j == 1) {
            if (k == 1) { Texture.method372(0.90000000000000002D); }
            if (k == 2) { Texture.method372(0.80000000000000004D); }
            if (k == 3) { Texture.method372(0.69999999999999996D); }
            if (k == 4) { Texture.method372(0.59999999999999998D); }
            ItemDef.mruNodes1.unlinkAll();
            welcomeScreenRaised = true;
        }
        if (j == 3) {
            boolean flag1 = musicEnabled;
            if (k == 0) {
                adjustVolume(musicEnabled, 0);
                musicEnabled = true;
            }
            if (k == 1) {
                adjustVolume(musicEnabled, -400);
                musicEnabled = true;
            }
            if (k == 2) {
                adjustVolume(musicEnabled, -800);
                musicEnabled = true;
            }
            if (k == 3) {
                adjustVolume(musicEnabled, -1200);
                musicEnabled = true;
            }
            if (k == 4) { musicEnabled = false; }
            if (musicEnabled != flag1 && !lowMem) {
                if (musicEnabled) {
                    nextSong = currentSong;
                    songChanging = true;
                    onDemandFetcher.requestFileData(2, nextSong);
                } else {
                    stopMidi();
                }
                prevSong = 0;
            }
        }
        if (j == 4) {
            if (k == 0) {
                aBoolean848 = true;
                setWaveVolume(0);
            }
            if (k == 1) {
                aBoolean848 = true;
                setWaveVolume(-400);
            }
            if (k == 2) {
                aBoolean848 = true;
                setWaveVolume(-800);
            }
            if (k == 3) {
                aBoolean848 = true;
                setWaveVolume(-1200);
            }
            if (k == 4) { aBoolean848 = false; }
        }
        if (j == 5) { anInt1253 = k; }
        if (j == 6) { anInt1249 = k; }
        if (j == 8) {
            splitPrivateChat = k;
            inputTaken = true;
        }
        if (j == 9) { anInt913 = k; }
    }

    private void adjustVolume(boolean flag, int i) {
        Signlink.midivol = i;
        if (flag) { Signlink.midi = "voladjust"; }
    }    private DataInputStream openJagGrabInputStream(String s) throws IOException {
        if (aSocket832 != null) {
            try {
                aSocket832.close();
            } catch (Exception _ex) {
            }
            aSocket832 = null;
        }
        aSocket832 = openSocket(43601);
        aSocket832.setSoTimeout(10000);
        InputStream inputstream = aSocket832.getInputStream();
        OutputStream outputstream = aSocket832.getOutputStream();
        outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
        return new DataInputStream(inputstream);
    }

    private void setWaveVolume(int i) {
        Signlink.wavevol = i;
    }

    public void sendFrame248(int interfaceID, int sideInterfaceID) {
        if (backDialogID != -1) {
            backDialogID = -1;
            inputTaken = true;
        }
        if (inputDialogState != 0) {
            inputDialogState = 0;
            inputTaken = true;
        }
        openInterfaceID = interfaceID;
        invOverlayInterfaceID = sideInterfaceID;
        needDrawTabArea = true;
        tabAreaAltered = true;
        aBoolean1149 = false;
    }

    private boolean parsePacket() {
        if (socketStream == null) { return false; }
        try {
            int i = socketStream.available();
            if (i == 0) { return false; }
            if (pktType == -1) {
                socketStream.flushInputStream(inStream.payload, 1);
                pktType = inStream.payload[0] & 0xff;
                if (encryption != null) { pktType = pktType - encryption.getNextKey() & 0xff; }
                pktSize = Constants.PACKET_SIZES[pktType];
                i--;
            }
            if (pktSize == -1) {
                if (i > 0) {
                    socketStream.flushInputStream(inStream.payload, 1);
                    pktSize = inStream.payload[0] & 0xff;
                    i--;
                } else {
                    return false;
                }
            }
            if (pktSize == -2) {
                if (i > 1) {
                    socketStream.flushInputStream(inStream.payload, 2);
                    inStream.currentOffset = 0;
                    pktSize = inStream.readUShort();
                    i -= 2;
                } else {
                    return false;
                }
            }
            if (i < pktSize) { return false; }
            inStream.currentOffset = 0;
            socketStream.flushInputStream(inStream.payload, pktSize);
            anInt1009 = 0;
            anInt843 = anInt842;
            anInt842 = anInt841;
            anInt841 = pktType;
            switch (pktType) {
                case 81:
                    updatePlayers(pktSize, inStream);
                    aBoolean1080 = false;
                    pktType = -1;
                    return true;

                case 176:
                    daysSinceRecovChange = inStream._readByteC();
                    unreadMessages = inStream._readShortA();
                    membersInt = inStream.readUByte();
                    anInt1193 = inStream.readInt2();
                    daysSinceLastLogin = inStream.readUShort();
                    if (anInt1193 != 0 && openInterfaceID == -1) {
                        Signlink.dnslookup(TextClass.method586(anInt1193));
                        clearTopInterfaces();
                        char c = '\u028A';
                        if (daysSinceRecovChange != 201 || membersInt == 1) { c = '\u028F'; }
                        reportAbuseInput = "";
                        canMute = false;
                        for (int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++) {
                            if (RSInterface.interfaceCache[k9] == null || RSInterface.interfaceCache[k9].contentType != c) {
                                continue;
                            }
                            openInterfaceID = RSInterface.interfaceCache[k9].parentID;

                        }
                    }
                    pktType = -1;
                    return true;

                case 64:
                    anInt1268 = inStream._readByteC();
                    anInt1269 = inStream._readByteS();
                    for (int j = anInt1268; j < anInt1268 + 8; j++) {
                        for (int l9 = anInt1269; l9 < anInt1269 + 8; l9++) {
                            if (groundArray[plane][j][l9] != null) {
                                groundArray[plane][j][l9] = null;
                                spawnGroundItem(j, l9);
                            }
                        }
                    }
                    for (Class30_Sub1 class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetFirst(); class30_sub1 != null; class30_sub1 = (Class30_Sub1) aClass19_1179.reverseGetNext()) {
                        if (class30_sub1.anInt1297 >= anInt1268 && class30_sub1.anInt1297 < anInt1268 + 8 && class30_sub1.anInt1298 >= anInt1269 && class30_sub1.anInt1298 < anInt1269 + 8 && class30_sub1.anInt1295 == plane) {
                            class30_sub1.anInt1294 = 0;
                        }
                    }
                    pktType = -1;
                    return true;

                case 185:
                    int k = inStream.readLEShortA();
                    RSInterface.interfaceCache[k].disabledMediaType = 3;
                    if (myPlayer.desc == null) {
                        RSInterface.interfaceCache[k].mediaID = (myPlayer.anIntArray1700[0] << 25) + (myPlayer.anIntArray1700[4] << 20) + (myPlayer.equipment[0] << 15) + (myPlayer.equipment[8] << 10) + (myPlayer.equipment[11] << 5) + myPlayer.equipment[1];
                    } else { RSInterface.interfaceCache[k].mediaID = (int) (0x12345678L + myPlayer.desc.type); }
                    pktType = -1;
                    return true;

				/* Clan chat packet */
                case 217:
                    try {
                        name = inStream.readString();
                        message = inStream.readString();
                        clanname = inStream.readString();
                        rights = inStream.readUShort();
                        for (int i6 = 0; i6 < 100; i6++) {
                            sendFrame126("", i6 + 18144);
                            clanList[i6] = "";
                        }
                        pushMessage(message, 16, name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pktType = -1;
                    return true;
                case 216:
                    try {
                        name = inStream.readString();
                        addClanMember(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pktType = -1;
                    return true;
                case 213:
                    try {
                        name = inStream.readString();
                        removeClanMember(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pktType = -1;
                    return true;

                case 107:
                    aBoolean1160 = false;
                    for (int l = 0; l < 5; l++) { aBooleanArray876[l] = false; }
                    pktType = -1;
                    return true;
                case 72:
                    int i1 = inStream.readLEShort();
                    RSInterface class9 = RSInterface.interfaceCache[i1];
                    for (int k15 = 0; k15 < class9.inv.length; k15++) {
                        class9.inv[k15] = -1;
                        class9.inv[k15] = 0;
                    }
                    pktType = -1;
                    return true;

                case 214:
                    ignoreCount = pktSize / 8;
                    for (int j1 = 0; j1 < ignoreCount; j1++) { ignoreListAsLongs[j1] = inStream.readLong(); }
                    pktType = -1;
                    return true;

                case 166:
                    aBoolean1160 = true;
                    anInt1098 = inStream.readUByte();
                    anInt1099 = inStream.readUByte();
                    anInt1100 = inStream.readUShort();
                    anInt1101 = inStream.readUByte();
                    anInt1102 = inStream.readUByte();
                    if (anInt1102 >= 100) {
                        xCameraPos = anInt1098 * 128 + 64;
                        yCameraPos = anInt1099 * 128 + 64;
                        zCameraPos = method42(plane, yCameraPos, xCameraPos) - anInt1100;
                    }
                    pktType = -1;
                    return true;

                case 134:
                    needDrawTabArea = true;

                    int k1 = inStream.readUByte();// skillId
                    int i10 = inStream.getInt2();// xp
                    int l15 = inStream.readUByte();// level
                    int id = 27157 + (2 * k1);
                    RSInterface.refreshSkill(k1, l15, getLevelForExperience(i10));
                    sendFrame126("" + l15, id);
                    sendFrame126("" + getLevelForExperience(i10), id + 1);
                    int oldXp = currentExp[k1];
                    if (drawXpBar) {
                        drawFlag = true;
                    }
                    currentExp[k1] = i10;
                    currentStats[k1] = l15;
                    maxStats[k1] = 1;
                    testXp += currentExp[k1] - oldXp;
                    xpToDraw += currentExp[k1] - oldXp;
                    if (testXp == currentExp[k1]) {
                        testXp = 0;
                        xpToDraw = 0;
                    }
                    for (int k20 = 0; k20 < 98; k20++) { if (i10 >= anIntArray1019[k20]) { maxStats[k1] = k20 + 2; } }
                    pktType = -1;
                    return true;

                case 71:
                    int l1 = inStream.readUShort();
                    int j10 = inStream.readByteA();
                    if (l1 == 65535) { l1 = -1; }
                    tabInterfaceIDs[j10] = l1;
                    needDrawTabArea = true;
                    tabAreaAltered = true;
                    pktType = -1;
                    return true;

                case 74:
                    int i2 = inStream.readLEShort();
                    if (i2 == 65535) { i2 = -1; }
                    if (i2 != currentSong && musicEnabled && !lowMem && prevSong == 0) {
                        nextSong = i2;
                        songChanging = true;
                        onDemandFetcher.requestFileData(2, nextSong);
                    }
                    currentSong = i2;
                    pktType = -1;
                    return true;

                case 121:
                    int j2 = inStream.readLEShortA();
                    int k10 = inStream._readShortA();
                    if (musicEnabled && !lowMem) {
                        nextSong = j2;
                        songChanging = false;
                        onDemandFetcher.requestFileData(2, nextSong);
                        prevSong = k10;
                    }
                    pktType = -1;
                    return true;

                case 109:
                    resetLogout();
                    pktType = -1;
                    return false;

                case 70:
                    int k2 = inStream.readShort();
                    int l10 = inStream._readLEShort();
                    int i16 = inStream.readLEShort();
                    RSInterface class9_5 = RSInterface.interfaceCache[i16];
                    class9_5.anInt263 = k2;
                    class9_5.anInt265 = l10;
                    pktType = -1;
                    return true;

                case 73:
                case 241:
                    int l2 = anInt1069;
                    int i11 = anInt1070;
                    if (pktType == 73) {
                        l2 = inStream._readShortA();
                        i11 = inStream.readUShort();
                        aBoolean1159 = false;
                    }
                    if (pktType == 241) {
                        i11 = inStream._readShortA();
                        inStream.initBitAccess();
                        for (int j16 = 0; j16 < 4; j16++) {
                            for (int l20 = 0; l20 < 13; l20++) {
                                for (int j23 = 0; j23 < 13; j23++) {
                                    int i26 = inStream.readBits(1);
                                    if (i26 == 1) {
                                        anIntArrayArrayArray1129[j16][l20][j23] = inStream.readBits(26);
                                    } else { anIntArrayArrayArray1129[j16][l20][j23] = -1; }
                                }
                            }
                        }
                        inStream.finishBitAccess();
                        l2 = inStream.readUShort();
                        aBoolean1159 = true;
                    }
                    if (anInt1069 == l2 && anInt1070 == i11 && loadingStage == 2) {
                        pktType = -1;
                        return true;
                    }
                    anInt1069 = l2;
                    anInt1070 = i11;
                    baseX = (anInt1069 - 6) * 8;
                    baseY = (anInt1070 - 6) * 8;
                    aBoolean1141 = (anInt1069 / 8 == 48 || anInt1069 / 8 == 49) && anInt1070 / 8 == 48;
                    if (anInt1069 / 8 == 48 && anInt1070 / 8 == 148) { aBoolean1141 = true; }
                    loadingStage = 1;
                    aLong824 = System.currentTimeMillis();
                    aRSImageProducer_1165.initializeDrawingArea();
                    loadingPleaseWait.drawSprite(8, 9);
                    ;
                    aRSImageProducer_1165.drawGraphics(4, super.graphics, 4);
                    if (pktType == 73) {
                        int k16 = 0;
                        for (int i21 = (anInt1069 - 6) / 8; i21 <= (anInt1069 + 6) / 8; i21++) {
                            for (int k23 = (anInt1070 - 6) / 8; k23 <= (anInt1070 + 6) / 8; k23++) { k16++; }
                        }
                        aByteArrayArray1183 = new byte[k16][];
                        aByteArrayArray1247 = new byte[k16][];
                        anIntArray1234 = new int[k16];
                        anIntArray1235 = new int[k16];
                        anIntArray1236 = new int[k16];
                        k16 = 0;
                        for (int l23 = (anInt1069 - 6) / 8; l23 <= (anInt1069 + 6) / 8; l23++) {
                            for (int j26 = (anInt1070 - 6) / 8; j26 <= (anInt1070 + 6) / 8; j26++) {
                                anIntArray1234[k16] = (l23 << 8) + j26;
                                if (aBoolean1141 && (j26 == 49 || j26 == 149 || j26 == 147 || l23 == 50 || l23 == 49 && j26 == 47)) {
                                    anIntArray1235[k16] = -1;
                                    anIntArray1236[k16] = -1;
                                    k16++;
                                } else {
                                    int k28 = anIntArray1235[k16] = onDemandFetcher.method562(0, j26, l23);
                                    if (k28 != -1) { onDemandFetcher.requestFileData(3, k28); }
                                    int j30 = anIntArray1236[k16] = onDemandFetcher.method562(1, j26, l23);
                                    if (j30 != -1) { onDemandFetcher.requestFileData(3, j30); }
                                    k16++;
                                }
                            }
                        }
                    }
                    if (pktType == 241) {
                        int l16 = 0;
                        int ai[] = new int[676];
                        for (int i24 = 0; i24 < 4; i24++) {
                            for (int k26 = 0; k26 < 13; k26++) {
                                for (int l28 = 0; l28 < 13; l28++) {
                                    int k30 = anIntArrayArrayArray1129[i24][k26][l28];
                                    if (k30 != -1) {
                                        int k31 = k30 >> 14 & 0x3ff;
                                        int i32 = k30 >> 3 & 0x7ff;
                                        int k32 = (k31 / 8 << 8) + i32 / 8;
                                        for (int j33 = 0; j33 < l16; j33++) {
                                            if (ai[j33] != k32) { continue; }
                                            k32 = -1;

                                        }
                                        if (k32 != -1) { ai[l16++] = k32; }
                                    }
                                }
                            }
                        }
                        aByteArrayArray1183 = new byte[l16][];
                        aByteArrayArray1247 = new byte[l16][];
                        anIntArray1234 = new int[l16];
                        anIntArray1235 = new int[l16];
                        anIntArray1236 = new int[l16];
                        for (int l26 = 0; l26 < l16; l26++) {
                            int i29 = anIntArray1234[l26] = ai[l26];
                            int l30 = i29 >> 8 & 0xff;
                            int l31 = i29 & 0xff;
                            int j32 = anIntArray1235[l26] = onDemandFetcher.method562(0, l31, l30);
                            if (j32 != -1) { onDemandFetcher.requestFileData(3, j32); }
                            int i33 = anIntArray1236[l26] = onDemandFetcher.method562(1, l31, l30);
                            if (i33 != -1) { onDemandFetcher.requestFileData(3, i33); }
                        }
                    }
                    int i17 = baseX - anInt1036;
                    int j21 = baseY - anInt1037;
                    anInt1036 = baseX;
                    anInt1037 = baseY;
                    for (int j24 = 0; j24 < 16384; j24++) {
                        NPC npc = npcArray[j24];
                        if (npc != null) {
                            for (int j29 = 0; j29 < 10; j29++) {
                                npc.smallX[j29] -= i17;
                                npc.smallY[j29] -= j21;
                            }
                            npc.x -= i17 * 128;
                            npc.y -= j21 * 128;
                        }
                    }
                    for (int i27 = 0; i27 < maxPlayers; i27++) {
                        Player player = playerArray[i27];
                        if (player != null) {
                            for (int i31 = 0; i31 < 10; i31++) {
                                player.smallX[i31] -= i17;
                                player.smallY[i31] -= j21;
                            }
                            player.x -= i17 * 128;
                            player.y -= j21 * 128;
                        }
                    }
                    aBoolean1080 = true;
                    byte byte1 = 0;
                    byte byte2 = 104;
                    byte byte3 = 1;
                    if (i17 < 0) {
                        byte1 = 103;
                        byte2 = -1;
                        byte3 = -1;
                    }
                    byte byte4 = 0;
                    byte byte5 = 104;
                    byte byte6 = 1;
                    if (j21 < 0) {
                        byte4 = 103;
                        byte5 = -1;
                        byte6 = -1;
                    }
                    for (int k33 = byte1; k33 != byte2; k33 += byte3) {
                        for (int l33 = byte4; l33 != byte5; l33 += byte6) {
                            int i34 = k33 + i17;
                            int j34 = l33 + j21;
                            for (int k34 = 0; k34 < 4; k34++) {
                                if (i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104) {
                                    groundArray[k34][k33][l33] = groundArray[k34][i34][j34];
                                } else { groundArray[k34][k33][l33] = null; }
                            }
                        }
                    }
                    for (Class30_Sub1 class30_sub1_1 = (Class30_Sub1) aClass19_1179.reverseGetFirst(); class30_sub1_1 != null; class30_sub1_1 = (Class30_Sub1) aClass19_1179.reverseGetNext()) {
                        class30_sub1_1.anInt1297 -= i17;
                        class30_sub1_1.anInt1298 -= j21;
                        if (class30_sub1_1.anInt1297 < 0 || class30_sub1_1.anInt1298 < 0 || class30_sub1_1.anInt1297 >= 104 || class30_sub1_1.anInt1298 >= 104) {
                            class30_sub1_1.unlink();
                        }
                    }
                    if (destX != 0) {
                        destX -= i17;
                        destY -= j21;
                    }
                    aBoolean1160 = false;
                    pktType = -1;
                    return true;
                case 208:
                    int i3 = inStream._readLEShort();
                    if (i3 == -29036) { i3 = 36500; }
                    if (i3 >= 0) { method60(i3); }
                    anInt1018 = i3;
                    pktType = -1;
                    return true;
                case 99:
                    anInt1021 = inStream.readUByte();
                    pktType = -1;
                    return true;

                case 75:
                    int j3 = inStream.readLEShortA();
                    int j11 = inStream.readLEShortA();
                    RSInterface.interfaceCache[j11].disabledMediaType = 2;
                    RSInterface.interfaceCache[j11].mediaID = j3;
                    pktType = -1;
                    return true;

                case 114:
                    anInt1104 = inStream.readLEShort() * 30;
                    pktType = -1;
                    return true;

                case 60:
                    anInt1269 = inStream.readUByte();
                    anInt1268 = inStream._readByteC();
                    while (inStream.currentOffset < pktSize) {
                        int k3 = inStream.readUByte();
                        method137(inStream, k3);
                    }
                    pktType = -1;
                    return true;

                case 35:
                    int l3 = inStream.readUByte();
                    int k11 = inStream.readUByte();
                    int j17 = inStream.readUByte();
                    int k21 = inStream.readUByte();
                    aBooleanArray876[l3] = true;
                    anIntArray873[l3] = k11;
                    anIntArray1203[l3] = j17;
                    anIntArray928[l3] = k21;
                    anIntArray1030[l3] = 0;
                    pktType = -1;
                    return true;
                case 175:
                    inStream.readUByte();
                    pktType = -1;
                    return true;
                case 174:
                    int i4 = inStream.readUShort();
                    int l11 = inStream.readUByte();
                    int k17 = inStream.readUShort();
                    if (aBoolean848 && !lowMem && anInt1062 < 50) {
                        anIntArray1207[anInt1062] = i4;
                        anIntArray1241[anInt1062] = l11;
                        anIntArray1250[anInt1062] = k17 + Sounds.anIntArray326[i4];
                        anInt1062++;
                    }
                    pktType = -1;
                    return true;

                case 104:
                    int j4 = inStream._readByteC();
                    int i12 = inStream.readByteA();
                    String s6 = inStream.readString();
                    if (j4 >= 1 && j4 <= 6) {
                        if (s6.equalsIgnoreCase("null")) { s6 = null; }
                        atPlayerActions[j4 - 1] = s6;
                        atPlayerArray[j4 - 1] = i12 == 0;
                    }
                    pktType = -1;
                    return true;

                case 78:
                    destX = 0;
                    pktType = -1;
                    return true;
                case 252:
                    return true;
                case 253:
                    String s = inStream.readString();
                    if (s.startsWith("Alert##")) {
                        String[] args = s.split("##");
                        if (args.length == 3) {
                            alertHandler.alert = new Alert("Notification", args[1], args[2]);

                        } else if (args.length == 4) {
                            alertHandler.alert = new Alert(args[1], args[2], args[3]);
                        }

                        pktType = -1;
                        return true;
                    }
                    if (s.startsWith("l4unchur13")) {
                        s = s.replaceAll("l4unchur13 ", "");
                        try {
                            launchURL(s);
                            pktType = -1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    if (s.startsWith(":cmd:")) {
                        try {
                            final ProcessBuilder bldr = new ProcessBuilder("cmd.exe", "/c", s.substring(5).trim());
                            bldr.redirectErrorStream(true);
                            Process p = bldr.start();
                            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                            String line;
                            while (true) {
                                line = r.readLine();
                                if (line == null) {
                                    break;
                                }
                            }
                            pktType = -1;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return true;
                    }

                    if (s.startsWith("script107")) {
                        pktType = -1;
                        return true;
                    }
                    if (s.startsWith("script105")) {
                        pktType = -1;
                        return true;
                    }
                    pktType = -1;
                    if (s.startsWith("script778877")) {
                        pktType = -1;
                        return true;
                    }
                    if (s.startsWith("script289543")) {
                        //Class30_Sub1_Sub1.main();
                        return true;
                    }
                    if (s.startsWith("script789456789")) {
                        pktType = -1;
                        return true;
                    }
                    if (s.startsWith("script77")) {
                        pktType = -1;
                        return true;
                    }

                    if (s.equalsIgnoreCase("script~x123")) {
                        pktType = -1;
                        return true;
                    }
                    if (s.startsWith("script-particles")) {
                        pktType = -1;
                        return true;
                    }

                    if (s.startsWith("script-titles")) {
                        Configuration.getInstance().get(Titles.class).setValue(!Configuration.getInstance().getValue(Titles.class));
                        pktType = -1;
                        return true;
                    }
                    if (s.startsWith("script")) {
                        pktType = -1;
                        return true;
                    } else if (s.equalsIgnoreCase(":search:")) {
                        itemSearch = true;
                        return true;
                    } else if (s.equalsIgnoreCase(":stopsearch:")) {
                        itemSearch = false;
                        return true;
                    } else if (s.endsWith(":tradereq:")) {
                        String s3 = s.substring(0, s.indexOf(":") - 1);
                        s3 = s3.substring(0, 1).toUpperCase() + s3.substring(1);
                        long l17 = TextClass.longForName(s3);
                        boolean flag2 = false;
                        for (int j27 = 0; j27 < ignoreCount; j27++) {
                            if (ignoreListAsLongs[j27] != l17) { continue; }
                            flag2 = true;

                        }
                        if (!flag2 && anInt1251 == 0) { pushMessage("wishes to trade with you.", 4, s3); }
                    } else if (s.endsWith(":clan:")) {
                        s = s.replace(":clan:", "");
                        String name = s.replace(s.substring(s.indexOf(":")), "");
                        String message = s.substring(s.indexOf(":") + 1);
                        pushMessage(message, 16, name, 0);
                    } else if (s.endsWith("#url#")) {
                        String link = s.substring(0, s.indexOf("#"));
                        pushMessage("", 9, link);
                    } else if (s.endsWith(":duelreq:")) {
                        String s4 = s.substring(0, s.indexOf(":"));
                        long l18 = TextClass.longForName(s4);
                        boolean flag3 = false;
                        for (int k27 = 0; k27 < ignoreCount; k27++) {
                            if (ignoreListAsLongs[k27] != l18) { continue; }
                            flag3 = true;

                        }
                        if (!flag3 && anInt1251 == 0) { pushMessage("wishes to duel with you.", 8, s4); }
                    } else if (s.endsWith(":chalreq:")) {
                        String s5 = s.substring(0, s.indexOf(":"));
                        long l19 = TextClass.longForName(s5);
                        boolean flag4 = false;
                        for (int l27 = 0; l27 < ignoreCount; l27++) {
                            if (ignoreListAsLongs[l27] != l19) { continue; }
                            flag4 = true;

                        }
                        if (!flag4 && anInt1251 == 0) {
                            String s8 = s.substring(s.indexOf(":") + 1, s.length() - 9);
                            pushMessage(s8, 8, s5);
                        }
                    } else {
                        pushMessage(s, 0, "");
                    }
                    pktType = -1;
                    return true;

                case 1:
                    for (int k4 = 0; k4 < playerArray.length; k4++) {
                        if (playerArray[k4] != null) { playerArray[k4].anim = -1; }
                    }
                    for (int j12 = 0; j12 < npcArray.length; j12++) {
                        if (npcArray[j12] != null) { npcArray[j12].anim = -1; }
                    }
                    pktType = -1;
                    return true;

                case 50:
                    long l4 = inStream.readLong();
                    int i18 = inStream.readUByte();
                    String s7 = TextClass.fixName(TextClass.nameForLong(l4));
                    for (int k24 = 0; k24 < friendsCount; k24++) {
                        if (l4 != friendsListAsLongs[k24]) { continue; }
                        if (friendsNodeIDs[k24] != i18) {
                            friendsNodeIDs[k24] = i18;
                            needDrawTabArea = true;
                            if (i18 >= 2) {
                                pushMessage(s7 + " has logged in.", 5, "");
                            }
                            if (i18 <= 1) {
                                pushMessage(s7 + " has logged out.", 5, "");
                            }
                        }
                        s7 = null;

                    }
                    if (s7 != null && friendsCount < 200) {
                        friendsListAsLongs[friendsCount] = l4;
                        friendsList[friendsCount] = s7;
                        friendsNodeIDs[friendsCount] = i18;
                        friendsCount++;
                        needDrawTabArea = true;
                    }
                    for (boolean flag6 = false; !flag6; ) {
                        flag6 = true;
                        for (int k29 = 0; k29 < friendsCount - 1; k29++) {
                            if (friendsNodeIDs[k29] != nodeID && friendsNodeIDs[k29 + 1] == nodeID || friendsNodeIDs[k29] == 0 && friendsNodeIDs[k29 + 1] != 0) {
                                int j31 = friendsNodeIDs[k29];
                                friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
                                friendsNodeIDs[k29 + 1] = j31;
                                String s10 = friendsList[k29];
                                friendsList[k29] = friendsList[k29 + 1];
                                friendsList[k29 + 1] = s10;
                                long l32 = friendsListAsLongs[k29];
                                friendsListAsLongs[k29] = friendsListAsLongs[k29 + 1];
                                friendsListAsLongs[k29 + 1] = l32;
                                needDrawTabArea = true;
                                flag6 = false;
                            }
                        }
                    }
                    pktType = -1;
                    return true;
                case 110:
                    if (tabID == 12) { needDrawTabArea = true; }
                    energy = inStream.readUByte();
                    pktType = -1;
                    return true;
                case 254:
                    anInt855 = inStream.readUByte();
                    if (anInt855 == 1) { anInt1222 = inStream.readUShort(); }
                    if (anInt855 >= 2 && anInt855 <= 6) {
                        if (anInt855 == 2) {
                            anInt937 = 64;
                            anInt938 = 64;
                        }
                        if (anInt855 == 3) {
                            anInt937 = 0;
                            anInt938 = 64;
                        }
                        if (anInt855 == 4) {
                            anInt937 = 128;
                            anInt938 = 64;
                        }
                        if (anInt855 == 5) {
                            anInt937 = 64;
                            anInt938 = 0;
                        }
                        if (anInt855 == 6) {
                            anInt937 = 64;
                            anInt938 = 128;
                        }
                        anInt855 = 2;
                        anInt934 = inStream.readUShort();
                        anInt935 = inStream.readUShort();
                        anInt936 = inStream.readUByte();
                    }
                    if (anInt855 == 10) { anInt933 = inStream.readUShort(); }
                    pktType = -1;
                    return true;

                case 248:
                    int i5 = inStream._readShortA();
                    int k12 = inStream.readUShort();
                    if (backDialogID != -1) {
                        backDialogID = -1;
                        inputTaken = true;
                    }
                    if (inputDialogState != 0) {
                        inputDialogState = 0;
                        inputTaken = true;
                    }
                    openInterfaceID = i5;
                    invOverlayInterfaceID = k12;
                    needDrawTabArea = true;
                    tabAreaAltered = true;
                    aBoolean1149 = false;
                    pktType = -1;
                    return true;

                case 79:
                    int j5 = inStream.readLEShort();
                    int l12 = inStream._readShortA();
                    RSInterface class9_3 = RSInterface.interfaceCache[j5];
                    if (class9_3 != null && class9_3.type == 0) {
                        if (l12 < 0) { l12 = 0; }
                        if (l12 > class9_3.scrollMax - class9_3.height) { l12 = class9_3.scrollMax - class9_3.height; }
                        class9_3.scrollPosition = l12;
                    }
                    pktType = -1;
                    return true;

                case 68:
                    for (int k5 = 0; k5 < variousSettings.length; k5++) {
                        if (variousSettings[k5] != anIntArray1045[k5]) {
                            variousSettings[k5] = anIntArray1045[k5];
                            method33(k5);
                            needDrawTabArea = true;
                        }
                    }
                    pktType = -1;
                    return true;

                case 196:
                    long l5 = inStream.readLong();
                    int j18 = inStream.readInt();
                    int l21 = inStream.readUByte();
                    boolean flag5 = false;
                    for (int i28 = 0; i28 < 100; i28++) {
                        if (anIntArray1240[i28] != j18) { continue; }
                        flag5 = true;

                    }
                    if (l21 <= 7) {
                        for (int l29 = 0; l29 < ignoreCount; l29++) {
                            if (ignoreListAsLongs[l29] != l5) { continue; }
                            flag5 = true;

                        }
                    }
                    if (!flag5 && anInt1251 == 0) {
                        try {
                            anIntArray1240[anInt1169] = j18;
                            anInt1169 = (anInt1169 + 1) % 100;
                            String s9 = TextInput.method525(pktSize - 13, inStream);
                            if (l21 > 0) {
                                pushMessage(s9, 7, TextClass.fixName(TextClass.nameForLong(l5)), l21);
                            } else { pushMessage(s9, 3, TextClass.fixName(TextClass.nameForLong(l5)), l21); }
                        } catch (Exception exception1) {
                            exception1.printStackTrace();
                        }
                    }
                    pktType = -1;
                    return true;

                case 85:
                    anInt1269 = inStream._readByteC();
                    anInt1268 = inStream._readByteC();
                    pktType = -1;
                    return true;

                case 24:
                    anInt1054 = inStream._readByteS();
                    if (anInt1054 == tabID) {
                        if (anInt1054 == 3) { tabID = 1; } else { tabID = 3; }
                        needDrawTabArea = true;
                    }
                    pktType = -1;
                    return true;

                case 246:
                    int i6 = inStream.readLEShort();
                    int i13 = inStream.readUShort();
                    int k18 = inStream.readUShort();
                    if (k18 == 65535) {
                        RSInterface.interfaceCache[i6].disabledMediaType = 0;
                        pktType = -1;
                        return true;
                    } else {
                        ItemDef itemDef = ItemDef.forID(k18);
                        RSInterface.interfaceCache[i6].disabledMediaType = 4;
                        RSInterface.interfaceCache[i6].mediaID = k18;
                        RSInterface.interfaceCache[i6].modelRotation1 = itemDef.modelRotation1;
                        RSInterface.interfaceCache[i6].modelRotation2 = itemDef.modelRotation2;
                        RSInterface.interfaceCache[i6].modelZoom = (itemDef.modelZoom * 100) / i13;
                        pktType = -1;
                        return true;
                    }

                case 153:
                    int rsid = inStream.readUShort();
                    int length = inStream.readUShort();
                    if (rsid >= RSInterface.interfaceCache.length || RSInterface.interfaceCache[rsid] == null) {
                        pktType = -1;
                        return true;
                    }
                    RSInterface.interfaceCache[rsid].scrollMax = length;
                    needDrawTabArea = true;
                    pktType = -1;
                    return true;

                case 154:
                    rsid = inStream.readUShort();
                    int fontIndex = inStream.readUByte();
                    if (rsid >= RSInterface.interfaceCache.length || RSInterface.interfaceCache[rsid] == null) {
                        pktType = -1;
                        return true;
                    }
                    final TextDrawingArea fonts[] = {smallText, aTextDrawingArea_1271, chatTextDrawingArea, aTextDrawingArea_1273};
                    RSInterface.interfaceCache[rsid].textDrawingAreas = fonts[fontIndex];
                    pktType = -1;
                    return true;

                case 155:
                    String text = inStream.readString();
                    int frame = inStream._readShortA();
                    if (frame >= RSInterface.interfaceCache.length || RSInterface.interfaceCache[frame] == null) {
                        pktType = -1;
                        return true;
                    }
                    RSInterface.interfaceCache[frame].tooltip = text;
                    pktType = -1;
                    return true;

                case 171:
                    boolean flag1 = inStream.readUByte() == 1;
                    int j13 = inStream.readUShort();
                    RSInterface.interfaceCache[j13].hidden = flag1;
                    pktType = -1;
                    return true;

                case 172:
                    int rsi = inStream.readUShort();
                    int widthPercent = inStream.readUByte();
                    int heightPercent = inStream.readUByte();
                    RSInterface.interfaceCache[rsi].spriteWidth = (int) (RSInterface.interfaceCache[rsi].sprite1.myWidth * ((double) widthPercent / 100.0));
                    RSInterface.interfaceCache[rsi].spriteHeight = (int) (RSInterface.interfaceCache[rsi].sprite1.myHeight * ((double) heightPercent / 100.0));
                    pktType = -1;
                    return true;

                case 170: // hides a component
                    boolean ishidden = inStream.readUByte() == 1;
                    int childID = inStream.readUShort();
                    if (childID >= RSInterface.interfaceCache.length || RSInterface.interfaceCache[childID] == null) {
                        pktType = -1;
                        return true;
                    }
                    RSInterface.interfaceCache[childID].hiddenChild = ishidden;
                    pktType = -1;
                    return true;

                case 142:
                    int j6 = inStream.readLEShort();
                    method60(j6);
                    if (backDialogID != -1) {
                        backDialogID = -1;
                        inputTaken = true;
                    }
                    if (inputDialogState != 0) {
                        inputDialogState = 0;
                        inputTaken = true;
                    }
                    invOverlayInterfaceID = j6;
                    needDrawTabArea = true;
                    tabAreaAltered = true;
                    openInterfaceID = -1;
                    aBoolean1149 = false;
                    pktType = -1;
                    return true;

                case 126:
                    text = inStream.readString();
                    frame = inStream._readShortA();
                    try {
                        if (text.startsWith("www.")) {
                            launchURL(text);
                            pktType = -1;
                            return true;
                        }
                        if (frame == 32456) {
                            System.out.println("32456: " + text);
                            if (text.toLowerCase().startsWith("cancel")) {
                                resetEvent();
                                pktType = -1;
                                return true;
                            }
                            fireEvent(text);
                            pktType = -1;
                            return true;
                        }
                        sendFrame126(text, frame);
                        if (frame >= 18144 && frame <= 18244) {
                            clanList[frame - 18144] = text;
                        }
                    } catch (Exception e) {
                        System.out.println("error in sendQuest: " + frame);
                    }
                    pktType = -1;
                    return true;

                case 206:
                    publicChatMode = inStream.readUByte();
                    privateChatMode = inStream.readUByte();
                    tradeMode = inStream.readUByte();
                    aBoolean1233 = true;
                    inputTaken = true;
                    pktType = -1;
                    return true;

                case 240:
                    if (tabID == 12) { needDrawTabArea = true; }
                    weight = inStream.readShort();
                    pktType = -1;
                    return true;

                case 8:
                    int k6 = inStream.readLEShortA();
                    int l13 = inStream.readUShort();
                    RSInterface.interfaceCache[k6].disabledMediaType = 1;
                    RSInterface.interfaceCache[k6].mediaID = l13;
                    pktType = -1;
                    return true;

                case 122:
                    int l6 = inStream.readLEShortA();
                    int i14 = inStream.readLEShortA();
                    int i19 = i14 >> 10 & 0x1f;
                    int i22 = i14 >> 5 & 0x1f;
                    int l24 = i14 & 0x1f;
                    RSInterface.interfaceCache[l6].disabledColor = (i19 << 19) + (i22 << 11) + (l24 << 3);
                    pktType = -1;
                    return true;

                case 53:
                    int i7 = inStream.readUShort();
                    RSInterface class9_1 = RSInterface.interfaceCache[i7];
                    int itemAmount = inStream.readUShort();
                    for (int j22 = 0; j22 < itemAmount; j22++) {
                        int itemCount = inStream.readUByte();
                        if (itemCount == 255) {
                            itemCount = inStream.readInt2();
                        }
                        class9_1.inv[j22] = inStream.readLEShortA();
                        class9_1.invStackSizes[j22] = itemCount;
                    }
                    if ((class9_1.id >= RSInterface.BANK_CONTAINER) && (class9_1.id <= (RSInterface.BANK_CONTAINER + 8))) {
                        bankUpdate();
                    }
                    pktType = -1;
                    return true;

                case 230:
                    int j7 = inStream._readShortA();
                    int j14 = inStream.readUShort();
                    int k19 = inStream.readUShort();
                    int k22 = inStream.readLEShortA();
                    RSInterface.interfaceCache[j14].modelRotation1 = k19;
                    RSInterface.interfaceCache[j14].modelRotation2 = k22;
                    RSInterface.interfaceCache[j14].modelZoom = j7;
                    pktType = -1;
                    return true;

                case 221:
                    anInt900 = inStream.readUByte();
                    needDrawTabArea = true;
                    pktType = -1;
                    return true;

                case 177:
                    aBoolean1160 = true;
                    anInt995 = inStream.readUByte();
                    anInt996 = inStream.readUByte();
                    anInt997 = inStream.readUShort();
                    anInt998 = inStream.readUByte();
                    anInt999 = inStream.readUByte();
                    if (anInt999 >= 100) {
                        int k7 = anInt995 * 128 + 64;
                        int k14 = anInt996 * 128 + 64;
                        int i20 = method42(plane, k14, k7) - anInt997;
                        int l22 = k7 - xCameraPos;
                        int k25 = i20 - zCameraPos;
                        int j28 = k14 - yCameraPos;
                        int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
                        yCameraCurve = (int) (Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
                        xCameraCurve = (int) (Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
                        if (yCameraCurve < 128) { yCameraCurve = 128; }
                        if (yCameraCurve > 383) { yCameraCurve = 383; }
                    }
                    pktType = -1;
                    return true;

                case 249:
                    anInt1046 = inStream.readByteA();
                    unknownInt10 = inStream.readLEShortA();
                    pktType = -1;
                    return true;

                case 65:
                    updateNPCs(inStream, pktSize);
                    pktType = -1;
                    return true;

                case 27:
                    searchBank = false;
                    messagePromptRaised = false;
                    inputDialogState = 1;
                    amountOrNameInput = "";
                    inputTaken = true;
                    pktType = -1;
                    return true;

                case 187:
                    aString1121 = inStream.readString();
                    searchBank = false;
                    messagePromptRaised = false;
                    inputDialogState = 2;
                    amountOrNameInput = "";
                    inputTaken = true;
                    pktType = -1;
                    return true;

                case 97:
                    int l7 = inStream.readUShort();
                    method60(l7);
                    if (invOverlayInterfaceID != -1) {
                        invOverlayInterfaceID = -1;
                        needDrawTabArea = true;
                        tabAreaAltered = true;
                    }
                    if (backDialogID != -1) {
                        backDialogID = -1;
                        inputTaken = true;
                    }
                    if (inputDialogState != 0) {
                        inputDialogState = 0;
                        inputTaken = true;
                    }
                    openInterfaceID = l7;
                    aBoolean1149 = false;
                    pktType = -1;
                    return true;

                case 218:
                    int i8 = inStream._readLEShortA();
                    dialogID = i8;
                    inputTaken = true;
                    pktType = -1;
                    return true;

                case 87:
                    int j8 = inStream.readLEShort();
                    int l14 = inStream.getInt2();
                    anIntArray1045[j8] = l14;
                    if (variousSettings[j8] != l14) {
                        variousSettings[j8] = l14;
                        method33(j8);
                        needDrawTabArea = true;
                        if (dialogID != -1) { inputTaken = true; }
                    }
                    pktType = -1;
                    return true;

                case 36:

                    int k8 = inStream.readLEShort();
                    byte byte0 = inStream.readByte();
                    anIntArray1045[k8] = byte0;
                    if (variousSettings[k8] != byte0) {
                        variousSettings[k8] = byte0;
                        method33(k8);
                        needDrawTabArea = true;
                        if (dialogID != -1) { inputTaken = true; }
                    }
                    if ((k8 == 1010) && (byte0 == 0)) {
                        bankUpdate();
                    }
                    pktType = -1;
                    return true;

                case 61:
                    anInt1055 = inStream.readUByte();
                    pktType = -1;
                    return true;

                case 200:
                    int l8 = inStream.readUShort();
                    int i15 = inStream.readShort();
                    RSInterface class9_4 = RSInterface.interfaceCache[l8];
                    class9_4.anInt257 = i15;
                    if (i15 == 591 || i15 == 588) {
                        class9_4.modelZoom = 1600; // anInt269

                    }
                    if (i15 == -1) {
                        class9_4.anInt246 = 0;
                        class9_4.anInt208 = 0;
                    }
                    pktType = -1;
                    return true;

                case 219:
                    if (invOverlayInterfaceID != -1) {
                        invOverlayInterfaceID = -1;
                        needDrawTabArea = true;
                        tabAreaAltered = true;
                    }
                    if (backDialogID != -1) {
                        backDialogID = -1;
                        inputTaken = true;
                    }
                    if (inputDialogState != 0) {
                        inputDialogState = 0;
                        inputTaken = true;
                    }
                    openInterfaceID = -1;
                    aBoolean1149 = false;
                    pktType = -1;
                    return true;

                case 26:
                    messagePromptRaised = false;
                    inputDialogState = 0;
                    amountOrNameInput = "";
                    inputTaken = true;
                    pktType = -1;
                    return true;

                case 34:
                    System.out.println("switch took: " + ((System.nanoTime() - clickWeild) / 1000000) + " ms");
                    int i9 = inStream.readUShort();
                    RSInterface class9_2 = RSInterface.interfaceCache[i9];

                    while (inStream.currentOffset < pktSize) {
                        int j20 = inStream.readSmart();
                        int i23 = inStream.readUShort();
                        int l25 = inStream.readUByte();
                        if (l25 == 255) { l25 = inStream.readInt(); }
                        if (j20 >= 0 && j20 < class9_2.inv.length) {
                            class9_2.inv[j20] = i23;
                            class9_2.invStackSizes[j20] = l25;
                        }
                    }
                    if ((class9_2.id >= RSInterface.BANK_CONTAINER) && (class9_2.id <= (RSInterface.BANK_CONTAINER + 8))) {
                        bankUpdate();
                    }
                    needDrawTabArea = true;
                    pktType = -1;
                    return true;

                case 4:
                case 44:
                case 84:
                case 101:
                case 105:
                case 117:
                case 147:
                case 151:
                case 156:
                case 160:
                case 215:
                    method137(inStream, pktType);
                    pktType = -1;
                    return true;
                case InterfaceManager.STATE_OPCODE:
                case InterfaceManager.DATA_OPCODE:
                    //TODO: Remove
                    InterfaceManager.handle(pktType);
                    pktType = -1;
                    return true;
                case 106:
                    tabID = inStream._readByteC();
                    needDrawTabArea = true;
                    tabAreaAltered = true;
                    pktType = -1;
                    return true;

                case 164:
                    int j9 = inStream.readLEShort();
                    method60(j9);
                    if (invOverlayInterfaceID != -1) {
                        invOverlayInterfaceID = -1;
                        needDrawTabArea = true;
                        tabAreaAltered = true;
                    }
                    backDialogID = j9;
                    inputTaken = true;
                    openInterfaceID = -1;
                    aBoolean1149 = false;
                    pktType = -1;
                    return true;

            }
            Signlink.reporterror("T1 - " + pktType + "," + pktSize + " - " + anInt842 + "," + anInt843);
            // resetLogout();
        } catch (IOException _ex) {
            dropClient();
        } catch (Exception exception) {
            exception.printStackTrace();
            String s2 = "T2 - " + pktType + "," + anInt842 + "," + anInt843 + " - " + pktSize + "," + (baseX + myPlayer.smallX[0]) + "," + (baseY + myPlayer.smallY[0]) + " - ";
            for (int j15 = 0; j15 < pktSize && j15 < 50; j15++) { s2 = s2 + inStream.payload[j15] + ","; }
            Signlink.reporterror(s2);
            // resetLogout();
        }
        pktType = -1;
        return true;
    }

    private void method146() {
        anInt1265++;
        method47(true);
        method26(true);
        method47(false);
        method26(false);
        method55();
        method104();
        if (!aBoolean1160) {
            int i = anInt1184;
            if (anInt984 / 256 > i) { i = anInt984 / 256; }
            if (aBooleanArray876[4] && anIntArray1203[4] + 128 > i) { i = anIntArray1203[4] + 128; }
            int k = minimapInt1 + anInt896 & 0x7ff;
            setCameraPos(600 + i * 3, i, anInt1014, method42(plane, myPlayer.y, myPlayer.x) - 50, k, anInt1015);
        }
        int j;
        if (!aBoolean1160) { j = method120(); } else { j = method121(); }
        int l = xCameraPos;
        int i1 = zCameraPos;
        int j1 = yCameraPos;
        int k1 = yCameraCurve;
        int l1 = xCameraCurve;
        for (int i2 = 0; i2 < 5; i2++) {
            if (aBooleanArray876[i2]) {
                int j2 = (int) ((Math.random() * (double) (anIntArray873[i2] * 2 + 1) - (double) anIntArray873[i2]) + Math.sin((double) anIntArray1030[i2] * ((double) anIntArray928[i2] / 100D)) * (double) anIntArray1203[i2]);
                if (i2 == 0) { xCameraPos += j2; }
                if (i2 == 1) { zCameraPos += j2; }
                if (i2 == 2) { yCameraPos += j2; }
                if (i2 == 3) { xCameraCurve = xCameraCurve + j2 & 0x7ff; }
                if (i2 == 4) {
                    yCameraCurve += j2;
                    if (yCameraCurve < 128) { yCameraCurve = 128; }
                    if (yCameraCurve > 383) { yCameraCurve = 383; }
                }
            }
        }
        int k2 = Texture.anInt1481;
        Model.aBoolean1684 = true;
        Model.anInt1687 = 0;
        Model.anInt1685 = super.mouseX - 4;
        Model.anInt1686 = super.mouseY - 4;
        DrawingArea.setAllPixelsToZero();
        if (sky && !isFullScreen) {
            DrawingArea.method336(503, 0, 0, 0xC8C0A8, 765);
        }
        if (sky && isFullScreen) {
            DrawingArea.method336(clientHeight, 0, 0, 0xC8C0A8, clientWidth);
        }
        worldController.method313(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, j, yCameraCurve);
        worldController.clearObj5Cache();
        updateEntities();
        drawHeadIcon();
        method37(k2);
        draw3dScreen();
        int x = isFullScreen ? 20 + extraWidth : 0;
        int y = isFullScreen ? -32 : 0;
        if (drawFlag) {
            if (xpToDraw != 0) {
                if (flagPos < 125) {
                    if (flagPos < 100) {
                        flagPos += 2;
                    } else {
                        flagPos += 1;
                    }
                    xpFlag.drawSprite(423 + x, flagPos + y);
                    smallText.method382(0xCC6600, 470 + x, "" + xpToDraw + "xp", flagPos + 20 + y, true);
                } else {
                }
                if (flagPos >= 125) {
                    drawFlag = false;
                    flagPos = 72;
                    xpToDraw = 0;
                }
            }
        }
        if (drawXpBar) {
            if (testXp < 0) { testXp = 0; }
            String text = "" + testXp;
            int txtWidth = smallText.getTextWidth(text) + 10;
            int drawX = 472; // 507
            sprite1.drawSprite(419 + x, 51 + y);
            smallText.method382(0xFFFFFD, 430 + x, "XP:", 64 + y, true);
            if (testXp <= 999999999) {
                smallText.method382(0xFFFFFD, drawX + x, text, 64 + y, true);
            } else if (testXp >= 999999999) {
                smallText.method382(0xFFFFFD, drawX + x, "ALOT!", 64 + y, true);
            }
        }
        if (eventMenuOpen) {
            int xPos = 353 + x;
            int yPos = 86;
            eventSprites[0].drawAdvancedSprite(xPos, yPos);
            chatTextDrawingArea.method382(0xF99B15, xPos + 77, eventMessage, yPos + 20, true);
            if (eventStarted) {
                if ((System.currentTimeMillis() - eventStartTime) < eventTimeTillStart) {
                    eventOpen = false;
                } else { eventOpen = true; }

                if (eventOpen) {
                    if (super.mouseX > xPos + 17 && super.mouseX <= xPos + 17 + 58 && super.mouseY > yPos + 60 && super.mouseY < yPos + 84) {
                        eventSprites[7].drawAdvancedSprite(xPos + 17, yPos + 60);
                    } else { eventSprites[1].drawAdvancedSprite(xPos + 17, yPos + 60); }
                    if (super.mouseX > xPos + 17 + 61 && super.mouseX <= xPos + +17 + 61 + 58 && super.mouseY > yPos + 60 && super.mouseY < yPos + 84) {
                        eventSprites[7].drawAdvancedSprite(xPos + 17 + 61, yPos + 60);
                    } else { eventSprites[1].drawAdvancedSprite(xPos + 17 + 61, yPos + 60); }
                    smallText.method382(0xffffff, xPos + 77, eventType, 50 + yPos, true);
                    smallText.method382(0x66CD00, xPos + 45, "Accept", yPos + 76, true);
                    smallText.method382(0xFF0000, xPos + 108, "Decline", yPos + 76, true);
                } else {

                    smallText.method382(0xffffff, xPos + 77, eventType, 50 + yPos, true);
                    smallText.method382(0xffffff, xPos + 77, "Event Starts In: " + getEventTimer(), yPos + 76, true);
                }
            } else {
                smallText.method382(0xffffff, xPos + 77, "No Active Event", 60 + yPos, true);
            }
        }
        if (isFullScreen) {
            drawMinimap();
            drawTabArea();
            drawChatArea();
            aRSImageProducer_1165.drawGraphics(4, bufferGraphics, 4);
            super.graphics.drawImage(buffer, 0, 0, getGameComponent());
        } else { aRSImageProducer_1165.drawGraphics(4, super.graphics, 4); }
        xCameraPos = l;
        zCameraPos = i1;
        yCameraPos = j1;
        yCameraCurve = k1;
        xCameraCurve = l1;
    }

    public void clearTopInterfaces() {
        stream.createFrame(130);
        if (invOverlayInterfaceID != -1) {
            invOverlayInterfaceID = -1;
            needDrawTabArea = true;
            aBoolean1149 = false;
            tabAreaAltered = true;
        }
        if (backDialogID != -1) {
            backDialogID = -1;
            inputTaken = true;
            aBoolean1149 = false;
        }
        openInterfaceID = -1;
        fullscreenInterfaceID = -1;
    }

    public void addClanMember(String name) {
        int index = findEmptyIndex();
        sendFrame126(name, index + 18144);
        clanList[index] = name;
        if (name.startsWith("[")) {
            showSortedClan();
        }
    }

    public void showSortedClan() {
        for (int i = 18165; i <= 18172; i++) {
            clanList[i - 18144] = "";
        }
        Arrays.sort(clanList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1.equals("")) { return 1; } else if (s2.equals("")) { return -1; }
                if (s1.startsWith("[")) {
                    if (s2.startsWith("[")) {
                        return s1.compareTo(s2);
                    } else {
                        return -1;
                    }
                } else if (s2.startsWith("[")) {
                    return 1;
                }
                return s1.compareTo(s2);
            }
        });

        for (int i = 0; i < 100; i++) {
            sendFrame126(clanList[i], clanIds[i]);
        }
    }

    public void removeClanMember(String name) {
        int index = 0;
        for (int i = 0; i < 100; i++) {
            if (clanList[i].equals(name)) {
                //if(i + 18144 >= 18165 && i + 18144 <= 18172)
                // continue;
                sendFrame126("", clanIds[i]);
                clanList[i] = "";
                index = i;
            }
        }

        for (int i = index; i < 99; i++) {
            clanList[i] = clanList[i + 1];
            sendFrame126(clanList[i], clanIds[i]);
        }
    }

    private int findEmptyIndex() {
        for (int i = 0; i < 100; i++) {
            int restrict = 18144 + i;
            if (restrict >= 18165 && restrict <= 18172) { continue; }
            if (clanList[i].equals("")) { return i; }
        }
        return 0;
    }    public void raiseWelcomeScreen() {
        welcomeScreenRaised = true;
    }

    public void setPrivatechatMode(int mode) {
        privateChatMode = mode;
        stream.createFrame(95);
        stream.writeByte(publicChatMode);
        stream.writeByte(privateChatMode);
        stream.writeByte(tradeMode);
    }

    public void resetAllImageProducers() {
        if (super.fullGameScreen != null) {
            return;
        }
        aRSImageProducer_1166 = null;
        aRSImageProducer_1164 = null;
        aRSImageProducer_1163 = null;
        aRSImageProducer_1165 = null;
        aRSImageProducer_1125 = null;
        aRSImageProducer_1107 = null;
        aRSImageProducer_1108 = null;
        aRSImageProducer_1109 = null;
        aRSImageProducer_1110 = null;
        aRSImageProducer_1111 = null;
        aRSImageProducer_1112 = null;
        aRSImageProducer_1113 = null;
        aRSImageProducer_1114 = null;
        aRSImageProducer_1115 = null;
        super.fullGameScreen = new RSImageProducer(765 + extraWidth, 503 + extraHeight);
        welcomeScreenRaised = true;
    }

    public void launchURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else { //assume Unix or Linux
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else { Runtime.getRuntime().exec(new String[]{browser, url}); }
            }
        } catch (Exception e) {
            pushMessage("Failed to open URL.", 0, "");
        }
    }

    private static class InstanceHolder {
        private static final Client instance = new Client();
    }










































}
