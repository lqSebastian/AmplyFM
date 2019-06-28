package com.cibertec.amplyfm.models.FavoriteTracks;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteItem implements Parcelable {


    @Ignore
    public FavoriteItem(String name, String album, String playcount, String duration) {
        this.name = name;
        this.album = album;
        this.playcount = playcount;
        this.duration = duration;
    }

    public FavoriteItem(String id, String name, String album,  String playcount, String duration,String artist, String imageDir) {
        this.id = id;
        this.name = name;
        this.album = album;
        this.artist = artist;
        this.imageDir = imageDir;
        this.playcount = playcount;
        this.duration = duration;
    }

    @NonNull
    @PrimaryKey()
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "album")
    private String album;

    @ColumnInfo(name = "artist")
    private String artist;

    @ColumnInfo(name = "imageDir")
    private String imageDir;


    public String getImageDir() {
        return imageDir;
    }

    public void setImageDir(String imageDir) {
        this.imageDir = imageDir;
    }



    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @ColumnInfo(name = "playcount")
    private String playcount;


    @ColumnInfo(name = "duration")
    private String duration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    protected FavoriteItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        album = in.readString();
        playcount = in.readString();
        duration = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(album);
        dest.writeString(playcount);
        dest.writeString(duration);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>() {
        @Override
        public FavoriteItem createFromParcel(Parcel in) {
            return new FavoriteItem(in);
        }

        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };
}
