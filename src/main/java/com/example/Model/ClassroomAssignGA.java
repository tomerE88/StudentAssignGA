package com.example.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ClassroomAssignGA {

    private int populationSize;
    private int maxGenerations;
    private double mutationRate;
    private double crossoverRate;
    private int eliteCount;
    public static int countGenerations ;
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

    // function that creates a new generation
    public void createGeneration() {
        int index = 0, popSize = this.pop.populationSize();
        Individual parent1, parent2, newIndividual1, newIndividual2;
        // create a new empty population
        Population newPopulation = new Population(true);

        System.out.println("Start of generation: " + countGenerations);

        // order the population by fitness
        this.pop.orderByFitness();

        // crossover to 70% of the population
        while (index < (this.populationSize) * 0.7) {
            // System.out.println("crossover loop: " + index);
            // get two parents from previous generation
            parent1 = tournamentSelection();
            parent2 = tournamentSelection();
            // crossover the parents
            newIndividual1 = crossover(parent1, parent2);
            newIndividual2 = crossover(parent2, parent1);
            // mutate first individual
            if (Math.random() < mutationRate) {
                mutation(newIndividual1);
            }
            // mutate second individual
            if (Math.random() < mutationRate) {
                mutation(newIndividual2);
            }
            // add the new individuals to the new population
            newPopulation.individuals.add(newIndividual1);
            newPopulation.individuals.add(newIndividual2);
            // increment the index by 2
            index += 2;
        }
        // reset the index
        index = 0;

        // add 10% of the population as elite
        while (index < popSize * 0.1) {
            // System.out.println("elite loop: " + index);
            newIndividual1 = this.pop.getIndividual(index);
            // mutate the elite individual
            // if (Math.random() < mutationRate) {
            //     mutation(newIndividual1);
            // }
            // add the elite individuals to the new population
            newPopulation.individuals.add(newIndividual1);
            // remove the elite individuals from the old population
            this.pop.individuals.remove(index);
            // increment the index
            index++;
        } 
        
        // add the rest of the population to the new population with selection
        while (newPopulation.populationSize() < popSize) {
            // System.out.println("rest loop: " + newPopulation.populationSize());
            if (this.pop.individuals.size() < 2)
                newIndividual1 = this.pop.getIndividual(0);
            else
                // get a new individual from the roulette wheel
                newIndividual1 = tournamentSelection();
            // mutate the new individual
            if (Math.random() < mutationRate) {
                mutation(newIndividual1);
            }
            // add the new individual to the new population
            newPopulation.individuals.add(newIndividual1);
            // remove the new individual from the old population
            this.pop.individuals.remove(newIndividual1);
        }

        this.pop = newPopulation;
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

        for (int i = 0; i < pop.populationSize(); i++) {
            // calculate the proportion of the individual in the population (fitness / sum)
            proportions[i] = pop.getIndividual(i).getFitness() / sum;
        }
        // sum all the proportions and add them to the cumulative proportions array
        for (int i = 0; i < pop.populationSize(); i++) {
            // add the proportion to the cumulative total
            cumulativeProportions[i] = proportions[i] + cumulativeTotal;
            cumulativeTotal += proportions[i];
        }
        // generate a random number between 0 and 1
        double random = Math.random();
        random = random * cumulativeTotal; // multiply the random number by the cumulative total
        // find the individual that corresponds to the random number
        for (int i = 0; i < pop.populationSize(); i++) {
            // if the random number is less than the cumulative proportion of the individual
            if (random < cumulativeProportions[i]) {
                return this.pop.getIndividual(i);
            }
        }
        // if the random number is not found return null
        return null;
    }
    
    private Individual getRandomIndividual() {
        int randomIndex = (int) (Math.random() * pop.populationSize()); // Generate a random index within the population size
        return this.pop.getIndividual(randomIndex);
    }

    // checks if already looped the max amount of generations or got max fitness value
    public boolean finishGeneration(Population pop) {
        boolean checkMaxGen, checkMaxFitness;
        // checks if number of current generations surpassed the max generations
        checkMaxGen = ClassroomAssignGA.countGenerations > this.maxGenerations;
        checkMaxFitness = pop.getFittest().getFitness() > 10000; // random number for now (change 100)!!!!!!!!!!!!!!
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
        Student stud1 = class1.getStudentsArray()[randStud1];
        Student stud2 = class2.getStudentsArray()[randStud2];

        // loops until the students are different
        while (stud1 == stud2) {
            randStud2 = (int) (Math.random() * class2.getNumStudents());
            stud2 = class2.getStudentsArray()[randStud2];

        }
        // switch between the students
        class1.switchStudents(stud1, stud2);
        class2.switchStudents(stud2, stud1);
        
    }

    // Procedure that performs crossover between two individuals to produce a new offspring
    public Individual crossover(Individual parent1, Individual parent2) {
        // Get the number of classrooms in the parents
        int numberOfClassrooms = parent1.getClassrooms().length;
        // Determine the random crossover point
        int crossoverPoint = (int) (Math.random() * numberOfClassrooms);
    
        // Set of all student IDs that have been assigned to a classroom
        Set<String> assignedStudentIds = new HashSet<>();
    
        // Offspring classrooms
        ClassRoom[] offspringClassrooms = new ClassRoom[numberOfClassrooms];
    
        // Copy classrooms from parent1 up to the crossover point, including non-student attributes
        for (int i = 0; i < crossoverPoint; i++) {
            offspringClassrooms[i] = new ClassRoom(parent1.getClassroom(i));
            // Add students to the set of assigned student IDs
            assignedStudentIds.addAll(parent1.getClassroom(i).getStudents().keySet());
        }
    
        // Prepare to track students from parent2, maintaining their order
        List<String> studentsOrder = new ArrayList<>();
    
        // Gather all students from parent2, maintaining the order they appear
        for (ClassRoom classroom : parent2.getClassrooms()) {
            for (String studentId : classroom.getStudents().keySet()) {
                if (!assignedStudentIds.contains(studentId)) {
                    studentsOrder.add(studentId);
                }
            }
        }
    
        int studentOrderIndex = 0;
    
        // For the remaining classrooms, fill with students not yet assigned, in the order they appear in parent2
        for (int i = crossoverPoint; i < numberOfClassrooms; i++) {
            // Create a new classroom, copying non-student attributes from the same classroom in parent1
            offspringClassrooms[i] = new ClassRoom(parent1.getClassroom(i));
            // Clear or reinitialize the students in the new classroom to start fresh
            offspringClassrooms[i].clearStudents(); // Assuming such a method exists to empty the student list/map
    
            // Iterate over studentsOrder to add students until the classroom reaches its original size
            while (offspringClassrooms[i].getNumStudents() < parent1.getClassroom(i).getNumStudents() && studentOrderIndex < studentsOrder.size()) {
                String studentId = studentsOrder.get(studentOrderIndex++);
                Student student = parent2.getStudentByID(studentId);
                offspringClassrooms[i].addStudent(studentId, student);
                assignedStudentIds.add(studentId);
            }
        }
    
        // Debugging log to ensure all students are assigned properly
        System.out.println("Total students after crossover: " + assignedStudentIds.size());
    
        // Create a new Individual with the combined classroom assignments
        return new Individual(offspringClassrooms);
    }

    public void evalPopulation(Student[] students, HashMap<Integer, Major> majors, SpecialRequest[]specialRequests) {
        for (Individual individual : this.pop.getIndividuals()) {
            // get classrooms from the individuals
            ClassRoom[] classrooms = individual.getClassrooms();
            double fitness = FitnessEvaluator.fitnessFunction(students, classrooms, majors, specialRequests, 1, 1, 1, 1, 1, 1, 1);
            individual.setFitness(fitness);
            System.out.println("***************************************************************");
            System.out.println("fitness score: " + fitness);
            System.out.println("***************************************************************");
        }
    }

    // function that runs the evolution cycle
    public void evolutionCycle() {
        // generate a random population
        this.pop = new Population(this.populationSize);
        // data from the database
        MainGA mainga = new MainGA();
        // get students from the database
        Student[] students = mainga.getStudents().values().toArray(new Student[0]);
        // get hashmap of all majors and major ID's
        HashMap<Integer, Major> majors = mainga.getMajors();
        // get all special requests
        SpecialRequest[]specialRequests = mainga.getSpecialRequests();
        // evaluate the population
        evalPopulation(students, majors, specialRequests);
        double popAvg = this.pop.sumFitness() / this.pop.populationSize();
        System.out.println("***************************************************************");
        System.out.println("Generation: " + countGenerations + " Average fitness: " + popAvg);
        System.out.println("***************************************************************");

        // increment the count of generations
        this.countGenerations++;

        // loops until the finishGeneration function returns false (reached max generations or max fitness)
        while (!finishGeneration(this.pop)) {
            // create a new generation
            createGeneration();
            // get classrooms from the individuals
            // evaluate the population
            evalPopulation(students, majors, specialRequests);
            popAvg = this.pop.sumFitness() / this.pop.populationSize();
            System.out.println("***************************************************************");
            System.out.println("Generation: " + countGenerations + " Average fitness: " + popAvg);
            System.out.println("***************************************************************");
            if (this.countGenerations == 49)
                System.out.println("dean");
            // increment the count of generations
            this.countGenerations++;
        }
    }

    public static void main(String[] args) {
        MainGA mainga = new MainGA();
        System.out.println("**************************************");
        ClassroomAssignGA classroomAssignGA = new ClassroomAssignGA(50, 50, 0.01, 0.9, 2);
        classroomAssignGA.evolutionCycle();
        System.out.println("**************************************");
        
    }

}
