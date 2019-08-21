package com.android.blessed.androidsurfeducation.models;

import com.google.gson.annotations.Expose;

public class UserInfo {
    @Expose
    private int id;

    @Expose
    private String username;

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private String userDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
}
