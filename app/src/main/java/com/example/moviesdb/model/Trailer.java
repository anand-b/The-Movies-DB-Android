package com.example.moviesdb.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Trailer implements Model {
    @NonNull private String id;
    @NonNull private String name;
    @NonNull private String externalId;
    @NonNull private String source;


    public Trailer(String id, String name, String externalId, String source) {
        this.id = id;
        this.name = name;
        this.externalId = externalId;
        this.source = source;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getExternalURL() {
        if ("youtube".equalsIgnoreCase(source)) {
            return "https://www.youtube.com/watch?v="+externalId;
        } else if ("vimeo".equalsIgnoreCase(source)) {
            return "https://www.vimeo.com/"+externalId;
        }
        return null;
    }
}
