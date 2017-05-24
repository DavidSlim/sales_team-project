package LogOut;

import DbConn.DbConn;
import alerts.alerts;
import java.io.IOException;
import java.sql.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LogOut {

    alerts al = new alerts();

    public Node LogOut(Node node) {
        Connection conn = DbConn.dbconn();
        try {
            conn.close();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login/loginFXML.fxml"));

            Parent root = (Parent) fxmlLoader.load();

            Scene scene = new Scene(root);
            stage.setTitle("Provide Login Credentials");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            Window stge = node.getScene().getWindow();
            stge.hide();

        } catch (SQLException | IOException ex) {
            al.conn_err(ex.toString());
        }
        return node;
    }

}
