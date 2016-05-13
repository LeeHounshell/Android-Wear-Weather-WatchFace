package com.harlie.android.sunshine.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;
import com.harlie.android.sunshine.app.sync.WearTalkService;

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

    public void reset() {
        Log.v(TAG, "reset");
        setDirty(false);
        setHighTemp(0);
        setLowTemp(0);
        setMetric(Utility.isMetric(AnalyticsApplication.getInstance().getApplicationContext()));
        setAreCloudsDark(false);
        setAreCloudsLow(false);
        setLightClouds(false);
        setModerateClouds(false);
        setHeavyClouds(false);
        setLightRain(false);
        setModerateRain(false);
        setHeavyRain(false);
        setLightSnow(false);
        setModerateSnow(false);
        setHeavySnow(false);
        setLightWind(false);
        setModerateWind(false);
        setHeavyWind(false);
        setLightStorm(false);
        setModerateStorm(false);
        setHeavyStorm(false);
    }

    // OWM weather codes from: http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
    public static void updateWearWeather(double windSpeed, int high, int low, int weatherId) {
        Log.v(TAG, "---> summarize weather for today..");
        WatchFaceDesignHolder watchFaceDesignHolder = WearTalkService.getWatchFaceDesignHolder();
        watchFaceDesignHolder.reset();
        Log.v(TAG, "setHighTemp: "+high);
        watchFaceDesignHolder.setHighTemp(high);
        Log.v(TAG, "setLowTemp: "+low);
        watchFaceDesignHolder.setLowTemp(low);
        boolean metric = Utility.isMetric(AnalyticsApplication.getInstance().getApplicationContext());
        Log.v(TAG, "setMetric: "+metric);
        watchFaceDesignHolder.setMetric(metric);
        if (windSpeed >= 35) {
            Log.v(TAG, "windSpeed >= 35");
            watchFaceDesignHolder.setHeavyWind(true);
        }
        else if (windSpeed >= 20) {
            Log.v(TAG, "windSpeed >= 20");
            watchFaceDesignHolder.setModerateWind(true);
        }
        else if (windSpeed >= 5) {
            Log.v(TAG, "windSpeed >= 5");
            watchFaceDesignHolder.setLightWind(true);
        }
        switch (weatherId) {
            case 200: {
                Log.v(TAG, "thunderstorm with light rain");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setLightStorm(true);
                watchFaceDesignHolder.setLightClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                break;
            }
            case 201: {
                Log.v(TAG, "thunderstorm with rain");
                watchFaceDesignHolder.setModerateRain(true);
                watchFaceDesignHolder.setModerateStorm(true);
                watchFaceDesignHolder.setModerateClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                break;
            }
            case 202: {
                Log.v(TAG, "thunderstorm with heavy rain");
                watchFaceDesignHolder.setHeavyRain(true);
                watchFaceDesignHolder.setHeavyStorm(true);
                watchFaceDesignHolder.setHeavyClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                break;
            }
            case 210: {
                Log.v(TAG, "light thunderstorm");
                watchFaceDesignHolder.setLightStorm(true);
                watchFaceDesignHolder.setLightClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                break;
            }
            case 211: {
                Log.v(TAG, "thunderstorm");
                watchFaceDesignHolder.setModerateStorm(true);
                watchFaceDesignHolder.setModerateClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                break;
            }
            case 212: {
                Log.v(TAG, "heavy thunderstorm");
                watchFaceDesignHolder.setHeavyStorm(true);
                watchFaceDesignHolder.setHeavyClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                break;
            }
            case 221: {
                Log.v(TAG, "ragged thunderstorm");
                watchFaceDesignHolder.setHeavyStorm(true);
                watchFaceDesignHolder.setHeavyClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                break;
            }
            case 230: {
                Log.v(TAG, "thunderstorm with light drizzle");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setLightClouds(true);
                watchFaceDesignHolder.setModerateStorm(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 231: {
                Log.v(TAG, "thunderstorm with drizzle");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setLightClouds(true);
                watchFaceDesignHolder.setModerateStorm(true);
                break;
            }
            case 232: {
                Log.v(TAG, "thunderstorm with heavy drizzle");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setLightClouds(true);
                watchFaceDesignHolder.setModerateStorm(true);
                break;
            }
            case 300: {
                Log.v(TAG, "light intensity drizzle");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 301: {
                Log.v(TAG, "drizzle");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 302: {
                Log.v(TAG, "heavy intensity drizzle");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 310: {
                Log.v(TAG, "light intensity drizzle rain");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 311: {
                Log.v(TAG, "drizzle rain");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 312: {
                Log.v(TAG, "heavy intensity drizzle rain");
                watchFaceDesignHolder.setModerateRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 313: {
                Log.v(TAG, "shower rain and drizzle");
                watchFaceDesignHolder.setModerateRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 314: {
                Log.v(TAG, "heavy shower rain and drizzle");
                watchFaceDesignHolder.setHeavyRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 321: {
                Log.v(TAG, "shower drizzle");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 500: {
                Log.v(TAG, "light rain");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 501: {
                Log.v(TAG, "moderate rain");
                watchFaceDesignHolder.setModerateRain(true);
                break;
            }
            case 502: {
                Log.v(TAG, "heavy intensity rain");
                watchFaceDesignHolder.setHeavyRain(true);
                break;
            }
            case 503: {
                Log.v(TAG, "very heavy rain");
                watchFaceDesignHolder.setHeavyRain(true);
                break;
            }
            case 504: {
                Log.v(TAG, "extreme rain");
                watchFaceDesignHolder.setHeavyRain(true);
                break;
            }
            case 511: {
                Log.v(TAG, "freezing rain");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setLightSnow(true);
                break;
            }
            case 520: {
                Log.v(TAG, "light intensity shower rain");
                watchFaceDesignHolder.setLightRain(true);
                break;
            }
            case 521: {
                Log.v(TAG, "shower rain");
                watchFaceDesignHolder.setModerateRain(true);
                break;
            }
            case 522: {
                Log.v(TAG, "heavy intensity shower rain");
                watchFaceDesignHolder.setHeavyRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 531: {
                Log.v(TAG, "ragged shower rain");
                watchFaceDesignHolder.setHeavyRain(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 600: {
                Log.v(TAG, "light snow");
                watchFaceDesignHolder.setLightSnow(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 601: {
                Log.v(TAG, "snow");
                watchFaceDesignHolder.setModerateSnow(true);
                break;
            }
            case 602: {
                Log.v(TAG, "heavy snow");
                watchFaceDesignHolder.setHeavySnow(true);
                break;
            }
            case 611: {
                Log.v(TAG, "sleet");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setModerateSnow(true);
                break;
            }
            case 612: {
                Log.v(TAG, "shower sleet");
                watchFaceDesignHolder.setModerateRain(true);
                watchFaceDesignHolder.setLightSnow(true);
                break;
            }
            case 615: {
                Log.v(TAG, "light rain and snow");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setLightSnow(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 616: {
                Log.v(TAG, "rain and snow");
                watchFaceDesignHolder.setModerateRain(true);
                watchFaceDesignHolder.setModerateSnow(true);
                break;
            }
            case 620: {
                Log.v(TAG, "light shower snow");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setLightSnow(true);
                break;
            }
            case 621: {
                Log.v(TAG, "shower snow");
                watchFaceDesignHolder.setModerateRain(true);
                watchFaceDesignHolder.setModerateSnow(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 622: {
                Log.v(TAG, "heavy shower snow");
                watchFaceDesignHolder.setHeavyRain(true);
                watchFaceDesignHolder.setHeavySnow(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 701: {
                Log.v(TAG, "mist");
                watchFaceDesignHolder.setOvercast(true);
                watchFaceDesignHolder.setAreCloudsLow(true);
                break;
            }
            case 711: {
                Log.v(TAG, "smoke");
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 721: {
                Log.v(TAG, "haze");
                watchFaceDesignHolder.setOvercast(true);
                watchFaceDesignHolder.setAreCloudsLow(true);
                break;
            }
            case 731: {
                Log.v(TAG, "sand / dust whirls");
                watchFaceDesignHolder.setLightWind(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 741: {
                Log.v(TAG, "fog");
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 751: {
                Log.v(TAG, "sand");
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 761: {
                Log.v(TAG, "dust");
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 762: {
                Log.v(TAG, "volcanic ash");
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 771: {
                Log.v(TAG, "squalls");
                watchFaceDesignHolder.setLightRain(true);
                watchFaceDesignHolder.setLightWind(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 781: {
                Log.v(TAG, "tornado");
                watchFaceDesignHolder.setHeavyWind(true);
                watchFaceDesignHolder.setAreCloudsLow(true);
                break;
            }
            case 800: {
                Log.v(TAG, "clouds");
                watchFaceDesignHolder.setModerateClouds(true);
                watchFaceDesignHolder.setAreCloudsLow(true);
                break;
            }
            case 801: {
                Log.v(TAG, "few clouds");
                watchFaceDesignHolder.setLightClouds(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 802: {
                Log.v(TAG, "scattered clouds");
                watchFaceDesignHolder.setModerateClouds(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 803: {
                Log.v(TAG, "broken clouds");
                watchFaceDesignHolder.setLightClouds(true);
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 804: {
                Log.v(TAG, "overcast clouds");
                watchFaceDesignHolder.setHeavyClouds(true);
                watchFaceDesignHolder.setOvercast(true);
                break;
            }
            case 900: {
                Log.v(TAG, "tornado");
                watchFaceDesignHolder.setHeavyWind(true);
                watchFaceDesignHolder.setAreCloudsLow(true);
                break;
            }
            case 901: {
                Log.v(TAG, "tropical storm");
                watchFaceDesignHolder.setHeavyStorm(true);
                watchFaceDesignHolder.setHeavyWind(true);
                watchFaceDesignHolder.setAreCloudsLow(true);
                break;
            }
            case 902: {
                Log.v(TAG, "hurricane");
                watchFaceDesignHolder.setHeavyStorm(true);
                watchFaceDesignHolder.setHeavyWind(true);
                watchFaceDesignHolder.setAreCloudsLow(true);
                break;
            }
            case 903: {
                Log.v(TAG, "cold");
                break;
            }
            case 904: {
                Log.v(TAG, "hot");
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 905: {
                Log.v(TAG, "windy");
                watchFaceDesignHolder.setModerateWind(true);
                break;
            }
            case 906: {
                Log.v(TAG, "hail");
                watchFaceDesignHolder.setModerateSnow(true);
                break;
            }
            case 950: {
                Log.v(TAG, "setting");
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 951: {
                Log.v(TAG, "calm");
                watchFaceDesignHolder.setSunshine(true);
                break;
            }
            case 952: {
                Log.v(TAG, "light breeze");
                watchFaceDesignHolder.setLightWind(true);
                break;
            }
            case 953: {
                Log.v(TAG, "gentle breeze");
                watchFaceDesignHolder.setLightWind(true);
                break;
            }
            case 954: {
                Log.v(TAG, "moderate breeze");
                watchFaceDesignHolder.setModerateWind(true);
                break;
            }
            case 955: {
                Log.v(TAG, "fresh breeze");
                watchFaceDesignHolder.setModerateWind(true);
                break;
            }
            case 956: {
                Log.v(TAG, "strong breeze");
                watchFaceDesignHolder.setHeavyWind(true);
                break;
            }
            case 957: {
                Log.v(TAG, "high wind, near gale");
                watchFaceDesignHolder.setHeavyWind(true);
                break;
            }
            case 958: {
                Log.v(TAG, "gale");
                watchFaceDesignHolder.setHeavyWind(true);
                break;
            }
            case 959: {
                Log.v(TAG, "severe gale");
                watchFaceDesignHolder.setHeavyWind(true);
                break;
            }
            case 960: {
                Log.v(TAG, "storm");
                watchFaceDesignHolder.setModerateWind(true);
                watchFaceDesignHolder.setModerateStorm(true);
                break;
            }
            case 961: {
                Log.v(TAG, "violent storm");
                watchFaceDesignHolder.setModerateWind(true);
                watchFaceDesignHolder.setHeavyStorm(true);
                watchFaceDesignHolder.setModerateClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                break;
            }
            case 962: {
                Log.v(TAG, "hurricane");
                watchFaceDesignHolder.setHeavyRain(true);
                watchFaceDesignHolder.setHeavyWind(true);
                watchFaceDesignHolder.setHeavyClouds(true);
                watchFaceDesignHolder.setAreCloudsDark(true);
                watchFaceDesignHolder.setHeavyStorm(true);
                break;
            }
        }
        watchFaceDesignHolder.setDirty(true);
    }

}
