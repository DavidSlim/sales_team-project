package login;

import DashBoard.DashBoardCtrl;
import DbConn.DbConn;
import alerts.alerts;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class loginCtrl implements Initializable {

    @FXML
    private TextField txtLogName;
    @FXML
    private PasswordField txtLogPass;
    @FXML
    private Button btLogout, btLogin;
    @FXML
    private VBox vbMain;
    alerts al = new alerts();

    ResultSet rs;
    PreparedStatement psmt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btLogin.setOnAction(e -> loginFunc());
        btLogout.setOnAction(e -> logOut());
        txtLogPass.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginFunc();
            }
        });
    }

    private void loginFunc() {

        Connection conn = DbConn.dbconn();

        try {

            String sql = "select * from loginreg where LoginName = ? and LoginPass = ?";
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, txtLogName.getText());
            psmt.setString(2, txtLogPass.getText());
            rs = psmt.executeQuery();

            if (txtLogName.getText().equals("") || txtLogPass.getText().equals("")) {
                String msg = "You Can Not Login Using Null Values";
                al.general(msg);
                conn.close();
            } else if (rs.next()) {

                Node node = vbMain;

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DashBoard/DashBoardFXML.fxml"));

                Parent root = (Parent) fxmlLoader.load();

                ///Pass Login Values To OtherCtrl
                DashBoardCtrl db = fxmlLoader.getController();
                db.setIdCo(rs.getString("IdCode"));
                db.setUserNames(rs.getString("UserName"));
                db.setUserRank(rs.getString("UserRank"));

                ////end of passing login values
                Scene scene = new Scene(root);
                stage.setTitle(rs.getString("UserName") + " : Currently Running The System");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();

                Window stge = node.getScene().getWindow();
                stge.hide();

                ////Call DashBoard Set Image Method
                DashBoardCtrl dboard = fxmlLoader.getController();
                dboard.txtMappListener();

            } else {
                String msg = "Either : " + txtLogName.getText() + " OR  Password Does Not Exist" + "\n" + "\n"
                        + "If You Are Not Registered. See The System Admin For Assistance";
                al.general(msg);
                txtLogPass.setText("");
                conn.close();
            }
        } catch (SQLException | IOException ex) {
            al.conn_err(ex.toString());
        }

    }

    private void logOut() {

        Connection conn = DbConn.dbconn();
        try {
            conn.close();
            String msg = "Succesfully Logged Out";
            al.general(msg);
        } catch (SQLException ex) {
            Logger.getLogger(loginCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
