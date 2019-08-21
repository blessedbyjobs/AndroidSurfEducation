package com.android.blessed.androidsurfeducation.login;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public interface LoginContract {
    interface Presenter {
        boolean isFieldEmpty(ExtendedEditText textFieldBox);
        boolean checkFields(ExtendedEditText textFieldBox1, ExtendedEditText textFieldBox2);
        void startAsyncTask();
    }

    interface Model {

    }
}
