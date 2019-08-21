package com.android.blessed.androidsurfeducation.login;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private boolean mEndIconWasDestroyed = false;

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

        mLoginBoxText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    mLoginBox.setHasFocus(false);
                    mPasswordBox.setHasFocus(true);
                }
                return true;
            }
        });

        mPasswordBoxText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (mEndIconWasDestroyed) {
                        hidePassword();
                        mEndIconWasDestroyed = false;
                    }
                    showKeyboard();
                }
                else hideKeyboard(mPasswordBox);
            }
        });

        mPasswordBoxText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    mPasswordBox.clearFocus();
                    hideKeyboard(mPasswordBox);
                }
                return false;
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
                if (!mPasswordIsHide) hidePassword();
                if (mLoginPresenter.checkFields(mLoginBoxText.getText().length(), mPasswordBoxText.getText().length())) {
                    if (!mLoginPresenter.isPasswordShort(mPasswordBoxText.getText().length())) {
                        mLoginPresenter.loginUser(getLogin(), getPassword());
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
        Log.i("Error", String.valueOf(editTextFieldBox.getText().length()));
        if (mLoginPresenter.isFieldEmpty(editTextFieldBox.getText().length())) textFieldBox.setError(getResources().getString(R.string.error_text),false);
    }

    @Override
    public void showErrors() {
        showError(mLoginBox, mLoginBoxText);
        showError(mPasswordBox, mPasswordBoxText);

        // если все поля пустые и нажать "Войти" отображается иконка, хотя не должна
        mPasswordBox.removeEndIcon();
        hidePassword();
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textFieldBox.getWindowToken(), 0);
    }

    public String getLogin() {
        return mLoginBoxText.getText().toString();
    }

    public String getPassword() {
        return mPasswordBoxText.getText().toString();
    }

    @Override
    public void unfocusFields() {
        mLoginBox.setHasFocus(false);
        mPasswordBox.setHasFocus(false);

        mPasswordBox.removeEndIcon();
        mEndIconWasDestroyed = true;
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
