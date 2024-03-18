package com.example.Controller;

import java.util.*;

public class Population {
    
    protected ArrayList<Individual> individuals; // array of single solutions
    private double populationFitness = -1; // fitness of the population
    // add population size

    // constructor that generate a random population
    public Population(int populationSize) {
        this.individuals = new ArrayList<Individual>(populationSize);
        // add individuals to the array with their chromosome length
        MainGA mainga = new MainGA();

        // loop through all the individuals in the population
        for (int i = 0; i < populationSize; i++) {
            // Create a new list for the classrooms that will be passed to the new individual
            ClassRoom[] classroomsForIndividual = new ClassRoom[mainga.getClassrooms().length];

            // Populate the new array with copies of the ClassRoom instances
            for (int j = 0; j < mainga.getClassrooms().length; j++) {
                classroomsForIndividual[j] = new ClassRoom(mainga.getClassrooms()[j]); // Use the copy constructor
            }

            Individual individual = new Individual(classroomsForIndividual);
            // generate a random individual
            individual.generateRandomIndividual();
            // add the individual to the population
            this.individuals.add(individual);
        }
    }

    // New constructor for an empty population
    public Population(boolean createEmptyPopulation) {
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
    public Individual getIndividual(int randomIndex) {
        return this.individuals.get(randomIndex);
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
        Collections.sort(this.individuals, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                return Double.compare(o1.getFitness(), o2.getFitness());
            }
        });
    }
}
