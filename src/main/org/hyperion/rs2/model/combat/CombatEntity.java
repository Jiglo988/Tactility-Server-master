package org.hyperion.rs2.model.combat;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.attack.BulwarkBeast;
import org.hyperion.rs2.model.combat.attack.CorporealBeast;
import org.hyperion.rs2.model.combat.attack.TormentedDemon;
import org.hyperion.rs2.model.content.bounty.BountyPerkHandler;
import org.hyperion.rs2.model.content.minigame.FightPits;

import java.util.*;

public class CombatEntity {

	public CombatEntity(Entity e) {
		entity = e;
		if(getEntity() instanceof Player) {
			player = (Player) getEntity();
		} else {
			n = (NPC) getEntity();
		}
	}

	public void nullShit() {
		opponent = null;
		attackers.clear();
		damageDealt.clear();
	    /*entity = null;
		p = null;
		n = null;
		summonedNpc = null;*/
	}

	public Map<String, Integer> getDamageDealt() {
		return damageDealt;
	}

	public Attack attack = null;
	private List<CombatEntity> attackers = new LinkedList<CombatEntity>();
	private Entity entity;
	private Player player = null;
	private NPC n = null;
	public NPC summonedNpc = null;

	public NPC getFamiliar() {
		return summonedNpc;
	}

	private boolean isPoisoned = false;
	public long lastHit = System.currentTimeMillis() - 30000;
	public long predictedAtk = System.currentTimeMillis();
	//public long predictedAtk2 = System.currentTimeMillis();
	private int atkSpeed = 2000;
	
	public boolean isFrozen() {
		if(System.currentTimeMillis() < freezeTimer) {
            if(player != null) {
                if(Rank.hasAbility(player, Rank.ADMINISTRATOR)) {
                   player.debugMessage("You are frozen for another: " + (freezeTimer - System.currentTimeMillis()) + "MS");
                }
            }
            return true;
        }
        return false;
	}
	public boolean canBeFrozen() {
		return !(System.currentTimeMillis() < (freezeTimer + 5000L));
	}
	public void setFreezeTimer(long time) {
		freezeTimer = System.currentTimeMillis() + time;
	}

	private long freezeTimer = 0;//can move after this time
	private int autoCastId = - 1;
	private int weaponPoisons = 0;
	private int atkEmote = 422;
	private int defEmote = 404;
	private int atkType = 2;//start with str
	public int trainSkill = 0;
	public int specialArmour = - 1;
	public int bowType = 0;//accurate, rapid, long
	private CombatEntity opponent = null;
	public int morrigansLeft = 0;
	//private int[] magicAttacks = new int[2];//you can change this to queue up magic attacks so they are executed in the right order
	private int magicAttackNext = 0;

	private Map<String, Integer> damageDealt = new HashMap<String, Integer>();

	public boolean isNpcAttackAble() {
		if(getEntity() instanceof Player) {
			return true;
		} else if(n.maxHealth <= 0) {
			return false;
		}
		return true;
	}

	public int getNextMagicAtk() {
		return magicAttackNext;
	}

	public void addSpellAttack(int j) {
		//getPlayerByName().getActionSender().sendMessage("Add Spell Atk : " + j);
		magicAttackNext = j;
	}

	public void deleteSpellAttack() {
		magicAttackNext = 0;
	}

	public CombatEntity getOpponent() {
		return opponent;
	}

	public List<CombatEntity> getAttackers() {
		return attackers;
	}

	public Player getPlayer() {
		return player;
	}

	public NPC getNPC() {
		return n;
	}

    public Optional<NPC> _getNPC() {
        return Optional.ofNullable(n);
    }

    public Optional<Player> _getPlayer() {
        return Optional.ofNullable(player);
    }

	public boolean isPoisoned() {
		return isPoisoned;
	}

	public int getAtkSpeed() {
		return atkSpeed;
	}

	public int getAutoCastId() {
		return autoCastId;
	}

	public int getAtkEmote() {
		return atkEmote;
	}

	public int getDefEmote() {
		return defEmote;
	}

	public int getWeaponPoison() {
		return weaponPoisons;
	}

	public int getAtkType() {
		return atkType;
	}


	public Entity getEntity() {
		return entity;
	}

	public void setPoisoned(boolean b) {
		isPoisoned = b;
	}


	public void setAtkType(int i) {
		//getPlayerByName().getActionSender().sendMessage("Your atkType is now " + i);
		atkType = i;
	}

	public void setWeaponPoison(int a) {
		weaponPoisons = a;
	}

	public void setDefEmote(int a) {
		defEmote = a;
	}

	public void setAtkEmote(int a) {
		atkEmote = a;
	}

	public void setAutoCastId(int a) {
		autoCastId = a;
	}

	public void setAtkSpeed(int a) {
		//System.out.println("Setting Attack speed: " + a);
		atkSpeed = a;
	}

	public void setOpponent(CombatEntity e) {

		opponent = e;
	}


	public boolean canMove() {
		return ! isFrozen();
	}

	public int getFightType() {
		return 1;
	}

	/**
	 * Gets the player who has caused most damage to the CombatEntity.
	 *
	 * @return
	 */
	public Player getKiller() {
		String winner = null;
		int max = 0;
		for(Map.Entry<String, Integer> entry : damageDealt.entrySet()) {
			int damage = entry.getValue();
			if(damage > max) {
				winner = entry.getKey();
				max = damage;
			}
			//System.out.println("killer: "+m+" : "+entry.getKey());
		}
		damageDealt.clear();
		if(winner == null)
			return null;
		return World.getPlayerByName(winner);
	}


	public int hit(int damage, Entity attacker, boolean poison, int style) {
		try {
            if(this == null)
                return 0;
			/**
			 * Since this seems to be the root of hitting (returns method {@link #inflictDamage})
			 * and that decrements player's HP, if inflictDamage is not called if the attacker is dead
			 * Then the glitch will not occur - hopefully ^.^
			 */
			if(player != null && attacker != null && attacker.isDead() && attacker instanceof Player) {
				return 0;
			}
			if(attacker == null && player != null) {
				return player.inflictDamage(damage, attacker, poison, style);
			}
			//now that poison etc has been dealt
			/*if(attacker == null)
				return 0;*/
			//Update last attacker
			if(attacker != null && getEntity() instanceof Player && attacker instanceof Player) {
				getPlayer().getLastAttack().updateLastAttacker(attacker.cE.getPlayer().getName());
			}
			int lastDamage = 0;
			//Load last damage dealt by attacker
			if(damageDealt != null && attacker != null) {
				if(attacker instanceof Player) {
					if (damageDealt.containsKey(((Player)attacker).getName())) {
						lastDamage = damageDealt.get(((Player)attacker).getName());
					}
                //Update damage dealt
					if (attacker instanceof Player) {
						damageDealt.put(((Player) attacker).getName(), lastDamage + damage);
					}
				}
            }
			if(getEntity() instanceof Player) {
				if(attacker instanceof Player) {
					Player atk = (Player)attacker;
					if(FightPits.inGame(atk)) {
						atk.increasePitsDamage(damage);
					}
					BountyPerkHandler.appendPrayerLeechPerk(atk, getPlayer(), damage);
				}
				return player.inflictDamage(damage, attacker, poison, style);
			} else {
                damage = endEffect(n, attacker, damage, style);
				return n.inflictDamage(damage, attacker, poison, style);
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error hitting!");
			return 0;
		}
	}
    //use this for end effect from now on, don't crowd the inflictdamg
    public static int endEffect( NPC n, Entity attacker, int damage, int style) {
        final int id = n.getDefinition().getId();
        if(attacker instanceof Player)
            return TormentedDemon.getReduction(n, (Player)attacker, damage);
        if(id == 8133) {
            if(attacker instanceof Player) {
                Player atk = (Player)attacker;
                damage = CorporealBeast.reduceDamage(atk, damage, style);
            }
        }
        if(id == 10106 && attacker instanceof Player) {
            Player atk = (Player) attacker;
            BulwarkBeast.handleRecoil(atk, damage);
        }
        if(id == 8596) {
            if(style == Constants.MELEE)
                damage = 0;
        }
        return damage;
    }


	public CombatEntity getCurrentAtker() {
		Object o[] = attackers.toArray();
		if(o != null) {
			if(o.length > 0) {
				if(System.currentTimeMillis() - lastHit > 10000) {
					attackers.clear();
					return null;
				}
				return ((CombatEntity) o[0]);
			}
		}
		return null;
	}


	public int getAbsX() {
		return getEntity().getPosition().getX();
	}

	public int getAbsY() {
		return getEntity().getPosition().getY();
	}

	public int getAbsZ() {
		return getEntity().getPosition().getZ();
	}

	public int getCombat() {
		if(getEntity() instanceof Player)
			return player.getSkills().getCombatLevel();
		else
			return n.getDefinition().combat();
	}

	public boolean isDoingAtk = false;
	//public long predictedAtk3 = System.currentTimeMillis();
	public boolean vacating = false;

	public void doAtkEmote() {
		isDoingAtk = true;
		if(getEntity() instanceof Player)
			doAnim(atkEmote);
		else
			doAnim(n.getDefinition().getAtkEmote(0));
	}

	public int getOffsetX() {
		if(getEntity() instanceof Player)
			return 0;
		return n.getDefinition().sizeX();
	}

	public int getOffsetY() {
		if(getEntity() instanceof Player)
			return 0;
		return n.getDefinition().sizeY();
	}

	public void doDefEmote() {
		if(isDoingAtk)
			return;
		if(getEntity() instanceof Player) {
            if(player.getNpcState()) {
                NPCDefinition def = NPCDefinition.forId(player.getNpcId());
                if(def.doesDefEmote())
                    doAnim(def.blockEmote());
                else

                    doAnim(defEmote);
            } else
			    doAnim(defEmote);
        } else if(n.getDefinition().blockEmote() > 0)
			doAnim(n.getDefinition().blockEmote());
	}

	public void doAnim(int id) {
		if(id < 1)
			return;

		getEntity().playAnimation(Animation.create(id, 0));
	}

	public void doGfx(int id) {
		if(id < 1)
			return;
		doGfx(id, 6553600);
	}

	public void doGfx(int id, int delay) {
		getEntity().playGraphics(Graphic.create(id, delay));
	}

	public void face(int x, int y) {
		getEntity().face(Position.create(x, y, 0));
	}

	public void face(int x, int y, boolean test) {
		getEntity().face(Position.create(x, y, 0));
	}

	public int getSlotId(Entity e) {
		if(e instanceof Player) {
			if(getEntity() instanceof NPC) {
				return (- getEntity().getIndex() - 1);
			} else {
				return (getEntity().getIndex() + 1);
			}
		} else {
			if(getEntity() instanceof Player) {
				return (- getEntity().getIndex() - 1);
			} else {
				return (getEntity().getIndex() + 1);
			}
		}
	}

	public int meleeDef(int fightType) {
		if(getEntity() instanceof Player)
			return Defence.meleeDef((Player) getEntity(), fightType);
		return 10;
	}

	public int rangeDef() {
		if(getEntity() instanceof Player)
			return Defence.rangeDef((Player) getEntity());
		return 10;
	}

	public int mageDef() {
		if(getEntity() instanceof Player)
			return Defence.mageDef((Player) getEntity());
		return 10;
	}

	public int meleeAtk(boolean bool) {
		if(getEntity() instanceof Player)
			return Defence.meleeAtk((Player) getEntity(), atkType, bool);
		return 10;
	}

	public int rangeAtk(boolean bool) {
		if(getEntity() instanceof Player)
			return Defence.rangeAtk((Player) getEntity(), bool);
		return 10;
	}

	public int mageAtk(int spellId) {
		if(getEntity() instanceof Player)
			return Defence.mageAtk((Player) getEntity(), spellId);
		return 10;
	}
}