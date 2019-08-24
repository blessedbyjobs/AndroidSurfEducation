package com.android.blessed.androidsurfeducation.login;

import android.os.AsyncTask;
import android.view.View;

import com.android.blessed.androidsurfeducation.models.LoginRequest;
import com.android.blessed.androidsurfeducation.models.LoginResponse;
import com.android.blessed.androidsurfeducation.network.NetworkService;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> implements LoginPresenterMVP {
    private PreferencesHelper mPreferencesHelper;

    private static final int MIN_PASSWORD_LENGTH = 6;

    public LoginPresenter(PreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public boolean isPasswordShort(int passwordLength) {
        return passwordLength < MIN_PASSWORD_LENGTH;
    }

    @Override
    public boolean isFieldEmpty(int textFieldBoxLength) {
        return textFieldBoxLength == 0;
    }

    @Override
    public boolean checkFields(int textFieldBox1Length, int textFieldBox2Length) {
        return textFieldBox1Length != 0 && textFieldBox2Length != 0;
    }

    @Override
    public void loginUser(String login, String password) {
        new LoginTask(new LoginRequest(login, password)).execute();
    }

    private void sendRequest(LoginRequest mRequest) {
        NetworkService.getInstance().getServerAPI()
                .loginUser(mRequest)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                        LoginResponse mResponse = response.body();
                        mPreferencesHelper.saveData(mResponse);
                        getViewState().moveToMainScreen();
                    }

                    @Override
                    public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                        if (t instanceof IOException) {
                            getViewState().showInternetError();
                        }
                        else {
                            getViewState().showLoginError();
                        }
                    }
                });
    }

    private class LoginTask extends AsyncTask<Void, Void, Void> {
        private LoginRequest mRequest;

        public LoginTask(LoginRequest request) {
            mRequest = request;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getViewState().disableLoginButton();
            getViewState().setProgressBarVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sendRequest(mRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getViewState().setProgressBarVisibility(View.INVISIBLE);
            getViewState().enableLoginButton();
        }
    }
}
