package com.hgosot.skopje;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        resetTitle();
    }




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MainApplication.localeManager.setLocale(newBase));
    }


    protected String getLanguage(){
        return MainApplication.localeManager.getLanguage();
    }

    private void resetTitle() {
        try {
            int label = getPackageManager().getActivityInfo(getComponentName(),PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        } catch (PackageManager.NameNotFoundException e) { }
    }
}
