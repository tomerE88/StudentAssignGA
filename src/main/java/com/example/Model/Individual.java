package com.example.Model;

import java.util.ArrayList;
import java.util.HashMap;

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

    // // gets studentID and returns the student
    // public Student getStudentByID(String studentID) {
    //     // loop through all classrooms and get the student
    //     for (ClassRoom classroom : this.classrooms) {
    //         // check if the student is in the classroom
    //         if (classroom.getStudents().containsKey(studentID)) {
    //             return classroom.getStudents().get(studentID);
    //         }
    //     }
    //     // if the student is not found return null
    //     return null;
    // }

    // generate a random individual for the first population. add random students to the different classrooms
    public void generateRandomIndividual() {
        int numOfStudents, classNum = 0;
        MainGA mainga = new MainGA();
        ArrayList<Student> students = new ArrayList<Student>(mainga.getStudents().values());
        // number of students
        numOfStudents = students.size();

        // select random students for the classroom !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! the is not true because i loop for every random and for each one do it 10 times
        for (int i = 0; i < numOfStudents; i++) {
            int rand = (int) (Math.random() * (students.size()-1));
            // if passed the last class, go back to first class
            if (classNum == classrooms.length)
                classNum = 0;
            
            // add the student to the classroom
            this.classrooms[classNum++].addStudent(students.get(rand));
            // remove the student from the list
            students.remove(rand);
        }
        System.out.println("******************end of random individual******************");
    }

    // get a classroom and an index and add it to the individual at the index
    public void addClassroom(ClassRoom classroom, int index) {
        this.classrooms[index] = classroom;
    }

    // checks if every classroom is full instead of given one
    public boolean allClassesFull(ClassRoom excludedClassroom) {
        for (ClassRoom classroom : classrooms) {
            // Check if the current classroom is the excluded one by comparing memory addresses
            // and if any classroom, except the excluded one, is not full, return false
            if (classroom != excludedClassroom && !classroom.isFull()) {
                return false;
            }
        }
        // If all classrooms, except the excluded one, are full, return true
        return true;
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
