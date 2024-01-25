package com.minhtu.serviceandroid.song.search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicApi {
    @GET("search")
    Call<String> searchMusicInfo(
            @Query("q") String name,
            @Query("searchEngine") String searchEngine
    );
}
