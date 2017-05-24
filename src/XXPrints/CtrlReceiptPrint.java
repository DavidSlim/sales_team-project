package XXPrints;

import java.net.URL;
import java.util.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import timeNow.timeNow;

public class CtrlReceiptPrint implements Initializable {

    timeNow tn = new timeNow();

    private List<String> nameList;
    private List<Double> priceList, qtyList, subTotalList;

    @FXML
    Label lblCashier, lblCash, lblTotal, lblS_no, lblBalance, lblTime, lblDate;
    @FXML
    TableView<getset_receipt> table;
    @FXML
    AnchorPane apMain;
    private final ObservableList<getset_receipt> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void tableCols() {
        TableColumn<getset_receipt, String> BCNameCol = new TableColumn<>("Prod Name");
        BCNameCol.setCellValueFactory(new PropertyValueFactory<>("bc_name"));
        BCNameCol.setMinWidth(220);
        BCNameCol.setMaxWidth(220);
        TableColumn<getset_receipt, String> QuantityCol = new TableColumn<>("Quantity");
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("pro_quantity"));
        QuantityCol.setMinWidth(70);
        QuantityCol.setMaxWidth(70);
        TableColumn<getset_receipt, String> PriceCol = new TableColumn<>("Price");
        PriceCol.setCellValueFactory(new PropertyValueFactory<>("pro_price"));
        PriceCol.setMinWidth(70);
        PriceCol.setMaxWidth(70);
        TableColumn<getset_receipt, String> SubTtCol = new TableColumn<>("SubTotal");
        SubTtCol.setCellValueFactory(new PropertyValueFactory<>("pro_subtotal"));
        SubTtCol.setMinWidth(100);
        SubTtCol.setMaxWidth(100);
        table.getColumns().addAll(BCNameCol, QuantityCol, PriceCol, SubTtCol);
    }

    public void setProd_nameList(List<String> receipt) {
        this.nameList = receipt;
    }

    public void setProd_priceList(List<Double> receipt) {
        this.priceList = receipt;
    }

    public void setProd_qtyList(List<Double> receipt) {
        this.qtyList = receipt;
    }

    public void setProd_subtotalList(List<Double> receipt) {
        this.subTotalList = receipt;
    }

    public void setCash(String value) {
        lblCash.setText(value);
    }

    public void setBalance(String value) {
        lblBalance.setText(value);
    }

    public void setTotal(String value) {
        lblTotal.setText(value);
    }

    public void setCashier(String value) {
        lblCashier.setText(value);
    }

    public void setS_no(int value) {
        lblS_no.setText("S/N" + String.format("%08d", value));
    }

    public void printRcpt() {
        lblTime.setText(tn.time() + " HRS");
        lblDate.setText(tn.date());
        data.removeAll(data);
        for (int i = 0; i < nameList.size(); i++) {
            getset_receipt gsr = new getset_receipt();
            gsr.setBc_name(nameList.get(i));
            gsr.setPro_price(priceList.get(i));
            gsr.setPro_quantity(qtyList.get(i));
            gsr.setPro_subtotal(subTotalList.get(i));
            data.add(gsr);
        }
        tableCols();
        table.setItems(data);
    }

    public void printJ() {
        PrintER pr = new PrintER();
        pr.printJob(apMain);
    }

}
