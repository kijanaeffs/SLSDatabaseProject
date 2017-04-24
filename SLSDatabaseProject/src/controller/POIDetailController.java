package controller;

import fxapp.MainFXApplication;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.*;

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

    @FXML
    private TableView<Row> table;

    @FXML
    private TableColumn<Row, String> dtCol;

    @FXML
    private TableColumn<Row, String> valueCol;

    @FXML
    private TableColumn<Row, String> typeCol;

    private Connection conn = MainFXApplication.getConnection();

    private String initLocation;

    private String initFlag;

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
        typeBox.getItems().add(0, "None Selected");
        typeBox.setValue("None Selected");

    }

    public void setUp(String loc, String flg) {
        if (flg.equals("True") || flg.equals("Yes")) {
            initFlag = "Yes";
        } else {
            initFlag = "No";
        }
        initLocation = loc;
        locationField.setText(initLocation);
        flaggedField.setText(initFlag);
    }

    private int isInputValid() {
        Object fromDate = fromDateField.getValue();
        Object toDate = toDateField.getValue();
        String fromData = fromDataField.getText();
        String toData = toDataField.getText();
        String fromTime = fromTimeField.getText();
        String toTime = toTimeField.getText();
        //test data
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
        //test time
        int fromHours = 0;
        int fromMinutes = 0;
        int toHours = 0;
        int toMinutes = 0;
        if ((fromTime.length() != 0 && toTime.length() == 0) ||
                (toTime.length() != 0 && fromTime.length() == 0)) {
            showTimeError();
            return 0;
        }
        if (fromTime.length() != 0 && toTime.length() != 0) {
            if (!fromTime.contains(":") || !toTime.contains(":")) {
                showTimeError();
                return 0;
            }
            String delim = ":";
            String[] tokens = fromTime.split(delim);
            String [] tokens2 = toTime.split(delim);
            if (tokens.length != 2 || tokens2.length != 2) {
                showTimeError();
                return 0;
            }
            try {
                fromHours = Integer.parseInt(tokens[0]);
                fromMinutes = Integer.parseInt(tokens[1]);
                toHours = Integer.parseInt(tokens2[0]);
                toMinutes = Integer.parseInt(tokens2[1]);
            } catch (NumberFormatException ne) {
                showTimeError();
                return 0;
            }
            if (fromHours > 23 || fromHours < 0 || fromMinutes > 59 || fromMinutes < 0) {
                showTimeError();
                return 0;
            }
            if (toHours > 23 || toHours < 0 || toMinutes > 59 || toMinutes < 0) {
                showTimeError();
                return 0;
            }

        }
        //test date
        if ((fromDate != null && toDate == null) ||
                (toDate != null && fromDate == null)) {
            showDateError();
            return 0;
        }
        if (((fromDate != null && toDate != null) && toTime.length() == 0) ||
                ((fromDate != null && toDate != null) && fromTime.length() == 0) ||
                ((fromTime.length() != 0 && toTime.length() != 0) && toDate == null) ||
                ((fromTime.length() != 0 && toTime.length() != 0) && fromDate == null)) {
            showDateTimeError();
            return 0;
        }
        if (fromDate != null && toDate != null) {
            /*String delim = ":";
            String[] tokens1 = fromTime.split(delim);
            String [] tokens2 = toTime.split(delim);
            fromHours = Integer.parseInt(tokens1[0]);
            fromMinutes = Integer.parseInt(tokens1[1]);
            toHours = Integer.parseInt(tokens2[0]);
            toMinutes = Integer.parseInt(tokens2[1]);*/
            String delim = "-";
            String[] tokens = fromDate.toString().split(delim);
            int year = Integer.parseInt(tokens[0]);
            int month = Integer.parseInt(tokens[1]);
            int day = Integer.parseInt(tokens[2]);
            Calendar fromCal = Calendar.getInstance();
            fromCal.set(year, month - 2 + 1, day, fromHours, fromMinutes, 0);
            tokens = toDate.toString().split(delim);
            year = Integer.parseInt(tokens[0]);
            month = Integer.parseInt(tokens[1]);
            day = Integer.parseInt(tokens[2]);
            Calendar toCal = Calendar.getInstance();
            toCal.set(year, month - 1, day, toHours, toMinutes, 0);
            if (toCal.before(fromCal)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Date Time Error");
                alert.setHeaderText("From Date After To Date");
                alert.setContentText("From date and time must be before to " +
                                "date and time.");
                alert.showAndWait();
                return 0;
            }
        }
        return 1;
    }

    private void showDateError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Date Ranges");
        alert.setHeaderText("Date ranges specified are invalid");
        alert.setContentText("To filter by date, you must have a" +
                " valid from date and a valid to date.");
        alert.showAndWait();
    }

    private void showTimeError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Time Ranges");
        alert.setHeaderText("Time ranges specified are invalid");
        alert.setContentText("To filter by time, you must have a" +
                " valid from time and a valid to time.");
        alert.showAndWait();
    }

    private void showDateTimeError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Date and Time Ranges");
        alert.setHeaderText("Time and Date ranges specified are invalid");
        alert.setContentText("To filter by time and date, you must fill" +
        " out all date and time fields.");
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
    private void handleApplyFilterPressed() throws SQLException {
        String type = typeBox.getValue().toString();
        if (isInputValid() == 1) {
            Integer minNum = 0;
            Integer maxNum = 0;
            if (fromDataField.getText().length() != 0) {
                minNum = Integer.parseInt(fromDataField.getText());
            }
            if (toDataField.getText().length() != 0) {
                maxNum = Integer.parseInt(toDataField.getText());
            }

            String loc = locationField.getText();
            Object fromDate = fromDateField.getValue();
            Object toDate = toDateField.getValue();
            String fromTime = fromTimeField.getText();
            String toTime = toTimeField.getText();
            Object sqlFromDate = null;
            Object sqlToDate = null;
            if (fromDate != null) {
                String delim = ":";
                String[] tokens = fromTime.split(delim);
                String [] tokens2 = toTime.split(delim);
                int fromHours = Integer.parseInt(tokens[0]);
                int fromMinutes = Integer.parseInt(tokens[1]);
                int toHours = Integer.parseInt(tokens2[0]);
                int toMinutes = Integer.parseInt(tokens2[1]);
                delim = "-";
                tokens = fromDate.toString().split(delim);
                int year = Integer.parseInt(tokens[0]);
                int month = Integer.parseInt(tokens[1]);
                int day = Integer.parseInt(tokens[2]);
                Calendar fromCal = Calendar.getInstance();
                fromCal.set(year, month - 1, day, fromHours, fromMinutes, 0);
                tokens = toDate.toString().split(delim);
                year = Integer.parseInt(tokens[0]);
                month = Integer.parseInt(tokens[1]);
                day = Integer.parseInt(tokens[2]);
                Calendar toCal = Calendar.getInstance();
                toCal.set(year, month - 1, day, toHours, toMinutes, 0);
                java.util.Date fDate = fromCal.getTime();
                sqlFromDate = new java.sql.Timestamp(fDate.getTime());
                java.util.Date tDate = toCal.getTime();
                sqlToDate = new java.sql.Timestamp(tDate.getTime());
            }


            String query = "SELECT DT, LocationName, DataValue, DataType FROM" +
                    " DATAPOINT WHERE LocationName = ? AND Accepted = ?";

            if (!type.equals("None Selected")) {
                query+= " AND";
                query+= " DataType = '" + type + "'";
            }
            if (fromDataField.getText().length() != 0) {
                query+= " AND";
                query+= " DataValue >= '" + minNum + "'";
                query+= " AND";
                query+= " DataValue <= '" + maxNum + "'";
            }

            System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, loc);
            ps.setString(2, "1");
            ResultSet rs = ps.executeQuery();
            ObservableList<Row> list = FXCollections.observableArrayList();
            Row temp;

            String location;
            String dt;
            String dvalue;
            String dtype;

            while (rs.next()) {
                temp = new Row();;
                location = rs.getString("LocationName");
                dt = rs.getString("DT");
                dvalue = rs.getString("DataValue");
                dtype = rs.getString("DataType");

                temp.setLoc(location);
                temp.setDt(dt);
                temp.setType(dtype);
                temp.setValue(dvalue);
                list.add(temp);
            }


            typeCol.setCellValueFactory(r -> {
                return new SimpleStringProperty(r.getValue().getType());
            });
            valueCol.setCellValueFactory(r -> {
                return new SimpleStringProperty(r.getValue().getvalue());
            });
            dtCol.setCellValueFactory(r -> {
                return new SimpleStringProperty(r.getValue().getDt());
            });
            table.setItems(list);
        }
    }

    @FXML
    private void handleResetFilterPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("../view/POIDetailScreen.fxml"));
        Stage stage = (Stage) resetFilterButton.getScene().getWindow();
        Parent root = loader.load();
        loader.<POIDetailController>getController()
                .setUp(initLocation, initFlag);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleFlagPressed() throws IOException, SQLException {
        String query;
        if (initFlag.equals("No")) {
            query = "UPDATE POI SET Flag = TRUE, DateFlagged = NOW() " +
                    "WHERE LocationName = ?";
            initFlag = "Yes";
        } else {
            query = "UPDATE POI SET Flag = FALSE, DateFlagged = NULL " +
                    "WHERE LocationName = ?";
            initFlag = "No";
        }
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, initLocation);
        preparedStmt.execute();
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("../view/POIDetailScreen.fxml"));
        Stage stage = (Stage) flagButton.getScene().getWindow();
        Parent root = loader.load();
        loader.<POIDetailController>getController()
                .setUp(initLocation, initFlag);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleBackPressed() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/ViewPOIScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
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
}
