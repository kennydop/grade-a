module com.gradea {
  requires javafx.controls;
  requires javafx.fxml;
  // requires com.google.firebase;
  // requires com.google.auth;

  opens com.gradea to javafx.fxml;

  exports com.gradea;
}
