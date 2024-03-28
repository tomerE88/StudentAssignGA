package com.example.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            parent1 = createRouletteWheel();
            parent2 = createRouletteWheel();
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
                newIndividual1 = createRouletteWheel();
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
        int candidatesSize = 5;
        // list of random individuals
        ArrayList<Individual> candidates = new ArrayList<Individual>();

        // get random individual and set as best individual
        Individual bestCandidate = getRandomIndividual();
        // add to array list
        candidates.add(bestCandidate);

        // iterates number of needed times
        for (int i = 1; i < candidatesSize; i++) {
            // get random individual
            Individual candidate = getRandomIndividual();
            // loops until found new individual
            while (!candidates.contains(candidate))
                candidate = getRandomIndividual();
            if (candidate.getFitness() > bestCandidate.getFitness())
            bestCandidate = candidate;
            // add the individual to the arraylist
            candidates.add(candidate);
        }
        return bestCandidate;
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
        int randClass = (int) (Math.random() * individual.getClassroomsLength());
        // get the classroom
        ClassRoom class1 = individual.getClassroom(randClass);
        // get a random student from class
        int randStud1 = (int) (Math.random() * class1.getNumStudents());
        // get the student
        Student stud1 = class1.getStudentFromIndex(randStud1);

        // random number between 0 and length of the individual.classrooms - 1
        int randNewClass = (int) (Math.random() * individual.getClassroomsLength());
        // get the classroom
        ClassRoom newClass = individual.getClassroom(randNewClass);

        // check if every other class already full
        if (!individual.allClassesFull(class1)) {
            // while the classes are not the same and we can insert to another class
            while (newClass != class1 && newClass.getNumStudents() < newClass.getMaxStudents()) {
                // random number between 0 and length of the individual.classrooms - 1
                randNewClass = (int) (Math.random() * individual.getClassroomsLength());
                // get the classroom
                newClass = individual.getClassroom(randNewClass);
            }

            // insert student to new class
            newClass.addStudent(stud1);
            
        }
        
    }

    // Procedure that performs crossover between two individuals to produce a new offspring
    public Individual crossover(Individual parent1, Individual parent2) {
        // Get the number of classrooms in the parents
        int numberOfClassrooms = parent1.getClassrooms().length;
        // Determine the random crossover point
        int crossoverPoint = (int) (Math.random() * numberOfClassrooms);
    
        // Set of all students that have been assigned to a classroom
        Set<Student> assignedStudents = new HashSet<>();
    
        // Offspring classrooms
        ClassRoom[] offspringClassrooms = new ClassRoom[numberOfClassrooms];
    
        // Copy classrooms from parent1 up to the crossover point, including non-student attributes
        for (int i = 0; i < crossoverPoint; i++) {
            offspringClassrooms[i] = new ClassRoom(parent1.getClassroom(i));
            // Add students to the set of assigned students
            assignedStudents.addAll(parent1.getClassroom(i).getStudents());
        }
    
        // Prepare to track students from parent2, maintaining their order
        List<Student> studentsOrder = new ArrayList<>();
    
        // Gather all students from parent2, maintaining the order they appear
        for (ClassRoom classroom : parent2.getClassrooms()) {
            for (Student student : classroom.getStudents()) {
                if (!assignedStudents.contains(student)) {
                    studentsOrder.add(student);
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
                Student student = studentsOrder.get(studentOrderIndex++);
                offspringClassrooms[i].getStudents().add(student);
                assignedStudents.add(student);
            }
        }
    
        // Debugging log to ensure all students are assigned properly
        System.out.println("Total students after crossover: " + assignedStudents.size());
    
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
    public Individual evolutionCycle() {
        Individual bestIndividual;
        // data from the database
        MainGA mainga = new MainGA();
        // generate a random population
        this.pop = new Population(this.populationSize, mainga.getClassrooms());
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
        bestIndividual = this.pop.getBestIndividual();
        System.out.println("best Ind: " + bestIndividual.getFitness());

        // increment the count of generations
        ClassroomAssignGA.countGenerations++;

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
            // increment the count of generations
            ClassroomAssignGA.countGenerations++;
            bestIndividual = this.pop.getBestIndividual();
            System.out.println("best Ind: " + bestIndividual.getFitness());
        }
        return bestIndividual;
    }

    public static void main(String[] args) {
        System.out.println("**************************************");
        ClassroomAssignGA classroomAssignGA = new ClassroomAssignGA(50, 50, 0.01, 0.9, 2);
        Individual bestIndividual = classroomAssignGA.evolutionCycle();
        System.out.println("The best individual is: " + bestIndividual.getFitness());
        System.out.println("**************************************");
        
    }

}
