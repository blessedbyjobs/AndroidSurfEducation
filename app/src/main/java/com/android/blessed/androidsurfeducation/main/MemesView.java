package com.android.blessed.androidsurfeducation.main;


import com.arellomobile.mvp.MvpView;

public interface MemesView extends MvpView {
    void showRequestError();
    void showProgressBar();
    void hideProgressBar();
    void showQueryError();
}
