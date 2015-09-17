package com.zacharycalabrese.doughboy.simplenews.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zacharycalabrese.doughboy.simplenews.R;

public class Web extends ActionBarActivity {
    private String urlToLoad;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getResources().getString(R.string.pref_dark_theme), false)) {
            this.setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        urlToLoad = intent.getStringExtra(getResources().getString(R.string.url_to_load));

        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.activity_web_web_view);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(urlToLoad);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    protected void onResume() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                getResources().getString(R.string.pref_dark_theme), false)) {
            this.setTheme(R.style.AppThemeDark);
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_web_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, webView.getTitle());
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, webView.getUrl());
            sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(sharingIntent);
            return true;
        } else if (id == R.id.menu_web_open_in_browser) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(webView.getUrl()));
            this.startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /*
        @Override
        public void onPageFinished(WebView view, String url) {

        }*/

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            Toast.makeText(getApplicationContext(), "Couldn't load page",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
