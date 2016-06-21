package com.svendroid.samplemanagerapp.model;

/**
 * Created by svhe on 05.05.2016.
 */
public class ProjectBasic {
    private String _id;
    private String name;
    private String created;
    private String[] users;
    private String[] measures;
    private String[] triggers;
    private String imageUrl;
    private boolean checkedIn;
    private boolean dirty = false;

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

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String[] getMeasures() {
        return measures;
    }

    public void setMeasures(String[] measure) {
        this.measures = measure;
    }

    public String[] getTriggers() {
        return triggers;
    }

    public void setTriggers(String[] triggers) {
        this.triggers = triggers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
