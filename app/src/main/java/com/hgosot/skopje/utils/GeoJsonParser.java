package com.hgosot.skopje.utils;

import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoJsonParser {
    private static final String FEATURE_COLLECTION_ARRAY = "features";
    // Feature object geometry member
    private static final String FEATURE_GEOMETRY = "geometry";
    // Geometry coordinates member
    private static final String GEOMETRY_COORDINATES_ARRAY = "coordinates";

    public static LatLng getFirstCoordinates(String geoJsonString) throws JSONException {

        JSONObject jsonObject = new JSONObject(geoJsonString);
        JSONArray array = jsonObject.getJSONArray(FEATURE_COLLECTION_ARRAY);
        JSONObject firstObject = array.getJSONObject(0);
        JSONObject geometry = firstObject.getJSONObject(FEATURE_GEOMETRY);
        Log.d("zz", geometry.toString());
        JSONArray coordinates = geometry.getJSONArray(GEOMETRY_COORDINATES_ARRAY);
        double latitude = coordinates.getDouble(1);
        double longitude = coordinates.getDouble(0);

        return new LatLng(latitude,longitude);
    }
}
