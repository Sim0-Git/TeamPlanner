package com.example.teamplanner.teams;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.teamplanner.R;
import com.example.teamplanner.service.TeamDataService;

public class RecyclerItemTeamActivity extends AppCompatActivity {

    private TeamDataService teamDataService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_team_view);

        teamDataService = new TeamDataService();
        teamDataService.init(this);
        teamDataService.getTeams();
    }
}
