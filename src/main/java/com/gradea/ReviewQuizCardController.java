package com.gradea;

import com.gradea.models.Quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReviewQuizCardController {
  @FXML
  private Label nameLabel;

  @FXML
  private Label descriptionLabel;

  @FXML
  private Label scoreLabel;

  @FXML
  private Button reviewButton;

  private Quiz quiz;

  @FXML
  private void initialize() {
    reviewButton.setOnMouseClicked(e -> {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view-quiz.fxml"));
        Parent root = loader.load();
        ViewQuizController controller = loader.getController();
        controller.setQuiz(quiz, "review");

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
    if (quiz.getDescription().length() > 60)
      descriptionLabel.setText(quiz.getDescription().substring(0, 60) + "...");
    else
      descriptionLabel.setText(quiz.getDescription());
    scoreLabel.setText(quiz.getPercentage() + "%" + " (" + quiz.getScore() + "/" + quiz.getTotalScore() + ")");
  }
}
