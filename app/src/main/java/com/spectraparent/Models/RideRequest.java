package com.spectraparent.Models;

public class RideRequest {
    int pageNumber;
    int pageSize;
    String UserId;

    public RideRequest(int pageNumber, int pageSize, String userId) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        UserId = userId;
    }


}
