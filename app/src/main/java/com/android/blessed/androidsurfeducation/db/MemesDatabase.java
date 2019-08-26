package com.android.blessed.androidsurfeducation.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.blessed.androidsurfeducation.models.Meme;

@Database(entities = {Meme.class}, version = 1)
public abstract class MemesDatabase extends RoomDatabase {
    public abstract MemeDAO mMemeDAO();
}
