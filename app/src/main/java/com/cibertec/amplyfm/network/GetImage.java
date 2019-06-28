package com.cibertec.amplyfm.network;

import com.cibertec.amplyfm.models.Image.ImageResponse;
import com.cibertec.amplyfm.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetImage {

    @Headers("Ocp-Apim-Subscription-Key: " + Constants.API_KEY_BING)
    @GET("?count=1")
    Call<ImageResponse> getImage(@Query("q") String searchTerm);
}
