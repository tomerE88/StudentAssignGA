package com.example.Controller;

import java.io.IOException;

import com.example.View.App;

import javafx.fxml.FXML;

public class SetInDBController {
     // Switch to the primary view
     @FXML
     private void switchToPrimary() throws IOException {
         App.setRoot("questionnaire");
     }
}
