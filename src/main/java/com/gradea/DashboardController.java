package com.gradea;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DashboardController {
  @FXML
  private Pane contentPane;

  @FXML
  private Button homeButton;
  @FXML
  private ImageView homeImage;

  @FXML
  public void initialize() {
    try {
      showHome();
    } catch (Exception e) {
      e.printStackTrace();
    }
    setUpNavButton(homeButton, homeImage);
  }

  @FXML
  private void showHome() throws Exception {
    Pane view = FXMLLoader.load(getClass().getResource("home.fxml"));
    contentPane.getChildren().setAll(view);
  }

  // TODO: Add similar methods for showQuizzes, showSettings, etc.

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

  private void setUpNavButton(Button _navButton, ImageView _navImage) {
    _navButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        // Create the color adjust effect
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1);

        // Create the blend effect
        Blend blush = new Blend(
            BlendMode.MULTIPLY,
            monochrome,
            new ColorInput(
                0,
                0,
                _navImage.getImage().getWidth(),
                _navImage.getImage().getHeight(),
                Color.web("#4262ff")));

        Paint currentPaint = ((ColorInput) blush.getBottomInput()).getPaint();
        Color newColor;

        if (currentPaint instanceof Color) {
          Color currentColor = (Color) currentPaint;

          // Check the current color and switch to the other color
          if (currentColor.equals(Color.web("#cccccc"))) {
            newColor = Color.web("#4262ff");
          } else {
            newColor = Color.web("#cccccc");
          }
          // Change the color of the Blend effect
          blush.setBottomInput(new ColorInput(
              0,
              0,
              _navImage.getImage().getWidth(),
              _navImage.getImage().getHeight(),
              newColor));
        }
      }
    });
  }
}
