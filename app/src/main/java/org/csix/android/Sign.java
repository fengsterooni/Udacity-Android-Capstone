package org.csix.android;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
