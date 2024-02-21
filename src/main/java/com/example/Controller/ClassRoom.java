package com.example.Controller;

import java.util.*;

public class ClassRoom {

    private int classID;
    private String className;
    private int maxStudents;
    private int minStudents;
    private int majorID; // Major associated with the class
    // list of all students in the class
    private List<Student> students;

    // Constructor to initialize a new ClassRoom object
    public ClassRoom(int classID, String className, int maxStudents, int minStudents, int majorID) {
        this.classID = classID;
        this.className = className;
        this.maxStudents = maxStudents;
        this.minStudents = minStudents;
        this.majorID = majorID;        
    }

    // generate random classroom for first population
    public ClassRoom generateRandomClassroom(int i) {
        int classID = i;
        String className = "Class" + classID;
        int maxStudents = rand.nextInt(50);
        int minStudents = rand.nextInt(50);
        int majorID = rand.nextInt(100);
        return new ClassRoom(classID, className, maxStudents, minStudents, majorID);
    }

    // Getter and setter for the majorID
    public int getMajorID() {
        return majorID;
    }

    public void setMajorID(int majorID) {
        this.majorID = majorID;
    }

    // Getter and setter for the students array
    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        if (students.size() <= maxStudents) {
            this.students = students;
        } else {
            // Handle the case where more students are added than the maximum capacity
            System.out.println("Error: Attempting to add more students than the maximum capacity of the class.");
        }
    }

    // Method to add a single student to the class
    public void addStudent(Student student) {
        if (this.students.size() < maxStudents) {
            this.students.add(student);
        } else {
            // Handle the case of exceeding maximum capacity
            System.out.println("Error: Class is already at maximum capacity. Cannot add more students.");
        }
    }

    // Method to display class room information
    @Override
    public String toString() {
        return "ClassRoom{" +
               "classID=" + classID +
               ", className='" + className + '\'' +
               ", maxStudents=" + maxStudents +
               ", majorID=" + majorID +
               ", students=" + students +
               '}';
    }
    
}

