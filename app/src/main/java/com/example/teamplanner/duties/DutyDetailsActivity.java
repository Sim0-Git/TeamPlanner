package com.example.teamplanner.duties;

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
import com.example.teamplanner.games.Game;
import com.example.teamplanner.games.GameDetailsActivity;
import com.example.teamplanner.recycler_view.DutyRecyclerViewAdapter;
import com.example.teamplanner.recycler_view.GameRecyclerViewAdapter;
import com.example.teamplanner.service.DutyDataService;
import com.example.teamplanner.service.GameDataService;

import java.util.List;

public class DutyDetailsActivity extends AppCompatActivity {

    Duty duty;
    EditText editTextDateDutyDetails;
    EditText editTextTimeDutyDetails;
    EditText editTextLocationDutyDetails;
    EditText editTextAddressDutyDetails;
    TextView textViewMembersDutyDetails;

    Long id;
    String matchName;
    String date;
    String time;
    String location;
    String address;
    String members;

    Button updateButton;
    Button deleteButton;
    PopupMenu popupMenu;

    DutyDataService dutyDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_details);

        dutyDataService=new DutyDataService();
        dutyDataService.init(this);
        dutyDataService.getDuties();

        editTextDateDutyDetails=findViewById(R.id.editTextDutyDateDetails);
        editTextTimeDutyDetails=findViewById(R.id.editTextDutyTimeDetails);
        editTextLocationDutyDetails=findViewById(R.id.editTextDutyLocationDetails);
        editTextAddressDutyDetails=findViewById(R.id.editTextDutyAddressDetails);
        textViewMembersDutyDetails=findViewById(R.id.editTextMembersDutyeDetails);

        updateButton= findViewById(R.id.updateButtonDutyDetail);
        deleteButton= findViewById(R.id.deleteButtonDutyDetail);

        Intent intentThatCalled=getIntent();
        if(intentThatCalled.hasExtra(Duty.DUTY_KEY)){
            duty=(Duty) intentThatCalled.getSerializableExtra(Duty.DUTY_KEY);

            id=duty.getId();
            matchName=duty.getMatchName();
            editTextDateDutyDetails.setText(duty.getDate());
            editTextTimeDutyDetails.setText(duty.getTime());
            editTextLocationDutyDetails.setText(duty.getLocation());
            editTextAddressDutyDetails.setText(duty.getAddress());
            textViewMembersDutyDetails.setText(duty.getMembers());
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentWithData();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentWithData();
            }
        });

        Button viewAllButton=findViewById(R.id.viewAllDutiesDetails);
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAllDuties(v);
            }
        });

        textViewMembersDutyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMembersPopupMenu(v);
            }
        });
    }

    private void showMembersPopupMenu(View v) {
        popupMenu=new PopupMenu(DutyDetailsActivity.this,v);
        popupMenu.getMenu().add("Empty");
        popupMenu.getMenu().add("1");
        popupMenu.getMenu().add("2");
        popupMenu.getMenu().add("3");
        popupMenu.getMenu().add("4");
        popupMenu.getMenu().add("5");
        popupMenu.show();
        //Set the editText with the user choice after clicking and option in the menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                textViewMembersDutyDetails.setText(item.getTitle());
                if(textViewMembersDutyDetails.getText()=="Empty"){
                    textViewMembersDutyDetails.setText("");
                }
                else
                    textViewMembersDutyDetails.setText(item.getTitle());
                return false;
            }
        });
    }

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
    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show(); }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setIntentWithData() {
        date= editTextDateDutyDetails.getText().toString();
        time= editTextTimeDutyDetails.getText().toString();
        location= editTextLocationDutyDetails.getText().toString();
        address= editTextAddressDutyDetails.getText().toString();
        members= textViewMembersDutyDetails.getText().toString();

        duty=new Duty(id,matchName,time,date,location,address,members);

        Intent goBack= new Intent();
        goBack.putExtra(Duty.DUTY_KEY,duty);

        if(updateButton.isPressed()){
            setResult(Constants.MODIFY_DUTY_DETAILS_ACTIVITY_CODE,goBack);
            finish();
        }else if(deleteButton.isPressed()){
            setResult(Constants.DELETE_DUTY_DETAILS_ACTIVITY_CODE,goBack);
            finish();
        }
    }
}