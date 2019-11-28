package com.example.moviesdb.util;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

import static com.example.moviesdb.util.Constants.API_KEY;

public class MoviesUtil {

    private static final String MOVIES_API_HOST = "api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String API_PATH_PREFIX = "movie";
    private static final String API_KEY_PARAM = "api_key";

    private static final String MOVIE_IMAGE_API_HOST = "image.tmdb.org";
    private static final String MOVIE_IMAGE_SIZE = "w500";

    private static Uri.Builder getMovieBaseUriBuilder() {
        return new Uri.Builder()
                .scheme("https")
                .authority(MOVIES_API_HOST)
                .appendPath(API_VERSION)
                .appendPath(API_PATH_PREFIX);
    }

    public static URL getSortedMoviesURL(@NonNull String sortBy) {
        try {
            Uri.Builder builder = getMovieBaseUriBuilder();
            builder.appendPath(sortBy);
            builder.appendQueryParameter(API_KEY_PARAM, API_KEY);
            return new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Uri.Builder getImageBaseUrl() {
        return new Uri.Builder()
                .scheme("https")
                .authority(MOVIE_IMAGE_API_HOST)
                .appendPath("t")
                .appendPath("p")
                .appendPath(MOVIE_IMAGE_SIZE);
    }

    public static URL getImageUrl(@Nullable String image) {
        if (image != null) {
            try {
                Uri.Builder builder = getImageBaseUrl();
                if (image.charAt(0) == '/') {
                    image = image.substring(1);
                }
                builder.appendPath(image);
                return new URL(builder.build().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
