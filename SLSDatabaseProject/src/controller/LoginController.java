package controller;


import fxapp.MainFXApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private Boolean isInputValid() throws SQLException {
        Connection conn = MainFXApplication.getConnection();
        String username = usernameField.getText();
        String password = passwordField.getText();

        String query = "SELECT EXISTS(SELECT Username, Pass FROM USERS " +
                "WHERE Username = ? AND " + "Pass = ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, username);
        preparedStmt.setString(2, password);

        // execute the preparedstatement
        preparedStmt.execute();

    }




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