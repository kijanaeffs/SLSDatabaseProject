package fxapp;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MainFXApplication extends Application {

    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        showLoginScreen(mainStage);
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void showLoginScreen(Stage mainStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/LoginScreen.fxml"));
        Parent rootLayout = loader.load();


        mainStage.setTitle("SLS Database App");
        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        mainStage.setScene(scene);
        mainStage.show();
    }
    public static void main(String[] args) throws SQLException {
        Connection conn = getConnection();

        /*
        // mysql insert statement
        Statement st = conn.createStatement();
        st.executeUpdate("INSERT INTO USERS (EmailAddress, Username, Pass, UserType) "
                +"VALUES ('mjohnson350@gatech.edu', 'mjohnson350', " +
                "'passwurd', " +
                "'Admin')");
        */
        /*
        // mysql delete statement
        String query = "DELETE FROM USERS WHERE Username = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, "Testing");

        // execute the preparedstatement
        preparedStmt.execute();
        */

        launch(args);
        conn.close();
        System.out.print("Successfully closed connection to MySQL server.");
    }

    private static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_59",
                    "cs4400_59",
                    "aw3qI2fz");
            if(!conn.isClosed())
                System.out.println("Successfully connected to " +
                        "MySQL server using TCP/IP...");
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return conn;
    }
}
