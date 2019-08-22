package com.android.blessed.androidsurfeducation.login;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.blessed.androidsurfeducation.models.LoginResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper implements LoginModel {
    private final SharedPreferences mPref;

    public static final String PREF_FILE_NAME = "surf_android_education_pref_file";

    private static final String TOKEN = "token";
    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String USERDESCRIPTION = "userDescription";

    @Inject
    public PreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private void clear() {
        mPref.edit().clear().apply();
    }

    private void putField(String name, String value) {
        mPref.edit().putString(name, value).apply();
    }

    @Override
    public String getToken() {
        return mPref.getString(TOKEN, null);
    }

    @Override
    public int getId() {
        return Integer.parseInt(mPref.getString(ID, null));
    }

    @Override
    public String getUsername() {
        return mPref.getString(USERNAME, null);
    }

    @Override
    public String getFirstName() {
        return mPref.getString(FIRSTNAME, null);
    }

    @Override
    public String getLastName() {
        return mPref.getString(LASTNAME, null);
    }

    @Override
    public String getUserDescription() {
        return mPref.getString(USERDESCRIPTION, null);
    }

    @Override
    public void saveData(LoginResponse response) {
        putField(TOKEN, response.getAccessToken());
        putField(ID, String.valueOf(response.getUserInfo().getId()));
        putField(USERNAME, response.getUserInfo().getUsername());
        putField(FIRSTNAME, response.getUserInfo().getFirstName());
        putField(LASTNAME, response.getUserInfo().getLastName());
        putField(USERDESCRIPTION, response.getUserInfo().getUserDescription());
    }
}
