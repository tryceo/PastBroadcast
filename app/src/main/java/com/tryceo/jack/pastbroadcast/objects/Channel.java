package com.tryceo.jack.pastbroadcast.objects;



/**
 * Created by Jack on 2/5/2015.
 *
 * Stores information for a Channel Object, like the Name and the URL of the logo
 */
public class Channel implements Comparable<Channel>{

    private String channelName;
    private String channelLogo;

    public StreamingPlatform getChannelPlatform() {
        return channelPlatform;
    }

    private StreamingPlatform channelPlatform;


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

    public void setChannelLogo(String channelLogo){
        this.channelLogo = channelLogo;
    }

    @Override

    public int compareTo(Channel channel) {
        if (channel == null){
            throw new NullPointerException("channel object is null");
        }
        else
            return this.channelName.compareTo(channel.getChannelName());

    }
}
