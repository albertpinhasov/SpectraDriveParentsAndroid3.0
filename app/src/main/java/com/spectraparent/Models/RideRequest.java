package com.spectraparent.Models;

public class RideRequest {
    int pageNumber;
    int pageSize;
    String UserId;
    int RideType;
    public RideRequest(int pageNumber, int pageSize, String userId,int RideType) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        UserId = userId;
        this.RideType=RideType;
    }


}
