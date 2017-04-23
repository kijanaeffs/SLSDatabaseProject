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
import java.util.Calendar;
import java.util.List;

/**
 * Created by Kijana on 4/22/2017.
 */
public class ViewPOIController {
    @FXML
    private ComboBox<String> locationBox;

    @FXML
    private ComboBox<String> cityBox;

    @FXML
    private ComboBox<String> stateBox;

    @FXML
    private TextField zipField;

    @FXML
    private CheckBox flaggedCheckBox;

    @FXML
    private DatePicker fromDateField;

    @FXML
    private DatePicker toDateField;

    @FXML
    private Button applyFilterButton;

    @FXML
    private Button resetFilterButton;

    @FXML
    private Button backButton;

    @FXML
    private Button viewButton;

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
        stateBox.setItems(states);

        String queryL = "SELECT LocationName FROM POI ORDER BY LocationName";
        PreparedStatement preparedStmt2 = conn.prepareStatement(queryL);
        ResultSet rsL = preparedStmt2.executeQuery();
        List<String> locationList = new ArrayList<String>();
        while(rsL.next()) {
            locationList.add(rsL.getString("LocationName"));
        }
        ObservableList locations = FXCollections.observableList(locationList);
        locationBox.setItems(locations);
    }

    private int isInputValid() {
        String zip = zipField.getText();
        Object fromDate = fromDateField.getValue();
        Object toDate = toDateField.getValue();
        if (zip.length() != 0) {
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
        }
        if ((fromDate != null && toDate == null) ||
                (toDate != null && fromDate == null)) {
            showDateError();
            return 0;
        }
        if (fromDate != null && toDate != null) {
            String delim = "-";
            String[] tokens = fromDate.toString().split(delim);
            int year = Integer.parseInt(tokens[0]);
            int month = Integer.parseInt(tokens[1]);
            int day = Integer.parseInt(tokens[2]);
            Calendar fromCal = Calendar.getInstance();
            fromCal.set(year, month - 1, day, 0, 0, 0);
            tokens = toDate.toString().split(delim);
            year = Integer.parseInt(tokens[0]);
            month = Integer.parseInt(tokens[1]);
            day = Integer.parseInt(tokens[2]);
            Calendar toCal = Calendar.getInstance();
            toCal.set(year, month - 1, day, 0, 0, 0);
            if (toCal.before(fromCal)) {
                showDateError();
                return 0;
            }
        }
        return 1;
    }

    private void showDateError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Date Range");
        alert.setHeaderText("Date ranges specified are invalid");
        alert.setContentText("To filter by date, you must have a" +
        " valid from date and a valid to date.");
        alert.showAndWait();
    }

    private void showZipError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Zip Code");
        alert.setHeaderText("Zip code field is invalid");
        alert.setContentText("Zip code must be valid to filter by zip.");
        alert.showAndWait();
    }

    /*@FXML
    private void handleLocationPicked() throws SQLException {
        String locSel = locationBox.getValue().toString();
        String queryL = "SELECT DISTINCT State FROM CITYSTATE WHERE Location " +
                "= ? ORDER BY Location";
        PreparedStatement preparedStatement = conn.prepareStatement(queryL);
        preparedStatement.setString(1, locSel);
        ResultSet rsL = preparedStatement.executeQuery();
        rsL.next();
        stateBox.getItems().add(rsL.getString("State"));
    }*/

    @FXML
    private void handleStatePicked() throws SQLException {
        String selState = stateBox.getValue().toString();
        String query2 = "SELECT DISTINCT City FROM CITYSTATE WHERE State = ? ORDER BY City";
        PreparedStatement preparedStmt = conn.prepareStatement(query2);
        preparedStmt.setString(1, selState);
        ResultSet rs2 = preparedStmt.executeQuery();
        List<String> cityList = new ArrayList<String>();
        while (rs2.next()) {
            cityList.add(rs2.getString("City"));
        }
        ObservableList cities = FXCollections.observableList(cityList);
        cityBox.setItems(cities);
    }

    @FXML
    private void handleCityClicked() throws IOException {
        Object selState = stateBox.getValue();
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
    private void handleApplyFilterPressed() throws IOException {
        if (isInputValid() == 1) {

        }
        /*Stage stage = (Stage) applyFilterButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleResetFilterPressed() throws IOException {
        Stage stage = (Stage) resetFilterButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/ViewPOIScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleViewPressed() throws IOException {
        Stage stage = (Stage) viewButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/POIDetailScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/OfficialHomeScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
