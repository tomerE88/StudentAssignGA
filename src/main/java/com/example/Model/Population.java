package com.example.Model;

import java.util.*;

public class Population {
    
    private static final int FIRST_INDIVIDUAL = 0;
    private static final int GET_LAST_INDEX = -1;
    // array of single solutions
    protected ArrayList<Individual> individuals;
    // number of individuals in the population
    protected int populationSize;

    // constructor that generate a population with a specific size
    public Population(int populationSize) {
        this.populationSize = populationSize;
        this.individuals = new ArrayList<Individual>(populationSize);
    }

    // New constructor for an empty population
    public Population() {
        this.individuals = new ArrayList<>();
    }

    // constructor that generate a random population
    public void randomPopulation(ClassRoom[] classrooms, Collection<Student> students) {
        ArrayList<Student> studentsList = new ArrayList<>(students);

        // loop through all the individuals in the population
        for (int i = 0; i < populationSize; i++) {
            // Create a new list for the classrooms that will be passed to the new individual
            ClassRoom[] classroomsForIndividual = new ClassRoom[classrooms.length];

            // Populate the new array with copies of the ClassRoom instances
            for (int j = 0; j < classrooms.length; j++) {
                classroomsForIndividual[j] = new ClassRoom(classrooms[j]); // Use the copy constructor
            }

            Individual individual = new Individual(classroomsForIndividual);
            // generate a random individual
            individual.generateRandomIndividual(studentsList);
            // add the individual to the population
            this.individuals.add(individual);
        }
    }

    // getters
    public ArrayList<Individual> getIndividuals() {
        return this.individuals;
    }

    // get rundom individual from array
    public Individual getIndividual(int index) {
        return this.individuals.get(index);
    }

    // return the number of individuals in a population
    public int getPopulationSize() {
        return this.individuals.size();
    }

    // sum all the fitness of the individuals in the population
    public double sumFitness() {
        double sum = 0;
        for (int i = 0; i < this.individuals.size(); i++) {
            sum += this.individuals.get(i).getFitness();
        }
        return sum;
    }

    // order the individuals in the population by their fitness in ascending order
    public void orderByFitness() {
        // use collections.sort to order the individuals by their fitness
        Collections.sort(this.individuals, new Comparator<Individual>() {
            @Override
            // compare the fitness of the individuals in descending order.
            // returns negetive number if o2 is less than o1, positive if greater and 0 if equal.
            public int compare(Individual o1, Individual o2) {
                return Double.compare(o1.getFitness(), o2.getFitness());
            }
        });
    }

    // return the individual with the best fitness
    public Individual getBestIndividual() {
        if (this.individuals.isEmpty()) {
            // If the population is empty, return null
            return null;
        }
        
        // Start with the first individual as the best candidate
        Individual bestIndividual = this.individuals.get(FIRST_INDIVIDUAL);
        
        // Iterate through the population to find the individual with the highest fitness
        for (Individual individual : this.individuals) {
            if (individual.getFitness() > bestIndividual.getFitness()) {
                // If the current individual has a higher fitness, update bestIndividual
                bestIndividual = individual;
            }
        }
        
        // Return the individual with the highest fitness
        return bestIndividual;
    }

    /*
     * switch between the individual in the index the function got and the last individual in the population
    */
    public void switchIndividuals(int index) {
        // get the last individual in the population
        Individual lastIndividual = this.individuals.get(this.individuals.size() + GET_LAST_INDEX);
        // get the individual in the index
        Individual individual = this.individuals.get(index);
        // switch the individuals
        this.individuals.set(index, lastIndividual);
        this.individuals.set(this.individuals.size() + GET_LAST_INDEX, individual);
    }

}
