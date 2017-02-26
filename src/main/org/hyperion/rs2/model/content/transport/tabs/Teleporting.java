package org.hyperion.rs2.model.content.transport.tabs;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.content.ContentTemplate;
import org.hyperion.util.ArrayUtils;

import java.util.Arrays;

/**
 * @author Daniel
 */
public class Teleporting implements ContentTemplate {

    @Override
    public boolean clickObject(Player player, int type, int id, int b, int c, int d) {
        if (type == 1) {
            final Tab tab = Tab.getByIntegerValue(id);
            if (tab != null) {
                tab.process(player);
            }
        }
        return false;
    }

    @Override
    public int[] getValues(int type) {
        return type != 1 ? null : ArrayUtils.fromList(Arrays.asList(8007, 8008, 8009, 8010, 8011, 8012, 18806));
    }
}
