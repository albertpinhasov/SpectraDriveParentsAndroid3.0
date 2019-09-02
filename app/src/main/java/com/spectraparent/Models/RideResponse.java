package com.spectraparent.Models;

import java.util.List;

public class RideResponse {

    private boolean success;

    private String message;

    private List<RideModel> data;

    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setData(List<RideModel> data){
        this.data = data;
    }
    public List<RideModel> getData(){
        return this.data;
    }
}
