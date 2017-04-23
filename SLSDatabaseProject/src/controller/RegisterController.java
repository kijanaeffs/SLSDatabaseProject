package controller;


import fxapp.MainFXApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class RegisterController {

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
        List<String> userTypeList = new ArrayList<>();
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
            return;
        }
    }

    @FXML
    private void handleCreatePressed() throws IOException {
        Stage stage = (Stage) createButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
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