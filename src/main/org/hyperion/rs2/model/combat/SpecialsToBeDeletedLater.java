package org.hyperion.rs2.model.combat;

public class SpecialsToBeDeletedLater {
 /*
  * paste here
  * 
  * 
  * public void activateSpecial(int weapon, int i){
		if(Server.npcHandler.npcs[i] == null && c.npcIndex > 0) {
			return;
		}
		if(Server.playerHandler.players[i] == null && c.playerIndex > 0) {
			return;
		}
		c.doubleHit = false;
		c.specEffect = 0;
		c.projectileStage = 0;
		c.specMaxHitIncrease = 2;
		if(c.npcIndex > 0) {
			c.oldNpcIndex = i;
		} else if (c.playerIndex > 0){
			c.oldPlayerIndex = i;
			Server.playerHandler.players[i].underAttackBy = c.playerId;
			Server.playerHandler.players[i].logoutDelay = System.currentTimeMillis();
			Server.playerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			Server.playerHandler.players[i].killerId = c.playerId;
		}
		switch(weapon) {
			
			case 1305: // dragon long
			c.gfx100(248);
			c.startAnimation(1058);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.specAccuracy = 1.10;
			c.specDamage = 1.20;
			break;//see this is a default special...
			
			case 1215: // dragon daggers
			case 1231:
			case 5680:
			case 5698:
			c.gfx100(252);
			c.startAnimation(1062);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.specAccuracy = 1.30;
			c.specDamage = 1.05;
			break;//see this is a default special...

                        case 10887:
                        c.gfx100(1027);
                        c.specAccuracy = 5.0;
			c.specDamage = 1.15;
                        c.startAnimation(5870);
                        c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
                        break;//see this is a default special...
			
			case 15027:
			c.gfx0(1950);
			c.startAnimation(10961);
			c.specAccuracy = 9.9;
			c.clawDamage = 0;
			
            if (c.playerIndex > 0) {
            	Client o = (Client) Server.playerHandler.players[c.playerIndex];
            	if (Misc.random(calculateMeleeAttack()) > Misc.random(o.getCombat().calculateMeleeDefence())) {
            		c.clawDamage = Misc.random(calculateMeleeMaxHit() + Misc.random(4));
            	}
            	c.clawIndex = c.playerIndex;
            	c.clawType = 1;
            } else if (c.npcIndex > 0) {
            	NPC n = Server.npcHandler.npcs[c.npcIndex];
            	if (Misc.random(calculateMeleeAttack()) > Misc.random(n.defence)) {
            		c.clawDamage = Misc.random(calculateMeleeMaxHit() + Misc.random(2));
            	}
            	c.clawIndex = c.npcIndex;
            	c.clawType = 2;
            }
			
			c.doubleHit = true;
			c.usingClaws = true;
			c.specEffect = 5;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;
			
			case 4151: // whip
			if(Server.npcHandler.npcs[i] != null) {
				Server.npcHandler.npcs[i].gfx100(341);
			}
			c.specAccuracy = 1.10;
			c.startAnimation(1658);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;//see this is a default special...
			
				case 11694: // ags
				c.startAnimation(7074);
				c.specDamage = 1.30;
				c.specAccuracy = 3.55;
				c.gfx0(1222);
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				break;//see this is a default special...

			case 11730:
			c.gfx100(1224);
			c.startAnimation(7072);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.ssSpec = true;
			c.specAccuracy = 1.30;
			break;//see this is a default special...

				case 15007: // Vesta spear
				c.startAnimation(10499);
				c.gfx0(1835);
                                c.specAccuracy = 1.25;
                                c.specEffect = 6;
                                c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				break;//see how there always the same variables? 
				
				
				ye but ur like splitting up the default specs from non default
				i just think it would be better to make one switch
				that goes through all ids ; simplicity owns sec mabey this will look better to you

				case 15006: // Vesta LongSword
				c.startAnimation(10502);
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()+1);
				c.specDamage = 1.35;
				c.specAccuracy = 2.00;
				break;
				case 15020: // Statius
				c.startAnimation(10505);
				c.gfx0(1840);
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()+1);
				c.specDamage = 1.35;
				c.specAccuracy = 2.25;
				break;
			
			case 11700:
				c.startAnimation(7070);		
				c.gfx0(1221);
				if (c.playerIndex > 0) {
			        } else {
				Server.npcHandler.npcs[c.npcIndex].gfx0(2104);
				}
				c.specAccuracy = 1.35;
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.specEffect = 2;
			break;
			
			case 11696:
				c.startAnimation(7073);
				c.gfx0(1223);
				c.specDamage = 1.30;
				c.specAccuracy = 1.85;
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.specEffect = 3;
			break;
			
			case 11698:
				c.startAnimation(7071);
				c.gfx0(1220);
				c.specAccuracy = 1.25;
				c.specEffect = 4;
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;
			
			case 1249:
				c.startAnimation(405);
				c.gfx100(253);
				if (c.playerIndex > 0) {
					Client o = (Client)Server.playerHandler.players[i];
					o.getPA().getSpeared(c.absX, c.absY);
				}	
			break;
			
			case 3204: // d hally
			c.gfx100(282);
			c.startAnimation(1203);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if(Server.npcHandler.npcs[i] != null && c.npcIndex > 0) {
				if(!c.goodDistance(c.getX(), c.getY(), Server.npcHandler.npcs[i].getX(), Server.npcHandler.npcs[i].getY(), 1)){
					c.doubleHit = true;
				}
			}
			if(Server.playerHandler.players[i] != null && c.playerIndex > 0) {
				if(!c.goodDistance(c.getX(), c.getY(), Server.playerHandler.players[i].getX(),Server.playerHandler.players[i].getY(), 1)){
					c.doubleHit = true;
					c.delayedDamage2 = Misc.random(calculateMeleeMaxHit());
				}
			}
			break;
			
			case 4153: // maul
			case 17646:
			c.startAnimation(1667);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
	
			c.gfx100(337);
			break;
			
			case 4587: // dscimmy
			c.gfx100(347);
			c.specEffect = 1;
			c.startAnimation(1872);
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			break;
			
			case 1434: // mace
			c.startAnimation(1060);
			c.gfx100(251);
			c.specMaxHitIncrease = 3;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase())+1;
			c.specDamage = 1.35;
			c.specAccuracy = 1.15;
			break;
			
			case 859: // magic long
			c.usingBow = true;
			c.bowSpecShot = 3;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();	
			c.lastWeaponUsed = weapon;
			c.startAnimation(426);
			c.gfx100(250);	
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			if (c.fightMode == 2)
				c.attackTimer--;
			break;

				case 15015: // Morrigan Throwing Axe
					c.usingRangeWeapon = true;
				c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
				c.getItems().deleteArrow();
				c.lastWeaponUsed = weapon;
				c.startAnimation(10501);
				c.gfx0(1836);
				c.hitDelay = 3;
				c.specAccuracy = 1.90;
				c.specDamage = 1.20;
				c.projectileStage = 1;
				c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				if (c.fightMode == 2)
				c.attackTimer--;
				if (c.playerIndex > 0)
				fireProjectilePlayer();
				else if (c.npcIndex > 0)
				fireProjectileNpc();
				break; 
				
				case 15016: // Morrigan Javeline
					c.usingRangeWeapon = true;
				c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
				c.getItems().deleteArrow();
				c.lastWeaponUsed = weapon;
				c.startAnimation(10504);
				c.gfx0(1838);
				c.specAccuracy = 2.00;
				c.specDamage = 1.30;
				c.hitDelay = 3;
				c.projectileStage = 1;
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				if (c.fightMode == 2)
				c.attackTimer--;
				if (c.playerIndex > 0)
				fireProjectilePlayer();
				else if (c.npcIndex > 0)
				fireProjectileNpc();
				break; 
			
			case 861: // magic short	
			c.usingBow = true;			
			c.bowSpecShot = 1;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();	
			c.lastWeaponUsed = weapon;
			c.startAnimation(1074);
			c.hitDelay = 3;
			c.projectileStage = 1;
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if (c.fightMode == 2)
				c.attackTimer--;
			if (c.playerIndex > 0)
				fireProjectilePlayer();
			else if (c.npcIndex > 0)
				fireProjectileNpc();	
			break;
			
			case 11235: // dark bow	
			c.usingBow = true;
			c.dbowSpec = true;
			c.rangeItemUsed = c.playerEquipment[c.playerArrows];
			c.getItems().deleteArrow();
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.hitDelay = 3;
			c.startAnimation(426);
			c.projectileStage = 1;
			c.gfx100(getRangeStartGFX());
			c.hitDelay = getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			if (c.fightMode == 2)
				c.attackTimer--;
			if (c.playerIndex > 0)
				fireProjectilePlayer();
			else if (c.npcIndex > 0)
				fireProjectileNpc();
			c.specAccuracy = 1.75;
			c.specDamage = 1.50;
			break;
		}
		c.delayedDamage = Misc.random(calculateMeleeMaxHit());
		c.delayedDamage2 = Misc.random(calculateMeleeMaxHit());
		c.usingSpecial = false;
		c.getItems().updateSpecialBar();
	}
	
	
	public boolean checkSpecAmount(int weapon) {
		switch(weapon) {
			case 1249:
			case 1215:
			case 1231:
			case 5680:
			case 5698:
			case 1305:
			case 1434:
			case 15006:
			if(c.specAmount >= 2.5) {
				c.specAmount -= 2.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			
			case 4151:
            case 11694:
		case 15027:
			case 11698:
			case 4153:
			case 15020:
			case 15007: 
			case 15016: 
			case 15015: 
                        case 10887:
			if(c.specAmount >= 5) {
				c.specAmount -= 5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			
			case 3204:
			if(c.specAmount >= 3) {
				c.specAmount -= 3;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			
			case 1377:
			case 15050:
			case 11696:
			case 11730:
			if(c.specAmount >= 10) {
				c.specAmount -= 10;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;
			
			case 4587:
			case 859:
			case 861:
			case 11235:
			case 11700:
			if(c.specAmount >= 5.5) {
				c.specAmount -= 5.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

			
			default:
			return true; // incase u want to test a weapon
		}
	}
	
	public void resetPlayerAttack() {
		c.usingMagic = false;
		c.npcIndex = 0;
		c.faceUpdate(0);
		c.playerIndex = 0;
		c.getPA().resetFollow();
		//c.sendMessage("Reset attack.");
	}
	
	public int getCombatDifference(int combat1, int combat2) {
		if(combat1 > combat2) {
			return (combat1 - combat2);
		}
		if(combat2 > combat1) {
			return (combat2 - combat1);
		}	
		return 0;
	}
  */
}
