package com.example.teamplanner.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamplanner.R;
import com.example.teamplanner.duties.Duty;
import com.example.teamplanner.listener.OnDutyListener;

import java.util.List;

public class DutyRecyclerViewAdapter extends RecyclerView.Adapter<DutyViewHolder>{

    private List<Duty> dutyList;
    private Context context;
    private OnDutyListener onDutyListener;

    public List<Duty> getDuties() {
        return dutyList;
    }

    public DutyRecyclerViewAdapter(List<Duty> dutyList, Context context, OnDutyListener onDutyListener) {
        this.dutyList = dutyList;
        this.context = context;
        this.onDutyListener=onDutyListener;
    }

    @NonNull
    @Override
    public DutyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());

        View dutyView=inflater.inflate(R.layout.recycler_duty_view,parent,false);

        DutyViewHolder dutyViewHolder=new DutyViewHolder(dutyView,onDutyListener);
        return dutyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DutyViewHolder holder, int position) {
        Duty duty= dutyList.get(position);
        holder.loadDuty(duty);

        holder.bindDutyByButton(duty, onDutyListener);
    }

    @Override
    public int getItemCount() {
        return dutyList.size();
    }

    public void replaceDuty(int position, Duty duty){
        dutyList.set(position,duty);
        notifyItemChanged(position);
    }

    public void deleteItemDuty(int position, Duty duty){
        dutyList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Duty duty) {
        dutyList.add(duty);
        notifyItemInserted(getItemCount());
    }
}
