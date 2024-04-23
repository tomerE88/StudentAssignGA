package com.example.Model;

import java.util.*;

public class ClassRoom {

    private int classID;
    private String className;
    private int maxStudents;
    private int minStudents;
    // Major associated with the class
    private int majorID;
    // hashmap of all students in the class and their index in the class
    private HashMap<Integer, Student> students;

    // Constructor to initialize a new ClassRoom object
    public ClassRoom(int classID, String className, int maxStudents, int minStudents, int majorID) {
        this.classID = classID;
        this.className = className;
        this.maxStudents = maxStudents;
        this.minStudents = minStudents;
        this.majorID = majorID;
        // this.students = new ArrayList<Student>();    
        this.students = new HashMap<Integer, Student>();    
    }

    public ClassRoom(ClassRoom other) {
        this.classID = other.classID;
        this.className = other.className;
        this.maxStudents = other.maxStudents;
        this.minStudents = other.minStudents;
        this.majorID = other.majorID;
        this.students = new HashMap<Integer, Student>();
        for (int i = 0; i < other.students.size(); i++) {
            this.students.put(i, other.students.get(i));
        }
    }

    public ClassRoom (ClassRoom other, boolean noClasses) {
        this.classID = other.classID;
        this.className = other.className;
        this.maxStudents = other.maxStudents;
        this.minStudents = other.minStudents;
        this.majorID = other.majorID;
        this.students = new HashMap<Integer, Student>();
    }

    // generate random classroom for first population
    public ClassRoom generateRandomClassroom(int i) {
        int classID = i;
        String className = "Class" + classID;
        return new ClassRoom(classID, className, maxStudents, minStudents, majorID);
    }

    public int getMajorID() {
        return majorID;
    }

    public int getClassID() {
        return classID;
    }

    public String getClassName() {
        return className;
    }
    
    public int getMaxStudents() {
        return maxStudents;
    }

    public int getMinStudents() {
        return minStudents;
    }

    // Getter and setter for the students hashmap
    public HashMap<Integer, Student> getStudents() {
        return this.students;
    }

    // Method to add a single student to the class
    public boolean addStudent(Student student) {
        // System.out.println("Adding student " + student.getStudentID() + " to class " + this.classID);
        if (this.students.size() < maxStudents) {
            this.students.put(this.students.size(), student);
            // this.students.add(student);
            return true;
        } 
        // Handle the case of exceeding maximum capacity
        System.out.println("Error: Class is already at maximum capacity. Cannot add more students.");
        return false;
    }

    // Method to get number of students in the class
    public int getNumStudents() {
        return this.students.size();
    }

    // switch between two students
    public void switchStudents(int removeIndex, Student StudentRemove, Student StudentAdd) {
        // remove student from class
        this.students.remove(removeIndex);
        // add the new student to the class
        this.students.put(removeIndex, StudentAdd);
        // this.students.add(StudentAdd);
    }

    // checks if the student is in the class
    public boolean isStudentInClass(Student student) {
        // check if the student is in the class
        if (this.students.containsValue(student))
            return true;
        return false;
    }

    // checks if the studentID is in the class
    public boolean isStudentInClass(String studentID) {
        for (Student student : this.students.values()) {
            if (student.getStudentID().equals(studentID))
                return true; // Student found
        }
        return false; // Student not found
    }

    

    // get student from index
    public Student getStudentFromIndex(int index) {
        return this.students.get(index);
    }

    // checks if the class if full
    public boolean isFull() {
        return getNumStudents() >= this.maxStudents;
    }
    
}

