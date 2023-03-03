package com.eternalcode.friends.friend;

import com.eternalcode.friends.database.FriendDatabaseService;
import com.eternalcode.friends.database.IgnoredPlayerDatabaseService;
import com.eternalcode.friends.invite.InviteManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class FriendManager {

    private final FriendDatabaseService friendDatabaseService;
    private final IgnoredPlayerDatabaseService ignoredPlayerDatabaseService;
    private final InviteManager inviteManager;

    private Map<UUID, List<UUID>> friends = new HashMap<>();
    private Map<UUID, List<UUID>> ignoredPlayers = new HashMap<>();

    public FriendManager(FriendDatabaseService friendDatabaseService, IgnoredPlayerDatabaseService ignoredPlayerDatabaseService, InviteManager inviteManager) {
        this.friendDatabaseService = friendDatabaseService;
        this.ignoredPlayerDatabaseService = ignoredPlayerDatabaseService;
        this.inviteManager = inviteManager;

         this.friendDatabaseService.load(this.friends);
         this.ignoredPlayerDatabaseService.load(this.ignoredPlayers);
    }

    public void addFriends(UUID uuid, UUID friendUUID) {
        checkPlayersExists(uuid, friendUUID);

        this.friends.get(uuid).add(friendUUID);
        this.friends.get(friendUUID).add(uuid);

        this.friendDatabaseService.add(uuid, friendUUID);

        this.inviteManager.removeInvite(uuid, friendUUID);
    }

    public void removeFriends(UUID uuid, UUID friendUUID) {
        checkPlayersExists(uuid, friendUUID);

        this.friends.get(uuid).remove(friendUUID);
        this.friends.get(friendUUID).remove(uuid);

        this.friendDatabaseService.remove(uuid, friendUUID);
    }

    public void addIgnoredPlayer(UUID uuid, UUID ignoredPlayerUUID) {
        checkPlayersExists(uuid, ignoredPlayerUUID);

        this.ignoredPlayers.get(uuid).add(ignoredPlayerUUID);

        this.ignoredPlayerDatabaseService.addIgnoredPlayer(uuid, ignoredPlayerUUID);
    }

    public void removeIgnoredPlayer(UUID uuid, UUID ignoredPlayerUUID) {
        checkPlayersExists(uuid, ignoredPlayerUUID);

        this.ignoredPlayers.get(uuid).remove(ignoredPlayerUUID);

        this.ignoredPlayerDatabaseService.removeIgnoredPlayer(uuid, ignoredPlayerUUID);
    }

    public List<UUID> getFriends(UUID uuid) {
        checkPlayerExists(uuid);

        return this.friends.get(uuid);
    }

    public boolean areFriends(UUID uuid, UUID friendUUID) {
        checkPlayersExists(uuid, friendUUID);

        return this.friends.get(uuid).contains(friendUUID);
    }

    public boolean isIgnoredByPlayer(UUID uuid, UUID playerUUID) {
        checkPlayersExists(uuid, playerUUID);

        return this.ignoredPlayers.get(playerUUID).contains(uuid);
    }

    private void checkPlayerExists(UUID uuid) {
        if (!this.friends.containsKey(uuid)) {
            this.friends.put(uuid, new ArrayList<>());
        }
        if (!this.ignoredPlayers.containsKey(uuid)) {
            this.ignoredPlayers.put(uuid, new ArrayList<>());
        }
    }

    private void checkPlayersExists(UUID... uuids) {
        for (UUID uuid : uuids) {
            checkPlayerExists(uuid);
        }
    }


}
