package com.gradea;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.gradea.controllers.Organizations;
import com.gradea.controllers.Quizzes;
import com.gradea.models.Organization;
import com.gradea.models.Question;
import com.gradea.models.Quiz;
import com.gradea.models.Response;
import com.gradea.utils.ErrorDialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class CreateQuizController {

  @FXML
  private TextField quizName;
  @FXML
  private TextField quizDescription;
  @FXML
  private TextField quizDuration;
  @FXML
  private ComboBox<Integer> quizPassingScore;
  @FXML
  private DatePicker quizStartDate;
  @FXML
  TextField quizStartHour;
  @FXML
  TextField quizStartMinute;
  @FXML
  private DatePicker quizDueDate;
  @FXML
  TextField quizDueHour;
  @FXML
  TextField quizDueMinute;
  @FXML
  private ComboBox<Integer> quizAttemptsAllowed;
  @FXML
  private ComboBox<Organization> quizOrganization;
  @FXML
  private Button createQuizButton;
  @FXML
  private Button addQuestionButton;
  @FXML
  private Label createQuizError;
  @FXML
  private VBox questionsContainer;

  private List<QuestionCardController> questionCardControllers = new ArrayList<>();

  private HomeController homeController;

  public void setHomeController(HomeController homeController) {
    this.homeController = homeController;
  }

  @FXML
  public void initialize() {
    createQuizButton.requestFocus();
    addQuestionCard();
    Response orgRes = Organizations.getInstance().getUserCreatedOrganizations();
    List<Organization> organizations = (List<Organization>) orgRes.getData();
    if (organizations.size() == 0) {
      createQuizError.setText("You must create an organization before creating a quiz.");
      return;
    }
    quizOrganization.getItems().addAll(organizations);

    quizOrganization.setCellFactory(new Callback<ListView<Organization>, ListCell<Organization>>() {
      @Override
      public ListCell<Organization> call(ListView<Organization> param) {
        return new ListCell<Organization>() {
          @Override
          protected void updateItem(Organization item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
              setText(null);
            } else {
              setText(item.getName());
            }
          }
        };
      }
    });

    quizOrganization.setConverter(new StringConverter<Organization>() {
      @Override
      public String toString(Organization organization) {
        if (organization == null) {
          return null;
        } else {
          return organization.getName();
        }
      }

      @Override
      public Organization fromString(String string) {
        return null;
      }
    });

    quizPassingScore.getItems().addAll(35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100);
    quizAttemptsAllowed.getItems().addAll(1, 2, 3, 4, 5);
    addQuestionButton.setOnAction(event -> addQuestionCard());
    createQuizButton.setOnAction(event -> createQuiz());
  }

  private void addQuestionCard() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("question-card.fxml"));
      Parent questionCard = loader.load();
      QuestionCardController controller = loader.getController();
      controller.setQuestionNumber(questionCardControllers.size() + 1);
      questionCardControllers.add(controller);
      questionsContainer.getChildren().add(questionCard);
    } catch (IOException e) {
      System.out.println("Error loading question card: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void createQuiz() {
    if (!validateInputs()) {
      return;
    }
    String name = quizName.getText();
    String description = quizDescription.getText();
    Integer duration = Integer.parseInt(quizDuration.getText());
    Integer passingScore = quizPassingScore.getValue();
    LocalDateTime startDate = convertToLocalDateTime(quizStartDate.getValue(),
        Integer.parseInt(quizStartHour.getText()), Integer.parseInt(quizStartMinute.getText()));
    LocalDateTime dueDate = convertToLocalDateTime(quizDueDate.getValue(), Integer.parseInt(quizDueHour.getText()),
        Integer.parseInt(quizDueMinute.getText()));
    Integer attemptsAllowed = quizAttemptsAllowed.getValue();
    Integer organizationID = quizOrganization.getValue().getID();
    String organizationName = quizOrganization.getValue().getName();
    Question[] questions = new Question[questionCardControllers.size()];

    // ...

    // Iterate over questionCardControllers to get their values
    for (QuestionCardController controller : questionCardControllers) {
      Question qn = controller.getQuestion();
      questions[controller.getQuestionNumber() - 1] = qn;
      System.out.println(qn.getQuestionText());
      System.out.println(qn.getType());
    }

    Quiz quiz = new Quiz(name, description, organizationID, organizationName, startDate, dueDate, duration,
        passingScore, attemptsAllowed, questions);

    Quizzes.getInstance().createQuiz(quiz);

    homeController.refreshQuizzes();
    // close the Create Quiz window
    quizName.getScene().getWindow().hide();
  }

  private LocalDateTime convertToLocalDateTime(LocalDate date, int hour, int minute) {

    // Create a LocalTime from the hours and minutes
    LocalTime time = LocalTime.of(hour, minute);

    // Combine the LocalDate and LocalTime into a LocalDateTime
    LocalDateTime dateTime = LocalDateTime.of(date, time);

    return dateTime;
  }

  private boolean validateInputs() {
    createQuizError.setText("");
    if (quizName.getText().isEmpty()) {
      createQuizError.setText("Please enter a quiz name");
      ErrorDialog.showErrorDialog("Field Error", "Please enter a quiz name", "");
      return false;
    } else if (quizOrganization.getValue() == null) {
      createQuizError.setText("Please select an organization");
      ErrorDialog.showErrorDialog("Field Error", "Please select an organization", "");
      return false;
    } else if (quizDescription.getText().isEmpty() || quizDescription.getText().length() < 10
        || quizDescription.getText().length() > 100) {
      createQuizError.setText("Quiz description must be between 10 and 100 characters");
      ErrorDialog.showErrorDialog("Field Error", "Quiz description must be between 10 and 100 characters", "");
      return false;
    } else if (quizStartDate.getValue() == null) {
      createQuizError.setText("Please select a start date");
      ErrorDialog.showErrorDialog("Field Error", "Please select a start date", "");
      return false;
    } else if (quizStartHour.getText().isEmpty()) {
      createQuizError.setText("Please enter a start hour");
      ErrorDialog.showErrorDialog("Field Error", "Please enter a start hour", "");
      return false;
    } else if (!quizStartHour.getText().matches("[0-9]+")) {
      createQuizError.setText("Start hour must be a number");
      ErrorDialog.showErrorDialog("Field Error", "Start hour must be a number", "");
      return false;
    } else if (Integer.parseInt(quizStartHour.getText()) < 0
        || Integer.parseInt(quizStartHour.getText()) > 23) {
      createQuizError.setText("Start hour must be between 0 and 23");
      ErrorDialog.showErrorDialog("Field Error", "Start hour must be between 0 and 23", "");
      return false;
    } else if (quizStartMinute.getText().isEmpty()) {
      createQuizError.setText("Please enter a start minute");
      ErrorDialog.showErrorDialog("Field Error", "Please enter a start minute", "");
      return false;
    } else if (!quizStartMinute.getText().matches("[0-9]+")) {
      createQuizError.setText("Start minute must be a number");
      ErrorDialog.showErrorDialog("Field Error", "Start minute must be a number", "");
      return false;
    } else if (Integer.parseInt(quizStartMinute.getText()) < 0
        || Integer.parseInt(quizStartMinute.getText()) > 59) {
      createQuizError.setText("Start minute must be between 0 and 59");
      ErrorDialog.showErrorDialog("Field Error", "Start minute must be between 0 and 59", "");
      return false;
    } else if (quizDueDate.getValue() == null) {
      createQuizError.setText("Please select a due date");
      ErrorDialog.showErrorDialog("Field Error", "Please select a due date", "");
      return false;
    } else if (quizDueHour.getText().isEmpty()) {
      createQuizError.setText("Please enter a due hour");
      ErrorDialog.showErrorDialog("Field Error", "Please enter a due hour", "");
      return false;
    } else if (!quizDueHour.getText().matches("[0-9]+")) {
      createQuizError.setText("Due hour must be a number");
      ErrorDialog.showErrorDialog("Field Error", "Due hour must be a number", "");
      return false;
    } else if (Integer.parseInt(quizDueHour.getText()) < 0
        || Integer.parseInt(quizDueHour.getText()) > 23) {
      createQuizError.setText("Due hour must be between 0 and 23");
      ErrorDialog.showErrorDialog("Field Error", "Due hour must be between 0 and 23", "");
      return false;
    } else if (quizDueMinute.getText().isEmpty()) {
      createQuizError.setText("Please enter a due minute");
      ErrorDialog.showErrorDialog("Field Error", "Please enter a due minute", "");
      return false;
    } else if (!quizDueMinute.getText().matches("[0-9]+")) {
      createQuizError.setText("Due minute must be a number");
      ErrorDialog.showErrorDialog("Field Error", "Due minute must be a number", "");
      return false;
    } else if (Integer.parseInt(quizDueMinute.getText()) < 0
        || Integer.parseInt(quizDueMinute.getText()) > 59) {
      createQuizError.setText("Due minute must be between 0 and 59");
      ErrorDialog.showErrorDialog("Field Error", "Due minute must be between 0 and 59", "");
      return false;
    } else if (quizDuration.getText().isEmpty() || Integer.parseInt(quizDuration.getText()) < 5
        || Integer.parseInt(quizDuration.getText()) > 259200) {
      createQuizError.setText("Quiz duration must be between 5 and 259200 seconds");
      ErrorDialog.showErrorDialog("Field Error", "Quiz duration must be between 5 and 259200 seconds", "");
      return false;
    } else if (quizStartDate.getValue().isEqual(quizDueDate.getValue())
        && Integer.parseInt(quizStartHour.getText()) > Integer.parseInt(quizDueHour.getText())) {
      createQuizError.setText("Start hour must be before due hour");
      ErrorDialog.showErrorDialog("Field Error", "Start hour must be before due hour", "");
      return false;
    } else if (quizStartDate.getValue().isEqual(quizDueDate.getValue()) &&
        Integer.parseInt(quizStartHour.getText()) == Integer.parseInt(quizDueHour.getText()) &&
        Integer.parseInt(quizStartMinute.getText()) > Integer.parseInt(quizDueMinute.getText())) {
      createQuizError.setText("Start minute must be before due minute");
      ErrorDialog.showErrorDialog("Field Error", "Start minute must be before due minute", "");
      return false;
    } else if (quizStartDate.getValue().isEqual(quizDueDate.getValue())
        && Integer.parseInt(quizStartHour.getText()) == Integer.parseInt(quizDueHour.getText())
        && Integer.parseInt(quizStartMinute.getText()) == Integer.parseInt(quizDueMinute.getText())) {
      createQuizError.setText("Start time must be before due time");
      ErrorDialog.showErrorDialog("Field Error", "Start time must be before due time", "");
      return false;
    } else if (quizStartDate.getValue().isAfter(quizDueDate.getValue())) {
      createQuizError.setText("Start date must be before due date");
      ErrorDialog.showErrorDialog("Field Error", "Start date must be before due date", "");
      return false;
    } else if (quizStartDate.getValue().isBefore(LocalDate.now())) {
      createQuizError.setText("Start date must today or later");
      ErrorDialog.showErrorDialog("Field Error", "Start date must be today or later", "");
      return false;
    } else if (convertToLocalDateTime(quizStartDate.getValue(), Integer.parseInt(quizStartHour.getText()),
        Integer.parseInt(quizStartMinute.getText())).isBefore(LocalDateTime.now())) {
      createQuizError.setText("Start time must be in the future");
      ErrorDialog.showErrorDialog("Field Error", "Start time must be in the future", "");
      return false;
    }
    // duration can't exceed due date
    else if (convertToLocalDateTime(quizStartDate.getValue(), Integer.parseInt(quizStartHour.getText()),
        Integer.parseInt(quizStartMinute.getText())).plusSeconds(Integer.parseInt(quizDuration.getText()))
        .isAfter(convertToLocalDateTime(quizDueDate.getValue(), Integer.parseInt(quizDueHour.getText()),
            Integer.parseInt(quizDueMinute.getText())))) {
      createQuizError.setText("Quiz duration exceeds due date");
      ErrorDialog.showErrorDialog("Field Error", "Quiz duration exceeds due date", "");
      return false;
    } else if (quizPassingScore.getValue() == null) {
      createQuizError.setText("Please select a passing score");
      ErrorDialog.showErrorDialog("Field Error", "Please select a passing score", "");
      return false;
    } else if (quizAttemptsAllowed.getValue() == null) {
      createQuizError.setText("Please select the number of attempts allowed");
      ErrorDialog.showErrorDialog("Field Error", "Please select the number of attempts allowed", "");
      return false;
    } else {
      for (QuestionCardController controller : questionCardControllers) {
        if (!controller.validateInputs()) {
          return false;
        }
      }
      return true;
    }
  }
}
