package com.eternalcode.friends.invite;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Invite {
    private final UUID from;
    private final UUID to;
    private final Duration duration;
    private final Instant expiretionDate;
    public Invite(UUID from, UUID to, Duration duration) {
        this.from = from;
        this.to = to;
        this.duration = duration;
        this.expiretionDate = Instant.now().plus(duration);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiretionDate);
    }

    public UUID getFrom() {
        return from;
    }

    public UUID getTo() {
        return to;
    }

    public Duration getDuration() {
        return duration;
    }
    public boolean equals(Invite invite) {
        return invite.getFrom().equals(from) && invite.getTo().equals(to);
    }
}
