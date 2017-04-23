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
public class PendingDataPointController {
    @FXML
    private Button backButton;

    @FXML
    private Button rejectButton;

    @FXML
    private Button acceptButton;

    @FXML
    private TableView<Row> table;

    @FXML
    private TableColumn<Row, String> locCol;

    @FXML
    private TableColumn<Row, String> typeCol;

    @FXML
    private TableColumn<Row, String> valueCol;

    @FXML
    private TableColumn<Row, String> timeCol;

    private Connection conn = MainFXApplication.getConnection();

    @FXML
    private void initialize() throws SQLException {

        String query = "SELECT LocationName, DataType, DataValue, DT FROM " +
                "DATAPOINT WHERE Accepted IS NULL ORDER BY LocationName";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();

        String location;
        String dt;
        String value;
        String type;
        ObservableList<Row> list = FXCollections.observableArrayList();
        while (rs.next()) {
            location = rs.getString("LocationName");
            dt = rs.getString("DT");
            value = rs.getString("DataValue");
            type = rs.getString("DataType");
            Row temp = new Row();
            temp.setLoc(location);
            temp.setDt(dt);
            temp.setValue(value);
            temp.setType(type);
            list.add(temp);
        }
        locCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().getLoc());
        });
        typeCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().getType());
        });
        valueCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().getvalue());
        });
        timeCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().getDt());
        });
        table.setItems(list);

    }

    public class Row {
        String dt;
        String loc;
        String value;
        String type;

        public String getDt() {
            return dt;
        }
        public String getLoc() {
            return loc;
        }
        public String getvalue() {
            return value;
        }
        public String getType() {
            return type;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    @FXML
    private void handleAcceptPressed() throws IOException, SQLException {
        Row selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        String location = selected.getLoc();
        String dt = selected.getDt();
        String query = "UPDATE DATAPOINT SET Accepted = TRUE WHERE " +
                "LocationName = ? AND DT = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, location);
        preparedStmt.setString(2, dt);
        preparedStmt.execute();
        Stage stage = (Stage) acceptButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/PendingDataPointScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleRejectPressed() throws IOException, SQLException {
        Row selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        String location = selected.getLoc();
        String dt = selected.getDt();
        String query = "UPDATE DATAPOINT SET Accepted = FALSE WHERE " +
                "LocationName = ? AND DT = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, location);
        preparedStmt.setString(2, dt);
        preparedStmt.execute();
        Stage stage = (Stage) rejectButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/PendingDataPointScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
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
