package com.gradea;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.gradea.controllers.Session;
import com.gradea.models.Question;
import com.gradea.models.Question.QuestionType;
import com.gradea.models.Quiz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

  private Quiz[] upcomingQuizes = {
      new Quiz(
          "Quiz 1",
          "This is the first quiz. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation",
          "GradeA",
          LocalDate.now(),
          LocalDate.now().plusDays(1),
          60,
          6,
          3,
          new Question[] {
              new Question("What is 1 + 1?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "2", 1),
              new Question("What is 2 + 2?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "4", 1),
              new Question("What is 3 + 3?", QuestionType.SHORT_ANSWER, new String[] {}, "6", 1),
              new Question("What is 4 + 4?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "8", 1),
              new Question("What is 5 + 5?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "10",
                  1),
              new Question("What is 6 + 6?", QuestionType.SHORT_ANSWER, new String[] {}, "12", 1),
              new Question("What is 7 + 7?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "14",
                  1),
              new Question("What is 8 + 8?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "16",
                  1),
              new Question("What is 9 + 9?", QuestionType.SHORT_ANSWER, new String[] {}, "18", 1),
              new Question("What is 10 + 10?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "20",
                  1)
          }),
      new Quiz(
          "Quiz 2",
          "This is the second quiz",
          "GradeA",
          LocalDate.now(),
          LocalDate.now().plusDays(7),
          60,
          6,
          3,
          new Question[] {
              new Question("What is 1 + 1?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "2", 1),
              new Question("What is 2 + 2?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "4", 1),
              new Question("What is 3 + 3?", QuestionType.SHORT_ANSWER, new String[] {}, "6", 1),
              new Question("What is 4 + 4?", QuestionType.MULTIPLE_CHOICE, new String[] { "8", "2", "12", "14" }, "8",
                  1),
              new Question("What is 5 + 5?", QuestionType.MULTIPLE_CHOICE, new String[] { "5", "9", "10", "15" }, "10",
                  1),
              new Question("What is 6 + 6?", QuestionType.SHORT_ANSWER, new String[] {}, "12", 1),
              new Question("What is 7 + 7?", QuestionType.MULTIPLE_CHOICE, new String[] { "11", "12", "13", "14" },
                  "14",
                  1),
              new Question("What is 8 + 8?", QuestionType.MULTIPLE_CHOICE, new String[] { "8", "12", "13", "16" }, "16",
                  1),
              new Question("What is 9 + 9?", QuestionType.SHORT_ANSWER, new String[] {}, "18", 1),
              new Question("What is 10 + 10?", QuestionType.MULTIPLE_CHOICE, new String[] { "100", "18", "20", "200" },
                  "20",
                  1),
          }),
      new Quiz(
          "Quiz 3",
          "This is the third quiz",
          "GradeA",
          LocalDate.now(),
          LocalDate.now().plusDays(30),
          120,
          60,
          3,
          new Question[] {

              new Question("What is the capital of France?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Paris", "London", "Berlin", "Madrid" }, "Paris", 5),
              new Question("What is the capital of Spain?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Paris", "London", "Berlin", "Madrid" }, "Madrid", 5),
              new Question("What is the capital of Germany?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Paris", "London", "Berlin", "Madrid" }, "Berlin", 5),
              new Question("The capital of United Kingdom is London.", QuestionType.TRUE_FALSE, null, "True", 5),
              new Question("What is the capital of the United States?", QuestionType.SHORT_ANSWER, null,
                  "Washington D.C.", 10),
              new Question("What is the capital of Ghana?", QuestionType.SHORT_ANSWER, null, "Accra", 10),
              new Question("Abuja is the capital of Nigeria. True or False?", QuestionType.TRUE_FALSE, null, "True", 5),
              new Question("What is the capital of South Africa?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Johannesburg", "Cape Town", "Pretoria", "Durban" }, "Cape Town", 5),
              new Question("What is the capital of Egypt?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Cairo", "Alexandria", "Giza", "Luxor" }, "Cairo", 5),
              new Question("What is the capital of Kenya?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Nairobi", "Mombasa", "Kisumu", "Nakuru" }, "Nairobi", 5),
              new Question("What is the capital of Morocco?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Rabat", "Casablanca", "Fes", "Marrakesh" }, "Rabat", 5),
              new Question("What is the capital of Algeria?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Algiers", "Oran", "Constantine", "Annaba" }, "Algiers", 5),
              new Question("What is the capital of Tunisia?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Tunis", "Sfax", "Sousse", "Kairouan" }, "Tunis", 5),
              new Question("What is the capital of Libya?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Tripoli", "Benghazi", "Misrata", "Tarhuna" }, "Tripoli", 5),
              new Question("What is the capital of Senegal?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Dakar", "Thies", "Kaolack", "Ziguinchor" }, "Dakar", 5),
              new Question("What is the capital of Ivory Coast?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Yamoussoukro", "Abidjan", "Bouake", "Daloa" }, "Yamoussoukro", 5),
              new Question("What is the capital of Mali?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Bamako", "Sikasso", "Mopti", "Segou" }, "Bamako", 5),
              new Question("What is the capital of Burkina Faso?", QuestionType.MULTIPLE_CHOICE,
                  new String[] { "Ouagadougou", "Bobo-Dioulasso", "Koudougou", "Banfora" }, "Ouagadougou", 5),
          })
  };

  private Quiz[] quizzesToReview = {
      new Quiz(
          "Quiz 0",
          "This is the first quiz, lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation",
          "GradeA",
          LocalDate.now(),
          LocalDate.now().plusDays(1),
          60,
          6,
          3,
          new Question[] {
              new Question("What is 1 + 1?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "2", 1,
                  "2"),
              new Question("What is 2 + 2?", QuestionType.MULTIPLE_CHOICE, new String[] { "1", "2", "3", "4" }, "4", 1,
                  "4"),
              new Question("What is 3 + 3?", QuestionType.SHORT_ANSWER, new String[] {}, "6", 1, "16"),
              new Question("What is 4 + 4?", QuestionType.MULTIPLE_CHOICE, new String[] { "8", "2", "12", "14" }, "8",
                  1, "8"),
              new Question("What is 5 + 5?", QuestionType.MULTIPLE_CHOICE, new String[] { "5", "9", "10", "15" }, "10",
                  1, "10"),
              new Question("What is 6 + 6?", QuestionType.SHORT_ANSWER, new String[] {}, "12", 1, "12"),
              new Question("What is 7 + 7?", QuestionType.MULTIPLE_CHOICE, new String[] { "11", "12", "13", "14" },
                  "14",
                  1, "14"),
              new Question("What is 8 + 8?", QuestionType.MULTIPLE_CHOICE, new String[] { "8", "12", "13", "16" }, "16",
                  1, "13"),
              new Question("What is 9 + 9?", QuestionType.SHORT_ANSWER, new String[] {}, "18", 1, "18"),
              new Question("What is 10 + 10?", QuestionType.MULTIPLE_CHOICE, new String[] { "100", "18", "20", "200" },
                  "20",
                  1, "20"),
          })
  };

  @FXML
  private void initialize() {
    header.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(header, Priority.ALWAYS);
    for (Quiz quiz : upcomingQuizes) {
      addQuizCard(quiz);
    }

    for (Quiz quiz : quizzesToReview) {
      addReviewQuizCard(quiz);
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
