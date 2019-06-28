package com.cibertec.amplyfm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;



public class TopTracksResponse implements Parcelable {
    @SerializedName("toptracks")
    private TopTracks topTracks;

    public TopTracks getTopTracks() {
        return topTracks;
    }

    public void setToptracks(TopTracks topTracks) {
        this.topTracks = topTracks;
    }

    protected TopTracksResponse(Parcel in) {
        topTracks = (TopTracks) in.readValue(TopTracks.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(topTracks);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TopTracksResponse> CREATOR = new Parcelable.Creator<TopTracksResponse>() {
        @Override
        public TopTracksResponse createFromParcel(Parcel in) {
            return new TopTracksResponse(in);
        }

        @Override
        public TopTracksResponse[] newArray(int size) {
            return new TopTracksResponse[size];
        }
    };
}