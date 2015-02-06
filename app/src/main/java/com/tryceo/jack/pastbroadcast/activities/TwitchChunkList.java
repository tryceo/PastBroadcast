package com.tryceo.jack.pastbroadcast.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tryceo.jack.pastbroadcast.R;
import com.tryceo.jack.pastbroadcast.helpers.TwitchVideoJSONParser;
import com.tryceo.jack.pastbroadcast.objects.Chunk;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack
 * <p/>
 * This class shows a list of chunks for a given video on Twitch
 * <p/>
 * Receives intent from TwitchVideoList
 * <p/>
 * Passes intent to external app to open flv files
 */

public class TwitchChunkList extends Activity {

    private BaseAdapter adapter;
    ProgressDialog process;
    List<Chunk> chunksArray;
    private static final String apiURL = "https://api.twitch.tv/api/videos/%s";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chunk_links);
        Intent intent = getIntent();
        String message = intent.getStringExtra(TwitchVideoList.ID);

        ActionBar actionBar = getActionBar();
        if (actionBar != null)//Just in case, should not happen, though
            actionBar.setDisplayHomeAsUpEnabled(false);

        process = new ProgressDialog(this);
        process.setTitle("Loading...");
        process.setMessage("Please Wait");

        chunksArray = new ArrayList<Chunk>();
        adapter = new ChunkAdapter(chunksArray);
        ListView listView = (ListView) findViewById(R.id.chunkList);
        listView.setAdapter(adapter);

        //Adds the onclick function for each object
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(chunksArray.get(i).getURL()), "video/flv");
                startActivity(intent);
            }
        });


        getTwitchLinks task = new getTwitchLinks();
        task.execute(String.format(apiURL, message));
        process.show();
    }

    public class ChunkAdapter extends BaseAdapter {

        private List<Chunk> chunks;

        public ChunkAdapter(List<Chunk> chunks) {
            this.chunks = chunks;
        }


        @Override
        public int getCount() {
            return chunks.size();
        }

        @Override
        public Chunk getItem(int i) {
            return chunks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = TwitchChunkList.this.getLayoutInflater();
                view = inflater.inflate(R.layout.chunk_row, viewGroup, false);
            }

            TextView chunkNumber = (TextView) view.findViewById(R.id.chunkNumber);
            TextView chunkTime = (TextView) view.findViewById(R.id.chunkTime);

            chunkNumber.setText("Chunk #" + (i + 1));

            int secs = chunks.get(i).getLength();

            int min = secs / 60;
            int hour = min / 60;
            min %= 60;
            int sec = secs % 60;

            chunkTime.setText(String.format("Length: %d:%02d:%02d", hour, min, sec));

            return view;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chunk_links, menu);
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
            chunksArray.addAll(result);
            adapter.notifyDataSetChanged();
            process.dismiss();
        }
    }
}
