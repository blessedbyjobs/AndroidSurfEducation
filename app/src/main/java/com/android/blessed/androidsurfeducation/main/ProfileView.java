package com.android.blessed.androidsurfeducation.main;

import com.android.blessed.androidsurfeducation.models.Meme;
import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface ProfileView extends MvpView {
    void updateMemes(List<Meme> memeList);
    void showProgressBar();
    void hideProgressBar();
    void showLogoutDialog();
}
