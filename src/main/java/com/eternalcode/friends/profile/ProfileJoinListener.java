package com.eternalcode.friends.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class ProfileJoinListener implements Listener {

    private final ProfileManager profileManager;

    public ProfileJoinListener(ProfileManager manager){
        this.profileManager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if (profileManager.hasProfile(player.getUniqueId())) {
            return;
        }

        profileManager.createProfile(player.getUniqueId());

    }
}
