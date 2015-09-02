package com.zacharycalabrese.doughboy.simplenews2.activity.Model;

import android.util.Log;

import com.orm.SugarRecord;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    public java.util.Date TIMESTAMP;

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

        String date = pubdate;

        java.util.Date utilDate = null;

        try {
            DateFormat formatter = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
            utilDate = formatter.parse(date);
        } catch (ParseException e) {
            Log.v("Error", e.toString());
        }

        TIMESTAMP = utilDate;
    }
}
