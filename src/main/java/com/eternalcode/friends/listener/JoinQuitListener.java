package com.eternalcode.friends.listener;

import com.comphenix.protocol.ProtocolManager;
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

    public JoinQuitListener(ProtocolManager protocolManager, NameTagService nameTagService, FriendManager friendManager) {
        this.protocolManager = protocolManager;
        this.nameTagService = nameTagService;
        this.friendManager = friendManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UUID playerUUID = player.getUniqueId();

        //sending ScoreboardTeam packet to update color of overhead player name
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            UUID onlinePlayerUUID = onlinePlayer.getUniqueId();

            if (playerUUID.equals(onlinePlayerUUID)) {
               continue;
            }

            if (!this.friendManager.areFriends(onlinePlayerUUID, playerUUID)) {
                this.nameTagService.createTeamPacketOfTwoNoFriends(player, onlinePlayer);

                continue;
            }

            this.nameTagService.createTeamPacketOfTwoFriends(player, onlinePlayer);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.nameTagService.removePlayersTeam(event.getPlayer());
    }


}
