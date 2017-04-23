package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Kijana on 4/22/2017.
 */
public class OfficialHomeController {
    @FXML
    private Button logoutButton;

    @FXML
    private Text filterPOIText;

    @FXML
    private Text reportText;


    @FXML
    private void handleFilterPOIPressed() throws IOException {
        Stage stage = (Stage) reportText.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/ViewPOIScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handlePOIReportPressed() throws IOException {
        Stage stage = (Stage) reportText.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/POIReportScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleLogoutPressed() throws IOException {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
