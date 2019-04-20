package com.example.myrun3.model;

import java.io.Serializable;

public class ExerciseEntry implements Serializable {
    private long id;
    private String activity_type;
    private String date_time;
    private String duration;
    private String distance;
    private String calorie;
    private String heart_rate;
    private String comment;

    public ExerciseEntry(long id, String input_type, String activity_type, String date_time, String duration,
                         String distance, String avg_page, String avg_speed, String calorie, String climb,
                         String heart_rate, String comment, String privacy, String gps){
        this.id = id;
        //String input_type1 = input_type;
        this.activity_type = activity_type;
        this.date_time = date_time;
        this.duration = duration;
        this.distance = distance;
//        String avg_pace = avg_page;
//        String avg_speed1 = avg_speed;
        this.calorie = calorie;
//        String climb1 = climb;
        this.heart_rate = heart_rate;
        this.comment = comment;
//        String privacy = privacy;
//        String gps = gps;

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getActType() {
        return activity_type;
    }

    public String getDateTime() {
        return date_time;
    }

    public String getDuration() {
        return duration;
    }

    public String getDistance() {
        return distance;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getHeartrate() {
        return heart_rate;
    }

    public String getComment() {
        return comment;
    }

//    public String getInputType() {
//        return this.input_type;
//    }
//
//    public String getAvgPace() {
//        return avg_pace;
//    }
//
//    public String getAvgSpeed() {
//        return avg_speed;
//    }
//
//    public String getClimb() {
//        return climb;
//    }
}
