package com.hgosot.skopje;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.hgosot.skopje.utils.LocaleManager;
import com.mapbox.mapboxsdk.Mapbox;

import jonathanfinerty.once.Once;

public class MainApplication extends Application {


    private static final String YOUR_MAPBOX_ACCESS_TOKEN ="pk.eyJ1IjoibWFsb21lY2UiLCJhIjoiY2pxNGVxNDI1MXQ4NTQydWxndmF3dWcybyJ9.pe_ce13GB6Gl3hq8kh6OtA" ;


    public static LocaleManager localeManager;

    @Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base);
        super.attachBaseContext(localeManager.setLocale(base));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(this);

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Mapbox.getInstance(getApplicationContext(), YOUR_MAPBOX_ACCESS_TOKEN);
        Once.initialise(this);
    }


}
