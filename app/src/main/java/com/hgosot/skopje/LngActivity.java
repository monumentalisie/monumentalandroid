package com.hgosot.skopje;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LngActivity extends BaseActivity {
    @BindView(R.id.lv_lngs)
    ListView listView;
    private ListViewAdapter adapter;
    private ArrayList<String> lngArrayList;
    private String currentLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lng);
        currentLng = getLanguage();
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set ListView header
        adapter = new ListViewAdapter(this, R.layout.item_lng_list, getData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener());
    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                handleLngSelection(position);
            }
        };
    }

    private List<String> getData() {
         lngArrayList = new ArrayList<>();

        lngArrayList.add("en");
        lngArrayList.add("de");
        lngArrayList.add("mk");
        lngArrayList.add("sq");
        lngArrayList.add("sr");

        return lngArrayList;


    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("test", " position" + adapter.getSelectedPosition());
        handleLngSelection(adapter.getSelectedPosition());
        return true;
    }

    private void handleLngSelection(int pos){


        String selectedItem = lngArrayList.get(pos);

        if (currentLng.equalsIgnoreCase(selectedItem)){
            onBackPressed();
            return;
        }

//        if (selectedItem.equalsIgnoreCase(LocaleManager.LANGUAGE_ENGLISH)) {
//
//            MainApplication.localeManager.setNewLocale(this, LocaleManager.LANGUAGE_ENGLISH);
//            // tvTakeTour.setText(getResources().getString(R.string.take_tour));
//
//            Log.d("test", "en");
//        } else if (selectedItem.equalsIgnoreCase(LocaleManager.LANGUAGE_MACEDONIAN)) {
//
//            MainApplication.localeManager.setNewLocale(this, LocaleManager.LANGUAGE_MACEDONIAN);
//            Log.d("test", "mk");
//
//            //  tvTakeTour.setText(getResources().getString(R.string.take_tour));
//
//        } else if (selectedItem.equalsIgnoreCase(LocaleManager.LANGUAGE_ALBANIAN)) {
//            MainApplication.localeManager.setNewLocale(this, LocaleManager.LANGUAGE_ALBANIAN);
//            Log.d("test", "sq");
//
//        }
        MainApplication.localeManager.setNewLocale(this, selectedItem);

        Intent i = new Intent(this, DrawerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();



    }
}
