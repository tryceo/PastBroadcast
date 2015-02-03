package com.tryceo.jack.pastbroadcast.objects;

/**
 * Created by Jack on 12/22/2014.
 *
 * Class for a video object.
 *
 * Used for Azubu videos
 */

public class AzubuVideo extends Video {

    @Override
    public void setLength(int a) {
        super.setLength(a/1000);//Azubu uses milliseconds for some reason...
    }

}
