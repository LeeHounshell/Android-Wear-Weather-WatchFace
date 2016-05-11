package com.harlie.android.sunshine.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

// data items come from the sunshine app into the watch
// from: https://gist.github.com/gabrielemariotti/117b05aad4db251f7534
public class ListenerService
        extends
            WearableListenerService
        implements
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            DataApi.DataListener
{
    private static final String TAG = "LEE: <" + ListenerService.class.getSimpleName() + ">";

    private static ListenerService sListenerService;
    private static GoogleApiClient sGoogleApiClient;
    private static WatchFaceDesignHolder sWatchFaceDesignHolder;

    public static final String SYNC_PATH = "/sunshine/sync";
    public static final String WEATHER_INFO_PATH = "/sunshine/weather";
    public static final String LEE_HOUNSHELL_WEAR_PATH = "/sunshine/lee-hounshell";
    public static final String LEE_HOUNSHELL_WEB_PAGE = "http://linkedin.com/pub/lee-hounshell/2/674/852";
    public static final String SYNC = "true";
    public static final String WEATHER_INFO_KEY = "weather_info";
    public static final String KEY_HIGH_TEMP = "high_temp";
    public static final String KEY_LOW_TEMP = "low_temp";

    public ListenerService() {
        super();
        Log.v(TAG, "constructor");
        sListenerService = this;
        sWatchFaceDesignHolder = new WatchFaceDesignHolder();
    }

    public static ListenerService getInstance() {
        Log.v(TAG, "getInstance");
        if (sListenerService == null) {
            Log.v(TAG, "create instance");
            sListenerService = new ListenerService();
        }
        return sListenerService;
    }

    public static void connect(Context context) {
        Log.v(TAG, "connect");
        sGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(getInstance())
                .addOnConnectionFailedListener(getInstance())
                .build();
        sGoogleApiClient.connect();
    }

    public static void disconnect() {
        Log.v(TAG, "disconnect");
        if (sGoogleApiClient != null && sGoogleApiClient.isConnected()) {
            sGoogleApiClient.disconnect();
            Wearable.DataApi.removeListener(sGoogleApiClient, getInstance());
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.v(TAG, "---------> onDataChanged");
        super.onDataChanged(dataEvents);
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataMapItem mapItem = DataMapItem.fromDataItem(event.getDataItem());
                String path = event.getDataItem().getUri().getPath();
                byte[] data = event.getDataItem().getData();
                if (ListenerService.WEATHER_INFO_PATH.equals(path)) {
                    Log.v(TAG, "---> GOT WEATHER_INFO_PATH!");
                    Log.w(TAG, "DATA: "+data);
                    String high = mapItem.getDataMap().getString(KEY_HIGH_TEMP);
                    if (high != null && ! high.equals("null")) {
                        sWatchFaceDesignHolder.setHighTemp( Integer.valueOf(high) );
                        sWatchFaceDesignHolder.setDirty(true);
                    }
                    String low = mapItem.getDataMap().getString(KEY_LOW_TEMP);
                    if (low != null && ! low.equals("null")) {
                        sWatchFaceDesignHolder.setLowTemp( Integer.valueOf(low) );
                        sWatchFaceDesignHolder.setDirty(true);
                    }
                }
                else if (ListenerService.SYNC_PATH.equals(path)) {
                    Log.v(TAG, "---> GOT SYNC_PATH!");
                    Log.w(TAG, "DATA: "+data);
                }
                else if (ListenerService.LEE_HOUNSHELL_WEAR_PATH.equals(path)) {
                    Log.v(TAG, "---> GOT LEE_HOUNSHELL_WEAR_PATH!");
                    Log.w(TAG, "DATA: "+data);
                }
                else {
                    Log.w(TAG, "UNEXPECTED PATH: "+path);
                    Log.w(TAG, "UNEXPECTED DATA: "+data);
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "onConnected");
        Wearable.DataApi.addListener(sGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG, "onConnectionFailed");
    }

    public static void viewLinkedInPage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "viewLinkedInPage");
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(sGoogleApiClient).await();
                Log.v(TAG, "NODE COUNT: " + nodes.getNodes().size());
                for (final Node node : nodes.getNodes()) {
                    Log.v(TAG, "*** SENDING MESAGE TO NODE ID: " + node.getId());
                    Wearable.MessageApi.sendMessage(
                            sGoogleApiClient,
                            node.getId(),
                            LEE_HOUNSHELL_WEAR_PATH,
                            LEE_HOUNSHELL_WEB_PAGE.getBytes()).setResultCallback(
                            new ResultCallback<MessageApi.SendMessageResult>() {
                                @Override
                                public void onResult(@NonNull MessageApi.SendMessageResult result) {
                                    if (!result.getStatus().isSuccess()) {
                                        Log.e(TAG, "FAILED TO SEND MESSAGE TO PHONE " + node.getDisplayName());
                                    } else {
                                        Log.v(TAG, "SUCCESS!! SENT MESSAGE TO PHONE " + node.getDisplayName());
                                    }
                                }
                            });
                }
            }
        }).start();
    }


    public static void createSyncMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.
                        getConnectedNodes(sGoogleApiClient).await();
                for (final Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(
                            sGoogleApiClient,
                            node.getId(),
                            SYNC_PATH,
                            SYNC.getBytes()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(@NonNull MessageApi.SendMessageResult result) {
                            if (!result.getStatus().isSuccess()) {
                                Log.e(TAG, "UNABLE TO SYNC WITH PHONE " + node.getDisplayName());
                            } else {
                                Log.v(TAG, "SUCCESS!! SYNC WITH PHONE " + node.getDisplayName());
                            }
                        }
                    });
                }
            }
        }).start();
    }

}
