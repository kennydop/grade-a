package com.gradea;

import java.util.List;

import com.gradea.controllers.Quizzes;
import com.gradea.models.Quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class QuizzesController {
  @FXML
  private GridPane allQuizzesGrid;

  List<Quiz> upcomingQuizes;

  @FXML
  public void initialize() {
    upcomingQuizes = Quizzes.getInstance().getQuizzes();
    populateGridPane(upcomingQuizes);
  }

  public void refreshQuizzes() {
    allQuizzesGrid.getChildren().clear();
    upcomingQuizes = Quizzes.getInstance().getQuizzes();
    populateGridPane(upcomingQuizes);
  }

  public void populateGridPane(List<Quiz> quizzes) {
    int rowIndex = 0;
    int colIndex = 0;

    for (Quiz quiz : quizzes) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz-card.fxml"));
        Node quizNode = loader.load();

        QuizCardController controller = loader.getController();
        controller.setQuiz(quiz);
        controller.setQuizzesController(this);

        allQuizzesGrid.add(quizNode, colIndex, rowIndex);
        colIndex++;
        if (colIndex > 3) {
          colIndex = 0;
          rowIndex++;
        }
      } catch (Exception e) {
        System.out.println("Error loading quiz card");
        e.printStackTrace();
      }

    }
  }
}
