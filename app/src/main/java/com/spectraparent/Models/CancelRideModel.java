package com.spectraparent.Models;

public class CancelRideModel {
    String CancellationStartDate;
    String CancellationEndDate;
    String Reason;

    public String getCancellationStartDate() {
        return CancellationStartDate;
    }

    public void setCancellationStartDate(String cancellationStartDate) {
        CancellationStartDate = cancellationStartDate;
    }

    public String getCancellationEndDate() {
        return CancellationEndDate;
    }

    public void setCancellationEndDate(String cancellationEndDate) {
        CancellationEndDate = cancellationEndDate;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getRideID() {
        return RideID;
    }

    public void setRideID(String rideID) {
        RideID = rideID;
    }

    String RideID;

}
