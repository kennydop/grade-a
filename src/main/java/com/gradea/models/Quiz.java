package com.gradea.models;

import java.time.LocalDate;
import java.util.Date;

public class Quiz {
  private String name;
  private String description;
  private String organization;
  private LocalDate startDate;
  private LocalDate endDate;
  private int duration;
  private int totalQuestions;
  private int passingScore;
  private int totalAttempts;
  private Question[] questions;

  public Quiz(String name, String description, String organization, LocalDate startDate, LocalDate endDate,
      int duration,
      int passingScore, int attemptsAllowed, Question[] questions) {
    this.name = name;
    this.description = description;
    this.organization = organization;
    this.startDate = startDate;
    this.endDate = endDate;
    this.duration = duration;
    this.totalQuestions = totalQuestions;
    this.passingScore = passingScore;
    this.totalAttempts = totalAttempts;
    this.questions = questions;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getTotalQuestions() {
    return totalQuestions;
  }

  public void setTotalQuestions(int totalQuestions) {
    this.totalQuestions = totalQuestions;
  }

  public int getPassingScore() {
    return passingScore;
  }

  public void setPassingScore(int passingScore) {
    this.passingScore = passingScore;
  }

  public int getTotalAttempts() {
    return totalAttempts;
  }

  public void setTotalAttempts(int totalAttempts) {
    this.totalAttempts = totalAttempts;
  }

  public Question[] getQuestions() {
    return questions;
  }

  public void setQuestions(Question[] questions) {
    this.questions = questions;
  }

  public double getScore() {
    double score = 0;
    for (Question question : questions) {
      if (question.isCorrect()) {
        score += question.getPoints();
      }
    }
    return score;
  }

  public int getPercentage() {
    return (int) Math.round((double) getScore() / (double) getTotalScore() * 100);
  }

  public boolean isPassed() {
    return getPercentage() >= getPassingScore();
  }

  public double getTotalScore() {
    double score = 0;
    for (Question question : questions) {
      score += question.getPoints();
    }
    return score;
  }

}
