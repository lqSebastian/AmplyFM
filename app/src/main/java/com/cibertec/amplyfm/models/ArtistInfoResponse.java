package com.cibertec.amplyfm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistInfoResponse implements Parcelable {

    @SerializedName("artist")
    @Expose
    private Artist artist;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }


    protected ArtistInfoResponse(Parcel in) {
        artist = (Artist) in.readValue(Artist.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(artist);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ArtistInfoResponse> CREATOR = new Parcelable.Creator<ArtistInfoResponse>() {
        @Override
        public ArtistInfoResponse createFromParcel(Parcel in) {
            return new ArtistInfoResponse(in);
        }

        @Override
        public ArtistInfoResponse[] newArray(int size) {
            return new ArtistInfoResponse[size];
        }
    };
}
