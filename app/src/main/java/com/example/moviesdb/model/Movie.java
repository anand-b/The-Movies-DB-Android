package com.example.moviesdb.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Movie implements Parcelable, Model {
    private final long id;
    @Nullable private final String title;
    @Nullable private final String posterPath;
    @Nullable private final String overview;
    private final long voteCount;
    private final double voteAverage;
    @Nullable private final String date;

    public Movie(long id, @Nullable String title, @Nullable String posterPath, @Nullable String overview, long voteCount, double voteAvg, @Nullable String date) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteCount = voteCount;
        this.voteAverage = voteAvg;
        this.date = date;
    }

    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteCount = in.readLong();
        voteAverage = in.readDouble();

        date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @NonNull
    public long getId() {return id;}

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getOverview() {
        return overview;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    public long getVoteCount() {
        return voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeLong(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeString(date);
    }
}
