package com.gradea.models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Quiz {
  private String name;
  private String description;
  private String organization;
  private LocalDate startDate;
  private LocalDate endDate;
  private int duration;
  private int passingScore;
  private int attemptsAllowed;
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
    this.passingScore = passingScore;
    this.attemptsAllowed = attemptsAllowed;
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

  public String getDueDate() {
    long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
    if (daysBetween == 0)
      return "Due today";
    else if (daysBetween == 1)
      return "Due tomorrow";
    else
      return "Due in " + daysBetween + " days";
  }

  public String getFormattedDuration() {
    Duration duration = Duration.ofSeconds(this.duration);

    long hours = duration.toHours();
    duration = duration.minusHours(hours);
    long minutes = duration.toMinutes();
    duration = duration.minusMinutes(minutes);
    long secs = duration.getSeconds();

    StringBuilder sb = new StringBuilder();
    if (hours > 0) {
      sb.append(hours).append(" hr");
      if (hours > 1)
        sb.append("s");
      if (minutes > 0 || secs > 0)
        sb.append(", ");
    }
    if (minutes > 0) {
      sb.append(minutes).append(" min");
      if (minutes > 1)
        sb.append("s");
      if (secs > 0)
        sb.append(", ");
    }
    if (secs > 0) {
      sb.append(secs).append(" sec");
      if (secs > 1)
        sb.append("s");
    }

    return sb.toString();
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getPassingScore() {
    return passingScore;
  }

  public void setPassingScore(int passingScore) {
    this.passingScore = passingScore;
  }

  public int getAttemptsAllowed() {
    return attemptsAllowed;
  }

  public void setAttemptsAllowed(int attemptsAllowed) {
    this.attemptsAllowed = attemptsAllowed;
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
