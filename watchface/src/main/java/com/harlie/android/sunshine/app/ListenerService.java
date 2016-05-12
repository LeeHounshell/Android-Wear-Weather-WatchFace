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
import com.google.android.gms.wearable.DataMap;
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
            DataApi.DataListener
{
    private static final String TAG = "LEE: <" + ListenerService.class.getSimpleName() + ">";

    private static ConnectionHandler sConnectionHandler;
    private static GoogleApiClient sGoogleApiClient;
    private static boolean connectionRequest;

    public static final String LEE_HOUNSHELL_WEAR_PATH = "/sunshine/lee-hounshell";
    public static final String LEE_HOUNSHELL_WEB_PAGE = "http://linkedin.com/pub/lee-hounshell/2/674/852";
    public static final String WEATHER_INFO_PATH = "/sunshine/weather";
    public static final String SYNC_PATH = "/sunshine/sync";

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
        sConnectionHandler.connect(context);
    }

    public static void disconnect() {
        Log.v(TAG, "disconnect");
        if (sConnectionHandler != null) {
            sConnectionHandler.disconnect();
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
                if (ListenerService.WEATHER_INFO_PATH.equals(path.substring(0, ListenerService.WEATHER_INFO_PATH.length()))) {
                    DataMap dmap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    Log.v(TAG, "---> GOT WEATHER_INFO_PATH="+path+", dmap="+dmap.toString());
                    // trigger update for the watchface display
                    WeatherWatchFace.getWatchFaceDesignHolder().fromDataMap(dmap);
                }
                else {
                    Log.w(TAG, "UNEXPECTED PATH: "+path);
                    Log.w(TAG, "UNEXPECTED DATA: "+data);
                }
            }
        }
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
                DataMap dmap = WeatherWatchFace.getWatchFaceDesignHolder().watchConfiguration();
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.
                        getConnectedNodes(sGoogleApiClient).await();
                for (final Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(
                            sGoogleApiClient,
                            node.getId(),
                            SYNC_PATH,
                            dmap.toString().getBytes()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
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
