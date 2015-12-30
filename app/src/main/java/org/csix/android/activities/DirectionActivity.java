package org.csix.android.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.ui.IconGenerator;

import org.csix.android.R;
import org.csix.android.utils.LocationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DirectionActivity extends BaseMapActivity {

    private Typeface font = null;
    private String locationAddress;
    private LatLng latLng;
    private String sfc;
    private GoogleMap map = null;
    private Marker marker;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_direction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        locationAddress = "20390 Park Place, Saratoga, CA 95070";
        latLng = LocationUtils.getAddress(this, locationAddress);
    }

    @Override
    protected void setupMap() {
        if (map == null) {
            map = getMap();
        }

        // Display the connection status
        if (marker != null)
            marker.setVisible(false);
        if (latLng != null) {
            CameraPosition cp = CameraPosition.builder().target(latLng).zoom(18).tilt(60).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            marker = map.addMarker(new MarkerOptions()
                    .position(latLng));

            setupSigns();
        }
    }


    private void setupSigns() {
        IconGenerator iconGenerator = new IconGenerator(this);

        String signName = getString(R.string.sign_csix_entrance);
        String signDesc = getString(R.string.sign_csix_entrance_desc);
        Sign sign = new Sign(
                37.258055,
                -122.030835,
                signName,
                signDesc,
                R.drawable.lifehouse2);
        iconGenerator.setRotation(180);
        iconGenerator.setContentRotation(-180);
        iconGenerator.setStyle(IconGenerator.STYLE_BLUE);
        addIcon(iconGenerator, sign.getName(), sign.getPosition());

        signName = getString(R.string.sign_church_entrance);
        signDesc = getString(R.string.sign_church_entrance_desc);
        sign = new Sign(
                37.258169,
                -122.029994,
                signName,
                signDesc,
                R.drawable.church2);
        iconGenerator.setRotation(90);
        iconGenerator.setContentRotation(0);
        iconGenerator.setStyle(IconGenerator.STYLE_PURPLE);
        addIcon(iconGenerator, sign.getName(), sign.getPosition());

        signName = getString(R.string.sign_lower_parking);
        signDesc = getString(R.string.sign_lower_parking_desc);
        sign = new Sign(
                37.259189,
                -122.030509,
                signName,
                signDesc,
                R.drawable.lowerparking2);
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
        private final double latitude;
        private final double longtitude;
        private String name;
        private final String notes;
        private final int img;

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
