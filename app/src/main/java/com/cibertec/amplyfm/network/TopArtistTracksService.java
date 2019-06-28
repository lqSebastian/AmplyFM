package com.cibertec.amplyfm.network;

import com.cibertec.amplyfm.models.TopTracksResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TopArtistTracksService {

    @GET("?format=json&method=artist.gettoptracks&period=overall&autocorrect=1")

    Call<TopTracksResponse> getTopTracks(@Query("artist") String artist, @Query("limit") int limit, @Query("api_key") String apiKey);
}
