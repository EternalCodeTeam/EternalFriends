package com.eternalcode.friends.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FriendManager {

    private Map<UUID, List<UUID>> friends = new HashMap<>();
    private Map<UUID, List<UUID>> ignoredPlayers = new HashMap<>();

    public void addFriends(UUID uuid, UUID friendUUID) {
        checkPlayersExists(uuid, friendUUID);

        this.friends.get(uuid).add(friendUUID);
        this.friends.get(friendUUID).add(uuid);
    }

    public void removeFriends(UUID uuid, UUID friendUUID) {
        checkPlayersExists(uuid, friendUUID);

        this.friends.get(uuid).remove(friendUUID);
        this.friends.get(friendUUID).remove(uuid);
    }

    public void addIgnoredPlayer(UUID uuid, UUID ignoredPlayerUUID) {
        checkPlayersExists(uuid, ignoredPlayerUUID);

        this.ignoredPlayers.get(uuid).add(ignoredPlayerUUID);
    }

    public void removeIgnoredPlayer(UUID uuid, UUID ignoredPlayerUUID) {
        checkPlayersExists(uuid, ignoredPlayerUUID);

        this.ignoredPlayers.get(uuid).remove(ignoredPlayerUUID);
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
