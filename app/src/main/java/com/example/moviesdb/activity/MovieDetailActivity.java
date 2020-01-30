package com.example.moviesdb.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.moviesdb.R;
import com.example.moviesdb.adapter.TrailersAdapter;
import com.example.moviesdb.model.Movie;
import com.example.moviesdb.model.Trailer;
import com.example.moviesdb.util.MoviesUtil;
import com.example.moviesdb.util.NetworkService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.moviesdb.util.Constants.MOVIE_EXTRA;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;

    private TextView trailerTitle;
    private ListView trailerList;
    private TrailersAdapter trailerAdapter;

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

            URL videosURL = MoviesUtil.getMovieTrailersURL(String.valueOf(movie.getId()));
            new NetworkService(new TrailerResponseProcessor()).execute(videosURL);
            trailerTitle = findViewById(R.id.trailer_title);
            trailerList = findViewById(R.id.trailer_list);
            trailerAdapter = new TrailersAdapter();
            trailerList.setAdapter(trailerAdapter);

            trailerList.setOnItemClickListener(new ViewTrailer());
            ViewCompat.setNestedScrollingEnabled(trailerList, true);
        }
    }

    private void setTrailers(@NonNull List<Trailer> trailers) {
        if (trailers.isEmpty()) {
            trailerTitle.setVisibility(View.GONE);
        } else {
            trailerTitle.setVisibility(View.VISIBLE);
            trailerTitle.setText(getString(R.string.trailers, trailers.size()));
        }
        trailerAdapter.setTrailers(trailers);

    }

    private class TrailerResponseProcessor implements NetworkService.NetworkResponseProcessor {

        @Override
        public void onSuccess(@Nullable String response) {
            if (response == null) {
                response = "";
            }

            try {
                JSONObject responseObject = new JSONObject(response);
                JSONArray trailersJSONArray = responseObject.optJSONArray("results");
                if (trailersJSONArray == null) {
                    trailersJSONArray = new JSONArray("");
                }
                List<Trailer> trailers = new ArrayList<>();
                for (int i = 0 ; i < trailersJSONArray.length() ; i++) {
                    JSONObject trailerJSONObject = trailersJSONArray.getJSONObject(i);
                    String type = trailerJSONObject.optString("type", "");
                    if ("trailer".equalsIgnoreCase(type)) {
                        trailers.add(new Trailer(
                                trailerJSONObject.optString("id", ""),
                                trailerJSONObject.optString("name", ""),
                                trailerJSONObject.optString("key", ""),
                                trailerJSONObject.optString("site", "")
                        ));
                    }
                }
                setTrailers(trailers);
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setTrailers(Collections.<Trailer>emptyList());
        }

        @Override
        public void onFailure() {
            setTrailers(Collections.<Trailer>emptyList());
        }
    }

    private class ViewTrailer implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Trailer trailer = trailerAdapter.getItem(position);
            if (trailer != null) {
                String url = trailer.getExternalURL();
                Intent viewTrailerIntent = new Intent();
                viewTrailerIntent.setAction(Intent.ACTION_VIEW);
                viewTrailerIntent.setData(Uri.parse(url));
                if (viewTrailerIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(viewTrailerIntent);
                }
            }
        }
    }
}
