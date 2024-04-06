package com.example.Model;

import java.util.*;

public class Population {
    
    protected ArrayList<Individual> individuals; // array of single solutions
    // private double populationFitness = -1; // fitness of the population!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // add population size

    // constructor that generate a random population
    public Population(int populationSize, ClassRoom[] classrooms, Collection<Student> students) {
        this.individuals = new ArrayList<Individual>(populationSize);
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

    // New constructor for an empty population
    public Population() {
        this.individuals = new ArrayList<>();
    }

    // getters
    public ArrayList<Individual> getIndividuals() {
        return this.individuals;
    }

    // return the indivisual with the best fitness
    public Individual getFittest() {
        double maxFit = -1;
        Individual maxInd = this.individuals.get(0);
        for (int i = 0; i < this.individuals.size(); i++) {
            if (this.individuals.get(i).getFitness() > maxFit)
            {
                maxFit = this.individuals.get(i).getFitness();
                maxInd = this.individuals.get(i);
            }
        }
        // Return the fittest individual at the offset
        return maxInd;
    }

    // get rundom individual from array
    public Individual getIndividual(int index) {
        return this.individuals.get(index);
    }

    // return the number of individuals in a population
    public int populationSize() {
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

    // order the individuals in the population by their fitness
    public void orderByFitness() {
        // use collections.sort to order the individuals by their fitness
        Collections.sort(this.individuals, new Comparator<Individual>() {
            @Override
            // compare the fitness of the individuals in descending order.
            // returns negetive number if o2 is less than o1, positive if greater and 0 if equal.
            public int compare(Individual o1, Individual o2) {
                // switched from o1.getFitness() - o2.getFitness() to o2.getFitness() - o1.getFitness() to sort in descending order from highest to lowest
                return Double.compare(o2.getFitness(), o1.getFitness());
            }
        });
    }

    // return the individual with the best fitness function
    public Individual getBestIndividual() {
        if (this.individuals.isEmpty()) {
            // If the population is empty, return null
            return null;
        }
        
        // Start with the first individual as the best candidate
        Individual bestIndividual = this.individuals.get(0);
        
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
}
