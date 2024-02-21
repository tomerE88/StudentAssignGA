package com.example.Controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Student {
    // Attributes of the Student class
    protected int studentID;
    protected String name;
    protected String gender;
    protected double averageGrades;
    protected int cityID;
    // list of top 3 majors that the student wants (index 0 is the best and index 2 is least best)
    protected Major[] majorPreferences;
    // list of top 3 friends of student (index 0 is the best and index 2 is least best)
    protected int[] friends;

    // Constructor to initialize a new Student object
    public Student(int studentID, String name, String gender, double averageGrades, int cityID) {
        this.studentID = studentID;
        this.name = name;
        this.gender = gender;
        this.averageGrades = averageGrades;
        this.cityID = cityID;
        // DB.preferencesForStudent(this.majorPreferences, studentID); // fix this!!!!!!!!!!!!
        this.friends = friends;
    }

    public Student(int studentID, String name, String gender, double averageGrades, int cityID, int[] friends) {
        this.studentID = studentID;
        this.name = name;
        this.gender = gender;
        this.averageGrades = averageGrades;
        this.cityID = cityID;
        //this.majorPreferences;
        this.friends = friends;
    }

    // Getters and Setters for each attribute
    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getAverageGrades() {
        return averageGrades;
    }

    public void setAverageGrades(double averageGrades) {
        this.averageGrades = averageGrades;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public Major[] getMajorPreferences() {
        return majorPreferences;
    }

    public void setMajorPreferences(Major[] majorPreferences) {
        if (majorPreferences.length <= 3) {
            this.majorPreferences = majorPreferences;
        } else {
            // If more than 3 preferences are provided, only take the first 3
            // this.majorPreferences = new ArrayList<>(majorPreferences.subList(0, 3));
        }
    }

    public int[] getFriends() {
        return friends;
    }

    public void setFriends(int[] friends) {
        if (friends.length <= 3) {
            this.friends = friends;
        } else {
            // If more than 3 friends are provided, only take the first 3
            this.friends = new int[3];
            this.friends = friends;
        }
    }

    // Method to display student information
    @Override
    public String toString() {
        return "Student{" +
               "studentID=" + studentID +
               ", name='" + name + '\'' +
               ", gender='" + gender + '\'' +
               ", averageGrades=" + averageGrades +
               ", cityID=" + cityID +
               ", majorPreferences=" + majorPreferences +
               ", friends=" + friends +
               '}';
    }


    public static void main(String[] args) {
        // Student st = new Student();
        // Major[] m = new Major[3];
        // preferencesForStudent(m);
        // System.out.println(m[0]);
        // System.out.println(m[1]);
        // System.out.println(m[2]);
        
    }
}

