package com.spectraparent.Models;

import com.spectraparent.Helpers.Location;

import java.util.ArrayList;

public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String countryCode;
    private String token;
    private String birthday;
    private String profile;
    private String userIdStr;
    private LocationModel schoolLocation;
    private String DeviceType ;

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }

    private String DeviceToken ;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public LocationModel getSchoolLocation() {
        return schoolLocation;
    }

    public void setSchoolLocation(LocationModel schoolLocation) {
        this.schoolLocation = schoolLocation;
    }

    public LocationModel getLearningCenterLocation() {
        return learningCenterLocation;
    }

    public void setLearningCenterLocation(LocationModel learningCenterLocation) {
        this.learningCenterLocation = learningCenterLocation;
    }

    public LocationModel getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(LocationModel homeLocation) {
        this.homeLocation = homeLocation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private LocationModel learningCenterLocation;
    private LocationModel homeLocation;
    private String userId;
    private String title;
    private Photo photo;
    private boolean isFirstLogin;
    private String role;
    private int gender;

    private ArrayList<Child> child;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public ArrayList<Child> getChild() {
        return child;
    }

    public void setChild(ArrayList<Child> child) {
        this.child = child;
    }

    public ArrayList<TrustedPersonModel> getTrustedPersons() {
        return trustedPersons;
    }

    public void setTrustedPersons(ArrayList<TrustedPersonModel> trustedPersons) {
        this.trustedPersons = trustedPersons;
    }
}
