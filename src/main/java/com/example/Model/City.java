package com.example.Model;

public class City {
    // Attributes of the City class
    private int cityID;
    private String name;

    // Constructor to initialize a new City object
    public City(int cityID, String name) {
        this.cityID = cityID;
        this.name = name;
    }

    // Getters and Setters for each attribute
    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Method to display city information
    @Override
    public String toString() {
        return "City{" +
               "cityID=" + cityID +
               ", name='" + name + '\'' +
               '}';
    }

    // Additional methods can be added as per your requirements
}
