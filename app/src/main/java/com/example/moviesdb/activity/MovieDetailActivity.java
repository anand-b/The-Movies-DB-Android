package com.example.moviesdb.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesdb.R;
import com.example.moviesdb.model.Movie;
import com.example.moviesdb.util.MoviesUtil;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.example.moviesdb.util.Constants.MOVIE_EXTRA;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(MOVIE_EXTRA)) {
            movie = intent.getParcelableExtra(MOVIE_EXTRA);
        }

        if (movie != null) {
            setTitle(movie.getTitle());
            TextView title = findViewById(R.id.movie_title);
            title.setText(movie.getTitle());

            TextView overview = findViewById(R.id.overview);
            overview.setText(movie.getOverview());

            TextView releaseDate = findViewById(R.id.release_date);
            releaseDate.setText(movie.getDate());

            TextView voteAvg = findViewById(R.id.vote_average);
            voteAvg.setText(getResources().getQuantityString(R.plurals.vote_average_text, movie.getVoteCount() == 1 ? 1 : 2, String.valueOf(movie.getVoteAverage()), movie.getVoteCount()));

            ImageView poster = findViewById(R.id.movie_poster);
            URL url = MoviesUtil.getImageUrl(movie.getPosterPath());
            if (url != null) {
                Picasso.get()
                        .load(url.toString())
                        .noPlaceholder()
                        .into(poster);
            }
        }
    }
}
