package com.gradea;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.gradea.models.Question;
import com.gradea.models.Question.QuestionType;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
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
import javafx.util.Duration;

public class QuizController {
  @FXML
  private StackPane rootPane;

  @FXML
  private Text questionCard;
  @FXML
  private Button option1, option2, option3, option4;
  @FXML
  private Button prevButton, nextButton;

  @FXML
  private TextField shortAnswer;
  @FXML
  private GridPane multipleChoice;
  @FXML
  private ImageView forwardImage;

  private List<Question> questions; // List of questions
  private int currentQuestionIndex; // Index of the current question

  @FXML
  private HBox questionIndicatorsBox;
  private List<Circle> questionIndicators;

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

  @FXML
  public void initialize() {
    // Initialize questions list
    questions = new ArrayList<>();
    questionIndicators = new ArrayList<>();

    // Load questions into the questions list here
    questions.addAll(
        List.of(
            new Question("What is your Name?", QuestionType.SHORT_ANSWER, null, "John Doe"),
            new Question("What is the capital of France?", QuestionType.MULTIPLE_CHOICE,
                List.of("Paris", "London", "Berlin", "Madrid"), "Paris"),
            new Question("What is the capital of Spain?", QuestionType.MULTIPLE_CHOICE,
                List.of("Paris", "London", "Berlin", "Madrid"), "Madrid"),
            new Question("What is the capital of Germany?", QuestionType.MULTIPLE_CHOICE,
                List.of("Paris", "London", "Berlin", "Madrid"), "Berlin"),
            new Question("The capital of United Kingdom is London.", QuestionType.TRUE_FALSE, null, "True"),
            new Question("What is the capital of the United States?", QuestionType.SHORT_ANSWER, null,
                "Washington D.C.")));

    int totalQuestions = questions.size();
    for (int i = 0; i < totalQuestions; i++) {
      final int questionNumber = i;
      // 5 is the radius of the circle
      Circle indicator = new Circle(totalQuestions > 50 ? 5 : totalQuestions > 30 ? 8 : 10);
      indicator.setFill(Color.WHITE); // Set initial color to white
      indicator.setCursor(Cursor.HAND);
      indicator.setOnMouseClicked(event -> setQuestion(questionNumber));
      questionIndicatorsBox.getChildren().add(indicator);
      questionIndicators.add(indicator);
    }

    shortAnswer.textProperty().addListener((observable, oldValue, newValue) -> {
      // Set the userAnswer in the Question object whenever the text changes
      if (questions.get(currentQuestionIndex).getType() == QuestionType.SHORT_ANSWER) {
        questions.get(currentQuestionIndex).setUserAnswer(newValue);
      }
    });

    // Set initial question
    setQuestion(0);

    remainingTime = 30; // the remaining time in seconds

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
        timerText.fontProperty().set(Font.font("Montserrat", FontWeight.BOLD, 12));
        if (!progressBar.getStyleClass().contains("timeRunningOut")) {
          progressBar.getStyleClass().add("timeRunningOut");
          rootPane.getStyleClass().remove("bg-color");
          rootPane.getStyleClass().add("timeRunningOutBg");
        }
      }
    });
    // Move to finished screen when countdown is finished
    countdown.setOnFinished(event -> {
      // Check if current question is answered and mark it as answered before swapping
      // question
      // if (questions.get(currentQuestionIndex).getUserAnswer() != "") {
      // markQuestionAsAnswered(currentQuestionIndex);
      // }

      // Remove clicked style from all buttons
      // Stream.of(option1, option2, option3, option4).forEach(button ->
      // button.getStyleClass().remove("clicked"));

      // Move to finished screen
      // load fxml
      FXMLLoader loader = new FXMLLoader(getClass().getResource("finished.fxml"));
      // Parent root = loader.load();

    });

    countdown.playFromStart();
  }

  private void setQuestion(int questionIndex) {
    // Check if current question is answered and mark it as answered before swapping
    // question
    System.out.println(questions.get(currentQuestionIndex).getQuestionText());
    if (questions.get(currentQuestionIndex).getUserAnswer() != "") {
      markQuestionAsAnswered(currentQuestionIndex);
    }

    // Remove clicked style from all buttons
    Stream.of(option1, option2, option3, option4).forEach(button -> button.getStyleClass().remove("clicked"));

    currentQuestionIndex = questionIndex;
    Question question = questions.get(questionIndex);
    questionCard.setText(question.getQuestionText());
    if (question.getType() == QuestionType.SHORT_ANSWER) {
      shortAnswer.setVisible(true);
      multipleChoice.setVisible(false);
      shortAnswer.setText(question.getUserAnswer());
      shortAnswer.requestFocus();
    } else if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
      shortAnswer.setVisible(false);
      multipleChoice.setVisible(true);
      List<String> options = question.getOptions();
      option1.setVisible(true);
      option1.setText(options.get(0));
      option2.setVisible(true);
      option2.setText(options.get(1));
      option3.setVisible(true);
      option3.setText(options.get(2));
      option4.setVisible(true);
      option4.setText(options.get(3));
    } else {
      List<String> options = question.getOptions();
      shortAnswer.setVisible(false);
      multipleChoice.setVisible(true);
      option1.setText("");
      option1.setVisible(false);
      option2.setText("");
      option2.setVisible(false);
      option3.setVisible(true);
      option3.setText(options.get(0));
      option4.setVisible(true);
      option4.setText(options.get(1));
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

    // Change Next to Finish if it's the last question
    if (questionIndex == questions.size() - 1) {
      // forwardImage.setVisible(false);
      // nextButton.setText("Finish");
    }
    nextButton.setDisable(questionIndex == questions.size());
    markCurrentQuestion(currentQuestionIndex);
  }

  @FXML
  public void handleOptionClick(ActionEvent event) {
    Button clickedOption = (Button) event.getSource();
    String selectedOptionText = clickedOption.getText();
    if (clickedOption.getStyleClass().contains("clicked")) {
      clickedOption.getStyleClass().remove("clicked");
      questions.get(currentQuestionIndex).setUserAnswer("");
      return;
    }
    clickedOption.getStyleClass().add("clicked"); // Add clicked style
    // Remove clicked style from all other buttons
    Stream.of(option1, option2, option3, option4)
        .filter(button -> button != clickedOption)
        .forEach(button -> button.getStyleClass().remove("clicked"));

    questions.get(currentQuestionIndex).setUserAnswer(selectedOptionText);

    // Check if the selected option is correct
    // If it is, you could give some visual feedback like turning the button green
    // If it's not correct, you could turn the button red and maybe disable the
    // other options until the user moves to the next question
  }

  @FXML
  public void handlePrevClick() {
    if (currentQuestionIndex > 0) {
      setQuestion(currentQuestionIndex - 1);
    }
  }

  @FXML
  public void handleNextClick() {
    if (currentQuestionIndex < questions.size() - 1) {
      setQuestion(currentQuestionIndex + 1);
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
