package com.gradea;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gradea.models.Quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class QuizCardController {
  @FXML
  private Label nameLabel;

  @FXML
  private Label descriptionLabel;

  @FXML
  private Label dueDateLabel;

  @FXML
  private Button viewQuizButton;

  private Quiz quiz;

  private HomeController homeController;
  private QuizzesController quizzesController;

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  public void setQuizzesController(QuizzesController quizzesController) {
    this.quizzesController = quizzesController;
  }

  @FXML
  private void initialize() {
    viewQuizButton.setOnMouseClicked(e -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view-quiz.fxml"));
        Parent root = loader.load();
        ViewQuizController controller = loader.getController();
        controller.setQuiz(quiz);
        controller.setHomeController(homeController);
        controller.setQuizzesController(quizzesController);

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setResizable(false);
        dialogStage.setScene(new Scene(root));
        dialogStage.show();

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
    LocalDateTime today = LocalDateTime.now();
    nameLabel.setText(quiz.getName());
    if (today.isAfter(quiz.getStartDate()) && today.isBefore(quiz.getEndDate())) {
      dueDateLabel.setText(quiz.getDueDate());
    } else {
      dueDateLabel.setText(quiz.getStartsInDate());

    }
    if (quiz.getDescription().length() > 40)
      descriptionLabel.setText(quiz.getDescription().substring(0, 40) + "...");
    else
      descriptionLabel.setText(quiz.getDescription());
  }
}
