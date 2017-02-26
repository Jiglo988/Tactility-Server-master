package org.hyperion.rs2.model.joshyachievementsv2.task.impl;

import java.util.List;
import org.hyperion.rs2.model.joshyachievementsv2.task.Task;
import org.hyperion.rs2.model.joshyachievementsv2.utils.AchievementUtils;

public class ItemOpenTask extends Task{

    private static class MyFilter extends Filter<ItemOpenTask>{

        private final int itemId;

        private MyFilter(final int itemId){
            super(ItemOpenTask.class);
            this.itemId = itemId;
        }

        public boolean test(final ItemOpenTask t){
            return super.test(t)
                    && t.itemIds.contains(itemId);
        }
    }

    public final List<Integer> itemIds;

    public ItemOpenTask(final int id, final List<Integer> itemIds, final int quantity){
        super(id, quantity);
        this.itemIds = itemIds;

        final String itemsJoined = AchievementUtils.joinItems(itemIds);

        desc = String.format("Open %,d %s", quantity, itemsJoined);
    }

    public static Filter<ItemOpenTask> filter(final int itemId){
        return new MyFilter(itemId);
    }
}
