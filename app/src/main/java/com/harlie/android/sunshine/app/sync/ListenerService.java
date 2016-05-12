package com.harlie.android.sunshine.app.sync;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.harlie.android.sunshine.app.AnalyticsApplication;
import com.harlie.android.sunshine.app.R;
import com.harlie.android.sunshine.app.WatchFaceDesignHolder;

// wear messages come from the watch into the sunshine app
// from: https://gist.github.com/gabrielemariotti/117b05aad4db251f7534
public class ListenerService
        extends
            WearableListenerService
        implements
            MessageApi.MessageListener
{
    private static final String TAG = "LEE: <sync." + ListenerService.class.getSimpleName() + ">";

    private static ConnectionHandler sConnectionHandler;
    private static GoogleApiClient sGoogleApiClient;
    private static WatchFaceDesignHolder sWatchFaceDesignHolder;

    public static final String SYNC_PATH = "/sunshine/sync";
    public static final String WEATHER_INFO_PATH = "/sunshine/weather";
    public static final String LEE_HOUNSHELL_WEAR_PATH = "/sunshine/lee-hounshell";

    private static class ConnectionHandler
            implements
                GoogleApiClient.ConnectionCallbacks,
                GoogleApiClient.OnConnectionFailedListener
    {
        private final String TAG = "LEE: <" + ConnectionHandler.class.getSimpleName() + ">";

        public void connect(Context context) {
            Log.v(TAG, "connect");
            sGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            sGoogleApiClient.connect();
        }

        public void disconnect() {
            Log.v(TAG, "disconnect");
            if (sGoogleApiClient != null && sGoogleApiClient.isConnected()) {
                sGoogleApiClient.disconnect();
            }
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.v(TAG, "onConnected");
            ListenerService.sendWeatherDataToWear(true);
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

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();
        connect(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    synchronized public static void connect(Context context) {
        Log.v(TAG, "connect");
        if (sConnectionHandler == null) {
            sConnectionHandler = new ConnectionHandler();
        }
        sWatchFaceDesignHolder = getWatchFaceDesignHolder();
        sConnectionHandler.connect(context);
    }

    public static void disconnect() {
        Log.v(TAG, "disconnect");
        if (sConnectionHandler != null) {
            sConnectionHandler.disconnect();
        }
    }

    static WatchFaceDesignHolder watchFaceDesignHolderOldValue;

    public static boolean sendWeatherDataToWear(final boolean force) {
        Log.v(TAG, "sendWeatherDataToWear");

        final WatchFaceDesignHolder watchFaceDesignHolder = sWatchFaceDesignHolder;
        if (force || watchFaceDesignHolderOldValue == null || ! watchFaceDesignHolderOldValue.equals(watchFaceDesignHolder)) {
            Log.w(TAG, "weather has changed - need to send message - ok");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // from: http://stackoverflow.com/questions/33716767/wearlistenerservice-ondatachanged-strange-behavior
                    //NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleClient);
                    final DataMap dataMap = watchFaceDesignHolder.toDataMap();
                    PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(sGoogleApiClient);
                    nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                        @Override
                        public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                            for (Node node : getConnectedNodesResult.getNodes()) {
                                final Node node2 = node;

                                // Construct a DataRequest and send over the data layer
                                // The system time is appended to ensure a unique path and force delivery.
                                // Google won't deliver data items it thinks were already seen.
                                PutDataMapRequest putDMR = PutDataMapRequest.create(ListenerService.WEATHER_INFO_PATH + "/" + System.currentTimeMillis());
                                putDMR.getDataMap().putAll(dataMap);
                                PutDataRequest request = putDMR.asPutDataRequest();
                                request.setUrgent();

                                Log.v(TAG, "requesting send DataMap to " + node.getDisplayName());
                                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(sGoogleApiClient, request);
                                pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                                    @Override
                                    public void onResult(DataApi.DataItemResult dataItemResult) {
                                        if (dataItemResult.getStatus().isSuccess()) {
                                            Log.v(TAG, "weather DataMap: " + dataMap + " requested send to: " + node2.getDisplayName());
                                            watchFaceDesignHolderOldValue = new WatchFaceDesignHolder(watchFaceDesignHolder);
                                        } else {
                                            Log.v(TAG, "ERROR: failed to request send weather DataMap");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }).start();
            return true;
        }
        else{
            Log.w(TAG, "no change in weather - message not sent - ok");
            return false;
        }
    }

    // receive the message from wear
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v(TAG, "---------> onMessageReceived");
        if (messageEvent.getPath().equals(LEE_HOUNSHELL_WEAR_PATH)) {
            String data = new String(messageEvent.getData());
            Log.v(TAG, "=========> AUTHOR MESSAGE RECEIVED: "+data);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            browserIntent.setData(Uri.parse(data));
            startActivity(browserIntent);
        }
        else if (messageEvent.getPath().equals(SYNC_PATH)) {
            String data = new String(messageEvent.getData());
            Log.v(TAG, "=========> SYNC MESSAGE RECEIVED: "+data);
            sendWeatherDataToWear(false);
            // report watch configuration to Google Analytics
            String categoryId = getResources().getString(R.string.category_id);
            String labelId =  getResources().getString(R.string.label_id);
            Log.v(TAG, "GA: category="+categoryId+", label="+labelId+", action="+data);
            AnalyticsApplication.getDefaultTracker().send(
                    new HitBuilders.EventBuilder()
                            .setCategory(categoryId)
                            .setAction(data)
                            .setLabel(labelId)
                            .build());
        }
        else {
            Log.v(TAG, "=========> UNKNOWN MESSAGE messageEvent: path="+messageEvent.getPath()+", data="+messageEvent.getData().toString());
        }
    }

    // a copy of this holder will get sent to the wearable
    synchronized public static WatchFaceDesignHolder getWatchFaceDesignHolder() {
        if (sWatchFaceDesignHolder == null) {
            sWatchFaceDesignHolder = new WatchFaceDesignHolder();
        }
        return sWatchFaceDesignHolder;
    }

}
