package com.spectraparent.Models;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RideModel {
    private String rideId;

    private List<ChildModel> child;

    private DriverModel driver;

    private String driverId;

    public List<ChildModel> getChild() {
        return child;
    }

    public void setChild(List<ChildModel> child) {
        this.child = child;
    }

    public int getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(int rideStatus) {
        this.rideStatus = rideStatus;
    }

    private int rideStatus;

    private List<LocationModel> locationData;

    private PoolData poolData;

    private String userId;

    private String rideName;

    private String metadata;

    private String comments;

    private Date createdOn;
    private Date scheduledRideOn;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private String updatedOn;
    private ArrayList<TrustedPersonModel> trustedPersons;

    public ArrayList<TrustedPersonModel> getTrustedPersons() {
        return trustedPersons;
    }

    public void setTrustedPersons(ArrayList<TrustedPersonModel> trustedPersons) {
        this.trustedPersons = trustedPersons;
    }

    private boolean isActive;

    public void setRideId(String rideId){
        this.rideId = rideId;
    }
    public String getRideId(){
        return this.rideId;
    }
    public void setChildModel(List<ChildModel> child){
        this.child = child;
    }
    public List<ChildModel> getChildModel(){
        return this.child;
    }
    public void setDriver(DriverModel driver){
        this.driver = driver;
    }
    public DriverModel getDriver(){
        return this.driver;
    }
    public void setDriverId(String driverId){
        this.driverId = driverId;
    }
    public String getDriverId(){
        return this.driverId;
    }
    public void setLocationData(List<LocationModel> locationData){
        this.locationData = locationData;
    }
    public List<LocationModel> getLocationData(){
        return this.locationData;
    }
    public void setPoolData(PoolData poolData){
        this.poolData = poolData;
    }
    public PoolData getPoolData(){
        return this.poolData;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setRideName(String rideName){
        this.rideName = rideName;
    }
    public String getRideName(){
        return this.rideName;
    }
    public void setMetadata(String metadata){
        this.metadata = metadata;
    }
    public String getMetadata(){
        return this.metadata;
    }
    public void setComments(String comments){
        this.comments = comments;
    }
    public String getComments(){
        return this.comments;
    }
    public void setCreatedOn(Date createdOn){
        this.createdOn = createdOn;
    }
    public void setSchedualOn(Date scheduledRideOn){
        this.scheduledRideOn = scheduledRideOn;
    }
    public Date getCreatedOn(){
        return this.createdOn;
    }
    public Date getSchendual(){
        return this.scheduledRideOn;
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