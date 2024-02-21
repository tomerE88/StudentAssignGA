package com.example.Controller;

import java.util.*;

public class Population {
    
    private Individual[] individuals; // array of single solutions
    private double populationFitness = -1; // fitness of the population
    // add population size

    // constructors
    public Population(int populationSize) {
        this.individuals = new Individual[populationSize];
        // add individuals to the array with their chromosome length
        for (int i = 0; i < populationSize; i++) {
            this.individuals[i] = new Individual();
        }
    }

    // generate random population
    public Population generateRandomPopulation(int populationSize) {
        Population population = new Population(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual();
            individual.generateRandomIndividual();
            population.individuals[i] = individual;
        }
        return population;
    }

    // getters
    public Individual[] getIndividuals() {
        return this.individuals;
    }

    // return the indivisual with the best fitness
    public Individual getFittest() {
        double maxFit = -1;
        Individual maxInd = this.individuals[0];
        for (int i = 0; i < this.individuals.length; i++) {
            if (this.individuals[i].getFitness() > maxFit)
            {
                maxFit = this.individuals[i].getFitness();
                maxInd = this.individuals[i];
            }
        }
        // Return the fittest individual at the offset
        return maxInd;
    }

    // get rundom individual from array
    public Individual getIndividual(int randomIndex) {
        return this.individuals[randomIndex];
    }

    // return the number of individuals in a population
    public int populationSize() {
        return this.individuals.length;
    }

    // sum all the fitness of the individuals in the population
    public double sumFitness() {
        double sum = 0;
        for (int i = 0; i < this.individuals.length; i++) {
            sum += this.individuals[i].getFitness();
        }
        return sum;
    }

}
