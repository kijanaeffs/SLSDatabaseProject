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

/**
 * Created by Kijana on 4/22/2017.
 */
public class POIDetailController {
   @FXML
   private TextField locationField;

    @FXML
    private TextField flaggedField;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private TextField fromDataField;

    @FXML
    private TextField toDataField;

    @FXML
    private DatePicker fromDateField;

    @FXML
    private DatePicker toDateField;

    @FXML TextField fromTimeField;

    @FXML TextField toTimeField;

    @FXML
    private Button applyFilterButton;

    @FXML
    private Button resetFilterButton;

    @FXML
    private Button backButton;

    @FXML
    private Button flagButton;

    private Connection conn = MainFXApplication.getConnection();

    @FXML
    private void initialize() throws SQLException {
        Statement statement = conn.createStatement();
        String queryD = "SELECT DataType FROM DATATYPE ORDER BY DataType";
        PreparedStatement preparedStmt = conn.prepareStatement(queryD);
        ResultSet rsD = preparedStmt.executeQuery();
        List<String> dataTypeList = new ArrayList<String>();
        while(rsD.next()) {
            dataTypeList.add(rsD.getString("DataType"));
        }
        ObservableList dataTypes = FXCollections.observableList(dataTypeList);
        typeBox.setItems(dataTypes);

    }
/*
    private int isInputValid() {
        Object fromDate = fromDateField.getValue();
        Object toDate = toDateField.getValue();
        String fromData = fromDataField.getText();
        String toData = toDataField.getText();
        String fromTime = fromTimeField.getText();
        String toTime = toTimeField.getText();

        if ((fromData.length() != 0 && toData.length() == 0) ||
                (toData.length() != 0 && fromData.length() == 0)) {
            showDataValueError();
            return 0;
        }
        if (fromData.length() != 0 && toData.length() != 0) {
            int toDataValue;
            int fromDataValue;
            try {
                fromDataValue = Integer.parseInt(fromData);
                toDataValue = Integer.parseInt(toData);

            } catch (NumberFormatException ne) {
                showDataValueError();
                return 0;
            }
            if (fromDataValue < 0 || toDataValue < 0 ||
                    toDataValue < fromDataValue) {
                showDataValueError();
                return 0;
            }
        }
        if ((fromTime.length() != 0 && toTime.length() == 0) ||
                (toTime.length() != 0 && fromTime.length() == 0)) {
            showTimeError();
            return 0;
        }
        if (fromTime.length() != 0 && toTIme.length() != 0) {
            if (!fromTime.contains(":") || !toTime.contains(":")) {
                showTimeError();
                return 0;
            }
            String delim = ":";
            String[] tokens = fromTime.split(delim);
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
            fromCal.set(year, month - 2 + 1, day, 0, 0, 0);
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
*/
    private void showDateError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Date Ranges");
        alert.setHeaderText("Date ranges specified are invalid");
        alert.setContentText("To filter by date, you must have a" +
                " valid from date and a valid to date.");
        alert.showAndWait();
    }

    private void showDataValueError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Data Ranges");
        alert.setHeaderText("Data ranges specified are invalid");
        alert.setContentText("To filter by data, you must have a" +
                " valid from data point to a valid to data point.");
        alert.showAndWait();
    }

    @FXML
    private void handleApplyFilterPressed() throws IOException {
        //if (isInputValid() == 1) {

        //}
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
                .getResource("../view/POIDetailScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleFlagPressed() throws IOException {
        /*Stage stage = (Stage) flagButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/ViewPOIScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
