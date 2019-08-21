package com.android.blessed.androidsurfeducation.login;

import android.os.AsyncTask;
import android.view.View;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.concurrent.TimeUnit;

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
    public boolean isFieldEmpty(ExtendedEditText textFieldBox) {
        return textFieldBox.getText().length() == 0;
    }

    @Override
    public boolean checkFields(ExtendedEditText textFieldBox1, ExtendedEditText textFieldBox2) {
        return !isFieldEmpty(textFieldBox1) && !isFieldEmpty(textFieldBox2);
    }

    @Override
    public void startAsyncTask() {
        new GreatAsyncTask().execute();
    }

    private class GreatAsyncTask extends AsyncTask<Void, Void, Void> {
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
            } catch (InterruptedException e){
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
