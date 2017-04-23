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
import java.util.Date;

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
    private ComboBox flagCombo;

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

    @FXML
    private TableView<Row> table;

    @FXML
    private TableColumn<Row,String> locationCol;

    @FXML
    private TableColumn<Row, String> cityCol;

    @FXML
    private TableColumn<Row, String> stateCol;

    @FXML
    private TableColumn<Row, String> zipCol;

    @FXML
    private TableColumn<Row, String> flagCol;

    @FXML
    private TableColumn<Row, String> dateCol;

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

        flagCombo.getItems().addAll("None Selected","Yes", "No");
        flagCombo.setValue("None Selected");
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
    private void handleApplyFilterPressed() throws SQLException {
        if (isInputValid() == 1) {

            String location = locationBox.getValue();
            String state = stateBox.getValue();
            String city = cityBox.getValue();
            String zip = zipField.getText();
            String flag = flagCombo.getValue().toString();
            Object fromDate = fromDateField.getValue();
            Object toDate = toDateField.getValue();
            /*if (flag.equals("None Selected")) {
                flag = null;
            }*/
            Object sqlFromDate = null;
            Object sqlToDate = null;
            if (fromDate != null) {
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
                java.util.Date fDate = fromCal.getTime();
                sqlFromDate = new java.sql.Timestamp(fDate.getTime());
                Date tDate = toCal.getTime();
                sqlToDate = new java.sql.Timestamp(tDate.getTime());
            }
            String query = "SELECT LocationName, City, State, ZipCode, Flag, " +
                    "DateFlagged FROM POI";
            if (location != null || city != null || state != null || zip
                    .length() != 0 || !flag.equals("None Selected")) {
                query+= " WHERE";
                if (location != null) {
                    query += " LocationName = '" + location + "'";
                }
                if (state != null) {
                    if (location != null) {
                        query+= " AND";
                    }
                    query += " State = '" + state + "'";
                }
                if (city != null) {
                    if (location != null || state != null) {
                        query+= " AND";
                    }
                    query += " City = '" + city + "'";
                }
                if (zip.length() != 0) {
                    if (location != null || state != null || city != null) {
                        query+= " AND";
                    }
                    query+= " ZipCode = '" + zip + "'";
                }
                if (!flag.equals("None Selected")) {
                    if (location != null || state != null || city != null ||
                            zip.length() != 0) {
                        query+= " AND";
                    }
                    if (flag.equals("Yes")) {
                        flag = "1";
                    } else if (flag.equals("No")){
                        flag = "0";
                    }
                    query+= " Flag = '" + flag + "'";

                }
            }
            //System.out.println(query);
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            Row temp;
            ObservableList<Row> list = FXCollections.observableArrayList();
           while(rs.next()) {
               temp = new Row();
               temp.setLocation(rs.getString("LocationName"));
               temp.setState(rs.getString("State"));
               temp.setCity(rs.getString("City"));
               temp.setZip(rs.getString("ZipCode"));
               temp.setFlag(rs.getString("Flag"));
               list.add(temp);
           }


            locationCol.setCellValueFactory(r -> {
                return new SimpleStringProperty(r.getValue().getLocation());
            });
            cityCol.setCellValueFactory(r -> {
                return new SimpleStringProperty(r.getValue().getCity());
            });
            stateCol.setCellValueFactory(r -> {
                return new SimpleStringProperty(r.getValue().getState());
            });
            zipCol.setCellValueFactory(r -> {
                return new SimpleStringProperty(r.getValue().getZip());
            });
            flagCol.setCellValueFactory(r -> {
                return new SimpleStringProperty(r.getValue().getFlag());
            });
            table.setItems(list);

        }

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
        Row selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        String location = selected.getLocation();
        String flag = selected.getFlag();

        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("../view/POIDetailScreen.fxml"));
        Stage stage = (Stage) viewButton.getScene().getWindow();
        Parent root = loader.load();
        loader.<POIDetailController>getController()
                .setUp(location, flag);
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

    public class Row {
        private String location;
        private String state;
        private String city;
        private String zip;
        String flag;
        Object fromDate;
        Object toFate;

        public void setLocation(String location) {
            this.location = location;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public void setFlag(String flag) {
            if (flag.equals("1")) {
                this.flag = "True";
            } else {
                this.flag = "False";
            }
        }

        public String getLocation() {
            return location;
        }

        public String getState() {
            return state;
        }

        public String getCity() {
            return city;
        }

        public String getZip() {
            return zip;
        }

        public String getFlag() {
            return flag;
        }
    }
}
