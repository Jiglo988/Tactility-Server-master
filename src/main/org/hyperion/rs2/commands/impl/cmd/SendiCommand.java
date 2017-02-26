package org.hyperion.rs2.commands.impl.cmd;

import org.hyperion.rs2.commands.NewCommand;
import org.hyperion.rs2.commands.util.CommandInput;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;

/**
 * Created by Drhales on 3/3/2016.
 */
public class SendiCommand extends NewCommand {

    private static final int[] GOLDFRAMES = {4233, 4246, 4247, 4247, 4249,
            4250, 6021, 4239, 4251, 4252, 4253, 4254, 4255, 6022, 4245, 4256,
            4257, 4258, 4259, 4260, 6023,};

    private static final int[] GOLDFRAMEITEMS = {1635, 1637, 1639, 1641,
            1643, 1645, 6564, 1654, 1656, 1658, 1660, 1662, 1664, 1673, 1675,
            1677, 1679, 1681, 1704,};

    private static boolean sendGoldInterface(final Player player) {
        for(int i = 0; i < GOLDFRAMEITEMS.length; i++) {
            player.getActionSender().sendInterfaceModel(GOLDFRAMES[i], 105,
                    GOLDFRAMEITEMS[i]);
        }
        return true;
    }

    public SendiCommand() {
        super("sendi", Rank.ADMINISTRATOR, new CommandInput<Integer>(integer -> integer > 0, "Integer", "An Interface ID"), new CommandInput<Integer>(integer -> integer > 0, "Integer", "Model ID"));
    }

    public boolean execute(final Player player, final String[] input) {
        final int id = Integer.parseInt(input[0].trim());
        final int model = Integer.parseInt(input[1].trim());
        sendGoldInterface(player);
        player.getActionSender().sendInterfaceModel(id, 105, model);
        return true;
    }

}
