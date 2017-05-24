package salesReport;

import DbConn.DbConn;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.transform.*;
import timeNow.timeNow;

public class SalesCtrlr implements Initializable {

    private String name = null;

    @FXML
    private TableView<getset> table;
    @FXML
    private TextField tfSearch;
    @FXML
    private Label lblDate, lblTime;
    @FXML
    private DatePicker dpStart, dpEnd;

    @FXML
    private Button btPrint;
    @FXML
    private VBox vbMain;
    private List<getset> data = new ArrayList<>();

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;
    timeNow tn = new timeNow();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableCols();
        //searchData();
        filterby();
        //btPrint.setOnAction(e -> printJob());

    }

    ////Populator To Fetch Database data
    private String populator(String sql) {
        try {
            data = new ArrayList();
            data.removeAll(data);
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                getset pgs = new getset();

                pgs.setId(Integer.parseInt(rs.getString("id")));
                pgs.setBCNO(rs.getString("BCNo"));
                pgs.setProdName(rs.getString("prodname"));
                pgs.setQuantity(Double.parseDouble(rs.getString("quantity")));
                pgs.setPrice(Double.parseDouble(rs.getString("price")));
                pgs.setSubTt(Double.parseDouble(rs.getString("SubTt")));
                pgs.setSno(rs.getString("sno"));
                pgs.setDate(rs.getString("date"));
                pgs.setTime(rs.getString("time"));
                pgs.setPostedBy(rs.getString("postedBy"));

                data.add(pgs);
            }
            table.setItems(null);
            table.setItems(FXCollections.observableArrayList(data));
        } catch (SQLException | NumberFormatException ex) {

        }
        return sql;
    }

    Predicate<getset> gname = (getset t) -> t.getPostedBy().equals("David Slim");

    public void filterby() {
        String sql = "select * from sales";

        try {
            data = new ArrayList();
            data.removeAll(data);
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                getset pgs = new getset();

                pgs.setId(Integer.parseInt(rs.getString("id")));
                pgs.setBCNO(rs.getString("BCNo"));
                pgs.setProdName(rs.getString("prodname"));
                pgs.setQuantity(Double.parseDouble(rs.getString("quantity")));
                pgs.setPrice(Double.parseDouble(rs.getString("price")));
                pgs.setSubTt(Double.parseDouble(rs.getString("SubTt")));
                pgs.setSno(rs.getString("sno"));
                pgs.setDate(rs.getString("date"));
                pgs.setTime(rs.getString("time"));
                pgs.setPostedBy(rs.getString("postedBy"));

                data.add(pgs);
            }
            data.stream().filter(gname).forEach(p -> {
                System.out.println(p);
            });
        } catch (SQLException | NumberFormatException ex) {

        }
    }

    public static List<getset> findSalesPredicate(List<getset> data, Predicate<getset> predicate) {

        List<getset> result = new ArrayList<>();
        data.stream().filter((sa) -> (predicate.test(sa))).forEach((sa) -> {
            result.add(sa);
        });
        return result;
    }

    private void pop() {
        String sql = "select * from sales";
        populator(sql);
        //System.out.println(data);
        Predicate<getset> firstN = p -> p.getPostedBy().equals("David Slim");
        System.out.println(findSalesPredicate(data, firstN));
    }

    private void tableCols() {

        TableColumn<getset, Integer> IdNoCol = new TableColumn<>("ID");
        IdNoCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        IdNoCol.setMinWidth(40);
        IdNoCol.setMaxWidth(40);

        TableColumn<getset, String> BCNoCol = new TableColumn<>("BCNO");
        BCNoCol.setCellValueFactory(new PropertyValueFactory<>("BCNO"));
        BCNoCol.setMinWidth(60);
        BCNoCol.setMaxWidth(60);

        TableColumn<getset, String> NameCol = new TableColumn<>("Prod Name");
        NameCol.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        NameCol.setMinWidth(280);
        NameCol.setMaxWidth(280);

        TableColumn<getset, Integer> QuantityCol = new TableColumn<>("Quantity");
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        QuantityCol.setMaxWidth(70);
        QuantityCol.setMinWidth(70);

        TableColumn<getset, Integer> PriceCol = new TableColumn<>("Price");
        PriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        PriceCol.setMaxWidth(70);
        PriceCol.setMinWidth(70);

        TableColumn<getset, Integer> SubTCol = new TableColumn<>("SubTOtal");
        SubTCol.setCellValueFactory(new PropertyValueFactory<>("subTt"));
        SubTCol.setMaxWidth(100);
        SubTCol.setMinWidth(100);

        TableColumn<getset, String> SnoCol = new TableColumn<>("S/No");
        SnoCol.setCellValueFactory(new PropertyValueFactory<>("sno"));
        SnoCol.setMinWidth(60);
        SnoCol.setMaxWidth(60);

        TableColumn<getset, String> DateCol = new TableColumn<>("Date");
        DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        DateCol.setMinWidth(70);
        DateCol.setMaxWidth(70);

        TableColumn<getset, String> TimeCol = new TableColumn<>("Time");
        TimeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TimeCol.setMinWidth(60);
        TimeCol.setMaxWidth(60);

        TableColumn<getset, String> PostCol = new TableColumn<>("Posted By");
        PostCol.setCellValueFactory(new PropertyValueFactory<>("postedBy"));
        PostCol.setMinWidth(150);
        PostCol.setMaxWidth(150);

        table.getColumns().addAll(IdNoCol, BCNoCol, NameCol, PriceCol, QuantityCol, SubTCol, SnoCol, DateCol, TimeCol, PostCol);

    }

    private void searchData() {
        tfSearch.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            String searchInd = tfSearch.getText();
            if (!tfSearch.getText().isEmpty()) {
                String sql = "select * from sales where id like   '%" + searchInd + "%'  "
                        + "OR bcno like   '%" + searchInd + "%'   OR prodname like   '%" + searchInd + "%'   "
                        + "OR quantity like   '%" + searchInd + "%'   OR  price like   '%" + searchInd + "%'   OR subtt like   '%" + searchInd + "%' "
                        + "OR postedby like   '%" + searchInd + "%'   OR date like   '%" + searchInd + "%' "
                        + "OR time like   '%" + searchInd + "%'   OR sno  like   '%" + searchInd + "%' ";
                populator(sql);
            } else {
            }

        });

        tfSearch.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!dpEnd.getEditor().getText().isEmpty() || !dpStart.getEditor().getText().isEmpty()) {
                String sql = "select * from sales where Date between  '" + dpStart.getValue().toString() + "' and '" + dpEnd.getValue().toString() + "' ";
                populator(sql);
            } else {
            }
        });

    }

    public void printJob() {

        Node node = vbMain;

        lblDate.setText(tn.date());
        lblTime.setText(tn.time());

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        PrinterJob job = PrinterJob.createPrinterJob();

        double pagePrintableWidth = pageLayout.getPrintableWidth();
        double pagePrintableHeight = pageLayout.getPrintableHeight();
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(25));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
        double scaleX = pagePrintableWidth / node.getBoundsInParent().getWidth();
        double scaleY = scaleX;
        double localScale = scaleX;
        double numberOfPages = Math.ceil((table.getPrefHeight() * localScale) / pagePrintableHeight);
        table.getTransforms().add(new Scale(scaleX, (scaleY)));
        table.getTransforms().add(new Translate(0, 0));
        Translate gridTransform = new Translate();
        table.getTransforms().add(gridTransform);

        if (job.showPrintDialog(node.getScene().getWindow())) {

            for (int i = 0; i < numberOfPages; i++) {
                gridTransform.setY(-i * (pagePrintableHeight / localScale));
                job.printPage(pageLayout, node);
            }
            job.endJob();
        }
        node.getScene().getWindow().hide();
    }

    public void setdBoardName(String FullNamesString) {
        this.name = FullNamesString;
    }
}
