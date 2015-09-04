package com.zacharycalabrese.doughboy.simplenews2.activity.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zacharycalabrese.doughboy.simplenews2.R;
import com.zacharycalabrese.doughboy.simplenews2.activity.Sync.Weather;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by zcalabrese on 9/3/15.
 */
public class News extends Fragment {
    private static final Hashtable<Integer, String> tabTitlesHashTable = new Hashtable<Integer, String>();
    private static Context context;
    private static final String POSITION_KEY = "position";
    private int tabPosition;
    SwipeRefreshLayout swipeRefreshLayout;

    public static News newFragment(int tabPosition, Context context, String[] tabTitles){
        setTabTitlesHashTable(tabTitles);
        News.context = context;

        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_KEY, tabPosition);
        News news = new News();
        news.setArguments(bundle);
        return news;
    }

    public static void setTabTitlesHashTable(String[] tabTitles) {
        for (int tab = 0; tab < tabTitles.length; tab++)
            tabTitlesHashTable.put(tab, tabTitles[tab]);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabPosition = getArguments().getInt(POSITION_KEY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tabPosition = getArguments().getInt(POSITION_KEY);
        final String category = (tabTitlesHashTable.get(tabPosition));

        com.zacharycalabrese.doughboy.simplenews2.activity.Data.News newsData =
                new com.zacharycalabrese.doughboy.simplenews2.activity.Data.News();

        List<com.zacharycalabrese.doughboy.simplenews2.activity.Helper.News> stories =
                newsData.getStoriesByCategory(category);

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.fragment_news_list_of_articles);
        listView.setAdapter(new com.zacharycalabrese.doughboy.simplenews2.activity.Adapter.News(
                getActivity(), stories));


        return rootView;
    }

    private void updateCurrentFeed(String currentCategory){
        Thread thread = new Thread() {
            @Override
            public void run() {
                com.zacharycalabrese.doughboy.simplenews2.activity.Sync.News news = new com.zacharycalabrese.doughboy.simplenews2.activity.Sync.News(context);
                news.fetchLatestNews();

                while (!news.getUpdatedNews()) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }

                //redrawScreen();
            }
        };
        thread.start();
    }
}
