package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Kijana on 4/22/2017.
 */
public class AddPOILocationController {
    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> cityBox;

    @FXML
    private ComboBox<String> stateBox;

    @FXML
    private TextField zipField;

    @FXML
    private Button backButton;

    @FXML
    private Button submitButton;


    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/AddDataPointScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleSubmitPressed() throws IOException {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/AddDataPointScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
