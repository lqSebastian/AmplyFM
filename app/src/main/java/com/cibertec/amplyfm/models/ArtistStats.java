package com.cibertec.amplyfm.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistStats implements Parcelable {

    @SerializedName("listeners")
    @Expose
    private String listeners;
    @SerializedName("playcount")
    @Expose
    private String playcount;

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    protected ArtistStats(Parcel in) {
        listeners = in.readString();
        playcount = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(listeners);
        dest.writeString(playcount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ArtistStats> CREATOR = new Parcelable.Creator<ArtistStats>() {
        @Override
        public ArtistStats createFromParcel(Parcel in) {
            return new ArtistStats(in);
        }

        @Override
        public ArtistStats[] newArray(int size) {
            return new ArtistStats[size];
        }
    };
}