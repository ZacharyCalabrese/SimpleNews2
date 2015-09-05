package com.zacharycalabrese.doughboy.simplenews2.activity.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zacharycalabrese.doughboy.simplenews2.R;

/**
 * Created by zcalabrese on 8/31/15.
 */
public class Weather extends ActionBarActivity {
    RecyclerView recyclerView;
    com.zacharycalabrese.doughboy.simplenews2.activity.Adapter.Weather weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.activity_weather_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        com.zacharycalabrese.doughboy.simplenews2.activity.Data.Weather weatherData =
                new com.zacharycalabrese.doughboy.simplenews2.activity.Data.Weather(this);


        weatherAdapter = new com.zacharycalabrese.doughboy.simplenews2.activity.Adapter.Weather(
                this, weatherData.getWeekData());
        recyclerView.setAdapter(weatherAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
