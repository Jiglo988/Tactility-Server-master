package org.hyperion.rs2.model.content.misc2;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.combat.SummoningData.SummonType;
import org.hyperion.rs2.model.combat.summoning.SummoningSpecial;
import org.hyperion.rs2.model.combat.summoning.impl.PackYak;
import org.hyperion.rs2.model.container.BoB;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 9/13/14
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class SummoningBoBs implements ContentTemplate {
	

    private static final int PACK_YAK_SCROLL = 12435;

    @Override
    public boolean clickObject(Player player, int type, int useItem, int itemUsedSlot, int onItem, int slot) {
        if(type == ClickType.NPC_OPTION1) {
            if(player.getCombat().getFamiliar() != null) {
                NPC npc = (NPC)World.getNpcs().get(slot);
                player.debugMessage("Npc ownerid "+npc.ownerId+" npc id "+useItem);
                if(npc.ownerId < 1)
                    return false;
                if(npc.ownerId != player.getIndex()) {
                    player.sendMessage("This is not your familiar.");
                    return false;
                }
                player.playAnimation(Animation.create(7270));
                if(player.getBoB() != null) {
                   int index2 = - 1;
                    for(Item item : player.getBoB().toArray()) {
                        if (item == null)
                            continue;
                        index2++;
                            BoB.withdraw(player, index2, item.getId(), item.getCount());
                        if(player.getInventory().freeSlots() == 0)
                            break;
                    }
                }
                return true;
            }
        } else if(type == ClickType.ITEM_ON_ITEM){
            if(player.getCombat().getFamiliar() != null && !player.isDead() && player.cE.getFamiliar().getDefinition().getId() == SummonType.PACKYAK.npcId && useItem == PACK_YAK_SCROLL) {
            	SummoningSpecial.preformSpecial(player, new PackYak(onItem, slot));
            }
        } else if (type == ClickType.EAT) {
            if(useItem == 15262) {
                if(player.getInventory().remove(Item.create(useItem)) >= 1) {
                    player.getInventory().add(Item.create(18016, 10000));
                }
            }
        }
        return false;
    }

    @Deprecated
    public void init() throws FileNotFoundException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int[] getValues(int type) {
    	if(type == ClickType.NPC_OPTION1)
    		return org.hyperion.rs2.model.content.skill.Summoning.BoBids;
    	else if(type == ClickType.ITEM_ON_ITEM)
    		return new int[]{PACK_YAK_SCROLL};  //To change body of implemented methods use File | Settings | File Templates.
        else
            return new int[]{15262};
    }
}
