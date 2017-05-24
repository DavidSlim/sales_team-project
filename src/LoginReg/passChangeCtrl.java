package LoginReg;

import DbConn.DbConn;
import alerts.alerts;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import timeNow.timeNow;

public class passChangeCtrl implements Initializable {

    @FXML
    TextField txtLgName;
    @FXML
    PasswordField txtOldLgPass;
    @FXML
    TextField txtOldPass;
    @FXML
    PasswordField txtLgPass;
    @FXML
    TextField txtPrevPass;
    @FXML
    PasswordField txtLgPassConfirm;

    @FXML
    Label lbId;
    @FXML
    Label lbIdCo;
    @FXML
    Label lbFullNames;
    @FXML
    Label lbMessage;
    @FXML
    Label lbTime;
    @FXML
    Label lbDate;
    @FXML
    Label lbUserRank;
    @FXML
    Label lbUserName;

    @FXML
    ImageView imgv;

    @FXML
    Button btChangePass;

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;
    alerts al = new alerts();
    timeNow tn = new timeNow();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextFieldListeners();
        btChangePass.setOnAction(e -> dataUpdate());
    }

    private void TextFieldListeners() {

        ///Listener to ensure data is mapped to their appropriate fields
        TextField[] tf = {txtLgName, txtLgPass, txtLgPassConfirm};
        for (TextField tf1 : tf) {
            tf1.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (txtOldLgPass.getText().isEmpty()) {
                    String msg = "Ensure Data Is Mapped To Appropriate Fields " + "\n" + "\n" + "By Filling Mapper Field";
                    al.general(msg);
                } else {
                }
            });
        }

        ///listener to make sure that old passwords match
        txtOldLgPass.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (txtOldLgPass.getText().equals(txtOldPass.getText()) && !txtOldLgPass.getText().isEmpty()) {
                lbMessage.setText("Old Passwords Matching");
                lbMessage.setTextFill(Color.web("#0035ff"));
            } else {
                lbMessage.setText("Old Password Mismatch");
                lbMessage.setTextFill(Color.web("#ff0000"));
            }
        });

        ///listener to make sure password field is not null
        txtLgPassConfirm.focusedProperty().addListener((ob, ov, nv) -> {
            nv = txtLgPass.getText().isEmpty() || txtLgName.getText().isEmpty();
            if (nv) {
                txtLgPassConfirm.setEditable(false);
                lbMessage.setText("Username or Password Is Null");
                lbMessage.setTextFill(Color.web("#ff0000"));
            } else {
                txtLgPassConfirm.setEditable(true);
            }
        });
        ///listener to make sure that confirmed password corresponds to password
        txtLgPassConfirm.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (txtLgPassConfirm.getText().equals(txtLgPass.getText()) && !txtLgPass.getText().isEmpty()) {
                lbMessage.setText("Change Credentials Now");
                lbMessage.setTextFill(Color.web("#0035ff"));
                btChangePass.setDisable(false);
            } else {
                btChangePass.setDisable(true);
                lbMessage.setText("Password Mismatch");
                lbMessage.setTextFill(Color.web("#ff0000"));
            }
        });

    }

    //register and check if person is already registered
    private void dataUpdate() {
        boolean status = txtLgName.getText().isEmpty() || txtLgPass.getText().isEmpty()
                || txtLgPassConfirm.getText().isEmpty() || lbIdCo.getText().isEmpty()
                || lbUserName.getText() == null || lbUserRank.getText() == null;
        if (status) {
            al.txtNull();
        } else {
            String sql = "update LoginReg set IdCode = ? , UserName = ? , LoginName = ? , LoginPass = ? , UserRank = ? , DateOfReg = ? where id = " + lbId.getText();
            try {
                psmt = conn.prepareStatement(sql);

                psmt.setString(1, lbIdCo.getText());
                psmt.setString(2, lbUserName.getText());
                psmt.setString(3, txtLgName.getText());
                psmt.setString(4, txtLgPass.getText());
                psmt.setString(5, lbUserRank.getText());
                psmt.setString(6, lbDate.getText());

                psmt.executeUpdate();

            } catch (Exception e) {
            }
            String msg = "" + lbFullNames.getText() + "" + "\n" + "\n" + "Login Credentials Changed";
            al.general(msg);
        }
    }

    ////Set Details From Login Reg Class/form
    public void setId(String IdString) {
        lbId.setText(IdString);
    }

    public void setIdCo(String IdCoString) {
        lbIdCo.setText(IdCoString);
    }

    public void setFullNames(String FullNamesString) {
        lbFullNames.setText(FullNamesString);
    }

    public void setUserName(String UserNameString) {
        lbUserName.setText(UserNameString);
    }

    public void setUserRank(String UserRankString) {
        lbUserRank.setText(UserRankString);
    }

    public void setLoginName(String LoginName) {
        txtLgName.setText(LoginName);
    }

    public void setLoginPass(String LoginPass) {
        txtOldPass.setText(LoginPass);
    }

    public void setDate(String DateString) {
        lbDate.setText(DateString);
    }

    public void setImage(Object ImageObject) throws IOException {

        byte[] bt = (byte[]) ImageObject;
        ByteArrayInputStream in = new ByteArrayInputStream(bt);
        BufferedImage read = ImageIO.read(in);

        imgv.setImage(SwingFXUtils.toFXImage(read, null));

    }

}
