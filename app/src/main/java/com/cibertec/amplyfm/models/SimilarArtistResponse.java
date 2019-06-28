package com.cibertec.amplyfm.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimilarArtistResponse implements Parcelable {

    @SerializedName("artist")
    @Expose
    private List<SimilarArtist> artist = null;

    public List<SimilarArtist> getArtist() {
        return artist;
    }

    public void setArtist(List<SimilarArtist> artist) {
        this.artist = artist;
    }


    protected SimilarArtistResponse(Parcel in) {
        if (in.readByte() == 0x01) {
            artist = new ArrayList<SimilarArtist>();
            in.readList(artist, SimilarArtist.class.getClassLoader());
        } else {
            artist = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (artist == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(artist);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SimilarArtistResponse> CREATOR = new Parcelable.Creator<SimilarArtistResponse>() {
        @Override
        public SimilarArtistResponse createFromParcel(Parcel in) {
            return new SimilarArtistResponse(in);
        }

        @Override
        public SimilarArtistResponse[] newArray(int size) {
            return new SimilarArtistResponse[size];
        }
    };
}