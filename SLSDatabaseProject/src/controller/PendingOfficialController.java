package controller;

import fxapp.MainFXApplication;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kijana on 4/22/2017.
 */
public class PendingOfficialController {
    @FXML
    private Button backButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Button acceptButton;

    @FXML
    private TableView<Row> table;

    @FXML
    private TableColumn<Row, String> userCol;

    @FXML
    private TableColumn<Row, String> emailCol;

    @FXML
    private TableColumn<Row, String> cityCol;

    @FXML
    private TableColumn<Row, String> stateCol;

    @FXML
    private TableColumn<Row, String> titleCol;

    private Connection conn = MainFXApplication.getConnection();

    @FXML
    private void initialize() throws SQLException {
        String query= "SELECT Username, EmailAddress, City, State, Title FROM" +
                " CITYOFFICIAL NATURAL JOIN USERS WHERE Approved IS NULL;";
        PreparedStatement pstate = conn.prepareStatement(query);
        ResultSet rs = pstate.executeQuery();
        ObservableList<Row> list = FXCollections.observableArrayList();
        String username;
        String emailadd;
        String city;
        String state;
        String title;
        Row temp = new Row();
        while(rs.next()) {
            username = rs.getString("Username");
            emailadd = rs.getString("EmailAddress");
            city = rs.getString("City");
            state = rs.getString("State");
            title = rs.getString("Title");

            temp = new Row();
            temp.user = username;
            temp.email = emailadd;
            temp.city = city;
            temp.state = state;
            temp.title = title;
            list.add(temp);
        }

        userCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().user);
        });
        emailCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().email);
        });
        cityCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().city);
        });
        stateCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().state);
        });
        titleCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().title);
        });

        table.setItems(list);
    }

    public class Row {
        String user;
        String email;
        String city;
        String state;
        String title;
    }

    @FXML
    private void handleAcceptPressed() throws IOException {
        /*Stage stage = (Stage) acceptButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleRejectPressed() throws IOException {
        /*Stage stage = (Stage) rejectButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/LoginScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();*/
    }

    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/AdminHomeScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
