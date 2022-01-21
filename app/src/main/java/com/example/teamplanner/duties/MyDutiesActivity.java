package com.example.teamplanner.duties;

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

import com.example.teamplanner.R;
import com.example.teamplanner.constants.Constants;
import com.example.teamplanner.games.Game;
import com.example.teamplanner.games.GameDetailsActivity;
import com.example.teamplanner.games.MainActivity;
import com.example.teamplanner.games.NewGameActivity;
import com.example.teamplanner.listener.OnDutyListener;
import com.example.teamplanner.profile.MyProfileActivity;
import com.example.teamplanner.profile.Profile;
import com.example.teamplanner.recycler_view.DutyRecyclerViewAdapter;
import com.example.teamplanner.recycler_view.GameRecyclerViewAdapter;
import com.example.teamplanner.service.DutyDataService;
import com.example.teamplanner.service.ProfileDataService;
import com.example.teamplanner.service.TeamDataService;
import com.example.teamplanner.teams.MyTeamsActivity;
import com.example.teamplanner.teams.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.example.teamplanner.constants.Constants.UPDATE_DUTY_DETAILS_ACTIVITY_CODE;
import static com.example.teamplanner.constants.Constants.UPDATE_GAME_DETAILS_ACTIVITY_CODE;

public class MyDutiesActivity extends AppCompatActivity implements OnDutyListener {

    Button addNewDutyButton;
    ImageButton addButton;

    private RecyclerView dutyRecyclerView ;
    private List<Duty> dutyList;
    public DutyDataService dutyDataService;
    private DutyRecyclerViewAdapter dutyRecyclerViewAdapter;

    private List<Profile> profileList;
    public ProfileDataService profileDataService;
    Profile userProfile;

    public List<Team> teamList;
    public TeamDataService teamDataService;

    View rootView;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_duties);

        rootView=findViewById(android.R.id.content).getRootView();

        //Creating a linear layout
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        dutyRecyclerView=findViewById(R.id.gameRecylcerView);
        dutyRecyclerView.setLayoutManager(linearLayoutManager);

        //Call the database, to get all the profiles
        profileDataService=new ProfileDataService();
        profileDataService.init(this);
        profileList= profileDataService.getProfiles();

        //Call the database, to get all the teams
        teamDataService=new TeamDataService();
        teamDataService.init(this);
        teamList= teamDataService.getTeams();

        //Call the database, to get all the teams
        dutyDataService=new DutyDataService();
        dutyDataService.init(this);
        dutyList= dutyDataService.getDuties();

        //Crate a RecyclerViewAdapter instance and pass the data
        dutyRecyclerViewAdapter=new DutyRecyclerViewAdapter(dutyList,this,this);

        //set the adapter to the RecyclerView
        dutyRecyclerView.setAdapter(dutyRecyclerViewAdapter);

        //Title Next Duties Page
        TextView upcomingVT=findViewById(R.id.nextDutiesTextView);
        //Changing single letters home page title
        SpannableStringBuilder spannable = new SpannableStringBuilder("NEXT DUTY");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),3, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),8, 9,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        upcomingVT.setText(spannable);

        ImageButton homeButton = findViewById(R.id.homeButton4);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToHomeScreen(v);
            }
        });
        ImageButton profileButton=findViewById(R.id.profileButton4);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToProfileScreen(v);
            }
        });
        //MyTeams button
        ImageButton myTeamsButton=findViewById(R.id.myTeamsButton4);
        myTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMyTeamsScreen(v);
            }
        });

        //Add button
        addNewDutyButton=findViewById(R.id.addDutiesButton);
        addNewDutyButton.setVisibility(View.INVISIBLE);//Set the button initially invisible
        //When the add button is clicked call the openMenu method
        addButton=findViewById(R.id.addButton4);
        addButton.setBackground(addButtonSetup());//Set button shape
        //Open submenu when the add button is clicked
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu(v);
            }
        });
        //When the add new game button is clicked the user get directed to the new game form
        addNewDutyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Resetting the add button and the new game button in their original state
                addButton.getBackground().setAlpha(255);
                addNewDutyButton.setVisibility(View.INVISIBLE);
                //Calling the method for opening a new game form
                openNewDutyForm(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if the code is the add game activity code
        if(requestCode== Constants.ADD_DUTY_ACTIVITY_CODE) {
            if (resultCode == RESULT_CANCELED)
                Snackbar.make(rootView, "No new duty has been added!", Snackbar.LENGTH_LONG).show();
            else if (resultCode == RESULT_OK) {
                addDutyData(data);
            }
        }
        if(requestCode== Constants.UPDATE_DUTY_DETAILS_ACTIVITY_CODE){
            if(resultCode==Constants.MODIFY_DUTY_DETAILS_ACTIVITY_CODE){
                updateDutyDetails(data);
            }else if(resultCode==Constants.DELETE_DUTY_DETAILS_ACTIVITY_CODE){
                deleteDuty(data);
            }
        }
    }

    private void addDutyData(Intent data) {
        Duty duty=(Duty) data.getSerializableExtra(Duty.DUTY_KEY);
        Long result= dutyDataService.add(duty);
        
        if(result>0){
            Duty duty1 = dutyDataService.getDuty(result);
            dutyRecyclerViewAdapter.addItem(duty1);
            message="A new Duty has been added.";
        }else{
            message="The Duty was not added!!!";
        }
        Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
    }

    private void updateDutyDetails(Intent data) {
        Long id;
        if(data.hasExtra(Duty.DUTY_KEY)){
            Duty duty=(Duty) data.getSerializableExtra(Duty.DUTY_KEY);
            id=duty.getId();

            boolean result= dutyDataService.update(duty);
            int position =dutyRecyclerViewAdapter.getDuties().indexOf(duty);
            if(position >= 0){
                duty=dutyDataService.getDuty(id);
                dutyRecyclerViewAdapter.replaceDuty(position,duty);
            }
            message="Duty "+ duty.getId()+" has been updated.";
            Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
        }

    }

    private void deleteDuty(Intent data) {
        Long id;
        if(data.hasExtra(Duty.DUTY_KEY)){
            Duty duty=(Duty) data.getSerializableExtra(Duty.DUTY_KEY);
            id=duty.getId();
            boolean result= dutyDataService.delete(duty);
            int position =dutyRecyclerViewAdapter.getDuties().indexOf(duty);
            if(position >= 0){
                duty=dutyDataService.getDuty(id);
                dutyRecyclerViewAdapter.deleteItemDuty(position,duty);
                message="The duty has been deleted.";
            }
            Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
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

    private void openMenu(View v) {
        //If the sub menu is shown in the activity set it as invisible, and keep the add button with its original opacity
        if(addNewDutyButton.isShown()){
            addNewDutyButton.setVisibility(View.INVISIBLE);
            addButton.getBackground().setAlpha(255);
        }
        //else set the sub menu as visible animating it, and change the opacity add button
        else{
            addNewDutyButton.setVisibility(View.VISIBLE);
            Animation menuAnimation= AnimationUtils.loadAnimation(this,R.anim.sub_menu_animation);
            addNewDutyButton.setAnimation(menuAnimation);
            addButton.getBackground().setAlpha(160);
        }
    }

    //Method that open a new form just if it is already present at list a team in the database
    private void openNewDutyForm(View v) {
        if(!(teamList.size()>0)){
            Snackbar.make(rootView, "You need to add at least a team first!", Snackbar.LENGTH_LONG).show();
        }
        else{
            Intent goToNewDutyForm= new Intent(MyDutiesActivity.this, NewDutyActivity.class);
            startActivityForResult(goToNewDutyForm, Constants.ADD_DUTY_ACTIVITY_CODE);
        }
    }
    private void moveToHomeScreen(View v) {
        Intent goToHomeScreen= new Intent(MyDutiesActivity.this, MainActivity.class);
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
        Intent goToProfileScreen= new Intent(MyDutiesActivity.this, MyProfileActivity.class);
        goToProfileScreen.putExtra(Profile.PROFILE_KEY,userProfile);
        setResult(RESULT_OK,goToProfileScreen);
        startActivity(goToProfileScreen);
    }
    //Switch to MyTeams activity
    private void moveToMyTeamsScreen(View v) {
        Intent goToMyTeamsScreen = new Intent(MyDutiesActivity.this, MyTeamsActivity.class);
        startActivity(goToMyTeamsScreen);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeScreen= new Intent(MyDutiesActivity.this, MainActivity.class);
        startActivity(goToHomeScreen);
        finish();
    }

    @Override
    public void onDutyClick(Duty duty) {
        showDutyDetails(duty);
    }

    private void showDutyDetails(Duty duty) {
        Intent goToDutyDetails= new Intent(MyDutiesActivity.this, DutyDetailsActivity.class);
        goToDutyDetails.putExtra(Duty.DUTY_KEY,duty);
        startActivityForResult(goToDutyDetails, UPDATE_DUTY_DETAILS_ACTIVITY_CODE);
    }


}