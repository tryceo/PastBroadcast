package com.tryceo.jack.pastbroadcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class Home extends Activity {

    public final static String CHANNEL_NAME = "Channel Name";
    public final static String STREAMING_WEBSITE = "Streaming Website";
    public static String website = "Twitch.tv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();

        setContentView(R.layout.activity_home);
        Spinner spinner = (Spinner) findViewById(R.id.website_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.website_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        ImageLoader.getInstance().init(config);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

    public void searchNumber(View view) {
        Intent intent = new Intent(this, VideoLinks.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        Spinner spinner = (Spinner) findViewById(R.id.website_spinner);
        String message = editText.getText().toString();
        intent.putExtra(CHANNEL_NAME, message);
        intent.putExtra(STREAMING_WEBSITE, spinner.getSelectedItem().toString());
        startActivity(intent);
    }

}
