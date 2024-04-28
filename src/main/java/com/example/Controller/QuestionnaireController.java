package com.example.Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import com.example.Model.ClassroomAssignGA;
import com.example.Model.Individual;
import com.example.View.App;


public class QuestionnaireController {

    public enum Priority {
        NO_PRIORITY("(0) no priority", 0),
        VERY_LOW_PRIORITY("(1) very low priority", 1),
        LOW_PRIORITY("(2) low priority", 2),
        MEDIUM_PRIORITY("(3) medium priority", 3),
        HIGH_PRIORITY("(4) high priority", 4),
        VERY_HIGH_PRIORITY("(5) very high priority", 5);

        private final String description;
        private final int value;

        Priority(String description, int value) {
            this.description = description;
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public int getValue() {
            return value;
        }
    }

    @FXML
    private ChoiceBox<Priority> Friends;

    @FXML
    private ChoiceBox<Priority> equality;

    @FXML
    private ChoiceBox<Priority> preferences;

    @FXML
    private ChoiceBox<Priority> sameCity;

    @FXML
    private ChoiceBox<Priority> specialOccasions;

    @FXML
    private ChoiceBox<Priority> grades;

    @FXML
    private ChoiceBox<Priority> studentType;

    @FXML
    private Button goToPlacement;


    // variables to store user's selection
    private int friendsRating;
    private int genderRating;
    private int preferencesRating;
    private int sameCityRating;
    private int specialOccasionsRating;
    private int gradesRating;
    private int studentTypeRating;
    // ranks will store all ratings
    private int ranks[];

    /*
     * This method is called when the QuestionnaireController is created
     * It will initialize the ChoiceBoxes with the options 0-5
     */
    @FXML
    private void initialize() {

        // Add option 0-5 to Friends ChoiceBox as the strings
        Friends.getItems().addAll(Priority.values());
        
         // Add option 0-5 to equality ChoiceBox as the strings
         equality.getItems().addAll(Priority.values());

        // Add option 0-5 to preferences ChoiceBox as the strings
        preferences.getItems().addAll(Priority.values());

         // Add option 0-5 to sameCity ChoiceBox as the strings
         sameCity.getItems().addAll(Priority.values());

        // Add option 0-5 to specialOccasions ChoiceBox as the strings
        specialOccasions.getItems().addAll(Priority.values());

         // Add option 0-5 to studentType ChoiceBox as the strings
         studentType.getItems().addAll(Priority.values());

        // Add option 0-5 to grades ChoiceBox as the strings
        grades.getItems().addAll(Priority.values());

        // initialize array
        ranks = new int[7];
    }

    /*
     * This method is called when the user clicks the "see placement" button
     * It will save the user's choices as numbers and switch to the secondary view
     */
    @FXML
    private void handleGoToPlacementAction(ActionEvent event) throws IOException {
        try {
        // Save the user's choices as numbers
        preferencesRating = preferences.getValue().getValue();
        friendsRating = Friends.getValue().getValue();
        sameCityRating = sameCity.getValue().getValue();
        genderRating = equality.getValue().getValue();     
        specialOccasionsRating = specialOccasions.getValue().getValue();
        studentTypeRating = studentType.getValue().getValue();
        gradesRating = grades.getValue().getValue();

        ranks[0] = preferencesRating;
        ranks[1] = friendsRating;
        ranks[2] = sameCityRating;
        ranks[3] = genderRating;
        ranks[4] = specialOccasionsRating;
        ranks[5] = studentTypeRating;
        ranks[6] = gradesRating;


        // Now you can use these variables as needed before switching views
        System.out.println("friendsRatings: " + friendsRating);
        System.out.println("genderRating: " + genderRating);
        System.out.println("preferencesRating: " + preferencesRating);
        System.out.println("sameCityRating: " + sameCityRating);
        System.out.println("specialOccasionsRating: " + specialOccasionsRating);
        System.out.println("grades: " + gradesRating);
        System.out.println("studentType: " + studentTypeRating);

        // Switch to the secondary view
        switchToSecondary();
        
        } catch (NullPointerException e) { // if at least one of the choice boxes is null
            System.out.println("One or more ChoiceBoxes have not been selected.");
        }
    }

    // switch to waiting screen
    @FXML
    private void switchToWaiting() throws IOException {
        App.setRoot("waiting");
    }

    // switch to waiting GUI and run the algorithm in the background
    @FXML
    private void switchToSecondary() {
        // Display the waiting screen
        try {
            App.setRoot("waitingScreen");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // create instance of the genetic algorithm
        ClassroomAssignGA cag = new ClassroomAssignGA(100, 100, 0.206, 0.622, 0.177);

        // Create a Task for the genetic algorithm to run in the background
        Task<Individual> task = new Task<Individual>() {
            @Override
            protected Individual call() throws Exception {
                cag.setRanks(ranks);
                return cag.evolutionCycle();
            }
        };
        
        // When the task is successfully completed, update the UI with the best individual
        task.setOnSucceeded(event -> {
            Individual bestIndividual = task.getValue();
            SecondaryController.setBestIndividual(bestIndividual);
            SecondaryController.setMajors(cag.getMajors());
            System.out.println("The best individual is: " + bestIndividual.getFitness());
            
            // Switch to the secondary view
            try {
                App.setRoot("secondary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        // Handle failed case
        task.setOnFailed(event -> {
            Throwable e = task.getException();
            System.err.println("Error during genetic algorithm execution: " + e.getMessage());
            e.printStackTrace();
            // You can also show an error message to the user here
        });
        
        // Run the task in a background thread
        Thread thread = new Thread(task);
        thread.start();
    }

}
