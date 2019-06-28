package com.cibertec.amplyfm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackInfoResponse implements Parcelable {

    @SerializedName("track")
    @Expose
    private Track track;

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }


    protected TrackInfoResponse(Parcel in) {
        track = (Track) in.readValue(Track.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(track);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TrackInfoResponse> CREATOR = new Parcelable.Creator<TrackInfoResponse>() {
        @Override
        public TrackInfoResponse createFromParcel(Parcel in) {
            return new TrackInfoResponse(in);
        }

        @Override
        public TrackInfoResponse[] newArray(int size) {
            return new TrackInfoResponse[size];
        }
    };
}