package com.gradea;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

import com.gradea.controllers.DB;
import com.gradea.controllers.Session;

/**
 * JavaFX App
 */
public class App extends Application {
  @Override
  public void init() {
    DB.getInstance().setupDatabaseConnection();
  }

  private static Scene scene;
  private static Scene splashScene;

  @Override
  public void start(Stage stage) throws IOException {
    // Load and show splash screen first
    Stage splashStage = new Stage();

    splashScene = new Scene(loadFXML("splash"));
    splashScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    splashStage.setScene(splashScene);
    splashStage.initStyle(StageStyle.UNDECORATED);
    splashStage.show();
    PauseTransition pause = new PauseTransition(Duration.seconds(3));

    pause.setOnFinished(e -> {

      Session.getInstance().loadUserFromPrefs();
      Session userSession = Session.getInstance();

      String fxml;
      if (userSession.getCurrentUser() != null) {
        fxml = "dashboard";
      } else {
        fxml = "auth";
      }

      try {
        scene = new Scene(loadFXML(fxml), 1000, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Grade A");
        splashStage.close();
        stage.show();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    });
    pause.play();

    // Session.getInstance().loadUserFromPrefs();
    // Session userSession = Session.getInstance();

    // String fxml;
    // if (userSession.getCurrentUser() != null) {
    // fxml = "dashboard";
    // } else {
    // fxml = "auth";
    // }

    // scene = new Scene(loadFXML(fxml), 1000, 600);
    // scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    // stage.setScene(scene);
    // stage.setTitle("Grade A");
    // // stage.setMaximized(true);
    // stage.show();
  }

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) {
    launch();
  }

}