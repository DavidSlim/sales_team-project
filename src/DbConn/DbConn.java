package DbConn;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.*;
import javafx.scene.control.Alert;

public class DbConn {

    public static Connection dbconn() {

        Connection conn = null;

        try {

            MysqlDataSource mds = new MysqlDataSource();
            mds.setServerName("localhost");
            mds.setPortNumber(3306);
            mds.setDatabaseName("sales_system");
            mds.setUser("root");
            mds.setPassword("");

            conn = mds.getConnection();

            return conn;

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Failed");
            alert.setHeaderText("Sorry you seem to be having a network connection error");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
        return null;

    }

    public static void DbClose(Connection conn, PreparedStatement psmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
            }
        }
        if (psmt != null) {
            try {
                psmt.close();
            } catch (Exception ex) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ex) {
            }
        }

    }
}
