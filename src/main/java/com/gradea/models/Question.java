package com.gradea.models;

import java.util.List;

public class Question {
  private String questionText;
  private List<String> options;
  private String correctAnswer;
  private QuestionType type;

  public Question(String questionText, QuestionType type, List<String> options, String correctAnswer) {
    this.questionText = questionText;
    this.type = type;
    this.options = options;
    this.correctAnswer = correctAnswer;
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

  public boolean isCorrect(String answer) {
    return answer.equals(correctAnswer);
  }

  public enum QuestionType {
    MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
  }

}
