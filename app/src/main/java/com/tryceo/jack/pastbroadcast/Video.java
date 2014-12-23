package com.tryceo.jack.pastbroadcast;

/**
 * Created by Jack on 10/15/2014.
 *
 * Class for a video object.
 *
 * Used for Twitch videos
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


    public void setTitle(String a) {
        title = a;
    }

    public void setId(String a) {
        id = a;
    }

    public void setRecordedAt(String a) {
        recordedAt = (a.replaceAll("T", " ")).replaceAll("Z", "");
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
