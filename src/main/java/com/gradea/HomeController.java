package com.gradea;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.gradea.controllers.Quizzes;
import com.gradea.controllers.Session;
import com.gradea.models.Question;
import com.gradea.models.Question.QuestionType;
import com.gradea.models.Quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController {
  @FXML
  private HBox header;
  @FXML
  private HBox upcomingQuizzesContainer;
  @FXML
  private HBox recentlyTakenQuizzesContainer;
  @FXML
  private Label upcomingQuizesLabel;
  @FXML
  private Label recentlyTakenQuizzesLabel;

  @FXML
  private void initialize() {
    // List<Quiz> upcomingQuizes = Quizzes.fetchUserQuizzes();
    // List<Quiz> quizzesToReview = Quizzes.fetchUserQuizzesToReview();
    // if (upcomingQuizes.size() == 0) {
    // upcomingQuizesLabel.setText("No Upcoming Quizzes");
    // } else {
    // for (Quiz quiz : upcomingQuizes) {
    // addQuizCard(quiz);
    // }
    // }

    // for (Quiz quiz : quizzesToReview) {
    // addReviewQuizCard(quiz);
    // }

    // Load the user's data here
  }

  @FXML
  void showJoinOrgDialog() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("join-org.fxml"));
      Parent root = loader.load();

      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.setTitle("Enter Organization Code");
      dialogStage.setResizable(false);
      dialogStage.setScene(new Scene(root));
      dialogStage.show();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @FXML
  void showCreateOrgDialog() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("create-org.fxml"));
      Parent root = loader.load();

      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.setTitle("Enter Organization Details");
      dialogStage.setScene(new Scene(root));
      dialogStage.show();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void addQuizCard(Quiz quiz) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz-card.fxml"));
      Node quizNode = loader.load();

      QuizCardController controller = loader.getController();
      controller.setQuiz(quiz);

      upcomingQuizzesContainer.getChildren().add(quizNode);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addReviewQuizCard(Quiz quiz) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("review-quiz-card.fxml"));
      Node quizNode = loader.load();

      ReviewQuizCardController controller = loader.getController();
      controller.setQuiz(quiz);

      recentlyTakenQuizzesContainer.getChildren().add(quizNode);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleLogoutButtonAction() {
    try {
      App.setRoot("auth");
      Session.getInstance().clearUser();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
