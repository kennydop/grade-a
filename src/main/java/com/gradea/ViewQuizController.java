package com.gradea;

import java.io.IOException;
import java.util.Optional;

import com.gradea.models.Quiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
      startQuizButton.setOnAction((ActionEvent event) -> {
        startQuiz();
      });
    } else {
      upcomingQuizInfo.setVisible(false);
      upcomingQuizInfo.setManaged(false);
      Image image = new Image(getClass().getResourceAsStream("review_quiz_art.png"));
      quizImage.setImage(image);
      scoreLabel.setText(quiz.getPercentage() + "%" + " (" + quiz.getScore() + "/" + quiz.getTotalScore() + ")");
      startQuizButton.setText("Review Quiz");
      startQuizButton.setOnAction((ActionEvent event) -> {
        reviewQuiz();
      });
    }
  }

  public void startQuiz() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz.fxml"));
      Parent root = loader.load();

      QuizController quizController = loader.getController();
      quizController.setQuiz(quiz);

      Stage stage = new Stage();
      stage.initStyle(StageStyle.UNDECORATED);
      stage.setAlwaysOnTop(true);
      stage.setMaximized(true);
      quizController.setStage(stage);

      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();

      // close this window
      Stage currentStage = (Stage) startQuizButton.getScene().getWindow();
      currentStage.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void reviewQuiz() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("finished.fxml"));
      Parent root = loader.load();

      FinishedController finishedController = loader.getController();
      finishedController.setQuiz(quiz);

      Stage stage = new Stage();
      stage.setTitle(quiz.getName() + " - Results");
      stage.setMaximized(true);

      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();

      // close this window
      Stage currentStage = (Stage) startQuizButton.getScene().getWindow();
      currentStage.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
