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
  private static List<Quiz> quizzes;
  private static List<Quiz> quizzesToReview;

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

  public List<Quiz> getQuizzes() {
    return quizzes;
  }

  public List<Quiz> getQuizzesToReview() {
    return quizzesToReview;
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
      fetchUserQuizzes();
    } catch (SQLException e) {
      System.out.println("Error occurred while creating a quiz: " + e.getMessage());
    }
  }

  public void fetchUserQuizzes() {
    int userId = Session.getInstance().getCurrentUser().getID();
    List<Quiz> _quizzes = new ArrayList<>();
    // Fetch the quiz details
    try {
      String sql = "SELECT q.*, o.name AS organization_name FROM quizzes q " +
          "JOIN organization_users ou ON q.org_id = ou.org_id " +
          "JOIN organizations o ON q.org_id = o.id " +
          "LEFT JOIN user_quiz_attempt uqa ON q.id = uqa.quiz_id AND uqa.user_id = ? " +
          "WHERE ou.user_id = ? AND uqa.quiz_id IS NULL AND q.end_date > NOW() " +
          "ORDER BY q.end_date ASC";

      PreparedStatement statement = dbConnection.prepareStatement(sql);
      statement.setInt(1, userId);
      statement.setInt(2, userId);
      ResultSet rs = statement.executeQuery();
      System.out.println("+++++++++++++++++++++++++++++++++++++++++");
      System.out.println(statement);
      System.out.println("+++++++++++++++++++++++++++++++++++++++++");
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
        Question[] questions = fetchQuestionsForQuiz(id);

        Quiz quiz = new Quiz(id, name, description, orgId, orgName, startDate, endDate, duration, passingScore,
            attemptsAllowed,
            questions);
        _quizzes.add(quiz);
      }
    } catch (SQLException e) {
      System.out.println("Error occurred while fetching quizzes: " + e.getMessage());
    }
    this.quizzes = _quizzes;
  }

  public void fetchUserQuizzesToReview() {
    int userId = Session.getInstance().getCurrentUser().getID();
    List<Quiz> _quizzes = new ArrayList<>();
    // Fetch the quiz details
    try {
      String sql = "SELECT DISTINCT q.*, o.name AS organization_name FROM quizzes q " +
          "INNER JOIN organization_users ou ON q.org_id = ou.org_id " +
          "INNER JOIN organizations o ON q.org_id = o.id " +
          "INNER JOIN user_quiz_attempt uqa ON q.id = uqa.quiz_id " +
          "WHERE uqa.user_id = ? " +
          "ORDER BY uqa.created_at ASC";
      PreparedStatement statement = dbConnection.prepareStatement(sql);
      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();
      System.out.println("+++++++++++++++++++++++++++++++++++++++++");
      System.out.println(statement);
      System.out.println("+++++++++++++++++++++++++++++++++++++++++");
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
        Question[] questions = fetchQuestionsForQuiz(id);

        for (int i = 0; i < questions.length; i++) {
          String submittedAnswer = fetchSubmittedAnswerForQuestion(questions[i].getID());
          questions[i].setUserAnswer(submittedAnswer);
        }

        Quiz quiz = new Quiz(id, name, description, orgId, orgName, startDate, endDate, duration, passingScore,
            attemptsAllowed,
            questions);
        _quizzes.add(quiz);
      }
    } catch (SQLException e) {
      System.out.println("Error occurred while fetching recent quizzes: " + e.getMessage());
    }
    this.quizzesToReview = _quizzes;
  }

  public void submitQuiz(Quiz quiz) {
    int userId = Session.getInstance().getCurrentUser().getID();

    try {
      for (Question question : quiz.getQuestions()) {
        String sql = "INSERT INTO submitted_answers (question_id, user_id, quiz_id, answer) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = dbConnection.prepareStatement(sql);
        statement.setString(1, String.valueOf(question.getID()));
        statement.setInt(2, userId);
        statement.setInt(3, quiz.getID());
        statement.setString(4, question.getUserAnswer());
        statement.executeUpdate();
      }

      // Update the score in user_quiz_attempt
      String sql = "INSERT INTO user_quiz_attempt (user_id, quiz_id, score) VALUES (?, ?, ?)";
      PreparedStatement statement = dbConnection.prepareStatement(sql);
      statement.setInt(1, userId);
      statement.setString(2, String.valueOf(quiz.getID()));
      statement.setString(3, String.valueOf(quiz.getScore()));
      statement.executeUpdate();
      fetchUserQuizzes();
      fetchUserQuizzesToReview();
    } catch (SQLException e) {
      System.out.println("Error occurred while submitting a quiz: " + e.getMessage());
    }
  }

  private void createQuestion(Question question, int quizId) throws SQLException {
    String sql = "INSERT INTO questions (quiz_id, correct_answer, question_text, question_type, option1, option2, option3, option4, points) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String[] options = question.getOptions();
    PreparedStatement statement = dbConnection.prepareStatement(sql);
    statement.setInt(1, quizId);
    statement.setString(2, question.getCorrectAnswer());
    statement.setString(3, question.getQuestionText());
    statement.setString(4, question.getType().toString());
    statement.setDouble(9, question.getPoints());
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

  private Question[] fetchQuestionsForQuiz(int quizId) throws SQLException {
    List<Question> questions = new ArrayList<>();
    String sql = "SELECT * FROM questions WHERE quiz_id = ?";
    PreparedStatement statement = dbConnection.prepareStatement(sql);
    statement.setInt(1, quizId);
    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
      int qid = rs.getInt("id");
      String questionText = rs.getString("question_text");
      String correctAnswer = rs.getString("correct_answer");
      QuestionType type = QuestionType.valueOf(rs.getString("question_type"));
      String[] options = new String[] { rs.getString("option1"), rs.getString("option2"), rs.getString("option3"),
          rs.getString("option4") };
      double points = rs.getDouble("points");
      // Create a question object from the result set
      Question question = new Question(qid, questionText, type, options, correctAnswer, points);
      questions.add(question);
    }
    return questions.toArray(new Question[0]);
  }

  public String fetchSubmittedAnswerForQuestion(int questionId) {
    int userId = Session.getInstance().getCurrentUser().getID();
    String sql = "SELECT answer FROM submitted_answers WHERE question_id = ? AND user_id = ?";
    String submittedAnswer = "";

    try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
      pstmt.setInt(1, questionId);
      pstmt.setInt(2, userId);
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        submittedAnswer = rs.getString("answer");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ;
    }

    return submittedAnswer;
  }

}
// The aim of this tutorial is to familiarize you with the GradeA platform by
// presenting you with a series of questions. These questions will have
// instructions for you to follow and will test your understanding of the
// platform's features.
// What is the name of this application? (Hint: GradeA)
// SHORT_ANSWER
// What Tab allows you to view all upcoming quizzes?
// MULTIPLE_CHOICE
// To view all notifications including alerts of due and upcoming quizzes, click
// on the
// MULTIPLE_CHOICE
// To create a quiz you would need to create an organization first.
// TRUE_FALSE
// Which of the following is NOT a type of answer format you can use in a
// question while creating a quiz?
// MULTIPLE_CHOICE
// You can NOT review quizzes after you've submitted them.
// TRUE_FALSE
// You will see a comprehensive list of all organizations you are a part of on
// the _______ page.
// MULTIPLE_CHOICE