package com.spectradrive.Models;

import java.util.ArrayList;

public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String token;
    private ArrayList<ChildModel> child;
    private ArrayList<TrustedPersonModel> trustedPersons;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<ChildModel> getChild() {
        return child;
    }

    public void setChild(ArrayList<ChildModel> child) {
        this.child = child;
    }

    public ArrayList<TrustedPersonModel> getTrustedPersons() {
        return trustedPersons;
    }

    public void setTrustedPersons(ArrayList<TrustedPersonModel> trustedPersons) {
        this.trustedPersons = trustedPersons;
    }
}
