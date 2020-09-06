package com.campus.sport.bean;

import java.util.Comparator;

/**
 * Created by Administrator on 2019/5/23.
 */

public class DataRanking {
    private int index;

    public DataRanking(int index, String name, int stepCount, String integral) {
        this.index = index;
        this.name = name;
        this.stepCount = stepCount;
        this.integral = integral;
    }

    public int getIndex() {

        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private String name;
    private int stepCount;
    private String integral;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
    public static class stepCountAsc implements Comparator<DataRanking>{
        @Override
        public int compare(DataRanking t0, DataRanking t1) {
            return -(t0.getStepCount()-t1.getStepCount());
        }
    }

}
