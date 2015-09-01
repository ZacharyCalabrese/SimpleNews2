package com.zacharycalabrese.doughboy.simplenews2.activity.Model;

import com.orm.SugarRecord;

/**
 * Created by zcalabrese on 8/27/15.
 */
public class News extends SugarRecord<News> {
    public String title;
    public String link;
    public String description;
    public String pubdate;
    public String timestamp;
    public Source source;

    public News(){

    }

    public News(String title, String link, String description, String pubdate,
                Source source){
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubdate = pubdate;
        this.source = source;
        this.timestamp = ((Long) System.currentTimeMillis()).toString();
        this.source = source;
    }
}
