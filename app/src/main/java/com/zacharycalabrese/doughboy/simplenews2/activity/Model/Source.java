package com.zacharycalabrese.doughboy.simplenews2.activity.Model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by zcalabrese on 8/27/15.
 */
public class Source extends SugarRecord<Source>{
    public String name;
    public String rssUrl;
    public String category;
    public Boolean subscribed;

    public Source(){

    }

    public Source(String name, String baseUrl, String rssUrl, String category, Boolean subscribed){
        this.name = name;
        this.rssUrl = rssUrl;
        this.category = category;
        this.subscribed = subscribed;
    }

    public List<News> getArticles(){
        return News.find(News.class, "source = ?", Long.toString(this.getId()));
    }
}