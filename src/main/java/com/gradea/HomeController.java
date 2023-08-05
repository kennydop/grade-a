package com.gradea;

import java.io.IOException;
import java.util.List;

import com.gradea.controllers.Quizzes;
import com.gradea.controllers.Session;
import com.gradea.models.Quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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

  List<Quiz> upcomingQuizes;
  List<Quiz> quizzesToReview;

  @FXML
  private void initialize() {
    upcomingQuizes = Quizzes.getInstance().fetchUserQuizzes();
    quizzesToReview = Quizzes.getInstance().fetchUserQuizzesToReview();

    if (upcomingQuizes.size() == 0) {
      upcomingQuizesLabel.setText("No Upcoming Quizzes");
    } else {
      for (Quiz quiz : upcomingQuizes) {
        System.out.println("Adding " + quiz.getName() + " to upcoming quizzes");
        addQuizCard(quiz);
      }
    }

    if (quizzesToReview.size() == 0) {
      recentlyTakenQuizzesLabel.setText("No Recent Quizzes");
    } else {
      for (Quiz quiz : quizzesToReview) {
        System.out.println("Adding " + quiz.getName() + " to Quizzes to Review");
        addReviewQuizCard(quiz);

      }
    }

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

  @FXML
  void handleCreateQuizButton() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("create-quiz.fxml"));
      Parent root = loader.load();

      // Get the CreateQuizController and set HomeController
      CreateQuizController createQuizController = loader.getController();
      createQuizController.setHomeController(this);

      Stage dialogStage = new Stage();
      dialogStage.setTitle("Create Quiz");
      dialogStage.setMaximized(true);
      dialogStage.setScene(new Scene(root));
      dialogStage.show();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void refreshQuizzes() {
    upcomingQuizzesContainer.getChildren().clear();
    recentlyTakenQuizzesContainer.getChildren().clear();
    initialize();
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
