package com.hgosot.skopje.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

import static android.os.Build.VERSION_CODES.N;

public class LocaleManager {
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_MACEDONIAN = "mk";
    public static final String LANGUAGE_ALBANIAN = "sq";
    public static final String LANGUAGE_BG = "bg";
    public static final String LANGUAGE_DE = "de";
    public static final String LANGUAGE_EL = "el";
    public static final String LANGUAGE_NL = "nl";
    public static final String LANGUAGE_PL= "pl";
    public static final String LANGUAGE_SR = "sr";
    public static final String LANGUAGE_TR= "tr";
    private static final String LANGUAGE_KEY = "language_key";


    private final SharedPreferences prefs;

    public LocaleManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context setLocale(Context c) {
        return updateResources(c, getLanguage());
    }

    public Context setNewLocale(Context c, String language) {
        persistLanguage(language);
        return updateResources(c, language);
    }

    public String getLanguage() {
        return prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        // use commit() instead of apply(), because sometimes we kill the application process immediately
        // which will prevent apply() to finish
        prefs.edit().putString(LANGUAGE_KEY, language).commit();
    }

    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());

        config.setLocale(locale);
        context = context.createConfigurationContext(config);

        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return isAtLeastVersion(N) ? config.getLocales().get(0) : config.locale;
    }

    public static boolean isAtLeastVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }
}
