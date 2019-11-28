package com.example.moviesdb.util;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

enum NetworkStatus {
    PENDING,
    EMPTY,
    ERROR,
    SUCCESS
}

class NetworkResponse {
    final int status;
    final String message;

    NetworkResponse(int status, @Nullable String message) {
        this.status = status;
        this.message = message;
    }
}

public class NetworkService extends AsyncTask<URL, Void, NetworkResponse> {

    private @NonNull NetworkStatus status = NetworkStatus.PENDING;


    public interface NetworkResponseProcessor {
        void onSuccess(@Nullable String response);
        void onFailure();
    }

    @Nullable private final NetworkResponseProcessor processor;

    public NetworkService(@Nullable NetworkResponseProcessor processor) {
        this.processor = processor;
    }

    @Override
    protected void onPreExecute() {
        status = NetworkStatus.PENDING;
    }

    private NetworkResponse fetch(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return new NetworkResponse(urlConnection.getResponseCode(), scanner.next());
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected NetworkResponse doInBackground(URL... urls) {
        URL fetchUrl = urls[0];
        try {
            NetworkResponse response = fetch(fetchUrl);
            if (response == null) {
                status = NetworkStatus.EMPTY;
            } else if (response.status == 200) {
                status = NetworkStatus.SUCCESS;
            } else if (response.status == 500){
                status = NetworkStatus.ERROR;
            } else {
                status = NetworkStatus.PENDING;
            }
            return response;
        } catch (IOException e) {
            status = NetworkStatus.ERROR;
            return new NetworkResponse(500, e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(NetworkResponse response) {
        if (processor != null && status != NetworkStatus.PENDING) {
            if (status == NetworkStatus.ERROR) {
                processor.onFailure();
            } else {
                processor.onSuccess(response.message);
            }
        }
    }
}