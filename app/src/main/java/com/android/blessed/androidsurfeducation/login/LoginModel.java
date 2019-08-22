package com.android.blessed.androidsurfeducation.login;

import com.android.blessed.androidsurfeducation.models.LoginResponse;

public interface LoginModel {
    void saveData(LoginResponse response);

    String getToken();
    int getId();
    String getUsername();
    String getFirstName();
    String getLastName();
    String getUserDescription();
}
