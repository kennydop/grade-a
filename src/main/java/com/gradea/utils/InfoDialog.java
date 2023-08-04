package com.gradea.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InfoDialog {
  public static void showInfoDialog(String title, String headerText, String contentText) {
    Platform.runLater(() -> {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle(title);
      alert.setHeaderText(headerText);
      alert.setContentText(contentText);
      alert.showAndWait();
    });
  }
}
