package com.gradea;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class JoinOrgController {

  @FXML
  void showJoinQuizDialog() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("join-org.fxml"));
      Parent root = loader.load();

      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.setResizable(false);
      dialogStage.setScene(new Scene(root));
      dialogStage.show();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
