package com.tryceo.jack.pastbroadcast;

/**
 * Created by Jack on 12/22/2014.
 *
 * Class for a video object.
 *
 * Used for Azubu videos
 */

public class AzubuVideo extends Video{

    private String videoUrl;

    public AzubuVideo() {
        super();
    }

    @Override
    public void setLength(int a) {
        super.setLength(a/1000);//Azubu uses milliseconds for some reason...
    }

    public void setVideoUrl(String url){
        videoUrl = url;
    }

    public String getVideoUrl(){
        return videoUrl;
    }
}
