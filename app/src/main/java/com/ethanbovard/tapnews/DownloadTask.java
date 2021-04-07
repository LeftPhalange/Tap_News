package com.ethanbovard.tapnews;

import android.os.AsyncTask;

import java.net.URL;
import java.util.concurrent.Callable;

public class DownloadTask implements Callable<String> {
    URL url;
    @Override
    public String call() throws Exception {
        return JSONManager.DownloadStringFromURL(url);
    }
}
