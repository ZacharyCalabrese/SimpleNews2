package com.zacharycalabrese.doughboy.simplenews2.activity.Activity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.zacharycalabrese.doughboy.simplenews2.R;
import com.zacharycalabrese.doughboy.simplenews2.activity.Data.Source;

import java.util.List;

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
                .replace(R.id.content_wrapper, new com.zacharycalabrese.doughboy.simplenews2.activity.Fragment.Sources())
                .commit();

        //toolbar.setTitleTextColor(getResources().getColor(R.color.white));
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
