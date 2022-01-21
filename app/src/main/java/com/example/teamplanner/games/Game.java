package com.example.teamplanner.games;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Objects;

//Class Duty with all the attributes, getters, setters and constructor
public class Game implements Serializable {

    public static final String GAME_KEY="game_key";

    private Long id;
    private String matchName;
    private String time;
    private String date;
    private String location;
    private String address;
    private String opponent;
    private String eventType;

    public Game() {
    }

    public Game(Long id,String matchName, String time, String date, String location, String address,String opponent, String eventType) {
        this.id=id;
        this.matchName = matchName;
        this.time = time;
        this.date = date;
        this.location = location;
        this.address = address;
        this.opponent=opponent;
        this.eventType = eventType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", matchName='" + matchName + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", opponent='" + opponent + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id.equals(game.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
