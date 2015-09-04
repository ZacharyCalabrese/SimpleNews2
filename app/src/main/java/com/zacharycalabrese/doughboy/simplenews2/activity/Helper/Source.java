package com.zacharycalabrese.doughboy.simplenews2.activity.Helper;

/**
 * Created by zcalabrese on 9/1/15.
 */
public class Source {
    public String name;
    public String rssUrl;
    public String category;
    public Boolean subscribed;
    public com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source source;

    public Source(String name, String rssUrl, String category, Boolean subscribed) {

        this.name = name;
        this.rssUrl = rssUrl;
        this.category = category;
        this.subscribed = subscribed;
    }

    public Source(String name, String rssUrl, String category, Boolean subscribed,
                  com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source source) {

        this.name = name;
        this.rssUrl = rssUrl;
        this.category = category;
        this.subscribed = subscribed;
        this.source = source;
    }
}
