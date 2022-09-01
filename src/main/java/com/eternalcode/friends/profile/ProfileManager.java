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

        profiles.put(uuid, profile);

        return profile;
    }

    public Optional<Profile> getProfileByUUID(UUID uuid) {
        return profiles.values().stream().filter(profile -> profile.getUuid().toString().equalsIgnoreCase(uuid.toString())).findFirst();
    }

    public boolean hasProfile(UUID uuid) {
        return profiles.containsKey(uuid);
    }

}
