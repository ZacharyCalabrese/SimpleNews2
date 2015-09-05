package com.zacharycalabrese.doughboy.simplenews2.activity.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.zacharycalabrese.doughboy.simplenews2.R;
import com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source;
import com.zacharycalabrese.doughboy.simplenews2.activity.Sync.News;
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWeatherAndNews();
            }
        });
        updateWeatherAndNews();
    }

    private void updateWeatherAndNews() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                updateWeather();

                News news = new News(getApplicationContext());
                news.fetchLatestNews();

                while (!news.getUpdatedNews()) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }

                redrawScreen();
            }
        };
        thread.start();
    }

    private void updateWeather(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String zipCode = sharedPreferences.getString("zip_code", "");

        try {
            if (zipCode.length() != 5) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showInputDialog();

                    }
                });
            }else {
                zipCode = sharedPreferences.getString("zip_code", "");
                Weather weather = new Weather(zipCode);
                weather.updateWeather();
            }
        }catch (NullPointerException e){
            Log.v("Nulpointer", "exception");
        }

    }

    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(Main.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input_zip, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.dialog_input_zip_edit_text);
        // Setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor zipCodeEdit = sharedPreferences.edit();
                        zipCodeEdit.putString("zip_code", editText.getText().toString());
                        zipCodeEdit.commit();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // Create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void ifFirstTimeRunning() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this);
        if (SP.getBoolean("first_time_running", true)) {
            initializeSourcesDatabase();
        }
    }

    private void initializeSourcesDatabase() {
        String[] sourcesNamesArray = this.getResources().getStringArray(R.array.base_sources_names);
        String[] sourcesUrlArray = this.getResources().getStringArray(R.array.base_sources_rss_url);

        for (String source : sourcesNamesArray) {

            String[] parts;
            parts = source.split("\\: ");

            String category = parts[0];
            String name = parts[1];
            String rssUrl = sourcesUrlArray[Arrays.asList(sourcesNamesArray).indexOf(source)];

            Source source1 = new Source(name, rssUrl, category, true);
            com.zacharycalabrese.doughboy.simplenews2.activity.Data.Source dataSource =
                    new com.zacharycalabrese.doughboy.simplenews2.activity.Data.Source();

            dataSource.addSource(source1);
        }

    }

    private void redrawScreen() {
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
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}