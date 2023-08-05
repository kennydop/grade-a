package com.gradea.models;

import java.util.List;

public class Question {
  private Integer id;
  private String questionText;
  private String[] options;
  private String correctAnswer;
  private QuestionType type;
  private String userAnswer;
  private double points;

  public Question(String questionText, QuestionType type, String[] options, String correctAnswer,
      double points) {
    this.id = null;
    this.questionText = questionText;
    this.type = type;
    if (type == QuestionType.TRUE_FALSE) {
      this.options = new String[] { "True", "False" };
    } else if (type == QuestionType.SHORT_ANSWER) {
      this.options = new String[] {};
      ;
    } else {
      this.options = options;
    }
    this.correctAnswer = correctAnswer;
    this.userAnswer = "";
    this.points = points;
  }

  public Question(Integer id, String questionText, QuestionType type, String[] options, String correctAnswer,
      double points) {
    this.id = id;
    this.questionText = questionText;
    this.type = type;
    if (type == QuestionType.TRUE_FALSE) {
      this.options = new String[] { "True", "False" };
    } else if (type == QuestionType.SHORT_ANSWER) {
      this.options = new String[] {};
      ;
    } else {
      this.options = options;
    }
    this.correctAnswer = correctAnswer;
    this.userAnswer = "";
    this.points = points;
  }

  public Question(Integer id, String questionText, QuestionType type, String[] options, String correctAnswer,
      double points,
      String userAnswer) {
    this.id = id;
    this.questionText = questionText;
    this.type = type;
    if (type == QuestionType.TRUE_FALSE) {
      this.options = new String[] { "True", "False" };
    } else if (type == QuestionType.SHORT_ANSWER) {
      this.options = new String[] {};
      ;
    } else {
      this.options = options;
    }
    this.correctAnswer = correctAnswer;
    this.userAnswer = userAnswer;
    this.points = points;
  }

  public int getID() {
    return id;
  }

  public String getQuestionText() {
    return questionText;
  }

  public String[] getOptions() {
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

  public void setOptions(String[] options) {
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

  public boolean isCorrect() {
    return userAnswer.toLowerCase().equals(correctAnswer.toLowerCase());
  }

  public double getPoints() {
    return points;
  }

  public void setPoints(double points) {
    this.points = points;
  }

  public enum QuestionType {
    MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER
  }

}
