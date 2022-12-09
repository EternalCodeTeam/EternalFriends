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
        this.expirationDate = Instant.now().plusSeconds(duration.toSeconds());
    }

    public UUID getFrom() {
        return from;
    }

    public UUID getTo() {
        return to;
    }

    public boolean isExpired() {
        return expirationDate.isBefore(Instant.now());
    }


    public boolean equals(Invite invite) {
        return invite.getFrom().equals(from) && invite.getTo().equals(to);
    }
}
