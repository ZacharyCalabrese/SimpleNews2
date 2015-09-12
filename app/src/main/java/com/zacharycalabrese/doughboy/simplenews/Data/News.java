package com.zacharycalabrese.doughboy.simplenews.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by zcalabrese on 9/2/15.
 */
public class News {
    private Context context;

    public News(){

    }

    public News(Context context) {
        this.context = context;
    }

    public List<com.zacharycalabrese.doughboy.simplenews.Helper.News>
        getStoriesByCategory(String category){

        List<com.zacharycalabrese.doughboy.simplenews.Model.News> results =
                com.zacharycalabrese.doughboy.simplenews.Model.News.findWithQuery(
                        com.zacharycalabrese.doughboy.simplenews.Model.News.class,
                        "Select * from News, Source where News.source = Source.id and subscribed = 1 and category = ?" +
                                "group by title order by date DESC limit 100", category);

        List<com.zacharycalabrese.doughboy.simplenews.Helper.News> returningList =
                new ArrayList<>();

        for (com.zacharycalabrese.doughboy.simplenews.Model.News item : results) {
            com.zacharycalabrese.doughboy.simplenews.Helper.News newsHelper =
                    new com.zacharycalabrese.doughboy.simplenews.Helper.News(item);


            newsHelper = cleanData(newsHelper);
            returningList.add(newsHelper);
        }

        return returningList;
    }

    public List<com.zacharycalabrese.doughboy.simplenews.Helper.News> getLatestHeadlines() {

        List<com.zacharycalabrese.doughboy.simplenews.Model.News> results =
                com.zacharycalabrese.doughboy.simplenews.Model.News.findWithQuery(
                        com.zacharycalabrese.doughboy.simplenews.Model.News.class,
                        "Select * from News, Source where News.source = Source.id and subscribed = 1 " +
                                "group by title order by date DESC limit 10");

        List<com.zacharycalabrese.doughboy.simplenews.Helper.News> returningList =
                new ArrayList<>();

        for (com.zacharycalabrese.doughboy.simplenews.Model.News item : results) {
            com.zacharycalabrese.doughboy.simplenews.Helper.News newsHelper =
                    new com.zacharycalabrese.doughboy.simplenews.Helper.News(item);


            newsHelper = cleanData(newsHelper);
            returningList.add(newsHelper);
        }

        return returningList;
    }

    private com.zacharycalabrese.doughboy.simplenews.Helper.News cleanData(
            com.zacharycalabrese.doughboy.simplenews.Helper.News input) {


        input.title = input.title.replace("&apos;", "'").replace("&quot;", "\"").
                replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");

        input.description = input.description.replace("&apos;", "'").replace("&quot;", "\"").
                replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");

        return input;
    }

    /**
     * INPUT INTO DATABASE BELOW
     */

    public void bulkLoadToDatabase(
            List<com.zacharycalabrese.doughboy.simplenews.Helper.News> newsHelperList) {

        if(newsHelperList.size() > 0) {
            List<com.zacharycalabrese.doughboy.simplenews.Model.News> newsModelList =
                    new ArrayList<>();

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Long latestTimestamp = sp.getLong("latest_news_timestamp", 0);
            ArrayList<Long> timestamps = new ArrayList<>();

            for (com.zacharycalabrese.doughboy.simplenews.Helper.News item : newsHelperList) {
                String date = item.pubdate;
                java.util.Date TIMESTAMP;
                Long time;

                java.util.Date utilDate = null;
                try {
                    DateFormat formatter = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                    utilDate = formatter.parse(date);

                    TIMESTAMP = utilDate;
                    time = TIMESTAMP.getTime();
                    timestamps.add(time);
                } catch (ParseException e) {
                    Log.e("Error", e.toString());
                    continue;
                }

                if (time > latestTimestamp) {
                    com.zacharycalabrese.doughboy.simplenews.Model.News newItem =
                            new com.zacharycalabrese.doughboy.simplenews.Model.News(item.title,
                                    item.link, item.description, item.pubdate, item.source);

                    newsModelList.add(newItem);
                }
            }

            Collections.sort(timestamps);

            SharedPreferences.Editor editor = sp.edit();
            editor.putLong("latest_news_timestamp", timestamps.get(timestamps.size() - 1));
            editor.apply();

            com.zacharycalabrese.doughboy.simplenews.Model.News.saveInTx(newsModelList);
        }
    }

    private void writeToDatabase(final com.zacharycalabrese.doughboy.simplenews.Helper.News
                                         article) {

        com.zacharycalabrese.doughboy.simplenews.Model.News news =
                new com.zacharycalabrese.doughboy.simplenews.Model.News(article.title,
                        article.link, article.description, article.pubdate, article.source);
        news.save();
    }
}
