package com.svendroid.samplemanagerapp.model;

/**
 * Created by svhe on 05.05.2016.
 */
public class MeasureExtended {
    private String _id;
    private String alias;
    private String type;
    private String text;
    private String[] values;
    private String[] children;
    private SimpleProject project;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getChildren() {
        return children;
    }

    public void setChildren(String[] children) {
        this.children = children;
    }

    public SimpleProject getProject() {
        return project;
    }

    public void setProject(SimpleProject project) {
        this.project = project;
    }

    public class SimpleProject {
        private String _id;
        private User[] users;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public User[] getUsers() {
            return users;
        }

        public void setUsers(User[] users) {
            this.users = users;
        }
    }
}
