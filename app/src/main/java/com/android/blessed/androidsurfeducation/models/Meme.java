package com.android.blessed.androidsurfeducation.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "memes")
public class Meme implements Serializable {
    @SerializedName("id") @Expose
    @PrimaryKey(autoGenerate = true)
    private long id;

    @SerializedName("title") @Expose
    private String title;

    @SerializedName("description") @Expose
    private String description;

    @SerializedName("isFavorite") @Expose
    private Boolean isFavorite;

    @SerializedName("createdDate") @Expose
    private Integer createdDate;

    @SerializedName("photoUtl") @Expose
    private String photoUtl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Integer getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Integer createdDate) {
        this.createdDate = createdDate;
    }

    public String getPhotoUtl() {
        return photoUtl;
    }

    public void setPhotoUtl(String photoUtl) {
        this.photoUtl = photoUtl;
    }
}
