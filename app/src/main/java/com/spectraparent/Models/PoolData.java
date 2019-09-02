package com.spectraparent.Models;

public class PoolData
{
    private String poolDataId;

    private DropPoint dropPoint;

    private String pickupPoint;

    private String createdOn;

    private String updatedOn;

    private boolean isActive;

    public void setPoolDataId(String poolDataId){
        this.poolDataId = poolDataId;
    }
    public String getPoolDataId(){
        return this.poolDataId;
    }
    public void setDropPoint(DropPoint dropPoint){
        this.dropPoint = dropPoint;
    }
    public DropPoint getDropPoint(){
        return this.dropPoint;
    }
    public void setPickupPoint(String pickupPoint){
        this.pickupPoint = pickupPoint;
    }
    public String getPickupPoint(){
        return this.pickupPoint;
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

