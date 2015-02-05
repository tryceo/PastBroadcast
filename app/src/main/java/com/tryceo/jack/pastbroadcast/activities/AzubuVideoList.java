package com.tryceo.jack.pastbroadcast.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tryceo.jack.pastbroadcast.R;
import com.tryceo.jack.pastbroadcast.helpers.AzubuVideoJSONParser;
import com.tryceo.jack.pastbroadcast.helpers.TwitchVideoJSONParser;
import com.tryceo.jack.pastbroadcast.objects.Video;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AzubuVideoList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azubu_video_list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_azubu_video_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    private class getTwitchVideos extends AsyncTask<String, Object, List<Video>> {
//        @Override
//        protected List<Video> doInBackground(String... urls) {
//            List<Video> videos = new ArrayList<Video>();
//            sentRequest = true;
//            try {
//                URL url = new URL(urls[0]);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream content = new BufferedInputStream(urlConnection.getInputStream());//get the stream of jsons
//                videos = AzubuVideoJSONParser.getVideos();
//
//                urlConnection.disconnect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return videos;
//        }
//
//        @Override
//        protected void onPostExecute(List<Video> result) {
//            videoArray.addAll(result);
//
//            adapter.notifyDataSetChanged();
//            sentRequest=false;
//            process.dismiss();
//        }
//    }
}
