package com.harlie.android.sunshine.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.util.Log;

import preference.WearPreferenceActivity;

public class WatchSettingsActivity extends WearPreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private final String TAG = "LEE: <" + WatchSettingsActivity.class.getSimpleName() + ">";

    private static final int NOTIFY_ID_LEE_HOUNSHELL = 1;

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
            String webPage = "http://linkedin.com/pub/lee-hounshell/2/674/852";
            Log.v(TAG, "===> OPEN ON PHONE: "+webPage);

            try {
                Intent linkedInIntent = new Intent(Intent.ACTION_VIEW);
                linkedInIntent.setData(Uri.parse(webPage));

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
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

                Intent notifyIntent = new Intent(this, ConfirmationActivity.class);
                notifyIntent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                notifyIntent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, webPage);
                startActivity(notifyIntent);

                /*
                NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_lee_hounshell, getString(R.string.common_open_on_phone), linkedInPendingIntent)
                    .build();

                Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_lee_hounshell)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.lee_hounshell))
                    .setContentTitle(getString(R.string.lee_hounshell))
                    .setContentText(getString(R.string.hire_me))
                    .setContentIntent(linkedInPendingIntent)
                    .setAutoCancel(true)
                    .extend(new NotificationCompat.WearableExtender().addAction(action))
                    .build();
                notificationManager.notify(NOTIFY_ID_LEE_HOUNSHELL, notification);
                */

                Log.v(TAG, "request sent.");
            }
            catch (Exception e) {
                Log.e(TAG, "LINKEDIN ACTION_VIEW NOTIFICATION ERROR: "+e);
            }
        }
    }
}
