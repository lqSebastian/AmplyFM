package com.cibertec.amplyfm.network;

import com.cibertec.amplyfm.models.ArtistInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetArtistInfo {

    @GET("?format=json&method=artist.getinfo")
    Call<ArtistInfoResponse> getArtistInfo(@Query("artist") String artist, @Query("api_key") String apiKey);
}
