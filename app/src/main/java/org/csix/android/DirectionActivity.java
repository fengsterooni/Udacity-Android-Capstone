package org.csix.android;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.ui.IconGenerator;

import butterknife.ButterKnife;

public class DirectionActivity extends BaseMapActivity {

    private int year;
    private int month;
    private int day;
    private String eventNotes = null;
    private String eventId;
    // private Event event;
    private Typeface font = null;
    private String locationAddress;
    private LatLng latLng;
    private String sfc;
    private GoogleMap map = null;
    private Marker marker;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_direction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        locationAddress = "20390 Park Place, Saratoga, CA 95070";
        latLng = LocationUtils.getAddress(this, locationAddress);
    }
/*
    saratoga & park
    37.259537, -122.030344

    Church entrance
    37.258169, -122.029994

    Lower parking lot
    37.259189, -122.030509

    Lifthouse (C6) Entrance
    37.258055, -122.030835
    */

    @Override
    protected void setupMap() {
        if (map == null) {
            map = getMap();
        }

        // Display the connection status
        if (marker != null)
            marker.setVisible(false);
        if (latLng != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.258055, -122.030835), 17);
            map.moveCamera(cameraUpdate);
            map.animateCamera(cameraUpdate);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            marker = map.addMarker(new MarkerOptions()
                    .position(latLng));
            dropPinEffect(marker);

            setupSigns();
        }
    }


    private void setupSigns() {
        IconGenerator iconGenerator = new IconGenerator(this);

        Sign sign = new Sign(37.258055, -122.030835, "Lifehouse\nCSix Entrance", "We meet here", R.drawable.lifehouse2);
        iconGenerator.setRotation(180);
        iconGenerator.setContentRotation(-180);
        iconGenerator.setStyle(IconGenerator.STYLE_BLUE);
        addIcon(iconGenerator, sign.getName(), sign.getPosition());

        sign = new Sign(37.258169, -122.029994, "Church Entrance", "Go pass here", R.drawable.church2);
        iconGenerator.setRotation(90);
        iconGenerator.setContentRotation(0);
        iconGenerator.setStyle(IconGenerator.STYLE_PURPLE);
        addIcon(iconGenerator, sign.getName(), sign.getPosition());

        sign = new Sign(37.259189, -122.030509, "Lower Parking Lot", "Please park here", R.drawable.lowerparking2);
        iconGenerator.setRotation(90);
        iconGenerator.setContentRotation(-90);
        iconGenerator.setStyle(IconGenerator.STYLE_GREEN);
        addIcon(iconGenerator, sign.getName(), sign.getPosition());

    }

    private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text)))
                .position(position)
                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        marker = map.addMarker(markerOptions);
        marker.showInfoWindow();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                // overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class Sign implements ClusterItem {
        private final LatLng position;
        private double latitude;
        private double longtitude;
        private String name;
        private String notes;
        private int img;

        public Sign(double lat, double lng, String name, String notes, int image) {
            this.latitude = lat;
            this.longtitude = lng;
            this.position = new LatLng(lat, lng);
            this.name = name;
            this.notes = notes;
            this.img = image;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }
    }

}
