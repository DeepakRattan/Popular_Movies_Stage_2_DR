package com.example.deepakrattan.popular_movies_stage_2_dr.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.deepakrattan.popular_movies_stage_2_dr.R;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieReviews;

import java.util.ArrayList;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder> {
    private Context context;
    private ArrayList<MovieReviews> movieReviewsArrayList;
    MovieReviews movieReviews;

    public MovieReviewAdapter(Context context, ArrayList<MovieReviews> movieReviewsArrayList) {
        this.context = context;
        this.movieReviewsArrayList = movieReviewsArrayList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_row_review, viewGroup, false);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int pos) {
        movieReviews = movieReviewsArrayList.get(pos);
        //String url = movieReviews.getUrl();
        // reviewViewHolder.webView.loadUrl(url);

        String author = movieReviews.getAuthor();
        String content = movieReviews.getContent();

        reviewViewHolder.txtAuthor.setText(author);
        reviewViewHolder.txtReview.setText(content);
    }

    @Override
    public int getItemCount() {
        return movieReviewsArrayList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        //WebView webView;

        TextView txtAuthor, txtReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            //webView = itemView.findViewById(R.id.webView);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtReview = itemView.findViewById(R.id.txtReview);
        }
    }


}
