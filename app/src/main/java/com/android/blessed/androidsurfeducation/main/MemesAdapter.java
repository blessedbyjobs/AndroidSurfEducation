package com.android.blessed.androidsurfeducation.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blessed.androidsurfeducation.R;
import com.android.blessed.androidsurfeducation.detail_meme.DetailMemeActivity;
import com.android.blessed.androidsurfeducation.global.GlobalApplication;
import com.android.blessed.androidsurfeducation.models.Meme;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

public class MemesAdapter extends RecyclerView.Adapter<MemesAdapter.MemesViewHolder> {
    public class MemesViewHolder extends RecyclerView.ViewHolder {

        private TextView mMemeTitle;
        private ImageView mMeme, mLikeMeme, mShareMeme;

        private String mMemeURI, mMemeDescription;

        private boolean mFavouriteMeme = false;

        public MemesViewHolder(View itemView) {
            super(itemView);
            mMeme = itemView.findViewById(R.id.meme_image_view);
            mMemeTitle = itemView.findViewById(R.id.meme_title);
            mLikeMeme = itemView.findViewById(R.id.like_meme_image_view);
            mShareMeme = itemView.findViewById(R.id.share_meme_image_view);

            mLikeMeme.setOnClickListener(view -> {
                if (!mFavouriteMeme) {
                    mLikeMeme.setImageResource(R.drawable.ic_like_meme_active);
                    mFavouriteMeme = true;
                }
                else {
                    mLikeMeme.setImageResource(R.drawable.ic_favorite);
                    mFavouriteMeme = false;
                }
            });
        }
    }

    private Context mContext;

    private List<Meme> mMemesList;

    public MemesAdapter(Context context, List<Meme> memesList) {
        this.mContext = context;
        this.mMemesList = memesList;
    }

    @Override
    public MemesViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.meme_recycler_view_item, null);
        return new MemesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final MemesViewHolder holder, final int position) {
        Meme mMemeData = mMemesList.get(position);
        Glide.with(mContext)
                .asDrawable()
                .load(mMemeData.getPhotoUtl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.mMeme.setImageDrawable(resource);
                        holder.mMemeURI = mMemeData.getPhotoUtl();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        holder.mMemeTitle.setText(mMemeData.getTitle());
        holder.mMemeDescription = mMemeData.getDescription();
        holder.mShareMeme.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mMemeData.getTitle() + " " + mMemeData.getDescription() + " " + mMemeData.getPhotoUtl());
            sendIntent.setType("text/plain");
            mContext.startActivity(Intent.createChooser(sendIntent, "Отправить"));
        });

        // старт активити детального мема
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), DetailMemeActivity.class);
            i.putExtra("MEME", mMemesList.get(position));
            view.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mMemesList.size();
    }

    public List<Meme> getData() {
        return mMemesList;
    }

    public void setData(List<Meme> memes) {
        this.mMemesList = memes;
    }
}
