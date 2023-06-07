package com.gradea;

// import com.google.firebase.auth.FirebaseAuth;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
  @FXML
  private TextField emailField;

  @FXML
  private TextField passwordField;

  @FXML
  private void handleLoginButtonAction() {
    String email = emailField.getText();
    String password = passwordField.getText();
    System.out.println("Login button clicked");
    System.out.println("Email: " + email);
    System.out.println("Password: " + password);

    // Authenticate user with Firebase here
    // FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    // .addOnCompleteListener(task -> {
    // if (task.isSuccessful()) {
    // // User is signed in
    // } else {
    // // Sign in failed
    // }
    // });
  }

}