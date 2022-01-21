package com.example.teamplanner.profile;

import java.io.Serializable;

//Class Duty with all the attributes, getters, setters and constructor
public class Profile implements Serializable {

    public static final String PROFILE_KEY="profile_key";

    private Long id;
    private String userName;
    private String birthday;
    private String jerseyNumber;
    private String positionPlayed;

    public Profile(Long id,String userName, String birthday, String jerseyNumber, String positionPlayed) {
        this.id=id;
        this.userName = userName;
        this.birthday = birthday;
        this.jerseyNumber = jerseyNumber;
        this.positionPlayed = positionPlayed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public String getPositionPlayed() {
        return positionPlayed;
    }

    public void setPositionPlayed(String positionPlayed) {
        this.positionPlayed = positionPlayed;
    }


}
