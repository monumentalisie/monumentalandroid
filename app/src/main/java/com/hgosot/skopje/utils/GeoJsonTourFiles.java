package com.hgosot.skopje.utils;

import java.util.HashMap;

public class GeoJsonTourFiles {

    public static String getGeoJsonById(int id){
        return jsonPairs.get(String.valueOf(id));



    }

    private static HashMap<String,String > jsonPairs = new HashMap<>();

    static {
        jsonPairs.put("1","tour_1.geojson");
        jsonPairs.put("2","tour_2.geojson");
        jsonPairs.put("3","tour_3.geojson");
        jsonPairs.put("4","tour_4.geojson");
        jsonPairs.put("5","tour_5.geojson");
        jsonPairs.put("6","tour_6.geojson");
        jsonPairs.put("7","tour_7.geojson");

    }


}
