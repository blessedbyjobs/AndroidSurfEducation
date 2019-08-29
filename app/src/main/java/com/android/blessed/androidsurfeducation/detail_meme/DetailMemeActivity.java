package com.android.blessed.androidsurfeducation.detail_meme;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.blessed.androidsurfeducation.R;
import com.android.blessed.androidsurfeducation.databinding.DetailMemeActivityBinding;
import com.android.blessed.androidsurfeducation.global.GlobalApplication;
import com.android.blessed.androidsurfeducation.models.Meme;
import com.android.blessed.androidsurfeducation.models.UserInfo;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DetailMemeActivity extends MvpAppCompatActivity implements DetailMemeView {
    @InjectPresenter
    DetailMemePresenter mDetailMemePresenter;

    private Toolbar mToolbar;

    private Meme mMeme;
    private UserInfo mUser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_meme_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_share:
                sendMemeToSomebody();
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DetailMemeActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.detail_meme_activity);

        setUser();
        setMeme();
        setToolbar();

        binding.setMeme(mMeme);
        binding.setUser(mUser);
    }

    private void setMeme() {
        Intent intent = getIntent();
        mMeme = (Meme) intent.getSerializableExtra("MEME");
    }

    private void setUser() {
        mUser = mDetailMemePresenter.getUserInfo();
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.detail_meme_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_close_meme);
    }

    void sendMemeToSomebody() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mMeme.getTitle() + " " + mMeme.getDescription() + " " + mMeme.getPhotoUtl());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Отправить"));
    }

    @BindingAdapter({"bind:imgUrl"})
    public static void setMemePicture(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @BindingAdapter({"bind:dateText"})
    public static void setDateText(TextView textView, String date) {
        long diffInDays = TimeUnit.DAYS.convert(new Date().getTime() - 1000 * Long.parseLong(date), TimeUnit.MILLISECONDS);

        if (diffInDays < 30) textView.setText(GlobalApplication.getAppContext().getResources().getQuantityString(R.plurals.meme_publication_date_days, (int) diffInDays, (int) diffInDays));
        else {
            if (diffInDays / 12 < 12) textView.setText(GlobalApplication.getAppContext().getResources().getQuantityString(R.plurals.meme_publication_date_month, (int) diffInDays, (int) diffInDays));
            else {
                diffInDays /= 365;
                textView.setText(GlobalApplication.getAppContext().getResources().getQuantityString(R.plurals.meme_publication_date_year, (int) diffInDays, (int) diffInDays));
            }
        }
    }
}
