package org.hyperion.rs2.model.possiblehacks;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.security.EncryptionStandard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 6/14/2016.
 */
public enum DataType {
    PASSWORD {
        @Override
        public void process(final Player player, final String password) {
            check(player);
            PossibleHacksHolder
                    .getInstance()
                    .getMap()
                    .get(player.getName().toLowerCase())
                    .getPasswords()
                    .add(String.format("%s -> %s [%s] @ %s", EncryptionStandard.decryptPassword(player.getPassword()), password, player.getShortIP(), stamp()));
        }

        @Override
        public void checkData(final Player player, final DataSet data) {
            final List<String> passwords = data.getPasswords();
            if (!passwords.isEmpty()) {
                passwords.forEach(player::sendMessage);
            } else {
                player.sendMessage("@red@This player has no current Password Changes@bla@.");
            }
        }
    },
    PROTOCOL {
        @Override
        public void process(final Player player, final String protocol) {
            check(player);
            PossibleHacksHolder
                    .getInstance()
                    .getMap()
                    .get(player.getName().toLowerCase())
                    .getProtocols()
                    .add(String.format("%s -> %s @ %s", player.lastIp, protocol, stamp()));
        }

        @Override
        public void checkData(final Player player, final DataSet data) {
            final List<String> protocols = data.getProtocols();
            if (!protocols.isEmpty()) {
                protocols.forEach(player::sendMessage);
            } else {
                player.sendMessage("@red@This player has no current Protocol Changes@bla@.");
            }
        }
    },
    ADDRESS {
        @Override
        public void process(final Player player, final String address) {
            check(player);
            PossibleHacksHolder
                    .getInstance()
                    .getMap()
                    .get(player.getName().toLowerCase())
                    .getAddresses()
                    .add(String.format("%d -> %s @ %s", player.getLastMac(), address, stamp()));
        }

        @Override
        public void checkData(final Player player, final DataSet data) {
            final List<String> addresses = data.getAddresses();
            if (!addresses.isEmpty()) {
                addresses.forEach(player::sendMessage);
            } else {
                player.sendMessage("@red@This player has no current Address Changes@bla@.");
            }
        }
    },
    ALL {
        @Override
        public void process(Player player, String value) {

        }

        @Override
        public void checkData(final Player player, final DataSet data) {
            PASSWORD.checkData(player, data);
            PROTOCOL.checkData(player, data);
            ADDRESS.checkData(player, data);
        }
    };

    private static void check(final Player player) {
        if (!PossibleHacksHolder.getInstance().getMap().containsKey(player.getName().toLowerCase())) {
            PossibleHacksHolder.getInstance().getMap().put(player.getName().toLowerCase(), new DataSet());
        }
    }

    public static DataSet getDataSet(final String value) {
        return PossibleHacksHolder.getInstance().getMap().get(value);
    }

    private static String stamp() {
        return new SimpleDateFormat("M/dd/yyyy HH:mm:ss zzz").format(new Date());
    }

    public abstract void process(Player player, String value);

    public abstract void checkData(Player player, DataSet data);
}
