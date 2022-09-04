package com.eternalcode.friends.invite;

import org.bukkit.Bukkit;

import java.util.*;

public class InviteManager {

    private Map<UUID, List<UUID>> receivedInvites = new HashMap<>();
    private Map<UUID, List<UUID>> sendedInvites = new HashMap<>();

    public void addInvite(UUID from, UUID to) {
        if (!sendedInvites.containsKey(from)) {
            sendedInvites.put(from, Arrays.asList(to));
        }
        else {
            sendedInvites.get(from).add(to);
        }
        if (!receivedInvites.containsKey(to)) {
            receivedInvites.put(to, Arrays.asList(from));
        }
        else {
            receivedInvites.get(to).add(from);
        }
    }

    public boolean hasSendedInvite(UUID from, UUID to) {
        if (!sendedInvites.containsKey(from)) return false;
        if (sendedInvites.get(from).contains(to))return true;
        return false;
    }

    public boolean hasReceivedInvite(UUID from, UUID to) {
        if (!receivedInvites.containsKey(to)) return false;
        if (receivedInvites.get(to).contains(from)) return true;
        return false;
    }

    public void removeInvite(UUID from, UUID to) {
        if (receivedInvites.containsKey(to)) {
            List<UUID> uuids = receivedInvites.get(to);
            if (uuids.contains(from)) {
                List<UUID> list = new ArrayList(uuids);
                list.remove(from);
                receivedInvites.replace(to, list);
            }
        }
        if (sendedInvites.containsKey(from)) {
            List<UUID> uuids = sendedInvites.get(from);
            if (uuids.contains(to)) {
                List<UUID> list = new ArrayList(uuids);
                list.remove(to);
                sendedInvites.replace(from, list);
            }
        }
    }

}
