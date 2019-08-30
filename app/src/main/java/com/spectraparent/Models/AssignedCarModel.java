package com.spectraparent.Models;

public class AssignedCarModel {
    public String getAssignedCarId() {
        return assignedCarId;
    }

    public void setAssignedCarId(String assignedCarId) {
        this.assignedCarId = assignedCarId;
    }

    public CarModel getCar() {
        return car;
    }

    public void setCar(CarModel car) {
        this.car = car;
    }

    private String assignedCarId;
    private CarModel car;
}
