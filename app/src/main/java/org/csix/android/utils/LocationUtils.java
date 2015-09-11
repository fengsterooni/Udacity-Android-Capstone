package org.csix.android.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class LocationUtils {
    public static LatLng getAddress(Context context, String address) {
        Geocoder coder = new Geocoder(context);
        LatLng latLng = null;
        try {
            List<Address> adresses = coder
                    .getFromLocationName(address, 1);
            for (Address add : adresses) {
                // as country etc.
                double longitude = add.getLongitude();
                double latitude = add.getLatitude();
                latLng = new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}
