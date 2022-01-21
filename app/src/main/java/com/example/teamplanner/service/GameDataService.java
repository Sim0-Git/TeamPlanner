package com.example.teamplanner.service;

import android.content.Context;

import com.example.teamplanner.games.Game;
import com.example.teamplanner.database.MemoSportDatabaseHelper;

import java.util.List;

public class GameDataService {
    private MemoSportDatabaseHelper sqlite;

    public void init(Context context) {
        sqlite = sqlite.getInstance(context);
    }

    //INSERT A GAME INTO THE DB AND RETURN THE ID OF THE GAME
    public Long add(Game game) {
        return sqlite.insertGame(game.getMatchName(),game.getTime(),game.getDate(),game.getLocation(),game.getAddress(),game.getOpponent(),game.getEventType());
    }


    //Delete a game by using the ID

    public boolean delete(Game game) {
        return sqlite.deleteGame(game.getId());
    }/*
    public boolean delete(Long id) {
        return sqlite.delete(id);
    }*/

    //Update a game name, date,time,location,address,event type for a given game ID

    public boolean update(Game game) {
        return sqlite.updateGame(game.getId(), game.getMatchName(), game.getTime(),game.getDate(),game.getLocation(),game.getAddress(),game.getOpponent(),game.getEventType());
    }
    /*
    public boolean update(Long id,String matchGame,String date,String time, String location, String address,String opponent, String eventType) {
        return sqlite.update(id,matchGame,date,time,location,address,opponent,eventType);
    }*/
    //Return all games registered in the games table
    public List<Game> getGames() {
        List<Game> games = sqlite.getGames();
        return games;
    }


    //return the game id
    public  Game getGame(Long id){
        return sqlite.getGame(id);
    }

}
