package com.example.teamplanner.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.teamplanner.R;
import com.example.teamplanner.constants.Constants;
import com.example.teamplanner.service.ProfileDataService;
import com.google.android.material.snackbar.Snackbar;

public class EditProfileActivity extends AppCompatActivity {

    private ProfileDataService profileDataService;
    Profile profile;

    EditText nameEditText;
    EditText birthdayEditText;
    EditText jerseyEditText;
    EditText positionEditText;

    Long id;
    String userName;
    String userBirthday;
    String jerseyNum;
    String positionPlayed;

    private static final int PICK_IMAGE=100;
    Bitmap imageBitmap;
    Uri imageURI;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileDataService=new ProfileDataService();
        profileDataService.init(this);


        nameEditText=findViewById(R.id.editTextUserName);
        birthdayEditText=findViewById(R.id.birthdayEditTxt);
        jerseyEditText=findViewById(R.id.jersyEditText);
        positionEditText=findViewById(R.id.positionEditText);


        Intent intentThatCalled=getIntent();
        if(intentThatCalled.hasExtra(Profile.PROFILE_KEY)){
            profile=(Profile) intentThatCalled.getSerializableExtra(Profile.PROFILE_KEY);

            id=profile.getId();
            nameEditText.setText(profile.getUserName());
            birthdayEditText.setText(profile.getBirthday());
            jerseyEditText.setText(profile.getJerseyNumber());
            positionEditText.setText(profile.getPositionPlayed());

        }

        profileImageView=findViewById(R.id.profileImgView);
        profileImageView.setDrawingCacheEnabled(true);
        imageBitmap=profileImageView.getDrawingCache();

        ImageButton uploadPicture= findViewById(R.id.uploadPicButton);
        uploadPicture.setBackground(addButtonSetup());
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPic(v);
            }
        });


        Button updateProfileBtn=findViewById(R.id.updateProfileBtn);
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(v);
            }
        });

        Button backBtn=findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardProfile(v);
            }
        });

    }

    private void uploadPic(View v) {
        Intent openGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(openGallery,PICK_IMAGE);
    }

    //Get the image from the gallery and set the profileImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE){
            imageURI= data.getData();
            profileImageView.setImageURI(imageURI);
            //Assign the image received in the profileImage view
            BitmapDrawable drawable = (BitmapDrawable) profileImageView.getDrawable();
        }
    }

    private void discardProfile(View v) {
        Intent discardProfile= new Intent();
        discardProfile.putExtra(Profile.PROFILE_KEY,"null");
        setResult(RESULT_CANCELED,discardProfile);
        finish();
    }


    private void updateProfile(View v) {

        userName= nameEditText.getText().toString();
        userBirthday= birthdayEditText.getText().toString();
        jerseyNum= jerseyEditText.getText().toString();
        positionPlayed= positionEditText.getText().toString();

        if(userName.trim().isEmpty()){
            Snackbar.make(v,"Team name cannot be empty",Snackbar.LENGTH_LONG).show();
            nameEditText.getText().clear();
            nameEditText.requestFocus();
            return;
        }
        if(userBirthday.trim().isEmpty()){
            Snackbar.make(v,"Sport type cannot be empty",Snackbar.LENGTH_LONG).show();
            birthdayEditText.getText().clear();
            birthdayEditText.requestFocus();
            return;
        }
        if(jerseyNum.trim().isEmpty()){
            Snackbar.make(v,"Sport type cannot be empty",Snackbar.LENGTH_LONG).show();
            jerseyEditText.getText().clear();
            jerseyEditText.requestFocus();
            return;
        }
        if(positionPlayed.trim().isEmpty()){
            Snackbar.make(v,"Sport type cannot be empty",Snackbar.LENGTH_LONG).show();
            positionEditText.getText().clear();
            positionEditText.requestFocus();
            return;
        }

        profile=new Profile(id,userName,userBirthday,jerseyNum,positionPlayed);

        Intent goBack= new Intent();
        goBack.putExtra(Profile.PROFILE_KEY,profile);
        //Trying to send the image back to the previous activity, but the file it might be too big!
        goBack.putExtra("image",imageBitmap);

        setResult(Constants.MODIFY_PROFILE_DETAILS_ACTIVITY_CODE,goBack);
        finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}