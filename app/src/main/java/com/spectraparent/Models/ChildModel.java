package com.spectraparent.Models;

import java.util.ArrayList;

public class ChildModel {
    private String childId;
    private String firstName;
    private String lastName;
    private String about;
    private ArrayList<PhotoModel> images;
    private String specialNeeds;
    private String otherSpecialNeeds;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public ArrayList<PhotoModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<PhotoModel> images) {
        this.images = images;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }

    public void setSpecialNeeds(String specialNeeds) {
        this.specialNeeds = specialNeeds;
    }

    public String getOtherSpecialNeeds() {
        return otherSpecialNeeds;
    }

    public void setOtherSpecialNeeds(String otherSpecialNeeds) {
        this.otherSpecialNeeds = otherSpecialNeeds;
    }


}
