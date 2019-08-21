package com.android.blessed.androidsurfeducation.login;

import com.arellomobile.mvp.MvpView;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public interface LoginView extends MvpView {
    void initializeFields();
    void setOnClickListeners();
    void showError(TextFieldBoxes textFieldBox, ExtendedEditText editTextFieldBox);
    void showErrors();
    void disableLoginButton();
    void enableLoginButton();
    void setProgressBarVisibility(int visibility);
}
