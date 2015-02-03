package com.tryceo.jack.pastbroadcast.objects;

/**
 * Created by Jack on 2/2/2015.
 */
public class TwitchVideo extends Video{

    private static final String apiURL = "https://api.twitch.tv/api/videos/%s";

    @Override
    public void setId(String a) {
       super.setId(a);
       super.setVideoUrl(String.format(apiURL, this.getId()));
    }

    @Override
    public void setRecordedAt(String a) {
        super.setRecordedAt((a.replaceAll("T", " ")).replaceAll("Z", ""));
    }

}
