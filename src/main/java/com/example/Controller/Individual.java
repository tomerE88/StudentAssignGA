package com.example.Controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.example.Controller.Database.DB;

public class Individual {

    private ClassRoom[] classrooms;

    private double fitness = -1;
    private static final int numClassrooms = 5;

    // constructors
    public Individual(ClassRoom[] classrooms) {
        this.classrooms = classrooms;
    }

    public Individual() {
        this.classrooms = new ClassRoom[numClassrooms];
        for (int i = 0; i < numClassrooms; i++) {
            //this.classrooms[i] = getClassRoomFromID(i + 1);
        }
    }

    // getters
    public double getFitness() {
        // return the fitness
        return this.fitness;
    }

    public ClassRoom getClassroom(int index) {
        return this.classrooms[index];
    }

    public ClassRoom[] getClassrooms() {
        return this.classrooms;
    }

    public int getClassroomsLength() {
        return this.classrooms.length;
    }

    // setters
    public void setClassrooms(ClassRoom[] classrooms) {
        this.classrooms = classrooms;
    }
    

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void generateRandomIndividual() {
        for (int i = 0; i < numClassrooms; i++) {
            // this.classrooms[i] = generateRandomClassroom();
        }
    }

    // get a classroom and an index and add it to the individual at the index
    public void addClassroom(ClassRoom classroom, int index) {
        this.classrooms[index] = classroom;
    }
// yup baby
}
