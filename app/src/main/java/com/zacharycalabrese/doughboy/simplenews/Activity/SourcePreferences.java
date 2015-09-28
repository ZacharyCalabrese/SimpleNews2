package com.zacharycalabrese.doughboy.simplenews.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.zacharycalabrese.doughboy.simplenews.Helper.Source;
import com.zacharycalabrese.doughboy.simplenews.R;

/**
 * Created by zcalabrese on 9/4/15.
 */
public class SourcePreferences extends ActionBarActivity {
    RecyclerView recyclerView;
    com.zacharycalabrese.doughboy.simplenews.Adapter.SourcePreferences sourceAdapter;

    public void onCreate(Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getResources().getString(R.string.pref_dark_theme), false)) {
            this.setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sources_preferences);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = new Bundle();
        args.putBundle("context", savedInstanceState);

        recyclerView = (RecyclerView) findViewById(R.id.activity_source_preferences_recycler_view);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.attachToRecyclerView(recyclerView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSource();
            }
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        sourceAdapter = new com.zacharycalabrese.doughboy.simplenews.Adapter.
                SourcePreferences(this, new
                com.zacharycalabrese.doughboy.simplenews.Data.Source().getAllSources());

        recyclerView.setAdapter(sourceAdapter);

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getResources().getString(R.string.pref_dark_theme), false)) {
            this.setTheme(R.style.AppThemeDark);
        }
        super.onResume();
    }

    private void addNewSource() {
        final com.zacharycalabrese.doughboy.simplenews.Data.Source sourceDataObject =
                new com.zacharycalabrese.doughboy.simplenews.Data.Source();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater layoutInflater = LayoutInflater.from(SourcePreferences.this);
                View promptView = layoutInflater.inflate(R.layout.dialog_input_add_source, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SourcePreferences.this);
                alertDialogBuilder.setView(promptView);

                final EditText source = (EditText)
                        promptView.findViewById(R.id.dialog_input_add_source_edit_text);

                final EditText rss = (EditText)
                        promptView.findViewById(R.id.dialog_input_add_source_rss_edit_text);

                final Spinner spinner = (Spinner) promptView.findViewById(R.id.dialog_input_add_category_spinner);

                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String category = spinner.getSelectedItem().toString();

                                Boolean successfullyAdded =
                                        sourceDataObject.addNewSource(source.getText().toString(),
                                                rss.getText().toString(), category);

                                if(!successfullyAdded){
                                    Toast.makeText(getApplicationContext(), "Could not add new feed"
                                            , Toast.LENGTH_SHORT).show();
                                }else{
                                    sourceAdapter.notifyDataSetChanged();
                                }
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
        });
    }
}
