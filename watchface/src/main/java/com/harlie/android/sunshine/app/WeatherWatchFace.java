/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harlie.android.sunshine.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import me.denley.preferencebinder.PreferenceBinder;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't shown. On
 * devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient mode.
 */
public class WeatherWatchFace extends CanvasWatchFaceService {
    private final String TAG = "LEE: <" + WeatherWatchFace.class.getSimpleName() + ">";

    private static Engine engine;

    /**
     * Update rate in milliseconds for interactive mode. We update once a second to advance the
     * second hand.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;
    private static WatchFaceDesignHolder sWatchFaceDesignHolder;
    private static Context mContext;

    @Override
    public Engine onCreateEngine() {
        Log.v(TAG, "onCreateEngine");
        mContext = this.getApplicationContext();
        Log.v(TAG, "connect WearTalkService..");
        WearTalkService.connect(getContext());
        engine = new Engine();
        return engine;
    }

    public static Context getContext() {
        return mContext;
    }

    private static class EngineHandler extends Handler
    {
        private final WeakReference<WeatherWatchFace.Engine> mWeakReference;

        public EngineHandler(WeatherWatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            WeatherWatchFace.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }

    }

    private class Engine extends CanvasWatchFaceService.Engine
    {
        private final String TAG = "LEE: <" + Engine.class.getSimpleName() + ">";

        final Handler mUpdateTimeHandler = new EngineHandler(this);

        private final float CENTER_GAP_AND_CIRCLE_RADIUS = 4f;
        private boolean mRegisteredTimeZoneReceiver = false;
        private SimpleDateFormat mDateFormat;
        private Bitmap mBackgroundBitmap;
        private Bitmap mBackgroundAmbientBitmap;
        private Bitmap mBackgroundAmbientGoldBitmap;
        private Bitmap mHourHandBitmap;
        private Bitmap mHourHandGlovesBitmap;
        private Bitmap mMinuteHandBitmap;
        private Bitmap mMinuteHandGlovesBitmap;
        private Bitmap mSecondHandBitmap;
        private Bitmap mBackgroundBitmapScaled;
        private Bitmap mBackgroundAmbientBitmapScaled;
        private Bitmap mBackgroundAmbientGoldBitmapScaled;
        private Bitmap mHourHandBitmapScaled;
        private Bitmap mHourHandGlovesBitmapScaled;
        private Bitmap mMinuteHandBitmapScaled;
        private Bitmap mMinuteHandGlovesBitmapScaled;
        private Bitmap mSecondHandBitmapScaled;
        private Paint mBackgroundPaint;
        private Paint mHandPaint;
        private Paint mHandPaintShadow;
        private Paint mHandPaintAccent;
        private Paint mHandPaintBright;
        private Paint mHandPaintGold;
        private Paint mHandPaintJoint;
        private Paint mHandPaintTempHigh;
        private Paint mHandPaintTempLow;
        private Paint mHandPaintDate;
        private boolean mAmbient;
        private boolean mDaylightChanged;
        private boolean mIsRound;
        private boolean mIsJewelStudded;
        private boolean mIsDeviceMuted;
        private Calendar mCalendar;
        private int mBatteryLevel;
        private int mTapCount;
        private int mHeight;
        private int mWidth;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        private boolean mLowBitAmbient;

        final String[] mWeekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        // receiver to update the time zone
        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCalendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            }
        };

        @Override
        public void onCreate(SurfaceHolder holder) {
            Log.v(TAG, "onCreate");
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(WeatherWatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setStatusBarGravity(Gravity.TOP | Gravity.RIGHT)
                    .setHotwordIndicatorGravity(Gravity.TOP | Gravity.LEFT)
                    .setPeekOpacityMode(WatchFaceStyle.PEEK_OPACITY_MODE_TRANSLUCENT)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());

            sWatchFaceDesignHolder = getWatchFaceDesignHolder();

            PreferenceBinder.bind(getContext(), sWatchFaceDesignHolder);

            // calculate current phase of the moon
            MoonCalculation moonCalculaion = new MoonCalculation();
            mCalendar = Calendar.getInstance();
            Date date = new Date();
            mCalendar.setTime(date);
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH) + 1;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            Log.v(TAG, "year="+year+", month="+month+", day="+day);
            sWatchFaceDesignHolder.setMoonPhase(moonCalculaion.moonPhase(year, month, day));
            mDateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());

            mHourHandBitmap = drawableToBitmap(getDrawable(R.drawable.hour_little_hand));
            mHourHandGlovesBitmap = drawableToBitmap(getDrawable(R.drawable.hour_little_hand_ambient));
            mMinuteHandBitmap = drawableToBitmap(getDrawable(R.drawable.minute_big_hand));
            mMinuteHandGlovesBitmap = drawableToBitmap(getDrawable(R.drawable.minute_big_hand_ambient));
            mSecondHandBitmap = drawableToBitmap(getDrawable(R.drawable.hypnosis));

            createPaint();

            // calculate if day or night
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            sWatchFaceDesignHolder.setDaytime((hour >= 6 && hour < 18));

            WindowManager wm = (WindowManager) WeatherWatchFace.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            mWidth = metrics.widthPixels;
            mHeight = metrics.heightPixels;

            createWatchFaceBitmaps();

            mBackgroundAmbientBitmap = drawableToBitmap(getDrawable(R.drawable.clock_face_ambient));
            mBackgroundAmbientBitmapScaled = Bitmap.createScaledBitmap(mBackgroundAmbientBitmap, mWidth, mHeight, true /* filter */);
            mBackgroundAmbientGoldBitmap = drawableToBitmap(getDrawable(R.drawable.clock_face_ambient_gold));
            mBackgroundAmbientGoldBitmapScaled = Bitmap.createScaledBitmap(mBackgroundAmbientGoldBitmap, mWidth, mHeight, true /* filter */);

            // sync with phone now
            WearTalkService.createSyncMessage();
        }

        public void createPaint() {
            Log.v(TAG, "createPaint");
            Resources resources = WeatherWatchFace.this.getResources();
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.background));

            mHandPaint = new Paint();
            mHandPaint.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.analog_hands));
            mHandPaint.setStrokeWidth(resources.getDimension(R.dimen.analog_hand_stroke));
            mHandPaint.setAntiAlias(true);
            mHandPaint.setStrokeCap((mIsRound) ? Paint.Cap.ROUND : Paint.Cap.SQUARE);

            mHandPaintShadow = new Paint();
            mHandPaintShadow.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.analog_hands));
            mHandPaintShadow.setStrokeWidth(resources.getDimension(R.dimen.analog_hand_stroke));
            mHandPaintShadow.setAntiAlias(true);
            mHandPaintShadow.setStrokeCap((mIsRound) ? Paint.Cap.ROUND : Paint.Cap.SQUARE);
            mHandPaintShadow.setShadowLayer(3.0f, 4.0f, 4.0f, Color.BLACK);
            mHandPaintShadow.setStyle(Paint.Style.STROKE);

            mHandPaintAccent = new Paint();
            mHandPaintAccent.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.battery_warning));
            mHandPaintAccent.setStrokeWidth(resources.getDimension(R.dimen.analog_hand_stroke));
            mHandPaintAccent.setAntiAlias(true);
            mHandPaintAccent.setStrokeCap((mIsRound) ? Paint.Cap.ROUND : Paint.Cap.SQUARE);
            mHandPaintAccent.setShadowLayer(3.0f, 4.0f, 4.0f, Color.BLACK);
            mHandPaintAccent.setStyle(Paint.Style.STROKE);

            mHandPaintBright = new Paint();
            mHandPaintBright.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.analog_hands_bright));
            mHandPaintBright.setStrokeWidth(resources.getDimension(R.dimen.analog_hand_stroke_bright));
            mHandPaintBright.setAntiAlias(true);
            mHandPaintBright.setStrokeCap((mIsRound) ? Paint.Cap.ROUND : Paint.Cap.SQUARE);

            mHandPaintGold = new Paint();
            mHandPaintGold.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.analog_hands_gold));
            mHandPaintGold.setStrokeWidth(resources.getDimension(R.dimen.analog_hand_stroke_bright));
            mHandPaintGold.setAntiAlias(true);
            mHandPaintGold.setStrokeCap((mIsRound) ? Paint.Cap.ROUND : Paint.Cap.SQUARE);

            mHandPaintJoint = new Paint();
            mHandPaintJoint.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.analog_hands));
            mHandPaintJoint.setStrokeWidth(resources.getDimension(R.dimen.analog_hand_stroke));
            mHandPaintJoint.setAntiAlias(true);
            mHandPaintJoint.setStrokeCap((mIsRound) ? Paint.Cap.ROUND : Paint.Cap.SQUARE);

            mHandPaintTempHigh = new Paint();
            mHandPaintTempHigh.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.temperature_high));
            mHandPaintTempHigh.setStrokeWidth(resources.getDimension(R.dimen.temperature_stroke));
            mHandPaintTempHigh.setAntiAlias(true);
            mHandPaintTempHigh.setTypeface(Typeface.DEFAULT);
            mHandPaintTempHigh.setTextSize(getResources().getInteger(R.integer.temperature_text_size));
            mHandPaintTempHigh.setStyle(Paint.Style.FILL);

            mHandPaintTempLow = new Paint();
            mHandPaintTempLow.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.temperature_low));
            mHandPaintTempLow.setStrokeWidth(resources.getDimension(R.dimen.temperature_stroke));
            mHandPaintTempLow.setAntiAlias(true);
            mHandPaintTempLow.setTypeface(Typeface.DEFAULT);
            mHandPaintTempLow.setTextSize(getResources().getInteger(R.integer.temperature_text_size));
            mHandPaintTempLow.setStyle(Paint.Style.FILL);

            mHandPaintDate = new Paint();
            mHandPaintDate.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), R.color.current_date));
            mHandPaintDate.setStrokeWidth(resources.getDimension(R.dimen.current_date_stroke));
            mHandPaintDate.setAntiAlias(true);
            mHandPaintDate.setTypeface(Typeface.DEFAULT);
            mHandPaintDate.setTextSize(getResources().getInteger(R.integer.temperature_text_size));
            mHandPaintDate.setStyle(Paint.Style.FILL);
        }

        private void createWatchFaceBitmaps() {
            Log.v(TAG, "createWatchBitmaps");
            mBackgroundBitmap = createWatchBackgroundBitmap(getWatchFaceDesignHolder());
            scaleWatchFace(mWidth, mHeight);
        }

        private Bitmap createWatchBackgroundBitmap(WatchFaceDesignHolder watchFaceDesignHolder) {
            Log.v(TAG, "createWatchBackgroundBitmap");
            watchFaceDesignHolder.setDirty(false);
            if (watchFaceDesignHolder == null) {
                Log.v(TAG, "use default clock_face");
                mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.clock_face));
                return mBackgroundBitmap;
            }
            Bitmap overlay;
            if (watchFaceDesignHolder.useStaticBackground()) {
                if (watchFaceDesignHolder.isDaytime()) {
                    if (watchFaceDesignHolder.isOvercast()) {
                        mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.day_overcast));
                        Log.v(TAG, "overcast - static");
                    }
                    else {
                        mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.day));
                        Log.v(TAG, "day - static");
                    }
                }
                else {
                    mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night));
                    Log.v(TAG, "night - static");
                }
                if (watchFaceDesignHolder.useStandardFace()) {
                    overlay = drawableToBitmap(getDrawable(R.drawable.not_windy_day_standard));
                    Log.v(TAG, "not_windy_day - standard - static");
                }
                else {
                    overlay = drawableToBitmap(getDrawable(R.drawable.not_windy_day));
                    Log.v(TAG, "not_windy_day - alternate - static");
                }
            }
            else {
                if (watchFaceDesignHolder.isDaytime()) {
                    if (watchFaceDesignHolder.isSunshine()) {
                        mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.day));
                        Log.v(TAG, "day");
                        if (!watchFaceDesignHolder.isHeavyClouds()
                                && !watchFaceDesignHolder.isHeavyRain()
                                && !watchFaceDesignHolder.isHeavySnow()
                                && !watchFaceDesignHolder.isHeavyStorm()) {
                            overlay = drawableToBitmap(getDrawable(R.drawable.sunny));
                            mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                            Log.v(TAG, "sunny");
                        }
                    } else if (watchFaceDesignHolder.isOvercast()) {
                        mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.day_overcast));
                        Log.v(TAG, "overcast");
                    } else {
                        mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.day));
                        Log.v(TAG, "day");
                    }
                } else {
                    switch (watchFaceDesignHolder.getMoonPhase()) {
                        case 1:
                            mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night_waxing_crescent));
                            Log.v(TAG, "night_waxing_crescent");
                            break;
                        case 2:
                            mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night_first_quarter));
                            Log.v(TAG, "night_first_quarter");
                            break;
                        case 3:
                            mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night_waxing_gibbous));
                            Log.v(TAG, "night_waxing_gibbous");
                            break;
                        case 4:
                            mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night_full_moon));
                            Log.v(TAG, "night_full_moon");
                            break;
                        case 5:
                            mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night_waning_gibbous));
                            Log.v(TAG, "night_waning_gibbous");
                            break;
                        case 6:
                            mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night_last_quarter));
                            Log.v(TAG, "night_last_quarter");
                            break;
                        case 7:
                            mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night_waning_crescent));
                            Log.v(TAG, "night_waning_crescent");
                            break;
                        case 0:
                        default:
                            mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.night_no_moon));
                            Log.v(TAG, "night_no_moon");
                            break;
                    }
                }

                // cloudy
                if (watchFaceDesignHolder.isHeavyClouds() || watchFaceDesignHolder.isModerateClouds() || watchFaceDesignHolder.isLightClouds()) {
                    if (watchFaceDesignHolder.isAreCloudsLow()) {
                        if (watchFaceDesignHolder.isAreCloudsDark()) {
                            if (watchFaceDesignHolder.isHeavyClouds() || watchFaceDesignHolder.isModerateClouds()) {
                                overlay = drawableToBitmap(getDrawable(R.drawable.moderate_dark_low_clouds));
                                Log.v(TAG, "moderate_dark_low_clouds");
                            } else {
                                overlay = drawableToBitmap(getDrawable(R.drawable.dark_low_cloud));
                                Log.v(TAG, "dark_low_cloud");
                            }
                        } else { // light clouds
                            if (watchFaceDesignHolder.isHeavyClouds() || watchFaceDesignHolder.isModerateClouds()) {
                                overlay = drawableToBitmap(getDrawable(R.drawable.moderate_light_low_clouds));
                                Log.v(TAG, "moderate_light_low_clouds");
                            } else {
                                overlay = drawableToBitmap(getDrawable(R.drawable.light_low_cloud));
                                Log.v(TAG, "light_low_cloud");
                            }
                        }
                    } else { // high clouds
                        if (watchFaceDesignHolder.isAreCloudsDark()) {
                            if (watchFaceDesignHolder.isHeavyClouds() || watchFaceDesignHolder.isModerateClouds()) {
                                overlay = drawableToBitmap(getDrawable(R.drawable.moderate_dark_high_clouds));
                                Log.v(TAG, "moderate_dark_high_clouds");
                            } else {
                                overlay = drawableToBitmap(getDrawable(R.drawable.dark_high_cloud));
                                Log.v(TAG, "dark_high_cloud");
                            }
                        } else { // light clouds
                            if (watchFaceDesignHolder.isHeavyClouds() || watchFaceDesignHolder.isModerateClouds()) {
                                overlay = drawableToBitmap(getDrawable(R.drawable.moderate_light_high_clouds));
                                Log.v(TAG, "moderate_light_high_clouds");
                            } else {
                                overlay = drawableToBitmap(getDrawable(R.drawable.light_high_cloud));
                                Log.v(TAG, "light_high_cloud");
                            }
                        }
                    }
                    if (overlay != null) {
                        mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    }
                }

                // rainy
                if (watchFaceDesignHolder.isHeavyRain() || watchFaceDesignHolder.isModerateRain() || watchFaceDesignHolder.isLightRain()) {
                    if (watchFaceDesignHolder.isHeavyRain()) {
                        overlay = drawableToBitmap(getDrawable(R.drawable.heavy_rain));
                        Log.v(TAG, "heavy_rain");
                    } else if (watchFaceDesignHolder.isModerateRain()) {
                        overlay = drawableToBitmap(getDrawable(R.drawable.moderate_rain));
                        Log.v(TAG, "moderate_rain");
                    } else {
                        overlay = drawableToBitmap(getDrawable(R.drawable.light_rain));
                        Log.v(TAG, "light_rain");
                    }
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                }

                // snowy
                if (watchFaceDesignHolder.isHeavySnow() || watchFaceDesignHolder.isModerateSnow() || watchFaceDesignHolder.isLightSnow()) {
                    if (watchFaceDesignHolder.isHeavySnow()) {
                        overlay = drawableToBitmap(getDrawable(R.drawable.heavy_snow));
                        Log.v(TAG, "heavy_snow");
                    } else if (watchFaceDesignHolder.isModerateSnow()) {
                        overlay = drawableToBitmap(getDrawable(R.drawable.moderate_snow));
                        Log.v(TAG, "moderate_snow");
                    } else {
                        overlay = drawableToBitmap(getDrawable(R.drawable.light_snow));
                        Log.v(TAG, "light_snow");
                    }
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                }

                // stormy
                if (watchFaceDesignHolder.isHeavyStorm() || watchFaceDesignHolder.isModerateStorm() || watchFaceDesignHolder.isLightStorm()) {
                    if (watchFaceDesignHolder.isHeavyStorm()) {
                        overlay = drawableToBitmap(getDrawable(R.drawable.heavy_storm));
                        Log.v(TAG, "heavy_storm");
                    } else if (watchFaceDesignHolder.isModerateStorm()) {
                        overlay = drawableToBitmap(getDrawable(R.drawable.moderate_storm));
                        Log.v(TAG, "moderate_storm");
                    } else {
                        overlay = drawableToBitmap(getDrawable(R.drawable.light_storm));
                        Log.v(TAG, "light_storm");
                    }
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                }

                if (watchFaceDesignHolder.useStandardFace()) {
                    // windy
                    if (watchFaceDesignHolder.isHeavyWind() || watchFaceDesignHolder.isModerateWind() || watchFaceDesignHolder.isLightWind()) {
                        overlay = drawableToBitmap(getDrawable(R.drawable.windy_day_standard));
                        Log.v(TAG, "windy_day - standard");
                    } else {
                        overlay = drawableToBitmap(getDrawable(R.drawable.not_windy_day_standard));
                        Log.v(TAG, "not_windy_day - standard");
                    }
                } else {
                    // windy
                    if (watchFaceDesignHolder.isHeavyWind() || watchFaceDesignHolder.isModerateWind() || watchFaceDesignHolder.isLightWind()) {
                        overlay = drawableToBitmap(getDrawable(R.drawable.windy_day));
                        Log.v(TAG, "windy_day - alternate");
                    } else {
                        overlay = drawableToBitmap(getDrawable(R.drawable.not_windy_day));
                        Log.v(TAG, "not_windy_day - alternate");
                    }
                }
            }
            mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);

            // clock face
            mIsJewelStudded = false;
            if (watchFaceDesignHolder.useRomanNumeralsFace()) {
                if (watchFaceDesignHolder.useGoldInlay()) {
                    overlay = drawableToBitmap(getDrawable(R.drawable.clock_face_roman_gold));
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    Log.v(TAG, "clock_face_roman_gold");
                } else {
                    overlay = drawableToBitmap(getDrawable(R.drawable.clock_face_roman_plain));
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    Log.v(TAG, "clock_face_roman_plain");
                }
            }
            else {
                // check for special cases
                if (watchFaceDesignHolder.useGoldInlay() && watchFaceDesignHolder.usePreciousStones()) {
                    mIsJewelStudded = true;
                    overlay = drawableToBitmap(getDrawable(R.drawable.clock_face_diamond));
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    Log.v(TAG, "clock_face_diamond");
                }
                else if (watchFaceDesignHolder.usePreciousStones()) {
                    mIsJewelStudded = true;
                    overlay = drawableToBitmap(getDrawable(R.drawable.clock_face_ruby));
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    Log.v(TAG, "clock_face_ruby");
                }
                else if (watchFaceDesignHolder.useGoldInlay()) {
                    overlay = drawableToBitmap(getDrawable(R.drawable.clock_face_gold));
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    Log.v(TAG, "clock_face_gold");
                }
                else {
                    overlay = drawableToBitmap(getDrawable(R.drawable.clock_face));
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    Log.v(TAG, "clock_face");
                }
            }

            // face tick marks - check for special cases
            if (! watchFaceDesignHolder.useSecondHand() || mAmbient || mIsJewelStudded || watchFaceDesignHolder.useHypnosis()) {
                Log.v(TAG, "tickmarks_none");
            }
            else if (! watchFaceDesignHolder.useStandardFace()
                    && ! watchFaceDesignHolder.useRomanNumeralsFace()
                    && watchFaceDesignHolder.useGoldInlay()
                    && watchFaceDesignHolder.usePreciousStones())
            {
                Log.v(TAG, "tickmarks_none");
            }
            else if (watchFaceDesignHolder.usePreciousStones() && ! watchFaceDesignHolder.useGoldInlay()) {
                overlay = drawableToBitmap(getDrawable(R.drawable.tickmarks_plain));
                mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                Log.v(TAG, "tickmarks_plain");
            }
            else if (watchFaceDesignHolder.usePreciousStones()) {
                overlay = drawableToBitmap(getDrawable(R.drawable.tickmarks_ivory));
                mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                Log.v(TAG, "precious_stones - tickmarks_ivory");
            }
            else if (! watchFaceDesignHolder.useRomanNumeralsFace() && ! watchFaceDesignHolder.useGoldInlay()) {
                Log.v(TAG, "tickmarks_none");
            }
            else if (! watchFaceDesignHolder.useRomanNumeralsFace()) {
                overlay = drawableToBitmap(getDrawable(R.drawable.tickmarks_plain));
                mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                Log.v(TAG, "tickmarks_plain");
            }
            else {
                Log.v(TAG, "tickmarks_none");
            }

            return mBackgroundBitmap;
        }

        // from: http://stackoverflow.com/questions/3674441/combining-2-images-overlayed
        public Bitmap combineImages(Bitmap topImage, Bitmap bottomImage) {
            if (topImage == null) {
                Log.w(TAG, "topImage is null!");
                return bottomImage;
            }
            if (bottomImage == null) {
                Log.w(TAG, "bottomImage is null!");
                return topImage;
            }
            Bitmap overlay = Bitmap.createBitmap(bottomImage.getWidth(), bottomImage.getHeight(), bottomImage.getConfig());
            Canvas canvas = new Canvas(overlay);
            canvas.drawBitmap(bottomImage, new Matrix(), null);
            canvas.drawBitmap(topImage, 0, 0, null);
            return overlay;
        }

        // from: http://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
        public Bitmap drawableToBitmap (Drawable drawable) {
            Bitmap bitmap;

            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if (bitmapDrawable.getBitmap() != null) {
                    return bitmapDrawable.getBitmap();
                }
            }

            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            mIsRound = insets.isRound();
        }

        @Override
        public void onDestroy() {
            Log.v(TAG, "onDestroy");
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            PreferenceBinder.unbind(getContext());
            Log.v(TAG, "disconnect WearTalkService..");
            WearTalkService.disconnect();
            super.onDestroy();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            Log.v(TAG, "onPropertiesChanged");
            super.onPropertiesChanged(properties);
            if (getWatchFaceDesignHolder() != null) {
                getWatchFaceDesignHolder().setDirty(true); // the watch face is out of sync now
            }
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            Date date = new Date();
            mCalendar.setTime(date);
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            boolean wasDaytime = getWatchFaceDesignHolder().isDaytime();
            getWatchFaceDesignHolder().setDaytime((hour >= 6 && hour < 18));
            if (wasDaytime !=  getWatchFaceDesignHolder().isDaytime()) {
                getWatchFaceDesignHolder().setDirty(true); // the watch face is out of sync now
            }
            mBatteryLevel = getBatteryLevel();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            Log.v(TAG, "onAmbientModeChanged");
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;
                if (mLowBitAmbient) {
                    mHandPaint.setAntiAlias(!inAmbientMode);
                }
                getWatchFaceDesignHolder().setDirty(true);
            }
            invalidate();

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) {
            Log.v(TAG, "onInterruptionFilterChanged");
            super.onInterruptionFilterChanged(interruptionFilter);
            mIsDeviceMuted = (interruptionFilter == android.support.wearable.watchface.WatchFaceService.INTERRUPTION_FILTER_NONE);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.v(TAG, "onSurfaceChanged");
            if (mBackgroundBitmap != null &&
                    ((mBackgroundBitmap.getWidth() != width) || (mBackgroundBitmap.getHeight() != height)))
            {
                scaleWatchFace(width, height);
            }
            super.onSurfaceChanged(holder, format, width, height);
        }

        private void scaleWatchFace(int width, int height) {
            Log.v(TAG, "scaleWatchFace");
            mBackgroundBitmapScaled = Bitmap.createScaledBitmap(mBackgroundBitmap, width, height, true /* filter */);
            float ratio = (float) width / mBackgroundBitmap.getWidth();
            mHourHandBitmapScaled = Bitmap.createScaledBitmap(mHourHandBitmap,
                    (int) (mHourHandBitmap.getWidth() * ratio),
                    (int) (mHourHandBitmap.getHeight() * ratio), true);
            mHourHandGlovesBitmapScaled = Bitmap.createScaledBitmap(mHourHandGlovesBitmap,
                    (int) (mHourHandGlovesBitmap.getWidth() * ratio),
                    (int) (mHourHandGlovesBitmap.getHeight() * ratio), true);
            mMinuteHandBitmapScaled = Bitmap.createScaledBitmap(mMinuteHandBitmap,
                    (int) (mMinuteHandBitmap.getWidth() * ratio),
                    (int) (mMinuteHandBitmap.getHeight() * ratio), true);
            mMinuteHandGlovesBitmapScaled = Bitmap.createScaledBitmap(mMinuteHandGlovesBitmap,
                    (int) (mMinuteHandGlovesBitmap.getWidth() * ratio),
                    (int) (mMinuteHandGlovesBitmap.getHeight() * ratio), true);
            mSecondHandBitmapScaled = Bitmap.createScaledBitmap(mSecondHandBitmap,
                    (int) (mSecondHandBitmap.getWidth() * ratio),
                    (int) (mSecondHandBitmap.getHeight() * ratio), true);
        }

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            Log.v(TAG, "onTapCommand");
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    // The user has started touching the screen.
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    mTapCount++;
                    mBackgroundPaint.setColor(ContextCompat.getColor(WeatherWatchFace.getContext(), mTapCount % 2 == 0 ?
                            R.color.background : R.color.background2));
                    break;
            }
            invalidate();
        }

        private String formatTemperature(int temp) {
            //char scale = (getWatchFaceDesignHolder().isMetric()) ? 'c' : 'f';
            char degree = '\u00B0';
            return String.format(Locale.getDefault(), "%02d%c", temp, degree);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            Date date = new Date();
            mCalendar.setTime(date);

            boolean useSecondHand = getWatchFaceDesignHolder().useSecondHand();
            boolean ambientOverride = getWatchFaceDesignHolder().useContinuousOn();
            boolean realAmbientMode = (mAmbient && ! ambientOverride);

            WatchFaceDesignHolder holder = getWatchFaceDesignHolder();

            if (holder.isDirty()) {
                Log.v(TAG, "*** WATCH FACE UPDATE ***");
                createWatchFaceBitmaps();
            }
            else {
                if (mCalendar.get(Calendar.HOUR) == 6 || mCalendar.get(Calendar.HOUR) == 18) {
                    if (!mDaylightChanged) {
                        Log.v(TAG, "*** WATCH FACE DAYLIGHT CHANGE ***");
                        mDaylightChanged = true;
                        createWatchFaceBitmaps();
                    }
                } else {
                    mDaylightChanged = false;
                }
            }

            // Draw the background.
            if (realAmbientMode) {
                canvas.drawColor(Color.BLACK);
                if (holder.useGoldInlay()) {
                    canvas.drawBitmap(mBackgroundAmbientGoldBitmapScaled, 0, 0, null);
                }
                else {
                    canvas.drawBitmap(mBackgroundAmbientBitmapScaled, 0, 0, null);
                }
            } else {
                if (mBackgroundBitmapScaled == null) {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackgroundPaint);
                }
                else {
                    canvas.drawBitmap(mBackgroundBitmapScaled, 0, 0, null);
                }
            }

            // Find the center. Ignore the window insets so that, on round watches with a
            // "chin", the watch face is centered on the entire screen, not just the usable portion.
            float centerX = bounds.width() / 2f;
            float centerY = bounds.height() / 2f;

            // we know there are 60 seconds in a minute and 360 degrees in a circle, so 1 second of time = 6 degrees arc

            int minutes = mCalendar.get(Calendar.MINUTE);
            float minRot = minutes / 30f * (float) Math.PI;
            float hrRot = ((mCalendar.get(Calendar.HOUR) + (minutes / 60f)) / 6f) * (float) Math.PI;

            float hrLength = centerX / 2;
            float minLength = hrLength + (hrLength / (mIsJewelStudded ? 2 : 3));

            // draw the hour and minute hands
            if (realAmbientMode) {
                if (! holder.useStandardFace() && ! holder.useGoldInlay() && ! holder.usePreciousStones()) {
                    // glow hour hand from Bitmap
                    Matrix matrix = new Matrix();
                    matrix.setRotate(hrRot / (float) Math.PI * 180, mHourHandGlovesBitmapScaled.getWidth() / 2, mHourHandGlovesBitmapScaled.getHeight() / 2);
                    canvas.drawBitmap(mHourHandGlovesBitmapScaled, matrix, mHandPaint);

                    // glow minute hand from Bitmap
                    matrix = new Matrix();
                    matrix.setRotate(minRot / (float) Math.PI * 180, mMinuteHandGlovesBitmapScaled.getWidth() / 2, mMinuteHandGlovesBitmapScaled.getHeight() / 2);
                    canvas.drawBitmap(mMinuteHandGlovesBitmapScaled, matrix, mHandPaint);
                }
                else {
                    // hour hand
                    float hrX = (float) Math.sin(hrRot) * hrLength;
                    float hrY = (float) -Math.cos(hrRot) * hrLength;
                    canvas.drawLine(centerX, centerY, centerX + hrX, centerY + hrY, mHandPaint);

                    // minute hand
                    float minX = (float) Math.sin(minRot) * minLength;
                    float minY = (float) -Math.cos(minRot) * minLength;
                    canvas.drawLine(centerX, centerY, centerX + minX, centerY + minY, mHandPaint);
                }
            }
            else if (! mAmbient || (mAmbient && holder.useContinuousOn())) {
                if (holder.useStandardFace()) {
                    final float minutesRot = mCalendar.get(Calendar.MINUTE) * 6f;
                    final float hourHandOffset = mCalendar.get(Calendar.MINUTE) / 2f;
                    final float hourRot = (mCalendar.get(Calendar.HOUR) * 30) + hourHandOffset;
                    // save the canvas state before we can begin to rotate it.
                    canvas.save();
                    canvas.rotate(hourRot, centerX, centerY);
                    // draw hour hand
                    canvas.drawLine(
                            centerX,
                            centerY - CENTER_GAP_AND_CIRCLE_RADIUS,
                            centerX,
                            centerY - hrLength,
                            (holder.useGoldInlay()) ? mHandPaintGold : mHandPaintBright);
                    canvas.rotate(minutesRot - hourRot, centerX, centerY);
                    // draw minute hand
                    canvas.drawLine(
                            centerX,
                            centerY - CENTER_GAP_AND_CIRCLE_RADIUS,
                            centerX,
                            centerY - minLength,
                            (holder.useGoldInlay()) ? mHandPaintGold : mHandPaintBright);
                    // draw pivot point
                    canvas.drawCircle(
                            centerX,
                            centerY,
                            CENTER_GAP_AND_CIRCLE_RADIUS,
                            mHandPaintJoint);
                    canvas.restore();
                } else {
                    // hour hand from Bitmap
                    Matrix matrix = new Matrix();
                    matrix.setRotate(hrRot / (float) Math.PI * 180, mHourHandBitmapScaled.getWidth() / 2, mHourHandBitmapScaled.getHeight() / 2);
                    canvas.drawBitmap(mHourHandBitmapScaled, matrix, mHandPaint);

                    // minute hand from Bitmap
                    matrix = new Matrix();
                    matrix.setRotate(minRot / (float) Math.PI * 180, mMinuteHandBitmapScaled.getWidth() / 2, mMinuteHandBitmapScaled.getHeight() / 2);
                    canvas.drawBitmap(mMinuteHandBitmapScaled, matrix, mHandPaint);
                }
            }

            if (!mAmbient && !mIsDeviceMuted && mBatteryLevel > 0) {
                float secRot;
                float seconds;
                if ((holder.usePreciousStones() && holder.useGoldInlay()) || holder.useHypnosis()) {
                    seconds = (mCalendar.get(Calendar.SECOND) + mCalendar.get(Calendar.MILLISECOND) / 1000f); // calculate for sweeping second hand
                } else {
                    seconds = mCalendar.get(Calendar.SECOND); // calculate for ticking second hand
                }
                secRot = seconds / 30f * (float) Math.PI;
                if (holder.useHypnosis()) {
                    // second hand from Bitmap
                    Matrix matrix = new Matrix();
                    matrix.setRotate(secRot / (float) Math.PI * 180, mSecondHandBitmapScaled.getWidth() / 2, mSecondHandBitmapScaled.getHeight() / 2);
                    canvas.drawBitmap(mSecondHandBitmapScaled, matrix, mHandPaint);
                } else if (useSecondHand) {
                    // second hand
                    float fullSecLength = minLength + (hrLength / 6);
                    // now shorten the length based on battery percentage remaining
                    float secLength = (fullSecLength * mBatteryLevel) / 100;
                    float secX = (float) Math.sin(secRot) * secLength;
                    float secY = (float) -Math.cos(secRot) * secLength;
                    float fullSecX = (float) Math.sin(secRot) * fullSecLength;
                    float fullSecY = (float) -Math.cos(secRot) * fullSecLength;
                    // we need to adjust the draw order to avoid a shadow appearing at the segment joint
                    if (mCalendar.get(Calendar.SECOND) >= 35 || mCalendar.get(Calendar.SECOND) <= 5) {
                        // draw the second hand battery segment using accent color
                        canvas.drawLine(centerX + secX, centerY + secY, centerX + fullSecX, centerY + fullSecY, mHandPaintAccent);
                        // draw the second hand base segment using normal color
                        canvas.drawLine(centerX, centerY, centerX + secX, centerY + secY, mHandPaintShadow);
                    } else {
                        // draw the second hand base segment using normal color
                        canvas.drawLine(centerX, centerY, centerX + secX, centerY + secY, mHandPaintShadow);
                        // draw the second hand battery segment using accent color
                        canvas.drawLine(centerX + secX, centerY + secY, centerX + fullSecX, centerY + fullSecY, mHandPaintAccent);
                    }
                    canvas.drawCircle(
                            centerX,
                            centerY,
                            CENTER_GAP_AND_CIRCLE_RADIUS,
                            mHandPaintJoint);
                }

                float text_Y_position = 55.0f;
                String padding = "        ";
                if (bounds.width() < 480) {
                    text_Y_position = 65.0f;
                    padding = "  ";
                }
                String high = formatTemperature(holder.getHighTemp());
                String low = formatTemperature(holder.getLowTemp());
                canvas.drawText(high, centerX - mHandPaintTempHigh.measureText(high + padding), text_Y_position, mHandPaintTempHigh);
                canvas.drawText(low, centerX + mHandPaintTempLow.measureText(padding), text_Y_position, mHandPaintTempLow);

                text_Y_position = 88.0f;
                int currentDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
                String theDate = mWeekdays[currentDayOfWeek - 1] + ", " + mDateFormat.format(date);
                canvas.drawText(theDate, centerX - mHandPaintDate.measureText(theDate)/2, text_Y_position, mHandPaintDate);
            }

            if ((holder.usePreciousStones() && holder.useGoldInlay()) || holder.useHypnosis()) {
                // Draw every frame as long as we're visible and in interactive mode.
                if (isVisible() && !mAmbient && !mIsDeviceMuted) {
                    invalidate(); // sweep the second hand
                }
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();
                // Update time zone in case it changed while we weren't visible.
                mCalendar.setTimeZone(TimeZone.getDefault());
                //Log.v(TAG, "connecting..");
                //WearTalkService.connect(getApplicationContext());
            }
            else {
                unregisterReceiver();
                //Log.v(TAG, "disconnecting..");
                //WearTalkService.disconnect();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            WeatherWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            WeatherWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }

    }

    // from: http://stackoverflow.com/questions/28938464/android-wear-watch-face-get-battery-percentage-of-phone
    private int getBatteryLevel()
    {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus =  registerReceiver(null, iFilter);
        if (batteryStatus == null) {
            return 0;
        }
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    }

    // this holder controls the wearable's appearance and behavior
    synchronized public static WatchFaceDesignHolder getWatchFaceDesignHolder() {
        if (sWatchFaceDesignHolder == null) {
            sWatchFaceDesignHolder = new WatchFaceDesignHolder();
        }
        return sWatchFaceDesignHolder;
    }

}
