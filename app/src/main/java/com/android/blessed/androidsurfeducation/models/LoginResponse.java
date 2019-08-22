package com.android.blessed.androidsurfeducation.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("userInfo")
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
