package com.gradea;

import com.gradea.models.Question;
import com.gradea.models.Question.QuestionType;
import com.gradea.utils.ErrorDialog;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class QuestionCardController {
  @FXML
  private Label questionNumberField;
  @FXML
  private TextField questionField;
  @FXML
  private TextField correctAnswerField;
  @FXML
  private ComboBox<String> questionTypeField;
  @FXML
  private TextField pointsField;
  @FXML
  private HBox optionsContainer;
  @FXML
  private TextField option1Field;
  @FXML
  private TextField option2Field;
  @FXML
  private TextField option3Field;
  @FXML
  private TextField option4Field;

  private Parent root;

  public Parent getRootNode() {
    return root;
  }

  public void setRootNode(Parent root) {
    this.root = root;
  }

  private CreateQuizController createQuizController;

  public void setCreateQuizController(CreateQuizController createQuizController) {
    this.createQuizController = createQuizController;
  }

  @FXML
  private void initialize() {
    optionsContainer.setDisable(true);
    optionsContainer.setVisible(false);
    questionTypeField.getItems().addAll("Multiple Choice", "True/False", "Short Answer");

  }

  public void setQuestionNumber(int questionNumber) {
    questionNumberField.setText(Integer.toString(questionNumber));
  }

  @FXML
  private void selectQuestionType() {
    handleQuestionType();
  }

  private void handleQuestionType() {
    if (questionTypeField.getValue().equals("Multiple Choice")) {
      optionsContainer.setDisable(false);
      optionsContainer.setVisible(true);
    } else {
      optionsContainer.setDisable(true);
      optionsContainer.setVisible(false);
    }
  }

  public Question getQuestion() {
    return new Question(getQuestionText(), getQuestionType(), getOptions(), getCorrectAnswer(), getPoints());
  }

  public int getQuestionNumber() {
    return Integer.parseInt(questionNumberField.getText());
  }

  private String getQuestionText() {
    return questionField.getText();
  }

  private String getCorrectAnswer() {
    return correctAnswerField.getText();
  }

  private QuestionType getQuestionType() {
    String qt = questionTypeField.getValue().toUpperCase().replace("/", "_").replace(" ", "_");

    return QuestionType.valueOf(qt);
  }

  private String[] getOptions() {
    return new String[] { option1Field.getText(), option2Field.getText(), option3Field.getText(),
        option4Field.getText() };
  }

  private double getPoints() {
    return Double.parseDouble(pointsField.getText());
  }

  @FXML
  private void removeQuestion() {
    createQuizController.removeQuestion(getQuestionNumber() - 1);
  }

  public boolean validateInputs() {
    if (getQuestionText().isEmpty()) {
      ErrorDialog.showErrorDialog("Question Error", "Question " + questionNumberField.getText() + " cannot be empty",
          "");
      return false;
    }
    if (getCorrectAnswer().isEmpty()) {
      ErrorDialog.showErrorDialog("Correct Answer Error",
          "Correct answer for question " + questionNumberField.getText() + " cannot be empty", "");
      return false;
    }
    if (questionTypeField.getValue() == null) {
      ErrorDialog.showErrorDialog("Question Type Error",
          "Question type for question " + questionNumberField.getText() + " cannot be empty", "");
      return false;
    }
    if (pointsField.getText().isEmpty()) {
      ErrorDialog.showErrorDialog("Points Error",
          "Points for question " + questionNumberField.getText() + " cannot be empty", "");
      return false;
    }
    if (!pointsField.getText().matches("[0-9]+(\\.[0-9]+)?")) {
      ErrorDialog.showErrorDialog("Points Error",
          "Points for question " + questionNumberField.getText() + " must be a number", "");
      return false;
    }
    if (Double.parseDouble(pointsField.getText()) <= 0) {
      ErrorDialog.showErrorDialog("Points Error",
          "Points for question " + questionNumberField.getText() + " must be greater than 0", "");
      return false;
    }
    if (getQuestionType() == QuestionType.MULTIPLE_CHOICE && getOptions().length == 4) {
      boolean answerPresent = false;
      for (String option : getOptions()) {
        if (option.toLowerCase().equals(getCorrectAnswer().toLowerCase())) {
          answerPresent = true;
          break;
        }
      }
      if (!answerPresent) {
        ErrorDialog.showErrorDialog("Correct Answer Error",
            "Correct answer for question " + questionNumberField.getText() + " must be one of the options", "");
        return false;
      }
    }
    if (getQuestionType() == QuestionType.TRUE_FALSE
        && !(getCorrectAnswer().toLowerCase().equals("true") || getCorrectAnswer().toLowerCase().equals("false"))) {
      ErrorDialog.showErrorDialog("Correct Answer Error",
          "Correct answer for question " + questionNumberField.getText() + " must be True or False", "");
      return false;
    }
    if (getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
      if (option1Field.getText().isEmpty() || option2Field.getText().isEmpty() || option3Field.getText().isEmpty()
          || option4Field.getText().isEmpty()) {
        ErrorDialog.showErrorDialog("Options Error",
            "Options for question " + questionNumberField.getText() + " cannot be empty", "");
        return false;
      }
    }
    return true;
  }

}
