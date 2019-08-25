package com.android.blessed.androidsurfeducation.main;


import com.android.blessed.androidsurfeducation.models.Meme;
import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface MemesView extends MvpView {
    void showRequestError();
    void showProgressBar();
    void hideProgressBar();
    void showQueryError();

    void updateMemes(List<Meme> mMemes);
    void preExecute();
    void postExecute();
}
