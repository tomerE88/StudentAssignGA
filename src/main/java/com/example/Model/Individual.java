package com.example.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.Model.Database.DB;

public class Individual {

    // magic numbers
    private static final int GET_LAST_INDEX = -1;

    
    // array of classrooms
    private ClassRoom[] classrooms;
    // fitness of the individual
    private double fitness = -1;

    // constructors
    public Individual(ClassRoom[] classrooms) {
        this.classrooms = classrooms;
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

    // generate a random individual for the first population. add random students to the different classrooms
    public void generateRandomIndividual(List<Student> students) {
        int numOfStudents, classNum = 0;
        
        List<Student> shuffledStudents = new ArrayList<>();
        shuffledStudents = shuffleStudents(students);
        
        // number of students
        numOfStudents = shuffledStudents.size();

        // select random students for the classroom
        for (int i = 0; i < numOfStudents; i++) {
            int rand = (int) (Math.random() * (shuffledStudents.size()-1));
            // if passed the last class, go back to first class
            if (classNum == classrooms.length)
                classNum = 0;
            
            // add the student to the classroom
            boolean added = this.classrooms[classNum++].addStudent(shuffledStudents.get(rand));
            // if student was added
            if (added) {
                // switch the student with the last student in the list
                shuffledStudents.set(rand, shuffledStudents.get(shuffledStudents.size() + GET_LAST_INDEX));
                // remove the student from the list
                shuffledStudents.remove(shuffledStudents.size()-1);
            }
        }
        System.out.println("******************end of random individual******************");
    }

    public ArrayList<Student> shuffleStudents(List<Student> students) {
        // Create a new ArrayList from the original list to avoid modifying it directly
        ArrayList<Student> shuffledStudents = new ArrayList<>(students);
        // Shuffle the new list
        Collections.shuffle(shuffledStudents);
        // Return the shuffled list
        return shuffledStudents;
    }

    // get a classroom and an index and add it to the individual at the index
    public void addClassroom(ClassRoom classroom, int index) {
        this.classrooms[index] = classroom;
    }

    /*
     * set classes to all students in the DB
     */
    public void setClassroomsInDB() {
         // DB instance
         DB db = new DB();
         // set the classes to the students
         db.setClassroomIDForAllStudents(this.classrooms);
         // close the connection
         db.disconnectSql();
    }

}
