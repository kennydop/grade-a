package com.gradea;

import java.io.IOException;
import java.util.regex.Pattern;

import com.gradea.controllers.Auth;
import com.gradea.models.AuthResults;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class AuthController {
  @FXML
  private StackPane rootPane;

  @FXML
  private TextField loginEmailField;

  @FXML
  private TextField loginPasswordField;

  @FXML
  private TextField fNameField;
  @FXML
  private TextField lNameField;
  @FXML
  private TextField registerEmailField;

  @FXML
  private PasswordField registerPasswordField;

  @FXML
  private PasswordField confirmPasswordField;
  @FXML
  private Node loginNode;
  @FXML
  private Node registerNode;

  @FXML
  private Text registerError;
  @FXML
  private Text loginError;

  private boolean isLoading = false;

  // Regular expression for email validation
  private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

  // Regular expression for password validation
  private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$");

  @FXML
  public void goToRegister() {
    if (rootPane.getChildren().get(1) == loginNode) {
      swap();
    }
  }

  @FXML
  public void goToLogin() {
    if (rootPane.getChildren().get(1) == registerNode) {
      swap();
    }
  }

  @FXML
  private void handleLoginButtonAction() {
    if (isLoading) {
      return;
    }
    loginError.setText("");
    isLoading = true;
    String email = loginEmailField.getText();
    String password = loginPasswordField.getText();

    if (!EMAIL_REGEX.matcher(email).matches()) {
      loginError.setText("Invalid email address");
      return;
    }

    AuthResults authResults = Auth.getInstance().login(email, password);
    if (authResults.getSuccess()) {
      // User is logged in, redirect to dashboard
      try {
        App.setRoot("dashboard");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      // Login failed
      loginError.setText(authResults.getMessage());
    }
    isLoading = false;
  }

  @FXML
  private void handleCreateAccountButtonAction() {
    if (isLoading) {
      return;
    }
    registerError.setText("");
    isLoading = true;
    String fName = fNameField.getText();
    String lName = lNameField.getText();
    String email = registerEmailField.getText();
    String password = registerPasswordField.getText();
    String confirmPassword = confirmPasswordField.getText();

    if (!EMAIL_REGEX.matcher(email).matches()) {
      registerError.setText("Invalid email address");
      return;
    }

    if (!PASSWORD_REGEX.matcher(password).matches()) {
      loginError.setText("Invalid password. Password must contain at least one digit, " +
          "one lowercase letter, one uppercase letter, and be between 8 to 20 characters.");
      return;
    }

    if (password.equals(confirmPassword)) {
      AuthResults authResults = Auth.getInstance().register(fName, lName, email, password);
      if (authResults.getSuccess()) {
        // User is registered, redirect to dashboard
        try {
          App.setRoot("dashboard");
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        // Registration failed
        registerError.setText(authResults.getMessage());
      }
    } else {
      // Passwords do not match
      registerError.setText("Passwords do not match");
    }
    isLoading = false;
  }

  @FXML
  public void swap() {
    Node topNode = rootPane.getChildren().get(1);
    rootPane.getChildren().remove(1);
    rootPane.getChildren().add(0, topNode);
  }
}
