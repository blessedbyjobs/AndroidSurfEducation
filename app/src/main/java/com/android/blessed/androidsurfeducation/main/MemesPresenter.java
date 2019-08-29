package com.android.blessed.androidsurfeducation.main;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.android.blessed.androidsurfeducation.global.GlobalApplication;
import com.android.blessed.androidsurfeducation.models.Meme;
import com.android.blessed.androidsurfeducation.network.NetworkService;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class MemesPresenter extends MvpPresenter<MemesView> implements MemesPresenterMVP {
    public MemesPresenter() {

    }

    @Override
    public void loadMemes() {
        MemesLoader memesLoader = new MemesLoader();
        memesLoader.startLoading();
        memesLoader.loadInBackground();
        memesLoader.stopLoading();
    }

    private void sendRequest() {
        NetworkService.getInstance().getServerAPI()
                .getMemes()
                .enqueue(new Callback<List<Meme>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Meme>> call, @NotNull Response<List<Meme>> response) {
                        getViewState().updateMemes(response.body());
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Meme>> call, @NotNull Throwable t) {
                        getViewState().showQueryError();
                        if (t instanceof IOException) {
                            getViewState().showRequestError();
                        }
                    }
                });
    }

    public class MemesLoader extends AsyncTaskLoader {
        public MemesLoader() {
            super(GlobalApplication.getAppContext());
        }

        @Nullable
        @Override
        public Object loadInBackground() {
            sendRequest();
            return null;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            getViewState().preExecute();
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            getViewState().postExecute();
        }
    }

}
