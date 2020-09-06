package com.campus.sport.bean;

/**
 * Created by Administrator on 2019/5/25.
 */

public class DataHistory {
    public DataHistory() {
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public DataHistory(String time, String stepCount, String distance) {

        this.time = time;
        this.stepCount = stepCount;
        this.distance = distance;
    }

    private String time,stepCount,distance;
}
