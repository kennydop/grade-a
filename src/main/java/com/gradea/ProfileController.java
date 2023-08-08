package com.gradea;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

import com.gradea.controllers.Organizations;
import com.gradea.controllers.Session;
import com.gradea.models.Organization;
import com.gradea.models.User;

public class ProfileController {

  @FXML
  private Label nameLabel;

  @FXML
  private Label emailLabel;

  @FXML
  private TableView<Organization> organizationsTable;

  @FXML
  private TableColumn<Organization, String> orgNameColumn;

  @FXML
  private TableColumn<Organization, String> orgUniqueCodeColumn;

  private User user;

  @FXML
  private void initialize() {
    // Initialize the organization table columns.
    orgNameColumn.setCellValueFactory(new PropertyValueFactory<Organization, String>("name"));
    orgUniqueCodeColumn.setCellValueFactory(new PropertyValueFactory<Organization, String>("uniqueCode"));

    setUser(Session.getInstance().getCurrentUser());
  }

  public void setUser(User user) {
    this.user = user;

    nameLabel.setText(user.getName());
    emailLabel.setText(user.getEmail());

    ObservableList<Organization> organizations = FXCollections.observableArrayList();
    organizations.addAll((List<Organization>) Organizations.getInstance().getUserOrganizations().getData());

    organizationsTable.setItems(organizations);
  }

}
