package com.eternalcode.friends.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ProfileManager {

    private final ProfileRepository profileRepository;
    private final Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileManager(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile createProfile(UUID uuid) {
        Profile profile = new Profile(uuid);

        this.profiles.put(uuid, profile);

        return profile;
    }

    public Optional<Profile> getProfileByUUID(UUID uuid) {
        return Optional.ofNullable(this.profiles.get(uuid));
    }

    public boolean hasProfile(UUID uuid) {
        return profiles.containsKey(uuid);
    }
}
