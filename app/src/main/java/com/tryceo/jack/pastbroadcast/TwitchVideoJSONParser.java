package com.tryceo.jack.pastbroadcast;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 10/28/2014.
 *
 * Helper class that parses the JSON returned by the Twitch api
 *
 * All static methods
 */

public class TwitchVideoJSONParser {

    public static List<Chunk> getChunks(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Chunk> chunks = new ArrayList<Chunk>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("chunks")) {
                    return readChunks(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } finally {
            reader.close();
        }
        return chunks;
    }

    private static List<Chunk> readChunks(JsonReader reader) throws IOException {
        List<Chunk> chunks = new ArrayList<Chunk>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("live")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    chunks.add(readChunk(reader));
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return chunks;
    }

    private static Chunk readChunk(JsonReader reader) throws IOException {
        Chunk chunk = new Chunk();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("url")) {
                chunk.setURL(reader.nextString());
            } else if (name.equals("length")) {
                chunk.setLength(reader.nextInt());
            } else {
                reader.skipValue();

            }
        }
        reader.endObject();
        return chunk;
    }

    public static List<Video> getVideos(InputStream in) throws IOException {
        //Gets the initial JSON object, and then passes the "videos" part to readVideos;
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Video> videos = new ArrayList<Video>();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("videos")) {
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
        //Gets the JSON array "video", and passes each JSON Object to readVideo. Returns a List of Videos
        List<Video> videos = new ArrayList<Video>();
        reader.beginArray();
        while (reader.hasNext()) {
            videos.add(readVideo(reader));
        }
        reader.endArray();
        return videos;
    }

    private static Video readVideo(JsonReader reader) throws IOException {
        //Gets the JSON Object video, parses it, and then returns a Video Object
        reader.beginObject();
        Video v = new Video();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                v.setTitle(reader.nextString());
            } else if (name.equals("_id")) {
                v.setId(reader.nextString());
            } else if (name.equals("recorded_at")) {
                v.setRecordedAt(reader.nextString());
            } else if (name.equals("length")) {
                v.setLength(reader.nextInt());
            } else if (name.equals("preview")) {
                v.setPreview(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return v;
    }
}



