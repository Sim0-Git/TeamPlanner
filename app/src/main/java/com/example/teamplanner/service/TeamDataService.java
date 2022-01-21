package com.example.teamplanner.service;

import android.content.Context;

import com.example.teamplanner.database.MemoSportDatabaseHelper;
import com.example.teamplanner.teams.Team;

import java.util.List;

public class TeamDataService {
    private MemoSportDatabaseHelper sqlite;

    public void init(Context context) {
        sqlite = sqlite.getInstance(context);
    }
    //INSERT A TEAM INTO THE DB AND RETURN THE ID OF THE GAME
    public Long add(Team team) {
        return sqlite.insertTeam(team.getTeam_name(),team.getSport());
    }


    //Delete a team by using the ID
    public boolean delete(Team team) {
        return sqlite.deleteTeam(team.getId());
    }

    //Update a team name, sport for a given game ID
    public boolean update(Team team) {
        return sqlite.updateTeam(team.getId(), team.getTeam_name(), team.getSport());
    }

    //Return all teams registered in the games table
    public List<Team> getTeams() {
        List<Team> teams = sqlite.getTeams();
        return teams;
    }

    //return the team id
    public  Team getTeam(Long id){
        return sqlite.getTeam(id);
    }

    //Not using this one
    public boolean updateTeam(Long id,String team_name,String sport){
        return sqlite.updateTeam(id,team_name,sport);
    }
}
