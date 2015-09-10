package com.zacharycalabrese.doughboy.simplenews2.activity.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebViewClient;

import com.zacharycalabrese.doughboy.simplenews2.R;
import android.webkit.WebView;
import android.view.KeyEvent;
import android.widget.Toast;

public class Web extends ActionBarActivity{

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String urlToLoad = intent.getStringExtra(getResources().getString(R.string.url_to_load));

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
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
                                    String failingUrl)
        {
            Toast.makeText(getApplicationContext(), "Couldn't load page",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
