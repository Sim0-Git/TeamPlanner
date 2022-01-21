package com.example.teamplanner.duties;

import android.os.Build;

import androidx.annotation.RequiresApi;
import java.io.Serializable;
import java.util.Objects;

//Class Duty with all the attributes, getters, setters and constructor
public class Duty implements Serializable {

    public static final String DUTY_KEY="duty_key";

    private Long id;
    private String matchName;
    private String time;
    private String date;
    private String location;
    private String address;
    private String members;

    public Duty() {
    }

    public Duty(Long id,String matchName, String time, String date, String location, String address,String members) {
        this.id=id;
        this.matchName = matchName;
        this.time = time;
        this.date = date;
        this.location = location;
        this.address = address;
        this.members=members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Duty{" +
                "id=" + id +
                ", matchName='" + matchName + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", members='" + members + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duty duty = (Duty) o;
        return id.equals(duty.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
