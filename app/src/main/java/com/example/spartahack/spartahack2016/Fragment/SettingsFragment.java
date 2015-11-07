package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.spartahack.spartahack2016.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
