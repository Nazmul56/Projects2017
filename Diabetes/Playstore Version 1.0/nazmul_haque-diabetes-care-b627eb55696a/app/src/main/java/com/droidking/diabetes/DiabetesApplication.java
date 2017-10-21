package com.droidking.diabetes;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class DiabetesApplication extends Application {
/*
    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    /*
synchronized public Tracker getDefaultTracker() {
    if (mTracker == null) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        mTracker = analytics.newTracker(BuildConfig.GOOGLE_ANALYTICS_TRACKER);
        mTracker.enableAdvertisingIdCollection(true);

        if (BuildConfig.DEBUG) {
            GoogleAnalytics.getInstance(this).setAppOptOut(true);
            Log.i("Glucosio", "DEBUG BUILD: ANALYTICS IS DISABLED");
        }
    }
    return mTracker;
}
*/
    @Override
    public void onCreate() {
        super.onCreate();

        String lang = "it_IT";// Get the language code from SharedPreferences;
        if (lang != null){
            Resources res = getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = new Locale(lang.toLowerCase());
            Log.e("locale", conf.locale.toString());
            res.updateConfiguration(conf, dm);
        }

       /* CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/lato-bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());*/
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/
}