package com.gradea;

import java.util.regex.Pattern;

import com.gradea.controllers.Organizations;
import com.gradea.models.Response;
import com.gradea.utils.InfoDialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateOrgController {

  @FXML
  private TextField orgName;

  @FXML
  private TextField orgSupportEmail;

  @FXML
  private Button createButton;

  @FXML
  private Label createError;

  @FXML
  private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

  @FXML
  public void initialize() {
    createError.requestFocus();
  }

  @FXML
  void createOrganization() {
    if (orgName.getText().isEmpty()) {
      createError.setText("Please enter an organization name");
      return;
    }
    if (!orgName.getText().matches("^[a-zA-Z0-9 ]*$")) {
      createError.setText("Organization name can only contain\nletters, numbers and spaces");
      return;
    }
    if (orgName.getText().length() < 2 || orgName.getText().length() > 40) {
      createError.setText("Organization name must be between\n2 and 40 characters long");
      return;
    }
    if (!EMAIL_REGEX.matcher(orgSupportEmail.getText()).matches()) {
      createError.setText("Invalid email address");
      return;
    }
    Response joinedRes = Organizations.getInstance().createOrganization(orgName.getText(), orgSupportEmail.getText());
    if (joinedRes.getSuccess()) {
      Stage stage = (Stage) createButton.getScene().getWindow();
      stage.close();
      InfoDialog.showInfoDialog("Created Organization",
          "You have successfully created " + orgName.getText() + " with unique code: " + joinedRes.getData().toString(),
          "Please share this code with others so they can join the organization");
    } else {
      createError.setText(joinedRes.getMessage());
    }
  }

}
