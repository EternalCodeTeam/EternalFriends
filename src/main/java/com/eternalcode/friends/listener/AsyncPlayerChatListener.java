package com.eternalcode.friends.listener;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.friend.FriendManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AsyncPlayerChatListener implements Listener {

    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;
    private final FriendManager friendManager;

    public AsyncPlayerChatListener(NotificationAnnouncer announcer, MessagesConfig messages, FriendManager friendManager) {
        this.announcer = announcer;
        this.messages = messages;
        this.friendManager = friendManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String inputMessage = event.getMessage();

        if (!inputMessage.startsWith("!")) {
            return;
        }

        event.setCancelled(true);

        String message = this.messages.friends.chatFormat
                .replace("{player}", player.getName())
                .replace("{message}", inputMessage.substring(1));
        this.announcer.announceMessage(player.getUniqueId(), message);

        for (UUID friend : this.friendManager.getFriends(player.getUniqueId())) {
            this.announcer.announceMessage(friend, message);
        }
    }
}