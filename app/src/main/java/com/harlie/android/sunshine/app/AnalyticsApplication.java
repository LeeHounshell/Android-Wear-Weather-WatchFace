/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harlie.android.sunshine.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class AnalyticsApplication extends Application {
    private final static String TAG = "LEE: <" + AnalyticsApplication.class.getSimpleName() + ">";

    private static AnalyticsApplication sInstance;
    private static Tracker sTracker;

    public void onCreate() {
        Log.v(TAG, "===> onCreate <===");
        AnalyticsApplication.sInstance = this;
        super.onCreate();
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized static public Tracker getDefaultTracker() {
        Log.v(TAG, "===> getDefaultTracker <===");
        if (sTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(getsInstance());
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            sTracker = analytics.newTracker(R.xml.global_tracker);
            sTracker.enableExceptionReporting(true);
            sTracker.enableAdvertisingIdCollection(true);
            sTracker.enableAutoActivityTracking(true);
        }
        return sTracker;
    }

    public static AnalyticsApplication getsInstance() {
        return sInstance;
    }

}

