package com.cibertec.amplyfm.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TopTracks {

    @SerializedName("track")
    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
