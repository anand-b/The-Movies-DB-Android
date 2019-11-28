package com.example.moviesdb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdb.R;
import com.example.moviesdb.model.Movie;
import com.example.moviesdb.util.MoviesUtil;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    public interface MovieDisplayListener {
        void onDisplayMovie(@NonNull Movie movie);
    }

    private List<Movie> movies;
    private final MovieDisplayListener listener;

    public MoviesAdapter(@Nullable MovieDisplayListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);
        return new MoviesViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }


    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Nullable
    private Movie getItemAtPosition(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return movies.get(position);
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final ImageView poster;
        private final MovieDisplayListener displayListener;

        MoviesViewHolder(@NonNull View itemView, @Nullable MovieDisplayListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_item_title);
            poster = itemView.findViewById(R.id.movie_item_poster);
            itemView.setOnClickListener(this);

            displayListener = listener;
        }

        void bind(Movie movie) {
            URL url = MoviesUtil.getImageUrl(movie.getPosterPath());
            if (url != null) {
                Picasso.get()
                        .load(url.toString())
                        .noPlaceholder()
                        .into(poster);
            }

            title.setText(movie.getTitle());
        }

        @Override
        public void onClick(View v) {
            Movie movie = getItemAtPosition(getAdapterPosition());
            if (displayListener != null && movie != null) {
                displayListener.onDisplayMovie(movie);
            }
        }
    }
}
