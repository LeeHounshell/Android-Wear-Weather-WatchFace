package com.harlie.android.wear.watchface;

import android.os.Parcel;
import android.os.Parcelable;

public class WatchFaceDesignHolder implements Parcelable {
    private boolean isDaytime;
    private boolean isSunshine;
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

    public boolean isDaytime() {
        return isDaytime;
    }

    public void setDaytime(boolean daytime) {
        isDaytime = daytime;
    }

    public boolean isSunshine() {
        return isSunshine;
    }

    public void setSunshine(boolean sunshine) {
        isSunshine = sunshine;
    }

    public int getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(int moonPhase) {
        this.moonPhase = moonPhase;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }

    public boolean isMetric() {
        return isMetric;
    }

    public void setMetric(boolean metric) {
        isMetric = metric;
    }

    public boolean isLightClouds() {
        return isLightClouds;
    }

    public void setLightClouds(boolean lightClouds) {
        isLightClouds = lightClouds;
    }

    public boolean isModerateClouds() {
        return isModerateClouds;
    }

    public void setModerateClouds(boolean moderateClouds) {
        isModerateClouds = moderateClouds;
    }

    public boolean isHeavyClouds() {
        return isHeavyClouds;
    }

    public void setHeavyClouds(boolean heavyClouds) {
        isHeavyClouds = heavyClouds;
    }

    public boolean isAreCloudsDark() {
        return areCloudsDark;
    }

    public void setAreCloudsDark(boolean areCloudsDark) {
        this.areCloudsDark = areCloudsDark;
    }

    public boolean isAreCloudsLow() {
        return areCloudsLow;
    }

    public void setAreCloudsLow(boolean areCloudsLow) {
        this.areCloudsLow = areCloudsLow;
    }

    public boolean isLightRain() {
        return isLightRain;
    }

    public void setLightRain(boolean lightRain) {
        isLightRain = lightRain;
    }

    public boolean isModerateRain() {
        return isModerateRain;
    }

    public void setModerateRain(boolean moderateRain) {
        isModerateRain = moderateRain;
    }

    public boolean isHeavyRain() {
        return isHeavyRain;
    }

    public void setHeavyRain(boolean heavyRain) {
        isHeavyRain = heavyRain;
    }

    public boolean isLightSnow() {
        return isLightSnow;
    }

    public void setLightSnow(boolean lightSnow) {
        isLightSnow = lightSnow;
    }

    public boolean isModerateSnow() {
        return isModerateSnow;
    }

    public void setModerateSnow(boolean moderateSnow) {
        isModerateSnow = moderateSnow;
    }

    public boolean isHeavySnow() {
        return isHeavySnow;
    }

    public void setHeavySnow(boolean heavySnow) {
        isHeavySnow = heavySnow;
    }

    public boolean isLightWind() {
        return isLightWind;
    }

    public void setLightWind(boolean lightWind) {
        isLightWind = lightWind;
    }

    public boolean isModerateWind() {
        return isModerateWind;
    }

    public void setModerateWind(boolean moderateWind) {
        isModerateWind = moderateWind;
    }

    public boolean isHeavyWind() {
        return isHeavyWind;
    }

    public void setHeavyWind(boolean heavyWind) {
        isHeavyWind = heavyWind;
    }

    public boolean isLightStorm() {
        return isLightStorm;
    }

    public void setLightStorm(boolean lightStorm) {
        isLightStorm = lightStorm;
    }

    public boolean isModerateStorm() {
        return isModerateStorm;
    }

    public void setModerateStorm(boolean moderateStorm) {
        isModerateStorm = moderateStorm;
    }

    public boolean isHeavyStorm() {
        return isHeavyStorm;
    }

    public void setHeavyStorm(boolean heavyStorm) {
        isHeavyStorm = heavyStorm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isDaytime ? (byte) 1 : (byte) 0);
        dest.writeByte(isSunshine ? (byte) 1 : (byte) 0);
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
        this.isDaytime = in.readByte() != 0;
        this.isSunshine = in.readByte() != 0;
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
}
