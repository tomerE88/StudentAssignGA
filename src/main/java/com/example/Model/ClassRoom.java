package com.example.Model;

import java.util.*;

public class ClassRoom {

    private int classID;
    private String className;
    private int maxStudents;
    private int minStudents;
    // Major associated with the class
    private int majorID;
    // array list of all students in the class
    private ArrayList<Student> students;

    // Constructor to initialize a new ClassRoom object
    public ClassRoom(int classID, String className, int maxStudents, int minStudents, int majorID) {
        this.classID = classID;
        this.className = className;
        this.maxStudents = maxStudents;
        this.minStudents = minStudents;
        this.majorID = majorID;
        this.students = new ArrayList<Student>();        
    }

    public ClassRoom(ClassRoom other) {
        this.classID = other.classID;
        this.className = other.className;
        this.maxStudents = other.maxStudents;
        this.minStudents = other.minStudents;
        this.majorID = other.majorID;
        this.students = new ArrayList<Student>(other.getStudents());
    }

    // generate random classroom for first population
    public ClassRoom generateRandomClassroom(int i) {
        int classID = i;
        String className = "Class" + classID;
        // int maxStudents = rand.nextInt(50);
        // int minStudents = rand.nextInt(50);
        // int majorID = rand.nextInt(100);
        return new ClassRoom(classID, className, maxStudents, minStudents, majorID);
    }


    public int getMajorID() {
        return majorID;
    }

    public void setMajorID(int majorID) {
        this.majorID = majorID;
    }

    
    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    
    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }


    public int getMinStudents() {
        return minStudents;
    }

    public void setMinStudents(int minStudents) {
        this.minStudents = minStudents;
    }

    // Getter and setter for the students array
    public ArrayList<Student> getStudents() {
        return this.students;
    }

    // // convert tyhe students to array and return array of students
    // public Student[] getStudentsArray() {
    //     return this.students.values().toArray(new Student[0]);
    // }

    public void setStudents(ArrayList<Student> students) {
        if (students.size() <= maxStudents) {
            this.students = students;
        } else {
            // Handle the case where more students are added than the maximum capacity
            System.out.println("Error: Attempting to add more students than the maximum capacity of the class.");
        }
    }

    // Method to add a single student to the class
    public void addStudent(Student student) {
        System.out.println("Adding student " + student.getStudentID() + " to class " + this.classID);
        if (this.students.size() < maxStudents) {
            this.students.add(student);
        } else {
            // Handle the case of exceeding maximum capacity
            System.out.println("Error: Class is already at maximum capacity. Cannot add more students.");
        }
    }

    // Method to get number of students in the class
    public int getNumStudents() {
        return this.students.size();
    }

    // switch between two students
    public void switchStudents(Student StudentRemove, Student StudentAdd) {
        // remove student from class
        this.students.remove(StudentRemove);
        // add the new student to the class
        this.students.add(StudentAdd);
    }

    // checks if the student is in the class
    public boolean isStudentInClass(Student student) {
        return this.students.contains(student);
    }

    // checks if the studentID is in the class
    public boolean isStudentInClass(String studentID) {
        for (Student student : this.students) {
            if (student.getStudentID().equals(studentID))
                return true; // Student found
        }
        return false; // Student not found
    }

    // clears all students from this classroom
    public void clearStudents() {
        this.students.clear();
    }

    // get student from index
    public Student getStudentFromIndex(int index) {
        return this.students.get(index);
    }

    // checks if the class if full
    public boolean isFull() {
        return getNumStudents() >= this.maxStudents;
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

