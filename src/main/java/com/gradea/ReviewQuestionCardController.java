package com.gradea;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReviewQuestionCardController {

  @FXML
  private Label qnNum;

  @FXML
  private Label qn;

  @FXML
  private Label userAns;

  @FXML
  private Label correctAns;

  @FXML
  private ImageView graphic;

  Image checkedImageAsset = new Image(getClass().getResourceAsStream("checked.png"));
  Image crossImageAsset = new Image(getClass().getResourceAsStream("cross.png"));

  public void setData(String questionNumber, String question, String userAnswer, String correctAnswer,
      boolean isCorrect) {
    qnNum.setText(questionNumber + ". ");
    qn.setText(question);
    userAns.setText("Your Answer: " + userAnswer);
    correctAns.setText("Correct Answer: " + correctAnswer);
    if (isCorrect) {
      graphic.setImage(checkedImageAsset);
      userAns.setStyle("-fx-text-fill: green;");
    } else {
      graphic.setImage(crossImageAsset);
      userAns.setStyle("-fx-text-fill: red;");
    }
  }

}
