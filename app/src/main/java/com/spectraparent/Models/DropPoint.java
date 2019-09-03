package com.spectraparent.Models;

public class DropPoint
{
    private String locationId;

    private double lat;

    private double lon;

    private String name;

    private String googleJson;

    private String createdOn;

    private String updatedOn;

    private boolean isActive;

    public void setLocationId(String locationId){
        this.locationId = locationId;
    }
    public String getLocationId(){
        return this.locationId;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public double getLat(){
        return this.lat;
    }
    public void setLon(double lon){
        this.lon = lon;
    }
    public double getLon(){
        return this.lon;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setGoogleJson(String googleJson){
        this.googleJson = googleJson;
    }
    public String getGoogleJson(){
        return this.googleJson;
    }
    public void setCreatedOn(String createdOn){
        this.createdOn = createdOn;
    }
    public String getCreatedOn(){
        return this.createdOn;
    }
    public void setUpdatedOn(String updatedOn){
        this.updatedOn = updatedOn;
    }
    public String getUpdatedOn(){
        return this.updatedOn;
    }
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    public boolean getIsActive(){
        return this.isActive;
    }
}

