package com.harlie.android.sunshine.app.sync;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.harlie.android.sunshine.app.WatchFaceDesignHolder;

// from: https://gist.github.com/gabrielemariotti/117b05aad4db251f7534
public class ListenerService
        extends
            WearableListenerService
        implements
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            MessageApi.MessageListener
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
    public void onConnected(Bundle bundle) {
        Log.v(TAG, "onConnected");
        sWatchFaceDesignHolder = new WatchFaceDesignHolder();
        Wearable.DataApi.addListener(sGoogleApiClient, ListenerService.getInstance());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(TAG, "onConnectionFailed");
    }

    static WatchFaceDesignHolder watchFaceDesignHolderOldValue;

    public static void sendWeatherDataToWear() {
        Log.v(TAG, "sendWeatherDataToWear");
        new Thread(new Runnable() {
            @Override
            public void run() {

                WatchFaceDesignHolder watchFaceDesignHolder = sWatchFaceDesignHolder;
                if (watchFaceDesignHolderOldValue == null || !watchFaceDesignHolderOldValue.equals(watchFaceDesignHolder)) {
                    Log.w(TAG, "weather has changed - need to send message - ok");
                    watchFaceDesignHolderOldValue = new WatchFaceDesignHolder(watchFaceDesignHolder);
                    PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(ListenerService.WEATHER_INFO_PATH);
                    Parcel weatherParcel = Parcel.obtain();
                    watchFaceDesignHolder.writeToParcel(weatherParcel, 0);
                    String[] weatherData = weatherParcel.createStringArray();
                    putDataMapRequest.getDataMap().putStringArray(ListenerService.WEATHER_INFO_KEY, weatherData);
                    PutDataRequest request = putDataMapRequest.asPutDataRequest();
                    request.setUrgent();
                    Log.v(TAG, "PutDataRequest created for " + ListenerService.WEATHER_INFO_KEY);
                    Wearable.DataApi.putDataItem(sGoogleApiClient, request)
                            .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                                @Override
                                public void onResult(DataApi.DataItemResult dataItemResult) {
                                    Log.v(TAG, "putDataItem onResult callback");
                                    if (!dataItemResult.getStatus().isSuccess()) {
                                        Log.e(TAG, "FAILED to sendWeatherDataToWear - path=" + ListenerService.WEATHER_INFO_PATH);
                                    } else {
                                        Log.e(TAG, "SUCCESS for sendWeatherDataToWear - path=" + ListenerService.WEATHER_INFO_PATH);
                                    }
                                }
                            });
                } else {
                    Log.w(TAG, "no change in weather - message not sent - ok");
                }
            }
        }).start();
    }

    /*
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.v(TAG, "onDataChanged");
        super.onDataChanged(dataEvents);
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataMapItem mapItem = DataMapItem.fromDataItem(event.getDataItem());
                String path = event.getDataItem().getUri().getPath();
                byte[] data = event.getDataItem().getData();
                if (ListenerService.WEATHER_INFO_PATH.equals(path)) {
                    Log.v(TAG, "---> GOT WEATHER_INFO_PATH!");
                    Log.w(TAG, "DATA: "+data);
                    // FIXME: fill in data
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
    */

    // receive the message from wear
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v(TAG, "onMessageReceived");
        Log.v(TAG, "messageEvent: path="+messageEvent.getPath()+", data="+messageEvent.getData());
        if (messageEvent.getPath().equals(LEE_HOUNSHELL_WEAR_PATH)) {
            Log.v(TAG, "=========> MESSAGE RECEIVED: "+messageEvent);
        }
        else {
            Log.v(TAG, "=========> UNKNOWN MESSAGE RECEIVED: "+messageEvent);
        }
    }

}
