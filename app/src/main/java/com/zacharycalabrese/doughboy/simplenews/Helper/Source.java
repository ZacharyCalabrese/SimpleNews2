package com.zacharycalabrese.doughboy.simplenews.Helper;

public class Source {
    public String name;
    public String rssUrl;
    public String category;
    public Boolean subscribed;
    public com.zacharycalabrese.doughboy.simplenews.Model.Source source;

    public Source(String name, String rssUrl, String category, Boolean subscribed) {

        this.name = name;
        this.rssUrl = rssUrl;
        this.category = category;
        this.subscribed = subscribed;
    }

    public Source(String name, String rssUrl, String category, Boolean subscribed,
                  com.zacharycalabrese.doughboy.simplenews.Model.Source source) {

        this.name = name;
        this.rssUrl = rssUrl;
        this.category = category;
        this.subscribed = subscribed;
        this.source = source;
    }

    public Source(com.zacharycalabrese.doughboy.simplenews.Model.Source item) {
        this.name = item.name;
        this.rssUrl = item.rssUrl;
        this.category = item.category;
        this.subscribed = item.subscribed;
    }
}
