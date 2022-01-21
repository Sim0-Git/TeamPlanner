package com.example.teamplanner.recycler_view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamplanner.R;
import com.example.teamplanner.games.Game;
import com.example.teamplanner.listener.OnGameListener;

public class GameViewHolder extends RecyclerView.ViewHolder {

    public final TextView matchTextView;
    public final TextView timeTextView;
    public final TextView dateTextView;
    public final TextView locationTextView;
    public final TextView addressTextView;
    public final TextView eventTypeTextView;

    private OnGameListener onGameListener;
    private Button onButtonListener;


    public GameViewHolder(@NonNull View itemView,OnGameListener onGameListener) {
        super(itemView);

        matchTextView=itemView.findViewById(R.id.matchTextView);
        timeTextView=itemView.findViewById(R.id.timeTextView);
        dateTextView=itemView.findViewById(R.id.dateTextView);
        locationTextView=itemView.findViewById(R.id.locationTextView);
        addressTextView=itemView.findViewById(R.id.addressTextView);
        eventTypeTextView=itemView.findViewById(R.id.eventTypeTextView);

        this.onGameListener=onGameListener;

        onButtonListener=itemView.findViewById(R.id.editGameButton);

    }

    public void loadGame(Game game){

        this.matchTextView.setText(game.getMatchName()+" vs "+game.getOpponent());
        this.timeTextView.setText(game.getTime());
        this.dateTextView.setText(game.getDate());
        this.locationTextView.setText(game.getLocation());
        this.addressTextView.setText(game.getAddress());
        this.eventTypeTextView.setText(game.getEventType());

    }

    //I might not use this one
    public void bindGame(Game game, OnGameListener onGameListener){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameListener.onGameClick(game);
            }
        });
    }
    //This method gets activated when the button is pressed instead of all the item view
    public void bindGameByButton(Game game, OnGameListener onGameListener){
        this.onButtonListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameListener.onGameClick(game);
            }
        });

    }

}
