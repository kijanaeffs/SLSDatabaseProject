package controller;


import fxapp.MainFXApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confPasswordField;

    @FXML
    private TextField titleField;

    @FXML
    private Button createButton;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox cityCombo;

    @FXML
    private ComboBox stateCombo;

    @FXML
    private ComboBox typeCombo;

    private Connection conn = MainFXApplication.getConnection();
    @FXML
    private void initialize() throws SQLException {

        String queryU = "SELECT COLUMN_TYPE FROM information_schema.COLUMNS " +
                "WHERE TABLE_NAME = ? AND COLUMN_NAME = ? ";
        PreparedStatement preparedStmtU = conn.prepareStatement(queryU);
        preparedStmtU.setString(1, "USERS");
        preparedStmtU.setString(2, "UserType");
        ResultSet rsu = preparedStmtU.executeQuery();
        rsu.next();
        String result = rsu.getString(1);
        int resultLength = result.length();
        result = result.substring(6,resultLength - 2);
        List<String> userTypeList = new ArrayList<String>();
        String delims = "','";
        String[] tokens = result.split(delims);
        for (int i = 0; i < tokens.length; i++ ) {
            userTypeList.add(tokens[i]);
        }
        ObservableList userTypes = FXCollections.observableList(userTypeList);
        typeCombo.setItems(userTypes);

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT DISTINCT State FROM CITYSTATE ORDER BY State");
        List<String> stateList = new ArrayList<String>();
        while(rs.next()) {
            stateList.add(rs.getString("State"));
        }
        ObservableList states = FXCollections.observableList(stateList);
        stateCombo.setItems(states);
    }

    private int isInputValid() throws SQLException {
        String username = usernameField.getText();
        String emailAddress = emailField.getText();
        String password = passwordField.getText();
        String confPassword = confPasswordField.getText();
        Object userType = typeCombo.getValue();
        Object state = stateCombo.getValue();
        Object city = cityCombo.getValue();
        String title = titleField.getText();
        if (username.length() == 0 || emailAddress.length() == 0 ||
                password.length() == 0 || confPassword.length() == 0 ||
                userType == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText("Required fields cannot be left blank.");
            alert.showAndWait();
            return 0;
        }
        if (!emailAddress.contains("@") || !emailAddress.contains(".")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText("Please enter a valid email address.");
            alert.showAndWait();
            return 0;
        }

        String query = "SELECT (EXISTS(SELECT Username FROM USERS WHERE " +
                "Username = ?) OR EXISTS(SELECT EmailAddress FROM USERS " +
                "WHERE EmailAddress = ?))";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, username);
        preparedStmt.setString(2, emailAddress);
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();
        if (rs.getInt(1) == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText("Username or email address is in use.");
            alert.showAndWait();
            return 0;
        }
        if (!password.equals(confPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText("Password and confirm password do " +
                            "not match.");
            alert.showAndWait();
            return 0;
        }
        if (userType.toString().equals("Official")) {
            if (state == null || city == null || title.length() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText("Please fill out all values in the " +
                        "form for City Official accounts.");
                alert.showAndWait();
                return 0;
            }
        }
        return 1;
    }

    private void addUser() throws SQLException {
        String username = usernameField.getText();
        String emailAddress = emailField.getText();
        String password = passwordField.getText();
        Object userType = typeCombo.getValue();
        Object state = stateCombo.getValue();
        Object city = cityCombo.getValue();
        String title = titleField.getText();

        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, emailAddress);
        preparedStmt.setString(2, username);
        preparedStmt.setString(3, password);
        preparedStmt.setString(4, userType.toString());
        preparedStmt.execute();

        if (userType.toString().equals("Official")) {
            query = "INSERT INTO CITYOFFICIAL VALUES(?, ?, 0, ?, ?)";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, username);
            preparedStmt.setString(2, title);
            preparedStmt.setString(3, city.toString());
            preparedStmt.setString(4, state.toString());
            preparedStmt.execute();
        }

    }

    @FXML
    private void handleStatePicked() throws SQLException {
        String selState = stateCombo.getValue().toString();
        String query2 = "SELECT DISTINCT City FROM CITYSTATE WHERE State = ? ORDER BY City";
        PreparedStatement preparedStmt = conn.prepareStatement(query2);
        preparedStmt.setString(1, selState);
        ResultSet rs2 = preparedStmt.executeQuery();
        List<String> cityList = new ArrayList<String>();
        while(rs2.next()) {
            cityList.add(rs2.getString("City"));
        }
        ObservableList cities = FXCollections.observableList(cityList);
        cityCombo.setItems(cities);
    }

    @FXML
    private void handleCityClicked() throws IOException {
        Object selState = stateCombo.getValue();
        //error message does not work yet
        if (selState == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No state selected");
            alert.setHeaderText("Please select a state.");
            alert.setContentText("The state must be selected before the city.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCreatePressed() throws IOException, SQLException {
        if (isInputValid() == 1) {
            addUser();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText("Thank you!");
            alert.setContentText("Your registration was successful.");
            alert.showAndWait();
            Stage stage = (Stage) createButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/LoginScreen.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

}