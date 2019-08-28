package com.android.blessed.androidsurfeducation.main;

import com.android.blessed.androidsurfeducation.global.GlobalApplication;
import com.android.blessed.androidsurfeducation.login.PreferencesHelper;
import com.android.blessed.androidsurfeducation.models.UserInfo;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView> implements ProfilePresenterMVP {
    private PreferencesHelper mPreferencesHelper = new PreferencesHelper(GlobalApplication.getAppContext());

    @Override
    public UserInfo getUser() {
        UserInfo user = new UserInfo();
        user.setUsername(mPreferencesHelper.getString(mPreferencesHelper.USERNAME));
        user.setUserDescription(mPreferencesHelper.getString(mPreferencesHelper.USERDESCRIPTION));
        return user;
    }
}
