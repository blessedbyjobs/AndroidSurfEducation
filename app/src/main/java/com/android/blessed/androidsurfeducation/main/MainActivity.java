package com.android.blessed.androidsurfeducation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.blessed.androidsurfeducation.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FrameLayout mFragmentContainer;

    private Fragment mMemes;
    private Fragment mProfile;

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initializeFields();
        setOnClickListeners();

        loadFragment(new MemesFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void initializeFields() {
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mFragmentContainer = findViewById(R.id.fragment_container);

        mMemes = new MemesFragment();
        mProfile = new ProfileFragment();
    }

    private void setOnClickListeners() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.bottom_navigation_memes:
                    fragment = mMemes;
                    break;

                case R.id.bottom_navigation_add_meme:
                    MainActivity.this.startActivity(new Intent(MainActivity.this, CreateMemeActivity.class));
                    break;

                case R.id.bottom_navigation_profile:
                    fragment = mProfile;
                    break;
            }

            return loadFragment(fragment);
        });
    }
}
