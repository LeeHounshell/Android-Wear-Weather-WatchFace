package com.harlie.android.sunshine.app;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.util.Log;

import java.util.List;

import preference.WearPreferenceActivity;

public class WatchSettingsActivity extends WearPreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private final String TAG = "LEE: <" + WatchSettingsActivity.class.getSimpleName() + ">";

    final int NOTIFY_ID_LEE_HOUNSHELL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    // from: http://www.grokkingandroid.com/checking-intent-availability/
    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(TAG, "===> onSharedPreferenceChanged - key="+key);
        if (key.equals("contact_lee")) {
            int animation = ConfirmationActivity.OPEN_ON_PHONE_ANIMATION;
            Log.v(TAG, "===> OPEN WEBPAGE ON PHONE: "+ ListenerService.LEE_HOUNSHELL_WEB_PAGE);

            try {
                Intent linkedInIntent = new Intent(Intent.ACTION_VIEW);
                if (isAvailable(this, linkedInIntent)) {
                    // view linked-in page on watch using a local browser, if one is available
                    linkedInIntent.setData(Uri.parse(ListenerService.LEE_HOUNSHELL_WEB_PAGE));

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
                }

                // show "on phone" animation
                Intent notifyIntent = new Intent(this, ConfirmationActivity.class);
                notifyIntent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                notifyIntent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, ListenerService.LEE_HOUNSHELL_WEB_PAGE);
                startActivity(notifyIntent);

                ListenerService.viewLinkedInPage();
                Log.v(TAG, "web page request sent.");
            }
            catch (Exception e) {
                Log.e(TAG, "LINKEDIN ACTION_VIEW NOTIFICATION ERROR: "+e);
            }
        }
    }

}
