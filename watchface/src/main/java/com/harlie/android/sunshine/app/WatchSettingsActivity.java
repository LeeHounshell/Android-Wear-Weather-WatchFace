package com.harlie.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
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
            Log.v(TAG, "===> OPEN WEBPAGE ON PHONE: "+ ListenerService.LEE_HOUNSHELL_WEB_PAGE);

            try {
                /*
                // view linked-in page on watch using a local browser, if one is available
                int NOTIFY_ID_LEE_HOUNSHELL = 1;
                Intent linkedInIntent = new Intent(Intent.ACTION_VIEW);
                linkedInIntent.setData(Uri.parse(WeatherWatchFace.LEE_HOUNSHELL_WEB_PAGE));

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                PendingIntent linkedInPendingIntent = PendingIntent.getActivity(this, 0, linkedInIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_lee_hounshell)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.lee_hounshell))
                        .setContentTitle(getString(R.string.lee_hounshell))
                        .setContentText(getString(R.string.hire_me))
                        .setContentIntent(linkedInPendingIntent)
                        .setAutoCancel(true)
                        .addAction(R.drawable.ic_lee_hounshell, getString(R.string.common_open_on_phone), linkedInPendingIntent);

                notificationManager.notify(NOTIFY_ID_LEE_HOUNSHELL, notificationBuilder.build());
                */

                // show "on phone" animation
                Intent notifyIntent = new Intent(this, ConfirmationActivity.class);
                notifyIntent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                notifyIntent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, ListenerService.LEE_HOUNSHELL_WEB_PAGE);
                startActivity(notifyIntent);

                ListenerService.viewLinkedInPage();
                Log.v(TAG, "request sent.");
            }
            catch (Exception e) {
                Log.e(TAG, "LINKEDIN ACTION_VIEW NOTIFICATION ERROR: "+e);
            }
        }
    }

}
