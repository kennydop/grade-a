package com.gradea.controllers;

import com.gradea.models.Quiz;
import com.gradea.models.Response;
import com.gradea.models.Question;
import com.gradea.models.Question.QuestionType;
import com.gradea.utils.InfoDialog;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Quizzes {

  private static final Quizzes instance = new Quizzes();
  private static Connection dbConnection;

  private Quizzes() {
    try {
      this.dbConnection = DB.getInstance().getConnection();
    } catch (Exception e) {
      System.out.println("Error connecting to database: " + e.getMessage());
    }
  }

  public static Quizzes getInstance() {
    return instance;
  }

  public void createQuiz(Quiz quiz) {
    try {
      String sql = "INSERT INTO quizzes (name, org_id, description, start_date, end_date, duration, passing_score, attempts_allowed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement statement = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, quiz.getName());
      statement.setInt(2, quiz.getOrganizationID());
      statement.setString(3, quiz.getDescription());
      statement.setTimestamp(4, java.sql.Timestamp.valueOf(quiz.getStartDate()));
      statement.setTimestamp(5, java.sql.Timestamp.valueOf(quiz.getEndDate()));
      statement.setInt(6, quiz.getDuration());
      statement.setInt(7, quiz.getPassingScore());
      statement.setInt(8, quiz.getAttemptsAllowed());

      statement.executeUpdate();

      // Fetch the auto-generated quiz ID
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        int quizId = generatedKeys.getInt(1);
        // Insert questions associated with this quiz
        for (Question question : quiz.getQuestions()) {
          createQuestion(question, quizId);
        }
      }
      InfoDialog.showInfoDialog("Quiz created successfully",
          quiz.getName() + " has been created for organization " + quiz.getOrganizationName(), "");
    } catch (SQLException e) {
      System.out.println("Error occurred while creating a quiz: " + e.getMessage());
    }
  }

  public List<Quiz> fetchUserQuizzes() {
    int userId = Session.getInstance().getCurrentUser().getID();
    List<Quiz> quizzes = new ArrayList<>();
    // Fetch the quiz details
    try {
      String sql = "SELECT q.*, o.name AS organization_name FROM quizzes q " +
          "LEFT JOIN organization_users ou ON q.org_id = ou.org_id " +
          "LEFT JOIN organizations o ON q.org_id = o.id OR q.org_id = 1 " +
          "WHERE ou.user_id = ? OR q.org_id = 1";
      PreparedStatement statement = dbConnection.prepareStatement(sql);
      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        // Create quiz object from result set
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int orgId = rs.getInt("org_id");
        String orgName = rs.getString("organization_name");
        LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
        LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();
        int duration = rs.getInt("duration");
        int passingScore = rs.getInt("passing_score");
        int attemptsAllowed = rs.getInt("attempts_allowed");

        // Fetch questions
        Question[] questions = fetchQuestionsForQuiz(rs.getString("id"));

        Quiz quiz = new Quiz(id, name, description, orgId, orgName, startDate, endDate, duration, passingScore,
            attemptsAllowed,
            questions);
        quizzes.add(quiz);
      }
    } catch (SQLException e) {
      System.out.println("Error occurred while fetching quizzes: " + e.getMessage());
    }
    return quizzes;
  }

  public void submitQuiz(String userId, Quiz quiz) {
    // This method should store user's answers to the questions
    try {
      for (Question question : quiz.getQuestions()) {
        String sql = "INSERT INTO submitted_answers (question_id, user_id, quiz_id, answer) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, String.valueOf(question.getId())); // Assume that the ID field is present in the Question
                                                                  // class
        statement.setString(2, userId);
        statement.setString(3, String.valueOf(quiz.getId())); // Assume that the ID field is present in the Quiz class
        statement.setString(4, question.getUserAnswer());
        statement.executeUpdate();
      }

      // Update the score in user_quiz_attempt
      String sql = "UPDATE user_quiz_attempt SET score = ? WHERE user_id = ? AND quiz_id = ?";
      PreparedStatement statement = dbConnection.prepareStatement(sql);
      statement.setString(1, String.valueOf(quiz.getScore()));
      statement.setString(2, userId);
      statement.setString(3, String.valueOf(quiz.getId())); // Assume that the ID field is present in the Quiz class
      statement.executeUpdate();

    } catch (SQLException e) {
      System.out.println("Error occurred while submitting a quiz: " + e.getMessage());
    }
  }

  private void createQuestion(Question question, int quizId) throws SQLException {
    String sql = "INSERT INTO questions (quiz_id, correct_answer, question_text, question_type, option1, option2, option3, option4) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    String[] options = question.getOptions();
    PreparedStatement statement = dbConnection.prepareStatement(sql);
    statement.setInt(1, quizId);
    statement.setString(2, question.getCorrectAnswer());
    statement.setString(3, question.getQuestionText());
    statement.setString(4, question.getType().toString());
    if (options.length == 0) {
      statement.setString(5, "");
      statement.setString(6, "");
      statement.setString(7, "");
      statement.setString(8, "");
    } else if (options.length == 1) {
      statement.setString(5, options[0]);
      statement.setString(6, "");
      statement.setString(7, "");
      statement.setString(8, "");
    } else if (options.length == 2) {
      statement.setString(5, options[0]);
      statement.setString(6, options[1]);
      statement.setString(7, "");
      statement.setString(8, "");
    } else if (options.length == 3) {
      statement.setString(5, options[0]);
      statement.setString(6, options[1]);
      statement.setString(7, options[2]);
      statement.setString(8, "");
    } else if (options.length == 4) {
      statement.setString(5, options[0]);
      statement.setString(6, options[1]);
      statement.setString(7, options[2]);
      statement.setString(8, options[3]);
    }
    statement.executeUpdate();

  }

  private Question[] fetchQuestionsForQuiz(String quizId) throws SQLException {
    List<Question> questions = new ArrayList<>();
    String sql = "SELECT * FROM questions WHERE quiz_id = ?";
    PreparedStatement statement = dbConnection.prepareStatement(sql);
    statement.setString(1, quizId);
    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
      int qid = rs.getInt("id");
      String questionText = rs.getString("question_text");
      String correctAnswer = rs.getString("correct_answer");
      QuestionType type = QuestionType.valueOf(rs.getString("question_type"));
      String[] options = new String[] { rs.getString("option1"), rs.getString("option2"), rs.getString("option3"),
          rs.getString("option4") };
      // Create a question object from the result set
      Question question = new Question(qid, questionText, type, options, correctAnswer, 0);
      questions.add(question);
    }
    return questions.toArray(new Question[0]);
  }

}
