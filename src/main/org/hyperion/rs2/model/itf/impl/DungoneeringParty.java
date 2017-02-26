package org.hyperion.rs2.model.itf.impl;

import org.hyperion.rs2.model.*;
import org.hyperion.rs2.model.content.clan.ClanManager;
import org.hyperion.rs2.model.itf.Interface;
import org.hyperion.rs2.model.itf.InterfaceManager;
import org.hyperion.rs2.net.ActionSender;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.TextUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Daniel
 */
public class DungoneeringParty extends Interface {

    public static final int ID = 13;

    public DungoneeringParty() {
        super(ID);
    }

    public static Interface getInterface() {
        return InterfaceManager.get(ID);
    }

    public static void respond(final Player player, final int value) {
        final Player leader = (Player) player.getExtraData().get("DungeonInvitation");
        if (leader != null && leader.getLocation().equals(Locations.Location.DUNGEONEERING_LOBBY)) {
            leader.write(getInterface().createDataBuilder().put((byte) value).putRS2String(player.getName()).toPacket());
            if (value == 0) {
                if (player.getDungeoneeringLeader() != null) {
                    if (player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().contains(player)) {
                        player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().remove(player);
                    }
                }
                player.setDungeoneeringLeader(leader);
                player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().add(player);
                final String party = String.format("Party %s", leader.getName());
                if (!player.getClanName().equalsIgnoreCase(party)) {
                    ClanManager.leaveChat(player, true, true);
                    ClanManager.joinClanChat(player, party, false);
                }
            }
            leader.sendf("Player @red@%s@bla@ has %s@bla@ your invite request.", TextUtils.titleCase(player.getName()), value != 0 ? "@red@declined" : "@gre@accepted");
            player.getExtraData().remove("DungeonInvitation");
        } else {
            player.sendf("This Dungeoneering invitation has expired.");
        }
    }

    public static void removeFromLobbyParty(final Player player) {
        final Player leader = player.getDungeoneeringLeader();
        if (leader != null) {
            if (leader != player) {
                if (leader.getDungeoneeringLobbyTeam().contains(player)) {
                    leader.getDungeoneeringLobbyTeam().remove(player);
                    leader.write(getInterface().createDataBuilder().put((byte) 1).putRS2String(player.getName()).toPacket());
                    leader.sendf("Player @red@%s@bla@ has been removed from your lobby party.", TextUtils.titleCase(player.getName()));
                }
                player.setDungeoneeringLeader(null);
            } else {
                if (!player.getDungeoneeringLobbyTeam().isEmpty()) {
                    player.getDungeoneeringLobbyTeam().stream().filter(target -> target != null && target != player).forEach(target -> {
                        player.write(getInterface().createDataBuilder().put((byte) 1).putRS2String(target.getName()).toPacket());
                        player.sendf("Player @red@%s@bla@ has been removed from your lobby party.", TextUtils.titleCase(target.getName()));
                        target.sendf("You have been removed from @red@%s@bla@'s Dungeoneering Party.", TextUtils.titleCase(player.getName()));
                        target.setDungeoneeringLeader(null);
                    });
                    player.getDungeoneeringLobbyTeam().clear();
                }
            }
        }
    }

    @Override
    public void handle(final Player player, final Packet packet) {
        final Button button = Button.getByIntegerValue(packet.getByte());
        if (button != null) {
            button.process(player, packet, DungeonDifficulty.values()[packet.getByte()]);
        }
    }

    private enum Button {
        START(0) {
            public void process(final Player player, final Packet packet, final DungeonDifficulty difficulty) {
                final int index = packet.getByte();
                if (index < 0) {
                    return;
                }
                if (player.getDungeoneeringLeader() != player) {
                    if (player.getDungeoneeringLeader() != null && player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().contains(player)) {
                        player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().remove(player);
                    }
                    player.setDungeoneeringLeader(player);
                }
                if (!player.getDungeoneeringLobbyTeam().contains(player)) {
                    player.getDungeoneeringLobbyTeam().add(player);
                }
                final List<Player> list = new CopyOnWriteArrayList<>();
                player.getDungeoneeringLobbyTeam().stream()
                        .filter(target -> target != null && target.getLocation().equals(Locations.Location.DUNGEONEERING_LOBBY))
                        .forEach(target -> {
                    if (target.getSkills().getLevel(Skills.DUNGEONEERING) < difficulty.min_level) {
                        player.sendf("Player @red@%s@bla@ does not meet the Dungeon requirements and has been removed.", target.getName());
                        player.write(getInterface().createDataBuilder().put((byte) 1).putRS2String(target.getName()).toPacket());
                    } else {
                        list.add(target);
                    }
                });
                player.getDungeoneeringLobbyTeam().clear();
                getInterface().hide(player);
                player.getDungeoneering().start(list, difficulty, DungeonDifficulty.DungeonSize.values()[index]);
            }
        },
        INVITE(1) {
            public void process(final Player player, final Packet packet, final DungeonDifficulty difficulty) {
                if (player.getLocation().equals(Locations.Location.DUNGEONEERING_LOBBY)) {
                    player.getDungeoneeringLobbyTeam().stream().filter(target -> target != null && !target.getLocation().equals(Locations.Location.DUNGEONEERING_LOBBY)).forEach(target -> {
                        player.getDungeoneeringLobbyTeam().remove(target);
                        target.setDungeoneeringLeader(null);
                        player.write(getInterface().createDataBuilder().put((byte) 1).putRS2String(target.getName()).toPacket());
                    });
                    final String name = packet.getRS2String();
                    final Player target = World.getPlayerByName(name);
                    if (player != target) {
                        if (player.getDungeoneeringLeader() != player) {
                            if (player.getDungeoneeringLeader() != null && player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().contains(player)) {
                                player.getDungeoneeringLeader().getDungeoneeringLobbyTeam().remove(player);
                            }
                            player.setDungeoneeringLeader(player);
                        }
                        if (!player.getDungeoneeringLobbyTeam().contains(player)) {
                            player.getDungeoneeringLobbyTeam().add(player);
                        }
                        final String party = String.format("Party %s", player.getName());
                        if (!player.getClanName().equalsIgnoreCase(party)) {
                            ClanManager.leaveChat(player, true, true);
                            ClanManager.joinClanChat(player, party, false);
                        }
                        if (target != null) {
                            if (player.getDungeoneeringLobbyTeam().contains(target)) {
                                player.sendf("Player @red@%s @bla@is already apart of your Dungeoneering Team.", TextUtils.titleCase(target.getName()));
                            } else {
                                if (player.getDungeoneeringLobbyTeam().size() >= 5) {
                                    player.sendMessage("Your Dungeoneering Party is currently full.");
                                    return;
                                }
                                if (target.getSkills().getLevel(Skills.DUNGEONEERING) < difficulty.min_level || !target.getLocation().equals(Locations.Location.DUNGEONEERING_LOBBY)) {
                                    player.write(getInterface().createDataBuilder().put((byte) 1).putRS2String(target.getName()).toPacket());
                                    return;
                                }
                                target.getActionSender().sendDialogue(String.format("Join %s?", TextUtils.titleCase(player.getName())), ActionSender.DialogueType.OPTION, 1, Animation.FacialAnimation.DEFAULT, "Yes, I want to join this dungeon.", "No, I don't want to join this dungeon.");
                                target.getExtraData().put("DungeonInvitation", player);
                                target.getInterfaceState().setNextDialogueId(0, 7000);
                                target.getInterfaceState().setNextDialogueId(1, 7001);
                            }
                        }
                    } else {
                        player.sendMessage("You cannot invite yourself!");
                    }
                }
            }
        },
        DELETE(2) {
            @Override
            public void process(final Player player, final Packet packet, final DungeonDifficulty difficulty) {
                final String name = packet.getRS2String();
                final Player target = World.getPlayerByName(name);
                if (target != null) {
                    if (player != target) {
                        if (target.getExtraData().get("DungeonInvitation") != null) {
                            target.getExtraData().remove("DungeonInvitation");
                        }
                        if (player.getDungeoneeringLobbyTeam().contains(target)) {
                            player.getDungeoneeringLobbyTeam().remove(target);
                            target.sendf("You have been removed from %s's Dungeoneering Party.", TextUtils.titleCase(player.getName()));
                        }
                    }
                }
            }
        };

        private static final Map<Integer, Button> BY_INTEGER_VALUE = Stream.of(values()).collect(Collectors.toMap(Button::getValue, Function.identity()));

        private final int value;

        Button(int value) {
            this.value = value;
        }

        public static Button getByIntegerValue(final int value) {
            return BY_INTEGER_VALUE.get(value);
        }

        public int getValue() {
            return value;
        }

        public abstract void process(Player player, Packet packet, DungeonDifficulty difficulty);
    }
}
