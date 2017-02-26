package org.hyperion.rs2.model.content.skill.summoning;

import org.hyperion.rs2.util.TextUtils;

/**
 * @author Daniel
 */
public enum Charm {
    GOLD(12158),
    GREEN(12159),
    CRIMSON(12160),
    ABYSSAL(12161),
    TALON_BEAST(12162),
    BLUE(12163),
    RAVAGER(12164),
    SHIFTER(12165),
    SPINNER(12166),
    TORCHER(12167),
    OBSIDIAN(12168);

    private final int ITEM;

    Charm(int item) {
        this.ITEM = item;
    }

    public int getItem() {
        return ITEM;
    }

    @Override
    public String toString() {
        return TextUtils.titleCase(super.toString().replace("_", " "));
    }
}
