package com.example.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.Model.Database.DB;

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
            if (added)
                // remove the student from the list
                shuffledStudents.remove(rand);
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
