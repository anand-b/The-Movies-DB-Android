package com.example.moviesdb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.moviesdb.R;
import com.example.moviesdb.model.Trailer;

import java.util.List;


public class TrailersAdapter extends BaseAdapter {
    private List<Trailer> trailers;

    @Override
    public int getCount() {
        return trailers != null ? trailers.size() : 0;
    }

    @Override
    public Trailer getItem(int position) {
        return position >= 0 && position < getCount() ? trailers.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.trailer_item_view, parent, false);
        }
        TextView trailerName = convertView.findViewById(R.id.trailer_name);
        Trailer trailer = getItem(position);
        if (trailer != null) {
            trailerName.setText(trailer.getName());
        }
        return convertView;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }
}
