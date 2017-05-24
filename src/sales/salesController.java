package sales;

import Combos.Combos;
import DbConn.DbConn;
import XXPrints.CtrlReceiptPrint;
import alerts.alerts;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import timeNow.timeNow;
import validators.validators;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sales.getset.*;
import static java.util.stream.Collectors.toMap;

public class salesController implements Initializable {

    validators val = new validators();
    Combos cbs = new Combos();
    timeNow tn = new timeNow();
    sno_n_sold2day sno = new sno_n_sold2day();

    private String post_by = null;
    private ObservableList<sales_getset> data = FXCollections.observableArrayList();
    private List<prod_getset> data_prods = new ArrayList<>();

    @FXML
    TextField txtBarCode, txtSearch, txtQuantity, txtBal, txtCash;
    @FXML
    Label lblCash, lblCashBal, lblTT, lblBal, lblPrice, lblProdName, lblTtProd, lblTtCost, lblCashier, lblDate;
    @FXML
    DatePicker dpStart, dpEnd;
    @FXML
    Button btCancel, btMap, btUpdate, btClear, btAdd, btDel, btSave;
    @FXML
    ComboBox<String> cbProds;

    @FXML
    TableView<sales_getset> table;
    @FXML
    TableView<prod_getset> table_prods;

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;
    alerts al = new alerts();

    ///Lists To Insert Into DataBase During Data Saving
    List<String> bcoders = new ArrayList<>();
    List<String> namersdb = new ArrayList<>();
    List<String> namers_rcpt = new ArrayList<>();
    List<Double> pricers = new ArrayList<>();
    List<Double> qtyers = new ArrayList<>();
    List<Double> subttlrs = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clean();
        tableCols();
        System.out.println("SOLD TODAY = " + sno.Sold2dayPop());
        txtQuantity.setOnKeyTyped(e -> val.numDot(e));
        cbProds.setItems(FXCollections.observableArrayList(cbs.products()));
        pop();
        listeners();
        tableClick();
        tableProCols();

        btAdd.setOnAction(e -> addItem());
        btDel.setOnAction(e -> delTable());
        btSave.setOnAction(e -> save2Db());
        btClear.setOnAction(e -> clear());
        btCancel.setOnAction(e -> clear());

        txtSearch.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            search();
        });
    }

    private void tableCols() {
        TableColumn<sales_getset, String> BCNoCol = new TableColumn<>("Bar Code");
        BCNoCol.setCellValueFactory(new PropertyValueFactory<>("BCNo"));
        BCNoCol.setMinWidth(100);
        BCNoCol.setMaxWidth(100);
        BCNoCol.getStyleClass().add("colstyle");

        TableColumn<sales_getset, String> NameCol = new TableColumn<>("Prod Name");
        NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        NameCol.setMinWidth(230);
        NameCol.setMaxWidth(230);
        NameCol.getStyleClass().add("colstyle");

        TableColumn<sales_getset, Double> QuantityCol = new TableColumn<>("Quantity");
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        QuantityCol.setMinWidth(70);
        QuantityCol.setMaxWidth(70);
        QuantityCol.getStyleClass().add("colstyle");

        TableColumn<sales_getset, Double> PriceCol = new TableColumn<>("Price");
        PriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        PriceCol.setMinWidth(100);
        PriceCol.setMaxWidth(100);
        PriceCol.getStyleClass().add("colstyle");

        TableColumn<sales_getset, Double> SubTtCol = new TableColumn<>("SubTotal");
        SubTtCol.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        SubTtCol.setMinWidth(100);
        SubTtCol.setMaxWidth(100);
        SubTtCol.getStyleClass().add("colstyle");

        TableColumn<sales_getset, String> PostByCol = new TableColumn<>("Posted By");
        PostByCol.setCellValueFactory(new PropertyValueFactory<>("postBy"));
        PostByCol.setMinWidth(200);
        PostByCol.setMaxWidth(200);
        PostByCol.getStyleClass().add("colstyle");

        TableColumn<sales_getset, String> SNoCol = new TableColumn<>("S/No");
        SNoCol.setCellValueFactory(new PropertyValueFactory<>("sno"));
        PostByCol.setMinWidth(200);
        PostByCol.setMaxWidth(200);
        PostByCol.getStyleClass().add("colstyle");

        table.getColumns().addAll(BCNoCol, NameCol, QuantityCol, PriceCol, SubTtCol, SNoCol);
    }

    private void tableProCols() {
        TableColumn<prod_getset, String> BCNoCol = new TableColumn<>("Bar Code");
        BCNoCol.setCellValueFactory(new PropertyValueFactory<>("bCNo"));
        BCNoCol.setMinWidth(120);
        BCNoCol.setMaxWidth(120);
        BCNoCol.getStyleClass().add("colstyle");

        TableColumn<prod_getset, String> NameCol = new TableColumn<>("Prod Name");
        NameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        NameCol.setMinWidth(320);
        NameCol.setMaxWidth(320);
        NameCol.getStyleClass().add("colstyle");

        TableColumn<prod_getset, Integer> PriceCol = new TableColumn<>("Price");
        PriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        PriceCol.setMinWidth(120);
        PriceCol.setMinWidth(120);
        PriceCol.getStyleClass().add("colstyle");

        table_prods.getColumns().addAll(BCNoCol, NameCol, PriceCol);
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

    private void clean() {
        List<sales_getset> nul = new ArrayList<>();
        data = FXCollections.observableArrayList();
        txtBarCode.clear();
        lblProdName.setText(null);
        data.removeAll(data);
        txtQuantity.clear();
        lblPrice.setText("0.00");
        txtCash.clear();
        lblBal.setText("Change : 0.00");
        lblTT.setText("0.00");
        lblTtCost.setText("Total : 0.00");
        lblCashBal.setText("0.00");
        table.setItems(FXCollections.observableArrayList(nul));
    }

    public void listeners() {

        ///Listener to display cash in a readable format with commas
        txtCash.textProperty().addListener((ob, ov, nv) -> {
            nv = txtCash.getText();
            if (!txtCash.getText().isEmpty()) {
                lblCash.setText("Cash : " + String.format("%,.2f", Double.parseDouble(nv)));
                lblCashBal.setText(String.valueOf(Double.parseDouble(nv)));
            } else {
                lblCash.setText("Cash : 0.00");
                lblCashBal.setText("0.00");
            }

        });
        txtCash.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, (javafx.scene.input.KeyEvent event) -> {
            if (!txtCash.getText().isEmpty()) {
                double bal = Double.parseDouble(txtCash.getText()) - Double.parseDouble(lblTT.getText());
                lblBal.setText("Change : " + String.format("%,.2f", bal));
            } else if (txtCash.getText().isEmpty()) {
                double bal = 0 - Double.parseDouble(lblTT.getText());
                lblBal.setText("Change : " + String.format("%,.2f", bal));
            } else {
            }
        });
        lblTT.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!txtCash.getText().isEmpty()) {
                double bal = Double.parseDouble(txtCash.getText()) - Double.parseDouble(lblTT.getText());
                lblBal.setText("Change : " + String.format("%,.2f", bal));
            } else if (txtCash.getText().isEmpty()) {
                double bal = 0 - Double.parseDouble(lblTT.getText());
                lblBal.setText("Change : " + String.format("%,.2f", bal));
            } else {
            }
        });

    }

    ///set items being purchased on the tableview for computations
    private void addItem() {
        if (txtBarCode.getText().isEmpty() || txtQuantity.getText().isEmpty()) {
            String msg = "Provide Quantity Of " + lblProdName.getText() + " Being Purchased.";
            al.general(msg);
        } else {
            //data = FXCollections.observableArrayList();
            sales_getset sgs = new sales_getset();
            sgs.setBCNo(txtBarCode.getText());
            sgs.setName(lblProdName.getText());
            sgs.setQuantity(Double.parseDouble(txtQuantity.getText()));
            sgs.setPrice(Double.parseDouble(lblPrice.getText()));
            sgs.setSubtotal(sgs.getSubtotal());
            sgs.setPostBy(this.post_by);
            sgs.setDate(tn.date());
            sgs.setTime(tn.time());
            sgs.setSno(sno.s_no());

            data.add(sgs);
            table.getItems().addAll(sgs);

            double total = 0.00;
            total = data.stream().mapToDouble(sales_getset::getSubtotal).sum();
            lblTtCost.setText("Total : " + String.format("%,.2f", total));
            lblTT.setText(String.valueOf(total));

            txtBarCode.clear();
            txtQuantity.clear();
            lblProdName.setText(null);
            lblPrice.setText(null);
        }
    }

    private void delTable() {
        sales_getset sgs = new sales_getset();
        ObservableList<sales_getset> row, all_table;
        all_table = table.getItems();
        row = table.getSelectionModel().getSelectedItems();
        row.forEach(all_table::remove);

        data.removeAll(data);
        data.addAll(all_table);

        double total = 0.00;
        total = data.stream().mapToDouble(sales_getset::getSubtotal).sum();
        lblTtCost.setText("Total : " + String.format("%,.2f", total));
        lblTT.setText(String.valueOf(total));
    }

    private void search() {
        String searcher = txtSearch.getText();
        List<prod_getset> gg = data_prods.stream().filter((x) -> x.getBCNo().contains(searcher) && x.getName().contains(searcher) && String.valueOf(x.getPrice()).contains(searcher)).collect(Collectors.toList());
        gg.forEach((prod_getset x) -> {
            table_prods.setItems(FXCollections.observableArrayList(x));
            System.out.println("tyyuuiiooo = " + FXCollections.observableArrayList(x));
        });
    }

    ////Populator To Fetch Database data
    private String populator(String sql) {
        try {
            data_prods = FXCollections.observableArrayList();
            data_prods.removeAll(data_prods);
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                prod_getset pgs = new prod_getset();

                pgs.setBCNo(rs.getString("BCNo"));
                pgs.setName(rs.getString("prodname"));
                pgs.setPrice(Double.parseDouble(rs.getString("prodprice")));

                data_prods.add(pgs);
            }
            table_prods.setItems(null);
            table_prods.setItems(FXCollections.observableArrayList(data_prods));
        } catch (SQLException | NumberFormatException ex) {
            al.conn_err(ex.toString());
        }
        return sql;
    }

    ///populate populated dat onto the products table
    private void pop() {
        String sql = "select * from products";
        populator(sql);
    }

    //click & press
    private void clkAndPress() {
        try {
            prod_getset row = table_prods.getSelectionModel().getSelectedItem();
            txtBarCode.setText(row.getBCNo());
            lblProdName.setText(row.getName());
            lblPrice.setText(String.valueOf(row.getPrice()));
        } catch (Exception ex) {
            //al.conn_err(ex.toString());
        }
    }

    ////Table click and press listener
    public void tableClick() {
        try {
            table_prods.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                if (event.getClickCount() == 1 && !table.getColumns().isEmpty()) {
                    clkAndPress();
                } else {
                }
            });
        } catch (Exception ex) {
            //al.conn_err(ex.toString());
        }
    }

    private void receipt() {

        Map<String, String> barcode = data.stream().collect(toMap(sales_getset::getBCNo, sales_getset::getBCNo, (s, a) -> s + ", " + a));
        Map<String, Double> qty = data.stream().collect(Collectors.groupingBy(sales_getset::getBCNo, Collectors.reducing(0.0, sales_getset::getQuantity, Double::sum)));
        Map<String, Double> subtotal = data.stream().collect(Collectors.groupingBy(sales_getset::getBCNo, Collectors.summingDouble(sales_getset::getSubtotal)));

        Map<String, String> sorted_bcode = barcode.entrySet().stream().sorted(Entry.comparingByValue()).collect(toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Map<String, Double> sorted_qty = qty.entrySet().stream().sorted(Entry.comparingByKey()).collect(toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Map<String, Double> sorted_subtotal = subtotal.entrySet().stream().sorted(Entry.comparingByKey()).collect(toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        this.qtyers.addAll(sorted_qty.values());
        this.subttlrs.addAll(sorted_subtotal.values());

        sorted_bcode.keySet().forEach((tt) -> {
            String sql = "select * from products where bcno=?";
            try {
                psmt = conn.prepareStatement(sql);
                psmt.setString(1, tt);
                rs = psmt.executeQuery();
                if (rs.next()) {
                    bcprice_getset bgs = new bcprice_getset();
                    bgs.setPrice(Double.parseDouble(rs.getString("prodprice")));
                    bgs.setProdname(rs.getString("prodname"));
                    bgs.setBcno(rs.getString("bcno"));
                    this.bcoders.add(bgs.getBcno());
                    this.namers_rcpt.add(bgs.getBcno() + "\n" + bgs.getProdname());
                    this.namersdb.add(bgs.getProdname());
                    this.pricers.add(bgs.getPrice());
                }
            } catch (SQLException ex) {
            }
        });
    }

    ///save data
    private void save2Db() {
        double bal = Double.parseDouble(lblCashBal.getText()) - Double.parseDouble(lblTT.getText());
        boolean status = !table.getItems().isEmpty() && !txtCash.getText().isEmpty() && bal >= 0.00;
        try {
            if (status) {
                receipt();
                ReceiptMake();
                String sql = "insert into sales (BCNo, ProdName, Quantity, Price,SubTt, PostedBy, Date, Time ,SNo) values(?,?,?,?,?,?,?,?,?)";
                psmt = conn.prepareStatement(sql);
                for (int i = 0; i < bcoders.size(); i++) {
                    int index = 1;
                    psmt.setString(index++, bcoders.get(i));
                    psmt.setString(index++, namersdb.get(i));
                    psmt.setString(index++, String.valueOf(qtyers.get(i)));
                    psmt.setString(index++, String.valueOf(pricers.get(i)));
                    psmt.setString(index++, String.valueOf(subttlrs.get(i)));
                    psmt.setString(index++, new ArrayList<>(Collections.nCopies(bcoders.size(), post_by)).get(i));
                    psmt.setString(index++, new ArrayList<>(Collections.nCopies(bcoders.size(), tn.date())).get(i));
                    psmt.setString(index++, new ArrayList<>(Collections.nCopies(bcoders.size(), tn.time())).get(i));
                    psmt.setString(index++, String.format("%08d", new ArrayList<>(Collections.nCopies(bcoders.size(), sno.s_no())).get(i)));
                    psmt.addBatch();
                }
                psmt.executeBatch();
                System.out.println("SOLD TODAY = " + sno.Sold2dayPop());
                clean();
            } else {
                al.txtNull();
            }
        } catch (SQLException ex) {
            al.conn_err(ex.toString());
        }
    }

    public void setdBoardName(String FullNamesString) {
        this.post_by = FullNamesString;
    }

    private void ReceiptMake() {

        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/XXPrints/FXMLReceiptPrint.fxml"));

            Parent root = (Parent) fxmlLoader.load();

            ///Pass Login Values To OtherCtrl
            CtrlReceiptPrint crp = fxmlLoader.getController();
            crp.setProd_nameList(new ArrayList(namers_rcpt));
            crp.setProd_priceList(new ArrayList(pricers));
            crp.setProd_qtyList(new ArrayList(qtyers));
            crp.setProd_subtotalList(new ArrayList(subttlrs));
            crp.setCashier(post_by);
            crp.setCash(String.valueOf(lblCash.getText()));
            crp.setBalance(String.valueOf(lblBal.getText()));
            crp.setTotal(String.valueOf(lblTtCost.getText()));
            crp.setS_no(sno.s_no());

            //end of passing login values
            Scene scene = new Scene(root);
            stage.setTitle(post_by + " : Currently Running The System");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            crp.printRcpt();
        } catch (IOException ex) {
            al.general(ex.toString());
        }

    }

}
