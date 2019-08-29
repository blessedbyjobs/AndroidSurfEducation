package com.android.blessed.androidsurfeducation.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.blessed.androidsurfeducation.R;
import com.android.blessed.androidsurfeducation.login.LoginActivity;
import com.android.blessed.androidsurfeducation.models.Meme;
import com.android.blessed.androidsurfeducation.models.UserInfo;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends MvpAppCompatFragment implements ProfileView {
    @InjectPresenter
    ProfilePresenter mProfilePresenter;

    private ImageView mUserPick;
    private TextView mUserName;
    private TextView mUserDescription;

    private RecyclerView mRecyclerView;
    private MemesAdapter mAdapter;
    private List<Meme> mMemes;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profile_menu_about:

                return true;

            case R.id.profile_menu_logout:
                mProfilePresenter.logoutUser();
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserPick = getView().findViewById(R.id.profile_image);
        mUserName = getView().findViewById(R.id.profile_username);
        mUserDescription = getView().findViewById(R.id.profile_description);

        mToolbar = getView().findViewById(R.id.profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.inflateMenu(R.menu.detail_meme_menu);
        getActivity().setTitle("Профиль");

        UserInfo user = mProfilePresenter.getUser();
        mUserName.setText(user.getUsername());
        mUserDescription.setText(user.getUserDescription());

        Glide.with(this)
                .load(R.drawable.ic_meme_image)
                .circleCrop()
                .into(mUserPick);


        mMemes = new ArrayList<>();

        mRecyclerView = getView().findViewById(R.id.profile_memes_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(getConfiguration(), StaggeredGridLayoutManager.VERTICAL);
        ((StaggeredGridLayoutManager) mLayoutManager).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MemesAdapter(getActivity(), mMemes);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = getView().findViewById(R.id.swipe_to_refresh_your_memes);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mProfilePresenter.loadLocalMemes();
            mSwipeRefreshLayout.setRefreshing(false);
        });

        mProfilePresenter.loadLocalMemes();
    }

    private int getConfiguration() {
        int numberOfColumns = 2;
        getResources().getConfiguration();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            numberOfColumns = 4;

        return numberOfColumns;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.profile_fragment, null);
    }

    @Override
    public void updateMemes(List<Meme> mNewMemes) {
        Log.i("Memes", String.valueOf(mNewMemes.size()));
        MemesDiffUtilCallback memesDiffUtilCallback = new MemesDiffUtilCallback(mAdapter.getData(), mNewMemes);
        DiffUtil.DiffResult memesDiffResult = DiffUtil.calculateDiff(memesDiffUtilCallback);
        mAdapter.setData(mNewMemes);
        memesDiffResult.dispatchUpdatesTo(mAdapter);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showLogoutDialog() {
        AlertDialog.Builder ImageDialog = new AlertDialog.Builder(getActivity());
        ImageDialog.setMessage(R.string.dialog_logout)
                .setPositiveButton(R.string.dialog_logout_yes, (dialog, id) -> {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton(R.string.dialog_logout_no, (dialog, id) -> {

                }).show();
    }
}
