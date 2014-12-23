package com.tryceo.jack.pastbroadcast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PastVideoLinks extends Activity {

    private BaseAdapter adapter;
    public ProgressDialog process;
    public static List<Video> videoArray;
    private static final String TWITCHAPIURL = "https://api.twitch.tv/kraken/channels/%s/videos?limit=100&offset=%d&broadcasts=true";
    private static final String AZUBUAPIURL = "http://www.azubu.tv/api/channel/%s/video/list?offset=0&limit=100&sortBy=date&sortType=desc";
    public final static String ID = "Video ID";
    public static String channelMessage;
    public static String websiteMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_past_video_links);

        Intent intent = getIntent();
        channelMessage = intent.getStringExtra(Home.CHANNEL_NAME);
        websiteMessage = intent.getStringExtra(Home.STREAMING_WEBSITE);
        process = new ProgressDialog(this);
        process.setTitle("Loading...");
        process.setMessage("Please Wait");

        videoArray = new ArrayList<Video>();
        adapter = new VideoAdapter(videoArray);
        ListView listView = (ListView) findViewById(R.id.videolist);
        listView.setAdapter(adapter);

        if (websiteMessage.equals("Twitch.tv")){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(PastVideoLinks.this, VideoLinks.class);
                    intent.putExtra(PastVideoLinks.ID, videoArray.get(i).getId());
                    startActivity(intent);
                }
            });

            getTwitchVideos  task = new getTwitchVideos();

            task.execute(String.format(TWITCHAPIURL, channelMessage, 0));
        }
        else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(((AzubuVideo)videoArray.get(i)).getVideoUrl()), "video/mp4");
                    startActivity(intent);
                }
            });

            getTwitchVideos  task = new getTwitchVideos();

            task.execute(String.format(AZUBUAPIURL, channelMessage, 0));
        }


        process.show();
    }


    public class VideoAdapter extends BaseAdapter {

        private List<Video> videos;
        public VideoAdapter (List<Video> videos){
            this.videos = videos;
        }


        @Override
        public int getCount() {
            return videos.size();
        }

        @Override
        public Video getItem(int i) {
            return videos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null){
                LayoutInflater inflater = PastVideoLinks.this.getLayoutInflater();
                view = inflater.inflate(R.layout.video_row, viewGroup, false);
            }

            TextView title = (TextView) view.findViewById(R.id.title);
            TextView recordedAt = (TextView) view.findViewById(R.id.recordedAt);
            TextView length = (TextView) view.findViewById(R.id.length);
            ImageView preview = (ImageView) view.findViewById(R.id.previewImage);

            ImageLoader imageLoader = ImageLoader.getInstance();

            imageLoader.displayImage(videos.get(i).getPreview(), preview);

            title.setText(videos.get(i).getTitle());

            recordedAt.setText(videos.get(i).getRecordedAt());

            int secs = videos.get(i).getLength();
            int min = secs / 60;
            int sec = secs % 60;

            length.setText(String.format("Video #%d   Length: %d:%s", i + 1, min, sec));

            return view;
        }
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
                if (websiteMessage.equals("Twitch.tv")) {
                    videos = TwitchVideoJSONParser.getVideos(content);
                }
                else {
                    videos = AzubuVideoJSONParser.getVideos(content);
                }
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return videos;
        }

        @Override
        protected void onPostExecute(List<Video> result) {
            videoArray.addAll(result);

            adapter.notifyDataSetChanged();
            process.dismiss();
        }
    }


}
