package com.example.teamplanner.teams;

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
import com.example.teamplanner.service.TeamDataService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TeamDetailsActivity extends AppCompatActivity {

    Team team;
    EditText teamNameEditText;
    TextView sportTypeTextView;

    Long id;
    String teamNameString;
    String sportTypeString;

    Button updateTeam;
    Button deleteTeam;
    Button viewAllTeams;

    View rootView;
    TeamDataService teamDataService;

    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        rootView=findViewById(android.R.id.content).getRootView();

        teamDataService=new TeamDataService();
        teamDataService.init(this);
        teamDataService.getTeams();

        teamNameEditText=findViewById(R.id.editTextTeamName);
        sportTypeTextView=findViewById(R.id.sportTyepTextView);

        updateTeam=findViewById(R.id.updateTeamButtonDetail);
        deleteTeam=findViewById(R.id.deleteTeamButtonDetail);
        viewAllTeams=findViewById(R.id.viewAllTeamButtonDetails);

        Intent intentThatCalled=getIntent();
        if(intentThatCalled.hasExtra(Team.TEAM_KEY)){
            team=(Team)intentThatCalled.getSerializableExtra(Team.TEAM_KEY);

            id=team.getId();
            teamNameEditText.setText(team.getTeam_name());
            sportTypeTextView.setText(team.getSport());
        }

        updateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentWithData();

            }
        });

        deleteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentWithData();
            }
        });

        //Popup menu for the Sport Type
        sportTypeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSportPopupMenu(v);
            }
        });

        viewAllTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAll(v);
            }
        });
    }

    private void showSportPopupMenu(View v) {
        popupMenu=new PopupMenu(TeamDetailsActivity.this,v);
        popupMenu.getMenu().add("Empty");
        popupMenu.getMenu().add("Volleyball");
        popupMenu.getMenu().add("Soccer");
        popupMenu.getMenu().add("Basketball");
        popupMenu.getMenu().add("Other");
        popupMenu.show();
        //Set the editText with the user choice after clicking and option in the menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sportTypeTextView.setText(item.getTitle());
                if(sportTypeTextView.getText()=="Empty"){
                    sportTypeTextView.setText("");
                }
                else
                    sportTypeTextView.setText(item.getTitle());
                return false;
            }
        });
    }

    private void setIntentWithData() {
        teamNameString=teamNameEditText.getText().toString();
        sportTypeString=sportTypeTextView.getText().toString();

        team= new Team(id,teamNameString,sportTypeString);

        Intent goBack= new Intent();
        goBack.putExtra(Team.TEAM_KEY,team);
        if (updateTeam.isPressed()){
            if(teamNameString.trim().isEmpty()){
                Snackbar.make(rootView, "Team name cannot be empty!", Snackbar.LENGTH_LONG).show();
                teamNameEditText.getText().clear();
                teamNameEditText.requestFocus();
                return;
            }
            if(sportTypeString.trim().isEmpty()){
                Snackbar.make(rootView, "Sport type cannot be empty!", Snackbar.LENGTH_LONG).show();
                sportTypeTextView.setText(" ");
                sportTypeTextView.requestFocus();
                return;
            }

            setResult(Constants.MODIFY_TEAM_DETAILS_ACTIVITY_CODE,goBack);
            finish();
        }else if(deleteTeam.isPressed()){
            setResult(Constants.DELETE_TEAM_DETAILS_ACTIVITY_CODE,goBack);
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void viewAll(View v) {
        List<Team> teamList= teamDataService.getTeams();
        String text="";
        if(teamList.size()>0){
            for(Team team: teamList){
                text= text.concat(team.toString()+teamList.indexOf(team)+"\n");
            }
            showMessage("Data",text);
        }else{
            showMessage("Records","Nothing found");
        }
    }
}