package com.hgosot.skopje;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hgosot.skopje.data.Place;
import com.hgosot.skopje.data.Tour;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TipsListActivity extends BaseListActivity {

    private DatabaseReference databaseReference;

    private boolean childListDisplayed;

   // private List<Tour> rootList;

   // @Override
   // protected String getToolbarTitle() {
  //      return "Skopje Tips";
  //  }

    @Override
    protected void onListItemClicked(Tour tour) {

        if(tour.getPlaces() != null){
            childListDisplayed = true;

            populateList(mapToAdapterData(tour));

        }else{
            Intent intent = new Intent(this,TourDetailActivity.class);
            intent.setAction(TourDetailActivity.ACTION_DISPLAY_SUB_TOUR);
            intent.putExtra(PREFS_TOUR, Parcels.wrap(tour));
            startActivity(intent);

        }


    }

    @Override
    protected void setListHeader() {
        tvTourHeader.setText(getResources().getString(R.string.tips));

        ivTourHeader.setBackgroundResource(R.drawable.tips_big);
      //  ivTourHeader.setImageDrawable(getResources().getDrawable(R.drawable.tips_big, getApplicationContext().getTheme()));


    }

    @Override
    protected void startAudio() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        pbLoadingTours.show();
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {

        final List<Tour> tours = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Tour tour = postSnapshot.getValue(Tour.class);
                    tours.add(tour);
                }

                populateList(tours);

                Log.d("zz", tours.size() + "size");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pbLoadingTours.hide();
            }
        });


    }

    private List<Tour> mapToAdapterData(Tour parent){
        List<Place> places = parent.getPlaces();
        List<Tour> tours = new ArrayList<>();

        for (Place place : places) {

            Tour tour = new Tour();
            tour.setId(place.getId());
            tour.setName(place.getName());
            tour.setInfoText(place.getInfoText());
            tour.setImage(parent.getImage());
            tours.add(tour);

        }


        return tours;

    }

    @Override
    public void onBackPressed() {

        if(childListDisplayed){

            getDataFromFirebase();

            childListDisplayed = false;

        }else{
            super.onBackPressed();
        }

    }
}
