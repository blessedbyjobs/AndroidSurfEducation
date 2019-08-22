package com.android.blessed.androidsurfeducation.login;

import com.arellomobile.mvp.MvpView;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public interface LoginView extends MvpView {
    void showError(TextFieldBoxes textFieldBox, ExtendedEditText editTextFieldBox);
    void showErrors();

    void disableLoginButton();
    void enableLoginButton();

    void setProgressBarVisibility(int visibility);

    void unfocusFields();

    void showPassword();
    void hidePassword();
    void showPasswordError();

    void showKeyboard();
    void hideKeyboard(TextFieldBoxes textFieldBox);

    void moveToMainScreen();

    void showLoginError();
}