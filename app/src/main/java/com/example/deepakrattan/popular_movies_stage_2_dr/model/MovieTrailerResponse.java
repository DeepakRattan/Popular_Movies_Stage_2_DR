package com.example.deepakrattan.popular_movies_stage_2_dr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieTrailer> movieTrailerList = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieTrailer> getResults() {
        return movieTrailerList;
    }

    public void setResults(List<MovieTrailer> movieTrailerList) {
        this.movieTrailerList = movieTrailerList;
    }

}
