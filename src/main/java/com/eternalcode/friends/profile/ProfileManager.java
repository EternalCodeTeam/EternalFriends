package com.eternalcode.friends.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    private final ProfileRepository profileRepository;
    private final Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileManager(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile createProfile(UUID uuid) {
        Profile profile = new Profile(uuid);

        profiles.put(uuid, profile);

        return profile;
    }

    public Profile getProfileByUUID(UUID uuid) {
        return profiles.get(uuid);
    }

    public boolean hasProfile(UUID uuid) {
        return profiles.containsKey(uuid);
    }

}
