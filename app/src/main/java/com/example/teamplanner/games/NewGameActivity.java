package com.example.teamplanner.games;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.teamplanner.R;
import com.example.teamplanner.service.GameDataService;
import com.example.teamplanner.service.TeamDataService;
import com.example.teamplanner.teams.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NewGameActivity extends AppCompatActivity {

    EditText dateEditText;
    EditText timeEditText;
    EditText locationEditText;
    EditText addressEditText;
    EditText opponentEditText;
    TextView eventEditTxt;
    TextView teamNameEditTxt;
    private GameDataService gameDataService;
    private TeamDataService teamDataService;
    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        gameDataService=new GameDataService();
        gameDataService.init(this);
        gameDataService.getGames();

        teamDataService=new TeamDataService();
        teamDataService.init(this);
        teamDataService.getTeams();

        //Title of the New Game form
        TextView upcomingVT=findViewById(R.id.newTeamTextView);
        //Changing single letters New Game form title
        SpannableStringBuilder spannable = new SpannableStringBuilder("NEW GAME");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),2, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),7, 8,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        upcomingVT.setText(spannable);



        //Hook all the data received from the user input
        dateEditText =findViewById(R.id.editTextDate);
        timeEditText =findViewById(R.id.editTextTime);
        locationEditText =findViewById(R.id.editTextLocation);
        addressEditText =findViewById(R.id.editTextTextPostalAddress);
        opponentEditText =findViewById(R.id.opponentEditText);
        eventEditTxt= findViewById(R.id.eventTypeTxtView);
        teamNameEditTxt=findViewById(R.id.teamTxtView);

        //Popup menu for the Event Type
        eventEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEventsPopupMenu(v);
            }
        });
        //Popup menu for the teams available
        teamNameEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeamsPopupMenu(v);
            }
        });

        //Button back and create setup
        Button backButton=findViewById(R.id.backButton);
        Button createButton= findViewById(R.id.createButton);
        backButton.setBackground(discardButtonSetup());//Set button shape
        createButton.setBackground(createButtonSetup());

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGame(v);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardGame(v);
            }
        });

        Button viewAllButton=findViewById(R.id.buttonViewAll);
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAll(v);
            }
        });

    }


    private void discardGame(View v) {
        Intent sendGame= new Intent();
        sendGame.putExtra(Game.GAME_KEY,"null");
        setResult(RESULT_CANCELED,sendGame);
        finish();
    }
    private void createNewGame(View v) {
        String team_name=teamNameEditTxt.getText().toString();
        String date=dateEditText.getText().toString();
        String time=timeEditText.getText().toString();
        String location=locationEditText.getText().toString();
        String address=addressEditText.getText().toString();
        String opponent=opponentEditText.getText().toString();
        String eventType=eventEditTxt.getText().toString();

        if(date.trim().isEmpty()){
            Snackbar.make(v,"Date cannot be empty",Snackbar.LENGTH_LONG).show();
            dateEditText.getText().clear();
            dateEditText.requestFocus();
            return;
        }
        if(time.trim().isEmpty()){
            Snackbar.make(v,"Time cannot be empty",Snackbar.LENGTH_LONG).show();
            timeEditText.getText().clear();
            timeEditText.requestFocus();
            return;
        }
        if(location.trim().isEmpty()){
            Snackbar.make(v,"Location cannot be empty",Snackbar.LENGTH_LONG).show();
            locationEditText.getText().clear();
            locationEditText.requestFocus();
            return;
        }
        if(address.trim().isEmpty()){
            Snackbar.make(v,"Address cannot be empty",Snackbar.LENGTH_LONG).show();
            addressEditText.getText().clear();
            addressEditText.requestFocus();
            return;
        }
        if(opponent.trim().isEmpty()){
            Snackbar.make(v,"Opponent cannot be empty",Snackbar.LENGTH_LONG).show();
            opponentEditText.getText().clear();
            opponentEditText.requestFocus();
            return;
        }
        //Set the Game constructor
        Game game=new Game(null,team_name,time,date,location,address,opponent,eventType);

        //Send back from this activity the key and the game object
        Intent sendGame= new Intent();
        sendGame.putExtra(Game.GAME_KEY,game);
        setResult(RESULT_OK,sendGame);
        finish();

        //
        Intent sendGameToRecyclerItem= new Intent(NewGameActivity.this, RecyclerItemActivity.class);
        sendGameToRecyclerItem.putExtra("Game",game);

    }

    //      METHOD TO VIEW ALL THE GAMES, NOT USEFUL HERE
    private void viewAll(View v) {
        List<Game> gameList= gameDataService.getGames();
        String text="";
        if(gameList.size()>0){
            for(Game game: gameList){
                text= text.concat(game.toString()+gameList.indexOf(game)+"\n");
            }
            showMessage("Data",text);
        }else{
            showMessage("Records","Nothing found");
        }

    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show(); }

    //Method that shows a popup menu for the user to choose an option
    private void showEventsPopupMenu(View v) {
        popupMenu=new PopupMenu(NewGameActivity.this,v);
        popupMenu.getMenu().add("Empty");
        popupMenu.getMenu().add("Practice match");
        popupMenu.getMenu().add("Competition match");
        popupMenu.getMenu().add("School Event");
        popupMenu.getMenu().add("Other");
        popupMenu.show();
        //Set the editText with the user choice after clicking and option in hte menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                eventEditTxt.setText(item.getTitle());
                if(eventEditTxt.getText()=="Empty"){
                    eventEditTxt.setText("");
                }
                else
                     eventEditTxt.setText(item.getTitle());
                return false;
            }
        });
    }

    //Method that shows a popup menu for the teams available
    private void showTeamsPopupMenu(View v) {
        List<Team> teamList= teamDataService.getTeams();
        popupMenu=new PopupMenu(NewGameActivity.this,v);
        for(Team team: teamList){
            popupMenu.getMenu().add(team.getTeam_name());
        }
        popupMenu.show();
        //Set the editText with the user choice after clicking and option in hte menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                teamNameEditTxt.setText(item.getTitle());
                return false;
            }
        });
    }

    //Method to set shape, color and opacity of the discard button
    private Drawable discardButtonSetup() {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                25, 25, 25, 25,
                25, 25, 25, 25
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor("#E53615"));
        shapeDrawable.getPaint().setAlpha(245);
        return shapeDrawable;
    }

    //Method to set shape, color and opacity of the create button
    private Drawable createButtonSetup() {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                25, 25, 25, 25,
                25, 25, 25, 25
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor("#6BBBD9"));
        shapeDrawable.getPaint().setAlpha(245);
        return shapeDrawable;
    }
}