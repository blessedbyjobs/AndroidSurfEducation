package com.android.blessed.androidsurfeducation.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.blessed.androidsurfeducation.models.Meme;

import java.util.List;

@Dao
public interface MemeDAO {
    @Query("SELECT * FROM memes")
    List<Meme> getAll();

    @Query("SELECT * FROM memes WHERE id = :id")
    Meme getById(long id);

    @Insert
    void insert(Meme meme);

    @Update
    void update(Meme meme);

    @Delete
    void delete(Meme meme);
}
