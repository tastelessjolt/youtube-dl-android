package me.harshithgoka.youtubedl;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;

public class HttpGetter {

    OkHttpClient client;

    HttpGetter() {
        client = new OkHttpClient();
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
