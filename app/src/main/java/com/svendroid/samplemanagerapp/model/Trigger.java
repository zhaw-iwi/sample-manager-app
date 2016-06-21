package com.svendroid.samplemanagerapp.model;

/**
 * Created by svhe on 05.05.2016.
 */
public class Trigger {
    private String _id;
    private String alias;
    private String type;
    private TimeSpan timeSpan;
    private String healthTrigger;
    private String socialTrigger;
    private String placeTrigger;
    private Trigger[] children;
    private String project;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TimeSpan getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getHealthTrigger() {
        return healthTrigger;
    }

    public void setHealthTrigger(String healthTrigger) {
        this.healthTrigger = healthTrigger;
    }

    public String getSocialTrigger() {
        return socialTrigger;
    }

    public void setSocialTrigger(String socialTrigger) {
        this.socialTrigger = socialTrigger;
    }

    public String getPlaceTrigger() {
        return placeTrigger;
    }

    public void setPlaceTrigger(String placeTrigger) {
        this.placeTrigger = placeTrigger;
    }

    public Trigger[] getChildren() {
        return children;
    }

    public void setChildren(Trigger[] children) {
        this.children = children;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
