package com.gradea;

import java.io.File;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DashboardController {
  @FXML
  private Pane contentPane;

  @FXML
  private Button homeButton;
  @FXML
  private ImageView homeImage;
  @FXML
  private Button quizButton;
  @FXML
  private ImageView quizImage;
  @FXML
  private Button settingButton;
  @FXML
  private ImageView settingImage;
  @FXML
  private Button notificationButton;
  @FXML
  private ImageView notificationImage;
  Image homeImageAsset = new Image(getClass().getResourceAsStream("home.png"));
  Image homeImageSelectedAsset = new Image(getClass().getResourceAsStream("home-selected.png"));
  Image quizImageAsset = new Image(getClass().getResourceAsStream("quiz.png"));
  Image quizImageSelectedAsset = new Image(getClass().getResourceAsStream("quiz-selected.png"));
  Image settingImageAsset = new Image(getClass().getResourceAsStream("setting.png"));
  Image settingImageSelectedAsset = new Image(getClass().getResourceAsStream("setting-selected.png"));
  Image notificationImageAsset = new Image(getClass().getResourceAsStream("bell-ring.png"));
  Image notificationImageSelectedAsset = new Image(getClass().getResourceAsStream("bell-ring-selected.png"));

  @FXML
  public void initialize() {
    try {
      showHome();
    } catch (Exception e) {
      e.printStackTrace();
    }
    setUpNavButton(homeButton, homeImage, homeImageAsset, homeImageSelectedAsset);
    setUpNavButton(quizButton, quizImage, quizImageAsset, quizImageSelectedAsset);
    setUpNavButton(settingButton, settingImage, settingImageAsset, settingImageSelectedAsset);
    setUpNavButton(notificationButton, notificationImage, notificationImageAsset, notificationImageSelectedAsset);
  }

  @FXML
  private void showHome() throws Exception {
    Pane view = FXMLLoader.load(getClass().getResource("home.fxml"));
    contentPane.getChildren().setAll(view);
  }

  @FXML
  private void showQuiz() throws Exception {
    Pane view = FXMLLoader.load(getClass().getResource("quiz.fxml"));
    contentPane.getChildren().setAll(view);
  }

  @FXML
  private void showSetting() throws Exception {
    Pane view = FXMLLoader.load(getClass().getResource("setting.fxml"));
    contentPane.getChildren().setAll(view);
  }

  @FXML
  private void createOrganization() {
    // Your code here
  }

  @FXML
  private void joinOrganization() {
    // Your code here
  }

  @FXML
  private void createQuiz() {
    // Your code here
  }

  private void setUpNavButton(Button _navButton, ImageView _navImage, Image _navImageAsset,
      Image _navImageSelectedAsset) {
    _navButton.setOnMouseClicked(event -> {
      // Reset all the buttons
      homeImage.setImage(homeImageAsset);
      homeButton.getStyleClass().remove("clicked");
      quizImage.setImage(quizImageAsset);
      quizButton.getStyleClass().remove("clicked");
      settingImage.setImage(settingImageAsset);
      settingButton.getStyleClass().remove("clicked");
      notificationImage.setImage(notificationImageAsset);
      notificationButton.getStyleClass().remove("clicked");

      // Change the image
      _navImage.setImage(_navImageSelectedAsset);
      // Change the color of the button
      _navButton.getStyleClass().add("clicked");
    });
  }
}
