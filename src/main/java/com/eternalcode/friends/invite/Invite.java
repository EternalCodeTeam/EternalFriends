package com.eternalcode.friends.invite;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Invite {

    private final UUID from;
    private final UUID to;
    private final Instant expirationDate;

    public Invite(UUID from, UUID to, Duration duration) {
        this.from = from;
        this.to = to;
        this.expirationDate = Instant.now().plus(duration);
    }

    public Invite(UUID from, UUID to, Instant expiretionDate) {
        this.from = from;
        this.to = to;
        this.expirationDate = expiretionDate;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expirationDate);
    }

    public UUID getFrom() {
        return this.from;
    }

    public UUID getTo() {
        return this.to;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public boolean equals(Invite invite) {
        return invite.getFrom().equals(from) && invite.getTo().equals(to);
    }
}
