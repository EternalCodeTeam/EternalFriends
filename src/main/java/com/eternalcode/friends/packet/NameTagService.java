package com.eternalcode.friends.packet;

import com.comphenix.protocol.ProtocolManager;
import com.eternalcode.friends.config.implementation.PluginConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class NameTagService {

    private final static ChatColor DEFAULT_PLAYER_NAME_COLOR = ChatColor.WHITE;

    private final ChatColor friendNameTagColor;

    private final ProtocolManager protocolManager;
    private final PluginConfig pluginConfig;

    public NameTagService(ProtocolManager protocolManager, PluginConfig pluginConfig) {
        this.protocolManager = protocolManager;
        this.pluginConfig = pluginConfig;
        this.friendNameTagColor = pluginConfig.friendColor;
    }

    public void createTeamPacketOfTwoFriends(Player one, Player two) {
        WrapperPlayServerScoreboardTeam firstPacket = new WrapperPlayServerScoreboardTeam()
            .setName(one.getName())
            .setMode(0)
            .setPlayers(List.of(one.getName()))
            .setNameTagVisibility("always")
            .setColor(friendNameTagColor);

        WrapperPlayServerScoreboardTeam secondPacket = new WrapperPlayServerScoreboardTeam()
            .setName(two.getName())
            .setMode(0)
            .setPlayers(List.of(two.getName()))
            .setNameTagVisibility("always")
            .setColor(friendNameTagColor);

        this.protocolManager.sendServerPacket(one, secondPacket.getHandle());
        this.protocolManager.sendServerPacket(two, firstPacket.getHandle());
    }

    public void createTeamPacketOfTwoNoFriends(Player one, Player two) {
        WrapperPlayServerScoreboardTeam firstPacket = new WrapperPlayServerScoreboardTeam()
            .setName(one.getName())
            .setMode(0)
            .setPlayers(List.of(one.getName()))
            .setNameTagVisibility("always")
            .setColor(DEFAULT_PLAYER_NAME_COLOR);

        WrapperPlayServerScoreboardTeam secondPacket = new WrapperPlayServerScoreboardTeam()
            .setName(two.getName())
            .setMode(0)
            .setPlayers(List.of(two.getName()))
            .setNameTagVisibility("always")
            .setColor(DEFAULT_PLAYER_NAME_COLOR);

        this.protocolManager.sendServerPacket(one, secondPacket.getHandle());
        this.protocolManager.sendServerPacket(two, firstPacket.getHandle());
    }

    public void updateNameTagOfTwoFriends(Player one, Player two) {
        WrapperPlayServerScoreboardTeam firstPacket = new WrapperPlayServerScoreboardTeam()
            .setName(one.getName())
            .setMode(2)
            .setColor(friendNameTagColor);

        WrapperPlayServerScoreboardTeam secondPacket = new WrapperPlayServerScoreboardTeam()
            .setName(two.getName())
            .setMode(2)
            .setColor(friendNameTagColor);

        this.protocolManager.sendServerPacket(one, secondPacket.getHandle());
        this.protocolManager.sendServerPacket(two, firstPacket.getHandle());
    }

    public void updateNameTagOfTwoNoFriends(Player one, Player two) {
        WrapperPlayServerScoreboardTeam firstPacket = new WrapperPlayServerScoreboardTeam()
            .setName(one.getName())
            .setMode(2)
            .setColor(DEFAULT_PLAYER_NAME_COLOR);

        WrapperPlayServerScoreboardTeam secondPacket = new WrapperPlayServerScoreboardTeam()
            .setName(two.getName())
            .setMode(2)
            .setColor(DEFAULT_PLAYER_NAME_COLOR);

        this.protocolManager.sendServerPacket(one, secondPacket.getHandle());
        this.protocolManager.sendServerPacket(two, firstPacket.getHandle());
    }

    public void removePlayersTeam(Player player) {
        this.protocolManager.broadcastServerPacket(
            new WrapperPlayServerScoreboardTeam()
                .setName(player.getName())
                .setMode(1)
                .getHandle()
        );
    }
}
