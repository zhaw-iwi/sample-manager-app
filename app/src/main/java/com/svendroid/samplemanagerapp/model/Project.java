package com.svendroid.samplemanagerapp.model;

/**
 * Created by svhe on 05.05.2016.
 */
public class Project {
    private String _id;
    private String name;
    private String created;
    private TimeSpan start;
    private String end;
    private User[] users;
    private Measure[] measure;
    private Trigger[] triggers;
    private String imageUrl;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public TimeSpan getStart() {
        return start;
    }

    public void setStart(TimeSpan start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Measure[] getMeasure() {
        return measure;
    }

    public void setMeasure(Measure[] measure) {
        this.measure = measure;
    }

    public Trigger[] getTriggers() {
        return triggers;
    }

    public void setTriggers(Trigger[] triggers) {
        this.triggers = triggers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
