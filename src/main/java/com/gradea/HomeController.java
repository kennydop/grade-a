package com.gradea;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class HomeController {
  @FXML
  private Circle avatar;
  @FXML
  private Label userName, quizzesTaken, organizations, averageScore;

  @FXML
  private void initialize() {
    // Load the user's data here
  }
}
