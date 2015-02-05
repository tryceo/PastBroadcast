package com.tryceo.jack.pastbroadcast.helpers;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jack on 2/5/2015.
 */
public class AzubuChannelParser {
    public static String getChannelLogo(InputStream in) throws IOException {
        //Gets the initial JSON object, and then passes the "data" part to readVideos;
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        String url = "";
        try {
            reader.beginObject();
            while (reader.hasNext() && url.length()== 0) {
                String name = reader.nextName();
                if (name.equals("data")) {
                    url = getChannelLogoURL(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }
        return url;
    }

    private static String getChannelLogoURL(JsonReader reader) throws IOException {

            reader.beginObject();

            String result = "404";
            while (reader.hasNext() && !result.equals("404")) {
                String name = reader.nextName();
                if (name.equals("url_thumbnail")) {
                    result = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

            return result;
    }
}
