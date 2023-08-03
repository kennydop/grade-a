package com.gradea.models;

public class User {
  private int userID;
  private String email;
  private String firstName;
  private String lastName;

  public User(int userID, String email, String firstName, String lastName) {
    this.userID = userID;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public int getUserID() {
    return userID;
  }

  public void setUserID(int userID) {
    this.userID = userID;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return firstName + " " + lastName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
