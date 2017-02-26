package org.hyperion.rs2.model;

public class SpecialBar {

	public static final int FULL = 100;
	public static final int EMPTY = 0;
	public static final int CYCLE_INCREMENT = FULL / 10;

	private int amount = FULL;

	private Player player;

	public SpecialBar(Player player) {
		this.player = player;
	}

	
	public void normalize() {
		if(amount >= FULL)
			return;

        amount += FULL / 10;


		if(amount > 100)
			amount = 100;
		sendSpecBar();
		sendSpecAmount();
	}
	/**
	 * break bar?
	 */
	public void surpassincrement(int increase) {
		amount += increase;
	}
	public void increment(int increase) {
		amount += increase;
		if(amount > FULL)
			amount = FULL;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void decrease(int decrease) {
		if(amount >= decrease)
			amount -= decrease;
		else
			amount = 0;
	}

	private void sendColouredBar(int barId) {
		int sendAmount = amount / 10;
		for(int i = 10; i > 0; i--) {
			player.getActionSender().packet70((sendAmount >= i ? 500 : 0), 0, (-- barId));
		}
	}

	public void sendSpecAmount() {
		String color = "";
		if(player.specOn)
			color = "@yel@";
		for(int i = 0; i < IDS.length; i++) {
			player.getActionSender().sendString(IDS[i],
					color + "Special Attack (" + amount + "%)");
		}
        player.getActionSender().sendString(26567, (player.specOn ? "1" : "0") + "_" + amount);
	}

	private static int IDS[] = {7812, 12335, 7586, 7611, 7561, 8505,
			7511, 7486, 7711, 7636, 8505, 12335,

	};


	public void sendSpecBar() {
		int weapon = - 1;
		if(player.getEquipment().get(3) != null)
			weapon = player.getEquipment().get(3).getId();
		switch(weapon) {
			case 4151: // whip
			case 15441:
			case 15442:
			case 15443:
			case 15444:
				player.getActionSender().sendFrame171(0, 12323);
				sendColouredBar(12335);
				break;
            case 15486://sol
            case 16153://sol
            case 16154://sol
            case 16155://sol
            case 16156://sol
                player.getActionSender().sendFrame171(0,  7574);
                sendColouredBar(7586);
                break;

			case 859: // magic bows
			case 861:
			case 13883:
			case 13879:
			case 15241:
			case 11235:
			case 15701:
			case 15702:
			case 15703:
			case 15704:
			case 15015:
			case 15016:
			case 14684:
				player.getActionSender().sendFrame171(0, 7549);
				sendColouredBar(7561);
				break;

			case 4587: // dscimmy
			case 11694: // ags
			case 5730:
            case 19605:
			case 11696:
			case 11698:
			case 19780:
			case 10858:
			case 11700:
			case 11730:
			case 13899:
			case 1305: // dragon long
				player.getActionSender().sendFrame171(0, 7599);
				sendColouredBar(7611);
				break;

			case 3204: // d hally
				player.getActionSender().sendFrame171(0, 8493);
				sendColouredBar(8505);
				break;

			case 1377: // d battleaxe
				player.getActionSender().sendFrame171(0, 7499);
				sendColouredBar(7511);
				break;

			case 4153: // gmaul
			case 17646:
			case 13902:
				player.getActionSender().sendFrame171(0, 7474);
				sendColouredBar(7486);
				break;

			case 1249: // dspear
				player.getActionSender().sendFrame171(0, 7674);
				sendColouredBar(7686);
				break;

			case 15027: // dragon claws
			case 14484:
				player.getActionSender().sendFrame171(0, 7800);
				sendColouredBar(7812);
				// specialAmount(player,specAmount, 7812);
				break;
			case 15020: // Statius War
				player.getActionSender().sendFrame171(0, 7474);
				sendColouredBar(7486);
				break;

			case 1215:// dragon dagger
			case 1231:
			case 15006:
			case 5680:
			case 15007:
			case 5698:

			case 15050: // SOL
				player.getActionSender().sendFrame171(0, 7574);
				sendColouredBar(7586);
				break;

            case 1434: // dragon mace
            case 11061: // dragon mace
	    case 17640:
			case 10887:
				player.getActionSender().sendFrame171(0, 7624);
				sendColouredBar(7636);
				break;

			default:
				player.getActionSender().sendFrame171(1, 7624); // mace interface
				player.getActionSender().sendFrame171(1, 7474); // hammer, gmaul
				player.getActionSender().sendFrame171(1, 7499); // axe
				player.getActionSender().sendFrame171(1, 7549); // bow interface
				player.getActionSender().sendFrame171(1, 7574); // sword interface
				player.getActionSender().sendFrame171(1, 7599); // scimmy sword
				// interface, for
				// most swords
				player.getActionSender().sendFrame171(1, 8493);
				player.getActionSender().sendFrame171(1, 12323); // whip interface
				break;
		}
	}


	/*
	public static void specialPowerOld(Player p) {
		for (int i = 7601, i2 = 1; i < 7611; i++, i2++) {
			if ((p.specialPower / 10) >= i2)
				p.getActionSender().packet70(500, 0, i);
			else
				p.getActionSender().packet70(0, 0, i);
		}

		refreshSendQuest(p);
	}
	*/

}
