package com.gradea;

import java.time.temporal.ChronoUnit;

import com.gradea.models.Quiz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QuizCardController {
  @FXML
  private Label nameLabel;

  @FXML
  private Label descriptionLabel;

  @FXML
  private Label dueDateLabel;

  private Quiz quiz;

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
    nameLabel.setText(quiz.getName());
    descriptionLabel.setText(quiz.getDescription());
    long daysBetween = ChronoUnit.DAYS.between(quiz.getStartDate(), quiz.getEndDate());
    if (daysBetween == 0)
      dueDateLabel.setText("Due today");
    else if (daysBetween == 1)
      dueDateLabel.setText("Due tomorrow");
    else
      dueDateLabel.setText("Due in " + quiz.getEndDate().toString());
  }
}
