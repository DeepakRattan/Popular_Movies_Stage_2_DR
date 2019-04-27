package com.example.deepakrattan.popular_movies_stage_2_dr.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

//Create database FavouriteMoviesDB
@Database(entities = {FavouriteMovies.class}, version = 1)
public abstract class FavouriteMoviesDB extends RoomDatabase {
    public abstract FavouriteMoviesDAO getDAO();

}
