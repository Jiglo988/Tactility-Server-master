package org.hyperion.rs2.model.itf;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.itf.impl.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jet on 1/2/2015.
 */
public final class InterfaceManager {

    private static final Map<Integer, Interface> MAP = new HashMap<>();

    static {
        addGlobal(new RecoveryInterface());
        addGlobal(new AskForHelp());
        addGlobal(new HelpInterface());
        addGlobal(new PendingRequests());
        addGlobal(new ChangePassword());
        addGlobal(new PlayerProfileInterface());
        addGlobal(new ItemContainer());
        addGlobal(new NameItemInterface());
        addGlobal(new ModerationInterface());
        addGlobal(new DungoneeringParty());
        //addGlobal(new AchievementInterface());
    }

    private final Player player;
    private final Map<Integer, Interface> activeMap;

    public InterfaceManager(final Player player) {
        this.player = player;

        activeMap = new HashMap<>();
    }

    public static void addGlobal(final Interface itf) {
        MAP.put(itf.getId(), itf);
    }

    public static <T extends Interface> T get(final int id) {
        return (T) MAP.get(id);
    }

    public Collection<Interface> getActiveInterfaces() {
        return activeMap.values();
    }

    public boolean isActive(final int id) {
        return activeMap.containsKey(id);
    }

    public boolean isActive(final Interface itf) {
        return itf != null && isActive(itf.getId());
    }

    protected void setActive(final Interface itf, final boolean active) {
        if (itf == null)
            return;
        if (active)
            activeMap.put(itf.getId(), itf);
        else
            activeMap.remove(itf.getId());
    }

    public void show(final int id, final int additionalFlags) {
        final Interface itf = get(id);
        if (itf == null)
            return;
        itf.show(player, additionalFlags);
    }

    public void show(final int id) {
        show(id, 0);
    }

    public void hide(final int id, final int additionalFlags) {
        final Interface itf = get(id);
        if (itf == null)
            return;
        itf.hide(player, additionalFlags);
    }

    public void hide(final int id) {
        hide(id, 0);
    }
}
