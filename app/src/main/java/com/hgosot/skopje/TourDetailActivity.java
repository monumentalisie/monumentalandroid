package com.hgosot.skopje;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgosot.skopje.data.Place;
import com.hgosot.skopje.data.Tour;
import com.hgosot.skopje.utils.FileUtils;
import com.hgosot.skopje.utils.GeoJsonTourFiles;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TourDetailActivity extends BaseActivity {

    public static final String PREFS_GEOJSON = "geojson";
    public static final String ACTION_DISPLAY_TOUR = "action tour";
    public static final String ACTION_DISPLAY_SUB_TOUR = "action sub tour";
    public static final String ACTION_DISPLAY_PLACE = "action place";
    public static final String IMAGE_URL_PREFS = "image prefs";
    public static final String TOUR_ID = "tour_id";
    @BindView(R.id.iv_tour)
    ImageView ivTour;
    @BindView(R.id.tv_title)
    TextView tvTourTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvTourSubTitle;
    @BindView(R.id.tv_content)
    TextView tvTourContent;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.iv_audio)
    ImageView iv_audio;
    @BindView(R.id.iv_map)
    ImageView iv_map;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.tv_location_label)
    TextView tv_location_label;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.iv_history)
    ImageView iv_history;
    @BindView(R.id.tv_history_label)
    TextView tv_history_label;
    @BindView(R.id.tv_history)
    TextView tv_history;
    @BindView(R.id.iv_author)
    ImageView iv_author;
    @BindView(R.id.tv_author_label)
    TextView tv_author_label;
    @BindView(R.id.tv_author)
    TextView tv_author;
    @BindView(R.id.iv_description)
    ImageView iv_description;
    @BindView(R.id.tv_description_label)
    TextView tv_description_label;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.iv_interior)
    ImageView iv_interior;
    @BindView(R.id.tv_interior_label)
    TextView tv_interior_label;
    @BindView(R.id.tv_interior)
    TextView tv_interior;
    @BindView(R.id.iv_meaning)
    ImageView iv_meaning;
    @BindView(R.id.tv_meaning_label)
    TextView tv_meaning_label;
    @BindView(R.id.tv_meaning)
    TextView tv_meaning;
    @BindView(R.id.iv_tour_list)
    ImageHorizontalListLayout iv_tour_list;
    private Tour tour;

    private Place place;

    private MediaPlayer mediaPlayer;
    private boolean audioOn = false;
    private String tourId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String name = "";
        String infoText = "";
        String imageUrl = "";

        if (getIntent() != null) {
            String action = getIntent().getAction();
            if (TextUtils.isEmpty(action)) {
                finish();
                return;
            }

            if (action.equalsIgnoreCase(ACTION_DISPLAY_TOUR) || action.equalsIgnoreCase(ACTION_DISPLAY_SUB_TOUR)) {

                tour = Parcels.unwrap(getIntent().getParcelableExtra(TourListActivity.PREFS_TOUR));

                name = tour.getName();
                infoText = tour.getInfoText();
                if (TextUtils.isEmpty(infoText)) {
                    infoText = tour.getDescription();
                }
                imageUrl = tour.getImage();

//                if (action.equalsIgnoreCase(ACTION_DISPLAY_TOUR)) {
//                    setupTourAudio();
//                } else {
//                    btnPlay.setVisibility(View.INVISIBLE);
//                    btnNext.setVisibility(View.INVISIBLE);
//
//                }

            } else if (action.equalsIgnoreCase(ACTION_DISPLAY_PLACE)) {

                place = Parcels.unwrap(getIntent().getParcelableExtra(TourListActivity.PREFS_PLACE));
                name = place.getName();
                infoText = place.getInfoText();
                if (getIntent().hasExtra(IMAGE_URL_PREFS))
                    imageUrl = getIntent().getStringExtra(IMAGE_URL_PREFS);

                if (getIntent().hasExtra(TOUR_ID))
                    tourId = String.valueOf(getIntent().getIntExtra(TOUR_ID, 0));

                btnPlay.setVisibility(View.INVISIBLE);

            } else {
                finish();
            }
        }


        toolbar.setTitle(name);
        tvTourTitle.setText(name);

        iv_audio.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(tour.getTitle())) {
            int size = 1;
            if (tour.getImages() != null) {
                size = tour.getImages().size();
            }
            List<String> images = FileUtils.getPlaceImages(tour.getId(), size);
            iv_tour_list.setData(images);
            tvTourTitle.setText(name.toUpperCase());
            tvTourSubTitle.setText(tour.getTitle());
            setupTourAudio();
            setupLocation();
            setupHistory();
            setupAuthor();
            setupDescription();
            setupInterior();
            setupMeaning();
        } else {
            List<String> images = FileUtils.getSkopjeImages(tour.getId(), tour.getImages().size());
            iv_tour_list.setData(images);

            tvTourContent.setText(infoText);
            tvTourContent.setVisibility(View.VISIBLE);
            tvTourSubTitle.setVisibility(View.GONE);
        }

        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=Your+location&daddr=" + tour.getLatitude() +"," + tour.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getIntent().getAction().equalsIgnoreCase(ACTION_DISPLAY_PLACE)) {
                    finish();
                    return;

                }
                if (getIntent().getAction().equalsIgnoreCase(ACTION_DISPLAY_TOUR)) {
                    String geoJson = GeoJsonTourFiles.getGeoJsonById(tour.getId());

                    if (TextUtils.isEmpty(geoJson)) return;

                    Log.d("zz", geoJson);
                    startMapActivity(geoJson);
                }

            }
        });

    }

    private void setupLocation() {
        if (!TextUtils.isEmpty(tour.getLocation())) {
            iv_location.setVisibility(View.VISIBLE);
            tv_location_label.setVisibility(View.VISIBLE);
            tv_location.setVisibility(View.VISIBLE);
            tv_location.setText(tour.getLocation());
            iv_location.setBackgroundResource(R.drawable.ic_arrow_down);
        } else {
            iv_location.setVisibility(View.GONE);
            tv_location_label.setVisibility(View.GONE);
            tv_location.setVisibility(View.GONE);
        }
        tv_location_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_location.getVisibility() == View.GONE) {
                    tv_location.setVisibility(View.VISIBLE);
                    iv_location.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_location.setVisibility(View.GONE);
                    iv_location.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
        iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_location.getVisibility() == View.GONE) {
                    tv_location.setVisibility(View.VISIBLE);
                    iv_location.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_location.setVisibility(View.GONE);
                    iv_location.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
    }

    private void setupHistory() {
        if (!TextUtils.isEmpty(tour.getHistory())) {
            iv_history.setVisibility(View.VISIBLE);
            tv_history_label.setVisibility(View.VISIBLE);
            tv_history.setVisibility(View.GONE);
            tv_history.setText(tour.getHistory());
            iv_history.setBackgroundResource(R.drawable.ic_arrow_left);
        } else {
            iv_history.setVisibility(View.GONE);
            tv_history_label.setVisibility(View.GONE);
            tv_history.setVisibility(View.GONE);
        }
        tv_history_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_history.getVisibility() == View.GONE) {
                    tv_history.setVisibility(View.VISIBLE);
                    iv_history.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_history.setVisibility(View.GONE);
                    iv_history.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
        iv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_history.getVisibility() == View.GONE) {
                    tv_history.setVisibility(View.VISIBLE);
                    iv_history.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_history.setVisibility(View.GONE);
                    iv_history.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
    }

    private void setupAuthor() {
        if (!TextUtils.isEmpty(tour.getAuthor())) {
            iv_author.setVisibility(View.VISIBLE);
            tv_author_label.setVisibility(View.VISIBLE);
            tv_author.setVisibility(View.GONE);
            tv_author.setText(tour.getAuthor());
            iv_author.setBackgroundResource(R.drawable.ic_arrow_left);
        } else {
            iv_author.setVisibility(View.GONE);
            tv_author_label.setVisibility(View.GONE);
            tv_author.setVisibility(View.GONE);
        }
        tv_author_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_author.getVisibility() == View.GONE) {
                    tv_author.setVisibility(View.VISIBLE);
                    iv_author.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_author.setVisibility(View.GONE);
                    iv_author.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
        iv_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_author.getVisibility() == View.GONE) {
                    tv_author.setVisibility(View.VISIBLE);
                    iv_author.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_author.setVisibility(View.GONE);
                    iv_author.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
    }

    private void setupDescription() {
        if (!TextUtils.isEmpty(tour.getDescription())) {
            iv_description.setVisibility(View.VISIBLE);
            tv_description_label.setVisibility(View.VISIBLE);
            tv_description.setVisibility(View.GONE);
            tv_description.setText(tour.getDescription());
            iv_description.setBackgroundResource(R.drawable.ic_arrow_left);
        } else {
            iv_description.setVisibility(View.GONE);
            tv_description_label.setVisibility(View.GONE);
            tv_description.setVisibility(View.GONE);
        }
        tv_description_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_description.getVisibility() == View.GONE) {
                    tv_description.setVisibility(View.VISIBLE);
                    iv_description.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_description.setVisibility(View.GONE);
                    iv_description.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
        iv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_description.getVisibility() == View.GONE) {
                    tv_description.setVisibility(View.VISIBLE);
                    iv_description.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_description.setVisibility(View.GONE);
                    iv_description.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
    }

    private void setupInterior() {
        if (!TextUtils.isEmpty(tour.getInterior())) {
            iv_interior.setVisibility(View.VISIBLE);
            tv_interior_label.setVisibility(View.VISIBLE);
            tv_interior.setVisibility(View.GONE);
            tv_interior.setText(tour.getInterior());
            iv_interior.setBackgroundResource(R.drawable.ic_arrow_left);
        } else {
            iv_interior.setVisibility(View.GONE);
            tv_interior_label.setVisibility(View.GONE);
            tv_interior.setVisibility(View.GONE);
        }
        tv_interior_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_interior.getVisibility() == View.GONE) {
                    tv_interior.setVisibility(View.VISIBLE);
                    iv_interior.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_interior.setVisibility(View.GONE);
                    iv_interior.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
        iv_interior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_interior.getVisibility() == View.GONE) {
                    tv_interior.setVisibility(View.VISIBLE);
                    iv_interior.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_interior.setVisibility(View.GONE);
                    iv_interior.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
    }

    private void setupMeaning() {
        if (!TextUtils.isEmpty(tour.getMeaning())) {
            iv_meaning.setVisibility(View.VISIBLE);
            tv_meaning_label.setVisibility(View.VISIBLE);
            tv_meaning.setVisibility(View.GONE);
            tv_meaning.setText(tour.getMeaning());
            iv_meaning.setBackgroundResource(R.drawable.ic_arrow_left);
        } else {
            iv_meaning.setVisibility(View.GONE);
            tv_meaning_label.setVisibility(View.GONE);
            tv_meaning.setVisibility(View.GONE);
        }
        tv_meaning_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_meaning.getVisibility() == View.GONE) {
                    tv_meaning.setVisibility(View.VISIBLE);
                    iv_meaning.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_meaning.setVisibility(View.GONE);
                    iv_meaning.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
        iv_meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_meaning.getVisibility() == View.GONE) {
                    tv_meaning.setVisibility(View.VISIBLE);
                    iv_meaning.setBackgroundResource(R.drawable.ic_arrow_down);
                } else {
                    tv_meaning.setVisibility(View.GONE);
                    iv_meaning.setBackgroundResource(R.drawable.ic_arrow_left);
                }
            }
        });
    }

    private void setupTourAudio() {
        int id = tour.getId();
        String lng = getLanguage();
        String fileName = FileUtils.getAudioById(id, lng);
        if (!TextUtils.isEmpty(fileName)) {
            int resourceId = getResources().getIdentifier(fileName,
                "raw", getPackageName());

            iv_audio.setVisibility(View.VISIBLE);
            initAudio(resourceId);
            iv_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (audioOn) {
                        audioOn = false;
                        iv_audio.setBackgroundResource(R.drawable.ic_audio_off);
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }
                    } else {
                        audioOn = true;
                        iv_audio.setBackgroundResource(R.drawable.ic_audio_on);
                        if (mediaPlayer != null ) {
                            mediaPlayer.start();
                        }
                    }
                }
            });
        }
    }


    private void initAudio(int resource) {
        mediaPlayer = MediaPlayer.create(this, resource);

    }


    private void startMapActivity(String geojson) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(TourListActivity.PREFS_TOUR, Parcels.wrap(tour));
        intent.putExtra(PREFS_GEOJSON, geojson);

        startActivity(intent);

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
