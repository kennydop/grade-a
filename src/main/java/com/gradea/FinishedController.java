package com.gradea;

import java.io.IOException;

import com.gradea.models.Question;
import com.gradea.models.Quiz;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FinishedController {
  private Quiz quiz;

  @FXML
  private Label score;
  @FXML
  private VBox reviewQnsCardContainer;

  @FXML
  public void initialize() {
    Platform.runLater(() -> {
      score.setText(quiz.getScore() + " / " + quiz.getTotalScore());
      loadCards();
    });
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  public void loadCards() {
    Question[] questions = quiz.getQuestions();
    for (int i = 0; i < questions.length; i++) {
      try {
        // Load the FXML file for the card
        FXMLLoader loader = new FXMLLoader(getClass().getResource("review_question_card.fxml"));

        // Load the root node from the FXMLLoader
        VBox card = loader.load();

        // Get the controller of the card and set the data
        ReviewQuestionCardController controller = loader.getController();
        controller.setData(
            String.valueOf(i + 1),
            questions[i].getQuestionText(),
            questions[i].getUserAnswer(),
            questions[i].getCorrectAnswer(), questions[i].isCorrect());

        // Add the card to the reviewQnsCardContainer
        reviewQnsCardContainer.getChildren().add(card);
      } catch (IOException e) {
        // Add error handling here
        e.printStackTrace();
      }
    }
  }
}
