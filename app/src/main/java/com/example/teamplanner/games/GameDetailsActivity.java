package com.example.teamplanner.games;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.teamplanner.R;
import com.example.teamplanner.constants.Constants;
import com.example.teamplanner.recycler_view.GameRecyclerViewAdapter;
import com.example.teamplanner.service.GameDataService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class GameDetailsActivity extends AppCompatActivity {

    Game game;
    EditText editTextDateDetails;
    EditText editTextTimeDetails;
    EditText editTextLocationDetails;
    EditText editTextAddressDetails;
    EditText editTextTextOpponentDetail;
    EditText editTextEventTypeDetails;
    TextView textViewEventTypeDetails;

    Long id;
    String matchName;
    String date;
    String time;
    String location;
    String address;
    String eventType;
    String opponent;

    Button updateButton;
    Button deleteButton;
    PopupMenu popupMenu;

    GameDataService gameDataService;
    View rootView;

    GameRecyclerViewAdapter gameRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        rootView=findViewById(android.R.id.content).getRootView();

        gameDataService=new GameDataService();
        gameDataService.init(this);
        gameDataService.getGames();

        editTextDateDetails=findViewById(R.id.editTextDateDetails);
        editTextTimeDetails=findViewById(R.id.editTextTimeDetails);
        editTextLocationDetails=findViewById(R.id.editTextLocationDetails);
        editTextAddressDetails=findViewById(R.id.editTextaddressDetails);
        editTextTextOpponentDetail=findViewById(R.id.editTextTextOpponentDetail);
        editTextEventTypeDetails=findViewById(R.id.editTexteventTypeDetails);
        textViewEventTypeDetails=findViewById(R.id.editTexteventTypeDetails);

        updateButton= findViewById(R.id.updateButtonDetail);
        deleteButton= findViewById(R.id.deleteButtonDetail);


        Intent intentThatCalled=getIntent();
        if(intentThatCalled.hasExtra(Game.GAME_KEY)){
            game=(Game)intentThatCalled.getSerializableExtra(Game.GAME_KEY);

            id=game.getId();
            matchName=game.getMatchName();
            editTextTextOpponentDetail.setText(game.getOpponent());
            editTextDateDetails.setText(game.getDate());
            editTextTimeDetails.setText(game.getTime());
            editTextLocationDetails.setText(game.getLocation());
            editTextAddressDetails.setText(game.getAddress());
            editTextEventTypeDetails.setText(game.getEventType());
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentWithData();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentWithData();
            }
        });

        Button viewAllButton=findViewById(R.id.viewAllDetails);
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAll(v);
            }
        });

        editTextEventTypeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEventsPopupMenu(v);
            }
        });

    }
    private void showEventsPopupMenu(View v) {
        popupMenu=new PopupMenu(GameDetailsActivity.this,v);
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
                textViewEventTypeDetails.setText(item.getTitle());
                if(textViewEventTypeDetails.getText()=="Empty"){
                    textViewEventTypeDetails.setText("");
                }
                else
                    textViewEventTypeDetails.setText(item.getTitle());
                return false;
            }
        });
    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setIntentWithData() {
        date= editTextDateDetails.getText().toString();
        time= editTextTimeDetails.getText().toString();
        location= editTextLocationDetails.getText().toString();
        address= editTextAddressDetails.getText().toString();
        eventType= editTextEventTypeDetails.getText().toString();
        opponent= editTextTextOpponentDetail.getText().toString();

        game=new Game(id,matchName,time,date,location,address,opponent,eventType);

        Intent goBack= new Intent();
        goBack.putExtra(Game.GAME_KEY,game);


        if(updateButton.isPressed()){
            String date=editTextDateDetails.getText().toString();
            String time=editTextTimeDetails.getText().toString();
            String location=editTextLocationDetails.getText().toString();
            String address=editTextAddressDetails.getText().toString();
            String opponent=editTextTextOpponentDetail.getText().toString();

            if(date.trim().isEmpty()){
                Snackbar.make(rootView, "Date cannot be empty!", Snackbar.LENGTH_LONG).show();
                editTextDateDetails.getText().clear();
                editTextDateDetails.requestFocus();
                return;
            }
            if(time.trim().isEmpty()){
                Snackbar.make(rootView, "Time type cannot be empty!", Snackbar.LENGTH_LONG).show();
                editTextTimeDetails.getText().clear();
                editTextTimeDetails.requestFocus();
                return;
            }
            if(location.trim().isEmpty()){
                Snackbar.make(rootView, "Location type cannot be empty!", Snackbar.LENGTH_LONG).show();
                editTextLocationDetails.getText().clear();
                editTextLocationDetails.requestFocus();
                return;
            }
            if(address.trim().isEmpty()){
                Snackbar.make(rootView, "Address type cannot be empty!", Snackbar.LENGTH_LONG).show();
                editTextAddressDetails.getText().clear();
                editTextAddressDetails.requestFocus();
                return;
            }
            if(opponent.trim().isEmpty()){
                Snackbar.make(rootView, "Opponent type cannot be empty!", Snackbar.LENGTH_LONG).show();
                editTextTextOpponentDetail.getText().clear();
                editTextTextOpponentDetail.requestFocus();
                return;
            }
            setResult(Constants.MODIFY_GAME_DETAILS_ACTIVITY_CODE,goBack);
            finish();
        }else if(deleteButton.isPressed()){
            setResult(Constants.DELETE_GAME_DETAILS_ACTIVITY_CODE,goBack);
            finish();
        }
    }
}