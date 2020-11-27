package com.hgosot.skopje.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static String imageFileAssetsPath = "file:///android_asset/";
    static String imageFilePath = "file:///android_asset/tour1/main1.jpg";
    public static String imageType = ".jpg";


    public static String readJSONFromAsset(Context context, String file) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static String getMainTourImage(String id) {


        // return imageFilePath;

        return imageFileAssetsPath + "tour" + id + "/main" + id + imageType;


    }

    public static String getPlaceImage(String tourId, String placeId) {


        // return imageFilePath;


        return imageFileAssetsPath + "tour" + tourId + "/" + tourId + "." + placeId + "." + imageType;


    }

    public static String getPlaceImage(String imageName) {


        // return imageFilePath;


        return imageFileAssetsPath + "place" +  "/" + imageName;


    }

    public static List<String> getPlaceImages(int id, int size) {
        List<String> images = new ArrayList<>();
        if (id == 5) {
            size = size - 1;
        }
        for (int i = 1; i <= size; i++) {
            if (i == 1) {
                images.add(imageFileAssetsPath + "place" + "/" + "o_" + id + ".jpg");
            } else {
                images.add(imageFileAssetsPath + "place" + "/" + "o_" + id + "_" + i + ".jpg");
            }
        }
        return images;
    }

    public static List<String> getSkopjeImages(int id, int size) {
        List<String> images = new ArrayList<>();
        if (id == 8) {
            size = size - 1;
        }if (id == 17) {
            size = size + 1;
        }
        for (int i = 1; i <= size; i++) {
            if (i == 1) {
                images.add(imageFileAssetsPath + "place" +  "/" + id +".jpg");
            } else {
                images.add(imageFileAssetsPath + "place" +  "/" + id + "_" + i +".jpg");
            }
        }

        return images;
    }

    public static String getAudio(String tourId,String lng){

        return tourId + "_" + lng;


    }

    public static String getAudioById(int id,String lng){
        String path;
        switch (id) {
            case 1:
                path = "del_3_" + lng;
                break;
            case 2:
                path = "del_4_" + lng;
                break;
            case 4:
                path = "del_5_" + lng;
                break;
            case 5:
                path = "del_6_" + lng;
                break;
            case 6:
                path = "del_7_" + lng;
                break;
            default: path = "";
        }
        return path;
    }

    public static String getEntryAudio(String lng){

        return "del_1_" + lng;


    }

    public static String getListAudio(String lng){

        return "del_2_" + lng;


    }
    public static String getJson(String lng){
        return "skopjeTours_" + lng + ".json";


    }
    public static String getOtherJson(String lng){
        return "otherCities_" + lng + ".json";
    }

}

