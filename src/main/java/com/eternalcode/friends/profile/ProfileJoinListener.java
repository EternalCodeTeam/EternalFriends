package com.eternalcode.friends.profile;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ProfileJoinListener implements Listener {

    private final ProfileManager profileManager;

    public ProfileJoinListener(ProfileManager manager){
        this.profileManager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (this.profileManager.getProfileByUUID(player.getUniqueId()).isPresent()) {
            return;
        }

        this.profileManager.createProfile(player.getUniqueId());
    }
}
