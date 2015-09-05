package com.zacharycalabrese.doughboy.simplenews2.activity.Fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.zacharycalabrese.doughboy.simplenews2.R;

/**
 * Created by zcalabrese on 9/5/15.
 */
public class Settings extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences_settings);
    }
}
