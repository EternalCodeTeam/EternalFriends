package com.eternalcode.friends.listener;

import com.eternalcode.friends.NotificationAnnouncer;
import com.eternalcode.friends.config.implementation.MessagesConfig;
import com.eternalcode.friends.profile.Profile;
import com.eternalcode.friends.profile.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;
import java.util.UUID;

public class AsyncPlayerChatListener implements Listener {

    private final ProfileManager profileManager;
    private final NotificationAnnouncer announcer;
    private final MessagesConfig messages;

    public AsyncPlayerChatListener(ProfileManager profileManager, NotificationAnnouncer announcer, MessagesConfig messages) {
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

        event.setCancelled(true);

        String message = this.messages.friends.chatFormat
                .replace("{player}", player.getName())
                .replace("{message}", inputMessage.substring(1));
        this.announcer.announceMessage(player.getUniqueId(), message);

        Optional<Profile> profileOptional = this.profileManager.getProfileByUUID(player.getUniqueId());

        if (profileOptional.isEmpty()) {
            return;
        }

        Profile profile = profileOptional.get();

        for (UUID friend : profile.getFriends()) {
            this.announcer.announceMessage(friend, message);
        }
    }
}