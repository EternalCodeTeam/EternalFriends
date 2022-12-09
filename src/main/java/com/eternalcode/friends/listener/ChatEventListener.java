package com.eternalcode.friends.listener;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.profile.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatEventListener implements Listener {

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;

    public ChatEventListener(ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages) {
        this.profileManager = profileManager;
        this.announcer = announcer;
        this.messages = messages;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String inputMessage = event.getMessage();
        if (!inputMessage.startsWith("!")) {
            return;
        }
        String message = this.messages.friends.chatFormat
                .replace("{player}", player.getName())
                .replace("{message}", inputMessage.substring(1));
        event.setCancelled(true);
        this.announcer.announceMessage(player.getUniqueId(), message);
        this.profileManager.getProfileByUUID(player.getUniqueId()).ifPresent(profile -> {
            for (UUID friend : profile.getFriends()) {
                this.announcer.announceMessage(friend, message);
            }
        });
    }
}