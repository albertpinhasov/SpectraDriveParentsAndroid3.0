package com.spectraparent.Models;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.List;

public class Child implements Serializable {
    private String childId;

    private String firstName;

    private String lastName;

    private String fullName;

    private String about;

    private List<Images> images;

    private String specialNeeds;

    private String otherSpecialNeeds;

    private String parent;

    public void setChildId(String childId){
        this.childId = childId;
    }
    public String getChildId(){
        return this.childId;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public String getFullName(){
        return this.fullName;
    }
    public void setAbout(String about){
        this.about = about;
    }
    public String getAbout(){
        return this.about;
    }
    public void setImages(List<Images> images){
        this.images = images;
    }
    public List<Images> getImages(){
        return this.images;
    }
    public void setSpecialNeeds(String specialNeeds){
        this.specialNeeds = specialNeeds;
    }
    public String getSpecialNeeds(){
        return this.specialNeeds;
    }
    public void setOtherSpecialNeeds(String otherSpecialNeeds){
        this.otherSpecialNeeds = otherSpecialNeeds;
    }
    public String getOtherSpecialNeeds(){
        return this.otherSpecialNeeds;
    }
    public void setParent(String parent){
        this.parent = parent;
    }
    public String getParent(){
        return this.parent;
    }

    @Override
    public String toString() {
        return "Child{" +
                "childId='" + childId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", about='" + about + '\'' +
                ", images=" + images +
                ", specialNeeds='" + specialNeeds + '\'' +
                ", otherSpecialNeeds='" + otherSpecialNeeds + '\'' +
                ", parent='" + parent + '\'' +
                '}';
    }
}
