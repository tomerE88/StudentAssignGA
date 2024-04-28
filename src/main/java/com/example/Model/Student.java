package com.example.Model;

public class Student {
    
    //student ID
    protected String studentID;
    // student name
    protected String name;
    // student gender
    protected String gender;
    // student average grades
    protected double averageGrades;
    // city ID of student
    protected int cityID;
    // code type of student
    protected int codeType;
    // list of top 3 majors that the student wants (index 0 is the best and index 2 is least best)
    protected Major[] majorPreferences;
    // list of top 3 friends of student (index 0 is the best and index 2 is least best)
    protected Student[] friends;

    // Constructor to initialize a new Student object
    public Student(String studentID, String name, String gender, double averageGrades, int cityID, int codeType) {
        this.studentID = studentID;
        this.name = name;
        this.gender = gender;
        this.averageGrades = averageGrades;
        this.cityID = cityID;
        this.codeType = codeType;
    }

    public Student(Student student) {
        this.studentID = student.studentID;
        this.name = student.name;
        this.gender = student.gender;
        this.averageGrades = student.averageGrades;
        this.cityID = student.cityID;
        this.codeType = student.codeType;
    }

    public Student(String studentID, String name, String gender, double averageGrades, int cityID, int codeType, Student[] friends) {
        this.studentID = studentID;
        this.name = name;
        this.gender = gender;
        this.averageGrades = averageGrades;
        this.cityID = cityID;
        this.codeType = codeType;
        this.friends = friends;
    }

    // Getters and Setters for each attribute
    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
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
        }
    }

    public Student[] getFriends() {
        return this.friends;
    }

    public void setFriends(Student[] friends) {
        if (friends.length <= 3) {
            this.friends = friends;
        } else {
            // If more than 3 friends are provided, only take the first 3
            this.friends = new Student[3];
            this.friends = friends;
        }
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }
}

