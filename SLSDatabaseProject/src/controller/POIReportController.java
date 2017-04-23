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
        String query = "SELECT LocationName, City, State, Flag FROM " +
                "POI ORDER BY LocationName";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();

        String query2;
        Integer aqMin;
        Integer aqAvg;
        Integer aqMax;
        Integer aqSum;
        Integer aqNum;
        Integer moldMin;
        Integer moldAvg;
        Integer moldMax;
        Integer moldSum;
        Integer moldNum;
        Integer points;

        ObservableList<Row> list = FXCollections.observableArrayList();
        while (rs.next()) {
            aqMin = Integer.MAX_VALUE;
            aqAvg = 0;
            aqMax = Integer.MIN_VALUE;
            aqSum = 0;
            aqNum = 0;
            moldMin = Integer.MAX_VALUE;
            moldAvg = 0;
            moldMax = Integer.MIN_VALUE;
            moldSum = 0;
            moldNum = 0;
            points = 0;

            String location = rs.getString("LocationName");
            String city = rs.getString("City");
            String state = rs.getString("State");
            String flag = rs.getString("Flag");

            query2 = "SELECT DataValue, DataType FROM DATAPOINT WHERE " +
                    "LocationName = ? AND Accepted = TRUE";
            PreparedStatement preparedstmt2 = conn.prepareStatement(query2);
            preparedstmt2.setString(1, location);
            ResultSet rs2 = preparedstmt2.executeQuery();
            while(rs2.next()) {
                String type = rs2.getString("DataType");
                Integer value = rs2.getInt("DataValue");
                if (type.equals("Mold")) {
                    if (value < moldMin) {
                        moldMin = value;
                    }
                    if (value > moldMax) {
                        moldMax = value;
                    }
                    moldNum++;
                    moldSum+= value;
                } else {
                    if (value < aqMin) {
                        aqMin = value;
                    }
                    if (value > aqMax) {
                        aqMax = value;
                    }
                    aqNum++;
                    aqSum+= value;
                }
            }
            if (moldNum == 0) {
                moldMin = 0;
                moldAvg = 0;
                moldMax = 0;
            } else {
                moldAvg = moldSum / moldNum;
            }
            if (aqNum == 0) {
                aqMin = 0;
                aqAvg = 0;
                aqMax = 0;
            } else {
                aqAvg = aqSum / aqNum;
            }
            points = moldNum + aqNum;

            Row temp = new Row();
            temp.setLoc(location);
            temp.setCty(city);
            temp.setSt(state);
            temp.setFlg(flag);
            temp.setMoldMin(moldMin);
            temp.setMoldAvg(moldAvg);
            temp.setMoldMax(moldMax);
            temp.setAqMin(aqMin);
            temp.setAqAvg(aqAvg);
            temp.setAqMax(aqMax);
            temp.setPoints(points);
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
            return new SimpleStringProperty(r.getValue().getFlg());
        });
        moldMinCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getMoldMin());
        });
        moldAvgCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getMoldAvg());
        });
        moldMaxCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getMoldMax());
        });
        aqMinCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getAqMin());
        });
        aqAvgCol.setCellValueFactory(r -> {
            return new SimpleObjectProperty<Integer>(r.getValue().getAqAvg());
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
        private String flg;
        private Integer moldMin;
        private Integer moldAvg;
        private Integer moldMax;
        private Integer aqMin;
        private Integer aqAvg;
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
        public void setFlg(String flg) {
            this.flg = flg;
        }
        public void setMoldMin(Integer moldMin) {
            this.moldMin = moldMin;
        }
        public void setMoldAvg(Integer moldAvg) {
            this.moldAvg = moldAvg;
        }
        public void setMoldMax(Integer moldMax) {
            this.moldMax = moldMax;
        }
        public void setAqMin(Integer aqMin) {
            this.aqMin = aqMin;
        }
        public void setAqAvg(Integer aqAvg) {
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
        public String getFlg() {
            return flg;
        }

        public Integer getMoldMin() {
            return moldMin;
        }

        public Integer getMoldAvg() {
            return moldAvg;
        }

        public Integer getMoldMax() {
            return moldMax;
        }

        public Integer getAqMin() {
            return aqMin;
        }

        public Integer getAqAvg() {
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
