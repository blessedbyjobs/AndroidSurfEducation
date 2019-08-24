package com.android.blessed.androidsurfeducation.main;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.blessed.androidsurfeducation.CustomSnackBar;
import com.android.blessed.androidsurfeducation.R;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class MemesFragment extends MvpAppCompatFragment implements MemesView {
    @InjectPresenter
    MemesPresenter mMemesPresenter;

    @ProvidePresenter
    MemesPresenter provideLoginPresenter() {
        return new MemesPresenter();
    }

    private TextView mTextView;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.memes_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = getView().findViewById(R.id.error_text);
        mProgressBar = getView().findViewById(R.id.progress_bar);
    }

    @Override
    public void onStart() {
        super.onStart();
        preExecute();
        mMemesPresenter.loadMemes();
        postExecute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showRequestError() {
        setUpSnackBar().show();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showQueryError() {
        mTextView.setVisibility(View.VISIBLE);
    }

    private Snackbar setUpSnackBar() {
        return new CustomSnackBar(Snackbar.make(getView().findViewById(R.id.error_text),
                getResources().getString(R.string.internet_error_text), Snackbar.LENGTH_LONG), getActivity())
                .getSnackbar();
    }

    private void setDarkening() {
        getView().getBackground().setColorFilter(getResources().getColor(R.color.backgroundDarkColor), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void unsetDarkening() {
        getView().getBackground().setColorFilter(getResources().getColor(R.color.backgroundColor), PorterDuff.Mode.CLEAR);
    }

    private void preExecute() {
        setDarkening();
        showProgressBar();
    }

    private void postExecute() {
        hideProgressBar();
        unsetDarkening();
    }
}
