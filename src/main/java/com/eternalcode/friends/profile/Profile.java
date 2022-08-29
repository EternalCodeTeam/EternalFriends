package com.eternalcode.friends.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Profile {

    private final UUID uuid;
    private final List<UUID> friends = new ArrayList<>();

    private boolean receiveInvites;
    private boolean friendsJoinNotification;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.receiveInvites = true;
        this.friendsJoinNotification = true;
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

    public boolean isReceiveInvites() {
        return receiveInvites;
    }

    public void setReceiveInvites(boolean receiveInvites) {
        this.receiveInvites = receiveInvites;
    }

    public boolean isFriendsJoinNotification() {
        return friendsJoinNotification;
    }

    public void setFriendsJoinNotification(boolean friendsJoinNotification) {
        this.friendsJoinNotification = friendsJoinNotification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;
        Profile profile = (Profile) o;
        return uuid.equals(profile.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

}
