package com.example.teamplanner.service;

import android.content.Context;

import com.example.teamplanner.database.MemoSportDatabaseHelper;
import com.example.teamplanner.profile.Profile;
import com.example.teamplanner.teams.Team;

import java.util.List;

public class ProfileDataService {
    private MemoSportDatabaseHelper sqlite;
    public void init(Context context) {
        sqlite = sqlite.getInstance(context);
    }

    //Add a new user profile
    public Long add(Profile profile) {
        return sqlite.insertProfile(profile.getUserName(),profile.getBirthday(),profile.getJerseyNumber(),profile.getPositionPlayed());
    }
    //Update a profile user name, birthday, jersey number and position played for a given game ID
    public boolean update(Profile profile){
        return sqlite.updateProfile(profile.getId(), profile.getUserName(), profile.getBirthday(),profile.getJerseyNumber(),profile.getPositionPlayed());
    }

    public List<Profile> getProfiles() {
        List<Profile> profiles = sqlite.getProfiles();
        return profiles;
    }

    //return the profile id
    public  Profile getProfile(Long id){
        return sqlite.getProfile(id);
    }
}
