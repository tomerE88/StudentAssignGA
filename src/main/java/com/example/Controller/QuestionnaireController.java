package com.example.Controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.print.Collation;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.util.stream.IntStream;

import com.example.View.App;


public class QuestionnaireController {

    @FXML
    private ChoiceBox<Integer> Friends;

    @FXML
    private ChoiceBox<Integer> equality;

    @FXML
    private ChoiceBox<Integer> preferences;

    @FXML
    private ChoiceBox<Integer> sameCity;

    @FXML
    private ChoiceBox<Integer> specialOccasions;

    @FXML
    private ChoiceBox<Integer> grades;

    @FXML
    private ChoiceBox<Integer> studentType;

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

    @FXML
    private void initialize() {

        // Add option 1-5 to Friends ChoiceBox
        IntStream.rangeClosed(1, 5).forEach(Friends.getItems()::add);

        // Add option 1-5 to equality ChoiceBox
        IntStream.rangeClosed(1, 5).forEach(equality.getItems()::add);

        // Add option 1-5 to preferences ChoiceBox
        IntStream.rangeClosed(1, 5).forEach(preferences.getItems()::add);

        // Add option 1-5 to sameCity ChoiceBox
        IntStream.rangeClosed(1, 5).forEach(sameCity.getItems()::add);

        // Add option 1-5 to Friends ChoiceBox
        IntStream.rangeClosed(1, 5).forEach(specialOccasions.getItems()::add);

        // Add option 1-5 to studentType ChoiceBox
        IntStream.rangeClosed(1, 5).forEach(studentType.getItems()::add);

        // Add option 1-5 to grades ChoiceBox
        IntStream.rangeClosed(1, 5).forEach(grades.getItems()::add);
    }

    @FXML
    private void handleGoToPlacementAction(ActionEvent event) throws IOException {
        try {
        // Save the user's choices
        friendsRating = Friends.getValue();
        genderRating = equality.getValue();
        preferencesRating = preferences.getValue();
        sameCityRating = sameCity.getValue();
        specialOccasionsRating = specialOccasions.getValue();
        gradesRating = grades.getValue();
        studentTypeRating = studentType.getValue();


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


    @FXML
    // switch to secondary GUI
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

}
