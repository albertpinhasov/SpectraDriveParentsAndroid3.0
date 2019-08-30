package com.spectraparent.Models;

import java.util.Date;

public class RideEventModel {
    private String rideEventId;
    private String eventName;
    private Date createdOn;

    public String getRideEventId() {
        return rideEventId;
    }

    public void setRideEventId(String rideEventId) {
        this.rideEventId = rideEventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
