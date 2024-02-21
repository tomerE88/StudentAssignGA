package com.example.Controller;

import java.util.List;
import java.util.Random;
import com.example.Controller.Population;

public class ClassroomAssignGA {

    private int populationSize;
    private int maxGenerations;
    private double mutationRate;
    private double crossoverRate;
    private int eliteCount;
    public static int countGenerations;
    private Population pop;

    // constructor
    public ClassroomAssignGA(int populationSize, int maxGenerations, double mutationRate, double crossoverRate,
     int eliteCount) {
        ClassroomAssignGA.countGenerations = 0;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.eliteCount = eliteCount;
        this.pop = initPopulation();

    }

    // setters
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setcrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public void setEliteCount(int eliteCount) {
        this.eliteCount = eliteCount;
    }

    // getters
    public int getPopulationSize() {
        return populationSize;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public int getEliteCount() {
        return eliteCount;
    }
    
    public static int getCountGenerations() {
        return countGenerations;
    }

    // function that initializes the population
    public Population initPopulation() {
        return pop.generateRandomPopulation(populationSize);
    }

    public void doGeneration() {

        // get parents
        Individual parent1 = getParent();
        Individual parent2 = getParent();
        
        while (parent1 == parent2) {
            parent2 = tournamentSelection();
        }

        // perform crossover
        crossover(pop);

        // mutate
    }

    // return one parent from previous population
    public Individual getParent() {
        return null;
    }

    // fuction that selects the best individuals from the population. gets two random individuals and returns the best one
    public Individual tournamentSelection() {
        // get two random individuals
        Individual candidate1 = getRandomIndividual();
        Individual candidate2 = getRandomIndividual();
        
        // change in case the two individuals are the same
        while (candidate1 == candidate2) {
            candidate2 = getRandomIndividual();
        }
        if (candidate1.getFitness() > candidate2.getFitness()) {
            return candidate1;
        } else {
            return candidate2;
        }
    }

    // functions that creates a roulette wheel for the population
    public Individual createRouletteWheel() throws Exception {
        // sum of all the fitness of the individuals in the population
        double sum = pop.sumFitness();
        // array of the proportions of the individuals in the population
        double proportions[] = new double[pop.populationSize()];
        // array of cumulative proportions
        double cumulativeProportions[] = new double[pop.populationSize()];
        double cumulativeTotal = 0; // total of the cumulative proportions

        for (int i = 0; i < this.populationSize; i++) {
            // calculate the proportion of the individual in the population (fitness / sum)
            proportions[i] = pop.getIndividual(i).getFitness() / sum;
        }
        // sum all the proportions and add them to the cumulative proportions array
        for (int i = 0; i < this.populationSize; i++) {
            // add the proportion to the cumulative total
            cumulativeProportions[i] = proportions[i] + cumulativeTotal;
            cumulativeTotal += proportions[i];
        }
        // generate a random number between 0 and 1
        double random = Math.random();
        random = random * cumulativeTotal; // multiply the random number by the cumulative total
        // find the individual that corresponds to the random number
        for (int i = 0; i < this.populationSize; i++) {
            if (random < cumulativeProportions[i]) {
                return pop.getIndividual(i);
            }
        }
        throw new Exception("Error: Roulette wheel selection failed");
    }
    
    private Individual getRandomIndividual() {
        int randomIndex = (int) (Math.random() * populationSize); // Generate a random index within the population size
        return pop.getIndividual(randomIndex);
    }

    // checks if already looped the max amount of generations or got max fitness value
    public boolean finishGeneration(Population pop) {
        boolean checkMaxGen, checkMaxFitness;
        // checks if number of current generations surpassed the max generations
        checkMaxGen = ClassroomAssignGA.countGenerations > this.maxGenerations;
        checkMaxFitness = pop.getFittest().getFitness() > 100; // random number for now (change 100)!!!!!!!!!!!!!!
        return (checkMaxGen || checkMaxFitness); // return true if at least one of the criterias are met
    }

    // procedure that add a mutation to the population - 
    // gets one of the individual answers and make a random change in it
    private void mutation() {
        return;
    }

    private Population crossover(Population pop)
    {
        int i;
        Population newPop = new Population(pop.populationSize());

        for (i = 0; i < pop.populationSize(); i++) {
            return pop;
        }
        return pop;
    }

    private double calculateFitness() {
        return 0;
    }

    public void evalPopulation(Population pop) {
        return;
    }

}
