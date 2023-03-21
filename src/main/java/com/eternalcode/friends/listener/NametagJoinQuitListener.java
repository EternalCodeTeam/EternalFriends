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

public class NametagJoinQuitListener implements Listener {

    private final ProtocolManager protocolManager;
    private final NameTagService nameTagService;
    private final FriendManager friendManager;

    public NametagJoinQuitListener(ProtocolManager protocolManager, NameTagService nameTagService, FriendManager friendManager) {
        this.protocolManager = protocolManager;
        this.nameTagService = nameTagService;
        this.friendManager = friendManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.sendScoreboardPacket(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.nameTagService.removePlayersTeam(event.getPlayer());
    }

    //send ScoreboardTeam packet to update color of player's nametag
    private void sendScoreboardPacket(Player player) {
        UUID playerUUID = player.getUniqueId();

        for (UUID friendUuid : this.friendManager.getFriends(playerUUID)) {
            if (playerUUID.equals(friendUuid)) {
                continue;
            }

            Player friend = player.getServer().getPlayer(friendUuid);

            if (friend == null) {
                continue;
            }

            this.nameTagService.createTeamPacketOfTwoFriends(player, friend);
        }
    }
}
