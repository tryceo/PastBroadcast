package com.tryceo.jack.pastbroadcast;

/**
 * Created by Jack on 12/22/2014.
 */
public class AzubuVideo extends Video{

    public String videoUrl;

    public AzubuVideo() {
        super();
    }

    public void setVideoUrl(String url){
        videoUrl = url;
    }

    public String getVideoUrl(){
        return videoUrl;
    }
}
