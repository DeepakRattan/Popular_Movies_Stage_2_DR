package com.example.deepakrattan.popular_movies_stage_2_dr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deepakrattan.popular_movies_stage_2_dr.R;
import com.example.deepakrattan.popular_movies_stage_2_dr.activity.MovieDetailActivity;
import com.example.deepakrattan.popular_movies_stage_2_dr.database.FavouriteMovies;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.APIClient;
import com.example.deepakrattan.popular_movies_stage_2_dr.util.RecyclerViewOnItemClickListener;
import com.example.deepakrattan.popular_movies_stage_2_dr.util.Shared_Pref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviePosterAdapter extends RecyclerView.Adapter<FavouriteMoviePosterAdapter.MovieViewHolder> {
    private Context context;
    private List<FavouriteMovies> favouriteMoviesArrayList;
    FavouriteMovies movie;
    RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public FavouriteMoviePosterAdapter(Context context, List<FavouriteMovies> favouriteMoviesArrayList, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.context = context;
        this.favouriteMoviesArrayList = favouriteMoviesArrayList;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_row_movie, viewGroup, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view, recyclerViewOnItemClickListener);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        //Getting data from ArrayList
        movie = favouriteMoviesArrayList.get(i);
        //Getting url of movie poster
        String posterPath = movie.getPosterPath();
        if (posterPath != null) {
            String complete_poster_path = APIClient.MOVIES_POSTER_BASE_URL + posterPath;
            Picasso.with(context).load(complete_poster_path).into(movieViewHolder.img_movie_poster);
        }
        //On Clicking movie poster
        /*movieViewHolder.img_movie_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(Shared_Pref.MOVIE_DETAIL, movie);
                context.startActivity(intent);

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return favouriteMoviesArrayList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_movie_poster;
        RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

        public MovieViewHolder(@NonNull View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
            super(itemView);
            img_movie_poster = itemView.findViewById(R.id.img_movie_poster);
            this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            recyclerViewOnItemClickListener.onClick(view, getAdapterPosition());


        }
    }

}

