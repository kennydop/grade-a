package com.gradea;

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
    System.out.println("Login button clicked");
    System.out.println("Email: " + email);
    System.out.println("Password: " + password);
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

    if (password.equals(confirmPassword)) {
      System.out.println("Create account button clicked");
      System.out.println("First Name: " + fName);
      System.out.println("Last Name: " + lName);
      System.out.println("Email: " + email);
      System.out.println("Password: " + password);

    } else {
      // Passwords do not match
      registerError.setText("Passwords do not match");
    }
    isLoading = false;
  }

  @FXML
  public void swap() {
    // RotateTransition rt = new RotateTransition(Duration.seconds(1), rootPane);
    // rt.setAxis(Rotate.Y_AXIS);
    // rt.setFromAngle(0);
    // rt.setToAngle(180);
    // rt.setOnFinished(event -> {
    // Swap the nodes in the stack
    Node topNode = rootPane.getChildren().get(1);
    rootPane.getChildren().remove(1);
    rootPane.getChildren().add(0, topNode);
    // });
    // rt.play();
  }
}
