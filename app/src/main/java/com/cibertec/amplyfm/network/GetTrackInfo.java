package com.cibertec.amplyfm.network;

import com.cibertec.amplyfm.models.TrackInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetTrackInfo {
    @GET("?format=json&method=track.getinfo")
    Call<TrackInfoResponse> getTrackInfo(@Query("artist") String artist,
                                         @Query("track") String track,
                                         @Query("api_key") String apiKey);
}
