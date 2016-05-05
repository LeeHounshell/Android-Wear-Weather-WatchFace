package com.harlie.android.sunshine.app.sync;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

// from: https://gist.github.com/gabrielemariotti/117b05aad4db251f7534
public class ListenerServiceFromWear extends WearableListenerService {
    private final String TAG = "LEE: <" + ListenerServiceFromWear.class.getSimpleName() + ">";

    private static final String LEE_HOUNSHELL_WEAR_PATH = "/lee-hounshell";

    // receive the message from wear
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v(TAG, "onMessageReceived");
        if (messageEvent.getPath().equals(LEE_HOUNSHELL_WEAR_PATH)) {
            Log.v(TAG, "=========> RECEIVED: "+messageEvent);
        }
    }

}
