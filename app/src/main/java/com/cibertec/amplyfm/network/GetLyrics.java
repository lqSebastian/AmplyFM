package com.cibertec.amplyfm.network;

import com.cibertec.amplyfm.models.Lyrics.Lyrics;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetLyrics {

    @GET("{artist}/{song}")
    Call<Lyrics> getLyrics(@Path(value = "artist", encoded = true) String artistName, @Path(value = "song", encoded = true) String songName );
}
