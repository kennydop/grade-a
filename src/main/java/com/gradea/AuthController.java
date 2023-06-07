package com.gradea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AuthController implements Initializable {
  @FXML
  private StackPane rootPane;

  @FXML
  private TextField emailField;

  @FXML
  private TextField loginPasswordField;

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField registerPasswordField;

  @FXML
  private PasswordField confirmPasswordField;

  private Node loginNode;
  private Node registerNode;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    try {
      // load the login node
      FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
      loginLoader.setControllerFactory(c -> ControllerFactory.getAuthControllerInstance());
      loginNode = loginLoader.load();

      // load the register node
      FXMLLoader registerLoader = new FXMLLoader(getClass().getResource("register.fxml"));
      registerLoader.setControllerFactory(c -> ControllerFactory.getAuthControllerInstance());
      registerNode = registerLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    rootPane.getChildren().addAll(registerNode, loginNode); // loginNode is at
    // the front
  }

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
  public void swap() {
    RotateTransition rt = new RotateTransition(Duration.seconds(1), rootPane);
    rt.setAxis(Rotate.Y_AXIS);
    rt.setFromAngle(0);
    rt.setToAngle(180);
    rt.setOnFinished(event -> {
      // Swap the nodes in the stack
      Node topNode = rootPane.getChildren().get(1);
      rootPane.getChildren().remove(1);
      rootPane.getChildren().add(0, topNode);
    });
    rt.play();
  }
}
