package com.gradea.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.gradea.utils.ErrorDialog;

public class DB {
  private static final DB instance = new DB();
  // // LOCAL MYSQL SERVER
  // protected static final String host = "localhost";
  // protected static final int port = 3306;
  // protected static final String user = "root";
  // protected static final String password = "root";
  // protected static final String dbName = "gradea";
  // protected static final String mysqlServerUrl = "jdbc:mysql://" + host + ":" +
  // port;

  // REMOTE MYSQL SERVER
  protected static final String host = "sql8.freemysqlhosting.net";
  protected static final int port = 3306;
  protected static final String user = "sql8638380";
  protected static final String password = "mabJYZZyXd";
  protected static final String dbName = "sql8638380";
  protected static final String mysqlServerUrl = "jdbc:mysql://" + host + ":" +
      port;

  // Connect to MySQL server with OR without password
  private static boolean connectWithPassword = true;

  private static Connection connection;

  private DB() {
  }

  public static DB getInstance() {
    return instance;
  }

  public Connection setupDatabaseConnection() {

    try {
      if (connection != null) {
        return connection;
      }

      if (connectWithPassword) {
        // Connect to MySQL server with password
        System.out.println("Connecting to MySQL server with password");
        connection = DriverManager.getConnection(mysqlServerUrl, user, password);
      } else {
        // Connect to MySQL server without password
        System.out.println("Connecting to MySQL server without password");
        connection = DriverManager.getConnection(mysqlServerUrl, user, "");
      }

      System.out.println("Connected to MySQL server");

      // Create the database if it doesn't exist
      String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS " + dbName;

      String useDatabaseSql = "USE " + dbName;
      Statement statement = connection.createStatement();
      statement.executeUpdate(createDatabaseSql);
      statement.executeUpdate(useDatabaseSql);
      System.out.println("Using database: " + dbName);

      // create all tables if they don't exist
      createTables();

    } catch (SQLException e) {
      System.out.println("Error connecting to MySQL server: " + e.getMessage());
      ErrorDialog.showErrorDialog("MySQL Error",
          "Error Connecting to MySQL Server " + (connectWithPassword ? "with password"
              : "without password"),
          e.getMessage());
    }
    return connection;
  }

  public static Connection getConnection() {
    try {
      return connection == null ? DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DB.dbName,
          DB.user, connectWithPassword ? DB.password : "") : connection;
    } catch (Exception e) {
      System.out.println("Error connecting to database: " + e.getMessage());
      ErrorDialog.showErrorDialog("MySQL Error", "Error Connecting to Database",
          e.getMessage());
    }
    return connection;
  }

  private void createTables() {
    createUserTable();
    createOrganizationsTable();
    createQuizzesTable();
    createQuestionsTable();
    createOrganizationUsersTable();
    createSubmittedAnswersTable();
    createUserQuizAttemptTable();
  }

  private void createUserTable() {
    String sql = "CREATE TABLE IF NOT EXISTS users (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "email VARCHAR(255) NOT NULL UNIQUE," +
        "first_name VARCHAR(255) NOT NULL," +
        "last_name VARCHAR(255) NOT NULL," +
        "password VARCHAR(255) NOT NULL," +
        "created_at TIMESTAMP DEFAULT '0000-00-00 00:00:00'," +
        "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
        ")";
    execSQL(sql);
    System.out.println("Created users table");
  }

  private void createOrganizationsTable() {
    String sql = "CREATE TABLE IF NOT EXISTS organizations (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "unique_code VARCHAR(255) NOT NULL," +
        "name VARCHAR(255) NOT NULL," +
        "created_by INT," +
        "support_email VARCHAR(255)," +
        "created_at TIMESTAMP DEFAULT '0000-00-00 00:00:00'," +
        "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
        "FOREIGN KEY (created_by) REFERENCES users(id)" +
        ")";
    execSQL(sql);
    System.out.println("Created organizations table");
  }

  private void createQuizzesTable() {
    String sql = "CREATE TABLE IF NOT EXISTS quizzes (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "org_id INT," +
        "name VARCHAR(255) NOT NULL," +
        "description TEXT," +
        "start_date DATETIME," +
        "end_date DATETIME," +
        "duration INT," +
        "passing_score INT," +
        "attempts_allowed INT," +
        "created_at TIMESTAMP DEFAULT '0000-00-00 00:00:00'," +
        "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
        "FOREIGN KEY (org_id) REFERENCES organizations(id)" +
        ")";
    execSQL(sql);
    System.out.println("Created quizzes table");
  }

  private void createQuestionsTable() {
    String sql = "CREATE TABLE IF NOT EXISTS questions (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "quiz_id INT," +
        "question_text TEXT," +
        "correct_answer VARCHAR(255)," +
        "question_type VARCHAR(255)," +
        "option1 TEXT," +
        "option2 TEXT," +
        "option3 TEXT," +
        "option4 TEXT," +
        "points DOUBLE," +
        "created_at TIMESTAMP DEFAULT '0000-00-00 00:00:00'," +
        "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
        "FOREIGN KEY (quiz_id) REFERENCES quizzes(id)" +
        ")";
    execSQL(sql);
    System.out.println("Created questions table");
  }

  private void createOrganizationUsersTable() {
    String sql = "CREATE TABLE IF NOT EXISTS organization_users (" +
        "user_id INT NOT NULL," +
        "org_id INT NOT NULL," +
        "created_at TIMESTAMP DEFAULT '0000-00-00 00:00:00'," +
        "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
        "FOREIGN KEY (user_id) REFERENCES users(id)," +
        "FOREIGN KEY (org_id) REFERENCES organizations(id)" +
        ")";
    execSQL(sql);
    System.out.println("Created organization_users table");
  }

  private void createSubmittedAnswersTable() {
    String sql = "CREATE TABLE IF NOT EXISTS submitted_answers (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "question_id INT," +
        "user_id INT," +
        "quiz_id INT," +
        "answer TEXT," +
        "created_at TIMESTAMP DEFAULT '0000-00-00 00:00:00'," +
        "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
        "FOREIGN KEY (question_id) REFERENCES questions(id)," +
        "FOREIGN KEY (user_id) REFERENCES users(id)," +
        "FOREIGN KEY (quiz_id) REFERENCES quizzes(id)" +
        ")";
    execSQL(sql);
    System.out.println("Created submitted_answers table");
  }

  private void createUserQuizAttemptTable() {
    String sql = "CREATE TABLE IF NOT EXISTS user_quiz_attempt (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "user_id INT," +
        "quiz_id INT," +
        "score INT," +
        "created_at TIMESTAMP DEFAULT '0000-00-00 00:00:00'," +
        "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
        "FOREIGN KEY (user_id) REFERENCES users(id)," +
        "FOREIGN KEY (quiz_id) REFERENCES quizzes(id)" +
        ")";
    execSQL(sql);
    System.out.println("Created user_quiz_attempt table");
  }

  public void execSQL(String cmd) {
    try {
      PreparedStatement stmt = connection.prepareStatement(cmd);
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Error executing sql: " + e.getMessage());
    }
  }
}