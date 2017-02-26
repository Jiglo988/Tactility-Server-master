package org.hyperion.rs2.model.itf.impl;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketBuilder;

import java.util.Map;

/**
 * Created by Jet on 1/20/2015.
 */
public class PlayerProfileInterface extends Interface{

    public static final int ID = 8;

    private static final int INIT = 1;
    private static final int INFO = 2;
    private static final int SKILLS = 3;
    private static final int NPC_LOGS = 4;

    private String lastKnownName;

    public PlayerProfileInterface(){
        super(ID);
    }

    public boolean view(final Player player, final String targetName){
        if(targetName.isEmpty()){
            player.sendf("Invalid name");
            return false;
        }
        final Player target = World.getPlayerByName(targetName);
        if(target == null){
            player.sendf("This player is offline");
            return false;
        }
        if(target.getPermExtraData().getBoolean("disableprofile") && !Rank.hasAbility(player, Rank.MODERATOR)) {
            player.sendMessage("Player has disabled his public profile");
            return false;
        }
        player.getExtraData().put("lastKnownName", targetName);
        show(player);
        player.write(
                createDataBuilder()
                .put((byte)INIT)
                .putRS2String(targetName)
                .putLong(target.getCreatedTime())
                .putLong(target.getPermExtraData().getLong("logintime"))
                .toPacket()
        );
        return true;
    }

    public void handle(final Player player, final Packet pkt){
        final int requestId = pkt.getByte();
        if(player.getExtraData().get("lastKnownName") == null)
            return;
        final Player viewing = World.getPlayerByName(player.getExtraData().getString("lastKnownName"));
        if(viewing == null){
            player.sendf("Cannot request data of an offline player's profile (yet)");
            return;
        }
        switch(requestId){
            case INFO:
                player.write(
                        createDataBuilder()
                        .put((byte)INFO)
                        .putShort(viewing.getPoints().getEloRating())
                        .putInt(viewing.getKillCount())
                        .putInt(viewing.getDeathCount())
                        .putInt(viewing.getPoints().getPkPoints())
                        .putInt(viewing.getPoints().getDonatorPoints())
                        .putInt(viewing.getPoints().getHonorPoints())
                        .toPacket()
                );
                break;
            case SKILLS:
                final PacketBuilder skills = createDataBuilder();
                skills.put((byte)SKILLS);
                for(int i = 0; i < 24; i++){
                    skills.put((byte) viewing.getSkills().getRealLevels()[i])
                        .putInt(viewing.getSkills().getXps()[i]);
                }
                player.write(skills.toPacket());
                break;
            case NPC_LOGS:
                final Map<Integer, Integer> map = viewing.getNPCLogs().map();
                final PacketBuilder npcLogs = createDataBuilder();
                npcLogs.put((byte)NPC_LOGS);
                npcLogs.putShort(map.size());
                for(final Map.Entry<Integer, Integer> entry : map.entrySet()){
                    npcLogs.putShort(entry.getKey())
                           .putShort(entry.getValue());
                }
                player.write(npcLogs.toPacket());
                break;
        }
    }
}
