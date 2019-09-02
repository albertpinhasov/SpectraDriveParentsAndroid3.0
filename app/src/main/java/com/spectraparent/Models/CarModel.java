package com.spectraparent.Models;

public class CarModel {
    private String carId;

    private String make;

    private String number;

    private String brand;

    private String model;

    private String color;

    private String registrationNumber;

    private String vin;

    private String insuranceExpiryDate;

    private String licenseExpiryDate;

    private String createdOn;

    private String updatedOn;

    private boolean isActive;

    public void setCarId(String carId){
        this.carId = carId;
    }
    public String getCarId(){
        return this.carId;
    }
    public void setMake(String make){
        this.make = make;
    }
    public String getMake(){
        return this.make;
    }
    public void setNumber(String number){
        this.number = number;
    }
    public String getNumber(){
        return this.number;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setModel(String model){
        this.model = model;
    }
    public String getModel(){
        return this.model;
    }
    public void setColor(String color){
        this.color = color;
    }
    public String getColor(){
        return this.color;
    }
    public void setRegistrationNumber(String registrationNumber){
        this.registrationNumber = registrationNumber;
    }
    public String getRegistrationNumber(){
        return this.registrationNumber;
    }
    public void setVin(String vin){
        this.vin = vin;
    }
    public String getVin(){
        return this.vin;
    }
    public void setInsuranceExpiryDate(String insuranceExpiryDate){
        this.insuranceExpiryDate = insuranceExpiryDate;
    }
    public String getInsuranceExpiryDate(){
        return this.insuranceExpiryDate;
    }
    public void setLicenseExpiryDate(String licenseExpiryDate){
        this.licenseExpiryDate = licenseExpiryDate;
    }
    public String getLicenseExpiryDate(){
        return this.licenseExpiryDate;
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
