package com.eternalcode.friends.listener;

import com.eternalcode.friends.profile.ProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    private final ProfileManager profileManager;

    public EntityDamageByEntityListener(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        event.getCause();

        this.profileManager.getProfileByUUID(damager.getUniqueId()).ifPresent(profile -> {
            if (profile.getFriends().contains(damaged.getUniqueId())) {
                event.setDamage(0D);
            }
        });
    }
}
