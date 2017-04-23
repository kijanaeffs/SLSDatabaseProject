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
public class POIDetailController {
   @FXML
   private TextField locationField;

    @FXML
    private TextField flaggedField;

    @FXML
    private ComboBox typeBox;

    @FXML
    private TextField fromDataField;

    @FXML
    private TextField toDataField;

    @FXML
    private TextField fromDateField;

    @FXML
    private TextField toDateField;

    @FXML
    private Button applyFilterButton;

    @FXML
    private Button resetFilterButton;

    @FXML
    private Button backButton;

    @FXML
    private Button flagButton;


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
    private void handleFlagPressed() throws IOException {
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
