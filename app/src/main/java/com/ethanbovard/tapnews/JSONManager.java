package com.ethanbovard.tapnews;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URL;

public class JSONManager {
    public static Object Deserialize (String requestUri, Class<Object> type) {
        Object obj = null;
        try {
            URL url = new URL(requestUri);
            String data = URLReader.downloadStringFromUrl(url);
            Gson gson = new Gson();
            obj = gson.fromJson(data, type);
        }
        catch (Exception exc) {
            // do some logging stuff here
        }
        return obj;
    }
}