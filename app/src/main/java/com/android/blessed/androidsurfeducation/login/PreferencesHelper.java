package com.android.blessed.androidsurfeducation.login;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.blessed.androidsurfeducation.models.LoginResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper implements SharedPreferencesHelper {
    private SharedPreferences mPref;

    public static final String PREF_FILE_NAME = "surf_android_education_pref_file";

    public final String TOKEN = "token";
    public final String ID = "id";
    public final String USERNAME = "username";
    public final String FIRSTNAME = "firstName";
    public final String LASTNAME = "lastName";
    public final String USERDESCRIPTION = "userDescription";

    @Inject
    public PreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private void clear() {
        mPref.edit().clear().apply();
    }

    private void putString(String name, String value) {
        mPref.edit().putString(name, value).apply();
    }

    private void putInt(String name, int value) {
        mPref.edit().putInt(name, value).apply();
    }

    @Override
    public String getString(String fieldName) {
        return mPref.getString(fieldName, null);
    }

    @Override
    public int getInt(String fieldName) {
        return mPref.getInt(fieldName, 0);
    }

    @Override
    public void saveData(LoginResponse response) {
        putString(TOKEN, response.getAccessToken());
        putInt(ID, response.getUserInfo().getId());
        putString(USERNAME, response.getUserInfo().getUsername());
        putString(FIRSTNAME, response.getUserInfo().getFirstName());
        putString(LASTNAME, response.getUserInfo().getLastName());
        putString(USERDESCRIPTION, response.getUserInfo().getUserDescription());
    }
}
