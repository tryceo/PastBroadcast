package com.tryceo.jack.pastbroadcast.objects;

/**
 * Created by Jack on 2/5/2015.
 */
public class Channel {

    private String channelName;
    private String channelLogo;

    public StreamingPlatform getChannelPlatform() {
        return channelPlatform;
    }

    private StreamingPlatform channelPlatform;


    public Channel(){
        this.channelName = "";
    }
    public Channel (String channelName, StreamingPlatform channelPlatform){
        this.channelName = channelName;
        this.channelPlatform = channelPlatform;

    }

    public String getChannelName(){
        return this.channelName;
    }

    public String getChannelLogo(){
        return this.channelLogo;
    }

    public void setChannelName(String channelName){
        this.channelName = channelName;
    }

    public void setChannelLogo(String channelLogo){
        this.channelLogo = channelLogo;
    }
}
