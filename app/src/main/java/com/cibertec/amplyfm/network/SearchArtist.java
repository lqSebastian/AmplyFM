package com.cibertec.amplyfm.network;

import com.cibertec.amplyfm.models.Search.SearchResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchArtist {

    @GET("?format=json&method=artist.search&limit=7")
    Call<SearchResults> searchArtist(@Query("artist") String artist,
                                     @Query("api_key") String apiKey);
}
