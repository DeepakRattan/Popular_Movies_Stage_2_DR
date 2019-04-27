package com.example.deepakrattan.popular_movies_stage_2_dr.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.deepakrattan.popular_movies_stage_2_dr.R;
import com.example.deepakrattan.popular_movies_stage_2_dr.adapter.MovieReviewAdapter;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieReviews;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieReviewsResponse;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.APIClient;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.APIInterface;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.ConnectivityHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private RecyclerView rvReviews;
    private RecyclerView.LayoutManager layoutManager;
    private APIInterface apiInterface;
    private int mID;
    public static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        rvReviews = findViewById(R.id.rvReviews);
        layoutManager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(layoutManager);

        getSupportActionBar().setTitle(R.string.reviews);

        //Getting Movie ID
        Bundle bundle = getIntent().getExtras();
        mID = bundle.getInt("movie_id");
        Log.d(TAG, "movieID is  " + mID);

        //Checking for internet connectivity
        boolean isConnectedToInternet = ConnectivityHelper.isConnectedToNetwork(ReviewActivity.this);
        if (isConnectedToInternet) {
            getReviews(mID);
        } else {
            Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_SHORT).show();
        }


    }

    private void getReviews(int mID) {
        apiInterface = APIClient.getRetrofitInstanceMovie().create(APIInterface.class);
        Call<MovieReviewsResponse> call = apiInterface.getMovieReviews(mID, APIClient.api_key);
        call.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
                MovieReviewsResponse movieReviewsResponse = response.body();
                ArrayList<MovieReviews> movieReviewsArrayList = (ArrayList<MovieReviews>) movieReviewsResponse.getMovieReviews();
                MovieReviewAdapter adapter = new MovieReviewAdapter(ReviewActivity.this, movieReviewsArrayList);
                rvReviews.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {

            }
        });


    }
}
