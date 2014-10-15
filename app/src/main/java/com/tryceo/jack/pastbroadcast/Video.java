package com.tryceo.jack.pastbroadcast;

/**
 * Created by Jack on 10/15/2014.
 */
public class Video {

    public String title;
    public String id;
    public String recordedAt;
    public int length;
    public String preview;

    public Video() {
        title = "";
        id = "";
        recordedAt = "";
        length = 0;
        preview = "";
    }

    public Video(String a, String b, String c, int d, String e) {
        title = a;
        id = b;
        recordedAt = c;
        length = d;
        preview = e;
    }

    public void setTitle(String a) {
        title = a;
    }

    public void setId(String a) {
        id = a;
    }

    public void setRecordedAt(String a) {

        recordedAt = a;
    }

    public void setLength(int a) {
        length = a;
    }

    public void setPreview(String a) {
        preview = a;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public int getLength() {
        return length;
    }

    public String getPreview() {
        return preview;
    }
}
