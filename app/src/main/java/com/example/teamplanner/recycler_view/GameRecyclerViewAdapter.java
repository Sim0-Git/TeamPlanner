package com.example.teamplanner.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamplanner.games.Game;
import com.example.teamplanner.R;
import com.example.teamplanner.listener.OnGameListener;

import java.util.List;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameViewHolder> {

    private List<Game> gamesList;
    private Context context;
    private OnGameListener onGameListener;

    public List<Game> getGames() {
        return gamesList;
    }

    public GameRecyclerViewAdapter(List<Game> gamesList, Context context, OnGameListener onGameListener) {
        this.gamesList = gamesList;
        this.context = context;
        this.onGameListener=onGameListener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());

        View gameView=inflater.inflate(R.layout.recycler_game_view,parent,false);

        GameViewHolder gameViewHolder=new GameViewHolder(gameView,onGameListener);
        return gameViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game= gamesList.get(position);
        holder.loadGame(game);

        holder.bindGameByButton(game, onGameListener);
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public void replaceGame(int position, Game game){
        gamesList.set(position,game);
        notifyItemChanged(position);
    }

    public void deleteItemGame(int position, Game game){
        gamesList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Game game) {
        gamesList.add(game);
        notifyItemInserted(getItemCount());
    }

}
