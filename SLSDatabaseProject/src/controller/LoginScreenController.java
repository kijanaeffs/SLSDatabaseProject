package controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreenController {

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;





    @FXML
    private void handleRegisterPressed() throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/RegisterScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleLoginPressed() throws IOException {
       /* Stage stage = (Stage) registerButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/RegisterScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show(); */
    }
}