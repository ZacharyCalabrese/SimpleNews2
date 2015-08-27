package com.zacharycalabrese.doughboy.simplenews2.activity.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zacharycalabrese.doughboy.simplenews2.R;
import com.zacharycalabrese.doughboy.simplenews2.activity.Sync.Weather;


public class Main extends ActionBarActivity {
    com.zacharycalabrese.doughboy.simplenews2.activity.Adapter.Main mainAdapter;
    RecyclerView recyclerView;
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

            updateWeather();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawScreen();
        thread.start();
        //updateWeather();
    }

    private void drawScreen(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mainAdapter = new com.zacharycalabrese.doughboy.simplenews2.activity.Adapter.Main();
        recyclerView.setAdapter(mainAdapter);
    }

    public void updateWeather(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainAdapter.notifyDataSetChanged();
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
