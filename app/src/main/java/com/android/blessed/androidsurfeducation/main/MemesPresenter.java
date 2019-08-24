package com.android.blessed.androidsurfeducation.main;

import com.android.blessed.androidsurfeducation.models.MemesResponse;
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
        sendRequest();
    }

    private void sendRequest() {
        NetworkService.getInstance().getServerAPI()
                .getMemes()
                .enqueue(new Callback<List<MemesResponse>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<MemesResponse>> call, @NotNull Response<List<MemesResponse>> response) {
                        List<MemesResponse> mMemesList = response.body();
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<MemesResponse>> call, @NotNull Throwable t) {
                        if (t instanceof IOException) {
                            getViewState().showRequestError();
                        } else getViewState().showQueryError();
                    }
                });
    }
}
