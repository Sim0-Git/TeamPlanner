package com.example.teamplanner.database;

import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import androidx.annotation.Nullable;

import com.example.teamplanner.duties.Duty;
import com.example.teamplanner.games.Game;
import com.example.teamplanner.profile.Profile;
import com.example.teamplanner.teams.Team;

import java.util.ArrayList;
        import java.util.List;

    //Operations related to the databse
public class MemoSportDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = MemoSportDatabaseHelper.class.getName();

    private static MemoSportDatabaseHelper mInstance = null;
    private Context context;

    //create database constants
    private static final String DATABASE_NAME = "memo_sport.db";
    private static final Integer DATABASE_VERSION = 1;

    private static final String TABLE_GAME_NAME = "Game";//Game Table
    private static final String TABLE_TEAM_NAME = "Team";//Team Table
    private static final String TABLE_PROFILE_NAME = "Profile";//User profile table
    private static final String TABLE_DUTY_NAME = "Duty";//Duties table

    //create constants for the Game table's columns
    private static final String GAME_COL_ID = "ID";
    private static final String COL_MATCH_NAME = "MATCH_NAME";
    private static final String COL_TIME = "DATE";
    private static final String COL_DATE = "TIME";
    private static final String COL_LOCATION = "LOCATION";
    private static final String COL_ADDRESS = "ADDRESS";
    private static final String COL_OPPONENT = "OPPONENT";
    private static final String COL_EVENT_TYPE = "EVENT_TYPE";

    //create constants for the Team table's columns
    private static final String TEAM_COL_ID="ID";
    private static final String COL_TEAM_NAME="TEAM_NAME";
    private static final String COL_SPORT_TYPE="SPORT";

    //create constants for the Profile table's columns
    private static final String PROFILE_COL_ID="ID";
    private static final String COL_USER_NAME="USER_NAME";
    private static final String COL_BIRTHDAY="BIRTHDAY";
    private static final String COL_JERSEY_NUMBER="JERSEY_NUMBER";
    private static final String COL_POSITION="POSITION";

    //create constants for the Duty table's columns
    private static final String DUTY_COL_ID = "ID";
    private static final String COL_DUTY_TEAM = "DUTY_TEAM_NAME";
    private static final String COL_DUTY_DATE = "DUTY_DATE";
    private static final String COL_DUTY_TIME = "DUTY_TIME";
    private static final String COL_DUTY_LOCATION = "DUTY_LOCATION";
    private static final String COL_DUTY_ADDRESS = "DUTY_ADDRESS";
    private static final String COL_MEMBERS_REQUIRED = "MEMBERS_REQUIRED";

    //create sql statements initial version
    private static final String CREATE_GAME_TABLE_ST = "CREATE TABLE " + TABLE_GAME_NAME + "(" + GAME_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_MATCH_NAME + " TEXT, " +
            COL_DATE + " TEXT, " +
            COL_TIME + " TEXT, " +
            COL_LOCATION + " TEXT, " +
            COL_ADDRESS + " TEXT, " +
            COL_OPPONENT + " TEXT, " +
            COL_EVENT_TYPE + " TEXT )";

    private static final String DROP_GAME_TABLE_ST = "DROP TABLE IF EXISTS " + TABLE_GAME_NAME;
    private static final String GET_ALL_GAME_ST = "SELECT * FROM " + TABLE_GAME_NAME;
    private static final String GET_GAME_BY_ID="SELECT * FROM " + TABLE_GAME_NAME + " WHERE " + GAME_COL_ID + "= ?";
    private  static final String UPDATE_GAME="UPDATE " + TABLE_GAME_NAME + " SET " + COL_DATE + " = " + COL_DATE + " + ? " + ", " + COL_TIME + " = " + COL_TIME + " + ? " + ", " + COL_LOCATION + " = " + COL_LOCATION + " + ? " + ", " + COL_ADDRESS + " = " + COL_ADDRESS + " + ? " + ", " + COL_OPPONENT + " = " + COL_OPPONENT + " + ? " + ", " + COL_EVENT_TYPE + " = " + COL_EVENT_TYPE + " + ? " + " WHERE " + GAME_COL_ID + "= ?";

    //create sql statements initial version
    private static final String CREATE_TEAM_TABLE_ST = "CREATE TABLE " + TABLE_TEAM_NAME + "(" + TEAM_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_TEAM_NAME + " TEXT, " +
            COL_SPORT_TYPE + " TEXT )";

    private static final String DROP_TEAM_TABLE_ST = "DROP TABLE IF EXISTS " + TABLE_TEAM_NAME;
    private static final String GET_ALL_TEAM_ST = "SELECT * FROM " + TABLE_TEAM_NAME;
    private static final String GET_TEAM_BY_ID="SELECT * FROM " + TABLE_TEAM_NAME + " WHERE " + TEAM_COL_ID + "= ?";
    private  static final String UPDATE_TEAM="UPDATE " + TABLE_TEAM_NAME + " SET " + COL_TEAM_NAME + " = " + COL_TEAM_NAME + " + ? " + ", " + COL_SPORT_TYPE + " = " + COL_SPORT_TYPE + " + ? " + " WHERE " + TEAM_COL_ID + "= ?";

    //create sql statements initial version
    private static final String CREATE_PROFILE_TABLE_ST = "CREATE TABLE " + TABLE_PROFILE_NAME + "(" + PROFILE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USER_NAME + " TEXT, " +
            COL_BIRTHDAY + " TEXT, " +
            COL_JERSEY_NUMBER + " TEXT, " +
            COL_POSITION + " TEXT )";

    private static final String DROP_PROFILE_TABLE_ST = "DROP TABLE IF EXISTS " + TABLE_PROFILE_NAME;
    private static final String GET_ALL_PROFILE_ST = "SELECT * FROM " + TABLE_PROFILE_NAME;
    private static final String GET_PROFILE_BY_ID="SELECT * FROM " + TABLE_PROFILE_NAME + " WHERE " + PROFILE_COL_ID + "= ?";
    private  static final String UPDATE_PROFILE="UPDATE " + TABLE_PROFILE_NAME + " SET " + COL_USER_NAME + " = " + COL_USER_NAME + " + ? " + ", " + COL_BIRTHDAY + " = " + COL_BIRTHDAY + " + ? "+ ", " + COL_JERSEY_NUMBER + " = " + COL_JERSEY_NUMBER + " + ? "+ ", " + COL_POSITION + " = " + COL_POSITION + " + ? " + " WHERE " + PROFILE_COL_ID + "= ?";

    //create sql statements initial version
    private static final String CREATE_DUTY_TABLE_ST = "CREATE TABLE " + TABLE_DUTY_NAME + "(" + DUTY_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_DUTY_TEAM + " TEXT, " +
            COL_DUTY_DATE + " TEXT, " +
            COL_DUTY_TIME + " TEXT, " +
            COL_DUTY_LOCATION + " TEXT, " +
            COL_DUTY_ADDRESS + " TEXT, " +
            COL_MEMBERS_REQUIRED + " TEXT )";

    private static final String DROP_DUTY_TABLE_ST = "DROP TABLE IF EXISTS " + TABLE_DUTY_NAME;
    private static final String GET_ALL_DUTY_ST = "SELECT * FROM " + TABLE_DUTY_NAME;
    private static final String GET_DUTY_BY_ID="SELECT * FROM " + TABLE_DUTY_NAME + " WHERE " + DUTY_COL_ID + "= ?";
    private  static final String UPDATE_DUTY="UPDATE " + TABLE_DUTY_NAME + " SET " + COL_DUTY_TEAM + " = " + COL_DUTY_TEAM + " + ? " + ", "+ COL_DUTY_DATE + " = " + COL_DUTY_DATE + " + ? " + ", " + COL_DUTY_TIME + " = " + COL_DUTY_TIME + " + ? " + ", " + COL_DUTY_LOCATION + " = " + COL_DUTY_LOCATION + " + ? " + ", " + COL_MEMBERS_REQUIRED + " = " + COL_MEMBERS_REQUIRED + " + ? " + " WHERE " + DUTY_COL_ID + "= ?";

    public static synchronized MemoSportDatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new MemoSportDatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /* Declare the database version, if the version is greater then the previous
        version already stored, then the onUpgrade method will be called.*/
    public MemoSportDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //This method gets executed only if the database does not exists, creating the following tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_GAME_TABLE_ST);
        sqLiteDatabase.execSQL(CREATE_TEAM_TABLE_ST);
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE_ST);
        sqLiteDatabase.execSQL(CREATE_DUTY_TABLE_ST);
    }

    //Update the database tables
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_GAME_TABLE_ST);
        sqLiteDatabase.execSQL(DROP_TEAM_TABLE_ST);
        sqLiteDatabase.execSQL(DROP_PROFILE_TABLE_ST);
        sqLiteDatabase.execSQL(DROP_DUTY_TABLE_ST);
        onCreate(sqLiteDatabase);
    }

    //Methods that adds Game, Team, Duties and profile.These methods insert new ows in the database, if the result is -1,
    // an error occurs and the row was not inserted, otherwise will return the row inserted.

    //GAME
    public Long insertGame(String match_name, String time, String date, String location, String address, String opponent, String event_type) {
    //create an instance of SQLITE database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MATCH_NAME, match_name);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_ADDRESS, address);
        contentValues.put(COL_OPPONENT, opponent);
        contentValues.put(COL_EVENT_TYPE, event_type);

        long result = db.insert(TABLE_GAME_NAME, null, contentValues);
        db.close();
        return result;
    }

    //TEAM
    public Long insertTeam(String team_name, String sport) {
        //create an instance of SQLITE database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TEAM_NAME, team_name);
        contentValues.put(COL_SPORT_TYPE, sport);

        long result = db.insert(TABLE_TEAM_NAME, null, contentValues);
        db.close();
        return result;
    }

    //PROFILE
    public Long insertProfile(String user_name, String birthday, String jersey_number, String position) {
        //create an instance of SQLITE database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_NAME, user_name);
        contentValues.put(COL_BIRTHDAY, birthday);
        contentValues.put(COL_JERSEY_NUMBER, jersey_number);
        contentValues.put(COL_POSITION, position);

        long result = db.insert(TABLE_PROFILE_NAME, null, contentValues);
        db.close();
        return result;
    }

    //DUTY
    public Long insertDuty(String name,String date, String time, String location, String address, String members) {
        //create an instance of SQLITE database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DUTY_TEAM,name);
        contentValues.put(COL_DUTY_DATE, date);
        contentValues.put(COL_DUTY_TIME, time);
        contentValues.put(COL_DUTY_LOCATION, location);
        contentValues.put(COL_DUTY_ADDRESS, address);
        contentValues.put(COL_MEMBERS_REQUIRED, members);

        long result = db.insert(TABLE_DUTY_NAME, null, contentValues);
        db.close();
        return result;
    }

    //Methods that updates Game, Team, Duties and profile.These methods updates existent rows in the database,
        // if the result is -1, an error occurs and the row was not updated, otherwise will return the row updated.

    //GAME
    public boolean updateGame(Long id, String match_name, String date, String time, String location, String address, String opponent, String event_type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(GAME_COL_ID, id);
        contentValues.put(COL_MATCH_NAME, match_name);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_ADDRESS, address);
        contentValues.put(COL_OPPONENT, opponent);
        contentValues.put(COL_EVENT_TYPE, event_type);

        int numOfRowsUpdated = db.update(TABLE_GAME_NAME, contentValues, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsUpdated == 1;
    }

    //TEAM
    public boolean updateTeam(Long id, String team_name, String sport) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_COL_ID, id);
        contentValues.put(COL_TEAM_NAME, team_name);
        contentValues.put(COL_SPORT_TYPE, sport);

        int numOfRowsUpdated = db.update(TABLE_TEAM_NAME, contentValues, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsUpdated == 1;
    }

    //PROFILE
    public boolean updateProfile(Long id, String user_name, String birthday, String jersey_number, String position) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_COL_ID, id);
        contentValues.put(COL_USER_NAME, user_name);
        contentValues.put(COL_BIRTHDAY, birthday);
        contentValues.put(COL_JERSEY_NUMBER, jersey_number);
        contentValues.put(COL_POSITION, position);

        int numOfRowsUpdated = db.update(TABLE_PROFILE_NAME, contentValues, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsUpdated == 1;
    }

    //Duty
    public boolean updateDuty(Long id,String name, String date, String time, String location, String address, String members) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DUTY_COL_ID, id);
        contentValues.put(COL_DUTY_TEAM, name);
        contentValues.put(COL_DUTY_DATE, date);
        contentValues.put(COL_DUTY_TIME, time);
        contentValues.put(COL_DUTY_LOCATION, location);
        contentValues.put(COL_DUTY_ADDRESS, address);
        contentValues.put(COL_MEMBERS_REQUIRED, members);

        int numOfRowsUpdated = db.update(TABLE_DUTY_NAME, contentValues, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsUpdated == 1;
    }



    //These methods delete rows from the tables Game,Team and Duty by getting the id of the row corresponding to
        // the id present in the database.
    //GAME
    public boolean deleteGame(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete return the # of rows affected by the query
        int numOfRowsDeleted = db.delete(TABLE_GAME_NAME, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsDeleted == 1;
    }

    //TEAM
    public boolean deleteTeam(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete return the # of rows affected by the query
        int numOfRowsDeleted = db.delete(TABLE_TEAM_NAME, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsDeleted == 1;
    }

    //DUTY
    public boolean deleteDuty(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete return the # of rows affected by the query
        int numOfRowsDeleted = db.delete(TABLE_DUTY_NAME, "ID = ?", new String[]{id.toString()});
        db.close();
        return numOfRowsDeleted == 1;
    }

    /*Methods that return list of the corresponding tables in the database */
    public List<Game> getGames() {
        List<Game> gameList = new ArrayList<>();
        Cursor cursor = getAllGames();

        if(cursor.getCount() > 0) {
            Game game;
            while (cursor.moveToNext()) {
                Long id = cursor.getLong(0);
                String match_name = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                String location = cursor.getString(4);
                String address = cursor.getString(5);
                String opponent = cursor.getString(6);
                String eventType = cursor.getString(7);

                game = new Game(id, match_name, date, time, location, address, opponent, eventType);
                gameList.add(game);
            }
        }
        cursor.close();
        return gameList;
    }

    public List<Team> getTeams() {
        List<Team> teamList = new ArrayList<>();
        Cursor cursor = getAllTeams();

        if(cursor.getCount() > 0) {
            Team team;
            while (cursor.moveToNext()) {
                Long id = cursor.getLong(0);
                String team_name = cursor.getString(1);
                String sport = cursor.getString(2);

                team = new Team(id, team_name, sport);
                teamList.add(team);
            }
        }
        cursor.close();
        return teamList;
    }

    public List<Profile> getProfiles() {
        List<Profile> profileList = new ArrayList<>();
        Cursor cursor = getAllProfiles();

        if(cursor.getCount() > 0) {
            Profile profile;
            while (cursor.moveToNext()) {
                Long id = cursor.getLong(0);
                String user_name = cursor.getString(1);
                String birthday = cursor.getString(2);
                String jersey_number = cursor.getString(3);
                String position = cursor.getString(4);

                profile = new Profile(id, user_name, birthday,jersey_number,position);
                profileList.add(profile);
            }
        }
        cursor.close();
        return profileList;
    }

    public List<Duty> getDuties() {
        List<Duty> dutyList = new ArrayList<>();
        Cursor cursor = getAllDuties();

        if(cursor.getCount() > 0) {
            Duty duty;
            while (cursor.moveToNext()) {
                Long id = cursor.getLong(0);
                String match_name = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                String location = cursor.getString(4);
                String address = cursor.getString(5);
                String members = cursor.getString(6);

                duty = new Duty(id, match_name, date, time, location, address,members);
                dutyList.add(duty);
            }
        }
        cursor.close();
        return dutyList;
    }


    // These methods return a cursor of all games,duties, profiles and teams from the corresponding
        // tables present in the database

    private Cursor getAllGames() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(GET_ALL_GAME_ST, null);
    }

    private Cursor getAllTeams() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(GET_ALL_TEAM_ST, null);
    }

    private Cursor getAllProfiles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(GET_ALL_PROFILE_ST, null);
    }

    private Cursor getAllDuties() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(GET_ALL_DUTY_ST, null);
    }

    //These methods return a game, duty, team or a profile from the corresponding table in the database
    public Game getGame(Long id){
        SQLiteDatabase db= this.getReadableDatabase();
        Game game=null;
        Cursor cursor = db.rawQuery(GET_GAME_BY_ID, new String[]{id.toString()});

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String match_name = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                String location = cursor.getString(4);
                String address = cursor.getString(5);
                String opponent = cursor.getString(6);
                String eventType = cursor.getString(7);

                game = new Game(id,match_name, date, time, location, address, opponent, eventType);
            }
        }
        cursor.close();
        return game;
    }

    public Team getTeam(Long id){
        SQLiteDatabase db= this.getReadableDatabase();
        Team team=null;
        Cursor cursor = db.rawQuery(GET_TEAM_BY_ID, new String[]{id.toString()});

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String team_name = cursor.getString(1);
                String sport = cursor.getString(2);

                team = new Team(id,team_name, sport);
            }
        }
        cursor.close();
        return team;
    }

    public Profile getProfile(Long id){
        SQLiteDatabase db= this.getReadableDatabase();
        Profile profile=null;
        Cursor cursor = db.rawQuery(GET_PROFILE_BY_ID, new String[]{id.toString()});

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String user_name = cursor.getString(1);
                String birthday = cursor.getString(2);
                String jersey_number = cursor.getString(3);
                String position = cursor.getString(4);

                profile = new Profile(id,user_name,birthday,jersey_number,position);
            }
        }
        cursor.close();
        return profile;
    }

    public Duty getDuty(Long id){
        SQLiteDatabase db= this.getReadableDatabase();
        Duty duty=null;
        Cursor cursor = db.rawQuery(GET_DUTY_BY_ID, new String[]{id.toString()});

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String match_name = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);
                String location = cursor.getString(4);
                String address = cursor.getString(5);
                String members = cursor.getString(6);

                duty = new Duty(id,match_name, date, time, location, address, members);
            }
        }
        cursor.close();
        return duty;
    }
}
