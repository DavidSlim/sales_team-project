package TeamMembers;

import Combos.Combos;
import DbConn.DbConn;
import XXPrints.CtrlTeamMembersPrint;
import alerts.alerts;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import login.loginCtrl;
import org.apache.commons.lang3.text.WordUtils;
import timeNow.timeNow;
import validators.validators;

public class teamMembersController implements Initializable {

    ///Stores Name From DashBoard
    private String name = null;
    
    @FXML
    TextField txtSearcher, txtWebsite, txtAcNo, txtBank, txtEmailAdd, txtPhoneNo, txtPinNo, txtOtherName, txtSirName, txtIdNo;
    @FXML
    ComboBox cbDashBoard, cbAssignedTo, cbRoutePlan, cbYOb, cbMOb, cbDOb, cbUserRank;
    @FXML
    Button btImgPicker, btPopulate, btUpdate, btClear, btDel, btSave, btAdd;
    @FXML
    Label lblDate, lblTime, lblAutoCode, lblIdd;
    @FXML
    ListView<String> listView;
    @FXML
    TableView<tm_getset> table;
    @FXML
    ImageView imgView;
    @FXML
    DatePicker dpEnd, dpStart;
    @FXML
    GridPane gpMain;
    @FXML
    Pane ppMain;
    
    timeNow tn = new timeNow();
    Combos cbs = new Combos();
    alerts al = new alerts();
    validators val = new validators();
    autoCodes ac = new autoCodes();
    
    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;

    //String img_s;
    private final SetImg si = new SetImg();
    private String img_path;
    private final Image img = new Image("/img/log.jpg");
    private ObservableList<tm_getset> data = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clean();
        AllValids av = new AllValids();
        av.email_valids();
        av.textVal();
        
        ComboBs();
        tableClick();
        searchData();
        deleter del = new deleter();
        
        btClear.setOnAction(e -> printScene());
        btImgPicker.setOnAction(e -> si.loadImage());
        btSave.setOnAction(e -> saveData());
        btPopulate.setOnAction(e -> pop());
        btDel.setOnAction(e -> del.delData());
        btUpdate.setOnAction(e -> updateData());
        
        txtWebsite.setEditable(false);
        cbUserRank.setOnAction(e -> assTo());
        tn.timeCurr(lblTime);
        
    }

    ///table columns
    private void tableCols() {
        TableColumn<tm_getset, Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        IdCol.setMaxWidth(200);
        TableColumn<tm_getset, String> AutoCol = new TableColumn<>("Auto Code");
        AutoCol.setCellValueFactory(new PropertyValueFactory<>("autocode"));
        AutoCol.setMaxWidth(200);
        TableColumn<tm_getset, String> IdNoCol = new TableColumn<>("ID No");
        IdNoCol.setCellValueFactory(new PropertyValueFactory<>("idno"));
        IdNoCol.setMaxWidth(200);
        TableColumn<tm_getset, String> SirNameCol = new TableColumn<>("Sir Name");
        SirNameCol.setCellValueFactory(new PropertyValueFactory<>("sirname"));
        SirNameCol.setMaxWidth(200);
        TableColumn<tm_getset, String> OtherNamesCol = new TableColumn<>("Other Names");
        OtherNamesCol.setCellValueFactory(new PropertyValueFactory<>("othernames"));
        OtherNamesCol.setMaxWidth(200);
        TableColumn<tm_getset, String> UserRankCol = new TableColumn<>("User Rank");
        UserRankCol.setCellValueFactory(new PropertyValueFactory<>("userrank"));
        UserRankCol.setMaxWidth(200);
        TableColumn<tm_getset, String> AssToCol = new TableColumn<>("Assigned To");
        AssToCol.setCellValueFactory(new PropertyValueFactory<>("assignedto"));
        AssToCol.setMaxWidth(200);
        TableColumn<tm_getset, String> DobCol = new TableColumn<>("DOB");
        DobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));
        DobCol.setMaxWidth(40);
        TableColumn<tm_getset, String> MobCol = new TableColumn<>("MOB");
        MobCol.setCellValueFactory(new PropertyValueFactory<>("mob"));
        MobCol.setMaxWidth(50);
        TableColumn<tm_getset, String> YobCol = new TableColumn<>("YOB");
        YobCol.setCellValueFactory(new PropertyValueFactory<>("yob"));
        YobCol.setMaxWidth(50);
        TableColumn<tm_getset, String> PinNoCol = new TableColumn<>("Pin No");
        PinNoCol.setCellValueFactory(new PropertyValueFactory<>("pinno"));
        PinNoCol.setMaxWidth(200);
        TableColumn<tm_getset, String> RoutePlanCol = new TableColumn<>("Route Plan");
        RoutePlanCol.setCellValueFactory(new PropertyValueFactory<>("routeplan"));
        RoutePlanCol.setMaxWidth(200);
        TableColumn<tm_getset, String> PhoneCol = new TableColumn<>("Phone No");
        PhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneno"));
        PhoneCol.setMaxWidth(200);
        TableColumn<tm_getset, String> EmailCol = new TableColumn<>("Email");
        EmailCol.setCellValueFactory(new PropertyValueFactory<>("emailadd"));
        EmailCol.setMaxWidth(200);
        TableColumn<tm_getset, String> BankCol = new TableColumn<>("Bank");
        BankCol.setCellValueFactory(new PropertyValueFactory<>("bank"));
        BankCol.setMaxWidth(60);
        TableColumn<tm_getset, String> ACNoCol = new TableColumn<>("AC/No");
        ACNoCol.setCellValueFactory(new PropertyValueFactory<>("acno"));
        ACNoCol.setMaxWidth(200);
        TableColumn<tm_getset, String> DAteOfRegCol = new TableColumn<>("Date Of Reg");
        DAteOfRegCol.setCellValueFactory(new PropertyValueFactory<>("dateofreg"));
        DAteOfRegCol.setMaxWidth(200);
        TableColumn<tm_getset, String> ImageCol = new TableColumn<>("Image");
        ImageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        ImageCol.setMaxWidth(200);
        TableColumn<tm_getset, String> PostByCol = new TableColumn<>("POSTED BY");
        PostByCol.setCellValueFactory(new PropertyValueFactory<>("postBy"));
        ImageCol.setMaxWidth(200);
        table.getColumns().addAll(AutoCol, SirNameCol, OtherNamesCol, UserRankCol, AssToCol, RoutePlanCol, PhoneCol, PostByCol);
    }

    ///DateOfBirth
    public void ComboBs() {
        
        cbMOb.setItems(FXCollections.observableArrayList(cbs.months()));
        cbYOb.setItems(FXCollections.observableArrayList(cbs.years()));
        cbDOb.setItems(FXCollections.observableArrayList(cbs.days()));
        
        cbUserRank.setItems(FXCollections.observableArrayList(cbs.userRanks()));
        cbRoutePlan.setItems(FXCollections.observableArrayList(cbs.location()));
        
    }

    ////Assigned to populator
    public void assTo() {
        
        String tmpMembers = cbUserRank.getSelectionModel().getSelectedItem().toString();
        
        if ("Sales Manager".equals(tmpMembers)) {
            cbAssignedTo.setItems(FXCollections.observableArrayList(cbs.manager()));
        } else if ("Sales Reps".equals(tmpMembers)) {
            cbAssignedTo.setItems(FXCollections.observableArrayList(cbs.salesmanager()));
        } else if ("Merchandizers".equals(tmpMembers)) {
            cbAssignedTo.setItems(FXCollections.observableArrayList(cbs.salesrep()));
        } else if ("Board Of Govs".equals(tmpMembers)) {
            cbAssignedTo.setItems(FXCollections.observableArrayList(cbs.bog()));
        } else {
        }
    }
    
    private void clear() {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Clear All Fields?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setHeaderText("Are You Sure? This Will Clear All Fields");
        alert.showAndWait();
        
        alert.setResizable(false);
        
        if (alert.getResult() == ButtonType.YES) {
            clean();
            DatePicker dpA[] = {dpStart, dpEnd};
            for (DatePicker dpAs : dpA) {
                dpAs.getEditor().setText("");
            }
            txtSearcher.clear();
            al.cleared();
        } else {
            al.clearNot();
        }
    }
    
    private void clean() {
        
        Tooltip asstooltip = new Tooltip("Assigned To");
        Tooltip routetooltip = new Tooltip("Select Routeplan");
        TextField tfCl[] = {txtAcNo, txtBank, txtEmailAdd, txtIdNo, txtOtherName, txtPhoneNo, txtPinNo, txtSirName, txtWebsite};
        for (TextField tfClr : tfCl) {
            tfClr.clear();
        }
        table.getColumns().clear();
        listView.getItems().clear();
        ComboBox cbCl[] = {cbDOb, cbMOb, cbYOb};
        for (ComboBox cbClr : cbCl) {
            cbClr.getSelectionModel().selectFirst();
        }
        cbAssignedTo.valueProperty().set(null);
        cbRoutePlan.valueProperty().set(null);
        cbAssignedTo.setTooltip(asstooltip);
        cbRoutePlan.setTooltip(routetooltip);
        lblAutoCode.setText("");
        lblIdd.setText("");
        lblDate.setText(tn.date());
        imgView.setImage(img);
        img_path = null;
    }

    //Generate AutoCode Value
    private class autoCodes {
        
        private String AcCode() {
            String idCo = null;
            List lst = new ArrayList();
            data = FXCollections.observableArrayList();
            data.removeAll(data);
            String tmpMembers = cbUserRank.getSelectionModel().getSelectedItem().toString();
            try {
                if ("Sales Manager".equals(tmpMembers)) {
                    rs = conn.createStatement().executeQuery("select * from salesmanager");
                    while (rs.next()) {
                        tm_getset tgs = new tm_getset();
                        tgs.id.set(rs.getInt("id"));
                        data.add(tgs);
                        lst.add(tgs.getId());
                    }
                    int idd = (int) lst.stream().reduce((a, h) -> h).orElse(0);
                    idCo = "SMG-" + tn.month() + "/" + String.format("%04d", (idd + 1)) + "/" + tn.year();
                    
                } else if ("Sales Reps".equals(tmpMembers)) {
                    rs = conn.createStatement().executeQuery("select * from salesrep");
                    while (rs.next()) {
                        tm_getset tgs = new tm_getset();
                        tgs.id.set(rs.getInt("id"));
                        data.add(tgs);
                        lst.add(tgs.getId());
                    }
                    int idd = (int) lst.stream().reduce((a, h) -> h).orElse(0);
                    idCo = "SRP-" + tn.month() + "/" + String.format("%04d", (idd + 1)) + "/" + tn.year();
                    
                } else if ("Merchandizers".equals(tmpMembers)) {
                    rs = conn.createStatement().executeQuery("select * from merchandizers");
                    while (rs.next()) {
                        tm_getset tgs = new tm_getset();
                        tgs.id.set(rs.getInt("id"));
                        data.add(tgs);
                        lst.add(tgs.getId());
                    }
                    int idd = (int) lst.stream().reduce((a, h) -> h).orElse(0);
                    idCo = "MRC-" + tn.month() + "/" + String.format("%04d", (idd + 1)) + "/" + tn.year();
                    
                } else if ("Board Of Govs".equals(tmpMembers)) {
                    rs = conn.createStatement().executeQuery("select * from boardofgovernors");
                    while (rs.next()) {
                        tm_getset tgs = new tm_getset();
                        tgs.id.set(rs.getInt("id"));
                        data.add(tgs);
                        lst.add(tgs.getId());
                    }
                    int idd = (int) lst.stream().reduce((a, h) -> h).orElse(0);
                    idCo = "BOG-" + tn.month() + "/" + String.format("%04d", (idd + 1)) + "/" + tn.year();
                } else {
                }
            } catch (Exception e) {
            }
            return idCo;
        }
    }

    ///TextFields Database Matchers for Save And Update
    private String db2Field(String sql) {
        
        try {
            
            data = FXCollections.observableArrayList();
            data.removeAll(data);
            psmt = conn.prepareStatement(sql);
            
            psmt.setString(1, ac.AcCode());
            psmt.setString(2, txtIdNo.getText().toUpperCase());
            psmt.setString(3, WordUtils.capitalizeFully(txtSirName.getText()));
            psmt.setString(4, WordUtils.capitalizeFully(txtOtherName.getText()));
            psmt.setString(5, cbUserRank.getValue().toString());
            psmt.setString(6, cbAssignedTo.getValue().toString());
            psmt.setString(7, cbDOb.getValue().toString());
            psmt.setString(8, cbMOb.getValue().toString());
            psmt.setString(9, cbYOb.getValue().toString());
            psmt.setString(10, txtPinNo.getText().toUpperCase());
            psmt.setString(11, cbRoutePlan.getValue().toString());
            psmt.setString(12, txtPhoneNo.getText());
            psmt.setString(13, txtEmailAdd.getText().toLowerCase());
            psmt.setString(14, WordUtils.capitalizeFully(txtBank.getText()));
            psmt.setString(15, txtAcNo.getText().toUpperCase());
            psmt.setString(16, lblDate.getText());
            psmt.setString(17, si.img_path());
            psmt.setString(18, name);
            
            psmt.executeUpdate();
            
        } catch (SQLException e) {
        }
        
        return sql;
    }

    ///Insert Data into database
    private void saveData() {
        
        boolean status = txtAcNo.getText().isEmpty() || txtBank.getText().isEmpty() || txtEmailAdd.getText().isEmpty()
                || txtIdNo.getText().isEmpty() || txtOtherName.getText().isEmpty() || txtPhoneNo.getText().isEmpty()
                || txtPinNo.getText().isEmpty() || txtSirName.getText().isEmpty() || cbDOb.getSelectionModel().getSelectedItem() == null
                || cbMOb.getSelectionModel().getSelectedItem() == null || cbUserRank.getSelectionModel().getSelectedItem() == null
                || cbAssignedTo.getSelectionModel().getSelectedItem() == null || img_path == null
                || cbYOb.getSelectionModel().getSelectedItem() == null || imgView.getImage().equals("");
        if (status) {
            al.txtNull();
        } else {
            
            String tmpSave = cbUserRank.getSelectionModel().getSelectedItem().toString();

            ///Save Sales Manager
            if ("Sales Manager".equals(tmpSave)) {
                String sql = "insert into salesmanager (AutoCode, IdNo, SirName, OtherNames, UserRank, AssignedTo, DOb ,MOb, YOb, PinNo, "
                        + "RoutePlan, PhoneNo, EmailAdd, Bank, ACNo, DateOfReg, image, postBy) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                db2Field(sql);
                clean();
            }

            //Insert Sales Reps
            if ("Sales Reps".equals(tmpSave)) {
                String sql = "insert into salesrep (AutoCode, IdNo, SirName, OtherNames, UserRank, AssignedTo, DOb ,MOb, YOb, PinNo, "
                        + "RoutePlan, PhoneNo, EmailAdd, Bank, ACNo, DateOfReg, image, postBy) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                db2Field(sql);
                clean();
            }

            ///Insert merchandizers
            if ("Merchandizers".equals(tmpSave)) {
                String sql = "insert into merchandizers (AutoCode, IdNo, SirName, OtherNames, UserRank, AssignedTo, DOb ,MOb, YOb, PinNo, "
                        + "RoutePlan, PhoneNo, EmailAdd, Bank, ACNo, DateOfReg, image, postBy) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                db2Field(sql);
                clean();
            }

            ///Insert Board Of Governers
            if ("Board Of Govs".equals(tmpSave)) {
                String sql = "insert into boardofgovernors (AutoCode, IdNo, SirName, OtherNames, UserRank, AssignedTo, DOb ,MOb, YOb, PinNo, "
                        + "RoutePlan, PhoneNo, EmailAdd, Bank, ACNo, DateOfReg, image, postBy) "
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                db2Field(sql);
                clean();
            }
            
            al.inserted();
        }
    }

    ///TextFields Database Matchers for Save And Update
    private String db2Update(String sql) {
        try {
            
            data = FXCollections.observableArrayList();
            data.removeAll(data);
            psmt = conn.prepareStatement(sql);
            
            psmt.setString(1, lblAutoCode.getText());
            psmt.setString(2, txtIdNo.getText().toUpperCase());
            psmt.setString(3, WordUtils.capitalizeFully(txtSirName.getText()));
            psmt.setString(4, WordUtils.capitalizeFully(txtOtherName.getText()));
            psmt.setString(5, cbUserRank.getValue().toString());
            psmt.setString(6, cbAssignedTo.getValue().toString());
            psmt.setString(7, cbDOb.getValue().toString());
            psmt.setString(8, cbMOb.getValue().toString());
            psmt.setString(9, cbYOb.getValue().toString());
            psmt.setString(10, txtPinNo.getText().toUpperCase());
            psmt.setString(11, cbRoutePlan.getValue().toString());
            psmt.setString(12, txtPhoneNo.getText());
            psmt.setString(13, txtEmailAdd.getText().toLowerCase());
            psmt.setString(14, WordUtils.capitalizeFully(txtBank.getText()));
            psmt.setString(15, txtAcNo.getText().toUpperCase());
            psmt.setString(16, lblDate.getText());
            psmt.setString(17, si.img_path());
            psmt.setString(18, name);
            
            al.inserted();
            
            psmt.executeUpdate();
            
        } catch (SQLException e) {
        }
        
        return sql;
    }

    /////Update Data
    private void updateData() {
        
        boolean status = txtAcNo.getText().isEmpty() || txtBank.getText().isEmpty() || txtEmailAdd.getText().isEmpty()
                || txtIdNo.getText().isEmpty() || txtOtherName.getText().isEmpty() || txtPhoneNo.getText().isEmpty()
                || txtPinNo.getText().isEmpty() || txtSirName.getText().isEmpty() || cbDOb.getSelectionModel().getSelectedItem() == null
                || cbMOb.getSelectionModel().getSelectedItem() == null || cbUserRank.getSelectionModel().getSelectedItem() == null || img_path == null
                || cbAssignedTo.getSelectionModel().getSelectedItem() == null || cbYOb.getSelectionModel().getSelectedItem() == null || imgView.getImage().equals("");
        
        if (status) {
            al.txtNull();
        } else if (table.getItems().isEmpty() || table.getSelectionModel().getSelectedItems().isEmpty()) {
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Update : " + txtSirName.getText() + " " + txtOtherName.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setHeaderText("Are You Sure You Want To Update This Record ?");
            alert.showAndWait();
            
            alert.setResizable(false);
            
            if (alert.getResult() == ButtonType.YES) {
                
                String tmpUp = cbUserRank.getSelectionModel().getSelectedItem().toString();
                
                if (null != tmpUp) {
                    switch (tmpUp) {
                        case "Sales Manager": {
                            String sql = "update salesmanager set AutoCode = ?, IdNo = ?, SirName = ?, OtherNames = ?, UserRank = ?,  AssignedTo = ?, DOb = ?,MOb = ?, YOb = ?, PinNo = ?, "
                                    + "RoutePlan = ?, PhoneNo = ?, EmailAdd = ?, Bank = ?, ACNo = ? , DateOfReg = ?, image =?, postBy =? where id = " + lblIdd.getText();
                            db2Update(sql);
                            clean();
                            pop();
                            al.update();
                            break;
                        }
                        case "Sales Reps": {
                            String sql = "update salesrep set AutoCode = ?, IdNo = ?, SirName = ?, OtherNames = ?, UserRank = ?,  AssignedTo = ?, DOb = ?,MOb = ?, YOb = ?, PinNo = ?, "
                                    + "RoutePlan = ?, PhoneNo = ?, EmailAdd = ?, Bank = ?, ACNo = ? , DateOfReg = ?, image =?, postBy =? where id = " + lblIdd.getText();
                            db2Update(sql);
                            clean();
                            pop();
                            al.update();
                            break;
                        }
                        case "Merchandizers": {
                            String sql = "update merchandizers set AutoCode = ?, IdNo = ?, SirName = ?, OtherNames = ?, UserRank = ?,  AssignedTo = ?, DOb = ?,MOb = ?, YOb = ?, PinNo = ?, "
                                    + "RoutePlan = ?, PhoneNo = ?, EmailAdd = ?, Bank = ?, ACNo = ? , DateOfReg = ?, image =?, postBy =? where id = " + lblIdd.getText();
                            db2Update(sql);
                            clean();
                            pop();
                            al.update();
                            break;
                        }
                        case "Board Of Govs": {
                            String sql = "update boardofgovernors set AutoCode = ?, IdNo = ?, SirName = ?, OtherNames = ?, UserRank = ?,  AssignedTo = ?, DOb = ?,MOb = ?, YOb = ?, PinNo = ?, "
                                    + "RoutePlan = ?, PhoneNo = ?, EmailAdd = ?, Bank = ?, ACNo = ? , DateOfReg = ?, image =?, postBy =? where id = " + lblIdd.getText();
                            db2Update(sql);
                            clean();
                            pop();
                            al.update();
                            break;
                        }
                        default:
                            break;
                    }
                }
                
            } else {
                al.updateNot();
            }
        }
        
    }

    //Delete Tableview row
    private void delTable() {
        
        ObservableList<tm_getset> productSelected, allProducts;
        allProducts = table.getItems();
        productSelected = table.getSelectionModel().getSelectedItems();
        
        productSelected.forEach(allProducts::remove);
        
    }

    /////Delete Data
    public class deleter {
        
        private String deleter(String sql) {
            
            try {
                psmt = conn.prepareStatement(sql);
                psmt.setString(1, lblAutoCode.getText());
                
                psmt.executeUpdate();
                al.deleted();
                delTable();
            } catch (Exception e) {
            }
            
            return sql;
        }
        
        private void delData() {
            
            String tmpDel = cbUserRank.getSelectionModel().getSelectedItem().toString();
            
            if (table.getItems().isEmpty() || table.getSelectionModel().getSelectedItems().isEmpty()) {
            } else {
                
                String name = txtSirName.getText() + " " + txtOtherName.getText();
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete : " + name + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.setHeaderText("Are You Sure You Want To Delete This Record ?");
                alert.showAndWait();
                
                alert.setResizable(false);
                
                if (alert.getResult() == ButtonType.YES) {
                    if ("Sales Manager".equals(tmpDel)) {
                        String sql = "delete from salesmanager where autocode = ?";
                        deleter(sql);
                    } else if ("Sales Reps".equals(tmpDel)) {
                        String sql = "delete from salesrep where autocode = ?";
                        deleter(sql);
                    } else if ("Merchandizers".equals(tmpDel)) {
                        String sql = "delete from merchandizers where autocode = ?";
                        deleter(sql);
                    } else if ("Board Of Govs".equals(tmpDel)) {
                        String sql = "delete from boardofgovernors where autocode = ?";
                        deleter(sql);
                    } else {
                    }
                    
                } else {
                    al.deletedNot();
                }
            }
        }
    }

    ////Populator To Fetch Database dat
    private String populator(String sql) {
        List lst = new ArrayList();
        Map<String, Double> map_c;
        clean();
        try {
            data = FXCollections.observableArrayList();
            data.removeAll(data);
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                tm_getset tgs = new tm_getset();
                
                tgs.id.set(rs.getInt("id"));
                tgs.autocode.set(rs.getString("autocode"));
                tgs.idno.set(rs.getString("idno"));
                tgs.sirname.set(rs.getString("sirname"));
                tgs.othernames.set(rs.getString("othernames"));
                tgs.userrank.set(rs.getString("userrank"));
                tgs.assignedto.set(rs.getString("assignedTo"));
                tgs.dob.set(rs.getString("dob"));
                tgs.mob.set(rs.getString("mob"));
                tgs.yob.set(rs.getString("yob"));
                tgs.pinno.set(rs.getString("pinno"));
                tgs.routeplan.set(rs.getString("routeplan"));
                tgs.phoneno.set(rs.getString("phoneno"));
                tgs.emailadd.set(rs.getString("emailadd"));
                tgs.bank.set(rs.getString("bank"));
                tgs.acno.set(rs.getString("acno"));
                tgs.dateofreg.set(rs.getString("dateofreg"));
                tgs.image_p.set(rs.getString("image"));
                tgs.postBy.set(rs.getString("postBy"));
                
                data.add(tgs);
                
                lst.add(tgs.getRouteplan());
                
            }
            map_c = (Map<String, Double>) lst.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
            
            tableCols();
            table.setItems(null);
            table.setItems(data);
            System.out.println("Values Ddd" + map_c.entrySet());
            
            System.out.println("Posted By ; " + name);
        } catch (Exception e) {
        }
        return sql;
    }

    ///pop data
    private void pop() {
        try {
            String tmpPop = cbUserRank.getSelectionModel().getSelectedItem().toString();
            if ("Sales Manager".equals(tmpPop)) {
                String sql = "select * from salesmanager";
                populator(sql);
                
            } else if ("Sales Reps".equals(tmpPop)) {
                String sql = "select * from salesrep";
                populator(sql);
            } else if ("Merchandizers".equals(tmpPop)) {
                String sql = "select * from merchandizers";
                populator(sql);
            } else if ("Board Of Govs".equals(tmpPop)) {
                String sql = "select * from boardofgovernors";
                populator(sql);
            } else {
            }
        } catch (Exception e) {
        }
    }

    //click & press
    private void clkAndPress() {
        
        try {
            
            tm_getset row = table.getSelectionModel().getSelectedItem();
            Image image = new Image(row.getImage_p());
            
            lblIdd.setText(String.valueOf(row.getId()));
            txtIdNo.setText(row.getIdno());
            lblAutoCode.setText(row.getAutocode());
            txtSirName.setText(row.getSirname());
            txtOtherName.setText(row.getOthernames());
            cbUserRank.setValue(row.getUserrank());
            cbAssignedTo.setValue(row.getAssignedto());
            cbDOb.setValue(row.getDob());
            cbMOb.setValue(row.getMob());
            cbYOb.setValue(row.getYob());
            txtPinNo.setText(row.getPinno());
            cbRoutePlan.setValue(row.getRouteplan());
            txtPhoneNo.setText(row.getPhoneno());
            txtEmailAdd.setText(row.getEmailadd());
            txtBank.setText(row.getBank());
            txtAcNo.setText(row.getAcno());
            lblDate.setText(row.getDateofreg());
            imgView.setImage(image);
            
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
    
    private class SetImg {

        ////Load image into imageview
        private void loadImage() {
            try {
                FileChooser fc = new FileChooser();
                File imgDir = fc.showOpenDialog(null);
                
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.bmp", "*.png", "*.jpg", "*.gif", "*.BMP", "*.PNG", "*.JPG", "*.GIF"));
                fc.setTitle("Open Image");
                
                if (imgDir != null) {
                    Image image = new Image(imgDir.toURI().toURL().toString(), 480, 320, true, true);
                    imgView.setImage(image);
                    img_path = imgDir.toURI().toURL().toString();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Image Error");
                    alert.setHeaderText("Default Image Set");
                    alert.showAndWait();
                    alert.setResizable(false);
                    imgView.setImage(img);
                    img_path = "/img/log.jpg";
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(teamMembersController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private String img_path() {
            return img_path;
        }
    }

    ////Email Validation
    public class AllValids {
        
        public void textVal() {
            
            txtPhoneNo.setOnKeyTyped(e -> val.numPh(e));
            TextField names[] = {txtBank, txtIdNo, txtOtherName, txtSirName, txtAcNo, txtIdNo};
            for (int i = 0; i < names.length; i++) {
                names[i].setOnKeyTyped(e -> val.others(e));
            }
            
        }
        
        public void email_valids() {
            txtEmailAdd.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue) {
                    try {
                        boolean status;
                        status = validators.email_val(txtEmailAdd.getText());
                        if (status) {
                            btSave.setDisable(false);
                            btUpdate.setDisable(false);
                        } else {
                            al.email();
                            btSave.setDisable(true);
                            btUpdate.setDisable(true);
                        }
                        
                    } catch (Exception e) {
                    }
                } else {
                }
            });
        }
    }

    ///Search via Date and Text
    private void searchData() {
        txtSearcher.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            String searchInd = txtSearcher.getText();
            String tmpSearcher = cbUserRank.getSelectionModel().getSelectedItem().toString();
            if ("Sales Manager".equals(tmpSearcher)) {
                String sql = "select * from salesmanager where autocode like   '%" + searchInd + "%'  "
                        + "OR idno like   '%" + searchInd + "%'   OR sirname like   '%" + searchInd + "%'   "
                        + "OR othernames like   '%" + searchInd + "%'   OR userrank like   '%" + searchInd + "%'   OR assignedTo like   '%" + searchInd + "%' "
                        + "OR dob like   '%" + searchInd + "%'   OR mob like   '%" + searchInd + "%' "
                        + "OR yob like   '%" + searchInd + "%'   OR pinno like   '%" + searchInd + "%' "
                        + "OR routeplan like   '%" + searchInd + "%'   OR phoneno like   '%" + searchInd + "%' "
                        + "OR emailadd like   '%" + searchInd + "%'   OR bank like   '%" + searchInd + "%' "
                        + "OR acno like   '%" + searchInd + "%'   OR dateofreg like   '%" + searchInd + "%' ";
                populator(sql);
            } else if ("Sales Reps".equals(tmpSearcher)) {
                String sql = "select * from salesrep where autocode like   '%" + searchInd + "%'  "
                        + "OR idno like   '%" + searchInd + "%'   OR sirname like   '%" + searchInd + "%'   "
                        + "OR othernames like   '%" + searchInd + "%'   OR userrank like   '%" + searchInd + "%'   OR assignedTo like   '%" + searchInd + "%' "
                        + "OR dob like   '%" + searchInd + "%'   OR mob like   '%" + searchInd + "%' "
                        + "OR yob like   '%" + searchInd + "%'   OR pinno like   '%" + searchInd + "%' "
                        + "OR routeplan like   '%" + searchInd + "%'   OR phoneno like   '%" + searchInd + "%' "
                        + "OR emailadd like   '%" + searchInd + "%'   OR bank like   '%" + searchInd + "%' "
                        + "OR acno like   '%" + searchInd + "%'   OR dateofreg like   '%" + searchInd + "%' ";
                populator(sql);
            } else if ("Merchandizers".equals(tmpSearcher)) {
                String sql = "select * from merchandizers where autocode like   '%" + searchInd + "%'  "
                        + "OR idno like   '%" + searchInd + "%'   OR sirname like   '%" + searchInd + "%'   "
                        + "OR othernames like   '%" + searchInd + "%'   OR userrank like   '%" + searchInd + "%'   OR assignedTo like   '%" + searchInd + "%' "
                        + "OR dob like   '%" + searchInd + "%'   OR mob like   '%" + searchInd + "%' "
                        + "OR yob like   '%" + searchInd + "%'   OR pinno like   '%" + searchInd + "%' "
                        + "OR routeplan like   '%" + searchInd + "%'   OR phoneno like   '%" + searchInd + "%' "
                        + "OR emailadd like   '%" + searchInd + "%'   OR bank like   '%" + searchInd + "%' "
                        + "OR acno like   '%" + searchInd + "%'   OR dateofreg like   '%" + searchInd + "%' ";
                populator(sql);
            } else if ("Board Of Govs".equals(tmpSearcher)) {
                String sql = "select * from boardofgovernors where autocode like   '%" + searchInd + "%'  "
                        + "OR idno like   '%" + searchInd + "%'   OR sirname like   '%" + searchInd + "%'   "
                        + "OR othernames like   '%" + searchInd + "%'   OR userrank like   '%" + searchInd + "%'   OR assignedTo like   '%" + searchInd + "%' "
                        + "OR dob like   '%" + searchInd + "%'   OR mob like   '%" + searchInd + "%' "
                        + "OR yob like   '%" + searchInd + "%'   OR pinno like   '%" + searchInd + "%' "
                        + "OR routeplan like   '%" + searchInd + "%'   OR phoneno like   '%" + searchInd + "%' "
                        + "OR emailadd like   '%" + searchInd + "%'   OR bank like   '%" + searchInd + "%' "
                        + "OR acno like   '%" + searchInd + "%'   OR dateofreg like   '%" + searchInd + "%' ";
                populator(sql);
            } else {
            }
        });
        
        txtSearcher.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!dpEnd.getEditor().getText().isEmpty() || !dpStart.getEditor().getText().isEmpty()) {
                String tmpSearcher = cbUserRank.getSelectionModel().getSelectedItem().toString();
                if ("Sales Manager".equals(tmpSearcher)) {
                    String sql = "select * from salesmanager where DateOfReg between  '" + dpStart.getValue().toString() + "' and '" + dpEnd.getValue().toString() + "' ";
                    populator(sql);
                } else if ("Sales Reps".equals(tmpSearcher)) {
                    String sql = "select * from salesrep where DateOfReg between  '" + dpStart.getValue().toString() + "' and '" + dpEnd.getValue().toString() + "' ";
                    populator(sql);
                } else if ("Merchandizers".equals(tmpSearcher)) {
                    String sql = "select * from merchandizers where DateOfReg between  '" + dpStart.getValue().toString() + "' and '" + dpEnd.getValue().toString() + "' ";
                    populator(sql);
                } else if ("Board Of Govs".equals(tmpSearcher)) {
                    String sql = "select * from boardofgovernors where DateOfReg between  '" + dpStart.getValue().toString() + "' and '" + dpEnd.getValue().toString() + "' ";
                    populator(sql);
                } else {
                }
            } else {
            }
        });
        
        dpEnd.setOnKeyPressed((KeyEvent e) -> {
            
            try {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    /*                        String sql = "select * from biz_reg where date_start between  '" + dpStart.getValue().toString() + "' and '" + dpEnd.getValue().toString() + "' ";
                        pop_biz.setSql(sql);*/
                    
                    System.out.println(dpStart.getValue() + " = " + dpEnd.getValue());
                    
                    LocalDate start = dpStart.getValue();
                    LocalDate end = dpEnd.getValue();
                    
                    List<LocalDate> dates_all = new ArrayList<>();
                    for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                        dates_all.add(d);
                    }
                                        
                    System.out.println(dates_all);
                    dates_all.stream().forEach(d -> System.out.println("Daates = " + d));
                    
                    tm_getset tgs = new tm_getset();
                    List<LocalDate> dates_all_iterate = new ArrayList<>();
                    dates_all_iterate.add(LocalDate.parse(tgs.getDateofreg()));
                    dates_all_iterate.stream().forEach(dt -> System.out.println("Date_It = " + dt));
                    //dates_all_iterate.stream().from(dpStart.getValue()).to(dpEnd.getValue()).stream().collect(Collectors.toList());

                } else {
                    al.general("Provide Dates To Be Searched");
                }
            } catch (Exception ex) {
            }
            
        });
        
    }
    
    private void printScene() {
        
        boolean stat = cbUserRank.getValue() == null || cbRoutePlan.getValue() == null || cbAssignedTo.getValue() == null;
        if (stat) {
            al.print();
        } else {
            
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/XXPrints/FXMLTeamMembersPrint.fxml"));
                
                Parent root = (Parent) fxmlLoader.load();

                ///Pass Login Values To OtherCtrl
                CtrlTeamMembersPrint tm = fxmlLoader.getController();
                tm.setFName(txtSirName.getText());
                tm.setOName(txtOtherName.getText());
                tm.setDob(cbDOb.getValue().toString() + "/" + cbMOb.getValue().toString() + "/" + cbYOb.getValue().toString());
                tm.setRPlan(cbRoutePlan.getValue().toString());
                tm.setPin(txtPinNo.getText());
                tm.setAssTo(cbAssignedTo.getValue().toString());
                tm.setPhone(txtPhoneNo.getText());
                tm.setBank(txtBank.getText());
                tm.setAcNo(txtAcNo.getText());
                tm.setEmail(txtEmailAdd.getText());
                tm.setUserRank(cbUserRank.getValue().toString());
                tm.setIdNat(txtIdNo.getText());
                tm.setIdCo(lblAutoCode.getText());
                tm.setDate(tn.date());
                tm.setTime(lblTime.getText());
                tm.setYear(String.valueOf(tn.year()));
                tm.setRegDate(lblDate.getText());
                
                tm_getset row = table.getSelectionModel().getSelectedItem();
                tm.setImage(new Image(row.getImage_p()));
                tm.setPostBy(name);

                //end of passing login values
                Scene scene = new Scene(root);
                stage.setTitle(txtOtherName.getText() + " Details");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();
                stage.close();

                ////Call Print Method
                tm.printJ();

                //tm.ss();
                clean();
            } catch (IOException ex) {
                Logger.getLogger(loginCtrl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setdBoardName(String FullNamesString) {
        this.name = FullNamesString;
    }
    
}
