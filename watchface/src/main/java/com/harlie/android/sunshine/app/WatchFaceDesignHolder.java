package com.harlie.android.sunshine.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;

import me.denley.preferencebinder.BindPref;
import me.denley.preferencebinder.PreferenceDefault;

// NOTE: this class needs to maintain Parcelable compatibility with the app version.
public class WatchFaceDesignHolder implements Parcelable {
    private static final String TAG = "LEE: <" + WatchFaceDesignHolder.class.getSimpleName() + ">";

    private boolean isDirty;
    private boolean isDaytime;
    private boolean isSunshine;
    private boolean isOvercast;
    private int moonPhase;
    private int highTemp;
    private int lowTemp;
    private boolean isMetric;
    private boolean isLightClouds;
    private boolean isModerateClouds;
    private boolean isHeavyClouds;
    private boolean areCloudsDark;
    private boolean areCloudsLow;
    private boolean isLightRain;
    private boolean isModerateRain;
    private boolean isHeavyRain;
    private boolean isLightSnow;
    private boolean isModerateSnow;
    private boolean isHeavySnow;
    private boolean isLightWind;
    private boolean isModerateWind;
    private boolean isHeavyWind;
    private boolean isLightStorm;
    private boolean isModerateStorm;
    private boolean isHeavyStorm;

    public boolean isDirty() {
        // check/mark dirty before returning..
        useSecondHand();
        useStandardFace();
        useRomanNumeralsFace();
        useGoldInlay();
        usePreciousStones();
        useStaticBackground();
        useContinuousOn();
        useHypnosis();
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        //Log.v(TAG, "setDirty: "+dirty);
        isDirty = dirty;
    }

    public boolean isDaytime() {
        return isDaytime;
    }

    public void setDaytime(boolean daytime) {
        //Log.v(TAG, "setDaytime: "+daytime);
        isDaytime = daytime;
    }

    public boolean isSunshine() {
        return isSunshine;
    }

    public void setSunshine(boolean sunshine) {
        Log.v(TAG, "setSunshine: "+sunshine);
        isSunshine = sunshine;
    }

    public boolean isOvercast() {
        return isOvercast;
    }

    public void setOvercast(boolean overcast) {
        Log.v(TAG, "setOvercast: "+overcast);
        isOvercast = overcast;
    }

    public int getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(int moonPhase) {
        Log.v(TAG, "setMoonPhase: "+moonPhase);
        this.moonPhase = moonPhase;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        Log.v(TAG, "setHighTemp: "+highTemp);
        this.highTemp = highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        Log.v(TAG, "setLowTemp: "+lowTemp);
        this.lowTemp = lowTemp;
    }

    public boolean isMetric() {
        return isMetric;
    }

    public void setMetric(boolean metric) {
        Log.v(TAG, "setMetric: "+metric);
        isMetric = metric;
    }

    public boolean isLightClouds() {
        return isLightClouds;
    }

    public void setLightClouds(boolean lightClouds) {
        Log.v(TAG, "setLightClouds: "+lightClouds);
        isLightClouds = lightClouds;
    }

    public boolean isModerateClouds() {
        return isModerateClouds;
    }

    public void setModerateClouds(boolean moderateClouds) {
        Log.v(TAG, "setModerateClouds: "+moderateClouds);
        isModerateClouds = moderateClouds;
    }

    public boolean isHeavyClouds() {
        return isHeavyClouds;
    }

    public void setHeavyClouds(boolean heavyClouds) {
        Log.v(TAG, "setHeavyClouds: "+heavyClouds);
        isHeavyClouds = heavyClouds;
    }

    public boolean isAreCloudsDark() {
        return areCloudsDark;
    }

    public void setAreCloudsDark(boolean areCloudsDark) {
        Log.v(TAG, "setAreCloudsDark: "+areCloudsDark);
        this.areCloudsDark = areCloudsDark;
    }

    public boolean isAreCloudsLow() {
        return areCloudsLow;
    }

    public void setAreCloudsLow(boolean areCloudsLow) {
        Log.v(TAG, "setAreCloudsLow: "+areCloudsLow);
        this.areCloudsLow = areCloudsLow;
    }

    public boolean isLightRain() {
        return isLightRain;
    }

    public void setLightRain(boolean lightRain) {
        Log.v(TAG, "setLightRain: "+lightRain);
        isLightRain = lightRain;
    }

    public boolean isModerateRain() {
        return isModerateRain;
    }

    public void setModerateRain(boolean moderateRain) {
        Log.v(TAG, "setModerateRain: "+moderateRain);
        isModerateRain = moderateRain;
    }

    public boolean isHeavyRain() {
        return isHeavyRain;
    }

    public void setHeavyRain(boolean heavyRain) {
        Log.v(TAG, "setHeavyRain: "+heavyRain);
        isHeavyRain = heavyRain;
    }

    public boolean isLightSnow() {
        return isLightSnow;
    }

    public void setLightSnow(boolean lightSnow) {
        Log.v(TAG, "setLightSnow: "+lightSnow);
        isLightSnow = lightSnow;
    }

    public boolean isModerateSnow() {
        return isModerateSnow;
    }

    public void setModerateSnow(boolean moderateSnow) {
        Log.v(TAG, "setModerateSnow: "+moderateSnow);
        isModerateSnow = moderateSnow;
    }

    public boolean isHeavySnow() {
        return isHeavySnow;
    }

    public void setHeavySnow(boolean heavySnow) {
        Log.v(TAG, "setHeavySnow: "+heavySnow);
        isHeavySnow = heavySnow;
    }

    public boolean isLightWind() {
        return isLightWind;
    }

    public void setLightWind(boolean lightWind) {
        Log.v(TAG, "setLightWind: "+lightWind);
        isLightWind = lightWind;
    }

    public boolean isModerateWind() {
        return isModerateWind;
    }

    public void setModerateWind(boolean moderateWind) {
        Log.v(TAG, "setModerateWind: "+moderateWind);
        isModerateWind = moderateWind;
    }

    public boolean isHeavyWind() {
        return isHeavyWind;
    }

    public void setHeavyWind(boolean heavyWind) {
        Log.v(TAG, "setHeavyWind: "+heavyWind);
        isHeavyWind = heavyWind;
    }

    public boolean isLightStorm() {
        return isLightStorm;
    }

    public void setLightStorm(boolean lightStorm) {
        Log.v(TAG, "setLightStorm: "+lightStorm);
        isLightStorm = lightStorm;
    }

    public boolean isModerateStorm() {
        return isModerateStorm;
    }

    public void setModerateStorm(boolean moderateStorm) {
        Log.v(TAG, "setModerateStorm: "+moderateStorm);
        isModerateStorm = moderateStorm;
    }

    public boolean isHeavyStorm() {
        return isHeavyStorm;
    }

    public void setHeavyStorm(boolean heavyStorm) {
        Log.v(TAG, "setHeavyStorm: "+heavyStorm);
        isHeavyStorm = heavyStorm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isDirty ? (byte) 1 : (byte) 0);
        dest.writeByte(isDaytime ? (byte) 1 : (byte) 0);
        dest.writeByte(isSunshine ? (byte) 1 : (byte) 0);
        dest.writeByte(isOvercast ? (byte) 1 : (byte) 0);
        dest.writeInt(this.moonPhase);
        dest.writeInt(this.highTemp);
        dest.writeInt(this.lowTemp);
        dest.writeByte(isMetric ? (byte) 1 : (byte) 0);
        dest.writeByte(isLightClouds ? (byte) 1 : (byte) 0);
        dest.writeByte(isModerateClouds ? (byte) 1 : (byte) 0);
        dest.writeByte(isHeavyClouds ? (byte) 1 : (byte) 0);
        dest.writeByte(areCloudsDark ? (byte) 1 : (byte) 0);
        dest.writeByte(areCloudsLow ? (byte) 1 : (byte) 0);
        dest.writeByte(isLightRain ? (byte) 1 : (byte) 0);
        dest.writeByte(isModerateRain ? (byte) 1 : (byte) 0);
        dest.writeByte(isHeavyRain ? (byte) 1 : (byte) 0);
        dest.writeByte(isLightSnow ? (byte) 1 : (byte) 0);
        dest.writeByte(isModerateSnow ? (byte) 1 : (byte) 0);
        dest.writeByte(isHeavySnow ? (byte) 1 : (byte) 0);
        dest.writeByte(isLightWind ? (byte) 1 : (byte) 0);
        dest.writeByte(isModerateWind ? (byte) 1 : (byte) 0);
        dest.writeByte(isHeavyWind ? (byte) 1 : (byte) 0);
        dest.writeByte(isLightStorm ? (byte) 1 : (byte) 0);
        dest.writeByte(isModerateStorm ? (byte) 1 : (byte) 0);
        dest.writeByte(isHeavyStorm ? (byte) 1 : (byte) 0);
    }

    public WatchFaceDesignHolder() {
    }

    protected WatchFaceDesignHolder(Parcel in) {
        this.isDirty = in.readByte() != 0;
        this.isDaytime = in.readByte() != 0;
        this.isSunshine = in.readByte() != 0;
        this.isOvercast = in.readByte() != 0;
        this.moonPhase = in.readInt();
        this.highTemp = in.readInt();
        this.lowTemp = in.readInt();
        this.isMetric = in.readByte() != 0;
        this.isLightClouds = in.readByte() != 0;
        this.isModerateClouds = in.readByte() != 0;
        this.isHeavyClouds = in.readByte() != 0;
        this.areCloudsDark = in.readByte() != 0;
        this.areCloudsLow = in.readByte() != 0;
        this.isLightRain = in.readByte() != 0;
        this.isModerateRain = in.readByte() != 0;
        this.isHeavyRain = in.readByte() != 0;
        this.isLightSnow = in.readByte() != 0;
        this.isModerateSnow = in.readByte() != 0;
        this.isHeavySnow = in.readByte() != 0;
        this.isLightWind = in.readByte() != 0;
        this.isModerateWind = in.readByte() != 0;
        this.isHeavyWind = in.readByte() != 0;
        this.isLightStorm = in.readByte() != 0;
        this.isModerateStorm = in.readByte() != 0;
        this.isHeavyStorm = in.readByte() != 0;
    }

    public static final Parcelable.Creator<WatchFaceDesignHolder> CREATOR = new Parcelable.Creator<WatchFaceDesignHolder>() {
        @Override
        public WatchFaceDesignHolder createFromParcel(Parcel source) {
            return new WatchFaceDesignHolder(source);
        }

        @Override
        public WatchFaceDesignHolder[] newArray(int size) {
            return new WatchFaceDesignHolder[size];
        }
    };

    // the watchface design also includes shared preference values (not part of the Parcelable)
    // we use the Denley preferencebinder code injection framework to handle those values.
    // from: https://github.com/denley/preferencebinder

    @PreferenceDefault("use_static_background") public static boolean STATICBACKGROUND_PREFERENCE_DEFAULT = false;
    @BindPref(value = "use_static_background")
    boolean useStaticBackground = STATICBACKGROUND_PREFERENCE_DEFAULT;
    boolean useStaticBackgroundOldValue = !STATICBACKGROUND_PREFERENCE_DEFAULT;

    public boolean useStaticBackground() {
        if (useStaticBackgroundOldValue != useStaticBackground) {
            useStaticBackgroundOldValue = useStaticBackground;
            setDirty(true);
        }
        return useStaticBackground;
    }

    @PreferenceDefault("use_secondhand") public static boolean SECONDHAND_PREFERENCE_DEFAULT = true;
    @BindPref(value = "use_secondhand")
    boolean useSecondHand = SECONDHAND_PREFERENCE_DEFAULT;
    boolean useSecondHandOldValue = !SECONDHAND_PREFERENCE_DEFAULT;

    public boolean useSecondHand() {
        if (useSecondHandOldValue != useSecondHand) {
            useSecondHandOldValue = useSecondHand;
            setDirty(true);
        }
        return useSecondHand;
    }

    @PreferenceDefault("use_standardface") public static boolean STANDARDFACE_PREFERENCE_DEFAULT = false;
    @BindPref(value = "use_standardface")
    boolean useStandardFace = STANDARDFACE_PREFERENCE_DEFAULT;
    boolean useStandardFaceOldValue = !STANDARDFACE_PREFERENCE_DEFAULT;

    public boolean useStandardFace() {
        if (useStandardFaceOldValue != useStandardFace) {
            useStandardFaceOldValue = useStandardFace;
            setDirty(true);
        }
        return useStandardFace;
    }

    @PreferenceDefault("use_roman_numerals") public static boolean ROMANFACE_PREFERENCE_DEFAULT = false;
    @BindPref(value = "use_roman_numerals")
    boolean useRomanNumeralsFace = ROMANFACE_PREFERENCE_DEFAULT;
    boolean useRomanNumeralsOldValue = !ROMANFACE_PREFERENCE_DEFAULT;

    public boolean useRomanNumeralsFace() {
        if (useRomanNumeralsOldValue != useRomanNumeralsFace) {
            useRomanNumeralsOldValue = useRomanNumeralsFace;
            setDirty(true);
        }
        return useRomanNumeralsFace;
    }

    @PreferenceDefault("use_gold_inlay") public static boolean GOLDINLAY_PREFERENCE_DEFAULT = false;
    @BindPref(value = "use_gold_inlay")
    boolean useGoldInlay = GOLDINLAY_PREFERENCE_DEFAULT;
    boolean useGoldInlayOldValue = !GOLDINLAY_PREFERENCE_DEFAULT;

    public boolean useGoldInlay() {
        if (useGoldInlayOldValue != useGoldInlay) {
            useGoldInlayOldValue = useGoldInlay;
            setDirty(true);
        }
        return useGoldInlay;
    }

    @PreferenceDefault("use_precious_stones") public static boolean PRECIOUSSTONES_PREFERENCE_DEFAULT = false;
    @BindPref(value = "use_precious_stones")
    boolean usePreciousStones = PRECIOUSSTONES_PREFERENCE_DEFAULT;
    boolean usePreciousStonesOldValue = !PRECIOUSSTONES_PREFERENCE_DEFAULT;

    public boolean usePreciousStones() {
        if (usePreciousStonesOldValue != usePreciousStones) {
            usePreciousStonesOldValue = usePreciousStones;
            setDirty(true);
        }
        return usePreciousStones;
    }

    @PreferenceDefault("use_continuous_on") public static boolean CONTINUOUSON_PREFERENCE_DEFAULT = false;
    @BindPref(value = "use_continuous_on")
    boolean useContinuousOn = CONTINUOUSON_PREFERENCE_DEFAULT;
    boolean useContinuousOnOldValue = !CONTINUOUSON_PREFERENCE_DEFAULT;

    public boolean useContinuousOn() {
        if (useContinuousOnOldValue != useContinuousOn) {
            useContinuousOnOldValue = useContinuousOn;
            setDirty(true);
        }
        return useContinuousOn;
    }

    @PreferenceDefault("use_hypnosis") public static boolean HYPNOSIS_PREFERENCE_DEFAULT = false;
    @BindPref(value = "use_hypnosis")
    boolean useHypnosis = HYPNOSIS_PREFERENCE_DEFAULT;
    boolean useHypnosisOldValue = !HYPNOSIS_PREFERENCE_DEFAULT;

    public boolean useHypnosis() {
        if (useHypnosisOldValue != useHypnosis) {
            useHypnosisOldValue = useHypnosis;
            setDirty(true);
        }
        return useHypnosis;
    }

    @PreferenceDefault("contact_lee") public static boolean CONTACTLEE_PREFERENCE_DEFAULT = false;
    @BindPref(value = "contact_lee")
    boolean contactLee = CONTACTLEE_PREFERENCE_DEFAULT;
    boolean contactLeeOldValue = !CONTACTLEE_PREFERENCE_DEFAULT;

    public boolean contactLee() {
        if (contactLeeOldValue != contactLee) {
            contactLeeOldValue = contactLee;
        }
        return contactLee;
    }

    public void fromDataMap(DataMap dmap) {
        Log.v(TAG, "fromDataMap");
        //setDaytime(dmap.getBoolean("isDaytime")); // instead use the local calculation for daytime
        setSunshine(dmap.getBoolean("isSunshine"));
        setOvercast(dmap.getBoolean("isOvercast"));
        //setMoonPhase(dmap.getInt("moonPhase")); // instead use the local calculation for moon phase
        setMetric(dmap.getBoolean("isMetric"));
        if (isMetric()) {
            setHighTemp(dmap.getInt("highTemp"));
            setLowTemp(dmap.getInt("lowTemp"));
        }
        else {
            // convert to Fahrenheit
            int high = (dmap.getInt("highTemp") * 9/5) + 32;
            int low = (dmap.getInt("lowTemp") * 9/5) + 32;
            setHighTemp(high);
            setLowTemp(low);
        }
        setLightClouds(dmap.getBoolean("isLightClouds"));
        setModerateClouds(dmap.getBoolean("isModerateClouds"));
        setHeavyClouds(dmap.getBoolean("isHeavyClouds"));
        setAreCloudsDark(dmap.getBoolean("areCloudsDark"));
        setAreCloudsLow(dmap.getBoolean("areCloudsLow"));
        setLightRain(dmap.getBoolean("isLightRain"));
        setModerateRain(dmap.getBoolean("isModerateRain"));
        setHeavyRain(dmap.getBoolean("isHeavyRain"));
        setLightSnow(dmap.getBoolean("isLightSnow"));
        setModerateSnow(dmap.getBoolean("isModerateSnow"));
        setHeavySnow(dmap.getBoolean("isHeavySnow"));
        setLightWind(dmap.getBoolean("isLightWind"));
        setModerateWind(dmap.getBoolean("isModerateWind"));
        setHeavyWind(dmap.getBoolean("isHeavyWind"));
        setLightStorm(dmap.getBoolean("isLightStorm"));
        setModerateStorm(dmap.getBoolean("isModerateStorm"));
        setHeavyStorm(dmap.getBoolean("isHeavyStorm"));
        setDirty(true);
    }

    public DataMap watchConfiguration() {
        Log.v(TAG, "watchConfiguration");
        DataMap dmap = new DataMap();
        dmap.putBoolean("useStaticBackground", useStaticBackground());
        dmap.putBoolean("useSecondHand", useSecondHand());
        dmap.putBoolean("useStandardFace", useStandardFace());
        dmap.putBoolean("useRomanNumeralsFace", useRomanNumeralsFace());
        dmap.putBoolean("useGoldInlay", useGoldInlay());
        dmap.putBoolean("usePreciousStones", usePreciousStones());
        dmap.putBoolean("useContinuousOn", useContinuousOn());
        dmap.putBoolean("useHypnosis", useHypnosis());
        return dmap;
    }

}
