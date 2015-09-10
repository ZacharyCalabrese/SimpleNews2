package com.zacharycalabrese.doughboy.simplenews.activity.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.zacharycalabrese.doughboy.simplenews.R;

/**
 * Created by zcalabrese on 9/4/15.
 */
public class SourcePreferences extends ActionBarActivity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sources_preferences);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = new Bundle();
        args.putBundle("context", savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_wrapper, new com.zacharycalabrese.doughboy.simplenews.activity.Fragment.Sources())
                .commit();

        //toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_source_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_source:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
