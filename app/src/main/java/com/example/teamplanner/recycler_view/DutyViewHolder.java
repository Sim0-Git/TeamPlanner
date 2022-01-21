package com.example.teamplanner.recycler_view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamplanner.R;
import com.example.teamplanner.duties.Duty;
import com.example.teamplanner.listener.OnDutyListener;

public class DutyViewHolder extends RecyclerView.ViewHolder {

    public final TextView teamNameTextView;
    public final TextView timeTextView;
    public final TextView dateTextView;
    public final TextView locationTextView;
    public final TextView addressTextView;
    public final TextView membersTextView;

    private OnDutyListener onDutyListener;
    private Button onButtonListener;

    public DutyViewHolder(@NonNull View itemView,OnDutyListener onDutyListener) {
        super(itemView);

        teamNameTextView=itemView.findViewById(R.id.dutyTeamTextView);
        timeTextView=itemView.findViewById(R.id.dutyTimeTextView);
        dateTextView=itemView.findViewById(R.id.dutyDateTextView);
        locationTextView=itemView.findViewById(R.id.dutyLocationTextView);
        addressTextView= itemView.findViewById(R.id.dutyAddressTextView);
        membersTextView= itemView.findViewById(R.id.dutyMembersTxtView);

        this.onDutyListener=onDutyListener;

        onButtonListener=itemView.findViewById(R.id.editDutyButton);
    }

    public void loadDuty(Duty duty){

        this.teamNameTextView.setText(duty.getMatchName());
        this.timeTextView.setText(duty.getTime());
        this.dateTextView.setText(duty.getDate());
        this.locationTextView.setText(duty.getLocation());
        this.addressTextView.setText(duty.getAddress());
        this.membersTextView.setText(duty.getMembers());
    }

    //This method gets activated when the button is pressed instead of all the item view
    public void bindDutyByButton(Duty duty, OnDutyListener onDutyListener){
        this.onButtonListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDutyListener.onDutyClick(duty);
            }
        });

    }
}
