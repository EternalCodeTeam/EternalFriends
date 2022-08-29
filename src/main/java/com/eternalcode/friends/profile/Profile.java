package com.eternalcode.friends.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile {

    private UUID uuid;
    private List<UUID> friends = new ArrayList<>();
    private boolean receiveInvites, friendsJoinNotification;

    public Profile(UUID uuid){
        this.receiveInvites = true;
        this.friendsJoinNotification = true;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
}
