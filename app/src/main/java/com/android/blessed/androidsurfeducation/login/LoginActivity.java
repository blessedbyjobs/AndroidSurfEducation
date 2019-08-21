package com.android.blessed.androidsurfeducation.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.blessed.androidsurfeducation.R;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class LoginActivity extends MvpAppCompatActivity implements LoginView {
    @InjectPresenter
    LoginPresenter mLoginPresenter;

    private TextFieldBoxes mLoginBox;
    private TextFieldBoxes mPasswordBox;
    private ExtendedEditText mLoginBoxText;
    private ExtendedEditText mPasswordBoxText;
    private Button mLoginButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initializeFields();
        setOnClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void initializeFields() {
        mLoginBox = findViewById(R.id.login_field_box);
        mPasswordBox = findViewById(R.id.password_field_box);
        mLoginBoxText = findViewById(R.id.login_edit_text);
        mPasswordBoxText = findViewById(R.id.password_edit_text);
        mProgressBar = findViewById(R.id.progress_bar);
        mLoginButton = findViewById(R.id.login_button);
    }

    @Override
    public void setOnClickListeners() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoginPresenter.checkFields(mLoginBoxText, mPasswordBoxText)) {
                    mLoginPresenter.startAsyncTask();
                }
                else {
                    showErrors();
                }
            }
        });

        mPasswordBox.getEndIconImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void showError(TextFieldBoxes textFieldBox, ExtendedEditText editTextFieldBox) {
        if (mLoginPresenter.isFieldEmpty(editTextFieldBox)) textFieldBox.setError(getResources().getString(R.string.error_text),false);
    }

    @Override
    public void showErrors() {
        showError(mLoginBox, mLoginBoxText);
        showError(mPasswordBox, mPasswordBoxText);
    }

    @Override
    public void disableLoginButton() {
        mLoginButton.setEnabled(false);
        mLoginButton.setTextSize(0);
    }

    @Override
    public void enableLoginButton() {
        mLoginButton.setEnabled(true);
        mLoginButton.setTextSize(14);
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }
}
