package com.example.deepakrattan.popular_movies_stage_2_dr.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String MOVIES_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    //Your API KEY HERE
    public static final String api_key = "";
    private static Retrofit retrofit;

    //This method will provide the Retrofit instance
    public static Retrofit getRetrofitInstanceMovie() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
