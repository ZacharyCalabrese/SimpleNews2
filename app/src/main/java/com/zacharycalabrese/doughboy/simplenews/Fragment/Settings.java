package com.zacharycalabrese.doughboy.simplenews.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.zacharycalabrese.doughboy.simplenews.Helper.Utils;
import com.zacharycalabrese.doughboy.simplenews.R;

/**
 * Created by zcalabrese on 9/5/15.
 */
public class Settings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences_settings);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        EditTextPreference editTextPreference = (EditTextPreference)
                this.findPreference(getResources().getString(R.string.shared_preference_zip_code));

        ListPreference listPreference = (ListPreference)
                this.findPreference(getResources().getString(R.string.shared_preference_country_code));

        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());

        final String zipCode = sharedPreferences.getString(
                getResources().getString(R.string.shared_preference_zip_code), "");

        final String countryCode = sharedPreferences.getString(
                getResources().getString(R.string.shared_preference_country_code), "");

        editTextPreference.setSummary(zipCode);
        listPreference.setSummary(countryCode);

        SwitchPreference switchPreference = (SwitchPreference)
                this.findPreference(getResources().getString(R.string.pref_dark_theme));

        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast.makeText(getActivity(), "Theme will change on app restart",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
