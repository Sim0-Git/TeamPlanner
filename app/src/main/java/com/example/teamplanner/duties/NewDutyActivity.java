package com.example.teamplanner.duties;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamplanner.R;
import com.example.teamplanner.games.Game;
import com.example.teamplanner.games.NewGameActivity;
import com.example.teamplanner.games.RecyclerItemActivity;
import com.example.teamplanner.service.DutyDataService;
import com.example.teamplanner.service.GameDataService;
import com.example.teamplanner.service.TeamDataService;
import com.example.teamplanner.teams.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NewDutyActivity extends AppCompatActivity {

    EditText dateEditText;
    EditText timeEditText;
    EditText locationEditText;
    EditText addressEditText;
    TextView membersEditText;
    TextView teamNameEditText;

    private DutyDataService dutyDataService;
    private TeamDataService teamDataService;
    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_duty);

        dutyDataService=new DutyDataService();
        dutyDataService.init(this);
        dutyDataService.getDuties();

        teamDataService=new TeamDataService();
        teamDataService.init(this);
        teamDataService.getTeams();

        //Title of the New Duty form
        TextView newDuty=findViewById(R.id.newDutyTextView);
        //Changing single letters New Game form title
        SpannableStringBuilder spannable = new SpannableStringBuilder("NEW DUTY");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),2, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),7, 8,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        newDuty.setText(spannable);

        //Hook all the data received from the user input
        dateEditText =findViewById(R.id.editTextDutyDate);
        timeEditText =findViewById(R.id.editDutyTextTime);
        locationEditText =findViewById(R.id.editDutyTextLocation);
        addressEditText =findViewById(R.id.editDutyTextPostalAddress);
        membersEditText =findViewById(R.id.membersRequiredTxtView);
        teamNameEditText=findViewById(R.id.textViewTeam);

        //Popup menu for the number of members required
        membersEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMembersPopupMenu(v);
            }
        });

        //Popup menu for the teams available
        teamNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeamsPopupMenu(v);
            }
        });

        //Button back and create setup
        Button backButton=findViewById(R.id.dutyBackButton);
        backButton.setBackground(discardButtonSetup());
        Button createButton= findViewById(R.id.dutyCreateButton);
        createButton.setBackground(createButtonSetup());

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDuty(v);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardDuty(v);
            }
        });

        Button viewAllButton=findViewById(R.id.buttonViewAll);
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAllDuties(v);
            }
        });

    }

    private void createNewDuty(View v) {
        String team_name=teamNameEditText.getText().toString();
        String date=dateEditText.getText().toString();
        String time=timeEditText.getText().toString();
        String location=locationEditText.getText().toString();
        String address=addressEditText.getText().toString();
        String members=membersEditText.getText().toString();

        if(team_name.trim().isEmpty()){
            Snackbar.make(v,"Team cannot be empty",Snackbar.LENGTH_LONG).show();
            teamNameEditText.setText(" ");
            teamNameEditText.requestFocus();
            return;
        }

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

        //Set the Duty constructor
        Duty duty=new Duty(null,team_name,time,date,location,address,members);

        //Send back from this activity the key and the game object
        Intent sendDuty= new Intent();
        sendDuty.putExtra(Duty.DUTY_KEY,duty);
        setResult(RESULT_OK,sendDuty);
        finish();

        Intent sendDutyToRecyclerItem= new Intent(NewDutyActivity.this, RecyclerItemDutyActivity.class);
        sendDutyToRecyclerItem.putExtra("Duty",duty);

    }

    private void discardDuty(View v) {
        Intent sendDuty= new Intent();
        sendDuty.putExtra(Duty.DUTY_KEY,"null");
        setResult(RESULT_CANCELED,sendDuty);
        finish();
    }

    //  METHOD TO VIEW ALL THE Duties
    private void viewAllDuties(View v) {
        List<Duty> dutyList= dutyDataService.getDuties();
        String text="";
        if(dutyList.size()>0){
            for(Duty duty: dutyList){
                text= text.concat(duty.toString()+dutyList.indexOf(duty)+"\n");
            }
            showMessage("Data",text);
        }else{
            showMessage("Records","Nothing found");
        }

    }

    private void showTeamsPopupMenu(View v) {
        List<Team> teamList= teamDataService.getTeams();
        popupMenu=new PopupMenu(NewDutyActivity.this,v);
        for(Team team: teamList){
            popupMenu.getMenu().add(team.getTeam_name());
        }
        popupMenu.show();
        //Set the editText with the user choice after clicking and option in hte menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                teamNameEditText.setText(item.getTitle());
                return false;
            }
        });
    }

    private void showMembersPopupMenu(View v) {
        popupMenu=new PopupMenu(NewDutyActivity.this,v);
        popupMenu.getMenu().add("Empty");
        popupMenu.getMenu().add("1");
        popupMenu.getMenu().add("2");
        popupMenu.getMenu().add("3");
        popupMenu.getMenu().add("4");
        popupMenu.getMenu().add("5");
        popupMenu.show();
        //Set the editText with the user choice after clicking and option in hte menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                membersEditText.setText(item.getTitle());
                if(membersEditText.getText()=="Empty"){
                    membersEditText.setText("");
                }
                else
                    membersEditText.setText(item.getTitle());
                return false;
            }
        });
    }
    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show(); }

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
