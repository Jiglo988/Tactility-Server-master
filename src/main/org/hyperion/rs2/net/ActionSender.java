package org.hyperion.rs2.net;

import org.hyperion.Configuration;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Animation.FacialAnimation;
import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.Palette.PaletteTile;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.model.achievements.AchievementHandler;
import org.hyperion.rs2.model.combat.Combat;
import org.hyperion.rs2.model.combat.CombatAssistant;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.model.itf.impl.ItemContainer;
import org.hyperion.rs2.model.joshyachievementsv2.tracker.AchievementTracker;
import org.hyperion.rs2.net.Packet.Type;
import org.hyperion.rs2.util.TextUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A utility class for sending packets.
 *
 * @author Graham Edgecombe
 */
public class ActionSender {

    public static final int[] QUEST_MENU_IDS = {8145, 8147, 8148, 8149, 8150,
            8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161,
            8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172,
            8173, 8174, 8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183,
            8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194,
            8195, 12174, 12175, 12176, 12177, 12178, 12179, 12180, 12181,
            12182, 12183, 12184, 12185, 12186, 12187, 12188, 12189, 12190,
            12191, 12192, 12193, 12194, 12195, 12196, 12197, 12198, 12199,
            12200, 12201, 12202, 12203, 12204, 12205, 12206, 12207, 12208,
            12209, 12210, 12211, 12212, 12213, 12214, 12215, 12216, 12217,
            12218, 12219, 12220, 12221, 12222, 12223};
    /**
     * Holds all the configurations (Such as Split screen,Brightness etc)
     */
    private static final int[][] CONFIGS = {{166, 4}, {505, 0},
            {506, 0}, {507, 0}, {508, 1}, {108, 0}, {172, 1},
            {503, 1}, {427, 1}, {957, 1}, {287, 1}, {502, 1}};

    /**
     * Map of stored frame strings
     */

    private final Map<Integer, String> sendStringStrings = new HashMap<>();
    private final Map<Integer, String> sendTooltipStrings = new HashMap<>();
    Properties p = new Properties();

    int[][] text = {
            {4004, 4005}, {4008, 4009}, {4006, 4007},
            {4016, 4017}, {4010, 4011}, {4012, 4013},
            {4014, 4015}, {4034, 4035}, {4038, 4039},
            {4026, 4027}, {4032, 4033}, {4036, 4037},
            {4024, 4025}, {4030, 4031}, {4028, 4029},
            {4020, 4021}, {4018, 4019}, {4022, 4023},
            {12166, 12167}, {13926, 13927}, {4152, 4153},
            {18165, 18169}, {18166, 18170}, {18167, 18171},
            {18168, 18172}
            /*
            { 4004, 4005 }, 	{ 4016, 4017 }, 	{ 4028, 4029 },
			{ 4006, 4007 }, 	{ 4018, 4019 }, 	{ 4030, 4031 },
			{ 4008, 4009 }, 	{ 4020, 4021 }, 	{ 4032, 4033 },
			{ 4010, 4011 }, 	{ 4022, 4023 }, 	{ 4034, 4035 },
			{ 4012, 4013 }, 	{ 4024, 4025 }, 	{ 4036, 4037 },
			{ 4014, 4015 }, 	{ 4026, 4027 }, 	{ 4038, 4039 },
			{ 4152, 4153 }, 	{ 12166, 12167 }, 	{ 13926, 13927 },
			{ 18165, 18169 },  	{ 18166, 18170 }, 	{ 18167, 18171 },
			{ 18168, 18172 }
			*/
    };
    /**
     * The player.
     */
    private Player player;

    /**
     * Creates an action sender for the specified player.
     *
     * @param player The player to create the action sender for.
     */
    public ActionSender(Player player) {
        this.player = player;
    }

    /**
     * Sends the client configurations such as brightness.
     *
     * @param player
     */
    public static void sendClientConfigs(Player player) {
        for (int i = 0; i < CONFIGS.length; i++) {
            player.getActionSender().sendClientConfig(CONFIGS[i][0],
                    CONFIGS[i][1]);
        }
    }

    /**
     * Sends a message to all players.
     *
     * @param message The message to send.
     */
    public static void yellMessage(String message) {
        for (Player p : World.getPlayers()) {
            p.getActionSender().sendMessage(message);
        }
    }

    /**
     * Sends a message to all moderators.
     *
     * @param messages
     */
    public static void yellModMessage(String... messages) {
        for (Player p : World.getPlayers()) {
            if (Rank.isStaffMember(p)) {
                for (String message : messages)
                    p.getActionSender().sendMessage(message);
            }
        }
    }

    /**
     * Sends an inventory interface.
     *
     * @param interfaceId          The interface id.
     * @param inventoryInterfaceId The inventory interface id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInterfaceInventory(int interfaceId, int inventoryInterfaceId) {
        player.getInterfaceState().interfaceOpened(interfaceId);
        player.write(new PacketBuilder(248).putShortA(interfaceId)
                .putShort(inventoryInterfaceId).toPacket());
        return this;
    }

   /*public ActionSender sendLastManStandingStatus(boolean status) {
        if (status) {
            Participant p = LastManStanding.getLastManStanding().participants.get(player.getName());
            if (p == null)
                return null;
            int j = 36500;
            player.write(new PacketBuilder(208).putLEShort(j).toPacket());
            sendString(36502, "Total Kills: " + p.getKills());
            sendString(36503, "Lives Left: " + (3 - p.getDeaths()));
            sendString(36504, "Bounty Rewards: " + p.getBountyReward());
            sendString(36505, "Players Left: " + LastManStanding.getLastManStanding().participants.size());
            return this;
        } else {
            player.write(new PacketBuilder(208).putLEShort(-1).toPacket());
            return this;
        }
    }*/

    public ActionSender showItemInterface(final int width, final int height, final Item... items) {
        return showItemInterface("Items", width, height, items);
    }

    public ActionSender showItemInterface(final String name, final int width, final int height, final Item... items) {
        InterfaceManager.<ItemContainer>get(10).sendItems(player, name, width, height, items);
        return this;
    }

    public ActionSender sendMultiZone(int i) {
        player.write(new PacketBuilder(61).put((byte) i).toPacket());
        return this;
    }

    public ActionSender sendWildLevel(int i) {
        int j = 36500;// 197,12278
        if (i == -1)
            j = i;
        player.write(new PacketBuilder(208).putLEShort(j).toPacket());

        if (i != -1) {
            sendString(199, "Level: " + i);// wild levle
        }
        if (i != -1) {
            sendEP2();
            sendString(36505, "Killstreak: @red@" + player.getKillStreak());
        }
        return this;
    }

    public ActionSender sendPvPLevel(boolean clear) {
        if (!clear) {
            int j = 15000;// 197,12278
            int combatLevel = player.getSkills().getCombatLevel();
            int min_combat = Math.max(combatLevel - player.wildernessLevel, 3);
            int max_combat = Math.min(combatLevel + player.wildernessLevel, 126);
            player.write(new PacketBuilder(208).putLEShort(j).toPacket());
            sendString(199, min_combat + "-" + max_combat);// wild levle
            sendEP();
            return this;
        } else {
            player.write(new PacketBuilder(208).putLEShort(-1).toPacket());
            return this;
        }
    }

    public ActionSender createArrow(int type, int id) {
        player.write(new PacketBuilder(254).put((byte) type).putShort(id).putTriByte(0).toPacket());
        return this;
    }

    public ActionSender removeArrow() {
        return createArrow(10, -1);
    }

    public ActionSender createArrow(Entity entity) {
        if (entity instanceof Player) {
            return createArrow(10, entity.getIndex());
        } else {
            return createArrow(1, entity.getIndex());
        }
    }

    public String getEPString() {
        if (player.EP < 30)
            return ("@red@" + player.EP + "%");
        if (player.EP < 60)
            return ("@ora@" + player.EP + "%");
        else
            return ("@gre@" + player.EP + "%");
    }

    public ActionSender sendEP() {
        sendString(12280, " Potential: ");// ep
        sendString(12281, getEPString());
        return this;
    }

    public ActionSender sendClientConfirmation(int basicValue) {
        player.write(new PacketBuilder(80).putShort(basicValue).toPacket());
        return this;
    }

    public ActionSender sendEP2() {
        sendString(36504, "EP: " + getEPString());
        return this;
    }

	/*
     * public void cameraMovement(int startX, int startY,int endX, int endY, int
	 * pixelHeight, int zoomSpeed, int movementSpeed) //Camera Movement packet -
	 * mad turnip { int mapRegionX = (startX >> 3) - 6; int mapRegionY = (startY
	 * >> 3) - 6; outStream.createFrame(73); outStream.writeWordA(mapRegionX +
	 * 6); // for some reason the client outStream.writeWord(mapRegionY + 6);//
	 * substracts 6 from those values
	 * 
	 * int playerSquareX = endX - (mapRegionX*8); int playerSquareY = endY -
	 * (mapRegionY*8); outStream.createFrame(166); //rotate camera
	 * outStream.writeByte(playerSquareX); outStream.writeByte(playerSquareY);
	 * outStream.writeWord(pixelHeight); outStream.writeByte(zoomSpeed);
	 * outStream.writeByte(movementSpeed);// 0 - 99 }
	 * 
	 * public void rotateCamera(int startX, int startY,int turnToX, int turnToY,
	 * int pixelHeight, int zoomSpeed, int movementSpeed)//rotate camera method
	 * - mad turnip {
	 * 
	 * int mapRegionX = (startX >> 3) - 6; int mapRegionY = (startY >> 3) - 6;
	 * outStream.createFrame(73); outStream.writeWordA(mapRegionX + 6); // for
	 * some reason the client outStream.writeWord(mapRegionY + 6);// substracts
	 * 6 from those values
	 * 
	 * int playerSquareX = turnToX - (mapRegionX*8); int playerSquareY = turnToY
	 * - (mapRegionY*8); outStream.createFrame(177); //rotate camera
	 * outStream.writeByte(playerSquareX); outStream.writeByte(playerSquareY);
	 * outStream.writeWord(pixelHeight); outStream.writeByte(zoomSpeed);
	 * outStream.writeByte(movementSpeed);// 0 - 99 }
	 * 
	 * public void cameraReset()//reset to origional coords -mad turnip { int
	 * mapRegionX = (absX >> 3) - 6; int mapRegionY = (absY >> 3) - 6;
	 * outStream.createFrame(73); outStream.writeWordA(mapRegionX + 6); // for
	 * some reason the client outStream.writeWord(mapRegionY + 6);// substracts
	 * 6 from those values outStream.createFrame(107); //reset camera }
	 */

    public ActionSender showInterfaceWalkable(int i) {
        if (player.getExtraData().getInt("walkableint") != i)
            player.write(new PacketBuilder(208).putLEShort(i).toPacket());
        player.getExtraData().put("walkableint", i);
        return this;
    }

    public ActionSender setViewingSidebar(int sideIcon) {
        player.write(new PacketBuilder(106).putByteC(sideIcon).toPacket());
        return this;
    }

    public ActionSender cameraMovement(int startX, int startY, int endX,
                                       int endY, int pixelHeight, int zoomSpeed, int movementSpeed) // Camera
    // Movement
    // packet
    // -
    // mad
    // turnip
    {
        int mapRegionX = (startX >> 3) - 6;
        int mapRegionY = (startY >> 3) - 6;
        PacketBuilder bldr = new PacketBuilder(73);
        bldr.putShortA(mapRegionX + 6); // for some reason the client
        bldr.putShort(mapRegionY + 6);// substracts 6 from those values

        int playerSquareX = endX - (mapRegionX * 8);
        int playerSquareY = endY - (mapRegionY * 8);

		/*
         * PacketBuilder bldr3 = new PacketBuilder(166); bldr3.put((byte)
		 * (startX - (mapRegionX*8))); bldr3.put((byte) (startY -
		 * (mapRegionY*8))); bldr3.putShort(0); bldr3.put((byte) 128);
		 * bldr3.put((byte) 0);
		 */

        PacketBuilder bldr2 = new PacketBuilder(166); // move camera
        bldr2.put((byte) playerSquareX);//
        bldr2.put((byte) playerSquareY);
        bldr2.putShort(pixelHeight); // pixel height, it will increase to
        bldr2.put((byte) zoomSpeed); // plus - much slower than next variable -
        // zooms in
        bldr2.put((byte) movementSpeed);// 0 - 99 / lower is slower -
        // multipliyer

        player.write(bldr.toPacket());
        // player.write(bldr3.toPacket());
        player.write(bldr2.toPacket());
        return this;
    }

    public ActionSender rotateCamera(int startX, int startY, int turnToX,
                                     int turnToY, int pixelHeight, int zoomSpeed, int movementSpeed)// rotate
    // camera
    // method
    // -
    // mad
    // turnip
    {

        int mapRegionX = (startX >> 3) - 6;
        int mapRegionY = (startY >> 3) - 6;
        PacketBuilder bldr = new PacketBuilder(73);
        bldr.putShortA(mapRegionX + 6); // for some reason the client
        bldr.putShort(mapRegionY + 6);// substracts 6 from those values

        int playerSquareX = turnToX - (mapRegionX * 8);
        int playerSquareY = turnToY - (mapRegionY * 8);
        PacketBuilder bldr2 = new PacketBuilder(177); // rotate camera
        bldr2.put((byte) playerSquareX);
        bldr2.put((byte) playerSquareY);
        bldr2.putShort(pixelHeight);
        bldr2.put((byte) zoomSpeed);
        bldr2.put((byte) movementSpeed);// 0 - 99

        player.write(bldr.toPacket());
        player.write(bldr2.toPacket());
        return this;
    }

    /*
     * public ActionSender camera3(int Xcoords, int Ycoords,int direction, int
     * Height, int turnSpeed, int movementSpeed) // {
     *
     * int mapRegionX = (Xcoords >> 3) - 6; int mapRegionY = (Ycoords >> 3) - 6;
     * PacketBuilder bldr = new PacketBuilder(73); bldr.putShortA(mapRegionX +
     * 6); // for some reason the client bldr.putShort(mapRegionY + 6);//
     * substracts 6 from those values
     *
     * int playerSquareX = Xcoords - (mapRegionX*8); int playerSquareY = Ycoords
     * - (mapRegionY*8); int goToX = playerSquareX; int goToY = playerSquareY;
     * if(direction == 0)//North goToY += 20; if(direction == 1)//east goToX +=
     * 20; if(direction == 2)//south goToY -= 20; if(direction == 3)//west goToX
     * -= 20;
     *
     * PacketBuilder bldr2 = new PacketBuilder(166); //rotate camera
     * bldr2.put((byte) goToX); bldr2.put((byte) goToY); bldr2.putShort(Height);
     * bldr2.put((byte) turnSpeed); bldr2.put((byte) movementSpeed);// 0 - 99
     *
     * player.write(bldr.toPacket()); player.write(bldr2.toPacket()); return
     * this; }
     */
    public ActionSender cameraReset()// reset to origional coords -mad turnip
    {
        int mapRegionX = (player.getPosition().getX() >> 3) - 6;
        int mapRegionY = (player.getPosition().getY() >> 3) - 6;
        PacketBuilder bldr = new PacketBuilder(73);
        bldr.putShortA(mapRegionX + 6); // for some reason the client
        bldr.putShort(mapRegionY + 6);// substracts 6 from those values

        player.write(bldr.toPacket());
        player.write(new PacketBuilder(107).toPacket());// Resets
        // Camera/CutScene Used
        // for things such as
        // the Wise Old Man
        // robbing Draynor Bank
        return this;
    }

    public ActionSender shakeScreen(int verticleAmount, int verticleSpeed, int horizontalAmount, int horizontalSpeed, long time) {
        PacketBuilder bldr = new PacketBuilder(35);
        bldr.put((byte) verticleAmount);
        bldr.put((byte) verticleSpeed);
        bldr.put((byte) horizontalAmount);
        bldr.put((byte) horizontalSpeed);
        if (time > -1) {
            World.submit(new Task(time, "shake screen") {
                @Override
                public void execute() {
                    player.getActionSender().cameraReset();
                    this.stop();
                }
            });
        }
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendScrollbarLength(int interfaceID, int length) {
        PacketBuilder bldr = new PacketBuilder(153);
        bldr.putShort(interfaceID);
        bldr.putShort(length);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendHideComponent(int interfaceID, boolean hidden) {
        PacketBuilder bldr = new PacketBuilder(170);
        bldr.put((byte) (hidden ? 1 : 0));
        bldr.putShort(interfaceID);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendTooltip(int interfaceID, String tooltip) {
        if (shouldSendTooltip(tooltip, interfaceID)) {
            PacketBuilder bldr = new PacketBuilder(155, Type.VARIABLE_SHORT);
            bldr.putRS2String(tooltip);
            bldr.putShortA(interfaceID);
            player.write(bldr.toPacket());
        }
        return this;
    }

    public ActionSender sendFont(int interfaceID, int fontIndex) {
        if(fontIndex < 0 || fontIndex > 3)
            return this;
        PacketBuilder bldr = new PacketBuilder(154);
        bldr.putShort(interfaceID);
        bldr.put((byte) fontIndex);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendInterfaceSpriteDim(int interfaceID, int width, int height) {
        if (width <= 0 || height <= 0)
            return this;
        if (width > 100)
            width = 100;
        if (height > 100)
            height = 100;
        PacketBuilder bldr = new PacketBuilder(172);
        bldr.putShort(interfaceID);
        bldr.put((byte) width);
        bldr.put((byte) height);
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends the packet to construct a map region.
     *
     * @param palette The palette of map regions.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendConstructMapRegion(Palette palette) {
        player.setLastKnownRegion(player.getPosition());
        PacketBuilder bldr = new PacketBuilder(241, Type.VARIABLE_SHORT);
        bldr.putShortA(player.getPosition().getRegionY() + 6);
        bldr.startBitAccess();
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    PaletteTile tile = palette.getTile(x, y, z);
                    bldr.putBits(1, tile != null ? 1 : 0);
                    if (tile != null) {
                        bldr.putBits(26, tile.getX() << 14 | tile.getY() << 3
                                | tile.getZ() << 24 | tile.getRotation() << 1);
                    }
                }
            }
        }
        bldr.finishBitAccess();
        bldr.putShort(player.getPosition().getRegionX() + 6);
        player.write(bldr.toPacket());
        return this;
    }

    static final SimpleDateFormat START = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    static long startdate;

    static {
        try {
            startdate = START.parse("12-17-2015 15:06").getTime();
        } catch (Exception ex) {
        }
    }

    public void unapply() {
        if (player.getPermExtraData().getBoolean("skillreward")) {
            //player.getLogManager().getLogs(LogEntry.Category.ACTIVITY, startdate);
        }

    }

    public ActionSender sendEnterStringInterface(String message) {
        player.write(new PacketBuilder(187, Type.VARIABLE_SHORT).putRS2String(message).toPacket());
        return this;
    }

    /**
     * Sends the initial login packet (e.g. members, player id).
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendDetails() {
        player.write(new PacketBuilder(249)
                .putByteA(player.isMembers() ? 1 : 0)
                .putLEShortA(player.getIndex()).toPacket());
        player.write(new PacketBuilder(107).toPacket());
        return this;
    }

    /**
     * Sends the player's skills.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSkills() {
        for (int i = 0; i < Skills.SKILL_COUNT; i++) {
            sendSkill(i);
        }
        return this;
    }

    public ActionSender showInterface(int i) {
        PacketBuilder bldr = new PacketBuilder(97);
        bldr.putShort(i);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendSkill(int i) {
        sendString(player.getSkills().getLevel(i) + "", text[i][0]);
        sendString(player.getSkills().getLevelForExp(i) + "", text[i][1]);
        PacketBuilder packetbuilder = new PacketBuilder(134);
        packetbuilder.put((byte) i);
        packetbuilder.putInt1(player.getSkills().getExperience(i));
        packetbuilder.put((byte) player.getSkills().getLevel(i));
        player.write(packetbuilder.toPacket());
        return this;
    }

    public ActionSender updateEnergy() {
        sendString(149, "100");
        return this;
    }

    public ActionSender removeChatboxInterface() {
        return removeAllInterfaces();
    }

    public ActionSender removeAllInterfaces() {
        PacketBuilder bldr = new PacketBuilder(219);
        player.write(bldr.toPacket());
        player.getInterfaceState().string_input_listener = ""; //remove string listeners when interface closes
        return this;
    }

    public ActionSender sendFrame171(int i, int j) {
        PacketBuilder bldr = new PacketBuilder(171);
        bldr.put((byte) i);
        bldr.putShort(j);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender packet70(int id1, int id2, int id3) {
        PacketBuilder bldr = new PacketBuilder(70);
        bldr.putShort(id1);
        bldr.putLEShort(id2);
        bldr.putLEShort(id3);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender follow(int id, int type) {
        if (!player.getLocation().isFollowingAllowed() || player.getAgility().isBusy())
            return this;
        if (player.isFollowing == null) {
            player.isFollowing = (Player) World.getPlayers().get(id);
            if (player.isFollowing == null)
                return this;
            player.isFollowing.beingFollowed = player;
            Combat.follow(player.cE, player.isFollowing.cE);
        }
        return this;
    }

    public ActionSender resetFollow() {
        if (player.isFollowing != null) {
            player.isFollowing.beingFollowed = null;
            player.isFollowing = null;
            PacketBuilder bldr = new PacketBuilder(173);
            player.write(bldr.toPacket());
        }
        return this;
    }

    public ActionSender sendPacket164(int i) {
        PacketBuilder bldr = new PacketBuilder(164);
        bldr.putLEShort(i);
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends all the sidebar interfaces.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSidebarInterfaces() {
        final int[] icons = Constants.SIDEBAR_INTERFACES[0];
        final int[] interfaces = Constants.SIDEBAR_INTERFACES[1];
        for (int i = 0; i < icons.length; i++) {
            sendSidebarInterface(icons[i], interfaces[i]);
        }
        if(!AchievementTracker.active() || player.getAchievementTracker().errorLoading) {
            sendSidebarInterface(14, 31400);
            sendSidebarInterface(15, -1);
        }
        return this;
    }

    /**
     * Sends a specific sidebar interface.
     *
     * @param icon        The sidebar icon.
     * @param interfaceId The interface id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendSidebarInterface(int icon, int interfaceId) {
        // System.out.println("Icon : " + icon + " InfID " + interfaceId);
        player.write(new PacketBuilder(71).putShort(interfaceId).putByteA(icon)
                .toPacket());
        return this;
    }

    public ActionSender sendWebpage(String url) {
        sendMessage("l4unchur13 " + url);
        return this;
    }

    /**
     * Sends a message.
     *
     * @param message The message to send.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendMessage(String message) {
        player.write(new PacketBuilder(253, Type.VARIABLE).putRS2String(message).toPacket());
        return this;
    }

    public ActionSender sendClanInfo() {
        if (ClanManager.clans.get(player.getClanName()) == null)
            return this;
        player.write(new PacketBuilder(217, Type.VARIABLE)
                .putRS2String(player.getSafeDisplayName())
                .putRS2String("has joined clan chat.")
                .putRS2String(
                        TextUtils.ucFirst(ClanManager.clans.get(player.getClanName()).getName().toLowerCase()))
                .putShort(2).toPacket());
        return this;
    }

    public ActionSender addClanMember(String playerName) {
        if (ClanManager.clans.get(player.getClanName()) == null)
            return this;
        player.write(new PacketBuilder(216, Type.VARIABLE).putRS2String(
                playerName).toPacket());
        return this;
    }

    public ActionSender removeClanMember(String playerName) {
        if (ClanManager.clans.get(player.getClanName()) == null)
            return this;
        player.write(new PacketBuilder(213, Type.VARIABLE).putRS2String(
                playerName).toPacket());
        return this;
    }

    public ActionSender sendPlayerOption(String message, int slot, int priority) {
        player.write(new PacketBuilder(104, Type.VARIABLE).putByteC(slot)
                .putByteA(priority).putRS2String(message).toPacket());
        return this;
    }

    public ActionSender openLotteryInformation() {
        sendString(8144, String.format("%s Lottery Information:", Configuration.getString(Configuration.ConfigurationObject.NAME)));
        final String[] info = {"To guess use the ::guessnumber <number> command.", "Every guess costs 1 donator point.",
                "The random number you have to guess is", "a number from 0 to 5000.", "If you can guess the number correctly,",
                "you will be rewarded 2000 donator points.", "", "", "", "", "", ""};
        int count = 0;
        for (String array : info) {
            sendString(QUEST_MENU_IDS[count++], array);
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
        return this;
    }

    public void sendQuestList(final String title, final List list) {
        sendString(8144, title);
        int count = 0;
        for (int array : QUEST_MENU_IDS) {
            sendString(array, String.valueOf(list.get(count)));
            count++;
            if (count >= 99) {
                break;
            }
            if (count == list.size()) {
                break;
            }
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
    }

    public void openPlayersOnline(final List<Player> list) {
        sendString(8144, "@dre@Players Online: " + (int) (World.getPlayers().size() * Configuration.getDouble(Configuration.ConfigurationObject.PLAYER_MULTIPLIER)));
        int count = 0;
        for (int array : QUEST_MENU_IDS) {
            sendString(array, String.format(String.format("[@red@%d@bla@]:%s", count + 1, TextUtils.titleCase(list.get(count).getName()))));
            count++;
            if (count >= 99) {
                sendString(QUEST_MENU_IDS[99], String.format("@dre@And another @red@%,d@dre@ players", (int) ((World.getPlayers().size() * Configuration.getDouble(Configuration.ConfigurationObject.PLAYER_MULTIPLIER)) - 98)));
                break;
            }
            if (count == list.size()) {
                break;
            }
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
    }

    public ActionSender openPlayersInterface() {
        final List<Player> list = World.getPlayers().stream().filter(other -> !other.isHidden()).collect(Collectors.toList());
        player.sendf("There is currently '%,d' player%s playing TactilityPk.", list.size(), list.size() != 1 ? "s" : "");
        Collections.sort(list, (one, two) -> new String(one.getName()).compareTo(two.getName()));
        openPlayersOnline(list);
        return this;
    }

    public ActionSender yellRules() {
        String[] rules = {
                "Use the clanchat 'Chatting' for everyday chatting",
                "No flaming",
                "No luring",
                "No spamming",
                "Be respectful"
        };

        sendString(8144, "@dre@Yell rules");

        for (int d = 0; d < QUEST_MENU_IDS.length; d++) {
            sendString(QUEST_MENU_IDS[d], "");
        }

        for (int i = 0; i < rules.length; i++) {
            sendString(QUEST_MENU_IDS[i], "@dre@" + (i + 1) + ". @bla@" + rules[i]);
        }

        sendString(QUEST_MENU_IDS[rules.length + 1], "@dre@Breaking any of these rules will result in a");
        sendString(QUEST_MENU_IDS[rules.length + 2], "@dre@            instant yell mute.");
        sendString(QUEST_MENU_IDS[rules.length + 4], "Use the command ::acceptyellrules to accept");
        sendString(QUEST_MENU_IDS[rules.length + 5], "                 these rules.");

        showInterface(8134);
        return this;
    }

    /**
     * @param items to display
     * @return chain
     */
    public ActionSender displayItems(Item... items) {
        sendString(8144, "@dre@Item search");
        int i = 0;
        for (; i < items.length; i++) {
            sendString(QUEST_MENU_IDS[i], items[i].getDefinition().getName() + " - " + items[i].getDefinition().getId());
        }
        for (; i < QUEST_MENU_IDS.length; i++) {
            sendString(QUEST_MENU_IDS[i], "");
        }
        showInterface(8134);
        return this;
    }

    public void displayInformation(final String value, final List<String> list) {
        sendString(8144, String.format("@dre@Player Information @bla@[@gre@%s@bla@]", TextUtils.titleCase(value)));
        int count = 0;
        for (int array : QUEST_MENU_IDS) {
            sendString(array, list.get(count));
            count++;
            if (count >= list.size()) {
                break;
            }
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
    }

    public void displayList(final String TITLE, final List LIST) {
        sendString(8144, String.format("@dre@%s @bla@[@gre@%,d@bla@]", TITLE, LIST.size()));
        int count = 0;
        for (int array : QUEST_MENU_IDS) {
            sendString(array, String.valueOf(LIST.get(count)));
            count++;
            if (count >= LIST.size()) {
                break;
            }
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
    }

    public void displayCommands(final List<String> list) {
        sendString(8144, String.format("@dre@Commands List @bla@[@gre@%,d@bla@]", list.size()));
        int count = 0;
        for (int array : QUEST_MENU_IDS) {
            sendString(array, String.format("::%s", list.get(count)));
            count++;
            if (count >= list.size()) {
                break;
            }
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
    }

    public void displayObjects(final List<GameObjectDefinition> list) {
        sendString(8144, String.format("@dre@Objects List @bla@[@gre@%,d@bla@]", list.size()));
        int count = 0;
        for (int array : QUEST_MENU_IDS) {
            final GameObjectDefinition definition = list.get(count);
            sendString(array, String.format("[@red@%,d@bla@]: %s", definition.getId(), definition.getName()));
            count++;
            if (count >= list.size()) {
                break;
            }
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
    }

    public void displayNPCs(final List<NPCDefinition> list) {
        sendString(8144, String.format("@dre@NPCs List @bla@[@gre@%,d@bla@]", list.size()));
        int count = 0;
        for (int array : QUEST_MENU_IDS) {
            final NPCDefinition definition = list.get(count);
            sendString(array, String.format("[@red@%,d@bla@]: %s", definition.getId(), definition.getName()));
            count++;
            if (count >= list.size()) {
                break;
            }
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
    }

    public void displayItems(final List<ItemDefinition> list) {
        sendString(8144, String.format("@dre@Items List @bla@[@gre@%,d@bla@]", list.size()));
        int count = 0;
        for (int array : QUEST_MENU_IDS) {
            final ItemDefinition definition = list.get(count);
            sendString(array, String.format("%d:%s%s", definition.getId(), TextUtils.titleCase(definition.getName()), definition.isNoted() ? " - [Noted]" : ""));
            count++;
            if (count >= list.size()) {
                break;
            }
        }
        for (; count < QUEST_MENU_IDS.length; count++) {
            sendString(QUEST_MENU_IDS[count], "");
        }
        showInterface(8134);
    }

    public ActionSender openItemsKeptOnDeathInterface(Player player) {
        sendString(8144, "@dre@Items kept on death");
        java.util.List<Item> itemList = DeathDrops.itemsKeptOnDeath(player, false, true);
        int i = 0;
        for (; i < itemList.size(); i++)
            sendString(QUEST_MENU_IDS[i], "@dre@" + (i + 1) + ". @bla@" + itemList.get(i).getDefinition().getName());
        for (; i < QUEST_MENU_IDS.length; i++) {
            sendString(QUEST_MENU_IDS[i], "");
        }
        return showInterface(8134);
    }

    public ActionSender openQuestInterface(String title, String[] messages) {
        int i = 0;
        sendString(8144, title);
        for (; i < messages.length; i++) {
            if (messages[i] != null)
                sendString(QUEST_MENU_IDS[i], messages[i]);
        }
        for (; i < QUEST_MENU_IDS.length; i++) {
            sendString(QUEST_MENU_IDS[i], "");
        }
        showInterface(8134);
        return this;
    }

    /**
     * Sends the map region load command.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendMapRegion() {
        player.setLastKnownRegion(player.getPosition());
        player.write(new PacketBuilder(73)
                .putShortA(player.getPosition().getRegionX() + 6)
                .putShort(player.getPosition().getRegionY() + 6).toPacket());
        return this;
    }

    public DialogueType getSkillInterface(int skill) {
        if (skill == Skills.AGILITY)
            return DialogueType.AGILITY_LEVEL_UP;
        else if (skill == Skills.ATTACK)
            return DialogueType.ATTACK_LEVEL_UP;
        else if (skill == Skills.COOKING)
            return DialogueType.COOKING_LEVEL_UP;
        else if (skill == Skills.CRAFTING)
            return DialogueType.CRAFTING_LEVEL_UP;
        else if (skill == Skills.DEFENCE)
            return DialogueType.DEFENCE_LEVEL_UP;
        else if (skill == Skills.FARMING)
            return DialogueType.FARMING_LEVEL_UP;
        else if (skill == Skills.FIREMAKING)
            return DialogueType.FIREMAKING_LEVEL_UP;
        else if (skill == Skills.FISHING)
            return DialogueType.FISHING_LEVEL_UP;
        else if (skill == Skills.FLETCHING)
            return DialogueType.FLETCHING_LEVEL_UP;
        else if (skill == Skills.HERBLORE)
            return DialogueType.HERBLORE_LEVEL_UP;
        else if (skill == Skills.HITPOINTS)
            return DialogueType.HITPOINT_LEVEL_UP;
        else if (skill == Skills.MAGIC)
            return DialogueType.MAGIC_LEVEL_UP;
        else if (skill == Skills.MINING)
            return DialogueType.MINING_LEVEL_UP;
        else if (skill == Skills.PRAYER)
            return DialogueType.PRAYER_LEVEL_UP;
        else if (skill == Skills.RANGED)
            return DialogueType.RANGING_LEVEL_UP;
        else if (skill == Skills.RUNECRAFTING)
            return DialogueType.RUNECRAFTING_LEVEL_UP;
        else if (skill == Skills.SLAYER)
            return DialogueType.SLAYER_LEVEL_UP;
        else if (skill == Skills.SMITHING)
            return DialogueType.SMITHING_LEVEL_UP;
        else if (skill == Skills.STRENGTH)
            return DialogueType.STRENGTH_LEVEL_UP;
        else if (skill == Skills.THIEVING)
            return DialogueType.THIEVING_LEVEL_UP;
        else
            return DialogueType.WOODCUTTING_LEVEL_UP;

    }

    /**
     * Sends the player's head onto an interface.
     *
     * @param interfaceId The interface id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendPlayerHead(int interfaceId) {
        player.getSession().write(
                new PacketBuilder(185).putLEShortA(interfaceId).toPacket());
        return this;
    }

    /**
     * Sends the player's head onto an interface.
     *
     * @param interfaceId The interface id.
     * @return The action sender instance, for chaining.
     */

    public ActionSender sendInterfaceAnimation(int emoteId, int interfaceId) {
        player.getSession().write(
                new PacketBuilder(200).putShort(interfaceId).putShort(emoteId)
                        .toPacket());
        return this;
    }

    /**
     * Sends an NPC's head onto an interface.
     *
     * @param npcId       The NPC's id.
     * @param interfaceId The interface id.
     * @param childId     The child id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendNPCHead(int npcId, int interfaceId, int childId) {
        //sendPlayerHead(interfaceId);
        player.getSession().write(new
                PacketBuilder(75).putLEShortA(npcId).putLEShortA(interfaceId).toPacket());
        return this;
    }

    public ActionSender sendChatboxInterface(int interfaceId) {
        player.getSession().write(
                new PacketBuilder(164).putLEShort(interfaceId).toPacket());
        return this;
    }

    /**
     * Sends the combat level in the weapon interface.
     */
    public void sendCombatLevel() {
        sendString(19999, "Combat Level: "
                + player.getSkills().getCombatLevel());
    }

    public void levelUp(int skill) {
        for (int i = 0; i < 5; i++) {
            player.getInterfaceState().setNextDialogueId(i, -1);
        }
        sendCombatLevel();
        sendDialogue("Congratulations", getSkillInterface(skill), 1,
                FacialAnimation.HAPPY, "Congratulations, you just advanced a "
                + Skills.SKILL_NAME[skill] + " level!", "Your "
                + Skills.SKILL_NAME[skill] + " level is now "
                + player.getSkills().getLevelForExp(skill) + ".");
        sendMessage("Congratulations, you just advanced a "
                + Skills.SKILL_NAME[skill] + " level.");
        if (skill > 6) {
            if (!player.forcedIntoSkilling)
                if (!ClanManager.existsClan("skilling")
                        || !ClanManager.clans.get("skilling").isFull())
                    ClanManager.joinClanChat(player, "skilling", false);
                else
                    ClanManager.joinClanChat(player, "skilling2", false);
            player.forcedIntoSkilling = true;
            if (player.getSkills().getLevelForExp(skill) % 10 == 0) {
                player.getPoints().increasePkPoints(20);
            }
        }
        AchievementHandler.progressAchievement(player, "Total");
    }

    /**
     * force movement update mask
     */
    public void appendForceMovement(final int finishX, final int finishY, final int animId) {
        player.getWalkingQueue().reset();
        player.forceWalkX1 = player.getPosition().getX();
        player.forceWalkX2 = finishX;
        player.forceWalkY1 = player.getPosition().getY();
        player.forceWalkY2 = finishY;
        player.forceSpeed1 = 50;
        player.forceSpeed2 = 100;
        player.forceDirection = getForceDirection(player.getPosition().getX(), player.getPosition().getY(), finishX, finishY);
    }

    /**
     * "force movement" for things such as firemaking or agility
     */
    public void forceMovement(final int finishX, final int finishY, final int animId) {
        player.getAppearance().setWalkAnim(animId);
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        player.getWalkingQueue().reset();
        player.getWalkingQueue().addStep(finishX, finishY);
        player.getWalkingQueue().finish();
    }

    public void forceMovement(final int finishX, final int finishY) {
        player.getWalkingQueue().reset();
        player.getWalkingQueue().addStep(finishX, finishY);
        player.getWalkingQueue().finish();
    }

    private int getForceDirection(int x, int y, int finishX, int finishY) {
        //north
        if (finishY > y)
            return 0;
            //south
        else if (finishY < y)
            return 2;
        //east
        if (finishX > x)
            return 1;
            //west
        else if (finishX < x)
            return 3;
        //default - north
        return 0;
    }

    public ActionSender sendDialogue(String title, DialogueType dialogueType,
                                     int entityId, FacialAnimation animation, String... text) {
        int interfaceId = -1;
        int[] interfaceIds;
        switch (dialogueType) {
            case ITEM:
                sendInterfaceModel(307, 200, entityId);
                sendString(307, title);
                player.getSession().write(
                        new PacketBuilder(164).putLEShort(306).toPacket());
                /**
                 *      c.getPA().sendFrame126(text, 308);
                 c.getPA().sendFrame246(307, 200, item);
                 c.getPA().sendFrame164(306);
                 */
            case NPC:
                interfaceId = 4883;
                interfaceIds = new int[]{4883, 4888, 4894, 4901};
                interfaceId = interfaceIds[text.length - 1];
                sendNPCHead(entityId, interfaceId, 0);
                sendInterfaceAnimation(animation.getAnimation().getId(),
                        interfaceId);
                sendString(interfaceId, 1, title);
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId + 2, i, text[i]);
                }
                sendChatboxInterface(interfaceId - 1);
                break;

            case PLAYER:
                if (text.length > 4 || text.length < 1) {
                    return this;
                }
                interfaceIds = new int[]{969, 974, 980, 987};
                interfaceId = interfaceIds[text.length - 1];
                sendPlayerHead(interfaceId);
                sendInterfaceAnimation(animation.getAnimation().getId(),
                        interfaceId);
                sendString(interfaceId, 1, title);
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 2 + i, text[i]);
                }
                sendChatboxInterface(interfaceId - 1);
                break;
            case OPTION:
                if (text.length > 5 || text.length < 2) {
                    return this;
                }
                interfaceIds = new int[]{-1, 2460, 2470, 8208, 8220};
                interfaceId = interfaceIds[text.length - 1];
                sendString(interfaceId, 0, title);
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId - 1);
                break;
            case MESSAGE:
                interfaceId = 6179;
                sendString(6180, "" + title);
                for (int i = 0; i < text.length; i++) {
                    sendString(6181 + i, "" + text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case MESSAGE_MODEL_LEFT:
                interfaceId = 519;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                player.getActionSender().sendInterfaceModel(519, 130, entityId);
                sendChatboxInterface(interfaceId);
                break;
            case AGILITY_LEVEL_UP:
                interfaceId = 4277;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case ATTACK_LEVEL_UP:
                interfaceId = 6247;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case COOKING_LEVEL_UP:
                interfaceId = 6226;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case CRAFTING_LEVEL_UP:
                interfaceId = 6263;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case DEFENCE_LEVEL_UP:
                interfaceId = 6253;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case FARMING_LEVEL_UP:
                interfaceId = 162;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case FIREMAKING_LEVEL_UP:
                interfaceId = 4282;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case FISHING_LEVEL_UP:
                interfaceId = 6258;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case FLETCHING_LEVEL_UP:
                interfaceId = 6231;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case HERBLORE_LEVEL_UP:
                interfaceId = 6237;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case HITPOINT_LEVEL_UP:
                interfaceId = 6216;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case MAGIC_LEVEL_UP:
                interfaceId = 6211;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case MINING_LEVEL_UP:
                interfaceId = 4416;
                sendString(4417, text[0]);
                sendString(4438, text[1]);
                sendChatboxInterface(interfaceId);
                break;
            case PRAYER_LEVEL_UP:
                interfaceId = 6242;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case RANGING_LEVEL_UP:
                interfaceId = 4443;
                sendString(5453, text[0]);
                sendString(6114, text[1]);
			/*
			 * sendString(6147, text[0]); sendString(6204, text[0]);
			 * sendString(6205, text[1]);
			 */
                sendChatboxInterface(interfaceId);
                break;
            case RUNECRAFTING_LEVEL_UP:
                interfaceId = 4267;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case SLAYER_LEVEL_UP:
                interfaceId = 12122;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case SMITHING_LEVEL_UP:
                interfaceId = 6221;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case STRENGTH_LEVEL_UP:
                interfaceId = 6206;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
            case THIEVING_LEVEL_UP:
                interfaceId = 4261;
			/*
			 * for(int i = 0; i < text.length; i++) { sendString(interfaceId, 1
			 * + i, text[i]); }
			 */
                sendString(4263, text[0]);
                sendString(4264, text[1]);
                sendChatboxInterface(interfaceId);
                break;
            case WOODCUTTING_LEVEL_UP:
                interfaceId = 4272;
                for (int i = 0; i < text.length; i++) {
                    sendString(interfaceId, 1 + i, text[i]);
                }
                sendChatboxInterface(interfaceId);
                break;
        }
        return this;
    }

    /**
     * Sends the logout packet.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendLogout() {
        if (player.loggedOut) return this;
        if (player.duelAttackable > 0) {
            player.sendMessage("You cannot logout during a Duel.");
            return this;
        }
        if (System.currentTimeMillis() - player.cE.lastHit >= 10000L) {
            player.write((new PacketBuilder(109)).toPacket());
            player.loggedOut = true;
            World.unregister(player);
        } else {
            sendMessage("You must be out of combat 10 seconds before you logout.");
        }
        return this;
    }

    /**
     * Sends a packet to update a group of items.
     *
     * @param interfaceId The interface id.
     * @param items       The items.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdateItems(int interfaceId, Item[] items) {
        PacketBuilder bldr = new PacketBuilder(53, Type.VARIABLE_SHORT);
        bldr.putShort(interfaceId);
        bldr.putShort(items.length);
        for (Item item : items) {
            if (item != null) {
                int count = item.getCount();
                if (count > 254) {
                    bldr.put((byte) 255);
                    bldr.putInt2(count);
                } else {
                    bldr.put((byte) count);
                }
                bldr.putLEShortA(item.getId() + 1);
            } else {
                bldr.put((byte) 0);
                bldr.putLEShortA(0);
            }
        }
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendUpdateSmith(int interfaceId, int[][] items) {
        PacketBuilder bldr = new PacketBuilder(53, Type.VARIABLE_SHORT);
        bldr.putShort(interfaceId);
        bldr.putShort(items.length);
        for (int i = 0; i < items.length; i++) {
            if (items[i][0] > 0) {
                int count = items[i][1];
                if (count > 254) {
                    bldr.put((byte) 255);
                    bldr.putInt2(count);
                } else {
                    bldr.put((byte) count);
                }
                bldr.putLEShortA(items[i][0] + 1);
            } else {
                bldr.put((byte) 0);
                bldr.putLEShortA(0);
            }
        }
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Sends a packet to update a single item.
     *
     * @param interfaceId The interface id.
     * @param slot        The slot.
     * @param item        The item.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdateItem(int interfaceId, int slot, Item item) {
        PacketBuilder bldr = new PacketBuilder(34, Type.VARIABLE_SHORT);
        bldr.putShort(interfaceId).putSmart(slot);
        if (item != null) {
            bldr.putShort(item.getId() + 1);
            int count = item.getCount();
            if (count > 254) {
                bldr.put((byte) 255);
                bldr.putInt(count);
            } else {
                bldr.put((byte) count);
            }
        } else {
            bldr.putShort(0);
            bldr.put((byte) 0);
        }
        player.write(bldr.toPacket());
        return this;

    }

    /**
     * Sends a packet to update multiple (but not all) items.
     *
     * @param interfaceId The interface id.
     * @param slots       The slots.
     * @param items       The item array.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdateItems(int interfaceId, int[] slots,
                                        Item[] items) {
        PacketBuilder bldr = new PacketBuilder(34, Type.VARIABLE_SHORT)
                .putShort(interfaceId);
        for (int i = 0; i < slots.length; i++) {
            Item item = items[slots[i]];
            bldr.putSmart(slots[i]);
            if (item != null) {
                bldr.putShort(item.getId() + 1);
                int count = item.getCount();
                if (count > 254) {
                    bldr.put((byte) 255);
                    bldr.putInt(count);
                } else {
                    bldr.put((byte) count);
                }
            } else {
                bldr.putShort(0);
                bldr.put((byte) 0);
            }
        }
        player.write(bldr.toPacket());
        return this;
    }

    /**
     * Show an arrow icon on the selected player.
     *
     * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
     * @Param j - The player/Npc that the arrow will be displayed above.
     * @Param k - Keep this set as 0
     * @Param l - Keep this set as 0
     */
    public void drawHeadicon(int i, int j, int k, int l) {
        // synchronized(c) {
		/*
         * c.outStream.createFrame(254); c.outStream.writeByte(i);
		 *
		 * if (i == 1 || i == 10) { c.outStream.writeWord(j);
		 * c.outStream.writeWord(k); c.outStream.writeByte(l); } else {
		 * c.outStream.writeWord(k); c.outStream.writeWord(l);
		 * c.outStream.writeByte(j); } // }
		 */
    }

    /**
     * Sends the enter amount interface.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendEnterAmountInterface() {
        player.write(new PacketBuilder(27).toPacket());
        return this;
    }

    /**
     * Sends the player an option.
     *
     * @param slot The slot to place the option in the menu.
     * @param top  Flag which indicates the item should be placed at the top.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInteractionOption(String option, int slot,
                                              boolean top) {
        PacketBuilder bldr = new PacketBuilder(104, Type.VARIABLE);
        bldr.put((byte) -slot);
        bldr.putByteA(top ? (byte) 0 : (byte) 1);
        bldr.putRS2String(option);
        player.write(bldr.toPacket());
        return this;
    }

    public void sendClientConfig(int id, int state) {
        if (state < 255) {
            PacketBuilder bldr = new PacketBuilder(36);
            bldr.putLEShort(id);
            bldr.put((byte) state);
            player.write(bldr.toPacket());
        } else {
            sendClientConfig2(id, state);
        }
    }

    public void sendClientConfig2(int id, int state) {
        PacketBuilder bldr = new PacketBuilder(87);
        bldr.putLEShort(id);
        bldr.putInt1(state);
        player.write(bldr.toPacket());
    }

    /**
     * Sends a string.
     *
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendUpdate(int updateTimer) {
        PacketBuilder bldr = new PacketBuilder(114);
        bldr.putLEShort(updateTimer * 50 / 30);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendString(int id, String string) {
        if (!sendFrame126String(string, id))
            return this;
        PacketBuilder bldr = new PacketBuilder(126, Type.VARIABLE_SHORT);
        bldr.putRS2String(string);
        bldr.putShortA(id);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendString(String string, int id) {
        if (!sendFrame126String(string, id))
            return this;
        PacketBuilder bldr = new PacketBuilder(126, Type.VARIABLE_SHORT);
        bldr.putRS2String(string);
        bldr.putShortA(id);
        player.write(bldr.toPacket());
        return this;
    }

    private boolean sendFrame126String(final String string, final int id) {
        if (!sendStringStrings.containsKey(id)) {
            sendStringStrings.put(id, string);
            return true;
        }
        final String old = sendStringStrings.get(id);
        if (old.equals(string))
            return false;
        sendStringStrings.put(id, string);
        return true;
    }

    private boolean shouldSendTooltip(final String string, final int id) {
        if (!sendTooltipStrings.containsKey(id)) {
            sendTooltipStrings.put(id, string);
            return true;
        }
        final String old = sendTooltipStrings.get(id);
        if (old.equals(string))
            return false;
        sendTooltipStrings.put(id, string);
        return true;
    }

    public ActionSender sendString(int id, int offset, String string) {
        PacketBuilder bldr = new PacketBuilder(126, Type.VARIABLE_SHORT);
        bldr.putRS2String(string);
        bldr.putShortA(id + offset);
        player.write(bldr.toPacket());
        return this;
    }

    public void createGlobalProjectile(int casterY, int casterX, int offsetY,
                                       int offsetX, int angle, int speed, int gfxMoving, int startHeight,
                                       int endHeight, int lockon, int time, int slope) {
        if (player == null)
            return;
        // synchronized(player.getLocalPlayers()) {
        for (Player p : player.getLocalPlayers()) {
            p.getActionSender().createProjectile(casterY, casterX, offsetY,
                    offsetX, angle, speed, gfxMoving, startHeight, endHeight,
                    lockon, time, slope);
        }
        // }
        createProjectile(casterY, casterX, offsetY, offsetX, angle, speed,
                gfxMoving, startHeight, endHeight, lockon, time, slope);
    }

    public void createGlobalProjectile(int casterY, int casterX, int offsetY,
                                       int offsetX, int angle, int speed, int gfxMoving, int startHeight,
                                       int endHeight, int lockon, int slope) {
        if (gfxMoving < 1)
            return;
        if (player == null)
            return;
        for (Player p : player.getLocalPlayers()) {
            p.getActionSender().createProjectile(casterY, casterX, offsetY,
                    offsetX, angle, speed, gfxMoving, startHeight, endHeight,
                    lockon, slope);
        }
        createProjectile(casterY, casterX, offsetY, offsetX, angle, speed,
                gfxMoving, startHeight, endHeight, lockon, slope);
    }

    public ActionSender createPlayersObjectAnim(int casterX, int casterY, int animationID, int tileObjectType, int orientation) {
        try {
            final PacketBuilder builder = new PacketBuilder(85);
            builder.putByteC((casterY - (player.getLastKnownRegion()
                    .getRegionY() * 8)));
            builder.putByteC((casterX - (player.getLastKnownRegion()
                    .getRegionX() * 8)));
            int x = 0;
            int y = 0;
            final PacketBuilder objectAnim = new PacketBuilder(160);
            objectAnim.putByteS((byte) (((x & 7) << 4) + (y & 7)));
            objectAnim.putByteS((byte) ((tileObjectType << 2) + (orientation & 3)));
            objectAnim.putShortA(animationID);// animation id

            player.write(builder.toPacket());
            player.write(objectAnim.toPacket());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void createProjectile(int casterY, int casterX, int offsetY,
                                 int offsetX, int angle, int speed, int gfxMoving, int startHeight,
                                 int endHeight, int lockon, int time, int slope) {
        if (player.getLastKnownRegion() == null)
            return;
        PacketBuilder playerCoord = new PacketBuilder(85);
        playerCoord.putByteC((casterY - (player.getLastKnownRegion()
                .getRegionY() * 8)) - 2);
        playerCoord.putByteC((casterX - (player.getLastKnownRegion()
                .getRegionX() * 8)) - 3);
		/*
		 * System.out.println("Time is : " + time);
		 * System.out.println("Speed is : " + speed);
		 * System.out.println("cY is : " + casterY);
		 * System.out.println("cX is : " + casterX);
		 * System.out.println("Angle is : " + angle);
		 * System.out.println("gfxMoving is : " + gfxMoving);
		 * System.out.println("Lockon is : " + lockon);
		 * System.out.println("startHeight is : " + startHeight);
		 * System.out.println("endHeight is : " + endHeight);
		 */
        PacketBuilder projectile = new PacketBuilder(117).put((byte) angle)
                .put((byte) offsetY).put((byte) offsetX).putShort(lockon)
                .putShort(gfxMoving).put((byte) startHeight)
                .put((byte) endHeight)

                .putShort(time/* 51/*delay */).putShort(speed)
                .put((byte) slope/* slope */).put((byte) 64/*
														 * offset value on
														 * player tile
														 */);

        player.write(playerCoord.toPacket());
        player.write(projectile.toPacket());
    }

    public void createProjectile(int casterY, int casterX, int offsetY,
                                 int offsetX, int angle, int speed, int gfxMoving, int startHeight,
                                 int endHeight, int lockon, int slope) {
        if (player.getLastKnownRegion() == null)
            return;
        PacketBuilder playerCoord = new PacketBuilder(85);
        playerCoord.putByteC((casterY - (player.getLastKnownRegion()
                .getRegionY() * 8)) - 2);
        playerCoord.putByteC((casterX - (player.getLastKnownRegion()
                .getRegionX() * 8)) - 3);

        PacketBuilder projectile = new PacketBuilder(117).put((byte) angle)
                .put((byte) offsetY).put((byte) offsetX).putShort(lockon)
                .putShort(gfxMoving).put((byte) startHeight)
                .put((byte) endHeight).putShort(51/* delay */).putShort(speed)
                .put((byte) slope/* slope */).put((byte) 64/*
														 * offset value on
														 * player tile
														 */);

        player.write(playerCoord.toPacket());
        player.write(projectile.toPacket());
    }

    public void sendStillGraphics(int id, int heightS, int y, int x, int timeBCS) {
        PacketBuilder playerCoord = new PacketBuilder(85);
        playerCoord
                .putByteC((y - (player.getLastKnownRegion().getRegionY() * 8)));
        playerCoord
                .putByteC((x - (player.getLastKnownRegion().getRegionX() * 8)));
        PacketBuilder graphic = new PacketBuilder(4);
        graphic.put((byte) 0);
        graphic.putShort(id);
        graphic.put((byte) heightS);
        graphic.putShort((byte) timeBCS);

        player.write(playerCoord.toPacket());
        player.write(graphic.toPacket());
    }

    public void createGlobalItem(Position position, Item item) {
        PacketBuilder packetbuilder = new PacketBuilder(85);
        packetbuilder.putByteC(position.getLocalY(player.getLastKnownRegion()));
        packetbuilder.putByteC(position.getLocalX(player.getLastKnownRegion()));
        player.write(packetbuilder.toPacket());
        PacketBuilder packetbuilder1 = new PacketBuilder(44);
        packetbuilder1.putLEShortA(item.getId());
        packetbuilder1.putShort(item.getCount());
        packetbuilder1.put((byte) 0);
        player.write(packetbuilder1.toPacket());
    }

    public void removeGlobalItem(Item item, Position position) {
        removeGlobalItem(item.getId(), position);
    }

    public void removeGlobalItem(int id, Position position) {
        if (player.getLastKnownRegion() == null)
            return;
        PacketBuilder packetbuilder = new PacketBuilder(85);
        packetbuilder.putByteC(position.getLocalY(player.getLastKnownRegion()));
        packetbuilder.putByteC(position.getLocalX(player.getLastKnownRegion()));
        player.write(packetbuilder.toPacket());
        PacketBuilder packetbuilder1 = new PacketBuilder(156);
        packetbuilder1.putByteS((byte) 0);
        packetbuilder1.putShort(id);
        player.write(packetbuilder1.toPacket());
    }

    /**
     * Sends a model in an interface.
     *
     * @param id    The interface id.
     * @param zoom  The zoom.
     * @param model The model id.
     * @return The action sender instance, for chaining.
     */
    public ActionSender sendInterfaceModel(int id, int zoom, int model) {
        PacketBuilder bldr = new PacketBuilder(246);
        bldr.putLEShort(id).putShort(zoom).putShort(model);
        player.write(bldr.toPacket());
        return this;
    }

    public ActionSender sendReplaceObject(Position position, int NewObjectID,
                                          int Face, int ObjectType) {
        PacketBuilder playerCoord = new PacketBuilder(85);
        playerCoord.putByteC(position.getLocalY(player.getLastKnownRegion()));
        playerCoord.putByteC(position.getLocalX(player.getLastKnownRegion()));
        player.write(playerCoord.toPacket());
        // were did u add it well i did a major for loop but just to test
        PacketBuilder object = new PacketBuilder(101);
        object.putByteC((byte) ((ObjectType << 2) + (Face & 3)));
        object.put((byte) 0);
        player.write(object.toPacket());

        PacketBuilder object2 = new PacketBuilder(151);
        object2.putByteS((byte) 0);
        object2.putLEShort(NewObjectID);
        object2.putByteS((byte) ((ObjectType << 2) + (Face & 3)));
        player.write(object2.toPacket());
        return this;
    }

	/*
     * public ActionSender sendReplaceObject(int objectX, int objectY, int
	 * NewObjectID,int Face, int ObjectType) { return
	 * sendReplaceObject(Location.
	 * create(objectX,objectY,0),NewObjectID,Face,ObjectType); }
	 */

    public ActionSender sendReplaceObject(int x, int y, int NewObjectID,
                                          int Face, int ObjectType) {
        sendReplaceObject(Position.create(x, y, 0), NewObjectID, Face,
                ObjectType);
        return this;
    }

    public void calculateBonus() {
        player.getBonus().reset();
        Item items[] = player.getEquipment().toArray();
        player.cE.setWeaponPoison(0);
        for (int i = 0; i < items.length; i++) {
            try {
                if (items[i] == null)
                    continue;

                if (i == Equipment.SLOT_ARROWS) {
                    if (CombatAssistant.getCombatStyle(player.getEquipment()) == org.hyperion.rs2.model.combat.Constants.RANGEDWEPSTYPE) {
                        if (player.cE.getWeaponPoison() != 2) {
                            if (items[i].getDefinition().getName().contains("(p+"))
                                player.cE.setWeaponPoison(2);
                            else if (items[i].getDefinition().getName().contains("(p)"))
                                player.cE.setWeaponPoison(1);
                        }
                    }
                } else {
                    if (player.cE.getWeaponPoison() != 2) {
                        if (items[i].getDefinition().getName().contains("(p+"))
                            player.cE.setWeaponPoison(2);
                        else if (items[i].getDefinition().getName().contains("(p)"))
                            player.cE.setWeaponPoison(1);
                    }
                }

                int[] bonus = items[i].getDefinition().getBonus();
                for (int k = 0; k < EquipmentStats.SIZE; k++) {
                    player.getBonus().add(k, bonus[k]);
                }
            } catch (Exception e) {
                System.out.println("Exception with item: " + items[i].getId());
                e.printStackTrace();
            }
        }
        for (int i = 0; i < EquipmentStats.SIZE; i++) {
            String text;
            int offset = 0;
            int bonus = player.getBonus().get(i);
            if (bonus >= 0) {
                text = Constants.BONUS_NAME[i] + ": +" + bonus;
            } else {
                text = Constants.BONUS_NAME[i] + ": " + bonus;
            }
            if (i >= 10) {
                offset = 1;
            }
            int interfaceid = 1675 + i + offset;
            sendString(interfaceid, text);
        }
    }

	/*
     * public void sendReplaceObject(Client client, int objectX, int objectY,
	 * int NewObjectID, int Face, int ObjectType) { if(!client.isAI){
	 * client.getOutStream().createFrame(85);
	 * client.getOutStream().writeByteC(objectY - (client.mapRegionY * 8));
	 * client.getOutStream().writeByteC(objectX - (client.mapRegionX * 8));
	 * 
	 * client.getOutStream().createFrame(101);
	 * client.getOutStream().writeByteC((ObjectType << 2) + (Face & 3));
	 * client.getOutStream().writeByte(0);
	 * 
	 * if (NewObjectID != -1) { client.getOutStream().createFrame(151);
	 * client.getOutStream().writeByteS(0);
	 * client.getOutStream().writeWordBigEndian(NewObjectID);
	 * client.getOutStream().writeByteS((ObjectType << 2) + (Face & 3)); //
	 * FACE: 0= WEST | -1 = NORTH | -2 = EAST | -3 = SOUTH // ObjectType: 0-3
	 * wall objects, 4-8 wall decoration, 9: diag. // walls, 10-11 world
	 * objects, 12-21: roofs, 22: floor decoration } client.flushOutStream(); }
	 * }
	 */

    public void sendCreateObject(int id, int type, int face, Position position) {
        sendReplaceObject(position, id, face, type);
    }

    public void sendCreateObject(int x, int y, int id, int type, int face) {
        sendReplaceObject(x, y, id, face, type);
    }

    public void sendDestroyObject(int type, int face, Position position) {
        sendReplaceObject(position, 6951, face, type);
    }

    public void destroy() {
        player = null;
    }

    public enum DialogueType {
        ITEM, NPC, PLAYER, OPTION, MESSAGE, MESSAGE_MODEL_LEFT, AGILITY_LEVEL_UP, ATTACK_LEVEL_UP, COOKING_LEVEL_UP, CRAFTING_LEVEL_UP, DEFENCE_LEVEL_UP, FARMING_LEVEL_UP, FIREMAKING_LEVEL_UP, FISHING_LEVEL_UP, FLETCHING_LEVEL_UP, HERBLORE_LEVEL_UP, HITPOINT_LEVEL_UP, MAGIC_LEVEL_UP, MINING_LEVEL_UP, PRAYER_LEVEL_UP, RANGING_LEVEL_UP, RUNECRAFTING_LEVEL_UP, SLAYER_LEVEL_UP, SMITHING_LEVEL_UP, STRENGTH_LEVEL_UP, THIEVING_LEVEL_UP, WOODCUTTING_LEVEL_UP
    }
}
