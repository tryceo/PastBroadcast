package com.tryceo.jack.pastbroadcast.Objects;

/**
 * Created by Jack on 10/15/2014.
 * <p/>
 * Class for a chunk object.
 * <p/>
 * Used for Twitch videos because they split their videos into 30 minute flv chunks
 */

public class Chunk {
    private String url = "";
    private int length = 0;

    public Chunk() {
        url = "";
        length = 0;
    }

    public void setURL(String a) {
        url = a;
    }

    public void setLength(int a) {
        length = a;
    }

    public String getURL() {
        return url;
    }

    public int getLength() {
        return length;
    }
}
