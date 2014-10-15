package com.tryceo.jack.pastbroadcast;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 10/13/2014.
 */
public class TwitchVideoXmlParser {

    private static final String qualitySource = "video_file_url";



    public static List<String> getLinksFromXML (InputStream in) throws XmlPullParserException, IOException{
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser, qualitySource);
        } finally {
            in.close();
        }

    }
    private static List<String> readFeed(XmlPullParser parser, String quality) throws XmlPullParserException, IOException{
        List<String> links = new ArrayList<String>();
        while (parser.next() != XmlPullParser.END_DOCUMENT){
            if (parser.getEventType()==XmlPullParser.START_TAG && parser.getName().equals(quality)){
                links.add(parser.nextText());
            }
        }
        return links;
    }


}
