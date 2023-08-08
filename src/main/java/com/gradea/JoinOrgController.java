package com.gradea;

import com.gradea.controllers.Organizations;
import com.gradea.models.Response;
import com.gradea.utils.InfoDialog;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class JoinOrgController {
  @FXML
  private TextField orgCode;

  @FXML
  private Button joinButton;

  @FXML
  private Label joinError;

  private HomeController homeController;

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  @FXML
  public void initialize() {
    joinError.requestFocus();
  }

  @FXML
  void joinOrganization() {
    if (orgCode.getText().isEmpty()) {
      joinError.setText("Please enter an organization unique code");
      return;
    }
    if (!orgCode.getText().matches("^[a-zA-Z0-9]*$")) {
      joinError.setText("Organization unique code can only contain\nletters and numbers");
      return;
    }
    if (orgCode.getText().length() != 5) {
      joinError.setText("Organization unique code must be\n5 characters long");
      return;
    }
    Response joinedRes = Organizations.getInstance().joinOrganization(orgCode.getText());
    if (joinedRes.getSuccess()) {
      homeController.refreshQuizzes();
      Stage stage = (Stage) joinButton.getScene().getWindow();
      stage.close();
      InfoDialog.showInfoDialog("Joined Organization", "You have successfully joined the organization", "");
    } else {
      joinError.setText(joinedRes.getMessage());
    }
  }
}
