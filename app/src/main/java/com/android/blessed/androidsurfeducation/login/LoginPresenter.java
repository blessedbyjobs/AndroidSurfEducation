package com.android.blessed.androidsurfeducation.login;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.android.blessed.androidsurfeducation.models.LoginRequest;
import com.android.blessed.androidsurfeducation.models.LoginResponse;
import com.android.blessed.androidsurfeducation.network.NetworkService;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> implements LoginContract.Presenter {
    private static final int MIN_PASSWORD_LENGTH = 6;

    public LoginPresenter() { }

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
        new Login(new LoginRequest(login, password)).execute();
    }

    private class Login extends AsyncTask<Void, Void, Void> {
        private LoginRequest mRequest;

        public Login(LoginRequest request) {
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
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getViewState().setProgressBarVisibility(View.INVISIBLE);
            getViewState().enableLoginButton();
        }
    };
}
