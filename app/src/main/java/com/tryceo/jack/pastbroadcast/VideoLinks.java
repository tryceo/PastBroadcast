package com.tryceo.jack.pastbroadcast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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


public class VideoLinks extends Activity {

    private ArrayAdapter<String> adapter;
    ProgressDialog process;
    List<String> linksArray;
    private static final String apiURL = "https://api.twitch.tv/api/videos/%s";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_links);
        Intent intent = getIntent();
        String message = intent.getStringExtra(PastVideoLinks.ID);
        process = new ProgressDialog(this);
        process.setTitle("Loading...");
        process.setMessage("Please Wait");

        linksArray = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, linksArray);
        ListView listView = (ListView) findViewById(R.id.listlinks);
        listView.setAdapter(adapter);

        //Adds the onclick function for each object
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(linksArray.get(i)), "video/flv");
                startActivity(intent);
            }
        });


        getTwitchLinks task = new getTwitchLinks();
        task.execute(String.format(apiURL, message));
        process.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_links, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }


    private class getTwitchLinks extends AsyncTask<String, Object, List<Chunk>> {
        @Override
        protected List<Chunk> doInBackground(String... urls) {
            List<Chunk> chunks = new ArrayList<Chunk>();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream content = new BufferedInputStream(urlConnection.getInputStream());//get the stream of jsons
                chunks = TwitchVideoJSONParser.getChunks(content);
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return chunks;
        }

        @Override
        protected void onPostExecute(List<Chunk> result) {
            adapter.clear();
            List<String> videos = new ArrayList<String>();
            List<String> videoLinks = new ArrayList<String>();
            for (int i = 0; i < result.size(); i++) {
                int secs = result.get(i).getLength();
                int min = secs / 60;
                int sec = secs % 60;
                videos.add(String.format("Video #%d   Length: %d:%s", i + 1, min, sec));
                videoLinks.add(result.get(i).getURL());
            }
            linksArray = videoLinks;
            adapter.addAll(videos);
            adapter.notifyDataSetChanged();
            process.dismiss();
        }
    }
}
