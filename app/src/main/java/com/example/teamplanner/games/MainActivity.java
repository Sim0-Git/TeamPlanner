package com.example.teamplanner.games;

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
import com.example.teamplanner.duties.MyDutiesActivity;
import com.example.teamplanner.profile.MyProfileActivity;
import com.example.teamplanner.profile.Profile;
import com.example.teamplanner.service.ProfileDataService;
import com.example.teamplanner.service.TeamDataService;
import com.example.teamplanner.teams.MyTeamsActivity;
import com.example.teamplanner.recycler_view.GameRecyclerViewAdapter;
import com.example.teamplanner.listener.OnGameListener;
import com.example.teamplanner.service.GameDataService;
import com.example.teamplanner.teams.Team;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Timer;

import static com.example.teamplanner.constants.Constants.UPDATE_GAME_DETAILS_ACTIVITY_CODE;

public class MainActivity extends AppCompatActivity implements OnGameListener {

    private RecyclerView gameRecyclerView;
    private List<Game> gameList;
    private GameRecyclerViewAdapter adapter;
    public GameDataService gameDataService;
    private List<Profile> profileList;
    public ProfileDataService profileDataService;
    Profile userProfile;
    public List<Team> teamList;
    public TeamDataService teamDataService;
    Button addNewGameButton;
    ImageButton addButton;

    View rootView;

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView=findViewById(android.R.id.content).getRootView();

        //Creating a linear layout
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        gameRecyclerView= findViewById(R.id.gameRecylcerView);
        gameRecyclerView.setLayoutManager(linearLayoutManager);

        //Call the database, to get all the teams
        teamDataService=new TeamDataService();
        teamDataService.init(this);
        teamList= teamDataService.getTeams();

        //Call the database, to get all the games
        gameDataService=new GameDataService();
        gameDataService.init(this);
        gameList= gameDataService.getGames();

        //Call the database, to get all the profiles
        profileDataService=new ProfileDataService();
        profileDataService.init(this);
        profileList= profileDataService.getProfiles();

        //Crate a RecyclerViewAdapter instance and pass the data
        adapter=new GameRecyclerViewAdapter(gameList,this,this);

        //set the adapter to the RecyclerView
        gameRecyclerView.setAdapter(adapter);

        //Title Home Page
        TextView upcomingVT=findViewById(R.id.upcomingGamesTextView);
        //Changing single letters home page title
        SpannableStringBuilder spannable = new SpannableStringBuilder("UPCOMING GAMES");
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),7, 8,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#6BBBD9")),13, 14,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        upcomingVT.setText(spannable);


        //Add button
        addNewGameButton=findViewById(R.id.addGamesButton);
        addNewGameButton.setVisibility(View.INVISIBLE);//Set the button initially invisible
        //When the add button is clicked call the openMenu method
        addButton=findViewById(R.id.addButton);
        addButton.setBackground(addButtonSetup());//Set button shape
        //Open submenu when the add button is clicked
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openMenu(v);
            }
        });
        //When the add new game button is clicked the user get directed to the new game form
        addNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Resetting the add button and the new game button in their original state
                addButton.getBackground().setAlpha(255);
                addNewGameButton.setVisibility(View.INVISIBLE);
                //Calling the method for opening a new game form
                openNewGameForm(v);
            }
        });
        //Profile button
        ImageButton profileButton=findViewById(R.id.profileButton1);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToProfileScreen(v);
            }
        });
        //MyTeams button
        ImageButton myTeamsButton=findViewById(R.id.myTeamsButton1);
        myTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMyTeamsScreen(v);
            }
        });
        //NextDuties button
        ImageButton dutiesButton=findViewById(R.id.dutiesButton1);
        dutiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMyDutiesScreen(v);
            }
        });

    }



    //Get the request code, result code and the data from the NewGameActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if the code is the add game activity code
        if(requestCode== Constants.ADD_GAME_ACTIVITY_CODE) {
            if (resultCode == RESULT_CANCELED)
                Snackbar.make(rootView, "No new game has been added!", Snackbar.LENGTH_LONG).show();
            else if (resultCode == RESULT_OK) {
                addGameData(data);
            }
        }
        if(requestCode== Constants.UPDATE_GAME_DETAILS_ACTIVITY_CODE){
            if(resultCode==Constants.MODIFY_GAME_DETAILS_ACTIVITY_CODE){
                updateGameDetails(data);
            }else if(resultCode==Constants.DELETE_GAME_DETAILS_ACTIVITY_CODE){
                deleteGame(data);
            }
        }
    }

    private void deleteGame(Intent data) {
        Long id;
        if(data.hasExtra(Game.GAME_KEY)){
            Game game=(Game)data.getSerializableExtra(Game.GAME_KEY);
            id=game.getId();
            boolean result= gameDataService.delete(game);
            int position =adapter.getGames().indexOf(game);
            if(position >= 0){
                game=gameDataService.getGame(id);
                adapter.deleteItemGame(position,game);
                message="The game has been deleted.";
            }

            Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
        }
    }

    private void updateGameDetails(Intent data) {
        Long id;

        if(data.hasExtra(Game.GAME_KEY)){
            Game game=(Game)data.getSerializableExtra(Game.GAME_KEY);
            id=game.getId();
            boolean result= gameDataService.update(game);
            int position =adapter.getGames().indexOf(game);

            if(position >= 0){
                game=gameDataService.getGame(id);
                adapter.replaceGame(position,game);
            }
            message="Game "+ game.getMatchName()+" vs "+game.getOpponent()+" has been updated.";
            Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
        }

    }

    //Method that add the game to the database and inform the user if the game has been added or not
    private void addGameData(Intent data) {
        Game game= (Game)data.getSerializableExtra(Game.GAME_KEY);
        Long result= gameDataService.add(game);
        if(result>0){
            Game game1 = gameDataService.getGame(result);
            adapter.addItem(game1);
            message="Game "+game.getMatchName()+" vs "+game.getOpponent()+" has been added.";
        }else{
            message="The game was not added!!!";
        }
        Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).show();
    }

    //Method that make visible the sub menu with a little animation
    private void openMenu(View v) {
        //If the sub menu is shown in the activity set it as invisible, and keep the add button with its original opacity
        if(addNewGameButton.isShown()){
            addNewGameButton.setVisibility(View.INVISIBLE);
            addButton.getBackground().setAlpha(255);
        }
        //else set the sub menu as visible animating it, and change the opacity add button
        else{
            addNewGameButton.setVisibility(View.VISIBLE);
            Animation menuAnimation= AnimationUtils.loadAnimation(this,R.anim.sub_menu_animation);
            addNewGameButton.setAnimation(menuAnimation);
            addButton.getBackground().setAlpha(160);
        }
    }

    //Method that open a new form just if it is already present at list a team in the database
    private void openNewGameForm(View v) {
        if(!(teamList.size()>0)){
            Snackbar.make(rootView, "You need to add at least a team first!", Snackbar.LENGTH_LONG).show();
        }
        else{
            Intent goToNewGameForm= new Intent(MainActivity.this, NewGameActivity.class);
            startActivityForResult(goToNewGameForm, Constants.ADD_GAME_ACTIVITY_CODE);
        }

    }
    //Switch to MyTeams activity
    private void moveToMyTeamsScreen(View v) {
        Intent goToMyTeamsScreen = new Intent(MainActivity.this, MyTeamsActivity.class);
        startActivity(goToMyTeamsScreen);
    }

    //Switch to Profile activity
    private void moveToProfileScreen(View v) {
        if(profileList.size()==0){
            //Setting already a profile in the database
            userProfile=new Profile(null ,"Your name/nickname","Your birthday","Your jersey number","Your position");
            profileDataService.add(userProfile);
        }
        userProfile=profileDataService.getProfile(1L);
        Intent goToProfileScreen= new Intent(MainActivity.this, MyProfileActivity.class);
        goToProfileScreen.putExtra(Profile.PROFILE_KEY,userProfile);
        setResult(RESULT_OK,goToProfileScreen);
        startActivity(goToProfileScreen);
    }

    private void moveToMyDutiesScreen(View v) {
        Intent goToMyDutiesScreen = new Intent(MainActivity.this, MyDutiesActivity.class);
        startActivity(goToMyDutiesScreen);
    }


    //Method to set shape, color and opacity of the component
    private Drawable textViewSetup() {
        RoundRectShape rectShape = new RoundRectShape(new float[]{
                30, 30, 30, 30,
                30, 30, 30, 30
        }, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setColor(Color.parseColor("#FFFFFF"));
        shapeDrawable.getPaint().setAlpha(245);
        return shapeDrawable;
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
    public void onGameClick(Game game) {
        showGameDetails(game);
    }

    private void showGameDetails(Game game) {
        Intent goToGameDetails= new Intent(MainActivity.this, GameDetailsActivity.class);
        goToGameDetails.putExtra(Game.GAME_KEY,game);
        startActivityForResult(goToGameDetails, UPDATE_GAME_DETAILS_ACTIVITY_CODE);
    }
}