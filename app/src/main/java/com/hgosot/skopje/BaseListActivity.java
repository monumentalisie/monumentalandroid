package com.hgosot.skopje;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgosot.skopje.adapter.TourListAdapter;
import com.hgosot.skopje.data.Tour;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public  abstract  class BaseListActivity extends BaseActivity {
    public static final String PREFS_TOUR = "tour_prefs";
    public static final String PREFS_PLACE = "place_prefs";

    @BindView(R.id.rv_tour_list)
    RecyclerView rvTourList;
    @BindView(R.id.iv_tour_image)
    ImageView ivTourHeader;
    @BindView(R.id.tv_tour_title)
    TextView tvTourHeader;
    @BindView(R.id.tv_brutalisam)
    TextView tvBrutalisam;
    @BindView(R.id.iv_brutalisam)
    ImageView ivBrutalisam;
    @BindView(R.id.pb_tours)
    ContentLoadingProgressBar pbLoadingTours;

    private TourListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    // protected abstract String getToolbarTitle();

     protected  abstract void onListItemClicked(Tour tour);


     protected abstract void setListHeader();

     protected abstract void startAudio();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  toolbar.setTitle(getToolbarTitle());

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvTourList.setLayoutManager(layoutManager);
        setListHeader();
        startAudio();

        // specify an adapter (see also next example)





    }

    protected void populateList(List<Tour> baseEvents){

        pbLoadingTours.hide();

        mAdapter = new TourListAdapter(baseEvents,this);
        mAdapter.setOnAdapterClickListener(new TourListAdapter.OnItemClickListener() {
            @Override
            public void onToursListClicked(Tour tour) {

                onListItemClicked(tour);

            }
        });
        rvTourList.setAdapter(mAdapter);



    }





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
