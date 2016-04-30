package com.harlie.android.wear.watchface;

import android.os.Bundle;
import android.util.Log;

import preference.WearPreferenceActivity;

public class WatchSettingsActivity extends WearPreferenceActivity {
    private final String TAG = "LEE: <" + WatchSettingsActivity.class.getSimpleName() + ">";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
