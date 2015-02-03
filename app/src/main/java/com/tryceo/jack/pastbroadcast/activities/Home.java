package com.tryceo.jack.pastbroadcast.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tryceo.jack.pastbroadcast.R;
import com.tryceo.jack.pastbroadcast.activities.TwitchVideoList;

/**
 * Created by Jack on 10/10/2014.
 *
 * Homescreen for the app.
 *
 * Passes intent to TwitchVideoList
 */

public class Home extends Activity {

    public final static String CHANNEL_NAME = "Channel Name";
    public final static String STREAMING_WEBSITE = "Streaming Website";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();

//        setContentView(R.layout.activity_home);
//        Spinner spinner = (Spinner) findViewById(R.id.website_spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
//                getResources().getStringArray(R.array.website_array));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        spinner.setAdapter(adapter);//set the adapter

        ImageLoader.getInstance().init(config);
        //Starts the ImageLoader

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);


//        if (actionView!= null){
//            final EditText searchMessage= (EditText) actionView.findViewById(R.id.edit_search_message);
//            if (searchMessage != null){
//                searchMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                        if (i == EditorInfo.IME_ACTION_SEARCH){
//                            searchChannel(searchMessage.getText().toString());
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//            }
//
//        }
//        Spinner spinner = (Spinner) findViewById(R.id.website_spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
//        getResources().getStringArray(R.array.website_array));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);//set the adapter

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

    public void searchChannel(String channel) {
        Intent intent = new Intent(this, TwitchVideoList.class);
        intent.putExtra(CHANNEL_NAME, channel);
        intent.putExtra(STREAMING_WEBSITE, "Twitch.tv");
        startActivity(intent);
    }

    public void searchNumber(View view) {
        Intent intent = new Intent(this, TwitchVideoList.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        Spinner spinner = (Spinner) findViewById(R.id.website_spinner);
        String message = editText.getText().toString();
        intent.putExtra(CHANNEL_NAME, message);
        //adds the choice between Twitch and Azubu
        startActivity(intent);
    }

}
