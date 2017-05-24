package LoginReg;

import Combos.Combos;
import DbConn.DbConn;
import alerts.alerts;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import targets.TargetsController;
import timeNow.timeNow;

public class loginRegCtrl implements Initializable {

    private String name = null;

    @FXML
    TextField txtIdCo, txtSearcher, txtLgName;
    @FXML
    PasswordField txtLgPass, txtLgPassConfirm;
    @FXML
    Label lbId, lbIdCo, lbFullNames, lbMessage, lbTime, lbDate;
    @FXML
    DatePicker dpS, dpE;
    @FXML
    ImageView imgv;
    @FXML
    ComboBox<String> cbUserRank, cbUserName;
    @FXML
    Button btReg, btUnReg, btMap, btPop, btPassChange;
    @FXML
    AnchorPane apMain;

    @FXML
    TableView<lg_getset> table;

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;
    alerts al = new alerts();
    Combos cbs = new Combos();
    CombosPop cbp = new CombosPop();
    timeNow tn = new timeNow();
    reg reg = new reg();
    TableEvnts tbev = new TableEvnts();
    UnReg unreg = new UnReg();

    private ObservableList<lg_getset> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lbDate.setText(tn.date());
        cbp.ComboBs();
        tn.timeCurr(lbTime);
        TextFieldListeners();
        btMap.setOnAction(e -> {
            if (!txtIdCo.getText().isEmpty()) {
                cbp.txtMappListener();
            } else {
                al.txtNull();
            }
        });

        btReg.setOnAction(e -> {
            reg.dataSave();
            reg.chkRcdExist();
        });
        btPop.setOnAction(e -> tbev.pop());
        btUnReg.setOnAction(e -> unreg.unreg());
        tbev.tableClick();
        btPassChange.setOnAction(e -> scene2scene());

    }

    private class CombosPop {

        private void ComboBs() {
            cbUserRank.setItems(FXCollections.observableArrayList(cbs.userRanks()));
        }

        ////Assigned to populator
        private void UserName() {
            try {
                String tmpMembers = cbUserRank.getSelectionModel().getSelectedItem();

                if ("Sales Manager".equals(tmpMembers)) {
                    cbUserName.setItems(FXCollections.observableArrayList(cbs.salesmanager()));
                } else if ("Sales Reps".equals(tmpMembers)) {
                    cbUserName.setItems(FXCollections.observableArrayList(cbs.salesrep()));
                } else if ("Merchandizers".equals(tmpMembers)) {
                    cbUserName.setItems(FXCollections.observableArrayList(cbs.merchandizers()));
                } else if ("Board Of Govs".equals(tmpMembers)) {
                    cbUserName.setItems(FXCollections.observableArrayList(cbs.bogovs()));
                } else {
                }
            } catch (Exception e) {
            }
        }

        private String cbSet(String sql) {
            String tmp = cbUserName.getValue();
            try {
                psmt = conn.prepareStatement(sql);
                psmt.setString(1, tmp);
                rs = psmt.executeQuery();
                if (rs.next()) {

                    byte[] bt = (byte[]) rs.getBytes("image");
                    ByteArrayInputStream in = new ByteArrayInputStream(bt);
                    BufferedImage read = ImageIO.read(in);

                    lbIdCo.setText(rs.getString("autocode"));
                    lbFullNames.setText(cbUserName.getValue() + " " + rs.getString("sirname"));
                    imgv.setImage(SwingFXUtils.toFXImage(read, null));

                }
            } catch (SQLException | IOException ex) {
                Logger.getLogger(TargetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return sql;
        }

        private String IdCoSet(String sql) {
            String tmp = txtIdCo.getText();
            try {
                psmt = conn.prepareStatement(sql);
                psmt.setString(1, tmp);
                rs = psmt.executeQuery();
                if (rs.next()) {

                    Image img = new Image(rs.getString("image"));

                    lbIdCo.setText(rs.getString("autocode"));
                    lbFullNames.setText(rs.getString("othernames") + " " + rs.getString("sirname"));
                    imgv.setImage(img);
                    cbUserName.setValue(rs.getString("othernames"));

                } else {
                    String msg = "This " + txtIdCo.getText() + " Does Not Exist." + "\n" + "\n" + "Please See The System Admin For Assistance";
                    al.general(msg);
                }
            } catch (SQLException ex) {
                Logger.getLogger(TargetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return sql;
        }

        private void txtMappListener() {
            String tmpMembers = cbUserRank.getSelectionModel().getSelectedItem();
            if ("Sales Manager".equals(tmpMembers)) {
                String sql = "select * from salesmanager where autocode=?";
                IdCoSet(sql);
            } else if ("Sales Reps".equals(tmpMembers)) {
                String sql = "select * from salesrep where autocode=?";
                IdCoSet(sql);
            } else if ("Merchandizers".equals(tmpMembers)) {
                String sql = "select * from merchandizers where autocode=?";
                IdCoSet(sql);
            } else if ("Board Of Govs".equals(tmpMembers)) {
                String sql = "select * from boardofgovernors where autocode=?";
                IdCoSet(sql);
            } else {
            }
        }

    }

    private void TextFieldListeners() {

        ///listener to ensure user level is selected first
        txtIdCo.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (cbUserRank.getSelectionModel().isEmpty()) {
                txtIdCo.setText("");
                String msg = "Select Your User Level To Proceed";
                al.general(msg);
            } else {
            }
        });

        ///Listener to ensure data is mapped to their appropriate fields
        TextField[] tf = {txtLgName, txtLgPass, txtLgPassConfirm};
        for (TextField tf1 : tf) {
            tf1.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (txtIdCo.getText().isEmpty()) {
                    String msg = "Ensure Data Is Mapped To Appropriate Fields " + "\n" + "\n" + "By Filling Mapper Field";
                    al.general(msg);
                    btReg.setDisable(true);
                } else {
                    btReg.setDisable(false);
                }
            });
        }

        ///listener to make sure password field is not null
        txtLgPassConfirm.focusedProperty().addListener((ob, ov, nv) -> {
            nv = txtLgPass.getText().isEmpty() || txtLgName.getText().isEmpty();
            if (nv) {
                txtLgPassConfirm.setEditable(false);
                lbMessage.setText("Username or Password Is Null");
                lbMessage.setTextFill(Color.DEEPPINK);
            } else {
                txtLgPassConfirm.setEditable(true);
            }
        });
        ///listener to make sure that confirmed password corresponds to password
        txtLgPassConfirm.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (txtLgPassConfirm.getText().equals(txtLgPass.getText()) && !txtLgPass.getText().isEmpty()) {
                lbMessage.setText("Register Now");
                lbMessage.setTextFill(Color.BLUE);
                btReg.setDisable(false);
            } else {
                lbMessage.setText("Password Mismatch");
                lbMessage.setTextFill(Color.RED);
                btReg.setDisable(true);
            }
        });

    }

    //register and check if person is already registered
    private class reg {

        private void dataSave() {
            boolean status = txtLgName.getText().isEmpty() || txtLgPass.getText().isEmpty()
                    || txtLgPassConfirm.getText().isEmpty() || lbIdCo.getText().isEmpty()
                    || cbUserName.getValue() == null || cbUserRank.getValue() == null;
            if (status) {
                al.txtNull();
            } else {
                String sql = "insert into LoginReg (IdCode, UserName, LoginName, LoginPass, UserRank, DateOfReg) values(?,?,?,?,?,?)";
                try {
                    psmt = conn.prepareStatement(sql);

                    psmt.setString(1, lbIdCo.getText());
                    psmt.setString(2, cbUserName.getValue());
                    psmt.setString(3, txtLgName.getText());
                    psmt.setString(4, txtLgPass.getText());
                    psmt.setString(5, cbUserRank.getValue());
                    psmt.setString(6, tn.date());

                    psmt.executeUpdate();
                    //al.inserted();
                } catch (Exception e) {
                }
            }
        }

        //Chks whether record exists in 
        private void chkRcdExist() {            
            try {
                String sql = "select * from LoginReg where IdCode = ?";
                psmt = conn.prepareStatement(sql);
                psmt.setString(1, lbIdCo.getText());
                rs = psmt.executeQuery();
                if (rs.next()) {
                    String msg = "Hey.. - " + lbFullNames.getText() + "" + "\n" + "\n" + "You Are Registered. " + "\n" + "\n" + "Please Contact The Admin For Any Assistance";
                    al.general(msg);
                }
            } catch (Exception e) {
            }
        }
    }

    private class UnReg {

        //Delete Tableview row
        private void delTable() {

            ObservableList<lg_getset> productSelected, allProducts;
            allProducts = table.getItems();
            productSelected = table.getSelectionModel().getSelectedItems();

            productSelected.forEach(allProducts::remove);

        }

        /////Delete Data
        private void unreg() {

            if (table.getItems().isEmpty() || table.getSelectionModel().getSelectedItems().isEmpty()) {
            } else {

                String name = lbFullNames.getText();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete : " + name + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.setHeaderText("Are You Sure You Want To Delete This Record ?");
                alert.showAndWait();

                alert.setResizable(false);

                if (alert.getResult() == ButtonType.YES) {

                    String sql = "delete from LoginReg where Id = ?";
                    try {
                        psmt = conn.prepareStatement(sql);
                        psmt.setString(1, lbId.getText());

                        psmt.executeUpdate();
                        al.deleted();
                        delTable();
                    } catch (Exception e) {
                    }
                } else {
                    al.deletedNot();
                }
            }

        }
    }

    private class TableEvnts {

        ///table columns
        private void tableCols() {
            TableColumn<lg_getset, String> IdCol = new TableColumn<>("ID");
            IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            IdCol.setMaxWidth(200);
            TableColumn<lg_getset, String> IdCodeCol = new TableColumn<>("Co. Id");
            IdCodeCol.setCellValueFactory(new PropertyValueFactory<>("idCode"));
            //IdCodeCol.setMaxWidth(200);
            TableColumn<lg_getset, String> UserNameCol = new TableColumn<>("User Name");
            UserNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
            //UserNameCol.setMaxWidth(200);
            TableColumn<lg_getset, String> LoginNameCol = new TableColumn<>("Login Name");
            LoginNameCol.setCellValueFactory(new PropertyValueFactory<>("loginname"));
            LoginNameCol.setMaxWidth(200);
            TableColumn<lg_getset, String> LoginPassCol = new TableColumn<>("LoginPass");
            LoginPassCol.setCellValueFactory(new PropertyValueFactory<>("loginpass"));
            LoginPassCol.setMaxWidth(200);
            TableColumn<lg_getset, String> UserRankCol = new TableColumn<>("User Rank");
            UserRankCol.setCellValueFactory(new PropertyValueFactory<>("userrank"));
            UserRankCol.setMaxWidth(200);
            TableColumn<lg_getset, String> DateOfRegCol = new TableColumn<>("Date Of Reg");
            DateOfRegCol.setCellValueFactory(new PropertyValueFactory<>("dor"));
            DateOfRegCol.setMaxWidth(200);

            table.getColumns().addAll(IdCodeCol, UserNameCol);
        }

        ////Populator To Fetch Database dat
        private String populator(String sql) {

            try {
                data = FXCollections.observableArrayList();
                data.removeAll(data);
                rs = conn.createStatement().executeQuery(sql);
                while (rs.next()) {
                    lg_getset lgs = new lg_getset();

                    lgs.id.set(rs.getString("id"));
                    lgs.idCode.set(rs.getString("idCode"));
                    lgs.username.set(rs.getString("username"));
                    lgs.loginname.set(rs.getString("loginname"));
                    lgs.loginpass.set(rs.getString("loginpass"));
                    lgs.userrank.set(rs.getString("userrank"));
                    lgs.dor.set(rs.getString("dateofreg"));

                    data.add(lgs);
                }
                table.getColumns().clear();
                tableCols();
                table.setItems(data);
            } catch (Exception e) {
            }
            return sql;
        }

        ///pop data
        private void pop() {
            try {
                String sql = "select * from loginreg";
                populator(sql);
            } catch (Exception e) {
            }
        }

        //click & press
        private void clkAndPress() {
            try {
                lg_getset row = table.getSelectionModel().getSelectedItem();
                lbId.setText(row.getId());
                txtIdCo.setText(row.getIdCode());
                txtLgName.setText(row.getLoginname());
                txtLgPass.setText(row.getLoginpass());
                txtLgPassConfirm.setText(txtLgPass.getText());
                cbUserName.setValue(row.getUsername());
                cbUserRank.setValue(row.getUserrank());
                lbDate.setText(row.getDor());
            } catch (Exception e) {
            }
        }

        ////Table click and press listener
        public void tableClick() {

            try {
                table.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
                    if ((event.getCode().equals(KeyCode.DOWN)) || (event.getCode().equals(KeyCode.UP))) {
                        clkAndPress();
                        cbp.txtMappListener();
                    } else {
                    }
                });
                table.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                    if ((event.getClickCount() == 1 || event.getClickCount() == 2) && !table.getColumns().isEmpty()) {
                        clkAndPress();
                        cbp.txtMappListener();
                    } else {
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    private void scene2scene() {

        Node node = apMain;

        boolean stat = lbDate.getText() == null || cbUserRank.getValue() == null || lbIdCo.getText() == null;
        if (stat) {
            String msg = "Map All Fields First";
            al.general(msg);
        } else {

            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("passChangeFXML.fxml"));

                Parent root = (Parent) fxmlLoader.load();

                ///Pass Login Values To OtherCtrl
                BufferedImage buff = SwingFXUtils.fromFXImage(imgv.getImage(), null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(buff, "jpg", baos);
                byte[] b = baos.toByteArray();

                passChangeCtrl pcc = fxmlLoader.getController();
                pcc.setId(lbId.getText());
                pcc.setIdCo(lbIdCo.getText());
                pcc.setFullNames(lbFullNames.getText());
                pcc.setUserName(cbUserName.getValue());
                pcc.setUserRank(cbUserRank.getValue());
                pcc.setLoginName(txtLgName.getText());
                pcc.setLoginPass(txtLgPass.getText());
                pcc.setDate(lbDate.getText());
                pcc.setImage(b);
                //end of passing login values

                Scene scene = new Scene(root);
                stage.setTitle("Change " + lbFullNames.getText() + "Login Details");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();

                Window stge = node.getScene().getWindow();
                stge.hide();

            } catch (IOException ex) {

            }
        }
    }

    public void setdBoardName(String FullNamesString) {
        this.name = FullNamesString;
    }

}
