package targets;

import Combos.Combos;
import DbConn.DbConn;
import alerts.alerts;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.ResourceBundle;
import java.util.logging.*;
import javafx.collections.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javax.imageio.ImageIO;
import timeNow.timeNow;
import validators.validators;
import targets.getset.*;

public class TargetsController implements Initializable {

    private String name = null;

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;
    validators val = new validators();
    alerts al = new alerts();
    timeNow tn = new timeNow();
    Combos cbs = new Combos();

    @FXML
    Label lblName, lblAchieved, lblTime, lblId, lblPrg, lblPerDisp, lblDate;
    @FXML
    TextField txtWeek1, txtWeek2, txtWeek3, txtWeek4, txtSetTar, txtSearcher;
    @FXML
    ProgressBar prgTarget;
    @FXML
    ComboBox<String> cbUserLevel, cbUserName, cbAssignedTo;
    @FXML
    Button btSave, btDelete, btUpdate, btPop, btClr, btNxt, btPrv, btFst, btLst;
    @FXML
    DatePicker dpStartDate, dpEndDate;
    @FXML
    ImageView imgV;
    @FXML
    TableView<tg_getset> table;
    @FXML
    HBox print;

    private final List<tg_getset> data = new ArrayList<>();

    String date = (LocalDate.now()).toString();

    Image img = new Image("/img/log.jpg");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nav_butts nav_b = new nav_butts();
        clean();
        ComboBs();
        cbListener cbl = new cbListener();
        cbUserLevel.setOnAction(e -> assTo());
        cbUserName.setOnAction(e -> cbl.cbListener());
        btClr.setOnAction(e -> clear());
        btSave.setOnAction(e -> dataSave());
        btPop.setOnAction(e -> pop());
        btDelete.setOnAction(e -> dataDel());
        btUpdate.setOnAction(e -> dataUpdate());
        nav_b.nav_clicks();
        textVal();
        totalCalcs();
        tfEmpty();
        tableClick();
        searchData();
        tn.timeCurr(lblTime);
    }

    private void tableCols() {
        TableColumn<tg_getset, Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        //IdCol.setMaxWidth(200);
        TableColumn<tg_getset, String> NameCol = new TableColumn<>("User Name");
        NameCol.setCellValueFactory(new PropertyValueFactory<>("usernames"));
        NameCol.setMinWidth(140);
        TableColumn<tg_getset, Double> Week1Col = new TableColumn<>("Week1");
        Week1Col.setCellValueFactory(new PropertyValueFactory<>("week1"));
        Week1Col.setMaxWidth(200);
        TableColumn<tg_getset, Double> Week2Col = new TableColumn<>("Week2");
        Week2Col.setCellValueFactory(new PropertyValueFactory<>("week2"));
        Week2Col.setMaxWidth(200);
        TableColumn<tg_getset, Double> Week3Col = new TableColumn<>("Week3");
        Week3Col.setCellValueFactory(new PropertyValueFactory<>("week3"));
        Week3Col.setMaxWidth(200);
        TableColumn<tg_getset, Double> Week4Col = new TableColumn<>("Week4");
        Week4Col.setCellValueFactory(new PropertyValueFactory<>("week4"));
        Week4Col.setMaxWidth(200);
        TableColumn<tg_getset, Double> SetTCol = new TableColumn<>("Set Tgt");
        SetTCol.setCellValueFactory(new PropertyValueFactory<>("settarget"));
        SetTCol.setMaxWidth(40);
        TableColumn<tg_getset, Double> AchTCol = new TableColumn<>("Achieved");
        AchTCol.setCellValueFactory(new PropertyValueFactory<>("achtarget"));
        AchTCol.setMinWidth(40);
        TableColumn<tg_getset, Double> PerCol = new TableColumn<>(" % ");
        PerCol.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        PerCol.setMinWidth(40);
        TableColumn<tg_getset, String> DORCol = new TableColumn<>("DateOfReg");
        DORCol.setCellValueFactory(new PropertyValueFactory<>("dateofreg"));
        DORCol.setMaxWidth(200);
        table.getColumns().addAll(NameCol, AchTCol, PerCol);
    }

    public void ComboBs() {
        cbUserLevel.setItems(FXCollections.observableArrayList(cbs.userRanks()));
    }

    private void textVal() {

        TextField names[] = {txtWeek1, txtWeek2, txtWeek3, txtWeek4, txtSetTar};
        for (int i = 0; i < names.length; i++) {
            names[i].setOnKeyTyped(e -> val.numDot(e));
        }

    }

    private void clean() {

        Tooltip asstooltip = new Tooltip("Assigned To");
        Tooltip usernametooltip = new Tooltip("Select UserName");

        cbAssignedTo.valueProperty().set(null);
        cbUserName.valueProperty().set(null);

        cbAssignedTo.setTooltip(asstooltip);
        cbUserName.setTooltip(usernametooltip);

        TextField tf[] = {txtSetTar, txtWeek1, txtWeek2, txtWeek3, txtWeek4};
        for (TextField tfCl : tf) {
            tfCl.setText("0");
        }

        lblAchieved.setText("");
        lblPerDisp.setText("");
        lblId.setText("");
        lblDate.setText("");

        prgTarget.setProgress(0);

        imgV.setImage(img);

        table.getColumns().clear();
        data.removeAll(data);

    }

    private void clear() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Clear All Fields?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setHeaderText("Are You Sure? This Will Clear All Fields");
        alert.showAndWait();

        alert.setResizable(false);

        if (alert.getResult() == ButtonType.YES) {
            txtSearcher.setText("");
            dpStartDate.getEditor().setText("");
            dpEndDate.getEditor().setText("");
            clean();
            al.cleared();
        } else {
            al.clearNot();
        }
    }

////Assigned to populator
    public void assTo() {
        try {
            String tmpMembers = cbUserLevel.getSelectionModel().getSelectedItem();

            if ("Sales Manager".equals(tmpMembers)) {
                cbUserName.setItems(null);
                cbUserName.setItems(FXCollections.observableArrayList(cbs.salesmanager()));
            } else if ("Sales Reps".equals(tmpMembers)) {
                cbUserName.setItems(null);
                cbUserName.setItems(FXCollections.observableArrayList(cbs.salesrep()));
            } else if ("Merchandizers".equals(tmpMembers)) {
                cbUserName.setItems(null);
                cbUserName.setItems(FXCollections.observableArrayList(cbs.merchandizers()));
            } else if ("Board Of Govs".equals(tmpMembers)) {
                cbUserName.setItems(null);
                cbUserName.setItems(FXCollections.observableArrayList(cbs.bogovs()));
            } else {

            }
        } catch (Exception e) {
        }
    }

    private class cbListener {

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

                    cbAssignedTo.setValue(rs.getString("assignedTo"));
                    lblName.setText(cbUserName.getValue());

                    imgV.setImage(SwingFXUtils.toFXImage(read, null));

                }
            } catch (SQLException | IOException ex) {
                Logger.getLogger(TargetsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return sql;
        }

        private void cbListener() {
            String tmpMembers = cbUserLevel.getSelectionModel().getSelectedItem();
            if ("Sales Manager".equals(tmpMembers)) {
                String sql = "select * from salesmanager where othernames=?";
                cbSet(sql);
            } else if ("Sales Reps".equals(tmpMembers)) {
                String sql = "select * from salesrep where othernames=?";
            } else if ("Merchandizers".equals(tmpMembers)) {
                String sql = "select * from merchandizers where othernames=?";
                cbSet(sql);
            } else if ("Board Of Govs".equals(tmpMembers)) {
                String sql = "select * from boardofgovernors where othernames=?";
                cbSet(sql);
            } else {
            }
        }

    }

    private void totalCalcs() {

        try {
            TextField ft[] = {txtWeek1, txtWeek2, txtWeek3, txtWeek4};

            for (TextField tf1 : ft) {

                if (txtSetTar.getText().isEmpty()) {
                    txtSetTar.setText("0");
                } else {
                }
                tf1.textProperty().addListener((ob, ov, nv) -> {

                    if (!tf1.getText().isEmpty()) {

                        double week1 = Double.parseDouble(txtWeek1.getText());
                        double week2 = Double.parseDouble(txtWeek2.getText());
                        double week3 = Double.parseDouble(txtWeek3.getText());
                        double week4 = Double.parseDouble(txtWeek4.getText());

                        double total = week1 + week2 + week3 + week4;

                        nv = String.valueOf(Math.round(total));

                        lblAchieved.setText(nv);

                        double prg = Float.parseFloat(nv) * 100 / Double.parseDouble(txtSetTar.getText());
                        int per = (int) Math.round(prg);

                        lblPrg.setText(String.valueOf(per));

                        if (lblPrg.getText().isEmpty()) {
                            lblPerDisp.setText("");
                        } else {
                            lblPerDisp.setText("%");
                        }

                    } else {

                    }

                });
            }

            txtSetTar.textProperty().addListener((ob, ov, nv) -> {

                if (!txtSetTar.getText().isEmpty()) {

                    double week1 = Double.parseDouble(txtWeek1.getText());
                    double week2 = Double.parseDouble(txtWeek2.getText());
                    double week3 = Double.parseDouble(txtWeek3.getText());
                    double week4 = Double.parseDouble(txtWeek4.getText());

                    double total = week1 + week2 + week3 + week4;

                    nv = String.valueOf(Math.round(total));

                    lblAchieved.setText(nv);

                    double prg = Float.parseFloat(nv) * 100 / Double.parseDouble(txtSetTar.getText());
                    int per = (int) Math.round(prg);

                    lblPrg.setText(String.valueOf(per));

                    if (lblPrg.getText().isEmpty()) {
                        lblPerDisp.setText("");
                    } else {
                        lblPerDisp.setText("%");
                    }

                }

            });

            lblPrg.textProperty().addListener((ob, oldValue, newValue) -> {
                newValue = lblPrg.getText();
                prgTarget.setProgress(Double.parseDouble(newValue) / 100);
            });
        } catch (Exception e) {
        }
    }

    private void tfEmpty() {

        txtSetTar.focusedProperty().addListener((ob, ov, nv) -> {
            TextField ft[] = {txtWeek1, txtWeek2, txtWeek3, txtWeek4};
            nv = txtSetTar.getText().isEmpty();
            for (TextField tf1 : ft) {
                if (nv) {
                    tf1.setDisable(true);
                } else {
                    tf1.setDisable(false);
                }
            }
        });
        txtWeek1.focusedProperty().addListener((ob, ov, nv) -> {
            TextField ftWk[] = {txtWeek2, txtWeek3, txtWeek4, txtSetTar};
            nv = txtWeek1.getText().isEmpty();
            for (TextField tf1 : ftWk) {
                if (nv) {
                    tf1.setEditable(false);
                } else {
                    tf1.setEditable(true);
                }
            }
        });
        txtWeek2.focusedProperty().addListener((ob, ov, nv) -> {
            TextField ftWk[] = {txtWeek1, txtWeek3, txtWeek4, txtSetTar};
            nv = txtWeek2.getText().isEmpty();
            for (TextField tf1 : ftWk) {
                if (nv) {
                    tf1.setEditable(false);
                } else {
                    tf1.setEditable(true);
                }
            }
        });
        txtWeek3.focusedProperty().addListener((ob, ov, nv) -> {
            TextField ftWk[] = {txtWeek1, txtWeek2, txtWeek4, txtSetTar};
            nv = txtWeek3.getText().isEmpty();
            for (TextField tf1 : ftWk) {
                if (nv) {
                    tf1.setEditable(false);
                } else {
                    tf1.setEditable(true);
                }
            }
        });
        txtWeek4.focusedProperty().addListener((ob, ov, nv) -> {
            TextField ftWk[] = {txtWeek1, txtWeek2, txtWeek3, txtSetTar};
            nv = txtWeek4.getText().isEmpty();
            for (TextField tf1 : ftWk) {
                if (nv) {
                    tf1.setEditable(false);
                } else {
                    tf1.setEditable(true);
                }
            }
        });

    }

    private void dataSave() {

        if (lblId.getText().isEmpty()) {
            dataIn();
        } else {
            dataUpdate();
        }

    }

    //TextField To Database Matcher for Update And Save
    private String db2Field(String sql) {
        try {
            psmt = conn.prepareStatement(sql);

            psmt.setString(1, cbUserName.getValue());
            psmt.setString(2, txtWeek1.getText());
            psmt.setString(3, txtWeek2.getText());
            psmt.setString(4, txtWeek3.getText());
            psmt.setString(5, txtWeek4.getText());
            psmt.setString(6, txtSetTar.getText());
            psmt.setString(7, lblAchieved.getText());
            psmt.setString(8, lblPrg.getText());
            psmt.setString(9, date);

            psmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(TargetsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

///Insert Data
    private void dataIn() {

        boolean status = txtWeek1.getText().isEmpty() || txtWeek2.getText().isEmpty() || txtWeek3.getText().isEmpty()
                || txtWeek4.getText().isEmpty() || txtSetTar.getText().isEmpty()
                || cbUserName.getSelectionModel().getSelectedItem() == null || lblAchieved.getText().isEmpty()
                || lblPerDisp.getText().isEmpty();
        if (status) {
            al.txtNull();
        } else {
            String tmpMembers = cbUserLevel.getSelectionModel().getSelectedItem();
            if ("Sales Manager".equals(tmpMembers)) {
                String sql = "insert into targetsSalesM (UserName, week1, week2, week3, week4, setTarget, achTarget, per, DateOfReg) values(?,?,?,?,?,?,?,?,?)";
                db2Field(sql);
            } else if ("Sales Reps".equals(tmpMembers)) {
                String sql = "insert into targetsSalesR (UserName, week1, week2, week3, week4, setTarget, achTarget, per, DateOfReg) values(?,?,?,?,?,?,?,?,?)";
                db2Field(sql);
            } else if ("Merchandizers".equals(tmpMembers)) {
                String sql = "insert into targetsMerch (UserName, week1, week2, week3, week4, setTarget, achTarget, per, DateOfReg) values(?,?,?,?,?,?,?,?,?)";
                db2Field(sql);
            } else {
            }
            al.inserted();
            clean();
        }

    }

    //Delete Tableview row
    private void delTable() {

        ObservableList<tg_getset> productSelected, allProducts;
        allProducts = table.getItems();
        productSelected = table.getSelectionModel().getSelectedItems();

        productSelected.forEach(allProducts::remove);

    }

    //Del Returner
    private String deleter(String sql) {
        try {
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, lblId.getText());

            psmt.executeUpdate();
            al.deleted();
            delTable();
        } catch (Exception e) {
        }
        return sql;
    }

    /////Delete Data
    private void dataDel() {

        String tmpDel = cbUserLevel.getSelectionModel().getSelectedItem();

        if (table.getItems().isEmpty() || table.getSelectionModel().getSelectedItems().isEmpty()) {
        } else {

            String name_d = cbUserName.getValue();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete : " + name_d + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setHeaderText("Are You Sure You Want To Delete This Record ?");
            alert.showAndWait();

            alert.setResizable(false);

            if (alert.getResult() == ButtonType.YES) {
                if ("Sales Manager".equals(tmpDel)) {
                    String sql = "delete from targetsSalesM where id = ?";
                    deleter(sql);
                } else if ("Sales Reps".equals(tmpDel)) {
                    String sql = "delete from targetsSalesR where id = ?";
                    deleter(sql);
                } else if ("Merchandizers".equals(tmpDel)) {
                    String sql = "delete from targetsMerch where id = ?";
                    deleter(sql);
                } else {
                }
            } else {
                al.deletedNot();
            };

        }

    }

    private void dataUpdate() {

        boolean status = txtWeek1.getText().isEmpty() || txtWeek2.getText().isEmpty() || txtWeek3.getText().isEmpty()
                || txtWeek4.getText().isEmpty() || txtSetTar.getText().isEmpty()
                || cbUserName.getSelectionModel().getSelectedItem() == null || lblAchieved.getText().isEmpty()
                || lblPerDisp.getText().isEmpty();

        if (status) {
            al.txtNull();
        } else if (table.getItems().isEmpty() || table.getSelectionModel().getSelectedItems().isEmpty()) {
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Update : " + cbUserName.getValue() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setHeaderText("Are You Sure You Want To Update This Target Record ?");
            alert.showAndWait();

            alert.setResizable(false);

            if (alert.getResult() == ButtonType.YES) {

                String tmpUp = cbUserLevel.getSelectionModel().getSelectedItem();

                if (null != tmpUp) {
                    switch (tmpUp) {
                        case "Sales Manager": {
                            String sql = "update targetsSalesM set UserName = ?, Week1 = ?, Week2 = ?,  Week3 = ?, Week4 = ?,SetTarget = ?, AchTarget = ?, Per = ?, DateOfReg= ?  where id = " + lblId.getText();
                            db2Field(sql);
                            clean();
                            pop();
                            break;
                        }
                        case "Sales Reps": {
                            String sql = "update targetsSalesR set UserName = ?, Week1 = ?, Week2 = ?,  Week3 = ?, Week4 = ?,SetTarget = ?, AchTarget = ?, Per = ?, DateOfReg= ?  where id = " + lblId.getText();
                            db2Field(sql);
                            clean();
                            pop();
                            break;
                        }
                        case "Merchandizers": {
                            String sql = "update targetsMerch set UserName = ?, Week1 = ?, Week2 = ?,  Week3 = ?, Week4 = ?,SetTarget = ?, AchTarget = ?, Per = ?, DateOfReg= ?  where id = " + lblId.getText();
                            db2Field(sql);
                            clean();
                            pop();
                            break;
                        }
                        default:
                            break;
                    }
                }
                al.update();
            } else {
                al.updateNot();
            }
        }

    }

    ////Populator
    private String populator(String sql) {
        clean();
        data.removeAll(data);
        try {
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {

                tg_getset tgs = new tg_getset();

                tgs.setId(Integer.parseInt(rs.getString("id")));
                tgs.setUsernames(rs.getString("username"));
                tgs.setWeek1(Double.parseDouble(rs.getString("week1")));
                tgs.setWeek2(Double.parseDouble(rs.getString("week2")));
                tgs.setWeek3(Double.parseDouble(rs.getString("week3")));
                tgs.setWeek4(Double.parseDouble(rs.getString("week4")));
                tgs.setSettarget(Double.parseDouble(rs.getString("settarget")));
                tgs.setAchtarget(Double.parseDouble(rs.getString("achtarget")));
                tgs.setPercentage(Double.parseDouble(rs.getString("per")));
                tgs.setDateofreg(rs.getString("dateofreg"));

                data.add(tgs);

            }

            tableCols();
            table.setItems(null);
            table.setItems(FXCollections.observableArrayList(data));
        } catch (SQLException ex) {
            Logger.getLogger(TargetsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    private void pop() {
        String tmpPop = cbUserLevel.getSelectionModel().getSelectedItem();
        boolean status = cbUserLevel.getSelectionModel().getSelectedItem() == null;
        if (!status) {
            if ("Sales Manager".equals(tmpPop)) {
                String sql = "select * from targetsSalesM";
                populator(sql);
            } else if ("Sales Reps".equals(tmpPop)) {
                String sql = "select * from targetsSalesR";
                populator(sql);
            } else if ("Merchandizers".equals(tmpPop)) {
                String sql = "select * from targetsMerch";
                populator(sql);
            }
        } else {
            al.general("Make Sure User Level Is Selecteed");
        }
    }

    //click & press
    private void clkAndPress() {
        try {
            tg_getset row = table.getSelectionModel().getSelectedItem();
            lblId.setText(String.valueOf(row.getId()));
            cbUserName.setValue(row.getUsernames());
            txtWeek1.setText(String.valueOf(row.getWeek1()));
            txtWeek2.setText(String.valueOf(row.getWeek2()));
            txtWeek3.setText(String.valueOf(row.getWeek3()));
            txtWeek4.setText(String.valueOf(row.getWeek4()));
            txtSetTar.setText(String.valueOf(row.getSettarget()));
            lblAchieved.setText(String.valueOf(row.getAchtarget()));
            lblPrg.setText(String.valueOf(row.getPercentage()));
            lblDate.setText(row.getDateofreg());
        } catch (Exception e) {
        }
    }

    ////Table click and press listener
    public void tableClick() {
        try {
            table.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
                if ((event.getCode().equals(KeyCode.DOWN)) || (event.getCode().equals(KeyCode.UP))) {
                    clkAndPress();
                } else {
                }
            });
            table.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                if ((event.getClickCount() == 1 || event.getClickCount() == 2) && !table.getColumns().isEmpty()) {
                    clkAndPress();
                } else {
                }
            });
        } catch (Exception e) {
        }
    }

    ///Search via Date and Text
    private void searchData() {
        txtSearcher.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            dpStartDate.getEditor().setText("");
            dpEndDate.getEditor().setText("");
            try {
                String searchInd = txtSearcher.getText();
                String tmpMembers = cbUserLevel.getSelectionModel().getSelectedItem();
                if ("Sales Manager".equals(tmpMembers)) {
                    String sql = "select * from targetsSalesM where username like  '%" + searchInd + "%'  "
                            + "OR week1 like   '%" + searchInd + "%'   OR week2 like   '%" + searchInd + "%'   "
                            + "OR week3 like   '%" + searchInd + "%'   OR week4 like   '%" + searchInd + "%'   "
                            + "OR settarget like   '%" + searchInd + "%' OR achtarget like   '%" + searchInd + "%' "
                            + "OR per like   '%" + searchInd + "%'   OR dateofreg like   '%" + searchInd + "%' ";
                    populator(sql);
                } else if ("Sales Reps".equals(tmpMembers)) {
                    String sql = "select * from targetsSalesR where username like  '%" + searchInd + "%'  "
                            + "OR week1 like   '%" + searchInd + "%'   OR week2 like   '%" + searchInd + "%'   "
                            + "OR week3 like   '%" + searchInd + "%'   OR week4 like   '%" + searchInd + "%'   "
                            + "OR settarget like   '%" + searchInd + "%' OR achtarget like   '%" + searchInd + "%' "
                            + "OR per like   '%" + searchInd + "%'   OR dateofreg like   '%" + searchInd + "%' ";
                    populator(sql);
                } else if ("Merchandizers".equals(tmpMembers)) {
                    String sql = "select * from targetsMerch where username like  '%" + searchInd + "%'  "
                            + "OR week1 like   '%" + searchInd + "%'   OR week2 like   '%" + searchInd + "%'   "
                            + "OR week3 like   '%" + searchInd + "%'   OR week4 like   '%" + searchInd + "%'   "
                            + "OR settarget like   '%" + searchInd + "%' OR achtarget like   '%" + searchInd + "%' "
                            + "OR per like   '%" + searchInd + "%'   OR dateofreg like   '%" + searchInd + "%' ";
                    populator(sql);
                } else {
                }
            } catch (Exception e) {
            }
        });

        txtSearcher.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!dpEndDate.getEditor().getText().isEmpty() || !dpStartDate.getEditor().getText().isEmpty()) {
                String tmpMembers = cbUserLevel.getSelectionModel().getSelectedItem();
                if ("Sales Manager".equals(tmpMembers)) {
                    String sql = "select * from targetsSalesM where DateOfReg between  '" + dpStartDate.getValue().toString() + "' and '" + dpEndDate.getValue().toString() + "' ";
                    populator(sql);
                } else if ("Sales Reps".equals(tmpMembers)) {
                    String sql = "select * from targetsSalesR where DateOfReg between  '" + dpStartDate.getValue().toString() + "' and '" + dpEndDate.getValue().toString() + "' ";
                    populator(sql);
                } else if ("Merchandizers".equals(tmpMembers)) {
                    String sql = "select * from targetsMerch where DateOfReg between  '" + dpStartDate.getValue().toString() + "' and '" + dpEndDate.getValue().toString() + "' ";
                    populator(sql);
                } else {
                }
            } else {
            }
        });

    }

    public class nav_butts {

        int pos;
        String sql;

        public List<tg_getset> BindData() {
            if (data.isEmpty()) {
                List<tg_getset> list_bt = new ArrayList<>();
                String tmp = cbUserLevel.getSelectionModel().getSelectedItem();
                if ("Sales Manager".equals(tmp)) {
                    this.sql = "select * from targetsSalesM";
                } else if ("Sales Reps".equals(tmp)) {
                    this.sql = "select * from targetsSalesR";
                } else if ("Merchandizers".equals(tmp)) {
                    this.sql = "select * from targetsMerch";
                } else {
                    this.sql = null;
                }
                try {
                    list_bt.removeAll(list_bt);
                    rs = conn.createStatement().executeQuery(sql);
                    while (rs.next()) {
                        tg_getset tgnav = new tg_getset();
                        tgnav.setId(Integer.parseInt(rs.getString("id")));
                        tgnav.setUsernames(rs.getString("username"));
                        tgnav.setWeek1(Double.parseDouble(rs.getString("week1")));
                        tgnav.setWeek2(Double.parseDouble(rs.getString("week2")));
                        tgnav.setWeek3(Double.parseDouble(rs.getString("week3")));
                        tgnav.setWeek4(Double.parseDouble(rs.getString("week4")));
                        tgnav.setSettarget(Double.parseDouble(rs.getString("settarget")));
                        tgnav.setAchtarget(Double.parseDouble(rs.getString("achtarget")));
                        tgnav.setPercentage(Double.parseDouble(rs.getString("per")));
                        tgnav.setDateofreg(rs.getString("dateofreg"));
                        list_bt.add(tgnav);
                    }
                    return list_bt;
                } catch (SQLException | NumberFormatException ex) {
                    al.conn_err(ex.toString());
                }
            } else {
                return data;
            }
            return null;
        }

        private void ShowPosInfo(int index) {
            lblId.setText(String.valueOf(BindData().get(index).getId()));
            cbUserName.setValue(BindData().get(index).getUsernames());
            txtWeek1.setText(String.valueOf(BindData().get(index).getWeek1()));
            txtWeek2.setText(String.valueOf(BindData().get(index).getWeek2()));
            txtWeek3.setText(String.valueOf(BindData().get(index).getWeek3()));
            txtWeek4.setText(String.valueOf(BindData().get(index).getWeek4()));
            txtSetTar.setText(String.valueOf(BindData().get(index).getSettarget()));
            lblAchieved.setText(String.valueOf(BindData().get(index).getAchtarget()));
            lblPrg.setText(String.valueOf(BindData().get(index).getPercentage()));
            lblDate.setText(BindData().get(index).getDateofreg());
        }

        //////Navigation buttons functions
        private void nxt() {
            pos++;
            if (pos < BindData().size()) {
                ShowPosInfo(pos);
            } else {
                pos = BindData().size() - 1;
                ShowPosInfo(pos);
                al.nav("End");
            }
        }

        private void fst() {
            pos = 0;
            ShowPosInfo(pos);
        }

        private void lst() {
            pos = BindData().size() - 1;
            ShowPosInfo(pos);
        }

        private void prv() {
            pos--;
            if (pos > 0) {
                ShowPosInfo(pos);
            } else {
                pos = 0;
                ShowPosInfo(pos);
                al.nav("Start");
            }
        }

        /////Clicksings
        private void nav_clicks() {
            btNxt.setOnAction(e -> {
                boolean status = cbUserLevel.getSelectionModel().getSelectedItem() == null;
                if (!status) {
                    nxt();
                } else {
                    al.general("Select User Level");
                }
            });
            btPrv.setOnAction(e -> {
                boolean status = cbUserLevel.getSelectionModel().getSelectedItem() == null;
                if (!status) {
                    prv();
                } else {
                    al.general("Select User Level");
                }
            });
            btFst.setOnAction(e -> {
                boolean status = cbUserLevel.getSelectionModel().getSelectedItem() == null;
                if (!status) {
                    fst();
                } else {
                    al.general("Select User Level");
                }
            });
            btLst.setOnAction(e -> {
                boolean status = cbUserLevel.getSelectionModel().getSelectedItem() == null;
                if (!status) {
                    lst();
                } else {
                    al.general("Select User Level");
                }
            });
        }

    }

    public void setdBoardName(String FullNamesString) {
        this.name = FullNamesString;
    }

}
