package com.example.Model;

public class Major {
    // Attributes of the Major class
    public int majorID;
    protected String name;
    protected double requiredGrade; // Minimum grade required for this major

    // Constructor to initialize a new Major object
    public Major(int majorID, String name, double requiredGrade) {
        this.majorID = majorID;
        this.name = name;
        this.requiredGrade = requiredGrade;
    }

    // Getters and Setters for each attribute
    public int getMajorID() {
        return majorID;
    }

    public void setMajorID(int majorID) {
        this.majorID = majorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRequiredGrade() {
        return requiredGrade;
    }

    public void setRequiredGrade(double requiredGrade) {
        this.requiredGrade = requiredGrade;
    }

    // Method to display major information
    @Override
    public String toString() {
        return "Major{" +
               "majorID=" + majorID +
               ", name='" + name + '\'' +
               ", requiredGrade=" + requiredGrade +
               '}';
    }

    // Additional methods can be added as needed
}

