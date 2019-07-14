package com.dimaoprog.newsapiapp.models;

import java.util.HashMap;
import java.util.Map;

public class UserDetails {

    private String firstName;
    private String secondName;
    private String telNumber;
    private String birthDay;
    private String country;
    private String city;
    private String selectedSources;

    public UserDetails() {
    }

    public UserDetails(String firstName, String secondName, String telNumber,
                       String birthDay, String country, String city, String selectedSources) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.telNumber = telNumber;
        this.birthDay = birthDay;
        this.country = country;
        this.city = city;
        this.selectedSources = selectedSources;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSelectedSources() {
        return selectedSources;
    }

    public void setSelectedSources(String selectedSources) {
        this.selectedSources = selectedSources;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("firstName", firstName);
        userMap.put("secondName", secondName);
        userMap.put("telNumber", telNumber);
        userMap.put("birthDay", birthDay);
        userMap.put("country", country);
        userMap.put("city", city);
        userMap.put("selectedSources", selectedSources);
        return userMap;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", selectedSources='" + selectedSources + '\'' +
                '}';
    }
}
