package com.zacharycalabrese.doughboy.simplenews.Activity;

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
import android.widget.Spinner;

import com.melnykov.fab.FloatingActionButton;
import com.zacharycalabrese.doughboy.simplenews.Helper.Source;
import com.zacharycalabrese.doughboy.simplenews.R;
import com.zacharycalabrese.doughboy.simplenews.Sync.News;
import com.zacharycalabrese.doughboy.simplenews.Sync.Weather;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


public class Main extends ActionBarActivity {
    private static final String LOG_TAG = Main.class.getName();
    com.zacharycalabrese.doughboy.simplenews.Adapter.Main mainAdapter;
    com.zacharycalabrese.doughboy.simplenews.Data.News news;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean weatherInputDialogShowing;
    private List<com.zacharycalabrese.doughboy.simplenews.Helper.News> newsHeadlines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getResources().getString(R.string.pref_dark_theme), false)) {
            this.setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);

        weatherInputDialogShowing = false;

        ifFirstTimeRunning();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        com.zacharycalabrese.doughboy.simplenews.Activity.News.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary_color, R.color.accent_color);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        news = new com.zacharycalabrese.doughboy.simplenews.Data.News(this);
        newsHeadlines = news.getLatestHeadlines();
        mainAdapter = new com.zacharycalabrese.doughboy.simplenews.Adapter.Main(this, newsHeadlines);
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
                        Log.e(LOG_TAG, "Error sleeping update weather and news");
                    }
                }

                redrawScreen();
            }
        };
        thread.start();
    }

    private void updateWeather() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String zipCode = sharedPreferences.getString(
                getResources().getString(R.string.shared_preference_zip_code), "");

        final String countryCode = sharedPreferences.getString(
                getResources().getString(R.string.shared_preference_country_code), "USA");

        try {
            if (zipCode.length() < 3) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showInputDialog();

                    }
                });
            } else {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        Weather weather = new Weather(zipCode, countryCode);
                        weather.updateWeather();

                        while (!weather.getUpdatedWeather()) {
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
        } catch (NullPointerException e) {
            Log.v(LOG_TAG, "Null pointer exception on update weather");
        }

    }

    protected void showInputDialog() {
        final Hashtable<String, String> countryCodeHashtable = new Hashtable<String, String>();
        String[] countryArray = getResources().getStringArray(R.array.countries);
        String[] countryCodeArray = getResources().getStringArray(R.array.countries_values);
        for (int i = 0; i < countryArray.length; i++) {
            countryCodeHashtable.put(countryArray[i], countryCodeArray[i]);
        }


        if (!weatherInputDialogShowing) {
            weatherInputDialogShowing = true;
            LayoutInflater layoutInflater = LayoutInflater.from(Main.this);
            View promptView = layoutInflater.inflate(R.layout.dialog_input_zip, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main.this);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(R.id.dialog_input_zip_edit_text);
            final Spinner spinner = (Spinner) promptView.findViewById(R.id.dialog_input_country_spinner);

            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor zipCodeEdit = sharedPreferences.edit();

                            zipCodeEdit.putString(getResources().getString(
                                            R.string.shared_preference_zip_code),
                                    editText.getText().toString());

                            zipCodeEdit.putString(getResources().getString(
                                            R.string.shared_preference_country_code),
                                    countryCodeHashtable.get(spinner.getSelectedItem().toString()));

                            zipCodeEdit.commit();
                            updateWeather();
                            weatherInputDialogShowing = false;
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    weatherInputDialogShowing = false;
                                }
                            });

            // Create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    private void ifFirstTimeRunning() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this);
        if (SP.getBoolean(getResources().getString(
                R.string.shared_preference_first_time_running), true)) {

            initializeSourcesDatabase();
            SharedPreferences.Editor editor = SP.edit();

            // Edit the saved preferences
            editor.putBoolean(getResources().getString(
                    R.string.shared_preference_first_time_running), false);

            editor.commit();
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
            com.zacharycalabrese.doughboy.simplenews.Data.Source dataSource =
                    new com.zacharycalabrese.doughboy.simplenews.Data.Source();

            dataSource.addSource(source1);
        }

    }

    private void redrawScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsHeadlines = news.getLatestHeadlines();
                mainAdapter.updateList(newsHeadlines);
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

    @Override
    public void onResume() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getResources().getString(R.string.pref_dark_theme), false)) {
            getApplicationContext().setTheme(R.style.AppThemeDark);
        }

        super.onResume();
        updateWeather();

        mainAdapter.notifyDataSetChanged();
    }
}