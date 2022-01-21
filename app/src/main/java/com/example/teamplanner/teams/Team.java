package com.example.teamplanner.teams;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Objects;

//Class Duty with all the attributes, getters, setters and constructor
public class Team implements Serializable {
    public static final String TEAM_KEY="team_key";

    private Long id;
    private String team_name;
    private String sport;

    public Team(Long id, String team_name, String sport) {
        this.id=id;
        this.team_name=team_name;
        this.sport=sport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", team_name='" + team_name + '\'' +
                ", sport='" + sport + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
