package com.zacharycalabrese.doughboy.simplenews2.activity.Activity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import com.zacharycalabrese.doughboy.simplenews2.activity.Data.Source;

import java.util.List;

/**
 * Created by zcalabrese on 9/4/15.
 */
public class SourcePreferences extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);

        Source sourceManager = new Source();
        String[] categories = sourceManager.getCategories();
        for (String cat : categories){
            PreferenceCategory category = new PreferenceCategory(this);
            category.setTitle(cat);
            screen.addPreference(category);

            List<com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source> results
                    = sourceManager.getSourceByCategory(cat);

            for(com.zacharycalabrese.doughboy.simplenews2.activity.Helper.Source res : results){
                CheckBoxPreference checkBoxPref = new CheckBoxPreference(this);
                checkBoxPref.setKey(res.name);
                checkBoxPref.setTitle(res.name);
                checkBoxPref.setSummary(res.rssUrl);
                checkBoxPref.setChecked(res.subscribed);
                checkBoxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        List<com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source> source =
                                com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.find(
                                        com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source.class,
                                        "name = ?", preference.getKey());

                        for(com.zacharycalabrese.doughboy.simplenews2.activity.Model.Source item : source){
                            item.subscribed = false;
                            item.save();
                        }


                        return true;
                    }
                });
                category.addPreference(checkBoxPref);
            }
        }
        setPreferenceScreen(screen);
    }
}
