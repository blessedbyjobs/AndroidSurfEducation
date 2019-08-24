package com.android.blessed.androidsurfeducation;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class CustomSnackBar {
    private Snackbar mSnackbar;
    private Context mContext;

    public CustomSnackBar(Snackbar snackbar, Context context) {
        mSnackbar = snackbar;
        mContext = context;
    }

    public Snackbar getSnackbar() {
        View mSnackBarView = mSnackbar.getView();
        mSnackBarView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.errorColor));
        TextView mSnackBarText = mSnackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        mSnackBarText.setTextColor(mContext.getResources().getColor(R.color.inactiveColor));
        return mSnackbar;
    }
}
