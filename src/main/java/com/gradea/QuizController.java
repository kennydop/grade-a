package com.gradea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.gradea.controllers.Quizzes;
import com.gradea.models.Question;
import com.gradea.models.Question.QuestionType;
import com.gradea.models.Quiz;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class QuizController {
  private Stage stage;

  @FXML
  private StackPane rootPane;

  @FXML
  private Text questionCard;
  @FXML
  private Button option1, option2, option3, option4;
  @FXML
  private Button prevButton, nextButton, submitButton;
  @FXML
  private TextField shortAnswer;
  @FXML
  private GridPane multipleChoice;
  @FXML
  private ImageView forwardImage;

  private Quiz quiz;
  private int currentQuestionIndex; // Index of the current question

  @FXML
  private HBox questionIndicatorsBox;
  private List<Circle> questionIndicators = new ArrayList<>();

  @FXML
  private ProgressBar progressBar;
  private Timeline countdown;
  private Timeline timer;
  @FXML
  private Text timerText;

  private long remainingTime;
  private java.time.Duration duration;
  private long hours;
  private long minutes;
  private long seconds;
  String countdownText;
  private boolean timeUp = false;
  private HomeController homeController;
  private QuizzesController quizzesController;

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  public void setQuizzesController(QuizzesController quizzesController) {
    this.quizzesController = quizzesController;
  }

  @FXML
  public void initialize() {

    submitButton.setDisable(true);
    submitButton.setVisible(false);
    nextButton.setDisable(false);
    nextButton.setVisible(true);

    Platform.runLater(() -> {
      int totalQuestions = quiz.getQuestions().length;
      for (int i = 0; i < totalQuestions; i++) {
        final int questionNumber = i;
        // 5 is the radius of the circle
        Circle indicator = new Circle(totalQuestions > 50 ? 5 : totalQuestions > 30 ? 8 : 10);
        indicator.setFill(Color.WHITE); // Set initial color to white
        indicator.setCursor(Cursor.HAND);
        indicator.setOnMouseClicked(event -> {
          if (timeUp)
            return;
          setQuestion(questionNumber);
        });
        questionIndicatorsBox.getChildren().add(indicator);
        questionIndicators.add(indicator);
      }

      shortAnswer.textProperty().addListener((observable, oldValue, newValue) -> {
        // Set the userAnswer in the Question object whenever the text changes
        if (quiz.getQuestions()[currentQuestionIndex].getType() == QuestionType.SHORT_ANSWER) {
          quiz.getQuestions()[currentQuestionIndex].setUserAnswer(newValue);
        }
      });

      // Set initial question
      setQuestion(0);

      remainingTime = 60; // the remaining time in seconds

      duration = java.time.Duration.ofSeconds(remainingTime);
      hours = duration.toHours();
      duration = duration.minusHours(hours);
      minutes = duration.toMinutes();
      duration = duration.minusMinutes(minutes);
      seconds = duration.getSeconds();

      countdownText = String.format("%02d:%02d:%02d", hours, minutes, seconds);

      // Set initial progress bar
      progressBar.setProgress(1); // Start at 100%

      countdown = new Timeline(
          new KeyFrame(
              Duration.seconds(0),
              new KeyValue(progressBar.progressProperty(), 1) // Start at 100%
      ),
          new KeyFrame(
              Duration.seconds(remainingTime), // Countdown duration
              new KeyValue(progressBar.progressProperty(), 0) // End at 0%
      ));

      timer = new Timeline(
          new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            if (remainingTime <= 0) {
              timer.stop();
            } else {
              remainingTime--;
              duration = java.time.Duration.ofSeconds(remainingTime);
              hours = duration.toHours();
              duration = duration.minusHours(hours);
              minutes = duration.toMinutes();
              duration = duration.minusMinutes(minutes);
              seconds = duration.getSeconds();

              countdownText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
              timerText.setText(countdownText);
            }
          }));
      timer.setCycleCount(Timeline.INDEFINITE);
      timer.play();

      progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
        double progress = newValue.doubleValue();
        if (progress <= 0.25) {
          timerText.setFill(Color.RED);
          timerText.fontProperty().set(Font.font("System", FontWeight.BOLD, 12));
          if (!progressBar.getStyleClass().contains("timeRunningOut")) {
            progressBar.getStyleClass().add("timeRunningOut");
            rootPane.getStyleClass().remove("bg-color");
            rootPane.getStyleClass().add("timeRunningOutBg");
          }
        }
      });
      // Move to finished screen when countdown is finished
      countdown.setOnFinished(event -> {
        timeUp = true;

        // Check if current question is answered and mark it as answered before swapping
        // screen
        if (quiz.getQuestions()[currentQuestionIndex].getUserAnswer() != "") {
          markQuestionAsAnswered(currentQuestionIndex);
        }

        // Remove clicked style from all buttons
        Stream.of(option1, option2, option3, option4).forEach(button -> button.getStyleClass().remove("clicked"));

        System.out.println("Time's up!");

        // finish quiz
        submitButton.setDisable(false);
        submitButton.setVisible(true);
        nextButton.setDisable(true);
        nextButton.setVisible(false);
        submitButton.fire();
      });

      countdown.playFromStart();
    });
  }

  public void setStage(Stage stage) {
    this.stage = stage;
    stage.setOnCloseRequest(e -> {
      timer.stop();
      countdown.stop();
    });
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  private void setQuestion(int questionIndex) {
    if (timeUp) {
      return;
    }
    // Check if current question is answered and mark it as answered before swapping
    // question
    System.out.println(quiz.getQuestions()[currentQuestionIndex].getQuestionText());
    if (quiz.getQuestions()[currentQuestionIndex].getUserAnswer() != "") {
      markQuestionAsAnswered(currentQuestionIndex);
    }

    // Remove clicked style from all buttons
    Stream.of(option1, option2, option3, option4).forEach(button -> button.getStyleClass().remove("clicked"));

    currentQuestionIndex = questionIndex;
    Question question = quiz.getQuestions()[questionIndex];
    questionCard.setText(question.getQuestionText());
    if (question.getType() == QuestionType.SHORT_ANSWER) {
      shortAnswer.setVisible(true);
      multipleChoice.setVisible(false);
      shortAnswer.setText(question.getUserAnswer());
      shortAnswer.requestFocus();
    } else if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
      shortAnswer.setVisible(false);
      multipleChoice.setVisible(true);
      String[] options = question.getOptions();
      option1.setVisible(true);
      option1.setText(options[0]);
      option2.setVisible(true);
      option2.setText(options[1]);
      option3.setVisible(true);
      option3.setText(options[2]);
      option4.setVisible(true);
      option4.setText(options[3]);
    } else {
      String[] options = question.getOptions();
      shortAnswer.setVisible(false);
      multipleChoice.setVisible(true);
      option1.setText("");
      option1.setVisible(false);
      option2.setText("");
      option2.setVisible(false);
      option3.setVisible(true);
      option3.setText(options[0]);
      option4.setVisible(true);
      option4.setText(options[1]);
    }

    // Set the user's answer if they've already answered the question
    String userAnswer = question.getUserAnswer();
    if (!userAnswer.isEmpty()) {
      if (question.getType() == QuestionType.SHORT_ANSWER) {
        shortAnswer.setText(userAnswer);
      } else if (question.getType() == QuestionType.TRUE_FALSE) {
        Stream.of(option3, option4).filter(button -> button.getText().equals(userAnswer))
            .findFirst().ifPresent(button -> button.getStyleClass().add("clicked"));
      } else if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
        Stream.of(option1, option2, option3, option4).filter(button -> button.getText().equals(userAnswer))
            .findFirst().ifPresent(button -> button.getStyleClass().add("clicked"));
      }
    }

    // Disable prev button if it's the first question
    prevButton.setDisable(questionIndex == 0);

    // if last question switch next and submit buttons
    if (currentQuestionIndex == quiz.getQuestions().length - 1) {
      submitButton.setDisable(false);
      submitButton.setVisible(true);
      nextButton.setDisable(true);
      nextButton.setVisible(false);
    } else {
      if (submitButton.isVisible()) {
        submitButton.setDisable(true);
        submitButton.setVisible(false);
        nextButton.setDisable(false);
        nextButton.setVisible(true);
      }
    }
    markCurrentQuestion(currentQuestionIndex);
  }

  @FXML
  public void handleOptionClick(ActionEvent event) {
    if (timeUp) {
      return;
    }
    Button clickedOption = (Button) event.getSource();
    String selectedOptionText = clickedOption.getText();
    if (clickedOption.getStyleClass().contains("clicked")) {
      clickedOption.getStyleClass().remove("clicked");
      quiz.getQuestions()[currentQuestionIndex].setUserAnswer("");
      return;
    }
    clickedOption.getStyleClass().add("clicked"); // Add clicked style
    // Remove clicked style from all other buttons
    Stream.of(option1, option2, option3, option4)
        .filter(button -> button != clickedOption)
        .forEach(button -> button.getStyleClass().remove("clicked"));

    quiz.getQuestions()[currentQuestionIndex].setUserAnswer(selectedOptionText);

    // Check if the selected option is correct
    // If it is, you could give some visual feedback like turning the button green
    // If it's not correct, you could turn the button red and maybe disable the
    // other options until the user moves to the next question
  }

  @FXML
  public void handlePrevClick() {
    if (timeUp) {
      return;
    }
    if (currentQuestionIndex > 0) {
      setQuestion(currentQuestionIndex - 1);
    }
  }

  @FXML
  public void handleNextClick() {
    if (timeUp) {
      return;
    }
    if (currentQuestionIndex < quiz.getQuestions().length - 1) {
      setQuestion(currentQuestionIndex + 1);
    }

  }

  @FXML
  public void handleSubmit(ActionEvent event) {
    submitButton.setVisible(false);
    submitButton.setDisable(true);
    timer.stop();
    countdown.stop();
    try {
      // load the finished screen
      FXMLLoader loader = new FXMLLoader(getClass().getResource("finished.fxml"));
      Parent finishedRoot = loader.load();
      FinishedController finishedController = loader.getController();
      finishedController.setQuiz(quiz);
      Quizzes.getInstance().submitQuiz(quiz);
      if (homeController != null) {
        homeController.refreshQuizzes();
      }
      if (quizzesController != null) {
        quizzesController.refreshQuizzes();
      }

      // get current stage
      Node source = (Node) event.getSource();
      Stage stage = (Stage) source.getScene().getWindow();

      // show the finished screen and close the current screen
      Scene finishedScene = new Scene(finishedRoot, 1000, 600);
      Stage finishedStage = new Stage();
      finishedStage.initStyle(StageStyle.DECORATED);
      finishedStage.setScene(finishedScene);
      stage.close();
      finishedStage.setTitle(quiz.getName() + " - Results");
      finishedStage.setMaximized(true);
      finishedStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void markQuestionAsAnswered(int questionNumber) {
    questionIndicators.get(questionNumber).setFill(Color.web("#4262ff"));
  }

  public void markCurrentQuestion(int questionNumber) {
    // Reset color of all indicators
    for (Circle indicator : questionIndicators) {
      if (indicator.getFill().equals(Color.web("#4262ff"))) {
        continue;
      } else {
        indicator.setFill(Color.WHITE);
      }
    }
    // Then set the color of the current question indicator
    questionIndicators.get(questionNumber).setFill(Color.ORANGE);
  }
}
