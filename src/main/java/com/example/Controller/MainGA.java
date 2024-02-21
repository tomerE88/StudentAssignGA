package com.example.Controller;

import java.util.Map;

import com.example.Controller.DB;
import com.example.Controller.Student;
import com.example.Controller.ClassroomAssignGA;
import com.example.Controller.Population;

public class MainGA {

    private Map<Integer, Student> students;

    public static void main(String[] args) {
        // Create GA object
        ClassroomAssignGA ga = new ClassroomAssignGA(100, 100, 0.01, 0.9, 2);
        // Initialize population
        Population population = ga.initPopulation();
        // Evaluate population
        ga.evalPopulation(population);
        
        // Print fitness
        // System.out.println("Found solution in " + generation + " generations");
        System.out.println("Best solution: " + population.getFittest().getFitness());
        System.out.println("Best solution: " + population.getFittest().toString());
    }
}
