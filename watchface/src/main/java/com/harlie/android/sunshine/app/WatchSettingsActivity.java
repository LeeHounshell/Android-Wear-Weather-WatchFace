package com.harlie.android.sunshine.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import preference.WearPreferenceActivity;

public class WatchSettingsActivity
        extends
            WearPreferenceActivity
        implements
            SharedPreferences.OnSharedPreferenceChangeListener,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener
{
    private final String TAG = "LEE: <" + WatchSettingsActivity.class.getSimpleName() + ">";

    private static final int NOTIFY_ID_LEE_HOUNSHELL = 1;
    GoogleApiClient mGoogleApiClient;
    Node mNode; // the connected device to send the message to
    private static final String LEE_HOUNSHELL_WEAR_PATH = "/lee-hounshell";
    private boolean mConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //Connect the GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

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
                /*
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
                */

                sendMessage();
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

    // from: https://gist.github.com/gabrielemariotti/117b05aad4db251f7534
    private void sendMessage() {
        Log.v(TAG, "sendMessage");
        if (mNode != null && mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, mNode.getId(), LEE_HOUNSHELL_WEAR_PATH, null).setResultCallback(

                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e(TAG, "Failed to send message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            }
                        }
                    }
            );
        }
        else {
            Log.e(TAG, "sendMessage() FAILED: mNode="+mNode+", mGoogleApiClient="+mGoogleApiClient);
        }
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        if (! mConnected) {
            mGoogleApiClient.connect();
            if (mGoogleApiClient.isConnected()) {
                mConnected = true;
                Log.v(TAG, "connected!");
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "onConnected");
        // Resolve the node = the connected device to send the message to
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                for (Node node : nodes.getNodes()) {
                    mNode = node;
                    Log.v(TAG, (mNode != null) ? "NODE FOUND!" : "NODE NOT FOUND.");
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG, "onConnectionFailed");
    }

}
