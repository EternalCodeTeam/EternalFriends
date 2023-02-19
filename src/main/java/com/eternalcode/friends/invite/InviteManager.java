package com.eternalcode.friends.invite;

import com.eternalcode.friends.config.implementation.PluginConfig;

import java.time.Duration;
import java.util.*;


public class InviteManager {

    private final PluginConfig pluginConfig;

    private final Map<UUID, List<Invite>> receivedInvites = new HashMap<>();
    private final Map<UUID, List<Invite>> sendedInvites = new HashMap<>();

    public InviteManager(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    public void addInvite(UUID from, UUID to) {
        Invite invite = new Invite(from, to, Duration.ofSeconds(pluginConfig.inviteExpirationTime));

        if (this.sendedInvites.containsKey(from)) {
            this.sendedInvites.get(from).add(invite);
        }
        else {
            this.sendedInvites.put(from, new ArrayList<>(List.of(invite)));
        }

        if (this.receivedInvites.containsKey(to)) {
            this.receivedInvites.get(to).add(invite);
        }
        else {
            this.receivedInvites.put(to, new ArrayList<>(List.of(invite)));
        }
    }

    public boolean hasSendedInvite(UUID from, UUID to) {
        if (!this.sendedInvites.containsKey(from)) {
            return false;
        }

        return this.sendedInvites.get(from).stream().filter(invite -> invite.getTo().equals(to)).count() > 0;
    }

    public boolean hasReceivedInvite(UUID from, UUID to) {
        if (!this.receivedInvites.containsKey(to)) {
            return false;
        }

        return this.receivedInvites.get(to).stream().filter(invite -> invite.getFrom().equals(from)).count() > 0;
    }

    public boolean isInviteExpired(UUID from, UUID to) {
        if (!this.sendedInvites.containsKey(from)) {
            return true;
        }
        if (!this.receivedInvites.containsKey(to)) {
            return true;
        }

        Optional<Invite> inviteOptional = this.sendedInvites.get(from).stream().filter(i -> i.getTo().equals(to)).findFirst();
        if (inviteOptional.isEmpty()) {
            return true;
        }

        Invite invite = inviteOptional.get();
        return invite.isExpired();
    }

    public void removeInvite(UUID from, UUID to) {
        if (hasReceivedInvite(from, to)) {
            this.receivedInvites.get(to).removeIf(invite -> invite.getFrom().equals(from));
        }
        if (hasSendedInvite(from, to)) {
            this.sendedInvites.get(from).removeIf(invite -> invite.getTo().equals(to));
        }
    }

    public List<Invite> getReceivedInvites(UUID uuid) {
        return this.receivedInvites.get(uuid);
    }
}
