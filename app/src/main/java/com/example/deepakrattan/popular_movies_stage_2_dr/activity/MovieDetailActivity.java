package com.example.deepakrattan.popular_movies_stage_2_dr.activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deepakrattan.popular_movies_stage_2_dr.R;
import com.example.deepakrattan.popular_movies_stage_2_dr.adapter.MovieTrailerAdapter;
import com.example.deepakrattan.popular_movies_stage_2_dr.database.FavouriteMovies;
import com.example.deepakrattan.popular_movies_stage_2_dr.database.FavouriteMoviesDAO;
import com.example.deepakrattan.popular_movies_stage_2_dr.database.FavouriteMoviesDB;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.Movie;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieListResponse;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieReviewsResponse;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieTrailer;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieTrailerResponse;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.APIClient;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.APIInterface;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.ConnectivityHelper;
import com.example.deepakrattan.popular_movies_stage_2_dr.util.RecyclerViewOnItemClickListener;
import com.example.deepakrattan.popular_movies_stage_2_dr.util.Shared_Pref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private TextView txtMovieTitle, txtRating, txtReleaseDate, txtOverView, txtTrailers;
    private ImageView imgMoviePoster;
    private Button btnFavourite, btnReview;
    private RecyclerView rvTrailers;
    private APIInterface apiInterface;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MovieTrailer> movieTrailerArrayList;
    private ArrayList<MovieTrailer> finalMovieTrailerArrayList;
    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieTrailer movieTrailer;
    private MovieReviewsResponse movieReviewsResponse;
    private Movie movie;
    private FavouriteMovies favouriteMovies;
    private FavouriteMoviesDAO favouriteMoviesDAO;
    private FavouriteMoviesDB favouriteMoviesDB;
    public static final String Database_Name = "FavouriteMoviesDB";
    private List<FavouriteMovies> favouriteMoviesList;
    public static final String TAG = "test";
    private String youtube_key;
    private String movieID, title, rating, releaseDate, overview, posterPath;
    RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;
    long id;
    private int mID;
    boolean isConnectedToInternet = ConnectivityHelper.isConnectedToNetwork(MovieDetailActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //findViewByID
        txtMovieTitle = findViewById(R.id.txt_Movie_Title);
        txtRating = findViewById(R.id.txt_movie_rating);
        txtReleaseDate = findViewById(R.id.txtReleaseDate);
        txtOverView = findViewById(R.id.txtOverview);
        imgMoviePoster = findViewById(R.id.imgMoviePoster);
        rvTrailers = findViewById(R.id.rvTrailers);
        btnReview = findViewById(R.id.btnReviews);
        btnFavourite = findViewById(R.id.btnFavourite);
        txtTrailers = findViewById(R.id.txtTrailers);

        favouriteMoviesDB = Room.databaseBuilder(getApplicationContext(), FavouriteMoviesDB.class, Database_Name).build();
        favouriteMoviesDAO = favouriteMoviesDB.getDAO();

        layoutManager = new LinearLayoutManager(this);
        rvTrailers.setLayoutManager(layoutManager);

        recyclerViewOnItemClickListener = new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                movieTrailer = finalMovieTrailerArrayList.get(position);
                youtube_key = movieTrailer.getKey();
                String youtube_url = "http://www.youtube.com/watch?v=" + youtube_key;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_url));
                startActivity(intent);
            }
        };

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFavourite();
            }
        });


        getSupportActionBar().setTitle(R.string.detail_activity);

        //Getting parcelable object from MoviesActivity
        //Checking whether the object returned is of type Movie or FavouriteMovies
        Object object = (getIntent().getParcelableExtra(Shared_Pref.MOVIE_DETAIL));
        //if object is of type Movie ,get popular/top_rated movie details from MoviesActivity
        if (object instanceof Movie) {
            movie = (Movie) object;
            mID = movie.getId();
            showMovieDetails(movie);
        }
        //if object is of type FavouriteMovies ,get favourite_movie details from MoviesActivity
        else if (object instanceof FavouriteMovies) {
            favouriteMovies = (FavouriteMovies) object;
            mID = favouriteMovies.getMid();
            showFavouriteMovieDetails(favouriteMovies);
        }

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MovieDetailActivity.this, ReviewActivity.class);
                intent.putExtra("movie_id", mID);
                Log.d(TAG, "id  " + mID);
                startActivity(intent);
            }
        });


    }

    private void showFavouriteMovieDetails(FavouriteMovies favouriteMovies) {
        txtMovieTitle.setText(favouriteMovies.getTitle());
        txtReleaseDate.setText("Release Date : " + favouriteMovies.getReleaseDate());
        txtRating.setText("Rating : " + String.valueOf(favouriteMovies.getVoteAverage()));
        txtOverView.setText(favouriteMovies.getOverview());

        String complete_poster_path = APIClient.MOVIES_POSTER_BASE_URL + favouriteMovies.getPosterPath();
        Picasso.with(MovieDetailActivity.this).load(complete_poster_path).into(imgMoviePoster);

        apiInterface = APIClient.getRetrofitInstanceMovie().create(APIInterface.class);
        getMovieTrailers(favouriteMovies.getMid());
        getReviews(favouriteMovies.getMid());
    }

    private void showMovieDetails(Movie movie) {
        txtMovieTitle.setText(movie.getTitle());
        txtReleaseDate.setText("Release Date : " + movie.getReleaseDate());
        txtRating.setText("Rating : " + String.valueOf(movie.getVoteAverage()));
        txtOverView.setText(movie.getOverview());

        String complete_poster_path = APIClient.MOVIES_POSTER_BASE_URL + movie.getPosterPath();
        Picasso.with(MovieDetailActivity.this).load(complete_poster_path).into(imgMoviePoster);

        apiInterface = APIClient.getRetrofitInstanceMovie().create(APIInterface.class);

        if (isConnectedToInternet) {
            getMovieTrailers(movie.getId());
            getReviews(movie.getId());
        } else {
            Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_SHORT).show();
        }

    }


    private void addToFavourite() {
        int mid = movie.getId();
        movieID = String.valueOf(mid);
        title = movie.getTitle();
        double voteAverage = movie.getVoteAverage();
        rating = String.valueOf(voteAverage);
        releaseDate = movie.getReleaseDate();
        overview = movie.getOverview();
        posterPath = movie.getPosterPath();
        new InsertTask().execute(movieID, title, rating, releaseDate, overview, posterPath);

    }

    public class InsertTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            movieID = params[0];
            title = params[1];
            rating = params[2];
            releaseDate = params[3];
            overview = params[4];
            posterPath = params[5];

            int mID = Integer.parseInt(movieID);
            double voteAverage = Double.parseDouble(rating);

            favouriteMovies = new FavouriteMovies(mID, title, voteAverage, releaseDate, overview, posterPath);
            id = favouriteMoviesDAO.insert(favouriteMovies);

            //Updating UI from background
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (id == -1)
                        Toast.makeText(MovieDetailActivity.this, "Insertion unsuccessful", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MovieDetailActivity.this, "Movie added to your favourites.", Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }

    }

    //Getting Reviews of Movie
    private void getReviews(int id) {
        Call<MovieReviewsResponse> call = apiInterface.getMovieReviews(id, APIClient.api_key);
        call.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
                movieReviewsResponse = response.body();
                int id1 = movieReviewsResponse.getId();
                //Toast.makeText(MovieDetailActivity.this, "id is " + id1, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {

            }
        });
    }


    public void getMovieTrailers(int id) {
        Call<MovieTrailerResponse> call = apiInterface.getMovieTrailers(id, APIClient.api_key);
        call.enqueue(new Callback<MovieTrailerResponse>() {
            @Override
            public void onResponse(Call<MovieTrailerResponse> call, Response<MovieTrailerResponse> response) {
                MovieTrailerResponse movieTrailerResponse = response.body();
                int mID = movieTrailerResponse.getId();
                movieTrailerArrayList = (ArrayList<MovieTrailer>) movieTrailerResponse.getResults();
                if (movieTrailerArrayList.size() != 0) {
                    String type = "Trailer";
                    // finalMovieArrayList contains only those MovieTrailer Objects
                    // where type is Trailer
                    finalMovieTrailerArrayList = new ArrayList<>();
                    for (int i = 0; i < movieTrailerArrayList.size(); i++) {
                        movieTrailer = movieTrailerArrayList.get(i);
                        if (movieTrailer.getType().equals(type)) {
                            finalMovieTrailerArrayList.add(movieTrailer);
                        }
                    }

                    movieTrailerAdapter = new MovieTrailerAdapter(MovieDetailActivity.this, finalMovieTrailerArrayList, recyclerViewOnItemClickListener);
                    rvTrailers.setAdapter(movieTrailerAdapter);
                } else {
                    //If trailers are not available for a movie,make the TextView Visibility Gone
                    txtTrailers.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<MovieTrailerResponse> call, Throwable t) {

            }
        });


    }

}
