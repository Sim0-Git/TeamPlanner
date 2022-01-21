package com.example.teamplanner.games;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teamplanner.R;
import com.example.teamplanner.service.GameDataService;

public class RecyclerItemActivity extends AppCompatActivity {

    private GameDataService gameDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_game_view);

        gameDataService=new GameDataService();
        gameDataService.init(this);
        gameDataService.getGames();

    }

}
