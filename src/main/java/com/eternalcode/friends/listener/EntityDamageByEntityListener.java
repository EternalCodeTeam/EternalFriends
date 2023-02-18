package com.eternalcode.friends.listener;

import com.eternalcode.friends.friend.FriendManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    private final FriendManager friendManager;

    public EntityDamageByEntityListener(FriendManager friendManager) {
        this.friendManager = friendManager;
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

        if (this.friendManager.areFriends(damager.getUniqueId(), damaged.getUniqueId())) {
            event.setDamage(0D);
        }
    }
}
