package com.eternalcode.friends.invite;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.*;


public class InviteManager {
    private final Plugin plugin;
    private final static Duration DEFAULT_INVITE_DURATION = Duration.ofSeconds(300);
    private Map<UUID, List<Invite>> receivedInvites = new HashMap<>();
    private Map<UUID, List<Invite>> sendedInvites = new HashMap<>();

    public InviteManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void addInvite(UUID from, UUID to) {
        Invite invite = new Invite(from, to, DEFAULT_INVITE_DURATION);
        if (sendedInvites.containsKey(from)) {
            sendedInvites.get(from).add(invite);
        }
        else {
            sendedInvites.put(from, new ArrayList<>(List.of(invite)));
        }
        if (receivedInvites.containsKey(to)) {
            receivedInvites.get(to).add(invite);
        }
        else {
            receivedInvites.put(to, new ArrayList<>(List.of(invite)));
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            removeInvite(from, to);
        }, DEFAULT_INVITE_DURATION.toSeconds() * 20);
    }

    public boolean hasSendedInvite(UUID from, UUID to) {
        if (!sendedInvites.containsKey(from)) {
            return false;
        }
        return sendedInvites.get(from).stream().filter(invite -> invite.getTo().equals(to)).count() > 0;
    }

    public boolean hasReceivedInvite(UUID from, UUID to) {
        if (!receivedInvites.containsKey(to)) {
            return false;
        }
        return receivedInvites.get(to).stream().filter(invite -> invite.getFrom().equals(from)).count() > 0;
    }

    public void removeInvite(UUID from, UUID to) {
        if (hasReceivedInvite(from, to)) {
            receivedInvites.get(to).removeIf(invite -> invite.getFrom().equals(from));
        }
        if (hasSendedInvite(from, to)) {
            sendedInvites.get(from).removeIf(invite -> invite.getTo().equals(to));
        }
    }

    public List<Invite> getReceivedInvites(UUID uuid) {
        return receivedInvites.get(uuid);
    }
}
