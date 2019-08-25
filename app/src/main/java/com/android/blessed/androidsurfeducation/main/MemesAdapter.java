package com.android.blessed.androidsurfeducation.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.blessed.androidsurfeducation.R;
import com.android.blessed.androidsurfeducation.models.Meme;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MemesAdapter extends RecyclerView.Adapter<MemesAdapter.MemesViewHolder> {
    public class MemesViewHolder extends RecyclerView.ViewHolder {

        private TextView mMemeTitle;
        private ImageView mMeme, mLikeMeme, mShareMeme;

        private boolean mFavouriteMeme = false;

        public MemesViewHolder(View itemView) {
            super(itemView);
            mMeme = itemView.findViewById(R.id.meme_image_view);
            mMemeTitle = itemView.findViewById(R.id.meme_title);
            mLikeMeme = itemView.findViewById(R.id.like_meme_image_view);
            mShareMeme = itemView.findViewById(R.id.share_meme_image_view);

            mLikeMeme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mFavouriteMeme) {
                        mLikeMeme.setImageResource(R.drawable.ic_like_meme_active);
                        mFavouriteMeme = true;
                    }
                    else {
                        mLikeMeme.setImageResource(R.drawable.ic_favorite);
                        mFavouriteMeme = false;
                    }
                }
            });

            mShareMeme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
    public void onBindViewHolder(@NotNull final MemesViewHolder holder, int position) {
        Meme mMemeData = mMemesList.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(mMemeData.getPhotoUtl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.mMeme.setImageBitmap(resource);
                        holder.mMeme.setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        holder.mMemeTitle.setText(mMemeData.getTitle());
    }

    @Override
    public int getItemCount() {
        return mMemesList.size();
    }
}
