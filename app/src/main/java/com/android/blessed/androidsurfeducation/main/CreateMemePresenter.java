package com.android.blessed.androidsurfeducation.main;

import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.android.blessed.androidsurfeducation.db.MemeDAO;
import com.android.blessed.androidsurfeducation.db.MemesDatabase;
import com.android.blessed.androidsurfeducation.global.GlobalApplication;
import com.android.blessed.androidsurfeducation.models.Meme;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@InjectViewState
public class CreateMemePresenter extends MvpPresenter<CreateMemeView> implements CreateMemePresenterMVP {
    MemesDatabase mMemesDatabase = GlobalApplication.getDatabase();
    MemeDAO mMemeDAO = mMemesDatabase.mMemeDAO();

    @Override
    public void insertMemeIntoDatabase(Meme meme) {
        CreateMemePresenter.MemesLoader Loader = new CreateMemePresenter.MemesLoader(meme);
        Loader.onStartLoading();
        Loader.loadInBackground();
        Loader.onEndLoading();
    }

    public class MemesLoader extends AsyncTaskLoader {
        private Meme mMeme;

        public MemesLoader(Meme meme) {
            super(GlobalApplication.getAppContext());
            mMeme = meme;
        }

        @Override
        protected void onStartLoading() {
            getViewState().preExecute();
        }

        @Override
        public List<String> loadInBackground() {
            if (mMeme != null) {
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    mMemeDAO.insert(mMeme);
                    getViewState().closeActivity();
                });
            } else {
                Log.i("Meme", "CreationError");
            }
            return null;
        }

        public void onEndLoading() {
            getViewState().postExecute();
        }
    }
}
