package com.eternalcode.friends.profile;

import java.util.*;

public class Profile {

    private final UUID uuid;
    private final List<UUID> friends = new ArrayList<>();

    //Map where key is uuid of player who invite when value is uuid of player who was invited
    private final Map<UUID, UUID> invites = new HashMap<>();

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

    public List<UUID> getReceivedInvites(){
        List<UUID> list = new ArrayList<>();
        for(Map.Entry<UUID, UUID> uuid :invites.entrySet()) {
            if(!uuid.getKey().toString().equalsIgnoreCase(this.uuid.toString())) continue;
            list.add(uuid.getValue());
        }
        return list;
    }

    public List<UUID> getSendedInvites(){
        List<UUID> list = new ArrayList<>();
        for(Map.Entry<UUID, UUID> uuid :invites.entrySet()) {
            if(uuid.getKey().toString().equalsIgnoreCase(this.uuid.toString())) continue;
            list.add(uuid.getValue());
        }
        return list;
    }

    public void removeInviteFrom(UUID uuid){
        for(Map.Entry<UUID, UUID> entry : invites.entrySet()) {
            if(!entry.getKey().toString().equalsIgnoreCase(this.uuid.toString())) continue;
            invites.remove(entry.getKey());
        }
    }

    public void removeInviteTo(UUID uuid){
        for(Map.Entry<UUID, UUID> entry : invites.entrySet()) {
            if(!entry.getValue().toString().equalsIgnoreCase(this.uuid.toString())) continue;
            invites.remove(entry.getKey(), entry.getValue());
        }
    }



    public void sendInviteTo(UUID uuid) {
        invites.put(this.uuid, uuid);
    }

    public void receiveInviteFrom(UUID uuid) {
        invites.put(uuid, this.uuid);
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
