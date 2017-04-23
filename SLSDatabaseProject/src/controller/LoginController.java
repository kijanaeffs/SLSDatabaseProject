package controller;


import fxapp.MainFXApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    private Connection conn = MainFXApplication.getConnection();


    private int isInputValid() throws SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String query = "SELECT EXISTS(SELECT Username, Pass FROM USERS " +
                "WHERE Username = ? AND " + "Pass = ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, username);
        preparedStmt.setString(2, password);
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();
        int check = rs.getInt(1);
        if (check == 0) {
            // Show the error message if bad data
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setHeaderText("Please correct invalid credentials");
            alert.setContentText("Username and password combination " +
            "do not exist.");
            alert.showAndWait();
            return 0;
        }
        query = "SELECT (NOT(EXISTS(SELECT Username FROM CITYOFFICIAL " +
                "WHERE Username = ?)) OR EXISTS(SELECT Username, Approved " +
                "FROM CITYOFFICIAL WHERE Username = ? AND Approved = TRUE))";
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, username);
        preparedStmt.setString(2, username);
        rs = preparedStmt.executeQuery();
        rs.next();
        check = rs.getInt(1);
        if (check == 0) {
            // Show the error message if bad data
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Credentials");
            alert.setHeaderText("Sorry");
            alert.setContentText("This account is not approved.");
            alert.showAndWait();
            return 0;
        }
        return 1;
    }

    private String getUserType() throws SQLException {
        String username = usernameField.getText();

        String query = "SELECT UserType FROM USERS WHERE Username = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, username);
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();
        return rs.getString(1);
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
    private void handleLoginPressed() throws IOException, SQLException {
        if (isInputValid() == 0) {
           passwordField.setText("");
        } else {
            String userType = getUserType();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            if (userType.equals("Admin")) {
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/AdminHomeScreen.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } else if (userType.equals("Official")) {
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/OfficialHomeScreen.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            } else if (userType.equals("Scientist")) {
                Parent root = FXMLLoader.load(getClass()
                        .getResource("../view/AddDataPointScreen.fxml"));
                stage.setScene(new Scene(root));
                stage.show();
            }
        }
    }
}