package com.android.blessed.androidsurfeducation.global;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.android.blessed.androidsurfeducation.db.MemesDatabase;

public class GlobalApplication extends Application {
    private static Context appContext;
    private static MemesDatabase mMemesDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        mMemesDatabase = Room.databaseBuilder(this, MemesDatabase.class, "memes").build();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static MemesDatabase getDatabase() {
        return mMemesDatabase;
    }
}
