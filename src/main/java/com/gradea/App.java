package com.gradea;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;

/**
 * JavaFX App
 */
public class App extends Application {
  @Override
  public void init() {
    // FirebaseOptions options = FirebaseOptions.builder()
    // .setCredentials(GoogleCredentials.getApplicationDefault())
    // .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
    // .build();
    // FirebaseApp.initializeApp(options);
  }

  private static Scene scene;

  @Override
  public void start(Stage stage) throws IOException {
    scene = new Scene(loadFXML("auth"), 800, 600);
    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    stage.setScene(scene);
    stage.setTitle("Grade A");
    stage.isMaximized();
    stage.show();
  }

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    fxmlLoader.setControllerFactory(c -> ControllerFactory.getAuthControllerInstance());
    return fxmlLoader.load();
  }

  public static void main(String[] args) {
    launch();
  }

}