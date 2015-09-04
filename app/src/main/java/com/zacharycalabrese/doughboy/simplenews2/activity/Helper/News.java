package com.zacharycalabrese.doughboy.simplenews2.activity.Helper;

import com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source;

import org.mcsoxford.rss.RSSItem;

/**
 * Created by zcalabrese on 8/27/15.
 */
public class News {
    public String title;
    public String link;
    public String description;
    public String pubdate;
    public com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source source;

    public News(String title, String link, String description, String pubdate,
                Source source) {

        this.title = title;
        this.link = link;
        this.description = description;
        this.pubdate = pubdate;
        this.source = source;
    }

    public News(RSSItem rssItem, Source source) {
        this.title = rssItem.getTitle();
        this.link = rssItem.getLink().toString();
        this.description = rssItem.getDescription();
        this.pubdate = rssItem.getPubDate().toString();
        this.source = source;
    }

    public News(com.zacharycalabrese.doughboy.simplenews2.activity.Model.News news) {
        this.title = news.title;
        this.link = news.link;
        this.description = news.description;
        this.pubdate = news.pubdate;
        this.source = news.source;
    }
}
