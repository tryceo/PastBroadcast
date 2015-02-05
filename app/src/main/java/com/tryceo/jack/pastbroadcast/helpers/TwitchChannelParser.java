package com.tryceo.jack.pastbroadcast.helpers;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Jack on 2/5/2015.
 */
public class TwitchChannelParser {

    public static String getChannelLogo(InputStream in) throws IOException {
        //Gets the initial JSON object, and then passes the "data" part to readVideos;
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        String url = "";
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("logo")) {
                    return reader.nextString();
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
}
