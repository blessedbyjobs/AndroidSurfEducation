package com.android.blessed.androidsurfeducation.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.blessed.androidsurfeducation.global.CustomSnackBar;
import com.android.blessed.androidsurfeducation.main.MainActivity;
import com.android.blessed.androidsurfeducation.R;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.snackbar.Snackbar;

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
        mLoginBoxText.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            private boolean backspacingFlag = false;
            private boolean editedFlag = false;
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                cursorComplement = s.length()-mLoginBoxText.getSelectionStart();

                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                String phone = string.replaceAll("[^\\d]", "");

                if (!editedFlag) {
                    // 9999999999 <- 10+
                    // (999) 999-99-99
                    if (phone.length() >= 10 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-" + phone.substring(6,8) + "-" + phone.substring(8,10);
                        mLoginBoxText.setText(ans);
                        mLoginBoxText.setSelection(mLoginBoxText.getText().length()-cursorComplement);
                    } else if (phone.length() >= 6 && !backspacingFlag) {
                        // 999999999 <- 6+
                        // (999) 999-999
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3,6) + "-" + phone.substring(6);
                        mLoginBoxText.setText(ans);
                        mLoginBoxText.setSelection(mLoginBoxText.getText().length()-cursorComplement);
                    } else if (phone.length() >= 3 && !backspacingFlag) {
                        // 99999 <- 3+
                        // (999) 99
                        editedFlag = true;
                        String ans = "(" +phone.substring(0, 3) + ") " + phone.substring(3);
                        mLoginBoxText.setText(ans);
                        mLoginBoxText.setSelection(mLoginBoxText.getText().length()-cursorComplement);
                    }
                    // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }
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

                    // если логин пустой, а пароль короткий -> отобразить ошибку
                    if (mLoginPresenter.isPasswordShort(mPasswordBoxText.getText().length())) showPasswordError();
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
        setUpSnackBar(getResources().getString(R.string.login_error_text)).show();
    }

    @Override
    public void showInternetError() {
        setUpSnackBar(getResources().getString(R.string.internet_error_text)).show();
    }

    private Snackbar setUpSnackBar(String text) {
        return new CustomSnackBar(Snackbar.make(findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG), this)
                .getSnackbar();
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
