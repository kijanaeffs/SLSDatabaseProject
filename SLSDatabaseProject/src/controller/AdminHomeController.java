package controller;

/**
 * Created by miles on 4/22/2017.
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
public class AdminHomeController {

    @FXML
    private Button logoutButton;

    @FXML
    private Text dataPointsText;

    @FXML
    private Text cityOfficialsText;

    @FXML
    private void handleLogoutPressed() throws IOException {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handlePendingPointsPressed() throws IOException {
        Stage stage = (Stage) dataPointsText.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/PendingDataPointScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handlePendingAccountsPressed() throws IOException {
        Stage stage = (Stage) cityOfficialsText.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/PendingOfficialScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
