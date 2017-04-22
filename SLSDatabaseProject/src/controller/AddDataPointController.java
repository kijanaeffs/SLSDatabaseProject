package controller;

/**
 * Created by miles on 4/22/2017.
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class AddDataPointController {

    @FXML
    private Button submitButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ComboBox<String> locationField;

    @FXML
    private ComboBox<String> dataTypeBox;

    @FXML
    private TextField dataValueField;

    @FXML
    private DatePicker dateField;

    @FXML
    private Text newLocationText;

    @FXML
    private void handleLogoutPressed() throws IOException {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
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

    @FXML
    private void handleNewLocationPressed() throws IOException {
        Stage stage = (Stage) newLocationText.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/AddPOILocationScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
