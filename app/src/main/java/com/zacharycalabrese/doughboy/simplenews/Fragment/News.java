package com.zacharycalabrese.doughboy.simplenews.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zacharycalabrese.doughboy.simplenews.R;

import java.util.ArrayList;
import java.util.Collections;
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

    public static void setTabTitlesHashTable(Context context){
        com.zacharycalabrese.doughboy.simplenews.Data.Source news =
                new com.zacharycalabrese.doughboy.simplenews.Data.Source();

        String[] tabTitles = news.getCategories();

        String[] standardCategories =
            context.getResources().getStringArray(R.array.categoryDefaultValues);

        List<String> cleanTitleList = new ArrayList<>();
        for(String title : tabTitles){
            for(String standardCat : standardCategories){
                if(title.equals(standardCat.substring(3)))
                    cleanTitleList.add(standardCat);
            }
        }

        Collections.sort(cleanTitleList);
        for(String title : cleanTitleList) {
            cleanTitleList.set(cleanTitleList.indexOf(title), title.substring(3));
        }
        tabTitles = cleanTitleList.toArray(new String[cleanTitleList.size()]);
        setTabTitlesHashTable(tabTitles);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabPosition = getArguments().getInt(POSITION_KEY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tabPosition = getArguments().getInt(POSITION_KEY);
        Log.v("Category createview:", Integer.toString(tabPosition));
        String category = (tabTitlesHashTable.get(tabPosition));

        if(tabTitlesHashTable == null || category == null){
            Log.v("Category stting tables:", "please work");
            setTabTitlesHashTable(getActivity());
            category = (tabTitlesHashTable.get(tabPosition));
        }

        Log.v("Category 2:", category);
        com.zacharycalabrese.doughboy.simplenews.Data.News newsData =
                new com.zacharycalabrese.doughboy.simplenews.Data.News();

        List<com.zacharycalabrese.doughboy.simplenews.Helper.News> stories =
                newsData.getStoriesByCategory(category);

        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.fragment_news_list_of_articles);
        listView.setAdapter(new com.zacharycalabrese.doughboy.simplenews.Adapter.News(
                getActivity(), stories));


        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.v("Category 1:", "PAUSE");
        tabPosition = getArguments().getInt(POSITION_KEY);
        Log.v("Category 1:", Integer.toString(tabPosition));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION_KEY, tabPosition);
        Log.v("Category sis:", Integer.toString(tabPosition));
    }

    private void updateCurrentFeed(String currentCategory){
        Thread thread = new Thread() {
            @Override
            public void run() {
                com.zacharycalabrese.doughboy.simplenews.Sync.News news = new com.zacharycalabrese.doughboy.simplenews.Sync.News(context);
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
