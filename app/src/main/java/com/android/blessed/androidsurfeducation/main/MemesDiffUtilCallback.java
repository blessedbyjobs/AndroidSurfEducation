package com.android.blessed.androidsurfeducation.main;

import androidx.recyclerview.widget.DiffUtil;

import com.android.blessed.androidsurfeducation.models.Meme;

import java.util.List;

public class MemesDiffUtilCallback extends DiffUtil.Callback {
    private final List<Meme> mOldMemeList;
    private final List<Meme> mNewMemeList;

    @Override
    public int getOldListSize() {
        return mOldMemeList.size();
    }

    public MemesDiffUtilCallback(List<Meme> oldMemeList, List<Meme> newMemeList) {
        mOldMemeList = oldMemeList;
        mNewMemeList = newMemeList;
    }

    @Override
    public int getNewListSize() {
        return mNewMemeList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Meme oldMeme = mOldMemeList.get(oldItemPosition);
        Meme newMeme = mNewMemeList.get(newItemPosition);
        return oldMeme.getId().equals(newMeme.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Meme oldMeme = mOldMemeList.get(oldItemPosition);
        Meme newMeme = mNewMemeList.get(newItemPosition);
        return oldMeme.getTitle().equals(newMeme.getTitle()) &&
                oldMeme.getPhotoUtl().equals(newMeme.getPhotoUtl()) &&
                oldMeme.getCreatedDate().equals(newMeme.getCreatedDate()) &&
                oldMeme.getIsFavorite() == newMeme.getIsFavorite() &&
                oldMeme.getDescription().equals(newMeme.getDescription());
    }
}
