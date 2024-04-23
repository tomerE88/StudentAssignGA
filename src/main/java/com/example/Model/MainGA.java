package com.example.Model;

import java.util.HashMap;

import com.example.Model.Database.DB;

public class MainGA {

    private DB db;
    private HashMap<String, Student> students;
    private ClassRoom[] classrooms;
    private SpecialRequest[] specialRequests;
    private HashMap<Integer, Major> majors;

    // setters and getters
    public HashMap<String, Student> getStudents() {
        return students;
    }

    public void setStudents(HashMap<String, Student> students) {
        this.students = students;
    }

    public ClassRoom[] getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(ClassRoom[] classrooms) {
        this.classrooms = classrooms;
    }

    public SpecialRequest[] getSpecialRequests() {
        return specialRequests;
    }

    public HashMap<Integer, Major> getMajors() {
        return this.majors;
    }

    public MainGA() {
         // get all students from the database
         this.db = new DB();
         this.students = this.db.getAllStudents();
         this.classrooms = this.db.getAllClassrooms();
         // for each student add their friends
         // also for each student add their major preferences
         for (Student student : this.students.values()) {
             student.setFriends(this.db.getFriendsFromID(student.getStudentID()));
             student.setMajorPreferences(this.db.getMajorPreferencesFromID(student.getStudentID()));
         }

         // get all majors from the database
         this.majors = db.getAllMajors();
         // get all special requests from the database
         this.specialRequests = this.db.getAllSpecialRequests();

         // close the sql connection
        db.disconnectSql();
    }
}
