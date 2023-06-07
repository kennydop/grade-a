package com.gradea;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class RegisterController {
  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private PasswordField confirmPasswordField;

  @FXML
  private void handleCreateAccountButtonAction() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    String confirmPassword = confirmPasswordField.getText();

    if (password.equals(confirmPassword)) {
      // Create user with Firebase here
      // FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
      // .addOnCompleteListener(task -> {
      // if (task.isSuccessful()) {
      // // User is signed in
      // } else {
      // // Sign up failed
      // }
      // });
    } else {
      // Passwords do not match
    }
  };

}
