package com.eternalcode.friends.profile;

import com.comphenix.protocol.ProtocolManager;
import com.eternalcode.friends.packet.NameTagService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.UUID;

public class ProfileJoinQuitListener implements Listener {

    private final ProfileManager profileManager;
    private final ProtocolManager protocolManager;
    private final NameTagService nameTagService;

<<<<<<< HEAD
    public ProfileJoinQuitListener(ProfileManager manager, ProtocolManager protocolManager, NameTagService nameTagService){
=======
    public ProfileJoinQuitListener(ProfileManager manager, ProtocolManager protocolManager){
>>>>>>> parent of 34fe860 (Update src/main/java/com/eternalcode/friends/profile/ProfileJoinQuitListener.java)
        this.profileManager = manager;
        this.protocolManager = protocolManager;
        this.nameTagService = nameTagService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UUID playerUUID = player.getUniqueId();

        //creating player's profile if not exists
        if (this.profileManager.getProfileByUUID(playerUUID).isEmpty()) {
            this.profileManager.createProfile(playerUUID);
        }

        //sending ScoreboardTeam packet to update color of overhead player name
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            UUID onlinePlayerUUID = onlinePlayer.getUniqueId();

            if (playerUUID.equals(onlinePlayerUUID)) {
               continue;
            }

            Optional<Profile> onlinePlayerProfileOptional = this.profileManager.getProfileByUUID(onlinePlayerUUID);
            if (onlinePlayerProfileOptional.isEmpty()) {
                continue;
            }

            Profile onlinePlayerProfile = onlinePlayerProfileOptional.get();

            if (!onlinePlayerProfile.isFriendWith(playerUUID)) {
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
