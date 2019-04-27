package com.example.deepakrattan.popular_movies_stage_2_dr.network;

import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieListResponse;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieReviewsResponse;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieTrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    // Get popular movies
    @GET("popular")
    Call<MovieListResponse> getPopularMovies(@Query("api_key") String api_key);

    //Get top rated movies
    @GET("top_rated")
    Call<MovieListResponse> getTopRatedMovies(@Query("api_key") String api_key);

    //Get Movie Trailers
    @GET("{id}/videos")
    Call<MovieTrailerResponse> getMovieTrailers(@Path(value = "id", encoded = true) int id, @Query("api_key") String api_key);

    //Get Movie Reviews
    @GET("{id}/reviews")
    Call<MovieReviewsResponse> getMovieReviews(@Path(value = "id", encoded = true) int id, @Query("api_key") String api_key);

}
