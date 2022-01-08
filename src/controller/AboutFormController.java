package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutFormController {

    public AnchorPane anchorPaneAbout;

    public void btnOkOnAction(ActionEvent actionEvent) throws IOException {
//        Stage stage = new Stage();
//        anchorPaneAbout.setVisible(false);
        Stage stage = (Stage) anchorPaneAbout.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"))));
        stage.setTitle("TextEd, A simple text editor");
        stage.show();
    }
}
