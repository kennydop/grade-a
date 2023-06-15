package com.gradea.models;

import java.util.List;

public class Question {
  private String questionText;
  private List<String> options;
  private String correctAnswer;
  private QuestionType type;
  private String userAnswer;

  public Question(String questionText, QuestionType type, List<String> options, String correctAnswer) {
    this.questionText = questionText;
    this.type = type;
    if (type == QuestionType.TRUE_FALSE) {
      this.options = List.of("True", "False");
    } else if (type == QuestionType.SHORT_ANSWER) {
      this.options = List.of();
    } else {
      this.options = options;
    }
    this.correctAnswer = correctAnswer;
    this.userAnswer = "";
  }

  public String getQuestionText() {
    return questionText;
  }

  public List<String> getOptions() {
    return options;
  }

  public String getCorrectAnswer() {
    return correctAnswer;
  }

  public QuestionType getType() {
    return type;
  }

  public String getUserAnswer() {
    return userAnswer;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public void setCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
  }

  public void setType(QuestionType type) {
    this.type = type;
  }

  public void setUserAnswer(String userAnswer) {
    this.userAnswer = userAnswer;
  }

  public boolean isCorrect(String answer) {
    return answer.equals(correctAnswer);
  }

  public enum QuestionType {
    MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
  }

}
