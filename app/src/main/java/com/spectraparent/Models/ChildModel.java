package com.spectraparent.Models;

import java.util.List;

public class ChildModel {
    private Child child;

    private Pickup pickup;

    private Drop drop;

    private String contacts;

    private List<String> events;

    private int index;

    public void setChild(Child child){
        this.child = child;
    }
    public Child getChild(){
        return this.child;
    }
    public void setPickup(Pickup pickup){
        this.pickup = pickup;
    }
    public Pickup getPickup(){
        return this.pickup;
    }
    public void setDrop(Drop drop){
        this.drop = drop;
    }
    public Drop getDrop(){
        return this.drop;
    }
    public void setContacts(String contacts){
        this.contacts = contacts;
    }
    public String getContacts(){
        return this.contacts;
    }
    public void setEvents(List<String> events){
        this.events = events;
    }
    public List<String> getEvents(){
        return this.events;
    }
    public void setIndex(int index){
        this.index = index;
    }
    public int getIndex(){
        return this.index;
    }

}
