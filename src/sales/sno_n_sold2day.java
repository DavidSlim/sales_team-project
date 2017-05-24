package sales;

import DbConn.DbConn;
import java.sql.*;
import java.util.*;
import javafx.collections.*;
import sales.getset.*;
import timeNow.timeNow;

public class sno_n_sold2day {

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;

    private final ObservableList<sales_getset> data = FXCollections.observableArrayList();

    public int s_no() {
        List<Integer> sno_make = new ArrayList();
        try {
            rs = conn.createStatement().executeQuery("select * from sales");
            while (rs.next()) {
                getset.sales_getset sgs = new getset.sales_getset();
                sgs.setSno(rs.getInt("sno"));
                sno_make.add(sgs.getSno());
            }
            int idd = ((int) sno_make.stream().reduce((a, h) -> h).orElse(0)) + 1;
            return idd;
        } catch (Exception e) {
        }
        return 0;
    }

    public double Sold2dayPop() {
        timeNow tn = new timeNow();
        String date_search = tn.date();
        List<date_getset> sold_today = new ArrayList<>();
        String sql = "select * from sales where date between  '" + date_search + "' and '" + date_search + "' ";
        try {
            sold_today.removeAll(sold_today);
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                date_getset dgs = new date_getset();
                dgs.setQuantity(Double.parseDouble(rs.getString("quantity")));
                sold_today.add(dgs);
            }
            double today;
            return today = sold_today.stream().mapToDouble(date_getset::getQuantity).sum();
        } catch (SQLException | NumberFormatException e) {
        }
        return 0.00;
    }
}
