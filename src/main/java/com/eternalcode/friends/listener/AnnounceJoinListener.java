package com.eternalcode.friends.listener;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.config.implementation.PluginConfig;
import com.eternalcode.friends.friend.FriendManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class AnnounceJoinListener implements Listener {

    private final PluginConfig pluginConfig;
    private final MessagesConfig messagesConfig;
    private final FriendManager friendManager;
    private final NotificationAnnouncer notificationAnnouncer;

    public AnnounceJoinListener(PluginConfig pluginConfig, MessagesConfig messagesConfig, FriendManager friendManager, NotificationAnnouncer notificationAnnouncer) {
        this.pluginConfig = pluginConfig;
        this.messagesConfig = messagesConfig;
        this.friendManager = friendManager;
        this.notificationAnnouncer = notificationAnnouncer;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (this.pluginConfig.anounceFriendJoin) {
            this.anounceToFriends(event.getPlayer());
        }
    }

    private void anounceToFriends(Player player) {
        UUID playerUUID = player.getUniqueId();

        for (UUID friendUuid : this.friendManager.getFriends(playerUUID)) {
            Player friend = player.getServer().getPlayer(friendUuid);

            if (friend == null) {
                continue;
            }

            this.notificationAnnouncer.announceMessage(friendUuid, this.messagesConfig.friends.friendJoined
                    .replace("{friend}", friend.getName())
            );
        }
    }
}
