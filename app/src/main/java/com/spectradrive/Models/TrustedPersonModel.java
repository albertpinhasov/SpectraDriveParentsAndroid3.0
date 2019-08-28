package com.spectradrive.Models;

import java.util.ArrayList;

public class TrustedPersonModel {
    private String trustedPersonId;
    private String firstName;
    private String lastName;
    private String email;
    private String dOB;
    private String phoneNumber;
    private String address;
    private String relationToChild;
    private String otherRelationToChild;
    private ArrayList<PhotoModel> images;

    public String getTrustedPersonId() {
        return trustedPersonId;
    }

    public void setTrustedPersonId(String trustedPersonId) {
        this.trustedPersonId = trustedPersonId;
    }

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

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRelationToChild() {
        return relationToChild;
    }

    public void setRelationToChild(String relationToChild) {
        this.relationToChild = relationToChild;
    }

    public String getOtherRelationToChild() {
        return otherRelationToChild;
    }

    public void setOtherRelationToChild(String otherRelationToChild) {
        this.otherRelationToChild = otherRelationToChild;
    }

    public ArrayList<PhotoModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<PhotoModel> images) {
        this.images = images;
    }
}
