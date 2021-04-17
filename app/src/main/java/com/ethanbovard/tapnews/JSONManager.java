package com.ethanbovard.tapnews;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JSONManager {
    public static Object Deserialize (URL url, Class<?> cl) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        DownloadTask task = new DownloadTask();
        task.url = url;
        Future<String> future = executor.submit(task);
        String json = future.get();
        executor.shutdown();
        Log.v("JSONManager", json);
        return new Gson().fromJson(json, cl);
    }
    public static String DownloadStringFromURL (URL url) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder data = new StringBuilder();
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            data.append(s);
        }
        return data.toString();
    }
}
