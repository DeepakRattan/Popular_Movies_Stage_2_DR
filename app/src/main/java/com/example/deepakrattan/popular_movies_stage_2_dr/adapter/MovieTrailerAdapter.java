package com.example.deepakrattan.popular_movies_stage_2_dr.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepakrattan.popular_movies_stage_2_dr.R;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieTrailer;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.APIClient;
import com.example.deepakrattan.popular_movies_stage_2_dr.util.RecyclerViewOnItemClickListener;

import java.util.ArrayList;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {
    private Context context;
    private ArrayList<MovieTrailer> movieTrailerArrayList;
    MovieTrailer movieTrailer;
    RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public MovieTrailerAdapter(Context context, ArrayList<MovieTrailer> movieTrailerArrayList, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.context = context;
        this.movieTrailerArrayList = movieTrailerArrayList;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_row_movie_trailer, viewGroup, false);
        MovieTrailerViewHolder movieTrailerViewHolder = new MovieTrailerViewHolder(view, recyclerViewOnItemClickListener);
        return movieTrailerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder movieTrailerViewHolder, int pos) {
        movieTrailer = movieTrailerArrayList.get(pos);
        movieTrailerViewHolder.ivArrow.setTag(pos);

        movieTrailerViewHolder.txtTrailerNumber.setText("Trailer " + (pos + 1));
    }

    @Override
    public int getItemCount() {
        return movieTrailerArrayList.size();
    }


    class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTrailerNumber;
        ImageView ivArrow;
        RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

        public MovieTrailerViewHolder(@NonNull View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
            super(itemView);
            txtTrailerNumber = itemView.findViewById(R.id.txtTrailerN);
            ivArrow = itemView.findViewById(R.id.ivArrow);
            this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewOnItemClickListener.onClick(view, getAdapterPosition());
        }
    }

}
