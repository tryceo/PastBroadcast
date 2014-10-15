package com.tryceo.jack.pastbroadcast;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 10/14/2014.
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
                    chunks = readChunks(reader);
                    return chunks;
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
            if (name.equals("url")){
                chunk.setURL(reader.nextString());
            } else if (name.equals("length")){
                chunk.setLength(reader.nextInt());
            } else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return chunk;
    }


}



