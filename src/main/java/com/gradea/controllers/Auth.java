package com.gradea.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.gradea.models.Response;
import com.gradea.models.User;

public class Auth {
  private static final Auth instance = new Auth();
  private static Connection dbConnection;

  private Auth() {
    try {
      this.dbConnection = DB.getInstance().getConnection();
    } catch (Exception e) {
      System.out.println("Error connecting to database: " + e.getMessage());
    }
  }

  public static Auth getInstance() {
    return instance;
  }

  // Register a new user
  public Response register(String email, String password, String firstName, String lastName) {

    String sql = "INSERT INTO users (email, password, first_name, last_name) VALUES (?, ?, ?, ?)";

    try {
      // Check if user already exists
      User existingUser = getUserByEmail(email);
      if (existingUser != null) {
        System.out.println("User already exists");
        return new Response(false, "User already exists");
      }

      // Register user
      String hashedPassword = hashPassword(password);
      PreparedStatement registerStmt = dbConnection.prepareStatement(sql);
      registerStmt.setString(1, email);
      registerStmt.setString(2, hashedPassword);
      registerStmt.setString(3, firstName);
      registerStmt.setString(4, lastName);

      registerStmt.executeUpdate();
      User user = getUserByEmail(email);
      Session.getInstance().setCurrentUser(user);
      return new Response(true, "User registered successfully");
    } catch (SQLException e) {
      System.err.println("Error registering user: " + e.getMessage());
      return new Response(false, "Error registering user");
    }
  }

  // Authenticate an existing user
  public Response login(String email, String password) {
    String sql = "SELECT * FROM users WHERE email = ?";

    try {
      PreparedStatement stmt = dbConnection.prepareStatement(sql);
      stmt.setString(1, email);
      ResultSet resultSet = stmt.executeQuery();
      if (!resultSet.next()) {
        return new Response(false, "User not found");
      }

      String hashedPassword = resultSet.getString("password");
      boolean isPasswordCorrect = verifyPassword(password, hashedPassword);
      if (isPasswordCorrect) {
        User user = getUserByEmail(email);
        Session.getInstance().setCurrentUser(user);
        return new Response(true, "User logged in successfully");
      } else {
        return new Response(false, "Incorrect password");
      }
    } catch (SQLException e) {
      System.err.println("Error logging in: " + e.getMessage());
      return new Response(false,
          e.getMessage().equals("Illegal operation on empty result set.") ? "User not found" : "Error logging in");
    }
  }

  // Fetch a user from the database by their email
  public User getUserByEmail(String email) {
    String sql = "SELECT * FROM users WHERE email = ?";

    try {
      PreparedStatement stmt = dbConnection.prepareStatement(sql);
      stmt.setString(1, email);
      ResultSet resultSet = stmt.executeQuery();

      if (resultSet.next()) {
        System.out.println("User found");
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");

        return new User(id, email, firstName, lastName);
      }
      System.out.println("User not found");
      return null; // User not found
    } catch (SQLException e) {
      System.err.println("Error getting user: " + e.getMessage());
      return null;
    }
  }

  // Hash a password using BCrypt
  private String hashPassword(String plainTextPassword) {
    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
  }

  // Verify a password against a hashed password
  private boolean verifyPassword(String plainTextPassword, String hashedPassword) {
    return BCrypt.checkpw(plainTextPassword, hashedPassword);
  }
}
