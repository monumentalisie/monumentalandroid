package com.hgosot.skopje;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hgosot.skopje.adapter.MapPagerAdapter;
import com.hgosot.skopje.data.Dot;
import com.hgosot.skopje.data.Place;
import com.hgosot.skopje.data.Tour;
import com.hgosot.skopje.utils.MiscUtils;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, PermissionsListener {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.pb_map_loading)
    ProgressBar progressBar;
    private MapView mapView;


    private static final String MARKER_SOURCE = "markers-source";
    private static final String DOT_SOURCE = "dot-source";
    private static final String LINE_SOURCE = "line-source";
    private static final String MARKER_STYLE_LAYER = "markers-style-layer";
    private static final String MARKER_IMAGE = "custom_marker";
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";
    private MapboxMap mapboxMap;

    private MapPagerAdapter adapter;

    public String geoJson;
    private Tour tour;
    private GeoJsonSource geoJsonSource;
    private PermissionsManager permissionsManager;
    private OfflineManager offlineManager;
    private boolean isEndNotified;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_activiy);
        ButterKnife.bind(this);

        permissionsManager = new PermissionsManager(this);

        offlineManager = OfflineManager.getInstance(this);


        geoJson = getIntent().getStringExtra(TourDetailActivity.PREFS_GEOJSON);
        tour = Parcels.unwrap(getIntent().getParcelableExtra(TourListActivity.PREFS_TOUR));

        mapView = (MapView) findViewById(R.id.mapView);
        int defualtgap = (int) MiscUtils.convertDpToPixel(40, this);
        adapter = new MapPagerAdapter(this, tour.getPlaces(),String.valueOf(tour.getId()));
        adapter.setOnItemClickListener(new MapPagerAdapter.OnPagerItemClickListener() {
            @Override
            public void onPageClicked(int pos) {

                //  Toast.makeText(MapActivity.this, "page " + pos + " clicked", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MapActivity.this, TourDetailActivity.class);
                i.setAction(TourDetailActivity.ACTION_DISPLAY_PLACE);
                i.putExtra(TourDetailActivity.IMAGE_URL_PREFS, tour.getImage());
                i.putExtra(TourDetailActivity.TOUR_ID, tour.getId());
                i.putExtra(BaseListActivity.PREFS_PLACE, Parcels.wrap(tour.getPlaces().get(pos)));
                startActivity(i);
            }
        });
        mViewPager.setPadding(defualtgap, 0, defualtgap, 0);
        mViewPager.setClipToPadding(false);
        mViewPager.setPageMargin(defualtgap / 2);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.d("zz", position + "");
                if (geoJsonSource != null) {


                    Place place = tour.getPlaces().get(position);

                    if (place.getDots() != null && !place.getDots().isEmpty()) {

                        List<Dot> dots = place.getDots();
                        List<Feature> features = new ArrayList<>();
                        for (int i = 0; i < dots.size(); i++) {
                            Dot dot = dots.get(i);

                            features.add(Feature.fromGeometry(Point.fromLngLat(dot.getLongitude(), dot.getLatitude())));
                        }


                        geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(features));


                    } else {
                        geoJsonSource.setGeoJson(Point.fromLngLat(place.getLongitude(), place.getLatitude()));

                    }


                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mapView.onCreate(savedInstanceState);

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location
            mapView.getMapAsync(this);

        } else {

            permissionsManager.requestLocationPermissions(this);
        }


    }


    private void drawLines(@NonNull FeatureCollection featureCollection) {

        if (mapboxMap != null) {
            Style style = mapboxMap.getStyle();
            if (style != null) {
                if (featureCollection.features().size() > 0) {
                    style.addSource(new GeoJsonSource(LINE_SOURCE, featureCollection));

// The layer properties for our line. This is where we make the line dotted, set the
// color, etc.
                    style.addLayer(new LineLayer("linelayer", LINE_SOURCE)
                            .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                    PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                    PropertyFactory.lineOpacity(.7f),
                                    PropertyFactory.lineWidth(3f),
                                    PropertyFactory.lineColor(getResources().getColor(R.color.red))));
                    drawPoints(featureCollection);
                    drawMarker(tour.getPlaces().get(0));
                    moveCamera(tour.getPlaces().get(0));


                }
            }
        }
    }


    private void drawMarker(Place place) {
        List<Feature> features = new ArrayList<>();
        features.add(Feature.fromGeometry(Point.fromLngLat(place.getLongitude(), place.getLatitude())));
        Style style = mapboxMap.getStyle();


        Source source = style.getSource(MARKER_SOURCE);

        style.addImage(MARKER_IMAGE, BitmapFactory.decodeResource(
                MapActivity.this.getResources(), R.drawable.marker_stroke));


        geoJsonSource = new GeoJsonSource(MARKER_SOURCE, FeatureCollection.fromFeatures(features));

        style.addSource(geoJsonSource);


        style.addLayer(new SymbolLayer(MARKER_STYLE_LAYER, MARKER_SOURCE)
                .withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconImage(MARKER_IMAGE),


// Adjust the second number of the Float array based on the height of your marker image.
// This is because the bottom of the marker should be anchored to the coordinate point, rather
// than the middle of the marker being the anchor point on the map.
                        PropertyFactory.iconOffset(new Float[]{0f, -20f})
                ));
    }


    private void moveCamera(Place place) {


        LatLng firstCord = new LatLng();
        firstCord.setLatitude(place.getLatitude());
        firstCord.setLongitude(place.getLongitude());

        CameraPosition position = new CameraPosition.Builder()
                .target(firstCord)
                .zoom(15)
                .build();
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));


    }


    private void drawPoints(@NonNull FeatureCollection featureCollection) {


        List<Feature> features = featureCollection.features();

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.red_circle);
        mapboxMap.getStyle().addImage("my-marker-image", icon);
        mapboxMap.getStyle().addSource(new GeoJsonSource(DOT_SOURCE, FeatureCollection.fromFeatures(features)));
        SymbolLayer symbolLayer = new SymbolLayer("layer-id", DOT_SOURCE);
        symbolLayer.setProperties(
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconImage("my-marker-image"),
                PropertyFactory.iconAnchor(Property.ICON_ANCHOR_CENTER)
        );

        mapboxMap.getStyle().addLayer(symbolLayer);

    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                if (offlineManager != null) {
                    offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
                        @Override
                        public void onList(OfflineRegion[] offlineRegions) {

                            if (offlineRegions.length == 0) {
                                loadMapOffline(style);
                            }
                            displayTourOnMap(style);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }

            }
        });

    }

    private void loadMapOffline(final Style style) {

        // Create a bounding box for the offline region
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(42.006856, 21.445212)) // Northeast
                .include(new LatLng(41.994391, 21.423848)) // Southwest
                .build();

        // Define the offline region
        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                style.getUrl(),
                latLngBounds,
                12,
                17,
                MapActivity.this.getResources().getDisplayMetrics().density);

        // Set the metadata
        byte[] metadata;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_FIELD_REGION_NAME, "Skopje");
            String json = jsonObject.toString();
            metadata = json.getBytes(JSON_CHARSET);
        } catch (Exception exception) {
            Log.d("error", exception.getMessage());
            metadata = null;
        }
        offlineManager.createOfflineRegion(definition,
                metadata, new OfflineManager.CreateOfflineRegionCallback() {
                    @Override
                    public void onCreate(OfflineRegion offlineRegion) {
                        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);

                        // Display the download progress bar
                      //  startProgress();
                        Toast.makeText(MapActivity.this, "downloading map..", Toast.LENGTH_SHORT).show();

                        // Monitor the download progress using setObserver

                        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                            @Override
                            public void onStatusChanged(OfflineRegionStatus status) {
                                // Calculate the download percentage and update the progress bar
                                double percentage = status.getRequiredResourceCount() >= 0
                                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                        0.0;
                                Log.d("test0", Math.round(percentage) + " progress");
                                if (status.isComplete()) {
                                    // Download complete

                                    endProgress("map saved offline");
                                } else if (status.isRequiredResourceCountPrecise()) {
                                    // Switch to determinate state

                                    Log.d("test", Math.round(percentage) + " progress");
                                    // setPercentage((int) Math.round(percentage));
                                }
                            }

                            @Override
                            public void onError(OfflineRegionError error) {
                                Log.d("error", error.getMessage());

                            }

                            @Override
                            public void mapboxTileCountLimitExceeded(long limit) {
                                Log.d("error", " limit over");
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {

                    }
                });


    }

    private void displayTourOnMap(Style style) {

        enableLocationComponent(style);

        if (!TextUtils.isEmpty(geoJson)) {
            new LoadGeoJson(MapActivity.this).execute(geoJson);
        }


    }


    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {


// Get an instance of the component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
        locationComponent.activateLocationComponent(this, loadedMapStyle);

// Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
        //  locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
        locationComponent.setRenderMode(RenderMode.COMPASS);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {

            mapView.getMapAsync(this);

        } else {
            Toast.makeText(this, "User location permission not granted", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private static class LoadGeoJson extends AsyncTask<String, Void, FeatureCollection> {

        private WeakReference<MapActivity> weakReference;

        LoadGeoJson(MapActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(String... args) {
            try {
                MapActivity activity = weakReference.get();
                if (activity != null) {
                    InputStream inputStream = activity.getAssets().open(args[0]);
                    return FeatureCollection.fromJson(convertStreamToString(inputStream));
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
        protected void onPostExecute(@Nullable FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            MapActivity activity = weakReference.get();
            if (activity != null && featureCollection != null) {
                activity.drawLines(featureCollection);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void startProgress() {

// Start and show the progress bar
        isEndNotified = false;
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setPercentage(final int percentage) {
        progressBar.setIndeterminate(false);
        progressBar.setProgress(percentage);
    }

    private void endProgress(final String message) {

        if (isEndNotified) {
            return;
        }


        isEndNotified = true;
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);


        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
