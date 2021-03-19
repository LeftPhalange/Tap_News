package com.ethanbovard.tapnews;

import java.net.*;
import java.io.*;

public class URLReader {
    public static String downloadStringFromUrl(URL url) {
        String data = "";
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String inputLine;
            StringBuilder sBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                sBuilder.append(inputLine);
            in.close();
            data = sBuilder.toString();
        } catch (IOException exc) {
            // do something
        }
        return data;
    }
}