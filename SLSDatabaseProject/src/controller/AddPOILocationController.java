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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kijana on 4/22/2017.
 */
public class AddPOILocationController {
    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> cityCombo;

    @FXML
    private ComboBox<String> stateCombo;

    @FXML
    private TextField zipField;

    @FXML
    private Button backButton;

    @FXML
    private Button submitButton;

    private Connection conn = MainFXApplication.getConnection();

    @FXML
    private void initialize() throws SQLException {
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
        String location = locationField.getText();
        Object state = stateCombo.getValue();
        Object city = cityCombo.getValue();
        String zip = zipField.getText();
        if (location.length() == 0 || state == null ||
                city == null || zip.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Entries");
            alert.setHeaderText("All fields must be completed");
            alert.setContentText("Please complete all fields.");
            alert.showAndWait();
            return 0;
        }
        if (zip.length() != 5) {
            showZipError();
            return 0;
        }
        int zipInt;
        try {
            zipInt = Integer.parseInt(zip);
        } catch (NumberFormatException ne) {
            showZipError();
            return 0;
        }
        if (zipInt < 0) {
            showZipError();
            return 0;
        }

        String query = "SELECT EXISTS(SELECT LocationName FROM POI WHERE" +
                " LocationName = ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, location);
        ResultSet rs = preparedStmt.executeQuery();
        rs.next();
        if (rs.getInt(1) == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("POI Location Duplicate");
            alert.setHeaderText("This POI Location name already exists");
            alert.setContentText("Please enter a unique POI location.");
            alert.showAndWait();
            return 0;
        }
        return 1;
    }

    private void addPOILocation() throws SQLException {
        String location = locationField.getText();
        Object state = stateCombo.getValue();
        Object city = cityCombo.getValue();
        String zip = zipField.getText();
        int zipInt = Integer.parseInt(zip);
        String query = "INSERT INTO POI(LocationName, City, State, ZipCode)" +
                " VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, location);
        preparedStmt.setString(2, city.toString());
        preparedStmt.setString(3, state.toString());
        preparedStmt.setInt(4, zipInt);
        preparedStmt.execute();
    }

    private void showZipError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Zip Code");
        alert.setHeaderText("Zip code field must be a valid zip");
        alert.setContentText("Please enter a valid zip code.");
        alert.showAndWait();
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
        if (selState == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No state selected");
            alert.setHeaderText("Please select a state.");
            alert.setContentText("The state must be selected before the city.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/AddDataPointScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleSubmitPressed() throws IOException, SQLException {
        if (isInputValid() == 1) {
            addPOILocation();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("POI Location Submission Successful");
            alert.setHeaderText("Thank you!");
            alert.setContentText("Your submission was successful.");
            alert.showAndWait();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/AddPOILocationScreen.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}
