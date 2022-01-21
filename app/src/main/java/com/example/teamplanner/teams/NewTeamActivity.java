package com.example.teamplanner.teams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.teamplanner.R;
import com.example.teamplanner.service.TeamDataService;
import com.google.android.material.snackbar.Snackbar;

public class NewTeamActivity extends AppCompatActivity {

    EditText teamNameEditTxt;
    TextView sportNameEditTxt;
    private TeamDataService teamDataService;
    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        teamDataService=new TeamDataService();
        teamDataService.init(this);
        teamDataService.getTeams();

        teamNameEditTxt=findViewById(R.id.teamNameEditTxt);
        sportNameEditTxt=findViewById(R.id.sportTypeEditTxt);

        //Popup menu for the Event Type
        sportNameEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSportPopupMenu(v);
            }
        });

        //Button discard and create setup
        Button backButton=findViewById(R.id.discardTeamButton);
        Button createTeamButton= findViewById(R.id.createTeamButton);
        backButton.setBackground(backButtonSetup());//Set button shape
        createTeamButton.setBackground(createButtonSetup());

        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTeam(v);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardTeam(v);
            }
        });
    }

    private void discardTeam(View v) {
        Intent sendTeam= new Intent();
        sendTeam.putExtra(Team.TEAM_KEY,"null");
        setResult(RESULT_CANCELED,sendTeam);
        finish();
    }

    private void createNewTeam(View v) {
        String teamName=teamNameEditTxt.getText().toString();
        String sportType=sportNameEditTxt.getText().toString();


        if(teamName.trim().isEmpty()){
            Snackbar.make(v,"Team name cannot be empty",Snackbar.LENGTH_LONG).show();
            teamNameEditTxt.getText().clear();
            teamNameEditTxt.requestFocus();
            return;
        }
        if(sportType.trim().isEmpty()){
            Snackbar.make(v,"Sport type cannot be empty",Snackbar.LENGTH_LONG).show();
            sportNameEditTxt.setText(" ");
            sportNameEditTxt.requestFocus();
            return;
        }

        //Set the Team constructor
        Team team=new Team(null,teamName,sportType);

        //Send back from this activity the key and the team object
        Intent sendTeam= new Intent();
        sendTeam.putExtra(Team.TEAM_KEY,team);
        setResult(RESULT_OK,sendTeam);
        finish();

        //
        Intent sendTeamToRecyclerItem= new Intent(NewTeamActivity.this, RecyclerItemTeamActivity.class);
        sendTeamToRecyclerItem.putExtra("Team",team);
    }

    private void showSportPopupMenu(View v) {
        popupMenu=new PopupMenu(NewTeamActivity.this,v);
        popupMenu.getMenu().add("Empty");
        popupMenu.getMenu().add("Volleyball");
        popupMenu.getMenu().add("Soccer");
        popupMenu.getMenu().add("Basketball");
        popupMenu.getMenu().add("Other");
        popupMenu.show();
        //Set the editText with the user choice after clicking an option in the menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sportNameEditTxt.setText(item.getTitle());
                if(sportNameEditTxt.getText()=="Empty"){
                    sportNameEditTxt.setText("");
                }
                else
                    sportNameEditTxt.setText(item.getTitle());
                return false;
            }
        });
    }

    private Drawable backButtonSetup() {
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