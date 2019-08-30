package com.spectraparent.Models;

public class DriverModel {
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AssignedCarModel getAssignedCar() {
        return assignedCar;
    }

    public void setAssignedCar(AssignedCarModel assignedCar) {
        this.assignedCar = assignedCar;
    }

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AssignedCarModel assignedCar;
}
