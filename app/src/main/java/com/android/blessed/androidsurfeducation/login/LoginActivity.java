package com.android.blessed.androidsurfeducation.login;

import android.content.Context;
import android.content.Intent;
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

import androidx.core.content.ContextCompat;

import com.android.blessed.androidsurfeducation.MainActivity;
import com.android.blessed.androidsurfeducation.R;
import com.android.blessed.androidsurfeducation.SplashScreenActivity;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;

import javax.inject.Inject;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class LoginActivity extends MvpAppCompatActivity implements LoginView {
    PreferencesHelper mCache;

    @InjectPresenter
    LoginPresenter mLoginPresenter;

    @ProvidePresenter
    LoginPresenter provideLoginPresenter() {
        mCache = new PreferencesHelper(getApplicationContext());
        return new LoginPresenter(mCache);
    }

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

    public void initializeFields() {
        mLoginBox = findViewById(R.id.login_field_box);
        mPasswordBox = findViewById(R.id.password_field_box);
        mLoginBoxText = findViewById(R.id.login_edit_text);
        mPasswordBoxText = findViewById(R.id.password_edit_text);
        mProgressBar = findViewById(R.id.progress_bar);
        mLoginButton = findViewById(R.id.login_button);
    }

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

    @Override
    public void moveToMainScreen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginError() {
        setUpSnackBar().show();
    }

    private Snackbar setUpSnackBar() {
        Snackbar mSnackBar = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.login_error_text), Snackbar.LENGTH_LONG);
        View mSnackBarView = mSnackBar.getView();
        mSnackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.errorColor));
        TextView mSnackBarText = mSnackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        mSnackBarText.setTextColor(getResources().getColor(R.color.inactiveColor));

        return mSnackBar;
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
