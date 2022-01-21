package com.example.teamplanner.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamplanner.R;
import com.example.teamplanner.games.Game;
import com.example.teamplanner.listener.OnGameListener;
import com.example.teamplanner.listener.OnTeamListener;
import com.example.teamplanner.teams.Team;

import java.util.List;

public class TeamRecyclerViewAdapter extends RecyclerView.Adapter<TeamViewHolder> {

    private List<Team> teamsList;
    private Context context;
    private OnTeamListener onTeamListener;

    public  List<Team> getTeamsList(){
        return teamsList;
    }

    public TeamRecyclerViewAdapter(List<Team> teamsList, Context context, OnTeamListener onTeamListener) {
        this.teamsList = teamsList;
        this.context = context;
        this.onTeamListener=onTeamListener;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());

        View teamView=inflater.inflate(R.layout.recycler_team_view,parent,false);

        TeamViewHolder teamViewHolder=new TeamViewHolder(teamView,onTeamListener);
        return teamViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team= teamsList.get(position);
        holder.loadTeam(team);

        holder.bindTeamByButton(team, onTeamListener);
    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }

    public void replaceTeam(int position, Team team){
        teamsList.set(position,team);
        notifyItemChanged(position);
    }
    public void deleteItemTeam(int position, Team team){
        teamsList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Team team) {
        teamsList.add(team);
        notifyItemInserted(getItemCount());
    }
}
