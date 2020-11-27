package com.hgosot.skopje.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class MiscUtils {


    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
