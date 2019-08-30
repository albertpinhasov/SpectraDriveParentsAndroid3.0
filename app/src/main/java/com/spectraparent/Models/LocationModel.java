package com.spectraparent.Models;

public class LocationModel {
    private Double lat=0.0;
    private Double lon=0.0;
    private String name;

    public LocationModel(double lat, double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }
    public LocationModel(){}

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
