package com.android.blessed.androidsurfeducation.main;

import androidx.loader.content.AsyncTaskLoader;

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
        MemesLoader Loader = new MemesLoader();
        Loader.onStartLoading();
        Loader.loadInBackground();
        Loader.onEndLoading();
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

        @Override
        protected void onStartLoading() {
            getViewState().preExecute();
        }

        @Override
        public List<String> loadInBackground() {
            sendRequest();
            return null;
        }

        public void onEndLoading() {
            getViewState().postExecute();
        }
    }

}
