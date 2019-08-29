package com.android.blessed.androidsurfeducation.main;

import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.android.blessed.androidsurfeducation.db.MemeDAO;
import com.android.blessed.androidsurfeducation.db.MemesDatabase;
import com.android.blessed.androidsurfeducation.global.GlobalApplication;
import com.android.blessed.androidsurfeducation.login.PreferencesHelper;
import com.android.blessed.androidsurfeducation.models.LoginRequest;
import com.android.blessed.androidsurfeducation.models.LoginResponse;
import com.android.blessed.androidsurfeducation.models.UserInfo;
import com.android.blessed.androidsurfeducation.network.NetworkService;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView> implements ProfilePresenterMVP {
    private PreferencesHelper mPreferencesHelper = new PreferencesHelper(GlobalApplication.getAppContext());
    private MemesDatabase mMemesDatabase = GlobalApplication.getDatabase();
    private MemeDAO mMemeDAO = mMemesDatabase.mMemeDAO();

    @Override
    public UserInfo getUser() {
        UserInfo user = new UserInfo();
        user.setUsername(mPreferencesHelper.getString(mPreferencesHelper.USERNAME));
        user.setUserDescription(mPreferencesHelper.getString(mPreferencesHelper.USERDESCRIPTION));
        return user;
    }

    @Override
    public void logoutUser() {
        sendRequest();
    }

    @Override
    public void loadLocalMemes() {
        MemesLoader memesLoader = new MemesLoader();
        memesLoader.loadInBackground();
    }

    private void sendRequest() {
        NetworkService.getInstance().getServerAPI()
                .logoutUser().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getViewState().showLogoutDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public class MemesLoader extends AsyncTaskLoader {
        public MemesLoader() {
            super(GlobalApplication.getAppContext());
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
        }

        @Override
        public List<String> loadInBackground() {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                Log.i("Memes", String.valueOf(mMemeDAO.getAll().size()));
                getViewState().updateMemes(mMemeDAO.getAll());
            });
            return null;
        }
    }
}
