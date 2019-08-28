package com.android.blessed.androidsurfeducation.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blessed.androidsurfeducation.R;
import com.android.blessed.androidsurfeducation.models.UserInfo;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;

public class ProfileFragment extends MvpAppCompatFragment implements ProfileView {
    @InjectPresenter
    ProfilePresenter mProfilePresenter;

    private ImageView mUserPick;
    private TextView mUserName;
    private TextView mUserDescription;

    private RecyclerView mRecyclerView;

    private Toolbar mToolbar;

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
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFields();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.profile_fragment, null);
    }

    void initializeFields() {
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
    }
}
