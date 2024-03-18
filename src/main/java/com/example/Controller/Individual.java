package com.example.Controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.Controller.MainGA;

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

    // gets studentID and returns the student
    public Student getStudentByID(String studentID) {
        // loop through all classrooms and get the student
        for (ClassRoom classroom : this.classrooms) {
            // check if the student is in the classroom
            if (classroom.getStudents().containsKey(studentID)) {
                return classroom.getStudents().get(studentID);
            }
        }
        // if the student is not found return null
        return null;
    }

    // generate a random individual for the first population. add random students to the different classrooms
    public void generateRandomIndividual() {
        MainGA mainga = new MainGA();
        ArrayList<Student> students = new ArrayList<Student>(mainga.getStudents().values());

        // select random students for the classroom !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! the is not true because i loop for every random and for each one do it 10 times
        for (int i = 0; i < students.size(); i++) {
            // loops for all classrooms
            for (int j = 0; j < classrooms.length; j++) {
                int rand = (int) (Math.random() * (students.size()-1));
                // add one student to each class at a time
                this.classrooms[j].addStudent(students.get(rand));
                // remove the student from the list
                students.remove(rand);
            }
        }
    }

    // get a classroom and an index and add it to the individual at the index
    public void addClassroom(ClassRoom classroom, int index) {
        this.classrooms[index] = classroom;
    }

    public static void main(String[] args) {
        MainGA mainga = new MainGA();
        Individual individual = new Individual(mainga.getClassrooms());
        System.out.println("**************************************");
        individual.generateRandomIndividual();
        System.out.println("**************************************");
        System.out.println("Individual: " + individual.getClassroom(0).getStudents());


        // // create evaluator and get values from individual
        // FitnessEvaluator evaluator = new FitnessEvaluator(mainga.getStudents().values().toArray(new Student[0]), individual.getClassrooms());
        // System.out.println("**************************************");
        // System.out.println(evaluator.getStudents()[0].getMajorPreferences()[0] + " " + evaluator.getStudents()[0].getMajorPreferences()[1] + " " + evaluator.getStudents()[0].getMajorPreferences()[2]);
        // double fitnessScore = evaluator.fitnessFunction(1, 2, 3, 4, 5, 6, 7);
        // System.out.println("Calculated fitness score: " + fitnessScore);
    }

}
