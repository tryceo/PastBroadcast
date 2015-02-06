package com.tryceo.jack.pastbroadcast.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.tryceo.jack.pastbroadcast.R;
import com.tryceo.jack.pastbroadcast.helpers.AzubuChannelParser;
import com.tryceo.jack.pastbroadcast.helpers.TwitchChannelParser;
import com.tryceo.jack.pastbroadcast.objects.Channel;
import com.tryceo.jack.pastbroadcast.objects.StreamingPlatform;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 10/10/2014.
 *
 * Homescreen for the app.
 *
 * Passes intent to TwitchVideoList
 */

public class Home extends Activity {

    private BaseAdapter adapter;
    private List<Channel> channels;
    private ProgressDialog progressDialog;
    public final static String CHANNEL_NAME = "Channel Name";
    public final static String STREAMING_WEBSITE = "Streaming Website";
    public final static String TWITCH_CHANNEL_API = "https://api.twitch.tv/kraken/channels/%s";
    public final static String AZUBU_CHANNEL_API = "http://api.azubu.tv/public/channel/%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .build();

        channels = new ArrayList<Channel>();
        SharedPreferences channelData = getPreferences(0);
        Map<String, ?> dataMap = channelData.getAll();
        for (Map.Entry<String, ?> entry: dataMap.entrySet()){
            boolean twitch = (Boolean) entry.getValue();
            if (twitch){
                channels.add(new Channel(entry.getKey(), StreamingPlatform.TWITCH));
            }else{
                channels.add(new Channel(entry.getKey(), StreamingPlatform.AZUBU));
            }
        }

        Collections.sort(channels);
//        channels.add(new Channel("nl_kripp", StreamingPlatform.TWITCH));
//        channels.add(new Channel("riotgames", StreamingPlatform.TWITCH));
//        channels.add(new Channel("summit1G", StreamingPlatform.TWITCH));
//        channels.add(new Channel("TrumpSC", StreamingPlatform.TWITCH));
//        channels.add(new Channel("tsm_theoddone", StreamingPlatform.TWITCH));
//        channels.add(new Channel("sodapoppin", StreamingPlatform.TWITCH));
//        channels.add(new Channel("brttrexpeita", StreamingPlatform.TWITCH));
//        channels.add(new Channel("iijeriichoii", StreamingPlatform.TWITCH));
//        channels.add(new Channel("Faker", StreamingPlatform.AZUBU));

        setContentView(R.layout.activity_home);
        final GridView channelGrid = (GridView) findViewById(R.id.channelGrid);
        adapter = new ChannelAdapter(channels);
        channelGrid.setAdapter(adapter);


        channelGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchChannel(channels.get(i));
            }
        });

        GetChannelLogos task = new GetChannelLogos();

        task.execute();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        MenuItem actionRefresh = menu.findItem(R.id.action_refresh);
        actionRefresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                recreate();
                return true;
            }
        });

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Channel Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                s=s.replaceAll("^[ \\t]+|[ \\t]+$", "");
                Spinner spinner = (Spinner) findViewById(R.id.website_spinner);
                StreamingPlatform platform;
                if (spinner.getSelectedItem().toString().equals("Twitch")){
                    platform = StreamingPlatform.TWITCH;
                }else {
                    platform = StreamingPlatform.AZUBU;
                }
                searchChannel(new Channel(s, platform));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    public class ChannelAdapter extends BaseAdapter {

        private DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)

                .build();

        private List<Channel> channels;

        public ChannelAdapter(List<Channel> channels) {
            this.channels = channels;
        }

        @Override
        public int getCount() {
            return channels.size();
        }

        @Override
        public Channel getItem(int i) {
            return channels.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                LayoutInflater inflater = Home.this.getLayoutInflater();
                view = inflater.inflate(R.layout.channel_grid_item, viewGroup, false);
            }

            //Get all the views
            TextView channelName = (TextView) view.findViewById(R.id.channelName);
            ImageView channelLogo = (ImageView) view.findViewById(R.id.channelImage);

            String logo = channels.get(i).getChannelLogo();
            if (logo != null) {
                //Set channelLogo
                ImageLoader imageLoader = ImageLoader.getInstance();
                ImageAware imageAware = new ImageViewAware(channelLogo, false);
                imageLoader.displayImage(channels.get(i).getChannelLogo(), imageAware, options);


                //Set channelName
                channelName.setText(channels.get(i).getChannelName());
            }

            return view;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private class GetChannelLogos extends AsyncTask<Channel, Void, Void> {

        @Override
        protected Void doInBackground(Channel ... channel) {

            for (int i = 0; i < channels.size(); i++) {

                Channel c = channels.get(i);
                StreamingPlatform platform = c.getChannelPlatform();

                switch (platform) {
                    case TWITCH:

                        try {
                            URL url = new URL(String.format(TWITCH_CHANNEL_API, c.getChannelName()));
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream content = new BufferedInputStream(urlConnection.getInputStream());
                            c.setChannelLogo(TwitchChannelParser.getChannelLogo(content));
                            urlConnection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case AZUBU:
                        try {
                            URL url = new URL(String.format(AZUBU_CHANNEL_API, c.getChannelName()));
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream content = new BufferedInputStream(urlConnection.getInputStream());
                            c.setChannelLogo(AzubuChannelParser.getChannelLogo(content));
                            urlConnection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    public void searchChannel(Channel channel) {
        Intent intent;

        SharedPreferences channelData = getPreferences(0);
        SharedPreferences.Editor editor = channelData.edit();

        if (channel.getChannelPlatform() == StreamingPlatform.TWITCH){
            intent = new Intent(this, TwitchVideoList.class);
            editor.putBoolean(channel.getChannelName(), true);
        }
        else {
            intent = new Intent(this, AzubuVideoList.class);
            editor.putBoolean(channel.getChannelName(), false);
        }

        intent.putExtra(CHANNEL_NAME, channel.getChannelName());
        editor.apply();
        startActivity(intent);
    }


//    public void addNumber(View view) {
//        Intent intent = new Intent(this, TwitchVideoList.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        Spinner spinner = (Spinner) findViewById(R.id.website_spinner);
//        String message = editText.getText().toString();
//        intent.putExtra(CHANNEL_NAME, message);
//        //adds the choice between Twitch and Azubu
//        startActivity(intent);
//    }

}
