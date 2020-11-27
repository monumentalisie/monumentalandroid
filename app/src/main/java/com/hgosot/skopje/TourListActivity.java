package com.hgosot.skopje;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.hgosot.skopje.data.Tour;
import com.hgosot.skopje.utils.FileUtils;

import org.parceler.Parcels;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

public class TourListActivity extends BaseListActivity {

    public String toursJsonFile;
    public boolean audioOn = false;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        switch (getLanguage()) {
//            case "en":
//                toursJsonFile ="skopjeTours_en.json";
//                break;
//            case "mk":
//                toursJsonFile ="skopjeTours_mk.json";
//                break;
//            case "sq":
//                toursJsonFile ="skopjeTours_sq.json";
//                break;
//
//        }
        toursJsonFile = FileUtils.getJson(getLanguage());
        new LoadJson(this).execute(toursJsonFile);




    }

  //  @Override
   // protected String getToolbarTitle() {
   //     return "Take a Tour";
   // }

    @Override
    protected void onListItemClicked(Tour tour) {
        startDetailActivity(tour);
    }

    @Override
    protected void setListHeader() {
        // android:src="@drawable/ic_map_24dp"

        ivTourHeader.setBackgroundResource(R.drawable.map_big);

        // ivTourHeader.setImageDrawable(getResources().getDrawable(R.drawable.map_big, getApplicationContext().getTheme()));
//        tvTourHeader.setText(getResources().getString(R.string.take_tour));


    }

    @Override
    protected void startAudio() {
        tvBrutalisam.setVisibility(View.VISIBLE);
        ivBrutalisam.setVisibility(View.VISIBLE);

        String fileName = FileUtils.getListAudio(getLanguage());

        int resourceId = getResources().getIdentifier(fileName,
                "raw", getPackageName());

        mediaPlayer = MediaPlayer.create(this, resourceId);

        ivBrutalisam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioOn) {
                    audioOn = false;
                    ivBrutalisam.setBackgroundResource(R.drawable.ic_audio_off);
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                } else {
                    audioOn = true;
                    ivBrutalisam.setBackgroundResource(R.drawable.ic_audio_on);
                    if (mediaPlayer != null ) {
                        mediaPlayer.start();
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }


    private void startDetailActivity(Tour tour) {
        Intent intent = new Intent(this, TourDetailActivity.class);
        intent.setAction(TourDetailActivity.ACTION_DISPLAY_TOUR);
        intent.putExtra(PREFS_TOUR, Parcels.wrap(tour));
        startActivity(intent);


    }

    private static class LoadJson extends AsyncTask<String, Void, List<Tour>> {

        private WeakReference<TourListActivity> weakReference;

        LoadJson(TourListActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected List<Tour> doInBackground(String... args) {
            try {
                TourListActivity activity = weakReference.get();
                if (activity != null) {
                    InputStream inputStream = activity.getAssets().open(args[0]);

                    String jsonInString = convertStreamToString(inputStream);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Tour>>() {
                    }.getType();
                    List<Tour> routesList = gson.fromJson(jsonInString, type);
                    return routesList;
                }
            } catch (Exception exception) {
                Log.e("zz", "Exception Loading GeoJSON: " + exception.toString());
            }
            return null;
        }

        static String convertStreamToString(InputStream is) {
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }


        @Override
        protected void onPostExecute(@Nullable List<Tour> routesList) {
            super.onPostExecute(routesList);
            TourListActivity activity = weakReference.get();
            if (activity != null && routesList != null) {
                activity.populateList(routesList);

            }
        }
    }


}
