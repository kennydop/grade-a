package com.gradea;

import com.gradea.models.Quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReviewQuizCardController {
  @FXML
  private Label nameLabel;

  @FXML
  private Label descriptionLabel;

  @FXML
  private Label scoreLabel;

  private Quiz quiz;

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
    nameLabel.setText(quiz.getName());
    descriptionLabel.setText(quiz.getDescription());
    scoreLabel.setText(quiz.getPercentage() + "%" + " (" + quiz.getScore() + "/" + quiz.getTotalScore() + ")");
  }
}
