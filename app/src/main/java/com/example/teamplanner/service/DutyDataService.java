package com.example.teamplanner.service;

import android.content.Context;

import com.example.teamplanner.database.MemoSportDatabaseHelper;
import com.example.teamplanner.duties.Duty;
import com.example.teamplanner.games.Game;

import java.util.List;

public class DutyDataService {
    private MemoSportDatabaseHelper sqlite;

    public void init(Context context) {
        sqlite = sqlite.getInstance(context);
    }
    //INSERT A Duty INTO THE DB AND RETURN THE ID OF THE duty
    public Long add(Duty duty) {
        return sqlite.insertDuty(duty.getMatchName(),duty.getTime(),duty.getDate(),duty.getLocation(),duty.getAddress(),duty.getMembers());
    }

    //Delete a duty by using the ID
    public boolean delete(Duty duty) {
        return sqlite.deleteDuty(duty.getId());
    }

    public boolean update(Duty duty) {
        return sqlite.updateDuty(duty.getId(),duty.getMatchName(),duty.getTime(),duty.getDate(),duty.getLocation(),duty.getAddress(),duty.getMembers());
    }

    //Return all duties registered in the duty table
    public List<Duty> getDuties() {
        List<Duty> duties = sqlite.getDuties();
        return duties;
    }

    //return the duty id
    public  Duty getDuty(Long id){
        return sqlite.getDuty(id);
    }
}
