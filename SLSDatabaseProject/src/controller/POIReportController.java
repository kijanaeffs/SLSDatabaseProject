package controller;

import fxapp.MainFXApplication;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInput;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kijana on 4/22/2017.
 */
public class POIReportController {
    @FXML
    private Button backButton;

    @FXML
    private TableView<Row> table;

    @FXML
    private TableColumn<Row, String> locCol;

    @FXML
    private TableColumn<Row, String> cityCol;

    @FXML
    private TableColumn<Row, String> stateCol;

    @FXML
    private TableColumn<Row, Integer> moldMinCol;

    @FXML
    private TableColumn<Row, Integer> moldAvgCol;

    @FXML
    private TableColumn<Row, Integer> moldMaxCol;

    @FXML
    private TableColumn<Row, Integer> aqMinCol;

    @FXML
    private TableColumn<Row, Integer> aqAvgCol;

    @FXML
    private TableColumn<Row, Integer> aqMaxCol;

    @FXML
    private TableColumn<Row, Integer> pointsCol;

    @FXML
    private TableColumn<Row, String> flagCol;



    private Connection conn = MainFXApplication.getConnection();
    @FXML
    private void initialize() throws SQLException {
        String query = "SELECT * FROM POIREPORT ORDER BY LocationName";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();

        Row temp;
        ObservableList<Row> list = FXCollections.observableArrayList();
        while (rs.next()) {
            temp = new Row();
            temp.setLoc(rs.getString("LocationName"));
            temp.setCty(rs.getString("City"));
            temp.setSt(rs.getString("State"));
            temp.setMoldMin(rs.getInt("MoldMin"));
            temp.setMoldAvg(rs.getDouble("MoldAvg"));
            temp.setMoldMax(rs.getInt("MoldMax"));
            temp.setAqMin(rs.getInt("MinAQ"));
            temp.setAqAvg(rs.getDouble("AvgAQ"));
            temp.setAqMax(rs.getInt("MaxAQ"));
            temp.setPoints(rs.getInt("NumPoints"));
            temp.setFlg(rs.getInt("Flag"));
            list.add(temp);
        }


        locCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().getLoc());
        });
        cityCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().getCty());
        });
        stateCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(r.getValue().getSt());
        });
        flagCol.setCellValueFactory(r -> {
            return new SimpleStringProperty(Integer.toString(r.getValue().getFlg
                    ()));
        });
        moldMinCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getMoldMin());
        });
        moldAvgCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty(r.getValue().getMoldAvg());
        });
        moldMaxCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getMoldMax());
        });
        aqMinCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getAqMin());
        });
        aqAvgCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty(r.getValue().getAqAvg());
        });
        aqMaxCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getAqMax());
        });
        pointsCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getPoints());
        });


        table.setItems(list);
    }

    public class Row {
        private String loc;
        private String st;
        private String cty;
        private Integer flg;
        private Integer moldMin;
        private Double moldAvg;
        private Integer moldMax;
        private Integer aqMin;
        private Double aqAvg;
        private Integer aqMax;
        private Integer points;

        public void setLoc(String loc) {
            this.loc = loc;
        }
        public void setSt(String st) {
            this.st = st;
        }
        public void setCty(String cty) {
            this.cty = cty;
        }
        public void setFlg(Integer flg) {
            this.flg = flg;
        }
        public void setMoldMin(Integer moldMin) {
            this.moldMin = moldMin;
        }
        public void setMoldAvg(Double moldAvg) {
            this.moldAvg = moldAvg;
        }
        public void setMoldMax(Integer moldMax) {
            this.moldMax = moldMax;
        }
        public void setAqMin(Integer aqMin) {
            this.aqMin = aqMin;
        }
        public void setAqAvg(Double aqAvg) {
            this.aqAvg = aqAvg;
        }
        public void setAqMax(Integer aqMax) {
            this.aqMax = aqMax;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

        public String getLoc() {
            return loc;
        }
        public String getSt() {
            return st;
        }
        public String getCty() {
            return cty;
        }
        public Integer getFlg() {
            return flg;
        }

        public Integer getMoldMin() {
            return moldMin;
        }

        public Double getMoldAvg() {
            return moldAvg;
        }

        public Integer getMoldMax() {
            return moldMax;
        }

        public Integer getAqMin() {
            return aqMin;
        }

        public Double getAqAvg() {
            return aqAvg;
        }

        public Integer getAqMax() {
            return aqMax;
        }

        public Integer getPoints() {
            return points;
        }
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
