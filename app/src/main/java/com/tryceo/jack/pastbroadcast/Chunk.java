package com.tryceo.jack.pastbroadcast;

/**
 * Created by Jack on 10/14/2014.
 */
public class Chunk {
    public String url = "";
    public int length = 0;

    public Chunk() {
        url = "";
        length = 0;
    }


    public Chunk(String a, int b) {
        url = a;
        length = b;
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
