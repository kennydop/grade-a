package com.gradea;

import java.io.IOException;

import com.gradea.controllers.Quizzes;
import com.gradea.controllers.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class DashboardController {
  @FXML
  private BorderPane rootPane;

  @FXML
  private Button homeButton;
  @FXML
  private ImageView homeImage;
  @FXML
  private Button quizzesButton;
  @FXML
  private ImageView quizzesImage;
  @FXML
  private Button settingButton;
  @FXML
  private Button logoutBtn;
  @FXML
  private ImageView settingImage;
  @FXML
  private Button notificationButton;
  @FXML
  private ImageView notificationImage;
  // @FXML
  // private Circle avatar;
  // @FXML
  // private Label userName, quizzesTaken, quizzesToTake, organizations,
  // averageScore;

  Image homeImageAsset = new Image(getClass().getResourceAsStream("home.png"));
  Image homeImageSelectedAsset = new Image(getClass().getResourceAsStream("home-selected.png"));
  Image quizzesImageAsset = new Image(getClass().getResourceAsStream("quiz.png"));
  Image quizzesImageSelectedAsset = new Image(getClass().getResourceAsStream("quiz-selected.png"));
  Image settingImageAsset = new Image(getClass().getResourceAsStream("setting.png"));
  Image settingImageSelectedAsset = new Image(getClass().getResourceAsStream("setting-selected.png"));
  Image notificationImageAsset = new Image(getClass().getResourceAsStream("bell-ring.png"));
  Image notificationImageSelectedAsset = new Image(getClass().getResourceAsStream("bell-ring-selected.png"));

  @FXML
  public void initialize() {
    setUpNavButton(homeButton, homeImage, homeImageAsset, homeImageSelectedAsset);
    setUpNavButton(quizzesButton, quizzesImage, quizzesImageAsset, quizzesImageSelectedAsset);
    setUpNavButton(settingButton, settingImage, settingImageAsset, settingImageSelectedAsset);
    setUpNavButton(notificationButton, notificationImage, notificationImageAsset, notificationImageSelectedAsset);
    try {
      Quizzes.getInstance().fetchUserQuizzes();
      Quizzes.getInstance().fetchUserQuizzesToReview();
      ;
      homeImage.setImage(homeImageSelectedAsset);
      homeButton.getStyleClass().add("clicked");
      showHome();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void showHome() throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
    Pane view = loader.load();
    HomeController homeController = loader.getController();
    homeController.setDashboardController(this);
    rootPane.setCenter(view);
  }

  @FXML
  private void showQuizzes() throws Exception {
    Pane view = FXMLLoader.load(getClass().getResource("quizzes.fxml"));
    rootPane.setCenter(view);
  }

  @FXML
  private void showSetting() throws Exception {
    Pane view = FXMLLoader.load(getClass().getResource("setting.fxml"));
    rootPane.setCenter(view);
  }

  @FXML
  private void showNotification() throws Exception {
    Pane view = FXMLLoader.load(getClass().getResource("setting.fxml"));
    rootPane.setCenter(view);
  }

  public void showProfile() throws Exception {
    homeImage.setImage(homeImageAsset);
    homeButton.getStyleClass().remove("clicked");
    quizzesImage.setImage(quizzesImageAsset);
    quizzesButton.getStyleClass().remove("clicked");
    settingImage.setImage(settingImageAsset);
    settingButton.getStyleClass().remove("clicked");
    notificationImage.setImage(notificationImageAsset);
    notificationButton.getStyleClass().remove("clicked");
    Pane view = FXMLLoader.load(getClass().getResource("profile.fxml"));
    rootPane.setCenter(view);
  }

  @FXML
  private void handleLogoutButtonAction() {
    try {
      App.setRoot("auth");
      Session.getInstance().clearUser();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setUpNavButton(Button _navButton, ImageView _navImage, Image _navImageAsset,
      Image _navImageSelectedAsset) {
    _navButton.setOnMouseClicked(event -> {
      // Reset all the buttons
      homeImage.setImage(homeImageAsset);
      homeButton.getStyleClass().remove("clicked");
      quizzesImage.setImage(quizzesImageAsset);
      quizzesButton.getStyleClass().remove("clicked");
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
