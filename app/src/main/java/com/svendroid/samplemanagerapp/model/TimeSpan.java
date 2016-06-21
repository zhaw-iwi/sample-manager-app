package com.svendroid.samplemanagerapp.model;

/**
 * Created by svhe on 05.05.2016.
 */
public class TimeSpan {
    private String cronStart;
    private String cronEnd;
    private int repeats;

    public String getCronStart() {
        return cronStart;
    }

    public void setCronStart(String cronStart) {
        this.cronStart = cronStart;
    }

    public String getCronEnd() {
        return cronEnd;
    }

    public void setCronEnd(String cronEnd) {
        this.cronEnd = cronEnd;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }
}
