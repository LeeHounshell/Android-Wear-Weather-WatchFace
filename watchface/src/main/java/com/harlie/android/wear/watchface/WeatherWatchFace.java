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

package com.harlie.android.wear.watchface;

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
import android.view.SurfaceHolder;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import me.denley.preferencebinder.PreferenceBinder;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't shown. On
 * devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient mode.
 */
public class WeatherWatchFace extends CanvasWatchFaceService {
    private final String TAG = "LEE: <" + WeatherWatchFace.class.getSimpleName() + ">";
    /**
     * Update rate in milliseconds for interactive mode. We update once a second to advance the
     * second hand.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    private WatchFaceDesignHolder mWatchFaceDesignHolder;
    private static Context mContext;

    @Override
    public Engine onCreateEngine() {
        mContext = this.getApplicationContext();
        return new Engine();
    }

    public static Context getContext() {
        return mContext;
    }

    private static class EngineHandler extends Handler {
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

    private class Engine extends CanvasWatchFaceService.Engine {
        private final String TAG = "LEE: <" + Engine.class.getSimpleName() + ">";

        private final float CENTER_GAP_AND_CIRCLE_RADIUS = 4f;

        final Handler mUpdateTimeHandler = new EngineHandler(this);
        boolean mRegisteredTimeZoneReceiver = false;
        Bitmap mBackgroundBitmap;
        Bitmap mBackgroundAmbientBitmap;
        Bitmap mBackgroundAmbientGoldBitmap;
        Bitmap mHourHandBitmap;
        Bitmap mMinuteHandBitmap;
        Bitmap mBackgroundBitmapScaled;
        Bitmap mBackgroundAmbientBitmapScaled;
        Bitmap mBackgroundAmbientGoldBitmapScaled;
        Bitmap mHourHandBitmapScaled;
        Bitmap mMinuteHandBitmapScaled;
        Paint mBackgroundPaint;
        Paint mHandPaint;
        Paint mHandPaintShadow;
        Paint mHandPaintAccent;
        Paint mHandPaintBright;
        Paint mHandPaintGold;
        Paint mHandPaintJoint;
        boolean mAmbient;
        boolean mDaylightChanged;
        boolean mIsRound;
        boolean mIsJewelStudded;
        Calendar mCalendar;
        int mBatteryLevel;
        int mTapCount;
        int mHeight;
        int mWidth;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;

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
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());

            mWatchFaceDesignHolder = new WatchFaceDesignHolder();

            PreferenceBinder.bind(getContext(), mWatchFaceDesignHolder);

            // calculate current phase of the moon
            MoonCalculation moonCalculaion = new MoonCalculation();
            mCalendar = Calendar.getInstance();
            Date date = new Date();
            mCalendar.setTime(date);
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH) + 1;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            Log.v(TAG, "year="+year+", month="+month+", day="+day);
            mWatchFaceDesignHolder.setMoonPhase(moonCalculaion.moonPhase(year, month, day));

            mHourHandBitmap = drawableToBitmap(getDrawable(R.drawable.hour_little_hand));
            mMinuteHandBitmap = drawableToBitmap(getDrawable(R.drawable.minute_big_hand));

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

            // calculate if day or night
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            mWatchFaceDesignHolder.setDaytime((hour >= 6 && hour < 18));

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
        }

        private void createWatchFaceBitmaps() {
            Log.v(TAG, "createWatchBitmaps");
            mBackgroundBitmap = createWatchBackgroundBitmap(mWatchFaceDesignHolder);
            scaleWatchFace(mWidth, mHeight);
        }

        private Bitmap createWatchBackgroundBitmap(WatchFaceDesignHolder watchFaceDesignHolder) {
            Log.v(TAG, "createWatchBackgroundBitmap");
            mWatchFaceDesignHolder.setDirty(false);
            if (watchFaceDesignHolder == null) {
                Log.v(TAG, "use default clock_face");
                mBackgroundBitmap = drawableToBitmap(getDrawable(R.drawable.clock_face));
                return mBackgroundBitmap;
            }
            Bitmap overlay;
            if (mWatchFaceDesignHolder.useStaticBackground()) {
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
                if (mWatchFaceDesignHolder.useStandardFace()) {
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
            if (mWatchFaceDesignHolder.useRomanNumeralsFace()) {
                if (mWatchFaceDesignHolder.useGoldInlay()) {
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
                if (mWatchFaceDesignHolder.useGoldInlay() && mWatchFaceDesignHolder.usePreciousStones()) {
                    mIsJewelStudded = true;
                    overlay = drawableToBitmap(getDrawable(R.drawable.clock_face_diamond));
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    Log.v(TAG, "clock_face_diamond");
                }
                else if (mWatchFaceDesignHolder.usePreciousStones()) {
                    mIsJewelStudded = true;
                    overlay = drawableToBitmap(getDrawable(R.drawable.clock_face_ruby));
                    mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                    Log.v(TAG, "clock_face_ruby");
                }
                else if (mWatchFaceDesignHolder.useGoldInlay()) {
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
            if (! mWatchFaceDesignHolder.useSecondHand() || mAmbient || mIsJewelStudded) {
                Log.v(TAG, "tickmarks_none");
            }
            else if (! mWatchFaceDesignHolder.useStandardFace()
                    && ! mWatchFaceDesignHolder.useRomanNumeralsFace()
                    && mWatchFaceDesignHolder.useGoldInlay()
                    && mWatchFaceDesignHolder.usePreciousStones())
            {
                Log.v(TAG, "tickmarks_none");
            }
            else if (mWatchFaceDesignHolder.usePreciousStones() && ! mWatchFaceDesignHolder.useGoldInlay()) {
                overlay = drawableToBitmap(getDrawable(R.drawable.tickmarks_plain));
                mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                Log.v(TAG, "tickmarks_plain");
            }
            else if (mWatchFaceDesignHolder.usePreciousStones()) {
                overlay = drawableToBitmap(getDrawable(R.drawable.tickmarks_ivory));
                mBackgroundBitmap = combineImages(overlay, mBackgroundBitmap);
                Log.v(TAG, "precious_stones - tickmarks_ivory");
            }
            else if (! mWatchFaceDesignHolder.useRomanNumeralsFace() && ! mWatchFaceDesignHolder.useGoldInlay()) {
                Log.v(TAG, "tickmarks_none");
            }
            else if (! mWatchFaceDesignHolder.useRomanNumeralsFace()) {
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
            super.onDestroy();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            Log.v(TAG, "onPropertiesChanged");
            super.onPropertiesChanged(properties);
            if (mWatchFaceDesignHolder != null) {
                mWatchFaceDesignHolder.setDirty(true); // the watch face is out of sync now
            }
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
            Date date = new Date();
            mCalendar.setTime(date);
            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            boolean wasDaytime = mWatchFaceDesignHolder.isDaytime();
            mWatchFaceDesignHolder.setDaytime((hour >= 6 && hour < 18));
            if (wasDaytime !=  mWatchFaceDesignHolder.isDaytime()) {
                mWatchFaceDesignHolder.setDirty(true); // the watch face is out of sync now
            }
            mBatteryLevel = getBatteryLevel();
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
                mWatchFaceDesignHolder.setDirty(true);
                invalidate();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
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
            mMinuteHandBitmapScaled = Bitmap.createScaledBitmap(mMinuteHandBitmap,
                    (int) (mMinuteHandBitmap.getWidth() * ratio),
                    (int) (mMinuteHandBitmap.getHeight() * ratio), true);
            mHourHandBitmapScaled = Bitmap.createScaledBitmap(mHourHandBitmap,
                    (int) (mHourHandBitmap.getWidth() * ratio),
                    (int) (mHourHandBitmap.getHeight() * ratio), true);
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

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            Date date = new Date();
            mCalendar.setTime(date);

            boolean useSecondHand = mWatchFaceDesignHolder.useSecondHand();
            boolean ambientOverride = mWatchFaceDesignHolder.useContinuousOn();
            boolean realAmbientMode = (mAmbient && ! ambientOverride);

            if (mWatchFaceDesignHolder.isDirty()) {
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
                if (mWatchFaceDesignHolder.useGoldInlay()) {
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
                // hour hand
                float hrX = (float) Math.sin(hrRot) * hrLength;
                float hrY = (float) -Math.cos(hrRot) * hrLength;
                canvas.drawLine(centerX, centerY, centerX + hrX, centerY + hrY, mHandPaint);

                // minute hand
                float minX = (float) Math.sin(minRot) * minLength;
                float minY = (float) -Math.cos(minRot) * minLength;
                canvas.drawLine(centerX, centerY, centerX + minX, centerY + minY, mHandPaint);
            }
            else if (! mAmbient || (mAmbient && mWatchFaceDesignHolder.useContinuousOn())) {
                if (mWatchFaceDesignHolder.useStandardFace()) {
                    final float minutesRotation = mCalendar.get(Calendar.MINUTE) * 6f;
                    final float hourHandOffset = mCalendar.get(Calendar.MINUTE) / 2f;
                    final float hoursRotation = (mCalendar.get(Calendar.HOUR) * 30) + hourHandOffset;
                    // save the canvas state before we can begin to rotate it.
                    canvas.save();
                    canvas.rotate(hoursRotation, centerX, centerY);
                    canvas.drawLine(
                            centerX,
                            centerY - CENTER_GAP_AND_CIRCLE_RADIUS,
                            centerX,
                            centerY - hrLength,
                            (mWatchFaceDesignHolder.useGoldInlay()) ? mHandPaintGold : mHandPaintBright);
                    canvas.rotate(minutesRotation - hoursRotation, centerX, centerY);
                    canvas.drawLine(
                            centerX,
                            centerY - CENTER_GAP_AND_CIRCLE_RADIUS,
                            centerX,
                            centerY - minLength,
                            (mWatchFaceDesignHolder.useGoldInlay()) ? mHandPaintGold : mHandPaintBright);
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
                    matrix.setRotate(minRot / (float) Math.PI * 180, mHourHandBitmapScaled.getWidth() / 2, mHourHandBitmapScaled.getHeight() / 2);
                    canvas.drawBitmap(mMinuteHandBitmapScaled, matrix, mHandPaint);
                }
            }

            if (useSecondHand && ! mAmbient && mBatteryLevel > 0) {
                // second hand
                float secRot = mCalendar.get(Calendar.SECOND) / 30f * (float) Math.PI;
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
                }
                else {
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
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mCalendar.setTimeZone(TimeZone.getDefault());
            } else {
                unregisterReceiver();
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

}
