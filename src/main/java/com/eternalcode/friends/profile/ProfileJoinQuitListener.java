package com.eternalcode.friends.profile;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.eternalcode.friends.packet.WrapperPlayServerScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Optional;

public class ProfileJoinQuitListener implements Listener {

    private final ProfileManager profileManager;
    private final ProtocolManager protocolManager;

    public ProfileJoinQuitListener(ProfileManager manager, ProtocolManager protocolManager){
        this.profileManager = manager;
        this.protocolManager = protocolManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (this.profileManager.getProfileByUUID(player.getUniqueId()).isEmpty()) {
            this.profileManager.createProfile(player.getUniqueId());
        }

        //sending ScoreboardTeam packet to update color of overhead player name
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            if (player.getUniqueId().equals(onlinePlayer.getUniqueId())) {
               continue;
            }

            Optional<Profile> onlinePlayerProfileOptional = this.profileManager.getProfileByUUID(onlinePlayer.getUniqueId());
            if (onlinePlayerProfileOptional.isEmpty()) {
                continue;
            }

            Profile onlinePlayerProfile = onlinePlayerProfileOptional.get();

            if (!onlinePlayerProfile.isFriendWith(player.getUniqueId())) {
                this.protocolManager.sendServerPacket(onlinePlayer, nonFriendTeamScoreboardPacket(player));
                this.protocolManager.sendServerPacket(player, nonFriendTeamScoreboardPacket(onlinePlayer));

                continue;
            }

            this.protocolManager.sendServerPacket(onlinePlayer, friendTeamScoreboardPacket(player));
            this.protocolManager.sendServerPacket(player, friendTeamScoreboardPacket(onlinePlayer));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        //removing player's scoreboard team
        PacketContainer packet = new WrapperPlayServerScoreboardTeam()
                .setName(event.getPlayer().getName())
                .setMode(1)
                .getHandle();

        this.protocolManager.broadcastServerPacket(packet);
    }

    private PacketContainer friendTeamScoreboardPacket(Player player) {
        return new WrapperPlayServerScoreboardTeam()
                .setName(player.getName())
                .setMode(0)
                .setPlayers(List.of(player.getName()))
                .setNameTagVisibility("always")
                .setFriendColor()
                .getHandle();
    }

    private PacketContainer nonFriendTeamScoreboardPacket(Player player) {
        return new WrapperPlayServerScoreboardTeam()
                .setName(player.getName())
                .setMode(0)
                .setPlayers(List.of(player.getName()))
                .setNameTagVisibility("always")
                .setColor(ChatColor.WHITE)
                .getHandle();
    }
}
