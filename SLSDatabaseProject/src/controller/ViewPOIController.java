package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Kijana on 4/22/2017.
 */
public class ViewPOIController {
    @FXML
    ComboBox locationBox;

    @FXML
    ComboBox cityBox;

    @FXML
    ComboBox stateBox;

    @FXML
    TextField zipField;

    @FXML
    CheckBox flaggedCheckBox;

    @FXML
    DatePicker fromDateField;

    @FXML
    DatePicker toDateField;

    @FXML
    Button applyFilterButton;

    @FXML
    Button resetFilterButton;

    @FXML
    Button backButton;

    @FXML
    Button viewButton;

    @FXML
    private void handleApplyFilterPressed() throws IOException {
        /*Stage stage = (Stage) applyFilterButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleResetFilterPressed() throws IOException {
        /*Stage stage = (Stage) resetFilterButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleViewPressed() throws IOException {
        /*Stage stage = (Stage) flagButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/ViewPOIScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
