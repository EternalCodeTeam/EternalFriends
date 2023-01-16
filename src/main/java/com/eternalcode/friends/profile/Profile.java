package com.eternalcode.friends.profile;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Profile {

    private final UUID uuid;
    private final List<UUID> friends = new ArrayList<>();
    private final List<UUID> ignoredPlayers = new ArrayList<>();

    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public void addFriend(UUID uuid) {
        this.friends.add(uuid);
    }

    public void removeFriend(UUID uuid) {
        this.friends.remove(uuid);
    }

    public void addIgnoredPlayer(UUID uuid) {
        this.ignoredPlayers.add(uuid);
    }

    public void removeIgnoredPlayer(UUID uuid) {
        this.ignoredPlayers.remove(uuid);
    }

    public boolean isIgnoredPlayer(UUID uuid) {
        return this.ignoredPlayers.contains(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Profile)) {
            return false;
        }

        Profile profile = (Profile) o;

        return this.uuid.equals(profile.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

}
