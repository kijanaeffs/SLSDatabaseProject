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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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
    private Text newLocationText;

    private Connection conn = MainFXApplication.getConnection();

    private void initialize() throws SQLException {
        /*String queryD = "SELECT DataType FROM DATATYPE ORDER BY DataType";
        PreparedStatement preparedStmt = conn.prepareStatement(queryD);
        ResultSet rsD = preparedStmt.executeQuery();
        List<String> dataTypeList = new ArrayList<String>();
        while(rsD.next()) {
            dataTypeList.add(rsD.getString("DataType"));
        }*/
        //ObservableList dataTypes = FXCollections.observableList(dataTypeList);
        /*List<String> test = new ArrayList<String>();
        test.add("A");
        test.add("B");
        ObservableList dataTypes = FXCollections.observableList(test);*/
        dataTypeCombo.getItems().addAll("A");

        String queryL = "SELECT LocationName FROM POI ORDER BY LocationName";
        PreparedStatement preparedStmt2 = conn.prepareStatement(queryL);
        ResultSet rsL = preparedStmt2.executeQuery();
        List<String> locationList = new ArrayList<>();
        while(rsL.next()) {
            locationList.add(rsL.getString("LocationName"));
        }
        ObservableList locations = FXCollections.observableList(locationList);
        locationCombo.setItems(locations);
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
    private void handleSubmitPressed() throws IOException {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/AddDataPointScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
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
