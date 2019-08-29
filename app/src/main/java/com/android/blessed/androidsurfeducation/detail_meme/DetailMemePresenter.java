package com.android.blessed.androidsurfeducation.detail_meme;

import com.android.blessed.androidsurfeducation.global.GlobalApplication;
import com.android.blessed.androidsurfeducation.login.PreferencesHelper;
import com.android.blessed.androidsurfeducation.models.UserInfo;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class DetailMemePresenter extends MvpPresenter<DetailMemeView> implements DetailMemePresenterMVP {
    private UserInfo mUser = new UserInfo();
    private PreferencesHelper mPreferencesHelper = new PreferencesHelper(GlobalApplication.getAppContext());

    @Override
    public UserInfo getUserInfo() {
        mUser.setId(mPreferencesHelper.getInt(mPreferencesHelper.ID));
        mUser.setUsername(mPreferencesHelper.getString(mPreferencesHelper.USERNAME));
        mUser.setFirstName(mPreferencesHelper.getString(mPreferencesHelper.FIRSTNAME));
        mUser.setLastName(mPreferencesHelper.getString(mPreferencesHelper.LASTNAME));
        mUser.setUserDescription(mPreferencesHelper.getString(mPreferencesHelper.USERDESCRIPTION));
        return mUser;
    }
}
