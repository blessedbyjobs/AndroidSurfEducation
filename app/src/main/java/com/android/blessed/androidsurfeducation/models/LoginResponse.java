package com.android.blessed.androidsurfeducation.models;

import com.google.gson.annotations.Expose;

public class LoginResponse {
    @Expose
    private String accessToken;

    @Expose
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
