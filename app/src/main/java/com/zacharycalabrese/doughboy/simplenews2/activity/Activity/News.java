package com.zacharycalabrese.doughboy.simplenews2.activity.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.zacharycalabrese.doughboy.simplenews2.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class News extends ActionBarActivity {
    private String[] tabTitles;
    ViewPager pager;
    Toolbar toolbar;
    PagerSlidingTabStrip tabs;
    private MyPagerAdapter adapter;
    private SystemBarTintManager mTintManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTabTitles();

        setContentView(R.layout.activity_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTabTitles(){
        com.zacharycalabrese.doughboy.simplenews2.activity.Data.Source news =
                new com.zacharycalabrese.doughboy.simplenews2.activity.Data.Source();

        tabTitles = news.getCategories();

        String[] standardCategories =
                this.getResources().getStringArray(R.array.categoryDefaultValues);

        List<String> cleanTitleList = new ArrayList<>();
        for(String title : tabTitles){
            for(String standardCat : standardCategories){
                if(title.equals(standardCat.substring(2)))
                    cleanTitleList.add(standardCat);
            }
        }

        Collections.sort(cleanTitleList);
        for(String title : cleanTitleList) {
            cleanTitleList.set(cleanTitleList.indexOf(title), title.substring(2));
        }

        tabTitles = cleanTitleList.toArray(new String[cleanTitleList.size()]);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return new com.zacharycalabrese.doughboy.simplenews2.activity.Fragment.News().
                    newFragment(position, getBaseContext(), tabTitles);
        }
    }
}
