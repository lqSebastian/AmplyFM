package com.cibertec.amplyfm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Track implements Parcelable {
    @SerializedName("duration")
    private String duration;
    @SerializedName("mbid")
    private String mbid;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private List<ImageItem> images;

    public Track(String duration, String mbid, String name, String playcount, Album album) {
        this.duration = duration;
        this.mbid = mbid;
        this.name = name;
        this.playcount = playcount;
        this.album = album;
    }

    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @SerializedName("playcount")
    private String playcount;
    @SerializedName("artist")
    private Artist artist;
    @SerializedName("url")
    private String url;

    @SerializedName("album")
    @Expose
    private Album album;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ImageItem> getImages() {
        return images;
    }

    public void setImage(List<ImageItem> images) {
        this.images = images;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        if (getImages() != null && getImages().size() > 0) {
            for (ImageItem img :
                    getImages()) {
                if (img.getSize().equalsIgnoreCase("large")) {
                    return img.getUrl();
                }
            }
        }
        return null;
    }

    protected Track(Parcel in) {
        duration = in.readString();
        mbid = in.readString();
        name = in.readString();
        if (in.readByte() == 0x01) {
            images = new ArrayList<ImageItem>();
            in.readList(images, ImageItem.class.getClassLoader());
        } else {
            images = null;
        }
        playcount = in.readString();
        artist = (Artist) in.readValue(Artist.class.getClassLoader());
        album = (Album) in.readValue(Album.class.getClassLoader());
        url = in.readString();
        imgUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duration);
        dest.writeString(mbid);
        dest.writeString(name);
        if (images == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(images);
        }
        dest.writeString(playcount);
        dest.writeValue(artist);
        dest.writeValue(album);
        dest.writeString(url);
        dest.writeString(imgUrl);

    }

    @SuppressWarnings("unused")
    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}