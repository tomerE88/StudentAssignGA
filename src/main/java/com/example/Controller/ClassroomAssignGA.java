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
        this.pop = generateRandomPopulation(this.populationSize);

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

    public Population getPopulation() {
        return pop;
    }

    // generate random population
    public Population generateRandomPopulation(int populationSize) {
        Population population = new Population(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual();
            individual.generateRandomIndividual();
            pop.individuals[i] = individual;
        }
        return population;
    }

    // function that creates a new generation
    public void createGeneration() {

        // get parents
        Individual parent1 = createRouletteWheel();
        Individual parent2 = createRouletteWheel();
        
        while (parent1 == parent2) {
            parent2 = createRouletteWheel();
        }

        // crossover
        if (Math.random() < crossoverRate) {
            crossover(parent1, parent2);
        }

        // mutate
        if (Math.random() < mutationRate) {
            mutation(pop.getFittest());
        }
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
        if (candidate1.getFitness() > candidate2.getFitness())
            return candidate1;

        return candidate2; 
    }

    // functions that creates a roulette wheel for the population and returns one individual
    public Individual createRouletteWheel() {
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
        return null;
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
    private void mutation(Individual individual) {
        // random number between 0 and length of the individual.classrooms - 1
        int randClass1 = (int) (Math.random() * individual.getClassroomsLength());
        int randClass2 = (int) (Math.random() * individual.getClassroomsLength());
        // get the classrooms
        ClassRoom class1 = individual.getClassroom(randClass1);
        ClassRoom class2 = individual.getClassroom(randClass2);
        // get a random student from each class
        int randStud1 = (int) (Math.random() * class1.getNumStudents());
        int randStud2 = (int) (Math.random() * class2.getNumStudents());
        // get the students
        Student stud1 = class1.getStudents().get(randStud1);
        Student stud2 = class2.getStudents().get(randStud2);

        // loops until the students are different
        while (stud1 == stud2) {
            randStud2 = (int) (Math.random() * class2.getNumStudents());
            stud2 = class2.getStudents().get(randStud2);

        }
        // change the students
        class1.setStudent(randStud2, stud2);
        class2.setStudent(randStud1, stud1);
        
    }

    // procedure that makes the crossover between two individuals!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!fix
    public Individual crossover (Individual parent1, Individual parent2) {
        int halfLen = parent1.getClassroomsLength() / 2;
        // create a new individual
        Individual newIndividual = new Individual();
        // loop through the classrooms
        for (int i = 0; i < parent1.getClassroomsLength(); i++) {
            // if the index is less than half the length, add the classroom from parent1
            if (i < halfLen) {
                newIndividual.addClassroom(parent1.getClassroom(i), i);
            }
            // if the index is greater than half the length, add the classroom from parent2
            else {
                newIndividual.addClassroom(parent2.getClassroom(i), i);
            }
        }
        return newIndividual;
    }

    private double calculateFitness() {
        return 0;
    }

    public void evalPopulation(Population pop) {
        return;
    }

    // function that runs the evolution cycle
    public void evolutionCycle() {
        // loops until the finishGeneration function returns false (reached max generations or max fitness)
        while (!finishGeneration(pop)) {
            // create a new generation
            createGeneration();
            // evaluate the population
            evalPopulation(pop);
            // increment the count of generations
            countGenerations++;
        }
    }

}
