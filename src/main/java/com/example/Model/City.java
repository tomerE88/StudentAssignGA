package com.example.Model;

public class City {
    
    // city ID
    private int cityID;
    // city name
    private String name;

    // Constructor to initialize a new City object
    public City(int cityID, String name) {
        this.cityID = cityID;
        this.name = name;
    }

    // Getters
    public int getCityID() {
        return cityID;
    }

    public String getName() {
        return name;
    }

}
