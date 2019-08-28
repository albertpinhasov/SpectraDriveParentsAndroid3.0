package com.spectradrive.Models;

import java.util.ArrayList;
import java.util.Date;

public class RideModel {
    private String rideId;
    private String rideName;
    private Date createdOn;
    private ArrayList<ChildModel> child;
    private DriverModel driver;
    private LocationModel dropPoint;
    private LocationModel pickupPoint;
    private ArrayList<LocationModel> locationData;
    private ArrayList<RideEventModel> events;

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public ArrayList<ChildModel> getChild() {
        return child;
    }

    public void setChild(ArrayList<ChildModel> child) {
        this.child = child;
    }

    public DriverModel getDriver() {
        return driver;
    }

    public void setDriver(DriverModel driver) {
        this.driver = driver;
    }

    public LocationModel getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(LocationModel dropPoint) {
        this.dropPoint = dropPoint;
    }

    public LocationModel getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(LocationModel pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public ArrayList<LocationModel> getLocationData() {
        return locationData;
    }

    public void setLocationData(ArrayList<LocationModel> locationData) {
        this.locationData = locationData;
    }

    public ArrayList<RideEventModel> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<RideEventModel> events) {
        this.events = events;
    }
}