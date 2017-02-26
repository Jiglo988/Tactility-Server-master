package org.hyperion.rs2.model.content.itfactivation;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.ItemDefinition;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.content.ClickType;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.TextUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 1/14/15
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class BonusXP extends Interface implements ContentTemplate {

    private static final int ID = 7;

    private static final int LAMP = 18808;

    public BonusXP() {
        super(ID);
    }

    @Override
    public void handle(final Player player, final Packet pkt) {
        final int skill = pkt.getByte();

        if(skill > Skills.SKILL_COUNT)
            return;
        try {
            if(!player.getExtraData().getBoolean("confirmskill")) {
                player.getSkills().getBonusXP().ifPresent(
                    s -> player.sendf("@red@WARNING@bla@: Your current bonus skill of @red@%s@bla@ with %s remaining will run out - please confirm",
                            Skills.SKILL_NAME[s.getSkill()], s.timeRemaining()));
                player.getExtraData().put("confirmskill", true);
                return;
            }
        }catch(final Exception ex) {
            ex.printStackTrace();
        }
        if(player.getInventory().remove(Item.create(LAMP)) == 1) {

            player.getExtraData().put("confirmskill", false);

            player.getSkills().setBonusXP(new Skills.CurrentBonusXP(skill));

            player.sendf("You have just started your bonus skill of: @red@%s", Skills.SKILL_NAME[skill]);
            hide(player);
        }

    }

    @Override
    public boolean itemOptionOne(Player player, int id, int slot, int interfaceId) {
        if(interfaceId == Inventory.INTERFACE) {
            show(player);
        }
        return true;
    }

    public int[] getValues(int type) {
        if(type == ClickType.EAT) {
            return new int[]{LAMP};
        }
        return new int[0];
    }


}
