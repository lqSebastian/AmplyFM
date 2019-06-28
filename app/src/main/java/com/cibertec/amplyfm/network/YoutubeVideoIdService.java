package com.cibertec.amplyfm.network;

import com.cibertec.amplyfm.models.Youtube.YoutubeUrl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeVideoIdService {

    @GET("?part=id&&fields=items%2Fid&maxResults=1")
    Call<YoutubeUrl> getVideoId(@Query("q") String videoSearch, @Query("key") String apiKey);
}