package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.combat.weapons.Weapon;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;
import org.hyperion.rs2.model.container.Equipment;

/**
 * A listener which updates the weapon tab.
 *
 * @author Graham Edgecombe
 */
public class WeaponContainerListener implements ContainerListener {

	/**
	 * The player.
	 */
	private Player player;

	/**
	 * Creates the listener.
	 *
	 * @param player The player.
	 */
	public WeaponContainerListener(Player player) {
		this.player = player;
	}

	@Override
	public void itemChanged(Container container, int slot) {
		if(slot == Equipment.SLOT_WEAPON) {
			if(player.getEquipment().ignoreOnce)
				player.getEquipment().ignoreOnce = false;
			// else
			sendWeapon();
		}
	}

	@Override
	public void itemsChanged(Container container, int[] slots) {
		for(int slot : slots) {
			if(slot == Equipment.SLOT_WEAPON) {
				sendWeapon();
				return;
			}
		}
	}

	@Override
	public void itemsChanged(Container container) {
		sendWeapon();
	}

	/**
	 * Sends weapon information.
	 */
	private void sendWeapon() {

		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		Item shield = player.getEquipment().get(Equipment.SLOT_SHIELD);
		int id = - 1;
		String name = null;
		if(weapon == null) {
			name = "Unarmed";
		} else {
			name = weapon.getDefinition().getName();
			id = weapon.getId();
		}
		String genericName = filterWeaponName(name).trim();
		sendWeapon(id, name, genericName);
		if(! player.ignoreOnLogin) {
			player.cE.setAutoCastId(- 1);
			player.getActionSender().sendClientConfig(108, 0);
		} else
			player.ignoreOnLogin = false;
		player.cE.setAtkEmote(WeaponAnimManager.getAttackAnimation(player, id,
				player.cE.getAtkType()));
		// player.cE.setAtkSpeed(WeaponAnimManager.getSpeed(genericName,id));
		if(id > 0)
			// System.out.println("Sending speed " +
			// ItemDefinition.definitions[id].WeaponSpeed);
			//System.out.println(ItemDefinition.forId(id));
			player.cE.setAtkSpeed(ItemDefinition.forId(id).getWeaponSpeed());
		if(player.debug) {
			player.getActionSender().sendMessage(
					WeaponAnimManager.getSpeed(genericName, id) + "");
		}
		if(shield == null)
			player.cE.setDefEmote(WeaponAnimManager.getDefendAnimation(player, id, - 1));
		else
			player.cE.setDefEmote(WeaponAnimManager.getDefendAnimation(player, id,
					shield.getId()));

		switch(id) {
			case 4151: // whip
			case 15441:
			case 15442:
			case 15443:
			case 15444:
				player.getActionSender().sendFrame171(0, 12323);
				break;
			case 4587: // dscimmy
				player.getActionSender().sendFrame171(0, 7599);
				break;
			case 859: // magic bows
			case 861:
			case 19143:
			case 19146:
			case 19149:
			case 11235:
			case 13883:
			case 13879:
			case 15241:
			case 15701:
			case 15702:
			case 15703:
			case 15704:
			case 15015:
			case 15016:
            		case 14679:
			case 14684:
				player.getActionSender().sendFrame171(0, 7549);
				break;
			case 1377: // d battleaxe
            case 15486:
            case 16153:
            case 16154:
            case 16155:
            case 16156:
				player.getActionSender().sendFrame171(0, 7499);
				break;
			case 4153:// gmaul
			case 17646:
			case 13902:
				player.getActionSender().sendFrame171(0, 7474);
				break;
			case 1249:
				player.getActionSender().sendFrame171(0, 7674);
				break;
			case 14484:
				player.getActionSender().sendFrame171(0, 7800);
				break;

			case 1305:
            case 1434:
            case 11061:
	    case 17640:
			case 10887:
			case 3204:
			case 5698:

			case 1215:
			case 1231:
			case 5680:
			case 1263:
			case 5716:
			case 5730:
			case 7158:
			case 7603:
			case 11730:
			case 13899:
            case 19605:
			case 11694:
			case 19780:
			case 10858:
			case 11696:
			case 11698:
			case 11700:

				// player.getActionSender().sendFrame171(0, 12323);
				player.getActionSender().sendFrame171(0, 7574);
				// player.getActionSender().sendFrame171(0, 7474);
				// player.getActionSender().sendFrame171(0, 7599);
				player.getActionSender().sendFrame171(0, 7549);
				player.getActionSender().sendFrame171(0, 8493);
				// player.getActionSender().sendFrame171(0, 7499);
				player.getActionSender().sendFrame171(0, 4738);
				player.getActionSender().sendFrame171(0, 7499);
				player.getActionSender().sendFrame171(0, 7574);
				player.getActionSender().sendFrame171(0, 7599);
				player.getActionSender().sendFrame171(0, 7624);
				// player.getActionSender().sendFrame171(0, 7674);
				player.getActionSender().sendFrame171(0, 7699);
				player.getActionSender().sendFrame171(0, 7724);
				player.getActionSender().sendFrame171(0, 7800);
				player.getActionSender().sendFrame171(0, 7474);
				player.getActionSender().sendFrame171(0, 7524);
				player.getActionSender().sendFrame171(0, 7549);
				player.getActionSender().sendFrame171(0, 7649);
				player.getActionSender().sendFrame171(0, 6117);
				player.getActionSender().sendFrame171(0, 8493);
				// player.getActionSender().sendFrame171(0, 12323);

				break;
			default:
	        /*
			 * player.getActionSender().sendFrame171(1, 12323);
			 * player.getActionSender().sendFrame171(1, 7574);
			 * player.getActionSender().sendFrame171(1, 7474);
			 * player.getActionSender().sendFrame171(1, 7599);
			 * player.getActionSender().sendFrame171(1, 7549);
			 * player.getActionSender().sendFrame171(1, 8493);
			 * player.getActionSender().sendFrame171(1, 7499);
			 * player.getActionSender().sendFrame171(1, 4738);
			 */
				player.getActionSender().sendFrame171(1, 7499);
				player.getActionSender().sendFrame171(1, 7574);
				player.getActionSender().sendFrame171(1, 7599);
				player.getActionSender().sendFrame171(1, 7624);
				player.getActionSender().sendFrame171(1, 7674);
				player.getActionSender().sendFrame171(1, 7699);
				player.getActionSender().sendFrame171(1, 7724);
				player.getActionSender().sendFrame171(1, 7800);
				player.getActionSender().sendFrame171(1, 7474);
				player.getActionSender().sendFrame171(1, 7524);
				player.getActionSender().sendFrame171(1, 7549);
				player.getActionSender().sendFrame171(1, 7649);
				player.getActionSender().sendFrame171(1, 6117);
				player.getActionSender().sendFrame171(1, 8493);
				player.getActionSender().sendFrame171(1, 12323);
				break;
		}
		Weapon weap = Weapon.forId(id);
		player.getAppearance().setAnimations(weap.getStandAnimation(player), weap.getWalkAnimation(player), weap.getRunAnimation(player));
	}

	/**
	 * Sends weapon information.
	 *
	 * @param id          The id.
	 * @param name        The name.
	 * @param genericName The filtered name.
	 */
	private void sendWeapon(int id, String name, String genericName) {
		if(name.equals("Unarmed")) {
			player.getActionSender().sendSidebarInterface(0, 5855);
			player.getActionSender().sendString(5857, name);
		} else if(name.endsWith("whip")) {
			player.getActionSender().sendSidebarInterface(0, 12290);
			player.getActionSender().sendInterfaceModel(12291, 200, id);
			player.getActionSender().sendString(12293, name);
		} else if(name.endsWith("Scythe")) {
			player.getActionSender().sendSidebarInterface(0, 776);
			player.getActionSender().sendInterfaceModel(777, 200, id);
			player.getActionSender().sendString(779, name);
		} else if((name.endsWith("c'bow") || name.equals("Karils crossbow")) && id != 14684) {
			player.getActionSender().sendSidebarInterface(0, 1749);
			player.getActionSender().sendInterfaceModel(1750, 200, id);
			player.getActionSender().sendString(1752, name);
		} else if(name.endsWith("bow") || name.contains("Morr")
				|| name.contains("cannon") || name.startsWith("Crystal bow")
				|| name.startsWith("Toktz-xil-ul") || name.equals("Seercull")
				|| name.endsWith("bow full") || name.contains("bow (class")) {
			player.getActionSender().sendSidebarInterface(0, 1764);
			player.getActionSender().sendInterfaceModel(1765, 200, id);
			player.getActionSender().sendString(1767, name);
		} else if(name.startsWith("Staff") || name.endsWith("staff")
				|| name.endsWith("Toktz-mej-tal") || name.endsWith("wand")) {
			player.getActionSender().sendSidebarInterface(0, 328);
			player.getActionSender().sendInterfaceModel(329, 200, id);
			player.getActionSender().sendString(331, name);
		} else if(genericName.endsWith("dart")
				|| genericName.endsWith("knife")
				|| genericName.endsWith("thrownaxe")
				|| genericName.equals("Toktz-xil-ul")) {
			player.getActionSender().sendSidebarInterface(0, 4446);
			player.getActionSender().sendInterfaceModel(4447, 200, id);
			player.getActionSender().sendString(4449, name);
		} else if(genericName.startsWith("dagger")) {
			player.getActionSender().sendSidebarInterface(0, 2276);
			player.getActionSender().sendInterfaceModel(2277, 200, id);
			player.getActionSender().sendString(2279, name);
		} else if(genericName.startsWith("pickaxe")) {
			player.getActionSender().sendSidebarInterface(0, 5570);
			player.getActionSender().sendInterfaceModel(5571, 200, id);
			player.getActionSender().sendString(5573, name);
		} else if(genericName.startsWith("axe")
				|| genericName.startsWith("battleaxe")) {
			player.getActionSender().sendSidebarInterface(0, 1698);
			player.getActionSender().sendInterfaceModel(1699, 200, id);
			player.getActionSender().sendString(1701, name);
		} else if(genericName.startsWith("Axe")
				|| genericName.startsWith("Battleaxe")) {
			player.getActionSender().sendSidebarInterface(0, 1698);
			player.getActionSender().sendInterfaceModel(1699, 200, id);
			player.getActionSender().sendString(1701, name);
		} else if(genericName.startsWith("halberd")) {
			player.getActionSender().sendSidebarInterface(0, 8460);
			player.getActionSender().sendInterfaceModel(8461, 200, id);
			player.getActionSender().sendString(8463, name);
		} else if(name.startsWith("Veracs flail")) {
			player.getActionSender().sendSidebarInterface(0, 3796);
			player.getActionSender().sendInterfaceModel(3797, 200, id);
			player.getActionSender().sendString(3799, name);
		} else if(genericName.endsWith("mace")
				|| genericName.endsWith("anchor")) {
			player.getActionSender().sendSidebarInterface(0, 3796);
			player.getActionSender().sendInterfaceModel(3797, 200, id);
			player.getActionSender().sendString(3799, name);
		} else if(genericName.contains("maul")
				|| genericName.equals("Tzhaar-ket-om")
				|| genericName.equals("Torags hammers")
				|| genericName.equals("Barrelchest anchor")
				|| genericName.contains("tatius")) {
			player.getActionSender().sendSidebarInterface(0, 425);
			player.getActionSender().sendInterfaceModel(426, 200, id);
			player.getActionSender().sendString(428, name);

		} else if(genericName.endsWith("spear")
				|| genericName.endsWith("Guthans warspear")) {
			player.getActionSender().sendSidebarInterface(0, 4679);
			player.getActionSender().sendInterfaceModel(4680, 200, id);
			player.getActionSender().sendString(4682, name);
		} else if(genericName.endsWith("claws")) {// 14484
			player.getActionSender().sendSidebarInterface(0, 7762);
			player.getActionSender().sendInterfaceModel(7763, 200, id);
			player.getActionSender().sendString(7765, name);

			/*
			 * } else if(genericName.endsWith("godsword") ||
			 * genericName.contains("2h") ||
			 * genericName.equals("Saradomin sword")) {
			 * player.getActionSender().sendSidebarInterface(0, 4705);
			 * player.getActionSender().sendInterfaceModel(4706, 200, id);
			 * player.getActionSender().sendString(4708, name);
			 * /*player.getActionSender().sendSidebarInterface(0, 2276);
			 * player.getActionSender().sendInterfaceModel(2277, 200, id);
			 * player.getActionSender().sendString(2279, name);
			 */
		} else {
			// player.getActionSender().sendMessage("LOLBOAT");
			player.getActionSender().sendSidebarInterface(0, 2423);
			player.getActionSender().sendInterfaceModel(2424, 200, id);
			player.getActionSender().sendString(2426, name);
		}
		player.getSpecBar().sendSpecBar();
		// player.getActionSender().sendString(19999,
		// "Combat Level: "+player.getSkills().getCombatLevel());
	}

	/**
	 * Filters a weapon name.
	 *
	 * @param name The original name.
	 * @return The filtered name.
	 */
	private String filterWeaponName(String name) {
		final String[] filtered = new String[]{"Iron", "Steel", "Scythe",
				"Black", "Mithril", "Adamant", "Rune", "Granite", "Dragon",
				"Drag", "Crystal", "Bronze"};
		for(String filter : filtered) {
			name = name.replaceAll(filter, "");
		}
		return name;
	}

}
