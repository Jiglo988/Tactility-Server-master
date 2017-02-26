package org.hyperion.rs2.model.content.transport;

import org.hyperion.Configuration;
import org.hyperion.rs2.model.DialogueManager;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.achievements.AchievementHandler;
import org.hyperion.rs2.model.combat.Magic;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

/**
 * @author UKF/Vegas/Arsen/Flux/Jack Daniels       <- Same Person
 */
public class Teleports implements ContentTemplate {


	private final static int[] ACTION_BUTTONS = {
			4171, 50056, 50235, 4140, 117112, 4143, 50245, 117123, 50253, 117123, 50253
			, 117131, 4146, 51005, 117154, 4150,

			177190, 177206, 177209, 177212, 177221, 176177, 178065, 178034, 178050, 178053,
			178056, 178059, 176162, 176168, 176146, 176165, 176171, 176246, 177006, 177009,
			177012, 177015, 177021, 177215,

			13069, 13053, 13045, 13061, 13035, //Interface Ids

			- 20003, - 20203, - 20303, - 19903, - 19703, //Resetting Ids

			- 20018, - 20015, - 20012, - 19934, - 19918, - 19915, - 19912, - 19909, - 20318, - 20312, - 20334, - 20315,
			-20234, -20218, -20215, -20212, -20034, -20009, -20309, -19734, -19718, -19715, -19712, -19709, -20209,

			1164, 1167, 1174, 1170, 1540,

			30064, 30075, 30083, 30106, 30114

	};

	@Override
	public boolean clickObject(Player player, int type, int a, int b, int c,
	                           int d) {
		if(type == 0) {
			player.getActionSender().removeAllInterfaces();
			switch(a) {
				case -20209:
					Magic.teleport(player, 2801, 4723, 0, false);
					break;
				/**
				 * Resetting
				 */
				case - 20003:
				case - 20203:
				case - 20303:
				case - 19903:
				case - 19703:
					resetTeleportInterface(player);
					break;
				/**
				 * Setting Sidebar Part
				 */
				case 13035: //City Interface
				case 1164:
				case 30064:
					player.getActionSender().sendSidebarInterface(6, 45800);
					break;
				case 50235: //Training Interface
				case 4140:
				case 117112:
				case 13045:
				case 1167:
				case 30106:
					player.getActionSender().sendSidebarInterface(6, 45300);
					break;
				case 4143: //Minigame
				case 30075:
				case 50245:
				case 117123:
				case 13053:
				case 1170:
					player.getActionSender().sendSidebarInterface(6, 45200);
					break;
				case 50253: //Boss
				case 117131:
				case 30083:
				case 4146:
				case 13069:
				case 1540:
					player.getActionSender().sendSidebarInterface(6, 45500);
					break;
				case 51005: //Pking
				case 30114:
				case 13061:
				case 117154:
				case 4150:
				case 1174:
					player.getActionSender().sendSidebarInterface(6, 45600);
					break;

				/**
				 * Actual Teleports
				 */
				case - 19734: //Lumbridge
					Magic.teleport(player, 3222, 3218, 0, false);
					break;
				case - 19718: //Varrock
					Magic.teleport(player, 3210, 3424, 0, false);
					break;
				case - 19715: // Edgeville
					Magic.teleport(player, 3084, 3490, 0, false);
					break;
				case - 19712: //Falador
					Magic.teleport(player, 2964, 3372, 0, false);
					break;
				case - 19709: //Camelot
					Magic.teleport(player, 2757, 3478, 0, false);
					break;
				case 177190: //Godwars
					//Magic.teleport(player, 2882, 5310, 2, false);
					break; //5624
				case 177206: //KBD
				case - 20018:
					 boolean acc = player.getExtraData().getBoolean("kbdtele");
					if(!acc) {
						player.sendImportantMessage("This zone is in deep wilderness and leads into multi combat.");
						player.sendImportantMessage("Teleport again if you wish to proceed.");
						player.getExtraData().put("kbdtele", true);
					} else
					    Magic.teleport(player, 3007, 3849, 0, false);
					break;
				case 177209: //Dag Kings
				case - 20015:
                    DialogueManager.openDialogue(player, 1889);
					break;
				case 177212: //Chaos Elemental
				case - 20012:
					acc = player.getExtraData().getBoolean("eletele");
					if(!acc) {
						player.sendImportantMessage("This zone is in deep wilderness and leads into multi combat.");
						player.sendImportantMessage("Teleport again if you wish to proceed.");
						player.getExtraData().put("eletele", true);

					} else
					    Magic.teleport(player, 3295, 3921, 0, false);
					break;
				case 178034: //Mage Bank
				case - 19934:
					Magic.teleport(player, 2539, 4716, 0, false);
					break;
				case 178050: //13 Deep
				case - 19918:
					Magic.goTo13s(player);
					break;
				case 178053: // Mid Wilderness
				case - 19915:
					Magic.teleport(player, 3315, 3665, 0, false);
					break;
				case 178056://Edgeville
				case - 19912:
					Magic.teleport(player, 3086, 3516, 0, false);
					break;
				case 178059: //Fun Pk
				case -19909:
					//DialogueManager.openDialogue(player, 106);
					//Magic.teleport(player, Location.create(2605, 3153, 0), false);
					Magic.teleport(player, Position.create(2594, 3156, 0), false);
                    AchievementHandler.progressAchievement(player, "Teleport to FunPk");
                    break;
				case 176162://Barrows
				case - 20318:
					Magic.teleport(player, 3565, 3314, 0, false);
					break;
				case 176168://Tzhaar
				case - 20312:
					Magic.teleport(player, 2438, 5172, 0, false);
					break;
				case 176146: //Duel
				case - 20334:
					Magic.teleport(player, 3366, 3266, 0, false);
					break;
				case 176165: //Pest Control Later Changed to Ranging Guild
				case - 20315:
					Magic.teleport(player, 2652, 3439, 0, false);
					break;
				case 176171: //Warriors Guild
				case - 20309:
					Magic.teleport(player, 2880, 3545, 0, false);
					break;
				case 176246: //Rock Crabers
				case - 20234:
					Magic.teleport(player, 2709, 3715, 0, false);
					player.getActionSender().sendMessage("@blu@Please note that combat skills can be set by using commands such as ::str 99");
					break;
				case 177006: //Taverly Dung
				case - 20218:
					Magic.teleport(player, 2884, 9798, 0, false);
					break;
				case 177009: //Brimhaven Dung
				case - 20215:
					Magic.teleport(player, 2710, 9466, 0, false);
					break;
				case 177012: //Slayer Tower
				case - 20212:
					Magic.teleport(player, 3428, 3537, 0, false);
					break;
				case 177015: //Godwars
				case - 20034:
					Magic.teleport(player, 2881, 5310, 2, false);
					break;
				case 177215:
				case - 20009://corp
					//player.getActionSender().sendMessage("Under development");
					if(player.getPoints().getPkPoints() < 20) {
						player.getActionSender().sendMessage("To enter his lair you must sacrifice 20 TactilityPK points!");
					} else {
						player.getPoints().setPkPoints((player.getPoints().getPkPoints() - 20));
						player.getActionSender().sendMessage("The mighty beast steals 20 "+ Configuration.getString(Configuration.ConfigurationObject.NAME) +" points from you upon entering!");
						player.sendMessage("@dre@Calm from afar, but if you grab the beast's attention...", "@dre@Then be prepared to die!");
						Magic.teleport(player,2533,4652,0,false);
					}
					
					break;
			}
		}
		return true;
	}

	public static void resetTeleportInterface(Player player) {
		if(player.getSpellBook().isAncient())
			player.getActionSender().sendSidebarInterface(6, 12855);
		else if(player.getSpellBook().isRegular())
			player.getActionSender().sendSidebarInterface(6, 1151);
		else if(player.getSpellBook().isLunars())
			player.getActionSender().sendSidebarInterface(6, 29999);
	}

	@Override
	public void init() throws FileNotFoundException {

	}

	@Override
	public int[] getValues(int type) {
		if(type == 0) {
			return ACTION_BUTTONS;
		}
		return null;
	}

	static {
	}
}
