package com.spectraparent.Models;

import java.util.Date;
import java.util.List;

public class ChildModel {
    private Child child;

    private Pickup pickup;

    private Drop drop;

    private String contacts;

    public Date getPickedupTime() {
        return pickedupTime;
    }

    public void setPickedupTime(Date pickedupTime) {
        this.pickedupTime = pickedupTime;
    }

    private List<RideEventModel> events;
    Date pickedupTime;
    private int index;

    public void setChild(Child child) {
        this.child = child;
    }

    public Child getChild() {
        return this.child;
    }

    public void setPickup(Pickup pickup) {
        this.pickup = pickup;
    }

    public Pickup getPickup() {
        return this.pickup;
    }

    public void setDrop(Drop drop) {
        this.drop = drop;
    }

    public Drop getDrop() {
        return this.drop;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContacts() {
        return this.contacts;
    }

    public void setEvents(List<RideEventModel> events) {
        this.events = events;
    }

    public List<RideEventModel> getEvents() {
        return this.events;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

}
