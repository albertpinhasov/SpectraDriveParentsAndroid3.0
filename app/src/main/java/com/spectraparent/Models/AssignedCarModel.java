package com.spectraparent.Models;

public class AssignedCarModel {
    private String assignedCarId;

    private CarModel car;

    private String createdOn;

    private String updatedOn;

    private boolean isActive;

    public void setAssignedCarId(String assignedCarId) {
        this.assignedCarId = assignedCarId;
    }

    public String getAssignedCarId() {
        return this.assignedCarId;
    }

    public void setCar(CarModel car) {
        this.car = car;
    }

    public CarModel getCar() {
        return this.car;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedOn() {
        return this.updatedOn;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return this.isActive;
    }
}

