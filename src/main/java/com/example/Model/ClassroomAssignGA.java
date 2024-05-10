package com.example.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassroomAssignGA {

    // magic numbers
    private static final int STARTING_COUNT = 0;
    private static final int GET_LAST_INDEX = -1;
    private static final int TOURNAMENT_SIZE = 10;
    private static final int NO_SUCH_ELEMENT = -1;



    // number of maximum generations
    private int maxGenerations;
    // rate of mutation
    private double mutationRate;
    // rate of crossover
    private double crossoverRate;
    // rate of elite individuals
    private double eliteCount;
    // counter of generations
    public static int countGenerations;
    // current population (contaion individuals)
    private Population pop;
    // ranks of each craiteria
    private int[] ranks;
    // hashmap of all majors
    private HashMap<Integer, Major> majors;

    // constructor
    public ClassroomAssignGA(int populationSize, int maxGenerations, double mutationRate, double crossoverRate,
     double eliteCount) {
        ClassroomAssignGA.countGenerations = STARTING_COUNT;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.eliteCount = eliteCount;
        this.pop = new Population(populationSize); // create new population
    }

    // setters
    public void setRanks(int[] ranks) {
        this.ranks = ranks;
    }

    // getters
    public Population getPopulation() {
        return pop;
    }

    public HashMap<Integer, Major> getMajors() {
        return this.majors;
    }

    // function that creates a new generation
    public void createGeneration() {
        int index = 0, popSize = this.pop.getPopulationSize();
        Individual parent1, parent2, newIndividual1, newIndividual2;
        // create a new empty population
        Population newPopulation = new Population(popSize);

        System.out.println("Start of generation: " + countGenerations);

        // order the population by fitness (best fitness is last)
        this.pop.orderByFitness();

        double cumulativeProportions[] = createRouletteWheel();

        // crossover to 62.2% of the population
        while (index < this.pop.getPopulationSize() * this.crossoverRate) {
            // System.out.println("crossover loop: " + index);
            // get two parents from previous generation
            parent1 = rouletteSelection(cumulativeProportions);
            parent2 = rouletteSelection(cumulativeProportions);
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
        // use to count the elite individuals
        int counter = STARTING_COUNT;

        // add 17.7% of the population as elite
        while (counter < popSize * this.eliteCount) {
            newIndividual1 = this.pop.getIndividual(pop.getPopulationSize() + GET_LAST_INDEX);
            // add the elite individuals to the new population
            newPopulation.individuals.add(newIndividual1);

            // remove in O(1)
            // remove the individual from the population
            pop.individuals.remove(this.pop.getPopulationSize() + GET_LAST_INDEX);

            // increment the index
            index--;
            counter++;
        }

        int selectedIndividualIndex;
        
        // add the rest of the population to the new population with selection
        while (newPopulation.getPopulationSize() < popSize) {
            // System.out.println("rest loop: " + newPopulation.getPopulationSize());
            if (this.pop.individuals.size() < 2)
                selectedIndividualIndex = 0;
                // newIndividual1 = this.pop.getIndividual(0);
            else
                // get a new individual from the tournament wheel
                selectedIndividualIndex = tournamentSelection();
            
            // get the individual from the index 
            newIndividual1 = this.pop.getIndividual(selectedIndividualIndex);

            // remove in O(1)
            // switch the places of the individual with the last individual in the population
            this.pop.switchIndividuals(selectedIndividualIndex);
            // remove the individual from the population
            this.pop.individuals.remove(pop.getPopulationSize() + GET_LAST_INDEX);

            // mutate the new individual
            if (Math.random() < mutationRate) {
                mutation(newIndividual1);
            }
            // add the new individual to the new population
            newPopulation.individuals.add(newIndividual1);
        }

        this.pop = newPopulation;
    }

    // fuction that selects the best individuals from the population. gets five random individuals and returns the best one
    public int tournamentSelection() {
        int candidatesSize = TOURNAMENT_SIZE;
        // list of random individuals
        ArrayList<Individual> candidates = new ArrayList<Individual>();

        // get random individual index and set as best individual
        int bestIndex = getRandomIndividualIndex();
        // add to array list
        candidates.add(this.pop.getIndividual(bestIndex));

        // iterates number of needed times
        for (int i = 1; i < candidatesSize; i++) {
            int randomIndex = getRandomIndividualIndex();
            while (candidates.contains(this.pop.getIndividual(randomIndex))) {
                randomIndex = getRandomIndividualIndex();
            }
            if (this.pop.getIndividual(randomIndex).getFitness() > this.pop.getIndividual(bestIndex).getFitness())
                bestIndex = randomIndex;
            // add to array list
            candidates.add(this.pop.getIndividual(randomIndex));
        }
        return bestIndex;
    }

    /*
      function that creates and return a roulette wheel
      of the proportions of the individuals in the population.
      each individual gets a "piece" of the roulette based on its fitness function.
     */
    public double[] createRouletteWheel() {
        // sum of all the fitness of the individuals in the population
        double sum = pop.sumFitness();
        // array of the proportions of the individuals in the population
        double proportions[] = new double[pop.getPopulationSize()];
        // array of cumulative proportions
        double cumulativeProportions[] = new double[pop.getPopulationSize()];
        double cumulativeTotal = 0; // total of the cumulative proportions

        for (int i = 0; i < pop.getPopulationSize(); i++) {
            // calculate the proportion of the individual in the population (fitness / sum)
            proportions[i] = pop.getIndividual(i).getFitness() / sum;
        }
        // sum all the proportions and add them to the cumulative proportions array
        for (int i = 0; i < pop.getPopulationSize(); i++) {
            // add the proportion to the cumulative total
            cumulativeProportions[i] = proportions[i] + cumulativeTotal;
            cumulativeTotal += proportions[i];
        }
        return cumulativeProportions;
    }
    
    /*
     * gets the cumulative proporrtions from createRouletteWheel
     * and remove the selected individual from the population
     * and return a random individual out of the roulette
     */
    public Individual rouletteSelection(double[] cumulativeProportions) {
        // generate a random number between 0 and 1 and multiply the random number by the cumulative total
        double random = Math.random() * cumulativeProportions[cumulativeProportions.length - 1];
        
        // get the index of the random individual
        int index = binarySearchRoulette(cumulativeProportions, random);
        if (index >= 0) {
            return pop.getIndividual(index);
        }
        return null;
    }

    /*
     * gets the cumulative proporrtions from createRouletteWheel
     * and finds the index of the random individual out of the roulette
     * in complexity of O(log(n))
     */
    public int binarySearchRoulette(double[] cumulativeProportions, double random) {
        int low = STARTING_COUNT, high = cumulativeProportions.length + GET_LAST_INDEX;

        // binary search for the random number in the cumulative proportions
        while (low <= high) {
            // find the middle of the array
            int mid = (low + high) / 2;
            // 
            if (random < cumulativeProportions[mid]) {
                high = mid - 1;
            } else if (random > cumulativeProportions[mid]) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        
        // If not exactly found, return the closest higher element (since low > high and low is incremented last) or -1 if not found
        return low < cumulativeProportions.length ? low : NO_SUCH_ELEMENT;
    }

    /*
     * return random individual index
     */
    private int getRandomIndividualIndex() {
        int randomIndex = (int) (Math.random() * pop.getPopulationSize()); // Generate a random index within the population size
        return randomIndex;
    }

    /*
     * gets a population
     * and checks if already looped the max amount of generations
     * or got max fitness value
     */
    public boolean finishGeneration(Population pop, double maxFitness) {
        boolean checkMaxGen, checkMaxFitness;
        // checks if number of current generations surpassed the max generations
        checkMaxGen = ClassroomAssignGA.countGenerations > this.maxGenerations;
        // checks if the fitness equals to the max fitness
        checkMaxFitness = pop.getBestIndividual().getFitness() >= maxFitness;
        return (checkMaxGen || checkMaxFitness); // return true if at least one of the criterias are met
    }

    public void mutation(Individual individual) {
        // random number between 0 and length of the individual.classrooms - 1
        int randClass1 = (int) (Math.random() * individual.getClassroomsLength());
        int randClass2;
        

        // loops until the classes are different
        do {
            randClass2 = (int) (Math.random() * individual.getClassroomsLength());
        } while (randClass1 == randClass2);

        // get the classrooms
        ClassRoom class1 = individual.getClassroom(randClass1);
        ClassRoom class2 = individual.getClassroom(randClass2);
        // get a random student from class
        int randStud1 = (int) (Math.random() * class1.getNumStudents());
        int randStud2 = (int) (Math.random() * class2.getNumStudents());

        // get the students
        Student stud1 = class1.getStudentFromIndex(randStud1);
        Student stud2 = class2.getStudentFromIndex(randStud2);

        // switch between the students
        class1.switchStudents(randStud1, stud1, stud2);
        class2.switchStudents(randStud2, stud2, stud1);

    }

    // Procedure that performs crossover between two individuals to produce a new offspring
    public Individual crossover(Individual parent1, Individual parent2) {
        // Get the number of classrooms in the parents
        int numberOfClassrooms = parent1.getClassrooms().length;
        // Determine the random crossover point
        int crossoverPoint = (int) (Math.random() * (numberOfClassrooms - 1));
    
        // Set of all students that have been assigned to a classroom
        Set<Student> assignedStudents = new HashSet<>();
    
        // Offspring classrooms
        ClassRoom[] offspringClassrooms = new ClassRoom[numberOfClassrooms];
    
        // Copy classrooms from parent1 up to the crossover point, including non-student attributes
        for (int i = 0; i <= crossoverPoint; i++) {
            offspringClassrooms[i] = new ClassRoom(parent1.getClassroom(i));
            // Add students to the set of assigned students
            assignedStudents.addAll(parent1.getClassroom(i).getStudents().values());
        }
    
        // Prepare to track students from parent2, maintaining their order
        List<Student> studentsOrder = new ArrayList<>();
    
        // Gather all students from parent2, maintaining the order they appear
        for (ClassRoom classroom : parent2.getClassrooms()) {
            for (Student student : classroom.getStudents().values()) {
                if (!assignedStudents.contains(student)) {
                    studentsOrder.add(student);
                }
            }
        }
    
        int studentOrderIndex = 0;
    
        // For the remaining classrooms, fill with students not yet assigned, in the order they appear in parent2
        for (int i = crossoverPoint + 1; i < numberOfClassrooms; i++) {
            // Create a new classroom, copying non-student attributes from the same classroom in parent1
            offspringClassrooms[i] = new ClassRoom(parent1.getClassroom(i), false);
    
            // Iterate over studentsOrder to add students until the classroom reaches its original size
            while (offspringClassrooms[i].getNumStudents() < parent1.getClassroom(i).getNumStudents() && studentOrderIndex < studentsOrder.size()) {
                Student student = studentsOrder.get(studentOrderIndex++);
                // Add the student to the classroom
                offspringClassrooms[i].addStudent(student);
                assignedStudents.add(student);
            }
        }
    
        // Debugging log to ensure all students are assigned properly
        System.out.println("Total students after crossover: " + assignedStudents.size());
    
        // Create a new Individual with the combined classroom assignments
        return new Individual(offspringClassrooms);
    }

    public void evalPopulation(Student[] students, HashMap<Integer, Major> majors, SpecialRequest[]specialRequests, int[] ranks) {
        for (Individual individual : this.pop.getIndividuals()) {
            // get classrooms from the individuals
            ClassRoom[] classrooms = individual.getClassrooms();
            double fitness = FitnessEvaluator.fitnessFunction(students, classrooms, majors, specialRequests, ranks);
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
        this.pop.randomPopulation(mainga.getClassrooms(), mainga.getStudents().values());
        // get students from the database
        Student[] students = mainga.getStudents().values().toArray(new Student[0]);
        // get hashmap of all majors and major ID's
        this.majors = mainga.getMajors();
        // get all special requests
        SpecialRequest[]specialRequests = mainga.getSpecialRequests();
        // evaluate the population
        evalPopulation(students, majors, specialRequests, this.ranks);
        double popAvg = this.pop.sumFitness() / this.pop.getPopulationSize();

        // increment the count of generations
        ClassroomAssignGA.countGenerations++;

        double maxFitness = FitnessEvaluator.getMaxFitness(this.ranks);

        // loops until the finishGeneration function returns false (reached max generations or max fitness)
        while (!finishGeneration(this.pop, maxFitness)) {
            // create a new generation
            createGeneration();
            // evaluate the population
            evalPopulation(students, majors, specialRequests, this.ranks);
            popAvg = this.pop.sumFitness() / this.pop.getPopulationSize();
            System.out.println("***************************************************************");
            System.out.println("Generation: " + countGenerations + " Average fitness: " + popAvg);
            System.out.println("***************************************************************");
            // increment the count of generations
            ClassroomAssignGA.countGenerations++;
        }
        bestIndividual = this.pop.getBestIndividual();
        return bestIndividual;
    }

}
