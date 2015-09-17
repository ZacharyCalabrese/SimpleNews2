package com.zacharycalabrese.doughboy.simplenews.Sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.zacharycalabrese.doughboy.simplenews.Data.Source;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcalabrese on 9/1/15.
 */
public class News {
    private static final String LOG_TAG = News.class.getName();
    private Context context;
    private Boolean updatedNews;
    private Boolean isValidRss;

    public News(Context context) {
        this.context = context;
        updatedNews = false;
    }


    public void fetchLatestNews() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Long lastUpdated = sp.getLong("last_updated_sources", 0);
        if( (System.currentTimeMillis() - 1200000) > lastUpdated) {
            Source source = new Source();
            ArrayList<com.zacharycalabrese.doughboy.simplenews.Helper.Source> results;
            results = source.getSubscriptions();

            new FetchNewsTask().execute(results);
        }else{
            updatedNews = true;
        }
    }

    public boolean getUpdatedNews() {
        return updatedNews;
    }

    private void finishedProcessing() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("last_updated_sources", System.currentTimeMillis());
        editor.commit();
    }

    private void processResults(List<com.zacharycalabrese.doughboy.simplenews.Helper.News>
                                        results) {

        com.zacharycalabrese.doughboy.simplenews.Data.News newsData =
                new com.zacharycalabrese.doughboy.simplenews.Data.News(context);
        newsData.bulkLoadToDatabase(results);

        if(results.size() > 0) {
            finishedProcessing();
        }

        updatedNews = true;
    }


    public class FetchNewsTask extends AsyncTask<
            List<com.zacharycalabrese.doughboy.simplenews.Helper.Source>, Void,
            List<com.zacharycalabrese.doughboy.simplenews.Helper.News>> {

        public FetchNewsTask() {
            super();
        }

        @Override
        protected List<com.zacharycalabrese.doughboy.simplenews.Helper.News> doInBackground(
                List<com.zacharycalabrese.doughboy.simplenews.Helper.Source>... params) {

            List<com.zacharycalabrese.doughboy.simplenews.Helper.Source> sources;

            sources = params[0];

            List<com.zacharycalabrese.doughboy.simplenews.Helper.News> results = new ArrayList<>();

            for(com.zacharycalabrese.doughboy.simplenews.Helper.Source source : sources){
                RSSReader rssReader = new RSSReader();
                RSSFeed rssFeed;

                try {
                    rssFeed = rssReader.load(source.rssUrl);
                    for (RSSItem rssItem : rssFeed.getItems()) {
                        com.zacharycalabrese.doughboy.simplenews.Helper.News item =
                                new com.zacharycalabrese.doughboy.simplenews.Helper.News(
                                        rssItem, source.source);

                        results.add(item);
                    }
                } catch (Exception e) {
                    Log.e("RSS Exception: ", e.toString());
                }
            }


            return results;
        }

        @Override
        protected void onPostExecute(List<com.zacharycalabrese.doughboy.simplenews.Helper.News>
                                             newses) {

            try {
                processResults(newses);
            }catch (NullPointerException e){
                updatedNews = true;
                Log.e(LOG_TAG, "No results; Possibly network exception");
            }

        }
    }
}
