module com.gradea {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires jbcrypt;
  requires java.prefs;

  opens com.gradea.models;
  opens com.gradea to javafx.fxml;

  exports com.gradea;
}
