package com.zacharycalabrese.doughboy.simplenews2.activity.Model;

import android.util.Log;

import com.orm.SugarRecord;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by zcalabrese on 8/27/15.
 */
public class News extends SugarRecord<News> {
    public String title;
    public String link;
    public String description;
    public String pubdate;
    public Source source;
    public Long date;

    public News(){

    }

    public News(String title, String link, String description, String pubdate,
                Source source) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubdate = pubdate;
        this.source = source;

        try {
            Log.v("Pubdate : ", pubdate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("EE MMM dd HH:mm:ss zzzz yyyy");
            date = sdf1.parse(pubdate).getTime();
        } catch (ParseException e) {
            Log.v("Error here", e.toString());
        }
    }
}
