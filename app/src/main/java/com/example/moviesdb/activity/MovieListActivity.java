package com.example.moviesdb.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviesdb.R;
import com.example.moviesdb.adapter.MoviesAdapter;
import com.example.moviesdb.model.Movie;
import com.example.moviesdb.util.MoviesUtil;
import com.example.moviesdb.util.NetworkService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.moviesdb.util.Constants.MOVIE_EXTRA;

public class MovieListActivity
        extends AppCompatActivity
        implements NetworkService.NetworkResponseProcessor, MoviesAdapter.MovieDisplayListener {

    enum SortBy {
        POPULARITY("popular", "Popular"),
        RATING("top_rated", "Top Rated");

        private final String sorter;
        private final String text;
        SortBy(String sorter, String text) {
            this.sorter = sorter;
            this.text = text;
        }

        String getSorter() {
            return sorter;
        }

        String getText() {
            return text;
        }
    }

    private SortBy sortFilter;
    private RecyclerView moviesRecyclerView;
    private MoviesAdapter adapter;
    private ProgressBar loading;
    private TextView emptyMoviesMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        moviesRecyclerView = findViewById(R.id.rv_movies_list);
        loading = findViewById(R.id.loading);
        emptyMoviesMessage = findViewById(R.id.empty_list);
        adapter = new MoviesAdapter(this);

        moviesRecyclerView.setAdapter(adapter);
        moviesRecyclerView.setHasFixedSize(true);

        int numCols = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            numCols = 3;
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numCols, RecyclerView.VERTICAL, false);
        moviesRecyclerView.setLayoutManager(layoutManager);

        sortFilter = SortBy.POPULARITY;
        setTitleByFilter();
        loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    private void setTitleByFilter() {
        setTitle(getString(R.string.sorted_by, sortFilter.getText()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_by_popularity) {
            if (sortFilter != SortBy.POPULARITY) {
                sortFilter = SortBy.POPULARITY;
                setTitleByFilter();
                loadMovies();
            }
            return true;
        } else if (id == R.id.action_sort_by_rating) {
            if (sortFilter != SortBy.RATING) {
                sortFilter = SortBy.RATING;
                setTitleByFilter();
                loadMovies();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {
        moviesRecyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        emptyMoviesMessage.setVisibility(View.GONE);
        URL url = MoviesUtil.getSortedMoviesURL(sortFilter.getSorter());
        new NetworkService(this).execute(url);
    }

    private void setMovies(@NonNull List<Movie> movies) {
        adapter.setMovies(movies);
        loading.setVisibility(View.GONE);
        if (movies.isEmpty()) {
            emptyMoviesMessage.setVisibility(View.VISIBLE);
        } else {
            emptyMoviesMessage.setVisibility(View.GONE);
            moviesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(@Nullable String response) {
        if (response == null) {
            response = "";
        }

        try {
            JSONObject responseObject = new JSONObject(response);
            JSONArray moviesJSONArray = responseObject.optJSONArray("results");
            if (moviesJSONArray == null) {
                moviesJSONArray = new JSONArray("");
            }
            List<Movie> movies = new ArrayList<>();
            for (int i = 0 ; i < moviesJSONArray.length() ; i++) {
                JSONObject movieJSONObject = moviesJSONArray.getJSONObject(i);
                movies.add(new Movie(
                        movieJSONObject.optLong("id"),
                        movieJSONObject.optString("title", ""),
                        movieJSONObject.optString("poster_path"),
                        movieJSONObject.optString("overview", ""),
                        movieJSONObject.optLong("vote_count", 0L),
                        movieJSONObject.optDouble("vote_average", 0.0),
                        movieJSONObject.optString("release_date", "")
                ));
            }
            setMovies(movies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure() {
        setMovies(Collections.<Movie>emptyList());
    }

    @Override
    public void onDisplayMovie(@NonNull Movie movie) {
        Intent movieDetailsIntent = new Intent(this, MovieDetailActivity.class);
        movieDetailsIntent.putExtra(MOVIE_EXTRA, movie);
        startActivity(movieDetailsIntent);
    }
}
