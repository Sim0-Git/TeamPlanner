package com.example.teamplanner.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teamplanner.R;
import com.example.teamplanner.constants.Constants;
import com.example.teamplanner.duties.MyDutiesActivity;
import com.example.teamplanner.games.MainActivity;
import com.example.teamplanner.service.ProfileDataService;
import com.example.teamplanner.service.TeamDataService;
import com.example.teamplanner.teams.MyTeamsActivity;
import com.example.teamplanner.teams.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    Profile userProfile;
    private ProfileDataService profileDataService;
    private List<Profile> profileList;

    TextView name;
    TextView birthday;
    TextView jersey;
    TextView position;
    ImageView imageProfileView;

    Long id;

    View rootView;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileDataService=new ProfileDataService();
        profileDataService.init(this);
        profileList= profileDataService.getProfiles();

        rootView=findViewById(android.R.id.content).getRootView();

        name=findViewById(R.id.userNameTextView);
        birthday=findViewById(R.id.birthdateTextView);
        jersey=findViewById(R.id.jerseyNumberTextView);
        position=findViewById(R.id.positionTextView);
        imageProfileView=findViewById(R.id.imageProfileView);

        Intent getProfile=getIntent();
        if(getProfile.hasExtra(Profile.PROFILE_KEY)){
            userProfile=(Profile)getProfile.getSerializableExtra(Profile.PROFILE_KEY);

            id=userProfile.getId();
            this.name.setText(userProfile.getUserName());
            this.birthday.setText(userProfile.getBirthday());
            this.jersey.setText(userProfile.getJerseyNumber());
            this.position.setText(userProfile.getPositionPlayed());
        }

        //Title Profile Page
        TextView myProfile=findViewById(R.id.myProfileTextView);
        //Changing single letters profile page title
        SpannableStringBuilder spannable = new SpannableStringBuilder("MY PROFILE");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),1, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),9, 10,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        myProfile.setText(spannable);


        Button editProfileBtn= findViewById(R.id.editProfileButton);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileForm(v);
            }
        });


        ImageButton homeButton = findViewById(R.id.homeButton2);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToHomeScreen(v);
            }
        });
        //MyTeams button
        ImageButton myTeamsButton=findViewById(R.id.myTeamsButton2);
        myTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMyTeamsScreen(v);
            }
        });
        //NextDuties button
        ImageButton dutiesButton=findViewById(R.id.dutiesButton2);
        dutiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMyDutiesScreen(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.UPDATE_PROFILE_ACTIVITY_CODE){
            if(resultCode==Constants.MODIFY_PROFILE_DETAILS_ACTIVITY_CODE){
                if(profileList.size()<=0){
                    createProfile(data);
                }else if(profileList.size()>0){
                    updateProfile(data);
                    Snackbar.make(rootView, "The profile has been updated", Snackbar.LENGTH_LONG).show();
                }
            }else if(resultCode==RESULT_CANCELED){
                Snackbar.make(rootView, "The profile hasn't been updated", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void createProfile(Intent data) {
        Profile profile= (Profile) data.getSerializableExtra(Profile.PROFILE_KEY);
        Long result= profileDataService.add(profile);
        this.name.setText(profile.getUserName());
        //message="The profile was created!";
        Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
    }

    private void updateProfile(Intent data){
        //Long id;
        if(data.hasExtra(Profile.PROFILE_KEY)){
            Profile profile= (Profile) data.getSerializableExtra(Profile.PROFILE_KEY);
            //Trying to get the image from the EditProfileActivity and set it as the profile image, but the file is too big
            if(data.hasExtra("image")){
                Bitmap bitmap = data.getParcelableExtra("image");
                imageProfileView.setImageBitmap(bitmap);
            }
            //id=profile.getId();
            boolean result= profileDataService.update(profile);
            //name.setText(profile.getUserName());
            this.name.setText(profile.getUserName());
            this.birthday.setText(profile.getBirthday());
            this.jersey.setText(profile.getJerseyNumber());
            this.position.setText(profile.getPositionPlayed());
            message="Profile "+ profile.getUserName()+" has been updated.";
            Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
        }
    }


    private void openEditProfileForm(View v) {
        Intent goToEditProfileForm= new Intent(MyProfileActivity.this, EditProfileActivity.class);
        goToEditProfileForm.putExtra(Profile.PROFILE_KEY,userProfile);
        setResult(RESULT_OK,goToEditProfileForm);
        startActivityForResult(goToEditProfileForm,Constants.UPDATE_PROFILE_ACTIVITY_CODE);
    }

    private void moveToHomeScreen(View v) {
        Intent goToHomeScreen= new Intent(MyProfileActivity.this, MainActivity.class);
        startActivity(goToHomeScreen);
        finish();
    }

    private void moveToMyDutiesScreen(View v) {
        Intent goToMyDutiesScreen = new Intent(MyProfileActivity.this, MyDutiesActivity.class);
        startActivity(goToMyDutiesScreen);
    }
    private void moveToMyTeamsScreen(View v) {
        Intent goToMyTeamsScreen = new Intent(MyProfileActivity.this, MyTeamsActivity.class);
        startActivity(goToMyTeamsScreen);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeScreen= new Intent(MyProfileActivity.this, MainActivity.class);
        startActivity(goToHomeScreen);
        finish();
    }

}