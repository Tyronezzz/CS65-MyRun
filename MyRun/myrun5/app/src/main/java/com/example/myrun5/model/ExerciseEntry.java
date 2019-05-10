/*
 * @author  Tao Hou
 * @version 1.0
 * @since   2019-04-15
 */

package com.example.myrun5.model;

import java.io.Serializable;


// constructor of ExerciseEntry
public class ExerciseEntry implements Serializable {

    private long id;
    private String activity_type;
    private String date_time;
    private String duration;
    private String distance;
    private String calorie;
    private String heart_rate;
    private String comment;
    private String avg_pace;
    private String input_type;
    private String avg_speed;
    private String climb;
    private String privacy;
    private String gps;
    private String deleted;
    private String synced;
    private String boarded;


    public ExerciseEntry()
    {

    }

    public ExerciseEntry(long id, String input_type, String activity_type, String date_time, String duration,
                         String distance, String avg_page, String avg_speed, String calorie, String climb,
                         String heart_rate, String comment, String privacy, String gps, String synced, String deleted, String boarded){
        this.id = id;
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
        this.synced = synced;
        this.deleted = deleted;
        this.boarded = boarded;

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

    public void setActType(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getDateTime() {
        return date_time;
    }

    public void setDateTime(String date_time) {
        this.date_time = date_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getHeartrate() {
        return heart_rate;
    }

    public void setHeartrate(String heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInputType() {
        return input_type;
    }

    public void setInputType(String input_type) {
        this.input_type = input_type;
    }

    public String getAvgPace() {
        return avg_pace;
    }

    public void setAvgPace(String avg_pace) {
        this.avg_pace = avg_pace;
    }

    public String getAvgSpeed() {
        return avg_speed;
    }

    public void setAvgSpeed(String avg_speed) {
        this.avg_speed = avg_speed;
    }

    public String getClimb() {
        return climb;
    }

    public void setClimb(String climb) {
        this.climb = climb;
    }

    public String getGps() {
        return gps;
    }

    public void setCGps(String gps) {
        this.gps = gps;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getSynced() {
        return synced;
    }

    public void setSynced(String synced) {
        this.synced = synced;
    }

    public String getBoarded() {
        return boarded;
    }

    public void setBoarded(String boarded) {
        this.boarded = boarded;
    }
}
