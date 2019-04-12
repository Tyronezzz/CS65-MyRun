package com.example.myrun3.model;

public class ExerciseEntry {
    private long id;
    private String input_type;
    private String activity_type;
    private String date_time;
    private String duration;
    private String distance;
    private String avg_pace;
    private String avg_speed;
    private String calorie;
    private String climb;
    private String heart_rate;
    private String comment;
    private String privacy;
    private String gps;


    ExerciseEntry(String input_type, String activity_type, String date_time, String duration,
                  String distance, String avg_page, String avg_speed, String calorie, String climb,
                  String heart_rate, String comment, String privacy, String gps){
        this.input_type = input_type;
        this.activity_type = activity_type;
        this.date_time = date_time;
        this.duration = duration;
        this.distance = distance;
        this.avg_pace = avg_page;
        this.avg_speed = avg_speed;
        this.calorie = calorie;
        this.climb = climb;
        this.heart_rate = heart_rate;
        this.comment = comment;
        this.privacy = privacy;
        this.gps = gps;

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setExerciseEntry(){

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


    public String getInputType() {
        return this.input_type;
    }

    public String getAvgPace() {
        return avg_pace;
    }

    public String getAvgSpeed() {
        return avg_speed;
    }

    public String getClimb() {
        return climb;
    }
}
