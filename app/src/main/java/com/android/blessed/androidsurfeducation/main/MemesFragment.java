package com.android.blessed.androidsurfeducation.main;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.blessed.androidsurfeducation.global.CustomSnackBar;
import com.android.blessed.androidsurfeducation.R;
import com.android.blessed.androidsurfeducation.models.Meme;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MemesFragment extends MvpAppCompatFragment implements MemesView {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<Meme> mMemes;

    private TextView mTextView;
    private ProgressBar mProgressBar;

    @InjectPresenter
    MemesPresenter mMemesPresenter;

    @ProvidePresenter
    MemesPresenter provideLoginPresenter() {
        return new MemesPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.memes_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = getView().findViewById(R.id.error_textview);
        mProgressBar = getView().findViewById(R.id.progress_bar);
        mRecyclerView = getView().findViewById(R.id.memes_recycler_view);
        mSwipeRefreshLayout = getView().findViewById(R.id.swipe_to_refresh_memes);

        mMemes = new ArrayList<>();

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(getConfiguration(), StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MemesAdapter(getActivity(), mMemes);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.activeColor));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMemesPresenter.loadMemes();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMemesPresenter.loadMemes();
    }

    @Override
    public void onPause() {
        setRetainInstance(true);
        super.onPause();
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

    @Override
    public void updateMemes(List<Meme> memes) {
        mMemes.clear();
        mMemes.addAll(memes);
        mAdapter.notifyDataSetChanged();
    }

    private Snackbar setUpSnackBar() {
        return new CustomSnackBar(Snackbar.make(getView().findViewById(R.id.error_textview),
                getResources().getString(R.string.internet_error_text), Snackbar.LENGTH_LONG), getActivity())
                .getSnackbar();
    }

    private void setDarkening() {
        getView().getBackground().setColorFilter(getResources().getColor(R.color.backgroundDarkColor), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void unsetDarkening() {
        getView().getBackground().setColorFilter(getResources().getColor(R.color.backgroundColor), PorterDuff.Mode.CLEAR);
    }

    @Override
    public void preExecute() {
        setDarkening();
        showProgressBar();
    }

    @Override
    public void postExecute() {
        hideProgressBar();
        unsetDarkening();
    }

    private int getConfiguration() {
        int numberOfColumns = 2;
        if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE)
            numberOfColumns = 4;

        return numberOfColumns;
    }
}
