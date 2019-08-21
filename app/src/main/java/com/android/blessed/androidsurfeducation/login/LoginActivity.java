package com.android.blessed.androidsurfeducation.login;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private boolean mPasswordIsHide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        initializeFields();
        setOnClickListeners();
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
        mLoginBoxText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) showKeyboard();
                else hideKeyboard(mLoginBox);
            }
        });

        mPasswordBoxText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) showKeyboard();
                else hideKeyboard(mPasswordBox);
            }
        });

        mLoginBoxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyboard();
            }
        });

        mPasswordBoxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyboard();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoginPresenter.checkFields(mLoginBoxText, mPasswordBoxText)) {
                    if (!mLoginPresenter.isPasswordShort(mPasswordBoxText.getText().length())) {
                        mLoginPresenter.startAsyncTask();
                    } else {
                        showPasswordError();
                    }
                } else {
                    showErrors();
                    unfocusFields();
                }
            }
        });

        mPasswordBox.getEndIconImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPasswordIsHide) {
                    showPassword();
                } else {
                    hidePassword();
                }
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

    @Override
    public void showPasswordError() {
        mPasswordBox.setError(getResources().getString(R.string.password_error_text), false);
    }

    @Override
    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void hideKeyboard(TextFieldBoxes textFieldBox) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textFieldBox.getWindowToken(), 0);
    }

    @Override
    public void unfocusFields() {
        mLoginBox.setHasFocus(false);
        mPasswordBox.setHasFocus(false);
    }

    @Override
    public void showPassword() {
        mPasswordIsHide = false;
        mPasswordBox.setEndIcon(getResources().getDrawable(R.drawable.ic_blanked_eye));
        mPasswordBoxText.setInputType(InputType.TYPE_CLASS_TEXT);
        mPasswordBoxText.setSelection(mPasswordBoxText.getText().length());
    }

    @Override
    public void hidePassword() {
        mPasswordIsHide = true;
        mPasswordBox.setEndIcon(getResources().getDrawable(R.drawable.ic_eye));
        mPasswordBoxText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordBoxText.setSelection(mPasswordBoxText.getText().length());
    }
}
