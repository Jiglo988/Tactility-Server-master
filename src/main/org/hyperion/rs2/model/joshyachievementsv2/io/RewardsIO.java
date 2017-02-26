package org.hyperion.rs2.model.joshyachievementsv2.io;

import org.hyperion.rs2.model.joshyachievementsv2.reward.Reward;
import org.hyperion.rs2.model.joshyachievementsv2.reward.Rewards;
import org.hyperion.rs2.model.joshyachievementsv2.reward.impl.ItemReward;
import org.hyperion.rs2.model.joshyachievementsv2.reward.impl.PointsReward;
import org.hyperion.rs2.model.joshyachievementsv2.reward.impl.SkillXpReward;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class RewardsIO extends IOManager<Reward, Rewards, RewardsIO.RewardIO>{

    public interface RewardIO<T extends Reward> extends IO<T>{

        default String tag(){
            return "reward";
        }
    }

    protected RewardsIO(){
        super("rewards", Rewards::new, r -> r.list);
    }

    protected void populate(){
        put(ItemReward.class, new RewardIO<ItemReward>(){
            public void encode(final Document doc, final Element root, final ItemReward r){
                final Element item = create(doc, "item");
                attr(item, "id", r.itemId);
                attr(item, "quantity", r.itemQuantity);
                attr(item, "preferInventory", r.preferInventory);
                root.appendChild(item);
            }

            public ItemReward decode(final Element root){
                final Element item = child(root, "item");
                final int itemId = intAttr(item, "id");
                final int itemQuantity = intAttr(item, "quantity");
                final boolean preferInventory = boolAttr(item, "preferInventory");
                return new ItemReward(itemId, itemQuantity, preferInventory);
            }
        });

        put(PointsReward.class, new RewardIO<PointsReward>(){
            public void encode(final Document doc, final Element root, final PointsReward r){
                final Element points = create(doc, "points");
                attr(points, "type", r.type.name());
                attr(points, "amount", r.amount);
                root.appendChild(points);
            }

            public PointsReward decode(final Element root){
                final Element points = child(root, "points");
                final PointsReward.Type type = PointsReward.Type.valueOf(attr(points, "type"));
                final int amount = intAttr(points, "amount");
                return new PointsReward(type, amount);
            }
        });

        put(SkillXpReward.class, new RewardIO<SkillXpReward>(){
            public void encode(final Document doc, final Element root, final SkillXpReward r){
                final Element skill = create(doc, "skill");
                attr(skill, "id", r.skill);
                attr(skill, "xp", r.xp);
                root.appendChild(skill);
            }

            public SkillXpReward decode(final Element root){
                final Element skill = child(root, "skill");
                final int id = intAttr(skill, "id");
                final int xp = intAttr(skill, "xp");
                return new SkillXpReward(id, xp);
            }
        });
    }

}
