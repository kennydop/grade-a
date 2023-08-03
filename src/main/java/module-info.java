module com.gradea {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  // requires org.mindrot.jbcrypt;

  opens com.gradea to javafx.fxml;

  exports com.gradea;
}
