package com.zacharycalabrese.doughboy.simplenews2.activity.Activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zacharycalabrese.doughboy.simplenews2.R;
import com.zacharycalabrese.doughboy.simplenews2.activity.Sync.Weather;

import java.util.Arrays;


public class Main extends ActionBarActivity {
    com.zacharycalabrese.doughboy.simplenews2.activity.Adapter.Main mainAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ifFirstTimeRunning();
        initializeSourcesDatabase();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary_color, R.color.accent_color);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mainAdapter = new com.zacharycalabrese.doughboy.simplenews2.activity.Adapter.Main(this);
        recyclerView.setAdapter(mainAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                updateWeather();
            }
        });
    }

    private void initializeSourcesDatabase(){
        String [] sourcesNamesArray = this.getResources().getStringArray(R.array.base_sources_names);
        String [] sourcesUrlArray = this.getResources().getStringArray(R.array.base_sources_rss_url);

        for(String source : sourcesNamesArray){

            String[] parts;
            parts = source.split("\\:");

            String category = parts[0];
            String name = parts[1];
            String rssUrl = sourcesUrlArray[Arrays.asList(sourcesNamesArray).indexOf(source)];

            Log.v("URL", rssUrl);
        }
    }

    public void updateWeather(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                Weather weather = new Weather();
                weather.updateWeather();
                while (!weather.getUpdatedWeather()){
                    try {
                        sleep(1000);
                    }catch (InterruptedException e){

                    }
                };

                redrawScreen();
            }
        };
        thread.start();
    }

    private void redrawScreen(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
