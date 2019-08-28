package com.android.blessed.androidsurfeducation.main;

import com.arellomobile.mvp.MvpView;

public interface CreateMemeView extends MvpView {
    void preExecute();
    void postExecute();
    void showProgressBar();
    void hideProgressBar();

    void closeActivity();
    void showDialog();
}
