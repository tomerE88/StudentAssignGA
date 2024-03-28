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
    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

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

    public void setSpecialRequests(SpecialRequest[] specialRequests) {
        this.specialRequests = specialRequests;
    }

    public HashMap<Integer, Major> getMajors() {
        return this.majors;
    }

    public void setMajors(HashMap<Integer, Major> majors) {
        this.majors = majors;
    }

    public MainGA() {
        int counter = 0;
         // get all students from the database
         this.db = new DB();
         this.students = this.db.getAllStudents();
         this.classrooms = this.db.getAllClassrooms();
         // for each student add their friends
         for (Student student : this.students.values()) {
             student.setFriends(this.db.getFriendsFromID(student.getStudentID()));
             counter++;
         }
         System.out.println("hello hello hello hello hello hello hello hello hello" + counter);
         // for each student add their major preferences
         for (Student student : this.students.values()) {
             student.setMajorPreferences(this.db.getMajorPreferencesFromID(student.getStudentID()));
         }

         this.majors = db.getAllMajors();

         this.specialRequests = this.db.getAllSpecialRequests();

         // close the sql connection
        db.disconnectSql();
    }

    public static void main(String[] args) {
        MainGA mainga = new MainGA();

        System.out.println("**************************************************************************");
        // print the student with the key 100000002
        System.out.println("student 100000002: " + mainga.students.get("100000002"));
        Student friends[] = mainga.students.get("100000002").getFriends();
        System.out.println("friends of student 100000002: " + friends[0].getStudentID() + " " + friends[1].getStudentID() + " " + friends[2].getStudentID());
        Major majorPreferences[] = mainga.students.get("100000002").getMajorPreferences();
        System.out.println("major preferences of student 100000002: " + majorPreferences[0].getMajorID() + " " + majorPreferences[1].getMajorID() + " " + majorPreferences[2].getMajorID());
        // print the classroom with the key 2
        System.out.println("classroom2: " + mainga.classrooms[2]);
        System.out.println("**************************************************************************");
        System.out.println(mainga.specialRequests[0].getStudentID1() + " " + mainga.specialRequests[0].getStudentID2() + " " + mainga.specialRequests[0].getReason());



        // Create GA object
        ClassroomAssignGA ga = new ClassroomAssignGA(100, 100, 0.01, 0.9, 2);
        // Initialize population
        Population population = ga.getPopulation();
        // // Evaluate population
        // ga.evalPopulation(population);
        
        // Print fitness
        // System.out.println("Found solution in " + generation + " generations");
        System.out.println("Best solution: " + population.getFittest().getFitness());
        System.out.println("Best solution: " + population.getFittest().toString());

        // Close the database connection
        mainga.db.disconnectSql();
    }
}
