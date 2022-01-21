package com.example.teamplanner.teams;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.teamplanner.constants.Constants;
import com.example.teamplanner.duties.MyDutiesActivity;
import com.example.teamplanner.games.MainActivity;
import com.example.teamplanner.listener.OnTeamListener;
import com.example.teamplanner.profile.MyProfileActivity;
import com.example.teamplanner.R;
import com.example.teamplanner.profile.Profile;
import com.example.teamplanner.recycler_view.TeamRecyclerViewAdapter;
import com.example.teamplanner.service.ProfileDataService;
import com.example.teamplanner.service.TeamDataService;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.example.teamplanner.constants.Constants.UPDATE_TEAM_DETAILS_ACTIVITY_CODE;

public class MyTeamsActivity extends AppCompatActivity implements OnTeamListener{

    Button addNewTeamButton;
    View rootView;
    private RecyclerView teamRecyclerView;
    public ProfileDataService profileDataService;
    private List<Team> teamList;
    private List<Profile> profileList;
    Profile userProfile;
    private TeamRecyclerViewAdapter adapter;
    public TeamDataService teamDataService;

    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

        rootView=findViewById(android.R.id.content).getRootView();

        //Creating a linear layout
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        teamRecyclerView= findViewById(R.id.gameRecylcerView);
        teamRecyclerView.setLayoutManager(linearLayoutManager);

        //Call the database, to get all the games
        teamDataService=new TeamDataService();
        teamDataService.init(this);
        teamList= teamDataService.getTeams();

        //Call the database, to get all the profiles
        profileDataService=new ProfileDataService();
        profileDataService.init(this);
        profileList= profileDataService.getProfiles();

        //Crate a RecyclerViewAdapter instance and pass the data
        adapter=new TeamRecyclerViewAdapter(teamList,this,this);

        //set the adapter to the RecyclerView
        teamRecyclerView.setAdapter(adapter);



        //Title my teams page
        TextView myTeamTextView= findViewById(R.id.myTeamsTextView);
        SpannableStringBuilder spannable= new SpannableStringBuilder("MY TEAMS");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),1,2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),7,8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        myTeamTextView.setText(spannable);

        ImageButton homeButton = findViewById(R.id.homeButton3);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToHomeScreen(v);
            }
        });

        ImageButton profileButton=findViewById(R.id.profileButton3);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToProfileScreen(v);
            }
        });
        //NextDuties button
        ImageButton dutiesButton=findViewById(R.id.dutiesButton3);
        dutiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMyDutiesScreen(v);
            }
        });

        //AddButton
        addNewTeamButton=findViewById(R.id.addTeamButton);
        addNewTeamButton.setVisibility(View.INVISIBLE);//Set the button initially invisible
        ImageButton addButton=findViewById(R.id.addButtonMyTeams);
        addButton.setBackground(addButtonSetup());//Set button shape
        //Open submenu when the add button is clicked
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu(v);
            }
        });
        addNewTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Resetting the add button and the new game button in their original state
                addButton.getBackground().setAlpha(255);
                addNewTeamButton.setVisibility(View.INVISIBLE);
                //Calling the method for opening a new game form
                openNewTeamForm(v);
            }
        });

    }

    private void openNewTeamForm(View v) {
        Intent goToNewTeamForm= new Intent(MyTeamsActivity.this, NewTeamActivity.class);
        startActivityForResult(goToNewTeamForm, Constants.ADD_TEAM_ACTIVITY_CODE);
    }

    //Get the request code, result code and the data from the NewTeamctivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if the code is the add team activity code
        if(requestCode== Constants.ADD_TEAM_ACTIVITY_CODE) {
            if (resultCode == RESULT_CANCELED)
                Snackbar.make(rootView, "No new team has been added!", Snackbar.LENGTH_LONG).show();
            else if (resultCode == RESULT_OK) {
                addTeamData(data);
            }
        }
        if(requestCode== Constants.UPDATE_TEAM_DETAILS_ACTIVITY_CODE){
            if(resultCode==Constants.MODIFY_TEAM_DETAILS_ACTIVITY_CODE){
                updateTeamDetails(data);
            }else if(resultCode==Constants.DELETE_TEAM_DETAILS_ACTIVITY_CODE){
                deleteTeam(data);
            }else if(resultCode==RESULT_CANCELED){
                Snackbar.make(rootView, "The team hasn't been updated!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void deleteTeam(Intent data) {
        Long id;
        String teamName;
        if(data.hasExtra(Team.TEAM_KEY)){
            Team team=(Team) data.getSerializableExtra(Team.TEAM_KEY);
            id=team.getId();
            teamName=team.getTeam_name();
            boolean result= teamDataService.delete(team);
            int position =adapter.getTeamsList().indexOf(team);
            if(position >= 0){
                team=teamDataService.getTeam(id);
                adapter.deleteItemTeam(position,team);
                message="The team "+ teamName +" has been deleted.";
            }

            Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
        }
    }

    private void updateTeamDetails(Intent data) {

        Long id;
        if(data.hasExtra(Team.TEAM_KEY)){
            Team team=(Team) data.getSerializableExtra(Team.TEAM_KEY);
            id=team.getId();

            boolean result= teamDataService.update(team);
            int position =adapter.getTeamsList().indexOf(team);

            if(position >= 0){
                team=teamDataService.getTeam(id);
                adapter.replaceTeam(position,team);
            }
            message="Team "+ team.getTeam_name()+" has been updated.";
            Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
        }
    }

    private void addTeamData(Intent data) {
        Team team= (Team) data.getSerializableExtra(Team.TEAM_KEY);
        Long result= teamDataService.add(team);
        if(result>0){
            Team team1 = teamDataService.getTeam(result);
            adapter.addItem(team1);
            message="Team "+ team.getTeam_name() +" has been added.";
        }else{
            message="The team was not added!!!";
        }
        Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
    }


    //Method that make visible the sub menu with a little animation
    private void openMenu(View v) {
        //If the button is shown in the activity set it as invisible
        if(addNewTeamButton.isShown()){
            addNewTeamButton.setVisibility(View.INVISIBLE);
        }
        else{//else set the button as visible animating it
            addNewTeamButton.setVisibility(View.VISIBLE);
            Animation menuAnimation= AnimationUtils.loadAnimation(this,R.anim.sub_menu_animation);
            addNewTeamButton.setAnimation(menuAnimation);
        }
    }

    //Method to setup the add button
    private Drawable addButtonSetup() {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                75, 75, 75, 75,
                75, 75, 75, 75
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
        return shapeDrawable;
    }

    private void moveToHomeScreen(View v) {
        Intent goToHomeScreen= new Intent(MyTeamsActivity.this, MainActivity.class);
        startActivity(goToHomeScreen);
        finish();
    }
    //Switch to Profile activity
    private void moveToProfileScreen(View v) {
        if(profileList.size()==0){
            userProfile=new Profile(null ,"Your name/nickname","Your birthday","Your jersey number","Your position");
            profileDataService.add(userProfile);
        }
        userProfile=profileDataService.getProfile(1L);
        Intent goToProfileScreen= new Intent(MyTeamsActivity.this, MyProfileActivity.class);
        goToProfileScreen.putExtra(Profile.PROFILE_KEY,userProfile);
        setResult(RESULT_OK,goToProfileScreen);
        startActivity(goToProfileScreen);
    }

    private void moveToMyDutiesScreen(View v) {
        Intent goToMyDutiesScreen = new Intent(MyTeamsActivity.this, MyDutiesActivity.class);
        startActivity(goToMyDutiesScreen);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeScreen= new Intent(MyTeamsActivity.this, MainActivity.class);
        startActivity(goToHomeScreen);
        finish();
    }

    @Override
    public void onTeamClick(Team team) {
        showTeamDetails(team);
    }

    private void showTeamDetails(Team team) {
        Intent goToTeamDetails= new Intent(MyTeamsActivity.this, TeamDetailsActivity.class);
        goToTeamDetails.putExtra(Team.TEAM_KEY,team);
        startActivityForResult(goToTeamDetails, UPDATE_TEAM_DETAILS_ACTIVITY_CODE);
    }
}