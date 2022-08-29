package com.eternalcode.friends.profile;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfileManager {

    private List<Profile> profileList = new ArrayList<>();

    public ProfileManager(){
        //loading profile data from database

        //saving profile data to database
    }

    public void addProfile(UUID uuid){
        profileList.add(new Profile(uuid));
    }

    public Profile getProfileByUUID(UUID uuid){
        for(Profile profile : profileList){
            if(profile.getUuid().toString().equalsIgnoreCase(uuid.toString())){
                return profile;
            }
        }
        return null;
    }

    public boolean hasProfile(UUID uuid){
        for(Profile profile : profileList){
            if(profile.getUuid().toString().equalsIgnoreCase(uuid.toString())){
                return true;
            }
        }
        return false;
    }
}
