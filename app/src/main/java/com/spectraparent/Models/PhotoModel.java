package com.spectraparent.Models;

import java.util.Date;

public class PhotoModel {
    private String photoId;
    private String smallPhotoUrl;
    private String standardPhotoUrl;
    private Date createdOn;

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getSmallPhotoUrl() {
        return smallPhotoUrl;
    }

    public void setSmallPhotoUrl(String smallPhotoUrl) {
        this.smallPhotoUrl = smallPhotoUrl;
    }

    public String getStandardPhotoUrl() {
        return standardPhotoUrl;
    }

    public void setStandardPhotoUrl(String standardPhotoUrl) {
        this.standardPhotoUrl = standardPhotoUrl;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }


}
