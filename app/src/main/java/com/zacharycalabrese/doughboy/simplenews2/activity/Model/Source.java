package com.zacharycalabrese.doughboy.simplenews2.activity.Model;

import com.orm.SugarRecord;

import java.util.ArrayList;
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

    public Source(String name, String rssUrl, String category, Boolean subscribed){
        this.name = name;
        this.rssUrl = rssUrl;
        this.category = category;
        this.subscribed = subscribed;
    }

    public Source(com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source source){
        this.name = source.name;
        this.rssUrl = source.rssUrl;
        this.category = source.category;
        this.subscribed = source.subscribed;
    }

    public List<News> getArticles(){
        return News.find(News.class, "source = ?", Long.toString(this.getId()));
    }
}
