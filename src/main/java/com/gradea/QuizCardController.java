package com.gradea;

import java.time.temporal.ChronoUnit;

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

  @FXML
  private void initialize() {
    viewQuizButton.setOnMouseClicked(e -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view-quiz.fxml"));
        Parent root = loader.load();
        ViewQuizController controller = loader.getController();
        controller.setQuiz(quiz);

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
    nameLabel.setText(quiz.getName());
    dueDateLabel.setText(quiz.getDueDate());
    if (quiz.getDescription().length() > 40)
      descriptionLabel.setText(quiz.getDescription().substring(0, 40) + "...");
    else
      descriptionLabel.setText(quiz.getDescription());

  }
}
