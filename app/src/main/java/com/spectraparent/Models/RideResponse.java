package com.spectraparent.Models;

import java.util.ArrayList;

public class RideResponse {
    boolean success;
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<RideModel> getData() {
        return data;
    }

    public void setData(ArrayList<RideModel> data) {
        this.data = data;
    }

    ArrayList<RideModel> data=new ArrayList();
}
