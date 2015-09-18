package com.zacharycalabrese.doughboy.simplenews.Helper;

import android.util.Log;

import com.zacharycalabrese.doughboy.simplenews.Model.Source;

import org.mcsoxford.rss.RSSItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class News {
    public String title;
    public String link;
    public String description;
    public String pubdate;
    public com.zacharycalabrese.doughboy.simplenews.Model.Source source;
    public Long timestamp;


    public News(String title, String link, String description, String pubdate,
                Source source) {

        this.title = title;
        this.link = link;
        this.description = description;
        this.pubdate = pubdate;
        this.source = source;

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("EE MMM dd HH:mm:ss zzzz yyyy");
            timestamp = sdf1.parse(this.pubdate).getTime();
        } catch (ParseException e) {
            Log.v("Error here", e.toString());
        }
    }

    public News(RSSItem rssItem, Source source) {
        this.title = rssItem.getTitle();
        this.link = rssItem.getLink().toString();
        this.description = rssItem.getDescription();
        this.pubdate = rssItem.getPubDate().toString();
        this.source = source;

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("EE MMM dd HH:mm:ss zzzz yyyy");
            timestamp = sdf1.parse(this.pubdate).getTime();
        } catch (ParseException e) {
            Log.v("Error here", e.toString());
        }
    }

    public News(com.zacharycalabrese.doughboy.simplenews.Model.News news) {
        this.title = news.title;
        this.link = news.link;
        this.description = news.description;
        this.pubdate = news.pubdate;
        this.source = news.source;
        this.timestamp = news.date;
    }
}
