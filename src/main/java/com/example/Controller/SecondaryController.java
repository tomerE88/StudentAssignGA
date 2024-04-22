package com.example.Controller;

import java.io.IOException;
import java.util.HashMap;

import com.example.Model.Individual;
import com.example.Model.Major;
import com.example.Model.Student;
import com.example.View.App;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SecondaryController {

    @FXML
    private VBox vboxDisplay; // vbox to display the best individual classes

    private static Individual bestIndividual;
    private static HashMap<Integer, Major> majors;

    // Setters
    public static void setBestIndividual(Individual individual) {
        bestIndividual = individual;
    }

    public static void setMajors(HashMap<Integer, Major> majors) {
        SecondaryController.majors = majors;
    }

    // initialize the display
    @FXML
    private void initialize() {
        if (bestIndividual != null) {
            updateDisplay();
        }
    }

    /*
    * Update the display with the best individual classes
    * and their students
    */
    private void updateDisplay() {
        vboxDisplay.getChildren().clear(); // Clear previous content if any

        // Loop through the best individual classes
        for (int i = 0; i < bestIndividual.getClassrooms().length; i++) {
            // Get the major of the class
            Major major = majors.get(bestIndividual.getClassroom(i).getMajorID());
            
            // Create and style the text for the class and major
            Text classAndMajorText = new Text(bestIndividual.getClassroom(i).getClassName() + " MAJOR: " + major.getName() + "\n");
            classAndMajorText.setFont(Font.font("System", FontWeight.BOLD, 16));
            
            // Create a text flow to display the class and major
            TextFlow textFlow = new TextFlow();
            textFlow.getChildren().add(classAndMajorText);
            
            // Loop through the students in the class
            for (Student student : bestIndividual.getClassrooms()[i].getStudents().values()) {
                // Create and style the text for each student
                Text studentText = new Text("   • " + student.getStudentID() + " - " + student.getName() + "\n");

                studentText.setFont(Font.font("System", FontWeight.NORMAL, 14));
                textFlow.getChildren().add(studentText);
            }
            
            // Add the text flow to the display
            vboxDisplay.getChildren().add(textFlow);
        }
    }

    // Switch to the primary view
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("questionnaire");
    }

    /*
     * set classes to all students
     */
    @FXML
    private void setInDB() throws IOException {
        bestIndividual.setClassroomsInDB();
        App.setRoot("setInDB");
    }
}

