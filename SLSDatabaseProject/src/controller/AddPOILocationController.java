package controller;

import fxapp.MainFXApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kijana on 4/22/2017.
 */
public class AddPOILocationController {
    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> cityBox;

    @FXML
    private ComboBox<String> stateBox;

    @FXML
    private TextField zipField;

    @FXML
    private Button backButton;

    @FXML
    private Button submitButton;

    @FXML
    private void initialize() throws SQLException {
        Connection conn = MainFXApplication.getConnection();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT DISTINCT State FROM CITYSTATE ORDER BY State");
        List<String> stateList = new ArrayList<String>();
        while(rs.next()) {
            stateList.add(rs.getString("State"));
        }
        ObservableList states = FXCollections.observableList(stateList);
        stateBox.setItems(states);

        ResultSet rs2 = statement.executeQuery("SELECT DISTINCT City FROM CITYSTATE ORDER BY City");
        List<String> cityList = new ArrayList<String>();
        while(rs2.next()) {
            cityList.add(rs2.getString("City"));
        }
        ObservableList cities = FXCollections.observableList(cityList);
        cityBox.setItems(cities);
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
    private void handleSubmitPressed() throws IOException {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/AddDataPointScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
