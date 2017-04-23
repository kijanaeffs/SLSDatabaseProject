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
        List<String> locationList = new ArrayList<>();
        while(rsL.next()) {
            locationList.add(rsL.getString("LocationName"));
        }
        ObservableList locations = FXCollections.observableList(locationList);
        locationBox.setItems(locations);
    }

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
        /*Stage stage = (Stage) applyFilterButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleResetFilterPressed() throws IOException {
        /*Stage stage = (Stage) resetFilterButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleViewPressed() throws IOException {
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
                .getResource("../view/OfficialHomeScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
