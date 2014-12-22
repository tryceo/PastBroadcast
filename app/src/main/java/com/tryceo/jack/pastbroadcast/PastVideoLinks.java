package com.tryceo.jack.pastbroadcast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PastVideoLinks extends Activity {

    private ArrayAdapter<String> adapter;
    ProgressDialog process;
    List<String> videoArray;
    private static final String apiURL = "https://api.twitch.tv/kraken/channels/%s/videos?limit=10&offset=%d&broadcasts=true";
    public final static String ID = "Channel Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_video_links);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Home.CHANNEL_NAME);
        process = new ProgressDialog(this);
        process.setTitle("Loading...");
        process.setMessage("Please Wait");

        videoArray = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, videoArray);
        ListView listView = (ListView) findViewById(R.id.videolist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PastVideoLinks.this, VideoLinks.class);
                intent.putExtra(PastVideoLinks.ID, videoArray.get(i));
                startActivity(intent);
            }
        });


        getTwitchVideos  task = new getTwitchVideos();
        task.execute(String.format(apiURL, message, 0));
        process.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.past_video_links, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class getTwitchVideos extends AsyncTask<String, Object, List<Video>> {
        @Override
        protected List<Video> doInBackground(String... urls) {
            List<Video> videos = new ArrayList<Video>();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream content = new BufferedInputStream(urlConnection.getInputStream());//get the stream of jsons
                videos = TwitchVideoJSONParser.getVideos(content);
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return videos;
        }

        @Override
        protected void onPostExecute(List<Video> result) {
            adapter.clear();
            List<String> videos = new ArrayList<String>();
            List<String> videoLinks = new ArrayList<String>();
            for (int i = 0; i < result.size(); i++) {
                videos.add(result.get(i).getTitle());
                videoLinks.add(result.get(i).getId());
            }
            videoArray = videoLinks;
            adapter.addAll(videos);
            adapter.notifyDataSetChanged();
            process.dismiss();
        }
    }


}
