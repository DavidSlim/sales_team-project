package RoutePlans;

import Combos.Combos;
import DbConn.DbConn;
import alerts.alerts;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.text.WordUtils;
import timeNow.timeNow;

public class RoutePlansController implements Initializable {

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;

    Combos cb = new Combos();
    alerts al = new alerts();
    timeNow tn = new timeNow();

    @FXML
    TextField txtSearcher, txtLocation, txtCounty, txtRegion, txtCountry;
    @FXML
    Label lblTime, lblDate, lblLocation, lblId;
    @FXML
    ComboBox cbDashBoard;
    @FXML
    DatePicker dpEnd, dpStart;
    @FXML
    Button btAdd, btPop, btUpdate, btDelete, btClear, btSave;
    @FXML
    TableView<route_getset> table;
    @FXML
    ListView listView;

    private String name = null;
    private ObservableList<route_getset> data;

    String date = LocalDate.now().toString();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clean();
        tableClick();
        tn.timeCurr(lblTime);
        searchData();
        //searcher();
        //cbDashBoard.setItems(FXCollections.observableArrayList(cb.dashBoard()));
        btClear.setOnAction(e -> clear());
        btSave.setOnAction(e -> saveData());
        btPop.setOnAction(e -> pop());
        btUpdate.setOnAction(e -> updateData());
        btDelete.setOnAction(e -> delData());
    }

    private void tableCols() {
        TableColumn<route_getset, String> IdCol = new TableColumn<>("ID");
        IdCol.setCellValueFactory(cd -> cd.getValue().IdProperty());
        TableColumn<route_getset, String> CountryCol = new TableColumn<>("Country");
        CountryCol.setCellValueFactory(cd -> cd.getValue().CountryProperty());
        TableColumn<route_getset, String> RegionCol = new TableColumn<>("Region");
        RegionCol.setCellValueFactory(cd -> cd.getValue().RegionProperty());
        TableColumn<route_getset, String> CountyCol = new TableColumn<>("County");
        CountyCol.setCellValueFactory(cd -> cd.getValue().CountyProperty());
        TableColumn<route_getset, String> LocationCol = new TableColumn<>("Location");
        LocationCol.setCellValueFactory(cd -> cd.getValue().LocationProperty());
        TableColumn<route_getset, String> DateCol = new TableColumn<>("Date Of Reg");
        DateCol.setCellValueFactory(cd -> cd.getValue().DateOfRegProperty());
        table.getColumns().addAll(CountryCol, RegionCol, CountyCol, LocationCol, DateCol);
    }

    private void clean() {
        TextField[] tfClr = {txtCounty, txtLocation, txtRegion, /*txtSearcher*/};
        for (TextField tfClrs : tfClr) {
            tfClrs.clear();
        }
        txtCountry.setText("Kenya");

        dpEnd.getEditor().setText("");
        dpStart.getEditor().setText("");
        lblDate.setText("");
        lblLocation.setText(null);
        listView.getItems().clear();
        //table.getColumns().clear();
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

    ///Save Data To DAtabase
    private void saveData() {
        boolean status = txtCountry.getText().isEmpty() || txtCounty.getText().isEmpty()
                || txtLocation.getText().isEmpty() || txtRegion.getText().isEmpty();
        if (status) {
            al.txtNull();
        } else {

            String sql = "insert into routeplans (Country, Region, County, Location ,DateOfReg) values(?,?,?,?,?)";
            try {

                psmt = conn.prepareStatement(sql);

                psmt.setString(1, WordUtils.capitalizeFully(txtCountry.getText()));
                psmt.setString(2, WordUtils.capitalizeFully(txtRegion.getText()));
                psmt.setString(3, WordUtils.capitalizeFully(txtCounty.getText()));
                psmt.setString(4, WordUtils.capitalizeFully(txtLocation.getText()));
                psmt.setString(5, date);

                al.inserted();

                psmt.executeUpdate();

                clean();

            } catch (Exception e) {
            }
        }

    }

    private void popLst() {

        try {
            data = FXCollections.observableArrayList();
            data.removeAll(data);

            String sql = "select * from routeplans";
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {

                listView.getItems().addAll(FXCollections.observableArrayList(rs.getString("location")));

            }

        } catch (Exception e) {
        }

    }
    ////Populate TableView & ListView

    private void pop() {

        try {

            data = FXCollections.observableArrayList();
            data.removeAll(data);
            clean();
            String sql = "select * from routeplans";
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                route_getset rgs = new route_getset();

                rgs.id.set(rs.getString("id"));
                rgs.country.set(rs.getString("country"));
                rgs.region.set(rs.getString("region"));
                rgs.county.set(rs.getString("county"));
                rgs.location.set(rs.getString("location"));
                rgs.dateofreg.set(rs.getString("dateofreg"));

                data.add(rgs);
            }
            tableCols();
            table.setItems(null);
            table.setItems(data);

        } catch (Exception e) {
        }
        popLst();
    }

    //Tableclick Method
    private void tableClick() {

        table.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if ((event.getCode().equals(KeyCode.DOWN)) || (event.getCode().equals(KeyCode.UP))) {
                clkAndPress();
            } else {
            }
        });
        table.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if ((event.getClickCount() == 1) && !table.getColumns().isEmpty()) {
                clkAndPress();
            } else {
            }
        });

    }

    ////TableClick
    private void clkAndPress() {

        route_getset row = table.getSelectionModel().getSelectedItem();

        lblId.setText(row.getId());
        txtCountry.setText(row.getCountry());
        txtCounty.setText(row.getCounty());
        txtRegion.setText(row.getRegion());
        txtLocation.setText(row.getLocation());
        lblLocation.setText(row.getLocation());
        lblDate.setText(row.getDateofreg());

    }

    private void updateData() {

        if (table.getItems().isEmpty() || table.getSelectionModel().getSelectedItems().isEmpty()) {
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Update : " + lblLocation.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setHeaderText("Are You Sure You Want To Update This route ?");
            alert.showAndWait();

            alert.setResizable(false);

            if (alert.getResult() == ButtonType.YES) {
                try {
                    data = FXCollections.observableArrayList();
                    data.removeAll(data);

                    String sql = "update routeplans set "
                            + "Country ='" + WordUtils.capitalizeFully(txtCountry.getText()) + "', "
                            + "region='" + WordUtils.capitalizeFully(txtRegion.getText()) + "', "
                            + "county='" + WordUtils.capitalizeFully(txtCounty.getText()) + "',"
                            + "location='" + WordUtils.capitalizeFully(txtLocation.getText()) + "', "
                            + "DateOfReg='" + lblDate.getText() + "' where id = " + lblId.getText();
                    psmt = conn.prepareStatement(sql);
                    psmt.executeUpdate();
                } catch (SQLException sQLException) {
                }

                al.update();
            } else {
                al.updateNot();
            }
        }
    }

    //Delete Tableview row
    private void delTable() {

        ObservableList<route_getset> productSelected, allProducts;
        allProducts = table.getItems();
        productSelected = table.getSelectionModel().getSelectedItems();

        productSelected.forEach(allProducts::remove);

    }

    /////Delete Data
    private void delData() {

        if (table.getItems().isEmpty() || table.getSelectionModel().getSelectedItems().isEmpty()) {
        } else {
            try {
                String sql = "delete from routeplans where location = ?";
                psmt = conn.prepareStatement(sql);
                psmt.setString(1, txtLocation.getText());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete : " + txtLocation.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.setHeaderText("Are You Sure You Want To Delete This Record ?");
                alert.showAndWait();

                alert.setResizable(false);

                if (alert.getResult() == ButtonType.YES) {

                    psmt.executeUpdate();
                    al.deleted();
                    //listView.getItems().remove(delRow);

                } else {
                    al.deletedNot();
                };
            } catch (Exception e) {

            }
            delTable();
        }

    }

    private void searcher() {
        data = FXCollections.observableArrayList();
        FilteredList<route_getset> filteredData = new FilteredList<>(this.data, p -> true);
        txtSearcher.textProperty().addListener((ob, ov, nv) -> {
            filteredData.setPredicate(routeplan -> {
                if (nv == null || nv.isEmpty()) {
                    return true;
                }
                String lcFilter = nv.toLowerCase();
                if (routeplan.getCountry().toLowerCase().contains(lcFilter)) {
                    return true;
                } else if (routeplan.getRegion().toLowerCase().contains(lcFilter)) {
                    return true;
                } else if (routeplan.getCounty().toLowerCase().contains(lcFilter)) {
                    return true;
                } else if (routeplan.getLocation().toLowerCase().contains(lcFilter)) {
                    return true;
                } else if (routeplan.getDateofreg().toLowerCase().contains(lcFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<route_getset> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        //tableCols();
        table.setItems(sortedData);
    }

    ///Search via Date and Text
    private void searchData() {

        txtSearcher.addEventFilter(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            table.getColumns().clear();
            tableCols();

            data = FXCollections.observableArrayList();
            String searchInd = txtSearcher.getText();

            try {
                String sql = "select * from routeplans where country like   '%" + searchInd + "%'  "
                        + "OR region like   '%" + searchInd + "%'   OR COUNTY like   '%" + searchInd + "%'   "
                        + "OR location like   '%" + searchInd + "%'   OR DateOfReg like   '%" + searchInd + "%'  ";
                psmt = conn.prepareStatement(sql);
                rs = psmt.executeQuery();
                while (rs.next()) {

                    route_getset rgs = new route_getset();

                    rgs.country.set(rs.getString("country"));
                    rgs.region.set(rs.getString("region"));
                    rgs.county.set(rs.getString("county"));
                    rgs.location.set(rs.getString("location"));
                    rgs.dateofreg.set(rs.getString("dateofreg"));

                    data.add(rgs);

                }
                table.setItems(data);

            } catch (SQLException ex) {
            }
        });

        txtSearcher.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            table.getColumns().clear();
            tableCols();
            data = FXCollections.observableArrayList();

            if (!dpEnd.getEditor().getText().isEmpty() || !dpStart.getEditor().getText().isEmpty()) {
                try {
                    String sql = "select * from routeplans where DateOfReg between  '" + dpStart.getValue().toString() + "' and '" + dpEnd.getValue().toString() + "' ";
                    psmt = conn.prepareStatement(sql);
                    rs = psmt.executeQuery();
                    data = FXCollections.observableArrayList();
                    while (rs.next()) {
                        route_getset rgs = new route_getset();

                        rgs.country.set(rs.getString("country"));
                        rgs.region.set(rs.getString("region"));
                        rgs.county.set(rs.getString("county"));
                        rgs.location.set(rs.getString("location"));
                        rgs.dateofreg.set(rs.getString("dateofreg"));

                        data.add(rgs);
                    }
                    table.setItems(data);

                } catch (SQLException ex) {
                }
            } else {
            }
        });
    }

    public void setdBoardName(String FullNamesString) {
        this.name = FullNamesString;
    }
}
