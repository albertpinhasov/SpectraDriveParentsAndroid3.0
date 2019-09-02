package com.spectraparent.Models;

public class Photo
{
    private String photoId;

    private String smallPhotoUrl;

    private String standardPhotoUrl;

    private String name;

    private String createdOn;

    public void setPhotoId(String photoId){
        this.photoId = photoId;
    }
    public String getPhotoId(){
        return this.photoId;
    }
    public void setSmallPhotoUrl(String smallPhotoUrl){
        this.smallPhotoUrl = smallPhotoUrl;
    }
    public String getSmallPhotoUrl(){
        return this.smallPhotoUrl;
    }
    public void setStandardPhotoUrl(String standardPhotoUrl){
        this.standardPhotoUrl = standardPhotoUrl;
    }
    public String getStandardPhotoUrl(){
        return this.standardPhotoUrl;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setCreatedOn(String createdOn){
        this.createdOn = createdOn;
    }
    public String getCreatedOn(){
        return this.createdOn;
    }
}

