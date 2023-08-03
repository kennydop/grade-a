package com.gradea.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// import org.mindrot.jbcrypt.BCrypt;

import com.gradea.models.AuthResults;

public class Auth {
  private static final Auth instance = new Auth();
  private Connection dbConnection;

  private Auth() {
    try {

      this.dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB.dbName,
          DB.user,
          DB.password);
    } catch (Exception e) {
      System.out.println("Error connecting to database: " + e.getMessage());
    }
  }

  public static Auth getInstance() {
    return instance;
  }

  // Register a new user
  public AuthResults register(String email, String firstName, String lastName, String password) {

    String sql = "INSERT INTO users (email, password, first_name, last_name) VALUES (?, ?, ?, ?)";
    String checkUserSql = "SELECT * FROM users WHERE email = ?";

    try {
      // Check if user already exists
      PreparedStatement userExistsStmt = dbConnection.prepareStatement(checkUserSql);
      userExistsStmt.setString(1, email);
      ResultSet resultSet = userExistsStmt.executeQuery();
      if (resultSet.next()) {
        return new AuthResults(false, "User already exists");
      }

      // Register user
      String hashedPassword = hashPassword(password);
      PreparedStatement registerStmt = dbConnection.prepareStatement(sql);
      registerStmt.setString(1, email);
      registerStmt.setString(2, hashedPassword);
      registerStmt.setString(3, firstName);
      registerStmt.setString(4, lastName);

      registerStmt.executeUpdate();
      return new AuthResults(true, "User registered successfully");
    } catch (SQLException e) {
      System.err.println("Error registering user: " + e.getMessage());
      return new AuthResults(false, "Error registering user");
    }
  }

  // Authenticate an existing user
  public AuthResults login(String email, String password) {
    String sql = "SELECT * FROM users WHERE email = ?";

    try {
      PreparedStatement stmt = dbConnection.prepareStatement(sql);
      stmt.setString(1, email);
      ResultSet resultSet = stmt.executeQuery();
      String hashedPassword = resultSet.getString("password");
      boolean isPasswordCorrect = verifyPassword(password, hashedPassword);
      if (isPasswordCorrect) {
        return new AuthResults(true, "User logged in successfully");
      } else {
        return new AuthResults(false, "Incorrect password");
      }
    } catch (SQLException e) {
      System.err.println("Error logging in: " + e.getMessage());
      return new AuthResults(false, "Error logging in");
    }
  }

  // Hash a password using BCrypt
  private String hashPassword(String plainTextPassword) {
    // return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    return plainTextPassword;
  }

  // Verify a password against a hashed password
  private boolean verifyPassword(String plainTextPassword, String hashedPassword) {
    // return BCrypt.checkpw(plainTextPassword, hashedPassword);
    return plainTextPassword.equals(hashedPassword);
  }
}
