package com.example.teamplanner.duties;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamplanner.R;
import com.example.teamplanner.service.DutyDataService;

public class RecyclerItemDutyActivity extends AppCompatActivity {

    private DutyDataService dutyDataService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_duty_view);

        dutyDataService=new DutyDataService();
        dutyDataService.init(this);
        dutyDataService.getDuties();
    }
}
