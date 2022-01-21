package com.example.teamplanner.recycler_view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamplanner.R;
import com.example.teamplanner.games.Game;
import com.example.teamplanner.listener.OnGameListener;
import com.example.teamplanner.listener.OnTeamListener;
import com.example.teamplanner.teams.Team;

public class TeamViewHolder extends RecyclerView.ViewHolder {

    public final TextView teamName;
    public final TextView sportType;

    private OnTeamListener onTeamListener;
    private Button onButtonListener;

    public TeamViewHolder(@NonNull View itemView, OnTeamListener onTeamListener) {
        super(itemView);

        teamName=itemView.findViewById(R.id.teamNameText);
        sportType=itemView.findViewById(R.id.sportTypeText);

        this.onTeamListener=onTeamListener;

        onButtonListener=itemView.findViewById(R.id.editTeamButton);
    }

    public void loadTeam(Team team){

        this.teamName.setText(team.getTeam_name());
        this.sportType.setText(team.getSport());
    }

    //This method gets activated when the button is pressed instead of all the item view
    public void bindTeamByButton(Team team, OnTeamListener onTeamListener){
        this.onButtonListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTeamListener.onTeamClick(team);
            }
        });

    }
}
