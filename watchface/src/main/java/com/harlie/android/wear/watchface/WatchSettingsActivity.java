package com.harlie.android.wear.watchface;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.activity.ConfirmationActivity;
import android.util.Log;

import preference.WearPreferenceActivity;

public class WatchSettingsActivity extends WearPreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private final String TAG = "LEE: <" + WatchSettingsActivity.class.getSimpleName() + ">";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(TAG, "===> onSharedPreferenceChanged - key="+key);
        if (key.equals("contact_lee")) {
            int animation = ConfirmationActivity.OPEN_ON_PHONE_ANIMATION;
            String message = "http://linkedin.com/pub/lee-hounshell/2/674/852";
            Log.v(TAG, "===> OPEN ON PHONE: "+message);
        }
    }
}
