package com.example.Controller;

import java.util.Map;

import com.example.Controller.Database.DB;

public class MainGA {

    private Map<String, Student> students;
    private Map<String, ClassRoom> classrooms;
    private DB db;

    public static void main(String[] args) {
        MainGA mainga = new MainGA();
        // get all students from the database
        DB db = new DB();
        // Connect to the database
        db.connectSql();
        mainga.students = db.getAllStudents();
        mainga.classrooms = db.getAllClassrooms();


        System.out.println("**************************************************************************");
        // print the student with the key 100000002
        System.out.println("student 100000002: " + mainga.students.get("100000002"));
        // print the classroom with the key 2
        System.out.println("classroom2: " + mainga.classrooms.get("2"));
        System.out.println("**************************************************************************");



        // Create GA object
        ClassroomAssignGA ga = new ClassroomAssignGA(100, 100, 0.01, 0.9, 2);
        // Initialize population
        Population population = ga.getPopulation();
        // Evaluate population
        ga.evalPopulation(population);
        
        // Print fitness
        // System.out.println("Found solution in " + generation + " generations");
        System.out.println("Best solution: " + population.getFittest().getFitness());
        System.out.println("Best solution: " + population.getFittest().toString());

        // Close the database connection
        db.disconnectSql();
    }
}
