package com.svendroid.samplemanagerapp.model;

import java.util.Date;

/**
 * Created by svhe on 05.05.2016.
 */
public class TriggerInstance {
    private String _id;
    private Date lastTrigger;
    private Date nextTrigger;
    private int triggersLeft;
    private String user;
    private MeasureWithoutTrigger measure;
    private Trigger trigger;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getLastTrigger() {
        return lastTrigger;
    }

    public void setLastTrigger(Date lastTrigger) {
        this.lastTrigger = lastTrigger;
    }

    public Date getNextTrigger() {
        return nextTrigger;
    }

    public void setNextTrigger(Date nextTrigger) {
        this.nextTrigger = nextTrigger;
    }

    public int getTriggersLeft() {
        return triggersLeft;
    }

    public void setTriggersLeft(int triggersLeft) {
        this.triggersLeft = triggersLeft;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public MeasureWithoutTrigger getMeasure() {
        return measure;
    }

    public void setMeasure(MeasureWithoutTrigger measure) {
        this.measure = measure;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
