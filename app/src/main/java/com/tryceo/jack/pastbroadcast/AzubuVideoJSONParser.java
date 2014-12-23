package com.tryceo.jack.pastbroadcast;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 10/28/2014.
 */
public class AzubuVideoJSONParser {

    public static List<Video> getVideos(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Video> videos = new ArrayList<Video>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("data")) {
                    return readVideos(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }
        return videos;
    }

    private static List<Video> readVideos(JsonReader reader) throws IOException {
        List<Video> videos = new ArrayList<Video>();
        reader.beginArray();
        while (reader.hasNext()) {
            videos.add(readVideo(reader));
        }
        reader.endArray();
        return videos;
    }

    private static Video readVideo(JsonReader reader) throws IOException {
        reader.beginObject();
        Video v = new AzubuVideo();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                v.setId(reader.nextString());
            } else if (name.equals("title")) {
                v.setTitle(reader.nextString());
            } else if (name.equals("created_at")) {
                v.setRecordedAt(reader.nextString());
            } else if (name.equals("duration")) {
                v.setLength(reader.nextInt());
            } else if (name.equals("thumbnail")) {
                v.setPreview(reader.nextString());
            } else if (name.equals("stream_params")) {
                ((AzubuVideo) v).setVideoUrl(readParams(reader.nextString())); //Casting takes place
            } else {
                reader.skipValue();
            }
        }

        reader.endObject();
        return v;
    }

    private static String readParams(String params) throws IOException {

        JsonReader reader = new JsonReader(new StringReader(params));

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("FLVURL")) {
                    return reader.nextString();//Returns a URL to the mp4 stream, not flv
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }

        return ""; //This should never happen

    }
}
