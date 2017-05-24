package products;

import DbConn.DbConn;
import RandomNo.ranNo;
import alerts.alerts;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.text.WordUtils;
import timeNow.timeNow;
import validators.validators;

public class productsController implements Initializable {

    private String name = null;

    ///TextFields
    @FXML
    TextField txtBC;
    @FXML
    TextField txtProdName;
    @FXML
    TextField txtProdDesc;
    @FXML
    TextField txtProdPrice;
    @FXML
    TextField txtSearcher;
    //Labels
    @FXML
    Label lblTime;
    @FXML
    Label lblAc;
    @FXML
    Label lblId;
    @FXML
    Label lblProdPrice;
    @FXML
    Label lblDate;
    @FXML
    Label lblDate1;
    @FXML
    Label lblDate2;
    @FXML
    Label lblTtItems;

    @FXML
    HBox hbmain;
    @FXML
    HBox hbtain;
    ///Buttons
    @FXML
    Button btPop;
    @FXML
    Button btClr;
    @FXML
    Button btDel;
    @FXML
    Button btSave;
    @FXML
    Button btUpdate;
    ///DatePicker
    @FXML
    DatePicker dpF;
    @FXML
    DatePicker dpStart;
    @FXML
    DatePicker dpEnd;
    ///Table
    @FXML
    TableView<product_getset> table;
    @FXML
    ListView listView;

    private ObservableList<product_getset> products = FXCollections.observableArrayList();

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;
    alerts al = new alerts();
    timeNow tn = new timeNow();
    ranNo rn = new ranNo();
    validators val = new validators();

    ObservableList<ObservableList> data;
    String date = (LocalDate.now()).toString();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtProdPrice.setOnKeyTyped(e -> val.numDot(e));
        clean();
        DatePicker[] dpA = {dpEnd, dpF, dpStart};
        for (DatePicker dpAs : dpA) {
            dpAs.setEditable(false);
            dpAs.setShowWeekNumbers(true);
        }
        ///Functions
        btClr.setOnAction(e -> clear());
        btSave.setOnAction(e -> saveData());
        btPop.setOnAction(e -> pop());
        btDel.setOnAction(e -> delData());
        btUpdate.setOnAction(e -> updateData());
        tableClick();
        searchData();
        tn.timeCurr(lblTime);
        listeners();
    }

    ///table columns
    private void tableCols() {

        TableColumn<product_getset, Integer> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        IdCol.setMaxWidth(200);

        TableColumn<product_getset, String> NameCol = new TableColumn<>("Name");
        NameCol.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        NameCol.setMaxWidth(200);

        TableColumn<product_getset, String> PriceCol = new TableColumn<>("Price");
        PriceCol.setCellValueFactory(new PropertyValueFactory<>("prodPrice"));
        PriceCol.setMaxWidth(60);

        TableColumn<product_getset, String> BCNoCol = new TableColumn<>("BCNo");
        BCNoCol.setCellValueFactory(new PropertyValueFactory<>("BCNo"));
        BCNoCol.setMaxWidth(200);

        TableColumn<product_getset, String> DateCol = new TableColumn<>("Date Of Reg");
        DateCol.setCellValueFactory(new PropertyValueFactory<>("DateOfReg"));
        DateCol.setMaxWidth(200);

        table.getColumns().addAll(IdCol, BCNoCol, NameCol, PriceCol, DateCol);

    }

    private void clear() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Clear All Fields?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setHeaderText("Are You Sure? This Will Clear All Fields");
        alert.showAndWait();
        alert.setResizable(false);
        if (alert.getResult() == ButtonType.YES) {
            clean();
            al.cleared();
        } else {
            al.clearNot();
        }
    }

    ////Clear Everything
    private void clean() {
        TextField[] tfClr = {txtBC, txtProdName, txtProdDesc, txtProdPrice, txtSearcher};
        for (TextField tfClrs : tfClr) {
            tfClrs.clear();
        }

        DatePicker[] dpClr = {dpStart, dpEnd};
        for (DatePicker dpClrs : dpClr) {
            dpClrs.getEditor().setText("");
        }

        Label[] lblClr = {lblProdPrice};
        for (int i = 0; i < lblClr.length; i++) {
            lblClr[i].setText("");
        }
        dpF.setValue(LocalDate.now());
        table.getColumns().clear();
        listView.getItems().clear();
        popLst();

    }

    ////Save Data Into DAtaBase
    private void saveData() {
        boolean status = txtProdName.getText().isEmpty() || txtProdPrice.getText().isEmpty();
        if (status) {
            al.txtNull();
        } else {

            String sql = "insert into products (BCNo, ProdName, ProdPrice,DateOfReg) values(?,?,?,?)";
            try {
                String bc = rn.ranNo();
                psmt = conn.prepareStatement(sql);

                psmt.setString(1, bc);
                psmt.setString(2, WordUtils.capitalizeFully(txtProdName.getText()));
                psmt.setString(3, WordUtils.capitalizeFully(txtProdPrice.getText()));
                psmt.setString(4, date);

                al.inserted();

                psmt.executeUpdate();

                clean();
                popLst();

            } catch (Exception e) {
            }
        }
    }

    ////Update Data
    private void updateData() {
        boolean status = txtProdName.getText().isEmpty() || txtProdPrice.getText().isEmpty();
        if (!status || !table.getItems().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Update : " + txtProdName.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setHeaderText("Are You Sure You Want To Update This Product ?");
            alert.showAndWait();

            alert.setResizable(false);

            if (alert.getResult() == ButtonType.YES) {
                try {
                    int index = 1;

                    String sql = "update products set BCNo=?, prodName=?,prodPrice=?, DateOfReg=?  where id=?";
                    psmt = conn.prepareStatement(sql);

                    psmt.setString(index++, txtBC.getText());
                    psmt.setString(index++, WordUtils.capitalizeFully(txtProdName.getText()));
                    psmt.setString(index++, WordUtils.capitalizeFully(txtProdPrice.getText()));
                    psmt.setString(index++, lblDate.getText());
                    psmt.setString(index++, lblId.getText());
                    psmt.executeUpdate();

                    clean();
                    pop();
                } catch (SQLException sQLException) {
                }
                al.update();
            } else {
                al.updateNot();
            }

        } else {
            al.txtNull();
        }
    }

    //Delete Tableview row
    private void delTable() {

        ObservableList<product_getset> productSelected, allProducts;
        allProducts = table.getItems();
        productSelected = table.getSelectionModel().getSelectedItems();

        productSelected.forEach(allProducts::remove);

    }

    /////Delete Data
    private void delData() {

        if (table.getItems().isEmpty()) {
        } else {
            try {
                String sql = "delete from products where BCNo = ?";
                psmt = conn.prepareStatement(sql);
                psmt.setString(1, txtBC.getText());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete : " + txtProdName.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.setHeaderText("Are You Sure You Want To Delete " + txtProdName.getText() + "?");
                alert.showAndWait();

                alert.setResizable(false);

                if (alert.getResult() == ButtonType.YES) {
                    psmt.executeUpdate();
                    al.deleted();
                } else {
                    al.deletedNot();
                }

            } catch (Exception e) {

            }
            delTable();
        }
        popLst();
    }

    ////Mouse Click And UpDown Presses
    private void clkAndPress() {

        product_getset row = table.getSelectionModel().getSelectedItem();

        lblId.setText(String.valueOf(row.getId()));
        txtBC.setText(row.getBCNo());
        txtProdName.setText(row.getProdName());
        txtProdPrice.setText(row.getProdPrice());
        lblProdPrice.setText("KSh : " + row.getProdPrice());
        dpF.getEditor().setText(row.getDateOfReg());
        lblDate.setText(row.getDateOfReg());

    }

    private void tableClick() {
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

    private String populator(String sql) {
        try {

            table.getColumns().clear();
            rs = conn.createStatement().executeQuery(sql);

            products = FXCollections.observableArrayList();
            products.removeAll(products);

            while (rs.next()) {

                product_getset pgs = new product_getset();

                pgs.setId(rs.getInt("id"));
                pgs.setBCNo(rs.getString("BCNo"));
                pgs.setProdName(rs.getString("prodName"));
                pgs.setProdPrice(rs.getString("prodPrice"));
                pgs.setDateOfReg(rs.getString("DateOfReg"));

                products.add(pgs);

            }
            tableCols();
            table.setItems(products);
            popLst();
        } catch (Exception e) {
        }
        return sql;
    }

    private void pop() {
        String sql = "select * from products";
        populator(sql);
        lblTtItems.setText("Total Items : " + String.valueOf(table.getItems().size()));
    }

    private void popLst() {
        try {
            listView.getItems().clear();
            String sql = "select * from products";
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                listView.getItems().addAll(FXCollections.observableArrayList(rs.getString("prodName")));
            }
        } catch (Exception e) {
        }
    }

    private void listeners() {
        lblAc.textProperty().bind(txtProdName.textProperty());
        lblProdPrice.textProperty().addListener((ob, ov, nv) -> {
            if (!txtProdPrice.getText().isEmpty()) {
                Double totalEx = Double.parseDouble(txtProdPrice.getText());
                lblProdPrice.setText("KSh " + String.format("%,.2f", totalEx));
            } else {
                lblProdPrice.setText("KSh: 0");
            }
        });
        txtProdPrice.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent) -> {
            if (!txtProdPrice.getText().isEmpty()) {
                Double totalEx = Double.parseDouble(txtProdPrice.getText());
                lblProdPrice.setText("KSh " + String.format("%,.2f", totalEx));
            } else {
                lblProdPrice.setText("KSh: 0");
            }
        });
        if (table.getItems().isEmpty()) {
        } else {
            int rowNo = table.getItems().size();
            lblTtItems.setText("Total Items : " + String.valueOf(rowNo));
        }
    }

    ///Search via Date and Text
    private void searchData() {
        txtSearcher.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            String searchInd = txtSearcher.getText();
            String sql = "select * from products where BCNo like   '%" + searchInd + "%'  "
                    + "OR prodName like   '%" + searchInd + "%' "
                    + "OR prodPrice like   '%" + searchInd + "%'   OR DateOfReg like   '%" + searchInd + "%'  ";
            populator(sql);
            lblTtItems.setText("Items = " + String.valueOf(table.getItems().size()));
        });
        txtSearcher.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!dpEnd.getEditor().getText().isEmpty() || !dpStart.getEditor().getText().isEmpty()) {
                String sql = "select * from products where DateOfReg between  '" + dpStart.getValue().toString() + "' and '" + dpEnd.getValue().toString() + "' ";
                populator(sql);
                lblTtItems.setText("Total Items : " + String.valueOf(table.getItems().size()));
            } else {
            }
        });
    }

    public void setdBoardName(String FullNamesString) {
        this.name = FullNamesString;
    }

}
