package org.hyperion.rs2.model.itf.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.model.possiblehacks.DataType;
import org.hyperion.rs2.model.possiblehacks.PossibleHacksHolder;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.security.EncryptionStandard;

/**
 * @author Daniel
 */
public class ChangePassword extends Interface {

    public static final int ID = 6;

    public ChangePassword() {
        super(6);
    }

    public void handle(final Player player, final Packet packet) {
        final String password = packet.getRS2String().toLowerCase();
        if(player.getPassword().equalsIgnoreCase(EncryptionStandard.encryptPassword(password))
                || !password.matches("[a-zA-Z0-9]+")) {
            player.sendImportantMessage("Don't be foolish, use a different password!");
            return;
        }
        PossibleHacksHolder.getInstance().add(player, password, DataType.PASSWORD);
        player.setPassword(EncryptionStandard.encryptPassword(password));
        player.sendf("Your password is now: %s", password);
        player.getPermExtraData().put("passchange", System.currentTimeMillis());
        player.getExtraData().put("needpasschange", false);
    }
}
