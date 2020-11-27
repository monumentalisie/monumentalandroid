package com.hgosot.skopje;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgosot.skopje.utils.FileUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerActivity extends BaseActivity {

    @BindView(R.id.iv_take_tour)
    ImageView ivTakeTour;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.tv_take_tour)
    TextView tvTakeTour;
    @BindView(R.id.tv_lng)
    TextView tvLanguage;
    @BindView(R.id.iv_tips)
    ImageView ivTips;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.iv_welcome)
    ImageView ivWelcome;
    private List<String> langList;
    private String currentLng;
    private boolean audioOn = false;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_drawer);
        ButterKnife.bind(this);
        startAudio();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.about);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrawerActivity.this, AboutActivity.class));
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ivTakeTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrawerActivity.this, TourListActivity.class));
            }
        });
        ivTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrawerActivity.this, OtherCitiesListActivity.class));
            }
        });
        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrawerActivity.this, GoodToKnowActivity.class));
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/MonumentalMK/?modal=admin_dodo_tour"));
                startActivity(browserIntent);
//                startActivity(new Intent(DrawerActivity.this, ShareActivity.class));
            }
        });

        ivWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioOn) {
                    audioOn = false;
                    ivWelcome.setBackgroundResource(R.drawable.ic_audio_off);
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                } else {
                    audioOn = true;
                    ivWelcome.setBackgroundResource(R.drawable.ic_audio_on);
                    if (mediaPlayer != null ) {
                        mediaPlayer.start();
                    }
                }
            }
        });

        langList = Arrays.asList(getResources().getStringArray(R.array.lang));
        currentLng = getLanguage();
        String capitilizedLetter = currentLng.substring(0, 1).toUpperCase() + currentLng.substring(1);
        tvLanguage.setText(currentLng.toUpperCase());
        tvLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DrawerActivity.this, LngActivity.class));

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
    protected void onStart() {
        super.onStart();
        if (mediaPlayer != null  && audioOn) {
            mediaPlayer.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startAudio(){

        String fileName = FileUtils.getEntryAudio(getLanguage());

        int resourceId = getResources().getIdentifier(fileName,
                "raw", getPackageName());

        mediaPlayer = MediaPlayer.create(this, resourceId);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.spinner_toolbar, menu);
//
//        MenuItem item = menu.findItem(R.id.spinner);
//        final Spinner spinner = (Spinner) item.getActionView();
//
////        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
////                R.array.lang, android.R.layout.simple_spinner_item);
//
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, langList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//        final int i = langList.indexOf(currentLng);
//
//        Log.d("test", "po" + i);
//
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
//
//                String selectedItem = langList.get(position);
//
//                if (currentLng.equalsIgnoreCase(selectedItem)) return;
//
//                if (selectedItem.equalsIgnoreCase(LocaleManager.LANGUAGE_ENGLISH)) {
//
//                    MainApplication.localeManager.setNewLocale(DrawerActivity.this, LocaleManager.LANGUAGE_ENGLISH);
//                    // tvTakeTour.setText(getResources().getString(R.string.take_tour));
//
//                    Log.d("test", "en");
//                } else if (selectedItem.equalsIgnoreCase(LocaleManager.LANGUAGE_MACEDONIAN)) {
//
//                    MainApplication.localeManager.setNewLocale(DrawerActivity.this, LocaleManager.LANGUAGE_MACEDONIAN);
//                    Log.d("test", "mk");
//
//                    //  tvTakeTour.setText(getResources().getString(R.string.take_tour));
//
//                } else if (selectedItem.equalsIgnoreCase(LocaleManager.LANGUAGE_ALBANIAN)) {
//                    MainApplication.localeManager.setNewLocale(DrawerActivity.this, LocaleManager.LANGUAGE_ALBANIAN);
//                }
//
//                Intent i = new Intent(DrawerActivity.this, SplashActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                finish();
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//        spinner.setAdapter(adapter);
//        spinner.setSelection(i);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
