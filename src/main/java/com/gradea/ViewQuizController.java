package com.gradea;

import java.util.Optional;

import com.gradea.models.Quiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewQuizController {
  @FXML
  private ImageView quizImage;
  @FXML
  private Label nameLabel;
  @FXML
  private Label descriptionLabel;
  @FXML
  private Label durationLabel;
  @FXML
  private Label dueDateLabel;
  @FXML
  private Label attemptsAllowedLabel;
  @FXML
  private Label numberOfQuestionsLabel;
  @FXML
  private Label organizationLabel;
  @FXML
  private Button startQuizButton;
  @FXML
  private Label scoreLabel;
  @FXML
  private VBox upcomingQuizInfo;
  @FXML
  private VBox reviewQuizInfo;

  private Quiz quiz;

  public void initialize() {
    startQuizButton.setOnAction(event -> startQuiz());
  }

  public void setQuiz(Quiz quiz) {
    _setQuiz(quiz, "upcoming");
  }

  public void setQuiz(Quiz quiz, String type) {
    _setQuiz(quiz, type);
  }

  private void _setQuiz(Quiz quiz, String type) {
    this.quiz = quiz;
    if (type == "upcoming") {
      reviewQuizInfo.setVisible(false);
      reviewQuizInfo.setManaged(false);
      nameLabel.setText(quiz.getName());
      descriptionLabel.setText(quiz.getDescription());
      attemptsAllowedLabel.setText(String.valueOf(quiz.getAttemptsAllowed()) + " attempts");
      numberOfQuestionsLabel.setText(String.valueOf(quiz.getQuestions().length) + " questions");
      durationLabel.setText(String.valueOf(quiz.getFormattedDuration()) + " - ");
      dueDateLabel.setText(quiz.getDueDate());
      organizationLabel.setText(quiz.getOrganization());
      Image image = new Image(getClass().getResourceAsStream("quiz_art.png"));
      quizImage.setImage(image);
      startQuizButton.setText("Take Quiz");
    } else {
      upcomingQuizInfo.setVisible(false);
      upcomingQuizInfo.setManaged(false);
      Image image = new Image(getClass().getResourceAsStream("review_quiz_art.png"));
      quizImage.setImage(image);
      scoreLabel.setText(quiz.getPercentage() + "%" + " (" + quiz.getScore() + "/" + quiz.getTotalScore() + ")");
      startQuizButton.setText("Review Quiz");
    }
  }

  private void startQuiz() {
    // Code to start the quiz
  }
}
