package com.eternalcode.friends.listener;

import com.comphenix.protocol.ProtocolManager;
import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.config.implementation.PluginConfig;
import com.eternalcode.friends.friend.FriendManager;
import com.eternalcode.friends.packet.NameTagService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class JoinQuitListener implements Listener {

    private final ProtocolManager protocolManager;
    private final NameTagService nameTagService;
    private final FriendManager friendManager;
    private final PluginConfig pluginConfig;
    private final MessagesConfig messagesConfig;
    private final NotificationAnnouncer notificationAnnouncer;

    public JoinQuitListener(ProtocolManager protocolManager, NameTagService nameTagService, FriendManager friendManager, PluginConfig pluginConfig, MessagesConfig messagesConfig, NotificationAnnouncer notificationAnnouncer) {
        this.protocolManager = protocolManager;
        this.nameTagService = nameTagService;
        this.friendManager = friendManager;
        this.pluginConfig = pluginConfig;
        this.messagesConfig = messagesConfig;
        this.notificationAnnouncer = notificationAnnouncer;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.sendScoreboardPacket(player);

        if (this.pluginConfig.anounceFriendJoin) {
            this.anounceToFriends(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.nameTagService.removePlayersTeam(event.getPlayer());
    }

    //send ScoreboardTeam packet to update color of player's nametag
    private void sendScoreboardPacket(Player player) {
        UUID playerUUID = player.getUniqueId();

        for (UUID uuid : this.friendManager.getFriends(playerUUID)) {
            if (playerUUID.equals(uuid)) {
                continue;
            }

            Player friend = player.getServer().getPlayer(uuid);

            if (friend == null) {
                continue;
            }

            if (!this.friendManager.areFriends(uuid, playerUUID)) {
                this.nameTagService.createTeamPacketOfTwoNoFriends(player, friend);

                continue;
            }

            this.nameTagService.createTeamPacketOfTwoFriends(player, friend);
        }
    }

    private void anounceToFriends(Player player) {
        UUID playerUUID = player.getUniqueId();

        for (UUID uuid : this.friendManager.getFriends(playerUUID)) {
            Player friend = player.getServer().getPlayer(uuid);

            if (friend == null) {
                continue;
            }

            this.notificationAnnouncer.announceMessage(uuid, this.messagesConfig.friends.friendJoined
                    .replace("{friend}", friend.getName())
            );
        }
    }


}
