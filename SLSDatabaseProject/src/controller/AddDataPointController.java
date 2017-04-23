package controller;

/**
 * Created by miles on 4/22/2017.
 */

import fxapp.MainFXApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddDataPointController {

    @FXML
    private Button submitButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ComboBox locationCombo;

    @FXML
    private ComboBox dataTypeCombo;

    @FXML
    private TextField dataValueField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField timeField;

    @FXML
    private Text newLocationText;

    private Connection conn = MainFXApplication.getConnection();

    @FXML
    private void initialize() throws SQLException {
        String queryD = "SELECT DataType FROM DATATYPE ORDER BY DataType";
        PreparedStatement preparedStmt = conn.prepareStatement(queryD);
        ResultSet rsD = preparedStmt.executeQuery();
        List<String> dataTypeList = new ArrayList<String>();
        while(rsD.next()) {
            dataTypeList.add(rsD.getString("DataType"));
        }
        ObservableList dataTypes = FXCollections.observableList(dataTypeList);
        dataTypeCombo.setItems(dataTypes);

        String queryL = "SELECT LocationName FROM POI ORDER BY LocationName";
        PreparedStatement preparedStmt2 = conn.prepareStatement(queryL);
        ResultSet rsL = preparedStmt2.executeQuery();
        List<String> locationList = new ArrayList<String>();
        while(rsL.next()) {
            locationList.add(rsL.getString("LocationName"));
        }
        ObservableList locations = FXCollections.observableList(locationList);
        locationCombo.setItems(locations);
    }

    private int isInputValid() {
        Object location = locationCombo.getValue();
        Object date = dateField.getValue();
        String time = timeField.getText();
        Object dataType = dataTypeCombo.getValue();
        String dataValueString = dataValueField.getText();
        //test for empty values
        if (location == null || date == null || time.length() == 0 ||
                dataType == null || dataValueString.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Fields");
            alert.setHeaderText("Please fill out all fields");
            alert.setContentText("Required fields cannot be left blank.");
            alert.showAndWait();
            return 0;
        }
        //test data value
        int dataValue;
        try {
            dataValue = Integer.parseInt(dataValueString);

        } catch (NumberFormatException ne) {
            showDataValueError();
            return 0;
        }
        if (dataValue < 0) {
            showDataValueError();
            return 0;
        }
        //test time
        if (!time.contains(":")) {
            showTimeError();
            return 0;
        }
        String delim = ":";
        String[] tokens = time.split(delim);
        if (tokens.length != 2) {
            showTimeError();
            return 0;
        }
        int hours;
        int minutes;
        try {
            hours = Integer.parseInt(tokens[0]);
            minutes = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException ne) {
            showTimeError();
            return 0;
        }
        if (hours > 23 || hours < 0 || minutes > 59 || minutes < 0) {
            showTimeError();
            return 0;
        }
        return 1;

    }

    private int addDataPoint() throws SQLException {
        Object location = locationCombo.getValue();
        Object dateObject = dateField.getValue();
        String time = timeField.getText();
        Object dataType = dataTypeCombo.getValue();
        String dataValueString = dataValueField.getText();

        int dataValue = Integer.parseInt(dataValueString);
        String delim = ":";
        String[] tokens = time.split(delim);
        int hour = Integer.parseInt(tokens[0]);
        int minute = Integer.parseInt(tokens[1]);
        delim = "-";
        tokens = dateObject.toString().split(delim);
        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int day = Integer.parseInt(tokens[2]);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, minute, 0);
        Date date = cal.getTime();
        Object sqlDate = new java.sql.Timestamp(date.getTime());

        String query = "INSERT INTO DATAPOINT VALUES (?, ?, NULL, ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setObject(1, sqlDate);
        preparedStmt.setString(2, location.toString());
        preparedStmt.setInt(3, dataValue);
        preparedStmt.setString(4, dataType.toString());
        try {
            preparedStmt.execute();
        } catch (Exception e) {
            showDuplicateError();
            return 0;
        }
        return 1;
    }

    private void showDuplicateError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Data Point");
        alert.setHeaderText("Duplicate Entry");
        alert.setContentText("A data point has already been submitted" +
        " for this location for the given time and date.");
        alert.showAndWait();
    }

    private void showTimeError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Time Field");
        alert.setHeaderText("Time is invalid");
        alert.setContentText("Please insert a valid time.");
        alert.showAndWait();
    }

    private void showDataValueError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Data Value Field");
        alert.setHeaderText("Please correct data value.");
        alert.setContentText("Data value must be a positive integer.");
        alert.showAndWait();
    }

    @FXML
    private void handleLogoutPressed() throws IOException {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleSubmitPressed() throws IOException, SQLException {
        if (isInputValid() == 1 && addDataPoint() == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Data Point Submission Successful");
            alert.setHeaderText("Thank you!");
            alert.setContentText("Your submission was successful.");
            alert.showAndWait();
            Stage stage = (Stage) submitButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass()
                    .getResource("../view/AddDataPointScreen.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        }
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
