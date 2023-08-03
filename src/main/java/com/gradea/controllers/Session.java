package com.gradea.controllers;

import java.util.prefs.Preferences;

import com.gradea.models.User;

public class Session {
  private static final Session instance = new Session();
  private User currentUser;
  private Preferences prefs;

  private Session() {
    prefs = Preferences.userNodeForPackage(com.gradea.App.class);
  }

  public static Session getInstance() {
    return instance;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;
    prefs.put("current_user_email", user.getEmail());
  }

  public void loadUserFromPrefs() {
    String email = prefs.get("current_user_email", null);
    if (email != null) {
      this.currentUser = Auth.getInstance().getUserByEmail(email);
    }
  }

  public void clearUser() {
    prefs.remove("current_user_email");
    this.currentUser = null;
  }
}
