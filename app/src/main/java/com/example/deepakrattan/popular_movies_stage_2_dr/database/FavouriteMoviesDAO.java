package com.example.deepakrattan.popular_movies_stage_2_dr.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

//Specify database operations here
@Dao
public interface FavouriteMoviesDAO {
    @Insert
    long insert(FavouriteMovies favouriteMovies);

    @Query("Select * from FAVOURITEMOVIES")
    List<FavouriteMovies> getFavouriteMovies();

}
