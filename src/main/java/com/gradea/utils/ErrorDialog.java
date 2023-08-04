package com.gradea.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorDialog {
  public static void showErrorDialog(String title, String headerText, String contentText) {
    Platform.runLater(() -> {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle(title);
      alert.setHeaderText(headerText);
      alert.setContentText(contentText);
      alert.showAndWait();
    });
  }
}
