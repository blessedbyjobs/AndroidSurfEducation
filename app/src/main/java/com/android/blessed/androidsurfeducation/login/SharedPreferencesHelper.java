package com.android.blessed.androidsurfeducation.login;

import com.android.blessed.androidsurfeducation.models.LoginResponse;

public interface SharedPreferencesHelper {
    void saveData(LoginResponse response);

    String getString(String fieldName);
    int getInt(String fieldName);
}
