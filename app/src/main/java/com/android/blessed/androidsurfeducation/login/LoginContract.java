package com.android.blessed.androidsurfeducation.login;

public interface LoginContract {
    interface Presenter {
        boolean isPasswordShort(int length);
        boolean isFieldEmpty(int textFieldBoxLength);
        boolean checkFields(int textFieldBox1Length, int textFieldBox2Length);
        void loginUser(String login, String password);
    }

    interface Model {

    }
}
