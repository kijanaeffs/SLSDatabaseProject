package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Kijana on 4/22/2017.
 */
public class PendingDataPointController {
    @FXML
    private Button backButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Button acceptButton;

    @FXML
    private void handleAcceptPressed() throws IOException {
        /*Stage stage = (Stage) acceptButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleRejectPressed() throws IOException {
        /*Stage stage = (Stage) rejectButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/AdminHomeScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
