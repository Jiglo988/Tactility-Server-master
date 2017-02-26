package org.hyperion.rs2.model.content.cluescroll;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.ClueScroll;
import org.hyperion.rs2.model.cluescroll.ClueScrollManager;
import org.hyperion.rs2.model.content.ContentTemplate;

import java.io.FileNotFoundException;
import java.util.Collection;

public class ClueScrollContentTemplate implements ContentTemplate  {

    public int[] getValues(final int type) {
        if (type != 1)
            return null;
        final Collection<ClueScroll> collection = ClueScrollManager.getAll();
        final int[] array = new int[collection.size()];
        int i = 0;
        for (final ClueScroll cs : collection)
            array[i++] = cs.getId();
        return array;
    }

    public void init() throws FileNotFoundException {
    }

    public boolean clickObject(final Player player, final int type, final int id, final int slot, final int interfaceId, final int idk) {
        final ClueScroll cs = ClueScrollManager.get(id);
        if (cs == null)
            return false;
        cs.send(player);
        return true;
    }
}
