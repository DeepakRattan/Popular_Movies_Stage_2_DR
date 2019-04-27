package com.example.deepakrattan.popular_movies_stage_2_dr.activity;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.deepakrattan.popular_movies_stage_2_dr.R;
import com.example.deepakrattan.popular_movies_stage_2_dr.adapter.FavouriteMoviePosterAdapter;
import com.example.deepakrattan.popular_movies_stage_2_dr.adapter.MoviePosterAdapter;
import com.example.deepakrattan.popular_movies_stage_2_dr.database.FavouriteMovies;
import com.example.deepakrattan.popular_movies_stage_2_dr.database.FavouriteMoviesDAO;
import com.example.deepakrattan.popular_movies_stage_2_dr.database.FavouriteMoviesDB;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.Movie;
import com.example.deepakrattan.popular_movies_stage_2_dr.model.MovieListResponse;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.APIClient;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.APIInterface;
import com.example.deepakrattan.popular_movies_stage_2_dr.network.ConnectivityHelper;
import com.example.deepakrattan.popular_movies_stage_2_dr.util.RecyclerViewOnItemClickListener;
import com.example.deepakrattan.popular_movies_stage_2_dr.util.Shared_Pref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.deepakrattan.popular_movies_stage_2_dr.util.Shared_Pref.PREF_NAME;

public class MoviesActivity extends AppCompatActivity {
    private RecyclerView rv_movies;
    private RecyclerView.LayoutManager layoutManager;
    private APIInterface apiInterface;
    private ArrayList<Movie> movieArrayList;
    private MoviePosterAdapter adapter;
    private FavouriteMoviePosterAdapter favouriteMoviePosterAdapter;
    private SharedPreferences sharedPreferences;
    private String sort_type;
    private FavouriteMovies favouriteMovies;
    private FavouriteMoviesDAO favouriteMoviesDAO;
    private FavouriteMoviesDB favouriteMoviesDB;
    public static final String Database_Name = "FavouriteMoviesDB";
    private List<FavouriteMovies> favouriteMoviesList;
    RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        //findViewByID
        rv_movies = findViewById(R.id.rv_movies);
        layoutManager = new GridLayoutManager(this, 2);
        rv_movies.setLayoutManager(layoutManager);


        favouriteMoviesDB = Room.databaseBuilder(getApplicationContext(), FavouriteMoviesDB.class, Database_Name).build();
        favouriteMoviesDAO = favouriteMoviesDB.getDAO();

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        sort_type = sharedPreferences.getString("sort_type", "popular");

        if (sort_type.equals("popular"))
            getSupportActionBar().setTitle(R.string.popular);
        else if (sort_type.equals("top_rated"))
            getSupportActionBar().setTitle(R.string.top_rated);
        else if (sort_type.equals("favourite"))
            getSupportActionBar().setTitle(R.string.favourite);

        //Checking for internet connectivity
        boolean isConnectedToInternet = ConnectivityHelper.isConnectedToNetwork(MoviesActivity.this);
        if (isConnectedToInternet) {
            getMovies(sort_type);
        } else {
            Toast.makeText(this, "Please check internet connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    void getMovies(String sort_type) {
        /*Create handle for the APIInterface interface*/
        apiInterface = APIClient.getRetrofitInstanceMovie().create(APIInterface.class);
        if (sort_type.equals("popular")) {
            //get popular movie by hitting API
            getPopularMovies();
        } else if (sort_type.equals("top_rated")) {
            //get top_rated movie by hitting API
            getTopRatedMovies();
        } else if (sort_type.equals("favourite")) {
            //get favourite Movies from Room Database
            getFavouriteMovies();
        }
    }

    //Get favourite Movies from Room Database
    private void getFavouriteMovies() {
        new ViewFavouriteMoviesTask().execute();
    }

    public class ViewFavouriteMoviesTask extends AsyncTask<Void, Void, List<FavouriteMovies>> {
        @Override
        protected List<FavouriteMovies> doInBackground(Void... voids) {
            favouriteMoviesList = favouriteMoviesDAO.getFavouriteMovies();
            return favouriteMoviesList;
        }

        @Override
        protected void onPostExecute(List<FavouriteMovies> favouriteMovies) {
            super.onPostExecute(favouriteMovies);
            recyclerViewOnItemClickListener = new RecyclerViewOnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    FavouriteMovies favouriteMovies = favouriteMoviesList.get(position);
                    Intent intent = new Intent(MoviesActivity.this, MovieDetailActivity.class);
                    intent.putExtra(Shared_Pref.MOVIE_DETAIL, favouriteMovies);
                    startActivity(intent);
                }
            };
            favouriteMoviePosterAdapter = new FavouriteMoviePosterAdapter(MoviesActivity.this, favouriteMovies, recyclerViewOnItemClickListener);
            rv_movies.setAdapter(favouriteMoviePosterAdapter);
        }
    }

    //Get popular movies
    void getPopularMovies() {
        Call<MovieListResponse> call = apiInterface.getPopularMovies(APIClient.api_key);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                MovieListResponse movieListResponse = response.body();
                movieArrayList = (ArrayList<Movie>) movieListResponse.getResults();
                RecyclerViewOnItemClickListener recyclerViewOnItemClickListener = new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Movie movie = movieArrayList.get(position);
                        Intent intent = new Intent(MoviesActivity.this, MovieDetailActivity.class);
                        intent.putExtra(Shared_Pref.MOVIE_DETAIL, movie);
                        startActivity(intent);
                    }
                };
                adapter = new MoviePosterAdapter(MoviesActivity.this, movieArrayList, recyclerViewOnItemClickListener);
                rv_movies.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {

            }
        });
    }

    //Get Top Rated Movies
    void getTopRatedMovies() {

        Call<MovieListResponse> call = apiInterface.getTopRatedMovies(APIClient.api_key);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                MovieListResponse movieListResponse = response.body();
                movieArrayList = (ArrayList<Movie>) movieListResponse.getResults();
                RecyclerViewOnItemClickListener recyclerViewOnItemClickListener = new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Movie movie = movieArrayList.get(position);
                        Intent intent = new Intent(MoviesActivity.this, MovieDetailActivity.class);
                        intent.putExtra(Shared_Pref.MOVIE_DETAIL, movie);
                        startActivity(intent);
                    }
                };
                adapter = new MoviePosterAdapter(MoviesActivity.this, movieArrayList, recyclerViewOnItemClickListener);
                rv_movies.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_sort_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            int selected = 0;
            sort_type = sharedPreferences.getString("sort_type", "popular");
            if (sort_type.equals("popular"))
                selected = 0;
            else if (sort_type.equals("top_rated"))
                selected = 1;
            else if (sort_type.equals("favourite"))
                selected = 2;
            builder.setTitle(R.string.dialog_title);
            builder.setSingleChoiceItems(R.array.sort_types, selected, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0)
                        editor.putString("sort_type", "popular");
                    else if (i == 1)
                        editor.putString("sort_type", "top_rated");
                    else if (i == 2)
                        editor.putString("sort_type", "favourite");
                }
            });

            builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editor.commit();

                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //user clicked cancel
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //refresh activity
                    Intent intent = new Intent(MoviesActivity.this, MoviesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
