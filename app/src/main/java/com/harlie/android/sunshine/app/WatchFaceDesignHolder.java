package com.harlie.android.sunshine.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;

// NOTE: this class needs to maintain Parcelable compatibility with the wear version.
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

    public WatchFaceDesignHolder(WatchFaceDesignHolder copy) {
        this.isDirty = copy.isDirty();
        this.isDaytime = copy.isDaytime();
        this.isSunshine = copy.isSunshine();
        this.isOvercast = copy.isOvercast();
        this.moonPhase = copy.getMoonPhase();
        this.highTemp = copy.getHighTemp();
        this.lowTemp = copy.getLowTemp();
        this.isMetric = copy.isMetric();
        this.isLightClouds = copy.isLightClouds();
        this.isModerateClouds = copy.isModerateClouds();
        this.isHeavyClouds = copy.isHeavyClouds();
        this.areCloudsDark = copy.areCloudsDark();
        this.areCloudsLow = copy.areCloudsLow();
        this.isLightRain = copy.isLightRain();
        this.isModerateRain = copy.isModerateRain();
        this.isHeavyRain = copy.isHeavyRain();
        this.isLightSnow = copy.isLightSnow();
        this.isModerateSnow = copy.isModerateSnow();
        this.isHeavySnow = copy.isHeavySnow();
        this.isLightWind = copy.isLightWind();
        this.isModerateWind = copy.isModerateWind();
        this.isHeavyWind = copy.isHeavyWind();
        this.isLightStorm = copy.isLightStorm();
        this.isModerateStorm = copy.isModerateStorm();
        this.isHeavyStorm = copy.isHeavyStorm();
    }

    public boolean isDirty() {
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

    public boolean areCloudsDark() {
        return areCloudsDark;
    }

    public void setAreCloudsDark(boolean areCloudsDark) {
        Log.v(TAG, "setAreCloudsDark: "+areCloudsDark);
        this.areCloudsDark = areCloudsDark;
    }

    public boolean areCloudsLow() {
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

    public static final Creator<WatchFaceDesignHolder> CREATOR = new Creator<WatchFaceDesignHolder>() {
        @Override
        public WatchFaceDesignHolder createFromParcel(Parcel source) {
            return new WatchFaceDesignHolder(source);
        }

        @Override
        public WatchFaceDesignHolder[] newArray(int size) {
            return new WatchFaceDesignHolder[size];
        }
    };

    public DataMap toDataMap() {
        DataMap dmap = new DataMap();
        dmap.putBoolean("isDaytime", isDaytime());
        dmap.putBoolean("isSunshine", isSunshine());
        dmap.putBoolean("isOvercast", isOvercast());
        dmap.putInt("moonPhase", getMoonPhase());
        dmap.putInt("highTemp", getHighTemp());
        dmap.putInt("lowTemp", getLowTemp());
        dmap.putBoolean("isMetric", isMetric());
        dmap.putBoolean("isLightClouds", isLightClouds());
        dmap.putBoolean("isModerateClouds", isModerateClouds());
        dmap.putBoolean("isHeavyClouds", isHeavyClouds());
        dmap.putBoolean("areCloudsDark", areCloudsDark());
        dmap.putBoolean("areCloudsLow", areCloudsLow());
        dmap.putBoolean("isLightRain", isLightRain());
        dmap.putBoolean("isModerateRain", isModerateRain());
        dmap.putBoolean("isHeavyRain", isHeavyRain());
        dmap.putBoolean("isLightSnow", isLightSnow());
        dmap.putBoolean("isModerateSnow", isModerateSnow());
        dmap.putBoolean("isHeavySnow", isHeavySnow());
        dmap.putBoolean("isLightWind", isLightWind());
        dmap.putBoolean("isModerateWind", isModerateWind());
        dmap.putBoolean("isHeavyWind", isHeavyWind());
        dmap.putBoolean("isLightStorm", isLightStorm());
        dmap.putBoolean("isModerateStorm", isModerateStorm());
        dmap.putBoolean("isHeavyStorm", isHeavyStorm());
        return dmap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WatchFaceDesignHolder that = (WatchFaceDesignHolder) o;

        if (isDirty() != that.isDirty()) return false;
        if (isDaytime() != that.isDaytime()) return false;
        if (isSunshine() != that.isSunshine()) return false;
        if (isOvercast() != that.isOvercast()) return false;
        if (getMoonPhase() != that.getMoonPhase()) return false;
        if (getHighTemp() != that.getHighTemp()) return false;
        if (getLowTemp() != that.getLowTemp()) return false;
        if (isMetric() != that.isMetric()) return false;
        if (isLightClouds() != that.isLightClouds()) return false;
        if (isModerateClouds() != that.isModerateClouds()) return false;
        if (isHeavyClouds() != that.isHeavyClouds()) return false;
        if (areCloudsDark != that.areCloudsDark) return false;
        if (areCloudsLow != that.areCloudsLow) return false;
        if (isLightRain() != that.isLightRain()) return false;
        if (isModerateRain() != that.isModerateRain()) return false;
        if (isHeavyRain() != that.isHeavyRain()) return false;
        if (isLightSnow() != that.isLightSnow()) return false;
        if (isModerateSnow() != that.isModerateSnow()) return false;
        if (isHeavySnow() != that.isHeavySnow()) return false;
        if (isLightWind() != that.isLightWind()) return false;
        if (isModerateWind() != that.isModerateWind()) return false;
        if (isHeavyWind() != that.isHeavyWind()) return false;
        if (isLightStorm() != that.isLightStorm()) return false;
        if (isModerateStorm() != that.isModerateStorm()) return false;
        return isHeavyStorm() == that.isHeavyStorm();

    }

    @Override
    public int hashCode() {
        int result = (isDirty() ? 1 : 0);
        result = 31 * result + (isDaytime() ? 1 : 0);
        result = 31 * result + (isSunshine() ? 1 : 0);
        result = 31 * result + (isOvercast() ? 1 : 0);
        result = 31 * result + getMoonPhase();
        result = 31 * result + getHighTemp();
        result = 31 * result + getLowTemp();
        result = 31 * result + (isMetric() ? 1 : 0);
        result = 31 * result + (isLightClouds() ? 1 : 0);
        result = 31 * result + (isModerateClouds() ? 1 : 0);
        result = 31 * result + (isHeavyClouds() ? 1 : 0);
        result = 31 * result + (areCloudsDark ? 1 : 0);
        result = 31 * result + (areCloudsLow ? 1 : 0);
        result = 31 * result + (isLightRain() ? 1 : 0);
        result = 31 * result + (isModerateRain() ? 1 : 0);
        result = 31 * result + (isHeavyRain() ? 1 : 0);
        result = 31 * result + (isLightSnow() ? 1 : 0);
        result = 31 * result + (isModerateSnow() ? 1 : 0);
        result = 31 * result + (isHeavySnow() ? 1 : 0);
        result = 31 * result + (isLightWind() ? 1 : 0);
        result = 31 * result + (isModerateWind() ? 1 : 0);
        result = 31 * result + (isHeavyWind() ? 1 : 0);
        result = 31 * result + (isLightStorm() ? 1 : 0);
        result = 31 * result + (isModerateStorm() ? 1 : 0);
        result = 31 * result + (isHeavyStorm() ? 1 : 0);
        return result;
    }
}
