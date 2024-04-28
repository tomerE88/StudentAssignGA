package com.example.Test;

import com.example.Model.ClassroomAssignGA;
import com.example.Model.Individual;

public class testBestSizes {

    /*
     * This method runs the genetic algorithm once
     * and returns the fitness of the best individual
     */
    public static double runOnce(int[] ranks, double mutation, double elitism, double crossoverRate) {

        ClassroomAssignGA cag = new ClassroomAssignGA(100, 100, mutation, crossoverRate, elitism);
        cag.setRanks(ranks);
        // get the best individual
        Individual bestInd = cag.evolutionCycle();
        // return the fitness of the best individual
        return bestInd.getFitness();
    }

    /*
     * This method runs the genetic algorithm 10 times
     * and returns the average of the best individuals
     */
    public static double avgOfSize() {

        int numRuns = 15;
        double mutation = 0.206;
        double elitism = 0.177;
        double crossoverRate = 0.622;
        double avg = 0;

        // initialize array of ranks
        int ranks[] = new int[7];
        for (int i = 0; i < 7; i++) {
            ranks[i] = 1;
        }

        double sum = 0;

        for (int j = 0; j < numRuns; j++) {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Run " + j);
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            sum += runOnce(ranks, mutation, elitism, crossoverRate);
        }
        avg = sum / numRuns;
        
        // return the average of best individuals
        return avg;
    }
    public static void main(String[] args) {

        double avg = avgOfSize();
        double mutation = 0.206;
        System.out.println("the avg of mutation " + mutation + " is " + avg);

        // for (int i=0; i<mutations.length; i++) {
        //     double mutation = mutations[i];
        //     double avg = avgs[i];
        //     System.out.println("the avg of mutation " + mutation + " is " + avg);
        // }

    }
}
