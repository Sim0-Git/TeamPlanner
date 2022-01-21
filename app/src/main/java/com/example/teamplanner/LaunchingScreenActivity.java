package com.example.teamplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.teamplanner.games.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching_screen);

        //Changing some logo letters colors
        TextView logoTextView= findViewById(R.id.logoTextView);
        SpannableStringBuilder spannable= new SpannableStringBuilder("MEMOSPORT");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#F2E20F")),3,4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#F2E20F")),8,9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        logoTextView.setText(spannable);

        //Using the class Timer to set a specified time that will show this activity just for few seconds, before passing to the MainActivity.
        Timer timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent goToMainActivity= new Intent(LaunchingScreenActivity.this, MainActivity.class);
                startActivity(goToMainActivity);
                finish();
            }
        },2500);

        //Animating the logo in the launching page
        Animation logoAnimation= AnimationUtils.loadAnimation(this,R.anim.logo_animation);
        logoTextView.setAnimation(logoAnimation);
    }
}