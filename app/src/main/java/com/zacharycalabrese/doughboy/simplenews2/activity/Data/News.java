package com.zacharycalabrese.doughboy.simplenews2.activity.Data;

import com.zacharycalabrese.doughboy.simplenews2.activity.Model.*;

/**
 * Created by zcalabrese on 9/2/15.
 */
public class News {

    public News(com.zacharycalabrese.doughboy.simplenews2.activity.Helper.News news){
        writeToDatabase(news);
    }

    private void writeToDatabase(com.zacharycalabrese.doughboy.simplenews2.activity.Helper.News
                                         article){

        com.zacharycalabrese.doughboy.simplenews2.activity.Model.News news =
                new com.zacharycalabrese.doughboy.simplenews2.activity.Model.News(article.title,
                        article.link, article.description, article.pubdate, article.source);
        news.save();
    }
}
