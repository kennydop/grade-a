package com.gradea;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.gradea.models.Question;
import com.gradea.models.Question.QuestionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class QuizController {
  @FXML
  private Text questionNumber;
  @FXML
  private Text questionCard;
  @FXML
  private Button option1, option2, option3, option4;
  @FXML
  private Button prevButton, nextButton;

  private List<Question> questions; // List of questions
  private int currentQuestionIndex; // Index of the current question

  @FXML
  public void initialize() {
    // Initialize questions list
    questions = new ArrayList<>();

    // Load questions into the questions list here
    questions.addAll(
        List.of(new Question("What is the capital of France?", QuestionType.MULTIPLE_CHOICE,
            List.of("Paris", "London", "Berlin", "Madrid"), "Paris"),
            new Question("What is the capital of Spain?", QuestionType.MULTIPLE_CHOICE,
                List.of("Paris", "London", "Berlin", "Madrid"), "Madrid"),
            new Question("What is the capital of Germany?", QuestionType.MULTIPLE_CHOICE,
                List.of("Paris", "London", "Berlin", "Madrid"), "Berlin"),
            new Question("What is the capital of the United Kingdom?", QuestionType.MULTIPLE_CHOICE,
                List.of("Paris", "London", "Berlin", "Madrid"), "London"),
            new Question("What is the capital of the United States?", QuestionType.MULTIPLE_CHOICE,
                List.of("Paris", "London", "Berlin", "Washington D.C."), "Washington D.C.")));
    // Set initial question
    setQuestion(0);
    questionNumber.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
  }

  private void setQuestion(int questionIndex) {
    currentQuestionIndex = questionIndex;
    Question question = questions.get(questionIndex);
    questionCard.setText(question.getQuestionText());
    List<String> options = question.getOptions();
    option1.setText(options.get(0));
    option2.setText(options.get(1));
    option3.setText(options.get(2));
    option4.setText(options.get(3));

    // Disable prev button if it's the first question
    prevButton.setDisable(questionIndex == 0);

    // Change Next to Finish if it's the last question
    if (questionIndex == questions.size() - 1) {
      nextButton.setText("Finish");
    }
    nextButton.setDisable(questionIndex == questions.size());
    questionNumber.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
  }

  @FXML
  public void handleOptionClick(ActionEvent event) {
    Button clickedOption = (Button) event.getSource();
    String selectedOptionText = clickedOption.getText();
    clickedOption.getStyleClass().add("clicked"); // Add clicked style
    System.out.println(selectedOptionText);
    // Remove clicked style from all other buttons
    Stream.of(option1, option2, option3, option4)
        .filter(button -> button != clickedOption)
        .forEach(button -> button.getStyleClass().remove("clicked"));

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
      // Remove clicked style from all buttons
      Stream.of(option1, option2, option3, option4).forEach(button -> button.getStyleClass().remove("clicked"));
    }
  }
}
